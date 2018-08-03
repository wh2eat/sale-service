/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'element','alog','aUser','ajx' ], function(exports) {
    var form = layui.form;
    var $ = layui.$;
    var alog = layui.alog;
    var aUser = layui.aUser;
    var ajx = layui.ajx;
    
    ajx.get({
        "url":"api/browser/repair/station/get/all"
    },function(data){
        if( null!=data && data.length>0){
            var showList = false;
            var userType = aUser.getType();
            if(10===userType){
                showList = true;
            }
            var crsid = aUser.getRepairStationId();
            alog.d("crsid:"+crsid);
            var ll = data.length;
            var listHtml="";
            for(var i=0;i<ll;i++){
                var dd = data[i];
                if(dd.udid===crsid){
                    $("span[name=login-repair-station-name]").html(dd.name);
                }
                listHtml += "<dd><a href='#' class='switch-repair-station' rsid='"+dd.udid+"' >"+dd.name+"</a></dd>";
            }
            $("dl[name=login-repair-station-list]").html(listHtml);
        }
    });
    
    $("dl[name=login-repair-station-list]").on("click",".switch-repair-station",function(){
        var rsid = $(this).attr("rsid");
        alog.d($(this).attr("rsid"));
        var nurl = window.location.href;
        alog.d(nurl)
        var sIdx = nurl.indexOf("?");
        alog.d("sIdx:"+sIdx);
        if(sIdx==-1){
            window.location.href=nurl+"?rsid="+rsid;
        }else{
            var rsidIdx = nurl.indexOf("rsid="); 
            alog.d("rsidIdx:"+rsidIdx);
            if(rsidIdx>-1){
                var paramStr = nurl.substring(sIdx+1,nurl.length);
                alog.d(paramStr);
                var params = paramStr.split("&");
                alog.d(params);
                var newParamStr = "";
                var pl = params.length;
                for(var ii=0;ii<pl;ii++){
                    if(params[ii].indexOf("rsid=")==-1){
                        newParamStr += "&"+params[ii];
                    }
                }
                alog.d("newParamStr:"+newParamStr);
                window.location.href=nurl.substring(0,sIdx)+"?rsid="+rsid+newParamStr;
            }else{
                window.location.href=nurl.substring(0,sIdx)+"?rsid="+rsid+"&"+nurl.substring(sIdx+1,nurl.length);
            }
        }
    })

    $(".sv-button-load-content").click(function() {
        var actionName = $(this).attr("name");
        var contentUrl = $(this).attr("contentUrl");
        $(".sv-main-content").load(contentUrl, {}, function() {
            alog.d("load finish.");
        });
    });

    $("a[name=logoutBtn]").click(function() {
        $("form[name=logoutForm]").submit();
    });

    exports('layout', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});
