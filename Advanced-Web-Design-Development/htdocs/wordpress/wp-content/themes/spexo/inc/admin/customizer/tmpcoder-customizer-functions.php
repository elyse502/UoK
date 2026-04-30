<?php

if ( ! defined( 'ABSPATH' ) ) {
    exit; // Exit if accessed directly.
}

if ( ! function_exists( 'tmpcoder_add_customizer_options' ) ) {

    function tmpcoder_add_customizer_options($redux_data){
        if( ! class_exists('Redux_Framework_Plugin', false) ) {
            $tmpcoderThemeMods = get_theme_mods();
            
            // Container Width Variable Declare
            $redux_data['container_width'] = isset($tmpcoderThemeMods['container_width']) ? $tmpcoderThemeMods['container_width'] : '';
            $redux_data['container_width_tablet'] = isset($tmpcoderThemeMods['container_width_tablet']) ? $tmpcoderThemeMods['container_width_tablet'] : '';
            $redux_data['container_width_mobile'] = isset($tmpcoderThemeMods['container_width_mobile']) ? $tmpcoderThemeMods['container_width_mobile'] : '';

            // Global color variable Declare
            $redux_data['primany_color'] = isset($tmpcoderThemeMods['primary_color']) ? $tmpcoderThemeMods['primary_color'] : '';
            $redux_data['secondary_color'] = isset($tmpcoderThemeMods['secondary_color']) ? $tmpcoderThemeMods['secondary_color'] : '';
            $redux_data['accent_color'] = isset($tmpcoderThemeMods['accent_color']) ? $tmpcoderThemeMods['accent_color'] : '';
            $redux_data['theme_color_1'] = isset($tmpcoderThemeMods['theme_color_1']) ? $tmpcoderThemeMods['theme_color_1'] : '';
            $redux_data['theme_color_2'] = isset($tmpcoderThemeMods['theme_color_2']) ? $tmpcoderThemeMods['theme_color_2'] : '';
            $redux_data['theme_color_3'] = isset($tmpcoderThemeMods['theme_color_3']) ? $tmpcoderThemeMods['theme_color_3'] : '';
            $redux_data['theme_color_4'] = isset($tmpcoderThemeMods['theme_color_4']) ? $tmpcoderThemeMods['theme_color_4'] : '';
            $redux_data['site_background_color'] = isset($tmpcoderThemeMods['site_background_color']) ? $tmpcoderThemeMods['site_background_color'] : '';
            $redux_data['link_color'] = isset($tmpcoderThemeMods['link_color']) ? $tmpcoderThemeMods['link_color'] : '';
            $redux_data['link_hover_color'] = isset($tmpcoderThemeMods['link_hover_color']) ? $tmpcoderThemeMods['link_hover_color'] : '';
            $redux_data['global_border_color'] = isset($tmpcoderThemeMods['border_color']) ? $tmpcoderThemeMods['border_color'] : '';

            // Theme Button Variable Declare For Desktop
            if ( isset($tmpcoderThemeMods['button_style_font-family']) && $tmpcoderThemeMods['button_style_font-family'] != '') {
                $button_fonts = json_decode($tmpcoderThemeMods['button_style_font-family'], true);
                
                $redux_data['button_style']['font-family'] = '"'.$button_fonts['font'].'", '.$button_fonts['category'];
                $redux_data['button_style']['font-weight'] = $button_fonts['regularweight'];
            }
            $redux_data['button_style']['text-align'] = isset($tmpcoderThemeMods['button_style_text-align']) ? $tmpcoderThemeMods['button_style_text-align'] : '';
            $redux_data['button_style']['text-transform'] = isset($tmpcoderThemeMods['button_style_text-transform']) ? $tmpcoderThemeMods['button_style_text-transform'] : '';
            $redux_data['button_style']['font-size'] = isset($tmpcoderThemeMods['button_style_font-size']) ? $tmpcoderThemeMods['button_style_font-size'].'px'  : '';
            $redux_data['button_style']['line-height'] = isset($tmpcoderThemeMods['button_style_line-height']) ? $tmpcoderThemeMods['button_style_line-height']  : '';
            $redux_data['button_style']['letter-spacing'] = isset($tmpcoderThemeMods['button_style_letter-spacing']) ? $tmpcoderThemeMods['button_style_letter-spacing'].'px'  : '';
            $redux_data['button_style']['color'] = isset($tmpcoderThemeMods['button_style_color']) ? $tmpcoderThemeMods['button_style_color']  : '';
            $redux_data['site_button_text_hover'] = isset($tmpcoderThemeMods['site_button_text_hover']) ? $tmpcoderThemeMods['site_button_text_hover']  : '';
            $redux_data['site_button_color'] = isset($tmpcoderThemeMods['site_button_color']) ? $tmpcoderThemeMods['site_button_color']  : '';
            $redux_data['site_button_color_hover'] = isset($tmpcoderThemeMods['site_button_color_hover']) ? $tmpcoderThemeMods['site_button_color_hover']  : '';
            if ( isset($tmpcoderThemeMods['button_border']) && $tmpcoderThemeMods['button_border'] != '') {
                $button_border = json_decode($tmpcoderThemeMods['button_border'], true);
                $redux_data['button_border']['border-top']    = $button_border['top'].'px';
                $redux_data['button_border']['border-right']  = $button_border['right'].'px';
                $redux_data['button_border']['border-bottom'] = $button_border['bottom'].'px';
                $redux_data['button_border']['border-left']   = $button_border['left'].'px';
            }
            $redux_data['button_border']['border-style'] = isset($tmpcoderThemeMods['button_border_border-style']) ? $tmpcoderThemeMods['button_border_border-style']  : '';
            $redux_data['button_border']['border-color'] = isset($tmpcoderThemeMods['button_border_border-color']) ? $tmpcoderThemeMods['button_border_border-color']  : '';
            if ( isset($tmpcoderThemeMods['button_border_hover']) && $tmpcoderThemeMods['button_border_hover'] != '') {
                $button_border_hover = json_decode($tmpcoderThemeMods['button_border_hover'], true);
                $redux_data['button_border_hover']['border-top']    = $button_border_hover['top'].'px';
                $redux_data['button_border_hover']['border-right']  = $button_border_hover['right'].'px';
                $redux_data['button_border_hover']['border-bottom'] = $button_border_hover['bottom'].'px';
                $redux_data['button_border_hover']['border-left']   = $button_border_hover['left'].'px';
            }
            $redux_data['button_border_hover']['border-style'] = isset($tmpcoderThemeMods['button_border_hover_border-style']) ? $tmpcoderThemeMods['button_border_hover_border-style']  : '';
            $redux_data['button_border_hover']['border-color'] = isset($tmpcoderThemeMods['button_border_hover_border-color']) ? $tmpcoderThemeMods['button_border_hover_border-color']  : '';
            $redux_data['button_border_radius'] = isset($tmpcoderThemeMods['button_border_radius']) ? $tmpcoderThemeMods['button_border_radius']  : '4';
            if ( isset($tmpcoderThemeMods['button_padding']) && $tmpcoderThemeMods['button_padding'] != '') {
                $button_padding = json_decode($tmpcoderThemeMods['button_padding'], true);
                $redux_data['button_padding']['border-top']    = $button_padding['top'].'px';
                $redux_data['button_padding']['border-right']  = $button_padding['right'].'px';
                $redux_data['button_padding']['border-bottom'] = $button_padding['bottom'].'px';
                $redux_data['button_padding']['border-left']   = $button_padding['left'].'px';
            } else {
                $redux_data['button_padding']['border-top']    = '11px';
                $redux_data['button_padding']['border-right']  = '20px';
                $redux_data['button_padding']['border-bottom'] = '11px';
                $redux_data['button_padding']['border-left']   = '20px';
            }

            // Theme Button Variable Declare For Tablet
            $redux_data['button_style_tablet']['font-size'] = isset($tmpcoderThemeMods['button_style_tablet_font-size']) ? $tmpcoderThemeMods['button_style_tablet_font-size'].'px' : '';
            $redux_data['button_style_tablet']['line-height'] = isset($tmpcoderThemeMods['button_style_tablet_line-height']) ? $tmpcoderThemeMods['button_style_tablet_line-height'] : '';
            $redux_data['button_style_tablet']['letter-spacing'] = isset($tmpcoderThemeMods['button_style_tablet_letter-spacing']) ? $tmpcoderThemeMods['button_style_tablet_letter-spacing'].'px' : '';
            if ( isset($tmpcoderThemeMods['button_padding_tablet']) && $tmpcoderThemeMods['button_padding_tablet'] != '') {
                $button_padding_tablet = json_decode($tmpcoderThemeMods['button_padding_tablet'], true);

                $redux_data['button_padding_tablet']['border-top']    = $button_padding_tablet['top'].'px';
                $redux_data['button_padding_tablet']['border-right']  = $button_padding_tablet['right'].'px';
                $redux_data['button_padding_tablet']['border-bottom'] = $button_padding_tablet['bottom'].'px';
                $redux_data['button_padding_tablet']['border-left']   = $button_padding_tablet['left'].'px';
            }

            // Theme Button Variable Declare For Mobile
            $redux_data['button_style_mobile']['font-size'] = isset($tmpcoderThemeMods['button_style_mobile_font-size']) ? $tmpcoderThemeMods['button_style_mobile_font-size'].'px' : '';
            $redux_data['button_style_mobile']['line-height'] = isset($tmpcoderThemeMods['button_style_mobile_line-height']) ? $tmpcoderThemeMods['button_style_mobile_line-height'] : '';
            $redux_data['button_style_mobile']['letter-spacing'] = isset($tmpcoderThemeMods['button_style_mobile_letter-spacing']) ? $tmpcoderThemeMods['button_style_mobile_letter-spacing'].'px' : '';
            if ( isset($tmpcoderThemeMods['button_padding_mobile']) && $tmpcoderThemeMods['button_padding_mobile'] != '') {
                $button_padding_mobile = json_decode($tmpcoderThemeMods['button_padding_mobile'], true);
                $redux_data['button_padding_mobile']['border-top']    = $button_padding_mobile['top'].'px';
                $redux_data['button_padding_mobile']['border-right']  = $button_padding_mobile['right'].'px';
                $redux_data['button_padding_mobile']['border-bottom'] = $button_padding_mobile['bottom'].'px';
                $redux_data['button_padding_mobile']['border-left']   = $button_padding_mobile['left'].'px';
            }

            // Theme font style for desktop
            if ( isset($tmpcoderThemeMods['site_fonts_options_font-family']) && $tmpcoderThemeMods['site_fonts_options_font-family'] != '') {
                $site_fonts = json_decode($tmpcoderThemeMods['site_fonts_options_font-family'], true);
                
                $redux_data['site_fonts_options']['font-family'] = '"'.$site_fonts['font'].'", '.$site_fonts['category'];
                $redux_data['site_fonts_options']['font-weight'] = $site_fonts['regularweight'];
            }
            $redux_data['site_fonts_options']['font-size'] = isset($tmpcoderThemeMods['site_fonts_options_font-size']) ? $tmpcoderThemeMods['site_fonts_options_font-size'].'px' : '';
            $redux_data['site_fonts_options']['line-height'] = isset($tmpcoderThemeMods['site_fonts_options_line-height']) ? $tmpcoderThemeMods['site_fonts_options_line-height'] : '';
            $redux_data['site_fonts_options']['letter-spacing'] = isset($tmpcoderThemeMods['site_fonts_options_letter-spacing']) ? $tmpcoderThemeMods['site_fonts_options_letter-spacing'].'px' : '';
            $redux_data['site_fonts_options']['color'] = isset($tmpcoderThemeMods['site_fonts_options_color']) ? $tmpcoderThemeMods['site_fonts_options_color'] : '';

            // Default values for each heading
            for ($i = 1; $i <= 6; $i++) {
                if (!empty($tmpcoderThemeMods['heading_'.$i.'_font-family'])) {
                    $heading_fonts = json_decode($tmpcoderThemeMods['heading_'.$i.'_font-family'], true);
                    $redux_data['heading_'.$i]['font-family'] = '"'.$heading_fonts['font'].'", '.$heading_fonts['category'];
                    $redux_data['heading_'.$i]['font-weight'] = $heading_fonts['regularweight'];
                } else {
                    // $redux_data['heading_'.$i]['font-family'] = '';
                    $redux_data['heading_'.$i]['font-weight'] = isset($tmpcoderThemeMods['heading_'.$i.'_font-weight']) ? $tmpcoderThemeMods['heading_'.$i.'_font-weight'] : '';
                }
            
                $redux_data['heading_'.$i]['text-transform'] = isset($tmpcoderThemeMods['heading_'.$i.'_text-transform']) ? $tmpcoderThemeMods['heading_'.$i.'_text-transform'] : '';
                $redux_data['heading_'.$i]['font-size'] = isset($tmpcoderThemeMods['heading_'.$i.'_font-size']) ? $tmpcoderThemeMods['heading_'.$i.'_font-size'].'px' : '';
                $redux_data['heading_'.$i]['line-height'] = isset($tmpcoderThemeMods['heading_'.$i.'_line-height']) ? $tmpcoderThemeMods['heading_'.$i.'_line-height'] : '';
                $redux_data['heading_'.$i]['letter-spacing'] = isset($tmpcoderThemeMods['heading_'.$i.'_letter-spacing']) ? $tmpcoderThemeMods['heading_'.$i.'_letter-spacing'].'px' : '';
                $redux_data['heading_'.$i]['color'] = isset($tmpcoderThemeMods['heading_'.$i.'_color']) ? $tmpcoderThemeMods['heading_'.$i.'_color'] : '';
            }
            
            // Theme font & heading style for tablet
            $redux_data['site_fonts_options_tablet']['font-size'] = isset($tmpcoderThemeMods['site_fonts_options_tablet_font-size']) ? $tmpcoderThemeMods['site_fonts_options_tablet_font-size'].'px' : '14px';
            $redux_data['site_fonts_options_tablet']['line-height'] = isset($tmpcoderThemeMods['site_fonts_options_tablet_line-height']) ? $tmpcoderThemeMods['site_fonts_options_tablet_line-height'] : '';
            $redux_data['site_fonts_options_tablet']['letter-spacing'] = isset($tmpcoderThemeMods['site_fonts_options_tablet_letter-spacing']) ? $tmpcoderThemeMods['site_fonts_options_tablet_letter-spacing'].'px' : '';

            // Define default values for each heading (h1 through h6) for tablet
            for ($i = 1; $i <= 6; $i++) {
                $redux_data['heading_'.$i.'_tablet']['font-size'] = isset($tmpcoderThemeMods['heading_'.$i.'_tablet_font-size']) ? $tmpcoderThemeMods['heading_'.$i.'_tablet_font-size'].'px' : '';
                $redux_data['heading_'.$i.'_tablet']['line-height'] = isset($tmpcoderThemeMods['heading_'.$i.'_tablet_line-height']) ? $tmpcoderThemeMods['heading_'.$i.'_tablet_line-height'] : '';
                $redux_data['heading_'.$i.'_tablet']['letter-spacing'] = isset($tmpcoderThemeMods['heading_'.$i.'_tablet_letter-spacing']) ? $tmpcoderThemeMods['heading_'.$i.'_tablet_letter-spacing'].'px' : '';
            }

            // Theme font & heading style for mobile
            $redux_data['site_fonts_options_mobile']['font-size'] = isset($tmpcoderThemeMods['site_fonts_options_mobile_font-size']) ? $tmpcoderThemeMods['site_fonts_options_mobile_font-size'].'px' : '';
            $redux_data['site_fonts_options_mobile']['line-height'] = isset($tmpcoderThemeMods['site_fonts_options_mobile_line-height']) ? $tmpcoderThemeMods['site_fonts_options_mobile_line-height'] : '';
            $redux_data['site_fonts_options_mobile']['letter-spacing'] = isset($tmpcoderThemeMods['site_fonts_options_mobile_letter-spacing']) ? $tmpcoderThemeMods['site_fonts_options_mobile_letter-spacing'].'px' : '';

            // Define default values for each heading (h1 through h6) for mobile
            for ($i = 1; $i <= 6; $i++) {
                $redux_data['heading_'.$i.'_mobile']['font-size'] = isset($tmpcoderThemeMods['heading_'.$i.'_mobile_font-size']) ? $tmpcoderThemeMods['heading_'.$i.'_mobile_font-size'].'px' : '';
                $redux_data['heading_'.$i.'_mobile']['line-height'] = isset($tmpcoderThemeMods['heading_'.$i.'_mobile_line-height']) ? $tmpcoderThemeMods['heading_'.$i.'_mobile_line-height'] : '';
                $redux_data['heading_'.$i.'_mobile']['letter-spacing'] = isset($tmpcoderThemeMods['heading_'.$i.'_mobile_letter-spacing']) ? $tmpcoderThemeMods['heading_'.$i.'_mobile_letter-spacing'].'px' : '';
            }

            return $redux_data;
        }
    }
    add_filter('tmpcoder_redux_options_theme_variable', 'tmpcoder_add_customizer_options');
}

