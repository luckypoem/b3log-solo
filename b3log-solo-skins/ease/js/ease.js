/*
 * Copyright (c) 2009, 2010, 2011, 2012, B3log Team
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
 * @fileoverview neoease js.
 *
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.4, May 18, 2012
 */
var getArticle = function (it, id) {
    var $abstract = $("#abstract" + id),
    $content = $("#content" + id),
    $it = $(it);
    
    if ($content.html() === "") {
        $.ajax({
            url: "/get-article-content?id=" + id,
            type: "GET",
            dataType: "html",
            beforeSend: function () {
                $abstract.css("background",
                    "url(" + latkeConfig.staticServePath + "/skins/neoease/images/ajax-loader.gif) no-repeat scroll center center #fefefe");
            },
            success: function(result, textStatus){
                $it.text(Label.abstractLabel);
                $content.html(result);
                $abstract.hide().css("background", "none");
                $content.fadeIn("slow");
            }
        });
    } else {
        if ($it.text() === Label.contentLabel) {
            $abstract.hide();
            $content.fadeIn();
            $it.text(Label.abstractLabel);
        } else {
            $content.hide();
            $abstract.fadeIn();
            $it.text(Label.contentLabel);
        }
    }
    
    return false;
};

var goTranslate = function () {
    window.open("http://translate.google.com/translate?sl=auto&tl=auto&u=" + location.href);  
};

var getNextPage = function () {
    var $more = $(".article-next");
    currentPage += 1;
    var path = "/articles/";
    if(location.pathname.indexOf("tags") > -1) {
        var tagsPathnaem = location.pathname.split("/tags/");
        var tags = tagsPathnaem[1].split("/");
        path = "/articles/tag/" + tags[0] + "/";
    } else if (location.pathname.indexOf("archives") > -1) {
        var archivesPathnaem = location.pathname.split("/archives/");
        var archives = archivesPathnaem[1].split("/");
        path = "/articles/archives/" + archives[0] + "/" + archives[1] + "/";
    }
    
    $.ajax({
        url: latkeConfig.staticServePath + path + currentPage,
        type: "GET",
        beforeSend: function () {
            $more.css("background",
                "url(" + latkeConfig.staticServePath + "/skins/neoease/images/ajax-loader.gif) no-repeat scroll center center #fefefe");
        },
        success: function(result, textStatus){
            if (textStatus !== "success") {
                return;
            }
            var articlesHTML = "",
            pagination = result.rslts.pagination;
            
            // append articles
            for (var i = 0; i < result.rslts.articles.length; i++) {
                var article = result.rslts.articles[i];
                
                articlesHTML += '<li class="article">' + 
                '<div class="article-title">' +
                '<h2>' +
                '<a class="ft-gray" href="' + latkeConfig.servePath + article.articlePermalink + '">' +
                article.articleTitle + 
                '</a>';
                if (article.hasUpdated) {
                    articlesHTML += '<sup class="tip">' + Label.updatedLabel + '</sup>';
                }
            
                if (article.articlePutTop) {
                    articlesHTML += '<sup class="tip">' + Label.topArticleLabel + '</sup>';
                }
            
                articlesHTML += '</h2>' +
                '<span onclick="getArticle(this, \'' + article.oId + '\');">' + Label.contentLabel + '</span>' +
                '<div class="right">' +
                '<a class="ft-gray" href="' + latkeConfig.servePath + article.articlePermalink + '#comments">' +
                + article.articleCommentCount + '&nbsp;&nbsp;' + Label.commentLabel +
                '</a>&nbsp;&nbsp;' +
                '<a class="ft-gray" href="' + latkeConfig.servePath + article.articlePermalink + '">' +
                article.articleViewCount + '&nbsp;&nbsp;' + Label.viewLabel +
                '</a>' +
                '</div>' +
                '<div class="clear"></div>' +
                '</div>' +
                '<div class="article-body">' +
                '<div id="abstract' + article.oId + '">' +
                article.articleAbstract + 
                '</div>' +
                '<div id="content' + article.oId + '" class="none"></div>' +
                '</div>' +
                '<div class="article-info">' +
                '<div class="right">';
                if (article.hasUpdated) {
                    articlesHTML += Util.toDate(article.articleUpdateDate, 'yy-MM-dd HH:mm');
                } else {
                    articlesHTML +=  Util.toDate(article.articleCreateDate, 'yy-MM-dd HH:mm');
                }
            
                articlesHTML += ' <a href="' + latkeConfig.servePath + '/authors/' + article.authorId + '">' + article.authorName + '</a>' +
                '</div>' +
                '<div class="left">' +
                Label.tag1Label + " ";
        
                var articleTags = article.articleTags.split(",");
                for (var j = 0; j < articleTags.length; j++) {
                    articlesHTML +=  '<a href="' + latkeConfig.servePath + '/tags/' + encodeURIComponent(articleTags[j])  + '">' +
                    articleTags[j] + '</a>';
            
                    if (j < articleTags.length - 1) {
                        articlesHTML += ", ";
                    }
                }
                
                articlesHTML += '</div>' +
            '<div class="clear"></div>' +
            '</div>' +
            '</li>';
            }
        
            $(".body>ul").append(articlesHTML);
            
            // 最后一页处理
            if (pagination.paginationPageCount === currentPage) {
                $more.remove();
            } else {
                $more.css("background", "none");  
            }
        }
    });
};

