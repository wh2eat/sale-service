/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table' ,'element','layout','aSystemCustomer','aform','apopup','alog' ], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog; 
    
    var aSystemCustomer = layui.aSystemCustomer;
    
    aSystemCustomer.renderTable();
    
    
    exports('systemCustomerList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

