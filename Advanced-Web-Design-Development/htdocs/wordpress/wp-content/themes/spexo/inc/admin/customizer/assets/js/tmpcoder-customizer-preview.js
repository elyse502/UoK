(function ($) {

    // Container width settings (general, tablet, and mobile)
    const containerWidthSettings = [
        { setting: 'container_width', variable: '--tmpcoder-container-width' },
        { setting: 'container_width_tablet', variable: '--tmpcoder-tablet-container-width' },
        { setting: 'container_width_mobile', variable: '--tmpcoder-mobile-container-width' }
    ];

    // handle binding container width settings to CSS variables
    function tmpcoder_update_container_width(settingsArray) {
        settingsArray.forEach(function (setting) {
            wp.customize(setting.setting, function (value) {
                value.bind(function (newVal) {
                    document.documentElement.style.setProperty(setting.variable, newVal + 'px');
                });
            });
        });
    }
    
    tmpcoder_update_container_width(containerWidthSettings);

    // Define settings and corresponding CSS variables for general settings
    const settingsToCssVars = {
        'primary_color': '--theme-primary-color',
        'secondary_color': '--theme-secondary-color',
        'accent_color': '--theme-accent-color',
        'site_background_color': '--theme-background-color',
        'link_color': '--theme-link-color',
        'link_hover_color': '--theme-link-hover-color',
        'border_color': '--button-border-color',
        'theme_color_1': '--theme-global-color-1',
        'theme_color_2': '--theme-global-color-2',
        'theme_color_3': '--theme-global-color-3',
        'theme_color_4': '--theme-global-color-4',
        'button_border_border-color': '--button-border-color',
        'button_border_hover_border-color': '--button-border-hover-color'
    };

    // Bind general settings to CSS variables
    Object.keys(settingsToCssVars).forEach((setting) => {
        const cssVar = settingsToCssVars[setting];
        wp.customize(setting, function (value) {
            value.bind(function (newValue) {
                document.documentElement.style.setProperty(cssVar, newValue);
            });
        });
    });

    // Function to update font CSS variables (including heading, tablet, and mobile settings)
    function tmpcoder_update_font_css_variable(control, variable, isPixel) {
        wp.customize(control, function (value) {
            value.bind(function (newValue) {
                document.documentElement.style.setProperty(variable, newValue + (isPixel ? 'px' : ''));
            });
        });
    }

    // Font controls
    const fontControls = [
        ['site_fonts_options_font-size', '--theme-font-size', true],
        ['site_fonts_options_line-height', '--theme-line-height'],
        ['site_fonts_options_letter-spacing', '--theme-letter-spacing', true],
        ['site_fonts_options_color', '--theme-text-color']
    ];

    fontControls.forEach(([control, variable, isPixel = false]) => {
        tmpcoder_update_font_css_variable(control, variable, isPixel);
    });

    // Heading Controls (H1-H6)
    for (let i = 1; i <= 6; i++) {
        const headingControls = [
            [`heading_${i}_text-transform`, `--heading${i}-text-transform`],
            [`heading_${i}_font-size`, `--heading${i}-font-size`, true],
            [`heading_${i}_line-height`, `--heading${i}-line-height`],
            [`heading_${i}_letter-spacing`, `--heading${i}-letter-spacing`, true],
            [`heading_${i}_color`, `--heading${i}-text-color`]
        ];

        headingControls.forEach(([control, variable, isPixel = false]) => {
            tmpcoder_update_font_css_variable(control, variable, isPixel);
        });

        tmpcoder_update_font_family(`heading_${i}_font-family`, `--heading${i}-font`);
    }

    // Tablet and Mobile Font and Heading Controls
    const responsiveControls = [
        { suffix: 'tablet', controls: [
            ['site_fonts_options_tablet_font-size', '--theme-font-size-tablet', true],
            ['site_fonts_options_tablet_line-height', '--theme-line-height-tablet'],
            ['site_fonts_options_tablet_letter-spacing', '--theme-letter-spacing-tablet', true],
            ['button_style_tablet_font-size', '--button-font-size-tablet', true],
            ['button_style_tablet_line-height', '--button-line-height-tablet'],
            ['button_style_tablet_letter-spacing', '--button-letter-spacing-tablet', true]
        ]}, 
        { suffix: 'mobile', controls: [
            ['site_fonts_options_mobile_font-size', '--theme-font-size-mobile', true],
            ['site_fonts_options_mobile_line-height', '--theme-line-height-mobile'],
            ['site_fonts_options_mobile_letter-spacing', '--theme-letter-spacing-mobile', true],
            ['button_style_mobile_font-size', '--button-font-size-mobile', true],
            ['button_style_mobile_line-height', '--button-line-height-mobile'],
            ['button_style_mobile_letter-spacing', '--button-letter-spacing-mobile', true]
        ]}
    ];

    responsiveControls.forEach(({ suffix, controls }) => {
        controls.forEach(([control, variable, isPixel = false]) => {
            wp.customize(control, function (value) {
                value.bind(function (newValue) {
                    document.documentElement.style.setProperty(variable, newValue + (isPixel ? 'px' : ''));
                });
            });
        });

        // Add H1-H6 controls dynamically for Tablet and Mobile
        for (let i = 1; i <= 6; i++) {
            const headingControls = [
                [`heading_${i}_tablet_font-size`, `--heading${i}-font-size-tablet`, true],
                [`heading_${i}_tablet_line-height`, `--heading${i}-line-height-tablet`],
                [`heading_${i}_tablet_letter-spacing`, `--heading${i}-letter-spacing-tablet`, true],
                [`heading_${i}_mobile_font-size`, `--heading${i}-font-size-mobile`, true],
                [`heading_${i}_mobile_line-height`, `--heading${i}-line-height-mobile`],
                [`heading_${i}_mobile_letter-spacing`, `--heading${i}-letter-spacing-mobile`, true]
            ];

            headingControls.forEach(([control, variable, isPixel = false]) => {
                tmpcoder_update_font_css_variable(control, variable, isPixel);
            });
        }
    });

    // Handle font-family JSON (with font and weight)
    function tmpcoder_update_font_family(control, variablePrefix) {
        wp.customize(control, function (value) {
            value.bind(function (newVal) {
                let fontDetails = JSON.parse(newVal);
                let fontFamily = fontDetails.font ? `"${fontDetails.font}", ${fontDetails.category}` : '';
                let fontWeight = fontDetails.regularweight || 'regular';

                // Set CSS Variables
                document.documentElement.style.setProperty(`${variablePrefix}-family`, fontFamily);
                document.documentElement.style.setProperty(`${variablePrefix}-weight`, fontWeight);
            });
        });
    }

    // Apply font-family JSON to font-family control
    tmpcoder_update_font_family('site_fonts_options_font-family', '--theme-font');
    tmpcoder_update_font_family('button_style_font-family', '--button-font');

    // Button style settings (general, tablet, and mobile)
    const buttonSettings = [
        { setting: 'button_style_text-align', variable: '--button-text-align' },
        { setting: 'button_style_text-transform', variable: '--button-text-transform' },
        { setting: 'button_style_font-size', variable: '--button-font-size', suffix: 'px' },
        { setting: 'button_style_line-height', variable: '--button-line-height', suffix: 'px' },
        { setting: 'button_style_letter-spacing', variable: '--button-letter-spacing', suffix: 'px' },
        { setting: 'button_style_color', variable: '--button-text-color' },
        { setting: 'site_button_text_hover', variable: '--button-text-hover-color' },
        { setting: 'site_button_color', variable: '--theme-button-background' },
        { setting: 'site_button_color_hover', variable: '--theme-button-background-hover' },
        { setting: 'button_border_border-style', variable: '--button-border-type' },
        { setting: 'button_border_hover_border-style', variable: '--button-border-hover-type' },
        { setting: 'button_border_radius', variable: '--button-border-radius', suffix: 'px' },
    ];

    buttonSettings.forEach(function (setting) {
        wp.customize(setting.setting, function (value) {
            value.bind(function (newVal) {
                let updatedValue = setting.suffix ? newVal + setting.suffix : newVal;
                document.documentElement.style.setProperty(setting.variable, updatedValue);
            });
        });
    });

    // Handle button borders (with separate values for top, right, bottom, left)
    function tmpcoder_update_button_borders(control, variablePrefix) {
        wp.customize(control, function (value) {
            value.bind(function (newVal) {
                let borderValues = JSON.parse(newVal);
                let top = parseFloat(borderValues.top);
                let right = parseFloat(borderValues.right);
                let bottom = parseFloat(borderValues.bottom);
                let left = parseFloat(borderValues.left);

                if (!isNaN(top) && !isNaN(right) && !isNaN(bottom) && !isNaN(left)) {
                    document.documentElement.style.setProperty(`${variablePrefix}-top`, top + 'px');
                    document.documentElement.style.setProperty(`${variablePrefix}-right`, right + 'px');
                    document.documentElement.style.setProperty(`${variablePrefix}-bottom`, bottom + 'px');
                    document.documentElement.style.setProperty(`${variablePrefix}-left`, left + 'px');
                }
            });
        });
    }

    tmpcoder_update_button_borders('button_border', '--button-border');
    tmpcoder_update_button_borders('button_border_hover', '--button-border-hover');

    // Handle button padding (with separate values for top, right, bottom, left)
    function tmpcoder_update_button_padding(control, variablePrefix) {
        wp.customize(control, function (value) {
            value.bind(function (newVal) {
                let paddingValues = JSON.parse(newVal);
                let top = parseFloat(paddingValues.top);
                let right = parseFloat(paddingValues.right);
                let bottom = parseFloat(paddingValues.bottom);
                let left = parseFloat(paddingValues.left);

                if (!isNaN(top) && !isNaN(right) && !isNaN(bottom) && !isNaN(left)) {
                    document.documentElement.style.setProperty(`${variablePrefix}-top`, top + 'px');
                    document.documentElement.style.setProperty(`${variablePrefix}-right`, right + 'px');
                    document.documentElement.style.setProperty(`${variablePrefix}-bottom`, bottom + 'px');
                    document.documentElement.style.setProperty(`${variablePrefix}-left`, left + 'px');
                }
            });
        });
    }

    tmpcoder_update_button_padding('button_padding', '--button-padding');
    tmpcoder_update_button_padding('button_padding_tablet', '--button-tablet-padding');
    tmpcoder_update_button_padding('button_padding_mobile', '--button-mobile-padding');

})(jQuery);
