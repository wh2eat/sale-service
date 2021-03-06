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
                    field : 'repairPacakge',
                    title : '送修快递单号',
                    width : '12%',
                    templet: function(d){
                        var repairPackage = d.repairPackage;
                        if(undefined!==repairPackage&&null!==repairPackage){
                            return repairPackage.expressNumber+"("+repairPackage.expressName+")";
                        }
                        return "";
                    }
                },{
                    field : 'endCustomerName',
                    title : '终端客户名称',
                    width : '12%'
                },{
                    field : 'sn',
                    title : 'SN',
                    width : '10%'
                },{
                    field : 'status',
                    title : '状态',
                    width : '15%',
                    templet: function(d){
                        return aRepairStatus.getDisplayText(d.status);
                    }
                },{
                    field : 'payStatus',
                    title : '支付状态',
                    templet:function(d){
                          if(d.charge==0){
                              return "免费维修";
                          }else{ 
                              if(0==d.payStatus){ 
                                  return "收费维修(未支付)";
                              }else if(1==d.payStatus){ 
                                  return "收费维修(用户拒绝)";
                              }else if(9==d.payStatus){ 
                                  return "收费维修(已支付)";
                              }else{
                                return "处理中";
                              } 
                         }
                    }
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
                    field : 'repairBackPackage',
                    title : '返客快递单号',
                    width : '12%',
                    templet: function(d){
                        var repairBackPackage = d.repairBackPackage;
                        if(undefined!==repairBackPackage&&null!==repairBackPackage){
                            return repairBackPackage.expressNumber+"("+repairBackPackage.expressName+")";
                        }
                        return '';
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

