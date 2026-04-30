<?php
/**
 * Customizer Setup and Customize Controls
 */

/**
 * Adds the individual sections, settings, and controls to the theme customizer
 */
class Tmpcoder_initialise_customizer_settings {

	public function __construct() {
		// Register Panels
		add_action( 'customize_register', array( $this, 'tmpcoder_add_customizer_panels' ) );

		// Register sections
		add_action( 'customize_register', array( $this, 'tmpcoder_add_customizer_sections' ) );

		// Register Layout controls
		add_action( 'customize_register', array( $this, 'tmpcoder_layout_controls' ) );
		
		// Register Global Colors controls
		add_action( 'customize_register', array( $this, 'tmpcoder_global_colors_controls' ) );

		// Register Typography controls
		add_action( 'customize_register', array( $this, 'tmpcoder_typography_desktop_controls' ) );
		add_action( 'customize_register', array( $this, 'tmpcoder_typography_tablet_controls' ) );
		add_action( 'customize_register', array( $this, 'tmpcoder_typography_mobile_controls' ) );

		// Register Buttons controls
		add_action( 'customize_register', array( $this, 'tmpcoder_buttons_desktop_controls' ) );
		add_action( 'customize_register', array( $this, 'tmpcoder_buttons_tablet_controls' ) );
		add_action( 'customize_register', array( $this, 'tmpcoder_buttons_mobile_controls' ) );

		// Register Footer controls
		add_action( 'customize_register', array( $this, 'tmpcoder_footer_controls' ) );

        add_action( 'customize_register', array( $this, 'tmpcoder_site_identify_logo_show_controls' ) );
	}

	/**
	 * Register the Customizer panels
	 */
	public function tmpcoder_add_customizer_panels( $wp_customize ) {

		// remove wp default colors section
		$wp_customize->remove_section('colors');

		/**
		 * Add Typography Panel
		 */
		$title = __( 'Typography', 'spexo' );
		$wp_customize->add_panel( 'typography_panel',
			array(
				'title' => esc_html( $title ),
				'priority' => 3,
			)
		);
		
		/**
		 * Add Buttons Panel
		 */
		 $wp_customize->add_panel( 'buttons_panel',
		 	array(
				'title' => esc_html__( 'Buttons', 'spexo' ),
				'priority' => 4,
			)
		);
	}

	/**
	 * Register the Customizer sections
	 */
	public function tmpcoder_add_customizer_sections( $wp_customize ) {

		/**
		 * Add Upsell Section
		 */
		$wp_customize->add_section( new Tmpcoder_Upsell_Section( $wp_customize, 'upsell_section',
			array(
				'title' => esc_html__( 'Spexo Addons PRO Features', 'spexo' ),
				'url' => TMPCODER_PURCHASE_PRO_URL.'?ref=tmpcoder-customizer',
				'backgroundcolor' => '#5729d9',
				'textcolor' => '#fff',
				'priority' => 0,
			)
		) );
		
		/**
		 * Add Layout Section
		 */
		$wp_customize->add_section( 'layout_section',
			array(
				'title' => esc_html__( 'Layout', 'spexo' ),
				'priority' => 1,
			)
		);

		/**
		 * Add Global Colors Section
		 */
		$wp_customize->add_section( 'global_colors',
			array(
				'title' => esc_html__( 'Global Colors', 'spexo' ),
				'description' => esc_html__( 'Add color to theme primary color, secondary color, background color and button color etc.', 'spexo' ),
				'priority' => 2,
			)
		);

		/**
		 * Add Typography Desktop Section
		 */
		$wp_customize->add_section( 'typography_desktop',
			array(
				'title' => esc_html__( 'Desktop', 'spexo' ),
				'description' => esc_html__( 'Set site fonts & headings for Desktop', 'spexo' ),
				'panel' => 'typography_panel'
			)
		);

		/**
		 * Add Typography Tablet Section
		 */
		$wp_customize->add_section( 'typography_tablet',
			array(
				'title' => esc_html__( 'Tablet', 'spexo' ),
				'description' => esc_html__( 'Set site fonts & headings for Tablet', 'spexo' ),
				'panel' => 'typography_panel'
			)
		);

		/**
		 * Add Typography Mobile Section
		 */
		$wp_customize->add_section( 'typography_mobile',
			array(
				'title' => esc_html__( 'Mobile', 'spexo' ),
				'description' => esc_html__( 'Set site fonts & headings for Mobile', 'spexo' ),
				'panel' => 'typography_panel'
			)
		);

		/**
		 * Add Buttons Desktop Section
		 */
		$wp_customize->add_section( 'buttons_desktop',
			array(
				'title' => esc_html__( 'Desktop', 'spexo' ),
				'description' => esc_html__( 'Set button colors, background & font style for Desktop', 'spexo' ),
				'panel' => 'buttons_panel'
			)
		);

		/**
		 * Add Buttons Tablet Section
		 */
		$wp_customize->add_section( 'buttons_tablet',
			array(
				'title' => esc_html__( 'Tablet', 'spexo' ),
				'description' => esc_html__( 'Set button font style for tablet', 'spexo' ),
				'panel' => 'buttons_panel'
			)
		);

		/**
		 * Add Buttons Mobile Section
		 */
		$wp_customize->add_section( 'buttons_mobile',
			array(
				'title' => esc_html__( 'Mobile', 'spexo' ),
				'description' => esc_html__( 'Set button font style for mobile', 'spexo' ),
				'panel' => 'buttons_panel'
			)
		);

		/**
		 * Add Footer Options Section
		 */
		$wp_customize->add_section( 'footer_options',
			array(
				'title' => esc_html__( 'Footer Options', 'spexo' ),
				'priority' => 4,
			)
		);
	}

	/**
	 * Register layout controls
	 */
	public function tmpcoder_layout_controls( $wp_customize ) {

		// Container width customize control (desktop)
		$wp_customize->add_setting( 'container_width',
			array(
				'default' => 1200,
				'transport' => 'postMessage',
				'sanitize_callback' => 'absint'
			)
		);
		$wp_customize->add_control( new Tmpcoder_Slider_Customize_Control( $wp_customize, 'container_width_control',
			array(
				'label' => esc_html__( 'Container Width (Desktop)', 'spexo' ),
				'section' => 'layout_section',
				'settings'    => 'container_width',
				'input_attrs' => array(
					'min' => 1024,
					'max' => 1920,
					'step' => 1,
				),
			)
		) );

		// Container width customize control (tablet)
		$wp_customize->add_setting( 'container_width_tablet',
			array(
				'default' => '1000',
				'transport' => 'postMessage',
				'sanitize_callback' => 'absint'
			)
		);
		$wp_customize->add_control( new Tmpcoder_Slider_Customize_Control( $wp_customize, 'container_width_tablet_control',
			array(
				'label' => esc_html__( 'Container Width (Tablet)', 'spexo' ),
				'section' => 'layout_section',
				'settings'    => 'container_width_tablet',
				'input_attrs' => array(
					'min' => 768,
					'max' => 1023,
					'step' => 1,
				),
			)
		) );
		
		// Container width customize control (mobile)
		$wp_customize->add_setting( 'container_width_mobile',
			array(
				'default' => '500',
				'transport' => 'postMessage',
				'sanitize_callback' => 'absint'
			)
		);
		$wp_customize->add_control( new Tmpcoder_Slider_Customize_Control( $wp_customize, 'container_width_mobile_control',
			array(
				'label' => esc_html__( 'Container Width (Mobile)', 'spexo' ),
				'section' => 'layout_section',
				'settings'    => 'container_width_mobile',
				'input_attrs' => array(
					'min' => 320,
					'max' => 767,
					'step' => 1,
				),
			)
		) );
	}

