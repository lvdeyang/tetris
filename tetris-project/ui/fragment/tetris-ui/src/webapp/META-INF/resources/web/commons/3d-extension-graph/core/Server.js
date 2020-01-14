/**
 * {
 *     data:数据缓存
 *     width:长
 *     height:宽
 *     depth:深
 *     x:x
 *     y:y
 *     z:z
 *     icon:服务图标
 *     surfaceColor:表面颜色
 *     lineColor:描边颜色
 *     label:{
 *         props:[]，
 *         backgroundColor:'标签背景色',
 *         lineColor:'描边颜色',
 *         color:文字颜色
 *     },
 *     connection:{
 *          lineColor:连线颜色
 *     }
 * }
 */
function Server(config){
    this.config = config;
    this.config.data.THREE = this;

    this.geometries = {
        box:new THREE.BoxBufferGeometry(config.width, config.height, config.depth)
    };

    this.materials = {
        iconMaterial:new THREE.MeshBasicMaterial({map:THREE.ImageUtils.loadTexture(config.icon)}),
        surfaceMaterial:new THREE.MeshBasicMaterial({color:config.surfaceColor})
    };

    this.obj3D = {
        core:new THREE.Mesh(this.geometries.box, [
            this.materials.surfaceMaterial,
            this.materials.surfaceMaterial,
            this.materials.iconMaterial,
            this.materials.surfaceMaterial,
            this.materials.surfaceMaterial,
            this.materials.surfaceMaterial]),
        line:new THREE.LineSegments(new THREE.EdgesGeometry(this.geometries.box), new THREE.LineBasicMaterial({color:config.lineColor}))
    };

    this.obj3D.core.position.set(config.x, config.y, config.z);
    this.obj3D.line.position.set(0, 0, 0);
    this.obj3D.core.add(this.obj3D.line);

    //生成链接点
    this.generatePoints()
         .label()
         .dispatchEvent(['mouseover', 'mouseout', 'click']);

}

/**
 * 调度事件
 * @param eventTypes 事件类型
 */
Server.prototype.dispatchEvent = function(eventTypes){
    if(!eventTypes || eventTypes.length<=0) return;
    var self = this;
    for(var i=0; i<eventTypes.length; i++){
        (function(index){
            $(self.obj3D.core).on(eventTypes[index], function(e){
                $(self).trigger(eventTypes[index]);
            });
        })(i);
    }
    return this;
};

/**
 * {
 *     icon:服务图标
 *     surfaceColor:表面颜色
 *     lineColor:描边颜色
 *     label:{
 *         backgroundColor:'标签背景色',
 *         lineColor:'描边颜色',
 *         color:文字颜色
 *     },
 *     connection:{
 *         lineColor:连线颜色
 *     }
 * }
 */
Server.prototype.setColor = function(config){
    var oldConfig = {
        icon:this.config.icon,
        surfaceColor:this.config.surfaceColor,
        lineColor:this.config.lineColor,
        label:{
            backgroundColor:this.config.label.backgroundColor,
            lineColor:this.config.label.lineColor,
            color:this.config.label.color
        },
        connection:{
            lineColor:this.config.connection.lineColor
        }
    };
    this.config = $.extend(true, this.config, config);
    config = this.config;
    if(oldConfig.icon !== config.icon){
        this.materials.iconMaterial.map = THREE.ImageUtils.loadTexture(config.icon);
    }
    if(oldConfig.surfaceColor !== config.surfaceColor){
        this.materials.surfaceMaterial.color.setHex(config.surfaceColor);
    }
    if(oldConfig.lineColor !== config.lineColor){
        this.obj3D.line.material.color.setHex(config.lineColor);
    }
    if(oldConfig.label.backgroundColor !== config.label.backgroundColor){
        $(this.obj3D.label.element).css('background-color','#'+(parseInt(config.label.backgroundColor).toString(16)));
    }
    if(oldConfig.label.lineColor !== config.label.lineColor){
        $(this.obj3D.label.element).css('border-color','#'+(parseInt(config.label.lineColor).toString(16)));
    }
    if(oldConfig.label.color !== config.label.color){
        $(this.obj3D.label.element).css('color','#'+(parseInt(config.label.color).toString(16)));
    }
    if(oldConfig.connection.lineColor !== config.connection.lineColor){
        if(this.obj3D.connections && this.obj3D.connections.length>0){
            for(var i=0; i<this.obj3D.connections.length; i++){
                this.obj3D.connections[i].material.color.setHex(config.connection.lineColor);
            }
        }
    }
};

