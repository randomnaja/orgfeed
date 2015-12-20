<#-- @ftlvariable name="pageVal" type="randomnaja.orgfeed.action.MainAction.PageVariable" -->
<!DOCTYPE html PUBLIC
        "-//W3C//DTD XHTML 1.1 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Main</title>

    <script type="text/javascript">
        APP.namespace('actions', {
            getAttachment: '${packageURI}/getAttachment.html'
        });
    </script>
    <style type="text/css">
        .image_float {
            display: block;
        }
    </style>
    <script>
        var activityIndicatorOn = function()
        {
            $j( '<div id="imagelightbox-loading"><div></div></div>' ).appendTo( 'body' );
        },
        activityIndicatorOff = function()
        {
            $j( '#imagelightbox-loading' ).remove();
        },

// OVERLAY

        overlayOn = function()
        {
            $j( '<div id="imagelightbox-overlay"></div>' ).appendTo( 'body' );
        },
        overlayOff = function()
        {
            $j( '#imagelightbox-overlay' ).remove();
        },

// CLOSE BUTTON

        closeButtonOn = function( instance )
        {
            $j( '<button type="button" id="imagelightbox-close" title="Close"></button>' ).appendTo( 'body' ).on( 'click touchend', function(){ $j( this ).remove(); instance.quitImageLightbox(); return false; });
        },
        closeButtonOff = function()
        {
            $j( '#imagelightbox-close' ).remove();
        },

// CAPTION

        captionOn = function()
        {
            var description = $j( 'a[href="' + $j( '#imagelightbox' ).attr( 'src' ) + '"] img' ).attr( 'alt' );
            if( description.length > 0 )
                $j( '<div id="imagelightbox-caption">' + description + '</div>' ).appendTo( 'body' );
        },
        captionOff = function()
        {
            $j( '#imagelightbox-caption' ).remove();
        },

// NAVIGATION

        navigationOn = function( instance, selector )
        {
            var images = $j( selector );
            if( images.length )
            {
                var nav = $j( '<div id="imagelightbox-nav"></div>' );
                for( var i = 0; i < images.length; i++ )
                    nav.append( '<button type="button"></button>' );

                nav.appendTo( 'body' );
                nav.on( 'click touchend', function(){ return false; });

                var navItems = nav.find( 'button' );
                navItems.on( 'click touchend', function()
                        {
                            var $this = $j( this );
                            if( images.eq( $this.index() ).attr( 'href' ) != $j( '#imagelightbox' ).attr( 'src' ) )
                                instance.switchImageLightbox( $this.index() );

                            navItems.removeClass( 'active' );
                            navItems.eq( $this.index() ).addClass( 'active' );

                            return false;
                        })
                        .on( 'touchend', function(){ return false; });
            }
        },
        navigationUpdate = function( selector )
        {
            var items = $j( '#imagelightbox-nav button' );
            items.removeClass( 'active' );
            items.eq( $j( selector ).filter( '[href="' + $j( '#imagelightbox' ).attr( 'src' ) + '"]' ).index( selector ) ).addClass( 'active' );
        },
        navigationOff = function()
        {
            $j( '#imagelightbox-nav' ).remove();
        },

