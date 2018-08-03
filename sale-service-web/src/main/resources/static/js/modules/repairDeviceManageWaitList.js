/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table','laydate' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
    var $ = layui.$;
    
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
                    field : 'sn',
                    title : 'SN',
                    width : 120
                        
                },{
                    field : 'model',
                    title : '机型',
                    width : 160
                },{
                    field : 'status',
                    title : '状态',
                    width : 160,
                    templet: function(d){
                        return aRepairStatus.getDisplayText(d.status);
                    }
                },{
                    field:'endCustomerName',
                    title:"终端客户名称"
                },{
                    field:'repairPackage',
                    title:"联系人",
                    templet: function(d){
                        var rp = d.repairPackage;
                        if(undefined !==rp&& null!==rp){
                            return rp.contacts;
                        }
                        return "";
                    }
                },{
                    field:'repairPackage',
                    title:"联系电话",
                    templet: function(d){
                        var rp = d.repairPackage;
                        if(undefined !==rp&& null!==rp){
                            return rp.contactNumber;
                        }
                        return "";
                    }
                },{
                    field:'repairPackage',
                    title:"联系地址",
                    templet: function(d){
                        var rp = d.repairPackage;
                        if(undefined !==rp&& null!==rp){
                            return rp.contactAddress;
                        }
                        return "";
                    }
                },{
                    field:'attachment',
                    title:"附件"
                },{
                    field : 'detectUser',
                    title : '维修人',
                    templet: function(d){
                        var detectUser = d.detectUser;
                        if(undefined!==detectUser&&null!==detectUser){
                            return detectUser.userName;
                        }
                        return "";
                    }
                },{
                    field : 'quotationUser',
                    title : '报价人',
                    templet: function(d){
                        var quotationUser = d.quotationUser;
                        if(undefined!==quotationUser&&null!==quotationUser){
                            return quotationUser.userName;
                        }
                        return "";
                    }                    
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
        
    exports('repairDeviceManageWaitList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

