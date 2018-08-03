/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table','laydate', 'aUser' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
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
    
    var aUser = layui.aUser;
    
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
            cols : [ [{
                title : 'ID',
                fixed : 'left',
                width : 60,
                templet: function(row){
                    return row.LAY_INDEX;
                  }
            },{
                    field : 'sn',
                    title : 'SN'                        
                },{
                    field:'warrantyType',
                    title:"保修类型",
                    templet: function(d){
                        return aDevice.getWarrantyText(d.warrantyType);
                    }
                },{
                    field : 'model',
                    title : '机型'
                },{
                    field : 'payType',
                    title : '支付类型',
                    templet: function(d){
                        return aDevice.getPayTypeText(d.payType);
                    }
                },{
                    field : 'costTotal',
                    title : '金额（单位：人民币）'
                },{
                    fixed: 'right', 
                    width:180, 
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
        
        var userId = aUser.getId();
        
        var postData={"userId":userId,"id":id,"sn":sn};
        
        if("user-confirm"===layEvent){
            alog.d(layEvent);
            userConfirm(postData);
            return false;
        }
        
        if("user-refuse"===layEvent){
            alog.d(layEvent);
            userRefuse(postData);
            return false;
        }
        
    });
    
    function userConfirm(postData){
        layer.confirm("用户同意报价并确定维修，是否确定？<br\>说明:<br/>1、如果付款类型为：“月结”则直接进入待维修状态。",function(idx){
            layer.close(idx);
            ajx.post({
                "url":"api/browser/repair/device/confirm/quotation",
                "data":JSON.stringify(postData)
            },function(data){
                if(data){
                    layer.msg("操作成功！",function(){
                        reloadRepairDeviceListTable();
                    });
                    return;
                }
                layer.msg("操作失败！",function(){
                    reloadRepairDeviceListTable();
                });
            },function(code,msg){
                layer.msg("操作失败！（code:"+code+",msg:"+msg+"）",function(){
                    reloadRepairDeviceListTable();
                });
            });
        });
    }
    
    function userRefuse(postData){
        layer.confirm("用户不同意报价，是否确定？<br\>说明:<br/>1、用户不同意报价，将停止维修并将设备返回给客户。",function(idx){
            layer.close(idx);
            ajx.post({
                "url":"api/browser/repair/device/refuse/quotation",
                "data":JSON.stringify(postData)
            },function(data){
                if(data){
                    layer.msg("操作成功！",function(){
                        reloadRepairDeviceListTable();
                    });
                    return;
                }
                layer.msg("操作失败！",function(){
                    reloadRepairDeviceListTable();
                });
            },function(code,msg){
                layer.msg("操作失败！（code:"+code+",msg:"+msg+"）",function(){
                    reloadRepairDeviceListTable();
                });
            });
        });
    }
    
    
    
    exports('repairDeviceManageConfirmList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

