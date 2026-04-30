<?php
/**
 * Gutenberg Compatibility File.
 *
 * @since 1.0.4
 * @package Spexo
 */


class Tmpcoder_Gutenberg {

	/**
	 * Constructor
	 */
	public function __construct() {
		add_action( 'wp', array( $this, 'is_layout_with_blocks' ), 1 );

        add_action( 'enqueue_block_editor_assets', array( $this, 'gutenberg_assets' ) );
        add_action( 'wp_enqueue_scripts', array( $this, 'gutenberg_assets' ) );
        
		add_filter( 'render_block_core/group', array( $this, 'add_inherit_width_group_class' ), 10, 2 );
		
	}

	/**
	 * Check if blocks has been used on the layout. Adding it for making moder compatibility CSS target specific.
	 *
	 * @since 1.0.4
	 * @return void
	 */
	public function is_layout_with_blocks() {

        // @codingStandardsIgnoreStart
        global $post;

        $post_id = 0;

        if ( is_home() ) {
            $post_id = get_option( 'page_for_posts' );
        } elseif ( is_archive() ) {
            global $wp_query;
            $post_id = $wp_query->get_queried_object_id();
        } elseif ( isset( $post->ID ) && ! is_search() && ! is_category() ) {
            $post_id = $post->ID;
        }		
		/** @psalm-suppress RedundantConditionGivenDocblockType */ // phpcs:ignore Generic.Commenting.DocComment.MissingShort
		if ( $post_id ) {
			/** @psalm-suppress RedundantConditionGivenDocblockType */ // phpcs:ignore Generic.Commenting.DocComment.MissingShort

            $current_post = get_post( absint( $post_id ) );

			/** @psalm-suppress TooManyArguments */ // phpcs:ignore Generic.Commenting.DocComment.MissingShort
			$enable_block_editor_attr = apply_filters( 'tmpcoder_disable_block_content_attr', true, $post_id );
			/** @psalm-suppress TooManyArguments */ // phpcs:ignore Generic.Commenting.DocComment.MissingShort

			/** @psalm-suppress PossiblyInvalidArgument */ // phpcs:ignore Generic.Commenting.DocComment.MissingShort
			if ( has_blocks( $current_post ) && $enable_block_editor_attr ) {
				/** @psalm-suppress PossiblyInvalidArgument */ // phpcs:ignore Generic.Commenting.DocComment.MissingShort                
                add_filter( 'body_class', array( $this, 'add_tmpcoder_body_classes') );

			}
		}
		// @codingStandardsIgnoreEnd
	}

    function gutenberg_assets(){

        /* Directory and Extension */
        $rtl = '';
        if ( is_rtl() ) {
            $rtl = '-rtl';
        }

        $tmpcoder_js_uri = get_template_directory_uri() . '/assets/js/tmpcoder-block-editor-script.js';
        /** @psalm-suppress InvalidArgument */ // phpcs:ignore Generic.Commenting.DocComment.MissingShort
        wp_enqueue_script( 'tmpcoder-block-editor-script', $tmpcoder_js_uri, false, TMPCODER_THEME_CORE_VERSION, 'all' );
        /** @psalm-suppress InvalidArgument */ // phpcs:ignore Generic.Commenting.DocComment.MissingShort

        $css_uri = get_template_directory_uri() . '/assets/css/tmpcoder-block-editor-styles' . $rtl . '.css';
        /** @psalm-suppress InvalidArgument */ // phpcs:ignore Generic.Commenting.DocComment.MissingShort
        wp_enqueue_style( 'tmpcoder-block-editor-styles', $css_uri, false, TMPCODER_THEME_CORE_VERSION, 'all' );
        /** @psalm-suppress InvalidArgument */ // phpcs:ignore Generic.Commenting.DocComment.MissingShort
        
    }

    

    function add_tmpcoder_body_classes( $classes ) {
        
        $classes[] = 'tmpcoder-block-builder tmpcoder-plain-container tmpcoder-no-sidebar';
        
        return $classes;
    }

	/**
	 * Add Group block custom class when "Inherit default layout" toggle enabled.
	 *
	 * @since 1.0.4
	 *
	 * @param string $block_content Rendered block content.
	 * @param array  $block         Block object.
	 *
	 * @return string Filtered block content.
	 */
	public function add_inherit_width_group_class( $block_content, $block ) {
		if (
			isset( $block['blockName'] ) && isset( $block['attrs']['layout']['inherit'] ) && $block['attrs']['layout']['inherit']
		) {
			$block_content = preg_replace(
				'/' . preg_quote( 'class="', '/' ) . '/',
				'class="tmpcoder-default-layout inherit-container-width ',
				$block_content,
				1
			);
		}

		return $block_content;
	}

	/**
	 * Update the block content with inner div.
	 *
	 * @since 1.0.4
	 *
	 * @param mixed $matches block content.
	 *
	 * @return string New block content.
	 */
	public function group_block_replace_regex( $matches ) {
		return $matches[1] . '<div class="wp-block-group__inner-container">' . $matches[2] . '</div>' . $matches[3];
	}

}

new Tmpcoder_Gutenberg();
