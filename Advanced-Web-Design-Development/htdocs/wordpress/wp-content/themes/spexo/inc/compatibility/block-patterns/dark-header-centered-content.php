<?php
/**
 * Dark header with centered content pattern.
 *
 * @package Spexo
 */

return array(
	'title'      => __( 'Dark header with centered content', 'spexo' ),
	'content'    => '<!-- wp:cover {"url":"' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/dark-header-with-centered-content.jpg","dimRatio":26,"focalPoint":{"x":"0.50","y":"0.50"},"minHeight":700,"metadata":{"categories":["spexo"],"patternName":"spexo/dark-header-centered-content","name":"Dark header with centered content"},"align":"full","className":"is-light"} -->
<div class="wp-block-cover alignfull is-light" style="min-height:700px"><span aria-hidden="true" class="wp-block-cover__background has-background-dim-30 has-background-dim"></span><img class="wp-block-cover__image-background" alt="" src="' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/dark-header-with-centered-content.jpg" style="object-position:50% 50%" data-object-fit="cover" data-object-position="50% 50%"/><div class="wp-block-cover__inner-container"><!-- wp:heading {"textAlign":"center","level":1,"style":{"typography":{"fontSize":"52px"}},"textColor":"white"} -->
<h1 class="wp-block-heading has-text-align-center has-white-color has-text-color" style="font-size:52px">Welcome to Spexo – Elevate Your Experience</h1>
<!-- /wp:heading -->

<!-- wp:paragraph {"align":"center","style":{"typography":{"fontSize":23}},"textColor":"white"} -->
<p class="has-text-align-center has-white-color has-text-color" style="font-size:23px">Discover a new way to showcase your content with style and impact.</p>
<!-- /wp:paragraph -->

<!-- wp:spacer {"height":"30px"} -->
<div style="height:30px" aria-hidden="true" class="wp-block-spacer"></div>
<!-- /wp:spacer -->

<!-- wp:group {"style":{"spacing":{"margin":{"top":"0"}}},"layout":{"type":"default"}} -->
<div class="wp-block-group" style="margin-top:0"><!-- wp:buttons {"layout":{"type":"flex","justifyContent":"center"}} -->
<div class="wp-block-buttons"><!-- wp:button {"className":"is-style-primary"} -->
<div class="wp-block-button is-style-primary"><a class="wp-block-button__link wp-element-button">Get Started</a></div>
<!-- /wp:button --></div>
<!-- /wp:buttons --></div>
<!-- /wp:group --></div></div>
<!-- /wp:cover -->',
	'categories' => array( 'spexo', 'header', 'hero', 'banner' ),
);
