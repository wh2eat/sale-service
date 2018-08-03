/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'laydate','table' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
    var $ = layui.$;
    
    laydate = layui.laydate;
    laydate.render({ 
        elem: '#deviceSearch4DeliveryTime' //或 elem: document.getElementById('test')、elem: lay('#test') 等
    });
    
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
                    field : 'repairInvoice',
                    title : '维修单号',
                    templet: function(d){
                        return d.repairInvoice.serialNumber;
                    }
                },{
                    field : 'repairInvoice',
                    title : '客户RMA',
                    templet: function(d){
                        return d.repairInvoice.customerRma;
                    }
                },{
                    field : 'repairPacakge',
                    title : '快递信息',
                    templet: function(d){
                        var repairPackage = d.repairPackage;
                        return repairPackage.expressName+":"+repairPackage.expressNumber;
                    }
                },{
                    field : 'sn',
                    title : 'SN'
                },{
                    field : 'status',
                    title : '状态',
                    templet: function(d){
                        return aRepairStatus.getDisplayText(d.status);
                    }
                },{
                    field:'warrantyType',
                    title:"保修类型",
                    templet: function(d){
                        return aDevice.getWarrantyText(d.warrantyType);
                    }
                },{
                    field : 'model',
                    title : '机型'
                }, {
                    field : 'manufactureTime',
                    title : '出厂日期',
                    templet: function(d){
                        var manufactureTime = d.manufactureTime;
                        return manufactureTime.substring(0,10);
                    }
                }, {
                    field : 'repairTimes',
                    title : '维修次数'
                }, {
                    field : 'createTime',
                    title : '送修日期'
                },{
                    fixed: 'right', 
                    width:200, 
                    align:'center', 
                    toolbar: '#repairDeviceTableToolTpl'
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
    
    $("button[name=commitRepairDevice2CheckBtn]").click(function(){
        var cs =  table.checkStatus('repairDeviceManageListTableId');
        alog.d(cs);        
        var datas = cs.data;
        if(datas.length<=0){
            layui.msg("请选择需要提交检测的设备!");
            return false;
        }
        
        var ids=[];
        var length = datas.length;
        for(var ii = 0;ii<length;ii++){
            var data = datas[ii];
            ids.push(data.id);
        }
        alog.d(ids);
        
        ajx.post({
            "url" : "api/browser/repair/device/commit/check",
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
        return false;
    });
    
    

    var layer = layui.layer;
    var repairDevicePagePopupIdx ;
    
    var repairDevicePopupType=0;//0:添加用户；1:查看用户；2:修改用户
    
    table.on('tool(repairDeviceTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var id = data.id;
        var sn = data.sn;
        if(layEvent === 'detail'){ //查看
            repairDevicePopupType = 1;
            showRepairDevicePage(id,sn);
        } else if(layEvent === 'del'){ //删除
          alog.d("del");
          deleteRepairDevice(id,sn);
        } else if(layEvent === 'edit'){ //编辑
            repairDevicePopupType = 2;
            showRepairDevicePage(id,sn);
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
                    rtn.manufactureTime = manufactureTime.substring(0,10);
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
            "data" : JSON.stringify({"device":deviceData,"invoiceSerialNumber":repairInvoiceSerialNumber,"packageSerialNumber":repairPackageSerialNumber})
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
                    layer.msg("维修设备添加失败！");
                }else if(2==repairDevicePopupType){
                    layer.msg("维修设备修改失败!");
                }
            });
        
        return false;
    });
        
    exports('repairDeviceWaitCheckList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

