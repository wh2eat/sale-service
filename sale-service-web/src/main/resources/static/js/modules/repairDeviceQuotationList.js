/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table', 'atable' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','aUser','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    var atable = layui.atable;
    
    var aRepairInvoice = layui.aRepairInvoice;
    
    var aRepairPackage = layui.aRepairPackage;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog;
    
    var aDevice = layui.aDevice;
    
    var aRepairStatus = layui.aRepairStatus;
    
    var ajx = layui.ajx;
    
    var aUser = layui.aUser;
    
    var lockStatus=49;
    
    
    var _repairDeviceId = $("form[lay-filter=repairDeviceFormFilter]").find("input[name=id]").val();
    var _repairDeviceSn = $("form[lay-filter=repairDeviceFormFilter]").find("input[name=sn]").val();

    
    var _repairDeviceStatus;
    
    var _fieldEdit = "text";

    function setRepairDeviceForm(){
        ajx.get({
            "url":"api/browser/repair/device/get?id="+_repairDeviceId+"&sn="+_repairDeviceSn
        },function(data){
            alog.d("setRepairDeviceForm");
            alog.d(data);
            _repairDeviceStatus = data.status;
            if(_repairDeviceStatus>=lockStatus){
                _fieldEdit = "";
                $("button[name=save-quotation]").attr("disabled","disable").addClass("layui-disabled");
                $("button[name=finish-quotation]").attr("disabled","disable").addClass("layui-disabled");
                $("button[name=refuse-quotation]").attr("disabled","disable").addClass("layui-disabled");
            }
            data.agentName = data.agentName+"--"+data.endCustomerName;
            form.val("repairDeviceFormFilter",data);
            renderListTable();
        },function(code,msg){
            alog.d(code+":"+message);
        });
    }
    setRepairDeviceForm();
    
    
    var repairDeviceDetailUrl='api/browser/repair/device/get/detect/invoice/detail';
    
    var repairDeivceQuotationTable;
    
    var repairDeviceQuotationInvoices;
    
    var _isFinishQuotation = false;
    
    var isEdit = "";
    
    function renderListTable(){
        
        var conditions = getListConditions();
        alog.d(conditions);
        repairDeivceQuotationTable = table.render({
            id : 'repairDeviceQuotationListTableId',
            elem : '#repairDeviceQuotationListTable',
            page : false,
            loading: true,
            skin:'row',
            even: false,
            limits:[10,20,50,100],
            cols : [ [{
                title : 'ID',
                fixed : 'center',
                width : 40,
                templet: function(row){
                    return row.LAY_INDEX;
                  }
                },{
                    field : 'malfunctionAppearance',
                    title : '问题现象'
                },{
                    field : 'malfunctionReason',
                    title : '问题原因'
                },{
                    field : 'responsibleParty',
                    title : '责任方'
                },{
                    field : 'repairSuggest',
                    title : '维修建议'
                        
                },{
                    field : 'quotationInvoice',
                    title : '确认状态',
                    templet: function(d){
                        var confirmStatus = atable.getfv(d,"quotationInvoice.confirmStatus");
                        alog.d(confirmStatus);                        
                        
                        if(1===confirmStatus){
                            return "已拒绝";
                        }else if(9===confirmStatus){
                            return "已确认";
                        }
                        return "待确认";
                    }                        
                },{
                    field:'id',
                    title:"是否报价",
                    templet: function(d){
                        return (1==d.quotedPrice?"是":"否");
                    }
                },{
                    field:'quotationInvoice_item',
                    title:"报价项目",
                    edit:_fieldEdit,
                    templet: function(d){
                        return atable.getfv(d,"quotationInvoice.item");
                    }
                }                
                ,{
                    field:'quotationInvoice_quantity',
                    title:"数量",
                    edit:_fieldEdit,
                    templet: function(d){
                        var rquantity =  atable.getfv(d,"quotationInvoice.quantity");
                        if(""==rquantity){
                            return 1;
                        }
                        return rquantity;    
                    }
                },{
                    field:'quotationInvoice_priceUnit',
                    title:"单价",
                    edit:_fieldEdit,
                    templet: function(d){
                        return atable.getfv(d,"quotationInvoice.priceUnit");
                    }
                },{
                    field:'quotationInvoice_priceTotal',
                    title:"总价",
                    templet: function(d){
                        return "<label name='priceTotal_"+d.id+"'>"+atable.getfv(d,"quotationInvoice.priceTotal")+"</label>";
                    }
                },{
                    fixed: 'right', 
                    width:80, 
                    align:'center', 
                    toolbar: '#repairDeviceQuotationListTableToolTpl'
                } ] ],
                url : repairDeviceDetailUrl,
                method : "POST",
                where:{params:JSON.stringify(conditions)},
                done: function(res, curr, count){
                    setRepairDeviceQuotationInvoices(res.data);
                    layer.msg("数据刷新成功!");
                }
        });
    }
    
    
    function reloadTable(){
        var conditions = getListConditions();
        alog.d(conditions);
        table.reload('repairDeviceQuotationListTableId', {
            url: repairDeviceDetailUrl,
            method : "POST",
            where:{params:JSON.stringify(conditions)},
            done: function(res, curr, count){
                setRepairDeviceQuotationInvoices(res.data);
                //reloadPageInfo();
                doSaveQuotation(false);
            }
          });
    }
    
    function setRepairDeviceQuotationInvoices(datas){
        repairDeviceQuotationInvoices = datas ;
        var rlength = repairDeviceQuotationInvoices.length;
        for(var ii=0;ii<rlength;ii++){
            var rdqi = repairDeviceQuotationInvoices[ii];
            var qi = rdqi.quotationInvoice;
            if(undefined ===qi || null ===qi){
                qi={};
            }
            var quantity = qi.quantity;
            if(undefined===quantity||null===quantity){
                quantity = 1;
            }
            qi.quantity=quantity;
            rdqi.quotationInvoice = qi;
        }
        alog.d(repairDeviceQuotationInvoices);
    }
    
    function reloadPageInfo(){
        layer.msg("开始刷新数据",{time:20000});        
        setRepairDeviceForm();
    }
    
    table.on('edit(repairDeviceQuotationListTableFilter)', function(obj){ //注：edit是固定事件名，test是table原始容器的属性 lay-filter="对应的值"
        alog.d(obj);  
        var rdid=obj.data.id;
        var fname=obj.field;
        var fvalue=obj.value;
        console.log(repairDeviceQuotationInvoices);
        
        updateRepairDeviceQuotationInvoices(rdid,fname,fvalue);
        
        console.log(repairDeviceQuotationInvoices);
        return false;
      });
    
    table.on('tool(repairDeviceQuotationListTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var id = data.id;
        var sn = data.sn;
        if( layEvent === 'quotation-modify'){ //通过
            if(_repairDeviceStatus>=lockStatus){
                layer.msg("报价信息已经锁定，不能进行操作！");
                return false;
            }
            alog.d(data);
            showQuotationConfirm(data);
        }
        return false;
    });
    
    var quotationConfirmPageIdx;
    
    function showQuotationConfirm(data){
        
        var qutationData = data.quotationInvoice;
        if(undefined === qutationData || null ===qutationData || undefined === qutationData.id || null === qutationData.id ){
            layer.msg("报价信息没有保存，不能进行操作!");
            return false;
        }
        
        alog.d(qutationData);
        
        var areaInfo = apopup.getArea($("#repairDeviceQuotationInvoiceConfirmPage"));
        alog.d(areaInfo);
                
        //repairInvoicePage
        quotationConfirmPageIdx = layer.open({
            id:"repairDeviceQuotationInvoiceConfirmPagePopup",
            type: 1,
            title:  ["编辑报价项目", 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width, areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairDeviceQuotationInvoiceConfirmPage'),
            success: function(layero, index){
                form.val("repairDeviceQuotationInvoiceFormFilter",qutationData);
            },
            btn:["保存","关闭"],
            yes:function(){
                quotationConfirm();
            }
        });
    }
    
    function quotationConfirm(){
        var data = aform.toObject("form[name=repairDeviceQuotationInvoiceForm]");
        if (1==data.confirmStatus) {
            layer.confirm("拒绝报价后，该条目报价信息将不计入总费用，请确认？",function(cidx){
                layer.close(cidx);
                ajx.post({
                    "url":"api/browser/repair/device/save/quotation/invoice/confirm",
                    "data":JSON.stringify(data)
                },function(data){
                    alog.d(data);
                    if (data) {
                        layer.close(quotationConfirmPageIdx);
                        reloadTable();
                    }else{
                        layer.msg("操作失败，请重试！");
                    }                    
                });
            });
        }else if(9==data.confirmStatus){
            layer.confirm("同意报价后，该条目报价信息将计入总费用，请确认？",function(cidx){
                layer.close(cidx);
                ajx.post({
                    "url":"api/browser/repair/device/save/quotation/invoice/confirm",
                    "data":JSON.stringify(data)
                },function(data){
                    alog.d(data);
                    if (data) {
                        layer.close(quotationConfirmPageIdx);
                        reloadTable();
                    }else{
                        layer.msg("操作失败，请重试！");
                    }                    
                });
            });
        }
    }    
    
    function getListConditions(){
        var formdata = aform.toObject("form[name=repairDeviceForm]");
        return {"repairDeviceId":formdata.id,"sn":formdata.sn,"quptationType":1};
    }
   
    function getQuotationInvoices(){
        var quotationInvoices = [];
        var length = repairDeviceQuotationInvoices.length;
        for (var ii = 0; ii < length; ii++) {
            alog.d("ii:"+ii);
            var rdqi = repairDeviceQuotationInvoices[ii];
            alog.d(rdqi);
            if (!(1==rdqi.quotedPrice)) {
                continue;
            }
            var qi = rdqi.quotationInvoice;
            if (1==rdqi.quotedPrice && (undefined === qi || null === qi)) {
                return 501;
            }
            qi.repairDeviceId = rdqi.repairDeviceId;
            qi.deviceId = rdqi.deviceId;
            qi.detectInvoiceId = rdqi.id;
            quotationInvoices.push(qi);            
        }
        return quotationInvoices;
    }
    
    $("form[name=repairDeviceForm]").find("input[name=laborCosts]").change(function(){
        automaticBilling();
        return false;
    });
    
    $("button[name=save-quotation]").click(function(){
       doSaveQuotation(true);
       return false;
    });
    
    var isDoSaveQuotation = false;
    
    function doSaveQuotation(isConfirm){
        var quotationInvoices = getQuotationInvoices();
        alog.d("quotationInvoices:");
        alog.d(quotationInvoices);
        
        if(501==quotationInvoices){
            layer.msg("请完成报价信息后再保存!");
            return false;
        }
        
        if (isDoSaveQuotation) {
            layer.msg("正在保存报价信息，请稍等!");
            return false;
        }
        
        isDoSaveQuotation = true;
        
        automaticBilling();
        
        var repairDeviceData = aform.toObject($("form[lay-filter=repairDeviceFormFilter]"));
        repairDeviceData.userId = aUser.getId();
        repairDeviceData.quotationInvoices = quotationInvoices;
        alog.d(repairDeviceData);
        
        
        if (isConfirm) {
            layer.confirm("是否确定保存报价数据？",function(idx){
                layer.close(idx);
                ajx.post(
                        {
                            "url":"api/browser/repair/device/save/quotation/invoice",
                            "data":JSON.stringify(repairDeviceData)
                        },function(data){
                            isDoSaveQuotation = false;
                            alog.d(data);
                            if(data){
                                layer.msg("报价保存成功!",function(){
                                    reloadPageInfo();
                                });
                            }else{
                                layer.msg("报价保存失败!",function(){
                                    reloadPageInfo();
                                });
                            }
                        },function(code,msg){
                            isDoSaveQuotation = false;
                            alog.d(code+","+msg);
                            layer.msg("报价保存失败!code:"+code+";msg:"+msg,function(){
                                reloadPageInfo();
                            });
                        }
                );
            });
        }else{
            ajx.post(
                    {
                        "url":"api/browser/repair/device/save/quotation/invoice",
                        "data":JSON.stringify(repairDeviceData)
                    },function(data){
                        isDoSaveQuotation = false;
                        alog.d(data);
                        if(data){
                            layer.msg("保存成功!",function(){
                                reloadPageInfo();
                            });
                        }else{
                            layer.msg("保存失败!",function(){
                                reloadPageInfo();
                            });
                        }
                    },function(code,msg){
                        isDoSaveQuotation = false;
                        alog.d(code+","+msg);
                        layer.msg("保存失败!code:"+code+";msg:"+msg,function(){
                            reloadPageInfo();
                        });
                    }
            );
        }
    }
    
    $("button[name=finish-quotation]").click(function(){
        
        var repairDeviceData = aform.toObject($("form[lay-filter=repairDeviceFormFilter]"));
        repairDeviceData.userId = aUser.getId();
        
        layer.confirm("用户同意报价信息，是否确定？（注意：确定报价后不能再修改相关信息。）",function(idx){
            layer.close(idx);
            ajx.post(
                    {
                        "url":"api/browser/repair/device/confirm/quotation",
                        "data":JSON.stringify(repairDeviceData)
                    },function(data){
                        alog.d(data);
                        if(data){
                            reloadPageInfo();
                            layer.msg("报价完成!");
                        }else{
                            layer.msg("操作失败，请确定报价单已经保存并且填写无误!");
                        }
                    },function(code,msg){
                        layer.msg("操作失败，请确定报价单已经保存并且填写无误!");
                    }
            );
        });
        return false;
    });
    
    
    $("button[name=refuse-quotation]").click(function(){
        
        var repairDeviceData = aform.toObject($("form[lay-filter=repairDeviceFormFilter]"));
        repairDeviceData.userId = aUser.getId();
        
        layer.confirm("用户不同意报价，是否确定？<br\>说明:<br/>1、用户不同意报价，将停止维修并将设备返回给客户。",function(idx){
            layer.close(idx);
            ajx.post({
                "url":"api/browser/repair/device/refuse/quotation",
                "data":JSON.stringify(repairDeviceData)
            },function(data){
                if(data){
                    layer.msg("操作成功！即将返回上一层",function(){
                        $("a[name=back-btn]").click();
                        var lurl = $("a[name=back-btn]").attr("href");
                        window.location.href=lurl;
                    });
                    return;
                }
                layer.msg("操作失败！");
            },function(code,msg){
                layer.msg("操作失败！（code:"+code+",msg:"+msg+"）");
            });
        });
       });
    
    function automaticBilling(){
        
        var costTotal = 0;
        
        var length = repairDeviceQuotationInvoices.length;
        
        for(var ii =0; ii<length;ii++){
            alog.d(ii);
            var rdqi = repairDeviceQuotationInvoices[ii];
            alog.d(rdqi);
            
            if(undefined == rdqi.quotationInvoice||null==rdqi.quotationInvoice){
                console.log("continue");
                continue;
            }
            
            if(!(1==rdqi.quotedPrice)){
                continue;
            }
            
            if(1===rdqi.quotationInvoice.confirmStatus){
                continue;
            }
            
            var quantity = parseInt(rdqi.quotationInvoice.quantity);
            if(isNaN(quantity)){
                console.log("continue");
                continue;
            }
            console.log(typeof quantity);
            
            var priceUnit = parseInt(rdqi.quotationInvoice.priceUnit);
            if(isNaN(priceUnit)){
                console.log("continue");
                continue;
            }
            
            var total = quantity*priceUnit;
            
            rdqi.quotationInvoice.priceTotal = total;
            
            $("label[name='priceTotal_"+rdqi.id+"']").html(total);
            costTotal += total;
        }
        alog.d("costTotal:"+costTotal);
        
        var laborCosts = $("form[name=repairDeviceForm]").find("input[name=laborCosts]").val();
        if(""===laborCosts){
            laborCosts = 0;
        }else{
            laborCosts = parseInt(laborCosts);
        }
        
        if(Number.NaN===costTotal){
            costTotal = 0;
        }
        costTotal = laborCosts+costTotal;
        $("form[name=repairDeviceForm]").find("input[name=costTotal]").val(costTotal);
    }
    
    function updateRepairDeviceQuotationInvoices(repairDeviceDetectInvoiceId,fileName,fieldValue){
        for(var ii in repairDeviceQuotationInvoices){
            var rdqi = repairDeviceQuotationInvoices[ii];
            if(rdqi.id==repairDeviceDetectInvoiceId){
                if(undefined == rdqi.quotationInvoice||null==rdqi.quotationInvoice){
                    rdqi.quotationInvoice={};
                }
                var ffname = fileName.substring(17,fileName.length);
                alog.d(ffname);
                rdqi.quotationInvoice[ffname]=fieldValue;
                alog.d("update success");
                break;
            }
        }
        automaticBilling();
    }
    
    exports('repairDeviceQuotationList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

