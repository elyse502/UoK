<?php
/**
 * Functions which enhance the theme by hooking into WordPress
 *
 * @package Spexo
 */

if ( ! defined( 'ABSPATH' ) ) {
    exit; // Exit if accessed directly.
}

/**
 * Adds custom classes to the array of body classes.
 *
 * @param array $classes Classes for the body element.
 * @return array
 */
function tmpcoder_body_classes( $classes ) {
	// Adds a class of hfeed to non-singular pages.
	if ( ! is_singular() ) {
		$classes[] = 'hfeed';
	}

	// Adds a class of no-sidebar when there is no sidebar present.
	if ( ! is_active_sidebar( 'sidebar-1' ) ) {
		$classes[] = 'no-sidebar';
	}

	return $classes;
}
add_filter( 'body_class', 'tmpcoder_body_classes' );

/**
 * Add a pingback url auto-discovery header for single posts, pages, or attachments.
 */
function tmpcoder_pingback_header() {
	
	if ( is_singular() && pings_open() ) {
		printf( '<link rel="pingback" href="%s">', esc_url( get_bloginfo( 'pingback_url' ) ) );
	}
}
add_action( 'wp_head', 'tmpcoder_pingback_header' );

/* Add a custom site icon URL */

if (!function_exists('tmpcoder_add_custom_site_icon')) {
	
	function tmpcoder_add_custom_site_icon($url){

		if ( Tmpcoder_Site_Settings::tmpcoder_has('tmpcoder_fav_site_icon') ) {
            $tmpcoder_fav_site_icon = Tmpcoder_Site_Settings::tmpcoder_get('tmpcoder_fav_site_icon');
			if (isset($tmpcoder_fav_site_icon['url']) && $tmpcoder_fav_site_icon['url'] != "") {
				return $tmpcoder_fav_site_icon['url'];
			}
		} elseif ( get_option( 'site_icon' ) ) {
			$site_icon_id = get_option( 'site_icon' );
			if ( isset( $site_icon_id ) && ! empty( $site_icon_id ) ) {
				$site_icon = wp_get_attachment_url( $site_icon_id );
				return $site_icon;
			}
		}
	} 
}

/* Hook the function to 'get_site_icon_url' filter */
add_filter('get_site_icon_url', 'tmpcoder_add_custom_site_icon');

