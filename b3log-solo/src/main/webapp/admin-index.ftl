<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="robots" content="none"/>
        <title>${blogTitle} - ${adminConsoleLabel}</title>
        <link type="text/css" rel="stylesheet" href="/css/${miniDir}default-admin${miniPostfix}.css"/>
        <link type="text/css" rel="stylesheet" href="/css/${miniDir}default-bowknot${miniPostfix}.css"/>
        <link type="text/css" rel="stylesheet" href="/css/${miniDir}default-base${miniPostfix}.css"/>
        <link rel="icon" type="image/png" href="/favicon.png"/>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5/jquery.min.js"></script>
        <script type="text/javascript" src="/js/lib/jsonrpc.min.js"></script>
        <script type="text/javascript" src="/js/lib/jquery/jquery.bowknot.min.js"></script>
        <script type="text/javascript" src="/js/lib/tiny_mce/tiny_mce.js"></script>
    </head>
    <body>
        <div id="loadMsg">${loadingLabel}</div>
        <div id="tipMsg"></div>
        <div id="adminMain">
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
                    <span class="left">${userName}&nbsp;|&nbsp;</span>
                    <span class='left homeIcon' onclick="window.location='/';" title='${indexLabel}'></span>
                    <span class='left'>&nbsp;|&nbsp;</span>
                    <span onclick='adminUtil.adminLogout();' class='left logoutIcon' title='${logoutLabel}'></span>
                </span>
                <div class="clear"></div>
            </div>
            <div id="allPanel">
                <div id="tabs">
                    <ul id="sideNavi">
                        <li>
                            <div data-index="article">
                                <a href="#article"><div class="left postIcon"></div>${postArticleLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="article-list">
                                <a href="#article-list"><div class="left articlesIcon"></div>${articleListLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="comment-list">
                                <a href="#comment-list"><div class="left articlesIcon"></div>${commentListLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="draft-list">
                                <a href="#draft-list"><div class="left draftsIcon"></div>${draftListLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="file-list">
                                <a href="#file-list"><div class="left fileIcon"></div>${fileListLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="page-list">
                                <a href="#page-list"><div class="left pageIcon"></div>${pageMgmtLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="link-list">
                                <a href="#link-list"><div class="left linkIcon"></div>${linkManagementLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="article-sync">
                                <a href="#article-sync"><div class="left blogSyncIcon"></div>${blogSyncLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="preference">
                                <a href="#preference"><div class="left preferenceIcon"></div>${preferenceLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="user-list">
                                <a href="#user-list"><div class="left usersIcon"></div>${userManageLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="plugin-list">
                                <a href="#plugin-list"><div class="left othersIcon"></div>${pluginMgmtLabel}</a>
                            </div>
                        </li>
                        <li>
                            <div data-index="others">
                                <a href="#others"><div class="left othersIcon"></div>${othersLabel}</a>
                            </div>
                        </li>
                    </ul>
                </div>
                <div id="main">
                    <div class="content" id="tabsContent">
                        <div id="tabs_article"></div>
                        <div id="tabs_article-list"></div>
                        <div id="tabs_comment-list"></div>
                        <div id="tabs_draft-list"></div>
                        <div id="tabs_link-list"></div>
                        <div id="tabs_preference"></div>
                        <div id="tabs_article-sync"></div>
                        <div id="tabs_page-list"></div>
                        <div id="tabs_file-list"></div>
                        <div id="tabs_others"></div>
                        <div id="tabs_user-list"></div>
                        <div id="tabs_plugin-list"></div>
                    </div>
                </div>
                <div class="footer">
                    Powered by
                    <a href="http://b3log-solo.googlecode.com" target="_blank">
                        <span style="color: orange;">B</span>
                        <span style="font-size: 9px; color: blue;"><sup>3</sup></span>
                        <span style="color: green;">L</span>
                        <span style="color: red;">O</span>
                        <span style="color: blue;">G</span>&nbsp;
                        <span style="color: orangered; font-weight: bold;">Solo</span>,
                    </a>ver ${version}
                </div>
            </div>
        </div>
        <script type="text/javascript" src="js/${miniDir}adminUtil${miniPostfix}.js"></script>
        <script type="text/javascript" src="js/${miniDir}util${miniPostfix}.js"></script>
        <script type="text/javascript">
            var adminUtil = new AdminUtil({
                "userRole": "${userRole}",
                "loadingLabel": "${loadingLabel}",
                "removeSuccLabel": "${removeSuccLabel}",
                "removeFailLabel": "${removeFailLabel}",
                "getSuccLabel": "${getSuccLabel}",
                "noCommentLabel": "${noCommentLabel}",
                "confirmRemoveLabel": "${confirmRemoveLabel}",
                "forbiddenLabel": "${forbiddenLabel}",
                "removeSuccLabel": "${removeSuccLabel}",
                "removeLabel": "${removeLabel}",
                "cancelPutTopLabel": "${cancelPutTopLabel}",
                "putTopLabel": "${putTopLabel}",
                "viewLabel": "${viewLabel}",
                "updateLabel": "${updateLabel}",
                "commentLabel": "${commentLabel}",
                "syncBlogJavaFailLabel": "${syncBlogJavaFailLabel}",
                "syncCSDNBlogFailLabel": "${syncCSDNBlogFailLabel}",
                "syncCnBlogsFailLabel": "${syncCnBlogsFailLabel}"
            });

            adminUtil.init();
            // Removes functions if enabled multiple users support
                <#if enabledMultipleUserSupport>
                var unUsed = ['article-sync'];
            for (var i = 0; i < unUsed.length; i++) {
                $("#" + unUsed[i] + "Tab").remove();
                $("#" + unUsed[i] + "Panel").remove();
            }
                </#if>
        </script>
    </body>
</html>
${plugins}