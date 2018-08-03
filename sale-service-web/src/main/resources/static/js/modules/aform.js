/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer' ],function(exports){
    
    var $ = layui.$;
    
    var obj = {
      toObject:function(fromObject){
          var o = {};
          var a = $(fromObject).serializeArray();
          $.each(a, function() {
              if (o[this.name] !== undefined) {
                  if (!o[this.name].push) {
                      o[this.name] = [o[this.name]];
                  }
                  o[this.name].push(this.value || '');
              } else {
                  o[this.name] = this.value || '';
              }
          });
          return o;
      },
      getVal:function(fromObject,fname){
          var fval = '';
          var a = $(fromObject).serializeArray();
          $.each(a, function() {
              if (undefined !== this.name 
                      && this.name == fname) {
                  fval = this.value;
                  return false;
              }
          });
          return fval;
      }
    };
  
  exports('aform', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
