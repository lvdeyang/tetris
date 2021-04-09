;(function($, window, document,undefined) {
	console.log('drag.js');
    //定义的构造函数
    var Drag = function(ele, opt) {
        this.$ele = ele,
				this.x = 0,
				this.y = 0,
        this.defaults = {
					parent:'parent',
					randomPosition:true,
					direction:'all',
					handler:false,
					dragBegin:function(x,y){},
					dragEnds:function(x,y){},
					dragMoves:function(x,y){}
				},
				this.options = $.extend({}, this.defaults, opt)
    }
    //定义方法
    Drag.prototype = {
        run: function() {
					var $this = this;
					var element = this.$ele;
					var randomPosition  = this.options.randomPosition; //位置
					var direction = this.options.direction; //方向
					var handler = this.options.handler;
					var parent = this.options.parent;
					var isDown = false; //记录鼠标是否按下
					var fun = this.options; //使用外部函数
					var X = 0,
							Y = 0,
							moveX,
							moveY;
					// 阻止冒泡
					//element.find('*').not('img').mousedown(function(e) {
					//	e.stopPropagation();
					//});
					//初始化判断
					if(parent == 'parent'){
						parent = element.parent();
					}else{
						parent = element.parents(parent);
					}
					if(!handler){
						handler = element;
						handler.css({cursor:'move'});
					}else{
						handler = element.find(handler);
						handler.css({cursor:'move'});
					}
					//初始化
					parent.css({position:'relative'});
					element.css({position:'absolute'});
					var boxWidth=0,boxHeight=0,sonWidth=0,sonHeight=0;
					//盒子 和 元素大小初始化
					initSize();
					if(randomPosition){randomPlace();}
					$(window).resize(function(){
						initSize();
						if(randomPosition){randomPlace();}
					});
					//盒子 和 元素大小初始化函数
					function initSize(){
						boxWidth = parent.outerWidth();
						boxHeight = parent.outerHeight();
						sonWidth = element.outerWidth();
						sonHeight = element.outerHeight();
					}
					//位置随机函数
					function randomPlace(){
						if(randomPosition){
							var randX = parseInt(Math.random()*(boxWidth-sonWidth));
							var randY = parseInt(Math.random()*(boxHeight-sonHeight));
							if(direction.toLowerCase() == 'x'){
								element.css({left:randX});
							}else if(direction.toLowerCase() == 'y'){
								element.css({top:randY});
							}else{
								element.css({left:randX,top:randY});
							}
						}
					}
					handler.on('mousedown.drag.ext',function(e){
						element = $(this);
						direction = $(this).data('myDrag').options.direction;
						isDown = true;
						X = e.pageX;
						Y = e.pageY;
						$this.x = element.position().left;
						$this.y = element.position().top;
						element.addClass('on');
						fun.dragBegin(parseInt(element.css('left')),parseInt(element.css('top')));

						$(document).on('mousemove.drag.ext', function(e){
							moveX = $this.x+e.pageX-X;
							moveY = $this.y+e.pageY-Y;
							function thisXMove(){ //x轴移动
								if(isDown == true){
									element.css({left:moveX});
								}else{
									return;
								}
								if(moveX < 0){
									element.css({left:0});
								}
								if(moveX > (boxWidth-sonWidth)){
									element.css({left:boxWidth-sonWidth});
								}
								return moveX;
							}
							function thisYMove(){ //y轴移动
								if(isDown == true){
									element.css({top:moveY});
								}else{
									return;
								}
								if(moveY < 0){
									element.css({top:0});
								}
								if(moveY > (boxHeight-sonHeight)){
									element.css({top:boxHeight-sonHeight});
								}
								return moveY;
							}
							function thisAllMove(){ //全部移动
								if(isDown == true){
									element.css({left:moveX,top:moveY});
								}else{
									return;
								}
								if(moveX < 0){
									element.css({left:0});
								}
								if(moveX > (boxWidth-sonWidth)){
									element.css({left:boxWidth-sonWidth});
								}
								if(moveY < 0){
									element.css({top:0});
								}
								if(moveY > (boxHeight-sonHeight)){
									element.css({top:boxHeight-sonHeight});
								}
							}
							if(isDown){
								fun.dragMoves(parseInt(element.css('left')),parseInt(element.css('top')));
							}else{
								return false;
							}
							if(direction.toLowerCase() == "x"){
								thisXMove();
							}else if(direction.toLowerCase() == "y"){
								thisYMove();
							}else if(direction.toLowerCase() === 'none'){
								//不移动
							}else{
								thisAllMove();
							}
						});

						$(document).on('mouseup.drag.ext',function(e){
							fun.dragEnds.apply(element, [parseInt(element.css('left')),parseInt(element.css('top'))]);
							element.removeClass('on');isDown = false;
							$(document).unbind('mousemove.drag.ext').unbind('mouseup.drag.ext');
						});

						return false;
					});

        },

		api:{
			stopMove:function(){
				this.options.bak_direction = this.options.direction;
				this.options.direction = 'none';
			},
			revertMove:function(){
				this.options.direction = this.options.bak_direction;
				this.options.bak_direction = null;
			}
		}
    };

    //插件
    $.fn.myDrag = function(options) {
		if(typeof options === 'string'){
			var args = [];
			for(var i=1; i<arguments.length; i++){
				args.push(arguments[i]);
			}
			var drag = this.data('myDrag');
			if(!drag) return this;
			drag.api[options].apply(drag, args);
		}else if(typeof options === 'object'){
			//创建实体
			var drag = new Drag(this, options);
			//调用方法
			drag.run();
			this.data('myDrag', drag);
		}
		return this;
    }
})(jQuery, window, document);