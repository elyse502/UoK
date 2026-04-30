<?php

if ( ! defined( 'ABSPATH' ) ) {
    exit; // Exit if accessed directly.
}

if ( !class_exists('Tmpcoder_Forms_Hooks') ){
    class Tmpcoder_Forms_Hooks {

        public function __construct()
        {  
            /* For Change  Search Box */
            add_filter( 'get_search_form', array($this,'tmpcoder_serch_form') );
        }

        /*
        * Customize Serch Form
        */
        
        function tmpcoder_serch_form( $form ) {

            $form = '<form method="get" id="searchform1" class="searchform" action="'.esc_url(home_url('/')).'">
                <div class="blog-search mb-45">
                    <input type="text" value="'.esc_attr(get_search_query()).'" name="s" id="s" placeholder="'.esc_attr__('Search...', 'spexo').'"/>
                    <button class="blog-search-btn" type="submit" id="searchsubmit"></button>
                </div>
            </form>';
            
            return $form;
        }
    }
    new Tmpcoder_Forms_Hooks();
}