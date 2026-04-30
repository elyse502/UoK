<?php 

add_action("wp_ajax_tmpcoder_get_recommended_plugins", "tmpcoder_get_recommended_plugins");
add_action("wp_ajax_nopriv_tmpcoder_get_recommended_plugins", "tmpcoder_get_recommended_plugins");
function tmpcoder_get_recommended_plugins(){

    if (!isset($_POST['nonce']) || !wp_verify_nonce(sanitize_text_field(wp_unslash($_POST['nonce'])), 'tmpcoder_get_plugins') ) {
    	exit; // Get out of here, the nonce is rotten!
    }
    
    if ( ! is_user_logged_in() ){
        esc_html_e("You must log in to site setup", 'spexo');
        die();
    }

    $tgmpaClass = $GLOBALS['tgmpa'];
    $plugins = array();
    $next_step = __('Next', 'spexo');
    $activated_plugin = [];

    if ( is_object($tgmpaClass) ){
        if ( empty($tgmpaClass->plugins) ){
            $tmpcoder_mainClass = new Tmpcoder_Main_Class();
            $tmpcoder_mainClass->tmpcoder_require_plugins();
            $tgmpaClass = $GLOBALS['tgmpa'];
        }

        if ( !empty($tgmpaClass->plugins) ) {
            foreach( $tgmpaClass->plugins as $plugKey => $plugin ){
                $image = '';
                $plugin_info = wp_remote_get('https://api.wordpress.org/plugins/info/1.0/'.$plugKey.'.json?fields=banners,icons');

                if ( is_array( $plugin_info ) && ! is_wp_error( $plugin_info ) ) {
                    $body    = json_decode($plugin_info['body'], true);
                    if ( isset($body['icons']) ){
                        if ( isset($body['icons']['svg']) ){
                            $image = $body['icons']['svg'];
                        }else if ( isset($body['icons']['2x']) ){
                            $image = $body['icons']['2x'];
                        }else if ( isset($body['icons']['1x']) ){
                            $image = $body['icons']['1x'];
                        }else{
                            $image = $body['icons']['default'];
                        }
                    }
                }

                $plugin['image'] = $image;
                $plugin['link'] = 'https://wordpress.org/plugins/'. $plugin['slug'];

                // modify these variables with your new/old plugin values
                $plugin_slug = $plugin['slug'];
                $plugin_file_path = $plugin['file_path'];

                if (is_plugin_active($plugin_file_path)) {
                    array_push($activated_plugin, $plugin_slug);
                }
                
                if ( tmpcoder_is_plugin_installed( $plugin_file_path ) && in_array($plugin_file_path, apply_filters('active_plugins', get_option('active_plugins'))) ){
                    $plugin['activated'] = true;
                }else if ( tmpcoder_is_plugin_installed( $plugin_file_path ) ) {
                    $plugin['installed'] = true;
                    $next_step = __('Install & Activate', 'spexo');
                }else{
                    $next_step = __('Install & Activate', 'spexo');
                }
                array_push($plugins, $plugin);
            }

            $skip_this = false;
            if (count($activated_plugin) == 3) {
                $skip_this = true;
                update_option(TMPCODER_THEME_SLUG.'_wizard_step', '2');
            }
        }
    }

    if ( !empty($plugins) ){
        update_option(TMPCODER_THEME_SLUG.'_wizard_step', '1');
        wp_send_json_success(
            array(
                'plugins'=> $plugins,
                'message'=> __('Plugins getting successfully.','spexo'),
                'next_step'=> $next_step,
                'skip_this' => $skip_this
            )
        );
    }else{
        $error = __('No recommended plugins found','spexo');
        wp_send_json_error(array('message'=> $error ));
    }
}

