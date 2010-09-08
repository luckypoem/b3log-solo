<form id="uploadForm" action="" method="post" enctype="multipart/form-data">
    <table class="form" width="40%" cellpadding="0" cellspacing="9">
        <tbody>
            <tr>
                <td>
                    <input type="file" name="myFile" size="50">
                </td>
                <td>
                    <input type="submit" value="Submit" class="button" style="height: 28px;">
                </td>
            </tr>
        </tbody>
    </table>
</form>
<div id="fileList">
</div>
<div id="filePagination" class="right margin12">
</div>
<script type="text/javascript">
    // variable
    var currentPage = 1,
    pageCount = 1,
    linksLength = 1;

    var initFile = function () {
        // uploadFile
        jsonRpc.fileService.getUploadURL(function (result, error) {
            $("#uploadForm").attr("action", result);
        });

        // init file list
        $("#fileList").table({
            resizable: true,
            colModel: [{
                    style: "padding-left: 6px;",
                    name: "${titleLabel}",
                    index: "title",
                    width: 30
                }, {
                    name: "${downloadURLLabel}",
                    index: "url",
                    style: "padding-left: 6px;",
                    width: 56
                }, {
                    textAlign: "center",
                    name: "${uploadDateLabel}",
                    index: "date",
                    width: 56
                }, {
                    name: "${sizeLabel}",
                    index: "size",
                    minWidth: 50
                }, {
                    name: "${downloadCountLabel}",
                    index: "count",
                    width: 56
                },{
                    textAlign: "center",
                    name: "${removeLabel}",
                    index: "delete",
                    width: 56
                },{
                    visible: false,
                    index: "id"
                }]
        });

        $("#filePagination").paginate({
            bindEvent: "getfileList",
            pageCount: 10,
            windowSize: 5,
            currentPage: 1,
            style: "google",
            isGoTo: false,
            lastPage: "${lastPageLabel}",
            nextPage: "${nextPagePabel}",
            previousPage: "${previousPageLabel}",
            firstPage: "${firstPageLabel}"
        });
    }
    initFile();

    var deleteFile = function (event) {
        var isDelete = confirm("${confirmRemoveLabel}");

        if (isDelete) {
            $("#tipMsg").text("${loadingLabel}");
            var requestJSONObject = {
                "oId": event.data.id[0]
            };

            var result = jsonRpc.linkService.removeLink(requestJSONObject);
            switch (result.sc) {
                case "REMOVE_LINK_SUCC":
                    getfileList(1);
                    $("#tipMsg").text("${removeSuccLabel}");
                    break;
                case "REMOVE_LINK_FAIL_":
                    $("#tipMsg").text("${removeFailLabel}");
                    break;
                default:
                    break;
            }
        }
    }

    var getfileList = function (pageNum) {
        $("#tipMsg").text("${loadingLabel}");
        currentPage = pageNum;
        var requestJSONObject = {
            "paginationCurrentPageNum": pageNum,
            "paginationPageSize": PAGE_SIZE,
            "paginationWindowSize": WINDOW_SIZE
        };
        var result = jsonRpc.linkService.getLinks(requestJSONObject);
        switch (result.sc) {
            case "GET_LINKS_SUCC":
                var links = result.links;
                var linkData = [];
                linksLength = links.length;

                for (var i = 0; i < links.length; i++) {
                    linkData[i] = {};
                    linkData[i].linkTitle = links[i].linkTitle;
                    linkData[i].linkAddress = links[i].linkAddress;
                    linkData[i].update = "<div class='updateIcon'></div>";
                    linkData[i].deleted = "<div class='deleteIcon'></div>";
                    linkData[i].id = links[i].oId;
                }

                $("#fileList").table({
                    update:{
                        data: linkData
                    }
                });

                if (result.pagination.paginationPageCount === 0) {
                    pageCount = 1;
                } else {
                    pageCount = result.pagination.paginationPageCount;
                }

                $("#filePagination").paginate({
                    update: {
                        currentPage: pageNum,
                        pageCount: pageCount
                    }
                });

                break;
            default:
                break;
        }
        $("#tipMsg").text("");
    }
    getfileList(1);
</script>