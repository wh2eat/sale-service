/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'alog' ], function(exports) {

    var $ = layui.$;

    var alog = layui.alog;

    var undef;

    function request(params,successCallback,failedCallback) {

        alog.d(params);

        var requestParams = {
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                var code = resp.code;
                if (100 == code) {
                    if (successCallback != undef) {
                        successCallback(resp.rtn);
                    }
                } else {
                    if (failedCallback != undef) {
                        failedCallback(code, resp.message);
                    }else{
                        layer.msg("数据加载失败![code:"+code+"]");
                    }
                }
            },
            "error" : function(XMLHttpRequest, textStatus, errorThrown) {
                layer.msg("数据加载失败!");
            }
        };

        // Merge params into requestParams
        $.extend(requestParams, params);

        $.ajax(requestParams);
    }

    var obj = {
        del : function(params, successCallback, failedCallBack) {
            var nparams = $.extend({
                "async" : true,
                "method" : "DELETE",
                "contentType" : "application/json"
            }, params);
            request(nparams, successCallback, failedCallBack);
        },
        get : function(params,successCallback,failedCallBack) {
            
            var nparams =$.extend({
                "async" : true,
                "method" : "GET",
                "contentType" : "application/json"
            },params);
            
            request(nparams,successCallback,failedCallBack);
            
        },syncGet : function(params,successCallback,failedCallBack) {
            
            var nparams =$.extend({
                "async" : false,
                "method" : "GET",
                "contentType" : "application/json"
            },params);
            
            request(nparams,successCallback,failedCallBack);   
        },post : function(params,successCallback,failedCallBack) {
            
            var nparams =$.extend({
                "async" : true,
                "method" : "POST",
                "contentType" : "application/json"
            },params);
            
            request(nparams,successCallback,failedCallBack);
            
        },syncPost : function(params,successCallback,failedCallBack) {
            
            var nparams =$.extend({
                "async" : false,
                "method" : "POST",
                "contentType" : "application/json"
            },params);
            
            request(nparams,successCallback,failedCallBack);   
        }
    };

    exports('ajx', obj); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});
