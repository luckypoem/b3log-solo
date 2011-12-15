<!-- Here we're establishing whether the page was loaded via Ajax or not, for dynamic purposes. If it's ajax, we're not bringing in footer.php -->
<div id="footer">
	<center>
		<div id="wptouch-switch-link">
			<script type="text/javascript">function switch_delayer() { window.location = "/?normal"}</script>Mobile Theme <a id="switch-link" onclick="wptouch_switch_confirmation();" href="javascript:return false;"></a>		</div>
	</center>
	<p><span style="color: gray;">&copy; ${year}</span> - <a href="http://${blogHost}">${blogTitle}</a></p>
	<p>Powered by <a href="http://b3log-solo.googlecode.com" target="_blank" class="logo">
    ${b3logLabel}</a>,
	Theme by <a href="http://dx.b3log.org" target="_blank">dx</a> &lt
	<a href="http://www.bravenewcode.com/products/wptouch-pro">WPtouch</a>.</p>
</div>
<script type="text/javascript" src="/js/lib/jquery/jquery.min.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/common${miniPostfix}.js?${staticResourceVersion}" charset="utf-8"></script>
<script type="text/javascript">
    var common = new Common({
        "em00Label": "${em00Label}",
        "em01Label": "${em01Label}",
        "em02Label": "${em02Label}",
        "em03Label": "${em03Label}",
        "em04Label": "${em04Label}",
        "em05Label": "${em05Label}",
        "em06Label": "${em06Label}",
        "em07Label": "${em07Label}",
        "em08Label": "${em08Label}",
        "em09Label": "${em09Label}",
        "em10Label": "${em10Label}",
        "em11Label": "${em11Label}",
        "em12Label": "${em12Label}",
        "em13Label": "${em13Label}",
        "em14Label": "${em14Label}"
    });
    common.init();
    common.replaceSideEm($("#recentComments li"));
    
    var toggleArchive = function (it) {
        var $it = $(it);
        $it.next().slideToggle(260, function () {
            var h4Obj = $it.find("h4");
            if (this.style.display === "none") {
                h4Obj.html("${archiveLabel} +");
            } else {
                h4Obj.html("${archiveLabel} -");
            }
        });
    }
</script>