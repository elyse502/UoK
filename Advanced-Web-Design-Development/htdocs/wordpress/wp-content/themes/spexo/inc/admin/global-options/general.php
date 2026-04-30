<?php
/**
 * @author  Spexo
 * @since   1.0.0
 * @version 1.0.0
 */

$opt_name = TMPCODER_THEME_OPTION_NAME;

Redux::setSection( $opt_name,
    array(
        'title' => esc_html__( 'General Options','spexo' ),
        'id'    => 'tmpcoder_general_options',
        'icon'  => 'el el-cog',
    )
);

$menus = wp_get_nav_menus();
$menuName = array();
foreach ($menus as $key => $value) {
    $menuName[$value->term_id] = $value->name; 
}

Redux::setSection( $opt_name,
    array(
        'title'   => esc_html__( 'Header Options','spexo' ),
        'id'      => 'tmpcoder_header_options',
        'subsection' => true,
        'heading' => '',
        'fields'  => array(
            array(
                'id'       => 'tmpcoder_logo_image',
                'type'     => 'media',
                'title'    => esc_html__( 'Logo Image','spexo' ),
                'subtitle' => esc_html__( 'Upload your logo','spexo' ),
                'default'  => array(
                    'url'=> get_template_directory_uri() . '/assets/images/logo.png'
                ),
            ),
            array(
                'id'       => 'tmpcoder_logo_text',
                'type'     => 'text',
                'title'    => esc_html__( 'Logo Text','spexo' ),             
                'subtitle' => esc_html__( 'Set your logo text here','spexo' ),
                'default'  => 'Spexo'              
            ),
            array(
                'id'       => 'tmpcoder_fav_site_icon',
                'type'     => 'media',
                'title'    => esc_html__( 'Favicon Icon','spexo' ),
                'subtitle' => esc_html__( 'Define favicon icon path here','spexo' ),
                'default'  => array(
                    'url'=> get_template_directory_uri() . '/assets/images/favicon.png'
                ),
            ),

            array(
                'id'       => 'tmpcoder_pre_loder',
                'type'     => 'switch',
                'title'    => esc_html__('Site Preloader','spexo'), 
                'subtitle'         => __('show this option before site load.','spexo'),
                'on' => esc_html__('Enable','spexo'),
                'off' => esc_html__('Disable','spexo'),
            ),
            array( 
                'id'               => 'tmpcoder_preloder_custom_html',
                'type'             => 'editor',
                'title'            => __('Preloader Custom Html','spexo'), 
                'desc'     => __('This is show before load website custom preloader','spexo'),
                'default'          => '<img src="'.get_template_directory_uri().'/assets/images/loader.gif" alt="" width="80" height="80">',
                'args'   => array(
                    'teeny'            => true,
                    'textarea_rows'    => 10
                )
            ),
        )
    ) 
);

Redux::setSection( $opt_name,
    array(
        'title'   => esc_html__( 'Footer Options','spexo' ),
        'id'      => 'tmpcoder_footer_options',
        'subsection' => true,
        'heading' => '',
        'fields'  => array(
            array(
                'id'               => 'tmpcoder_footer_copyright_text',
                'type'             => 'editor',
                'title'            => __('Footer Copyright','spexo'),
                'subtitle'         => __('Footer copyright show in default footer. <br> Dynamic footer text change from Spexo Addons > Site Builder > Footer','spexo'),
                'description' => sprintf(
                    /* translators: %s is replaced by copyright symbol, current year, and site title */
                    'Shortcode List: [COPYRIGHT] = %1$s, [CURRENT_YEAR] = %2$s, [SITE_TITLE] = %3$s ',
                    /* Escaping dynamic content for safe output */
                    '&copy;',
                    esc_html(gmdate('Y')),
                    esc_html(get_bloginfo('name'))
                ),
                'default' => sprintf(
                    /* translators: %1$s is the URL, %2$s is the theme name */
                    'Copyright [COPYRIGHT] [CURRENT_YEAR] [SITE_TITLE] | Powered by <a href="%1$s" target="_blank" rel="noopener noreferrer">%2$s</a>',
                    esc_url('https://spexoaddons.com/'),
                    esc_html('Spexo WordPress Theme')
                ),
                'args'   => array(
                    'teeny'            => true,
                    'textarea_rows'    => 10
                )
            ),            
        ),
    )
);

Redux::setSection( $opt_name,
    array(
        'title'   => esc_html__( 'Sidebar Options', 'spexo' ),
        'id'      => 'tmpcoder_sidebar_options',
        'subsection' => true,
        'heading' => '',
        'fields'  => array(
            array(
                'id'       => 'tmpcoder_sidebar_customizer_note',
                'type'     => 'raw',
                'title'    => esc_html__( 'Sidebar Settings Location', 'spexo' ),
                'subtitle' => esc_html__( 'Click the button below to edit sidebar settings via the WordPress Customizer.', 'spexo' ),
                'content'  => '<a href="' . esc_url( admin_url( 'customize.php?autofocus[section]=tmpcoder_sidebar_settings' ) ) . '" class="button button-primary" target="_blank">' . esc_html__( 'Open Sidebar Settings in Customizer', 'spexo' ) . '</a>',
            ),
        ),
    )
);