var ease = {
    $header: $(".header"),
    $banner: $(".header").find(".banner"),
    headerH: $(".header").height(),
    $body: $(".body"),
    $nav: $(".nav"),
    getCurrentPage: function () {
        var $next = $(".article-next");
        if ($next.length > 0) {
            window.currentPage = $next.data("page");
        }
    },
    
    setNavCurrent: function () {
        $(".nav ul a").each(function () {
            var $this = $(this);
            if ($this.attr("href") === latkeConfig.servePath + location.pathname) {
                $this.addClass("current");
            } else if (/\/[0-9]+$/.test(location.pathname)) {
                $(".nav ul li")[0].className = "current";
            }
        });
    },
    
    initCommon: function () {
        Util.init();
        Util.replaceSideEm($(".recent-comments-content"));
        Util.buildTags("tagsSide");
        this.$body.css("paddingTop", this.headerH + "px");
    },
    
    initArchives: function () {
        var $archives = $(".archives");
        if ($archives.length < 1) {
            return;
        }
        
        var years = [],
        $archiveList = $archives.find("span").each(function () {
            var year = $(this).data("year"),
            tag = true;
            for (var i = 0; i < years.length; i++) {
                if (year === years[i]) {
                    tag = false;
                    break;
                }
            }
            if (tag) {
                years.push(year);
            }
        });
        
        var yearsHTML = "";
        for (var j = 0; j < years.length; j++) {
            var monthsHTML = "";
            for (var l = 0; l < $archiveList.length; l++) {
                var $month = $($archiveList[l]);
                if ($month.data("year") === years[j]) {
                    monthsHTML += $month.html();
                }
            }
            yearsHTML += "<div><h3 class='ft-gray'>" + years[j] + "</h3>" + monthsHTML + "</div>";
        }
        
        $archives.html(yearsHTML);
    },
    
    scrollEvent: function () {
        var _it = this;
        $(window).scroll(function () {
            // go top icon show or hide
            var y = $(window).scrollTop();

            if (y > _it.headerH) {
                var bodyH = $(window).height();
                var top =  y + bodyH - 21;
                if ($("body").height() - 58 <= y + bodyH) {
                    top = $(".footer").offset().top - 21; 
                }
                $("#goTop").fadeIn("slow").css("top", top);
            } else {
                $("#goTop").hide();
            }
        
            // header event
            if (y > _it.headerH && _it.$banner.css("display") !== "none") {
                _it.$header.css("top", "0");
                _it.$banner.css("display", "none");
                _it.$body.css("paddingTop", _it.$nav.height() + "px");
            }
        
            if (y < _it.headerH && _it.$banner.css("display") !== "block") {
                _it.$header.css("top", "auto");
                _it.$banner.css("display", "block");
                _it.$body.css("paddingTop", _it.headerH + "px");
            }
        
        // show next page
        });
    }
};
    
(function () {
    ease.getCurrentPage();
    ease.initCommon();
    ease.scrollEvent();
    ease.setNavCurrent();
    ease.initArchives();
})();