/**
 * Adds inline styles based on Customizer options for responsive typography and button styles.
 */
if ( ! function_exists( 'tmpcoder_add_customizer_options_inline_style' ) ) {
    function tmpcoder_add_customizer_options_inline_style() {
        $viewport_tablet = 1025;
		$viewport_mobile = 768;

        // Check if we're in the Customizer preview
        if ( is_customize_preview() ) {
            // Adjust breakpoints specifically for the Customizer preview
            $viewport_tablet = 768;
            $viewport_mobile = 480;
        }

        $style = '
        @media (max-width: '.$viewport_tablet.'px) {
            body {
                font-size: var(--theme-font-size-tablet);
                line-height: var(--theme-line-height-tablet);
                letter-spacing: var(--theme-letter-spacing-tablet);
            }
            h1, h1.wp-block-heading {
                font-size: var(--heading1-font-size-tablet);
                line-height: var(--heading1-line-height-tablet);
                letter-spacing: var(--heading1-letter-spacing-tablet);
            }
            h2, h2.wp-block-heading {
                font-size: var(--heading2-font-size-tablet);
                line-height: var(--heading2-line-height-tablet);
                letter-spacing: var(--heading2-letter-spacing-tablet);
            }
            h3, h3.wp-block-heading {
                font-size: var(--heading3-font-size-tablet);
                line-height: var(--heading3-line-height-tablet);
                letter-spacing: var(--heading3-letter-spacing-tablet);
            }
            h4, h4.wp-block-heading {
                font-size: var(--heading4-font-size-tablet);
                line-height: var(--heading4-line-height-tablet);
                letter-spacing: var(--heading4-letter-spacing-tablet);
            }
            h5, h5.wp-block-heading {
                font-size: var(--heading5-font-size-tablet);
                line-height: var(--heading5-line-height-tablet);
                letter-spacing: var(--heading5-letter-spacing-tablet);
            }
            h6, h6.wp-block-heading {
                font-size: var(--heading6-font-size-tablet);
                line-height: var(--heading6-line-height-tablet);
                letter-spacing: var(--heading6-letter-spacing-tablet);
            }
            body .site-main .wp-block-button .wp-element-button {
                font-size: var(--button-font-size-tablet);
                line-height: var(--button-line-height-tablet);
                letter-spacing: var(--button-letter-spacing-tablet);
                padding-top: var(--button-tablet-padding-top);
                padding-right: var(--button-tablet-padding-right);
                padding-left: var(--button-tablet-padding-left);
                padding-bottom: var(--button-tablet-padding-bottom);
            }
        }
        @media (max-width: '.$viewport_mobile.'px) {
            body {
                font-size: var(--theme-font-size-mobile);
                line-height: var(--theme-line-height-mobile);
                letter-spacing: var(--theme-letter-spacing-mobile);
            }
            h1, h1.wp-block-heading {
                font-size: var(--heading1-font-size-mobile);
                line-height: var(--heading1-line-height-mobile);
                letter-spacing: var(--heading1-letter-spacing-mobile);
            }
            h2, h2.wp-block-heading {
                font-size: var(--heading2-font-size-mobile);
                line-height: var(--heading2-line-height-mobile);
                letter-spacing: var(--heading2-letter-spacing-mobile);
            }
            h3, h3.wp-block-heading {
                font-size: var(--heading3-font-size-mobile);
                line-height: var(--heading3-line-height-mobile);
                letter-spacing: var(--heading3-letter-spacing-mobile);
            }
            h4, h4.wp-block-heading {
                font-size: var(--heading4-font-size-mobile);
                line-height: var(--heading4-line-height-mobile);
                letter-spacing: var(--heading4-letter-spacing-mobile);
            }
            h5, h5.wp-block-heading {
                font-size: var(--heading5-font-size-mobile);
                line-height: var(--heading5-line-height-mobile);
                letter-spacing: var(--heading5-letter-spacing-mobile);
            }
            h6, h6.wp-block-heading {
                font-size: var(--heading6-font-size-mobile);
                line-height: var(--heading6-line-height-mobile);
                letter-spacing: var(--heading6-letter-spacing-mobile);
            }
            body .site-main .wp-block-button .wp-element-button {
                font-size: var(--button-font-size-mobile);
                line-height: var(--button-line-height-mobile);
                letter-spacing: var(--button-letter-spacing-mobile);
                padding-top: var(--button-mobile-padding-top);
                padding-right: var(--button-mobile-padding-right);
                padding-left: var(--button-mobile-padding-left);
                padding-bottom: var(--button-mobile-padding-bottom);
            }
        }';

        if ( is_customize_preview() ) {
            $style .= '
            @media (max-width: '.$viewport_tablet.'px) {
                .site-footer:not(.dynamic-footer) .footer-inner, 
                .site-main .page-content, 
                .tmpcoder-site-header .site-footer .footer-inner, 
                .tmpcoder-site-header .site-header .header-inner, 
                .tmpcoder-site-header .site-header:not(.dynamic-header), 
                .tmpcoder-site-header.page-header .entry-title, 
                body.post-type-archive .site-main, 
                body.single-product .site-main, 
                body.tax-product_cat .site-main {
                    max-width: var(--tmpcoder-tablet-container-width);
                }
            }
            @media (max-width: '.$viewport_mobile.'px) {
                .site-footer:not(.dynamic-footer) .footer-inner, 
                .site-main .page-content, 
                .tmpcoder-site-header .site-footer .footer-inner, 
                .tmpcoder-site-header .site-header .header-inner, 
                .tmpcoder-site-header .site-header:not(.dynamic-header), 
                .tmpcoder-site-header.page-header .entry-title, 
                body.post-type-archive .site-main, 
                body.single-product .site-main, 
                body.tax-product_cat .site-main {
                    max-width: var(--tmpcoder-mobile-container-width);
                }
            }';
        }

        // Check if the Redux Framework plugin is not active
        if( ! class_exists('Redux_Framework_Plugin', false) ) {            
            wp_register_style( 'tmpcoder-customizer-option', false );
            wp_enqueue_style( 'tmpcoder-customizer-option' );
            wp_add_inline_style('tmpcoder-customizer-option', tmpcoder_trim_customizer_style($style));
        }
    }
    add_action( 'wp_enqueue_scripts', 'tmpcoder_add_customizer_options_inline_style', 9999 );
}

