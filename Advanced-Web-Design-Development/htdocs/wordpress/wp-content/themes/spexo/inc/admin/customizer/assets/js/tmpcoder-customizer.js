jQuery( document ).ready(function($) {
	"use strict";

	/**
	 * Slider Customize Control
	 */

	// Set our slider defaults and initialise the slider
	$('.slider-custom-control').each(function(){
		var sliderValue = $(this).find('.customize-control-slider-value').val();
		var newSlider = $(this).find('.slider');
		var sliderMinValue = parseFloat(newSlider.attr('slider-min-value'));
		var sliderMaxValue = parseFloat(newSlider.attr('slider-max-value'));
		var sliderStepValue = parseFloat(newSlider.attr('slider-step-value'));

		newSlider.slider({
			value: sliderValue,
			min: sliderMinValue,
			max: sliderMaxValue,
			step: sliderStepValue,
			change: function(e,ui){
				// Important! When slider stops moving make sure to trigger change event so Customizer knows it has to save the field
				$(this).parent().find('.customize-control-slider-value').trigger('change');
	      }
		});
	});

	// Change the value of the input field as the slider is moved
	$('.slider').on('slide', function(event, ui) {
		$(this).parent().find('.customize-control-slider-value').val(ui.value);
	});

	// Reset slider and input field back to the default value
	$('.slider-reset').on('click', function() {
		var resetValue = $(this).attr('slider-reset-value');
		$(this).parent().find('.customize-control-slider-value').val(resetValue);
		$(this).parent().find('.slider').slider('value', resetValue);
	});

	// Update slider if the input field loses focus as it's most likely changed
	$('.customize-control-slider-value').blur(function() {
		var resetValue = $(this).val();
		var slider = $(this).parent().find('.slider');
		var sliderMinValue = parseInt(slider.attr('slider-min-value'));
		var sliderMaxValue = parseInt(slider.attr('slider-max-value'));

		// Make sure our manual input value doesn't exceed the minimum & maxmium values
		if(resetValue < sliderMinValue) {
			resetValue = sliderMinValue;
			$(this).val(resetValue);
		}
		if(resetValue > sliderMaxValue) {
			resetValue = sliderMaxValue;
			$(this).val(resetValue);
		}
		$(this).parent().find('.slider').slider('value', resetValue);
	});


	/**
	 * Dropdown Select2 Customize Control
	 */	
	$('.customize-control-dropdown-select2').each(function(){
		$('.customize-control-select2').select2({
			allowClear: true
		});
	});

	$(".customize-control-select2").on("change", function() {
		var select2Val = $(this).val();
		$(this).parent().find('.customize-control-dropdown-select2').val(select2Val).trigger('change');
	});
	
	/**
	 * Dimensional Customize Control
	 */	
	$(document).on('input', '.dimensional-input', function () {
		var $control = $(this).closest('.customize-control');
		var inputValues = {};

		// Collect the values of all dimensional inputs
		$control.find('.dimensional-input').each(function () {
			var dimension = $(this).data('dimension');
			inputValues[dimension] = $(this).val();
		});

		$control.find('.dimensional-hidden-value').val(JSON.stringify(inputValues)).trigger('change');
	});

	/**
	 * Googe Font Select Customize Control
	 */
	$(".google-fonts-list").each(function (i, obj) {
		if (!$(obj).hasClass("select2-hidden-accessible")) {
			$(obj).select2();
		}
	});

	$(".google-fonts-list").on("change", function () {
		var elementRegularWeight = $(this).closest(".google_fonts_select_control").find(".google-fonts-regularweight-style");
		var elementItalicWeight = $(this).closest(".google_fonts_select_control").find(".google-fonts-italicweight-style");
		var elementBoldWeight = $(this).closest(".google_fonts_select_control").find(".google-fonts-boldweight-style");
		var selectedFont = $(this).val();
		var customizerControlName = $(this).attr("control-name");

		// Clear existing dropdowns
		elementRegularWeight.empty();
		elementItalicWeight.empty();
		elementBoldWeight.empty();
		elementItalicWeight.prop("disabled", false);
		elementBoldWeight.prop("disabled", false);

		// Get the Google Fonts control object
		var bodyfontcontrol = _wpCustomizeSettings.controls[customizerControlName];

		// Find the selected font in the font list
		var index = bodyfontcontrol.tmpcoderfontslist.findIndex(font => font.font === selectedFont);

		if (index === -1) {
			console.error("Font not found in list: " + selectedFont);
			return;
		}

		var selectedFontData = bodyfontcontrol.tmpcoderfontslist[index];

		// Default to "regular" if no variants found
		var variants = selectedFontData.variants && selectedFontData.variants.length ? selectedFontData.variants : ["regular"];

		let weightLabels = {
			"100": "Thin 100",
			"200": "Extra Light 200",
			"300": "Light 300",
			"400": "Regular 400",
			"500": "Medium 500",
			"600": "Semi-Bold 600",
			"700": "Bold 700",
			"800": "Extra Bold 800",
			"900": "Black 900"
		};

		let addedVariants = new Set();

		// Populate the weight/style dropdowns
		var weightFound = false;
		$.each(variants, function (key, variant) {
			if (typeof variant !== "string") return;

			// Remove 'i' from the variant
			var cleanedVariant = variant.replace('i', '');

			if (!addedVariants.has(cleanedVariant)) {
				addedVariants.add(cleanedVariant);

				// Use the custom label if available
				var label = weightLabels[cleanedVariant] || cleanedVariant;

				elementRegularWeight.append($("<option></option>").val(cleanedVariant).text(label));

				// If the current variant is "400", mark it as found
				if (cleanedVariant === "400") {
					weightFound = true;
				}
			}
		});

		// If no weight is found, set 400 (Regular 400) as the default
		if (!weightFound) {
			elementRegularWeight.append($("<option></option>").val("400").text("Regular 400"));
		}

		// Set the regular weight dropdown to "400" by default
		elementRegularWeight.val("400");

		// Update the font category
		$(this).closest(".google_fonts_select_control").find(".google-fonts-category").val(selectedFontData.category);

		tmpcoderGetAllSelects($(this).closest(".google_fonts_select_control"));
	});

	$(".google_fonts_select_control select").on("change", function () {
		tmpcoderGetAllSelects($(this).closest(".google_fonts_select_control"));
	});

	function tmpcoderGetAllSelects($element) {
		var selectedFont = {
			font: $element.find(".google-fonts-list").val(),
			regularweight: $element.find(".google-fonts-regularweight-style").val() || "400",
			italicweight: $element.find(".google-fonts-italicweight-style").val() || "normal",
			boldweight: $element.find(".google-fonts-boldweight-style").val() || "normal",
			category: $element.find(".google-fonts-category").val()
		};

		$element.find(".customize-control-google-font-selection").val(JSON.stringify(selectedFont)).trigger("change");
	}

	/**
	 * WP ColorPicker Alpha Color Picker Control
	 */
	$('.wpcolorpicker-alpha-color-picker').each(function( i, obj ) {
		var colorPickerInput = $(this);
		var paletteColors = _wpCustomizeSettings.controls[$(this).attr('id')].colorpickerpalette;
		var options = {
			palettes: paletteColors,
			change: function(event, ui) {
				setTimeout(function(){
					colorPickerInput.trigger('change');
				},1);
			}
		};
		$(obj).wpColorPicker(options);
	} );

	/**
	 * TinyMCE customize Control
	 */
	$('.customize-control-tinymce-editor').each(function(){
		// Get the toolbar strings that were passed from the PHP Class
		var tinyMCEToolbar1String = _wpCustomizeSettings.controls[$(this).attr('id')].tmpcodertinymcetoolbar1;
		var tinyMCEToolbar2String = _wpCustomizeSettings.controls[$(this).attr('id')].tmpcodertinymcetoolbar2;
		var tinyMCEMediaButtons = _wpCustomizeSettings.controls[$(this).attr('id')].tmpcodermediabuttons;

		wp.editor.initialize( $(this).attr('id'), {
			tinymce: {
				wpautop: true,
				toolbar1: tinyMCEToolbar1String,
				toolbar2: tinyMCEToolbar2String
			},
			quicktags: true,
			mediaButtons: tinyMCEMediaButtons
		});
	});
	$(document).on( 'tinymce-editor-init', function( event, editor ) {
		editor.on('change', function(e) {
			tinyMCE.triggerSave();
			$('#'+editor.id).trigger('change');
		});
	});

});


