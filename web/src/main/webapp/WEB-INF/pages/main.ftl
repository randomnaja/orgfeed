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

<div id="all-pages-container">
    <label>All of pages</label>
    <#list pageVal.pages as page>
        <div class="each-page-container">
            <a href="renderPageId.html?param.pageId=${page.facebookId?html}">
                ${page.name!?html}
            </a>
        </div>
    <#else>
        No page
    </#list>
</div>

</body>

</html>

