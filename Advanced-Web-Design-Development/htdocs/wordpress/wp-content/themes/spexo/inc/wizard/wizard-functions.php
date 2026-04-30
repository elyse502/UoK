<?php 

if ( !function_exists('tmpcoder_is_plugin_installed') ){
    function tmpcoder_is_plugin_installed( $slug ) {
        if ( ! function_exists( 'get_plugins' ) ) {
            require_once ABSPATH . 'wp-admin/includes/plugin.php';
        }
        $all_plugins = get_plugins();
        
        if ( !empty( $all_plugins[$slug] ) ) {
        return true;
        } else {
        return false;
        }
    }
}

if ( !function_exists('tmpcoder_plugin_basefile_path') ){
    function tmpcoder_plugin_basefile_path( $slug ) {
        if ( ! function_exists( 'get_plugins' ) ) {
            require_once ABSPATH . 'wp-admin/includes/plugin.php';
        }
        $all_plugins = get_plugins();
        $plugin_basefile_path = '';

        foreach( $all_plugins as $plg_key => $plg_val ){
            $plug_folderArr = explode('/', $plg_key);
            $plugin_folder_slug = $plug_folderArr[0];
            if ( $plugin_folder_slug == $slug ){
            //if ( $plg_val['TextDomain'] == $slug ){
                $plugin_basefile_path = $plg_key;
                break;
            }
        }
        return $plugin_basefile_path;
    }
}

/**
** Install a plugin.
*/
if (!function_exists('tmpcoder_install_plugin')) {
    
    function tmpcoder_install_plugin( $plugin_slug ) {
        if ( ! current_user_can( 'install_plugins' ) ) {
            return;
        }

        if ( ! function_exists( 'plugins_api' ) ) {
            require_once ABSPATH . 'wp-admin/includes/plugin-install.php';
        }
        if ( ! class_exists( 'WP_Upgrader' ) ) {
            require_once ABSPATH . 'wp-admin/includes/class-wp-upgrader.php';
        }

        if ( false === filter_var( $plugin_slug, FILTER_VALIDATE_URL ) ) {
            $api = plugins_api(
                'plugin_information',
                array(
                    'slug'   => $plugin_slug,
                    'fields' => array(
                        'short_description' => false,
                        'sections'          => false,
                        'requires'          => false,
                        'rating'            => false,
                        'ratings'           => false,
                        'downloaded'        => false,
                        'last_updated'      => false,
                        'added'             => false,
                        'tags'              => false,
                        'compatibility'     => false,
                        'homepage'          => false,
                        'donate_link'       => false,
                    ),
                )
            );

            $download_link = $api->download_link;
        } else {
            $download_link = $plugin_slug;
        }

        // Use AJAX upgrader skin instead of plugin installer skin.
        // ref: function wp_ajax_install_plugin().
        $upgrader = new Plugin_Upgrader( new WP_Ajax_Upgrader_Skin() );

        $install = $upgrader->install( $download_link );

        if ( false === $install ) {
            return false;
        } else {
            return true;
        }
    }
}

/**
** Update a plugin.
*/
if (!function_exists('tmpcoder_update_plugin')) {
    
    function tmpcoder_update_plugin( $plugin_path ) {
        if ( ! current_user_can( 'install_plugins' ) ) {
            return;
        }

        if ( ! function_exists( 'plugins_api' ) ) {
            require_once ABSPATH . 'wp-admin/includes/plugin-install.php';
        }
        if ( ! class_exists( 'WP_Upgrader' ) ) {
            require_once ABSPATH . 'wp-admin/includes/class-wp-upgrader.php';
        }

        // Use AJAX upgrader skin instead of plugin installer skin.
        // ref: function wp_ajax_install_plugin().
        $upgrader = new Plugin_Upgrader( new WP_Ajax_Upgrader_Skin() );

        $upgrade = $upgrader->upgrade( $plugin_path );

        if ( false === $upgrade ) {
            return false;
        } else {
            return true;
        }
    }
}