	/**
	 * Register Global Colors controls
	 */
	public function tmpcoder_global_colors_controls( $wp_customize ) {
		$controls = array(
	        array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'primary_color',
	            'default'  => '#5729d9',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Primary Color", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'secondary_color',
	            'default'  => '#010101',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Secondary Color", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'accent_color',
	            'default'  => '#b2b1b1',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Accent Color", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'site_background_color',
	            'default'  => '#ffffff',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Body Background", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'link_color',
	            'default'  => '#5729d9',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Link Color", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'link_hover_color',
	            'default'  => '#5729d9',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Link Hover Color", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'border_color',
	            'default'  => '#E6E8EA',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Border Color", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'theme_color_1',
	            'default'  => '#010101',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Theme Color 1", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'theme_color_2',
	            'default'  => '#f4f2f9',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Theme Color 2", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'theme_color_3',
	            'default'  => '#ffffff',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Theme Color 3", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'theme_color_4',
	            'default'  => '#ffffff',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Theme Color 4", 'spexo' ),
	            'section'  => 'global_colors',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
		);

		foreach ( $controls as $control ) {
	        $wp_customize->add_setting(
	            $control['setting'],
	            array(
	                'default'           => $control['default'],
					'transport' 		=> isset($control['transport']) ? $control['transport'] : 'postMessage',
	                'sanitize_callback' => $control['sanitize'],
	            )
	        );

	        $control_class = $control['control'];
	        $control_args = array(
			    'label'       => $control['label'],
			    'section'     => $control['section'],
			);

			if ( isset( $control['description'] ) ) {
			    $control_args['description'] = $control['description'];
			}

			if ( isset( $control['input_attrs'] ) ) {
			    $control_args['input_attrs'] = $control['input_attrs'];
			}
			
			if ( isset( $control['choices'] ) ) {
			    $control_args['choices'] = $control['choices'];
			}

			$wp_customize->add_control(
                new $control_class(
                    $wp_customize,
                    $control['setting'],
                    $control_args
                )
            );
	    }
	}

	/**
	 * Register Typography Desktop controls
	 */
	public function tmpcoder_typography_desktop_controls( $wp_customize ) {
	    // Define common settings and controls dynamically in an array
	    $controls = array(
	        array(
	            'control'  => 'Tmpcoder_Notice_Customize_Control',
	            'setting'  => 'site_fonts_options_title',
	            'default'  => '',
	            'sanitize' => 'tmpcoder_text_sanitization',
	            'label'    => esc_html__( 'Site Fonts', 'spexo' ),
	            'section'  => 'typography_desktop',
	            'transport' => 'postMessage',
	        ),
	        array(
	            'control'  => 'Tmpcoder_Google_Font_Select_Customize_Control',
	            'setting'  => 'site_fonts_options_font-family',
	            'default'  => json_encode(
					array(
						'font' 			=> 'Open Sans',
						'regularweight' => 'regular',
						'category' 		=> 'sans-serif'
					)
				),
	            'sanitize' => 'tmpcoder_google_font_sanitization',
	            'label'    => '',
	            'description' => esc_html__( 'Font Family', 'spexo' ),
	            'section'  => 'typography_desktop',
	            'input_attrs' => array(
	                'font_count' => 'all',
	                'orderby' => 'alpha',
	            ),
	        ),
	        array(
                'control'  => 'Tmpcoder_Slider_Customize_Control',
                'setting'  => "site_fonts_options_font-size",
                'default'  => 36,
                'sanitize' => 'tmpcoder_range_sanitization',
                'label'    => esc_html__( "Font Size (px)", 'spexo' ),
                'section'  => 'typography_desktop',
                'input_attrs' => array(
                    'min' => 14,
                    'max' => 90,
                    'step' => 1,
                ),
            ),
	        array(
                'control'  => 'Tmpcoder_Slider_Customize_Control',
                'setting'  => "site_fonts_options_line-height",
                'default'  => 1.5,
                'sanitize' => 'tmpcoder_range_sanitization',
                'label'    => esc_html__( "Line Height", 'spexo' ),
                'section'  => 'typography_desktop',
                'input_attrs' => array(
                    'min' => 1,
                    'max' => 4,
                    'step' => 0.1,
                ),
            ),
            array(
                'control'  => 'Tmpcoder_Slider_Customize_Control',
                'setting'  => "site_fonts_options_letter-spacing",
                'default'  => 0,
                'sanitize' => 'tmpcoder_range_sanitization',
                'label'    => esc_html__( "Letter Spacing (px)", 'spexo' ),
                'section'  => 'typography_desktop',
                'input_attrs' => array(
                    'min' => 0,
                    'max' => 4,
                    'step' => 0.1,
                ),
            ),
            array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'site_fonts_options_color',
	            'default'  => '#727272',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => '',
	            'description' => esc_html__( 'Font Color', 'spexo' ),
	            'section'  => 'typography_desktop',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),	  
	    );

