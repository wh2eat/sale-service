/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer','alog','table','aform','aRepairStatus' ],function(exports){
    
    var $ = layui.$;
    
    var alog = layui.alog;
    
    var table = layui.table;
    
    var aform = layui.aform;
    
    var aRepairStatus = layui.aRepairStatus;
    
    var undef;
    
    function setAction(){
        $("form[name=repairPackageSearchForm]").submit(function(){
            _reloadListTable();
            return false;
        });   
    }
    setAction();
        
    var repairPackageTableUrl='api/browser/repair/package/list';
    
    function _renderListTable(isSearch){
        
        var filterData = _getConditionData();
        
        var isPage=true;
        
        var colsConfig;
        if(0==isSearch){
            colsConfig = [ [ {
                title : 'ID',
                fixed : 'left',
                width : 60,
                templet : function(row) {
                    return row.LAY_INDEX;
                }
            }, {
                field : 'serialNumber',
                title : '编号'
            }, {
                field : 'expressNumber',
                title : '快递单号'
            }, {
                field : 'expressName',
                title : '快递名称'
            }, {
                field : 'status',
                title : '状态',
                templet : function(row) {
                    return aRepairStatus.getBigDisplayText(row.status);
                }
            }, {
                field : 'contacts',
                title : '联系人'
            }, {
                field : 'contactNumber',
                title : '联系电话'
            }, {
                field : 'contactAddress',
                title : '联系地址'
            }, {
                fixed : 'right',
                width : 200,
                align : 'center',
                toolbar : '#repairPackageListTableToolTpl'
            } ] ];
        } else {
            
            isPage=false;
            
            colsConfig = [ [ {
                checkbox : true
            },{
                field : 'invoice',
                title : '维修单号',
                templet: function(d){
                    return d.invoice.serialNumber;
                }
            }, {
                field : 'expressNumber',
                title : '快递单号'
            }, {
                field : 'expressName',
                title : '快递名称'
            }, {
                field : 'contacts',
                title : '联系人'
            }, {
                field : 'contactNumber',
                title : '联系电话'
            } ] ];
        }
        
        table.render({
            id : 'repairPackageListTableId',
            elem : '#repairPackageListTable',
            page : isPage,
            loading: true,
            skin:'row',
            even: false,
            limits:[10,20,50,100],
            cols : colsConfig ,
            url : repairPackageTableUrl,
            method : "POST",
            where:{params:JSON.stringify(filterData)},
            done: function(res, curr, count){
                
            }
        });
    }
    
    function _reloadListTable(){
        var filterData = _getConditionData();
        table.reload('repairPackageListTableId', {
            url: repairPackageTableUrl,
            method : "POST",
            where:{params:JSON.stringify(filterData)}
          });
    }
    
    function _getConditionData(){
        return aform.toObject($("form[name=repairPackageSearchForm]"));
    }
    
    var obj = {
      renderListTable:function(){
          _renderListTable(0);
      },
      reloadListTable:function(){
          _reloadListTable();
      },
      renderSearchListTable:function(){
          _renderListTable(1);
      },
      reloadSearchListTable:function(){
          _reloadListTable();
      },
      getSData:function(){
          var checkStatus = table.checkStatus('repairPackageListTableId');
          console.log(checkStatus);
          var data = checkStatus.data;
          if(data.length=1){
              return data[0];
          }else if(data.length==0){
              layui.msg("请选择维修包！");
              return null;
          }else{
              layui.msg("当前为单选模式，请选择单个维修包数据！");
              return null;
          }
      },
      setRepairInvoice : function (invoiceSerialNumber){
          $("form[name=repairPackageSearchForm]").find("input[name='invoice']").val(invoiceSerialNumber);
      },
      getPackageData:function(isSync,serialNumber,cbSuccess,cbFail){
          if(undef==isSync){
              isSync = true;
          }
          $.ajax({
              "async" : isSync,
              "url" : "api/browser/repair/package/get?serialNumber="+serialNumber+"&"+(new Date().getTime()),
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
    };
  
  exports('aRepairPackage', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
