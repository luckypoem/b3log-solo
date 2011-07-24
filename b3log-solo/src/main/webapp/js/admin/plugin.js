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
 *  plugin manager for admin
 *
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.1, July 24, 2011
 */
var plugins = {};

admin.plugin = {
    add: function (data) {  
        $("#tabs").tabs("add", data);
        
        // 当插件属于 tools 时，当前页面属于 tools，导航需展开 
        if (data.path.indexOf("/tools/") === 0) {
            admin.tools.push("#" + data.id);
        }
    }
};