( function( $ ) {
	"use strict";

	window.pluginCount = tmpcoder_localize.pluginCount;
	window.pluginSucessCount = tmpcoder_localize.pluginSucessCount;
	window.pluginsList = tmpcoder_localize.pluginsList;
	window.pluginsListFile = tmpcoder_localize.pluginsListFile;
	window.pluginNotInstallCount = tmpcoder_localize.pluginNotInstallCount;
	window.installedPluginNotActivated = tmpcoder_localize.installedPluginNotActivated;

	$( document ).on( 'click', '.tmpcoder-notice .notice-dismiss', function(){
		jQuery.post( tmpcoder_localize.ajaxUrl, {
			action: 'tmpcoder_dismissed_handler',
			notice: $( this ).closest( '.tmpcoder-notice' ).data( 'notice' ),
		});
		$( '.tmpcoder-notice' ).hide();
	});

	$( document ).on('wp-plugin-install-success' , function( event, response ){

		event.preventDefault();

		window.pluginSucessCount++;

		console.log('pluginSucessCount', window.pluginSucessCount);
		console.log('pluginCount', window.pluginCount);

		var activatingText = tmpcoder_localize.recommendedPluiginActivatingText;
		var tmpcoderSitesLink = tmpcoder_localize.tmpcoderSitesLink;
		var tmpcoderPluginRecommendedNonce = tmpcoder_localize.tmpcoderPluginManagerNonce;
		var $message = jQuery(event.target);
		var $init = ['elementor/elementor.php','sastra-essential-addons-for-elementor/sastra-essential-addons-for-elementor.php','redux-framework/redux-framework.php'];

		if (window.pluginSucessCount == window.pluginCount || window.pluginSucessCount == window.pluginNotInstallCount )
		{
			setTimeout( function() {

				$.ajax({
					url: tmpcoder_localize.ajaxUrl,
					type: 'POST',
					data: {
						'action' 	: 'tmpcoder_recommended_plugin_activate',
						'security'  : tmpcoderPluginRecommendedNonce,
						'init' 	 	: $init,
					},
				})
				.done(function(result){
					console.error( result );
					if( result.success ) {
						$message.removeClass( 'tmpcoder-activate-recommended-plugin tmpcoder-install-recommended-plugin button button-primary install-now activate-now updating-message' );
						$message.parent('.tmpcoder-addon-link-wrapper').parent('.tmpcoder-recommended-plugin').addClass('active');
						window.location.href = tmpcoder_localize.tmpcoderRedirectionLink;
					} else {
						$message.removeClass( 'updating-message' );
					}
				});
			}, 1200 );
		}
	});

	$(document).on('click', '.tmpcoder-notice .button-primary', function( e ) {
	    
	    console.log('pluginsList:', window.pluginsList);
	    
	    if ( 'install-activate' === $(this).data('action') && ! $( this ).hasClass('init') ) {
			var $self = $(this),
				$button = jQuery( e.target ),
				$document   = jQuery(document),
				$href = $self.attr('href');

			$self.addClass('init');

			$self.html('Installing Templates Kit Library <span class="dot-flashing"></span>');

			if ( $button.hasClass( 'updating-message' ) || $button.hasClass( 'button-disabled' ) ) {
				return;
			}

			if ( wp.updates.shouldRequestFilesystemCredentials && ! wp.updates.ajaxLocked ) {
				wp.updates.requestFilesystemCredentials( e );

				$document.on( 'credential-modal-cancel', function() {
					var $message = $( '.tmpcoder-install-recommended-plugin.updating-message' );

					$message
						.addClass('tmpcoder-activate-recommended-plugin')
						.removeClass( 'updating-message tmpcoder-install-recommended-plugin' )
						.text( wp.updates.l10n.installNow );

					wp.a11y.speak( wp.updates.l10n.updateCancel, 'polite' );
				} );
			}

			if ( window.pluginsList.length > 0 ){
			    var plugins = window.pluginsList;

    			$.each( plugins, function( index, plugin_slug ) {
    
    				// Add each plugin activate request in Ajax queue.
    				// @see wp-admin/js/updates.js
    				wp.updates.queue.push( {
    					action: 'install-plugin', // Required action.
    					data:   {
    						slug: plugin_slug
    					}
    				});	
    			});
    
    			// Required to set queue.
    			wp.updates.queueChecker();
			
			}else{
			    var tmpcoderPluginRecommendedNonce = tmpcoder_localize.tmpcoderPluginManagerNonce;
			    $.ajax({
					url: tmpcoder_localize.ajaxUrl,
					type: 'POST',
					data: {
						'action' 	: 'tmpcoder_recommended_plugin_activate',
						'security'  : tmpcoderPluginRecommendedNonce,
						'init' 	 	: window.installedPluginNotActivated,
					},
				})
				.done(function(result){
					if( result.success ) {
				        //  $message.removeClass( 'tmpcoder-activate-recommended-plugin tmpcoder-install-recommended-plugin button button-primary install-now activate-now updating-message' );
				        //  $message.parent('.tmpcoder-addon-link-wrapper').parent('.tmpcoder-recommended-plugin').addClass('active');
						window.location.href = tmpcoder_localize.tmpcoderRedirectionLink;
					} else {
				        //  $message.removeClass( 'updating-message' );
					}
				});
			}

			e.preventDefault();
		}
	} );

	/**
	 * After plugin active redirect and deactivate activation notice
	 */

	$( document ).on('tmpcoder-after-plugin-active', function( event, tmpcoderSitesLink, activatedSlug ){

	 	event.preventDefault();

		if ( activatedSlug.indexOf( 'elementor' ) >= 0 || activatedSlug.indexOf( 'jeg-elementor-kit' ) >= 0 ) {
			
			window.location.href = tmpcoderSitesLink + '&tmpcoder-disable-activation-notice';
		}
 	});

} )( jQuery );
