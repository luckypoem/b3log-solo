<style type="text/css">
    #top {
        background-image: linear-gradient(top,#FFFFFF,#E5E5E5);
        background-image: -moz-linear-gradient(top,#FFFFFF,#E5E5E5);
        background-image: -ms-linear-gradient(top,#FFFFFF,#E5E5E5);
        background-image: -o-linear-gradient(top,#FFFFFF,#E5E5E5);
        background-image: -webkit-gradient(linear,left top,left bottom,from(#FFFFFF),to(#E5E5E5));
        filter:  progid:DXImageTransform.Microsoft.gradient(startColorstr='#FFFFFF', endColorstr='#E5E5E5');
        border-bottom: 1px solid #E5E5E5;
        height: 26px;
        line-height: 26px;
    }

    #top a, #top span span {
        border-right: 1px solid #D9D9D9;
        color: #4C4C4C;
        float: left;
        line-height: 14px;
        margin: 6px 0;
        padding: 0 6px;
        text-decoration: none;
        text-shadow: 0 -1px 0 #FFFFFF;
        font-weight: normal;
    }

    #top a:hover, #top a.hover {
        background-color: #EEF2FA;
        border-left-color: #707070;
        border-radius: 0 13px 13px 0;
        margin: 0px;
        line-height: 26px;
    }
</style>
<div id="top">
    <a href="http://b3log-solo.googlecode.com" target="_blank" class="hover">
        B3log Solo
    </a>
    <span class="right" id="admin">
        <#if isLoggedIn>
        <span>${userName}</span>
        <#if isAdmin>
        <a href="javascript:common.clearCache('all');">
            ${clearAllCacheLabel}
        </a>
        <a href="javascript:common.clearCache();">
            ${clearCacheLabel}
        </a>
        <a href="/admin-index.do#main" title="${adminLabel}">
            ${adminLabel}
        </a>
        <a href="${logoutURL}" title="${logoutLabel}">${logoutLabel}</a>
        </#if>
        <#else>
        <a href="${loginURL}" title="${loginLabel}">${loginLabel}</a>
        </#if>
        <#if isMobileRequest>
        <script type="text/javascript">
        function switch_delayer() { location.reload();}
        var Cookie = {
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
        function wptouch_switch_confirmation(skin ) {
                        Cookie.createCookie("btouch_switch_toggle", skin, 365);
                            setTimeout('switch_delayer()', 1250 ); 
        }
        </script>
        <a href="javascript:void(0)" onclick="wptouch_switch_confirmation('mobile');" title="${mobileLabel}">${mobileLabel}</a>
        </#if>
    </span>
    <div class="clear"></div>
</div>