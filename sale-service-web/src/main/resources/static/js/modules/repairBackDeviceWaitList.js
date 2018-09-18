/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer','aUser', 'table','laydate','aSystemCustomer' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var aUser = layui.aUser;
    
    var laydate = layui.laydate;
    laydate.render({ 
        elem: '#deviceSearch4DeliveryTime' //或 elem: document.getElementById('test')、elem: lay('#test') 等
    });
    
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
                {checkbox:true},
                {
                    field:'endCustomerName',
                    title:"终端客户名称"
                },{
                    field : 'sn',
                    title : 'SN',
                    width : 85
                        
                },{
                    field : 'model',
                    title : '机型',
                    width : 120
                },{
                    field : 'status',
                    title : '状态',
                    width : 120,
                    templet: function(d){
                        return aRepairStatus.getDisplayText(d.status);
                    }
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
                } ,{
                    field:'attachment',
                    title:"附件"
                },{
                    field : 'createTime',
                    title : "送修时间",
                    width : 80
                },{
                    field:'remark',
                    title:"备注"
                } ] ],
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
    
    var selectDeviceIds;
    
    $("button[name=backAndShipBtn]").click(function(){
        var checkStatus = table.checkStatus('repairDeviceManageListTableId'); //test即为基础参数id对应的值
        var datas = checkStatus.data;
        if(null==datas||datas.length==0){
            showRepairBackPackagePage({});
        }else{
            var length = datas.length;
            var repairPackageId=datas[0].repairPackageId;
            selectDeviceIds = [];
            for(var ii=0;ii<length;ii++){
                var dd = datas[ii];
//                if(dd.repairPackageId!==repairPackageId){
//                    layer.msg("创建返客包时，只能选择相同维修包内的设备!");
//                    return false;
//                }
                if(dd.status!=70){
                    layer.msg("创建返客包时，设备状态必须全部为：待返客!");
                    return false;
                }
                selectDeviceIds.push(dd.id);
            }
            var repairPackageData = datas[0].repairPackage;
            var repairPackage = {};
            repairPackage.contacts = repairPackageData.contacts;
            repairPackage.contactNumber = repairPackageData.contactNumber;
            repairPackage.contactAddress = repairPackageData.contactAddress;
            
            showRepairBackPackagePage(repairPackage);
        }
    });
    
    var emptyFormData = aform.toObject($("#repairBackPackageForm"));
    alog.d(emptyFormData);
        
    function restRepairBackPackageForm(){
        form.val("repairBackPackageFormFilter", emptyFormData);
    }
    
    function showRepairBackPackagePage(repairPackage){
                
        restRepairBackPackageForm();
        
        var areaInfo = apopup.getArea($("#repairBackPackageFromPage"));
        alog.d(areaInfo);
        
        //repairBackPackageFromPage
        repairBackPackageFormPagePopupIdx = layer.open({
            id:"repairBackPackageFromPagePopup",
            type: 1,
            title:  ["创建返客包", 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width, areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairBackPackageFromPage'),
            success: function(layero, index){
                form.val("repairBackPackageFormFilter", repairPackage);
            }
        });
    }
    
    
    $("#repairBackPackageForm").submit(function(){
        
        $("button[type='submit']").attr("disabled","disabled");
        $("button[lay-filter='repairBackPackageFormRestFilter']").attr("disabled","disabled");
        
        var repairBackPackageInfo = aform.toObject($("#repairBackPackageForm"));
        var userUdid = aUser.getId();
        
        $.ajax({
            "url" : "api/browser/repair/back/package/save",
            "type" : "POST",
            "data" : JSON.stringify({"backPackage":repairBackPackageInfo,"userId":userUdid,"repairDeviceIds":selectDeviceIds}),
            "contentType" : "application/json",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                var code = resp.code;
                if (100==code&&resp.rtn) {
                    layer.close(repairBackPackageFormPagePopupIdx);
                    reloadRepairDeviceListTable();
                    layer.msg("返客包添加成功!");
                }else {
                    layer.msg("返客包添加失败！");
                    return false;
                }
            },
            "error":function(XMLHttpRequest, textStatus, errorThrown){
            }
        });
        
        return false;
    });
    
    var aSystemCustomer = layui.aSystemCustomer;
    
    $("a[name=searchSystemCustomerBtn]").click(function(){
        aSystemCustomer.showSelectPage(function(data){
            form.val("repairBackPackageFormFilter",data);
        });
    });
    
        
    exports('repairBackDeviceWaitList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

