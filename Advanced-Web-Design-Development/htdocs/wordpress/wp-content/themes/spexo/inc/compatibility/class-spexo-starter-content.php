<?php
/**
 * Starter Content Compatibility.
 *
 * @since 1.0.4
 * @package Spexo
 */

/**
 * Class Spexo_Starter_Content
 */
class Spexo_Starter_Content {
	const TMPCODER_HOME_SLUG     = 'home';
	const TMPCODER_ABOUT_SLUG    = '#about-us';
	const TMPCODER_SERVICES_SLUG = '#services';
	const TMPCODER_REVIEWS_SLUG  = '#reviews';
	const TMPCODER_WHY_US_SLUG   = '#whyus';
	const TMPCODER_CONTACT_SLUG  = '#contact-us';

	/**
	 * Constructor
	 */
	public function __construct() {
		$is_fresh_site = get_option( 'fresh_site' );

		if ( ! $is_fresh_site ) {
			return;
		}

		// Adding post meta and inserting post.
		add_action(
			'wp_insert_post',
			array(
				$this,
				'register_listener',
			),
			3,
			99
		);

		// Save spexo settings into database.
		add_action(
			'customize_save_after',
			array(
				$this,
				'save_spexo_settings',
			),
			10,
			3
		);

		if ( ! is_customize_preview() ) {
			return;
		}

		// preview customizer values.
		add_filter( 'default_post_metadata', array( $this, 'starter_meta' ), 99, 3 );

		add_filter( 'spexo_theme_defaults', array( $this, 'theme_defaults' ) );

		add_filter( 'spexo_global_color_palette', array( $this, 'theme_color_palettes_defaults' ) );

	}

	/**
	 * Load default starter meta.
	 *
	 * @since 4.0.2
	 * @param mixed  $value Value.
	 * @param int    $post_id Post id.
	 * @param string $meta_key Meta key.
	 *
	 * @return string Meta value.
	 */
	public function starter_meta( $value, $post_id, $meta_key ) {
		if ( get_post_type( $post_id ) !== 'page' ) {
			return $value;
		}
		if ( 'site-content-layout' === $meta_key ) {
			return 'plain-container';
		}
		if ( 'theme-transparent-header-meta' === $meta_key ) {
			return 'enabled';
		}
		if ( 'site-sidebar-layout' === $meta_key ) {
			return 'no-sidebar';
		}
		if ( 'site-post-title' === $meta_key ) {
			return 'disabled';
		}
		return $value;
	}

	/**
	 * Register listener to insert post.
	 *
	 * @since 1.0.4
	 * @param int      $post_ID Post Id.
	 * @param \WP_Post $post Post object.
	 * @param bool     $update Is update.
	 */
	public function register_listener( $post_ID, $post, $update ) {

		if ( $update ) {
			return;
		}

		$custom_draft_post_name = get_post_meta( $post_ID, '_customize_draft_post_name', true );

		$is_from_starter_content = ! empty( $custom_draft_post_name );

		if ( ! $is_from_starter_content ) {
			return;
		}

		if ( 'page' === $post->post_type ) {
			update_post_meta( $post_ID, 'site-content-layout', 'plain-container' );
			update_post_meta( $post_ID, 'theme-transparent-header-meta', 'enabled' );
			update_post_meta( $post_ID, 'site-sidebar-layout', 'no-sidebar' );
			update_post_meta( $post_ID, 'site-post-title', 'disabled' );
		}
	}

	/**
	 *  Get customizer json
	 *
	 * @since 1.0.4
	 *  @return mixed value.
	 */
	public function get_customizer_json() {
		try {
			$request = wp_remote_get( get_template_directory() . '/inc/compatibility/starter-content/tmpcoder-settings-export.json' );
		} catch ( Exception $ex ) {
			$request = null;
		}

		if ( is_wp_error( $request ) ) {
			return false; // Bail early.
		}

		// @codingStandardsIgnoreStart
		/**
		 * @psalm-suppress PossiblyNullReference
		 * @psalm-suppress UndefinedMethod
		 * @psalm-suppress PossiblyNullArrayAccess
		 * @psalm-suppress PossiblyNullArgument
		 * @psalm-suppress InvalidScalarArgument
		 */
		return json_decode( $request['body'], 1 );
		// @codingStandardsIgnoreEnd
	}

	/**
	 *  Save Spexo customizer settings into database.
	 *
	 * @since 1.0.4
	 */
	public function save_spexo_settings() {

		$settings = self::get_customizer_json();

		// Delete existing dynamic CSS cache.
		delete_option( 'spexo-settings' );

		if ( ! empty( $settings['customizer-settings'] ) ) {
			foreach ( $settings['customizer-settings'] as $option => $value ) {
				update_option( $option, $value );
			}
		}
	}

	/**
	 * Load default spexo settings.
	 *
	 * @since 1.0.4
	 * @param mixed $defaults defaults.
	 * @return mixed value.
	 */
	public function theme_defaults( $defaults ) {
		$json     = '';
		$settings = self::get_customizer_json();

		if ( ! empty( $settings['customizer-settings'] ) ) {
			$json = $settings['customizer-settings']['spexo-settings'];
		}

		return $json ? $json : $defaults;
	}

	/**
	 * Load default color palettes.
	 *
	 * @since 1.0.4
	 * @param mixed $defaults defaults.
	 * @return mixed value.
	 */
	public function theme_color_palettes_defaults( $defaults ) {
		$json     = '';
		$settings = self::get_customizer_json();

		if ( ! empty( $settings['customizer-settings'] ) ) {
			$json = $settings['customizer-settings']['spexo-color-palettes'];
		}

		return $json ? $json : $defaults;
	}


