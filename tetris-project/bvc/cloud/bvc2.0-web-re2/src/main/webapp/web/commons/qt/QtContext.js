function QtEvent(id, fn){
    this.id = id;
    this.fn = fn;
}

function QtContext(id, onChannelReady){
    var self = this;
    try{
        if(window.content){
            self.id = id;
            self.onChannelReady = onChannelReady;
            self.channel = {objects:{content:window.content}};
            self.events = [];
            self.channel.objects.content.slotSetId(self.id);
            var myFn = function(e){
                e = $.parseJSON(e);
                var targetEventId = e.id;
                for(var i=0; i<self.events.length; i++){
                    if(self.events[i].id.split('.')[0]==targetEventId && typeof self.events[i].fn==='function'){
                        self.events[i].fn(e);
                    }
                }
            };
            self.channel.objects.content.signalFromQt.connect(myFn);
            if(typeof self.onChannelReady === 'function'){
                setTimeout(function(){
                    self.onChannelReady();
                }, 200);
            }
        }else{
            new QWebChannel(qt.webChannelTransport, function(channel){
                self.id = id;
                self.onChannelReady = onChannelReady;
                self.channel = channel;
                self.events = [];
                self.channel.objects.content.slotSetId(self.id);
                self.channel.objects.content.signalFromQt.connect(function(e){
                    e = $.parseJSON(e);
                    var targetEventId = e.id;
                    for(var i=0; i<self.events.length; i++){
                        if(self.events[i].id.split('.')[0]==targetEventId && typeof self.events[i].fn==='function'){
                            self.events[i].fn(e);
                        }
                    }
                });
                if(typeof self.onChannelReady === 'function'){
                    self.onChannelReady();
                }
            });
        }
    }catch(e){
        console.log('当前不是qt环境');
    }
}

QtContext.prototype.alert = function(title, message){
    //this.channel.objects.content.slotPopInformation(title, message);
    this.info(message);
};

QtContext.prototype.info = function(message){
    this.channel.objects.content.slotDisplayMsg(message, '消息');
};

QtContext.prototype.success = function(message){
    this.channel.objects.content.slotDisplayMsg(message, '成功');
};

QtContext.prototype.warning = function(message){
    this.channel.objects.content.slotDisplayMsg(message, '警告');
};

QtContext.prototype.error = function(message){
    this.channel.objects.content.slotDisplayMsg(message, '错误');
};

/**
 * 提示确认框
 * @param title 标题
 * @param message 文本内容
 * @param leftButtonText 按钮一文本
 * @param rightButtonText 按钮二文本
 * @param onLeftButtonClick 按钮一点击
 * @param onRightButtonClick 按钮二点击
 */
QtContext.prototype.confirm = function(){
    var title = arguments[0];
    var message = arguments[1];
    if(arguments.length===6 ||
        arguments.length===5 ||
        (arguments.length===4&&typeof arguments[3]!=='function')){
        var leftButtonText = arguments[2];
        var rightButtonText = arguments[3];
        var onLeftButtonClick = arguments[4];
        var onRightButtonClick = arguments[5];
        this.channel.objects.content.slotPopQuestionThreeBtn(title, message, leftButtonText, rightButtonText, '取消', 1, function(buttonIndex){
            if(buttonIndex===0 && typeof onLeftButtonClick==='function'){
                onLeftButtonClick();
            }else if(buttonIndex===1 && typeof onRightButtonClick==='function'){
                onRightButtonClick();
            }
        });
        /*this.channel.objects.content.slotPopQuestionTwoBtn(title, message, leftButtonText, rightButtonText, 1, function(buttonIndex){
            if(buttonIndex===0 && typeof onLeftButtonClick==='function'){
                onLeftButtonClick();
            }else if(buttonIndex===1 && typeof onRightButtonClick==='function'){
                onRightButtonClick();
            }
        });*/
    }else if(arguments.length===3 ||
        (arguments.length===4&&typeof arguments[3]==='function')){
        var okButtonText = arguments[2];
        var okButtonClick = arguments[3];
        this.channel.objects.content.slotPopQuestionTwoBtn(title, message, okButtonText, '取消', 0, function(buttonIndex){
            if(buttonIndex===0 && typeof okButtonClick==='function'){
                okButtonClick();
            }
        });
    }
};