/**
 * Trim and optimize custom CSS for faster page loading.
 *
 * This function removes comments and unnecessary white spaces from the given CSS string 
 * to reduce file size and improve performance.
 *
 * @param string $css The CSS code to be optimized.
 * @return string The optimized CSS code.
 */
if ( ! function_exists( 'tmpcoder_trim_customizer_style' ) ) {
    function tmpcoder_trim_customizer_style( $css = '' ) {

        // Trim white space for faster page loading.
        if ( ! empty( $css ) ) {
            $css = preg_replace( '!/\*[^*]*\*+([^/][^*]*\*+)*/!', '', $css );
            $css = str_replace( array( "\r\n", "\r", "\n", "\t", '  ', '    ', '    ' ), '', $css );
            $css = str_replace( ', ', ',', $css );
        }
        return $css;
    }
}

/**
 * Enqueue custom scripts for the WordPress Customizer preview.
 *
 * This function loads a JavaScript file that enhances the WordPress Customizer's live preview.
 *
 * @return void
 */
if ( ! function_exists( 'tmpcoder_enqueue_customizer_scripts' ) ) {
    function tmpcoder_enqueue_customizer_scripts() {
        wp_enqueue_script(
            'tmpcoder-customizer-preview',
            TMPCODER_CUSTOMIZER_ASSETS . 'js/tmpcoder-customizer-preview'.tmpcoder_min_suffix().'.js',
            array( 'jquery', 'customize-preview' ),
            TMPCODER_THEME_CORE_VERSION,
            true
        );
    }
    add_action( 'customize_preview_init', 'tmpcoder_enqueue_customizer_scripts' );
}

