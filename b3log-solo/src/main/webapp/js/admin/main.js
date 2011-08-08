/*
 * Copyright (c) 2009, 2010, 2011, B3log Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * main for admin
 *
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.1, Aug 8, 2011
 */

/* main 相关操作 */
admin.main = {
    /*
     * 移除未使用的 tag
     */
    removeUnusedTags: function () {
        $("#tipMsg").text("");
        jsonRpc.tagService.removeUnusedTags(function (result, error) {
            try {
                if (result.sc === "REMOVE_UNUSED_TAGS_SUCC") {
                    $("#tipMsg").text(Label.removeSuccLabel);
                } else {
                    $("#tipMsg").text(Label.removeFailLabel);
                }
            } catch (e) {
            }
        });
    }
};

/*
 * 注册到 admin 进行管理 
 */
admin.register.main =  {
    "obj": admin.main,
    "init": function () {
        $("#loadMsg").text("");
    }
}