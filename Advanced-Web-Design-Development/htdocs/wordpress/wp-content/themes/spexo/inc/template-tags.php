<?php
/**
 * Custom template tags for this theme
 *
 * Eventually, some of the functionality here could be replaced by core features.
 *
 * @package Spexo
 */

if ( ! defined( 'ABSPATH' ) ) {
    exit; // Exit if accessed directly.
}

if ( ! function_exists( 'tmpcoder_posted_on' ) ) :
	/**
	 * Prints HTML with meta information for the current post-date/time.
	 */
	function tmpcoder_posted_on() {
		$time_string = '<time class="entry-date published updated" datetime="%1$s">%2$s</time>';
		if ( get_the_time( 'U' ) !== get_the_modified_time( 'U' ) ) {
			$time_string = '<time class="entry-date published" datetime="%1$s">%2$s</time>';
		}

		$time_string = sprintf(
			$time_string,
			esc_attr( get_the_date( DATE_W3C ) ),
			esc_html( get_the_date() ),
			esc_attr( get_the_modified_date( DATE_W3C ) ),
			esc_html( get_the_modified_date() )
		);

		
        echo '<span class="posted-on">' . wp_kses($time_string, array('time' => array(
            'class'  => array(),
            'datetime' => array(),
        ))) . '</span>';

	}
endif;

if ( ! function_exists( 'tmpcoder_posted_by' ) ) :
	/**
	 * Prints HTML with meta information for the current author.
	 */
	function tmpcoder_posted_by() {
		$byline = sprintf(
			/* translators: %s: post author. */
			esc_html_x( 'by %s', 'post author','spexo' ),
			'<span class="author vcard"><a class="url fn n" href="' . esc_url( get_author_posts_url( get_the_author_meta( 'ID' ) ) ) . '">' . esc_html( get_the_author() ) . '</a></span>'
		);

		$allow_html = array(
			'span' => array('class' => array()),
			'a' => array('class' => array(),'href' => array(),
		));

		echo '<span class="byline"> ' . wp_kses($byline, $allow_html).'</span>';
	}
endif;

if ( ! function_exists( 'tmpcoder_post_thumbnail' ) ) :
	/**
	 * Displays an optional post thumbnail.
	 *
	 * Wraps the post thumbnail in an anchor element on index views, or a div
	 * element when on single views.
	 */
	function tmpcoder_post_thumbnail() {

		if ( post_password_required() || is_attachment() || ! has_post_thumbnail() ) {
			return;
		}

		if ( is_singular() ) :
			?>

			<div class="post-thumbnail">
				<?php the_post_thumbnail(); ?>
			</div><!-- .post-thumbnail -->

		<?php else : ?>

			<a class="post-thumbnail" href="<?php the_permalink(); ?>" aria-hidden="true" tabindex="-1">
				<?php
					the_post_thumbnail(
						'post-thumbnail',
						array(
							'alt' => the_title_attribute(
								array(
									'echo' => false,
								)
							),
						)
					);
				?>
			</a>

			<?php
		endif; // End is_singular().
	}
	
endif;

if ( ! function_exists( 'tmpcoder_wp_body_open' ) ) :
	/**
	 * Shim for sites older than 5.2.
	 *
	 * @link https://core.trac.wordpress.org/ticket/12563
	 */

	function tmpcoder_wp_body_open() {
		do_action( 'wp_body_open' );
	}

endif;

if ( !function_exists( 'tmpcoder_posted_comments' ) ) :
	/**
	 * Blog category
	 *
	 */
	function tmpcoder_posted_comments() {
		$comments = get_comments_number();
		if ( $comments ) {
			/* translators: 1: list of categories. */
			printf( '<span class="cat-links">' . esc_html__( '%1$s Comments','spexo' ) . '</span>', esc_html($comments) );
		}
	}

endif;

if ( !function_exists('tmpcoder_show_tags') ):
    function tmpcoder_show_tags() {
        $post_tags = get_the_tags();
        $output = '';
        if ( ! empty( $post_tags ) ) {
            foreach ( $post_tags as $tag ) {
                $output .= '<a href="' . esc_attr( get_tag_link( $tag->term_id ) ) . '" class="btn button">' .  esc_html($tag->name) . '</a>';
            }
        }
        return $output;
    }
endif;