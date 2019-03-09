/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table' ,'element','aform','apopup','alog' ], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog; 
        
    table.render({
        id : 'customerSuggestionListTableId',
        elem : '#customerSuggestionListTable',
        page : true,
        loading: true,
        skin:'row',
        even: false,
        limits:[20,50,100],
        cols : [ [ {
            title : 'ID',
            fixed : 'left',
            width : 60,
            templet: function(row){
                return row.LAY_INDEX;
              }
        },{
            field : 'category',
            title : '反馈',
            templet: function(d){
                var cd = d.createTime; 
                return d.category+"，"+d.deviceAmount+"台，"+cd.split(" ")[0];
              }
        },{
            fixed: 'right', 
            width:65, 
            align:'left', 
            toolbar: '#customerSuggestionListTableToolTpl'
        }  ] ],
        url : '/web/api/browser/customer/suggestion/find/list',
        method : "POST",
        where:{params:JSON.stringify({})},
        done: function(res, curr, count){
            alog.d("done");
        }
    });
    
    table.on('tool(customerSuggestionListTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var udid = data.serialNumber;
        var id = data.id;
        if(layEvent === 'detail'){ //查看
            
        } 
        return false;
      });
    
    exports('customerSuggestionList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

