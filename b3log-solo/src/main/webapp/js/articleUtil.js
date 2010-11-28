/*
 * Copyright (c) 2009, 2010, B3log Team
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

var ArticleUtil = function () {
    this.currentCommentId = "";
};

$.extend(ArticleUtil.prototype, {
    commentUtil: {
        version:"0.0.0.1",
        author: "lly219@gmail.com"
    },
    
    insertEmotions:  function (name) {
        if (name === undefined) {
            name = "";
        }
        
        $("#emotions" + name + " img").click(function () {
            // TODO: should be insert it at the after of cursor
            var key = this.className;
            $("#comment" + name).val($("#comment" + name).val() + key).focus();
        });
    },

    validateComment: function (state, tip) {
        if (state === undefined) {
            state = '';
        }
        var commentName = $("#commentName" + state).val().replace(/(^\s*)|(\s*$)/g, ""),
        commenterContent = $("#comment" + state).val().replace(/(^\s*)|(\s*$)/g, "");
        if (2 > commentName.length || commentName.length > 20) {
            $("#commentErrorTip" + state).html(ArticleUtil.tip.nameTooLong);
            $("#commentName" + state).focus();
        } else if ($("#commentEmail" + state).val().replace(/\s/g, "") === "") {
            $("#commentErrorTip" + state).html(ArticleUtil.tip.mailCannotEmpty);
            $("#commentEmail" + state).focus();
        } else if(!/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test($("#commentEmail" + state).val())) {
            $("#commentErrorTip" + state).html(ArticleUtil.tip.mailInvalid);
            $("#commentEmail" + state).focus();
        } else if (2 > commenterContent.length || commenterContent.length > 500) {
            $("#commentErrorTip" + state).html(ArticleUtil.tip.commentContentCannotEmpty);
            $("#comment" + state).focus();
        } else if ($("#commentValidate" + state).val().replace(/\s/g, "") === "") {
            $("#commentErrorTip" + state).html(ArticleUtil.tip.captchaCannotEmpty);
            $("#commentValidate" + state).focus();
        } else {
            return true;
        }
        return false;
    },
    
    hideComment: function (id) {
        $("#commentItemRef" + id).hide();
    },

    addCommentAjax: function (commentHTML, state) {
        if ($("#comments .comments-header").length > 0) {
            $("#comments .comments-header").after(commentHTML);
        } else if ($("#comments>div").first().length === 1) {
            $("#comments>div").first().before(commentHTML);
        } else {
            $("#comments").first().before(commentHTML);
        }

        if (state === "") {
            $("#commentErrorTip").html("");
            $("#comment").val("");
            $("#commentEmail").val("");
            $("#commentURL").val("");
            $("#commentName").val("");
            $("#commentValidate").val("");
            $("#captcha").attr("src", "/captcha.do?code=" + Math.random());
        } else {
            $("#replyForm").remove();
        }
    },

    replaceEmotions: function (commentContentHTML, skin) {
        var commentContents = commentContentHTML.split("[em");
        commentContentHTML = commentContents[0];
        for (var j = 1; j < commentContents.length; j++) {
            var key = commentContents[j].substr(0, 2),
            emImgHTML = "<img src='/skins/" + skin + "/emotions/em" + key
            + ".png'/>";
            commentContentHTML += emImgHTML + commentContents[j].slice(3);
        }
        return commentContentHTML;
    },
    
    getDate: function (time,type) {
        var c = new Date(time);
        var d=c.getFullYear(),month=c.getMonth()+1,day=c.getDate(),hours=c.getHours(),seconds=c.getSeconds(),minutes=c.getMinutes();
        if(month<10){
            month="0"+month.toString()
        }
        if(day<10){
            day="0"+day.toString()
        }
        if(hours<10){
            hours="0"+hours.toString()
        }
        if(minutes<10){
            minutes="0"+minutes.toString()
        }
        if(seconds<10){
            seconds="0"+seconds.toString()
        }
        switch(type){
            case undefined:
                return d + "-" + month + "-" + day;
                break;
            case "yyyy-mm-dd hh:mm:ss":
                return d + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
                break;
            default:
                return false;
                break
        }
    },

    load: function () {
        // code high lighter
        SyntaxHighlighter.autoloader(
            'js jscript javascript  /js/lib/SyntaxHighlighter/scripts/shBrushJScript.js',
            'java                   /js/lib/SyntaxHighlighter/scripts/shBrushJava.js',
            'xml                    /js/lib/SyntaxHighlighter/scripts/shBrushXml.js'
            );

        SyntaxHighlighter.config.tagName = "pre";
        SyntaxHighlighter.config.stripBrs = true;
        SyntaxHighlighter.defaults['toolbar'] = false;
        SyntaxHighlighter.all();

        // submit comment
        $("#commentValidate").keypress(function (event) {
            if (event.keyCode === 13) {
                submitComment();
            }
        });
    },

    loadRandomArticles: function () {
        // getRandomArticles
        jsonRpc.articleService.getRandomArticles(function (result, error) {
            if (result && !error) {
                var randomArticles = result.list;
                if (0 === randomArticles.length) {
                    return;
                }

                var listHtml = "";
                for (var i = 0; i < randomArticles.length; i++) {
                    var article = randomArticles[i];
                    var title = article.articleTitle;
                    var randomArticleLiHtml = "<li>"
                    + "<a href='" + article.articlePermalink +"'>"
                    +  title + "</a></li>"
                    listHtml += randomArticleLiHtml
                }

                var randomArticlesDiv = $("#randomArticles");
                randomArticlesDiv.attr("class", "article-relative");
                var randomArticleListHtml = "<h5>" + ArticleUtil.tip.randomArticles + "</h5>"
                + "<ul class='marginLeft12'>"
                + listHtml + "</ul>";
                randomArticlesDiv.append(randomArticleListHtml);
            }
        });
    },

    loadTool: function () {
        // article view count
        jsonRpc.statisticService.incArticleViewCount(function (result, error) {}, "${article.oId}");

        // Stack initialize
        var openspeed = 300;
        var closespeed = 300;
        $('.stack>img').toggle(function(){
            var vertical = 0;
            var horizontal = 0;
            var $el=$(this);
            $el.next().children().each(function(){
                $(this).animate({
                    top: '-' + vertical + 'px',
                    left: horizontal + 'px'
                }, openspeed);
                vertical = vertical + 36;
                horizontal = (horizontal+.42)*2;
            });
            $el.next().animate({
                top: '-21px',
                left: '-6px'
            }, openspeed).addClass('openStack')
            .find('li a>img').animate({
                width: '28px',
                marginLeft: '9px'
            }, openspeed);
            $el.animate({
                paddingTop: '0'
            });
        }, function(){
            //reverse above
            var $el=$(this);
            $el.next().removeClass('openStack').children('li').animate({
                top: '32px',
                left: '6px'
            }, closespeed);
            $el.next().find('li a>img').animate({
                width: '32px',
                marginLeft: '0'
            }, closespeed);
            $el.animate({
                paddingTop: '9px'
            });
        });

        // Stacks additional animation
        $('.stack li a').hover(function(){
            $("img",this).animate({
                width: '32px'
            }, 100);
            $("span",this).animate({
                marginRight: '12px'
            });
        },function(){
            $("img",this).animate({
                width: '28px'
            }, 100);
            $("span",this).animate({
                marginRight: '0'
            });
        });
    }
});

ArticleUtil = new ArticleUtil();
