<#include "macro-head.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${blogTitle}">
        <meta name="keywords" content="${metaKeywords}"/>
        <meta name="description" content="<#list articles as article>${article.articleTitle}<#if article_has_next>,</#if></#list>"/>
        </@head>
    </head>
    <body>
        <div class="wrapper">
            <div class="bg-bottom">
                <#include "top-nav.ftl">
                <div class="content">
                    <div class="header">
                        <#include "header.ftl">
                    </div>
                    <div class="body">
                        <div class="left main">
                            <#include "article-list.ftl">
                        </div>
                        <div class="left side">
                            <#include "side.ftl">
                        </div>
                        <div class="clear"></div>
                    </div>
                    <div class="footer">
                        <#include "footer.ftl">
                    </div>
                </div>
            </div>
        </div>
        <div class='goTopIcon' onclick='util.goTop();'></div>
        <div class='goBottomIcon' onclick='util.goBottom();'></div>
    </body>
</html>
