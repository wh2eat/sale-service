/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer','alog', 'table','form','aform','laydate'],function(exports){
    
    var $ = layui.$;
    
    var alog = layui.alog;
    
    var table = layui.table;
    
    var laydate = layui.laydate;
    
    var form = layui.form;
    
    var aform = layui.aform;
        
    var undef;
    
    function _getAll(cbSuccess,cbFail){
        $.ajax({
            "url" : "api/browser/repair/station/get/all?"+(new Date().getTime()),
            "type" : "GET",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                if(100==resp.code){
                    if (cbSuccess!=undef) {
                        cbSuccess(resp.rtn);
                    }
                }else{
                    if (cbFail!=undef) {
                        cbFail(resp.code,resp.message);
                    }else{
                        layer.msg("数据加载失败!（"+resp.message+")");
                    }
                }
            },
            "error":function(XMLHttpRequest, textStatus, errorThrown){
                layer.msg("数据加载失败!");
            }
        });
    }
    
    function _setOptions(fieldSection,sid){
        _getAll(function(datas){
            if (null!=datas&&datas.length>0) {
                var length = datas.length;
                var options = "";
                for(var i=0;i<length;i++){
                    var dd = datas[i];
                    options += "<option value='"+dd.id+"' "+(sid===dd.id?"selected":"")+">"+dd.name+"</option>"
                }
                alog.d(fieldSection);
                if(undef!==fieldSection){
                    $(fieldSection).html(options);
                }
            }
        });
    }
    
    var obj = {
      getAll:function(cbSuccess,cbFail){
          _getAll(cbSuccess,cbFail);
      },
      setOptions:function(fieldSection,sid){
          _setOptions(fieldSection,sid);
      }
    };
  
  exports('aRepairStation', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
