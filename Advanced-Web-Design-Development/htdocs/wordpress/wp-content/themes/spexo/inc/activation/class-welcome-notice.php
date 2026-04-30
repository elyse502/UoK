<?php

if ( ! defined( 'ABSPATH' ) ) {
	exit; // Exit if accessed directly.
}

/**
 * Welcome Notice class.
 */
class Tmpcoder_Welcome_Notice {

	/**
	** Constructor.
	*/
	public function __construct() {

		// Render Notice
		add_action( 'admin_notices', array($this, 'tmpcoder_render_notice') );

		// Enque AJAX Script
		add_action( 'admin_enqueue_scripts', array($this, 'tmpcoder_admin_enqueue_scripts'), 5 );

		add_action( 'wp_ajax_tmpcoder_dismissed_handler', array($this, 'tmpcoder_dismissed_handler') );

		// Reset
		add_action( 'switch_theme', array($this, 'tmpcoder_reset_notices') );
		add_action( 'after_switch_theme', array($this, 'tmpcoder_reset_notices') );

		// Install Plugins

		add_action( 'wp_ajax_tmpcoder_cancel_elementor_redirect', array($this, 'tmpcoder_cancel_elementor_redirect') );

		add_action( 'wp_ajax_tmpcoder_recommended_plugin_activate', array($this, 'tmpcoder_required_plugin_activate') );
	}

	public function tmpcoder_cancel_elementor_redirect() {
		exit;
	}

	/**
	** Get plugin status.
	*/
	public function tmpcoder_get_plugin_status( $plugin_path ) {

		if ( ! current_user_can( 'install_plugins' ) ) {
			return;
		}

		if ( ! file_exists( WP_PLUGIN_DIR . '/' . $plugin_path ) ) {
			return 'not_installed';
		} else {
			$plugin_updates = get_site_transient( 'update_plugins' );
			$plugin_needs_update = is_object($plugin_updates) ? array_key_exists($plugin_path, $plugin_updates->response) : false;

			if ( in_array( $plugin_path, (array) get_option( 'active_plugins', array() ), true ) || is_plugin_active_for_network( $plugin_path ) ) {
				return $plugin_needs_update ? 'active_update' : 'active';
			} else {
				return $plugin_needs_update ? 'inactive_update' : 'inactive';
			}	
		}
	}

	/**
	** Render Notice
	*/

	public function tmpcoder_render_notice() {

		if ( isset($_GET['page']) && $_GET['page'] == 'tmpcoder-plugin-wizard' || isset($_GET['page']) && $_GET['page'] == 'tmpcoder-theme-wizard' || isset($_GET['page']) && $_GET['page'] == 'tmpcoder-setup-wizard' ){ // phpcs:ignore WordPress.Security.NonceVerification.Recommended
			return;
		}
		
		global $pagenow; // use wp prebuild variable

		$screen = get_current_screen();

		$elementor_status = $this->tmpcoder_get_plugin_status( 'elementor/elementor.php' );
		$sastra_addon_status = $this->tmpcoder_get_plugin_status( 'sastra-essential-addons-for-elementor/sastra-essential-addons-for-elementor.php' );
		$redux_framework_status = $this->tmpcoder_get_plugin_status( 'redux-framework/redux-framework.php' );	

		$flex_attr = '';
		/*if ( 'inactive' === $elementor_status || 'inactive_update' === $elementor_status || 'inactive_update' === $sastra_addon_status || 'inactive
			' === $sastra_addon_status || 'inactive' === $redux_framework_status || 'inactive_update' === $redux_framework_status ) {
			$flex_attr = ' display:none;';
		}*/
		if ( 'active' === $elementor_status && 'active' === $sastra_addon_status && 'active' === $redux_framework_status ) {
			$flex_attr = ' display:none;';
		}else{
		    $flex_attr = ' display:inline-flex !important;';
		}
		
// 		if ( 'spexo-welcome' !== $screen->parent_base ) 
// 		{
		    $transient_name = sprintf( '%s_activation_notice', get_template() );
		    
		    if ( ! get_transient( $transient_name ) ) {
				?>
				<div class="tmpcoder-notice notice notice-success tmpcoder-required-plugin-notice is-dismissible" data-notice="<?php echo esc_attr( $transient_name ); ?>" style="<?php echo esc_attr($flex_attr); ?>">
					<button type="button" class="notice-dismiss"></button>
					<?php $this->tmpcoder_render_notice_content(); ?>
				</div>
				<?php
			}
// 		}
	}

