<?php
/**
 * Blog simple pattern.
 *
 * @package Spexo
 */

return array(
	'title'      => __( 'Blog simple', 'spexo' ),
	'content'    => '<!-- wp:group {"metadata":{"categories":["posts"],"patternName":"spexo/blog-simple","name":"Blog simple"},"align":"wide","style":{"spacing":{"padding":{"top":"var:preset|spacing|x-large","bottom":"var:preset|spacing|x-large"},"blockGap":"var:preset|spacing|x-large"}},"layout":{"type":"constrained"}} -->
<div class="wp-block-group alignwide" style="padding-top:var(--wp--preset--spacing--x-large);padding-bottom:var(--wp--preset--spacing--x-large)"><!-- wp:heading {"textAlign":"left","level":1,"align":"wide","style":{"typography":{"fontSize":"52px","lineHeight":"1.2"}}} -->
<h1 class="wp-block-heading alignwide has-text-align-left" style="font-size:52px;line-height:1.2">Latest Posts</h1>
<!-- /wp:heading -->

<!-- wp:query {"queryId":15,"query":{"perPage":9,"pages":0,"offset":0,"postType":"post","order":"desc","orderBy":"date","author":"","search":"","exclude":[],"sticky":"","inherit":false},"align":"wide","layout":{"type":"default"}} -->
<div class="wp-block-query alignwide"><!-- wp:post-template {"align":"wide","layout":{"type":"default","columnCount":3}} -->
<!-- wp:columns {"style":{"spacing":{"blockGap":{"top":"var:preset|spacing|large","left":"var:preset|spacing|large"},"margin":{"bottom":"var:preset|spacing|medium"}}}} -->
<div class="wp-block-columns" style="margin-bottom:var(--wp--preset--spacing--medium)"><!-- wp:column {"verticalAlignment":"center","width":"75%","style":{"spacing":{"blockGap":"var:preset|spacing|xx-small"}}} -->
<div class="wp-block-column is-vertically-aligned-center" style="flex-basis:75%"><!-- wp:post-terms {"term":"category","className":"tmpcoder-is-style-post-terms-pill","style":{"typography":{"fontStyle":"normal","fontWeight":"500"},"spacing":{"padding":{"bottom":"var:preset|spacing|xx-small"}}}} /-->

<!-- wp:post-title {"level":3,"isLink":true,"style":{"spacing":{"margin":{"top":"var:preset|spacing|x-small"}},"typography":{"fontSize":"33px","lineHeight":"1.2"}}} /-->

<!-- wp:post-excerpt {"moreText":"","style":{"typography":{"fontSize":"16px"}}} /-->

<!-- wp:group {"style":{"spacing":{"blockGap":"var:preset|spacing|xx-small","padding":{"top":"var:preset|spacing|xx-small","bottom":"var:preset|spacing|xx-small"}}},"textColor":"neutral","fontSize":"small","layout":{"type":"flex","flexWrap":"nowrap"}} -->
<div class="wp-block-group has-neutral-color has-text-color has-small-font-size" style="padding-top:var(--wp--preset--spacing--xx-small);padding-bottom:var(--wp--preset--spacing--xx-small)"><!-- wp:post-author {"showAvatar":false,"showBio":false,"isLink":true,"style":{"elements":{"link":{"color":{"text":"#777777"}}},"color":{"text":"#777777"}}} /-->

<!-- wp:paragraph {"fontSize":"x-small"} -->
<p class="has-x-small-font-size">·</p>
<!-- /wp:paragraph -->

<!-- wp:post-date {"format":"M j, Y"} /--></div>
<!-- /wp:group --></div>
<!-- /wp:column -->

<!-- wp:column {"verticalAlignment":"center","width":"25%"} -->
<div class="wp-block-column is-vertically-aligned-center" style="flex-basis:25%"><!-- wp:post-featured-image {"isLink":true,"height":"224px","style":{"border":{"radius":"8px"}}} /--></div>
<!-- /wp:column --></div>
<!-- /wp:columns -->

<!-- wp:separator {"className":"is-style-wide","style":{"spacing":{"margin":{"top":"var:preset|spacing|x-small","bottom":"0"}},"color":{"background":"#e1e5ea"}}} -->
<hr class="wp-block-separator has-text-color has-alpha-channel-opacity has-background is-style-wide" style="margin-top:var(--wp--preset--spacing--x-small);margin-bottom:0;background-color:#e1e5ea;color:#e1e5ea"/>
<!-- /wp:separator -->
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
