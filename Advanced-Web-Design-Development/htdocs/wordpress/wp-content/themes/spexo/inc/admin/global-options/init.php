<?php
/**
 * @author  Spexo
 * @since   1.0.0
 * @version 1.0.0
 */

if ( ! class_exists( 'Redux' ) ) {
    get_template_part('inc/admin/global-options/variable-compiler');
    return;
}

$opt_name = TMPCODER_THEME_OPTION_NAME;
$theme = (is_object(wp_get_theme()->parent())) ? wp_get_theme()->parent() : wp_get_theme();

$args = array(
    'opt_name'             => $opt_name,
    // This is where your data is stored in the database and also becomes your global variable name.
    'disable_tracking'     => true,
    // Version that appears at the top of your panel
    'menu_type'            => 'submenu', // menu, submenu, hidden
    'page_parent' => 'spexo-welcome',
    //Specify if the admin menu should appear or not. Options: menu or submenu (Under appearance only)
    'allow_sub_menu'       => true,
    // Show the sections below the admin menu item or not
    'menu_title'           => esc_html__( 'Global Options', 'spexo' ),
    'page_title'           => esc_html(sprintf(/* translators: %s is the theme name */esc_html__('%s Settings', 'spexo'),TMPCODER_THEME_NAME)),
    // Set it you want google fonts to update weekly. A google_api_key value is required.
    'google_update_weekly' => false,
    // Must be defined to add google fonts to the typography module
    'async_typography'     => true,
    // Disable this in case you want to create your own google fonts loader
    'admin_bar'            => true,
    // Show the panel pages on the admin bar
    'admin_bar_icon'       => 'dashicons-menu',
    // Choose an icon for the admin bar menu
    'admin_bar_priority'   => 50,
    // Choose an priority for the admin bar menu
    'global_variable'      => '',
    // Set a different name for your global variable other than the opt_name
    'dev_mode'             => false,
    'forced_dev_mode_off'  => false,
    // Show the time the page took to load, etc
    'update_notice'        => false,
    // If dev_mode is enabled, will notify developer of updated versions available in the GitHub Repo
    'customizer'           => true,
    // OPTIONAL -> Give you extra features
    'page_priority'        => '2',
    // For a full list of options, visit: http://codex.wordpress.org/Function_Reference/add_submenu_page#Parameters
    'page_permissions'     => 'manage_options',
    // Permissions needed to access the options panel.
    'menu_icon'            => '',
    // Specify a custom URL to an icon
    'last_tab'             => '',
    // Force your panel to always open to a specific tab (by id)
    'page_icon'            => 'icon-themes',
    // Icon displayed in the admin panel next to your menu_title
    'page_slug'            => 'spexo_addons_global_settings',
    // Page slug used to denote the panel, will be based off page title then menu title then opt_name if not provided
    'save_defaults'        => true,
    // On load save the defaults to DB before user clicks save or not
    'default_show'         => true,
    // If true, shows the default value next to each field that is not the default value.
    'default_mark'         => '',
    // What to print by the field's title if the value shown is default. Suggested: *
    'show_import_export'   => true,
    // Shows the Import/Export panel when not used as a field.
    // CAREFUL -> These options are for advanced use only
    'transient_time'       => 60 * MINUTE_IN_SECONDS,
    'output'               => true,
    // Global shut-off for dynamic CSS output by the framework. Will also disable google fonts output
    'output_tag'           => true,
    // Allows dynamic CSS to be generated for customizer and google fonts, but stops the dynamic CSS from going to the head
    'footer_credit'        => '&nbsp;',
    // FUTURE -> Not in use yet, but reserved or partially implemented. Use at your own risk.
    'database'             => '',
    // possible: options, theme_mods, theme_mods_expanded, transient. Not fully functional, warning!
    'use_cdn'              => true,
    // If you prefer not to use the CDN for Select2, Ace Editor, and others, you may download the Redux Vendor Support plugin yourself and run locally or embed it in your code.
);

if ( !defined( 'TMPCODER_PLUGIN_NAME' ) ) {
    unset($args['page_parent']);
}

Redux::setArgs( $opt_name, $args );

add_action('redux/'.TMPCODER_THEME_OPTION_NAME.'/panel/before','tmpcoder_heading_markup');   
function tmpcoder_heading_markup(){
    
    if ( function_exists( 'tmpcoder_render_admin_header' ) ) {

        if( defined('TMPCODER_PRO_ADDONS_ASSETS_URL') ) {
            $main_header_logo = TMPCODER_PRO_ADDONS_ASSETS_URL.'images/spexo-logo-web-pro.svg';
        } else if(defined('TMPCODER_ADDONS_ASSETS_URL')) {
            $main_header_logo = TMPCODER_ADDONS_ASSETS_URL.'images/spexo-logo-web.svg';
        } else {
            $main_header_logo = get_template_directory_uri().'/assets/images/logo.png';
        }

        tmpcoder_render_admin_header($main_header_logo, 'global-options');
    }
} 

get_template_part('inc/admin/global-options/theme-colors');
get_template_part('inc/admin/global-options/theme-button-style');
get_template_part('inc/admin/global-options/theme-typography');
get_template_part('inc/admin/global-options/general');
get_template_part('inc/admin/global-options/theme-custom-css');
get_template_part('inc/admin/global-options/variable-compiler');