import Raphael from 'raphael';
;(function($) {
	//斜线表头
	$.fn.slashHeader = function(setting){
		/*
		* renderTo  渲染位置
		* headerArr  表头数组
		* proportion  表头数组百分比
		* initialPoint  起始点 top/bottom
		* stroke  斜线颜色
		* strokeWidth  斜线宽度
		* fontClass  标题文字样式
		* */
		var def = {
		    renderTo: $(document.body),
		    headerArr : [],
		    proportion: [],
		    initialPoint: 'top',
		    stroke: '#000',
		    strokeWidth: 1,
			fontClass: ''
	    };
		var options = $.extend({},def,setting);
		//计算坐标
		var pointArr = [],pointNum = 0,headerArr = options.headerArr;
		options.renderTo = (typeof options.renderTo == 'string' ? $(options.renderTo) : options.renderTo);
		if(options.headerArr.length < 2){
			if(options.headerArr.length < 1){
				return;
			}else{
				options.renderTo.text(options.headerArr[0]);
			}
		}
		
		options.renderWidth = options.renderTo.width();
		options.renderHeight = options.renderTo.height();
		options.renderTo.css({
			'overflow':'hidden',
			'position':'relative'
		});
		if(typeof options.initialPoint != 'string'){
			options.initialPoint = 'top';
		}else if(options.initialPoint != 'top' && options.initialPoint != 'bottom'){
			options.initialPoint = 'top';
		}
		if(options.initialPoint == 'top'){
			options.renderStart = {'x': 0,'y': 0};
			options.renderEnd = {'x': options.renderWidth,'y': options.renderHeight};
		}else{
			options.renderStart = {'x': 0,'y': options.renderHeight};
			options.renderEnd = {'x': options.renderWidth,'y': 0};
		}
		var proportion = options.proportion;
		pointNum = headerArr.length - 1;
		if(proportion.length > 0){
			proportion.length = headerArr.length;
			var totalPro = 0,empty = 0;
			for(var i = 0; i < proportion.length; i++){
				if(proportion[i]){
					proportion[i] = parseFloat(proportion[i]);
					totalPro = parseFloat(totalPro + proportion[i]);
				}else{
					empty++;
				}
			}
			if(totalPro > 0 && totalPro <= 100){
				if(totalPro != 100 || empty != 0){
					for(var i = 0; i < proportion.length; i++){
						if(!proportion[i]){
							proportion[i] = (100-totalPro)/empty;
						}
					}
				}
				setPointArrByPro();
			}else{
				srtAvePoint();
			}
		}else{
			srtAvePoint();
		}
		
		//开始绘制
		var elm = options.renderTo[0];
		var paper = new Raphael(elm);
		options.renderTo.find('svg').css('float','left');
		for(var i = 0; i < pointArr.length; i++){
			var pathStr = 'M' + options.renderStart.x + ',' + options.renderStart.y
						+ 'L' + pointArr[i].x + ',' + pointArr[i].y;
			paper.path(pathStr).attr({
		      "stroke": options.stroke,
		      "stroke-width": options.strokeWidth
			});
		}
		pointArr.push(options.renderEnd);	
		for(var i = 0; i < headerArr.length; i++){
			var fontStyle = '';
			if(options.initialPoint == 'top'){
				if(i == 0){
					fontStyle = 'left:0;bottom:0;';
				}else if(Math.abs(pointArr[i].y - pointArr[i-1].y) > 0){
					if(i == headerArr.length - 1){
						var divY = 0;
					}else{
						var divY = pointArr[i].y;
					}
					fontStyle = 'right:0;top:'+divY+'px';
				}else if(Math.abs(pointArr[i].x - pointArr[i-1].x) > 0){
					fontStyle = 'left:'+pointArr[i-1].x+'px;bottom:0;';
				}else if(headerArr.length == 2 && i == 1){
					fontStyle = 'right:0;top:0';
				}
			}else{
				if(i == 0){
					fontStyle = 'left:0;top:0;';
				}else if(Math.abs(pointArr[i].y - pointArr[i-1].y) > 0){
					if(i == headerArr.length - 1 ){
						var divY = 0;
					}else{
						var divY = options.renderStart.y - pointArr[i].y;
					}
					fontStyle = 'right:0;bottom:'+divY+'px';
				}else if(Math.abs(pointArr[i].x - pointArr[i-1].x) > 0){
					fontStyle = 'left:'+pointArr[i-1].x+'px;top:0;';
				}else if(headerArr.length == 2 && i == 1){
					fontStyle = 'right:0;bottom:0';
				}
			}
			var div = '<div class="'+options.fontClass+'" style="position:absolute;'
					+''+fontStyle+'">'+headerArr[i]+'</div>';
			options.renderTo.append(div);
		}
		function srtAvePoint(){
			if(headerArr.length%2 == 0){
				pointArr[headerArr.length/2-1] = options.renderEnd;
				if(pointNum-1 > 0){
					setPointArr();
				}
			}else{
				setPointArr();
			}
		}
		function setPointArrByPro(){
			for(var i = 0; i < proportion.length; i++){
				if(!proportion[i] || proportion[i] == 0){
					proportion.splice(i,1);
					headerArr.splice(i,1);
					i--;
				}
			}
			var proArea = 0;
			for(var i = 0; i < proportion.length; i++){
				proArea +=  proportion[i];
				if(proArea == 100){
					return;
				}else if(proArea > 50){
					var item = {};
					item.x = options.renderWidth;
					if(options.initialPoint == 'top'){
						item.y = options.renderHeight - options.renderHeight * 2 *(proArea-50)/100
					}else{
						item.y = options.renderHeight * 2 *(proArea-50)/100
					}
					pointArr[i] = item;
				}else{
					var item = {
						'x':options.renderWidth * 2 *proArea/100
					}
					if(options.initialPoint == 'top'){
						item.y = options.renderEnd.y;
					}else{
						item.y = 0;
					}
					pointArr[i] = item;
				}
			}
		}
		function setPointArr(){
			for(var i = 0; i < pointNum; i++){
				if(i < (headerArr.length/2-1)){
					var item = {
						'x':Number((options.renderWidth / (headerArr.length/2) * (i-0+1)).toFixed(2))
					}
					if(options.initialPoint == 'top'){
						item.y = options.renderEnd.y;
					}else{
						item.y = 0;
					}
					pointArr[i] = item;
				}else if(i > (headerArr.length/2-1)){
					var item = {};
					item.x = options.renderWidth;
					if(options.initialPoint == 'top'){
						item.y = Number((options.renderHeight - options.renderHeight/ (headerArr.length/2) * (i-headerArr.length/2+1)).toFixed(2));
					}else{
						item.y = Number((options.renderHeight/ (headerArr.length/2) * (i-headerArr.length/2+1)).toFixed(2));
					}
					pointArr[i] = item;
				}
			}
		}
	}
})(jQuery);