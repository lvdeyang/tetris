//资源配置域对象
console.log('mainFrane.js');
//主框架页面域对象
var mainFrame = (function(){

    //子页面入口方法
    var appMain = function(){};
    //信息提示封装
    var info =function(msg,fn){
        if(msg===null || typeof msg==='undefined'){
            msg = $('<div></div>');
        }
        gC.zkAlert.title = '信息提示';
        gC.zkAlert.content = msg;
        gC.zkAlert.commitBtn = null;
        if(typeof fn === 'function'){
            gC.zkAlert.onClose = fn;
        }else{
            gC.zkAlert.onClose = null;
        }
        gC.zkAlert.hasFooter = 'false';
        zk_Component.zkAlert(gC.zkAlert);
        zk_Component.timoutCloseZkAlert(2);
    };
    var error = function(msg){
        if(msg===null || typeof msg==='undefined'){
            msg = $('<div></div>');
        }
        gC.zkAlert.title = '错误信息';
        gC.zkAlert.content = msg;
        gC.zkAlert.commitBtn = null;
        gC.zkAlert.onClose = null;
        gC.zkAlert.hasFooter = 'false';
        zk_Component.zkAlert(gC.zkAlert);
        zk_Component.timoutCloseZkAlert(2);
    };
    //展示遮罩层
    var showModelLayer = function(id, $target){
        var $layer = $('#'+id),
            _targetHeight = parseFloat($target.css('height')),
            _targetWidth = parseFloat($target.css('width')),
            _position = getElementPosition($target[0], 0 ,0);

        $layer.css({
            'width':_targetWidth+'px',
            'height':_targetHeight+'px',
            'left':_position.left,
            'top':_position.top
        }).show();

        $(document).unbind('mousemove.layermove.'+id);
        $(document).on('mousemove.layermove.'+id, function(){
            var _position = getElementPosition($target[0], 0 ,0);
            $layer.css({
                'left':_position.left,
                'top':_position.top
            });
        });
    };
    //隐藏遮罩层
    var hideModelLayer = function(id){
        $('#'+id).hide();
        $(document).unbind('mousemove.layermove.'+id);
    };

    // 获取一个元素的位置
    var getElementPosition = function(element,left,top){
        if(element!==null && typeof element!=='undefined' && element.id !== 'zk_Container'){
            left = left + parseFloat(element.offsetLeft);
            top = top + parseFloat(element.offsetTop);
            var _position = getElementPosition(element.offsetParent, left, top);
        }else{
            return {
                "left":left,
                "top":top
            }
        }
        return _position;
    };

    /*
     options = {
     id:'',
     size:'',
     disable:'',
     styleClass:'',
     check:'',
     codeList:'',
     onChange:''
     }
     */
    var getSelect = function(options){
        var _disable = options.disable?'disabled':'', i, _codeList = options.codeList || {}, _optionTemplate = '',
            $select = $('<select '+_disable+' id="'+options.id+'" class="form-control" data_check="'+options.check+'"></select>');

        if(options.styleClass) $select.addClass(options.styleClass);
        if(options.size) $select.addClass('input-'+options.size);

        for(i in _codeList){
            if(i !== 'selected'){
                if(i !== _codeList['selected']){
                    _optionTemplate += '<option value="'+i+'">'+_codeList[i]+'</option>';
                }else{
                    _optionTemplate += '<option value="'+i+'" selected>'+_codeList[i]+'</option>';
                }
            }
        }
        $select.append(_optionTemplate);
        if(typeof options.onChange === 'function'){
            $select.on('change', function(){
                var $this = $(this);
                options.onChange.apply($this[0], [$this.val()]);
            });
        }
        return $select;
    };

    //获取表单行
    /*
     options = {
     label:'',
     formList:[]
     }
     */
    var getFormRow = function(options){
        var $row, $inputCell,
            $formList=options.formList,
            i, _size='sm',
            _tmpClass, $tmp, _formList=[];
        for(i=0; i<$formList.length; i++){
            $tmp = $formList[i];
            _formList.push($tmp[0]);
            if(_size !== 'lg'){
                _tmpClass = $tmp.attr('class');
                if(_tmpClass.indexOf('lg') >= 0){
                    _size = 'lg';
                }else if(_size='sm' && (_tmpClass.indexOf('md')>=0||(_tmpClass.indexOf('lg')<0&&_tmpClass.indexOf('md')<0&&_tmpClass.indexOf('sm')<0))){
                    _size = 'md';
                }
            }
        }
        $row = $('<div class="row"><span class="cell '+_size+'"><label>'+options.label+'</label></span></div>');
        $inputCell = $('<span class="cell '+_size+'"></span>');
        $row.append($inputCell.append(_formList));
        return $row;
    };

    //targetClass在$outer下是唯一的,$outer不要设置padding或者border
    var initDynamicHeight = function($outer, targetClass){
        var $counter = $outer.find('.counter_v'),
            _outerHeight = parseFloat($outer.css('height')),
            $tmp,
            _countHeight = parseInt($outer.css('padding-top')) + parseInt($outer.css('padding-bottom'));
        $counter.each(function(){
            var $this = $(this),
                _class = $this.attr('class'),
                _tmpHeight;
            if(_class && _class.indexOf(targetClass)>=0){
                $tmp = $this;
            }else{
                _tmpHeight = parseFloat($this.css('height'));
                _countHeight += _tmpHeight;
            }
        });
        if($tmp && $tmp[0])
            $tmp.css('height', (_outerHeight - _countHeight) + 'px');
    };

    //创建面板
    var addScroll = function(obj,axis,scrollerStyle){
        scrollerStyle = '3d-thick';
        //scrollerStyle = '3d';
        obj.mCustomScrollbar({
            theme:scrollerStyle,
            axis:axis,
            set_width:false,
            set_height:false,
            scrollInertia:0,
            scrollEasing:'animation',
            mouseWheel:true,
            autoDraggerLength:true,
            scrollButtons:{
                enable:true,
                scrollType:'continuous',
                scrollSpeed:20,
                scrollAmount:40
            },
            advanced:{
                updateOnBrowserResize:true,
                updateOnContentResize:true,
                autoExpandHorizontalScroll:true,
                autoScrollOnFocus:true
            }
        });
    };

    //获取面板内容
    var getScrollContent = function(element){
        var _class = element.attr('class');
        if(_class && _class.indexOf('mCustomScrollbar') < 0){
            addScroll(element,'xy','dark-3');
        }
        return element.find('.mCSB_container').first();
    }

    return {
        info:info,
        error:error,
        appMain:appMain,
        showModelLayer:showModelLayer,
        hideModelLayer:hideModelLayer,
        getSelect:getSelect,
        getFormRow:getFormRow,
        initDynamicHeight:initDynamicHeight,
        addScroll:addScroll,
        getScrollContent:getScrollContent
    }
})();