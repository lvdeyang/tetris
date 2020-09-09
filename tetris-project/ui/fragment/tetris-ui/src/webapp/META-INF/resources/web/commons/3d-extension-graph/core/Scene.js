/**
 * {
 *     $el:jquery dom
 *     backgroundColor:背景色
 *     camera:{
 *         x:'相机x',
 *         y:'相机y',
 *         z:'相机z',
 *         near:'相机近视点',
 *         far:'相机远视点',
 *         target:{
 *             x:'相机目标x',
 *             y:'相机目标y',
 *             z:'相机目标z'
 *         }
 *     }
 * }
 */
function Scene(config){
    this.config = config;
    config.$el.css({'position':'relative'});

    this.raycaster = new THREE.Raycaster();

    this.renders = {
        webgl:new THREE.WebGLRenderer({antialias:true}),
        css2D:new THREE.CSS2DRenderer()
    };
    this.renders.webgl.setPixelRatio(window.devicePixelRatio);
    this.resize();

    config.$el.append(this.renders.webgl.domElement);
    $(this.renders.webgl.domElement).css({
        'position':'absolute',
        'left':0,
        'top':0
    });

    config.$el.append(this.renders.css2D.domElement);
    $(this.renders.css2D.domElement).css({
        'position':'absolute',
        'left':0,
        'top':0
    });

    this.scene = new THREE.Scene();
    this.scene.background = new THREE.Color(config.backgroundColor);

    this.camera = new THREE.PerspectiveCamera(45, config.$el[0].clientWidth/config.$el[0].clientHeight, config.camera.near, config.camera.far);
    this.camera.position.set(config.camera.x, config.camera.y, config.camera.z);

    this.controls = new THREE.OrbitControls(this.camera, this.renders.css2D.domElement);
	this.controls.target = new THREE.Vector3(config.camera.target.x, config.camera.target.y, config.camera.target.z);
    this.controls.update();

    this.dispatchEvent();

    var self = this;
    var animate = function(){
        requestAnimationFrame(animate);
        self.controls.update();
        self.renders.webgl.render(self.scene, self.camera);
        self.renders.css2D.render(self.scene, self.camera);
    };
    animate();

    /*var self = this;
     this.controls.addEventListener('change', function(){
     console.log(self.camera);
     console.log(self.camera.position);
     console.log(self.controls.target);
     console.log('--------------');
     });*/
}

/**
 * 调度事件
 */
Scene.prototype.dispatchEvent = function(){
    var self = this;
    var dispatchEvent = function(type, mouse){
        self.raycaster.setFromCamera(mouse, self.camera);
        var intersects = self.raycaster.intersectObjects(self.scene.children);
        if(intersects.length > 0){
            for(var i=0; i<intersects.length; i++){
                $(intersects[i].object).trigger(type);
            }
        }
    };

    //点击事件
    $(self.renders.css2D.domElement).on('click', function(event){
        event.preventDefault();
        var rect = this.getBoundingClientRect();
        dispatchEvent('click', new THREE.Vector2(((event.clientX-rect.left)/rect.width)*2-1, -((event.clientY-rect.top)/rect.height)*2+1));
    });

    //处理鼠标移入移出
    if(!this.mouseinObjs) self.mouseinObjs = [];

    $(self.renders.css2D.domElement).on('mousemove', function(event){
        event.preventDefault();
        var rect = this.getBoundingClientRect();
        self.raycaster.setFromCamera(new THREE.Vector2(((event.clientX-rect.left)/rect.width)*2-1, -((event.clientY-rect.top)/rect.height)*2+1), self.camera);
        var intersects = self.raycaster.intersectObjects(self.scene.children);
        if(intersects.length > 0){
            var _objArr = [];
            for(var i=0; i<intersects.length; i++){
                var finded = false;
                for(var j=0; j<self.mouseinObjs.length; j++){
                    if(self.mouseinObjs[j] === intersects[i].object){
                        finded = true;
                        break;
                    }
                }
                _objArr.push(intersects[i].object);
                if(!finded){
                    $(intersects[i].object).trigger('mouseover');
                }
            }
            for(var i=0; i<self.mouseinObjs.length; i++){
                var finded = false;
                for(var j=0; j<_objArr.length; j++){
                    if(self.mouseinObjs[i] === _objArr[j]){
                        finded = true;
                        break;
                    }
                }
                if(!finded){
                    $(self.mouseinObjs[i]).trigger('mouseout');
                }
            }
            self.mouseinObjs.splice(0, self.mouseinObjs.length);
            for(var i=0; i<_objArr.length; i++){
                self.mouseinObjs.push(_objArr[i]);
            }
        }else{
            for(var i=0; i<self.mouseinObjs.length; i++){
                $(self.mouseinObjs[i]).trigger('mouseout');
            }
            self.mouseinObjs.splice(0, self.mouseinObjs.length);
        }
    });
    return this;
};

/**
 * 设置背景色
 * @param backgroundColor 背景色
 */
Scene.prototype.setBackgroundColor = function(backgroundColor){
    var config = this.config;
    config.backgroundColor = backgroundColor;
    this.scene.background = new THREE.Color(backgroundColor);
    return this;
};

/**
 * 尺寸改变重绘
 */
Scene.prototype.resize = function(){
    var config = this.config;
    this.renders.webgl.setSize(config.$el[0].clientWidth, config.$el[0].clientHeight);
    this.renders.css2D.setSize(config.$el[0].clientWidth, config.$el[0].clientHeight);
};

/**
 * 从场景中移除对象
 * @param obj 拓补图对象
 */
Scene.prototype.remove = function(obj){
    this.scene.remove(obj.obj3D.core);
    return this;
};

/**
 * 批量从场景中移除对象
 * @param obj 拓补图对象数组
 */
Scene.prototype.removeAll = function(objs){
    for(var i=0; i<objs.length; i++){
        this.remove(objs[i]);
    }
    return this;
};

/**
 * 向场景中添加对象
 * @param obj 拓补图对象
 * @returns this
 */
Scene.prototype.add = function(obj){
    this.scene.add(obj.obj3D.core);
    return this;
};

/**
 * 批量向场景中添加对象
 * @param objs 拓补图对象数组
 * @returns this
 */
Scene.prototype.addAll = function(objs){
    for(var i=0; i<objs.length; i++){
        this.add(objs[i]);
    }
    return this;
};

/**
 * @returns 对象类型
 */
Scene.prototype.instanceOf = function(){
    return 'Scene';
};
