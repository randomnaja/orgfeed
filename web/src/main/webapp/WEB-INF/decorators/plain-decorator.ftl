<!DOCTYPE html PUBLIC
        "-//W3C//DTD XHTML 1.1 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" class="yui3-loading">
<head>
    <title>${title!'Missing Page Title!!'}</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <meta name="URL" content="<@s.url forceAddSchemeHostAndPort='true' includeParams='all' />">

    <script type="text/javascript" src="${contextPath}/scripts/prototype-1.7.1.js"></script>

    <script type="text/javascript" src="http://yui.yahooapis.com/3.9.0/build/yui/yui-min.js"></script>
    <#--<script type="text/javascript" src="${contextPath}/scripts/yui/yui-min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/datatype-date-parse-min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/event-custom-base-min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/oop-min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/event-custom-complex-min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/datatype-date-format-min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/datatype-date-format_th-TH.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/datatype-date-math-min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/datatype-date-parse-min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/datatype-number-format-min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/datatype-number-parse-min.js"></script>
    <script type="text/javascript" src="${contextPath}/scripts/yui/intl-min.js"></script>
    -->
    <script type="text/javascript" src="${contextPath}/scripts/common.js"></script>

    <#--<link href="http://yui.yahooapis.com/3.9.0/build/cssreset/reset-min.css" rel="stylesheet" type="text/css">
    <link href="http://yui.yahooapis.com/3.9.0/build/cssfonts/fonts-min.css" rel="stylesheet" type="text/css">
    <link href="http://yui.yahooapis.com/3.9.0/build/cssbase/base-min.css" rel="stylesheet" type="text/css">
    <link href="http://yui.yahooapis.com/3.9.0/build/cssbutton/cssbutton-min.css" rel="stylesheet" type="text/css">
    <link href="http://yui.yahooapis.com/2013-03-13/gallery-layout/assets/gallery-layout-core.css" rel="stylesheet" type="text/css">
    <link href="http://yui.yahooapis.com/2013-03-13/gallery-layout/assets/skins/sam/gallery-layout.css" rel="stylesheet" type="text/css">
    <link href="http://yui.yahooapis.com/2013-03-13/gallery-busyoverlay/assets/skins/sam/gallery-busyoverlay.css" rel="stylesheet" type="text/css">
-->
    <link href="${contextPath}/css/public.css" rel="stylesheet" type="text/css">
    <link href="${contextPath}/css/github-markdown.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
    <link href="${contextPath}/css/lightbox.css" rel="stylesheet" type="text/css">
    <link href="${contextPath}/css/octicons.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="//fonts.googleapis.com/css?family=Fjalla+One">

    <script>
        var $j = jQuery.noConflict();
        // $j is now an alias to the jQuery function; creating the new alias is optional.
    </script>
    <script type="text/javascript" src="${contextPath}/scripts/imagelightbox.js"></script>

${head}
</head>
<body class="yui3-skin-sam">
    <div id="main-layout">
        ${body}
    </div>
</body>
</html>
