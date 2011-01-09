<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${article.articleTitle} - ${blogTitle}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="keywords" content="<#list article.articleTags?split(",") as articleTag>${articleTag}<#if articleTag_has_next>,</#if></#list>"/>
        <meta name="description" content="${article.articleAbstract}"/>
        <meta name="author" content="B3log Team"/>
        <meta name="generator" content="B3log"/>
        <meta name="copyright" content="B3log"/>
        <meta name="revised" content="B3log,${article.articleCreateDate?string('yyyy-MM-dd HH:mm:ss')}"/>
        <meta http-equiv="Window-target" content="_top"/>
        <!-- Stylesheets -->
        <link rel="stylesheet" href="/skins/${skinDirName}/style.css" type="text/css" media="screen" />
        <link rel="stylesheet" href="/skins/${skinDirName}/jquery.lightbox-0.5.css" type="text/css" media="screen" />
        <link rel='stylesheet' id='/skins/${skinDirName}/wp-pagenavi-css'  href='pagenavi-css.css' type='text/css' media='all' />
        <link rel="stylesheet" href="/skins/${skinDirName}/dd-formmailer.css" type="text/css" media="screen" />
        <style type="text/css">
            #aktt_tweet_form {
                margin: 0;
                padding: 5px 0;
            }
            #aktt_tweet_form fieldset {
                border: 0;
            }
            #aktt_tweet_form fieldset #aktt_tweet_submit {
                float: right;
                margin-right: 10px;
            }
            #aktt_tweet_form fieldset #aktt_char_count {
                color: #666;
            }
            #aktt_tweet_posted_msg {
                background: #ffc;
                display: none;
                margin: 0 0 5px 0;
                padding: 5px;
            }
            #aktt_tweet_form div.clear {
                clear: both;
                float: none;
            }
        </style>
        <link rel="stylesheet" href="/skins/${skinDirName}/wp-syntax.css" type="text/css" media="screen" />
        <style type="text/css">.broken_link, a.broken_link {text-decoration: line-through}</style>
        <link rel="stylesheet" href="/skins/${skinDirName}/print.css" type="text/css" media="print" />
        <link type="text/css" rel="stylesheet" href="/js/lib/SyntaxHighlighter/styles/shCoreEclipse.css"/>
        <link type="text/css" rel="stylesheet" href="/js/lib/SyntaxHighlighter/styles/shThemeEclipse.css"/>
        <link type="text/css" rel="stylesheet" href="/styles/default-base.css"/>
        <link type="text/css" rel="stylesheet" href="/skins/${skinDirName}/default-index.css"/>
        <link href="/blog-articles-feed.do" title="ATOM" type="application/atom+xml" rel="alternate" />
        <link rel="icon" type="image/png" href="/favicon.png"/>
        ${htmlHead}
    </head>
    <body class="home blog">
        <#include "common-top.ftl">
        <div id="wrapper">
            <div id="header">
                <#include "article-header.ftl">
            </div>
            <div id="content">                
                <div class="main">
                    <#include "article-detail-main.ftl">
                </div>
                <div id="sidebar">
                    <#include "article-side.ftl">
                </div>
                <div class="clear"></div>
            </div>
            <div class="footer">
                <#include "article-footer.ftl">
            </div>
            <div class="stack addthis_toolbox">
                <img src="/images/stack.png" alt="stack"/>
                <ul id="stack" class="custom_images">
                    <li><a class="addthis_button_googlebuzz"><span>Buzz</span><img src="/images/buzz.png" alt="Share to Buzz" /></a></li>
                    <li><a class="addthis_button_twitter"><span>Twitter</span><img src="/images/twitter.png" alt="Share to Twitter" /></a></li>
                    <li><a class="addthis_button_delicious"><span>Delicious</span><img src="/images/delicious.png" alt="Share to Delicious" /></a></li>
                    <li><a class="addthis_button_facebook"><span>Facebook</span><img src="/images/facebook.png" alt="Share to Facebook" /></a></li>
                    <li><a class="addthis_button_more"><span>More...</span><img src="/images/addthis.png" alt="More..." /></a></li>
                </ul>
            </div>
        </div> 
        <script type="text/javascript" src="/skins/${skinDirName}/js/enMonth.js"></script>
        <script type="text/javascript" src="/js/articleUtil.js"></script>
        <script type="text/javascript" src="/js/lib/SyntaxHighlighter/scripts/shCore.js"></script>
        <script type="text/javascript" src="/js/lib/SyntaxHighlighter/scripts/shAutoloader.js"></script>
        <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js"></script>
        <script type="text/javascript">
            var articleUtil = new ArticleUtil({
                "nameTooLongLabel": "${nameTooLongLabel}",
                "mailCannotEmptyLabel": "${mailCannotEmptyLabel}",
                "mailInvalidLabel": "${mailInvalidLabel}",
                "commentContentCannotEmptyLabel": "${commentContentCannotEmptyLabel}",
                "captchaCannotEmptyLabel": "${captchaCannotEmptyLabel}",
                "randomArticles1Label": "${randomArticles1Label}",
                "captchaErrorLabel": "${captchaErrorLabel}",
                "loadingLabel": "${loadingLabel}",
                "oId": "${article.oId}",
                "blogHost": "${blogHost}",
                "externalRelevantArticlesDisplayCount": "${externalRelevantArticlesDisplayCount}",
                "externalRelevantArticles1Label": "${externalRelevantArticles1Label}"
            });

            var addComment = function (result, state) {
                var commentHTML = '<div id="commentItem' + result.oId + '"><div class="comment-panel"><div class="comment-title">';

                if ($("#commentURL" + state).val().replace(/\s/g, "") === "") {
                    commentHTML += '<a name="' + result.oId + '" class="left">' + $("#commentName" + state).val() + '</a>';
                } else {
                    commentHTML += '<a href="http://' + $("#commentURL" + state).val() + '" target="_blank" name="'
                        + result.oId + '" class="left">' + $("#commentName" + state).val() + '</a>';
                }

                if (state !== "") {
                    var commentOriginalCommentName = $("#commentItem" + articleUtil.currentCommentId).find(".comment-title a").first().text();
                    commentHTML += '&nbsp;@&nbsp;<a href="' + result.commentSharpURL.split("#")[0] + '#' + articleUtil.currentCommentId + '"'
                        + 'onmouseover="showComment(this, \'' + articleUtil.currentCommentId + '\');"'
                        + 'onmouseout="articleUtil.hideComment(\'' + articleUtil.currentCommentId + '\')">' + commentOriginalCommentName + '</a>';
                }

                commentHTML += '<div class="right">' + result.commentDate
                    + '&nbsp;<a class="noUnderline" href="javascript:replyTo(\'' + result.oId + '\');">${replyLabel}</a>'
                    + '</div><div class="clear"></div></div><div class="comment-body">'
                    + '<div class="left comment-picture"><img alt="' + $("#commentName" + state).val()
                    + '" src="' + result.commentThumbnailURL + '"/>'
                    + '</div><div class="comment-content">' + articleUtil.replaceEmotions($("#comment" + state).val().replace(/\n/g,"<br/>").replace(/</g, "&lt;").replace(/>/g, "&gt;"), "madeincima") + '</div><div class="clear"></div>'
                    + '</div></div></div>';

                articleUtil.addCommentAjax(commentHTML, state);
            }

            var replyTo = function (id) {
                var commentFormHTML = "<table class='form comment-reply' id='replyForm'><tbody><tr><th>${commentName1Label}"
                    + "</th><td colspan='2'><input class='normalInput' id='commentNameReply' value='" + Cookie.readCookie("commentName") + "'/>"
                    + "</td></tr><tr><th>${commentEmail1Label}</th><td colspan='2'>"
                    + "<input class='normalInput' id='commentEmailReply' value='" + Cookie.readCookie("commentEmail") + "'/></td></tr><tr>"
                    + "<th>${commentURL1Label}</th><td colspan='2'><div id='commentURLLabelReply'>"
                    + "http://</div><input id='commentURLReply' value='" + Cookie.readCookie("commentURL") + "'/>"
                    + "</td></tr><tr><th>${commentEmotions1Label}</th><td id='emotionsReply'>" + $("#emotions").html()
                    + "</td></tr><tr><th valign='top'>${commentContent1Label}</th><td colspan='2'>"
                    + "<textarea rows='10' cols='96' id='commentReply'></textarea></td></tr><tr>"
                    + "<th>${captcha1Label}</th><td><input class='normalInput' id='commentValidateReply'/>"
                    + "<img id='captchaReply' alt='validate' src='/captcha.do?" + new Date().getTime() + "'></img></td><th>"
                    + "<span class='error-msg' id='commentErrorTipReply'/>"
                    + "</th></tr><tr><td colspan='3' align='right'>"
                    + "<button onclick=\"articleUtil.submitComment('" + id + "', 'Reply');\">${submmitCommentLabel}</button>"
                    + "</td></tr></tbody></table>";

                articleUtil.addReplyForm(id, commentFormHTML);
                $("#commentURLReply").focus(function (event) {
                    if ($.browser.version !== "7.0") {
                        $("#commentURLLabelReply").css({"border":"2px solid #73A6FF","border-right":"0px"});
                    }
                }).blur(function () {
                    $("#commentURLLabelReply").css({"border":"2px inset #CCCCCC","border-right":"0px"});
                }).width($("#commentReply").width() - $("#commentURLLabelReply").width());
            }

            var showComment = function (it, id) {
                if ( $("#commentItemRef" + id).length > 0) {
                    $("#commentItemRef" + id).show();
                } else {
                    var $refComment = $("#commentItem" + id + " .comment-panel").clone();
                    $refComment.removeClass().addClass("comment-body-ref").attr("id", "commentItemRef" + id);
                    $refComment.find(".comment-title .right a").remove();
                    $("#comments").append($refComment);
                }
                var position =  $(it).position();
                $("#commentItemRef" + id).css({
                    "top": (position.top + 23) + "px",
                    "left": "88px"
                });
            }

            var loadAction = function () {
                // comment url
                $("#commentURL").focus(function (event) {
                    if ($.browser.version !== "7.0") {
                        $("#commentURLLabel").css({"border":"2px solid #73A6FF","border-right":"0px"});
                    }
                }).blur(function () {
                    $("#commentURLLabel").css({"border":"2px inset #CCCCCC","border-right":"0px"});
                }).width($("#comment").width() - $("#commentURLLabel").width());

                util.replaceCommentsEm("#comments .comment-content", "madeincima");

                articleUtil.load();
                articleUtil.loadRandomArticles();

                // externalRelevantArticles
                    <#if 0 != externalRelevantArticlesDisplayCount>
                    articleUtil.loadExternalRelevantArticles("<#list article.articleTags?split(",") as articleTag>${articleTag}<#if articleTag_has_next>,</#if></#list>");
                    </#if>
                    articleUtil.loadTool();

            }
            loadAction();
        </script>
    </body>
</html>