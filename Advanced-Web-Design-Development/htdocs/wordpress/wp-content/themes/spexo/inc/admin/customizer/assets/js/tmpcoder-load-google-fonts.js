document.addEventListener("DOMContentLoaded", function() {
    /**
	 * Load Google Fonts
	 */

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

    const fontVariables = [
        '--theme-font-family', '--heading1-font-family', '--heading2-font-family', '--heading3-font-family', 
        '--heading4-font-family', '--heading5-font-family', '--heading6-font-family', '--button-font-family'
    ];
    
    fontVariables.forEach((cssVar, index) => tmpcoder_load_google_font(cssVar, `tmpcoder-google-font-${index + 1}`));

    new MutationObserver(() => fontVariables.forEach((cssVar, index) => tmpcoder_load_google_font(cssVar, `tmpcoder-google-font-${index + 1}`)))
        .observe(document.documentElement, { attributes: true, attributeFilter: ["style"] });
});
