<#list articles as article>
<div class="article">
    <div class="article-header">
        <h2>
        <a class="noUnderline" href="${article.articlePermalink}">
            ${article.articleTitle}
            <#if article.articleUpdateDate?datetime != article.articleCreateDate?datetime>
            <sup>
                ${updatedLabel}
            </sup>
            </#if>
            <#if article.articlePutTop>
            <sup>
                ${topArticleLabel}
            </sup>
            </#if>
        </a>
    </h2>
    </div>
    <div class="left article-info">
        <div class="article-date">
            <#if article.articleUpdateDate?datetime != article.articleCreateDate?datetime>
            ${article.articleUpdateDate?string("yyyy-MM-dd HH:mm:ss")}
            <#else>
            ${article.articleCreateDate?string("yyyy-MM-dd HH:mm:ss")}
            </#if>
        </div>
        <div class="article-comment">
            <a href="/article-detail.do?oId=${article.oId}#comments" class="left">
                ${commentLabel}（${article.articleCommentCount}）
            </a>
        </div>
    </div>
    <div class="right article-main">
        <em class="article-tags">
            <#list article.articleTags as articleTag>
            <a href="/tag-articles.do?oId=${articleTag.oId}">
                ${articleTag.tagTitle}
            </a>
            <#if articleTag_has_next>,</#if>
            </#list>
        </em>
        <div class="article-abstract">
            ${article.articleAbstract}
        </div>
    </div>
    <div class="clear"></div>
</div>
<div class="line right"></div>
<div class="clear"></div>
</#list>
<#if 0 != paginationPageCount>
<div class="pagination">
    <#if paginationPageNums?first != 1>
    <a href="/${actionName}.do?<#if oId??>oId=${oId}&</#if>paginationCurrentPageNum=1">${firstPageLabel}</a>
    <a id="previousPage" href="/${actionName}.do?<#if oId??>oId=${oId}&</#if>paginationCurrentPageNum={paginationFirstPageNum}">${previousPageLabel}</a>
    </#if>
    <#list paginationPageNums as paginationPageNum>
    <a href="/${actionName}.do?<#if oId??>oId=${oId}&</#if>paginationCurrentPageNum=${paginationPageNum}">${paginationPageNum}</a>
    </#list>
    <#if paginationPageNums?last!=paginationPageCount>
    <a id="nextPage" href="/${actionName}.do?<#if oId??>oId=${oId}&</#if>paginationCurrentPageNum={paginationLastPageNum}">${nextPagePabel}</a>
    <a href="/${actionName}.do?<#if oId??>oId=${oId}&</#if>paginationCurrentPageNum=${paginationPageCount}">${lastPageLabel}</a>
    </#if>
    &nbsp;&nbsp;${sumLabel} ${paginationPageCount} ${pageLabel}
</div>
</#if>
<script type="text/javascript">
    (function () {
        var local = window.location.search.substring(1);
        if (local === "") {
            localStorage.setItem("currentPage", 1);
        } else {
            var paramURL = local.split("&");
            for (var i = 0; i < paramURL.length; i++) {
                if (paramURL[i].split("=")[0] === "paginationCurrentPageNum") {
                    localStorage.setItem("currentPage", paramURL[i].split("=")[1]);
                }
            }
        }

        $(".pagination a").each(function () {
            var $it = $(this);
            $it.removeClass("selected");
            if ($it.text() === localStorage.getItem("currentPage")) {
                $it.addClass("selected");
            }
        });

        if ($("#nextPage").length > 0) {
            $("#nextPage").attr("href", $("#nextPage").attr("href").replace("{paginationLastPageNum}", parseInt(localStorage.getItem("currentPage")) + 1));
        }
        if ($("#previousPage").length > 0) {
            $("#previousPage").attr("href", $("#previousPage").attr("href").replace("{paginationFirstPageNum}", parseInt(localStorage.getItem("currentPage")) - 1));
        }
    })();
</script>