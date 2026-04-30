<?php
/**
 * The header for our theme
 *
 * This is the template that displays all of the <head> section and everything up until <div id="content">
 *
 * @link https://developer.wordpress.org/themes/basics/template-files/#template-partials
 *
 * @package Spexo
 */

if ( ! defined( 'ABSPATH' ) ) {
    exit; // Exit if accessed directly.
}

$viewport_content = apply_filters( 'tmpcoder_viewport_content', 'width=device-width, initial-scale=1' );
$enable_skip_link = apply_filters( 'tmpcoder_enable_skip_link', true );
$skip_link_url = apply_filters( 'tmpcoder_skip_link_url', '#content' );

?>
<!doctype html>
<html <?php language_attributes(); ?>>
<head>
	<meta charset="<?php bloginfo( 'charset' ); ?>">
	<meta name="viewport" content="<?php echo esc_attr( $viewport_content ); ?>">
	<link rel="profile" href="https://gmpg.org/xfn/11">
	<?php wp_head(); ?>
</head>
<body <?php body_class(); ?>>
<?php wp_body_open(); ?>
	
<?php if ( $enable_skip_link ) { ?>
<a class="skip-link screen-reader-text" href="<?php echo esc_url( $skip_link_url ); ?>"><?php echo esc_html__( 'Skip to content','spexo' ); ?></a>
<?php } ?>

<?php 

do_action( 'tmpcoder_wp_body_open' );

tmpcoder_header_before();

tmpcoder_header();

tmpcoder_header_after();
