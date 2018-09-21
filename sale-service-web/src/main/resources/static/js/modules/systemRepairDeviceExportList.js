/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer','ajx', 'table' ,'element','layout','aform','apopup','alog','aUser','laydate' ], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog; 
    
    var aUser = layui.aUser;
    
    var ajx = layui.ajx;
    
    var laydate = layui.laydate;
    laydate.render({ 
        elem: '#srdeStartTime' //或 elem: document.getElementById('test')、elem: lay('#test') 等
    });
    
    laydate.render({ 
        elem: '#srdeEndTime' //或 elem: document.getElementById('test')、elem: lay('#test') 等
    });
    
    laydate.render({ 
        elem: '#searchCreateTime' //或 elem: document.getElementById('test')、elem: lay('#test') 等
    });
    
    var systemRepairDeviveExportPageIdx = null;
    
    $("button[name=addExportTaskBtn]").click(function(){
        
        var popupTitle="导出维修报表";
        var areaInfo = apopup.getArea($("#systemRepairDeviveExportPage"));
        alog.d(areaInfo);
        
        systemRepairDeviveExportPageIdx = layer.open({
            id:"systemRepairDeviveExportPageId",
            type: 1,
            title:  [popupTitle, 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width,areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#systemRepairDeviveExportPage')
        });
        return false;
    });
    
    form.on("submit(systemRepairDeviceExportFormSubmitFilter)",function(data){
        alog.d(data);
        var postData={};
        postData.param = data.field;
        postData.userId=aUser.getId();
        alog.d(postData);
        ajx.post({
            "url" : "api/browser/sys/export/repairDevice",
            "data" :  JSON.stringify(postData)
        },
        function(rtn) {
            if(rtn){
                layer.msg("导出任务创建成功!");
                layer.close(systemRepairDeviveExportPageIdx);
                reloadRepairDeviceExportListTable();
            }else{
                layer.msg("操作失败，请重试!");
            }
        });
        return false;
    });
    
    //
    var repairDeviceExportListUrl='api/browser/sys/export/find/list';
    table.render({
        id : 'repairDeviceExportListTableId',
        elem : '#repairDeviceExportListTable',
        page : true,
        loading: true,
        skin:'row',
        even: false,
        limits:[10,20,50,100],
        cols : [ [ {
            title : 'ID',
            fixed : 'left',
            width : 60,
            templet: function(row){
                return row.LAY_INDEX;
              }
        }, {
            field : 'taskName',
            title : '任务名称'
        },{
            field : 'status',
            title : '状态',
            templet: function(d){
                var status = d.status;
                if('0'==status){
                    return "已创建";
                }else if('1'==status){
                    return "正在导出";
                }else if('9'==status){
                    return '已完成';
                }else if('-1'==status){
                    return '导出失败';
                }else{
                    return  '未知('+status+')';
                }
            }
        },{
            field:'remark',
            title:"备注"
        },{
            field : 'createTime',
            title : '创建时间'
        },{
            title : '操作',
            fixed: 'right', 
            width:120, 
            align:'center', 
            toolbar: '#repairDeviceExportTableToolTpl'
        }  ] ],
        url : repairDeviceExportListUrl,
        method : "POST",
        where:{params:JSON.stringify(getFilterData())},
        done: function(res, curr, count){
        }
    });
    
    function getFilterData(){
        var filterData = aform.toObject($("form[name=repairDeviceExportSearchForm]"));
        filterData.userId=aUser.getId();
        return filterData;
    }
    
    function reloadRepairDeviceExportListTable(){
        table.reload('repairDeviceExportListTableId', {
            url: repairDeviceExportListUrl,
            method : "POST",
            where:{params:JSON.stringify(getFilterData())}
          });
        return false;
    }
    
    table.on('tool(repairDeviceExportListTableFilter)', function(obj){
        alog.d(obj);
        var event = obj.event;
        if('del'==event){
            var data = obj.data;
            if(!(-1==data.status||9==data.status)){
                layer.alert("当前任务未完成，无法删除!");
                return false;
            }
            
            layer.confirm("是否确定删除任务："+data.taskName+"?",function(){
                var postData={};
                postData.taskId=data.id;
                postData.userId=aUser.getId();
                
                ajx.post({
                    "url" : "api/browser/sys/export/delete",
                    "data" :  JSON.stringify(postData)
                },
                function(rtn) {
                    if(rtn){
                        layer.msg("操作成功!");
                        reloadRepairDeviceExportListTable();
                    }else{
                        layer.msg("操作失败!");
                    }
                });
            });
        }
       
        return false;
    });
    
    form.on("submit(repairDeviceExportSearchFormSubmitFilter)",function(data){
        reloadRepairDeviceExportListTable();
        return false;
    });
            
    exports('systemRepairDeviceExportList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

