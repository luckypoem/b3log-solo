/*
 * Copyright (c) 2009, 2010, 2011, 2012, B3log Team
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
 * @fileoverview editor
 *
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.1, Apr 25, 2012
 */

/*
 * @description Create Editor can use all editor. 
 *                e.g: TinyMCE, wnd 
 * @constructor
 */
admin.editors.tinyMCE = {
    /*
     * @description 初始化编辑器
     * @param conf 编辑器初始化参数
     * @param conf.kind 编辑器类型
     * @param conf.id 编辑器渲染元素 id
     * @param conf.language 编辑器使用预研
     * @param conf.fun 编辑器首次加载完成后回调函数
     */
    init: function (conf) {
        if (conf.kind && conf.kind === "simple") {
            try {
                tinyMCE.init({
                    // General options
                    language: conf.language,
                    mode : "exact",
                    elements : conf.id,
                    theme : "advanced",

                    // Theme options
                    theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,undo,redo,|,bullist,numlist",
                    theme_advanced_buttons2 : "",
                    theme_advanced_buttons3 : "",
                    theme_advanced_toolbar_location : "top",
                    theme_advanced_toolbar_align : "left",
                
                    valid_children : "+body[style]"
                });
            } catch (e) {
                $("#tipMsg").text("TinyMCE load fail");
            }
        } else {
            try {
                tinyMCE.init({
                    // General options
                    language: conf.language,
                    mode : "exact",
                    elements : conf.id,
                    theme : "advanced",
                    plugins : "spellchecker,autosave,style,advhr,advimage,advlink,preview,inlinepopups,media,paste,fullscreen,syntaxhl,wordcount",

                    // Theme options
                    theme_advanced_buttons1 : "formatselect,fontselect,fontsizeselect,|,bold,italic,underline,strikethrough,forecolor,|,link,unlink,image,iespell,media,syntaxhl,",
                    theme_advanced_buttons2 : "undo,redo,|,bullist,numlist,outdent,indent,|,justifyleft,justifycenter,justifyright,justifyfull,|,pastetext,pasteword,|,advhr,blockquote,charmap,|,spellchecker,cleanup,fullscreen,code,preview,",
                    theme_advanced_buttons3 : "",
                    theme_advanced_toolbar_location : "top",
                    theme_advanced_toolbar_align : "left",
                    theme_advanced_resizing : true,
                    theme_advanced_statusbar_location : "bottom",
                
                    extended_valid_elements: "link[type|rel|href|charset],pre[name|class],iframe[src|width|height|name|align]",

                    valid_children : "+body[style]",
                    relative_urls: false,
                    remove_script_host: false,
                    oninit : function () {
                        if (typeof(conf.fun) === "function") {
                            conf.fun();
                        }
                    }
                });
            } catch (e) {
                $("#tipMsg").text("TinyMCE load fail");
            }
        }
    },
    
    /*
     * @description 获取编辑器值
     * @param {string} id 编辑器id
     * @returns {string} 编辑器值
     */
    getContent: function (id) {
        var content = "";
        try {
            content = tinyMCE.get(id).getContent();
        } catch (e) {
            content = $("#" + id).val();
        }
        return content;
    },
    
    /*
     * @description 设置编辑器值
     * @param {string} id 编辑器 id
     * @param {string} content 设置编辑器值
     */
    setContent: function (id, content) {
        try {
            if (tinyMCE.get(id)) {
                tinyMCE.get(id).setContent(content);
            } else {
                $("#" + id).val(content);
            }
        } catch (e) {
            $("#" + id).val(content);
        }
    }
};