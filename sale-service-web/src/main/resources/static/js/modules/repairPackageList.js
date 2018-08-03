/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aSystemCustomer','apopup','aRepairStatus','alog'], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var aRepairInvoice = layui.aRepairInvoice;
    
    var aRepairPackage = layui.aRepairPackage;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog;
    
    var aRepairStatus = layui.aRepairStatus;

    var repairInvoiceSerialNumber = aform.getVal("form[name=repairInvoiceROForm]","id");
    
    function checkParentData(){
        if(undefined==repairInvoiceSerialNumber
                ||null==repairInvoiceSerialNumber
                ||""==repairInvoiceSerialNumber){
            return false;
        }
        return true;
    }
    
    var _repairInvoiceStatus = 90;
    
    if(""!=repairInvoiceSerialNumber){
        aRepairInvoice.getInvoiceData(true,repairInvoiceSerialNumber,function(repairInvoiceData){
            alog.d(repairInvoiceData);
            _repairInvoiceStatus = repairInvoiceData.status;
            setRepairInvoiceTable(repairInvoiceData);
        });
    }else{
        var emptyRepairInvoiceData={
                "serialNumber":"---",
                "contacts":"----",
                "status":"---",
                "createTime":"---"
        };
        setRepairInvoiceTable(emptyRepairInvoiceData);
    }
    
    function setRepairInvoiceTable(data){
        table.render({
            id : 'repairInvoiceDataTableId',
            elem : '#repairInvoiceDataTable',
            page : false,
            skin:'row',
            cols : [   [{
                field : 'serialNumber',
                title : '编号'
            },  {
                field : 'contacts',
                title : '联系人'
            }, {
                field : 'status',
                title : '状态',
                templet: function(d){
                    if("---"==d.status){
                        return "----";
                    }
                    return aRepairStatus.getDisplayText(d.status);
                }
            },{
                field : 'createTime',
                title : '创建时间'
            },] ],
            data:[data]
        });
    }
    
    var repairInvoiceSelectPageIdx;
    
    $("button[name=switchInvoiceBtn]").click(function(){
        
        var areaInfo = apopup.getArea($("#repairInvoiceSelectPage"));
        
        //repairInvoicePage
        repairInvoiceSelectPageIdx = layer.open({
            id:"repairInvoiceSelectPageId",
            type: 1,
            title:  ["切换维修单", 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width,areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairInvoiceSelectPage'),
            btn: ['选择','关闭'],
            yes:function(){
                var sdata = aRepairInvoice.getSData();
                if(null==sdata){
                    return false;
                }
                repairInvoiceSerialNumber = sdata.serialNumber;
                aRepairInvoice.getInvoiceData(true,repairInvoiceSerialNumber,function(repairInvoiceData){
                    alog.d(repairInvoiceData);
                    setRepairInvoiceTable(repairInvoiceData);
                    aRepairPackage.setRepairInvoice(repairInvoiceSerialNumber);
                    aRepairPackage.reloadListTable();
                    layer.close(repairInvoiceSelectPageIdx);
                    return false;
                });
            },
            btn2:function(){
                layer.close(repairInvoiceSelectPageIdx);
                return false;
            },
            success: function(layero, index){
                aRepairInvoice.renderSTable();
            }
        });        
        return false;
    });
    
    aRepairPackage.renderListTable(repairInvoiceSerialNumber);    

    var layer = layui.layer;
    var repairInvoicePagePopupIdx ;
    
    var repairPackagePopupType=0;//0:添加用户；1:查看用户；2:修改用户
    var userPasswordChange = false;
    
    table.on('tool(repairPackageListTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var status = data.status;
        if(status>=20&&layEvent !== 'detail'){
            layer.msg("维修包已经开始处理，不能进行操作!");
            return false;
        }
        
        var serialNumber = data.serialNumber;
        if(layEvent === 'detail'){ //查看
            repairPackagePopupType = 1;
            showRepairPackagePage(serialNumber);
        } else if(layEvent === 'del'){ //删除
          alog.d("del");
          deleteRepairInvoice(data.expressNumber,data.contacts,serialNumber,data.id);
        } else if(layEvent === 'edit'){ //编辑
            repairPackagePopupType = 2;
            showRepairPackagePage(serialNumber);
        }
        return false;
      });
    
    function deleteRepairInvoice(expressNumber,contacts,serialNumber,id){
        
        var deleteTip = "是否确定删除维修包："+serialNumber+"？<br/>快递单号："+expressNumber+"<br/>联系人："+contacts;
        
        layer.confirm(deleteTip, function(index){
            layer.close(index);
            $.ajax({
                "async" : false,
                "url" : "api/browser/repair/package/delete?serialNumber="+serialNumber+"&id="+id,
                "type" : "DELETE",
                "contentType" : "application/json",
                'beforeSend' : function(request) {
                },
                "success" : function(resp) {
                    if(100==resp.code&&resp.rtn){
                        layer.msg("维修包删除成功!");
                        aRepairPackage.reloadListTable(repairInvoiceSerialNumber);
                    }else{
                        layer.msg("维修包删除失败!");
                    }
                },
                "error":function(XMLHttpRequest, textStatus, errorThrown){
                }
            });
          });
    }
    
    $("button[name=addRepairPackageBtn]").click(function(){
        if(!checkParentData()){
            layer.msg("请选择维修单信息!");
            return false;
        }
        
        if(_repairInvoiceStatus>=69){
            layer.msg("维修单已经维修完成，不能添加维修包!");
            return false;
        }
        
        repairPackagePopupType = 0;
        showRepairPackagePage();
    });
    
    var emptyFormData = aform.toObject($("#repairPackageForm"));
    alog.d(emptyFormData);
    
    function restRepairPackageForm(){
        form.val("repairPackageFormFilter", emptyFormData);
    }
    
    function showRepairPackagePage(serialNumber){
        
        alog.d("serialNumber:"+serialNumber);
        
        restRepairPackageForm();
        
        var areaInfo = apopup.getArea($("#repairPackagePage"),(1==repairPackagePopupType));
        alog.d(areaInfo);
        
        $("button[lay-filter='repairPackageFormSumitFilter']").removeClass("layui-hide");;
        $("button[lay-filter='repairPackageFormRestFilter']").removeClass("layui-hide");
        $("a[name=searchSystemCustomerBtn]").addClass("layui-hide");
        
        alog.d("repairPackagePopupType:"+repairPackagePopupType);
        
        var popupTitle="添加维修包";
        if(0==repairPackagePopupType){
            popupTitle = "添加维修包";
            $("a[name=searchSystemCustomerBtn]").removeClass("layui-hide");
        }else if(1==repairPackagePopupType){
            popupTitle = "查看维修包";
            $("button[lay-filter='repairPackageFormSumitFilter']").addClass("layui-hide");;
            $("button[lay-filter='repairPackageFormRestFilter']").addClass("layui-hide");
        }else if(2==repairPackagePopupType){
            popupTitle = "修改维修包";
            $("button[lay-filter='repairPackageFormRestFilter']").addClass("layui-hide");
        }
        
        //repairInvoicePage
        repairInvoicePagePopupIdx = layer.open({
            id:"repairPackagePagePopup",
            type: 1,
            title:  [popupTitle, 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width,areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairPackagePage'),
            success: function(layero, index){
                if ( undefined != serialNumber) {
                    loadRepairPackageInfo(serialNumber);
                }else{
                    setContactsInfo();
                }
            }
        });
    }
    
    function setContactsInfo(serialNumber){
        aRepairInvoice.getInvoiceData(false,repairInvoiceSerialNumber,function(data){
            alog.d(data);
            var formData={};
            formData.contactAddress=data.contactAddress;
            formData.contactNumber = data.contactNumber;
            formData.contacts = data.contacts;
            form.val("repairPackageFormFilter",formData);
        });
    }
        
    function loadRepairPackageInfo(serialNumber){
        aRepairPackage.getPackageData(false,serialNumber,function(repairPackage){
            form.val("repairPackageFormFilter",repairPackage);
        });
    }
        
    //form.verify({}); 
    
    form.on('submit(repairPackageFormSumitFilter)',function(data){
        
        $("button[lay-filter='userAddFormSumitFilter']").attr("disabled","disabled");
        $("button[lay-filter='userAddFormRestFilter']").attr("disabled","disabled");
        
        alog.d('userAddFormSumitFilter');
        alog.d(data);
        alog.d(data.field);
        var packageData = data.field;
        
        $.ajax({
            "url" : "api/browser/repair/package/save",
            "type" : "POST",
            "data" : JSON.stringify({"pkg":packageData,"invoiceSerialNumber":repairInvoiceSerialNumber}),
            "contentType" : "application/json",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                $("button[lay-filter='repairPackageFormSumitFilter']").removeAttr("disabled");
                $("button[lay-filter='repairPackageFormRestFilter']").removeAttr("disabled");
                var code = resp.code;
                if (100==code&&resp.rtn) {
                    if(0==repairPackagePopupType){
                        layer.msg("维修单添加成功!");
                    }else if(2==repairPackagePopupType){
                        layer.msg("维修单修改成功!");
                    }
                    alog.d("repairInvoicePagePopupIdx:"+repairInvoicePagePopupIdx);
                    alog.d("layer.index:"+layer.index);
                    layer.close(repairInvoicePagePopupIdx);
                    aRepairPackage.reloadListTable(repairInvoiceSerialNumber);
                }else {
                    if(0==repairPackagePopupType){
                        layer.msg("维修单添加失败！");
                    }else if(2==repairPackagePopupType){
                        layer.msg("维修单修改失败!");
                    }
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
            form.val("repairPackageFormFilter",data);
        });
        return false;
    });
        
    alog.d("repairInvoiceList start");
    exports('repairPackageList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

