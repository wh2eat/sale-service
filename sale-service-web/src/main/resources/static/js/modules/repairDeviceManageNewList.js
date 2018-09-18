/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table','laydate' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;
    
    var laydate = layui.laydate;

    var table = layui.table;
    
    var aRepairInvoice = layui.aRepairInvoice;
    
    var aRepairPackage = layui.aRepairPackage;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog;
    
    var aDevice = layui.aDevice;
    
    var aRepairStatus = layui.aRepairStatus;
    
    var ajx = layui.ajx;
    
    var repairDeviceTableUrl='api/browser/repair/device/find/list';
    
    function renderListTable(){
        
        var conditions = getConditions();
        alog.d(conditions);
        
        table.render({
            id : 'repairDeviceManageListTableId',
            elem : '#repairDeviceManageListTable',
            page : true,
            loading: true,
            skin:'row',
            even: false,
            limits:[10,20,50,100],
            cols : [ [
                {checkbox:true},{
                    field : 'repairInvoice',
                    title : '维修单号',
                    width : '10%',
                    templet: function(d){
                        return d.repairInvoice.serialNumber;
                    }
                },{
                    field : 'repairPacakge',
                    title : '快递信息',
                    width : '10%',
                    templet: function(d){
                        var repairPackage = d.repairPackage;
                        return repairPackage.expressName+":"+repairPackage.expressNumber;
                    }
                },{
                    field : 'endCustomerName',
                    title : '终端客户名称'
                },{
                    field : 'sn',
                    title : 'SN',
                    width : '10%'
                        
                },{
                    field : 'status',
                    title : '状态',
                    width : '8%',
                    templet: function(d){
                        return aRepairStatus.getDisplayText(d.status);
                    }
                },{
                    field:'warrantyType',
                    title:"保修类型",
                    width : '8%',
                    templet: function(d){
                        return aDevice.getWarrantyText(d.warrantyType);
                    }
                },{
                    field : 'model',
                    title : '机型',
                    width : '5%',
                }, {
                    field : 'repairTimes',
                    title : '维修次数',
                    width : '8%'
                }, {
                    field : 'manufactureTime',
                    title : '出厂日期',
                    width : '10%',
                    templet: function(d){
                        var manufactureTime = d.manufactureTime;
                        return manufactureTime.substring(0,10);
                    }
                }, {
                    field : 'createTime',
                    title : '送修日期',
                    width : '10%'
                }  ] ],
                url : repairDeviceTableUrl,
                method : "POST",
            where:{params:JSON.stringify(conditions)},
            done: function(res, curr, count){
            }
        });
    }
    renderListTable();
    
    function getConditions(){
        return aform.toObject("form[name=repairDeviceSearchForm]");
    }
    
    function reloadRepairDeviceListTable(){
        var conditions = getConditions();
        alog.d(conditions);
        
        table.reload('repairDeviceManageListTableId', {
            url: repairDeviceTableUrl,
            method : "POST",
            where:{params:JSON.stringify(conditions)}
          });
    }
    
    $("form[name=repairDeviceSearchForm]").submit(function(){
        reloadRepairDeviceListTable();
        return false;
    });
        
    $("button[name=commitRepairDevice2CheckBtn]").click(function(){
        var cs =  table.checkStatus('repairDeviceManageListTableId');
        alog.d(cs);        
        var datas = cs.data;
        if(datas.length<=0){
            layer.msg("请选择需要提交检测的设备!");
            return false;
        }
        layer.confirm('是否确定将选中设备提交检测?', function(index){
            layer.close(index);
            var ids=[];
            var length = datas.length;
            for(var ii = 0;ii<length;ii++){
                var data = datas[ii];
                ids.push(data.id);
            }
            alog.d(ids);
            
            ajx.post({
                "url" : "api/browser/repair/device/commit/check",
                "data" :  JSON.stringify({"repairDeviceIds":ids})
            },
            function(rtn) {
                if(rtn){
                    layer.msg("操作成功!");
                    reloadRepairDeviceListTable();
                }else{
                    layer.msg("操作失败，请重试!");
                }
            });
            
        });
        return false;
    });
        
    exports('repairDeviceManageNewList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

