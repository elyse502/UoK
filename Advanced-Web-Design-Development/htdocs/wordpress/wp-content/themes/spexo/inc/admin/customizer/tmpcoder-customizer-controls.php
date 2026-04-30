<?php
/**
 * Tmpcoder Customizer Controls
 */

if ( class_exists( 'WP_Customize_Control' ) ) {

	/**
	 * WPColorPicker Alpha Color Picker Custom Control
	 */
	class Tmpcoder_Alpha_Color_Control extends WP_Customize_Control {
		/**
		 * The type of control being rendered
		 */
		public $type = 'wpcolorpicker-alpha-color';
		/**
		 * ColorPicker Attributes
		 */
		public $attributes = "";
		/**
		 * Color palette defaults
		 */
		public $defaultPalette = array(
			'#000000',
			'#ffffff',
			'#dd3333',
			'#dd9933',
			'#eeee22',
			'#81d742',
			'#1e73be',
			'#8224e3',
		);
		/**
		 * Constructor
		 */
		public function __construct( $manager, $id, $args = array(), $options = array() ) {
			parent::__construct( $manager, $id, $args );
			$this->attributes .= 'data-default-color="' . esc_attr( $this->value() ) . '"';
			$this->attributes .= 'data-alpha="true"';
			$this->attributes .= 'data-alpha-reset="' . ( isset( $this->input_attrs['resetalpha'] ) ? $this->input_attrs['resetalpha'] : 'true' ) . '"';
			$this->attributes .= 'data-alpha-custom-width="0"';
			$this->attributes .= 'data-alpha-enabled="true"';
		}
		/**
		 * Enqueue our scripts and styles
		 */
		public function enqueue() {
			wp_enqueue_script( 'wp-color-picker-alpha', TMPCODER_CUSTOMIZER_ASSETS . 'js/wp-color-picker-alpha'.tmpcoder_min_suffix().'.js', array( 'wp-color-picker' ), TMPCODER_THEME_CORE_VERSION, true );

			wp_enqueue_script( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'js/tmpcoder-customizer'.tmpcoder_min_suffix().'.js', array( 'jquery', 'wp-color-picker-alpha' ), TMPCODER_THEME_CORE_VERSION, true );

			wp_enqueue_style( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'css/tmpcoder-customizer'.tmpcoder_min_suffix().'.css', array(), TMPCODER_THEME_CORE_VERSION, 'all' );

			wp_enqueue_style( 'wp-color-picker' );
		}
		/**
		 * Pass our Palette colours to JavaScript
		 */
		public function to_json() {
			parent::to_json();
			$this->json['colorpickerpalette'] = isset( $this->input_attrs['palette'] ) ? $this->input_attrs['palette'] : $this->defaultPalette;
		}
		/**
		 * Render the control in the customizer
		 */
		public function render_content() {
		?>
		  <div class="wpcolorpicker_alpha_color_control">
				<?php if( !empty( $this->label ) ) { ?>
					<span class="customize-control-title"><?php echo esc_html( $this->label ); ?></span>
				<?php } ?>
				<?php if( !empty( $this->description ) ) { ?>
					<span class="customize-control-description"><?php echo esc_html( $this->description ); ?></span>
				<?php } ?>
				<input type="text" class="wpcolorpicker-alpha-color-picker color-picker" id="<?php echo esc_attr( $this->id ); ?>" name="<?php echo esc_attr( $this->id ); ?>" value="<?php echo esc_attr( $this->value() ); ?>" <?php echo wp_kses_post($this->attributes); ?> <?php $this->link(); ?> />
			</div>
		<?php
		}
	}

	/**
	 * Slider Customize Control
	 */
	class Tmpcoder_Slider_Customize_Control extends WP_Customize_Control {
		/**
		 * The type of control being rendered
		 */
		public $type = 'slider_control';
		/**
		 * Enqueue our scripts and styles
		 */
		public function enqueue() {
			wp_enqueue_script( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'js/tmpcoder-customizer'.tmpcoder_min_suffix().'.js', array( 'jquery', 'jquery-ui-core' ), TMPCODER_THEME_CORE_VERSION, true );

			wp_enqueue_style( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'css/tmpcoder-customizer'.tmpcoder_min_suffix().'.css', array(), TMPCODER_THEME_CORE_VERSION, 'all' );
		}
		/**
		 * Render the control in the customizer
		 */
		public function render_content() {
		?>
			<div class="slider-custom-control">
				<span class="customize-control-title"><?php echo esc_html( $this->label ); ?></span><input type="number" id="<?php echo esc_attr( $this->id ); ?>" name="<?php echo esc_attr( $this->id ); ?>" value="<?php echo esc_attr( $this->value() ); ?>" class="customize-control-slider-value" <?php $this->link(); ?> />
				<div class="slider" slider-min-value="<?php echo esc_attr( $this->input_attrs['min'] ); ?>" slider-max-value="<?php echo esc_attr( $this->input_attrs['max'] ); ?>" slider-step-value="<?php echo esc_attr( $this->input_attrs['step'] ); ?>"></div><span class="slider-reset dashicons dashicons-image-rotate" slider-reset-value="<?php echo esc_attr( $this->value() ); ?>"></span>
			</div>
		<?php
		}
	}

	/**
	 * Image Radio Button Customize Control
	 */
	class Tmpcoder_Image_Radio_Button_Customize_Control extends WP_Customize_Control {
		/**
		 * The type of control being rendered
		 */
		public $type = 'image_radio_button';
		/**
		 * Enqueue our scripts and styles
		 */
		public function enqueue() {
			wp_enqueue_style( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'css/tmpcoder-customizer'.tmpcoder_min_suffix().'.css', array(), TMPCODER_THEME_CORE_VERSION, 'all' );
		}
		/**
		 * Render the control in the customizer
		 */
		public function render_content() {
		?>
			<div class="image_radio_button_control">
				<?php if( !empty( $this->label ) ) { ?>
					<span class="customize-control-title"><?php echo esc_html( $this->label ); ?></span>
				<?php } ?>
				<?php if( !empty( $this->description ) ) { ?>
					<span class="customize-control-description"><?php echo esc_html( $this->description ); ?></span>
				<?php } ?>

				<?php foreach ( $this->choices as $key => $value ) { ?>
					<label class="radio-button-label">
						<input type="radio" name="<?php echo esc_attr( $this->id ); ?>" value="<?php echo esc_attr( $key ); ?>" <?php $this->link(); ?> <?php checked( esc_attr( $key ), $this->value() ); ?>/>
						<img src="<?php echo esc_attr( $value['image'] ); ?>" alt="<?php echo esc_attr( $value['name'] ); ?>" title="<?php echo esc_attr( $value['name'] ); ?>" />
					</label>
				<?php	} ?>
			</div>
		<?php
		}
	}

	/**
	 * Upsell section
	 */
	class Tmpcoder_Upsell_Section extends WP_Customize_Section {
		/**
		 * The type of control being rendered
		 */
		public $type = 'tmpcoder-pro';
		/**
		 * The Upsell URL
		 */
		public $url = '';
		/**
		 * The background color for the control
		 */
		public $backgroundcolor = '';
		/**
		 * The text color for the control
		 */
		public $textcolor = '';
		/**
		 * Enqueue our scripts and styles
		 */
		public function enqueue() {
			wp_enqueue_script( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'js/tmpcoder-customizer'.tmpcoder_min_suffix().'.js', array( 'jquery' ), TMPCODER_THEME_CORE_VERSION, true );

			wp_enqueue_style( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'css/tmpcoder-customizer'.tmpcoder_min_suffix().'.css', array(), TMPCODER_THEME_CORE_VERSION, 'all' );
		}
		/**
		 * Render the section, and the controls that have been added to it.
		 */
		protected function render() {
			$bkgrndcolor = !empty( $this->backgroundcolor ) ? esc_attr( $this->backgroundcolor ) : '#fff';
			$color = !empty( $this->textcolor ) ? esc_attr( $this->textcolor ) : '#5729d9';
			?>
			<li id="accordion-section-<?php echo esc_attr( $this->id ); ?>" class="tmpcoder_pro_section accordion-section control-section control-section-<?php echo esc_attr( $this->id ); ?> cannot-expand">
				<h3 class="tmpcoder-pro-section-title" <?php echo ' style="color:' . esc_html( $color ) . ';border-left-color:' . esc_html( $bkgrndcolor ) .';border-right-color:' . esc_html( $bkgrndcolor ) .';"'; ?>>
					<a href="<?php echo esc_url( $this->url); ?>" target="_blank"><?php echo esc_html( $this->title ); ?></a>
				</h3>
			</li>
			<?php
		}
	}

	/**
	 * Divider Control
	 */
	class Tmpcoder_Divider_Customize_Control extends WP_Customize_Control {
		/**
		 * The type of control being rendered
		 */
		public $type = 'simple_divider';
		/**
		 * Define the available widths for the divider
		 */
		private $available_divider_widths = array( "default", "full", "half" );
		/**
		 * Define the available types of divider
		 */
		private $available_divider_types = array( "solid", "dashed", "dotted", "double" );
		/**
		 * Define the width of the divider. Either 'default', 'full', or 'half'. Default = 'default'
		 */
		private $dividerwidth = 'default';
		/**
		 * Define the type of divider line. Either 'solid', 'dashed' or 'dotted'. Default = 'solid'
		 */
		private $dividertype = 'solid';
		/**
		 * Define size of the top margin in px. Default = 20
		 */
		private $margintop = 20;
		/**
		 * Define size of the top margin in px. Default = 20
		 */
		private $marginbottom = 20;
		/**
		 * Constructor
		 */
		public function __construct( $manager, $id, $args = array(), $options = array() ) {
			parent::__construct( $manager, $id, $args );
			// Check the width of the divider
			if ( isset( $this->input_attrs['width'] ) ) {
				if ( in_array( strtolower( $this->input_attrs['width'] ), $this->available_divider_widths, true ) ) {
					$this->dividerwidth = strtolower( $this->input_attrs['width'] );
				}
			}
			// Check the type of divider
			if ( isset( $this->input_attrs['type'] ) ) {
				if ( in_array( strtolower( $this->input_attrs['type'] ), $this->available_divider_types, true ) ) {
					$this->dividertype = strtolower( $this->input_attrs['type'] );
				}
			}
			// Check if the top margin is specified and valid. Will accept int and string values. i.e. 42 or '42'
			if ( isset( $this->input_attrs['margintop'] ) &&  is_numeric( $this->input_attrs['margintop'] ) ) {
				$this->margintop = abs( (int)$this->input_attrs['margintop'] );
			}
			// Check if the bottom margin is specified and valid. Will accept int and string values. i.e. 42 or '42'
			if ( isset( $this->input_attrs['marginbottom'] ) &&  is_numeric( $this->input_attrs['marginbottom'] ) ) {
				$this->marginbottom = abs( (int)$this->input_attrs['marginbottom'] );
			}
		}
		/**
		 * Enqueue our scripts and styles
		 */
		public function enqueue() {
			wp_enqueue_style( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'css/tmpcoder-customizer'.tmpcoder_min_suffix().'.css', array(), TMPCODER_THEME_CORE_VERSION, 'all' );
		}
		/**
		 * Render the control in the customizer
		 */
		public function render_content() {
		?>
			<div class="simple-divider-custom-control simple-divider-type-<?php echo esc_attr($this->dividertype); ?> simple-divider-width-<?php echo esc_attr($this->dividerwidth); ?>" style="margin-top:<?php echo esc_attr($this->margintop); ?>px;margin-bottom:<?php echo esc_attr($this->marginbottom); ?>px"></div>
		<?php
		}
	}


	/**
    * Google Font Select Customize Control
    */
    class Tmpcoder_Google_Font_Select_Customize_Control extends WP_Customize_Control {
        /**
        * The type of control being rendered
        */
        public $type = 'google_fonts';
        /**
        * The list of Google Fonts
        */
        private $fontList = false;
        /**
        * The saved font values decoded from json
        */
        private $fontValues = array();
        /**
        * The index of the saved font within the list of Google fonts
        */
        private $fontListIndex = 0;
        /**
        * The number of fonts to display from the json file. Either positive integer or 'all'. Default = 'all'
        */
        private $fontCount = 'all';
        /**
        * The font list sort order. Either 'alpha' or 'popular'. Default = 'alpha'
        */
        private $fontOrderBy = 'alpha';
        /**
        * Get our list of fonts from the json file
        */
        public function __construct( $manager, $id, $args = array(), $options = array() ) {
            parent::__construct( $manager, $id, $args );
            // Get the font sort order
            if ( isset( $this->input_attrs['orderby'] ) && strtolower( $this->input_attrs['orderby'] ) === 'popular' ) {
                $this->fontOrderBy = 'popular';
            }
            // Get the list of Google fonts
            if ( isset( $this->input_attrs['font_count'] ) ) {
                if ( 'all' != strtolower( $this->input_attrs['font_count'] ) ) {
                    $this->fontCount = ( abs( (int) $this->input_attrs['font_count'] ) > 0 ? abs( (int) $this->input_attrs['font_count'] ) : 'all' );
                }
            }
            $this->fontList = $this->tmpcoder_getGoogleFonts( 'all' );
            // Decode the default json font value
            $this->fontValues = json_decode( $this->value() );
            // Find the index of our default font within our list of Google fonts
            $this->fontListIndex = $this->tmpcoder_getFontIndex( $this->fontList, $this->fontValues->font );
        }
        /**
        * Enqueue our scripts and styles
        */
        public function enqueue() {
            wp_enqueue_script( 'tmpcoder-select2', TMPCODER_CUSTOMIZER_ASSETS . 'js/select2'.tmpcoder_min_suffix().'.js', array( 'jquery' ), TMPCODER_THEME_CORE_VERSION, true );

            wp_enqueue_script( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'js/tmpcoder-customizer'.tmpcoder_min_suffix().'.js', array( 'tmpcoder-select2-js' ), TMPCODER_THEME_CORE_VERSION, true );

            wp_enqueue_style( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'css/tmpcoder-customizer'.tmpcoder_min_suffix().'.css', array(), TMPCODER_THEME_CORE_VERSION, 'all' );

            wp_enqueue_style( 'tmpcoder-select2', TMPCODER_CUSTOMIZER_ASSETS . 'css/select2'.tmpcoder_min_suffix().'.css', array(), TMPCODER_THEME_CORE_VERSION, 'all' );
        }
        /**
        * Export our List of Google Fonts to JavaScript
        */
        public function to_json() {
            parent::to_json();
            $this->json['tmpcoderfontslist'] = $this->fontList;
        }
		/**
		 * Render the control in the customizer
		 */
		public function render_content() {
			$fontCounter = 0;
			$isFontInList = false;
			$fontListStr = '';

			if( !empty($this->fontList) ) {
				?>
				<div class="google_fonts_select_control">
					<?php if( !empty( $this->label ) ) { ?>
						<span class="customize-control-title"><?php echo esc_html( $this->label ); ?></span>
					<?php } ?>
					<?php if( !empty( $this->description ) ) { ?>
						<span class="customize-control-description"><?php echo esc_html( $this->description ); ?></span>
					<?php } ?>
					<input type="hidden" id="<?php echo esc_attr( $this->id ); ?>" name="<?php echo esc_attr( $this->id ); ?>" value="<?php echo esc_attr( $this->value() ); ?>" class="customize-control-google-font-selection" <?php $this->link(); ?> />
					<div class="google-fonts">
						<select class="google-fonts-list" control-name="<?php echo esc_attr( $this->id ); ?>">
							<?php
								foreach( $this->fontList as $key => $value ) {
									$fontCounter++;
									$fontListStr .= '<option value="' . esc_html( $value['font'] ) . '" ' . selected( $this->fontValues->font, $value['font'], false ) . '>' . esc_html( $value['font'] ) . '</option>';
									if ( $this->fontValues->font === $value['font'] ) {
										$isFontInList = true;
									}
									if ( is_int( $this->fontCount ) && $fontCounter === $this->fontCount ) {
										break;
									}
								}
								if ( !$isFontInList && $this->fontListIndex ) {
									// If the default or saved font value isn't in the list of displayed fonts, add it to the top of the list as the default font
									$fontListStr = '<option value="' . esc_html( $this->fontList[$this->fontListIndex]['font'] ) . '" ' . selected( $this->fontValues->font, $this->fontList[$this->fontListIndex]['font'], false ) . '>' . esc_html( $this->fontList[$this->fontListIndex]['font'] ) . ' (default)</option>' . $fontListStr;
								}
								// Display our list of font options
								echo wp_kses($fontListStr, array(
									'option' => array(
										'selected' => array(),
										'data-*' => array(),
									)
								));
							?>
						</select>
					</div>
					<div class="customize-control-description"><?php esc_html_e( 'Font Weight & Style', 'spexo' ) ?></div>
					<div class="weight-style">
						<select class="google-fonts-regularweight-style customize-control-select2">
							<?php
								$weightLabels = array(
									"100" => "Thin 100",
									"200" => "Extra Light 200",
									"300" => "Light 300",
									"400" => "Regular 400",
									"500" => "Medium 500",
									"600" => "Semi-Bold 600",
									"700" => "Bold 700",
									"800" => "Extra Bold 800",
									"900" => "Black 900"
								);

								$uniqueVariants = array();
								foreach( $this->fontList[$this->fontListIndex]['variants'] as $key => $value ) {
									// Remove the "i" from the variant (if it's the "italic" variant)
									$cleanedValue = str_replace('i', '', $value);

									if (!in_array($cleanedValue, $uniqueVariants)) {
										$uniqueVariants[] = $cleanedValue;
										$label = isset($weightLabels[$cleanedValue]) ? $weightLabels[$cleanedValue] : $cleanedValue;
										
										echo '<option value="' . esc_html( $cleanedValue ) . '" ' . selected( $this->fontValues->regularweight, $cleanedValue, false ) . '>' . esc_html( $label ) . '</option>';
									}
								}
							?>
						</select>
					</div>

					<input type="hidden" class="google-fonts-category" value="<?php echo esc_attr($this->fontValues->category); ?>">
				</div>
				<?php
			}
		}
        /**
        * Find the index of the saved font in our multidimensional array of Google Fonts
        */
        public function tmpcoder_getFontIndex( $haystack, $needle ) {
            foreach( $haystack as $key => $value ) {
                if( $value['font'] == $needle ) {
                    return $key;
                }
            }
            return false;
        }
		/**
		* Return the list of Google Fonts from the Google Fonts API.
		* This method is updated to handle missing properties and use the correct structure.
		*/
		public function tmpcoder_getGoogleFonts( $count = 30 ) {
			$googleFontsUrl = "https://fonts.google.com/metadata/fonts";

			$args = array(
				'headers' => array(
					'User-Agent' => 'Mozilla/5.0', // Required to bypass Google restrictions
				)
			);

			$request = wp_remote_get( $googleFontsUrl, $args );

			if ( is_wp_error( $request ) ) {
				return "";
			}
			
			$body = wp_remote_retrieve_body( $request );

			// Remove Google's anti-CSRF prefix: ")]}'"
			$body = preg_replace('/^\)\]\}\'/', '', $body);
			$content = json_decode( $body );

			if ( !isset( $content->familyMetadataList ) || !is_array( $content->familyMetadataList ) ) {
				return "";
			}

			$fonts = $content->familyMetadataList;
			$fontList = [];

			foreach ( $fonts as $font ) {
				$fontName = isset( $font->family ) ? $font->family : '';
				$variants = isset( $font->variants ) && is_array( $font->variants ) ? $font->variants : [];
				
				if ( empty( $variants ) && isset( $font->fonts ) && is_object( $font->fonts ) ) {
					$variants = array_keys( get_object_vars( $font->fonts ) );
				}

				if ( empty( $variants ) ) {
					$variants = ['400'];
				}

				$category = isset( $font->category ) ? $font->category : 'sans-serif';

				$fontList[] = [
					'font'          => $fontName,
					'regularweight' => '400',
					'category'      => $category,
					'variants'      => $variants,
					'subsets'       => isset( $font->subsets ) ? $font->subsets : [] 
				];
			}

			return ( $count === 'all' ) ? $fontList : array_slice( $fontList, 0, $count );
		}
    }



	/**
	 * Dropdown Select2 Customize Control
	 */
	class Tmpcoder_Dropdown_Select2_Customize_Control extends WP_Customize_Control {
		/**
		 * The type of control being rendered
		 */
		public $type = 'dropdown_select2';
		/**
		 * The type of Select2 Dropwdown to display. Can be either a single select dropdown or a multi-select dropdown. Either false for true. Default = false
		 */
		private $multiselect = false;
		/**
		 * The Placeholder value to display. Select2 requires a Placeholder value to be set when using the clearall option. Default = 'Please select...'
		 */
		private $placeholder = 'Please select...';
		/**
		 * Constructor
		 */
		public function __construct( $manager, $id, $args = array(), $options = array() ) {
			parent::__construct( $manager, $id, $args );
			// Check if this is a multi-select field
			if ( isset( $this->input_attrs['multiselect'] ) && $this->input_attrs['multiselect'] ) {
				$this->multiselect = true;
			}
			// Check if a placeholder string has been specified
			if ( isset( $this->input_attrs['placeholder'] ) && $this->input_attrs['placeholder'] ) {
				$this->placeholder = $this->input_attrs['placeholder'];
			}
		}
		/**
		 * Enqueue scripts and styles
		 */
		public function enqueue() {
			wp_enqueue_script( 'tmpcoder-select2', TMPCODER_CUSTOMIZER_ASSETS . 'js/select2'.tmpcoder_min_suffix().'.js', array( 'jquery' ), TMPCODER_THEME_CORE_VERSION, true );

			wp_enqueue_script( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'js/tmpcoder-customizer'.tmpcoder_min_suffix().'.js', array( 'tmpcoder-select2-js' ), TMPCODER_THEME_CORE_VERSION, true );

			wp_enqueue_style( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'css/tmpcoder-customizer'.tmpcoder_min_suffix().'.css', array(), TMPCODER_THEME_CORE_VERSION, 'all' );

			wp_enqueue_style( 'tmpcoder-select2', TMPCODER_CUSTOMIZER_ASSETS . 'css/select2'.tmpcoder_min_suffix().'.css', array(), TMPCODER_THEME_CORE_VERSION, 'all' );
		}
		/**
		 * Render the control in the customizer
		 */
		public function render_content() {
			$defaultValue = $this->value();
			if ( $this->multiselect ) {
				$defaultValue = explode( ',', $this->value() );
			}
		?>
			<div class="dropdown_select2_control">
				<?php if( !empty( $this->label ) ) { ?>
					<label for="<?php echo esc_attr( $this->id ); ?>" class="customize-control-title">
						<?php echo esc_html( $this->label ); ?>
					</label>
				<?php } ?>
				<?php if( !empty( $this->description ) ) { ?>
					<span class="customize-control-description"><?php echo esc_html( $this->description ); ?></span>
				<?php } ?>
				<input type="hidden" id="<?php echo esc_attr( $this->id ); ?>" class="customize-control-dropdown-select2" value="<?php echo esc_attr( $this->value() ); ?>" name="<?php echo esc_attr( $this->id ); ?>" <?php $this->link(); ?> />
				<select name="select2-list-<?php echo esc_attr( $this->multiselect ? 'multi[]' : 'single' ); ?>" class="customize-control-select2" data-placeholder="<?php echo esc_attr($this->placeholder); ?>" <?php echo esc_attr( $this->multiselect ? 'multiple="multiple" ' : '' ); ?>>
					<?php
						if ( !$this->multiselect ) {
							// When using Select2 for single selection, the Placeholder needs an empty <option> at the top of the list for it to work (multi-selects dont need this)
							echo '<option></option>';
						}
						foreach ( $this->choices as $key => $value ) {
							if ( is_array( $value ) ) {
								echo '<optgroup label="' . esc_attr( $key ) . '">';
								foreach ( $value as $optgroupkey => $optgroupvalue ) {
									if( $this->multiselect ){
										echo '<option value="' . esc_attr( $optgroupkey ) . '" ' . ( in_array( esc_attr( $optgroupkey ), $defaultValue ) ? 'selected="selected"' : '' ) . '>' . esc_attr( $optgroupvalue ) . '</option>';
									}
									else{
										echo '<option value="' . esc_attr( $optgroupkey ) . '" ' . selected( esc_attr( $optgroupkey ), $defaultValue, false )  . '>' . esc_attr( $optgroupvalue ) . '</option>';
									}
								}
								echo '</optgroup>';
							}
							else {
								if( $this->multiselect ){
									echo '<option value="' . esc_attr( $key ) . '" ' . ( in_array( esc_attr( $key ), $defaultValue ) ? 'selected="selected"' : '' ) . '>' . esc_attr( $value ) . '</option>';
								}
								else{
									echo '<option value="' . esc_attr( $key ) . '" ' . selected( esc_attr( $key ), $defaultValue, false )  . '>' . esc_attr( $value ) . '</option>';
								}
							}
						}
					?>
				</select>
			</div>
		<?php
		}
	}

	/**
	 * Notice Customize Control
	 */
	class Tmpcoder_Notice_Customize_Control extends WP_Customize_Control {
		/**
		 * The type of control being rendered
		 */
		public $type = 'tmpcoder_notice';
		/**
		 * Render the control in the customizer
		 */
		public function render_content() {
			$allowed_html = array(
				'a' => array(
					'href' => array(),
					'title' => array(),
					'class' => array(),
					'target' => array(),
				),
				'br' => array(),
				'em' => array(),
				'strong' => array(),
				'i' => array(
					'class' => array()
				),
				'span' => array(
					'class' => array(),
				),
				'code' => array(),
			);
		?>
			<div class="simple-notice-custom-control">
				<?php if( !empty( $this->label ) ) { ?>
					<span class="customize-control-title"><?php echo esc_html( $this->label ); ?></span>
				<?php } ?>
				<?php if( !empty( $this->description ) ) { ?>
					<span class="customize-control-description"><?php echo wp_kses( $this->description, $allowed_html ); ?></span>
				<?php } ?>
			</div>
		<?php
		}
	}

	/**
	 * Dimensions Customize Control
	 */
	class Tmpcoder_Dimensional_Customize_Control extends WP_Customize_Control {
		public $type = 'dimensional';
	
		public function render_content() {
			$dimensions = array('top', 'right', 'bottom', 'left');
			$saved_values = json_decode($this->value(), true);
	
			?>
			<label>
				<span class="customize-control-title"><?php echo esc_html($this->label); ?></span>
				<div class="dimensional-fields-row">
					<?php foreach ($dimensions as $dimension) : ?>
						<div class="dimensional-field">
							<input 
								type="number" 
								placeholder="<?php echo esc_attr ( ucfirst($dimension) ); ?>" 
								class="dimensional-input" 
								data-dimension="<?php echo esc_attr($dimension); ?>"
								value="<?php echo esc_attr(isset($saved_values[$dimension]) ? $saved_values[$dimension] : ''); ?>"
							/>
							<span class="customize-control-description"><?php echo esc_html( ucfirst($dimension) ); ?></span>
						</div>
					<?php endforeach; ?>
					<input type="hidden" class="dimensional-hidden-value" <?php $this->link(); ?> />
				</div>
			</label>
			<?php
		}
	}

	/**
	 * TinyMCE Customize Control
	 */
	class Tmpcoder_TinyMCE_Customize_Control extends WP_Customize_Control {
		/**
		 * The type of control being rendered
		 */
		public $type = 'tinymce_editor';
		/**
		 * Enqueue our scripts and styles
		 */
		public function enqueue(){
			wp_enqueue_script( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'js/tmpcoder-customizer'.tmpcoder_min_suffix().'.js', array( 'jquery' ), TMPCODER_THEME_CORE_VERSION, true );

			wp_enqueue_style( 'tmpcoder-customize-controls', TMPCODER_CUSTOMIZER_ASSETS . 'css/tmpcoder-customizer'.tmpcoder_min_suffix().'.css', array(), TMPCODER_THEME_CORE_VERSION, 'all' );
			wp_enqueue_editor();
		}
		/**
		 * Pass our TinyMCE toolbar string to JavaScript
		 */
		public function to_json() {
			parent::to_json();
			$this->json['tmpcodertinymcetoolbar1'] = isset( $this->input_attrs['toolbar1'] ) ? esc_attr( $this->input_attrs['toolbar1'] ) : 'bold italic bullist numlist alignleft aligncenter alignright link';
			$this->json['tmpcodertinymcetoolbar2'] = isset( $this->input_attrs['toolbar2'] ) ? esc_attr( $this->input_attrs['toolbar2'] ) : '';
			$this->json['tmpcodermediabuttons'] = isset( $this->input_attrs['mediaButtons'] ) && ( $this->input_attrs['mediaButtons'] === true ) ? true : false;
		}
		/**
		 * Render the control in the customizer
		 */
		public function render_content(){
		?>
			<div class="tinymce-control">
				<span class="customize-control-title"><?php echo esc_html( $this->label ); ?></span>
				<?php if( !empty( $this->description ) ) { ?>
					<span class="customize-control-description"><?php echo wp_kses_post( $this->description ); ?></span>
				<?php } ?>
				<textarea id="<?php echo esc_attr( $this->id ); ?>" class="customize-control-tinymce-editor" <?php $this->link(); ?>><?php echo esc_html( $this->value() ); ?></textarea>
				<span class="customize-control-description"> <?php echo wp_kses_post( 'Shortcode List:<br>[COPYRIGHT] = &copy;<br>[CURRENT_YEAR] = ' . gmdate('Y') . '<br>[SITE_TITLE] = ' . get_bloginfo('name') ); ?></span>
			</div>
		<?php
		}
	}

	/**
	 * Only allow values between a certain minimum & maxmium range
	 *
	 * @param  number	Input to be sanitized
	 * @return number	Sanitized input
	 */
	if ( ! function_exists( ' tmpcoder_in_range' ) ) {
		function tmpcoder_in_range( $input, $min, $max ){
			if ( $input < $min ) {
				$input = $min;
			}
			if ( $input > $max ) {
				$input = $max;
			}
			return $input;
		}
	}

	/**
	 * Slider sanitization
	 *
	 * @param  string	Slider value to be sanitized
	 * @return string	Sanitized input
	 */
	if ( ! function_exists( 'tmpcoder_range_sanitization' ) ) {
		function tmpcoder_range_sanitization( $input, $setting ) {
			$attrs = $setting->manager->get_control( $setting->id )->input_attrs;

			$min = ( isset( $attrs['min'] ) ? $attrs['min'] : $input );
			$max = ( isset( $attrs['max'] ) ? $attrs['max'] : $input );
			$step = ( isset( $attrs['step'] ) ? $attrs['step'] : 1 );

			$number = floor( $input / $attrs['step'] ) * $attrs['step'];

			return tmpcoder_in_range( $number, $min, $max );
		}
	}

	/**
	 * Google Font sanitization
	 *
	 * @param  string	JSON string to be sanitized
	 * @return string	Sanitized input
	 */
	if ( ! function_exists( 'tmpcoder_google_font_sanitization' ) ) {
		function tmpcoder_google_font_sanitization( $input ) {
			$val =  json_decode( $input, true );
			if( is_array( $val ) ) {
				foreach ( $val as $key => $value ) {
					$val[$key] = sanitize_text_field( $value );
				}
				$input = json_encode( $val );
			}
			else {
				$input = json_encode( sanitize_text_field( $val ) );
			}
			return $input;
		}
	}

	/**
	 * Alpha Color (Hex, RGB & RGBa) sanitization
	 *
	 * @param  string	Input to be sanitized
	 * @return string	Sanitized input
	 */
	if ( ! function_exists( 'tmpcoder_hex_rgba_sanitization' ) ) {
		function tmpcoder_hex_rgba_sanitization( $input, $setting ) {
			if ( empty( $input ) || is_array( $input ) ) {
				return $setting->default;
			}

			if ( false === strpos( $input, 'rgb' ) ) {
				// If string doesn't start with 'rgb' then santize as hex color
				$input = sanitize_hex_color( $input );
			} else {
				if ( false === strpos( $input, 'rgba' ) ) {
					// Sanitize as RGB color
					$input = str_replace( ' ', '', $input );
					sscanf( $input, 'rgb(%d,%d,%d)', $red, $green, $blue );
					$input = 'rgb(' . tmpcoder_in_range( $red, 0, 255 ) . ',' . tmpcoder_in_range( $green, 0, 255 ) . ',' . tmpcoder_in_range( $blue, 0, 255 ) . ')';
				}
				else {
					// Sanitize as RGBa color
					$input = str_replace( ' ', '', $input );
					sscanf( $input, 'rgba(%d,%d,%d,%f)', $red, $green, $blue, $alpha );
					$input = 'rgba(' . tmpcoder_in_range( $red, 0, 255 ) . ',' . tmpcoder_in_range( $green, 0, 255 ) . ',' . tmpcoder_in_range( $blue, 0, 255 ) . ',' . tmpcoder_in_range( $alpha, 0, 1 ) . ')';
				}
			}
			return $input;
		}
	}

	/**
	 * Text sanitization
	 *
	 * @param  string	Input to be sanitized (either a string containing a single string or multiple, separated by commas)
	 * @return string	Sanitized input
	 */
	if ( ! function_exists( 'tmpcoder_text_sanitization' ) ) {
		function tmpcoder_text_sanitization( $input ) {
			if ( strpos( $input, ',' ) !== false) {
				$input = explode( ',', $input );
			}
			if( is_array( $input ) ) {
				foreach ( $input as $key => $value ) {
					$input[$key] = sanitize_text_field( $value );
				}
				$input = implode( ',', $input );
			}
			else {
				$input = sanitize_text_field( $input );
			}
			return $input;
		}
	}

	/**
	 * Dimensions sanitization
	 */
	if ( ! function_exists( 'tmpcoder_dimensions_sanitization' ) ) {
		function tmpcoder_dimensions_sanitization( $input ) {
			$val =  json_decode( $input, true );
			if( is_array( $val ) ) {
				foreach ( $val as $key => $value ) {
					$val[$key] = sanitize_text_field( $value );
				}
				$input = json_encode( $val );
			}
			else {
				$input = json_encode( sanitize_text_field( $val ) );
			}
			return $input;
		}
	}

    /**
	 * Boolean sanitization
	 */
    if ( ! function_exists( 'tmpcoder_sanitize_boolean' ) ) {
        function tmpcoder_sanitize_boolean( $value ) {
            return filter_var( $value, FILTER_VALIDATE_BOOLEAN );
        }
    }
}
