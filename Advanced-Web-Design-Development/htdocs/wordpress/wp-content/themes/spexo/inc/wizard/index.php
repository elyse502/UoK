<?php

if ( ! defined( 'ABSPATH' ) ) { exit; } // Exit if accessed directly

get_template_part('inc/wizard/wizard-functions');
get_template_part('inc/wizard/wizard-ajax-api');

add_action('admin_enqueue_scripts', 'tmpcoder_theme_admin_enqueue_script_custom');
function tmpcoder_theme_admin_enqueue_script_custom(){
    $current_screen = get_current_screen();
    if ( isset($current_screen->base) && ($current_screen->base == 'admin_page_tmpcoder-theme-wizard' || $current_screen->base == 'appearance_page_tmpcoder-theme-wizard') ){
        wp_enqueue_style( 'tmpcoder-theme-wizard-admin-style', get_template_directory_uri() . '/inc/wizard/css/wizard-style' . Tmpcoder_Theme_Setup_Wizard::script_suffix() . '.css', false, '1.0.0' );
        wp_enqueue_script( 'tmpcoder-theme-wizard-admin-script', get_template_directory_uri() . '/inc/wizard/js/wizard-script' . Tmpcoder_Theme_Setup_Wizard::script_suffix() . '.js', false, '1.0.0' );

        $wizard_step = get_option(TMPCODER_THEME_SLUG.'_wizard_step');
        
        // Define an array of data to pass to JavaScript
        $my_data = array(
            // 'ajax_url' => admin_url( 'admin-ajax.php' ),
            // 'some_value' => 'This is a value from PHP',
            'tmpcoder_admin_url' => admin_url(),
            'wizard_step' => esc_js($wizard_step),
            'get_plugins_nonce'  => esc_js(wp_create_nonce( 'tmpcoder_get_plugins') ),
            'get_pro_addons_info_nonce'  => esc_js(wp_create_nonce( 'tmpcoder_get_pro_addons_info') ),
            'form_nonce' => esc_js( wp_nonce_field( 'tmpcoder_install_plugins','_wpnonce', true, false) ),
            'next_step_btn' => esc_html__("Next Step",'spexo'),
            'required_plugin_installing' =>  esc_html__("Installing Recommended Plugins",'spexo'),
            'getting_required_plugins' =>  esc_html__("Getting Recommended Plugins Info",'spexo'),
            'install_required_plugins' =>  esc_html__("Install Recommended Plugins",'spexo'),
            'install_required_plugins_text' =>  sprintf("Make sure %s is running the most recent version. %s is designed to work with the recommended plugins listed below.", esc_html(TMPCODER_THEME_NAME), esc_html(TMPCODER_THEME_NAME)),
            'install_and_activate' =>  esc_html__("Install & Activate",'spexo'),
            'installed_and_activate' =>  esc_html__("Installed & Activate",'spexo'),
            'installed_and_activated' =>  esc_html__("Activated",'spexo'),
            'loading_pro_plugin_info' =>  esc_html__("Pro Plugin Info Loading...",'spexo'),
            'network_error' => esc_html__("check network connection, try again.",'spexo'),
        );

        // Pass the data to the JavaScript file
        wp_localize_script( 'tmpcoder-theme-wizard-admin-script', 'tmpcoderMessages', $my_data );
    }
}


class Tmpcoder_Theme_Setup_Wizard {

    /**
     * @var Tmpcoder_Theme_Setup_Wizard
     */
    private static $_instance;

    /**
     * @return Tmpcoder_Theme_Setup_Wizard
     */
    public static function instance() {
        if ( is_null( self::$_instance ) ) {
            self::$_instance = new self();
        }

        return self::$_instance;
    }

    function __construct(){

        $wizard_run = get_option(TMPCODER_THEME_SLUG.'_wizard_done' , 0);
        if ( $wizard_run == 0 ) {
            add_action( 'admin_menu', array($this, 'register_newpage') );
        }

        add_action( 'admin_notices', array($this, 'wizard_admin_notice_success') );
        
    }

    public static function script_suffix() {
        // $dir = is_rtl() ? '-rtl' : '';
        return defined( 'SCRIPT_DEBUG' ) && SCRIPT_DEBUG ? '' : '.min';
    }

    function wizard_admin_notice_success() {
        if ( isset($_GET['saved']) && $_GET['saved'] == "wizard" ){ // phpcs:ignore WordPress.Security.NonceVerification.Recommended    
            delete_option(TMPCODER_THEME_SLUG.'_wizard_step');
            update_option(TMPCODER_THEME_SLUG.'_wizard_done', 1);
            ?>
            <div class="notice notice-success is-dismissible">
                <p><?php esc_html_e( 'Congrats, The Setup Wizard has successfully set up your website.', 'spexo' ); ?></p>
            </div>
            <?php
        }
    }

