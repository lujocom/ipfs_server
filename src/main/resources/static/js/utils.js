
//是否是微信浏览器
function is_weixin(us){
    if(null == us || us == undefined){
        return false;
    }
    var ua = us.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger') {
        return true;
    } else {
        return false;
    }
}



function is_qq(ua){
    if(ua.indexOf("mqqbrowser") > -1) {
        return true;
    } else {
        return false;
    }
}

function is_opera(ua){
    if(ua.indexOf("opr") > -1) {
        return true;
    } else {
        return false;
    }
}

function is_chrome(ua){
    if(ua.indexOf("chrome") > -1) {
        return true;
    } else {
        return false;
    }
}

function is_mx(ua){
    if(ua.indexOf("mxbroser") > -1) {
        return true;
    } else {
        return false;
    }
}


function is_uc(ua){
    if(ua.indexOf("ucbrowser") > -1) {
        return true;
    } else {
        return false;
    }
}

function is_firefox(ua){
    if(ua.indexOf("firefox") > -1) {
        return true;
    } else {
        return false;
    }
}

function is_ie(ua){
    if(ua.indexOf("msie") > -1) {
        return true;
    } else {
        return false;
    }
}

function is_sogou(ua){
    if(ua.indexOf("sogoumobilebrowser") > -1) {
        return true;
    } else {
        return false;
    }
}

function is_safari(ua){
    if(ua.indexOf("safari") > -1) {
        return true;
    } else {
        return false;
    }
}

/**
 * 图片处理为居中显示的jQuery插件
 */
(function ($) {
    $.fn.dealImage2Center = function () {

        return this.each(function () {
            var $this = $(this);
            var objHeight = this.naturalHeight;//图片高度
            var objWidth = this.naturalWidth;//图片宽度
            var _setImageStyle = function (width, height) {
                var parentHeight = $this.parent().height();//图片父容器高度
                var parentWidth = $this.parent().width();//图片父容器宽度
                /*console.log();*/
                var ratio = width / height;

                var tempHeight = parentWidth / ratio;
                var tempWidth = parentHeight * ratio;

                if (tempHeight >= parentHeight) {
                    $this.width(parentWidth);
                    $this.height(tempHeight);
                    $this.css("top", (parentHeight - tempHeight) / 2 + "px")
                } else {
                    $this.height(parentHeight);
                    $this.width(tempWidth);
                    $this.css("left", (parentWidth - tempWidth) / 2 + "px");
                }

                $this.data('loaded', 1);
                $this.css('visibility', 'visible');
            };

            !function () {
                if ($this.hasClass('scrollLoaded')) {//已处理过则跳过
                    return;
                }

                if (objHeight > 0 && objWidth > 0) {
                    _setImageStyle(objWidth, objHeight);
                }
                if (!objHeight || objHeight <= 0 || !objWidth || objWidth <= 0) {
                    var timer = setInterval(function () {
                        if ($this[0].complete) {
                            _setImageStyle($this[0].naturalWidth, $this[0].naturalHeight);
                            clearInterval(timer);
                        }
                    }, 300);
                }
            }();
        });
    }
})($);


function getRootPath(){
    var curWwwPath=window.document.location.href;
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    var localhostPaht=curWwwPath.substring(0,pos);
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(projectName);
}
function getPath(){
    var curWwwPath=window.document.location.href;
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    var localhostPaht=curWwwPath.substring(0,pos);
    return(localhostPaht);
}

function createCookie(name,value,days) {
    expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime()+(days*24*60*60*1000));
        var expires = "; expires="+date.toGMTString();
    }
    document.cookie = name+"="+value+expires+";path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' '){
            c = c.substring(1,c.length);
        }
        if (c.indexOf(nameEQ) == 0) {
            return c.substring(nameEQ.length,c.length);
        }
    }
    return null;
}

function downLoadFile(options) {
    var config = $.extend(true, { method: 'post' }, options);
    var $iframe = $('<iframe id="down-file-iframe" />');
    var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
    $form.attr('action', config.url);
    for (var key in config.data) {
        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    $form[0].submit();
    $iframe.remove();
}
