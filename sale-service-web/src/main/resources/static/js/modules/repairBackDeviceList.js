/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table' ,'laydate','element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
    var $ = layui.$;
    
    var laydate = layui.laydate;
    laydate.render({ 
        elem: '#deviceSearch4BackTime' //或 elem: document.getElementById('test')、elem: lay('#test') 等
    });
    
    var form = layui.form;
    
    var aform = layui.aform;

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
                    templet: function(d){
                        return d.repairInvoice.serialNumber;
                    }
                },{
                    field : 'repairPacakge',
                    title : '维修包编号',
                    templet: function(d){
                        var repairPackage = d.repairPackage;
                        return repairPackage.serialNumber;
                    }
                },{
                    field : 'sn',
                    title : 'SN',
                    width : 150
                        
                },{
                    field : 'model',
                    title : '机型',
                    width : 150
                        
                },{
                    field : 'repairBackPacakge',
                    title : '返客快递单',
                    templet: function(d){
                        var repairBackPackage = d.repairBackPackage;
                        alog.d(repairBackPackage);
                        if(undefined === repairBackPackage || null===repairBackPackage){
                            return "";
                        }
                        return repairBackPackage.expressName+":"+repairBackPackage.expressNumber;
                    }
                },{
                    field : 'backTime',
                    title : '发货时间'
                },{
                    field : 'createTime',
                    title : '送修时间'
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
        
    $("button[name=confirmRepairFinishBtn]").click(function(){
        var cs =  table.checkStatus('repairDeviceManageListTableId');
        alog.d(cs);        
        var datas = cs.data;
        if(datas.length<=0){
            layer.msg("请选择设备!");
            return false;
        }
        layer.confirm('是否确定选中设备已完成维修?', function(index){
            layer.close(index);
            var ids=[];
            var length = datas.length;
            for(var ii = 0;ii<length;ii++){
                var data = datas[ii];
                ids.push(data.id);
            }
            alog.d(ids);
            
            ajx.post({
                "url" : "api/browser/repair/device/finish/confirm",
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
        
    exports('repairBackDeviceList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

