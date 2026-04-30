<?php

/**
 * The template for displaying 404 pages (not found)
 *
 * @link https://codex.wordpress.org/Creating_an_Error_404_Page
 *
 * @package Spexo
 */

if ( ! defined( 'ABSPATH' ) ) {
	exit; // Exit if accessed directly.
}

get_header();

?>

<main id="content" class="site-main">
	<?php if ( apply_filters( 'tmpcoder_elementor_page_title', true ) ) : ?>
		<header class="page-header">
			<h1 class="entry-title"><?php echo esc_html__( 'The page can&rsquo;t be found.','spexo' ); ?></h1>
		</header>
	<?php endif; ?>
	<div class="page-content">
		<p><?php echo esc_html__( 'It looks like nothing was found at this location.','spexo' ); ?></p>
		<div class="tmpcoder-404-search">
			<?php the_widget( 'WP_Widget_Search' ); ?>
		</div>
	</div>

</main>

<?php

get_footer();

