<div class="tabPanel">
    <div class="tabs">
        <span class="selected" id="preferences" onclick="changeTab(this);">
            ${paramSettingsLabel}
        </span>
        <span id="skins" onclick="changeTab(this);">
            ${skinLabel}
        </span>
        <span id="syncGoogle" onclick="changeTab(this);">
            ${googleLabel}
        </span>
    </div>
    <div class="tabMain">
        <div id="preferencesPanel">
            <table class="form subTable" width="99%" cellpadding="0" cellspacing="9px">
                <tbody>
                    <tr>
                        <th width="234px">
                            ${blogTitle1Label}
                        </th>
                        <td>
                            <input id="blogTitle"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${blogSubtitle1Label}
                        </th>
                        <td>
                            <input id="blogSubtitle"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${blogHost1Label}
                        </th>
                        <td>
                            <input id="blogHost"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${adminGmail1Label}
                        </th>
                        <td>
                            <input id="adminGmail"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${noticeBoard1Label}
                        </th>
                        <th>
                            <textarea rows="9" id="noticeBoard"></textarea>
                        </th>
                    </tr>
                    <tr>
                        <th>
                            ${htmlhead1Label}
                        </th>
                        <th>
                            <textarea rows="9" id="htmlHead"></textarea>
                        </th>
                    </tr>
                </tbody>
            </table>
            <table class="form subTable" width="99%" cellpadding="0" cellspacing="9px">
                <tbody>
                    <tr>
                        <th width="234px">
                            ${localeString1Label}
                        </th>
                        <td>
                            <select id="localeString">
                                <option value="zh_CN">简体中文</option>
                                <option value="en_US">Englisth(US)</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${indexTagDisplayCnt1Label}
                        </th>
                        <td>
                            <input id="mostUsedTagDisplayCount" class="normalInput"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${indexRecentCommentDisplayCnt1Label}
                        </th>
                        <td>
                            <input id="recentCommentDisplayCount" class="normalInput"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${indexMostCommentArticleDisplayCnt1Label}
                        </th>
                        <td>
                            <input id="mostCommentArticleDisplayCount" class="normalInput"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${pageSize1Label}
                        </th>
                        <td>
                            <input id="articleListDisplayCount" class="normalInput"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            ${windowSize1Label}
                        </th>
                        <td>
                            <input id="articleListPaginationWindowSize" class="normalInput"/>
                        </td>
                    </tr>
                    <tr>
                        <th colspan="2">
                            <button onclick="changePreference();">${updateLabel}</button>
                        </th>
                    </tr>
                </tbody>
            </table>
        </div>
        <div id="skinsPanel" class="none">
            <button onclick="changePreference();">${saveLabel}</button>
            <div id="skinMain">
            </div>
            <button onclick="changePreference();">${saveLabel}</button>
        </div>
        <div id="syncGooglePanel" class="none">
            <table class="form" width="99%" cellpadding="0" cellspacing="9px">
                <tbody>
                    <tr>
                        <td width="160">
                            ${OAuthConsumerSecret1Label}
                        </td>
                        <td colspan="2">
                            <input id="secret"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <a href="javascript:oauthBuzz();" id="buzz">Google Buzz</a>
                        </td>
                        <td align="left">
                            <input type="checkbox" class="normalInput" id="syncBuzz"/>
                        </td>
                        <td>
                            ${postToBuzzWhilePublishArticleLabel}
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" align="right">
                            <button onclick="changePreference();">${saveLabel}</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript">
    var localeString = "";
    var getPreference = function () {
        $("#tipMsg").text("${loadingLabel}");
        jsonRpc.preferenceService.getPreference(function (result, error) {
            switch (result.sc) {
                case "GET_PREFERENCE_SUCC":
                    var preference = result.preference;
                    $("#blogTitle").val(preference.blogTitle),
                    $("#blogSubtitle").val(preference.blogSubtitle),
                    $("#mostCommentArticleDisplayCount").val(preference.mostCommentArticleDisplayCount);
                    $("#recentCommentDisplayCount").val(preference.recentCommentDisplayCount);
                    $("#mostUsedTagDisplayCount").val(preference.mostUsedTagDisplayCount);
                    $("#articleListDisplayCount").val(preference.articleListDisplayCount);
                    $("#articleListPaginationWindowSize").val(preference.articleListPaginationWindowSize);
                    $("#blogHost").val(preference.blogHost);
                    $("#adminGmail").val(preference.adminGmail);
                    $("#localeString").val(preference.localeString);
                    $("#noticeBoard").val(preference.noticeBoard);
                    $("#htmlHead").val(preference.htmlHead);
                    $("#secret").val(preference.googleOAuthConsumerSecret);
                    preference.enablePostToBuzz ? $("#syncBuzz").attr("checked", "checked") : $("#syncBuzz").removeAttr("checked");
                    localeString = preference.localeString;

                    $("#skinMain").data("skinDirName", preference.skinDirName);
                    var skins = eval('(' + preference.skins + ')');
                    var skinsHTML = "";
                    for (var i = 0; i < skins.length; i++) {
                        if (skins[i].skinName === preference.skinName
                            && skins[i].skinDirName === preference.skinDirName ) {
                            skinsHTML += "<div title='" + skins[i].skinDirName
                                + "' class='left skinItem selected'><img class='skinPreview' src='skins/"
                                + skins[i].skinDirName + "/preview.png'/><span>" + skins[i].skinName + "</span></div>"
                        } else {
                            skinsHTML += "<div title='" + skins[i].skinDirName
                                + "' class='left skinItem'><img class='skinPreview' src='skins/"
                                + skins[i].skinDirName + "/preview.png'/><span>" + skins[i].skinName + "</span></div>"
                        }
                    }
                    $("#skinMain").append(skinsHTML + "<div class='clear'></div>");

                    $(".skinItem").click(function () {
                        $(".skinItem").removeClass("selected");
                        $(this).addClass("selected");
                        $("#skinMain").data("skinDirName", this.title);
                    });
                    break;
                default:
                    break;
            }
            $("#tipMsg").text("");
        });
    }
    
    getPreference();
    
    var changeTab = function (it) {
        var tabs = ['preferences', 'skins', 'syncGoogle'];
        for (var i = 0; i < tabs.length; i++) {
            if (it.id === tabs[i]) {
                $("#" + tabs[i] + "Panel").show();
                $("#" + tabs[i]).addClass("selected");
            } else {
                $("#" + tabs[i] + "Panel").hide();
                $("#" + tabs[i]).removeClass("selected");
            }
        }
    }
    
    var changePreference = function () {
        $("#tipMsg").text("${loadingLabel}");

        if ($("#syncGoogle").hasClass("selected")) {
            if ("" === $("#secret").val().replace(/\s/g, "")) {
                $("#tipMsg").text("${contentEmptyLabel}");
                return;
            }
        }
        
        var requestJSONObject = {
            "preference": {
                "blogTitle": $("#blogTitle").val(),
                "blogSubtitle": $("#blogSubtitle").val(),
                "mostCommentArticleDisplayCount": $("#mostCommentArticleDisplayCount").val(),
                "recentArticleDisplayCount": 10, // XXX: remove recentArticleDisplayCount
                "recentCommentDisplayCount": $("#recentCommentDisplayCount").val(),
                "mostUsedTagDisplayCount": $("#mostUsedTagDisplayCount").val(),
                "articleListDisplayCount": $("#articleListDisplayCount").val(),
                "articleListPaginationWindowSize": $("#articleListPaginationWindowSize").val(),
                "skinDirName": $("#skinMain").data("skinDirName"),
                "blogHost": $("#blogHost").val(),
                "adminGmail": $("#adminGmail").val(),
                "localeString": $("#localeString").val(),
                "noticeBoard": $("#noticeBoard").val(),
                "htmlHead": $("#htmlHead").val(),
                "googleOAuthConsumerSecret": $("#secret").val(),
                "enablePostToBuzz": $("#syncBuzz").attr("checked")
            }
        }

        jsonRpc.preferenceService.updatePreference(function (result, error) {
            switch (result.sc) {
                case "UPDATE_PREFERENCE_SUCC":
                    $("#tipMsg").text("${updateSuccLabel}");
                    if ($("#localeString").val() !== localeString) {
                        window.location.reload();
                    }
                    break;
                case "GET_ARTICLE_FAIL_":
                    break;
                default:
                    break;
            }
        }, requestJSONObject);
    }

    var oauthBuzz = function () {
        if ("" === $("#secret").val().replace(/\s/g, "")) {
            $("#tipMsg").text("${contentEmptyLabel}");
            return;
        }
       // window.
        window.location = "buzz-oauth.do?googleOAuthConsumerSecret=" + $("#secret").val();
    }
</script>
