<?php
/**
 * The template for displaying the footer
 *
 * Contains the closing of the #content div and all content after.
 *
 * @link https://developer.wordpress.org/themes/basics/template-files/#template-partials
 *
 * @package Spexo
 */

if ( ! defined( 'ABSPATH' ) ) {
    exit; // Exit if accessed directly.
}

tmpcoder_footer_before();
        
tmpcoder_footer();
    
tmpcoder_footer_after(); 

?>

<?php wp_footer(); ?>

</body>
</html>