    function register_newpage(){
        add_theme_page(
            'Wizard',                      // Page title
            'Setup Wizard',                // Menu title
            'manage_options',              // Capability
            'tmpcoder-theme-wizard',       // Menu slug
            array($this, 'tmpcoder_theme_func') // Function to display the page
        );        
    }
    
    function tmpcoder_theme_func(){
        $current_user = wp_get_current_user();
        $display_name = $current_user->display_name;
        ?>
        <div class="wrap tmpcoder-container tmpcoder-theme-wizard">
            <hr class="wp-header-end">            
            

            <div class="theme-wizard-main">
                <ul class="nav-tab-wrapper theme-wizard-nav wp-clearfix">
                    <li class="nav-tab theme-welcome" data-tab="theme-welcome">
                        <span class="step-number">1</span><?php echo esc_html( sprintf(
                            /* translators: %s is Theme Name */
                          __( 'Welcome %s Theme', 'spexo' ) , ucfirst( TMPCODER_THEME_NAME )) ); ?>
                    </li>
                    <li class="nav-tab install-plugins disabled" data-tab="install-plugins">
                        <span class="step-number">2</span><?php esc_html_e('Install Recommended Plugins', 'spexo'); ?>
                    </li>
                    <li class="nav-tab get-pro-plugins disabled" data-tab="get-pro-plugins">
                        <span class="step-number">3</span><?php esc_html_e('Get a Spexo Addons Pro', 'spexo'); ?>
                    </li>                  
                </ul>
                <div id="theme-welcome" class="tab-content tab-content-theme-welcome active">
                    <div class="tmpcoder-message-box theme-install-part">
                        <div class="wizard-header-logo">
                            <img src="<?php echo esc_url(get_template_directory_uri() . '/assets/images/logo.png'); ?>" />
                        </div>
                        <h2 class="wizard-heading"><?php echo esc_html( sprintf(
                            /* translators: %1$s is Theme Name, %2$s is User Name. */
                          __( 'Welcome To %1$s Theme Wizard, %2$s!', 'spexo' ) , ucfirst( TMPCODER_THEME_NAME ), ucfirst( $display_name ) ) ); ?></h2>
                        <p class="wizard-title-text"><?php echo esc_html( sprintf( 
                            /* translators: %s is Theme Name. */
                            apply_filters( 'theme_admin_setup_welcome_text', __( 'We recommend making use of the %s Theme Wizard to create your website. the easiest way to get started.', 'spexo' ) ) , TMPCODER_THEME_NAME) ); ?>
                        </p>
                        <div class="next-step-action">
                            <button class="button button-primary next-step-btn"><?php esc_html_e('Next','spexo'); ?></button>
                            <a class="skip-theme-wizard"><?php esc_html_e('Skip Setup & Go to Dashboard','spexo' ); ?></a>
                        </div>
                    </div>
                </div>
                <div id="install-plugins" class="tab-content tab-content-install-plugins">
                    <div class="tmpcoder-message-box install-plugin-part">
                    </div>
                </div>
                <div id="get-pro-plugins" class="tab-content tab-content-get-pro-plugins">
                    <div class="tmpcoder-message-box1 pro-plugins-part">
                    </div>
                </div>
                <div class="process-loader hide">
                    <span class="loader-image"></span>
                    <span class="loader-text"></span>
                </div>
            </div>
        </div>

        <div class="tmpcoder-skip-theme-wizard-popup-wrap tmpcoder-admin-popup-wrap">
            <div class="tmpcoder-skip-theme-wizard-popup tmpcoder-admin-popup">
                <div id="tmpcoder-skip-theme-wizard-confirm-popup" class="mfp-hide">
                    <h2 class="popup-heading"> <?php esc_html_e('Skip the Setup Wizard?','spexo') ?> </h2>
                    <div class="popup-content">
                         <p class="popup-message"><?php esc_html_e('Heads up! Would you like to continue without completing the setup wizard?', 'spexo') ?></p>
                        <p class="popup-message"><?php echo wp_kses_post(__('You’ll still be able to access the setup wizard later from the <strong>“Appearance → Setup Wizard”</strong> menu.', 'spexo')); ?></p>
                        <a class="button button-primary popup-close"><?php esc_html_e('Continue Setup', 'spexo') ?></a>
                        <a class="button button-secondary tmpcoder-skip-theme-wizard-confirm-button"><?php esc_html_e('Yes, Skip', 'spexo') ?></a>
                    </div>
                </div>
            </div>
        </div>

        <?php 
    }    
}

new Tmpcoder_Theme_Setup_Wizard();
