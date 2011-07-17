<div id="sideNavi">
    <div id="statistic">
        <div>
            ${viewCount1Label}
            <span class='error-msg'>
                ${statistic.statisticBlogViewCount}
            </span>
        </div>
        <div>
            ${articleCount1Label}
            <span class='error-msg'>
                ${statistic.statisticPublishedBlogArticleCount}
            </span>
        </div>
        <div>
            ${commentCount1Label}
            <span class='error-msg'>
                ${statistic.statisticPublishedBlogCommentCount}
            </span>
        </div>
    </div>
    <div class="block notice">
        <h3>${noticeBoardLabel}</h3>
        <ul>
            <li>${noticeBoard}</li>
        </ul>
    </div>
    <div class="line"></div>
    <div class="block">
        <h3 id="recentCommentsLabel">${recentCommentsLabel}</h3>
        <ul id="recentComments">
            <#list recentComments as comment>
            <li>
                <a href="${comment.commentSharpURL}" title="${comment.commentContent}">
                    ${comment.commentName}: ${comment.commentContent}
                </a>
            </li>
            </#list>
        </ul>
        <div class='clear'></div>
    </div>
    <div class="line"></div>
    <div class="block mostCommentArticles">
        <h3>${mostCommentArticlesLabel}</h3>
        <ul id="mostCommentArticles">
            <#list mostCommentArticles as article>
            <li>
                <a title="${article.articleTitle}" href="${article.articlePermalink}">
                    <sup>[${article.articleCommentCount}]</sup>${article.articleTitle}
                </a>
            </li>
            </#list>
        </ul>
        <div class='clear'></div>
    </div>
    <div class="line"></div>
    <div class="block mostViewCountArticles">
        <h3>${mostViewCountArticlesLabel}</h3>
        <ul id="mostViewCountArticles">
            <#list mostViewCountArticles as article>
            <li>
                <a title="${article.articleTitle}" href="${article.articlePermalink}">
                    <sup>[${article.articleViewCount}]</sup>${article.articleTitle}
                </a>
            </li>
            </#list>
        </ul>
        <div class='clear'></div>
    </div>
    <div class="line"></div>
    <div class="block popTags">
        <h3>${popTagsLabel}</h3>
        <ul>
            <#list mostUsedTags as tag>
            <li>
                <a title="${tag.tagTitle}(${tag.tagPublishedRefCount})" href="/tags/${tag.tagTitle?url('UTF-8')}">
                    ${tag.tagTitle}(${tag.tagPublishedRefCount})
                </a>
                <img onclick="window.location='/tag-articles-feed.do?oId=${tag.oId}'"
                     alt="${tag.tagTitle}" src="/images/feed.png"/>
            </li>
            </#list>
        </ul>
        <div class='clear'></div>
    </div>
    <div class="line"></div>
    <div class="block">
        <h3>${linkLabel}</h3>
        <ul id="sideLink">
            <#list links as link>
            <li>
                <a href="${link.linkAddress}" title="${link.linkTitle}" target="_blank">
                    ${link.linkTitle}
                </a>
            </li>
            </#list>
        </ul>
        <div class='clear'></div>
    </div>
    <div class="line"></div>
    <div class="block">
        <h3>${archiveLabel}</h3>
        <ul>
            <#list archiveDates as archiveDate>
            <li>
                <#if "en" == localeString?substring(0, 2)>
                <a href="/archives/${archiveDate.archiveDateYear}/${archiveDate.archiveDateMonth}"
                   title="${archiveDate.monthName} ${archiveDate.archiveDateYear}(${archiveDate.archiveDatePublishedArticleCount})">
                    ${archiveDate.monthName} ${archiveDate.archiveDateYear}(${archiveDate.archiveDatePublishedArticleCount})</a>
                <#else>
                <a href="/archives/${archiveDate.archiveDateYear}/${archiveDate.archiveDateMonth}"
                   title="${archiveDate.archiveDateYear} ${yearLabel} ${archiveDate.archiveDateMonth} ${monthLabel}(${archiveDate.archiveDatePublishedArticleCount})">
                    ${archiveDate.archiveDateYear} ${yearLabel} ${archiveDate.archiveDateMonth} ${monthLabel}(${archiveDate.archiveDatePublishedArticleCount})</a>
                </#if>
            </li>
            </#list>
        </ul>
        <div class='clear'></div>
    </div>
    <#if 1 != users?size>
    <div class="line"></div>
    <div class="block">
        <h3>${authorLabel}</h3>
        <ul>
            <#list users as user>
            <li>
                <a class="star-icon" href="/authors/${user.oId}">
                    ${user.userName}
                </a>
            </li>
            </#list>
        </ul>
        <div class='clear'></div>
    </div>
    </#if>
</div>
