<?php
/**
 * Three column featured center pattern.
 *
 * @package Spexo
 */

return array(
	'title'      => __( 'Three column featured center', 'spexo' ),
	'content'    => '<!-- wp:group {"metadata":{"categories":["featured"],"patternName":"spexo/three-column-featured-center","name":"Three column featured center"},"style":{"spacing":{"padding":{"top":"var:preset|spacing|x-large","bottom":"var:preset|spacing|x-large"}}},"layout":{"type":"constrained"}} -->
<div class="wp-block-group" style="padding-top:var(--wp--preset--spacing--x-large);padding-bottom:var(--wp--preset--spacing--x-large)"><!-- wp:columns {"align":"wide","style":{"spacing":{"blockGap":{"top":"var:preset|spacing|x-large","left":"var:preset|spacing|x-large"}}}} -->
<div class="wp-block-columns alignwide"><!-- wp:column {"style":{"spacing":{"blockGap":"var:preset|spacing|xx-small"}}} -->
<div class="wp-block-column"><!-- wp:image {"id":139,"sizeSlug":"large","linkDestination":"none","align":"center","style":{"color":{"duotone":["#5729d9","#bf1864"]}}} -->
<figure class="wp-block-image aligncenter size-large"><img src="' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/featured-1.svg" alt="" class="wp-image-139"/></figure>
<!-- /wp:image -->

<!-- wp:heading {"textAlign":"center","level":4,"style":{"spacing":{"margin":{"top":"var:preset|spacing|small"},"padding":{"bottom":"10px"}},"typography":{"fontStyle":"normal","fontWeight":"600","lineHeight":"1.2"}},"fontSize":"medium"} -->
<h4 class="wp-block-heading has-text-align-center has-medium-font-size" style="margin-top:var(--wp--preset--spacing--small);padding-bottom:10px;font-style:normal;font-weight:600;line-height:1.2">Design with Freedom</h4>
<!-- /wp:heading -->

<!-- wp:paragraph {"align":"center","style":{"typography":{"fontSize":"15px"}}} -->
<p class="has-text-align-center" style="font-size:15px">Unlock unlimited possibilities to shape your website’s appearance and functionality to suit your preferences.</p>
<!-- /wp:paragraph --></div>
<!-- /wp:column -->

<!-- wp:column {"style":{"spacing":{"blockGap":"var:preset|spacing|xx-small"}}} -->
<div class="wp-block-column"><!-- wp:image {"id":866,"sizeSlug":"large","linkDestination":"none","align":"center","style":{"color":{"duotone":["rgb(87, 41, 217)","rgb(191, 24, 100)"]}}} -->
<figure class="wp-block-image aligncenter size-large"><img src="' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/featured-2.svg" alt="" class="wp-image-866"/></figure>
<!-- /wp:image -->

<!-- wp:heading {"textAlign":"center","level":4,"style":{"spacing":{"margin":{"top":"var:preset|spacing|small"},"padding":{"bottom":"var:preset|spacing|xx-small"}},"typography":{"fontStyle":"normal","fontWeight":"600","lineHeight":"1.2"}},"fontSize":"medium"} -->
<h4 class="wp-block-heading has-text-align-center has-medium-font-size" style="margin-top:var(--wp--preset--spacing--small);padding-bottom:var(--wp--preset--spacing--xx-small);font-style:normal;font-weight:600;line-height:1.2">Lightning Speed</h4>
<!-- /wp:heading -->

<!-- wp:paragraph {"align":"center","style":{"typography":{"fontSize":"15px"}}} -->
<p class="has-text-align-center" style="font-size:15px">Deliver blazing-fast page loads for an exceptional website user experience and improved SEO performance.</p>
<!-- /wp:paragraph --></div>
<!-- /wp:column -->

<!-- wp:column {"style":{"spacing":{"blockGap":"var:preset|spacing|xx-small"}}} -->
<div class="wp-block-column"><!-- wp:image {"id":867,"sizeSlug":"large","linkDestination":"none","align":"center","style":{"color":{"duotone":["rgb(87, 41, 217)","rgb(191, 24, 100)"]}}} -->
<figure class="wp-block-image aligncenter size-large"><img src="' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/featured-3.svg" alt="" class="wp-image-867"/></figure>
<!-- /wp:image -->

<!-- wp:heading {"textAlign":"center","level":4,"style":{"spacing":{"margin":{"top":"var:preset|spacing|small"},"padding":{"bottom":"var:preset|spacing|xx-small"}},"typography":{"fontStyle":"normal","fontWeight":"600","lineHeight":"1.2"}},"fontSize":"medium"} -->
<h4 class="wp-block-heading has-text-align-center has-medium-font-size" style="margin-top:var(--wp--preset--spacing--small);padding-bottom:var(--wp--preset--spacing--xx-small);font-style:normal;font-weight:600;line-height:1.2">SEO Friendly</h4>
<!-- /wp:heading -->

<!-- wp:paragraph {"align":"center","style":{"typography":{"fontSize":"15px"}}} -->
<p class="has-text-align-center" style="font-size:15px">Achieve top search results and grow your audience with optimized, efficient code for better visibility and performance.</p>
<!-- /wp:paragraph --></div>
<!-- /wp:column --></div>
<!-- /wp:columns --></div>
<!-- /wp:group -->',
	'categories' => array( 'spexo', 'featured' ),
);
