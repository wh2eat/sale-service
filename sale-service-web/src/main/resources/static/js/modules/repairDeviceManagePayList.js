/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer','laydate', 'table', 'aUser' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
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
                    width : '8%'
                        
                },{
                    field : 'model',
                    title : '机型',
                    width : '8%'
                },{
                    field:'warrantyType',
                    title:"保修类型",
                    width : '8%',
                    templet: function(d){
                        return aDevice.getWarrantyText(d.warrantyType);
                    }
                },{
                    field : 'payType',
                    title : '支付类型',
                    width : '8%',
                    templet: function(d){
                        return aDevice.getPayTypeText(d.payType);
                    }
                },{
                    field : 'detectUser',
                    title : '维修人',
                    width : '8%',
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
                    width : '8%',
                    templet: function(d){
                        var quotationUser = d.quotationUser;
                        if(undefined!==quotationUser&&null!==quotationUser){
                            return quotationUser.userName;
                        }
                        return "";
                    }                    
                },{
                    field : 'costTotal',
                    title : '金额',
                    width : '12%',
                    templet: function(d){
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
        
        var userId = aUser.getId();
        
        var postData={"userId":userId,"id":id,"sn":sn};
        
        if("pay-finish"===layEvent){
            alog.d(layEvent);
            payFinish(data);
            return false;
        }
                
    });
    
    var repairDevicePayPagePopupIdx;
    
    function payFinish(data){
        
      repairDevicePayPagePopupIdx = layer.open({
            id:"repairDevicePayFromPagePopup",
            type: 1,
            title:  ["完成支付", 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            area:"600px",
            shade: true,
            offset: 'auto',
            content: $('#repairDevicePayFormPage')
            ,btn: ['支付已经完成']
            ,yes: function(index, layero){
                var pd = aform.toObject("form[lay-filter='repairDevicePayFormFilter']");
                pd.userId = aUser.getId();
                doPayFinish(pd);
            }
            ,success: function(layero, index){
                alog.d(data);
                $("#repairDevicePayForm").find("input[name=id]").val(data.id);
                $("#repairDevicePayForm").find("input[name=sn]").val(data.sn);
                
                $("label[name=repairDeviceCostTotal]").html(data.costTotal);
            }
        });
    }
    
    function doPayFinish(postData){
        ajx.post({
            "url":"api/browser/repair/device/finish/pay",
            "data":JSON.stringify(postData)
        },function(data){
            if(data){
                layer.msg("操作成功！",function(){
                    layer.close(repairDevicePayPagePopupIdx);
                    reloadRepairDeviceListTable();
                });
                return;
            }
            layer.msg("操作失败！");
        },function(code,msg){
            layer.msg("操作失败！（code:"+code+",msg:"+msg+"）");
        });
    }
    
    $("button[name=batch-finish-pay]").click(function(){
        var cs = table.checkStatus('repairDeviceManageListTableId');
        var datas = cs.data;
        var dls = datas.length;
        if(dls==0){
            layer.alert("请选择设备！");
            return false;
        }
        
        var showHtml="<form name='repairDeviceBatchPayForm' class='layui-form'>";
        showHtml+= "<table class='layui-table'>";
        showHtml+="<thead><tr>";
        showHtml+="<th>SN</th>";
        showHtml+="<th>支付类型</th>";
        showHtml+="<th>支付总额</th>";
        showHtml+="<th>备注</th>";
        showHtml+="</tr></thead><tbody>";
        var total=0;
        for(var i=0 ; i< dls ; i++){
            var da = datas[i];
            showHtml+="<tr>";
            showHtml+="<td>"+da.sn+"</td>";
            showHtml+="<td>"+aDevice.getPayTypeText(da.payType)+"</td>";
            showHtml+="<td>"+da.costTotal+"（"+aDevice.getCurrency(da.currency)+"）</td>";
            total += parseInt(da.costTotal);
            showHtml+="<td>" +
                    "<input type='hidden' name='row"+i+"' value='"+da.id+"'>" +
                    "<input type='hidden' name='row"+i+"' value='"+da.sn+"'>" +
            		"<input type='text' name='row"+i+"' class='layui-input'>" +
            		"</td>";
            showHtml+="</tr>";
        }
        showHtml += "<tr>" +
                    "<td>总额</td>"+
                    "<td colspan='3'>"+total+"</td>"+
        		"</tr>";
        showHtml += "</tbody></table></form>";
        
        $("#repairDeviceBatchPayFormPage").html(showHtml);
        
        repairDevicePayPagePopupIdx = layer.open({
            id:"repairDeviceBatchPayFromPagePopup",
            type: 1,
            title:  ["完成支付", 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            area:"800px",
            shade: true,
            offset: 'auto',
            content: $("#repairDeviceBatchPayFormPage")
            ,btn: ['支付已经完成']
            ,yes: function(index, layero){
               var formData = aform.toObject($("form[name=repairDeviceBatchPayForm]"));
               alog.d(formData);
               var pd={};
               pd.batchDevices = formData;
               pd.userId = aUser.getId();
               
               layer.confirm("是否确定已选设备支付完成，费用总额："+total+"?",function(){
                   ajx.post({
                       "url":"api/browser/repair/device/batch/finish/pay",
                       "data":JSON.stringify(pd)
                   },function(data){
                       if(data){
                           layer.msg("操作成功！",function(){
                               layer.close(repairDevicePayPagePopupIdx);
                               reloadRepairDeviceListTable();
                           });
                           return;
                       }
                       layer.msg("操作失败！");
                   },function(code,msg){
                       layer.msg("操作失败！（code:"+code+",msg:"+msg+"）");
                   });
               });
               return false;
            }
            ,success: function(layero, index){
                
            }
        });
    });
    
    exports('repairDeviceManagePayList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

