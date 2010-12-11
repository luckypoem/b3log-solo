<div class="header-user">
    <div id="statistic">
        <span>${viewCount1Label}
            <span class='error-msg'>
                ${statistic.statisticBlogViewCount}
            </span>
            &nbsp;&nbsp;
        </span>
        <span>
            ${articleCount1Label}
            <span class='error-msg'>
                ${statistic.statisticPublishedBlogArticleCount}
            </span>
            &nbsp;&nbsp;
        </span>
        <span>
            ${commentCount1Label}
            <span class='error-msg'>
                ${statistic.statisticPublishedBlogCommentCount}
            </span>
        </span>
    </div>
</div>
<div class="header-navi">
    <div class="header-navi-main">
        <div class="left">
            <a href="/" class="header-title">
                ${blogTitle}
            </a>
            <span class="sub-title">${blogSubtitle}</span>
        </div>
        <div class="right">
            <ul class="tabs">
                <li class="tab">
                    <a href="/">${homeLabel}</a>
                </li>
                <li class="tab">
                    <a href="/tags.html">${allTagsLabel}</a>
                </li>
                <li class="tab" id="header-pages">
                    <a>
                        <span class="left">
                            XXXXXX
                        </span>
                        <span class="arrow-dowm-icon"></span>
                    </a>
                    <ul class="sub-tabs">
                        <#list pageNavigations as page>
                        <li class="sub-tab">
                            <a href="${page.pagePermalink}">${page.pageTitle}</a>
                        </li>
                        </#list>
                    </ul>
                </li>
                <li class="tab">
                    <a class="left" href="/blog-articles-feed.do">
                    你好
                        <img src="/images/feed.png" alt="Atom"/>
                    </a>
                </li>
            </ul>
        </div>
        <div class="clear"></div>
    </div>
</div>
<script type="text/javascript">
    var replaceCommentsEm = function (selector) {
        var $commentContents = $(selector);
        for (var i = 0; i < $commentContents.length; i++) {
            var str = $commentContents[i].innerHTML;
            var ems = str.split("[em");
            var content = ems[0];
            for (var j = 1; j < ems.length; j++) {
                var key = ems[j].substr(0, 2),
                emImgHTML = "<img src='/skins/classic/emotions/em" + key
                    + ".png'/>";
                content += emImgHTML + ems[j].slice(3);
            }
            $commentContents[i].innerHTML = content;
        }
    }
</script>