// ARROWS

        arrowsOn = function( instance, selector )
        {
            var $arrows = $j( '<button type="button" class="imagelightbox-arrow imagelightbox-arrow-left"></button><button type="button" class="imagelightbox-arrow imagelightbox-arrow-right"></button>' );

            $arrows.appendTo( 'body' );

            $arrows.on( 'click touchend', function( e )
            {
                e.preventDefault();

                var $this	= $j( this ),
                        $target	= $j( selector + '[href="' + $j( '#imagelightbox' ).attr( 'src' ) + '"]' ),
                        index	= $target.index( selector );

                if( $this.hasClass( 'imagelightbox-arrow-left' ) )
                {
                    index = index - 1;
                    if( !$j( selector ).eq( index ).length )
                        index = $j( selector ).length;
                }
                else
                {
                    index = index + 1;
                    if( !$j( selector ).eq( index ).length )
                        index = 0;
                }

                instance.switchImageLightbox( index );
                return false;
            });
        },
        arrowsOff = function()
        {
            $j( '.imagelightbox-arrow' ).remove();
        };
    </script>

    <script type="text/javascript">
        (function() {
            APP.namespace('request.parameters', {});
            document.on('layout:init', function(event) {
            });// end layout:init

            document.on('dom:loaded', function(evt){

                $$('#help_expand')[0].on('click', function(){
                    var s = $$('#search_help')[0].getAttribute('style');

                    if (s == 'display:none') {
                        $$('#search_help')[0].setAttribute('style', 'display:block');
                    } else {
                        $$('#search_help')[0].setAttribute('style', 'display:none');
                    }
                });

                $$('.attachment').each(function(ele,idx){
                    ele.innerHTML = '';
                    new Ajax.Request(APP.actions.getAttachment, {
                        method : 'POST',
                        asynchronous : true,
                        encoding : 'UTF-8',
                        evalJSON : true,
                        parameters : Object.toQueryString({"param.postId":ele.getAttribute('data-postid')}),
                        onSuccess : (function(transport) {
                            APP.response.handlers.success(transport, function(json){
                                // json.data //attachments
                                var ul = new Element('ul');
                                json.data.each(function(media,mediaIdx){
                                    var img = new Element('img', {
                                        src:media.imageUrl,
                                        /*width: 60,*/
                                        height: 60,
                                        class:'image_float',
                                        alt: media.mediaUrl
                                    });
                                    var a = new Element('a', {
                                        href: media.imageUrl,
                                        'data-imagelightbox' : ele.id
                                    });
                                    a.appendChild(img);
                                    var li = new Element('li');
                                    li.appendChild(a);
                                    ul.appendChild(li);
                                });
                                ele.appendChild(ul);

                                var selectorF = 'a[data-imagelightbox="'+ele.id+'"]';
                                var instanceF = $j( selectorF ).imageLightbox(
                                        {
                                            allowedTypes:   '.*',
                                            preloadNext:    true,                   // bool;            silently preload the next image
                                            enableKeyboard: true,                   // bool;            enable keyboard shortcuts (arrows Left/Right and Esc)
                                            quitOnEnd:      true,
                                            animationSpeed: 100,
                                            onStart:		function() { navigationOn( instanceF, selectorF ); overlayOn(); closeButtonOn( instanceF ); arrowsOn( instanceF, selectorF ); },
                                            onEnd:			function() { navigationOff(); overlayOff(); captionOff(); closeButtonOff(); arrowsOff(); activityIndicatorOff(); },
                                            onLoadStart: 	function() { captionOff(); activityIndicatorOn(); },
                                            onLoadEnd:	 	function() { navigationUpdate( selectorF ); captionOn(); activityIndicatorOff(); $j( '.imagelightbox-arrow' ).css( 'display', 'block' ); }
                                        });
                            });
                        })
                    });
                });

            });
        })();
    </script>
</head>

<body>
<div id="criteria">
    <form action="${packageURI}/searchAndRenderPageId.html" method="GET">
        <label>Search : </label><input type="text" name="param.searchText" value="${param.searchText!?html}"/>
        <input type="hidden" name="param.pageId" value="${param.pageId?html}"/>
        <input type="submit" value="Go"/>
    </form>
    <a href="#" id="help_expand">HELP</a>
