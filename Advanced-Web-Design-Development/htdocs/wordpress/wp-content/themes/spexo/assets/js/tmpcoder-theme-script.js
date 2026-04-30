(function($) {
    "use strict";

    jQuery(document).ready(function(){
        jQuery(".select2-container").addClass("elementor-field-textual");

        $(document).on('click keydown', '.opener', function (e) {
            // Handle click, Enter, or tap
            if (e.type === 'click' || (e.type === 'keydown' && (e.key === 'Enter' || e.keyCode === 13))) {
                e.preventDefault();
                console.log("menu open");
                let $this = $(this);

                if ($this.next().hasClass('show')) {
                    // Close submenu
                    $this.parent().removeClass('active');
                    $this.next().removeClass('show');
                    $this.next().slideUp(350);
                } else {
                    // Close all open submenus first
                    $this.closest('ul').find('li ul').removeClass('show').slideUp(350);
                    // Open the current submenu
                    $this.parent().addClass('active');
                    $this.next().addClass('show');
                    $this.next().slideDown(350);
                }
            }
        });
        
        tmpcoder_menu_toggle_action();

    });
    
    jQuery(window).resize(function(){
        tmpcoder_menu_toggle_action();
    });
    
    
    function tmpcoder_menu_toggle_action(){
        jQuery('.site-navigation .wp-mobile-toggle-wrap').removeClass('wp-mobile-toggle-open');
        jQuery('.wp-default-primary-menu .page_item_has_children').each(function(){
            jQuery(this).find('> ul').attr('style','');
        });
        if ( jQuery(window).width() > 1024 ){
            jQuery('.site-navigation .wp-mobile-toggle-wrap').next().show();
            
            jQuery('.wp-default-primary-menu > ul').addClass('primary-menu');   
            setTimeout(focusMenuWithChildren, 100);
            
        }else{
            jQuery('.site-navigation .wp-mobile-toggle-wrap').next().hide();
            
            jQuery('.wp-default-primary-menu ul li').removeClass('focus');
            
        }
    }

    jQuery(document).on('change','.select2-hidden-accessible',function(){
        jQuery(".select2-container").addClass("elementor-field-textual");
    });

    jQuery(document).on('click keydown', '.site-navigation .wp-mobile-toggle-wrap', function (event) {
        // Check if it's a click or the Enter key (keyCode 13)
        if (event.type === 'click' || (event.type === 'keydown' && (event.key === 'Enter' || event.keyCode === 13))) {
            if (!jQuery(this).hasClass('wp-mobile-toggle-open')) {
                jQuery(this).addClass('wp-mobile-toggle-open');
                jQuery(this).next().slideDown('slow');
            } else {
                jQuery(this).removeClass('wp-mobile-toggle-open');
                jQuery(this).next().slideUp('slow');
            }
            // Prevent default behavior for the Enter key to avoid unintended scrolling
            event.preventDefault();
        }
    });
    
    // Close menu on Escape key press
    jQuery(document).on('keydown', function (event) {
        if (event.key === 'Escape' || event.keyCode === 27) {
            const openToggles = jQuery('.site-navigation .wp-mobile-toggle-wrap.wp-mobile-toggle-open');
            openToggles.removeClass('wp-mobile-toggle-open'); // Remove open class
            openToggles.next().slideUp('slow'); // Close the menu
        }
    });
    
    jQuery('header .page_item_has_children').each(function(){
        jQuery(this).find('> a').after('<span class="opener" tabindex="0" aria-expanded="false"></span>');
    });
    
    function focusMenuWithChildren(){
        // Get all the link elements within the primary menu.
        var links, i, len,
            menu = document.querySelector( '.wp-default-primary-menu' );

        if (!menu || window.innerWidth < 1024) {
            return; // Exit if no menu or screen width is less than 1024
        }
        
        links = menu.getElementsByTagName( 'a' );

        // Each time a menu link is focused or blurred, toggle focus.
        for ( i = 0, len = links.length; i < len; i++ ) {
            links[i].addEventListener( 'focus', toggleFocus, true );
            links[i].addEventListener( 'blur', toggleFocus, true );
        }

        //Sets or removes the .focus class on an element.
        function toggleFocus() {
            
            if (window.innerWidth < 1024) {
                return; // Exit if no menu or screen width is less than 1024
            }
            
            var self = this;
            // Move up through the ancestors of the current link until we hit .primary-menu.
            while ( -1 === self.className.indexOf( 'primary-menu' ) ) {
                // On li elements toggle the class .focus.
                if ( 'li' === self.tagName.toLowerCase() ) {
                    if ( -1 !== self.className.indexOf( 'focus' ) ) {
                        self.className = self.className.replace( ' focus', '' );
                    } else {
                        self.className += ' focus';
                    }
                }
                self = self.parentElement;
            }
        }
    }   

    // smooth scolling
    const navigation_selector = 'header nav.site-navigation a[href^="#"], footer nav.site-navigation a[href^="#"]';
    const links = document.querySelectorAll(navigation_selector);

    links.forEach(link => {
        link.addEventListener('click', function(event) {
            event.preventDefault();
            const targetId = this.getAttribute('href');
            const targetElement = document.querySelector(targetId);
            
            targetElement.scrollIntoView({
            behavior: 'smooth'
            });
        });
    });
    
})(jQuery);

/*---- Preloader Start -----*/
jQuery(window).on("load", function() {
  "use strict";
    // Preloader
    jQuery('.parent-preloader').delay(350).fadeOut('slow');
});
 /*---- Preloader End -----*/

document.addEventListener("DOMContentLoaded", function() {
  const hash = window.location.hash;
  if (hash) {
    const elementId = hash.substring(1);
    const element = document.getElementById(elementId);
    if (element) {
      element.scrollIntoView({ behavior: "smooth", block: "start" });
    const themePrimaryColor = getComputedStyle(document.documentElement).getPropertyValue('--theme-global-color-2').trim();
      element.style.transition = "background-color 0.3s ease";
      element.style.backgroundColor = themePrimaryColor || "#f0f8ff";
      setTimeout(() => {
        element.style.backgroundColor = "";
      }, 1000);
    }
  }
});

/*----- One page page scrolling -----*/
document.addEventListener("DOMContentLoaded", function () {
  if (window.location.hash) {
    const hash = window.location.hash;
    const target = document.querySelector(hash);

    if (target) {
      // Add small delay to wait until full page loads
      setTimeout(() => {
        target.scrollIntoView({ behavior: "smooth" });
      }, 300);
    }
  }
});