/**
 * {
 *  id:'level0',
 *  groups:[{
 *      id:'spring-eureka',
 *      types:[{
 *          id:'eureka',
 *          name:'注册中心',
 *          servers:[{
 *              id:'eureka0'
 *          }]
 *      }]
 *  }]
 * }
 */
function Graph($el, levels, theme){

    var data = levels;

    if(!theme){
        theme = {
            normal:{
                scene:{
                    backgroundColor:0xeeeeee
                },
                group:{
                    lineColor:0x777777
                },
                type:{
                    backgroundColor:0xffffff,
                    color:0x777777
                },
                server:{
                    surfaceColor:0xffffff,
                    lineColor:0x777777,
                    label:{
                        backgroundColor:0xffffff,
                        lineColor:0x777777,
                        color:0x777777
                    },
                    connection:{
                        lineColor:0x777777
                    }
                }
            },
            mouseover:{
                server:{
                    surfaceColor:0xeeeeee,
                    lineColor:0x000000,
                    label:{
                        backgroundColor:0xeeeeee,
                        lineColor:0x000000,
                        color:0x000000
                    },
                    connection: {
                        lineColor: 0x000000
                    }
                }
            },
            click:{
                server:{
                    surfaceColor:0x67c23a,
                    lineColor:0x539c2f,
                    label:{
                        backgroundColor:0x67c23a,
                        lineColor:0x539c2f,
                        color:0xffffff
                    },
                    connection: {
                        lineColor: 0x539c2f
                    }
                }
            }
        };
    }

    //层级间距
    this.levelSpace = {x:50, y:80};
    //分组间距
    this.groupSpace = {x:10, z:10};
    //分组padding
    this.groupPadding = 5;
    //类型间距
    this.typeSpace = {z:5};
    //类型尺寸
    this.typeSize = {width:30, height:10};
    //类型字号
    this.typeFontSize = 4;
    //服务器间距
    this.serverSpace = {x:10};
    //服务尺寸
    this.serverSize = {width:10, height:2, depth:10};

    //计算层的y位置
    if(data.length === 1){
        data[0].y = 0;
    }else {
        data[0].y = this.levelSpace.y;
        data[1].y = 0;
        if(data.length > 2){
            for(var i=2; i<data.length; i++){
                data[i].y = -(this.levelSpace.y*(i-1));
            }
        }
    }

    //计算层的x位置
    var beginX = -((data.length-1)*this.levelSpace.x/2);
    for(var i=0; i<data.length; i++){
        if(i===0){
            data[i].x = beginX;
        }else{
            data[i].x = data[i-1].x + this.levelSpace.x;
        }
    }

    //转换数据结构便于遍历
    var loopLevels = [];
    var loopGroups = [];
    var loopTypes = [];
    var loopServers = [];

    //计算平面的时候，由于平面旋转了90度，所以平面的height对应了坐标系的z轴
    for(var i=0; i<data.length; i++){
        var level = data[i];
        loopLevels.push(level);
        var groups = level.groups;
        //计算组的尺寸
        for(var j=0; j<groups.length; j++){
            var group = groups[j];
            var types = group.types;
            group.height = (types.length-1)*this.typeSpace.z + types.length*this.typeSize.height + this.groupPadding*2;
            var serverLength = [];
            for(var k=0; k<types.length; k++){
                serverLength.push(types[k].servers.length);
            }
            group.width = Math.max.apply(null, serverLength) * (this.serverSpace.x+this.serverSize.width) + this.typeSize.width + this.groupPadding*2;
        }

        //计算组的位置
        var totalGroupZ = 0;
        for(var j=0; j<groups.length; j++){
            totalGroupZ += groups[j].height;
        }
        totalGroupZ += ((groups.length-1)*this.groupSpace.z);
        for(var j=0; j<groups.length; j++){
            var group = groups[j];
            loopGroups.push(group);
            group.x = level.x;
            if(j === 0){
                group.z = totalGroupZ / 2;
            }else{
                group.z = groups[j-1].z + groups[j-1].height + this.groupSpace.z;
            }
            group.y = level.y;
            var types = group.types;
            for(var k=0; k<types.length; k++){
                //计算type位置和尺寸
                var type = types[k];
                loopTypes.push(type);
                type.x = -group.width/2 + this.groupPadding + this.typeSize.width/2 + group.x;
                if(k === 0){
                    type.z = group.z + group.height/2 - this.groupPadding - this.typeSize.height/2;
                }else{
                    type.z = types[k-1].z - this.typeSize.height - this.typeSpace.z;
                }
                type.y = level.y;
                type.width = this.typeSize.width;
                type.height = this.typeSize.height;
                type.fontSize = this.typeFontSize;
                var servers = type.servers;
                for(var m=0; m<servers.length; m++){
                    //计算server位置和尺寸
                    var server = servers[m];
                    loopServers.push(server);
                    if(m === 0){
                        server.x = type.x + this.typeSize.width/2 + this.serverSpace.x + this.serverSize.width/2;
                    }else{
                        server.x = servers[m-1].x + this.serverSize.width + this.serverSpace.x;
                    }
                    server.z = type.z;
                    server.y = level.y;
                    server.width = this.serverSize.width;
                    server.height = this.serverSize.height;
                    server.depth = this.serverSize.depth;
                }
            }
        }
    }

    this.scene = new Scene({
        $el:$el,
        backgroundColor:theme.normal.scene.backgroundColor,
        camera:{
            x:56,
            y:247,
            z:160,
            near:1,
            far:100000,
            target:{
                x:-24,
                y:-2,
                z:48
            }
        }
    });

    var _3Objs = [];
    for(var i=0; i<loopGroups.length; i++){
        var group = loopGroups[i];
        _3Objs.push(new Group({
            data:group,
            width:group.width,
            height:group.height,
            x:group.x,
            y:group.y,
            z:group.z,
            lineColor:theme.normal.group.lineColor,
            dashSize:5,
            gapSize:5
        }));
    }
    for(var i=0; i<loopTypes.length; i++){
        var type = loopTypes[i];
        _3Objs.push(new Type({
            data:type,
            width:type.width,
            height:type.height,
            x:type.x,
            y:type.y,
            z:type.z,
            backgroundColor:theme.normal.type.backgroundColor,
            text:type.name,
            fontSize:type.fontSize,
            color:theme.normal.type.color
        }));
    }
    for(var i=0; i<loopServers.length; i++){
        var server = loopServers[i];
        var _3server = new Server({
            data:server,
            width:server.width,
            height:server.height,
            depth:server.depth,
            x:server.x,
            y:server.y,
            z:server.z,
            icon:server.icon,
            surfaceColor:theme.normal.server.surfaceColor,
            lineColor:theme.normal.server.lineColor,
            label:{
                props:['ip'],
                backgroundColor:theme.normal.server.label.backgroundColor,
                lineColor:theme.normal.server.label.lineColor,
                color:theme.normal.server.label.color
            },
            connection:{
                lineColor:theme.normal.server.connection.lineColor
            }
        });
        _3Objs.push(_3server);
        $(_3server).on('click', function(){
            if(!this.config.data.current){
                for(var j=0; j<loopServers.length; j++){
                    if(loopServers[j].current){
                        loopServers[j].current = false;
                        loopServers[j].THREE.setColor({
                            surfaceColor:theme.normal.server.surfaceColor,
                            lineColor:theme.normal.server.lineColor,
                            label:{
                                backgroundColor:theme.normal.server.label.backgroundColor,
                                lineColor:theme.normal.server.label.lineColor,
                                color:theme.normal.server.label.color
                            },
                            connection: {
                                lineColor:theme.normal.server.connection.lineColor
                            }
                        });
                    }
                }
                this.setColor({
                    surfaceColor:theme.click.server.surfaceColor,
                    lineColor:theme.click.server.lineColor,
                    label:{
                        backgroundColor:theme.click.server.label.backgroundColor,
                        lineColor:theme.click.server.label.lineColor,
                        color:theme.click.server.label.color
                    },
                    connection: {
                        lineColor: theme.click.server.connection.lineColor
                    }
                });
                this.config.data.current = true;
            }
        });
        $(_3server).on('mouseover', function(){
            if(!this.config.data.current){
                this.setColor({
                    surfaceColor:theme.mouseover.server.surfaceColor,
                    lineColor:theme.mouseover.server.lineColor,
                    label:{
                        backgroundColor:theme.mouseover.server.label.backgroundColor,
                        lineColor:theme.mouseover.server.label.lineColor,
                        color:theme.mouseover.server.label.color
                    },
                    connection: {
                        lineColor: theme.mouseover.server.connection.lineColor
                    }
                });
            }
        });
        $(_3server).on('mouseout', function(){
            if(!this.config.data.current){
                this.setColor({
                    surfaceColor:theme.normal.server.surfaceColor,
                    lineColor:theme.normal.server.lineColor,
                    label:{
                        backgroundColor:theme.normal.server.label.backgroundColor,
                        lineColor:theme.normal.server.label.lineColor,
                        color:theme.normal.server.label.color
                    },
                    connection: {
                        lineColor:theme.normal.server.connection.lineColor
                    }
                });
            }
        });
    }
    for(var i=0; i<loopServers.length; i++){
        var outerServer = loopServers[i];
        if(outerServer.refs && outerServer.refs.length>0){
            for(var j=0; j<loopServers.length; j++){
                var innerServer = loopServers[j];
                if(outerServer.refs.indexOf(innerServer.id) >= 0){
                    outerServer.THREE.connect(innerServer.THREE);
                }
            }
        }
    }
    this.scene.addAll(_3Objs);
}

/**
 * 修改尺寸重绘
 * @returns this
 */
Graph.prototype.resize = function(){
    this.scene.resize();
    return this;
};

/**
 * @returns 对象类型
 */
Graph.prototype.instanceOf = function(){
    return 'Graph';
};