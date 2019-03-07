/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer' ],function(exports){
    
    var $ = layui.$;
    
    layui.extend({
        jweixin: '{/}http://res.wx.qq.com/open/js/jweixin-1.4.0' // {/}的意思即代表采用自有路径，即不跟随 base 路径
    })
    
    console.log(jweixin);  
    
  exports('jweixin',jweixin); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
