/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define(['layer', 'form','alog'], function(exports){
    var form = layui.form;
    var alog = layui.alog;

    //监听提交
    form.on('submit(loginBtn)', function(data) {
        alog.d(JSON.stringify(data.field));
    });
  
  exports('login', {}); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
