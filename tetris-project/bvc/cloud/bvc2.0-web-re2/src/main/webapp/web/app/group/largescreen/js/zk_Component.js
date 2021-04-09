/**
 * Created by lvdeyang on 2015/9/22. 公用的组件
 */
var zk_Component = (function(){
    /**
	 * 全局的枚举
	 */
    var zkEnum = {
        NOPARAM:'%@#$'
	};

    // window组件关闭事件
    var windowCloseFns = {};
	/**
	 * 消息弹框的关闭事件
	 */
	var alertOnClose = {};
    /**
	 * 展示弹框 config = { id:弹框的Id，系统默认弹框id为zk_Alert_default, width:0到96的整数,
	 * title:标题, content:内容，可以是文字，也可以是$对象， left:弹框左边距,默认200, top:弹框上边距,默认100,
	 * modalId:指定一个遮罩，默认为zk_Modal_default 
	 * hasFooter:'true'/'false'  是否展示footer
	 * commitBtn:{ text:按钮文字,
	 * callback:function(event,btn,able,content)
	 * onClose:function()关闭事件
	 * }这是一个lineHeightBtn如果不配置弹框里面只有一个关闭按钮 } return alert
	 */
    var zkAlert = function(config){
        // 鸭式辩型检验
        if(config==null || typeof config!='object'
            || config.title==null || typeof config.title=='undefined'
            || config.content==null || typeof config.content=='undefined'){
            throw new Error('zkAlert(config)配置有误');
        }
        var _id = config.id;
        if(config.id==null || typeof config.id=='undefined'){
            _id = 'zk_Alert_default';
        }
        var _width = config.width;
        if(_width==null || typeof _width=='undefined'){
            _width = 56;
        }
        var _title = config.title;
        var _content = config.content;
        var _left = config.left;
        if(_left==null || typeof _left=='undefined'){
            _left = '200px';
        }
        var _top = config.top;
        if(_top==null || typeof _top=='undefined'){
            _top = '100px';
        }
        var _modalId = config.modalId;
        if(typeof $('#'+_modalId).attr('id') == 'undefined'){
            _modalId = 'zk_Modal_default';
        }
        var _commitBtn = config.commitBtn;
        
        var _alert = $('#'+_id);
        
        if(!_alert.is(':hidden')) return;
        
        _alert.attr('data_timestamp',getDistinctId());
        if(_alert.attr('id')==null || typeof _alert.attr('id')=='undefined'){
            throw new Error('不存在id为：'+_id+'的弹框');
        }
        
        var _onClose = config.onClose;
        
        alertOnClose[_id] = config.onClose;
        
        var _alertClass = _alert.attr('class');
        _alert.attr('class','');
        _alertClass = _alertClass.split(' ');
        for(var i in _alertClass){
            if(_alertClass[i].indexOf('col-md-')<0){
                _alert.addClass(_alertClass[i]);
            }
        }
        _alert.addClass('col-md-'+_width);
        _alert.attr('data_modal',_modalId);
        var _alertTitle = _alert.children('.zk_Alert_header').first().children('.zk_Alert_title').first();
        var _alertBody = _alert.children('.zk_Alert_body').first();
        _alertTitle.text(_title);
        if(typeof _content == 'string'){
            _alertBody.text(_content);
        }else if(typeof _content == 'object'){
            _alertBody.append(_content);
        }else{
            throw new Error('错误的content类型');
        }
        var _alertFoot = _alert.children('.zk_Alert_foot').first();
        var _hasFooter = config.hasFooter;
        if(_hasFooter == 'false'){
        	_alertFoot.hide();
        	_alertBody.css('border-radius','0px 0px 7px 7px');
        }else{
        	_alertFoot.show();
        	_alertBody.css('border-radius','0px 0px 0px 0px');
        }
        if(typeof _commitBtn == 'object' && _commitBtn!=null){
            var _fn = {};
            _fn.callback = _commitBtn.callback;
            var _callback = _fn.callback;
            if(typeof _callback != 'function'){
                throw new Error('请指定commitBtn的回调函数callback');
            }
            var _btncfg = {
                text:_commitBtn.text,
                type:'btn-primary',
                icon:'../images/base/alertBox/alertBtn.png',
                pressIcon:'../images/base/alertBox/alertBtnPress.png',
                container:$('<div></div>'),
                callback:function(event,btn,able){
                	_callback.apply(_fn,[event,btn,able,_content]);
                }
            }
            var _btn = createLineHeightBtn(_btncfg);
            _btn.children('.lineHeightBtn').first();
            _alertFoot.append($('<div>&nbsp;</div>')).append(_btn);
        }
        _alert.css({
            left:_left,
            top:_top
        });
        showModal(_modalId);
        _alert.fadeIn(function(){
        	//先这么写着
	       	if(typeof mainFrame !== 'undefined')
	       		mainFrame.showModelLayer('alert_layer', _alert);
        });
        _alert.attr('data_status','show');
        return _alert;
    }
    /**
	 * 关闭消息框 id:消息框id
	 * flag用于识别是否是头部关闭按钮触发的事件
	 */
    var closeZkAlert = function(id, flag){
        if(id==null || typeof id=='undefined'){
            var id = 'zk_Alert_default';
        }
        var _alert = $('#'+id);
        if(typeof _alert.attr('id') == 'undefined'){
            throw new Error('不存在id为:'+id+'的消息框');
        }
        var _dataStatus = _alert.attr('data_status');
        if(_dataStatus == 'show'){
        	var _alertFoot = _alert.children('.zk_Alert_foot').first();
            var _alertTitle = _alert.children('.zk_Alert_header').first().children('.zk_Alert_title').first();
            var _alertbody = _alert.children('.zk_Alert_body').first();
            _alertTitle.text('');
            _alertbody.empty();
            _alertFoot.children().not('.closeButton').remove();
            var _modalId = _alert.attr('data_modal');
            hiedModal(_modalId);
            //_alert.hide('slowly');
			_alert.hide();
            _alert.attr('data_status','hide');
            if(typeof alertOnClose[id] == 'function'){
            	alertOnClose[id](_alert, flag);
            	delete alertOnClose[id];
            }
        }
		//先这么写着
        if(typeof mainFrame !== 'undefined')
        	mainFrame.hideModelLayer('alert_layer');
    };
    // 消息框延迟关闭
    var timoutCloseZkAlert = function(minute,id){
    	if(id===null||typeof id==='undefined'||id===''){
    		id = 'zk_Alert_default';
    	}
    	var _alert = $('#'+id);
    	
    	var _time0 = _alert.attr('data_timestamp');
    	
    	if(typeof minute!=='number'){
    		minute = 5;
    	}
    	minute = minute*1000;
    	setTimeout(function(){
    		var _curtime = _alert.attr('data_timestamp');
    		if(_curtime !== _time0){
    			// do nothing
    		}else{
    			closeZkAlert(id);
    		}
    	},minute);
    };

	/**
	 * 模态框展示
	 */
	var showModal = function(id,noFadeIn){
		// 因为用户可能改变浏览器大小，因此要重新设置高度
		if(noFadeIn === 'true'){
			$('#'+id).show();
		}else{
			$('#'+id).fadeIn();
		}
	};
	/**
	 * 模态框隐藏
	 */
	var hiedModal = function(id){
		$('#'+id).fadeOut();
	};

    var createAlert = function(id){
        var _alert = $('#'+id);
        if(typeof _alert.attr('id') != 'undefined'){
            throw new Error('id重复：'+id);
        }
        _alert = $('<div data_status="hide" data_modal="zk_Modal_default" class="zk_Alert" id="'+id+'">'+
            '<div class="zk_Alert_header">'+
            '<div class="zk_Alert_title"></div>'+
            '<div class="zk_Alert_close" style="float:right!important;padding-top:3px">'+
            '<a class="closeButton"><img src="../images/base/alertBox/closeBtn.png" style="float:right!important;"/></a>'+
            '</div>'+
            '</div>'+
            '<div class="zk_Alert_body"></div>'+
            '<div class="zk_Alert_foot"></div>'+
            '</div>');
        var _alertHead = _alert.children('.zk_Alert_header').first();
        var _alertFoot = _alert.children('.zk_Alert_foot').first();
        var _alertTitle = _alert.children('.zk_Alert_header').first().children('.zk_Alert_title').first();
        var _alertbody = _alert.children('.zk_Alert_body').first();
        var _headCloseBtn = _alert.children('.zk_Alert_header').first().children('.zk_Alert_close').first().children('.closeButton').first();
        var _closeButton = $('<div class="closeButton"></div>');
        _alertFoot.append(_closeButton);
        _headCloseBtn.bind('click',function(){
            closeZkAlert(id, 'head');
        });
        _alertHead.bind('mousedown',function(event){
            var _this = $(this);
            var _parent = _this.parent('.zk_Alert');
            var _baseX = event.clientX;
            var _baseY = event.clientY;

            var _mousemoveCallback = function(event){
                var _mouseX = event.clientX;
                var _mouseY = event.clientY;
                var _detaX = parseFloat(_mouseX) - parseFloat(_baseX);
                if(_detaX == 0 && _detaY == 0){
                    _detaX = 1;
                    _detaY = 1;
                }
                var _detaY = parseFloat(_mouseY) - parseFloat(_baseY);
                var curTop = parseFloat(_parent.css('top'));
                var curLeft = parseFloat(_parent.css('left'));
                var _moveX = curLeft+_detaX;
                var _moveY = curTop+_detaY;
                if(_moveX < 0) _moveX = 0;
                if(_moveX > document.body.clientWidth-parseFloat(_parent[0].offsetWidth)){
                    _moveX = document.body.clientWidth-parseFloat(_parent[0].offsetWidth);
                }
                if(_moveY < 0) _moveY = 0;
                if(_moveY > parseInt($('body').css('height'))-parseInt(_parent[0].offsetHeight)){
                    _moveY = parseInt($('body').css('height'))-parseInt(_parent[0].offsetHeight);
                }

                _parent.css({
                    top:_moveY,
                    left:_moveX
                });
                _baseX = _mouseX;
                _baseY = _mouseY;
            }

            var _mouseupCallback = function(){
                $(document).unbind('mousemove', _mousemoveCallback);
                $(document).unbind('mouseup', _mouseupCallback);
            }

            $(document).bind('mousemove', _mousemoveCallback);
            $(document).bind('mouseup', _mouseupCallback);
        });
        var _btnCfg = {
            text:'关闭',
            type:'btn-primary',
            icon:'../images/base/alertBox/alertBtn.png',
            pressIcon:'../images/base/alertBox/alertBtnPress.png',
            container:_closeButton,
            callback:function(event,btn,able){
                closeZkAlert(id);
                able(btn);
            }
        };
        var _btn = createLineHeightBtn(_btnCfg);
        _btn.children('.lineHeightBtn').first();
        $('body').append(_alert);
        return _alert;
    };

    // 通用的按钮复活函数
    var able = function(element){
        element.attr('data_commited','false');
        hideLoading();
    };

    /**
     * 隐藏loading动画
     */
    var hideLoading = function(){
        hiedModal('zk_Loading_default');
    };

    /**
     * 创建基本按钮 config = { text:按钮文字（允许）,
	 * icon:图标路径（../../images/PRF-2/允许拒绝按钮.png）, pressIcon:按钮按下时的图标,
	 * container:添加在哪个元素下面(可以传$对象或者元素的id) callback:function(event,btnObj)点击时的事件
	 * param:按钮参数 } return config.container
     */
    var createLineHeightBtn = function(config){
        // 鸭式辩型检验
        if(config==null || typeof config!='object'
            || config.text==null || typeof config.text=='undefined'
            || config.container==null || typeof config.container=='undefined'
            || config.callback==null || typeof config.callback!='function'){
            throw new Error('createLineHeightBtn(config)配置错误，请参照方法使用文档！');
        }

        var _text = config.text;
        var _type = config.type || 'btn-primary';
        var _icon = config.icon;
        var _pressIcon = config.pressIcon;
        var _container = config.container;
        var _data_info = config.data_info;
        var _param = config.param;
        if(_param===null || typeof _param==='undefined' || _param===''){
            var _param = zkEnum.NOPARAM;
        }
        var _fn = {};
        _fn.callback = config.callback;
        var _callback = _fn.callback;

        if(typeof _container=='object'){
            var _container = config.container;
        }else{
            _container = $('#'+config.container);
            if(_container.attr('id')==null || typeof _container.attr('id')=='undefined'){
                throw new Error('createCheck()：不存在id为'+config.container+'的元素。');
            }
        }
        _container.empty();
        var _btn = $('<button class="btn '+_type+'" data_param="'+_param+'" data_commited="false" data_info='+ _data_info +'>'+_text+'</button>');
        _container.append(_btn);
        _btn.bind('click',function(event){
            var _data_param = $(this).attr('data_param');
            var _commited = $(this).attr('data_commited');
            if(_commited == 'false'){
                $(this).attr('data_commited','true');
                if(_data_param === zkEnum.NOPARAM){
                    _callback.apply(_fn,[event,$(this),able]);
                }else{
                    _callback.apply(_fn,[event,$(this),able,_data_param]);
                }
            }else{
                alert('重复提交');
            }
        });
        return _container;
    };

    // 获取一个不重复的元素id
    var getDistinctId = function(){
        var _id = getTimeStamp();
        while(true){
            if($('#'+_id).attr('id')!=null || typeof $('#'+_id).attr('id')!='undefined'){
                _id = getTimeStamp();
            }else{
                break;
            }
        }
        return _id;
    };

    // 获取一个时间戳
    var getTimeStamp = function(){
        var _date = new Date();
        var _timeStamp= ''+_date.getFullYear()+ _date.getMonth()+_date.getDate()+_date.getTime();
        return _timeStamp;
    };

    /**
     * 弹出窗体 config = { size:lg|nomarl|sm,id:窗体的id，默认为zk_Window_default, title:标题,
	 * content:窗体的内容,可以为字符串或者$对象 optBtns:[ { text:按钮文字
	 * callback:function(event,btn,able) } ... ] commitBtn:{ text:按钮文字,
	 * callback:function(event,btn,able,content) }
	 * onClose:function(windowHeader,windowButtons,windowContent)window关闭时的回调函数 }
     */
    var popWindow = function(config){
        // 鸭式辩型检验
        if(config==null || typeof config!='object'
            || config.content==null || typeof config.content=='undefined'){
            throw new Error('popWindow(config)配置有误');
        }
        var _id = config.id;
        var _title = config.title;
        var _content = config.content;
        var _size = config.size || 'window_md';
        if(typeof _id == 'undefined'){
            _id = 'zk_Window_default';
        }
        var _commitBtn = config.commitBtn;
        var _modal = $('#'+_id);

        if(!_modal.is(':hidden')) return;

        if(typeof _modal.attr('id') == 'undefined'){
            throw new Error('id为：'+_id+'的窗体不存在');
        }
        if(typeof config.onClose === 'function'){
            windowCloseFns[_id] = config.onClose;
        }
        var _window = _modal.children('.zk_Window').first();
        _window.removeClass('window_sm').removeClass('window_md').removeClass('window_lg').removeClass('window_xs').addClass(_size);
        var _layout = _window.data('layout');

        var _windowHeader = _window.children('.zk_Window_header').first();
        var _windowButtons = _window.find('.buttons').first();
        _windowHeader.text(_title);

        var _windowBody = _window.find('.zk_Window_body').first();

        if(_layout === 'leftRight'){
            var _leftContent = _window.find('.content.layoutLeft').first();
            var _rightContent = _window.find('.layoutRight .content').first();

            if(!_content.left || !_content.right){
                throw new Error('content配置错误');
            }

            if(typeof _content.left == 'string'){
                _leftContent.text(_content.left);
            }else if(typeof _content.left == 'object'){
                _leftContent.append(_content.left);
            }else{
                throw new Error('无法识别的content类型');
            }

            if(typeof _content.right == 'string'){
                _rightContent.text(_content.right);
            }else if(typeof _content.right == 'object'){
                _rightContent.append(_content.right);
            }else{
                throw new Error('无法识别的content类型');
            }

        }else{
            var _windowContent = _window.children('.zk_Window_body').first().children('.content').first();
            if(typeof _content == 'string'){
                _windowContent.text(_content);
            }else if(typeof _content == 'object'){
                _windowContent.append(_content);
            }else{
                throw new Error('无法识别的content类型');
            }
        }


        if(typeof _commitBtn == 'object' && _commitBtn!=null){
            var _fn = {};
            _fn.callback = _commitBtn.callback;
            var _callback = _fn.callback;
            if(typeof _callback != 'function'){
                throw new Error('请指定commitBtn的callback');
            }
            var _btnContainer = $('<div></div>');
            _windowButtons.append(_btnContainer);
            var _btncfg = {
                text:_commitBtn.text,
                icon:'../images/base/window/commitBtn.png',
                container:_btnContainer,
                callback:function(event,btn,able){
                    _callback.apply(_fn,[event,btn,able,_content]);
                }
            };
            _btnContainer = createLineHeightBtn(_btncfg);
            _btnContainer.children('div').first().css('font-weight','bold');
        }
        _modal.fadeIn('slowly');
        //先这么调着吧
        if(typeof mainFrame !== 'undefined')
            mainFrame.showModelLayer('model_layer', _windowBody);
        return _window;
    };

    /**
     * 创建一个窗体,默认创建id为zk_Window_default的窗体 id:窗体id
     */
    var createWindow = function(options){

        var i18n = {
            'zh_CN':{closeText:'关闭'},
            'ko':{closeText:'Close'},
            'en_US':{closeText:'Close'},
            'en':{closeText:'Close'},
        }

        var id = options.id;
        var locale = options.locale || 'zh_CN';
        var layout = options.layout || 'default';//default leftRight

        var localeObj = i18n[locale] || i18n['en'];

        var _modal = $('#'+id);
        if(typeof _modal.attr('id') != 'undefined'){
            throw new Error('id重复：'+id);
        }

        if(layout === 'default'){
            _modal = $('<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 zk_Window_modal" id="'+id+'">'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 zk_Window">'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 zk_Window_header"></div>'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 zk_Window_body">'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 blank"><span class="zk_window_closeBtn">&times;</span></div>'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 content"></div>'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 buttons"></div>'+
                '</div>'+
                '</div>'+
                '</div>');
        }else if(layout === 'leftRight'){
            _modal = $('<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 zk_Window_modal" id="'+id+'">'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 zk_Window">'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 zk_Window_header"></div>'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 zk_Window_body">'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 blank"><span class="zk_window_closeBtn">&times;</span></div>'+
                '<div class="col-xs-48 col-sm-48 col-md-48 col-lg-48 content layoutLeft"></div>'+
                '<div class="col-xs-48 col-sm-48 col-md-48 col-lg-48 layoutRight">'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 content"></div>'+
                '<div class="col-xs-96 col-sm-96 col-md-96 col-lg-96 buttons"></div>'+
                '</div>'+
                '</div>'+
                '</div>'+
                '</div>');
        }

        $('body').append(_modal);
        var _window = _modal.children('.zk_Window').first();
        _window.data('layout', layout);
        var _windowButtons = _window.find('.buttons').first();
        var _closeBtn = $('<div class="closeButton"></div>');
        var _closeBtnSm = _window.children('.zk_Window_body').first().children('.blank').first().children('span').first();
        _closeBtnSm.bind('click',function(event){
            event = event || window.event;
            event.stopPropagation();
            closeWindow(id);
        });
        _windowButtons.append(_closeBtn);
        var _btnCfg = {
            text:localeObj.closeText,
            icon:'../images/base/window/commitBtn.png',
            container:_closeBtn,
            callback:function(event,btn,able){
                closeWindow(id);
                able(btn);
            }
        };
        _closeBtn = createLineHeightBtn(_btnCfg);
        _closeBtn.children('div').first().css('font-weight','bold');
        return _window;
    };

    /**
     * 窗体关闭 id:modalId
     */
    var closeWindow = function(id, flag){
        if(typeof id == 'undefined' || id==null || id==''){
            id = 'zk_Window_default';
        }
        var _modal = $('#'+id);
        var _window = _modal.children('.zk_Window').first();

        var _windowHeader = _window.children('.zk_Window_header').first();
        var _windowButtons = _window.find('.buttons').first();
        var _windowContent = _window.find('.content');
        _modal.fadeOut(function(){
            _windowHeader.text('');
            _windowContent.empty();
            _windowButtons.children().not('.closeButton').remove();
            var _onClose = windowCloseFns[id];
            if(typeof _onClose === 'function'){
                _onClose.apply(windowCloseFns,[_windowHeader, _windowButtons, _windowContent, flag]);
                delete windowCloseFns[id];
            }
        });
        //先这么调着吧
        if(typeof mainFrame !== 'undefined')
            mainFrame.hideModelLayer('model_layer');
        return _window;
    };

    /**
     * 往数组中加入一个不重复的元素
     */
    function addParam(arr,param){
        if(arr.length == 0){
            arr.push(param);
        }else{
            for(var i in arr){
                if(arr[i] == param){
                    return;
                }else {
                    if(i == arr.length-1){
                        arr.push(param);
                    }
                }
            }
        }
    }

    /**
     * 从数组中删除一个元素，返回删除后的数组
     */
    function removeParam(arr,param){
        var _newArr = [];
        for(var i in arr){
            if(arr[i] != param){
                _newArr.push(arr[i]);
            }
        }
        return _newArr;
    }

    return {
        createAlert:createAlert,
        zkAlert:zkAlert,
        closeZkAlert:closeZkAlert,
        timoutCloseZkAlert:timoutCloseZkAlert,
        popWindow:popWindow,
        createWindow:createWindow,
        closeWindow:closeWindow,
        addParam:addParam,
        removeParam:removeParam
    };
})();