console.log('largeScreenConf');
var largeScreenConf = function(){
    var _width = 300,
        _height = 220;

    var dataCache = {
        lgScreenId:null,
        confId:null,

        z_index:3,

        //物理屏绘制信息
        metadata:null,

        //物理屏信息
        screens:[],

        //配置内容
        sources:[],

        //虚拟屏幕与真实屏幕比例
        resolutionX: null,
        resolutionY: null
    };

    var initData = function(lgScreenId, confId){
        //缓存
        dataCache.lgScreenId = lgScreenId;
        dataCache.confId = confId;

        //缓存数据
        gFn.queryScreens(lgScreenId, function(metadata){
            dataCache.metadata = metadata;
            gFn.querySources(confId, function(){
                init();
            });
        });

    };

    var init = function(){

        //绘制物理屏幕
        var singleScreen = drawScreen();

        //绘制窗口视图
        //drawViewPort();

        //初始化设备列表
        gFn.queryDevList();

        //初始化大屏配置信息
        drawLargescreenConfig(singleScreen);

        //初始化模板列表
        templateListInit();

    };

    //初始化模板列表
    var templateListInit = function(){
        var $templateList = $('.templateList');

        //一分屏
        var $oneScreen = $templateList.find('.oneScreen');
        var oneScreenInfo = {
            cell:{
                column:1,
                row:1,
                style:'custom1'
            },
            cellspan:[]
        };

        $oneScreen.data('template', oneScreenInfo);

        //左右分屏
        var $twoScreen = $templateList.find('.twoScreen');
        var twoScreenInfo = {
            cell:{
                column:2,
                row:1,
                style:'custom2'
            },
            cellspan:[]
        };

        $twoScreen.data('template', twoScreenInfo);

        //四分屏
        var $fourScreen = $templateList.find('.fourScreen');
        var fourScreenInfo = {
            cell:{
                column:2,
                row:2,
                style:'custom4'
            },
            cellspan:[]
        };

        $fourScreen.data('template', fourScreenInfo);

        //五分屏
        var $fiveScreen = $templateList.find('.fiveScreen');
        var fiveScreenInfo = {
            cell:{
                column:4,
                row:4,
                style:'custom5'
            },
            cellspan:[{x:0, y:0, r:3, b:2}]
        };

        $fiveScreen.data('template', fiveScreenInfo);

        //六分屏
        var $sixScreen = $templateList.find('.sixScreen');
        var sixScreenInfo = {
            cell:{
                column:3,
                row:3,
                style:'custom6'
            },
            cellspan:[{x:0, y:0, r:1, b:1}]
        };

        $sixScreen.data('template', sixScreenInfo);

        //九分屏
        var $nineScreen = $templateList.find('.nineScreen');
        var nineScreenInfo = {
            cell:{
                column:3,
                row:3,
                style:'custom6'
            },
            cellspan:[]
        };

        $nineScreen.data('template', nineScreenInfo);

    };

    //初始化大屏配置信息
    var drawLargescreenConfig = function(singleScreen) {
        var sources = dataCache.sources,
            realWidth = dataCache.metadata.width,
            realHeight = dataCache.metadata.height,
            _rows = dataCache.metadata.rows,
            _columns = dataCache.metadata.columns,
            _singleWidth = singleScreen.width,
            _singleHeight = singleScreen.height,
            _resolutionX = dataCache.resolutionX,
            _resolutionY = dataCache.resolutionY;

        if(sources.length <=0) return;

        for(var k=0;k<sources.length;k++){
            //该源所占屏幕的所有序列号
            var _nums = [];
            var screens = sources[k].screens;
            //源所占屏幕行数，列数，总数，源左上坐标所在屏号，该行尾屏号，该列尾屏号，该行下区间，上区间，源左上坐标左侧有几列，上侧有几行
            var _sourceRows, _sourceColumns, _length, _firstNum, _rowsNum, _columnsNum, _downSection, _upSection, _leftScreen, _topScreen;
            //源信息
            var _sourceLeft, _sourceTop, _sourceWidth, _sourceHeight;

            for(var n=0;n<screens.length;n++){
                var _num = screens[n].objScreen.serialNum;
                _nums.push(_num);
            }

            _length = _nums.length;

            //数组arr排序
            var compare = function (x, y) {//比较函数
                if (x < y) {
                    return -1;
                } else if (x > y) {
                    return 1;
                } else {
                    return 0;
                }
            };

            _nums.sort(compare);
            _firstNum = _nums[0];

            _leftScreen = (_firstNum-1)%_columns;
            _topScreen = Math.floor((_firstNum-1)/_columns);

            for(var j=1;j<=_rows;j++){
                if(_firstNum>_columns*(j-1) && _firstNum<=_columns*j){
                    _downSection = _columns*(j-1);
                    _upSection = _columns*j;
                    break;
                }
            }

            _sourceColumns = 0;
            for(var i=0;i<_nums.length;i++){
                if(_nums[i]>_downSection && _nums[i]<=_upSection){
                    _sourceColumns++;
                }
            }

            _sourceRows = _length/_sourceColumns;

            _columnsNum = _firstNum + _sourceColumns - 1;
            _rowsNum = _firstNum + (_sourceRows - 1) * _columns;

            _sourceLeft = _leftScreen * _singleWidth;
            _sourceTop = _topScreen * _singleHeight;

            _sourceWidth = 0;
            _sourceHeight = 0;
            for(var m=0;m<screens.length;m++){
                if(screens[m].objScreen.serialNum === _firstNum){
                    _sourceLeft = _sourceLeft + screens[m].screenLocations.x * realWidth * _resolutionX.split('/')[0]/ _resolutionX.split('/')[1];
                    _sourceTop = _sourceTop + screens[m].screenLocations.y * realHeight * _resolutionY.split('/')[0]/ _resolutionY.split('/')[1];

                }

                _sourceWidth = _sourceWidth + screens[m].screenLocations.w * realWidth * _resolutionX.split('/')[0]/ _resolutionX.split('/')[1];
                _sourceHeight = _sourceHeight + screens[m].screenLocations.h * realHeight * _resolutionY.split('/')[0]/ _resolutionY.split('/')[1];
            }

            _sourceWidth = _sourceWidth / _sourceRows;
            _sourceHeight = _sourceHeight / _sourceColumns;

            var $screen = $('#screen-wrapper').find('.screen');
            var _l = parseInt($screen.offset().left),
                _t = parseInt($screen.offset().top),
                actualSourceLeft = _l ? _l : 0,
                actualSourceTop = _t ? _t : 0;

            var posInfo = {
                'type': sources[k].type,
                '_width': _sourceWidth,
                '_height': _sourceHeight,
                'posX': _sourceLeft + actualSourceLeft,
                'posY': _sourceTop + actualSourceTop,
                '_index': sources[k].z_index,
                '_uuid': sources[k].uuid,
                '_time': sources[k].time,
                '_name': sources[k].name//节目名称
            };
            //节目屏幕创建+内容栏添加
            screenBuild(posInfo);

        }
    };

    //绘制物理屏幕
    var drawScreen = function(){
        var screenMetadata = dataCache.metadata;

        //窗口尺寸
        var $screenWrapper = $('#screen-wrapper');
        var screenWrapperWidth = parseFloat($screenWrapper[0].offsetWidth);
        var screenWrapperHeight = parseFloat($screenWrapper[0].offsetHeight);

        //根据宽计算高
        var resultWidth = 0;
        var resultHeight = 0;

        //比例
        var resolution = (screenMetadata.height * screenMetadata.rows) / (screenMetadata.width * screenMetadata.columns);
        var height = screenWrapperWidth * resolution;
        if(height > screenWrapperHeight){
            //根据高计算宽
            var width = screenWrapperHeight / resolution;
            resultWidth = parseInt(width);
            resultHeight = parseInt(screenWrapperHeight);
        }else{
            resultWidth = parseInt(screenWrapperWidth);
            resultHeight = parseInt(height);
        }

        //计算单屏宽高
        //var singleWidth = parseInt(resultWidth / screenMetadata.columns) -1;
        //var singleHeight = parseInt(resultHeight / screenMetadata.rows) -1;
        var singleWidth = parseInt(resultWidth / screenMetadata.columns);
        var singleHeight = parseInt(resultHeight / screenMetadata.rows);

        //获得虚拟屏与真实屏比例
        dataCache.resolutionX = singleWidth+'/'+screenMetadata.width;
        dataCache.resolutionY = singleHeight+'/'+screenMetadata.height;
        //dataCache.resolution = singleHeight/screenMetadata.height;

        //var totalWidth = (singleWidth * screenMetadata.columns) + (screenMetadata.columns - 1);
        //var totalHeight = (singleHeight * screenMetadata.rows) + (screenMetadata.rows - 1);
        var totalWidth = singleWidth * screenMetadata.columns + 1;
        var totalHeight = singleHeight * screenMetadata.rows + 1;

        var tplScreen = '<table border="1" class="screen" style="width:'+totalWidth+'px; height:'+totalHeight+'px; z-index:1;position:absolute"><tbody>';
        for(var i=0; i<screenMetadata.rows; i++){
            tplScreen += '<tr>';
            for(var j=0; j<screenMetadata.columns; j++){
                tplScreen += '<td style="width:'+singleWidth+'px; height:'+singleHeight+'px;">' +
                    '<div class="screenBase" style="height: 100%; width: 100%;"></div></td>';
            }
            tplScreen += '</tr>';
        }
        tplScreen += '</tbody></table>';

        $screenWrapper.append(tplScreen);

        //绑定拖拽drop
        dropEvent();

        return {
            'width': singleWidth,
            'height': singleHeight
        }
    };

    //绘制窗口视图
    var drawViewPort = function(){
        var screenMetadata = dataCache.metadata;

        var $viewPort = $('#viewPort');

        //设定窗口宽度
        var viewPortWidth = parseFloat($viewPort[0].clientWidth);

        //获取真实窗口的尺寸
        var $screenWrapper = $('#screen-wrapper');
        var $screen = $screenWrapper.find('table');
        var screenWrapperWidth = parseFloat($screenWrapper[0].offsetWidth);
        var screenWrapperHeight = parseFloat($screenWrapper[0].offsetHeight);

        //计算窗口视图的高度
        var viewPortHeight = (viewPortWidth * screenWrapperHeight) / screenWrapperWidth;

        $viewPort.css({width:viewPortWidth+'px', height:viewPortHeight+'px'});

        //绘制视图版物理屏
        var td = $screen.find('td')[0];
        var singleWidth = parseFloat(td.clientWidth);
        var singleHeight = parseFloat(td.clientHeight);

        var viewSingleWidth = (singleWidth * viewPortWidth) / screenWrapperWidth;
        var viewSingleHeight = (singleHeight * viewPortHeight) / screenWrapperHeight;

        var viewTotalWidth = (viewSingleWidth * screenMetadata.columns) + (screenMetadata.columns - 1);
        var viewTotalHeight = (viewSingleHeight * screenMetadata.rows) + (screenMetadata.rows - 1);

        var tplViewScreen = '<table border="1" class="screen" style="width:'+viewTotalWidth+'px; height:'+viewTotalHeight+'px;"><tbody>';
        for(var i=0; i<screenMetadata.rows; i++){
            tplViewScreen += '<tr>';
            for(var j=0; j<screenMetadata.columns; j++){
                tplViewScreen += '<td style="width:'+viewSingleWidth+'px; height:'+viewSingleHeight+'px;"></td>';
            }
            tplViewScreen += '</tr>';
        }
        tplViewScreen += '</tbody></table>';

        $viewPort.prepend(tplViewScreen);

        $('#view-window-wrapper').css({
            width:viewTotalWidth + 'px',
            height:viewTotalHeight + 'px'
        });

    };

    //资源列表拖拽至大屏(绑定事件)
    var dropEvent = function(){
        var $screen = $('#screen-wrapper').find('.screen');

        $('#screen-wrapper')[0].addEventListener('drop', function (e) {
            var target = e.target;
            if($(target).hasClass('screenBase')){
                var _dragParam = e.dataTransfer.getData('dragdata');
                //var _index = parseInt(e.dataTransfer.getData('dragTargetIndex')),
                var _name = _dragParam.split('@@')[3],
                    _uuid = _dragParam.split('@@')[8],
                    _type = _dragParam.split('@@')[0],
                    _layerId = _dragParam.split('@@')[7],
                    _channelId = _dragParam.split('@@')[10],
                    posX = e.pageX,
                    posY = e.pageY;

                if($screen.hasClass('screenLocked')){
                    mainFrame.info('布局已被锁定！');
                }else{
                    if(_type == "device"){
                        var posInfo = {
                            'type':_type,
                            '_width': _width,
                            '_height': _height,
                            'posX': posX,
                            'posY': posY,
                            '_index':dataCache.z_index,
                            '_uuid': _uuid + '@@' + _layerId + '@@' + _channelId,
                            '_name': _name//节目名称
                        };

                        //节目屏幕创建+内容栏添加
                        screenBuild(posInfo);

                    }else if(_type == "template"){

                        templateBulid();

                        createTemplate();

                    }else if(_type == "polling"){
                        var posInfo = {
                            'type':_type,
                            '_width': _width,
                            '_height': _height,
                            'posX': posX,
                            'posY': posY,
                            '_index':dataCache.z_index,
                            '_uuid': [],
                            '_name': "轮询屏",//轮询名称就叫‘轮询’
                            '_time': null
                        };

                        //节目屏幕创建+内容栏添加
                        screenBuild(posInfo);
                    }
                }
            }
        });
        $('#screen-wrapper')[0].addEventListener('dragover', function(e){e.preventDefault();});
    };

    //模板创建(dom)
    var templateBulid = function(){
        var $screen = $('#screen-wrapper').find('.screen'),
            $tbody = $screen.find('tbody'),
            $layout = $screen.find('.layout-wrapper'),
            $cancelTemplate = $('.cancelTemplate');

        var $templateList = $('.templateProperty');

        if($layout.length <= 0){
            var _width = $tbody[0].offsetWidth,
                _height = $tbody[0].offsetHeight;

            var templateScreen = '<div class="layout-wrapper" style="width:'+ _width +'px; height:'+ _height +'px; left: 0; top: 0; position: absolute"></div>';

            $screen.append(templateScreen);
        }

        $cancelTemplate.show();
        $templateList.show();
    };

    //节目屏幕创建
    var screenBuild = function(posInfo){
        //节目屏幕创建
        var $screen = $('#screen-wrapper').find('.screen');
        var sources = dataCache.sources;
        var _l = parseInt($screen.offset().left),
            _t = parseInt($screen.offset().top),
            actualSourceLeft = _l ? _l : 0,
            actualSourceTop = _t ? _t : 0,
            actualSourceItemLeft = parseInt(posInfo.posX - actualSourceLeft),
            actualSourceItemTop = parseInt(posInfo.posY - actualSourceTop),
        //标识是不是新建
            newFlag = true;

        var $actualSourceItem;

        //右侧超过边框
        if ((actualSourceItemLeft + posInfo._width) > $screen[0].clientWidth) {
            actualSourceItemLeft = $screen[0].clientWidth - posInfo._width;
        }

        //下侧超过边框
        if ((actualSourceItemTop + posInfo._height) > $screen[0].clientHeight) {
            actualSourceItemTop = $screen[0].clientHeight - posInfo._height;
        }

        if(posInfo.type == "device"){
            for (var i = 0; i < sources.length; i++) {
                if (posInfo._uuid == sources[i].uuid && posInfo.type == sources[i].type && posInfo._index == sources[i].z_index) {
                    newFlag = false;
                }
            }

            //如果是设备
            $actualSourceItem = $('<div class="actualSourceItem videoSourceItem"><span class="actualSourceItemMsg">' + posInfo._name + '</span></div>');

        }else if(posInfo.type == "polling"){
            for (var j = 0; j < sources.length; j++) {
                if (posInfo.type == sources[j].type && posInfo._index == sources[j].z_index) {
                    newFlag = false;
                }
            }

            //如果是轮询
            $actualSourceItem = $('<div class="actualSourceItem pollingSourceItem"><span class="actualSourceItemMsg">' + posInfo._name + '</span></div>');
        }

        $actualSourceItem.css({
            'width': posInfo._width + 'px',
            'height': posInfo._height + 'px',
            'left': actualSourceItemLeft + 'px',
            'top': actualSourceItemTop + 'px',
            'z-index': posInfo._index
        });

        //数据绑定
        var sourceInfo = {
            '_type': posInfo.type,
            '_width': posInfo._width,
            '_height': posInfo._height,
            'z_index': posInfo._index,
            '_relativeX': actualSourceItemLeft,
            '_relativeY': actualSourceItemTop,
            '_uuid': posInfo._uuid,
            '_name': posInfo._name,//节目名称
            //存真实源信息（只有剪切时跟上面信息不一样）
            '_cacheX': actualSourceItemLeft,
            '_cacheY': actualSourceItemTop,
            '_cacheW': posInfo._width,
            '_cacheH': posInfo._height,
            '_time': posInfo._time
        };
        $actualSourceItem.data('info', sourceInfo);

        if (newFlag) {

            var arr = sourceBelongScreen($actualSourceItem);

            var flag = screenBindCache($actualSourceItem, arr);

            if (!flag) return;
        }

        $screen.append($actualSourceItem);

        moveFunc($actualSourceItem);
        dropFunc($actualSourceItem);

        //内容栏创建
        contentListBulid($actualSourceItem, newFlag);
    };

    //内容栏创建
    var contentListBulid = function($actualSourceItem, newFlag){

        //内容栏添加
        var $content = $('.right').find('.bottom').find('.content').first(),
            initLength = $content.find('.contentList').length,
            $contentList;

        var posInfo = $actualSourceItem.data('info');

        if (!initLength) {
            $contentList = $('<ul class="contentList"></ul>');
            $content.empty().append($contentList);
        } else {
            $contentList = $content.find('.contentList');
        }

        if(posInfo._type == "device"){
            var $contentListItem = $('<li class="contentListItem vedioSourceContent" ><span>' + posInfo._name + '</span>' +
                    '<span title="隐藏对应源" class="display-icon"><img src="../images/content/display-source.png" /></span>' +
                    '<span title="显示对应源" class="hidden-icon"><img src="../images/content/hidden-source.png" /></span>' +
                    '<span title="解锁对应源" class="locked-icon"><img src="../images/content/locked-source.png" /></span>' +
                    '<span title="锁定对应源" class="unlocked-icon"><img src="../images/content/unlocked-source.png" /></span>' +
                    '</li>');
        }else if(posInfo._type == "polling"){
            var $contentListItem = $('<li class="contentListItem pollingSourceContent" ><span>' + posInfo._name + '</span><span class="pollingTitle" style="margin-left: 20px"></span>' +
                    '<span title="隐藏对应源" class="display-icon"><img src="../images/content/display-source.png" /></span>' +
                    '<span title="显示对应源" class="hidden-icon"><img src="../images/content/hidden-source.png" /></span>' +
                    '<span title="解锁对应源" class="locked-icon"><img src="../images/content/locked-source.png" /></span>' +
                    '<span title="锁定对应源" class="unlocked-icon"><img src="../images/content/unlocked-source.png" /></span>' +
                    '</li>');
        }

        $contentList.prepend($contentListItem);

        if ($contentList.hasClass('contentDrag')) {
            $contentList.sortable({
                onDrop: function ($item, container, _super, event) {
                    $item.removeClass(container.group.options.draggedClass).removeAttr("style");
                    $("body").removeClass(container.group.options.bodyClass);
                    contentListManage($contentList);
                }
            });
        }

        if(posInfo._type == "polling"){
            var $pollingTitle =$contentListItem.find('.pollingTitle');
            var _text = '已选择设备（'+posInfo._uuid.length+'）';
            $pollingTitle.empty().append(_text);

            $pollingTitle.data('pollingmem', posInfo._uuid);
            $pollingTitle.data('timer', posInfo._time);
        }


        //屏幕和内容栏绑定
        $actualSourceItem.data('node', $contentListItem);
        $contentListItem.data('node', $actualSourceItem);

        //图层管理
        if (newFlag) {
            contentListManage($contentList);
        }
    };

    //绑定移动
    var moveFunc = function($this){
        $this.myDrag({
            randomPosition:false,

            dragEnds:function(x,y){
                console.log(x);
                console.log(y);
                var arr,
                    dragEndsInfo = this.data('info'),
                    _x = dragEndsInfo._relativeX;
                _y = dragEndsInfo._relativeY;
                _cacheX = dragEndsInfo._cacheX;
                _cacheY = dragEndsInfo._cacheY;

                var _dragEndsInfo = $.extend(true, {}, dragEndsInfo);
                _dragEndsInfo._relativeX = x;
                _dragEndsInfo._cacheX = _cacheX+ (x-_x);
                _dragEndsInfo._relativeY = y;
                _dragEndsInfo._cacheY = _cacheY + (y-_y);
                this.data('info', _dragEndsInfo);

                arr = sourceBelongScreen(this);

                var flag = screenBindCache(this, arr);

                if(!flag){
                    this.css({top:_y, left: _x});
                    this.data('info', dragEndsInfo);
                }

            }
        });
    };

    //源drop
    var dropFunc = function($this){
        var $screen = $('#screen-wrapper').find('.screen');

        $this[0].addEventListener('drop', function (e) {
            var target = e.target;
            var _dragParam = e.dataTransfer.getData('dragdata');
            //var _index = parseInt(e.dataTransfer.getData('dragTargetIndex')),
            var _name = _dragParam.split('@@')[3],
                _uuid = _dragParam.split('@@')[8],
                _type = _dragParam.split('@@')[0],
                _layerId = _dragParam.split('@@')[7],
                _channelId = _dragParam.split('@@')[10],
                posX = e.pageX,
                posY = e.pageY;

            if(!$screen.hasClass('screenLocked')){
                if(_type == "device"){
                    var posInfo = {
                        'type':_type,
                        '_width': _width,
                        '_height': _height,
                        'posX': posX,
                        'posY': posY,
                        '_index':dataCache.z_index,
                        '_uuid': _uuid + '@@' + _layerId + '@@' + _channelId,
                        '_name': _name//节目名称
                    };

                    //节目屏幕创建+内容栏添加
                    screenBuild(posInfo);

                }else if(_type == "template"){

                    templateBulid();

                    createTemplate();

                }else if(_type == "polling"){
                    var posInfo = {
                        'type':_type,
                        '_width': _width,
                        '_height': _height,
                        'posX': posX,
                        'posY': posY,
                        '_index':dataCache.z_index,
                        '_uuid': [],
                        '_name': "轮询屏",//轮询名称就叫‘轮询’
                        '_time': null
                    };

                    //节目屏幕创建+内容栏添加
                    screenBuild(posInfo);
                }
            }else{
                if(_type == 'device' || _type == 'polling'){

                    var sourceInfo = $this.data('info');

                    //更新源、更新缓存、更新内容栏
                    var _sourceInfo = $.extend(true, {}, sourceInfo);

                    //更新源
                    _sourceInfo._name = _name;
                    _sourceInfo._uuid = _uuid;
                    $this.data('info', _sourceInfo);
                    $this.find('span').text(_name);

                    //更新缓存源信息（位置信息不变）
                    updateSrcWithoutLoc(_sourceInfo, sourceInfo);

                    //更新内容栏
                    var $content = $this.data('node');
                    $content.find('span').eq(0).text(_name);

                }
            }
        });
    };

    //算法--屏幕所覆盖的物理屏序号(以行数和列数建立坐标，分别算出屏幕左上角点和右下角点所在坐标区域，然后计算出在此区域中所有物理屏序列号数)
    var sourceBelongScreen = function($source){
        var screenMetadata = dataCache.metadata,
            $screenBase = $('.screen').find('.screenBase').eq(0),
            singleScreenWidth = $screenBase.parent()[0].offsetWidth,
            singleScreenHeight = $screenBase.parent()[0].offsetHeight,
            rows = screenMetadata.rows,
            columns = screenMetadata.columns,
            sourceInfo = $source.data('info'),
            sourceWidth = sourceInfo._width,
            sourceHeight = sourceInfo._height,
            sourceX = sourceInfo._relativeX,
            sourceY = sourceInfo._relativeY;

        var numberXStart,
            numberYStart,
            numberXEnd,
            numberYEnd,
            sourceXStart = sourceX,
            sourceYStart = sourceY,
            sourceXEnd = sourceX + sourceWidth,
            sourceYEnd = sourceY + sourceHeight,
            numberArr = [];

        //取起点--左上角（上边界只判大于，下边界只判小于，中间都判）
        for(var i=1;i<=rows;i++){
            if(i == 1){
                if(sourceYStart < (i * singleScreenHeight)) {
                    numberYStart = i;
                    break;
                }
            }else if(i == rows) {
                if(sourceYStart >= ((i - 1) * singleScreenHeight)) {
                    numberYStart = i;
                    break;
                }
            }else{
                if ( sourceYStart < (i * singleScreenHeight) && sourceYStart >= ((i - 1) * singleScreenHeight)) {
                    numberYStart = i;
                    break;
                }
            }
        }

        for(var j=1;j<=columns;j++){
            if(j == 1){
                if(sourceXStart < (j*singleScreenWidth)) {
                    numberXStart = j;
                    break;
                }
            }else if(j == columns) {
                if(sourceXStart >= ((j-1)*singleScreenWidth)) {
                    numberXStart = j;
                    break;
                }
            }else{
                if ( sourceXStart < (j*singleScreenWidth) && sourceXStart >= ((j-1)*singleScreenWidth)) {
                    numberXStart = j;
                    break;
                }
            }
        }

        //取终点--右下角（上边界只判大于，下边界只判小于，中间都判）
        for(var i=1;i<=rows;i++){
            if(i == 1 && rows != 1){
                if(sourceYEnd <= (i * singleScreenHeight)) {
                    numberYEnd = i;
                    break;
                }
            }else if(i == rows) {
                if(sourceYEnd > ((i - 1) * singleScreenHeight)) {
                    numberYEnd = i;
                    break;
                }
            }else{
                if ( sourceYEnd <= (i * singleScreenHeight) && sourceYEnd > ((i - 1) * singleScreenHeight)) {
                    numberYEnd = i;
                    break;
                }
            }
        }

        for(var j=1;j<=columns;j++){
            if(j == 1 && columns != 1){
                if(sourceXEnd <= (j*singleScreenWidth)) {
                    numberXEnd = j;
                    break;
                }
            }else if(j == columns) {
                if(sourceXEnd > ((j-1)*singleScreenWidth)) {
                    numberXEnd = j;
                    break;
                }
            }else{
                if ( sourceXEnd <= (j*singleScreenWidth) && sourceXEnd > ((j-1)*singleScreenWidth)) {
                    numberXEnd = j;
                    break;
                }
            }
        }

        //计算区域内所有屏幕序号
        for(var i = numberYStart; i <= numberYEnd; i++){
            for(var j = numberXStart; j <= numberXEnd; j++){
                var numTemp = (i-1)*columns + j;
                numberArr.push(numTemp);
            }
        }

        console.log(numberArr);

        return numberArr;
    };

    //屏幕（源）实时与缓存绑定
    var screenBindCache = function($source, arr){
        var sourceInfo = $source.data('info'),
            screensCacheInfo = dataCache.screens,
            sourceCacheInfo = dataCache.sources;

        //找到该屏幕（源）在缓存中数据
        var _source = sourceIndexScreen(sourceInfo, sourceCacheInfo);

        //区分序列号
        var serialArr = serialNumIndexScreen(_source.screens, arr);

        //缓存数据重构
        var flag = dataCacheRebuild(screensCacheInfo, sourceInfo, _source, serialArr);

        console.log(sourceCacheInfo);
        console.log(screensCacheInfo);

        return flag;
    };

    //坐标维护：根据源和单个物理屏维护
    var coordinateBindCache = function(sourceInfo, num){
        var $physcreen = $('.middle').find('.screen').find('.screenBase').eq(num-1),
            _columns = dataCache.metadata.columns,
            _rows = dataCache.metadata.rows;

        //定义每个num所在屏左和上分别还有几个屏
        var _leftNum = (num-1)%_columns,
            _topNum = Math.floor((num-1)/_columns);

        //获取该物理屏left、top、width和height(+1加boder)
        var _phyWidth = $physcreen.parent()[0].offsetWidth,
            _phyHeight = $physcreen.parent()[0].offsetHeight,
            _phyLeft = _leftNum*_phyWidth,
            _phyTop = _topNum*_phyHeight;

        //获取源的left、top、width和height
        var _sourceWidth = sourceInfo._width,
            _sourceHeight = sourceInfo._height,
            _sourceLeft = sourceInfo._relativeX,
            _sourceTop = sourceInfo._relativeY;

        //虚拟源信息--相对于原始比例
        var _virSourceWidth = sourceInfo._cacheW,
            _virSourceHeight = sourceInfo._cacheH,
            _virSourceLeft = sourceInfo._cacheX,
            _virSourceTop = sourceInfo._cacheY;

        //计算出源与该物理屏重合部分l t w d
        var _coincideLeft, _coincideTop, _coincideWidth, _coincideHeight;

        //_coincideLeft计算
        if(_sourceLeft >= _phyLeft){
            _coincideLeft = _sourceLeft;
        }else{
            _coincideLeft = _phyLeft;
        }

        //_coincideTop计算
        if(_sourceTop >= _phyTop){
            _coincideTop = _sourceTop;
        }else{
            _coincideTop = _phyTop;
        }

        //_coincideWidth计算
        if((_sourceLeft - _phyLeft + _sourceWidth) >= _phyWidth){
            _coincideWidth = _phyWidth - (_coincideLeft - _phyLeft);
        }else{
            _coincideWidth = _sourceWidth - (_coincideLeft - _sourceLeft);
        }

        //_coincideHeight计算
        if((_sourceTop - _phyTop + _sourceHeight) >= _phyHeight){
            _coincideHeight = _phyHeight - (_coincideTop - _phyTop);
        }else{
            _coincideHeight = _sourceHeight - (_coincideTop - _sourceTop);
        }

        var coincideInfo = {
            screenLocations: {x:null,y:null,w:null,h:null},
            sourceLocations: {x:null,y:null,w:null,h:null}
        };

        //计算对物理屏的相对坐标
        var realWidth = dataCache.metadata.width,
            realHeight = dataCache.metadata.height;

        coincideInfo.screenLocations.x = ((_coincideLeft - _phyLeft) / _phyWidth);
        coincideInfo.screenLocations.y = ((_coincideTop - _phyTop) / _phyHeight);
        coincideInfo.screenLocations.w = (_coincideWidth / _phyWidth);
        coincideInfo.screenLocations.h = (_coincideHeight / _phyHeight);

        //计算对源的相对坐标
        coincideInfo.sourceLocations.x = (_coincideLeft - _virSourceLeft)/_virSourceWidth;
        coincideInfo.sourceLocations.y = (_coincideTop - _virSourceTop)/_virSourceHeight;
        coincideInfo.sourceLocations.w = _coincideWidth / _virSourceWidth;
        coincideInfo.sourceLocations.h = _coincideHeight / _virSourceHeight;

        //源转换为百分比
        coincideInfo.sourceLocations.x = (coincideInfo.sourceLocations.x);
        coincideInfo.sourceLocations.y = (coincideInfo.sourceLocations.y);
        coincideInfo.sourceLocations.w = (coincideInfo.sourceLocations.w);
        coincideInfo.sourceLocations.h = (coincideInfo.sourceLocations.h);

        return coincideInfo;
    };

    //缓存数据重构
    var dataCacheRebuild = function(phyScreens, sourceInfo, source, arr){
        var _sources = dataCache.sources;
        var existArr = arr.existArr,
            addArr = arr.addArr,
            removeArr = arr.removeArr,
            screens = source.screens;

        //新物理屏数据的添加
        for(var j=0;j<addArr.length;j++){
            for(var l=0;l<phyScreens.length;l++){
                if(phyScreens[l].serialNum == addArr[j]){
                    var useDno = useDnoAgreement(phyScreens[l]);
                    if(useDno == null) {
                        mainFrame.info('物理屏'+addArr[j]+'解码能力已占满！');
                        return false;
                    }

                    var coincideInfo = coordinateBindCache(sourceInfo, addArr[j]);
                    var screen = {
                        objScreen:phyScreens[l],
                        useDno:useDno,
                        //新建screenLocations和sourceLocations
                        screenLocations: coincideInfo.screenLocations,
                        sourceLocations: coincideInfo.sourceLocations
                    };

                    screens.push(screen);
                }
            }
        }

        //源第一次拖入
        if(addArr.length > 0 && existArr.length == 0 && removeArr.length == 0){
            _sources.push(source);
        }

        //已覆盖物理屏数据的更新
        for(var i=0;i<existArr.length;i++){
            for(var m=0;m<screens.length;m++){
                if(screens[m].objScreen.serialNum == existArr[i]){
                    //更新screenLocations和sourceLocations
                    var coincideInfo = coordinateBindCache(sourceInfo, existArr[i]);

                    screens[m].screenLocations = coincideInfo.screenLocations;
                    screens[m].sourceLocations = coincideInfo.sourceLocations;
                }
            }
        }

        //不覆盖的物理屏删除
        for(var k=0;k<removeArr.length;k++){
            for(var n=0;n<screens.length;n++){
                if(screens[n].objScreen.serialNum == removeArr[k]){

                    //解码号置为可用
                    //todo:测试objScreen改变会不会改变screens--可以
                    var dnos = screens[n].objScreen.dnos;
                    for(var i=0;i<dnos.length;i++){
                        if(dnos[i].code == screens[n].useDno){
                            dnos[i].status = 0;
                        }
                    }

                    //直接删除该screen
                    screens.splice(n,1);
                }
            }
        }

        return true;

    };

    //协商物理屏解码号
    var useDnoAgreement = function(screen){
        var dnos = screen.dnos,
            useDno = null;
        for(var i=0;i<dnos.length;i++){
            if(dnos[i].status == 0){
                dnos[i].status = 1;
                useDno = dnos[i].code;
                break;
            }
        }

        return useDno;
    };

    //找已有屏幕数据缓存或者绑定缓存
    var sourceIndexScreen = function(sourceInfo,sourceCacheInfo){
        var _resolutionX = dataCache.resolutionX,
            _resolutionY = dataCache.resolutionY;
        var	_source = {};
        //找出对应源
        if(sourceInfo._type == "device"){
            for(var i=0;i<sourceCacheInfo.length;i++){
                if(sourceCacheInfo[i].uuid == sourceInfo._uuid && sourceCacheInfo[i].type == sourceInfo._type && sourceCacheInfo[i].z_index == sourceInfo.z_index){
                    _source = sourceCacheInfo[i];
                }
            }

        }else if(sourceInfo._type == "polling"){
            for(var i=0;i<sourceCacheInfo.length;i++){
                if(sourceCacheInfo[i].type == sourceInfo._type && sourceCacheInfo[i].z_index == sourceInfo.z_index){
                    _source = sourceCacheInfo[i];
                }
            }
        }

        if(JSON.stringify(_source) == "{}"){
            _source = {
                $dom:null,
                uuid:sourceInfo._uuid,
                name:sourceInfo._name,
                z_index:sourceInfo.z_index,
                type:sourceInfo._type,
                width: _width * _resolutionX.split('/')[1] / _resolutionX.split('/')[0],
                height: _height * _resolutionY.split('/')[1] / _resolutionY.split('/')[0],
                screens:[]
            };
        }

        return _source;
    };

    //物理屏索引序列号，分别找到已有的、增加的和要删除的序列号
    var serialNumIndexScreen = function(screens, arr){
        var sourceCacheInfo = dataCache.sources;
        var existArr=[], addArr=[], removeArr=[], serialNumArr=[];

        for(var m=0;m<screens.length;m++) {
            serialNumArr.push(screens[m].objScreen.serialNum);
        }

        if(JSON.stringify(screens) == '[]'){
            addArr = arr;
        }else{
            //区分序列号
            for(var i=0;i<serialNumArr.length;i++) {
                for (var j = 0; j < arr.length; j++) {
                    if (arr[j] == serialNumArr[i]) {
                        existArr.push(arr[j]);
                    }
                }
            }

            addArr = arr;
            removeArr = serialNumArr;
            for(var k=0;k<existArr.length;k++){
                for(var i in arr){
                    if(addArr[i] == existArr[k]){
                        addArr.splice(i,1);
                    }
                }

                for(var i in serialNumArr){
                    if(removeArr[i] == existArr[k]){
                        removeArr.splice(i,1);
                    }
                }
            }
        }

        var returnObj = {
            addArr: addArr,
            existArr: existArr,
            removeArr: removeArr
        };
        console.log(returnObj);

        return returnObj;

    };

    //转换协议数据
    var generateTasks = function(sources){

        var tasks = [];

        if(sources && sources.length>0){
            for(var i=0; i<sources.length; i++){
                var task = {content:{src:{}}, location:[]};

                var source = sources[i];

                task.type = source.type;
                task.time = source.time;
                //源的bundleId
                task.content.src.uuid = source.uuid;
                //这个地方先写死了
                task.content.src.streamtype = 0;
                task.content.src.width = source.width;
                task.content.src.height = source.height;
                task.content.src.name = source.name;

                var screens = source.screens;

                for(var j=0; j<screens.length; j++){
                    var location = {relation:[]};
                    task.location.push(location);
                    var relation = {src:{}, dst:{}};
                    location.relation.push(relation);

                    var screen = screens[j];

                    relation.src.x = screen.sourceLocations.x;
                    relation.src.y = screen.sourceLocations.y;
                    relation.src.w = screen.sourceLocations.w;
                    relation.src.h = screen.sourceLocations.h;
                    //先写死，（2：裁剪；1：保持宽高比；0：充满）
                    relation.src.prop = screens.length > 1? 2: 1;
                    if(screen.sourceLocations.w < 1 || screen.sourceLocations.h < 1){
                        relation.src.prop = 2;
                    }

                    relation.dst.uuid = screen.objScreen.jv230Uuid;
                    relation.dst.ability = screen.objScreen.ablity;
                    relation.dst.dno = screen.useDno;
                    relation.dst.zindex = source.z_index;
                    relation.dst.x = screen.screenLocations.x;
                    relation.dst.y = screen.screenLocations.y;
                    relation.dst.w = screen.screenLocations.w;
                    relation.dst.h = screen.screenLocations.h;
                }

                tasks.push(task);
            }
        }

        return tasks;
    };

    //更新缓存中源信息（位置不变）
    var updateSrcWithoutLoc = function(_sourceInfo, sourceInfo){
        var sources = dataCache.sources;

        for(var i=0; i<sources.length;i++){
            if(sources[i].uuid.toString() == sourceInfo._uuid.toString() && sources[i].z_index == sourceInfo.z_index && sources[i].type == sourceInfo._type){
                sources[i].uuid = _sourceInfo._uuid;
                sources[i].name = _sourceInfo._name;
                sources[i].type = _sourceInfo._type;
                sources[i].time = _sourceInfo._type;
                break;
            }
        }

        console.log(sources);
    };

    var updatePollingSrc = function(sourceInfo){
        var sources = dataCache.sources;

        for(var i=0; i<sources.length;i++){
            if(sources[i].z_index == sourceInfo.z_index && sources[i].type == sourceInfo._type){
                sources[i].uuid = sourceInfo._uuid;
                sources[i].time = sourceInfo._time;
                break;
            }
        }
    };

    //属性显示
    var showProperty = function($source){
        var sourceInfo = $source.data('info');

        if(sourceInfo._type == "device"){

        }else if(sourceInfo._type == "polling"){

        }
    };

    //更新轮询信息（选择源时uuid和time更新）
    var updatePollingInfo = function($pollingInfo){
        var pollingDevice = $pollingInfo.data('pollingmem');
        var pollingTime = $pollingInfo.data('timer');

        var $contentItem = $pollingInfo.parent();
        var $source = $contentItem.data('node');
        var sourceInfo = $source.data('info');

        sourceInfo._time = pollingTime;

        sourceInfo._uuid = [];

        for(var i=0;i<pollingDevice.length;i++){
            var uuid = pollingDevice[i];
            sourceInfo._uuid.push(uuid);
        }

        $source.data('info', sourceInfo);

        updatePollingSrc(sourceInfo);
    };

    //手动设置坐标改变某一个源的位置(return 坐标信息)
    var coordinateSetLoc = function($source, _x, _y, _w, _h){
        var _resolutionX = dataCache.resolutionX,
            _resolutionY = dataCache.resolutionY;

        var sourceInfo = $source.data('info');
        var $screen = $('#screen-wrapper').find('.screen');
        var screenW = $screen[0].offsetWidth,
            screenH = $screen[0].offsetHeight;
        var _realX = _x * _resolutionX.split('/')[0] / _resolutionX.split('/')[1],
            _realY = _y * _resolutionY.split('/')[0] / _resolutionY.split('/')[1],
            _realW = _w * _resolutionX.split('/')[0] / _resolutionX.split('/')[1],
            _realH = _h * _resolutionY.split('/')[0] / _resolutionY.split('/')[1];
        var returnParam = {
            x:null,
            y:null,
            w:null,
            h:null
        };

        var _sourceInfo = $.extend(true, {}, sourceInfo);

        //移动前源位置信息
        var _width = sourceInfo._width,
            _height = sourceInfo._height,
            _left = sourceInfo._relativeX,
            _top = sourceInfo._relativeY;

        returnParam.x = _left;
        returnParam.y = _top;
        returnParam.w = _width;
        returnParam.h = _height;

        //坐标位置超边界处理
        if((_realX+_realW) > screenW || (_realY+_realH) > screenH){
            mainFrame.info('超出边界范围！');
            return returnParam;
        }

        _sourceInfo._width = _realW;
        _sourceInfo._height = _realH;
        _sourceInfo._relativeX = _realX;
        _sourceInfo._relativeY = _realY;

        //真实源分辨率改变
        var _relutionX = (_left - _x)/ _width,
            _relutionY = (_top - _y)/ _height,
            _relutionW = (_width - _w)/ _width,
            _relutionH = (_height - _h)/ _height;

        _sourceInfo._cacheX = sourceInfo._cacheX - (_relutionX * sourceInfo._cacheW);
        _sourceInfo._cacheY = sourceInfo._cacheY - (_relutionY * sourceInfo._cacheH);
        _sourceInfo._cacheW = sourceInfo._cacheW * (1 - _relutionW);
        _sourceInfo._cacheH = sourceInfo._cacheH * (1 - _relutionH);

        $source.data('info', _sourceInfo);

        var arr = sourceBelongScreen($source);

        var flag = screenBindCache($source, arr);

        if (!flag) {

            $source.data('info', sourceInfo);
            return returnParam;
        }

        returnParam.x = _realX;
        returnParam.y = _realY;
        returnParam.w = _realW;
        returnParam.h = _realH;

        $source.css({
            'left':_realX,
            'top':_realY,
            'width':_realW +'px',
            'height':_realH +'px'
        });

        return returnParam;

    };

    //更新属性栏坐标值
    var updateCoordinate = function($source){
        var _resolutionX = dataCache.resolutionX,
            _resolutionY = dataCache.resolutionY;
        var $coordinateArea = $('.right').find('.top').find('.coordinateArea');
        var sourceInfo = $source.data('info');
        var $inputX = $coordinateArea.find('.screenPosX').find('.inputPosX'),
            $inputY = $coordinateArea.find('.screenPosY').find('.inputPosY'),
            $inputW = $coordinateArea.find('.screenWidth').find('.inputWidth'),
            $inputH = $coordinateArea.find('.screenHeight').find('.inputHeight');
        var sourceX = sourceInfo._relativeX * _resolutionX.split('/')[1] / _resolutionX.split('/')[0],
            sourceY = sourceInfo._relativeY * _resolutionY.split('/')[1] / _resolutionY.split('/')[0],
            sourceW = sourceInfo._width * _resolutionX.split('/')[1] / _resolutionX.split('/')[0],
            sourceH = sourceInfo._height * _resolutionY.split('/')[1]/ _resolutionY.split('/')[0];

        $inputX.val(sourceX);
        $inputY.val(sourceY);
        $inputW.val(sourceW);
        $inputH.val(sourceH);

    };

    /**************************/
    /***********事件************/
    /**************************/

    //点击返回
    $(document).off('click.back').on('click.back', '.backHomePage', backFunc);

    //选中节目源事件
    $(document).off('click.actualSourceItem').on('click.actualSourceItem', '.actualSourceItem, .contentListItem', sourceSelect);

    //点击菜单栏缩放事件
    $(document).unbind('click.scale.select.source').on('click.scale.select.source', '.scaleSource', sourceScale);

    //点击菜单栏裁剪事件
    $(document).off('click.cut.select.source').on('click.cut.select.source', '.clipSource', sourceCut);

    //内容栏删除事件
    $(document).off('click.delete.select.source').on('click.delete.select.source', '.trashIcon', sourceDelete);

    //锁定对应原
    $(document).off('click.locked.source').on('click.locked.source', '.unlocked-icon', sourceLocked);

    //解锁对应原
    $(document).off('click.unlocked.source').on('click.unlocked.source', '.locked-icon', sourceUnlocked);

    //隐藏对应原
    $(document).off('click.shown.source').on('click.shown.source', '.display-icon', sourceHidden);

    //显示对应原
    $(document).off('click.hidden.source').on('click.hidden.source', '.hidden-icon', sourceShown);

    //点击允许内容栏拖拽
    $(document).off('click.drag.content').on('click.drag.content', '.nodragIcon', dragContent);

    //点击禁止内容栏拖拽
    $(document).off('click.forbiddenDrag.content').on('click.forbiddenDrag.content', '.dragIcon', forbiddenDragContent);

    //点击保存配置
    $(document).off('click.saveConfig.source').on('click.saveConfig.source', '.saveSource', saveConfig);

    //点击上屏
    $(document).off('click.onScreen.source').on('click.onScreen.source', '.onscreenSource', startLargescreen);

    //点击下屏
    $(document).off('click.downScreen.source').on('click.downScreen.source', '.downscreenSource', stopLargescreen);

    //点击清空配置
    $(document).off('click.clearConfig.source').on('click.clearConfig.source', '.clearSource', clearConfig);

    //点击锁定布局
    $(document).off('click.lockedLayout.source').on('click.lockedLayout.source', '.lockSource', lockedSource);

    //点击解锁布局
    $(document).off('click.unlockedLayout.source').on('click.unlockedLayout.source', '.unlockSource', unlockedSource);

    //点击创建模板
    $(document).off('click.create.template').on('click.create.template', '.ls-config-commit-btn', createTemplate);

    //点击选择模板
    $(document).off('click.choose.template').on('click.choose.template', '.template', chooseTemplate);

    //点击取消模板
    $(document).off('click.cancel.template').on('click.cancel.template', '.cancelTemplate', cancelTemplate);

    //点击选择轮询成员
    $(document).off('click.choose.polling').on('click.choose.polling', '.pollingText', function(){
        gFn.addPollingDev($(this));
    });

    //点击选择混音列表
    $(document).off('click.choose.audio').on('click.choose.audio', '.setAudio', openAudio);

    //点击改变源的坐标值
    $(document).off('click.change.coordinate').on('click.change.coordinate', '.screenConfigBtn', changeCoordinateFunc);

    /**************************/
    /***********方法************/
    /**************************/

    //改变源坐标值方法
    function changeCoordinateFunc(){
        var $this = $(this);
        var $source = $('.sourceActive');

        var _x = $this.parent().find('.inputPosX').val(),
            _y = $this.parent().find('.inputPosY').val(),
            _w = $this.parent().find('.inputWidth').val(),
            _h = $this.parent().find('.inputHeight').val();

        //校验输入是否为数字
        if(isNaN(Number(_x))) return mainFrame.info('X输入不为数字！请重新输入');
        if(isNaN(Number(_y))) return mainFrame.info('Y输入不为数字！请重新输入');
        if(isNaN(Number(_w))) return mainFrame.info('W输入不为数字！请重新输入');
        if(isNaN(Number(_h))) return mainFrame.info('H输入不为数字！请重新输入');

        coordinateSetLoc($source, _x, _y, _w, _h);

    }

    //取消模板
    function cancelTemplate(){
        var $layout = $('.layout-wrapper');
        var $template = $('.template');
        var $templateList = $('.templateProperty');

        $templateList.hide();
        $layout.remove();
        $(this).hide();
        $template.removeClass('chosen');
    }

    //选择模板
    function chooseTemplate(){
        var $this = $(this),
            $template = $('.template'),
            $layout;

        //先清空配置
        var $content = $('.right').find('.bottom').find('.content').find('.contentList');
        var $contentItem = $content.find('.contentListItem');

        if($contentItem.length > 0){
            clearConfig();
        }

        cancelTemplate();

        templateBulid();

        $template.removeClass('chosen');
        $this.addClass('chosen');
        var templateInfo = $this.data('template');

        $layout = $('.layout-wrapper');

        //拿到宽高
        var layoutWidth = $layout[0].offsetWidth,
            layoutHeight = $layout[0].offsetHeight;

        $layout['layout-auto']('create', {
            cell: templateInfo.cell,
            editable: false,
            cellspan: templateInfo.cellspan,
            event: {
                drop:function(tdInfo, e){
                    var $cell = $(this);
                    var column = templateInfo.cell.column,
                        row = templateInfo.cell.row;
                    var _dragParam = e.dataTransfer.getData('dragdata');

                    var _name = _dragParam.split('@@')[3],
                        _uuid = _dragParam.split('@@')[8],
                        _type = _dragParam.split('@@')[0],
                        _layerId = _dragParam.split('@@')[7],
                        _channelId = _dragParam.split('@@')[10];

                    if(_type == 'device' || _type == 'polling'){
                        $cell['layout-auto']('setData', true);

                        //drop的单个td的信息
                        //var tdInfo = $cell['layout-auto']('getLayout');
                        console.log(tdInfo);

                        var sourceInfo = $cell.data('info');

                        if(sourceInfo != null){

                            //更新源、更新缓存、更新内容栏
                            var _sourceInfo = $.extend(true, {}, sourceInfo);

                            //更新源
                            _sourceInfo._name = _name;
                            _sourceInfo._uuid = _uuid + '@@' + _layerId + '@@' + _channelId;
                            $cell.data('info', _sourceInfo);
                            $cell.text(_name);

                            //更新缓存源信息（位置信息不变）
                            updateSrcWithoutLoc(_sourceInfo, sourceInfo);

                            //更新内容栏
                            var $content = $cell.data('node');
                            $content.find('span').eq(0).text(_name);

                        }else{

                            if(_type == 'polling') {
                                _uuid = [];
                                _name = "轮询屏";
                            }
                            if(_type == 'device') {
                                _uuid = _uuid + "@@" + _layerId + "@@" + _channelId;
                            }

                            //数据绑定
                            sourceInfo = {
                                '_type': _type,
                                '_width': tdInfo.w.split('/')[0] * layoutWidth / tdInfo.w.split('/')[1],
                                '_height': tdInfo.h.split('/')[0] * layoutHeight / tdInfo.h.split('/')[1],
                                'z_index': dataCache.z_index++,
                                '_relativeX': tdInfo.x * (layoutWidth / column),
                                '_relativeY': tdInfo.y * (layoutHeight / row),
                                '_uuid': _uuid,
                                '_name': _name,//节目名称
                                //存真实源信息（只有剪切时跟上面信息不一样）
                                '_cacheX': tdInfo.x * (layoutWidth / column),
                                '_cacheY': tdInfo.y * (layoutHeight / row),
                                '_cacheW': tdInfo.w.split('/')[0] * layoutWidth / tdInfo.w.split('/')[1],
                                '_cacheH': tdInfo.h.split('/')[0] * layoutHeight / tdInfo.h.split('/')[1]
                            };

                            $cell.data('info', sourceInfo);

                            //绑定缓存
                            var arr = sourceBelongScreen($cell);

                            var flag = screenBindCache($cell, arr);

                            if(!flag) return;

                            //创建内容栏
                            contentListBulid($cell, true);

                            //flag为false不走这里
                            $cell.text(_name);
                            $cell.css({
                                'background-color': 'rgb(22, 145, 184)',
                                'color': '#fff',
                                'font-size': '18px'
                            });
                        }

                    }else if(_type == 'template'){
                        return;
                    }
                }
            }
        });

    }

    //替换源信息（分为设备替换设备、设备替换轮询、轮询替换设备、轮询替换轮询）
    function replaceSourceInfo($source, _name, _type, _uuid){
        var sourceInfo = $source.data('info');
        var _sourceInfo = $.extend(true, {}, sourceInfo);

        if(_type == 'device' && sourceInfo._type == 'device'){

            //更新源
            _sourceInfo._name = _name;
            _sourceInfo._uuid = _uuid;
            $source.data('info', _sourceInfo);
            $source.text(_name);

            //更新缓存源信息（位置信息不变）
            updateSrcWithoutLoc(_sourceInfo, sourceInfo);

            //更新内容栏
            var $content = $source.data('node');
            $content.find('span').eq(0).text(_name);

        }else if(_type == 'device' && sourceInfo._type == 'polling'){

            //更新源
            _sourceInfo._name = _name;
            _sourceInfo._uuid = _uuid;
            _sourceInfo._type = _type;
            _sourceInfo._time = null;
            $source.data('info', _sourceInfo);
            $source.text(_name);

            //更新缓存源信息（位置信息不变）
            updateSrcWithoutLoc(_sourceInfo, sourceInfo);

            //更新内容栏
            var $content = $source.data('node');
            var $pollingTitle = $('<span class="pollingTitle" style="margin-left: 20px"></span>');
            $content.find('span').eq(0).text(_name);
            $content.removeClass('pollingSourceContent').addClass('vedioSourceContent');
            $content.find('span').eq(0).append($pollingTitle);

        }else if(_type == 'polling' && sourceInfo._type == 'device'){

            //更新源
            _sourceInfo._name = _name;
            _sourceInfo._uuid = _uuid;
            _sourceInfo._type = _type;
            $source.data('info', _sourceInfo);
            $source.text(_name);

            //更新缓存源信息（位置信息不变）
            updateSrcWithoutLoc(_sourceInfo, sourceInfo);

            //更新内容栏
            var $content = $source.data('node');
            $content.find('span').eq(0).text(_name);
            $content.removeClass('vedioSourceContent').addClass('pollingSourceContent');
            $content.find('.pollingTitle').remove();

        }else if(_type == 'polling' && sourceInfo._type == 'polling'){

            return;

        }
    }

    //创建模板
    function createTemplate(){
        var row = $('#row').val(),
            column = $('#column').val(),
            $layout = $('.layout-wrapper'),
            $template = $('.template');


        //先清空配置
        var $content = $('.right').find('.bottom').find('.content').find('.contentList');
        var $contentItem = $content.find('.contentListItem');

        if($contentItem.length > 0){
            clearConfig();
        }

        $layout.show();
        $layout.empty();
        $template.removeClass('chosen');

        //拿到宽高
        var layoutWidth = $layout[0].offsetWidth,
            layoutHeight = $layout[0].offsetHeight;

        $layout['layout-auto']('create', {
            cell:{
                column: column,
                row: row,
                style:'custom'
            },
            name:'split_'+row+'x'+column,
            editable:true,
            event:{
                drop:function(tdInfo, e){
                    var $cell = $(this);
                    var _dragParam = e.dataTransfer.getData('dragdata');

                    var _name = _dragParam.split('@@')[3],
                        _uuid = _dragParam.split('@@')[8],
                        _type = _dragParam.split('@@')[0],
                        _layerId = _dragParam.split('@@')[7],
                        _channelId = _dragParam.split('@@')[10];

                    if(_type == 'device' || _type == 'polling'){
                        $cell['layout-auto']('setData', true);

                        //drop的单个td的信息
                        //var tdInfo = $cell['layout-auto']('getLayout');
                        console.log(tdInfo);

                        var sourceInfo = $cell.data('info');

                        if(sourceInfo != null){

                            //更新源、更新缓存、更新内容栏
                            var _sourceInfo = $.extend(true, {}, sourceInfo);

                            //更新源
                            _sourceInfo._name = _name;
                            _sourceInfo._uuid = _uuid + '@@' + _layerId + '@@' + _channelId;
                            $cell.data('info', _sourceInfo);
                            $cell.text(_name);

                            //更新缓存源信息（位置信息不变）
                            updateSrcWithoutLoc(_sourceInfo, sourceInfo);

                            //更新内容栏
                            var $content = $cell.data('node');
                            $content.find('span').eq(0).text(_name);

                        }else{

                            if(_type == 'polling') {
                                _uuid = [];
                                _name = "轮询屏";
                            }
                            if(_type == 'device') {
                                _uuid = _uuid + "@@" + _layerId + "@@" +_channelId;
                            }

                            //数据绑定
                            sourceInfo = {
                                '_type': _type,
                                '_width': tdInfo.w.split('/')[0] * layoutWidth / tdInfo.w.split('/')[1],
                                '_height': tdInfo.h.split('/')[0] * layoutHeight / tdInfo.h.split('/')[1],
                                'z_index': dataCache.z_index++,
                                '_relativeX': tdInfo.x * (layoutWidth / column),
                                '_relativeY': tdInfo.y * (layoutHeight / row),
                                '_uuid': _uuid,
                                '_name': _name,//节目名称
                                //存真实源信息（只有剪切时跟上面信息不一样）
                                '_cacheX': tdInfo.x * (layoutWidth / column),
                                '_cacheY': tdInfo.y * (layoutHeight / row),
                                '_cacheW': tdInfo.w.split('/')[0] * layoutWidth / tdInfo.w.split('/')[1],
                                '_cacheH': tdInfo.h.split('/')[0] * layoutHeight / tdInfo.h.split('/')[1]
                            };

                            $cell.data('info', sourceInfo);

                            //绑定缓存
                            var arr = sourceBelongScreen($cell);

                            var flag = screenBindCache($cell, arr);

                            if(!flag) {
                                $cell.removeData('info');
                                return;
                            }

                            //创建内容栏
                            contentListBulid($cell, true);

                            //flag为false不走这里
                            $cell.text(_name);
                            $cell.css({
                                'background-color': 'rgb(22, 145, 184)',
                                'color': '#fff',
                                'font-size': '18px'
                            });
                        }

                    }else if(_type == 'template'){
                        return;
                    }
                },
                cellspan:function(l){
                    console.log(l);

                    //更新（修改）缓存中源信息
                    if(l.data){
                        var $cell = $(this);
                        var _sourceInfo = $cell.data('info'),
                            _newSourceInfo = $.extend(true, {}, _sourceInfo);

                        _newSourceInfo._width = l.w.split('/')[0] * layoutWidth / l.w.split('/')[1];
                        _newSourceInfo._height = l.h.split('/')[0] * layoutHeight / l.h.split('/')[1];
                        _newSourceInfo._relativeX = l.x * (layoutWidth / column);
                        _newSourceInfo._relativeY = l.y * (layoutHeight / row);
                        //存真实源信息（只有剪切时跟上面信息不一样）
                        _newSourceInfo._cacheX = l.x * (layoutWidth / column);
                        _newSourceInfo._cacheY = l.y * (layoutHeight / row);
                        _newSourceInfo._cacheW = l.w.split('/')[0] * layoutWidth / l.w.split('/')[1];
                        _newSourceInfo._cacheH = l.h.split('/')[0] * layoutHeight / l.h.split('/')[1];

                        $cell.data('info', _newSourceInfo);

                        //cache信息更新
                        var arr = sourceBelongScreen($cell);

                        var flag = screenBindCache($cell, arr);
                    }
                }
            }
        });
    }

    //返回
    function backFunc(){
        window.history.back();
    }

    //清空配置方法
    function clearConfig(){
        var $screen = $('.middle').find('.screen'),
            $content = $('.right').find('.bottom').find('.content').find('.contentList');

        var $contentItem = $content.find('.contentListItem');

        //清空配置按钮和事件处理
        if($contentItem.length > 0){
            $('.contentBottom').find('.dragIcon').hide();
            $('.contentBottom').find('.nodragIcon').show();
            $content.removeClass('contentDrag');
            $content.sortable('destroy');
        }else{
            mainFrame.info("当前屏幕无配置！");
        }

        $contentItem.each(function(){
            $this = $(this);
            $source = $this.data('node');

            sourceRemove($source);
        });
    }

    //上屏
    function startLargescreen(){
        var $this = $(this);
        var confId = dataCache.confId;
        var $screenWrapper = $('.full-screen-container').find('.middle');

        gFn.startLargescreen(confId, function(){
            $screenWrapper.addClass('viewing');
        });
    }

    //下屏
    function stopLargescreen(){
        var $this = $(this);
        var confId = dataCache.confId;
        var $screenWrapper = $('.full-screen-container').find('.middle');

        gFn.stopLargescreen(confId, function(){
            $screenWrapper.removeClass('viewing');
        });
    }

    //保存配置方法
    function saveConfig(){
        var sources = dataCache.sources;

        var tasks = generateTasks(sources),
            lgScreenId = dataCache.lgScreenId,
            confId = dataCache.confId;

        console.log(tasks);

        gFn.saveScreenConfig(lgScreenId, confId, tasks, function(){});

    }

    //打开音频设置
    function openAudio(){
        var lgScreenId = dataCache.lgScreenId,
            confId = dataCache.confId;

        var $screenWrapper = $('.full-screen-container').find('.middle');
        var viewFlag = false;
        if($screenWrapper.hasClass('viewing')) viewFlag = true;

        if(viewFlag){
            mainFrame.info('请下屏后再点击设置音频');
        }else{
            gFn.addAudioDev(lgScreenId,confId);
        }
    }

    //节目源选中方法
    function sourceSelect(){
        var $this = $(this);
        var $sourceItem = null;
        var $sourceList = null;
        var $cutting = $('.screen').find('.cutting'),
            $scaling = $('#screen-wrapper').find('.scaling'),
            $contentList = $('.right').find('.bottom').find('.content').find('.contentList');

        var $device = $('.deviceProperty');
        var $polling = $('.pollingText');

        //裁剪和缩放时点击节目源不会选中
        if($cutting.length <= 0 && $scaling.length <= 0){
            if($this.is('.contentListItem') && !$contentList.hasClass('contentDrag')){
                $sourceItem = $this;
                $sourceList = $sourceItem.data('node');
            }else if($this.is('.actualSourceItem')){
                $sourceList = $this;
                $sourceItem = $sourceList.data('node');
            }else{
                return;
            }

            $('.contentList .contentListItem').removeClass('contentActive');
            $('.screen .actualSourceItem').removeClass('sourceActive');
            $('.screen .cell').removeClass('sourceActive');

            $sourceList.addClass('sourceActive');
            $sourceItem.addClass('contentActive');

            $polling.parent().hide();

            if($sourceItem.hasClass('pollingSourceContent')){
                $device.show();
                $device.find('input').removeAttr('disabled');
                //坐标值更新
                updateCoordinate($sourceList);

                $polling.parent().show();
                $polling.data('src', $sourceItem);
            }else if($sourceItem.hasClass('vedioSourceContent')){
                $device.show();
                $device.find('input').removeAttr('disabled');
                //坐标值更新
                updateCoordinate($sourceList);
            }

            if($sourceList.hasClass('cell')){
                $device.find('.screenConfigBtn').hide();
                $device.find('input').attr('disabled', 'disabled');
            }else{
                $device.find('.screenConfigBtn').show();
                $device.find('input').removeAttr('disabled');
            }
        }
    }

    //节目源裁剪方法
    function sourceCut() {
        var $source = $('.screen').find('.sourceActive'),
            $content = $source.data('node');
        var $cutting = $('.screen').find('.cutting');
        var $scaling = $('.scaling');

        if ($source.length <= 0) {
            mainFrame.info("请先选中要裁剪的屏幕！");
            return
        }

        if($cutting.length > 0){
            mainFrame.info("裁剪屏幕已存在，请操作！");
            return;
        }

        if($scaling.length > 0){
            mainFrame.info("缩放屏幕已存在，请先缩放！");
            return;
        }

        if($source.hasClass('sourceHidden')){
            mainFrame.info("该源已被隐藏！");
            return;
        }

        if($source.hasClass('sourceLocked')){
            mainFrame.info("该源已被锁定！");
            return;
        }

        var _left = parseInt($source.css('left')),
            _top = parseInt($source.css('top')),
            _width = parseInt($source.css('width')),
            _height = parseInt($source.css('height')),
            _z_index = parseInt($source.css('z-index')),
            $clone = $source.clone(true);

        //Jcrop插件
        var jcrop_api_cut;
        $('.sourceActive').Jcrop({
            addClass: 'cutting',
            //设置默认选框
            setSelect: [_width/4, _height/4, _width*3/4, _height*3/4],
            //双击进行裁剪
            onDblClick: function () {
                //裁剪部分相对于裁剪前的位置信息
                var chipInfo = jcrop_api_cut.tellSelect(),
                    _chipX = chipInfo.x,
                    _chipY = chipInfo.y,
                    _chipW = chipInfo.w,
                    _chipH = chipInfo.h;

                $('.cutting').before($clone);
                $clone.data('node', $content);
                $content.data('node', $clone);

                //源信息绑定更新(缓存)
                var sourceInfo = $clone.data('info'),
                    _sourceInfo = $.extend(true, {}, sourceInfo);

                _sourceInfo._relativeX = _left+_chipX;
                _sourceInfo._relativeY = _top+_chipY;
                _sourceInfo._width = _chipW;
                _sourceInfo._height = _chipH;

                $clone.data('info', _sourceInfo);

                //裁剪撤销
                jcrop_api_cut.destroy();

                $clone.css({
                    'width':_chipW+'px',
                    'height':_chipH+'px',
                    'left':(_left+_chipX)+'px',
                    'top':(_top+_chipY)+'px',
                    'z-index':_z_index
                });

                //cache信息更新
                var arr = sourceBelongScreen($clone);

                var flag = screenBindCache($clone, arr);

                if(!flag){
                    $clone.css({
                        'left':_left,
                        'top':_top,
                        'width':_width+'px',
                        'height':_height+'px'
                    });

                    $clone.data('info', sourceInfo);

                }

                moveFunc($clone);
            },
            //取消裁剪
            onRelease: function(){
                $('.cutting').before($clone);
                $clone.data('node', $content);
                $content.data('node', $clone);
                jcrop_api_cut.destroy();
                moveFunc($clone);
            }
        }, function() {
            jcrop_api_cut = this;
            $('.screen').find('.cutting').css({
                'position':'absolute',
                'left':_left+'px',
                'top':_top+'px'
            });

            $('.screen').find('.sourceActive').css({'border':'2px black solid'});
        });
    }

    //节目源缩放方法
    function sourceScale(){
        var $screen = $('.middle').find('.screen'),
            $source = $('.sourceActive'),
            $cutting = $screen.find('.cutting'),
            $scaling = $('.scaling');

        if ($source.length <= 0) {
            mainFrame.info("请先选中要缩放的屏幕");
            return
        }

        if($cutting.length > 0){
            mainFrame.info("裁剪屏幕已存在，请先裁剪");
            return;
        }

        if($scaling.length > 0){
            mainFrame.info("缩放屏幕已存在，请操作");
            return;
        }

        if($source.hasClass('sourceHidden')){
            mainFrame.info("该源已被隐藏！");
            return;
        }

        if($source.hasClass('sourceLocked')){
            mainFrame.info("该源已被锁定！");
            return;
        }

        var _left = parseInt($source.css('left')),
            _top = parseInt($source.css('top')),
            _width = parseInt($source.css('width')),
            _height = parseInt($source.css('height')),
            _z_index = parseInt($source.css('z-index')),
            $clone = $screen.clone(true);

        var jcrop_api_scale;
        $screen.Jcrop({
            addClass: 'scaling',
            //选框改变进行缩放
            onChange: function(){
                var scaleInfo = jcrop_api_scale.tellSelect(),
                    _chipX = scaleInfo.x,
                    _chipY = scaleInfo.y,
                    _chipW = scaleInfo.w,
                    _chipH = scaleInfo.h;

                $source.css({
                    'left':_chipX,
                    'top':_chipY,
                    'width':_chipW+'px',
                    'height':_chipH+'px'
                });
            },
            //双击选定缩放
            onDblClick: function () {
                //缩放部分相对于裁剪前的位置信息
                var chipInfo = jcrop_api_scale.tellSelect(),
                    _chipX = chipInfo.x,
                    _chipY = chipInfo.y,
                    _chipW = chipInfo.w,
                    _chipH = chipInfo.h;

                var $tbody = $screen.find('tbody');
                var tbodyW = $tbody[0].offsetWidth,
                    tbodyH = $tbody[0].offsetHeight;
                var _dvtW,_dvtH;

                _dvtW = tbodyW > _chipW ? _chipW: tbodyW;
                _dvtH = tbodyH > _chipH ? _chipH: tbodyH;

                var _relutionX = (_left - _chipX)/ _width,
                    _relutionY = (_top - _chipY)/ _height,
                    _relutionW = (_width - _dvtW)/ _width,
                    _relutionH = (_height - _dvtH)/ _height;

                $('.scaling').before($clone);

                //缩放撤销
                jcrop_api_scale.destroy();

                var $newSource = $clone.find('.sourceActive');

                $newSource.css({
                    'left':_chipX,
                    'top':_chipY,
                    'width':_dvtW+'px',
                    'height':_dvtH+'px'
                });

                //源信息绑定更新(缓存)
                var sourceInfo = $newSource.data('info');
                var _sourceInfo = $.extend(true, {}, sourceInfo);

                _sourceInfo._width = _dvtW;
                _sourceInfo._height = _dvtH;
                _sourceInfo._relativeX = _chipX;
                _sourceInfo._relativeY = _chipY;
                _sourceInfo._cacheX = sourceInfo._cacheX - (_relutionX * sourceInfo._cacheW);
                _sourceInfo._cacheY = sourceInfo._cacheY - (_relutionY * sourceInfo._cacheH);
                _sourceInfo._cacheW = sourceInfo._cacheW * (1 - _relutionW);
                _sourceInfo._cacheH = sourceInfo._cacheH * (1 - _relutionH);

                $newSource.data('info', _sourceInfo);

                //cache信息更新
                var arr = sourceBelongScreen($newSource);

                var flag = screenBindCache($newSource, arr);

                if(!flag){
                    $newSource.css({
                        'left':_left,
                        'top':_top,
                        'width':_width+'px',
                        'height':_height+'px'
                    });

                    $newSource.data('info', sourceInfo);

                }

                $clone.find('.actualSourceItem').each(function(){
                    var $this = $(this);
                    var $content = $this.data('node');
                    $content.data('node', $this);
                    moveFunc($this);
                });
            },
            //取消缩放
            onRelease: function(){
                $('.scaling').before($clone);
                jcrop_api_scale.destroy();

                $clone.find('.actualSourceItem').each(function(){
                    var $this = $(this);
                    var $content = $this.data('node');
                    $content.data('node', $this);
                    moveFunc($this);
                });
            }
        }, function() {
            jcrop_api_scale = this;
        });

        //设置默认选框
        jcrop_api_scale.setSelect([_left, _top, _left+_width, _top+_height]);

    }

    //节目源删除方法
    function sourceDelete(){
        var $cutting = $('.screen').find('.cutting'),
            $scaling = $('#screen-wrapper').find('.scaling'),
            $content = $('.right').find('.bottom').find('.content').find('.contentList');

        if($cutting.length <= 0 && $scaling.length <= 0){
            sourceRemove($('.sourceActive'));
        }

        var $contentItem = $content.find('.contentListItem');

        //源删完之后按钮和事件处理
        if($contentItem.length <= 0){
            $('.contentBottom').find('.dragIcon').hide();
            $('.contentBottom').find('.nodragIcon').show();
            $content.removeClass('contentDrag');
            $content.sortable('destroy');
        }
    }

    //删除源方法--删除源、删除缓存、删除列表
    function sourceRemove($source){
        var sourceInfo = $source.data('info'),
            $contentItem = $source.data('node'),
            sourceCacheInfo = dataCache.sources;

        var $device = $('.deviceProperty');

        var $pollingText = $('.pollingText');

        if($source.length == 0) return;

        //隐藏坐标栏
        if(!$device.is(':hidden')){
            $device.hide();
        }

        //清缓存
        for(var i=0;i<sourceCacheInfo.length;i++){
            if(sourceCacheInfo[i].uuid.toString() == sourceInfo._uuid.toString() && sourceCacheInfo[i].type == sourceInfo._type && sourceCacheInfo[i].z_index == sourceInfo.z_index){

                var screens = sourceCacheInfo[i].screens;
                for(var j=0;j<screens.length;j++){
                    var objScreen = screens[j].objScreen;
                    var useNo = screens[j].useDno;
                    var dnos = objScreen.dnos;

                    for(var k=0;k<dnos.length;k++){
                        if(dnos[k].code == useNo){
                            dnos[k].status = 0;
                            break;
                        }
                    }
                }

                //解码能力释放
                sourceCacheInfo.splice(i,1);
            }
        }

        //清页面
        if($contentItem.hasClass('pollingSourceContent')){
            $pollingText.parent().hide();
        }

        if($source.is('div')){
            $source.remove();
        }else if($source.is('td')){
            $source.empty();
            $source.removeClass('sourceActive');
            $source.removeData('info');
            $source.removeAttr('style');
        }

        $contentItem.remove();
    }

    //锁定源方法
    function sourceLocked(){

        var $this = $(this),
            $actualSourceItem = $this.parent().data('node');
        $content = $('.right').find('.bottom').find('.content').find('.contentList');

        if(!$content.hasClass('contentDrag')){
            $this.hide();
            $this.parent().find('.locked-icon').show();
            $actualSourceItem.addClass('sourceLocked');

            //禁用拖拽
            $actualSourceItem.myDrag('stopMove');
        }

    }

    //解锁源方法
    function sourceUnlocked(){

        var $this = $(this),
            $actualSourceItem = $this.parent().data('node');
        $content = $('.right').find('.bottom').find('.content').find('.contentList');

        if(!$content.hasClass('contentDrag')){
            $this.hide();
            $this.parent().find('.unlocked-icon').show();
            $actualSourceItem.removeClass('sourceLocked');

            //恢复拖拽
            $actualSourceItem.myDrag('revertMove');
        }

    }

    //隐藏源方法
    function sourceHidden(){

        var $this = $(this),
            $actualSourceItem = $this.parent().data('node');
        $content = $('.right').find('.bottom').find('.content').find('.contentList');

        if(!$content.hasClass('contentDrag')){
            $this.hide();
            $this.parent().find('.hidden-icon').show();
            $actualSourceItem.addClass('sourceHidden');
        }

    }

    //显示源方法
    function sourceShown(){

        var $this = $(this),
            $actualSourceItem = $this.parent().data('node');
        $content = $('.right').find('.bottom').find('.content').find('.contentList');

        if(!$content.hasClass('contentDrag')){

            $this.hide();
            $this.parent().find('.display-icon').show();
            $actualSourceItem.removeClass('sourceHidden');
        }

    }

    //点击允许内容栏拖拽
    function dragContent(){

        var $content = $('.right').find('.bottom').find('.content').find('.contentList');

        if($content.length>0){
            $(this).hide();
            $(this).parent().find('.dragIcon').show();
            $content.addClass('contentDrag');

            $content.sortable({
                onDrop:function ($item, container, _super, event) {
                    $item.removeClass(container.group.options.draggedClass).removeAttr("style");
                    $("body").removeClass(container.group.options.bodyClass);
                    contentListManage($content);
                }
            });
        }

    }

    //点击禁止内容栏拖拽
    function forbiddenDragContent(){

        var $content = $('.right').find('.bottom').find('.content').find('.contentList');

        $(this).hide();
        $(this).parent().find('.nodragIcon').show();
        $content.removeClass('contentDrag');

        $content.sortable('destroy');
    }

    //定义z-index标识，为了防止每次图层管理出现相同的z_index
    var z_indexFlag = true;
    //图层管理
    function contentListManage($contentList){
        //z_index起始数
        var z_indexNum;
        if(z_indexFlag){
            z_indexNum = 100;
            z_indexFlag = false;
        }else{
            z_indexNum = 200;
            z_indexFlag = true;
        }

        var $contentListItem = $contentList.find('.contentListItem'),
            length = $contentListItem.length,
            sources = dataCache.sources;

        for(var i=0;i<length;i++){
            var $contentItem = $contentListItem.eq(i);
            $source = $contentItem.data('node');
            sourceInfo = $source.data('info');

            //修改index
            $source.css('z-index', z_indexNum-i);

            //修改缓存数据绑定--z_index
            for(var j=0;j<sources.length;j++){
                if(sources[j].uuid.toString() == sourceInfo._uuid.toString() && sources[j].type == sourceInfo._type && sources[j].z_index == sourceInfo.z_index){
                    sources[j].z_index = z_indexNum-i;
                    sourceInfo.z_index = z_indexNum-i;
                    $source.data('info', sourceInfo);
                }
            }
        }
    }

    //锁定布局
    function lockedSource(){
        var $screen = $('#screen-wrapper').find('.screen');
        var $contentListItem = $('.contentListItem');

        $screen.addClass('screenLocked');
        $(this).hide();
        $('.unlockSource').show();

        $contentListItem.each(function(){
            var $this = $(this),
                $actualSourceItem = $this.data('node');

            if(!$actualSourceItem.hasClass('sourceLocked')){
                $this.find('.unlocked-icon').hide();
                $this.find('.locked-icon').show();
                $actualSourceItem.addClass('sourceLocked');

                //禁用拖拽
                $actualSourceItem.myDrag('stopMove');
            }
        });
    }

    //解锁布局
    function unlockedSource(){
        var $screen = $('#screen-wrapper').find('.screen');
        var $contentListItem = $('.contentListItem');

        $screen.removeClass('screenLocked');
        $(this).hide();
        $('.lockSource').show();

        $contentListItem.each(function(){
            var $this = $(this),
                $actualSourceItem = $this.data('node');

            if($actualSourceItem.hasClass('sourceLocked')){
                $this.find('.locked-icon').hide();
                $this.find('.unlocked-icon').show();
                $actualSourceItem.removeClass('sourceLocked');

                //恢复拖拽
                $actualSourceItem.myDrag('revertMove');
            }
        });
    }

    return {
        initData:initData,
        dataCache:dataCache,
        updatePollingInfo:updatePollingInfo
    };

}();