<?php
/**
 * Banner pattern.
 *
 * @package Spexo
 */

return array(
	'title'      => __( 'Banner', 'spexo' ),
	'content'    => '<!-- wp:cover {"url":"' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/business-banner-bg.png","id":702,"dimRatio":0,"overlayColor":"background-secondary","isUserOverlayColor":true,"minHeight":685,"minHeightUnit":"px","isDark":false,"metadata":{"name":"Banner","categories":["banner"],"patternName":"spexo/banner"},"align":"full","style":{"spacing":{"padding":{"top":"0","right":"0","bottom":"0","left":"0"}}},"layout":{"type":"default"}} -->
<div class="wp-block-cover alignfull is-light" style="padding-top:0;padding-right:0;padding-bottom:0;padding-left:0;min-height:685px"><span aria-hidden="true" class="wp-block-cover__background has-background-secondary-background-color has-background-dim-0 has-background-dim"></span><img class="wp-block-cover__image-background wp-image-702" alt="" src="' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/business-banner-bg.png" data-object-fit="cover"/><div class="wp-block-cover__inner-container"><!-- wp:group {"align":"wide","style":{"spacing":{"padding":{"left":"var:preset|spacing|40","right":"var:preset|spacing|40"},"margin":{"top":"0","bottom":"0"}}},"layout":{"type":"constrained"}} -->
<div class="wp-block-group alignwide" style="margin-top:0;margin-bottom:0;padding-right:var(--wp--preset--spacing--40);padding-left:var(--wp--preset--spacing--40)"><!-- wp:columns {"verticalAlignment":"center","align":"wide","style":{"spacing":{"padding":{"top":"0","bottom":"0"}}}} -->
<div class="wp-block-columns alignwide are-vertically-aligned-center" style="padding-top:0;padding-bottom:0"><!-- wp:column {"verticalAlignment":"center","width":"55%"} -->
<div class="wp-block-column is-vertically-aligned-center" style="flex-basis:55%"><!-- wp:group {"align":"wide","style":{"spacing":{"blockGap":"0px","padding":{"right":"0px","left":"0px","top":"40px","bottom":"var:preset|spacing|40"}}},"layout":{"type":"constrained"}} -->
<div class="wp-block-group alignwide" style="padding-top:40px;padding-right:0px;padding-bottom:var(--wp--preset--spacing--40);padding-left:0px"><!-- wp:paragraph {"align":"left","style":{"typography":{"letterSpacing":"2px","textTransform":"uppercase"},"color":{"text":"#5729d9"},"spacing":{"margin":{"bottom":"var:preset|spacing|xx-small"}}},"fontSize":"x-small"} -->
<p class="has-text-align-left has-text-color has-x-small-font-size" style="color:#5729d9;margin-bottom:var(--wp--preset--spacing--xx-small);letter-spacing:2px;text-transform:uppercase">Build Your Website</p>
<!-- /wp:paragraph -->

<!-- wp:heading {"textAlign":"left","style":{"typography":{"lineHeight":"1.3","fontStyle":"normal","fontWeight":"700","fontSize":"40px"}}} -->
<h2 class="wp-block-heading has-text-align-left" style="font-size:40px;font-style:normal;font-weight:700;line-height:1.3">Tailored Solutions for Your Business</h2>
<!-- /wp:heading -->

<!-- wp:group {"layout":{"type":"default"}} -->
<div class="wp-block-group"><!-- wp:columns {"style":{"spacing":{"margin":{"top":"0","bottom":"0"},"padding":{"right":"0","left":"0","top":"0","bottom":"0"}}}} -->
<div class="wp-block-columns" style="margin-top:0;margin-bottom:0;padding-top:0;padding-right:0;padding-bottom:0;padding-left:0"><!-- wp:column {"width":"","style":{"spacing":{"padding":{"right":"0","left":"0","top":"0","bottom":"0"}}}} -->
<div class="wp-block-column" style="padding-top:0;padding-right:0;padding-bottom:0;padding-left:0"><!-- wp:paragraph -->
<p>Craft a stunning online presence with our expert solutions. Elevate your brand, engage your audience, and grow your business effortlessly.</p>
<!-- /wp:paragraph --></div>
<!-- /wp:column -->

<!-- wp:column {"width":"33.33%","style":{"spacing":{"padding":{"right":"0","left":"0","top":"0","bottom":"0"}}}} -->
<div class="wp-block-column" style="padding-top:0;padding-right:0;padding-bottom:0;padding-left:0;flex-basis:33.33%"></div>
<!-- /wp:column --></div>
<!-- /wp:columns --></div>
<!-- /wp:group -->

<!-- wp:buttons {"style":{"spacing":{"margin":{"top":"40px","bottom":"0px"},"blockGap":"20px"}},"layout":{"type":"flex","justifyContent":"left"}} -->
<div class="wp-block-buttons" style="margin-top:40px;margin-bottom:0px"><!-- wp:button {"textAlign":"left","className":"is-style-fill"} -->
<div class="wp-block-button is-style-fill"><a class="wp-block-button__link has-text-align-left wp-element-button" href="#">About Us</a></div>
<!-- /wp:button --></div>
<!-- /wp:buttons --></div>
<!-- /wp:group --></div>
<!-- /wp:column -->

<!-- wp:column {"verticalAlignment":"center"} -->
<div class="wp-block-column is-vertically-aligned-center"><!-- wp:image {"id":170,"aspectRatio":"3/4","scale":"cover","sizeSlug":"full","linkDestination":"none"} -->
<figure class="wp-block-image size-full"><img src="' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/business-banner.png" alt="" class="wp-image-170" style="aspect-ratio:3/4;object-fit:cover"/></figure>
<!-- /wp:image --></div>
<!-- /wp:column --></div>
<!-- /wp:columns --></div>
<!-- /wp:group --></div></div>
<!-- /wp:cover -->',
	'categories' => array( 'spexo', 'banner' ),
);
