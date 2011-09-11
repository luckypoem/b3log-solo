<#macro comments commentList permalink>
<div class="share">
    <a class="share-comment" href="#commentForm">
        ${commentList?size}&nbsp;&nbsp;${commentLabel}
    </a>
    <span class="clear"></span>
</div>
<div id="comments">
    <#list commentList as comment>
    <div id="${comment.oId}" class="<#if comment_index % 2 == 0>comment-even<#else>comment-odd</#if>">
        <img class="comment-header" title="${comment.commentName}"
             alt="${comment.commentName}" src="${comment.commentThumbnailURL}"/>
        <div class="comment-panel">
            <#if "http://" == comment.commentURL>
            ${comment.commentName}
            <#else>
            <a href="${comment.commentURL}" target="_blank">${comment.commentName}</a>
            </#if>
            <#if comment.isReply>&nbsp;@
            <a href="${permalink}#${comment.commentOriginalCommentId}"
               onmouseover="showComment(this, '${comment.commentOriginalCommentId}');"
               onmouseout="page.hideComment('${comment.commentOriginalCommentId}')">${comment.commentOriginalCommentName}</a>
            </#if>
            <div class="right">
                <a href="javascript:replyTo('${comment.oId}');">${replyLabel}</a>
                &nbsp;|&nbsp;
                ${comment.commentDate?string("yyyy-MM-dd HH:mm:ss")}
            </div>
            <span class="clear"></span>
            <div class="article-body">${comment.commentContent}</div>
        </div>
        <span class="clear"></span>
    </div>
    </#list>
</div>
<div class="form">
    <h4>${postCommentsLabel}</h4>
    <table id="commentForm">
        <tbody>
            <tr>
                <td colspan="2">
                    <input type="text" class="normalInput" id="commentName"/>
                    <label for="commentName">${commentNameLabel}</label>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="text" class="normalInput" id="commentEmail"/>
                    <label for="commentEmail">${commentEmailLabel}</label>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="text" id="commentURL"/>
                    <label for="commentURL">${commentURLLabel}</label>
                </td>
            </tr>
            <tr>
                <td id="emotions" colspan="2">
                    <span class="em00" title="${em00Label}"></span>
                    <span class="em01" title="${em01Label}"></span>
                    <span class="em02" title="${em02Label}"></span>
                    <span class="em03" title="${em03Label}"></span>
                    <span class="em04" title="${em04Label}"></span>
                    <span class="em05" title="${em05Label}"></span>
                    <span class="em06" title="${em06Label}"></span>
                    <span class="em07" title="${em07Label}"></span>
                    <span class="em08" title="${em08Label}"></span>
                    <span class="em09" title="${em09Label}"></span>
                    <span class="em10" title="${em10Label}"></span>
                    <span class="em11" title="${em11Label}"></span>
                    <span class="em12" title="${em12Label}"></span>
                    <span class="em13" title="${em13Label}"></span>
                    <span class="em14" title="${em14Label}"></span>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <textarea rows="10" cols="96" id="comment"></textarea>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="text" class="normalInput" id="commentValidate"/>
                    <img id="captcha" alt="validate" src="/captcha.do"></img>
                </td>
                <th>
                    <span class="tip" id="commentErrorTip"/>
                </th>
            </tr>
            <tr>
                <td colspan="2" align="right">
                    <button id="submitCommentButton" onclick="page.submitComment();">${submmitCommentLabel}</button>
                </td>
            </tr>
        </tbody>
    </table>
</div>
</#macro>

<#macro comment_script oId>
<script type="text/javascript" src="/js/page${miniPostfix}.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/lib/SyntaxHighlighter/scripts/shCore.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/lib/SyntaxHighlighter/scripts/shAutoloader.js" charset="utf-8"></script>
<script type="text/javascript">
    var page = new Page({
        "nameTooLongLabel": "${nameTooLongLabel}",
        "mailCannotEmptyLabel": "${mailCannotEmptyLabel}",
        "mailInvalidLabel": "${mailInvalidLabel}",
        "commentContentCannotEmptyLabel": "${commentContentCannotEmptyLabel}",
        "captchaCannotEmptyLabel": "${captchaCannotEmptyLabel}",
        "captchaErrorLabel": "${captchaErrorLabel}",
        "loadingLabel": "${loadingLabel}",
        "oId": "${oId}",
        "skinDirName": "${skinDirName}",
        "blogHost": "${blogHost}",
        "randomArticles1Label": "${randomArticlesLabel}",
        "externalRelevantArticles1Label": "${externalRelevantArticlesLabel}"
    });

    var addComment = function (result, state) {
        var oddEven = "";
        if ($("#comments>div").first().hasClass("comment-even")) {
            oddEven = "comment-odd";
        } else {
            oddEven = "comment-even";
        }
        var commentHTML = '<div id="' + result.oId + '" class="oddEven"><img class="comment-header" \
            title="' + $("#commentName" + state).val() + '" alt="' + $("#commentName" + state).val() + 
            '" src="' + result.commentThumbnailURL + '"/><div class="comment-panel">';

        if ($("#commentURL" + state).val().replace(/\s/g, "") === "") {
            commentHTML += $("#commentName" + state).val();
        } else {
            commentHTML += '<a href="http://' + $("#commentURL" + state).val() + 
                '" target="_blank">' + $("#commentName" + state).val() + '</a>';
        }

        if (state !== "") {
            var commentOriginalCommentName = $("#" + page.currentCommentId).find(".comment-panel>a").text();
            commentHTML += '&nbsp;@&nbsp;<a href="' + result.commentSharpURL.split("#")[0] + '#' + page.currentCommentId + '"'
                + 'onmouseover="showComment(this, \'' + page.currentCommentId + '\');"'
                + 'onmouseout="page.hideComment(\'' + page.currentCommentId + '\')">' + commentOriginalCommentName + '</a>';
        }
            
        commentHTML += '<div class="right"><a href="javascript:replyTo(\'' + result.oId 
            + '\');">${replyLabel}</a>' + ' &nbsp;|&nbsp;' +  result.commentDate
            + '</div><span class="clear"></span><div class="article-body">' + 
            Util.replaceEmString($("#comment" + state).val().replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\n/g,"<br/>"))
            + '</div></div><span class="clear"></span></div>';

        page.addCommentAjax(commentHTML, state);
    }

    var replyTo = function (id) {
        var commentFormHTML = "<table class='form comment-reply' id='replyForm'>";
        page.addReplyForm(id, commentFormHTML);
        $("#replyForm label").each(function () {
            $this = $(this);
            $this.attr("for", $this.attr("for") + "Reply");
        });
    }
            
    var showComment = function (it, id) {
        if ( $("#commentRef" + id).length > 0) {
            $("#commentRef" + id).show();
        } else {
            var $refComment = $("#" + id).clone();
            $refComment.removeClass().addClass("comment-body-ref").attr("id", "commentRef" + id).append("<span class='arrow'></span>");
            $refComment.find(".comment-panel .none").remove();
            $("#comments").append($refComment);
        }
        $("#commentRef" + id).css("top", ($(it).position().top + 20) + "px");
    };

    (function () {
        page.load();
        // emotions
        page.replaceCommentsEm("#comments .article-body");
            <#nested>
        })();
</script>
</#macro>