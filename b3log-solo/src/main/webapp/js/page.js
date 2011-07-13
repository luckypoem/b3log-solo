/*
 * Copyright (c) 2009, 2010, 2011, B3log Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *  index for admin
 *
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.5, July 13, 2011
 */
var Page = function (tips) {
    this.currentCommentId = "";
    this.tips = tips;
};

$.extend(Page.prototype, {    
    insertEmotions:  function (name) {
        if (name === undefined) {
            name = "";
        }
        
        $("#emotions" + name + " span").click(function () {
            var $comment = $("#comment" + name);
            var endPosition = Util.getCursorEndPosition($comment[0]);
            var key = "[" + this.className + "]",
            textValue  = $comment[0].value;
            textValue = textValue.substring(0, endPosition) + key + textValue.substring(endPosition, textValue.length);
            $("#comment" + name).val(textValue);

            if ($.browser.msie) {
                endPosition -= textValue.split('\n').length - 1;
                var oR = $comment[0].createTextRange();
                oR.collapse(true);
                oR.moveStart('character', endPosition + 6);
                oR.select();
            } else {
                $comment[0].setSelectionRange(endPosition + 6, endPosition + 6);
            }
        });
    },

    validateComment: function (state) {
        var commentName = $("#commentName" + state).val().replace(/(^\s*)|(\s*$)/g, ""),
        commenterContent = $("#comment" + state).val().replace(/(^\s*)|(\s*$)/g, "");
        if (2 > commentName.length || commentName.length > 20) {
            $("#commentErrorTip" + state).html(this.tips.nameTooLongLabel);
            $("#commentName" + state).focus();
        } else if ($("#commentEmail" + state).val().replace(/\s/g, "") === "") {
            $("#commentErrorTip" + state).html(this.tips.mailCannotEmptyLabel);
            $("#commentEmail" + state).focus();
        } else if(!/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test($("#commentEmail" + state).val())) {
            $("#commentErrorTip" + state).html(this.tips.mailInvalidLabel);
            $("#commentEmail" + state).focus();
        } else if (2 > commenterContent.length || commenterContent.length > 500) {
            $("#commentErrorTip" + state).html(this.tips.commentContentCannotEmptyLabel);
            $("#comment" + state).focus();
        } else if ($("#commentValidate" + state).val().replace(/\s/g, "") === "") {
            $("#commentErrorTip" + state).html(this.tips.captchaCannotEmptyLabel);
            $("#commentValidate" + state).focus();
        } else {
            return true;
        }
        return false;
    },
    
    replaceCommentsEm: function (selector) {
        var $commentContents = $(selector);
        for (var i = 0; i < $commentContents.length; i++) {
            var str = $commentContents[i].innerHTML;
            $commentContents[i].innerHTML =  this.replaceEmString(str);;
        }
    },
    
    replaceEmString: function (str) {
        var commentSplited = str.split("[em");
        if (commentSplited.length === 1) {
            return str;
        }
        str = "<span class='em-span'>" + commentSplited[0] + "</span>";
        if ($.trim(commentSplited[0]) === "") {
            str = "";
        }
        for (var j = 1; j < commentSplited.length; j++) {
            var key = commentSplited[j].substr(0, 2);
            str += "<span class='em" + key + "'></span>" + "<span class='em-span'>" +  commentSplited[j].slice(3) + "</span>";
        }
        return str + "<div class='clear'></div>";
    },

    load: function () {
        var that = this;
        // emotions
        this.insertEmotions();
        
        // code high lighter
        SyntaxHighlighter.autoloader(
            'js jscript javascript  /js/lib/SyntaxHighlighter/scripts/shBrushJScript.js',
            'java                   /js/lib/SyntaxHighlighter/scripts/shBrushJava.js',
            'xml                    /js/lib/SyntaxHighlighter/scripts/shBrushXml.js'
            );

        SyntaxHighlighter.config.tagName = "pre";
        SyntaxHighlighter.config.stripBrs = true;
        SyntaxHighlighter.defaults.toolbar = false;
        SyntaxHighlighter.all();

        // submit comment
        $("#commentValidate").keypress(function (event) {
            if (event.keyCode === 13) {
                that.submitComment();
            }
        });

        // cookie
        $("#commentEmail").val(Cookie.readCookie("commentEmail"));
        $("#commentURL").val(Cookie.readCookie("commentURL"));
        $("#commentName").val(Cookie.readCookie("commentName"));
    },

    loadRandomArticles: function () {
        var randomArticles1Label = this.tips.randomArticles1Label;
        // getRandomArticles
        $.ajax({
            url: "/get-random-articles.do",
            type: "POST",
            success: function(result, textStatus){
                var randomArticles = result.randomArticles;
                if (0 === randomArticles.length) {
                    return;
                }

                var listHtml = "";
                for (var i = 0; i < randomArticles.length; i++) {
                    var article = randomArticles[i];
                    var title = article.articleTitle;
                    var randomArticleLiHtml = "<li>" + "<a href='" + article.articlePermalink +"'>" +  title + "</a></li>";
                    listHtml += randomArticleLiHtml;
                }

                var randomArticleListHtml = "<h4>" + randomArticles1Label + "</h4>" + "<ul class='marginLeft12'>" + listHtml + "</ul>";
                $("#randomArticles").append(randomArticleListHtml);
            }
        });
    },
    
    loadExternalRelevantArticles: function (tags) {
        var tips = this.tips;
        $.ajax({
            url: "http://rhythm.b3log.org:80/get-articles-by-tags.do?tags=" + tags
            + "&blogHost=" + tips.blogHost + "&paginationPageSize=" + tips.externalRelevantArticlesDisplayCount,
            type: "GET",
            dataType:"jsonp",
            jsonp: "callback",
            error: function(){
            // alert("Error loading articles from Rhythm");
            },
            success: function(data, textStatus){
                var articles = data.articles;
                if (0 === articles.length) {
                    return;
                }
                var listHtml = "";
                for (var i = 0; i < articles.length; i++) {
                    var article = articles[i];
                    var title = article.articleTitle;
                    var articleLiHtml = "<li>"
                    + "<a target='_blank' href='" + article.articlePermalink + "'>"
                    +  title + "</a></li>"
                    listHtml += articleLiHtml
                }
                
                var randomArticleListHtml = "<h4>" + tips.externalRelevantArticles1Label + "</h4>"
                + "<ul class='marginLeft12'>"
                + listHtml + "</ul>";
                $("#externalRelevantArticles").append(randomArticleListHtml);
            }
        });
    },
    
    submitComment: function (commentId, statue) {
        if (!statue) {
            statue = '';
        }
        var tips = this.tips,
        type = "article";
        if (tips.externalRelevantArticlesDisplayCount === undefined) {
            type = "page";
        }
        if (this.validateComment(statue)) {
            $("#submitCommentButton" + statue).attr("disabled", "disabled");
            $("#commentErrorTip" + statue).html(this.tips.loadingLabel);
            var requestJSONObject = {
                "oId": tips.oId,
                "commentContent": $("#comment" + statue).val().replace(/(^\s*)|(\s*$)/g, ""),
                "commentEmail": $("#commentEmail" + statue).val(),
                "commentURL": "http://" + $("#commentURL" + statue).val().replace(/(^\s*)|(\s*$)/g, ""),
                "commentName": $("#commentName" + statue).val().replace(/(^\s*)|(\s*$)/g, ""),
                "captcha": $("#commentValidate" + statue).val()
            };

            if (statue === "Reply") {
                requestJSONObject.commentOriginalCommentId = commentId;
            }
            $.ajax({
                type: "POST",
                url: "/add-" + type + "-comment.do",
                data: JSON.stringify(requestJSONObject),
                success: function(result){
                    switch (result.sc) {
                        case "COMMENT_" + type.toUpperCase() + "_SUCC":
                            addComment(result, statue);
                            break;
                        case "CAPTCHA_ERROR":
                            $("#commentErrorTip" + statue).html(tips.captchaErrorLabel);
                            $("#captcha" + statue).attr("src", "/captcha.do?code=" + Math.random());
                            $("#commentValidate" + statue).val("").focus();
                            break;
                        default:
                            break;
                    }
                    $("#submitCommentButton" + statue).removeAttr("disabled");
                }
            });

            Cookie.createCookie("commentName", requestJSONObject.commentName, 365);
            Cookie.createCookie("commentEmail", requestJSONObject.commentEmail, 365);
            Cookie.createCookie("commentURL", $("#commentURL" + statue).val().replace(/(^\s*)|(\s*$)/g, ""), 365);
        }
    },

    addReplyForm: function (id, commentFormHTML) {
        var that = this;
        if (id === this.currentCommentId) {
            if (Cookie.readCookie("commentName")  === "") {
                $("#commentNameReply").focus();
            } else {
                $("#commentReply").focus();
            }
            return;
        } else {
            $("#replyForm").remove();
            $("#" + id).append(commentFormHTML  + $("table").html() + "</table>");
            
            // change id, bind event and set value
            $("#replyForm input, #replyForm textarea").each(function () {
                this.id = this.id + "Reply";
            });
            
            $("#commentNameReply").val(Cookie.readCookie("commentName"));
            
            $("#commentEmailReply").val(Cookie.readCookie("commentEmail"));
            
            var $label = $("#commentURLLabel");
            if ($label.length === 1) {
                $label.attr("id", $label.attr("id") + "Reply");
            }
            $("#commentURLReply").val(Cookie.readCookie("commentURL"));
            
            $("#emotions").attr("id", $("#emotions").attr("id") + "Reply");
            this.insertEmotions("Reply");
            
             $("#commentValidateReply").keypress(function (event) {
                if (event.keyCode === 13) {
                    that.submitComment(id, 'Reply');
                }
            });
            $("#captcha").attr("id", $("#captcha").attr("id") + "Reply");
            $("#commentErrorTip").attr("id", $("#commentErrorTip").attr("id") + "Reply").html("");
            
            $("#submitCommentButton").attr("id", $("#submitCommentButtonReply").attr("id") + "Reply").
            removeAttr("onclick").click(function () {
                that.submitComment(id, 'Reply');
            });
            
            if (Cookie.readCookie("commentName")  === "") {
                $("#commentNameReply").focus();
            } else {
                $("#commentReply").focus();
            }
        }
        this.currentCommentId = id;
    },

    hideComment: function (id) {
        $("#commentRef" + id).hide();
    },

    addCommentAjax: function (commentHTML, state) {
        if ($("#comments>div").first().length === 1) {
            $("#comments>div").first().before(commentHTML);
        } else {
            $("#comments").html(commentHTML);
        }

        if (state === "") {
            $("#commentErrorTip").html("");
            $("#comment").val("");
            $("#commentValidate").val("");
            $("#captcha").attr("src", "/captcha.do?code=" + Math.random());
        } else {
            $("#replyForm").remove();
        }
    }
});

var Cookie = {
    readCookie: function (name) {
        var nameEQ = name + "=";
        var ca = document.cookie.split(';');
        for(var i=0;i < ca.length;i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1,c.length);
            if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
        }
        return "";
    },

    eraseCookie: function (name) {
        this.createCookie(name,"",-1);
    },

    createCookie: function (name,value,days) {
        var expires = "";
        if (days) {
            var date = new Date();
            date.setTime(date.getTime()+(days*24*60*60*1000));
            expires = "; expires="+date.toGMTString();
        }
        document.cookie = name+"="+value+expires+"; path=/";
    }
};
