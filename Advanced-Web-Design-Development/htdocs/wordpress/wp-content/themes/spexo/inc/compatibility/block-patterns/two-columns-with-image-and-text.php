<?php
/**
 * Two columns with image and text pattern.
 *
 * @package Spexo
 */

return array(
	'title'      => __( 'Two columns with image and text', 'spexo' ),
	'content'    => '<!-- wp:columns {"metadata":{"categories":["spexo"],"patternName":"spexo/two-columns-with-image-and-text","name":"Two columns with image and text"}} -->
<div class="wp-block-columns"><!-- wp:column {"width":""} -->
<div class="wp-block-column"><!-- wp:image {"id":206,"sizeSlug":"large","linkDestination":"none"} -->
<figure class="wp-block-image size-large"><img src="' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/two-columns-with-image-and-text.jpg" alt="" class="wp-image-206"/></figure>
<!-- /wp:image --></div>
<!-- /wp:column -->

<!-- wp:column {"verticalAlignment":"center","style":{"spacing":{"blockGap":"0","padding":{"right":"var:preset|spacing|xx-small","left":"var:preset|spacing|x-small"}}},"layout":{"type":"default"}} -->
<div class="wp-block-column is-vertically-aligned-center" style="padding-right:var(--wp--preset--spacing--xx-small);padding-left:var(--wp--preset--spacing--x-small)"><!-- wp:heading {"style":{"spacing":{"margin":{"bottom":"var:preset|spacing|x-small"}},"typography":{"fontSize":"40px","lineHeight":"1.2"}}} -->
<h2 class="wp-block-heading" style="margin-bottom:var(--wp--preset--spacing--x-small);font-size:40px;line-height:1.2">The Graceful Deer: Silent Guardian of the Forest</h2>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p><em>"Nature\'s whisper, moving with grace—deer embody the quiet strength of the wild."</em></p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>Deer are among the most elegant and adaptive creatures, thriving in forests, grasslands, and mountains. With their unmatched agility and keen senses, they navigate the wild effortlessly, always alert to the unseen. Their powerful legs carry them across vast distances, while their sharp hearing detects danger before it approaches.</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"style":{"spacing":{"margin":{"bottom":"var:preset|spacing|large"}}}} -->
<p style="margin-bottom:var(--wp--preset--spacing--large)">From the majestic antlers of the stag to the gentle presence of the doe, deer symbolize grace, survival, and the untamed beauty of nature.</p>
<!-- /wp:paragraph -->

<!-- wp:buttons {"style":{"spacing":{"padding":{"bottom":"0"}}}} -->
<div class="wp-block-buttons" style="padding-bottom:0"><!-- wp:button {"className":"is-style-primary"} -->
<div class="wp-block-button is-style-primary"><a class="wp-block-button__link wp-element-button">Discover Deer</a></div>
<!-- /wp:button --></div>
<!-- /wp:buttons --></div>
<!-- /wp:column --></div>
<!-- /wp:columns -->',
	'categories' => array( 'spexo', 'columns' ),
);
