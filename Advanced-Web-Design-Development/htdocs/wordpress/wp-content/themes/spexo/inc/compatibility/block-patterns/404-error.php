<?php
/**
 * 404 error pattern.
 *
 * @package Spexo
 */

return array(
	'title'      => __( '404 Error', 'spexo' ),
	'content'    => '<!-- wp:group {"tagName":"main","metadata":{"categories":["spexo"],"patternName":"spexo/404-error","name":"404 Error Page"},"style":{"spacing":{"padding":{"top":"80px","bottom":"80px"}}},"layout":{"inherit":true,"type":"constrained"}} -->
<main class="wp-block-group" style="padding-top:80px;padding-bottom:80px"><!-- wp:heading {"className":"has-text-align-center","style":{"typography":{"fontSize":"clamp(4rem, 40vw, 20rem)","fontWeight":"200","lineHeight":"1","fontStyle":"normal"}}} -->
<h2 class="wp-block-heading has-text-align-center" style="font-size:clamp(4rem, 40vw, 20rem);font-style:normal;font-weight:200;line-height:1"> 4<mark style="background-color: rgba(0, 0, 0, 0); color:var(--theme-button-background);">0</mark>4 </h2>
<!-- /wp:heading -->

<!-- wp:paragraph {"align":"center"} -->
<p class="has-text-align-center">This page could not be found. Try searching instead.</p>
<!-- /wp:paragraph -->

<!-- wp:search {"label":"Search","showLabel":false,"placeholder":"Search Now","width":75,"widthUnit":"%","buttonText":"Search","buttonUseIcon":true,"align":"center","backgroundColor":"primary"} /--></main>
<!-- /wp:group -->',
	'categories' => array( 'spexo', 'columns' ),
);
