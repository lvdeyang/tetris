define([
    'text!' + window.APPPATH + 'system/resource/dictionary/page-dic.html',
    'restfull',
    'config',
    'commons',
    'vue',
    'element-ui',
    'bvc2-header',
    'bvc2-system-nav-side',
    'bvc2-system-table-base',
	'extral'
], function(tpl, ajax, config, commons, Vue){

    var pageId = 'page-dic';

    var init = function(){

        //设置标题
        commons.setTitle(pageId);

        ajax.get('/system/dictionary/query', null, function(data){
            //转换数据格式
            var typeOptions = [];
            var typeArr = data.dicTypes;
            if(typeArr && typeArr.length>0){
                for(var i=0; i<typeArr.length; i++){
                	typeOptions.push({
                        label:typeArr[i],
                        value:typeArr[i]
                    });
                }
            }
            
            var levelOptions = [];
            var levelArr = data.servLevels;
            if(levelArr && levelArr.length>0){
            	for(var i=0; i<levelArr.length; i++){
            		levelOptions.push({
            			label:levelArr[i],
            			value:levelArr[i],
            			disabled:false
            		});
            	}
            }
            var levelOptionsCopy = $.extend(true, [], levelOptions);
            
            var parentOptions = [];
            var parentArr = data.parentLevelNames;
            if(parentArr && parentArr.length>0){
            	for(var i=0; i<parentArr.length; i++){
            		parentOptions.push({
            			label:parentArr[i].content,
            			value:parentArr[i].content,
            			disabled:false
            		});
            	}
            }
            var parentOptionsCopy = $.extend(true, [], parentOptions);
            
            var regionOptions=[]
            var regionArr = data.regions;
            for(var i=0;i<regionArr.length;i++){
            	regionOptions.push({
            		label:regionArr[i].content,
                    value:regionArr[i].boId,
                    disabled:false
                });
            }
            var regionOptionsCopy = $.extend(true, [], regionOptions);
            
            
            var $page = document.getElementById(pageId);
            $page.innerHTML = tpl;

            var v_dictpl = new Vue({
                el:'#' + pageId + '-wrapper',
                data:{
					menurouter: false,
					shortCutsRoutes:commons.data,
					active:"/page-dic",
                    header:commons.getHeader(1),
                    side:{
                        active:'0-9'
                    },
                    dic:{
                        buttonCreate:'新建',
                        buttonRemove:'删除',
                        sourceDatas:[{
                        	label:'栏目层级',
                        	options:levelOptionsCopy,
                        },{
                        	label:'所属一级栏目',
                        	options:parentOptionsCopy,
                        },{
                        	label:'所属地区？组织',
                        	options:regionOptionsCopy
                        }],
                        columns:[{
                            label:'类型',
                            prop:'dicType',
                            type:'select',
                            options:typeOptions,
                            change:function(row, column, instance) {
								//类型和栏目层级联动
								var _row = row, _column = column;
								var defaultData = {label: '默认', value: '默认'};

								ajax.get('/system/dictionary/query', null, function (data) {

									var level = data.servLevels;
									var levelOptions = [];
									if(level && level.length>0){
										for(var i=0; i<level.length; i++){
											levelOptions.push({
												label:level[i],
												value:level[i]
											})
										}
									}
									if (_row.dicType == '直播') {
										instance.columns[2].options.splice(0, instance.columns[2].options.length);
										//var level = instance.sourcedatas[0].options;
										for (var k = 0; k < levelOptions.length; k++) {
											instance.columns[2].options.push(levelOptions[k]);
										}
										for (var i = 0; i < instance.columns[2].options.length; i++) {
											if (instance.columns[2].options[i].label != '一级栏目') {
												instance.columns[2].options[i].disabled = true;
											}
										}

										instance.columns[3].options.splice(0, instance.columns[3].options.length);
										instance.columns[3].options.push(defaultData);

										instance.columns[4].options.splice(0, instance.columns[4].options.length);
										instance.columns[4].options.push(defaultData);

										instance.columns[5].editable = false;
										instance.columns[6].editable = false;
									} else if (_row.dicType == '点播') {
										instance.columns[2].options.splice(0, instance.columns[2].options.length);
										//var level = instance.sourcedatas[0].options;
										for (var k = 0; k < levelOptions.length; k++) {
											instance.columns[2].options.push(levelOptions[k]);
										}
										for (var i = 0; i < instance.columns[2].options.length; i++) {
											if (instance.columns[2].options[i].label == '默认') {
												instance.columns[2].options[i].disabled = true;
											} else {
												instance.columns[2].options[i].disabled = false;
											}
										}

										instance.columns[5].editable = false;
										instance.columns[6].editable = false;
									}else if(_row.dicType == '组织'){
											instance.columns[2].options.splice(0, instance.columns[2].options.length);
											instance.columns[3].options.splice(0, instance.columns[3].options.length);
											instance.columns[4].options.splice(0, instance.columns[4].options.length);

											instance.columns[2].options.push(defaultData);
											instance.columns[3].options.push(defaultData);
											instance.columns[4].options.push(defaultData);

											instance.columns[5].editable = true;
											instance.columns[6].editable = true;
									}else if(_row.dicType == '存储位置'){
										instance.columns[2].options.splice(0, instance.columns[2].options.length);
										instance.columns[3].options.splice(0, instance.columns[3].options.length);
										instance.columns[4].options.splice(0, instance.columns[4].options.length);

										instance.columns[2].options.push(defaultData);
										instance.columns[3].options.push(defaultData);
										instance.columns[4].options.push(defaultData);

										instance.columns[5].editable = true;
										instance.columns[6].editable = false;
									}
								})



							}
                        },{
                            label:'名称',
                            prop:'content',
                            type:'simple', 
                        },{
                        	label:'栏目层级',
                        	prop:'servLevel',
                        	type:'select',
                        	options:levelOptions,
                        	change: function(row, column, instance){
                        		//栏目层级和所属一级栏目、所属地区联动
                        		var _row = row, _column = column;
                            	var defaultData = {label:'默认', value:'默认'};
								ajax.get('/system/dictionary/query', null, function (data) {

									var levelOptions = [];
									var level = data.servLevels;
									if(level && level.length>0){
										for(var i=0; i<level.length; i++){
											levelOptions.push({
												label:level[i],
												value:level[i]
											})
										}
									}

									var parentOptions = [];
									var parentLevel = data.parentLevelNames;
									if(parentLevel && parentLevel.length>0){
										for(var i=0; i<parentLevel.length; i++){
											parentOptions.push({
												label:parentLevel[i].content,
												value:parentLevel[i].content
											})
										}
									}

									var regionOptions = [];
									var region = data.regions;
									if(region && region.length>0){
										for(var i=0; i<region.length; i++){
											regionOptions.push({
												label:region[i].content,
												value:region[i].boId
											})
										}
									}

									if(_row.dicType == '点播'){
										instance.columns[2].options.splice(0, instance.columns[2].options.length);
										//var level = instance.sourcedatas[0].options;
										for(var k=0; k<levelOptions.length; k++){
											instance.columns[2].options.push(levelOptions[k]);
										}
										for(var i=0; i<instance.columns[2].options.length; i++){
											instance.columns[2].options[i].disabled = false;
										}

										if(_row.servLevel == '一级栏目'){
											instance.columns[3].options.splice(0, instance.columns[3].options.length);
											instance.columns[3].options.push(defaultData);
											instance.columns[4].options.splice(0, instance.columns[4].options.length);
											//var region = instance.sourcedatas[2].options;
											instance.columns[4].options.push(defaultData);
											for(var k=0; k<regionOptions.length; k++){
												instance.columns[4].options.push(regionOptions[k]);
											}
										}else if(_row.servLevel == '会议类型'){//二级栏目
											instance.columns[3].options.splice(0, instance.columns[3].options.length);
											//var region = instance.sourcedatas[1].options;
											for(var k=0; k<parentOptions.length; k++){
												instance.columns[3].options.push(parentOptions[k]);
											}
											instance.columns[4].options.splice(0, instance.columns[4].options.length);
											instance.columns[4].options.push(defaultData);
										}
									}

								})
                        	}
                        },{
                        	label:'父栏目（可不选）',//'所属一级栏目',//后台自动选择
                        	prop:'parentLevelName',
                        	type:'select',
                        	options:parentOptions
                        },{
                            label:'所属组织',
                            prop:'boId',
                            type:'select',
                            options:regionOptions
                        },{
                            label:'code',
                            prop:'code',
                            type:'simple',
                            editable:true
                        },{
                            label:'IP',
                            prop:'ip',
                            type:'simple',
                            editable:true
                        }],
                        load:'/system/dictionary/load',
                        save:'/system/dictionary/save',
                        update:'/system/dictionary/update',
                        remove:'/system/dictionary/remove',
                        removebatch:'/system/dictionary/remove/all',
//                        options:[{
//                            label:'编辑',
//                            icon:'el-icon-document',
//                            click:'edit-gears'
//                        }],
                        pk:'id'
                    }
                },
                methods:{
//                    editGears:function(row){
//                        window.location.hash = '#/page-dic-gears/' + row.id + '/' + row.name;
//                    }
                	rowEditAfter:function(row, e){
                		var _row = row, instance = this;
                		var defaultData = {label:'默认', value:'默认'};
                        if(_row.dicType == '直播'){
                    		instance.dic.columns[2].options.splice(0, instance.dic.columns[2].options.length);
                    		var level = instance.dic.sourceDatas[0].options;
                    		for(var k=0; k<level.length; k++){
                				instance.dic.columns[2].options.push(level[k]);
                			}
                			for(var i=0; i<instance.dic.columns[2].options.length; i++){
                				if(instance.dic.columns[2].options[i].label != '一级栏目'){
                					instance.dic.columns[2].options[i].disabled = true;
                				}
                			}

                			instance.dic.columns[3].options.splice(0, instance.dic.columns[3].options.length);
                			instance.dic.columns[3].options.push(defaultData);

                			instance.dic.columns[4].options.splice(0, instance.dic.columns[4].options.length);
                			var region = instance.dic.sourceDatas[2].options;
                			for(var k=0; k<region.length; k++){
                				instance.dic.columns[4].options.push(region[k]);
                			}

                			instance.dic.columns[5].editable = false;
                    		instance.dic.columns[6].editable = false;
                        }else if(_row.dicType == '点播'){
                    		instance.dic.columns[2].options.splice(0, instance.dic.columns[2].options.length);
                    		var level = instance.dic.sourceDatas[0].options;
                    		for(var k=0; k<level.length; k++){
                				instance.dic.columns[2].options.push(level[k]);
                			}
                			for(var i=0; i<instance.dic.columns[2].options.length; i++){
                				if(instance.dic.columns[2].options[i].label == '默认'){
                					instance.dic.columns[2].options[i].disabled = true;
                				}else{
                					instance.dic.columns[2].options[i].disabled = false;
                				}
                			}

                			instance.dic.columns[5].editable = false;
                    		instance.dic.columns[6].editable = false;
                        }else if(_row.dicType == '地区'){
                        		instance.dic.columns[2].options.splice(0, instance.dic.columns[2].options.length);
                        		instance.dic.columns[3].options.splice(0, instance.dic.columns[3].options.length);
                        		instance.dic.columns[4].options.splice(0, instance.dic.columns[4].options.length);

                        		instance.dic.columns[2].options.push(defaultData);
                        		instance.dic.columns[3].options.push(defaultData);
                        		instance.dic.columns[4].options.push(defaultData);

                        		instance.dic.columns[5].editable = true;
                        		instance.dic.columns[6].editable = true;
                        	}
                	}
                }
            });

        });
    };

    var destroy = function(){

    };

    var groupList = {
        path:'/' + pageId,
        component:{
            template:'<div id="' + pageId + '" class="page-wrapper"></div>'
        },
        init:init,
        destroy:destroy
    };

    return groupList;
});