/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer' ],function(exports){
    
    var $ = layui.$;
    
    var isDebug = false;
    
    var obj = {
      d:function(obj){
          if(isDebug){
              console.log(obj);
          }
      }
    };
  
  exports('alog', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
