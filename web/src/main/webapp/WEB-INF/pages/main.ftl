<#-- @ftlvariable name="pageVal" type="randomnaja.orgfeed.action.MainAction.PageVariable" -->
<!DOCTYPE html PUBLIC
        "-//W3C//DTD XHTML 1.1 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Main</title>

    <script type="text/javascript">
        APP.namespace('actions', {

        });
    </script>
    <script type="text/javascript">
        (function() {
            APP.namespace('request.parameters', {});
            document.on('layout:init', function(event) {
            });// end layout:init
        })();
    </script>
</head>

<body>
<div class="layout-module-col">
    <div class="criteria layout-module">
        <div class="layout-m-hd"></div>
        <div class="layout-m-bd" style="overflow: scroll">
            All of my pages
            <#list pageVal.pages as page>
                <a href="renderPageId.html?param.pageId=${page.facebookId?html}">
                    ${page.name!?html}
                </a>
            <#else>
                No page
            </#list>
        </div>
        <div class="layout-m-ft"></div>
    </div>
</div>

</body>

</html>