/**
 * 创建关键点
 */
Server.prototype.generatePoints = function(){
    var config = this.config;
    this.keyPoints = {
        //表现中心点
        faceCenterPoint:{
            up:new THREE.Vector3(config.x, config.y+config.height/2, config.z),
            right:new THREE.Vector3(config.x+config.width/2, config.y, config.z),
            bottom:new THREE.Vector3(config.x, config.y-config.height/2, config.z),
            left:new THREE.Vector3(config.x-config.width/2, config.y, config.z),
            front:new THREE.Vector3(config.x, config.y, config.z+config.depth/2),
            back:new THREE.Vector3(config.x, config.y, config.z-config.depth/2)
        }
    };
    return this;
};

/**
 * 打标签
 */
Server.prototype.label = function(){
    var config = this.config;
    var $label = $('<div style="line-height:20px; border-radius:4px; border-width:1px; border-style:solid;"></div>');
    if(config.label.props && config.label.props.length>0){
        var findProps = false;
        for(var i=0; i<config.label.props.length; i++){
            if(config.data[config.label.props[i]]){
                findProps = true;
                $label.append('<div>'+config.data[config.label.props[i]]+'</div>');
            }
        }
        if(findProps){
            $label.css({
                'background-color':'#'+(parseInt(config.label.backgroundColor).toString(16)),
                'border-color':'#'+(parseInt(config.label.lineColor).toString(16)),
                'color':'#'+(parseInt(config.label.color).toString(16)),
                'padding':'4px'
            });
        }
    }
    var label = new THREE.CSS2DObject($label[0]);
    label.position.set(0, $label[0].clientHeight/2+8, 0);
    this.obj3D.label = label;
    this.obj3D.core.add(label);
    return this;
};

/**
 * 连线
 * @param other 另一个Server对象
 */
Server.prototype.connect = function(other){
    var config = this.config;
    if(!this.obj3D.connections) this.obj3D.connections = [];
    var geometry = new THREE.Geometry();
    geometry.vertices.push(new THREE.Vector3(this.config.x, this.config.y, this.config.z));
    geometry.vertices.push(new THREE.Vector3(other.config.x, other.config.y, other.config.z));
    var connection = new THREE.Line(geometry, new THREE.LineBasicMaterial({color:this.config.connection.lineColor}));
    connection.position.set(-config.x, -config.y, -config.z);
    this.obj3D.connections.push(connection);
    this.obj3D.core.add(connection);
    return this;
};

/**
 * 设置位置
 * @param x x
 * @param y y
 * @param z z
 */
Server.prototype.setPosition = function(x, y, z){
    var config = this.config;
    config.x = x;
    config.y = y;
    config.z = z;
    this.obj3D.core.position.set(x, y, z);
    this.obj3D.line.position.set(x, y, z);
    return this;
};

/**
 * 销毁对象
 */
Server.prototype.dispose = function(){
    this.geometries.box.dispose();
    this.materials.iconMaterial.dispose();
    this.materials.iconMaterial.map.dispose();
    this.materials.surfaceMaterial.dispose();
    if(this.obj3D.connections && this.obj3D.connections.length>0){
        for(var i=0; i<this.obj3D.connections.length; i++){
            var line = this.obj3D.connections[i];
            line.geometry.dispose();
            line.material.dispose();
        }
    }
};

/**
 * @returns 对象类型
 */
Server.prototype.instanceOf = function(){
    return 'Server';
};