/**
 * Enqueue Google Fonts loading script for the theme.
 *
 * This function loads a JavaScript file that handles the loading of Google Fonts in the theme. 
 *
 * @return void
 */
if ( ! function_exists( 'tmpcoder_enqueue_google_fonts_script' ) ) {
    function tmpcoder_enqueue_google_fonts_script() {
        wp_enqueue_script(
            'tmpcoder-google-fonts',
            TMPCODER_CUSTOMIZER_ASSETS . 'js/tmpcoder-load-google-fonts' . tmpcoder_min_suffix() . '.js',
            array(),
            TMPCODER_THEME_CORE_VERSION,
            true
        );
    }
    add_action( 'wp_enqueue_scripts', 'tmpcoder_enqueue_google_fonts_script' );
}

/**
 * Set default theme_mod values after starter content is published.
 *
 * Applies initial default styles (layout, buttons, fonts, headings) for desktop, tablet, and mobile.
 * Skips if Redux is active or starter content has already been applied.
 *
 * @return void
 */
if ( ! function_exists( 'tmpcoder_set_theme_mods_after_starter_content_publish' ) ) {

    function tmpcoder_set_theme_mods_after_starter_content_publish() {
        if ( ! class_exists( 'Redux_Framework_Plugin', false ) ) {

            // Check if it's the first time starter content was published
            if ( get_option( 'tmpcoder_starter_content_applied' ) ) {
                return;
            }

            // Base defaults
            $defaults = array(
                'container_width'                      => '1200',
                'container_width_tablet'               => '1000',
                'container_width_mobile'               => '500',

                'site_button_text_hover'               => '#6192f8',
                'site_button_color'                    => '#6192f8',
                'button_border_border-color'           => '#6192f8',
                'button_border_hover_border-color'     => '#6192f8',

                'button_style_text-align'              => 'center',
                'button_style_text-transform'          => 'uppercase',
                'button_style_font-size'               => '16',
                'button_style_line-height'             => '1',
                'button_border_radius'                 => '4',

                'site_fonts_options_font-size'         => '14',
                'site_fonts_options_color'             => '#777777',
                'site_fonts_options_mobile_font-size'  => '14',
                'site_fonts_options_mobile_line-height'=> '1.5',
            );

            // Keys for headings
            $heading_keys = array( 'font-family', 'font-weight', 'font-size', 'text-transform', 'line-height', 'letter-spacing', 'color' );

            // Desktop heading defaults
            $heading_defaults = array(
                1 => array( 'font-weight' => '600', 'font-size' => '52', 'line-height' => '1.2', 'color' => 'rgb(29,29,29)' ),
                2 => array( 'font-weight' => '600', 'font-size' => '40', 'line-height' => '1.2', 'text-transform' => 'none', 'color' => '#1d1d1d' ),
                3 => array(),
                4 => array(),
                5 => array( 'font-weight' => '600', 'font-size' => '18', 'line-height' => '1.2', 'text-transform' => 'none', 'color' => '#121619' ),
                6 => array( 'font-weight' => '500', 'font-size' => '18', 'line-height' => '1.2', 'text-transform' => 'uppercase', 'letter-spacing' => '2', 'color' => 'rgb(97,146,248)' ),
            );

            // Tablet heading defaults
            $heading_defaults_tablet = array(
                1 => array( 'font-size' => '40', 'line-height' => '1.1' ),
                2 => array( 'font-size' => '34', 'line-height' => '1.1' ),
                3 => array(),
                4 => array(),
                5 => array( 'font-size' => '20', 'line-height' => '1.1' ),
                6 => array( 'font-size' => '18', 'line-height' => '1.1', 'letter-spacing' => '2' ),
            );

            // Mobile heading defaults
            $heading_defaults_mobile = array(
                1 => array( 'font-size' => '33', 'line-height' => '1.5' ),
                2 => array( 'font-size' => '20', 'line-height' => '1.1' ),
                3 => array(),
                4 => array(),
                5 => array( 'font-size' => '20', 'line-height' => '1.1' ),
                6 => array( 'font-size' => '16', 'line-height' => '1.1', 'letter-spacing' => '2' ),
            );

            // Merge desktop heading defaults
            foreach ( $heading_defaults as $i => $values ) {
                foreach ( $heading_keys as $key ) {
                    $defaults[ "heading_{$i}_{$key}" ] = isset( $values[ $key ] ) ? $values[ $key ] : '';
                }
            }

            // Merge tablet heading defaults
            foreach ( $heading_defaults_tablet as $i => $values ) {
                foreach ( array( 'font-size', 'line-height', 'letter-spacing' ) as $key ) {
                    $defaults[ "heading_{$i}_tablet_{$key}" ] = isset( $values[ $key ] ) ? $values[ $key ] : '';
                }
            }

            // Merge mobile heading defaults
            foreach ( $heading_defaults_mobile as $i => $values ) {
                foreach ( array( 'font-size', 'line-height', 'letter-spacing' ) as $key ) {
                    $defaults[ "heading_{$i}_mobile_{$key}" ] = isset( $values[ $key ] ) ? $values[ $key ] : '';
                }
            }

            // Set each theme_mod if not already set and value is not empty
            foreach ( $defaults as $key => $value ) {
                if ( get_theme_mod( $key, false ) === false && $value !== '' ) {
                    set_theme_mod( $key, $value );
                }
            }

            // Set flag to avoid re-running
            update_option( 'tmpcoder_starter_content_applied', true );
        }
    }

    add_action( 'customize_register', 'tmpcoder_set_theme_mods_after_starter_content_publish' );
}