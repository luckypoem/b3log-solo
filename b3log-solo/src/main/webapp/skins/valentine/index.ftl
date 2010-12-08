<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${blogTitle}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="keywords" content="${metaKeywords}"/>
        <meta name="description" content="<#list articles as article>${article.articleTitle}<#if article_has_next>,</#if></#list>"/>
        <meta name="author" content="B3log Team"/>
        <meta name="generator" content="B3log"/>
        <meta name="copyright" content="B3log"/>
        <meta name="revised" content="B3log, 2010"/>
        <meta http-equiv="Window-target" content="_top"/>
        <link type="text/css" rel="stylesheet" href="/skins/${skinDirName}/default-index.css"/>
        <link href="blog-articles-feed.do" title="ATOM" type="application/atom+xml" rel="alternate" />
        <link rel="icon" type="image/png" href="/favicon.png"/>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.3/jquery.min.js"></script>
        <script type="text/javascript" src="/js/lib/jsonrpc.min.js"></script>
        ${htmlHead}
    </head>
    <body>
        <div id="wrapper-home">
            <div id="header-home"><!-- header --></div>
            <div id="content-home">
                <div id="home-rinside">
                    <!-- The Loop -->
                    <#include "individual-blue.ftl">
                    <!-- End Loop-->
                </div>
                <div id="home-sidebar">
                    <div id="home-tag"><!--home tag --></div>
                    <#include "sidebar.ftl">
                </div>

                <div id="home-wendyside">
                    <!-- The Loop -->
                    <#include "individual-pink.ftl">
                    <!-- End Loop-->
                </div>
            </div>
            <#include "prefooter-home.ftl">
            <#include "footer.ftl">
        </div>
    </body>
</html>