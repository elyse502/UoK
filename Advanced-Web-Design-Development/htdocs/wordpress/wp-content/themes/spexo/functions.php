<?php

/**
 * Spexo functions and definitions
 *
 * @link https://developer.wordpress.org/themes/basics/theme-functions/
 *
 * @package Spexo
 */

if ( ! defined( 'ABSPATH' ) ) {
    exit; // Exit if accessed directly.
}

$tmpcoder_theme = (is_object(wp_get_theme()->parent())) ? wp_get_theme()->parent() : wp_get_theme();

define('TMPCODER_THEME_NAME', $tmpcoder_theme->get( 'Name' ));

if ( ! defined( 'TMPCODER_THEME_SLUG' ) ) {
    define( 'TMPCODER_THEME_SLUG', $tmpcoder_theme->get( 'TextDomain' ) );
}

if ( ! defined( 'TMPCODER_THEME_CORE_VERSION' ) ) {
    define( 'TMPCODER_THEME_CORE_VERSION', trim( $tmpcoder_theme->get('Version')));
}

if ( !defined('TMPCODER_THEME_OPTION_NAME') ){
    define('TMPCODER_THEME_OPTION_NAME', 'tmpcoder_global_theme_options_'.TMPCODER_THEME_SLUG);
}

if ( !defined('TMPCODER_SPEXO_ADDONS_WIDGETS_URL') ){
    define('TMPCODER_SPEXO_ADDONS_WIDGETS_URL', 'https://spexoaddons.com/widgets');
}

if ( ! defined( 'TMPCODER_PURCHASE_PRO_URL' ) ) {
    define( 'TMPCODER_PURCHASE_PRO_URL', esc_url( 'https://spexoaddons.com/spexo-addons-pro/' ) );
}

if ( ! defined( 'TMPCODER_CUSTOMIZER_ASSETS' ) ) {
    define( 'TMPCODER_CUSTOMIZER_ASSETS', trailingslashit( get_template_directory_uri() ) . 'inc/admin/customizer/assets/' );
}

if ( ! function_exists('tmpcoder_display_php_version_notices') ){
    add_action( 'admin_notices', 'tmpcoder_display_php_version_notices' );
    function tmpcoder_display_php_version_notices(){

        $php_version = null;
        if ( defined( 'PHP_VERSION' ) ) {
            $php_version = PHP_VERSION;
        } elseif ( function_exists( 'phpversion' ) ) {
            $php_version = phpversion();
        }
        if ( null === $php_version ) {
            echo wp_kses( '<div class="notice notice-error">
                        <p>PHP Version could not be detected.</p>
                    </div>', array('div' => 'class', 'p' => array()) );
        } else {
            if ( version_compare( $php_version, '7.4' ) >= 0 ) {
                $message = '';
            } else {
                echo '<div class="notice notice-error"><p>';
                printf(
                    /* translators: %s is the PHP version */
                    esc_html__('Your site is running on an outdated version of PHP %s. The minimum recommended version of PHP is 7.4.', 'spexo'),
                    esc_html($php_version)
                );
                echo '<a href="'.esc_url(admin_url().'?page=spexo-welcome&tab=system-info').'">'. esc_html('See more details') .'</a>';
                echo '</p></div>';
            }
        }

        // Get the memory from PHP's configuration.
        $memory = ini_get( 'memory_limit' );
        // If we can't get it, fallback to WP_MEMORY_LIMIT.
        if ( ! $memory || -1 === $memory ) {
            $memory = wp_convert_hr_to_bytes( WP_MEMORY_LIMIT );
        }
        // Make sure the value is properly formatted in bytes.
        if ( ! is_numeric( $memory ) ) {
            $memory = wp_convert_hr_to_bytes( $memory );
        }
        if ( $memory < 128000000 ) : 
            echo '<div class="notice notice-error"><p>';
            printf(
                /* translators: 1$s is the memory size, 2$s is the URL to the documentation */
                esc_html__( '%1$s - We recommend setting memory to at least <strong>128MB</strong>. Please define memory limit in <strong>wp-config.php</strong> file. To learn how, see: <a href="%2$s" target="_blank" rel="noopener noreferrer">Increasing memory allocated to PHP.</a>', 'spexo' ),
                esc_html( size_format( $memory ) ),
                esc_url( 'http://codex.wordpress.org/Editing_wp-config.php#Increasing_memory_allocated_to_PHP' )
            );
            echo '</p></div>';
        endif;
    }
}

if (!function_exists('tmpcoder_min_suffix')) {

    function tmpcoder_min_suffix(){

        return defined( 'SCRIPT_DEBUG' ) && SCRIPT_DEBUG ? '' : '.min';
    }
}

add_action( 'after_switch_theme', 'tmpcoder_theme_upgrade_action');
function tmpcoder_theme_upgrade_action(){
    $tmpcoder_theme_setting = get_option(TMPCODER_THEME_OPTION_NAME);
    if( empty($tmpcoder_theme_setting) ) {
        $tmpcoder_theme_setting = get_option('tmpcoder_global_theme_options_sastrawp');
        if ( !empty($tmpcoder_theme_setting) ){
            update_option(TMPCODER_THEME_OPTION_NAME, $tmpcoder_theme_setting);
        }
    }
}

/*
 * Include Function file    
*/
require get_template_directory() . '/inc/theme-includes.php';