<?php
/**
 * @author  Spexo
 * @since   1.0.0
 * @version 1.0.0
 */

$opt_name = TMPCODER_THEME_OPTION_NAME;

Redux::setSection( $opt_name, 
    array(
        'title'   => esc_html__( 'Global Colors','spexo' ),
        'id'      => 'tmpcoder_section_color_options1',        
        'icon'    => 'el el-adjust',
        'desc'    => "Add color to theme primary color, secondary color, background color and button color etc.",
        'fields'  => array(
            array(
                'id'       => 'primany_color',
                'type'     => 'color',
                'title'    => esc_html__( 'Primary Color','spexo' ),             
                'subtitle' => esc_html__( 'Set your theme main color','spexo' ),
                'transparent' => false,
	            'default'  => '#5729d9',
	            'validate' => 'color',
            ),
            array(
                'id'       => 'secondary_color',
                'type'     => 'color',
                'title'    => esc_html__( 'Secondary Color','spexo' ),             
                'subtitle' => esc_html__( 'Set your theme secondary color','spexo' ),
                'transparent' => false,
	            'default'  => '#010101',
	            'validate' => 'color',
            ),
            array(
                'id'       => 'accent_color',
                'type'     => 'color',
                'title'    => esc_html__( 'Accent Color','spexo' ),             
                'subtitle' => esc_html__( 'Set your theme accent color','spexo' ),
                'transparent' => false,
	            'default'  => '#b2b1b1',
	            'validate' => 'color',
            ),
            array(
                'id'       => 'accent_color',
                'type'     => 'color',
                'title'    => esc_html__( 'Accent Color','spexo' ),             
                'transparent' => false,
                'default'  => '#b2b1b1',
                'validate' => 'color',
            ),
            array(
                'id'       => 'theme_color_1',
                'type'     => 'color',
                'title'    => esc_html__( 'Theme Color 1','spexo' ),             
                'transparent' => false,
                'default'  => '#010101',
                'validate' => 'color',
            ),
            array(
                'id'       => 'theme_color_2',
                'type'     => 'color',
                'title'    => esc_html__( 'Theme Color 2','spexo' ),             
                'transparent' => false,
                'default'  => '#f4f2f9',   
                'validate' => 'color',
            ),
            array(
                'id'       => 'theme_color_3',
                'type'     => 'color',
                'title'    => esc_html__( 'Theme Color 3','spexo' ),
                'transparent' => false,
                'default'  => '#ffffff',
                'validate' => 'color',
            ),
            array(
                'id'       => 'theme_color_4',
                'type'     => 'color_rgba',
                'title'    => esc_html__( 'Theme Color 4','spexo' ),             
                'transparent' => false,
                'default'  => '#ffffff',
                'validate' => 'color',
            ),
            array(
                'id'       => 'site_background_color',
                'type'     => 'background',
                'title'    => __('Body Background','spexo'),
                'subtitle' => __('Body background with image, color, etc.','spexo'),
                'desc'     => __('You can set your site background color','spexo'),
                'background-repeat' => false,
                'background-attachment' => false,
                'background-position' => false,
                'background-image' => false,
                'background-size' => false,
                'transparent' => false,
                'default'  => array(
                    'background-color' => '#ffffff',
                )
            ),
            array(
                'id'       => 'link_color',
                'type'     => 'color',
                'title'    => esc_html__( 'Link Color','spexo' ),             
                'subtitle' => esc_html__( 'Set your theme link color','spexo' ),
                'transparent' => false,
                'default'  => '#5729d9',
                'validate' => 'color',
            ),
            array(
                'id'       => 'link_hover_color',
                'type'     => 'color',
                'title'    => esc_html__( 'Link Hover Color','spexo' ),             
                'subtitle' => esc_html__( 'Set your theme link hover color','spexo' ),
                'transparent' => false,
                'default'  => '#1d2327',
                'validate' => 'color',
            ),
            array(
                'id'       => 'global_border_color',
                'type'     => 'color',
                'title'    => esc_html__( 'Border Color','spexo' ),             
                'subtitle' => esc_html__( 'Set your theme border color','spexo' ),
                'transparent' => false,
                'default'  => '#E6E8EA',
                'validate' => 'color',
            ),
        )
    )
);

