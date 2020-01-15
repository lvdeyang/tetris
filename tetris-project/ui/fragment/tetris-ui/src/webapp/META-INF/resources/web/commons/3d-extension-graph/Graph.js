/**
 * $el:dom对象,
 * levels:[{
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
 * }],
 * theme:皮肤,
 * event:{
 *  serverSelected:function(){}
 * }
 */
function Graph($el, levels, theme, event){

    var data = levels;

    theme = theme || Graph.prototype.defaultTheme;

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
        backgroundColor:theme.scene.backgroundColor,
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
            lineColor:theme.group.lineColor,
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
            backgroundColor:theme.type.backgroundColor,
            text:type.name,
            fontSize:type.fontSize,
            color:theme.type.color
        }));
    }
    for(var i=0; i<loopServers.length; i++){
        var server = loopServers[i];
        var colors = theme.server[server.status.toLowerCase()];
        var _3server = new Server({
            data:server,
            width:server.width,
            height:server.height,
            depth:server.depth,
            x:server.x,
            y:server.y,
            z:server.z,
            icon:server.icon,
            surfaceColor:colors.normal.surfaceColor,
            lineColor:colors.normal.lineColor,
            label:{
                props:['ip'],
                backgroundColor:colors.normal.label.backgroundColor,
                lineColor:colors.normal.label.lineColor,
                color:colors.normal.label.color
            },
            connection:{
                lineColor:colors.normal.connection.lineColor
            }
        });
        _3Objs.push(_3server);
        $(_3server).on('click', function(){
            var self = this;
            var done = function(){
                for(var j=0; j<loopServers.length; j++){
                    var colors = theme.server[loopServers[j].status.toLowerCase()];
                    if(loopServers[j].current){
                        loopServers[j].current = false;
                        loopServers[j].THREE.setColor({
                            surfaceColor:colors.normal.surfaceColor,
                            lineColor:colors.normal.lineColor,
                            label:{
                                backgroundColor:colors.normal.label.backgroundColor,
                                lineColor:colors.normal.label.lineColor,
                                color:colors.normal.label.color
                            },
                            connection: {
                                lineColor:colors.normal.connection.lineColor
                            }
                        });
                    }
                }
                var colors = theme.server[self.config.data.status.toLowerCase()];
                self.setColor({
                    surfaceColor:colors.click.surfaceColor,
                    lineColor:colors.click.lineColor,
                    label:{
                        backgroundColor:colors.click.label.backgroundColor,
                        lineColor:colors.click.label.lineColor,
                        color:colors.click.label.color
                    },
                    connection: {
                        lineColor:colors.click.connection.lineColor
                    }
                });
                self.config.data.current = true;
            };
            if(!self.config.data.current){
                if(typeof event.serverSelected === 'function'){
                    event.serverSelected(self.config.data, done);
                }else{
                    done();
                }
            }
        });
        $(_3server).on('mouseover', function(){
            var colors = theme.server[this.config.data.status.toLowerCase()];
            if(!this.config.data.current){
                this.setColor({
                    surfaceColor:colors.mouseover.surfaceColor,
                    lineColor:colors.mouseover.lineColor,
                    label:{
                        backgroundColor:colors.mouseover.label.backgroundColor,
                        lineColor:colors.mouseover.label.lineColor,
                        color:colors.mouseover.label.color
                    },
                    connection: {
                        lineColor:colors.mouseover.connection.lineColor
                    }
                });
            }
        });
        $(_3server).on('mouseout', function(){
            var colors = theme.server[this.config.data.status.toLowerCase()];
            if(!this.config.data.current){
                this.setColor({
                    surfaceColor:colors.normal.surfaceColor,
                    lineColor:colors.normal.lineColor,
                    label:{
                        backgroundColor:colors.normal.label.backgroundColor,
                        lineColor:colors.normal.label.lineColor,
                        color:colors.normal.label.color
                    },
                    connection: {
                        lineColor:colors.normal.connection.lineColor
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
    this.theme = theme;
    this.data = {
        levels:loopLevels,
        groups:loopGroups,
        types:loopTypes,
        servers:loopServers
    };
}

/**
 * 更新服务状态
 * @param servers {id:'服务实例id', status:'状态'}
 */
Graph.prototype.refreshServerStatus = function(servers){
    var theme = this.theme;
    var existServers = this.data.servers;
    for(var i=0; i<existServers.length; i++){
        var existServer = existServers[i];
        for(var j=0; j<servers.length; j++){
            var server = servers[j];
            if(existServer.id===server.id && existServer.status!==server.status){
                existServer.status = server.status;
                var colors = theme.server[existServer.status.toLowerCase()];
                if(existServer.current){
                    colors = colors.click;
                }else{
                    colors = colors.normal;
                }
                existServer.THREE.setColor({
                    surfaceColor:colors.surfaceColor,
                    lineColor:colors.lineColor,
                    label:{
                        backgroundColor:colors.label.backgroundColor,
                        lineColor:colors.label.lineColor,
                        color:colors.label.color
                    },
                    connection: {
                        lineColor:colors.connection.lineColor
                    }
                });
                break;
            }
        }
    }
};

/**
 * 默认皮肤
 */
Graph.prototype.defaultTheme = {
    scene:{
        backgroundColor:0xf4f4f5
    },
    group:{
        lineColor:0xd3d4d6
    },
    type:{
        backgroundColor:0xffffff,
        color:0x909399
    },
    server:{
        up:{
            normal:{
                surfaceColor:0xf0f9eb,
                lineColor:0xc2e7b0,
                label:{
                    backgroundColor:0xf0f9eb,
                    lineColor:0xc2e7b0,
                    color:0x67c23a
                },
                connection: {
                    lineColor: 0xc2e7b0
                }
            },
            mouseover:{
                surfaceColor:0xcff7b8,
                lineColor:0x9ede7f,
                label:{
                    backgroundColor:0xcff7b8,
                    lineColor:0x9ede7f,
                    color:0x53c51a
                },
                connection: {
                    lineColor: 0x9ede7f
                }
            },
            click:{
                surfaceColor:0x67c23a,
                lineColor:0x4d9c25,
                label:{
                    backgroundColor:0x67c23a,
                    lineColor:0x4d9c25,
                    color:0xffffff
                },
                connection: {
                    lineColor: 0x4d9c25
                }
            }
        },
        down:{
            normal:{
                surfaceColor:0xfef0f0,
                lineColor:0xfbc4c4,
                label:{
                    backgroundColor:0xfef0f0,
                    lineColor:0xfbc4c4,
                    color:0xf56c6c
                },
                connection: {
                    lineColor: 0xfbc4c4
                }
            },
            mouseover:{
                surfaceColor:0xfbd7d7,
                lineColor:0xf7a5a5,
                label:{
                    backgroundColor:0xfbd7d7,
                    lineColor:0xf7a5a5,
                    color:0xf55656
                },
                connection: {
                    lineColor: 0xf7a5a5
                }
            },
            click:{
                surfaceColor:0xf56c6c,
                lineColor:0xd45454,
                label:{
                    backgroundColor:0xf56c6c,
                    lineColor:0xd45454,
                    color:0xffffff
                },
                connection: {
                    lineColor: 0xd45454
                }
            }
        }
    }
};

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