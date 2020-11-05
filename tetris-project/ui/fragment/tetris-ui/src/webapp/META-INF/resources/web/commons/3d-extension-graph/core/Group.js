/**
 * {
 *     data:数据缓存
 *     width:长
 *     height:宽
 *     x:x
 *     y:y
 *     z:z
 *     lineColor:描边颜色
 *     dashSize:虚线尺寸
 *     gapSize:虚线间隔尺寸
 * }
 */
function Group(config){
    this.config = config;
    this.config.data.THREE = this;

    this.geometries = {
        plane:new THREE.PlaneBufferGeometry(config.width, config.height)
    };

    this.geometries.plane.rotateX(-Math.PI/2);

    this.obj3D = {
        core:new THREE.LineSegments(new THREE.EdgesGeometry(this.geometries.plane), new THREE.LineDashedMaterial({
            color:config.lineColor,
            linewidth: 1,
            scale:1,
            dashSize:config.dashSize,
            gapSize:config.gapSize
        }))
    };

    this.obj3D.core.computeLineDistances();
    this.obj3D.core.position.set(config.x, config.y, config.z);

}

/**
 * 设置位置
 * @param x x
 * @param y y
 * @param z z
 * @returns this
 */
Group.prototype.setPosition = function(x, y, z){
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
Group.prototype.instanceOf = function(){
    return 'Group';
};
