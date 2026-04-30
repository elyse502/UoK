(function($){

    function showNotice(message, messageType) {
        $('.theme-wizard-main').before('<div class="theme-wizard-notice notice notice-'+ messageType +'">'+ message +'</div>');
    }

    function addRightSign(tab_type=''){
        if ( $('[data-tab="'+ tab_type +'"] .wizard-right-sign').length == 0 ){
            $('[data-tab="'+ tab_type +'"]').append('<span class="wizard-right-sign dashicons dashicons-yes-alt"></span>');
        }
    }

    function removeNotice() {
        $(".theme-wizard-notice").slideUp(300, function() { $(this).remove(); });
    }

    $('[data-tab="theme-welcome"]').addClass('nav-tab-active');
    $('#theme-welcome').addClass('active');
    
    $(document).ready(function(){

        if ( tmpcoderMessages.wizard_step == '' ){
            $('[data-tab="theme-welcome"]').addClass('nav-tab-active');
            $('#theme-welcome').addClass('active');
        }else if ( tmpcoderMessages.wizard_step == '1' ){
            addRightSign('theme-welcome');
            $('[data-tab="theme-welcome"]').addClass('disabled');
            setTimeout(() => {
                $('[data-tab="install-plugins"]').removeClass('disabled');
                $('[data-tab="install-plugins"]').trigger('click');
            }, 100);
        }else if ( tmpcoderMessages.wizard_step == '2' ){
            addRightSign('theme-welcome');
            $('[data-tab="theme-welcome"]').addClass('disabled');
            addRightSign('install-plugins');
            $('[data-tab="install-plugins"]').addClass('disabled');
            setTimeout(() => {
                $('[data-tab="get-pro-plugins"]').removeClass('disabled');
                $('[data-tab="get-pro-plugins"]').trigger('click');
            }, 100);
        }
        else{
            setTimeout(() => {
                $('[data-tab="install-plugins"]').trigger('click');
            }, 100);
        }
    });
    
    /* ----------- skip wizard with confirmation - [START] -----------*/

    jQuery(document).on('click', '.skip-theme-wizard', function(e) {
        e.preventDefault();    
        tmpcoder_skip_theme_wizard_confirm_popup_open();
    });
    
    jQuery(document).on('click', '.tmpcoder-skip-theme-wizard-confirm-button', function(e) {
        e.preventDefault();
        jQuery('.tmpcoder-skip-theme-wizard-popup-wrap').fadeOut();

        tmpcoder_skip_theme_setup_wizard();
    });

    jQuery(document).on('click','.popup-close', function(){
        jQuery('#tmpcoder-skip-theme-wizard-confirm-popup').fadeOut();
        jQuery('.tmpcoder-admin-popup').fadeOut();
        jQuery('.tmpcoder-skip-theme-wizard-popup-wrap').fadeOut();
    })
    
    function tmpcoder_skip_theme_wizard_confirm_popup_open() {
        jQuery('#tmpcoder-skip-theme-wizard-confirm-popup').fadeIn();
        jQuery('.tmpcoder-skip-theme-wizard-popup-wrap').fadeIn();
        jQuery('.tmpcoder-admin-popup').fadeIn();
    }
    
    function tmpcoder_skip_theme_setup_wizard() {
        window.location.href = tmpcoderMessages.tmpcoder_admin_url;
    }
    /* ----------- skip wizard with confirmation - [END] -----------*/

    $( document ).on('click', '#theme-welcome .next-step-btn', function(){
        $('[data-tab="install-plugins"]').removeClass('disabled');
        $('[data-tab="install-plugins"]').trigger('click');
        addRightSign('theme-welcome');
    });

    $(document).on('submit', '.install-plugins-form', function(e){
        e.preventDefault();

        $('.process-loader').removeClass('hide');
        $('.process-loader .loader-text').text(tmpcoderMessages.required_plugin_installing);

        var formdata = $(this).serialize();

        console.log('formdata', $(this).serializeArray());

        if( formdata['plugins'] ){
            formdata['plugins'] = JSON.stringify(formdata['plugins']);
        }

        $.ajax({
            url: ajaxurl,
            type: 'POST',
            data: formdata,
            beforeSend: function () {
                //Before sending, like showing a loading animation                        
            },
            success: function(resp){

                if( resp.includes('00000') ){
                    var dataResp = resp.split('00000');
                    resp = JSON.parse(dataResp[1]);
                }
                
                $('.process-loader').addClass('hide');
                $('.process-loader .loader-text').text('');

                if( resp.success ){

                    showNotice(resp.data.message, 'success');
                    setTimeout(removeNotice, 5000);

                    addRightSign('install-plugins');
                    $('[data-tab="install-plugins"]').addClass('disabled');
                    $('[data-tab="get-pro-plugins"]').removeClass('disabled');
                    $('[data-tab="get-pro-plugins"]').trigger('click');

                }else{
                    if ( resp?.data?.message != "" ){
                        showNotice(resp.data.message,"error");
                        setTimeout(removeNotice, 5000);
                    }
                }
            },
            error: function(err) {
                $('.process-loader').addClass('hide');
                $('.process-loader .loader-text').text('');
                showNotice(tmpcoderMessages.network_error, "error");
                setTimeout(removeNotice, 5000);
            },
        });
    });

    function tmpcoder_get_recommended_plugins(){

        $('.process-loader').removeClass('hide');
        $('.process-loader .loader-text').text(tmpcoderMessages.getting_required_plugins);

        var js_exist = $('#templatescoder-core-import-demos-js-extra').length;
        if ( $('#select-demo #templatescoder-core-import-demos-js-extra').length == 1 ) {
            js_exist = 0;
        }

        $.ajax({
            url: ajaxurl,
            type: 'POST',
            data: "action=tmpcoder_get_recommended_plugins&js_exist="+ js_exist +"&nonce="+ tmpcoderMessages.get_plugins_nonce,
            // dataType: 'json',
            beforeSend: function () {
                //Before sending, like showing a loading animation
            },
            success: function(resp){

                $('.process-loader').addClass('hide');
                $('.process-loader .loader-text').text('');

                if( resp.success ){
                        
                    if (resp.data.skip_this){
                        $('.get-pro-plugins').removeClass('disabled');
                        $('.get-pro-plugins').trigger('click');
                        addRightSign('install-plugins');
                    }

                    if ( resp?.data?.plugins ){
                        
                        var plg_html = '<div class="feature-section recommended-plugins three-col demo-import-boxed">';
                        if ( resp.data.plugins.length > 0 ){
                            resp.data.plugins.forEach(pluginRow => {
                                plg_html += '<div class="col plugin_box"><div class="theme-grid-box">';
                                plg_html += '<a target="_blank" title="'+ pluginRow.name +'" href="'+ pluginRow.link +'"><img src="'+ pluginRow.image +'" alt="plugin box image" class="demo-preview plugin-preview" loading="lazy"></a>';

                                plg_html += '<div class="action_bar "><span class="plugin_name">'+ pluginRow.name +'</span>';
                                plg_html += '<span class="plugin-card-plugin action_button active">';
                                if ( pluginRow.activated ){
                                    plg_html += '<label>'+ tmpcoderMessages.installed_and_activated +'</label>';
                                }else if ( pluginRow.installed ){
                                    plg_html += '<input type="checkbox" class="plugin-checkbox" id="plugins-'+ pluginRow.slug +'" name="plugins['+ pluginRow.slug +']" value="1" checked /><label for="plugins-'+ pluginRow.slug +'">'+ tmpcoderMessages.installed_and_activate +'</label>';
                                }else{
                                    plg_html += '<input type="checkbox" class="plugin-checkbox" name="plugins['+ pluginRow.slug +']" value="1" checked /><label>'+ tmpcoderMessages.install_and_activate +'</label>';
                                }
                                plg_html += '</span>';
                                plg_html += '</div>';

                                plg_html += '</div></div>';
                            });
                        }
                        plg_html += '</div>';

                        plg_html += tmpcoderMessages.form_nonce; // nonce passing.
                        
                        plg_html += '<div class="next-step-action"><input type="hidden" name="action" value="tmpcoder_install_recommended_plugins" />';

                        if ( resp.data.next_step ){
                            plg_html += '<button type="submit" class="button button-primary next-step-btn">'+ resp.data.next_step +'</button>';
                        }else{
                            plg_html += '<button type="submit" class="button button-primary next-step-btn">'+ tmpcoderMessages.next_step_btn +'</button>';
                        }
                        plg_html += '</div>';

                        plg_html = '<h2 class="wizard-heading">'+ tmpcoderMessages.install_required_plugins +'</h2><p class="wizard-title-text">'+ tmpcoderMessages.install_required_plugins_text +'</p><form class="install-plugins-form" method="POST">'+ plg_html +'</form>';

                        $('#install-plugins .tmpcoder-message-box').html(plg_html);

                    }
                    
                }else{
                }
            },
            error: function(err) {
                $('.process-loader').addClass('hide');
                $('.process-loader .loader-text').text('');
            },
        });
    }

    function tmpcoder_get_pro_plugin_info(){

        $('.process-loader').removeClass('hide');
        $('.process-loader .loader-text').text(tmpcoderMessages.loading_pro_plugin_info);

        $.ajax({
            url: ajaxurl,
            type: 'POST',
            data: "action=tmpcoder_get_pro_plugin_info&nonce="+ tmpcoderMessages.get_pro_addons_info_nonce,
            // dataType: 'json',
            beforeSend: function () {
                //Before sending, like showing a loading animation
            },
            success: function(resp){

                if( resp.success ){
                    $('.process-loader').addClass('hide');
                    $('.process-loader .loader-text').text('');
                    $('#get-pro-plugins .pro-plugins-part').html(resp.data.data);
                }else{

                    $('.process-loader').addClass('hide');
                    $('.process-loader .loader-text').text('');
                    $('#get-pro-plugins .pro-plugins-part').html(tmpcoderMessages.license_error);
                }
            },
            error: function(err) {
                console.log('err', err);
                $('.process-loader').addClass('hide');
                $('.process-loader .loader-text').text('');
            },
        });
    }

    $(document).on('click', '.theme-wizard-nav .nav-tab', function(){
        if ( $(this).hasClass('disabled') ){
            return;
        }
        const parentRef = $(this).closest('.theme-wizard-nav');
        const tab_id = $(this).attr('data-tab');
        parentRef.find('.nav-tab').removeClass('nav-tab-active');
        $('.tab-content').removeClass('active');
        $(this).addClass('nav-tab-active');
        $('#'+ tab_id).addClass('active');

        if ( tab_id == 'install-plugins' ){
            tmpcoder_get_recommended_plugins();
        }

        if ( tab_id == 'get-pro-plugins' ){
            tmpcoder_get_pro_plugin_info();
        }
    });

})(jQuery)