/**
 * Load Google Fonts
 */
document.addEventListener("DOMContentLoaded", function() {
    let loadedFonts = new Set();

    function tmpcoder_load_google_font(cssVariable, fontLinkId) {
        let fontFamily = getComputedStyle(document.documentElement).getPropertyValue(cssVariable).trim().replace(/['"]/g, "").split(",")[0].trim();
        if (fontFamily && fontFamily !== "Poppins" && !loadedFonts.has(fontFamily)) {
            loadedFonts.add(fontFamily);
            let weights = "100,200,300,400,500,600,700,800,900";
            let googleFontUrl = `https://fonts.googleapis.com/css?family=${encodeURIComponent(fontFamily)}:${weights}&display=swap`;
            
            let existingFontLink = document.getElementById(fontLinkId);
            if (existingFontLink && existingFontLink.href === googleFontUrl) return;
            if (existingFontLink) existingFontLink.remove();
            
            let fontLink = document.createElement("link");
            fontLink.id = fontLinkId;
            fontLink.rel = "stylesheet";
            fontLink.href = googleFontUrl;
            document.head.appendChild(fontLink);
        }
    }

    const fontVariables = Array(
        '--theme-font-family', '--heading1-font-family', '--heading2-font-family', '--heading3-font-family', '--heading4-font-family', '--heading5-font-family', '--heading6-font-family', '--button-font-family'
    );
    
    fontVariables.forEach((cssVar, index) => tmpcoder_load_google_font(cssVar, `tmpcoder-google-font-${index + 1}`));

    new MutationObserver(() => fontVariables.forEach((cssVar, index) => tmpcoder_load_google_font(cssVar, `tmpcoder-google-font-${index + 1}`)))
        .observe(document.documentElement, { attributes: true, attributeFilter: ["style"] });
});