	/**
	 * Return starter content definition.
	 *
	 * @return mixed|void
	 * @since 1.0.4
	 */
	public function get() {

		$nav_items_header = array(
			'home'     => array(
				'type'      => 'post_type',
				'object'    => 'page',
				'object_id' => '{{' . self::TMPCODER_HOME_SLUG . '}}',
			),
			'services'    => array(
				'title' => __( 'Services', 'spexo' ),
				'type'  => 'custom',
				'url'   => '{{' . self::TMPCODER_SERVICES_SLUG . '}}',
			),
			'about-us' => array(
				'title' => __( 'About', 'spexo' ),
				'type'  => 'custom',
				'url'   => '{{' . self::TMPCODER_ABOUT_SLUG . '}}',
			),
			'reviews'  => array(
				'title' => __( 'Reviews', 'spexo' ),
				'type'  => 'custom',
				'url'   => '{{' . self::TMPCODER_REVIEWS_SLUG . '}}',
			),
			'faq'      => array(
				'title' => __( 'Why Us', 'spexo' ),
				'type'  => 'custom',
				'url'   => '{{' . self::TMPCODER_WHY_US_SLUG . '}}',
			),
			'contact-us'  => array(
				'title' => __( 'Contact', 'spexo' ),
				'type'  => 'custom',
				'url'   => '{{' . self::TMPCODER_CONTACT_SLUG . '}}',
			),
		);

		$content = array(
			'theme_mods'  => array(
				'custom_logo' => '{{featured-image-logo}}',
				'site_icon'   => '{{site_icon}}',
				'logo_show_tagline' => 0,
                'logo_display' => 1,
			),	
			'attachments' => array(
				'featured-image-logo' => array(
					'post_title' =>  _x( 'Logo', 'Theme starter content', 'spexo' ),
					'post_content' =>  _x( 'Attachment Description', 'Theme starter content', 'spexo' ),
					'post_excerpt' =>  _x( 'Attachment Caption', 'Theme starter content', 'spexo' ),
					'file'	 => 'assets/images/starter-content/logo.png',
				),
				'site_icon' => array(
					'post_title' => _x( 'Site Icon', 'Theme starter content', 'spexo' ),
					'file'       => 'assets/images/starter-content/favicon.png',
				),
			),		
			'nav_menus'   => array(
				'primary'     => array(
					'name'  => esc_html__( 'Primary', 'spexo' ),
					'items' => $nav_items_header,
				),
				'footer_menu' => array(
					'name'  => esc_html__( 'Primary', 'spexo' ),
					'items' => $nav_items_header,
				),
			),
			'options'     => array(
				'page_on_front' => '{{' . self::TMPCODER_HOME_SLUG . '}}',
				'show_on_front' => 'page',
			),
			'posts'       => array(
				self::TMPCODER_HOME_SLUG => require get_template_directory() . '/inc/compatibility/starter-content/home.php', // PHPCS:ignore WPThemeReview.CoreFunctionality.FileInclude.FileIncludeFound
			),
		);

		return apply_filters( 'spexo_starter_content', $content );
	}
}

// add_action('init', 'tmpcoder_pre_set_theme_mod_settings');
function tmpcoder_pre_set_theme_mod_settings(){
    add_filter('pre_set_theme_mod_custom_logo', 'tmpcoder_pre_set_custom_logo');
    add_filter('pre_set_theme_mod_site_icon', 'tmpcoder_pre_set_site_icon');

	// Force set the logo on theme activation
	if ( ! get_theme_mod( 'custom_logo' ) ) {
        tmpcoder_pre_set_custom_logo('');
    }
}

function tmpcoder_pre_set_custom_logo($value) {

    // If a logo is already set, return it.
    if (!empty($value)) {
        return $value;
    }

    // Define the default logo URL
    $logo_path = get_template_directory() . '/assets/images/logo.png';
    $logo_url  = get_template_directory_uri() . '/assets/images/logo.png';

    // Ensure the file exists before proceeding
    if (!file_exists($logo_path)) {
        return $value;
    }

    // Check if an attachment for this logo already exists using WP_Query
    $attachment_query = new WP_Query(array(
        'post_type'      => 'attachment',
        'title'          => 'Predefined Logo',
        'posts_per_page' => 1,
    ));

    if ($attachment_query->have_posts()) {
        $attachment = $attachment_query->posts[0];
        return $attachment->ID;
    }

    // Insert a new attachment into the database
    $attachment_data = array(
        'guid'           => $logo_url,
        'post_mime_type' => 'image/png',
        'post_title'     => 'Predefined Logo',
        'post_content'   => '',
        'post_status'    => 'inherit',
    );

    $attach_id = wp_insert_attachment($attachment_data);
    return $attach_id;
}

function tmpcoder_pre_set_site_icon($value) {
    // If a logo is already set, return it.
    if (!empty($value)) {
        return $value;
    }

    // Define the default logo URL
    $logo_path = get_template_directory() . '/assets/images/favicon.png';
    $logo_url  = get_template_directory_uri() . '/assets/images/favicon.png';

    // Ensure the file exists before proceeding
    if (!file_exists($logo_path)) {
        return $value;
    }

    // Check if an attachment for this logo already exists
    $attachment = get_page_by_title('Predefined Favicon', OBJECT, 'attachment');
    if ($attachment) {
        return $attachment->ID;
    }

    // Insert a new attachment into the database
    $attachment_data = array(
        'guid'           => $logo_url,
        'post_mime_type' => 'image/png',
        'post_title'     => 'Predefined Favicon',
        'post_content'   => '',
        'post_status'    => 'inherit',
    );

    $attach_id = wp_insert_attachment($attachment_data);
    return $attach_id;
}
