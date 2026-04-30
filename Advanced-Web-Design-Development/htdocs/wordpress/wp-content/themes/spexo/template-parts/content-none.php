<?php
/**
 * Template part for displaying a message that posts cannot be found
 *
 * @link https://developer.wordpress.org/themes/basics/template-hierarchy/
 *
 * @package Spexo
 */

if ( ! defined( 'ABSPATH' ) ) {
    exit; // Exit if accessed directly.
}

?>

<div class="col-md-12 wow fadeInUp animated spexo-nothing-found-content">
	<div class="section-heading text-center">
		<h2 class="heading-title"><?php esc_html_e( 'Nothing Found','spexo' ); ?></h2>

	<?php
	if ( is_home() && current_user_can( 'publish_posts' ) ) :

		printf(
			'<p class="heading-des">' . wp_kses(
				/* translators: 1: link to WP admin new post page. */
				__( 'Ready to publish your first post? <a href="%1$s">Get started here</a>.','spexo' ),
				array(
					'a' => array(
					'href' => array(),
					),
				)
			) . '</p>',

			esc_url( admin_url( 'post-new.php' ) )
		);

	elseif ( is_search() ) :

		?>

		<p class="heading-des"><?php esc_html_e( 'Sorry, but nothing matched your search terms. Please try again with some different keywords.','spexo' ); ?></p>
		
		<?php
		
	else :

		?>

		<p><?php esc_html_e( 'It seems we can&rsquo;t find what you&rsquo;re looking for. Perhaps searching can help.','spexo' ); ?></p>
		<?php
		get_search_form();

	endif;

	?>
	</div>
</div>