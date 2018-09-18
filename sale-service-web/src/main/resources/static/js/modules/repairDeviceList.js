/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table','laydate' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aUser','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
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
    
    var aUser = layui.aUser;
    
    var _userId = aUser.getId();
    
    var laydate = layui.laydate;
    
    var _repairPackageStatus=0;

    var repairInvoiceSerialNumber = aform.getVal("form[name=repairPacakgeROForm]","invoiceId");
    alog.d("repairInvoiceSerialNumber:"+repairInvoiceSerialNumber);
    
    var repairPackageSerialNumber = aform.getVal("form[name=repairPacakgeROForm]","id");
    alog.d("repairPackageSerialNumber:"+repairPackageSerialNumber);
    
    var repairStationUdid=aUser.getRepairStationId();
    alog.d("repairStationUdid:"+repairStationUdid);
    
    $("#repairDeviceForm").on("change","input[name=model]",function(){
        var inputVal =$(this).val() 
        alog.d(inputVal);
        if(null===inputVal||""===inputVal){
            return;
        }
        var idx = inputVal.indexOf("-");
        if(idx==-1){
            return;
        }
        
        var machineType = inputVal.substring(0,idx);
        alog.d("machineType:"+machineType);
        $("#repairDeviceForm").find("input[name=machineType]").val(machineType);
    });
        
    function checkParentData(){
        if(undefined ==repairInvoiceSerialNumber 
                ||null==repairInvoiceSerialNumber
                ||""==repairInvoiceSerialNumber){
            return false;
        }
        if(undefined ==repairPackageSerialNumber 
                ||null==repairPackageSerialNumber
                ||""==repairPackageSerialNumber){
            return false;
        }
        
        return true;
    }
    
    function setRepairPackageROForm(invoiceSerialNumber,packageSerialNumber){
        
        repairInvoiceSerialNumber=invoiceSerialNumber;
        $("form[name='repairPacakgeROForm']").find("input[name='invoiceId']").val(invoiceSerialNumber);
        
        repairPackageSerialNumber = packageSerialNumber;
        $("form[name='repairPacakgeROForm']").find("input[name='id']").val(packageSerialNumber);
        
    }
    
    function setRepairPackageTable(data){
        table.render({
            id : 'repairDeviceBaseDataTableId',
            elem : '#repairDeviceBaseDataTable',
            page : false,
            skin:'row',
            cols : [   [ {
                field : 'invoice',
                title : '维修单号',
                templet: function(d){
                    if(null==d.invoice|| undefined ==d.invoice){
                        return "---";
                    }
                    return d.invoice.serialNumber;
                }
            },{
                field : 'serialNumber',
                title : '维修包单号'
            },{
                field : 'contacts',
                title : '联系人信息',
                templet: function(d){
                    if("---"==d.expressName){
                        return "---";
                    }
                    return d.contacts+","+d.contactNumber;
                }
            },{
                field : 'expressName',
                title : '快递信息',
                templet: function(d){
                    if("---"==d.expressName){
                        return "---";
                    }
                    var dis = d.expressName+":"+d.expressNumber;
                    if(":"==dis){
                        return "NA";
                    }
                    return dis;
                }
            },{
                field : 'createTime',
                title : '送修日期'
            }] ],
            data:[data]
        });
    }
    
    var repairPackageSwitchPageIdx;
    
    function showSwitchRepairPackagePage(){
        
        var areaInfo = apopup.getArea($("#repairPackageSwitchPage"));
        alog.d(areaInfo);
                
        repairPackageSwitchPageIdx = layer.open({
            id:"repairPackageSwitchPagePopup",
            type: 1,
            title:  ["维修包切换", 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width,areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairPackageSwitchPage'),
            btn: ['切换','关闭'],
            yes:function(){
                var sdata = aRepairPackage.getSData();
                if(null==sdata){
                    return false;
                }
                alog.d(sdata);
                setRepairPackageROForm(sdata.invoice.serialNumber,sdata.serialNumber);
                reloadRepairDeviceListTable();
                loadRepairPacakgeData();
                layer.close(repairPackageSwitchPageIdx);
                return false;
            },
            btn2:function(){
                layer.close(repairPackageSwitchPageIdx);
                return false;
            },
            success: function(layero, index){
                aRepairPackage.renderSearchListTable();
            }
        });
        
        return false;
    }
    
    function loadRepairPacakgeData(){
        aRepairPackage.getPackageData(true,repairPackageSerialNumber,function(repairPackage){
            setRepairPackageTable(repairPackage);
            _repairPackageStatus = repairPackage.status;
        });
    }
        
    function init(){
        
        //注册维修包切换事件
        $("a[name='switchRepairPackageBtn']").click(function(){
            showSwitchRepairPackagePage();
        });
        
        //初始化维修包数据
        if(!checkParentData()){
            var data={
                "contacts":"---",
                "expressName":"---",
                "status":"---",
                "receiptTime":"---"
            };
            setRepairPackageTable(data);
        }else{
            loadRepairPacakgeData();
        }
    }
    
    //执行初始化
    init();
    
    var repairDeviceTableUrl='api/browser/repair/device/list';
    
    table.render({
        id : 'repairDeviceTableId',
        elem : '#repairDeviceListTable',
        page : true,
        loading: true,
        skin:'row',
        even: false,
        limits:[10,20,50,100],
        cols : [ [ {
            title : 'ID',
            width : 40,
            templet: function(row){
                return row.LAY_INDEX;
              }
        }, {
            field : 'expressNumber',
            title : '快递单号',
            width : '10%'
        }, {
            field : 'endCustomerName',
            title : '终端客户名称',
            width : '10%'
        }, {
            field : 'sn',
            title : 'SN',
            width : '10%'
        },{
            field : 'status',
            title : '状态',
            templet: function(d){
                return aRepairStatus.getDisplayText(d.status);
            }
        },{
            field:'warrantyType',
            title:"保修类型",
            width : '8%',
            templet: function(d){
                return aDevice.getWarrantyText(d.warrantyType);
            }
        },{
            field : 'model',
            title : '机型',
            width : '8%'
        }, {
            field : 'manufactureTime',
            title : '出厂日期',
            width : '8%',
            templet: function(d){
                var manufactureTime = d.manufactureTime;
                if (undefined!=manufactureTime && null!=manufactureTime) {
                    return manufactureTime.substring(0,10);
                }
                return "NA";
            }
        }, {
            field : 'repairTimes',
            title : '维修次数',
            width : '6%'
        }, {
            field : 'deliveryTime',
            title : '送修日期',
            width : '8%'
        },{ 
            width:220, 
            align:'center', 
            toolbar: '#repairDeviceTableToolTpl'
        }  ] ],
        url : repairDeviceTableUrl,
        method : "POST",
        where:{params:JSON.stringify(getCondition())},
        done: function(res, curr, count){
        }
    });
    
    function getCondition(){
        var formData = aform.toObject($("form[name=repairDeviceSearchForm]"));
        formData.repairStationUdid=repairStationUdid;
        formData.invoiceSerialNumber=repairInvoiceSerialNumber;
        formData.packageSerialNumber=repairPackageSerialNumber;
        alog.d(formData);
        return formData;
    }
    
//    {"repairStationUdid":repairStationUdid,"invoiceSerialNumber":repairInvoiceSerialNumber,"packageSerialNumber":repairPackageSerialNumber}
    function reloadRepairDeviceListTable(){
        table.reload('repairDeviceTableId', {
            url: repairDeviceTableUrl,
            method : "POST",
            where:{params:JSON.stringify(getCondition())}
          });
    }

    var layer = layui.layer;
    var repairDevicePagePopupIdx ;
    
    var repairDevicePopupType=0;//0:添加用户；1:查看用户；2:修改用户
    
    table.on('tool(repairDeviceTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var status = data.status;
        
        var id = data.id;
        var sn = data.sn;
        if(layEvent === 'detail'){ //查看
            repairDevicePopupType = 1;
            showRepairDevicePage(id,sn);
        } else if(layEvent === 'del'){ //删除
            if(status>=20){
                layer.msg("设备已经开始处理，不能进行删除操作!");
                return false;
            }
          alog.d("del");
          deleteRepairDevice(id,sn);
        } else if(layEvent === 'edit'){ //编辑
            repairDevicePopupType = 2;
            showRepairDevicePage(id,sn);
        }else if("editStatus"==layEvent){//修改状态
            showRepairDeviceStatusPage(id,sn);
        }
        return false;
      });
    
    function deleteRepairDevice(id,sn){
        
        var deleteTip = "是否确定删除维修设备："+sn+"？";
        
        layer.confirm(deleteTip, function(index){
            layer.close(index);
            
            ajx.del({
                "url" : "api/browser/repair/device/delete?id="+id+"&sn="+sn,
                },
                function(rtn) {
                    if(rtn){
                        layer.msg("维修设备删除成功!");
                        reloadRepairDeviceListTable();
                    }else{
                        layer.msg("维修设备删除失败!");
                    }
                }
            );
          });
    }
    
    $("button[name=addRepairPackageBtn]").click(function(){
        if(!checkParentData()){
            layer.msg("请选择维修包信息！");
            return false;
        }
        
        if(_repairPackageStatus>=20){
            layer.msg("维修包已经开始处理，不能添加设备!");
            return false;
        }
        
        
        repairDevicePopupType = 0;
        showRepairDevicePage();
    });
    
    var emptyFormData = aform.toObject($("#repairDeviceForm"));
    alog.d(emptyFormData);
    
    function restRepairDeviceForm(){
        form.val("repairDeviceFormFilter", emptyFormData);
    }
    
    $("a[name='loadRepairDeviceInfo']").click(function(){
        var sn = aform.getVal("#repairDeviceForm","sn");
        if(""==sn||$.trim(sn).length<7){
            layer.msg("请输入正确的sn！");
            return false;
        }
        ajx.get({
            "url" : "api/browser/repair/device/get/base?sn="+sn,
            },
            function(rtn) {
                if(rtn){
                    var manufactureTime = rtn.manufactureTime;
                    rtn.id='';
                    rtn.manufactureTime = (undefined!=manufactureTime&&null!=manufactureTime)?(manufactureTime.substring(0,10)):"NA";
                    form.val("repairDeviceFormFilter", rtn);
                }else{
                    layer.msg("没有找到设备信息！");
                }
            }
        );
    });    
    
    function showRepairDevicePage(id,sn){
                
        restRepairDeviceForm();
        
        var areaInfo = apopup.getArea($("#repairDevicePage"),(1==repairDevicePopupType));
        alog.d(areaInfo);
        
        $("button[lay-filter='repairDeviceFormSumitFilter']").removeClass("layui-hide");
        $("button[lay-filter='repairDeviceFormRestFilter']").removeClass("layui-hide");
        $("a[name='loadRepairDeviceInfo']").removeClass("layui-hide");
        
        
        alog.d("repairDevicePopupType:"+repairDevicePopupType);
        
        var popupTitle="添加维修设备";
        if(0==repairDevicePopupType){
            popupTitle = "添加维修设备";
        }else if(1==repairDevicePopupType){
            popupTitle = "查看维修设备";
            $("button[lay-filter='repairDeviceFormSumitFilter']").addClass("layui-hide");;
            $("button[lay-filter='repairDeviceFormRestFilter']").addClass("layui-hide");
            $("a[name='loadRepairDeviceInfo']").addClass("layui-hide");
        }else if(2==repairDevicePopupType){
            popupTitle = "修改维修设备";
            $("button[lay-filter='repairDeviceFormRestFilter']").addClass("layui-hide");
        }
        
        //repairInvoicePage
        repairDevicePagePopupIdx = layer.open({
            id:"repairDevicePagePopup",
            type: 1,
            title:  [popupTitle, 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width,areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairDevicePage'),
            success: function(layero, index){
                
                laydate.render({
                    elem: '#deviceManufactureTime' //指定元素
                });
                
                if ( undefined != id) {
                    loadRepairDeviceInfo(id,sn);
                }
            }
        });
    }
        
    function loadRepairDeviceInfo(id,sn){
        
        ajx.get({
            "url":"api/browser/repair/device/get?id="+id+"&sn="+sn
        },
        function(repairDevice){
            form.val("repairDeviceFormFilter",repairDevice);
        });
    }
        
    //form.verify({}); 
    
    function removeFormButtonDisabled(){
        $("button[lay-filter='repairDeviceFormSumitFilter']").removeAttr("disabled");
        $("button[lay-filter='repairDeviceFormRestFilter']").removeAttr("disabled");
    }
    
    function addFormButtonDisabled(){
        $("button[lay-filter='repairDeviceFormSumitFilter']").attr("disabled","disabled");
        $("button[lay-filter='repairDeviceFormRestFilter']").attr("disabled","disabled");
    }
    
    form.on('submit(repairDeviceFormSumitFilter)',function(data){
        
        addFormButtonDisabled();
        
        alog.d(data);
        alog.d(data.field);
        var deviceData = data.field;
        
        ajx.post({
            "url" : "api/browser/repair/device/save",
            "data" : JSON.stringify({"userId":_userId,"device":deviceData,"invoiceSerialNumber":repairInvoiceSerialNumber,"packageSerialNumber":repairPackageSerialNumber})
            },
            function(resp) {
                removeFormButtonDisabled();
                alog.d(resp);
                if(0==repairDevicePopupType){
                    layer.msg("维修设备添加成功!");
                }else if(2==repairDevicePopupType){
                    layer.msg("维修设备修改成功!");
                }
                alog.d("repairDevicePagePopupIdx:"+repairDevicePagePopupIdx);
                alog.d("layer.index:"+layer.index);
                layer.close(repairDevicePagePopupIdx);
                reloadRepairDeviceListTable();
            },
            function(code,message){
                removeFormButtonDisabled();
                if(0==repairDevicePopupType){
                    if('system_object_exist'==message){
                        layer.msg("维修设备添加失败,原因：同一个维修单重复添加相同sn的设备！");
                    }else{
                        layer.msg("维修设备添加失败！");
                    }
                }else if(2==repairDevicePopupType){
                    layer.msg("维修设备修改失败!");
                }
            });
        
        return false;
    });
    
    $("form[name=repairDeviceSearchForm]").submit(function(){
        reloadRepairDeviceListTable();
        return false;
    });
    
    
    var repairDeviceStatusPagePopupIdx = null;
    function showRepairDeviceStatusPage(id,sn){
        
        $("form[name=repairDeviceStatusForm]").find("input[name=id]").val(id);
        $("form[name=repairDeviceStatusForm]").find("input[name=sn]").val(sn);
                
        var areaInfo = apopup.getArea($("#repairDeviceStatusPage"));
        alog.d(areaInfo);
        
        var popupTitle="修改设备状态";
        
        repairDeviceStatusPagePopupIdx = layer.open({
            id:"repairDeviceStatusPagePopupId",
            type: 1,
            title:  [popupTitle, 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width,areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairDeviceStatusPage'),
            success: function(layero, index){
            }
        });
    }
    
    $("form[name=repairDeviceStatusForm]").submit(function(){
        
        var fdata = aform.toObject($("form[name=repairDeviceStatusForm]"));
        var pdata = {"userId":aUser.getId()};
        pdata.device = fdata;
        alog.d(pdata);
        
        layer.confirm("是否确定修改设备状态？", function(index){
            layer.close(index);
            ajx.post({
                "url" : "api/browser/repair/device/save",
                "data": JSON.stringify(pdata)
                },
                function(rtn) {
                    if(rtn){
                        layer.msg("状态修改成功!");
                        layer.close(repairDeviceStatusPagePopupIdx);
                        reloadRepairDeviceListTable();
                    }else{
                        layer.msg("状态修改失败!");
                    }
                }
            );
          });
        
        return false;
    });
    
    exports('repairDeviceList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

