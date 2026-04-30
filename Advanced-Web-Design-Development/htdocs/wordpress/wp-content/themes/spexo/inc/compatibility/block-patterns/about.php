<?php
/**
 * About pattern.
 *
 * @package Spexo
 */

return array(
	'title'      => __( 'About', 'spexo' ),
	'content'    => '<!-- wp:group {"metadata":{"categories":["about"],"patternName":"spexo/about","name":"About"},"align":"full","style":{"spacing":{"padding":{"bottom":"60px","top":"60px"}}},"layout":{"type":"constrained"}} -->
<div class="wp-block-group alignfull" style="padding-top:60px;padding-bottom:60px"><!-- wp:paragraph {"align":"center","style":{"typography":{"letterSpacing":"2px","textTransform":"uppercase"},"color":{"text":"#5729d9"},"spacing":{"margin":{"bottom":"0"}}}} -->
<p class="has-text-align-center has-text-color" style="color:#5729d9;margin-bottom:0;letter-spacing:2px;text-transform:uppercase">Who We Are</p>
<!-- /wp:paragraph -->

<!-- wp:heading {"textAlign":"center","level":3,"style":{"spacing":{"margin":{"top":"var:preset|spacing|x-small"}},"typography":{"fontSize":"30px","lineHeight":"1.2"}}} -->
<h3 class="wp-block-heading has-text-align-center" style="margin-top:var(--wp--preset--spacing--x-small);font-size:30px;line-height:1.2">A contemporary restaurant serving irresistible flavors for over 45 years. Savor our seasonal menu and the freshness of natural ingredients.</h3>
<!-- /wp:heading -->

<!-- wp:columns {"align":"wide","style":{"spacing":{"margin":{"top":"35px"}}}} -->
<div class="wp-block-columns alignwide" style="margin-top:35px"><!-- wp:column -->
<div class="wp-block-column"><!-- wp:paragraph -->
<p>Great food is more than just a meal; it\'s an experience. For over 45 years, we\'ve been crafting delicious dishes with the finest ingredients, bringing people together over flavors they love.</p>
<!-- /wp:paragraph -->

<!-- wp:quote {"className":"is-style-default","style":{"spacing":{"margin":{"bottom":"var:preset|spacing|small","top":"0"}}}} -->
<blockquote class="wp-block-quote is-style-default" style="margin-top:0;margin-bottom:var(--wp--preset--spacing--small)"><!-- wp:paragraph {"fontSize":"small"} -->
<p class="has-small-font-size"><em>"Food is not just nourishment; it\'s a journey of taste, passion, and tradition. Every dish tells a story, and we take pride in making each one unforgettable."</em></p>
<!-- /wp:paragraph --></blockquote>
<!-- /wp:quote -->

<!-- wp:paragraph {"style":{"elements":{"link":{"color":{"text":"var:preset|color|primary"}}}},"textColor":"primary"} -->
<p class="has-primary-color has-text-color has-link-color"><strong>— Michael Reynolds, Founder</strong></p>
<!-- /wp:paragraph -->

<!-- wp:cover {"url":"' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/about-1.jpg","id":343,"dimRatio":0,"overlayColor":"base","isUserOverlayColor":true,"minHeight":269,"minHeightUnit":"px","style":{"spacing":{"margin":{"top":"60px"}}},"layout":{"type":"constrained"}} -->
<div class="wp-block-cover" style="margin-top:60px;min-height:269px"><span aria-hidden="true" class="wp-block-cover__background has-base-background-color has-background-dim-0 has-background-dim"></span><img class="wp-block-cover__image-background wp-image-343" alt="" src="' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/about-1.jpg" data-object-fit="cover"/><div class="wp-block-cover__inner-container"><!-- wp:paragraph {"align":"center","placeholder":"Write title…","fontSize":"large"} -->
<p class="has-text-align-center has-large-font-size"></p>
<!-- /wp:paragraph --></div></div>
<!-- /wp:cover --></div>
<!-- /wp:column -->

<!-- wp:column -->
<div class="wp-block-column"><!-- wp:cover {"url":"' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/about-2.jpg","id":342,"dimRatio":0,"customOverlayColor":"#968c81","isUserOverlayColor":true,"minHeight":581,"minHeightUnit":"px","layout":{"type":"constrained"}} -->
<div class="wp-block-cover" style="min-height:581px"><span aria-hidden="true" class="wp-block-cover__background has-background-dim-0 has-background-dim" style="background-color:#968c81"></span><img class="wp-block-cover__image-background wp-image-342" alt="" src="' . trailingslashit( get_template_directory_uri() ) . 'assets/images/patterns/about-2.jpg" data-object-fit="cover"/><div class="wp-block-cover__inner-container"><!-- wp:paragraph {"align":"center","placeholder":"Write title…","fontSize":"large"} -->
<p class="has-text-align-center has-large-font-size"></p>
<!-- /wp:paragraph --></div></div>
<!-- /wp:cover --></div>
<!-- /wp:column -->

<!-- wp:column {"verticalAlignment":"center"} -->
<div class="wp-block-column is-vertically-aligned-center"><!-- wp:group {"style":{"spacing":{"blockGap":"0","padding":{"top":"40px","bottom":"40px"}},"border":{"width":"3px"}},"borderColor":"primary","layout":{"type":"constrained"}} -->
<div class="wp-block-group has-border-color has-primary-border-color" style="border-width:3px;padding-top:40px;padding-bottom:40px"><!-- wp:heading {"textAlign":"center","level":3,"style":{"typography":{"fontSize":"30px","lineHeight":"1.2"}}} -->
<h3 class="wp-block-heading has-text-align-center" style="font-size:30px;line-height:1.2">Contact Us</h3>
<!-- /wp:heading -->

<!-- wp:paragraph {"align":"center"} -->
<p class="has-text-align-center">Your table awaits—call now!</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"align":"center","style":{"spacing":{"margin":{"bottom":"var:preset|spacing|xx-small"}},"typography":{"fontSize":"16px"}}} -->
<p class="has-text-align-center" style="margin-bottom:var(--wp--preset--spacing--xx-small);font-size:16px"><strong>Reserve Your Table</strong></p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"align":"center","style":{"typography":{"fontSize":"25px"}}} -->
<p class="has-text-align-center" style="font-size:25px">+1 1234 5678 90</p>
<!-- /wp:paragraph -->

