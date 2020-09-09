/**
 * {
 *     data:数据缓存
 *     width:长
 *     height:宽
 *     x:x
 *     y:y
 *     z:z
 *     backgroundColor:背景色
 *     text:文本
 *     fontSize:文本字号
 *     color:文本颜色
 * }
 */
function Type(config){
    this.config = config;
    this.config.data.THREE = this;

    this.geometries = {
        plane:new THREE.Geometry()
    };
    this.geometries.plane.fromBufferGeometry(new THREE.PlaneBufferGeometry(config.width, config.height));
    this.geometries.plane.vertices.push(new THREE.Vector3(config.width/2+config.height/2, 0, 0));
    this.geometries.plane.faces.push(new THREE.Face3(1,3,4));

    this.materials = {
        plane:new THREE.MeshBasicMaterial({color:config.backgroundColor})
    };

    this.obj3D = {
        core:new THREE.Mesh(this.geometries.plane, this.materials.plane),
        cloneBottom:new THREE.Mesh(this.geometries.plane, this.materials.plane)
    };
    this.obj3D.core.position.set(config.x, config.y, config.z);
    this.obj3D.core.rotateX(-Math.PI/2);
    this.obj3D.cloneBottom.position.set(0,0,0);
    this.obj3D.cloneBottom.rotateX(-Math.PI);
    this.obj3D.core.add(this.obj3D.cloneBottom);

    var self = this;
    var loader = new THREE.FontLoader();
    loader.load( '/web/commons/3d-extension-graph/fonts/Microsoft YaHei_Regular.json', function(font){
        self.materials.text = new THREE.MeshBasicMaterial({color:config.color});
        self.geometries.text = new THREE.TextBufferGeometry(config.text, {
            font:font,
            size:config.fontSize,
            height:.1,
            curveSegments:1,
            bevelEnabled:false,
            bevelThickness:10,
            bevelSize:8,
            bevelOffset:0,
            bevelSegments:5
        });
        self.obj3D.text = new THREE.Mesh(self.geometries.text, self.materials.text);
        var letterWidth = (config.fontSize/8)*10/2;
        var letterHeight = (config.fontSize/9)*10;
        self.obj3D.text.position.set(-letterWidth*config.text.replace(/[^x00-xff]/g,"01").length/2, -letterHeight/2, 0);
        self.obj3D.core.add(self.obj3D.text);
    });
}

/**
 * 设置位置
 * @param x x
 * @param y y
 * @param z z
 */
Type.prototype.setPosition = function(x, y, z){
    var config = this.config;
    config.x = x;
    config.y = y;
    config.z = z;
    this.obj3D.core.position.set(x, y, z);
    return this;
};

/**
 * @returns 对象类型
 */
Type.prototype.instanceOf = function(){
    return 'Type';
};
