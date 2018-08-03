/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer' ],function(exports){
    
    var $ = layui.$;
        
    var obj = {
      getArea:function(pageId,isView){
          
          var area={};
          
          var mainHeight = $(window).height();//$(".sv-main-content").height();
          var mainWidth = $(window).width();//$(".sv-main-content").width();
          
          var popupHeight = $(pageId).attr("fitHeight");
          var popupWidth = $(pageId).attr("fitWidth");
          
          var areaWidth=popupWidth;
          if(mainWidth<areaWidth){
              areaWidth = mainWidth;
          }
          
          var left = (mainWidth-areaWidth)/2
          area.left = left;
          
          areaWidth = areaWidth+"px";
          area.width=areaWidth;

          
          var areaHeight = popupHeight;
          if(mainHeight<areaHeight){
              areaHeight = mainHeight;
          }
          if(isView){
              areaHeight = areaHeight-20;
          }
          var top=(mainHeight-areaHeight)/2;
          area.top = top;
          areaHeight = areaHeight+"px";
          area.height=areaHeight;
          
          return area;
      }
    };
  
  exports('apopup', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