	/**
	** Render Notice Content
	*/
	public function tmpcoder_render_notice_content() {
		
		$action = 'install-activate';
		$redirect_url = 'javascript:void(0)';
		
		$elementor_status = $this->tmpcoder_get_plugin_status( 'elementor/elementor.php' );
		$sastra_addon_status = $this->tmpcoder_get_plugin_status( 'sastra-essential-addons-for-elementor/sastra-essential-addons-for-elementor.php' );
		$redux_framework_status = $this->tmpcoder_get_plugin_status( 'redux-framework/redux-framework.php' );
		
		if ( 'active' === $elementor_status && 'active' === $sastra_addon_status && 'active' === $redux_framework_status ) {
			$action = 'default';
		}

		$screen = get_current_screen();
		$flex_attr = '';
		$display_attr = 'display: inline-block !important';
		
		if ( 'appearance_page_about-tmpcoder' === $screen->id ) {
			$flex_attr = 'display:-webkit-box;display:-ms-flexbox;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-ms-flex-direction:column;flex-direction:column;-webkit-box-pack:center;-ms-flex-pack:center;justify-content:center';
			$display_attr = 'display: none !important';
		}

		?>

		<div class="welcome-message" style="<?php echo esc_attr($flex_attr); ?>">
			<h1 style="<?php echo esc_attr($display_attr); ?>">
				<?php
					echo esc_html(
					    sprintf(
					        /* translators: %s is the theme name */
					        esc_html__('Welcome to %s', 'spexo'),
					        TMPCODER_THEME_NAME
					    )
					);
				?>
			</h1>
			<p>
				<?php
					echo sprintf(
					    /* translators: %s is the description of prebuilt sites */
					    esc_html__( '%1$s comes with %2$s with various designs to pick from.', 'spexo' ),
					    esc_html(TMPCODER_THEME_NAME),
					    '<strong>Elementor based 40+ prebuilt sites</strong>'
					);
					
					echo sprintf(
					    /* translators: %s is the description of premium widgets */
					    esc_html__('%s and many other Elementor Extensions & Effects.', 'spexo'),
					    '<strong><a href="' . esc_url(TMPCODER_SPEXO_ADDONS_WIDGETS_URL) . '" target="_blank" rel="noopener noreferrer">100+ Premium Elementor Widgets</a> like Post and Product Grid, Slider, Menu, Theme & Woocommerce Shop Builder</strong>'
					);
					
					esc_html_e('Easily Customize every fundamental part of your WordPress site with Spexo Theme & Woocommerce Shop Builder.', 'spexo');
				
					echo sprintf(
					    /* translators: %s is the list of customizable elements */
					    esc_html__('Including your %s etc.', 'spexo'),
					    '<strong>Header, Footer, Archives, Product Archives, Posts, Products, Default Pages, My Account, Cart, Checkout, Category, 404 Pages,</strong>'
					);
				
					echo sprintf(
					    /* translators: %s is the list of site builder plugins */
					    esc_html__('Build your site with Spexo and our site builder plugins %s.', 'spexo'),
					    '<strong>Elementor, Spexo Addons For Elementor & Redux Framework</strong>'
					);
				?>
			</p>
			<div class="action-buttons">
				<a href="<?php echo esc_attr($redirect_url); ?>" class="button button-primary tmpcoder-install-recommended-plugin" data-action="<?php echo esc_attr($action); ?>">
					<?php 

					echo sprintf(
					    /* translators: %s is an icon or other HTML element */
					    esc_html__('Get Started with Prebuild Demos %s', 'spexo'),
					    /* Since the dynamic part is HTML, it should be safely output directly */
					    wp_kses_post('<span class="dashicons dashicons-arrow-right-alt"></span>')
					);

				 	?>
				</a>
			</div>
		</div>

		<div class="image-wrap">
			<img src="<?php echo esc_url(get_template_directory_uri().'/inc/activation/img/welcome-banner.png'); ?>" alt="">
		</div>

		<?php
	}

	/**
	** Reset Notice.
	*/

	public function tmpcoder_reset_notices() {
		delete_transient( sprintf( /* translators: %s is template name */ '%s_activation_notice', get_template() ) );
	}

	/**
	** Dismissed handler
	*/
	public function tmpcoder_dismissed_handler() {
		wp_verify_nonce( null );

		if ( isset( $_POST['notice'] ) ) {
			set_transient( sanitize_text_field( wp_unslash( $_POST['notice'] ) ), true, 0 );
		}
	}

	/**
	** Register scripts and styles for welcome notice.
	*/