<!-- wp:group {"style":{"spacing":{"margin":{"top":"40px"}}},"layout":{"type":"constrained"}} -->
<div class="wp-block-group" style="margin-top:40px"><!-- wp:group {"style":{"spacing":{"blockGap":"0"}},"layout":{"type":"constrained"}} -->
<div class="wp-block-group"><!-- wp:paragraph {"align":"center","style":{"spacing":{"margin":{"bottom":"var:preset|spacing|xx-small"}}}} -->
<p class="has-text-align-center" style="margin-bottom:var(--wp--preset--spacing--xx-small)"><strong>Location</strong></p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"align":"center"} -->
<p class="has-text-align-center">Gourmet Avenue, Flavor Town,<br>Street 22, BTK 4567</p>
<!-- /wp:paragraph --></div>
<!-- /wp:group -->

<!-- wp:group {"style":{"spacing":{"blockGap":"0"}},"layout":{"type":"constrained"}} -->
<div class="wp-block-group"><!-- wp:paragraph {"align":"center","style":{"spacing":{"margin":{"bottom":"var:preset|spacing|xx-small"}}}} -->
<p class="has-text-align-center" style="margin-bottom:var(--wp--preset--spacing--xx-small)"><strong>Lunch Time</strong></p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"align":"center"} -->
<p class="has-text-align-center">Monday to Sunday <br>11.00 am – 2.30pm</p>
<!-- /wp:paragraph --></div>
<!-- /wp:group -->

<!-- wp:group {"style":{"spacing":{"blockGap":"0"}},"layout":{"type":"constrained"}} -->
<div class="wp-block-group"><!-- wp:paragraph {"align":"center","style":{"spacing":{"margin":{"bottom":"var:preset|spacing|xx-small"}}}} -->
<p class="has-text-align-center" style="margin-bottom:var(--wp--preset--spacing--xx-small)"><strong>Dinner Time</strong></p>
<!-- /wp:paragraph -->

<!-- wp:paragraph {"align":"center"} -->
<p class="has-text-align-center">Monday to Sunday <br>07.00 pm – 10.00 pm</p>
<!-- /wp:paragraph --></div>
<!-- /wp:group --></div>
<!-- /wp:group --></div>
<!-- /wp:group --></div>
<!-- /wp:column --></div>
<!-- /wp:columns --></div>
<!-- /wp:group -->',
	'categories' => array( 'spexo', 'about' ),
);
