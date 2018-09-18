/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer','laydate', 'table' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var laydate = layui.laydate;
    
    var aRepairInvoice = layui.aRepairInvoice;
    
    var aRepairPackage = layui.aRepairPackage;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog;
    
    var aDevice = layui.aDevice;
    
    var aRepairStatus = layui.aRepairStatus;
    
    var ajx = layui.ajx;
    
    
    function setRepairDeviceForm(){
        var _repairDeviceId=$("form[name=repairDeviceHiddenForm]").find("input[name=id]").val();
        var _repairDeviceSn=$("form[name=repairDeviceHiddenForm]").find("input[name=sn]").val();
        ajx.get({
            "url":"api/browser/repair/device/get?id="+_repairDeviceId+"&sn="+_repairDeviceSn
        },function(data){
            form.val("repairDeviceInfoFormFilter",data);
            renderListTable();
        },function(code,msg){
            alog.d(code+":"+message);
        });
    }
    setRepairDeviceForm();
    
    var repairDeviceTableUrl='api/browser/repair/device/find/detect/invoice/list';
    
    function renderListTable(){
        
        var conditions = getConditions();
        alog.d(conditions);
        
        table.render({
            id : 'repairDeviceDetectInvoiceListTableId',
            elem : '#repairDeviceDetectInvoiceListTable',
            page : true,
            loading: true,
            skin:'row',
            even: false,
            limits:[10,20,50,100],
            cols : [ [{
                title : 'ID',
                width : 60,
                templet: function(row){
                    return row.LAY_INDEX;
                  }
                },{
                    field : 'malfunctionAppearance',
                    title : '故障现象',
                    width : '20%'                        
                },{
                    field : 'malfunctionReason',
                    title : '故障原因',
                    width : '20%'
                },{
                    field:'responsibleParty',
                    title:"责任方",
                    width : '15%'
                },{
                    field : 'repairSuggest',
                    title : '修复建议',
                    width : '15%'
                },{
                    field : 'quotedPrice',
                    title : '报价',
                    width : '10%',
                    templet: function(d){
                        return '1'==d.quotedPrice?"是":"否";
                    }                    
                },
                {
                    align:'center', 
                    toolbar: '#repairDeviceDetectInvoiceListTableToolTpl'
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
        return {"repairDeviceId":getRepairDeviceId()};
    }
    
    function getRepairDeviceId(){
        return $("form[name=repairDeviceHiddenForm]").find("input[name=id]").val();
    }
    
    function reloadRepairDeviceListTable(){
        var conditions = getConditions();
        table.reload('repairDeviceDetectInvoiceListTableId', {
            url: repairDeviceTableUrl,
            method : "POST",
            where:{params:JSON.stringify(conditions)}
          });
    }
    
    $("form[name=repairDeviceSearchForm]").submit(function(){
        reloadRepairDeviceListTable();
        return false;
    });        
    
    table.on('tool(repairDeviceDetectInvoiceListTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var id = data.id;
        var sn = data.sn;
        if( layEvent === 'cancel-quotation'){ //通过
            
        } else if(layEvent === 'quotation'){ //删除
            
        } else if(layEvent === 'delete'){
            
        } 
        return false;
    });
    
    setBtnActions();
    function setBtnActions(){
        
        $("button[name=batch-review-success]").click(function(){
            batchReviewSuccess();
           return false; 
        });
        
        $("button[name=batch-review-failed]").click(function(){
            batchReviewFailed();
            return false; 
        });
    }    
    
    function batchReviewSuccess(){
        
        var datas = getCheckedDatas();
        if(null==datas){
            layer.msg("请选择审核通过的设备！");
            return false;
        }
        
        var rdids = getRdids(datas);
        layer.confirm("是否确定已选择设备检测通过？",function(){
            reviewPass(rdids);
        });
    }
    
    function batchReviewFailed(){
        
        var datas = getCheckedDatas();
        if(null==datas){
            layer.msg("请选择审核不通过的设备！");
            return false;
        }
        var rdids = getRdids(datas);
        layer.confirm("是否确定已选择设备检测不通过？",function(){
            reviewFail(rdids);
        });
        return false;
    }
    
    function getRdids(datas){
        var rdids = [];
        var length = datas.length;
        alog.d("length:"+length);
        for(var ii = 0;ii<length;ii++){
            alog.d(datas[ii]);
            rdids.push(datas[ii].id);
        }
        return rdids;
    }
    
    function getCheckedDatas(){
        var checkStatus = table.checkStatus('repairDeviceManageListTableId');
        var datas = checkStatus.data;
        alog.d(datas);
        if (null===datas||datas.length===0) {
            return null;
        }
        return datas;
    }
    
    var reviewDetailConfirmIdx;
    
    function reviewDetail(rowData){
        var detectInvoices = rowData.detectInvoices;
        if (undefined ==detectInvoices|| null==detectInvoices) {
            return "NA";
        }
        var length = detectInvoices.length;
        var dis="<table style='width:100%' class='layui-table'>";
        dis += "<tr>" +
                "<td>序号</td>" +
                 "<td>故障现象</td>" +
                 "<td>故障原因</td>" +
                 "<td>责任方</td>" +
                 "<td>修复建议</td>" +
                 "<td>报价</td>" +
                 "</tr>";
        for(var i=0;i<length;i++){
            var di = detectInvoices[i];
            dis += "<tr>" +
            "<td>"+(i+1)+"</td>" +
            "<td>"+ di.malfunctionAppearance+"</td>" +
            "<td>"+di.malfunctionReason+"</td>" +
            "<td>"+di.responsibleParty+"</td>" +
            "<td>"+di.repairSuggest+"</td>" +
            "<td>"+(1==di.quotedPrice?"是":"否")+"</td>" +
            "</tr>";
        }
        dis+="</table>";
        
        var confirmTip = "<div style='padding:20px;'>设备SN："+rowData.sn+"<br/>检测报告如下：<br/>"+dis+"</div>";
        
        reviewDetailConfirmIdx = layer.open({
            id:"reviewConfirmPopupId",
            type: 1,
            title:  ["检测审核", 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            area:'800px',
            shade: true,
            offset: 'auto',
            content: confirmTip ,
            btn: ['通过','不通过'],
            yes:function(){
                var rdids = [rowData.id];
                reviewPass(rdids);
            },
            btn2:function(){
                var rdids = [rowData.id];
                reviewFail(rdids);
            }
        });
    }
    
    function closeReviewDetailConfirm(){
        
        if (undefined === reviewDetailConfirmIdx || null === reviewDetailConfirmIdx) {
            return;
        }
        
        layer.close(reviewDetailConfirmIdx);
        reviewDetailConfirmIdx = null;
    }
    
    function reviewPass(rdids){
        reviewFinish(true,rdids);
    }
        
    function reviewFail(rdids){
        reviewFinish(false,rdids);
    }
    
    function reviewFinish(isPass,rdids){
        
        alog.d(rdids);
        
        var result = 'fail';
        if(isPass){
            result = 'pass';
        }
                
        layer.confirm(isPass?"是否确定通过审核？":"是否确定不通过审核?",function(){
            ajx.post({
                "url" : "api/browser/repair/device/detect/review/finish",
                "data" : JSON.stringify({"result":result,"repairDeviceIds":rdids})
                },
                function(resp) {
                    reloadRepairDeviceListTable();
                    layer.msg("操作成功!",function(){
                        closeReviewDetailConfirm();
                    });
                },
                function(code,message){
                    layer.msg("操作失败!（code:"+code+";message:"+message+"）");
                }
            );
        });
    }
    
    exports('repairDeviceManageReviewDetectInvoiceList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