	    // Add H1-H6 controls
	    foreach ( range( 1, 6 ) as $heading_level ) {
	        $controls = array_merge( $controls, array(
	        	array(
		            'control'  => 'Tmpcoder_Divider_Customize_Control',
		            // 'setting'  => "heading_{$heading_level}_divider",
					/* translators: %s: Heading level */
					'setting' => sprintf(__('heading_%s_divider', 'spexo'), $heading_level),
		            'default'  => '',
		            'sanitize' => '',
		            'label'    => '',
		            'section'  => 'typography_desktop',
		            'transport' => 'postMessage',
		            'input_attrs' => array(
		                'width' => 'full',
		                'type' => 'solid',
		                'margintop' => 20,
		                'marginbottom' => 30,
		            ),
		        ),
		        array(
		            'control'  => 'Tmpcoder_Notice_Customize_Control',
		            'setting'  => "heading_{$heading_level}_title",
		            'default'  => '',
		            'sanitize' => 'tmpcoder_text_sanitization',
					/* translators: %s: This is the heading level */
					'label'    => sprintf(esc_html__('Heading %s Font', 'spexo'), $heading_level),
		            'section'  => 'typography_desktop',
		            'transport' => 'postMessage',
		        ),

		        array(
		            'control'  => 'Tmpcoder_Google_Font_Select_Customize_Control',
		            'setting'  => "heading_{$heading_level}_font-family",
		            'default'  => json_encode(
						array(
							'font' => 'Poppins',
							'regularweight' => 'regular',
							'category' => 'sans-serif'
						)
					),
		            'sanitize' => 'tmpcoder_google_font_sanitization',
		            'label'    => '',
		            'description' => esc_html__( 'Font Family', 'spexo' ),
		            'section'  => 'typography_desktop',
		            'input_attrs' => array(
		                'font_count' => 'all',
		                'orderby' => 'alpha',
		            ),
		        ),
		        array(
		        	'control'  => 'Tmpcoder_Dropdown_Select2_Customize_Control',
	                'type'     => 'select',
	                'setting'  => "heading_{$heading_level}_text-transform",
	                'default'  => 'initial',
	                'sanitize' => 'tmpcoder_text_sanitization',
	                'label'    => esc_html__( "Text Transform", 'spexo' ),
	                'section'  => 'typography_desktop',
	                'choices'  => array(
	                    'none'       => esc_html__( 'None', 'spexo' ),
	                    'capitalize' => esc_html__( 'Capitalize', 'spexo' ),
	                    'uppercase'  => esc_html__( 'Uppercase', 'spexo' ),
	                    'lowercase'  => esc_html__( 'Lowercase', 'spexo' ),
	                    'initial'    => esc_html__( 'Initial', 'spexo' ),
	                    'inherit'    => esc_html__( 'Inherit', 'spexo' ),
	                ),
	            ),
		        array(
	                'control'  => 'Tmpcoder_Slider_Customize_Control',
	                'setting'  => "heading_{$heading_level}_font-size",
	                'default'  => (48 -( $heading_level * 6)),
	                'sanitize' => 'tmpcoder_range_sanitization',
	                'label'    => esc_html__( "Font Size (px)", 'spexo' ),
	                'section'  => 'typography_desktop',
	                'input_attrs' => array(
	                    'min' => 16,
	                    'max' => 90,
	                    'step' => 1,
	                ),
	            ),
		        array(
	                'control'  => 'Tmpcoder_Slider_Customize_Control',
	                'setting'  => "heading_{$heading_level}_line-height",
	                'default'  => 1.2,
	                'sanitize' => 'tmpcoder_range_sanitization',
	                'label'    => esc_html__( "Line Height", 'spexo' ),
	                'section'  => 'typography_desktop',
	                'input_attrs' => array(
	                    'min' => 1,
	                    'max' => 4,
	                    'step' => 0.1,
	                ),
	            ),
	            array(
	                'control'  => 'Tmpcoder_Slider_Customize_Control',
	                'setting'  => "heading_{$heading_level}_letter-spacing",
	                'default'  => 0,
	                'sanitize' => 'tmpcoder_range_sanitization',
	                'label'    => esc_html__( "Letter Spacing (px)", 'spexo' ),
	                'section'  => 'typography_desktop',
	                'input_attrs' => array(
	                    'min' => 0,
	                    'max' => 4,
	                    'step' => 0.1,
	                ),
	            ),
	            array(
		            'control'  => 'Tmpcoder_Alpha_Color_Control',
		            'setting'  => "heading_{$heading_level}_color",
		            'default'  => '#1d2327',
		            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
		            'label'    => '',
		            'description' => esc_html__( 'Font Color', 'spexo' ),
		            'section'  => 'typography_desktop',
		            'input_attrs' => array(
		                'resetalpha' => false,
		                'palette'    => array(
		                    'rgba(99,78,150,1)',
		                    'rgba(67,78,150,1)',
		                    'rgba(34,78,150,.7)',
		                    'rgba(3,78,150,1)',
		                    'rgba(7,110,230,.9)',
		                    'rgba(234,78,150,1)',
		                    'rgba(99,78,150,.5)',
		                    'rgba(190,120,120,.5)',
		                ),
		            ),
		        ),	  
	        ) );
	    }

