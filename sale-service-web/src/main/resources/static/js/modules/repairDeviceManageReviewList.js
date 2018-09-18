/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer','laydate', 'table' ,'element','layout','aform','aRepairInvoice','aRepairPackage','aDevice','apopup','aRepairStatus','alog','ajx'], function(exports) {
    
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
                    width : '10%',
                    templet: function(d){
                        return d.repairInvoice.serialNumber;
                    }
                },{
                    field : 'endCustomerName',
                    title : '终端客户名称',
                    width : '10%'                        
                },{
                    field : 'sn',
                    title : 'SN',
                    width : '10%'
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
                    width : '8%',
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
                }
                ,{
                    field : 'detectInvoices',
                    title : '检测报告预览',
                    templet: function(d){
                        var detectInvoices = d.detectInvoices;
                        if (undefined ==detectInvoices|| null==detectInvoices) {
                            return "NA";
                        }
                        var length = detectInvoices.length;
                        var dis="";
                        for(var i=0;i<length;i++){
                            if(dis!=""){
                                dis += "<br/>";
                            }
                            var di = detectInvoices[i];
                            dis += (i+1)+"、"+ di.malfunctionAppearance+"，"+di.malfunctionReason+"，"+di.responsibleParty+"，"+di.repairSuggest+"，"+(1==di.quotedPrice?"需要报价":"不报价");
                        }
                        return dis;
                    }
                },{
                    fixed: 'right', 
                    width:140, 
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
        if( layEvent === 'review-success'){ //通过
            reviewPass([data.id]);
        } else if(layEvent === 'review-failed'){ //删除
            reviewFail([data.id]);          
        } else if(layEvent === 'review-detail'){
            reviewDetail(data);
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
    
    exports('repairDeviceManageReviewList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