	public function tmpcoder_admin_enqueue_scripts( $page ) {
		
		$pluginCount = 3;
		$pluginSucessCount = 0;
		$pluginNotInstallCount = 0;
		$pluginsList = array();
		$installedPluginNotActivated = array();
		
		$elementor_status = $this->tmpcoder_get_plugin_status( 'elementor/elementor.php' );
		$sastra_status = $this->tmpcoder_get_plugin_status( 'sastra-essential-addons-for-elementor/sastra-essential-addons-for-elementor.php' );
		$redux_status = $this->tmpcoder_get_plugin_status( 'redux-framework/redux-framework.php' );

		if ('not_installed' === $elementor_status ) {
			array_push($pluginsList,'elementor');
			$pluginNotInstallCount++;
	 	}

	 	if ('not_installed' === $sastra_status ) {
			array_push($pluginsList,'sastra-essential-addons-for-elementor');
			$pluginNotInstallCount++;
	 	}

	 	if ('not_installed' === $redux_status) {
			array_push($pluginsList,'redux-framework');
			$pluginNotInstallCount++;
	 	}

	 	if ('active' === $elementor_status ) {
			$pluginCount -= 1; 		 	
			$pluginSucessCount += 1; 		 	
	 	}
	 	if ('active' === $sastra_status) {
			$pluginCount -= 1; 		 	
			$pluginSucessCount += 1; 		 	
	 	}
	 	if ('active' === $redux_status) {
			$pluginCount -= 1; 		 	
			$pluginSucessCount += 1; 		 	
	 	}
	 	
	 	if ('inactive' === $elementor_status ) {
			array_push($installedPluginNotActivated, 'elementor/elementor.php');
	 	}
	 	if ('inactive' === $sastra_status) {
			array_push($installedPluginNotActivated, 'sastra-essential-addons-for-elementor/sastra-essential-addons-for-elementor.php');
	 	}
	 	if ('inactive' === $redux_status) {
			array_push($installedPluginNotActivated, 'redux-framework/redux-framework.php');
	 	}

		// Enqueue Scripts
		wp_enqueue_script( 'tmpcoder-welcome-notice-js', get_template_directory_uri() . '/inc/activation/js/tmpcoder-welcome-notice'.tmpcoder_min_suffix().'.js', array('jquery', 'updates'), false, true );

		wp_localize_script( 'tmpcoder-welcome-notice-js', 'tmpcoder_localize', array(
				'failed_message' => esc_html__( 'Something went wrong, contact support.','spexo' ),
				'ajaxUrl' => admin_url( 'admin-ajax.php' ),
				'tmpcoderSitesLink' => admin_url( 'themes.php?page=starter-templates' ),
				'tmpcoderRedirectionLink' => admin_url( 'admin.php?page=spexo-welcome' ),
				'recommendedPluiginActivatingText'   => __( 'Activating','spexo' ) . '&hellip;',
				'recommendedPluiginDeactivatingText' => __( 'Deactivating','spexo' ) . '&hellip;',
				'recommendedPluiginActivateText'     => __( 'Activate','spexo' ),
				'recommendedPluiginDeactivateText'   => __( 'Deactivate','spexo' ),
				'recommendedPluiginSettingsText'     => __( 'Settings','spexo' ),
				'tmpcoderPluginManagerNonce'         => wp_create_nonce( 'spexo_plugin_manager_nonce' ),
				'pluginCount'            		 	 => $pluginCount,
				'pluginSucessCount'            		 => $pluginSucessCount,
				'pluginsList'            		 	 => $pluginsList,
				'pluginNotInstallCount'              => $pluginNotInstallCount,
				'installedPluginNotActivated'        => $installedPluginNotActivated,
		) );

		// Enqueue Styles.
		wp_enqueue_style( 'tmpcoder-welcome-notice-css', get_template_directory_uri() . '/inc/activation/css/tmpcoder-welcome-notice'.tmpcoder_min_suffix().'.css','', TMPCODER_THEME_CORE_VERSION 
		);
	}

	/**
	 * Required Plugin Activate
	 *
	 * @since 1.0.0
	*/

	public function tmpcoder_required_plugin_activate() {

		$plugin_init = array('elementor/elementor.php','sastra-essential-addons-for-elementor/sastra-essential-addons-for-elementor.php','redux-framework/redux-framework.php');

		if (is_array($plugin_init)) {
			
			foreach ($plugin_init as $value) {
				
				$activate = activate_plugin( $value, '', false, true );
			}
		}

		if ( is_wp_error( $activate ) ) {
			
			wp_send_json_error(
				array(
					'success' => false,
					'message' => $activate->get_error_message(),
				)
			);
			
		}

		$this->tmpcoder_reset_notices();

		wp_send_json_success(
			array(
				'success' => true,
				'message' => __( 'Plugin Successfully Activated','spexo' ),
			)
		);
	}
}

new Tmpcoder_Welcome_Notice();