/**
 * 弹出无边框窗体
 * @param url
 * @param params
 * @param settings
 *     int width 宽
 *     int height 高
 *     string title 窗体标题
 */
QtContext.prototype.window = function(url, params, settings){
    params = params?encodeURI($.toJSON(params)):'';
    settings = settings? $.toJSON(settings):'';
    this.channel.objects.content.slotPopPage(url, params, settings);
};

/**
 * 弹出有边框窗体
 * @param url
 * @param params
 * @param settings
 *     int width 宽
 *     int height 高
 *     string title 窗体标题
 */
QtContext.prototype.windowWithFrame = function(url, params, settings){
    params = params?encodeURI($.toJSON(params)):'';
    settings = settings? $.toJSON(settings):'';
    this.channel.objects.content.slotPopPageWithFrame(url, params, settings);
};

QtContext.prototype.getWindowParams = function(){
    var params = null;
    var query = window.location.search.substring(1);
    var vars = query.split('&');
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split('=');
        if(pair[0] == 'params'){
            params = pair[1];
            break;
        }
    }
    params = params || {};
    if(typeof params === 'string'){
        params = $.parseJSON(decodeURI(params));
    }
    return params;
};

QtContext.prototype.get = function(keyArray, fn){
    if(typeof fn !== 'function') return;
    if(window.content){
        var values = this.channel.objects.content.slotGetGlobalVar($.toJSON(keyArray));
        fn($.parseJSON(values));
    }else{
        this.channel.objects.content.slotGetGlobalVar($.toJSON(keyArray), function(values){
            fn($.parseJSON(values));
        });
    }
};

QtContext.prototype.set = function(key, value){
    this.channel.objects.content.slotSetGlobalVar(key, value);
};

QtContext.prototype.destroy = function(){
    this.channel.objects.content.slotClose();
};

QtContext.prototype.linkedWebview = function(webviewId, message){
    if(typeof message === 'object') message = $.toJSON(message);
    this.channel.objects.content.slotSendMsgToHtml(message, webviewId);
};

QtContext.prototype.invoke = function(){
    var _arguments = arguments;
    if(!_arguments || _arguments.length<=0) return;
    var method = _arguments[0];
    var params = [];
    for(var i=1; i<_arguments.length; i++){
        params.push(typeof arguments[i]==='object'? $.toJSON(arguments[i]):arguments[i]);
    }
    this.channel.objects.content[method].apply(this.channel.objects.content, params);
};

/**
 * 事件列表
 * signalRecvMsgFromHtml(message) 从webview中接到其他webview的联动
 *   @param message webview发的消息
 * mousedown(scope) qt 检测mousedown事件
 *   @param scope 鼠标区域，'qt'(鼠标落在qt组件上)或channelId(webview channelId)
 */
QtContext.prototype.on = function(eventName, fn){
    if(typeof fn !== 'function') return;
    this.events.push(new QtEvent(eventName, fn));
};

/**
 * 解绑事件
 * @param 事件路径
 */
QtContext.prototype.unbind = function(eventName){
    if(this.events.length <= 0) return;
    var filteredEvents = [];
    var preRemovePath = eventName.split('.');
    for(var i=0; i<this.events.length; i++){
        var testPath = this.events[i].id.split('.');
        var matched = true;
        if(preRemovePath.length > testPath.length){
            matched = false;
        }else{
            for(var j=0; j<preRemovePath.length; j++){
                if(preRemovePath[j] !== testPath[j]){
                    matched = false;
                    break;
                }
            }
        }
        if(!matched){
            filteredEvents.push(this.events[i]);
        }
    }
    this.events = filteredEvents;
};