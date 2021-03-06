<#include "macro-head.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${tag.tagTitle} - ${blogTitle}">
        <meta name="keywords" content="${metaKeywords},${tag.tagTitle}"/>
        <meta name="description" content="<#list articles as article>${article.articleTitle}<#if article_has_next>,</#if></#list>"/>
        </@head>
    </head>
    <body>
        ${topBarReplacement}
        <#include "header.ftl">
        <div class="body">
            <h2>
                <a rel="alternate" href="${servePath}/tag-articles-feed.do?oId=${tag.oId}">
                    ${tag1Label}
                    ${tag.tagTitle}
                    (${tag.tagPublishedRefCount})
                </a>
            </h2>
            <#include "article-list.ftl">
        </div>
        <#include "footer.ftl">
    </body>
</html>
