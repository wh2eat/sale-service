/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer','aform' ],function(exports){
    
    var $ = layui.$;
    
    var aform = layui.aform;
    
    var obj = {
      getId:function(){
          var userObj = aform.toObject($("form[name=globalHidenForm]"));
          return userObj.id; 
      },
      getType:function(){
          var userObj = aform.toObject($("form[name=globalHidenForm]"));
          return userObj.type; 
      },
      getRepairStationId:function(){
          var userObj = aform.toObject($("form[name=globalHidenForm]"));
          return userObj.loginRepairStationId;
      }
    };
  
  exports('aUser', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
