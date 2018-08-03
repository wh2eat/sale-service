/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer','alog','aRepairStatus', 'table','aUser','form','aform','laydate'],function(exports){
    
    var $ = layui.$;
    
    var alog = layui.alog;
    
    var table = layui.table;
    
    var laydate = layui.laydate;
    
    var form = layui.form;
    
    var aform = layui.aform;
    
    var aRepairStatus = layui.aRepairStatus;
            
    var undef;
    
    var repairInvoiceTableUrl='api/browser/repair/invoice/list';
    
    function getConditionData(){
        return aform.toObject($("form[name=repairInvoiceSearchForm]"));
    }
    
    function setAction(){
        
        $("form[name=repairInvoiceSearchForm]").submit(function(){
            alog.d("--------- submit -----------");
            
            var cds = getConditionData();
            alog.d(cds);
            
            try {
                reloadListTable(cds);   
            } catch (e) {
                alog.d(e);
            }            
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });
    }
    
    function setDateInput(){
        laydate.render({ 
            elem: '#repairInvoiceCreateTime'
        });
        
    }
    
    setAction();
    
    setDateInput();
    
    
    function renderListTable(model,scb){
        
        var cds = getConditionData();
        
        if(undef==model){
            model=0;
        }
        
        var isPage = true;
        var isEven = true;
        
        var colsConfig;
        if(0==model){
            colsConfig=[ [ {
                title : 'ID',
                fixed : 'left',
                width : 60,
                templet: function(row){
                    return row.LAY_INDEX;
                  }
            }, {
                field : 'serialNumber',
                title : '编号'
            }, {
                field : 'status',
                title : '状态',
                templet: function(d){
                    return aRepairStatus.getBigDisplayText(d.status);
                }
            }, {
                field : 'contacts',
                title : '联系人'
            }, {
                field : 'email',
                title : '邮箱'
            }, {
                field : 'contactAddress',
                title : '联系地址'
            }, {
                field : 'createTime',
                title : '创建时间'
            },{
                fixed: 'right', 
                width:200, 
                align:'center', 
                toolbar: '#repairInvoiceListTableToolTpl'
            }  ] ];
        }else if(1==model){
            
            isPage = false;
            
            colsConfig=[ [
                {checkbox: true},
                {
                    field : 'serialNumber',
                    title : '编号'
                }, {
                    field : 'status',
                    title : '状态',
                    templet: function(d){
                        return aRepairStatus.getBigDisplayText(d.status);
                      }
                },{
                    field : 'contacts',
                    title : '联系人'
                }, {
                    field : 'email',
                    title : '邮箱'
                }, {
                    field : 'contactAddress',
                    title : '联系地址'
                }, {
                    field : 'createTime',
                    title : '创建时间'
                }  ] ]; 
        }
        
        table.render({
            id : 'repairInvoiceListTableId',
            elem : '#repairInvoiceListTable',
            page : isPage,
            loading: true,
            skin:'row',
            even: isEven,
            limits:[10,20,50,100],
            cols : colsConfig,
            url : repairInvoiceTableUrl,
            method : "POST",
            where:{params:JSON.stringify(cds)},
            done: function(res, curr, count){
                alog.d("done");
                alog.d(scb);
                if (undefined !== scb) {
                    alog.d("run scb");
                    scb.apply();
                }
            }
        });
    }
    
    function reloadListTable(){
        var cds = getConditionData();
        table.reload('repairInvoiceListTableId', {
            url: repairInvoiceTableUrl,
            method : "POST",
            where:{params:JSON.stringify(cds)}
        });
    }
    
    var obj = {
      getInvoiceData:function(isSync,serialNumber,cbSuccess,cbFail){
          $.ajax({
              "async" : isSync,
              "url" : "api/browser/repair/invoice/get?serialNumber="+serialNumber+"&"+(new Date().getTime()),
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
      },
      renderTable : function(scb){
          alog.d(scb);
          renderListTable(0,scb);          
      },
      renderSTable : function(){
          renderListTable(1);          
      },
      reloadTable: function (conditions){
          reloadListTable();
      },
      getSData:function(){
          var checkStatus = table.checkStatus('repairInvoiceListTableId');
          console.log(checkStatus);
          var data = checkStatus.data;
          if(data.length=1){
              return data[0];
          }else if(data.length==0){
              layui.msg("请选择维修单！");
              return null;
          }else{
              layui.msg("当前为单选模式，请选择单个维修单数据！");
              return null;
          }
      }
    };
  
  exports('aRepairInvoice', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
