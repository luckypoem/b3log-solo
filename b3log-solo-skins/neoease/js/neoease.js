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
 * @fileoverview neoease js.
 *
 * @author <a href="mailto:LLY219@gmail.com">Liyuan Li</a>
 * @version 1.0.0.2, Sep 5, 2011
 */
var goTop = function (acceleration) {
    acceleration = acceleration || 0.1;

    var y = $(window).scrollTop();
    var speed = 1 + acceleration;
    window.scrollTo(0, Math.floor(y / speed));

    if (y > 0) {
        var invokeFunction = "goTop(" + acceleration + ")";
        window.setTimeout(invokeFunction, 16);
    }
};

var collapseArchive = function (it, year) {
    var tag = true;
    if (it.className === "collapse-ico") {
        it.className = "expand-ico";
        tag = false;
    } else {
        it.className = "collapse-ico";
    }
    
    $("#archiveSide li").each(function () {
        var $this = $(this);
        // hide other year month archives
        if ($this.data("year") === year) {
            if (tag) {
                $(this).show();
            } else {
                $(this).hide();
            }
        }
    });
};
    
(function () {
    // go top icon show or hide
    $(window).scroll(function () {
        var y = $(window).scrollTop();

        if (y > 182) {
            var top =  y + window.innerHeight - 21;
            if ($("body").height() - 58 <= y + window.innerHeight) {
                top = $(".footer").offset().top - 21; 
            }
            $("#goTop").fadeIn("slow").css("top", top);
        } else {
            $("#goTop").hide();
        }
    });
    
    
    // archive
    var currentYear = (new Date()).getFullYear(),
    year = currentYear;
    $("#archiveSide li").each(function (i) {
        var $this = $(this);
        
        // hide other year month archives
        if ($this.data("year") !== currentYear) {
            $(this).hide()
        }
        
        // append year archive
        if (year !== $this.data("year")) {
            year = $this.data("year");
            $this.before("<li><div onclick='collapseArchive(this, " + year + ")' class='expand-ico'>" + year + "</div></li>");
        }
    });
    
    // recent comment mouse event
    $(".recent-comments li").mouseenter(function () {
        var $ico = $(this).find("div>span");
        if ($ico[0].style.display === "none" ||$ico[0].style.display === "") {
            if ($(".recent-comments-content>a").height() > 30) {
                $ico.show();
            }
        }
    }).mouseleave(function () {
        var $ico = $(this).find("div>span");
        if ($ico[0].style.display === "inline") {
            $ico.hide();
        }
    });
    
    // recent comment mouse click
    $(".recent-comments .expand-ico").click(function () {
        if (this.className === "expand-ico") {
            $(this).parent().next().css("height", "auto");
            this.className = "collapse-ico";
        } else {
            $(this).parent().next().animate({
                "height": "16px"
            });
            this.className = "expand-ico";
        }
    });
    
    // nav current
    $(".nav ul a").each(function () {
        var $this = $(this);
        if ($this.attr("href") === location.pathname) {
            $this.parent().addClass("current");
        } else if (/\/[0-9]+$/.test(location.pathname)) {
            $(".nav ul li")[0].className = "current";
        }
    })
})();