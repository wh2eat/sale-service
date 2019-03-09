/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer','laytpl','form','aform', 'element','alog','ajx','upload'], function(exports) {
    var form = layui.form;
    var $ = layui.$;
    var alog = layui.alog;
    var ajx = layui.ajx;
    var aform = layui.aform;
    var element = layui.element;
    
    
    var fdata = aform.toObject("form[name=customerSuggestionForm]");
    
    initTable();
    
    function initTable(){
        layer.load();
        ajx.post({
            "url":"/web/api/browser/customer/suggestion/get/detail",
            "data":JSON.stringify(fdata)
        },function(resp){
            if (null!=resp) {
                for(var fname in resp){
                    if(fname=='attachment'){
                        var attachmentStr = resp[fname];
                        if(null!=attachmentStr&&""!=attachmentStr){
                            var attachments = attachmentStr.split(",");
                            var length = attachments.length;
                            for(var i=0;i<length;i++){
                                $(".attachment").append("<li><a href='../download?id="+attachments[i]+"&category=customerSuggestionAttachment' target='_blank'>附件-"+(i+1)+"</a></li>")
                            }
                        }
                    }else{
                        $("."+fname).html(resp[fname]);
                    }
                }
            }
            
            layer.closeAll();
        },function(error){
            layer.closeAll();
        });
    }
    
    
//    $("div[name=uploadAttachmentList]").on("click",".upload-attachment-view",function(){
//        layer.msg("view");
//    });
  
    
//    $("button[name=uploadAttchmentButton]").click(function(){
        
//        wx.chooseImage({
//            count: 1, // 默认9
//            sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
//            sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
//            success: function (res) {
//                console.log(res);
//                var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
//            }
//        });
//        return false;
//    });
    
//    ajx.post(
//            {
//                "url":"/web/api/browser/wxpn/get/sign",
//                "data":JSON.stringify({"url":"http://192.168.1.135/web/suggestion"})
//             },
//             function(resp){
//                 console.log(resp);
//                 wxInit(resp);
//             },
//             function(fresp){
//                 console.log(fresp);
//             }
//    );
    
    function wxInit(data){
        wx.config({
            debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: data.appId, // 必填，公众号的唯一标识
            timestamp: data.timestamp, // 必填，生成签名的时间戳
            nonceStr: data.noncestr, // 必填，生成签名的随机串
            signature: data.sign,// 必填，签名
            jsApiList: ["chooseImage","previewImage","uploadImage","downloadImage"] // 必填，需要使用的JS接口列表
        });
        
        wx.ready(function(){
            // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
            
            wx.checkJsApi({
                jsApiList: ["chooseImage","previewImage","uploadImage","downloadImage"], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                success: function(res) {
                // 以键值对的形式返回，可用的api值true，不可用为false
                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                }
            });
        });
        
        wx.error(function(res){
            // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        });
    }
        
    exports('customerSuggestion', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});
