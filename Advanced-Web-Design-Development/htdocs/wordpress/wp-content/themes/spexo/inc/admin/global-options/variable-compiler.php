<?php

// Add styles to frontend
add_action('wp_head', 'tmpcoder_wp_head_theme_variable', 1);

// Add styles to Gutenberg editor
add_action('enqueue_block_editor_assets', 'tmpcoder_wp_head_theme_variable', 1);

function tmpcoder_wp_head_theme_variable(){
    
    if ( class_exists( 'Tmpcoder_Site_Settings' ) ){        
        $redux_data = Tmpcoder_Site_Settings::tmpcoder_get_all_data();

        $redux_data = apply_filters('tmpcoder_redux_options_theme_variable', $redux_data);

        $css = tmpcoder_redux_options_update_theme_variable($redux_data, '', '');
        // Decode HTML entities
        $decoded_css = html_entity_decode($css, ENT_QUOTES, 'UTF-8');
        // Escape the CSS before adding it inline
        $escaped_css = wp_strip_all_tags($decoded_css);
        wp_register_style('tmpcoder-theme-variable', false);
        wp_enqueue_style('tmpcoder-theme-variable');
        wp_add_inline_style('tmpcoder-theme-variable', $escaped_css);
    }
}

if ( ! function_exists('tmpcoder_redux_options_update_theme_variable') ){

    function tmpcoder_redux_options_update_theme_variable($options, $css, $changed_values){        
        $css = $css != "" ? $css : '';

        // Apply Variables to Both Frontend & Block Editor (.editor-styles-wrapper is for block editor)
        $css .= ":root, .editor-styles-wrapper {\n";

        // Container Width Variable Declare
        if ( isset($options['container_width']) && $options['container_width'] != "" ){
            $css .= "--tmpcoder-container-width:". esc_html($options['container_width'])."px;\n";
        }else{
            $css .= "--tmpcoder-container-width: 1200px;\n";
        }

        if ( isset($options['container_width_tablet']) && $options['container_width_tablet'] != "" ){
            $css .= "--tmpcoder-tablet-container-width:". esc_html($options['container_width_tablet'])."px;\n";
        }else{
            $css .= "--tmpcoder-tablet-container-width: 1024px;\n";
        }

        if ( isset($options['container_width_mobile']) && $options['container_width_mobile'] != "" ){
            $css .= "--tmpcoder-mobile-container-width:". esc_html($options['container_width_mobile'])."px;\n";
        }else{
            $css .= "--tmpcoder-mobile-container-width: 767px;\n";
        }


        // Primary Color Variable Declare
        if ( isset($options['primany_color']) && $options['primany_color'] != "" ){
            $css .= "--theme-primary-color:". esc_html($options['primany_color']).";\n";
        }else{
            $css .= "--theme-primary-color: #5729d9;\n";
        }
        
        // Secondary Color Variable Declare
        if ( isset($options['secondary_color']) && $options['secondary_color'] != "" ){
            $css .= "--theme-secondary-color:". esc_html($options['secondary_color']).";\n";
        }else{
            $css .= "--theme-secondary-color: #010101;\n";
        }

        // Accent Color Variable Declare
        if ( isset($options['accent_color']) && $options['accent_color'] != "" ){
            $css .= "--theme-accent-color:". esc_html($options['accent_color']).";\n";
        }else{
            $css .= "--theme-accent-color: #b2b1b1;\n";
        }
        
        if ( isset($options['site_background_color']) && $options['site_background_color'] != "" ){
            if( isset($options['site_background_color']['background-color'] ) ) {
                $css .= "--theme-background-color:". esc_html($options['site_background_color']['background-color']).";\n";    
            } elseif ( isset($options['site_background_color'] ) ) {
                $css .= "--theme-background-color:". esc_html($options['site_background_color']).";\n";
            } else {
                $css .= "--theme-background-color: #ffffff;\n";
            }
        }
        else{
            $css .= "--theme-background-color: #ffffff;\n";
        }  
        
        // Button Variable Declare
        if ( isset($options['site_button_color']) && $options['site_button_color'] != "" ){
            $css .= "--theme-button-background:". esc_html($options['site_button_color']).";\n";
        }else{
            $css .= "--theme-button-background: #5729d9;\n";
        }

        if ( isset($options['site_button_color_hover']) && $options['site_button_color_hover'] != "" ){
            $css .= "--theme-button-background-hover:". esc_html($options['site_button_color_hover']).";\n";
        }else{
            $css .= "--theme-button-background-hover: #ffffff;\n";
        }

        // Link Color Variable Declare
        if ( isset($options['link_color']) && $options['link_color'] != "" ){
            $css .= "--theme-link-color:". esc_html($options['link_color']).";\n";
        }else{
            $css .= "--theme-link-color: #5729d9;\n";
        }

        // Link Hover Color Variable Declare
        if ( isset($options['link_hover_color']) && $options['link_hover_color'] != "" ){
            $css .= "--theme-link-hover-color:". esc_html($options['link_hover_color']).";\n";
        }else{
            $css .= "--theme-link-hover-color: #1d2327;\n";
        }

        // Global Border Color Variable Declare
        if ( isset($options['global_border_color']) && $options['global_border_color'] != "" ){
            $css .= "--theme-global-border-color:". esc_html($options['global_border_color']).";\n";
        }else{
            $css .= "--theme-global-border-color: #E6E8EA;\n";
        }

        // Theme Color 1
        if ( isset($options['theme_color_1']) && $options['theme_color_1'] != "" ){
            $css .= "--theme-global-color-1:". esc_html($options['theme_color_1']).";\n";
        }else{
            $css .= "--theme-global-color-1: #010101;\n";
        }

        // Theme Color 2
        if ( isset($options['theme_color_2']) && $options['theme_color_2'] != "" ){
            $css .= "--theme-global-color-2:". esc_html($options['theme_color_2']).";\n";
        }else{
            $css .= "--theme-global-color-2: #f4f2f9;\n";
        }

        // Theme Color 3
        if ( isset($options['theme_color_3']) && $options['theme_color_3'] != "" ){
            $css .= "--theme-global-color-3:". esc_html($options['theme_color_3']).";\n";
        }else{
            $css .= "--theme-global-color-3: #ffffff;\n";
        }

        // Theme Color 4
        if ( isset($options['theme_color_4']['rgba']) && $options['theme_color_4']['rgba'] != ''){
            $css .= "--theme-global-color-4:". esc_html($options['theme_color_4']['rgba']).";\n";
        }else{
            $css .= "--theme-global-color-4: #ffffff;\n";
        }

        // Site Variable Declare
        if ( isset($options['site_fonts_options']['font-family']) && $options['site_fonts_options']['font-family'] != "" ){
            $css .= "--theme-font-family:". esc_html($options['site_fonts_options']['font-family']).";\n";
        }else{
            $css .= "--theme-font-family: \"Poppins\", Sans-serif;\n";
        }
        
        if ( isset($options['site_fonts_options']['font-weight']) && $options['site_fonts_options']['font-weight'] != "" ){
            $css .= "--theme-font-weight:". esc_html($options['site_fonts_options']['font-weight']).";\n";
        }else{
            $css .= "--theme-font-weight: 400;\n";
        }
        
        /* Theme font style for desktop start */

        if ( isset($options['site_fonts_options']['font-size']) && $options['site_fonts_options']['font-size'] != "" ){
            $css .= "--theme-font-size:". esc_html($options['site_fonts_options']['font-size']).";\n";
        }else{
            $css .= "--theme-font-size: 14px;\n";
        }
        
        if ( isset($options['site_fonts_options']['line-height']) && $options['site_fonts_options']['line-height'] != "" ){
            $css .= "--theme-line-height:". esc_html($options['site_fonts_options']['line-height']).";\n";
        }else{
            $css .= "--theme-line-height: 1.55;\n";
        }

        if ( isset($options['site_fonts_options']['letter-spacing']) && $options['site_fonts_options']['letter-spacing'] != "" ){
            $css .= "--theme-letter-spacing:". esc_html($options['site_fonts_options']['letter-spacing']).";\n";
        }else{
            $css .= "--theme-letter-spacing: normal;\n";
        }

        /* Theme font style for desktop end */

        /* Theme font style for tablet start */

        if ( isset($options['site_fonts_options_tablet']['font-size']) && $options['site_fonts_options_tablet']['font-size'] != "" ){
            $css .= "--theme-font-size-tablet:". esc_html($options['site_fonts_options_tablet']['font-size']).";\n";
        }else{
            $css .= "--theme-font-size-tablet: 14px;\n";
        }
        
        if ( isset($options['site_fonts_options_tablet']['line-height']) && $options['site_fonts_options_tablet']['line-height'] != "" ){
            $css .= "--theme-line-height-tablet:". esc_html($options['site_fonts_options_tablet']['line-height']).";\n";
        }else{
            $css .= "--theme-line-height-tablet: 24px;\n";
        }

        if ( isset($options['site_fonts_options_tablet']['letter-spacing']) && $options['site_fonts_options_tablet']['letter-spacing'] != "" ){
            $css .= "--theme-letter-spacing-tablet:". esc_html($options['site_fonts_options_tablet']['letter-spacing']).";\n";
        }else{
            $css .= "--theme-letter-spacing-tablet: normal;\n";
        }

        /* Theme font style for tablet end */

        /* Theme font style for mobile start */

        if ( isset($options['site_fonts_options_mobile']['font-size']) && $options['site_fonts_options_mobile']['font-size'] != "" ){
            $css .= "--theme-font-size-mobile:". esc_html($options['site_fonts_options_mobile']['font-size']).";\n";
        }else{
            $css .= "--theme-font-size-mobile: 14px;\n";
        }
        
        if ( isset($options['site_fonts_options_mobile']['line-height']) && $options['site_fonts_options_mobile']['line-height'] != "" ){
            $css .= "--theme-line-height-mobile:". esc_html($options['site_fonts_options_mobile']['line-height']).";\n";
        }else{
            $css .= "--theme-line-height-mobile: 24px;\n";
        }

        if ( isset($options['site_fonts_options_mobile']['letter-spacing']) && $options['site_fonts_options_mobile']['letter-spacing'] != "" ){
            $css .= "--theme-letter-spacing-mobile:". esc_html($options['site_fonts_options_mobile']['letter-spacing']).";\n";
        }else{
            $css .= "--theme-letter-spacing-mobile: normal;\n";
        }

        /* Theme font style for mobile end */

        if ( isset($options['site_fonts_options']['color']) && $options['site_fonts_options']['color'] != "" ){
            $css .= "--theme-text-color:". esc_html($options['site_fonts_options']['color']).";\n";
        }else{
            $css .= "--theme-text-color: #727272;\n";
        }

        // Heading Variable Declare for desktop

        for ($i=1; $i <= 6; $i++) { 
            if ( isset($options['heading_'.$i]['font-family']) && $options['heading_'.$i]['font-family'] != "" ){
                $css .= "--heading".$i."-font-family:". esc_html($options['heading_'.$i]['font-family']).";\n";
            }else{
                $css .= "--heading".$i."-font-family: \"Poppins\", Sans-serif;\n";
            }
            
            if ( isset($options['heading_'.$i]['font-options']) && $options['heading_'.$i]['font-options'] != "" ){
                $css .= "--heading".$i."-font-options:". esc_html($options['heading_'.$i]['font-options']).";\n";
            }
            
            if ( isset($options['heading_'.$i]['font-backup']) && $options['heading_'.$i]['font-backup'] != "" ){
                $css .= "--heading".$i."-font-backup:". esc_html($options['heading_'.$i]['font-backup']).";\n";
            }
            
            if ( isset($options['heading_'.$i]['font-weight']) && $options['heading_'.$i]['font-weight'] != "" ){
                $css .= "--heading".$i."-font-weight:". esc_html($options['heading_'.$i]['font-weight']).";\n";
            }else{
                if ( $i == 1 || $i == 2){
                    $css .= "--heading".$i."-font-weight: 600;\n";
                }elseif ($i == 3) {
                    $css .= "--heading".$i."-font-weight: 500;\n";
                }else{
                    $css .= "--heading".$i."-font-weight: 400;\n";
                }
            }
            
            if ( isset($options['heading_'.$i]['text-align']) && $options['heading_'.$i]['text-align'] != "" ){
                $css .= "--heading".$i."-font-style:". esc_html($options['heading_'.$i]['font-style']).";\n";
            }else{
                $css .= "--heading".$i."-font-style: normal;\n";
            }

            if ( isset($options['heading_'.$i]['subsets']) && $options['heading_'.$i]['subsets'] != "" ){
                $css .= "--heading".$i."-subsets:". esc_html($options['heading_'.$i]['subsets']).";\n";
            }       
            
            if ( isset($options['heading_'.$i]['text-align']) && $options['heading_'.$i]['text-align'] != "" ){
                $css .= "--heading".$i."-text-align:". esc_html($options['heading_'.$i]['text-align']).";\n";
            }else{
                $css .= "--heading".$i."-text-align: initial;\n";
            }

            if ( isset($options['heading_'.$i]['text-transform']) && $options['heading_'.$i]['text-transform'] != "" ){
                $css .= "--heading".$i."-text-transform:". esc_html($options['heading_'.$i]['text-transform']).";\n";
            }else{
                $css .= "--heading".$i."-text-transform: initial;\n";
            }
            
            if ( isset($options['heading_'.$i]['font-size']) && $options['heading_'.$i]['font-size'] != "" ){
                $css .= "--heading".$i."-font-size:". esc_html($options['heading_'.$i]['font-size']).";\n";
            }else{
                // $css .= "--heading".$i."-font-size:". (48 -( $i * 6)) ."px;\n";
                if ( $i == 1){
                    $css .= "--heading".$i."-font-size: 32px;\n";
                }elseif ($i == 2) {
                    $css .= "--heading".$i."-font-size: 26px;\n";
                }elseif ($i == 3) {
                    $css .= "--heading".$i."-font-size: 20px;\n";
                }elseif ($i == 4) {
                    $css .= "--heading".$i."-font-size: 17px;\n";
                }elseif ($i == 5) {
                    $css .= "--heading".$i."-font-size: 15px;\n";
                }else{
                    $css .= "--heading".$i."-font-size: 13px;\n";
                }
            }
            
            if ( isset($options['heading_'.$i]['line-height']) && $options['heading_'.$i]['line-height'] != "" ){
                $css .= "--heading".$i."-line-height:". esc_html($options['heading_'.$i]['line-height']).";\n";
            }else{
                //$css .= "--heading".$i."-line-height:". (48 -( $i * 6)) ."px;\n";
                // $css .= "--heading".$i."-line-height: 1.2;\n";
                if ( $i == 1){
                    $css .= "--heading".$i."-line-height: 48px;\n";
                }elseif ($i == 2) {
                    $css .= "--heading".$i."-line-height: 30px;\n";
                }elseif ($i == 3) {
                    $css .= "--heading".$i."-line-height: 24px;\n";
                }elseif ($i == 4) {
                    $css .= "--heading".$i."-line-height: 22px;\n";
                }elseif ($i == 5) {
                    $css .= "--heading".$i."-line-height: 20px;\n";
                }else{
                    $css .= "--heading".$i."-line-height: 16px;\n";
                }
            }

            if ( isset($options['heading_'.$i]['letter-spacing']) && $options['heading_'.$i]['letter-spacing'] != "" ){
                $css .= "--heading".$i."-letter-spacing:". esc_html($options['heading_'.$i]['letter-spacing']).";\n";
            }else{
                $css .= "--heading".$i."-letter-spacing:normal;\n";
            }
            
            if ( isset($options['heading_'.$i]['color']) && $options['heading_'.$i]['color'] != "" ){
                $css .= "--heading".$i."-text-color:". esc_html($options['heading_'.$i]['color']).";\n";
            }else{
                $css .= "--heading".$i."-text-color: #1d2327;\n";
            }
            
        }

        /* Heading Variable Declare for tablet */

        for ($i=1; $i <= 6; $i++) { 
            
            if ( isset($options['heading_'.$i.'_tablet']['font-size']) && $options['heading_'.$i.'_tablet']['font-size'] != "" ){
                $css .= "--heading".$i."-font-size-tablet:". esc_html($options['heading_'.$i.'_tablet']['font-size']).";\n";
            }else{
                $css .= "--heading".$i."-font-size-tablet:". (40 -( $i * 4)) ."px;\n";
            }
            
            if ( isset($options['heading_'.$i.'_tablet']['line-height']) && $options['heading_'.$i.'_tablet']['line-height'] != "" ){
                $css .= "--heading".$i."-line-height-tablet:". esc_html($options['heading_'.$i.'_tablet']['line-height']).";\n";
            }else{
                // $css .= "--heading".$i."-line-height-tablet:". (40 -( $i * 4)) ."px;\n";
                $css .= "--heading".$i."-line-height-tablet: 1.2;\n";
            }

            if ( isset($options['heading_'.$i.'_tablet']['letter-spacing']) && $options['heading_'.$i.'_tablet']['letter-spacing'] != "" ){
                $css .= "--heading".$i."-letter-spacing-tablet:". esc_html($options['heading_'.$i.'_tablet']['letter-spacing']).";\n";
            }else{
                $css .= "--heading".$i."-letter-spacing-tablet:normal;\n";
            }
        }

        /* Heading Variable Declare for mobile */

        for ($i=1; $i <= 6; $i++) { 
            
            if ( isset($options['heading_'.$i.'_mobile']['font-size']) && $options['heading_'.$i.'_mobile']['font-size'] != "" ){
                $css .= "--heading".$i."-font-size-mobile:". esc_html($options['heading_'.$i.'_mobile']['font-size']).";\n";
            }else{
                $css .= "--heading".$i."-font-size-mobile:". (28 -( $i * 3)) ."px;\n";
            }
            
            if ( isset($options['heading_'.$i.'_mobile']['line-height']) && $options['heading_'.$i.'_mobile']['line-height'] != "" ){
                $css .= "--heading".$i."-line-height-mobile:". esc_html($options['heading_'.$i.'_mobile']['line-height']).";\n";
            }else{
                // $css .= "--heading".$i."-line-height-mobile:". (28 -( $i * 3)) ."px;\n";
                $css .= "--heading".$i."-line-height-mobile: 1.2;\n";
            }

            if ( isset($options['heading_'.$i.'_mobile']['letter-spacing']) && $options['heading_'.$i.'_mobile']['letter-spacing'] != "" ){
                $css .= "--heading".$i."-letter-spacing-mobile:". esc_html($options['heading_'.$i.'_mobile']['letter-spacing']).";\n";
            }else{
                $css .= "--heading".$i."-letter-spacing-mobile:normal;\n";
            }
        }

        // Theme Button Variable Declare For Desktop
        if ( isset($options['button_style']['font-family']) && $options['button_style']['font-family'] != "" ){
            $css .= "--button-font-family:". esc_html($options['button_style']['font-family']).";\n";
        }else{
            $css .= "--button-font-family: \"Poppins\", Sans-serif;\n";
        }
        // $options['button_style']['font-options']
        // $options['button_style']['google'];
        
        if ( isset($options['button_style']['font-weight']) && $options['button_style']['font-weight'] != "" ){
            $css .= "--button-font-weight:". esc_html($options['button_style']['font-weight']).";\n";
        }else{
            $css .= "--button-font-weight: 400;\n";
        }

        if ( isset($options['button_style']['text-align']) && $options['button_style']['text-align'] != "" ){
            $css .= "--button-text-align:". esc_html($options['button_style']['text-align']).";\n";
        }else{
            $css .= "--button-text-align: initial;\n";
        }
        
        if ( isset($options['button_style']['text-transform']) && $options['button_style']['text-transform'] != "" ){
            $css .= "--button-text-transform:". esc_html($options['button_style']['text-transform']).";\n";
        }else{
            $css .= "--button-text-transform: initial;\n";
        }

        if ( isset($options['button_style']['font-size']) && $options['button_style']['font-size'] != "" ){
            $css .= "--button-font-size:". esc_html($options['button_style']['font-size']).";\n";
        }else{
            $css .= "--button-font-size: 16px;\n";
        }
        
        if ( isset($options['button_style']['line-height']) && $options['button_style']['line-height'] != "" ){
            $css .= "--button-line-height:". esc_html($options['button_style']['line-height']).";\n";
        }else{
            $css .= "--button-line-height: 24px;\n";
        }

        if ( isset($options['button_style']['letter-spacing']) && $options['button_style']['letter-spacing'] != "" ){
            $css .= "--button-letter-spacing:". esc_html($options['button_style']['letter-spacing']).";\n";
        }else{
            $css .= "--button-letter-spacing:normal;\n";
        }

        if ( isset($options['button_style']['color']) && $options['button_style']['color'] != "" ){
            $css .= "--button-text-color:". esc_html($options['button_style']['color']).";\n";
        }else{
            $css .= "--button-text-color: #ffffff;\n";
        }

        if ( isset($options['site_button_text_hover']) && $options['site_button_text_hover'] != "" ){
            $css .= "--button-text-hover-color:". esc_html($options['site_button_text_hover']).";\n";
        }else{
            $css .= "--button-text-hover-color: #5729d9;\n";
        }

        if ( isset($options['button_border_radius']) && $options['button_border_radius'] != "" ){
            $css .= "--button-border-radius:". esc_html($options['button_border_radius']."px").";\n";
        }else{
            $css .= "--button-border-radius: none;\n";
        }

        if ( isset($options['button_padding']['border-top']) && $options['button_padding']['border-top'] != "" ){
            $css .= "--button-padding-top:". esc_html($options['button_padding']['border-top']).";\n";
        }else{
            $css .= "--button-padding-top: 10px;\n";
        }

        if ( isset($options['button_padding']['border-right']) && $options['button_padding']['border-right'] != "" ){
            $css .= "--button-padding-right:". esc_html($options['button_padding']['border-right']).";\n";
        }else{
            $css .= "--button-padding-right: 15px;\n";
        }

        if ( isset($options['button_padding']['border-bottom']) && $options['button_padding']['border-bottom'] != "" ){
            $css .= "--button-padding-bottom:". esc_html($options['button_padding']['border-bottom']).";\n";
        }else{
            $css .= "--button-padding-bottom: 10px;\n";
        }

        if ( isset($options['button_padding']['border-left']) && $options['button_padding']['border-left'] != "" ){
            $css .= "--button-padding-left:". esc_html($options['button_padding']['border-left']).";\n";
        }else{
            $css .= "--button-padding-left: 15px;\n";
        }

        if ( isset($options['button_border']['border-color']) && $options['button_border']['border-color'] != "" ){
            $css .= "--button-border-color:". esc_html($options['button_border']['border-color']).";\n";
        }else{
            $css .= "--button-border-color: #5729d9;\n";
        }

        if ( isset($options['button_border_hover']['border-color']) && $options['button_border_hover']['border-color'] != "" ){

            $css .= "--button-border-hover-color:". esc_html($options['button_border_hover']['border-color']).";\n";
        }else{
            $css .= "--button-border-hover-color: #5729d9;\n";
        }

        if ( isset($options['button_border']['border-style']) && $options['button_border']['border-style'] != "" ){
            $css .= "--button-border-type:". esc_html($options['button_border']['border-style']).";\n";
        }else{
            $css .= "--button-border-type: solid;\n";
        }

        if ( isset($options['button_border']['border-top']) && $options['button_border']['border-top'] != "" ){
            $css .= "--button-border-top:". esc_html($options['button_border']['border-top']).";\n";
        }else{
            $css .= "--button-border-top: 1px;\n";
        }

        if ( isset($options['button_border']['border-right']) && $options['button_border']['border-right'] != "" ){
            $css .= "--button-border-right:". esc_html($options['button_border']['border-right']).";\n";
        }else{
            $css .= "--button-border-right: 1px;\n";
        }

        if ( isset($options['button_border']['border-bottom']) && $options['button_border']['border-bottom'] != "" ){
            $css .= "--button-border-bottom:". esc_html($options['button_border']['border-bottom']).";\n";
        }else{
            $css .= "--button-border-bottom: 1px;\n";
        }

        if ( isset($options['button_border']['border-left']) && $options['button_border']['border-left'] != "" ){
            $css .= "--button-border-left:". esc_html($options['button_border']['border-left']).";\n";
        }else{
            $css .= "--button-border-left: 1px;\n";
        }

        /* Button border hover style start */ 

        if ( isset($options['button_border_hover']['border-style']) && $options['button_border_hover']['border-style'] != "" ){
            $css .= "--button-border-hover-type:". esc_html($options['button_border_hover']['border-style']).";\n";
        }else{
            $css .= "--button-border-hover-type: solid;\n";
        }

        if ( isset($options['button_border_hover']['border-top']) && $options['button_border_hover']['border-top'] != "" ){
            $css .= "--button-border-hover-top:". esc_html($options['button_border_hover']['border-top']).";\n";
        }else{
            $css .= "--button-border-hover-top: 1px;\n";
        }

        if ( isset($options['button_border_hover']['border-right']) && $options['button_border_hover']['border-right'] != "" ){
            $css .= "--button-border-hover-right:". esc_html($options['button_border_hover']['border-right']).";\n";
        }else{
            $css .= "--button-border-hover-right: 1px;\n";
        }

        if ( isset($options['button_border_hover']['border-bottom']) && $options['button_border_hover']['border-bottom'] != "" ){
            $css .= "--button-border-hover-bottom:". esc_html($options['button_border_hover']['border-bottom']).";\n";
        }else{
            $css .= "--button-border-hover-bottom: 1px;\n";
        }

        if ( isset($options['button_border_hover']['border-left']) && $options['button_border_hover']['border-left'] != "" ){
            $css .= "--button-border-hover-left:". esc_html($options['button_border_hover']['border-left']).";\n";
        }else{
            $css .= "--button-border-hover-left: 1px;\n";
        }

        /* Button border hover style end */ 

        /* Theme Button Variable Declare For Tablet */

        if ( isset($options['button_style_tablet']['font-size']) && $options['button_style_tablet']['font-size'] != "" ){
            $css .= "--button-font-size-tablet:". esc_html($options['button_style_tablet']['font-size']).";\n";
        }else{
            $css .= "--button-font-size-tablet: 16px;\n";
        }
        
        if ( isset($options['button_style_tablet']['line-height']) && $options['button_style_tablet']['line-height'] != "" ){
            $css .= "--button-line-height-tablet:". esc_html($options['button_style_tablet']['line-height']).";\n";
        }else{
            $css .= "--button-line-height-tablet: 20px;\n";
        }

        if ( isset($options['button_style_tablet']['letter-spacing']) && $options['button_style_tablet']['letter-spacing'] != "" ){
            $css .= "--button-letter-spacing-tablet:". esc_html($options['button_style_tablet']['letter-spacing']).";\n";
        }else{
            $css .= "--button-letter-spacing-tablet:normal;\n";
        }

        if ( isset($options['button_padding_tablet']['border-top']) && $options['button_padding_tablet']['border-top'] != "" ){
            $css .= "--button-tablet-padding-top:". esc_html($options['button_padding_tablet']['border-top']).";\n";
        }else{
            $css .= "--button-tablet-padding-top: 5px;\n";
        }

        if ( isset($options['button_padding_tablet']['border-right']) && $options['button_padding_tablet']['border-right'] != "" ){
            $css .= "--button-tablet-padding-right:". esc_html($options['button_padding_tablet']['border-right']).";\n";
        }else{
            $css .= "--button-tablet-padding-right: 5px;\n";
        }

        if ( isset($options['button_padding_tablet']['border-bottom']) && $options['button_padding_tablet']['border-bottom'] != "" ){
            $css .= "--button-tablet-padding-bottom:". esc_html($options['button_padding_tablet']['border-bottom']).";\n";
        }else{
            $css .= "--button-tablet-padding-bottom: 5px;\n";
        }

        if ( isset($options['button_padding_tablet']['border-left']) && $options['button_padding_tablet']['border-left'] != "" ){
            $css .= "--button-tablet-padding-left:". esc_html($options['button_padding_tablet']['border-left']).";\n";
        }else{
            $css .= "--button-tablet-padding-left: 5px;\n";
        }

        /* Theme Button Variable Declare For Mobile */

        if ( isset($options['button_style_mobile']['font-size']) && $options['button_style_mobile']['font-size'] != "" ){
            $css .= "--button-font-size-mobile:". esc_html($options['button_style_mobile']['font-size']).";\n";
        }else{
            $css .= "--button-font-size-mobile: 14px;\n";
        }
        
        if ( isset($options['button_style_mobile']['line-height']) && $options['button_style_mobile']['line-height'] != "" ){
            $css .= "--button-line-height-mobile:". esc_html($options['button_style_mobile']['line-height']).";\n";
        }else{
            $css .= "--button-line-height-mobile: 24px;\n";
        }

        if ( isset($options['button_style_mobile']['letter-spacing']) && $options['button_style_mobile']['letter-spacing'] != "" ){
            $css .= "--button-letter-spacing-mobile:". esc_html($options['button_style_mobile']['letter-spacing']).";\n";
        }else{
            $css .= "--button-letter-spacing-mobile:normal;\n";
        }

        if ( isset($options['button_padding_mobile']['border-top']) && $options['button_padding_mobile']['border-top'] != "" ){
            $css .= "--button-mobile-padding-top:". esc_html($options['button_padding_mobile']['border-top']).";\n";
        }else{
            $css .= "--button-mobile-padding-top: 5px;\n";
        }

        if ( isset($options['button_padding_mobile']['border-right']) && $options['button_padding_mobile']['border-right'] != "" ){
            $css .= "--button-mobile-padding-right:". esc_html($options['button_padding_mobile']['border-right']).";\n";
        }else{
            $css .= "--button-mobile-padding-right: 5px;\n";
        }

        if ( isset($options['button_padding_mobile']['border-bottom']) && $options['button_padding_mobile']['border-bottom'] != "" ){
            $css .= "--button-mobile-padding-bottom:". esc_html($options['button_padding_mobile']['border-bottom']).";\n";
        }else{
            $css .= "--button-mobile-padding-bottom: 5px;\n";
        }

        if ( isset($options['button_padding_mobile']['border-left']) && $options['button_padding_mobile']['border-left'] != "" ){
            $css .= "--button-mobile-padding-left:". esc_html($options['button_padding_mobile']['border-left']).";\n";
        }else{
            $css .= "--button-mobile-padding-left: 5px;\n";
        }

        $css .= "}\n";

        return $css;
    }
}
