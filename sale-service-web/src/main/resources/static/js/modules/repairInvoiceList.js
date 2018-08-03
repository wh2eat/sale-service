/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table' ,'element','layout','aform','aRepairInvoice','aSystemCustomer','apopup','aRepairStatus','alog'], function(exports) {
    
    var $ = layui.$;
    
    var alog = layui.alog;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var aRepairInvoice = layui.aRepairInvoice;
    
    var apopup = layui.apopup;
    
    var aRepairStatus = layui.aRepairStatus;
    
    aRepairInvoice.renderTable();
    
    table.on('tool(repairInvoiceListTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var status = data.status;
        if(status>=20&&layEvent !== 'detail'){
            layer.msg("维修单已经开始处理，不能进行操作!");
            return false;
        }
        
        var serialNumber = data.serialNumber;
        if(layEvent === 'detail'){ //查看
            repairInvoicePopupType = 1;
            showRepairInvoicePage(serialNumber);
        } else if(layEvent === 'del'){ //删除
          alog.d("del");
          deleteRepairInvoice(serialNumber,data.id);
        } else if(layEvent === 'edit'){ //编辑
            repairInvoicePopupType = 2;
            showRepairInvoicePage(serialNumber);
        }
        return false;
      });

    var layer = layui.layer;
    var repairInvoicePagePopupIdx ;
    
    var repairInvoicePopupType=0;//0:添加用户；1:查看用户；2:修改用户
    var userPasswordChange = false;
    
    function deleteRepairInvoice(serialNumber,id){
        
        var deleteTip = "是否确定删除维修单："+serialNumber+"?";
        
        layer.confirm(deleteTip, function(index){
            layer.close(index);
            $.ajax({
                "async" : false,
                "url" : "api/browser/repair/invoice/delete?serialNumber="+serialNumber+"&id="+id,
                "type" : "DELETE",
                "contentType" : "application/json",
                'beforeSend' : function(request) {
                },
                "success" : function(resp) {
                    if(100==resp.code&&resp.rtn){
                        layer.msg("维修单删除成功!");
                        aRepairInvoice.reloadTable();
                    }else{
                        layer.msg("维修单删除失败!");
                    }
                },
                "error":function(XMLHttpRequest, textStatus, errorThrown){
                }
            });
          });
    }
    
    $("button[name=addRepairInvoiceBtn]").click(function(){
        repairInvoicePopupType = 0;
        showRepairInvoicePage();
    });
    
    var emptyFormData = aform.toObject($("#repairInvoiceForm"));
    alog.d(emptyFormData);
    
    function restRepairInvoiceForm(){
        form.val("repairInvoiceFormFilter", emptyFormData);
    }
    
    function showRepairInvoicePage(serialNumber){
        
        restRepairInvoiceForm();
        
        var areaInfo = apopup.getArea($("#repairInvoiceFormPage"),(1==repairInvoicePopupType));
        alog.d(areaInfo);
                
        $("button[lay-filter='repairInvoiceFormSumitFilter']").removeClass("layui-hide");
        $("button[lay-filter='repairInvoiceFormRestFilter']").removeClass("layui-hide");
        $("a[name=systemCustomerSearchBtn]").addClass("layui-hide");
        
        alog.d("repairInvoicePopupType:"+repairInvoicePopupType);
        
        var popupTitle="添加维修单";
        if(0==repairInvoicePopupType){
            popupTitle = "添加维修单";
            $("a[name=systemCustomerSearchBtn]").removeClass("layui-hide");
        }else if(1==repairInvoicePopupType){
            popupTitle = "查看维修单";
            $("button[lay-filter='repairInvoiceFormSumitFilter']").addClass("layui-hide");;
            $("button[lay-filter='repairInvoiceFormRestFilter']").addClass("layui-hide");
        }else if(2==repairInvoicePopupType){
            popupTitle = "修改维修单";
            $("button[lay-filter='repairInvoiceFormRestFilter']").addClass("layui-hide");
        }
        
        //repairInvoicePage
        repairInvoicePagePopupIdx = layer.open({
            id:"repairInvoiceFromPagePopup",
            type: 1,
            title:  [popupTitle, 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width, areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairInvoiceFormPage'),
            success: function(layero, index){
                if ( undefined != serialNumber) {
                    loadRepairInvoiceInfo(serialNumber);
                }
            }
        });
    }
        
    function loadRepairInvoiceInfo(serialNumber){
        
        aRepairInvoice.getInvoiceData(false,serialNumber,function(repairInvoice){
            form.val("repairInvoiceFormFilter",repairInvoice);
        });
    }
        
    
    form.on('submit(repairInvoiceFormSumitFilter)',function(data){
        
        $("button[lay-filter='userAddFormSumitFilter']").attr("disabled","disabled");
        $("button[lay-filter='userAddFormRestFilter']").attr("disabled","disabled");
        
        alog.d('userAddFormSumitFilter');
        alog.d(data);
        alog.d(data.field);
        var invoiceData = data.field;
        
        $.ajax({
            "url" : "api/browser/repair/invoice/save",
            "type" : "POST",
            "data" : JSON.stringify({"invoice":invoiceData}),
            "contentType" : "application/json",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                $("button[lay-filter='repairInvoiceFormSumitFilter']").removeAttr("disabled");
                $("button[lay-filter='repairInvoiceFormRestFilter']").removeAttr("disabled");
                var code = resp.code;
                if (100==code&&resp.rtn) {
                    if(0==repairInvoicePopupType){
                        layer.msg("维修单添加成功!");
                    }else if(2==repairInvoicePopupType){
                        layer.msg("维修单修改成功!");
                    }
                    alog.d("repairInvoicePagePopupIdx:"+repairInvoicePagePopupIdx);
                    alog.d("layer.index:"+layer.index);
                    aRepairInvoice.reloadTable();
                    layer.close(repairInvoicePagePopupIdx);
                }else {
                    if(0==repairInvoicePopupType){
                        layer.msg("维修单添加失败！");
                    }else if(2==repairInvoicePopupType){
                        layer.msg("维修单修改失败!");
                    }
                }
            },
            "error":function(XMLHttpRequest, textStatus, errorThrown){
            }
        });
        return false;
    });
    
    var aSystemCustomer = layui.aSystemCustomer ;
    
    $("a[name=systemCustomerSearchBtn]").click(function(){
        aSystemCustomer.showSelectPage(function(data){
            form.val("repairInvoiceFormFilter",data);
        });
    });
    
    exports('repairInvoiceList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

