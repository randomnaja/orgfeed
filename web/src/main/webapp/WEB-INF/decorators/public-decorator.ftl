<!DOCTYPE html PUBLIC
        "-//W3C//DTD XHTML 1.1 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" class="yui3-loading">
<head>
    <title>${title!'Missing Page Title!!'}</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <meta name="URL" content="<@s.url forceAddSchemeHostAndPort='true' includeParams='all' />">
    <link rel="shortcut icon" type="image/png" href="<@s.url value="/" />images/oli-icon.png">
    <link href="${jsYuiHome}/3.9.0/build/cssreset/reset-min.css" rel="stylesheet" type="text/css">
    <link href="${jsYuiHome}/3.9.0/build/cssfonts/fonts-min.css" rel="stylesheet" type="text/css">
    <link href="${jsYuiHome}/3.9.0/build/cssbase/base-min.css" rel="stylesheet" type="text/css">
    <link href="${jsYuiHome}/3.9.0/build/cssbutton/cssbutton-min.css" rel="stylesheet" type="text/css">
    <link href="${jsYuiGallery}/2013-03-13/gallery-layout/assets/gallery-layout-core.css" rel="stylesheet" type="text/css">
    <link href="${jsYuiGallery}/2013-03-13/gallery-layout/assets/skins/sam/gallery-layout.css" rel="stylesheet" type="text/css">
    <link href="${jsYuiGallery}/2013-03-13/gallery-busyoverlay/assets/skins/sam/gallery-busyoverlay.css" rel="stylesheet" type="text/css">
    <link href="<@s.url value="/" />css/default.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${jsPrototypeHome}/prototype-1.7.1.js"></script>
    <script type="text/javascript" src="${jsYuiHome}/3.9.0/build/yui/yui-min.js"></script>
    <script type="text/javascript" src="${jsYuiGallery}/2013-03-13/gallery-layout/gallery-layout-min.js"></script>
    <script type="text/javascript" src="${jsYuiGallery}/2013-03-13/gallery-layout-cols/gallery-layout-cols-min.js"></script>
    <script type="text/javascript" src="${jsYuiGallery}/2013-03-13/gallery-dimensions/gallery-dimensions-min.js"></script>
    <script type="text/javascript" src="${jsYuiGallery}/2013-03-13/gallery-node-optimizations/gallery-node-optimizations-min.js"></script>
    <script type="text/javascript" src="${jsYuiGallery}/2013-03-13/gallery-funcprog/gallery-funcprog-min.js"></script>
    <script type="text/javascript" src="${jsYuiGallery}/2013-03-13/gallery-object-extras/gallery-object-extras-min.js"></script>
    <script type="text/javascript" src="${jsYuiGallery}/2013-03-13/gallery-nodelist-extras/gallery-nodelist-extras-min.js"></script>
    <script type="text/javascript" src="${jsYuiGallery}/2013-03-13/gallery-nodelist-extras2/gallery-nodelist-extras2-min.js"></script>
    <script type="text/javascript" src="${jsYuiGallery}/2013-03-13/gallery-busyoverlay/gallery-busyoverlay-min.js"></script>
    <script type="text/javascript" src="${jsYuiGallery}/2013-03-13/gallery-uuid/gallery-uuid-min.js"></script>
    <script type="text/javascript" src="<@s.url value="/" />scripts/common.js"></script>
    <script type="text/javascript" src="<@s.url value="/" />scripts/layout-manager.js"></script>
    ${head}
</head>
    <body class="yui3-skin-sam">
        <div class="layout-hd">
        </div>
        <div class="layout-bd" style="visibility:hidden;overflow: scroll;">
            ${body}
        </div>
        <div class="layout-ft" style="visibility:hidden;">
            <div class="page-footer">
                <table>
                    <tr>
                        <td class="copyright">Copyright &copy; .&nbsp;&nbsp;All rights reserved.</td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
