<?php
/**
 * Block Patterns.
 *
 * @since 1.0.4
 * @package Spexo
 */


if ( ! class_exists( 'Tmpcoder_block_patterns' ) ) {

    /**
     * Class Tmpcoder_block_patterns
     *
     * Class for defining and registering block patterns.
     */

    class Tmpcoder_block_patterns {

        /**
         * Array of block pattern slugs.
         *
         * @var array
         */
        private $patterns = [
            'dark-header-centered-content',
            '404-error',
            'banner',
            'service-we-provide',
            'two-columns-with-image-and-text',
            'our-case-studies',
            'welcome-to-spexo-section',
            'hero-content-video-icon',
            'services-we-provide-2',
            'about',
            'blog-simple',
            'blog-grid',
            'blog-left-featured-image',
            'two-column-featured-left',
            'three-column-featured-center',
            'three-column-featured-grid',
            'teams-three-columns',
            'teams-four-columns-circle',
            'pricing-three-column',
        ];
    
    
        public function __construct() {
            add_action( 'init', [ $this, 'tmpcoder_define_patterns' ] );
            add_action( 'init', [ $this, 'tmpcoder_register_block_pattern_categories' ] );
        }
    
        /**
         * Define and register the block patterns.
         */
        public function tmpcoder_define_patterns() {
            if ( ! function_exists( 'register_block_pattern' ) ) {
                return;
            }
    
            foreach ( $this->patterns as $pattern ) {
                $file = get_template_directory() . '/inc/compatibility/block-patterns/' . $pattern . '.php';

                if ( file_exists( $file ) ) {

                    register_block_pattern(
                        'spexo/' . $pattern,
                        require $file 
                    );
                }
            }
        }

        /**
         * Define and register the block pattern categories.
         */
        public function tmpcoder_register_block_pattern_categories() {
            if ( function_exists( 'register_block_pattern_category' ) ) {
                register_block_pattern_category('spexo',
                    array(
                        'label' => __('Spexo', 'spexo'),
                    )
                );
            }
        }
    }
}

/**
 * Initialise Block Patterns
 */
new Tmpcoder_block_patterns();