add_action("wp_ajax_tmpcoder_install_recommended_plugins", "tmpcoder_install_recommended_plugins");
add_action("wp_ajax_nopriv_tmpcoder_install_recommended_plugins", "tmpcoder_install_recommended_plugins");
function tmpcoder_install_recommended_plugins(){

    // Check if nonce is valid.
    if ( ! isset($_POST['_wpnonce']) || ! wp_verify_nonce(sanitize_text_field(wp_unslash ($_POST['_wpnonce'])), 'tmpcoder_install_plugins' ) ) {
        exit;
    }
    
    if ( ! is_user_logged_in() ){
        esc_html_e("You must log in to site setup", 'spexo');
        die();
    }

    $plugins = '';
    if ( isset($_POST['plugins']) && is_array($_POST['plugins']) ) {
        $plugins = array_map('sanitize_text_field',wp_unslash($_POST['plugins']));
    }

    $tgmpaClass = $GLOBALS['tgmpa'];
    $error = array();

    ob_start(); // default print off

    if ( is_object($tgmpaClass) ){
        if ( empty($tgmpaClass->plugins) ){
            $tmpcoder_mainClass = new Tmpcoder_Main_Class();
            $tmpcoder_mainClass->tmpcoder_require_plugins();
            $tgmpaClass = $GLOBALS['tgmpa'];
        }

        if ( !empty($tgmpaClass->plugins) ) {
            foreach( $tgmpaClass->plugins as $plugKey => $plugin ){

                if ( isset($plugins[$plugKey]) && $plugins[$plugKey] == '1' ){

                    // modify these variables with your new/old plugin values
                    $plugin_slug = $plugin['slug'];
                    $plugin_file_path = $plugin['file_path'];
                    
                    // echo 'Check if new plugin is already installed - ';
                    if ( tmpcoder_is_plugin_installed( $plugin_file_path ) ) {
                        tmpcoder_update_plugin( $plugin_file_path );
                        $installed = true;

                    } else {
                        $plugin_zip = $tgmpaClass->get_download_url($plugin_slug);

                        $installed = tmpcoder_install_plugin( $plugin_slug );
                    }

                    if ( !is_wp_error( $installed ) && $installed ) {

                        $plugin_file_path = tmpcoder_plugin_basefile_path($plugin_slug);

                        // echo 'Activating new plugin.';
                        $activate = activate_plugin( $plugin_file_path );
                       
                        if ( is_wp_error( $activate ) ){
                            $error[] = $plugin['name'].': '.$activate->get_error_message();
                        }
                        
                    } else {
                        $error[] = $plugin['name'];
                    }
                }
            }
        }
    }

    ob_end_clean();

    echo "  ";
    echo "00000";

    if ( empty($error) ){
        update_option(TMPCODER_THEME_SLUG.'_wizard_step', '2');
        update_option('sastra_addons_wizard_page', 1);
        update_option('spexo_addons_wizard_page', 1);

        echo wp_json_encode( array('success'=> true, 'data' => array("message"=> __('All recommended plugins installed & activated successfully.','spexo') ) ) );
        exit;

    }else{
        $error = implode(', ', $error). __(' Could not install','spexo');
        wp_send_json_error(array('message'=> $error ));
    }
}

add_action("wp_ajax_tmpcoder_get_pro_plugin_info", "tmpcoder_get_pro_plugin_info");
add_action("wp_ajax_nopriv_tmpcoder_get_pro_plugin_info", "tmpcoder_get_pro_plugin_info");
function tmpcoder_get_pro_plugin_info(){
    
    if (!isset($_POST['nonce']) || !wp_verify_nonce(sanitize_text_field(wp_unslash($_POST['nonce'])), 'tmpcoder_get_pro_addons_info') ) {
    	exit; // Get out of here, the nonce is rotten!
    }
    
    if ( ! is_user_logged_in() ){
        esc_html_e("You must log in to site setup", 'spexo');
        die();
    }

    $admin_url = admin_url('?saved=wizard');

    if ( is_plugin_active( 'sastra-essential-addons-for-elementor/sastra-essential-addons-for-elementor.php' ) ) {
        
        $admin_url = admin_url('admin.php?page=tmpcoder-import-demo&saved=wizard');
    }

    ob_start();
        echo wp_kses_post(sprintf(
            /* translators: %s is License Activation Heading */
            '<h2 class="wizard-heading">%s</h2>', __("Get Spexo Addons Pro", 'spexo')));
            echo '<p class="wizard-title-text">'.esc_html('Unlock access to all our premium widgets and features.').'</p>';
            echo '<ul class="tmpcoder-wizard-pro-features-list">
                    <li>'.esc_html('80+ Pro Widgets').'</li>
                    <li>'.esc_html('75+ Pro Prebuilt Blocks').'</li>
                    <li>'.esc_html('25+ Pro Prebuilt Sections').'</li>
                    <li>'.esc_html('30+ Pro Prebuilt WebSites').'</li>
                </ul>';

            echo "<a target='_blank' href='".esc_url(TMPCODER_PURCHASE_PRO_URL.'?ref=tmpcoder-theme-wizard')."' class='tmpcoder-get-pro-btn'>";
            ?>

            <img src="<?php echo esc_url(get_template_directory_uri().'/assets/images/pro-icon.svg'); ?>">
            <span><?php echo esc_html__( 'Get Pro Now', 'spexo' ); ?></span>

            <?php
            echo "</a>";

            echo '<div class="next-step-action">';
            echo '<a href='.esc_url($admin_url).' class="button button-primary next-step-btn">'.esc_html__('Done', 'spexo').'</a>';
            echo '</div>';

    $output = ob_get_contents();
    ob_end_clean();

    wp_send_json_success(array( 'data'=> $output ));
}