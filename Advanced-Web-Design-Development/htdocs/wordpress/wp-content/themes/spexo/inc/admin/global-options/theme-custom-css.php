<?php
/**
 * @author  Spexo
 * @since   1.0.0
 * @version 1.0.0
 */

$opt_name = TMPCODER_THEME_OPTION_NAME;

Redux::setSection( $opt_name, 
    array(
        'title'   => esc_html__( 'Custom CSS','spexo' ),
        'id'      => 'tmpcoder_custom_css_section',        
        'icon'    => 'el el-css',
        'desc'    => __( 'Add your custom CSS to style the theme.', 'spexo' ),
        'fields' => array(
            array(
                'id'       => 'tmpcoder_custom_css',
                'type'     => 'ace_editor',
                'mode'     => 'css',
                'title'    => __( 'Custom CSS', 'spexo' ),
                'subtitle' => __( 'Enter your custom CSS.', 'spexo' ),
                'theme'    => 'chrome',
                'desc'     => __( 'Add your custom CSS here.', 'spexo' ),
                'default'  => '',
            ),
        ),
    )
);

Redux::setSection( $opt_name, 
    array(
        'title'   => esc_html__( 'Custom JS', 'spexo' ),
        'id'      => 'tmpcoder_custom_js_section',
        'icon' => 'el el-file-edit',
        'desc'    => __( 'Add custom JavaScript to the head or footer.', 'spexo' ),
        'fields'  => array(
            array(
                'id'       => 'tmpcoder_custom_js_head',
                'type'     => 'ace_editor',
                'mode'     => 'javascript',
                'title'    => __( 'Add JS Under &lt;head&gt;', 'spexo' ),
                'subtitle' => __( 'To add your JavaScript code inside the &lt;head&gt; tag, enter it here. The code will be displayed within the &lt;head&gt; tag.', 'spexo' ),
                'theme'    => 'chrome',
                'desc'     => __( 'Do not include &lt;script&gt; tags. Just the JS code.', 'spexo' ),
                'default'  => '',
            ),
            array(
                'id'       => 'tmpcoder_custom_js_footer',
                'type'     => 'ace_editor',
                'mode'     => 'javascript',
                'title'    => __( 'Add JS Before &lt;/body&gt;', 'spexo' ),
                'subtitle' => __( 'To add your JavaScript code before the closing &lt;/body&gt; tag, enter it here. The code will be displayed just before the &lt;/body&gt; tag.', 'spexo' ),
                'theme'    => 'chrome',
                'desc'     => __( 'Do not include &lt;script&gt; tags. Just the JS code.', 'spexo' ),
                'default'  => '',
            ),
        ),
    )
);