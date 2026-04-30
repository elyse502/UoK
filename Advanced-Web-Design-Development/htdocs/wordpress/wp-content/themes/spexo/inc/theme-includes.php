<?php

if ( ! defined( 'ABSPATH' ) ) {
    exit; // Exit if accessed directly.
}

get_template_part('inc/theme-functions');
get_template_part('inc/theme-hooks');
get_template_part('inc/forms-hooks');

/* This file include in after_setup_theme hook for avoid load_theme_textdomain error */
add_action('after_setup_theme', 'tmpcoder_after_setup_theme_func');
function tmpcoder_after_setup_theme_func(){
    get_template_part('inc/admin/global-options/init');
} 

/**
 * Custom template tags for this theme.
 */
get_template_part('inc/template-tags');

/**
 * Functions which enhance the theme by hooking into WordPress.
*/
get_template_part('inc/template-functions');

/**
 * Install Required Plugins.
*/
get_template_part('inc/admin/class-tgm-plugin-activation');

/**
 * Load Wizard file.
 */
get_template_part('inc/wizard/index');

/**
 * Compatibility
*/
get_template_part('inc/compatibility/class-spexo-starter-content');

/**
 * Block Patterns
 */
get_template_part('inc/compatibility/tmpcoder-patterns');

/**
 * Load all Customizer options
 */
if( ! class_exists('Redux_Framework_Plugin', false) ) { 
    get_template_part('inc/admin/customizer/tmpcoder-customizer');
}

add_filter( 'wpcf7_autop_or_not', '__return_false' );

function tmpcoder_get_theme_version_in_db(){
    return get_option('tmpcoder_theme_core_version', '');
}

function tmpcoder_set_theme_version_in_db($version){
    return update_option('tmpcoder_theme_core_version', $version);
}

add_action('init', 'tmpcoder_activation_after_switch_theme');
function tmpcoder_activation_after_switch_theme(){
    $current_ver_db = tmpcoder_get_theme_version_in_db();
    if ( $current_ver_db != '' ){
        if ( version_compare( $current_ver_db, TMPCODER_THEME_CORE_VERSION, '<' ) ){
            // upgrade action
            tmpcoder_set_theme_version_in_db(TMPCODER_THEME_CORE_VERSION);
            tmpcoder_cache_clear_after_update();
        }
    }else{
        // initial action
        tmpcoder_set_theme_version_in_db(TMPCODER_THEME_CORE_VERSION);
        tmpcoder_cache_clear_after_update();

    }
    // Default version add in migration version
    $_theme_migration = get_option('tmpcoder_theme_migration', '');
    if ( $_theme_migration == "" ){
        update_option('tmpcoder_theme_migration', TMPCODER_THEME_CORE_VERSION);
    }
}

function tmpcoder_cache_clear_after_update(){
    // Elementor options cache clear
    if ( did_action( 'elementor/loaded' ) ) {
        // Automatically purge and regenerate the Elementor CSS cache
        \Elementor\Plugin::instance()->files_manager->clear_cache();
    }

    if ( function_exists('tmpcoder_redux_options_update_theme_variable') ){
        $tmpcoder_theme_options = Tmpcoder_Site_Settings::tmpcoder_get_all_data();
        tmpcoder_redux_options_update_theme_variable($tmpcoder_theme_options, '', '');
    }

    delete_transient( 'templatescore_premium_plugin_info' );
    delete_transient( 'templatescore_premium_plugins_info' );
    delete_transient( 'templatescore_premium_theme_info' );
}

/*
** Notices
*/

get_template_part( 'inc/activation/class-welcome-notice' );

add_action( 'after_switch_theme', 'tmpcoder_activation_time');
add_action('after_setup_theme', 'tmpcoder_activation_time');
    
function tmpcoder_activation_time() {
    if ( false === get_option( 'tmpcoder_activation_time' ) ) {
        add_option( 'tmpcoder_activation_time', strtotime('now') );
    }
}

function tmpcoder_activation_hook() {
    if ( is_admin() ) {
        $wizard_run = get_option(TMPCODER_THEME_SLUG.'_wizard_page' , 0);
        $wizard_run2 = defined('TMPCODER_PRO_PLUGIN_KEY') ? get_option(TMPCODER_PRO_PLUGIN_KEY.'_wizard_page' , 0) : 0;
        if ( $wizard_run == 0 && $wizard_run2 != 1) {
            update_option(TMPCODER_THEME_SLUG.'_wizard_page', 1);
            wp_redirect( admin_url('admin.php?page=tmpcoder-theme-wizard') );
            exit();
        }
    }
}
add_action( 'after_switch_theme', 'tmpcoder_activation_hook' );

if (!function_exists('tmpcoder_avoid_elementor_redirection')) {
    
    function tmpcoder_avoid_elementor_redirection($plugin) {
        if ($plugin == 'elementor/elementor.php') {
            delete_transient( 'elementor_activation_redirect' );
        }
        if ($plugin == 'sastra-essential-addons-for-elementor/sastra-essential-addons-for-elementor.php') {
            delete_transient('tmpcoder_plugin_do_activation_redirect');
        }
    }
    add_action('activated_plugin', 'tmpcoder_avoid_elementor_redirection');
}

function tmpcoder_remove_auto_sizes_css_fix($add_auto_sizes){
    return false;
}
add_filter('wp_img_tag_add_auto_sizes', 'tmpcoder_remove_auto_sizes_css_fix');