</div>
<div id="search_help" class="markdown-body" style="display:none">
    <h2>Terms</h2>
    <p>
        A Single Term is a single word such as
        <blockquote>
            "test"
        </blockquote>
        or
        <blockquote>
            "hello".
        </blockquote>
    </p>

    <h2>Fields</h2>
    <p>
        typing field name followed by a colon ":"

        <blockquote>
            title:"The Right Way" AND text:go
        </blockquote>

        or

        <blockquote>
            title:"Do it right" AND right
        </blockquote>

        <blockquote>
            title:Do it right
        </blockquote>

    </p>

    <h2>Term Modifiers</h2>
    <p>
        To perform a single character wildcard search use the "?" symbol.

        To perform a multiple character wildcard search use the "*" symbol.

        <blockquote>
            te?t
        </blockquote>

        <blockquote>
        test*
        </blockquote>
        You can also use the wildcard searches in the middle of a term.

        <blockquote>
        te*t
        </blockquote>
    </p>

    <h2>Fuzzy Searches</h2>

    <p>
        fuzzy searches based on the Levenshtein Distance.
        To do a fuzzy search use the tilde, "~", symbol at the end of a Single word Term.

        <blockquote>
            roam~
        </blockquote>

        Specify the required similarity. The value is between 0 and 1,
        with a value closer to 1 only terms with a higher similarity will be matched. For example:

        <blockquote>
            roam~0.8
        </blockquote>

        The default that is used if the parameter is not given is 0.5.

    </p>

    <h2>Proximity Searches</h2>

    <p>
        <blockquote>"jakarta apache"~10</blockquote>
    </p>

    <h2>Range Searches</h2>
    Range Queries allow one to match documents whose field(s) values are between the lower and upper bound specified by the Range Query. Range Queries can be inclusive or exclusive of the upper and lower bounds. Sorting is done lexicographically.

    <p>
        Inclusive

        <blockquote>
            mod_date:[20020101 TO 20030101]
        </blockquote>

        Exclusive

        <blockquote>
            title:{Aida TO Carmen}
        </blockquote>
    </p>

    <h2>Boosting a Term</h2>

    <p>
        Default boost factor is 1.

        <blockquote>jakarta^4 apache</blockquote>

        <blockquote>"jakarta apache"^4 "Apache Lucene"</blockquote>
    </p>


    <h2>Boolean Operators</h2>

    <p>
        The OR operator is the default conjunction operator.

        <blockquote>
        "jakarta apache" jakarta
        </blockquote>
        or

        <blockquote>
        "jakarta apache" OR jakarta
        </blockquote>

        AND
        <blockquote>
            "jakarta apache" AND "Apache Lucene"
        </blockquote>

        +
        <blockquote>
            +jakarta lucene
        </blockquote>

        NOT
        <blockquote>
            "jakarta apache" NOT "Apache Lucene"
        </blockquote>

        <blockquote>
            NOT "jakarta apache"
        </blockquote>

        -
        <blockquote>
            "jakarta apache" -"Apache Lucene"
        </blockquote>
    </p>

    <h2>Grouping</h2>

    <p>

        <blockquote>
            (jakarta OR apache) AND website
        </blockquote>
    </p>

    <h2>Field Grouping</h2>
    <p>
        <blockquote>
            title:(+return +"pink panther")
        </blockquote>
    </p>

    <h2>Escaping Special Characters</h2>
    <p>
        List of special characters
        <blockquote>
            + - && || ! ( ) { } [ ] ^ " ~ * ? : \
        </blockquote>

        Example.

        <blockquote>
            \(1\+1\)\:2
        </blockquote>
    </p>

</div>
<div id="container">
    <#list pageVal.posts as post>
        <div class="desc_container">
            <div class="desc">
                <div class="desc_label"><span class="desc_label_ele octicon octicon-comment"></span></div>
                <div class="desc_val"><span class="desc_val_ele">${post.message!?html}</span></div>
            </div>
            <div>
                <span>${post.createdTime}</span>
                <span><a href="${post.postUrl!?html}" target="_blank" title="${post.facebookId?html}">Facebook Link</a></span>
            </div>
        </div>
        <div id="post_${post.id}" class="attachment" data-postid="${post.id}">
            PLACEHOLDER : id : post_${post.facebookId?html}
        </div>
    <#else>
        Not found
    </#list>
</div>
</body>

</html>