	    // Loop through the controls array and add settings and controls
	    foreach ( $controls as $control ) {
	        $wp_customize->add_setting(
	            $control['setting'],
	            array(
	                'default'           => $control['default'],
					'transport' 		=> isset($control['transport']) ? $control['transport'] : 'postMessage',
	                'sanitize_callback' => $control['sanitize'],
	            )
	        );

	        $control_class = $control['control'];
	        $control_args = array(
			    'label'       => $control['label'],
			    'section'     => $control['section'],
			);

			if ( isset( $control['description'] ) ) {
			    $control_args['description'] = $control['description'];
			}

			if ( isset( $control['input_attrs'] ) ) {
			    $control_args['input_attrs'] = $control['input_attrs'];
			}

			if ( isset( $control['type'] ) ) {
			    $control_args['type'] = $control['type'];
			}

			if ( isset( $control['choices'] ) ) {
			    $control_args['choices'] = $control['choices'];
			}

	       if ( $control_class === 'WP_Customize_Control' ) {
	            $wp_customize->add_control(
	                $control['setting'],
	                $control_args
	            );
	        } else {
	            $wp_customize->add_control(
	                new $control_class(
	                    $wp_customize,
	                    $control['setting'],
	                    $control_args
	                )
	            );
	        }
	    }
	}

	/**
	 * Register Typography Tablet controls
	 */
	public function tmpcoder_typography_tablet_controls( $wp_customize ) {

	    // Define the controls dynamically in an array
	    $controls = array(
	    	// Site font tablet controls - start
	        array(
	            'control'  => 'Tmpcoder_Notice_Customize_Control',
	            'setting'  => 'site_fonts_options_tablet_title',
	            'default'  => '',
	            'sanitize' => 'tmpcoder_text_sanitization',
	            'label'    => esc_html__( 'Site Fonts', 'spexo' ),
	            'section'  => 'typography_tablet',
	            'transport' => 'postMessage',
	        ),
	        array(
	            'control'  => 'Tmpcoder_Slider_Customize_Control',
	            'setting'  => 'site_fonts_options_tablet_font-size',
	            'default'  => 36,
	            'sanitize' => 'tmpcoder_range_sanitization',
	            'label'    => esc_html__( 'Font Size (px)', 'spexo' ),
	            'section'  => 'typography_tablet',
	            'input_attrs' => array(
	                'min' => 14,
	                'max' => 90,
	                'step' => 1,
	            ),
	        ),
	        array(
	            'control'  => 'Tmpcoder_Slider_Customize_Control',
	            'setting'  => 'site_fonts_options_tablet_line-height',
	            'default'  => 1.5,
	            'sanitize' => 'tmpcoder_range_sanitization',
	            'label'    => esc_html__( 'Line Height', 'spexo' ),
	            'section'  => 'typography_tablet',
	            'input_attrs' => array(
	                'min' => 1,
	                'max' => 4,
	                'step' => .1,
	            ),
	        ),
	        array(
	            'control'  => 'Tmpcoder_Slider_Customize_Control',
	            'setting'  => 'site_fonts_options_tablet_letter-spacing',
	            'default'  => 0,
	            'sanitize' => 'tmpcoder_range_sanitization',
	            'label'    => esc_html__( 'Letter Spacing (px)', 'spexo' ),
	            'section'  => 'typography_tablet',
	            'input_attrs' => array(
	                'min' => 0,
	                'max' => 4,
	                'step' => .1,
	            ),
	        ),
	    );
	    // Site font tablet controls - end

	    // Add H1-H6 controls
	    foreach ( range( 1, 6 ) as $heading_level ) {
	        $controls = array_merge( $controls, array(
	        	array(
		            'control'  => 'Tmpcoder_Divider_Customize_Control',
		            'setting'  => "heading_{$heading_level}_tablet_divider",
		            'default'  => '',
		            'sanitize' => '',
		            'label'    => '',
		            'section'  => 'typography_tablet',
		            'transport' => 'postMessage',
		            'input_attrs' => array(
		                'width' => 'full',
		                'type' => 'solid',
		                'margintop' => 20,
		                'marginbottom' => 30,
		            ),
		        ),
		        array(
		            'control'  => 'Tmpcoder_Notice_Customize_Control',
		            'setting'  => "heading_{$heading_level}_tablet_title",
		            'default'  => '',
		            'sanitize' => 'tmpcoder_text_sanitization',
					/* translators: %s: This is the heading level */
		            'label'    => sprintf(esc_html__('Heading %s Font', 'spexo'), $heading_level),
		            'section'  => 'typography_tablet',
		            'transport' => 'postMessage',
		        ),
		        array(
		            'control'  => 'Tmpcoder_Slider_Customize_Control',
		            'setting'  => "heading_{$heading_level}_tablet_font-size",
		            'default'  => 36,
		            'sanitize' => 'tmpcoder_range_sanitization',
		            'label'    => esc_html__( 'Font Size (px)', 'spexo' ),
		            'section'  => 'typography_tablet',
		            'input_attrs' => array(
		                'min' => 16,
		                'max' => 90,
		                'step' => 1,
		            ),
		        ),
		        array(
		            'control'  => 'Tmpcoder_Slider_Customize_Control',
		            'setting'  => "heading_{$heading_level}_tablet_line-height",
		            'default'  => 1.5,
		            'sanitize' => 'tmpcoder_range_sanitization',
		            'label'    => esc_html__( 'Line Height', 'spexo' ),
		            'section'  => 'typography_tablet',
		            'input_attrs' => array(
		                'min' => 1,
		                'max' => 4,
		                'step' => .1,
		            ),
		        ),
		        array(
		            'control'  => 'Tmpcoder_Slider_Customize_Control',
		            'setting'  => "heading_{$heading_level}_tablet_letter-spacing",
		            'default'  => 0,
		            'sanitize' => 'tmpcoder_range_sanitization',
		            'label'    => esc_html__( 'Letter Spacing (px)', 'spexo' ),
		            'section'  => 'typography_tablet',
		            'input_attrs' => array(
		                'min' => 0,
		                'max' => 4,
		                'step' => .1,
		            ),
		        ),
	        ) );
	    }


	    // Loop through the controls array and add settings and controls
	    foreach ( $controls as $control ) {
	        $wp_customize->add_setting(
	            $control['setting'],
	            array(
	                'default'           => $control['default'],
					'transport' 		=> isset($control['transport']) ? $control['transport'] : 'postMessage',
	                'sanitize_callback' => $control['sanitize'],
	            )
	        );

	        $control_class = $control['control'];
	        $control_args = array(
	            'label'       => $control['label'],
	            'section'     => $control['section'],
	        );

	        // Merge input attributes if provided
	        if ( isset( $control['input_attrs'] ) ) {
	            $control_args['input_attrs'] = $control['input_attrs'];
	        }

	        $wp_customize->add_control(
	            new $control_class(
	                $wp_customize,
	                $control['setting'],
	                $control_args
	            )
	        );
	    }
	}

	/**
	 * Register Typography Mobile controls
	 */
	public function tmpcoder_typography_mobile_controls( $wp_customize ) {

	    $controls = array(
	    	// Site font mobile controls - start
	        array(
	            'control'  => 'Tmpcoder_Notice_Customize_Control',
	            'setting'  => 'site_fonts_options_mobile_title',
	            'default'  => '',
	            'sanitize' => 'tmpcoder_text_sanitization',
	            'label'    => esc_html__( 'Site Fonts', 'spexo' ),
	            'section'  => 'typography_mobile',
	            'transport' => 'postMessage',
	        ),
	        array(
	            'control'  => 'Tmpcoder_Slider_Customize_Control',
	            'setting'  => 'site_fonts_options_mobile_font-size',
	            'default'  => 36,
	            'sanitize' => 'tmpcoder_range_sanitization',
	            'label'    => esc_html__( 'Font Size (px)', 'spexo' ),
	            'section'  => 'typography_mobile',
	            'input_attrs' => array(
	                'min' => 14,
	                'max' => 90,
	                'step' => 1,
	            ),
	        ),
	        array(
	            'control'  => 'Tmpcoder_Slider_Customize_Control',
	            'setting'  => 'site_fonts_options_mobile_line-height',
	            'default'  => 1.5,
	            'sanitize' => 'tmpcoder_range_sanitization',
	            'label'    => esc_html__( 'Line Height', 'spexo' ),
	            'section'  => 'typography_mobile',
	            'input_attrs' => array(
	                'min' => 1,
	                'max' => 4,
	                'step' => .1,
	            ),
	        ),
	        array(
	            'control'  => 'Tmpcoder_Slider_Customize_Control',
	            'setting'  => 'site_fonts_options_mobile_letter-spacing',
	            'default'  => 0,
	            'sanitize' => 'tmpcoder_range_sanitization',
	            'label'    => esc_html__( 'Letter Spacing (px)', 'spexo' ),
	            'section'  => 'typography_mobile',
	            'input_attrs' => array(
	                'min' => 0,
	                'max' => 4,
	                'step' => .1,
	            ),
	        ),
	    );
	    // Site font mobile controls - end

	    // Add H1-H6 controls
	    foreach ( range( 1, 6 ) as $heading_level ) {
	        $controls = array_merge( $controls, array(
	        	array(
		            'control'  => 'Tmpcoder_Divider_Customize_Control',
		            'setting'  => "heading_{$heading_level}_mobile_divider",
		            'default'  => '',
		            'sanitize' => '',
		            'label'    => '',
		            'section'  => 'typography_mobile',
		            'transport' => 'postMessage',
		            'input_attrs' => array(
		                'width' => 'full',
		                'type' => 'solid',
		                'margintop' => 20,
		                'marginbottom' => 30,
		            ),
		        ),
		        array(
		            'control'  => 'Tmpcoder_Notice_Customize_Control',
		            'setting'  => "heading_{$heading_level}_mobile_title",
		            'default'  => '',
		            'sanitize' => 'tmpcoder_text_sanitization',
					/* translators: %s: This is the heading level */
		            'label'    => sprintf(esc_html__('Heading %s Font', 'spexo'), $heading_level),
		            'section'  => 'typography_mobile',
		            'transport' => 'postMessage',
		        ),
		        array(
		            'control'  => 'Tmpcoder_Slider_Customize_Control',
		            'setting'  => "heading_{$heading_level}_mobile_font-size",
		            'default'  => 36,
		            'sanitize' => 'tmpcoder_range_sanitization',
		            'label'    => esc_html__( 'Font Size (px)', 'spexo' ),
		            'section'  => 'typography_mobile',
		            'input_attrs' => array(
		                'min' => 16,
		                'max' => 90,
		                'step' => 1,
		            ),
		        ),
		        array(
		            'control'  => 'Tmpcoder_Slider_Customize_Control',
		            'setting'  => "heading_{$heading_level}_mobile_line-height",
		            'default'  => 1.5,
		            'sanitize' => 'tmpcoder_range_sanitization',
		            'label'    => esc_html__( 'Line Height', 'spexo' ),
		            'section'  => 'typography_mobile',
		            'input_attrs' => array(
		                'min' => 1,
		                'max' => 4,
		                'step' => .1,
		            ),
		        ),
		        array(
		            'control'  => 'Tmpcoder_Slider_Customize_Control',
		            'setting'  => "heading_{$heading_level}_mobile_letter-spacing",
		            'default'  => 0,
		            'sanitize' => 'tmpcoder_range_sanitization',
		            'label'    => esc_html__( 'Letter Spacing (px)', 'spexo' ),
		            'section'  => 'typography_mobile',
		            'input_attrs' => array(
		                'min' => 0,
		                'max' => 4,
		                'step' => .1,
		            ),
		        ),
	        ) );
	    }


	    // Loop through the controls array and add settings and controls
	    foreach ( $controls as $control ) {
	        $wp_customize->add_setting(
	            $control['setting'],
	            array(
	                'default'           => $control['default'],
					'transport' 		=> isset($control['transport']) ? $control['transport'] : 'postMessage',
	                'sanitize_callback' => $control['sanitize'],
	            )
	        );

	        $control_class = $control['control'];
	        $control_args = array(
	            'label'       => $control['label'],
	            'section'     => $control['section'],
	        );

	        // Merge input attributes if provided
	        if ( isset( $control['input_attrs'] ) ) {
	            $control_args['input_attrs'] = $control['input_attrs'];
	        }

	        $wp_customize->add_control(
	            new $control_class(
	                $wp_customize,
	                $control['setting'],
	                $control_args
	            )
	        );
	    }
	}

	/**
	 * Register Buttons Desktop controls
	 */
	public function tmpcoder_buttons_desktop_controls( $wp_customize ) {

	    $controls = array(
	        array(
	            'control'  => 'Tmpcoder_Notice_Customize_Control',
	            'setting'  => 'button_style_title',
	            'default'  => '',
	            'sanitize' => 'tmpcoder_text_sanitization',
	            'label'    => esc_html__( 'Button Style', 'spexo' ),
	            'section'  => 'buttons_desktop',
	            'transport' => 'postMessage',
	        ),
	        array(
	            'control'  => 'Tmpcoder_Google_Font_Select_Customize_Control',
	            'setting'  => 'button_style_font-family',
	            'default'  => json_encode(
					array(
						'font' 			=> 'open sans',
						'regularweight' => 'regular',
						'category' 		=> 'sans-serif'
					)
				),
	            'sanitize' => 'tmpcoder_google_font_sanitization',
	            'label'    => '',
	            'description' => esc_html__( 'Font Family', 'spexo' ),
	            'section'  => 'buttons_desktop',
	            'input_attrs' => array(
	                'font_count' => 'all',
	                'orderby' => 'alpha',
	            ),
	        ),
	        array(
                'control'  => 'Tmpcoder_Dropdown_Select2_Customize_Control',
                'setting'  => "button_style_text-align",
                'default'  => 'initial',
                'sanitize' => 'tmpcoder_text_sanitization',
                'label'    => esc_html__( "Text Align", 'spexo' ),
                'section'  => 'buttons_desktop',
                'choices'  => array(
                    'inherit' => esc_html__( 'Inherit', 'spexo' ),
                    'left'    => esc_html__( 'Left', 'spexo' ),
                    'right'   => esc_html__( 'Right', 'spexo' ),
                    'center'  => esc_html__( 'Center', 'spexo' ),
                    'justify' => esc_html__( 'Justify', 'spexo' ),
                    'initial' => esc_html__( 'Initial', 'spexo' ),
                ),
            ),
	        array(
                'control'  => 'Tmpcoder_Dropdown_Select2_Customize_Control',
                'setting'  => "button_style_text-transform",
                'default'  => 'initial',
                'sanitize' => 'tmpcoder_text_sanitization',
                'label'    => esc_html__( "Text Transform", 'spexo' ),
                'section'  => 'buttons_desktop',
                'choices'  => array(
                    'none'       => esc_html__( 'None', 'spexo' ),
                    'capitalize' => esc_html__( 'Capitalize', 'spexo' ),
                    'uppercase'  => esc_html__( 'Uppercase', 'spexo' ),
                    'lowercase'  => esc_html__( 'Lowercase', 'spexo' ),
                    'initial'    => esc_html__( 'Initial', 'spexo' ),
                    'inherit'    => esc_html__( 'Inherit', 'spexo' ),
                ),
            ),
	        array(
                'control'  => 'Tmpcoder_Slider_Customize_Control',
                'setting'  => "button_style_font-size",
                'default'  => 36,
                'sanitize' => 'tmpcoder_range_sanitization',
                'label'    => esc_html__( "Font Size (px)", 'spexo' ),
                'section'  => 'buttons_desktop',
                'input_attrs' => array(
                    'min' => 16,
                    'max' => 90,
                    'step' => 1,
                ),
            ),
	        array(
                'control'  => 'Tmpcoder_Slider_Customize_Control',
                'setting'  => "button_style_line-height",
                'default'  => 24,
                'sanitize' => 'tmpcoder_range_sanitization',
                'label'    => esc_html__( "Line Height", 'spexo' ),
                'section'  => 'buttons_desktop',
                'input_attrs' => array(
                    'min' => 1,
                    'max' => 30,
                    'step' => 1,
                ),
            ),
            array(
                'control'  => 'Tmpcoder_Slider_Customize_Control',
                'setting'  => "button_style_letter-spacing",
                'default'  => 0,
                'sanitize' => 'tmpcoder_range_sanitization',
                'label'    => esc_html__( "Letter Spacing (px)", 'spexo' ),
                'section'  => 'buttons_desktop',
                'input_attrs' => array(
                    'min' => 0,
                    'max' => 4,
                    'step' => 0.1,
                ),
            ),
            array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'button_style_color',
	            'default'  => '#ffffff',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => '',
	            'description' => esc_html__( 'Font Color', 'spexo' ),
	            'section'  => 'buttons_desktop',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
	        // Set your theme site button font hover color control
        	array(
	            'control'  => 'Tmpcoder_Divider_Customize_Control',
	            'setting'  => "site_button_text_hover_divider",
	            'default'  => '',
	            'sanitize' => '',
	            'label'    => '',
	            'section'  => 'buttons_desktop',
	            'transport' => 'postMessage',
	            'input_attrs' => array(
	                'width' => 'half',
	                'type' => 'solid',
	                'margintop' => -5,
	                'marginbottom' => -5,
	            ),
	        ),
	        array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'site_button_text_hover',
	            'default'  => '#5729d9',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Site Button Font Hover Color", 'spexo' ),
	            'description' => esc_html__( 'Set your theme site button font hover color', 'spexo' ),
	            'section'  => 'buttons_desktop',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
	       	// Site Button Background Color control
	        array(
	            'control'  => 'Tmpcoder_Divider_Customize_Control',
	            'setting'  => "site_button_color_divider",
	            'default'  => '',
	            'sanitize' => '',
	            'label'    => '',
	            'section'  => 'buttons_desktop',
	            'transport' => 'postMessage',
	            'input_attrs' => array(
	                'width' => 'full',
	                'type' => 'solid',
	                'margintop' => 20,
	                'marginbottom' => 30,
	            ),
	        ),
	        array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'site_button_color',
	            'default'  => '#5729d9',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Site Button Background Color", 'spexo' ),
	            'description' => esc_html__( 'Set your theme site button background color', 'spexo' ),
	            'section'  => 'buttons_desktop',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
	        // Site Button Background Hover Color control
	        array(
	            'control'  => 'Tmpcoder_Divider_Customize_Control',
	            'setting'  => "site_button_color_hover_divider",
	            'default'  => '',
	            'sanitize' => '',
	            'label'    => '',
	            'section'  => 'buttons_desktop',
	            'transport' => 'postMessage',
	            'input_attrs' => array(
	                'width' => 'half',
	                'type' => 'solid',
	                'margintop' => -5,
	                'marginbottom' => -5,
	            ),
	        ),
	        array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'site_button_color_hover',
	            'default'  => '#ffffff',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => esc_html__( "Site Button Background Hover Color", 'spexo' ),
	            'description' => esc_html__( 'Set your theme site button background hover color', 'spexo' ),
	            'section'  => 'buttons_desktop',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
	        // Button Border control
	        array(
	            'control'  => 'Tmpcoder_Divider_Customize_Control',
	            'setting'  => "button_border_divider",
	            'default'  => '',
	            'sanitize' => '',
	            'label'    => '',
	            'section'  => 'buttons_desktop',
	            'transport' => 'postMessage',
	            'input_attrs' => array(
	                'width' => 'full',
	                'type' => 'solid',
	                'margintop' => 20,
	                'marginbottom' => 30,
	            ),
	        ),
            array(
                'control'  => 'Tmpcoder_Dimensional_Customize_Control',
                'setting'  => "button_border",
                'default'  => json_encode(
					array(
						'top' 	 => 5,
						'right'	 => 10,
						'bottom' => 5,
						'left' 	 => 10
					)
                ),
                'sanitize' => 'tmpcoder_dimensions_sanitization',
                'label'    => esc_html__( "Button Border", 'spexo' ),
                'section'  => 'buttons_desktop',
            ),
            array(
                'control'  => 'Tmpcoder_Dropdown_Select2_Customize_Control',
                'setting'  => "button_border_border-style",
                'default'  => 'solid',
                'sanitize' => 'tmpcoder_text_sanitization',
                'label'    => '',
                'section'  => 'buttons_desktop',
                'choices'  => array(
                    'solid'  => esc_html__( 'Solid', 'spexo' ),
                    'dashed' => esc_html__( 'Dashed', 'spexo' ),
                    'dotted' => esc_html__( 'Dotted', 'spexo' ),
                    'double' => esc_html__( 'Double', 'spexo' ),
                    'none'   => esc_html__( 'None', 'spexo' ),
                ),
            ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'button_border_border-color',
	            'default'  => '#5729d9',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => '',
	            'section'  => 'buttons_desktop',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
            // Button Border Hover control
	        array(
	            'control'  => 'Tmpcoder_Divider_Customize_Control',
	            'setting'  => "button_border_hover_divider",
	            'default'  => '',
	            'sanitize' => '',
	            'label'    => '',
	            'section'  => 'buttons_desktop',
	            'transport' => 'postMessage',
	            'input_attrs' => array(
	                'width' => 'half',
	                'type' => 'solid',
	                'margintop' => -5,
	                'marginbottom' => -5,
	            ),
	        ),
            array(
                'control'  => 'Tmpcoder_Dimensional_Customize_Control',
                'setting'  => "button_border_hover",
                'default'  => json_encode(
					array(
						'top' 	 => 5,
						'right'	 => 10,
						'bottom' => 5,
						'left' 	 => 10
					)
                ),
                'sanitize' => 'tmpcoder_dimensions_sanitization',
                'label'    => esc_html__( "Button Border Hover", 'spexo' ),
                'section'  => 'buttons_desktop',
            ),
            array(
                'control'  => 'Tmpcoder_Dropdown_Select2_Customize_Control',
                'setting'  => "button_border_hover_border-style",
                'default'  => 'solid',
                'sanitize' => 'tmpcoder_text_sanitization',
                'label'    => '',
                'section'  => 'buttons_desktop',
                'choices'  => array(
                    'solid'  => esc_html__( 'Solid', 'spexo' ),
                    'dashed' => esc_html__( 'Dashed', 'spexo' ),
                    'dotted' => esc_html__( 'Dotted', 'spexo' ),
                    'double' => esc_html__( 'Double', 'spexo' ),
                    'none'   => esc_html__( 'None', 'spexo' ),
                ),
            ),
			array(
	            'control'  => 'Tmpcoder_Alpha_Color_Control',
	            'setting'  => 'button_border_hover_border-color',
	            'default'  => '#5729d9',
	            'sanitize' => 'tmpcoder_hex_rgba_sanitization',
	            'label'    => '',
	            'section'  => 'buttons_desktop',
	            'input_attrs' => array(
	                'resetalpha' => false,
	                'palette'    => array(
	                    'rgba(99,78,150,1)',
	                    'rgba(67,78,150,1)',
	                    'rgba(34,78,150,.7)',
	                    'rgba(3,78,150,1)',
	                    'rgba(7,110,230,.9)',
	                    'rgba(234,78,150,1)',
	                    'rgba(99,78,150,.5)',
	                    'rgba(190,120,120,.5)',
	                ),
	            ),
	        ),
            // Button border radius control
            array(
	            'control'  => 'Tmpcoder_Divider_Customize_Control',
	            'setting'  => "button_border_radius_divider",
	            'default'  => '',
	            'sanitize' => '',
	            'label'    => '',
	            'section'  => 'buttons_desktop',
	            'transport' => 'postMessage',
	            'input_attrs' => array(
	                'width' => 'full',
	                'type' => 'solid',
	                'margintop' => 20,
	                'marginbottom' => 30,
	            ),
	        ),
            array(
                'control'  => 'Tmpcoder_Slider_Customize_Control',
                'setting'  => "button_border_radius",
                'default'  => 0,
                'sanitize' => 'tmpcoder_range_sanitization',
                'label'    => esc_html__( "Button Border Radius", 'spexo' ),
                // 'description' => esc_html__( 'if set Border Radius - 0 then Border Radius unset', 'spexo' ),
                'section'  => 'buttons_desktop',
                'input_attrs' => array(
                    'min' => 0,
                    'max' => 100,
                    'step' => 1,
                ),
            ),
            // Button padding control
            array(
	            'control'  => 'Tmpcoder_Divider_Customize_Control',
	            'setting'  => "button_padding_divider",
	            'default'  => '',
	            'sanitize' => '',
	            'label'    => '',
	            'section'  => 'buttons_desktop',
	            'transport' => 'postMessage',
	            'input_attrs' => array(
	                'width' => 'full',
	                'type' => 'solid',
	                'margintop' => 20,
	                'marginbottom' => 30,
	            ),
	        ),
	        array(
                'control'  => 'Tmpcoder_Dimensional_Customize_Control',
                'setting'  => "button_padding",
                'default'  => json_encode(
					array(
						'top' 	 => 5,
						'right'	 => 10,
						'bottom' => 5,
						'left' 	 => 10
					)
                ),
                'sanitize' => 'tmpcoder_dimensions_sanitization',
                'label'    => esc_html__( "Button Padding", 'spexo' ),
                'section'  => 'buttons_desktop',
            ),
	    );

	    // Loop through the controls array and add settings and controls
	    foreach ( $controls as $control ) {
	        $wp_customize->add_setting(
	            $control['setting'],
	            array(
	                'default'           => $control['default'],
					'transport' 		=> isset($control['transport']) ? $control['transport'] : 'postMessage',
	                'sanitize_callback' => $control['sanitize'],
	            )
	        );

	        $control_class = $control['control'];
	        $control_args = array(
			    'label'       => $control['label'],
			    'section'     => $control['section'],
			);

			if ( isset( $control['description'] ) ) {
			    $control_args['description'] = $control['description'];
			}

			if ( isset( $control['input_attrs'] ) ) {
			    $control_args['input_attrs'] = $control['input_attrs'];
			}
			
			if ( isset( $control['choices'] ) ) {
			    $control_args['choices'] = $control['choices'];
			}

			$wp_customize->add_control(
                new $control_class(
                    $wp_customize,
                    $control['setting'],
                    $control_args
                )
            );
	    }
	}
	
	/**
	 * Register Buttons Tablet controls
	 */
	public function tmpcoder_buttons_tablet_controls( $wp_customize ) {

	    // Define common settings and controls dynamically in an array
	    $controls = array(
	        array(
	            'control'  => 'Tmpcoder_Notice_Customize_Control',
	            'setting'  => 'button_style_tablet_options_title',
	            'default'  => '',
	            'sanitize' => 'tmpcoder_text_sanitization',
	            'label'    => esc_html__( 'Button Style', 'spexo' ),
	            'section'  => 'buttons_tablet',
	            'transport' => 'postMessage',
	        ),
			array(
                'control'  => 'Tmpcoder_Slider_Customize_Control',
                'setting'  => "button_style_tablet_font-size",
                'default'  => 36,
                'sanitize' => 'tmpcoder_range_sanitization',
                'label'    => esc_html__( "Font Size (px)", 'spexo' ),
                'section'  => 'buttons_tablet',
                'input_attrs' => array(
                    'min' => 16,
                    'max' => 90,
                    'step' => 1,
                ),
            ),
			array(
                'control'  => 'Tmpcoder_Slider_Customize_Control',
                'setting'  => "button_style_tablet_line-height",
                'default'  => 1.5,
                'sanitize' => 'tmpcoder_range_sanitization',
                'label'    => esc_html__( "Line Height", 'spexo' ),
                'section'  => 'buttons_tablet',
                'input_attrs' => array(
                    'min' => 1,
                    'max' => 4,
                    'step' => 0.1,
                ),
            ),
			array(
                'control'  => 'Tmpcoder_Slider_Customize_Control',
                'setting'  => "button_style_tablet_letter-spacing",
                'default'  => 0,
                'sanitize' => 'tmpcoder_range_sanitization',
                'label'    => esc_html__( "Letter Spacing (px)", 'spexo' ),
                'section'  => 'buttons_tablet',
                'input_attrs' => array(
                    'min' => 0,
                    'max' => 4,
                    'step' => 0.1,
                ),
            ),
			// Button padding tablet control
	        array(
	            'control'  => 'Tmpcoder_Divider_Customize_Control',
	            'setting'  => "button_padding_tablet_divider",
	            'default'  => '',
	            'sanitize' => '',
	            'label'    => '',
	            'section'  => 'buttons_tablet',
	            'input_attrs' => array(
	                'width' => 'full',
	                'type' => 'solid',
	                'margintop' => 20,
	                'marginbottom' => 30,
	            ),
	        ),
            array(
                'control'  => 'Tmpcoder_Dimensional_Customize_Control',
                'setting'  => "button_padding_tablet",
                'default'  => json_encode(
					array(
						'top' 	 => 5,
						'right'	 => 10,
						'bottom' => 5,
						'left' 	 => 10
					)
                ),
                'sanitize' => 'tmpcoder_dimensions_sanitization',
                'label'    => esc_html__( "Button Padding", 'spexo' ),
                'section'  => 'buttons_tablet',
            ),
	    );

	    // Loop through the controls array and add settings and controls
	    foreach ( $controls as $control ) {
	        $wp_customize->add_setting(
	            $control['setting'],
	            array(
	                'default'           => $control['default'],
					'transport' 		=> isset($control['transport']) ? $control['transport'] : 'postMessage',
	                'sanitize_callback' => $control['sanitize'],
	            )
	        );

	        $control_class = $control['control'];
	        $control_args = array(
			    'label'       => $control['label'],
			    'section'     => $control['section'],
			);

			if ( isset( $control['description'] ) ) {
			    $control_args['description'] = $control['description'];
			}

			if ( isset( $control['input_attrs'] ) ) {
			    $control_args['input_attrs'] = $control['input_attrs'];
			}

			if ( isset( $control['choices'] ) ) {
			    $control_args['choices'] = $control['choices'];
			}

			$wp_customize->add_control(
                new $control_class(
                    $wp_customize,
                    $control['setting'],
                    $control_args
                )
            );
	    }
	}

	/**
	* Register Buttons mobile controls
	*/
	public function tmpcoder_buttons_mobile_controls( $wp_customize ) {

		// Define common settings and controls dynamically in an array
		$controls = array(
			array(
				'control'  => 'Tmpcoder_Notice_Customize_Control',
				'setting'  => 'button_style_mobile_options_title',
				'default'  => '',
				'sanitize' => 'tmpcoder_text_sanitization',
				'label'    => esc_html__( 'Button Style', 'spexo' ),
				'section'  => 'buttons_mobile',
				'transport' => 'postMessage',
			),
			array(
				'control'  => 'Tmpcoder_Slider_Customize_Control',
				'setting'  => "button_style_mobile_font-size",
				'default'  => 36,
				'sanitize' => 'tmpcoder_range_sanitization',
				'label'    => esc_html__( "Font Size (px)", 'spexo' ),
				'section'  => 'buttons_mobile',
				'input_attrs' => array(
					'min' => 16,
					'max' => 90,
					'step' => 1,
				),
			),
			array(
				'control'  => 'Tmpcoder_Slider_Customize_Control',
				'setting'  => "button_style_mobile_line-height",
				'default'  => 1.5,
				'sanitize' => 'tmpcoder_range_sanitization',
				'label'    => esc_html__( "Line Height", 'spexo' ),
				'section'  => 'buttons_mobile',
				'input_attrs' => array(
					'min' => 1,
					'max' => 4,
					'step' => 0.1,
				),
			),
			array(
				'control'  => 'Tmpcoder_Slider_Customize_Control',
				'setting'  => "button_style_mobile_letter-spacing",
				'default'  => 0,
				'sanitize' => 'tmpcoder_range_sanitization',
				'label'    => esc_html__( "Letter Spacing (px)", 'spexo' ),
				'section'  => 'buttons_mobile',
				'input_attrs' => array(
					'min' => 0,
					'max' => 4,
					'step' => 0.1,
				),
			),
			// Button padding mobile control
			array(
				'control'  => 'Tmpcoder_Divider_Customize_Control',
				'setting'  => "button_padding_mobile_divider",
				'default'  => '',
				'sanitize' => '',
				'label'    => '',
				'section'  => 'buttons_mobile',
				'input_attrs' => array(
					'width' => 'full',
					'type' => 'solid',
					'margintop' => 20,
					'marginbottom' => 30,
				),
			),
			array(
				'control'  => 'Tmpcoder_Dimensional_Customize_Control',
				'setting'  => "button_padding_mobile",
				'default'  => json_encode(
					array(
						'top' 	 => 5,
						'right'	 => 10,
						'bottom' => 5,
						'left' 	 => 10
					)
                ),
				'sanitize' => 'tmpcoder_dimensions_sanitization',
				'label'    => esc_html__( "Button Padding", 'spexo' ),
				'section'  => 'buttons_mobile',
			),
		);

		// Loop through the controls array and add settings and controls
		foreach ( $controls as $control ) {
			$wp_customize->add_setting(
				$control['setting'],
				array(
					'default'           => $control['default'],
					'transport' 		=> isset($control['transport']) ? $control['transport'] : 'postMessage',
					'sanitize_callback' => $control['sanitize'],
				)
			);

			$control_class = $control['control'];
			$control_args = array(
				'label'       => $control['label'],
				'section'     => $control['section'],
			);

			if ( isset( $control['description'] ) ) {
				$control_args['description'] = $control['description'];
			}

			if ( isset( $control['input_attrs'] ) ) {
				$control_args['input_attrs'] = $control['input_attrs'];
			}

			if ( isset( $control['choices'] ) ) {
				$control_args['choices'] = $control['choices'];
			}

			$wp_customize->add_control(
				new $control_class(
					$wp_customize,
					$control['setting'],
					$control_args
				)
			);
		}
	}

	/**
	 * Register footer controls
	 */
	public function tmpcoder_footer_controls( $wp_customize ) {

		$wp_customize->add_setting( 'footer_copyright',
			array(
				'default' => wp_kses_post( __( 'Copyright [COPYRIGHT] [CURRENT_YEAR] [SITE_TITLE] | Powered by <a href="https://spexoaddons.com/" target="_blank">Spexo Wordpress Theme</a>', 'spexo' ) ),
				// 'transport' => 'postMessage',
				'transport' => 'refresh',
				'sanitize_callback' => 'wp_kses_post'
			)
		);
		$wp_customize->add_control( new Tmpcoder_TinyMCE_Customize_Control( $wp_customize, 'footer_copyright',
			array(
				'label' => esc_html__( 'Footer Copyright', 'spexo' ),
				'description' => wp_kses_post( __( 'Footer copyright text is shown by default. <a href="https://wordpress.org/plugins/sastra-essential-addons-for-elementor/" target="_blank">Install Spexo Addons</a> to customize it via <a href="https://spexoaddons.com/documentation/custom-footer/" target="_blank">Site Builder > Footer</a>.', 'spexo' ) ),
				'section' => 'footer_options',
				'input_attrs' => array(
					'toolbar1' => 'bold italic bullist numlist alignleft aligncenter alignright link',
					'toolbar2' => '',
					'mediaButtons' => true,
				)
			)
		) );
	}

    
    function tmpcoder_site_identify_logo_show_controls( $wp_customize ) {
        
        // Add a setting to show or hide the logo
        $wp_customize->add_setting( 'logo_display', array(
            'default'           => true, // Default checked
            'sanitize_callback' => 'tmpcoder_sanitize_boolean',
            'transport' => 'refresh',
        ) );
    
        // Add the checkbox control
        $wp_customize->add_control( 'logo_display', array(
            'type'        => 'checkbox',
            'section'     => 'title_tagline', // Site Identity section
            'label'       => __( 'Display Site Logo', 'spexo' ),
            'description' => __( 'Show or hide the site logo in the header.', 'spexo' ),
        ) );
    }
}

/**
 * Load all Customizer Controls
 */
get_template_part('inc/admin/customizer/tmpcoder-customizer-controls');

/**
 * Functions which enhance the cutomizer
 */
get_template_part('inc/admin/customizer/tmpcoder-customizer-functions');

/**
 * Initialise Customizer settings
 */
$tmpcoder_settings = new Tmpcoder_initialise_customizer_settings();
