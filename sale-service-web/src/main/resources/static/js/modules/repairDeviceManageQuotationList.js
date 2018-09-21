/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer','laydate', 'table', 'aUser' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;
    
    var  laydate = layui.laydate;
    laydate.render({ 
        elem: '#deviceSearch4DeliveryTime' //或 elem: document.getElementById('test')、elem: lay('#test') 等
    });

    var table = layui.table;
    
    var aUser= layui.aUser;
    
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
            cols : [ [{checkbox:true},{
                    field : 'repairInvoice',
                    title : '维修单号',
                    width : '12%',
                    templet: function(d){
                        return d.repairInvoice.serialNumber;
                    }
                },{
                    field : 'endCustomerName',
                    title : '终端客户名称',
                    width : '12%'                        
                },{
                    field : 'sn',
                    title : 'SN',
                    width : '10%',                        
                },{
                    field:'warrantyType',
                    title:"保修类型",
                    width : '10%',
                    templet: function(d){
                        return aDevice.getWarrantyText(d.warrantyType);
                    }
                },{
                    field : 'model',
                    title : '机型',
                    width : '10%'
                },{
                    field : 'detectUser',
                    title : '维修人',
                    width : '10%',
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
                    width : '10%',
                    templet: function(d){
                        var quotationUser = d.quotationUser;
                        if(undefined!==quotationUser&&null!==quotationUser){
                            return quotationUser.userName;
                        }
                        return "";
                    }                    
                },{
                    field : 'costTotal',
                    title : '总费用',
                    width : '12%',
                    templet: function(d){
                        
                        var costTotal = d.costTotal;
                        if (undefined===costTotal || null===costTotal) {
                            return "未报价";
                        }
                        return d.costTotal+"（"+aDevice.getCurrency(d.currency)+"）";
                    }                       
                },{
                    align:'center', 
                    toolbar: '#repairDeviceManageListTableToolTpl'
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
    
    table.on('tool(repairDeviceManageListTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var id = data.id;
        var sn = data.sn;
    });
    
    $("button[name=refuse-quotation]").click(function(){
        var checkStatus = table.checkStatus('repairDeviceManageListTableId');
        var datas = checkStatus.data;
        var ls = datas.length;
        if(ls==0){
            layer.alert("请选择设备！");
            return false;
        }
        var ds = [];
        for(var i=0;i<ls;i++){
            var data = datas[i];
            alog.d(data);
            var device={"id":data.id,"sn":data.sn};
            ds[i]=device;
        }
        
        var pd={};
        pd.userId = aUser.getId();
        pd.devices=ds;
        
        layer.confirm('是否确定，用户已拒绝选中设备的报价信息？', {icon: 3, title:'提示'}, function(index){
            layer.close(index);
            ajx.post({
                "url" : "api/browser/repair/device/batch/refuse/quotation",
                "data" :  JSON.stringify(pd)
            },
            function(rtn) {
                if(rtn){
                    layer.msg("操作成功!");
                    reloadRepairDeviceListTable();
                }else{
                    layer.msg("操作失败，请确定报价信息已保存!");
                }
            });
        });
        return false;
    });
    
    $("button[name=confirm-quotation]").click(function(){
        var checkStatus = table.checkStatus('repairDeviceManageListTableId');
        var datas = checkStatus.data;
        var ls = datas.length;
        if(ls==0){
            layer.alert("请选择设备！");
            return false;
        }
        var ds = [];
        for(var i=0;i<ls;i++){
            var data = datas[i];
            alog.d(data);
            var device={"id":data.id,"sn":data.sn};
            ds[i]=device;
        }
        
        var pd={};
        pd.userId = aUser.getId();
        pd.devices=ds;
        
        layer.confirm('是否确定，用户已同意选中设备的报价信息？', {icon: 3, title:'提示'}, function(index){
            layer.close(index);
            ajx.post({
                "url" : "api/browser/repair/device/batch/confirm/quotation",
                "data" :  JSON.stringify(pd)
            },
            function(rtn) {
                if(rtn){
                    layer.msg("操作成功!");
                    reloadRepairDeviceListTable();
                }else{
                    layer.msg("操作失败，请确定报价信息已保存!");
                }
            });
        });
        
        return false;
    });
    
    exports('repairDeviceManageQuotationList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

