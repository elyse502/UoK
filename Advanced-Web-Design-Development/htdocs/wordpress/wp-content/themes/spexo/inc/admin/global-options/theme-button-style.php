<?php
/**
 * @author  Spexo
 * @since   1.0.0
 * @version 1.0.0
 */

$opt_name = TMPCODER_THEME_OPTION_NAME;

	Redux::setSection( $opt_name,
	    array(
	        'title' => esc_html__( 'Buttons','spexo' ),
	        'id'    => 'spexo_button_options',
	        'icon'  => 'el el-cog',
	    )
	);

    Redux::setSection( $opt_name, 
        array(
            'title'   => esc_html__( 'Desktop','spexo' ),
            'id'      => 'spexo_section_button_options', 
            'subsection' => true,
        	'heading' => '',       
            'icon'    => 'el el-btn',
            'desc'    => "Set button colors, background & font style for Desktop",
            'fields'  => array(
                array(
	                'id'          => 'button_style',
			        'type'        => 'typography', 
			        'title'       => __('Button Style','spexo'),
			        'google'      => true, 
			        'all_styles'  => true,
			        'output'      => array(),
			        'units'       =>'px',
			        'subtitle'    => __('Typography option with each property can be called individually.','spexo'),
                    'subsets'     => false,
                    'font-backup' => false,
                    'text-transform' => true,
                    'text-align' => true,
                    'letter-spacing' => true,
                    'default'     => array(
			            'font-family' => 'Poppins',
                        'font-style'  => '600',
                        'font-size' => '16px',
                        'line-height' => '20px',
                    	'letter-spacing' => '1px',
			            'color'       => '#ffffff',
			        ),
	            ),
	            array(
	                'id'       => 'site_button_text_hover',
	                'type'     => 'color',
	                'title'    => esc_html__( 'Site Button Font Hover Color','spexo' ),             
	                'subtitle' => esc_html__( 'Set your theme site button font hover color','spexo' ),
	                'output'    => array(), // An array of CSS selectors
	                'transparent' => false,
		            'default'  => '#5729d9',
		            'validate' => 'color',
	            ),
	            array(
	                'id'       => 'site_button_color',
	                'type'     => 'color',
	                'title'    => esc_html__( 'Site Button Background Color','spexo' ),             
	                'subtitle' => esc_html__( 'Set your theme site button background color','spexo' ),
	                'output'    => array(), // An array of CSS selectors
		            'default'  => '#5729d9',
		            'validate' => 'color',
	            ),
	            array(
	                'id'       => 'site_button_color_hover',
	                'type'     => 'color',
	                'title'    => esc_html__( 'Site Button Background Hover Color','spexo' ),             
	                'subtitle' => esc_html__( 'Set your theme site button background hover color','spexo' ),
	                'output'    => array(), // An array of CSS selectors
		            'default'  => 'transparent',
		            'validate' => 'color',
	            ),
		      	array(
				    'id' => 'button_border',
				    'type' => 'border',
				    'title' => __('Button Border','spexo'),
				    'all' => false,
				    'default' => array(
				        'border-color' => '#5729d9',
				        'border-style' => 'solid',
				        'border-top' => '1px',
				        'border-right' => '1px',
				        'border-bottom' => '1px',
				        'border-left' => '1px'
				    )
				),
				array(
				    'id' => 'button_border_hover',
				    'type' => 'border',
				    'title' => __('Button Border Hover','spexo'),
				    'all' => false,
				    'default' => array(
				        'border-color' => '#5729d9',
				        'border-hover-style' => 'solid',
				        'border-hover-top' => '0px',
				        'border-hover-right' => '0px',
				        'border-hover-bottom' => '0px',
				        'border-hover-left' => '0px'
				    )
				),
                array(
                    'id' => 'button_border_radius',
                    'type' => 'slider',
                    'title' => __('Button Border Radius','spexo'),
                    'subtitle' => __('if set Border Radius - 0 then Border Radius unset','spexo'),
                    "default" => 4,
                    "min" => 0,
                    "step" => 1,
                    "max" => 100,
                    'display_value' => 'text'
                ),
                array(
				    'id' => 'button_padding',
				    'type' => 'border',
				    'title' => __('Button Padding','spexo'),
				    'all' => false,
                    'color' => false,
                    'style' => false,
				    'default' => array(
				        'border-top' => '10px',
				        'border-right' => '15px',
				        'border-bottom' => '10px',
				        'border-left' => '15px'
				    )
				),
	        )
        )
    );

    Redux::setSection( $opt_name, 
        array(
            'title'   => esc_html__( 'Tablet','spexo' ),
            'id'      => 'spexo_section_button_options_tablet', 
            'subsection' => true,
        	'heading' => '',              
            'icon'    => 'el el-btn',
            'desc'    => "Set button font style for tablet",
            'fields'  => array(
                array(
	                'id'          => 'button_style_tablet',
			        'type'        => 'typography', 
			        'title'       => __('Button Style','spexo'),
			        'google'      => true, 
			        'all_styles'  => true,
			        'output'      => array(),
			        'units'       =>'px',
			        'subtitle'    => __('Typography option with each property can be called individually.','spexo'),
                    'subsets'     => false,
                    'font-backup' => false,
                    'font-style' => false,
                    'font-weight' => false,
                    'text-transform' => false,
                    'text-align' => false,
                    'font-family' => false,
                    'letter-spacing' => true,
                    'color'	=> false,
                    'default'     => array(
                        'font-size' => '16px',
                        'line-height' => '20px',
                        'letter-spacing' => '1px',
			        ),
	            ),
	            array(
				    'id' => 'button_padding_tablet',
				    'type' => 'border',
				    'title' => __('Button Padding','spexo'),
				    'all' => false,
                    'color' => false,
                    'style' => false,
				    'default' => array(
				        'border-top' => '5px',
				        'border-right' => '5px',
				        'border-bottom' => '5px',
				        'border-left' => '5px'
				    )
				),
	        )
        )
    );

    Redux::setSection( $opt_name, 
        array(
            'title'   => esc_html__( 'Mobile','spexo' ),
            'id'      => 'spexo_section_button_options_mobile', 
            'subsection' => true,
        	'heading' => '',              
            'icon'    => 'el el-btn',
            'desc'    => "Set button font style for mobile",
            'fields'  => array(
                array(
	                'id'          => 'button_style_mobile',
			        'type'        => 'typography', 
			        'title'       => __('Button Style','spexo'),
			        'google'      => true, 
			        'all_styles'  => true,
			        'output'      => array(),
			        'units'       =>'px',
			        'subtitle'    => __('Typography option with each property can be called individually.','spexo'),
                    'subsets'     => false,
                    'font-backup' => false,
                    'font-style' => false,
                    'font-weight' => false,
                    'text-transform' => false,
                    'text-align' => false,
                    'font-family' => false,
                    'letter-spacing' => true,
                    'color'	=> false,
                    'default'     => array(
                        'font-size' => '16px',
                        'line-height' => '20px',
                        'letter-spacing' => '1px',
			        ),
	            ),
	            array(
				    'id' => 'button_padding_mobile',
				    'type' => 'border',
				    'title' => __('Button Padding','spexo'),
				    'all' => false,
                    'color' => false,
                    'style' => false,
				    'default' => array(
				        'border-top' => '5px',
				        'border-right' => '5px',
				        'border-bottom' => '5px',
				        'border-left' => '5px'
				    )
				),
	        )
        )
    );


