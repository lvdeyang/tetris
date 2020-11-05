console.log("this is gFn.js for largeScreenConf");
var gFn = {
	
	//获取物理屏信息
	queryScreens:function(lgScreenId, success){
		$.post('/largescreenControlController/getLargescreenRelationPhyscreen', {
			devId:lgScreenId
		}, function(data, status){
			if(status === 'success'){
				var _msg = data.msg;
	            if(_msg!==null && typeof _msg!=='undefined' && _msg!==''){
	                mainFrame.info(_msg);
	            }else{
	            	
	            	//设置大屏元数据
	            	var metadata = {
	            		rows:parseInt(data.largescreenInfo.rows),
	            		columns:parseInt(data.largescreenInfo.columns),
	            		width:parseFloat(data.largescreenInfo.physcreens[0].width),
	            		height:parseFloat(data.largescreenInfo.physcreens[0].height)
	            	};
	            	
	            	//维护内存数据
	            	gFn.cacheScreens(data.largescreenInfo.physcreens);
	            	
	            	if(typeof success === 'function') success(metadata);
	            	
	            }
			}else{
				mainFrame.error('未知程序异常！');
			}
		});
	},

	//保存大屏配置
	saveScreenConfig: function(lgScreenId, confId, tasks, success){

		$.post('/largescreenControlController/saveLargescreenConfigList', {
			largescreenConfigId: confId,
			task: $.toJSON(tasks)
		}, function(data, status){
			if(status === 'success'){
				var _msg = data.msg;
				if(_msg!==null && typeof _msg!=='undefined' && _msg!==''){
					mainFrame.info(_msg);
				}else{
					mainFrame.info('保存配置成功！');
				}
			}else{
				mainFrame.error('未知程序异常！');
			}
		});

	},

	//上屏
	startLargescreen: function(confId, success){

		$.post('/largescreenControlController/startLargescreenControl', {
			largescreenConfigId: confId
		}, function(data, status){
			if(status === 'success'){
				var _msg = data.msg;
				if(_msg!==null && typeof _msg!=='undefined' && _msg!==''){
					mainFrame.info(_msg);
				}else{
					mainFrame.info('上屏！');
					if(typeof success === 'function') success();
				}
			}else{
				mainFrame.error('未知程序异常！');
			}
		});

	},

	//下屏
	stopLargescreen: function(confId, success){

		$.post('/largescreenControlController/stopLargescreenControl', {
			largescreenConfigId: confId
		}, function(data, status){
			if(status === 'success'){
				var _msg = data.msg;
				if(_msg!==null && typeof _msg!=='undefined' && _msg!==''){
					mainFrame.info(_msg);
				}else{
					mainFrame.info('下屏！');
					if(typeof success === 'function') success();
				}
			}else{
				mainFrame.error('未知程序异常！');
			}
		});

	},
	
	//转换物理屏数据存于内存--详细数据结构请看文档
	cacheScreens:function(dataScreens){
		if(!dataScreens) return;
		var screens = largeScreenConf.dataCache.screens;
		for(var i=0; i<dataScreens.length; i++){
			var dataScreen = dataScreens[i];
			var screen = {
				$dom:null,
				name:dataScreen.name,
				serialNum:dataScreen.serialNum,
			    jv230Uuid:dataScreen.jv230Uuid,
			    ablity:dataScreen.abilityId,
			    width:dataScreen.width,
			    height:dataScreen.height,
			    dnos:[],
				audio:{
					ip:dataScreen.ip,
					port:dataScreen.port
				},
			    sources:[]
			};
			var decodeList = dataScreen.decodeList;
			for(var j=0; j<decodeList.length; j++){
				screen.dnos.push({
					code:decodeList[j].decodeNum,
					status:0
				});
			}
			screens.push(screen);
		}
	},
	
	//获取配置信息
	querySources:function(confId, success){
		$.post('/largescreenControlController/getLargescreenConfigListInfo', {
			largescreenConfigId:confId
		}, function(data, status){
			if(status === 'success'){
				var _msg = data.msg;
	            if(_msg!==null && typeof _msg!=='undefined' && _msg!==''){
	                mainFrame.info(_msg);
	            }else{

	            	//维护内存数据
	            	gFn.cacheSources(data.largescreenConfigInfo.configTaskVoList);
	            	
	            	if(typeof success === 'function') success();
	            	
	            }
			}else{
				mainFrame.error('未知程序异常！');
			}
		});
	},
	
	//转换配置信息存于内存--详细数据结构请看文档
	cacheSources:function(dataSources){
		if(!dataSources) return;
		var sources = largeScreenConf.dataCache.sources;
		var screens = largeScreenConf.dataCache.screens;
		for(var i=0; i<dataSources.length; i++){
			var dataSource = dataSources[i];
			if(dataSource.type == 'audio'){
				var audioInfoList = JSON.parse(dataSource.content).audioList,
					volume = JSON.parse(dataSource.content).volume;

				$('.setAudio').data('audioDev', audioInfoList);
				$('.setAudio').data('audioVolume', volume);

			}else if(dataSource.type == 'device' || dataSource.type == 'polling'){
				var source = {
					$dom:null,
					uuid:JSON.parse(dataSource.content).src.uuid,
					name:JSON.parse(dataSource.content).src.name,
					z_index:JSON.parse(dataSource.configLocationList[0].location).relation[0].dst.zindex,
					type:dataSource.type,
					time:dataSource.time,
					screens:[]
				};

				//添加关联物理屏
				var configLocationList = dataSource.configLocationList;
				for(var j=0;j<configLocationList.length;j++){
					var relation = JSON.parse(configLocationList[j].location).relation;
					//}
					//var relation = JSON.parse(dataSource.configLocationList[0].location).relation;
					//for(var j=0; j<relation.length; j++){
					var objScreen = null;
					for(var k=0; k<screens.length; k++){
						//jv230uuid 与ability唯一确定一块屏幕
						if(relation[0].dst.uuid===screens[k].jv230Uuid){
							objScreen = screens[k];
							break;
						}
					}

					//占用物理屏解码
					for(var k=0; k<objScreen.dnos.length; k++){
						if(objScreen.dnos[k].code == relation[0].dst.dno){
							objScreen.dnos[k].status = 1;
							break;
						}
					}

					var linkScreen = {
						objScreen:objScreen,
						useDno:relation[0].dst.dno,
						screenLocations:{
							x:relation[0].dst.x,
							y:relation[0].dst.y,
							w:relation[0].dst.w,
							h:relation[0].dst.h
						},
						sourceLocations:{
							x:relation[0].src.x,
							y:relation[0].src.y,
							w:relation[0].src.w,
							h:relation[0].src.h
						}
					};

					//双向数据关联
					source.screens.push(linkScreen);
					//objScreen.sources.push(source);

				}
				//加入缓存
				sources.push(source);
			}
		}
	},
	
	//获取设备列表
    queryDevList:function(){
        var $sourceList = $('#source-list');
        var _initParam = {
                openLevel:2
            };
        $.post('/largescreenControlController/getEncodeBelongInst', _initParam, function (data, status) {
            if (status === 'success') {
                var _msg = data.msg;
                if (_msg !== null && typeof _msg !== 'undefined' && _msg !== '') {
                    mainFrame.info(_msg);
                } else {
                	
                	//插入三个节点
                	var device_data = data.node.childrenList[0];

					data.node.childrenList[0] = {
						checkable:false,
						draggable:true,
						nodeContent:'新建模板',
						param:"template@@0@@模板",
						type:"file"
					};

                	data.node.childrenList[1] = {
                		checkable:false,
                		draggable:true,
                		nodeContent:'轮询',
                		param:"polling@@1@@轮询",
                		type:"file"
                	};
                	
                	data.node.childrenList[2] = {
            			checkable:false,
                		draggable:true,
                		nodeContent:'字幕',
                		param:"subtitles@@2@@字幕",
                		type:"file"	
                	};
                	
                	data.node.childrenList[3] = {
            			checkable:false,
                		draggable:true,
                		nodeContent:'图片',
                		param:"picture@@3@@图片",
                		type:"file"		
                	};
                	
                	data.node.childrenList[4] = device_data;
                	
                	$sourceList.tree('create', {
                        data:[data.node],
                        event:{
                            click:function(param, complete){
                                //禁止点击效果
                                complete();
                            },
                            open:function(param, complete){
                                var $node = $(this);
                                if($node.tree('is', 'empty')){
                                    var reqParam = {};
                                    if(param !== 'root') {
                                        reqParam.instId = param.split('@@')[1];
                                    }
                                    $.post('/largescreenControlController/getEncodeBelongInst', reqParam, function(data,status){
                                        var _msg = data.msg;
                                        if(_msg!==null && typeof _msg!=='undefined' && _msg!==''){
                                            mainFrame.info(_msg);
                                        }else{
                                            var nodeList = data.nodeList;
                                            for(var i=0; i<nodeList.length; i++){
                                                $node.tree('append', nodeList[i]);
                                            }
                                            if($node.tree('is', 'checked')){
                                                $node.tree('uncheck', 'children');
                                                $node.tree('check', 'children');
                                            }

                                            complete();
                                        }
                                    });
                                }else{
                                    complete();
                                }
                            },
							drag:function(param, event){
								event.dataTransfer.setData('dragdata', param);
							}

                        }
                    });
                }
            } else {
                mainFrame.error("未知程序异常");
            }
        });
    },

	//添加轮询设备
	addPollingDev:function($this){
		if($this.css('cursor') == 'pointer'){
			var $windowContent = $('<div class="windowContent width_100 height_100"></div>'),
				$form = $('<form class="zkForm counter_v"></form>'),
				$formRight = $('<span class="formCol"></span>'),
				$scrollerContainer = $('<div class="scrollerContainer width_100 counter_v"></div>'),
				$left = $('<div class="left width_46 height_100 float_l"></div>'),
				$middle = $('<div class="middle width_8 height_100 float_l"></div>'),
				$right = $('<div class="right width_46 height_100 float_l"></div>'),
				$selectList = $('<ul class="list-group selectList"></ul>'),
				$list,
				$parent = $this.data('src'),
				$screenTitle =  $parent.find('.pollingTitle'),
				pollingInfo =  $screenTitle.data('pollingmem');
			var pollingtime = $screenTitle.data('timer'),
				$sessionParams,_memberList=[], timeOnList = [30,25,20,15,10,5], time, num;
			switch(parseInt(pollingtime)){//轮询时间显示为上一次更改
				case 25:
					num = '1';
					break;
				case 20:
					num = '2';
					break;
				case 15:
					num = '3';
					break;
				case 10:
					num = '4';
					break;
				case 5:
					num = '5';
					break;
				default:
					num = '0';
			}
			timeOnList['selected'] = num;
			time = num;
			$sessionParams = mainFrame.getFormRow({
				'label':'轮询间隔（秒）',
				'formList':[
					mainFrame.getSelect({
						id:'iSessionParam',
						codeList:timeOnList,
						onChange:function(value){
							time = value;
						}
					})
				]
			});
			$form.append($formRight.append($sessionParams));
			gC.popWindow.id = 'zk_Window_default';
			gC.popWindow.onClose = function(){
				gC.popWindow.onClose = null;
			};
			gC.popWindow.size = 'window_md';
			gC.popWindow.title = '选择轮询设备';
			gC.popWindow.content = $windowContent;
			gC.popWindow.commitBtn.text = '确定';
			gC.popWindow.commitBtn.callback = function(event,btn,able){
				//表单验证
				if(_memberList.length <= 0){
					mainFrame.info('您还没有选择轮询设备');
					able(btn);
					return;
				}
				var _text = '已选择设备（'+_memberList.length+'）';
				$screenTitle.empty().append(_text);
				$screenTitle.css('color','#fff');
				$parent.find('.screenText').css('color','#fff');
				$screenTitle.removeData('pollingmem');
				$screenTitle.removeData('uuid');
				$screenTitle.removeData('dstType');
				$screenTitle.data('pollingmem',_memberList);//将轮询设备信息保存到屏幕标题栏中
				//添加设备后显示轮询按钮
				//$parent.find('.startOrStopIcon').empty().append('<img src="'+_basePath+'images/webVideoConference/conferenceOperation/startPolling.png">');

				var timer = timeOnList[time];//轮询时间
				if(typeof timer == 'undefined')timer = 30;
				$screenTitle.removeData('timer');
				$screenTitle.data('timer',timer);

				// gFn.pollingSplitStatusSetting();//设置轮询状态
				zk_Component.closeWindow();

				largeScreenConf.updatePollingInfo($screenTitle);
			};
			var $leftContent, $rightContent;
			zk_Component.popWindow(gC.popWindow);
			$windowContent.append($form)
				.append($scrollerContainer.append($left)
					.append($middle)
					.append($right));
			mainFrame.initDynamicHeight($windowContent, 'scrollerContainer');

			mainFrame.addScroll($left, 'xy', 'dark-3');
			mainFrame.addScroll($right, 'y', 'dark-3');
			$leftContent = mainFrame.getScrollContent($left);
			$rightContent = mainFrame.getScrollContent($right);
			$rightContent.append($selectList);

			$.post('/largescreenControlController/getPollingEncodeBelongInst', {'openLevel':2}, function (data, status) {
				if (status === 'success') {
					var _msg = data.msg;
					if (_msg !== null && typeof _msg !== 'undefined' && _msg !== '') {
						mainFrame.info(_msg);
					} else {
						$leftContent.tree('create', {
							data:[data.node],
							event:{
								click:function(param, complete){
									//禁止点击效果
									complete();
								},
								open:function(param, complete){
									var $node = $(this);
									if($node.tree('is', 'empty')){
										var reqParam = {};
										if(param !== 'root') {
											reqParam.instId = param.split('@@')[1];
										}
										$.post('/largescreenControlController/getPollingEncodeBelongInst', reqParam, function(data,status){
											var _msg = data.msg;
											if(_msg!==null && typeof _msg!=='undefined' && _msg!==''){
												gFn.info(_msg);
											}else{
												var nodeList = data.nodeList;
												for(var i=0; i<nodeList.length; i++){
													$node.tree('append', nodeList[i]);
												}
												if($node.tree('is', 'checked')){
													$node.tree('uncheck', 'children');
													$node.tree('check', 'children');
												}

												var $onScreenList = $('.selectList').find('.pollingSelectedDev'),
													checkdata;
												for(var m=0;m<$onScreenList.length;m++){//初始成员的复选框状态选中,//获取参会设备列表
													checkdata = $onScreenList.eq(m).data('devSlectedUuid');
													//遍历节点
													var $checkBox = $leftContent.find('.file');
													$checkBox.each(function(){
														var $this = $(this),
															_param = $this.attr('data_param').split('@@'),
															_uuid = _param[8];
														if(_uuid===checkdata){
															$this.tree('check');
															//$this.find('a .checkbox').addClass('checked');
															return false;
														}
													});

												}
												complete();
											}
										});
									}else{
										complete();
									}
								},
								check:function(param, complete){
									var paramInfo = param.split('@@'),
										_deviceType = paramInfo[0],
										_deviceName = paramInfo[3],
										_instName = paramInfo[4],
										_deviceUuid = paramInfo[8],
										_deviceLayerId = paramInfo[7],
										_deviceChannelId = paramInfo[10],
										$pollingSelectedDev = $selectList.find('.pollingSelectedDev');
									if($pollingSelectedDev.length==0 && _deviceType == 'device'){
										$list = $('<li class="leaf list-group-item pollingSelectedDev">' +
											'<span class="float_r display_inlineBlock align_c width_60px">' +
											'<button class="btn btn-primary btn-xs devRemove margin_r_10"><span class="glyphicon glyphicon-remove"></span>移除</button>' +
											'</span>' + '<span class="abbreviation pollingDstDevItemName">'+_deviceName+'</span>' + '</li>');
										$selectList.append($list);
										//缓存数据
										$list.data('devSlectedUuid', _deviceUuid);
										$list.data('target', param);
										//添加成员
										zk_Component.addParam(_memberList, _deviceType+'@@'+_deviceUuid+'@@'+_deviceName+'@@'+_instName+'@@'+_deviceLayerId+'@@'+_deviceChannelId);
										//事件代理
										$list.on('click.memberRemove.btn', '.devRemove', function(event){
											var $this = $(this),
												$currItem = $this.closest('li').first(),
												_currParam = $currItem.data('target'),
												_currParamUuid = $currItem.data('devSlectedUuid'),
												_paramInfo = _currParam.split('@@'),
												memInfo = _paramInfo[0]+'@@'+_paramInfo[8]+'@@'+_paramInfo[3]+'@@'+_paramInfo[5];
											_memberList = zk_Component.removeParam(_memberList, memInfo);
											//遍历列表
											var $itemList = $leftContent.find('.file');
											$itemList.each(function(){
												var $that = $(this),
													_param = $that.attr('data_param').split('@@'),
													_uuid = _param[8];
												if(_uuid===_currParamUuid){
													$that.tree('uncheck');
													//$that.find('a .checkbox').removeClass('checked');
													return false;
												}
											});
											$currItem.remove();
										});
										complete();
										return;
									}
									for(var i=0;i<$pollingSelectedDev.length;i++){
										var _uuid = $pollingSelectedDev.eq(i).data('devSlectedUuid');
										if(_deviceUuid == _uuid)break;
										if(i == $pollingSelectedDev.length-1 && _deviceType == 'device'){
											$list = $('<li class="leaf list-group-item pollingSelectedDev">' +
												'<span class="float_r display_inlineBlock align_c width_60px">' +
												'<button class="btn btn-primary btn-xs devRemove margin_r_10"><span class="glyphicon glyphicon-remove"></span>移除</button>' +
												'</span>' + '<span class="abbreviation pollingDstDevItemName">'+_deviceName+'</span>' + '</li>');
											$selectList.append($list);
											//缓存数据
											$list.data('devSlectedUuid', _deviceUuid);
											$list.data('target', param);
											//添加成员
											zk_Component.addParam(_memberList, _deviceType+'@@'+_deviceUuid+'@@'+_deviceName+'@@'+_instName+'@@'+_deviceLayerId+'@@'+_deviceChannelId);
											//事件代理
											$list.on('click.memberRemove.btn', '.devRemove', function(event){
												var $this = $(this),
													$currItem = $this.closest('li').first(),
													_currParam = $currItem.data('target'),
													_currParamUuid = $currItem.data('devSlectedUuid'),
													_paramInfo = _currParam.split('@@'),
													memInfo = _paramInfo[0]+'@@'+_paramInfo[8]+'@@'+_paramInfo[3]+'@@'+_paramInfo[5]+'@@'+_paramInfo[7]+'@@'+_paramInfo[10];
												_memberList = zk_Component.removeParam(_memberList, memInfo);
												//遍历列表
												var $itemList = $leftContent.find('.file');
												$itemList.each(function(){
													var $that = $(this),
														_param = $that.attr('data_param').split('@@'),
														_uuid = _param[8];
													if(_uuid===_currParamUuid){
														$that.tree('uncheck');
														//$that.find('a .checkbox').removeClass('checked');
														return false;
													}
												});
												$currItem.remove();
											});
										}
									}

									complete();
								},
								uncheck:function(param, complete){
									var paramInfo = param.split('@@'),
										memInfo = paramInfo[0]+'@@'+paramInfo[8]+'@@'+paramInfo[3]+'@@'+paramInfo[5]+'@@'+_paramInfo[7]+'@@'+_paramInfo[10],
										_deviceUuid = paramInfo[8];
									//遍历列表
									var $itemList = $selectList.find('li');
									$itemList.each(function(){
										var $this = $(this),
											_uuid = $this.data('devSlectedUuid');
										if(_uuid===_deviceUuid){
											$this.remove();
											_memberList = zk_Component.removeParam(_memberList, memInfo);//删除数组封装的方法返回的是新数组，需要重新赋值
											return false;
										}
									});
									complete();
								}

							}
						});
						var $onScreenList = $('.selectList').find('.pollingSelectedDev'),
							checkdata;
						for(var m=0;m<$onScreenList.length;m++){//初始成员的复选框状态选中,//获取参会设备列表
							checkdata = $onScreenList.eq(m).data('devSlectedUuid');
							//遍历节点
							var $checkBox = $leftContent.find('.file');
							$checkBox.each(function(){
								var $this = $(this),
									_param = $this.attr('data_param').split('@@'),
									_uuid = _param[8];
								if(_uuid===checkdata){
									$this.tree('check');
									//$this.find('a .checkbox').addClass('checked');
									return false;
								}
							});

						}
					}
				} else {
					mainFrame.error("未知程序异常");
				}
			});

			//如果屏幕标题中有轮询设备信息，则再次点击添加轮询设备时显示相应的窗口
			if(typeof pollingInfo!=='undefined'){
				for(var i=0;i<pollingInfo.length;i++){
					var _devName = pollingInfo[i].split('@@')[2],
						_devUuid = pollingInfo[i].split('@@')[1];
					//console.log(pollingInfo[i])
					$list = $('<li class="leaf list-group-item pollingSelectedDev">' +
						'<span class="float_r display_inlineBlock align_c width_60px">' +
						'<button class="btn btn-primary btn-xs devRemove margin_r_10"><span class="glyphicon glyphicon-remove"></span>移除</button>' +
						'</span>' + '<span class="abbreviation pollingDstDevItemName">'+_devName+'</span>' + '</li>');
					$selectList.append($list);
					$list.data('target', pollingInfo[i]);
					$list.data('devSlectedUuid', _devUuid);
					zk_Component.addParam(_memberList, pollingInfo[i]);
					//事件代理
					$list.on('click.memberSelect.btn', '.devRemove', function(event){
						var $this = $(this),
							$currItem = $this.closest('li').first(),
							_currParamInfo = $currItem.data('target').split('@@'),
							_currType = _currParamInfo[0],
							_currUuid = _currParamInfo[1],
							$checkBox;
						_memberList = zk_Component.removeParam(_memberList, $currItem.data('target'));
						$currItem.remove();
						//遍历节点
						$checkBox = $left.find('.file');
						$checkBox.each(function(){
							var $this = $(this),
								_param = $this.attr('data_param').split('@@'),
								_type = _param[0],
								_uuid = _param[8];
							if(_type===_currType && _currUuid===_uuid){
								$this.tree('uncheck');
								//$this.find('a .checkbox').removeClass('checked');
								return false;
							}
						});

					});
				}
			}
		}
	},

	//添加混音设备
	addAudioDev: function(lgScreenId, confId){
		var $screenWrapper = $('.full-screen-container').find('.middle');
		//代表是否在上屏
		var viewFlag = false;
		if($screenWrapper.hasClass('viewing')) viewFlag = true;

		var $windowContent = $('<div class="windowContent width_100 height_100"></div>'),
			$scrollerContainer = $('<div class="scrollerContainer width_100 counter_v"></div>'),
			$left = $('<div class="left width_46 height_90 float_l"></div>'),
			$middle = $('<div class="middle width_8 height_90 float_l"></div>'),
			$right = $('<div class="right width_46 height_90 float_l"></div>'),
			$selectListDev = $('<ul class="list-group selectList selectListDev"></ul>'),
			$leftConent, $rightContent, $list;

		var $volumeWrap = $('<ul class="lanren"></ul>'),
			$volumeLi = $('<li></li>'),
			$volumeGainIcon = $('<span class="volumeGainIcon"></span>'),
			$volumescale_panel = $('<div class="volumescale_panel"><input id="largescreen-volume-range" type="range" min="0" max="100" value="100" style="position:relative; bottom:7px;"/></div>'),
			$volumeValue = $('<span id="largescreen-volume-text" class="volumeValue">100</span>');
		$volumeWrap.append($volumeLi.append($volumeGainIcon)
			.append($volumescale_panel)
			.append($volumeValue));

		var initAudioList, audioInfoList, audioListId=[], audioListUuid=[];
		gC.popWindow.id = 'zk_Window_default';
		gC.popWindow.onClose = function(){
			gC.popWindow.onClose = null;
		};

		gC.popWindow.title = '混音设置';
		gC.popWindow.content = $windowContent;
		gC.popWindow.commitBtn.text = '确定';
		gC.popWindow.commitBtn.callback = function(event,btn,able){

			audioInfoList = [];
			//缓存audio数据信息
			$('.mixAudioSelectedDev').each(function(){
				var $this = $(this);
				var audioInfo = {
					name: $this.find('.mixAudioDstDevItemName').text(),
					uuid: $this.data('devSlectedUuid'),
					id: $this.data('devSlectedId')
				};

				audioInfoList.push(audioInfo);
			});

			$('.setAudio').data('audioDev', audioInfoList);
			$('.setAudio').data('audioVolume', $('#largescreen-volume-range').val());

			var screens = largeScreenConf.dataCache.screens;
			var audioIp, audioPort;

			//取左上角第一个物理屏的音频接收ip和port
			for(var i=0;i<screens.length;i++){
				if(screens[i].serialNum == 1){
					audioIp = screens[i].audio.ip;
					audioPort = screens[i].audio.port;
				}
			}

			var tasks = {
				'type':'audio',
				'content':{
					'audioList':audioInfoList,
					'uuid': audioListUuid,
					'volume': $('#largescreen-volume-range').val(),
					'audioIp': audioIp,
					'audioPort': audioPort
				},
				'viewFlag': viewFlag
			};

			var _reqParam = {
				largescreenId: lgScreenId,
				largescreenConfigId: confId,

				task: $.toJSON(tasks)
			};
			$.post('/largescreenControlController/saveLargescreenAudio', _reqParam, function(data, status){
				if(status === 'success'){
					var _msg = data.msg;
					if(_msg!==null && typeof _msg!=='undefined' && _msg!==''){
						mainFrame.info(_msg);
						return;
					}else{
						if(viewFlag){
							var msg = ('等待服务器响应中，请稍等......');
							gC.zkAlert.title = '信息提示';
							gC.zkAlert.content = msg;
							gC.zkAlert.commitBtn = null;
							gC.zkAlert.onClose = null;
							gC.zkAlert.hasFooter = 'false';
							zk_Component.zkAlert(gC.zkAlert);
							zk_Component.timoutCloseZkAlert(1);
						}
					}
				}else{
					mainFrame.error('未知的程序异常');
				}
			});
			zk_Component.closeWindow();
		};
		zk_Component.popWindow(gC.popWindow);

		$windowContent.append($volumeWrap)
			.append($scrollerContainer.append($left)
				.append($middle)
				.append($right));
		mainFrame.initDynamicHeight($windowContent, 'scrollerContainer');

		mainFrame.addScroll($left, 'xy', 'dark-3');
		mainFrame.addScroll($right, 'y', 'dark-3');
		$leftConent = mainFrame.getScrollContent($left);
		$rightContent = mainFrame.getScrollContent($right);
		$rightContent.append($selectListDev);

		//音量调节
		$(document).unbind('mousedown.autonomus.volume.range')
			.on('mousedown.autonomus.volume.range', '#largescreen-volume-range', function(){
				var $range = $(this);
				var $text  = $('#largescreen-volume-text');
				var volume = 0;
				var mousemove = false;
				$(document).on('mousemove.autonomus.volume.conf.range', function(){
					volume = $range[0].value;
					$text.text(volume);
				});
			});

		//获取音量值
		var audioVolume = $('.setAudio').data('audioVolume');
		if(audioVolume != null){
			$('#largescreen-volume-range').val(audioVolume);
			$('#largescreen-volume-text').text(audioVolume);
		}

		//获取混音设备列表
		var audioDev = $('.setAudio').data('audioDev');
		if(audioDev != null){
			initAudioList = audioDev;
			if(initAudioList.length>0){
				var _devUuid, _devName, _devId, $slectedDevList;
				for(var j=0;j<initAudioList.length;j++){
					_devName = initAudioList[j].name;
					_devUuid = initAudioList[j].uuid;
					_devId = initAudioList[j].id;
					zk_Component.addParam(audioListId,_devId);
					zk_Component.addParam(audioListUuid,_devUuid);

					$slectedDevList = $('<li class="leaf list-group-item mixAudioSelectedDev">'
						+'<span class="mixAudioDstDevItemName">'+_devName+'</span>'
						+'<button class="btn btn-primary btn-xs devRemove margin_r_10 float_r"><span class="glyphicon glyphicon-remove"></span>移除</button>'
						+'</li>');
					$selectListDev.append($slectedDevList);
					$slectedDevList.data('devSlectedUuid',_devUuid);
					$slectedDevList.data('devSlectedId',_devId);
					var $checkBox = $leftConent.find('.file');
					$checkBox.each(function(){
						var $this = $(this),
							_param = $this.attr('data_param').split('@@'),
							_uuid = _param[8]+'@@'+_param[7]+'@@'+_param[11];
						if(_uuid===_devUuid){
							$this.tree('check');
							//$this.find('a .checkbox').addClass('checked');
							return false;
						}
					});
				}
			}
		}

		//事件代理
		$('.mixAudioSelectedDev').on('click.audioMemberRemove.btn', '.devRemove', function(event){
			var $this = $(this),
				$currItem = $this.closest('li').first(),
				_currParamUuid = $currItem.data('devSlectedUuid'),
				_currParamId = $currItem.data('devSlectedId');
			audioListId = zk_Component.removeParam(audioListId, _currParamId);
			audioListUuid = zk_Component.removeParam(audioListUuid, _currParamUuid);

			//遍历列表
			var $itemList = $leftConent.find('.file');
			$itemList.each(function(){
				var $that = $(this),
					_param = $that.attr('data_param').split('@@'),
					_uuid = _param[8]+'@@'+_param[7]+'@@'+_param[11];
				if(_uuid===_currParamUuid){
					$that.tree('uncheck');
					//$that.find('a .checkbox').removeClass('checked');
					return false;
				}
			});
			$currItem.remove();
		});

		$.post('/largescreenControlController/getPollingEncodeBelongInst', {'openLevel':2}, function (data, status) {
			if (status === 'success') {
				var _msg = data.msg;
				if (_msg !== null && typeof _msg !== 'undefined' && _msg !== '') {
					mainFrame.info(_msg);
				} else {
					$leftConent.tree('create', {
						data:[data.node],
						event:{
							click:function(param, complete){
								//禁止点击效果
								complete();
							},
							open:function(param, complete){
								var $node = $(this);
								if($node.tree('is', 'empty')){
									var reqParam = {};
									if(param !== 'root') {
										reqParam.instId = param.split('@@')[1];
									}
									$.post('/largescreenControlController/getPollingEncodeBelongInst', reqParam, function(data,status){
										var _msg = data.msg;
										if(_msg!==null && typeof _msg!=='undefined' && _msg!==''){
											gFn.info(_msg);
										}else{
											var nodeList = data.nodeList;
											for(var i=0; i<nodeList.length; i++){
												$node.tree('append', nodeList[i]);
											}
											if($node.tree('is', 'checked')){
												$node.tree('uncheck', 'children');
												$node.tree('check', 'children');
											}

											var $audioListSelect = $('.selectListDev').find('.mixAudioSelectedDev'),
												checkdata;
											if($audioListSelect.length>0){
												for(var m=0;m<$audioListSelect.length;m++){//初始成员的复选框状态选中,//获取参会设备列表
													checkdata = $audioListSelect.eq(m).data('devSlectedUuid');
													//遍历节点
													var $checkBox = $leftConent.find('.file');
													$checkBox.each(function(){
														var $this = $(this),
															_param = $this.attr('data_param').split('@@'),
															_uuid = _param[8];
														if(_uuid===checkdata){
															$this.tree('check');
															//$this.find('a .checkbox').addClass('checked');
															return false;
														}
													});

												}
											}
											complete();
										}
									});
								}else{
									complete();
								}
							},
							check:function(param, complete){
								var paramInfo = param.split('@@'),
									_deviceType = paramInfo[0],
									_deviceId = parseInt(paramInfo[1]),
									_deviceName = paramInfo[3],
									_deviceUuid = paramInfo[8]+'@@'+paramInfo[7]+'@@'+paramInfo[11],
									$mixAudioSelectedDev = $selectListDev.find('.mixAudioSelectedDev');
								if($mixAudioSelectedDev.length==0 && _deviceType == 'device'){
									$list = $('<li class="leaf list-group-item mixAudioSelectedDev">'
										+'<span class="mixAudioDstDevItemName">'+_deviceName+'</span>'
										+'<button class="btn btn-primary btn-xs devRemove margin_r_10 float_r"><span class="glyphicon glyphicon-remove"></span>移除</button>'
										+'</li>');
									$selectListDev.append($list);
									//缓存数据
									$list.data('devSlectedUuid', _deviceUuid);
									$list.data('devSlectedId', _deviceId);
									//添加成员
									zk_Component.addParam(audioListId, _deviceId);
									zk_Component.addParam(audioListUuid, _deviceUuid);

									//事件代理
									$list.on('click.audioMemberRemove.btn', '.devRemove', function(event){
										var $this = $(this),
											$currItem = $this.closest('li').first(),
											_currParamId = $currItem.data('devSlectedId'),
											_currParamUuid = $currItem.data('devSlectedUuid');
										audioListId = zk_Component.removeParam(audioListId, _currParamId);
										audioListUuid = zk_Component.removeParam(audioListUuid, _currParamUuid);

										//遍历列表
										var $itemList = $leftConent.find('.file');
										$itemList.each(function(){
											var $that = $(this),
												_param = $that.attr('data_param').split('@@'),
												_uuid = _param[8]+'@@'+_param[7]+'@@'+_param[11];
											if(_uuid===_currParamUuid){
												$that.tree('uncheck');
												//$that.find('a .checkbox').removeClass('checked');
												return false;
											}
										});
										$currItem.remove();
									});
									complete();
									return;
								}
								for(var i=0;i<$mixAudioSelectedDev.length;i++){
									var _uuid = $mixAudioSelectedDev.eq(i).data('devSlectedUuid');
									if(_uuid == _deviceUuid)break;
									if(i == $mixAudioSelectedDev.length-1 && _deviceType == 'device'){
										$list = $('<li class="leaf list-group-item mixAudioSelectedDev">'
											+'<span class="mixAudioDstDevItemName">'+_deviceName+'</span>'
											+'<button class="btn btn-primary btn-xs devRemove margin_r_10 float_r"><span class="glyphicon glyphicon-remove"></span>移除</button>'
											+'</li>');
										$selectListDev.append($list);
										//缓存数据
										$list.data('devSlectedUuid', _deviceUuid);
										$list.data('devSlectedId', _deviceId);
										//添加成员
										zk_Component.addParam(audioListId, _deviceId);
										zk_Component.addParam(audioListUuid, _deviceUuid);

										//事件代理
										$list.on('click.audioMemberRemove.btn', '.devRemove', function(event){
											var $this = $(this),
												$currItem = $this.closest('li').first(),
												_currParamId = $currItem.data('devSlectedId'),
												_currParamUuid = $currItem.data('devSlectedUuid');
											audioListId = zk_Component.removeParam(audioListId, _currParamId);
											audioListUuid = zk_Component.removeParam(audioListUuid, _currParamUuid);

											//遍历列表
											var $itemList = $leftConent.find('.file');
											$itemList.each(function(){
												var $that = $(this),
													_param = $that.attr('data_param').split('@@'),
													_uuid = _param[8]+'@@'+_param[7]+'@@'+_param[11];
												if(_uuid===_currParamUuid){
													$that.tree('uncheck');
													//$that.find('a .checkbox').removeClass('checked');
													return false;
												}
											});
											$currItem.remove();
										});
									}
								}

								complete();
							},
							uncheck:function(param, complete){
								var paramInfo = param.split('@@'),
									_deviceId = parseInt(paramInfo[1]),
									_deviceUuid = paramInfo[8];
								//遍历列表
								var $itemList = $selectListDev.find('li');
								$itemList.each(function(){
									var $this = $(this),
										_uuid = $this.data('devSlectedUuid');
									if(_uuid===_deviceUuid){
										$this.remove();
										audioListId = zk_Component.removeParam(audioListId, _deviceId);//删除数组封装的方法返回的是新数组，需要重新赋值
										audioListUuid = zk_Component.removeParam(audioListUuid, _deviceUuid);

										return false;
									}
								});
								complete();
							}

						}
					});

					var $audioListSelect = $('.selectListDev').find('.mixAudioSelectedDev'),
						checkdata;
					if($audioListSelect.length>0){
						for(var m=0;m<$audioListSelect.length;m++){//初始成员的复选框状态选中,//获取参会设备列表
							checkdata = $audioListSelect.eq(m).data('devSlectedUuid');
							//遍历节点
							var $checkBox = $leftConent.find('.file');
							$checkBox.each(function(){
								var $this = $(this),
									_param = $this.attr('data_param').split('@@'),
									_uuid = _param[8]+'@@'+_param[7]+'@@'+_param[11];
								if(_uuid===checkdata){
									$this.tree('check');
									//$this.find('a .checkbox').addClass('checked');
									return false;
								}
							});

						}
					}
				}

			} else {
				mainFrame.error("未知程序异常");
			}
		});
	}

};