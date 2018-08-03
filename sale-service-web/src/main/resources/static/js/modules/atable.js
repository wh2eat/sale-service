/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer' ],function(exports){
    
    var $ = layui.$;
    
    function _getfv(obj,fieldName){
        
        if(undefined===obj){
            return "";
        }
        
        if(null===obj){
            return "";
        }
        
        if(typeof obj !=='object'){
            return "";
        }
        
        if (fieldName.indexOf(".")==-1) {
          return obj[fieldName];
        }
        
        var fns = fieldName.split(".");
        var length = fns.length;
        var cobj = obj;
        for (var ii = 0; ii < length ; ii++) {
            cobj = cobj[fns[ii]];
            if (undefined === cobj) {
                return "";
            }
            
            if (null === cobj) {
                return "";
            }
        }
        return cobj;
    }
    
    var obj = {
      getfv:function(obj,fieldName){
          return _getfv(obj,fieldName);
      }
    };
  
  exports('atable', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
