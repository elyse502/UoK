<?php
/**
 * Blog grid pattern.
 *
 * @package Spexo
 */

return array(
	'title'      => __( 'Blog grid', 'spexo' ),
	'content'    => '<!-- wp:group {"metadata":{"categories":["posts"],"patternName":"spexo/blog-grid","name":"Blog grid"},"align":"wide","className":" ","style":{"spacing":{"padding":{"top":"var:preset|spacing|x-large","bottom":"var:preset|spacing|x-large"},"blockGap":"var:preset|spacing|large"}},"layout":{"type":"constrained"}} -->
<div class="wp-block-group alignwide" style="padding-top:var(--wp--preset--spacing--x-large);padding-bottom:var(--wp--preset--spacing--x-large)"><!-- wp:heading {"level":1,"align":"wide","style":{"typography":{"fontSize":"52px"}}} -->
<h1 class="wp-block-heading alignwide" style="font-size:52px">Latest Posts</h1>
<!-- /wp:heading -->

<!-- wp:query {"queryId":0,"query":{"perPage":6,"pages":0,"offset":0,"postType":"post","order":"desc","orderBy":"date","author":"","search":"","exclude":[],"sticky":"","inherit":false},"align":"wide","layout":{"type":"constrained"}} -->
<div class="wp-block-query alignwide"><!-- wp:post-template {"align":"wide","layout":{"type":"grid","columnCount":3}} -->
<!-- wp:post-featured-image {"isLink":true,"height":"400px","style":{"border":{"radius":"8px"}}} /-->

<!-- wp:group {"style":{"spacing":{"padding":{"top":"var:preset|spacing|xx-small","right":"var:preset|spacing|x-small","left":"var:preset|spacing|x-small"},"blockGap":"0"}},"layout":{"type":"flex","orientation":"vertical","justifyContent":"left","flexWrap":"wrap"}} -->
<div class="wp-block-group" style="padding-top:var(--wp--preset--spacing--xx-small);padding-right:var(--wp--preset--spacing--x-small);padding-left:var(--wp--preset--spacing--x-small)"><!-- wp:post-terms {"term":"category","className":"tmpcoder-is-style-post-terms-pill","style":{"typography":{"fontStyle":"normal","fontWeight":"500","textDecoration":"none"},"layout":{"selfStretch":"fit","flexSize":null}}} /-->

<!-- wp:post-title {"level":3,"isLink":true,"style":{"spacing":{"margin":{"top":"var:preset|spacing|xx-small"}},"typography":{"fontSize":"28px","lineHeight":"1.2"}}} /-->

<!-- wp:group {"style":{"spacing":{"blockGap":"var:preset|spacing|xx-small","margin":{"top":"var:preset|spacing|xx-small"}}},"textColor":"neutral","layout":{"type":"flex","flexWrap":"nowrap"}} -->
<div class="wp-block-group has-neutral-color has-text-color" style="margin-top:var(--wp--preset--spacing--xx-small)"><!-- wp:post-author {"avatarSize":24,"showAvatar":false,"isLink":true,"style":{"color":{"text":"#777777"},"elements":{"link":{"color":{"text":"#777777"}}}}} /-->

<!-- wp:paragraph {"fontSize":"x-small"} -->
<p class="has-x-small-font-size">·</p>
<!-- /wp:paragraph -->

<!-- wp:post-date {"format":"M j, Y"} /--></div>
<!-- /wp:group --></div>
<!-- /wp:group -->
<!-- /wp:post-template -->

<!-- wp:query-pagination {"paginationArrow":"arrow","layout":{"type":"flex","justifyContent":"center"}} -->
<!-- wp:query-pagination-previous /-->

<!-- wp:query-pagination-numbers /-->

<!-- wp:query-pagination-next /-->
<!-- /wp:query-pagination --></div>
<!-- /wp:query --></div>
<!-- /wp:group -->',
	'categories' => array( 'spexo', 'posts' ),
);
