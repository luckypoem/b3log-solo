<div class="padding12" style="line-height: 28px;">
    ${cacheStatusLabel} &nbsp; <button onclick="changeCacheStatus(this);">${openLabel}</button>
</div>
<div class="table-main">
    <div class="table-header">
        <table cellspacing="0" cellpadding="0" style="width:100%">
            <tbody>
                <tr>
                    <th>
                        ${titleLabel}
                    </th>
                    <th width="120">
                        ${typeLabel}
                    </th>
                </tr>
            </tbody>
        </table>
    </div>
    <div class="table-body">
        <table cellspacing="0" cellpadding="0" style="width:100%">
            <tbody>
                <#list pages as page>
                <tr class="table-oddRow">
                    <td style="padding-left: 6px;">
                        <a target="_blank" href="${page.link?substring(5)}">${page.cachedTitle}</a>
                    </td>
                    <td style="text-align: center;width: 120px">
                        ${page.cachedType}
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>
<script type="text/javascript">    
    var changeCacheStatus = function (it) {
        var $it = $(it);
        if ($it.text() === "${openLabel}") {
            $it.text("${closeLabel}")
        } else {
            $it.text("${openLabel}")
        }
    }
</script>