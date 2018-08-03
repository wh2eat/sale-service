/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'laytpl','table','laydate' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;
    
    var laytpl = layui.laytpl;
    
    var laydate = layui.laydate;
    laydate.render({ 
        elem: '#deviceSearch4DeliveryTime' //或 elem: document.getElementById('test')、elem: lay('#test') 等
    });

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
                    width : 140
                },{
                    field : 'status',
                    title : '状态',
                    width : 140,
                    templet: function(d){
                        return aRepairStatus.getDisplayText(d.status);
                    }
                },{
                    field : 'endCustomerName',
                    title : '终端客户名称'
                },{
                    field : 'repairPacakge',
                    title : '送修快递单号',
                    templet: function(d){
                        var repairPackage = d.repairPackage;
                        if(undefined!==repairPackage&&null!==repairPackage){
                            return repairPackage.expressNumber+"("+repairPackage.expressName+")";
                        }
                        return "";
                    }
                },{
                    field : 'repairBackPackage',
                    title : '返客快递单号',
                    templet: function(d){
                        var repairBackPackage = d.repairBackPackage;
                        if(undefined!==repairBackPackage&&null!==repairBackPackage){
                            return repairBackPackage.expressNumber+"("+repairBackPackage.expressName+")";
                        }
                        return '';
                    }
                }, {
                    field : 'createTime',
                    title : '送修日期',
                    width : 180
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
    
    $("button[name=repairDeviceSearchBtn]").click(function(){
        reloadRepairDeviceListTable();
        return false;
    });
    
    
    $("button[name=viewSearchResultBtn]").click(function(){
        var checkStatus = table.checkStatus('repairDeviceManageListTableId');
        var datas=checkStatus.data;
        if(undefined === datas || null === datas || datas.length==0){
            layer.msg("请选择维修信息！");
            return false;
        }
        showSearchResult(datas);
    });
    
    var getTpl = $("#repairDeviceSearchResultTpl").html();
    
    function showSearchResult(datas){
        laytpl(getTpl).render(datas, function(html){
            layer.open({
                type: 1, 
                area:'1024px',
                maxmin:true,
                title:"维修设备状态",
                content: html 
              });
       });
    }
       
        
    exports('repairDeviceManageSearchList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

