<div id="top">
    <a href="http://b3log-solo.googlecode.com" class="logo" target="_blank">
        <span style="color: orange;margin-left:0px;">B</span>
        <span style="font-size: 9px; color: blue;"><sup>3</sup></span>
        <span style="color: green;">L</span>
        <span style="color: red;">O</span>
        <span style="color: blue;">G</span>&nbsp;
        <span style="color: orangered; font-weight: bold;">Solo</span>
    </a>
    <span class="right">
        <span id="admin"></span>
    </span>
    <div class="clear"></div>
</div>
<div class="top">
    <div id="navigation">
        <a href="/" class="home">${homeLabel}</a>
        <a href="/tags.html" class="about">${allTagsLabel}</a>
        <#list pageNavigations as page>
                <a href="${page.pagePermalink}" class="${page.pageTitle}">${page.pageTitle}</a>
        </#list>
        <a href="/blog-articles-feed.do" class="classifiche">${atomLabel}</a>
        <!--<a class="extra" href="#">extra</a>-->
    </div>
    <div class="thinks"></div>
</div>