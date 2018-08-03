/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer','laytpl','form','aform', 'element','alog','aUser','ajx','aRepairStatus'], function(exports) {
    var form = layui.form;
    var $ = layui.$;
    var alog = layui.alog;
    var aUser = layui.aUser;
    var ajx = layui.ajx;
    var aform = layui.aform;
    var element = layui.element;
    var aRepairStatus = layui.aRepairStatus;
    
    var laytpl = layui.laytpl;
    
    alog.d($("#serarchFormSearchType"));
    
    form.on('select(searchTypeFilter)', function(data){
        var sv = data.value;
        if('expressName'===sv){
            $("form[name=searchForm]").find("input[name=searchValue]").attr("placeholder","请输入快递单号");
        }else if("deviceSn"===sv){
            $("form[name=searchForm]").find("input[name=searchValue]").attr("placeholder","请输入设备SN");
        }
    });
    
    $("button[name=searchBtn]").click(function(){
        doSearch();
        return false;
    });
    
    function doSearch(){
        var pdata = aform.toObject($("form[name=searchForm]"));
        alog.d(pdata);
        var sv = pdata.searchValue;
        if (""===sv) {
            layer.msg("请输入搜索内容!");
            return false;
        }
        ajx.post({
            "url":"/web/api/browser/repair/device/search/process",
            "data":JSON.stringify(pdata)
        },function(rdata){
            alog.d(rdata);
            if (undefined === rdata|| null===rdata) {
                $(".process-result-not-found").removeClass("layui-hide");
                $(".process-result").addClass("layui-hide");
                return;
            }
            $(".process-result-not-found").addClass("layui-hide");
            $(".process-result").removeClass("layui-hide");
            var lth = rdata.length;
            for (var ii = 0; ii < lth; ii++) {
                alog.d(rdata[ii]);
                var ss = rdata[ii].status;
                alog.d(ss);
                alog.d(aRepairStatus);
                alog.d(aRepairStatus.getBigDisplayText(ss));
                rdata[ii].displayStatus = aRepairStatus.getBigDisplayText(ss);
                var rds = rdata[ii].repairDevices;
                if (undefined === rds || null ===rds) {
                    continue;
                }
                var rdsl = rds.length;
                for (var jj = 0; jj < rdsl; jj++) {
                    var ds = aRepairStatus.getUserDisplayText(rds[jj].status);
                    if(rds[jj].status>70&&(undefined !==rds[jj].repairBackPackage || null!==rds[jj].repairBackPackage)){
                        ds += "，快递信息："+rds[jj].repairBackPackage.expressNumber+"（"+rds[jj].repairBackPackage.expressName+"）";
                    }
                    rds[jj].displayStatus = ds;
                }
            }
            
            var searchResultTpl =$("#searchResultTpl").html();
            alog.d(searchResultTpl);
            
            laytpl(searchResultTpl).render(rdata, function(html){
                $(".process-result").html(html);
                element.render("collapse");
            });
            
        });
    }
    
    var table = layui.table;
    
    function renderTable(datas){
        //执行渲染
        table.render({
          id:"userRepairProcessTableId",
          elem: '#userRepairProcessTable' //指定原始表格元素选择器（推荐id选择器）
          ,data:datas
          ,cols: [{
              field: 'sn',
              title: '设备SN'
          },{
              field: 'model',
              title: '机型'
          },{
              field: 'status',
              title: '状态'
          }] 
        });
    }
    
    exports('search', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});
