/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer','alog'],function(exports){
    
    var $ = layui.$;
    
    var alog=layui.alog;
    
    var obj = {
      
    };
  
  exports('aRepairBackPackage', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
