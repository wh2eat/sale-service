/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table' ,'element','layout','aform','apopup','alog','aSystemCustomer','ajx','aRepairStatus','aUser' ], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog; 
    
    var aUser = layui.aUser;
    
    var aRepairStatus = layui.aRepairStatus;
    
    var ajx = layui.ajx;
    
    var _repairStationUdid = aUser.getRepairStationId();
    
    table.render({
        id : 'repairBackPackageListTableId',
        elem : '#repairBackPackageListTable',
        page : true,
        loading: true,
        skin:'row',
        even: false,
        limits:[10,20,50,100],
        cols : [ [ {
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
            templet: function(row){
                return aRepairStatus.getDisplayText(row.status);
            }
        }, 
        {
            field : 'expressName',
            title : '快递名称'
        },{
            field : 'expressNumber',
            title : '快递单号'
        },
        {
            field : 'contacts',
            title : '联系人'
        }, {
            field : 'contactNumber',
            title : '联系电话',
            templet: function(row){
                return row.contactNumber;
            }
        }, {
            field : 'contactAddress',
            title : '联系地址'
        },{
            fixed: 'right', 
            width: 220, 
            align:'center', 
            toolbar: '#repairBackPackageListTableToolTpl'
        }  ] ],
        url : 'api/browser/repair/back/package/get/list',
        method : "POST",
        where:{params:JSON.stringify({"repairStationUdid":_repairStationUdid})},
        done: function(res, curr, count){
            alog.d("done");
        }
    });
    
    function reloadRepairBackPackageListTable(){
      table.reload('repairBackPackageListTableId', {
         url: 'api/browser/repair/back/package/get/list',
         method : "POST",
         where:{params:JSON.stringify({"repairStationUdid":_repairStationUdid})}
      });
    }

    var layer = layui.layer;
    var repairBackPackageFormPagePopupIdx ;
    
    var repairBackPackagePopupType=0;//0:添加用户；1:查看用户；2:修改用户
    var userPasswordChange = false;
    
    table.on('tool(repairBackPackageListTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var udid = data.serialNumber;
        
        if(layEvent === 'detail'){ //查看
            repairBackPackagePopupType = 1;
            showRepairBackPackagePage(udid);
            return ;
        }
        
        if(data.status>=71){
            layer.msg("返客包已发货，禁止操作!")
            return false;
        }
        
        if(layEvent === 'del'){ //删除
          alog.d("del");
          deleteRepairBackPackage(data.serialNumber,udid);
        } else if(layEvent === 'edit'){ //编辑
            repairBackPackagePopupType = 2;
            showRepairBackPackagePage(udid);
        }else if(layEvent === 'ship'){
            ship(data);
        }
        return false;
      });
    
    
    
    function deleteRepairBackPackage(name,id){
        
        var deleteTip = "是否确定删除返客包："+name+"?";
        
        layer.confirm(deleteTip, function(index){
            layer.close(index);
            $.ajax({
                "async" : false,
                "url" : "api/browser/repair/back/package/delete?id="+id,
                "type" : "DELETE",
                "contentType" : "application/json",
                'beforeSend' : function(request) {
                },
                "success" : function(resp) {
                    if(100==resp.code&&resp.rtn){
                        layer.msg("返客包删除成功!");
                        reloadRepairBackPackageListTable();
                    }else{
                        layer.msg("返客包删除失败!");
                    }
                },
                "error":function(XMLHttpRequest, textStatus, errorThrown){
                }
            });
          });
    }
    
    $("button[name=addRepairBackPackageBtn]").click(function(){
        repairBackPackagePopupType = 0;
        showRepairBackPackagePage();
    });
    
    var emptyFormData = aform.toObject($("#repairBackPackageForm"));
    alog.d(emptyFormData);
    
    function restRepairBackPackageForm(){
        form.val("repairBackPackageFormFilter", emptyFormData);
    }
    
    function showRepairBackPackagePage(udid){
        
        alog.d("udid:"+udid);
        
        restRepairBackPackageForm();
        
        var areaInfo = apopup.getArea($("#repairBackPackageFromPage"),(1==repairBackPackagePopupType));
        alog.d(areaInfo);
        
        $("button[lay-filter='repairBackPackageFormSumitFilter']").removeClass("layui-hide");;
        $("button[lay-filter='repairBackPackageFormRestFilter']").removeClass("layui-hide");
        $("a[name=searchSystemCustomerBtn]").addClass("layui-hide");
        
        alog.d("repairBackPackagePopupType:"+repairBackPackagePopupType);
        
        var popupTitle="添加返客包";
        if(0==repairBackPackagePopupType){
            $("a[name=searchSystemCustomerBtn]").removeClass("layui-hide");
            popupTitle = "添加返客包";
        }else if(1==repairBackPackagePopupType){
            popupTitle = "查看返客包";
            $("button[lay-filter='repairBackPackageFormSumitFilter']").addClass("layui-hide");
            $("button[lay-filter='repairBackPackageFormRestFilter']").addClass("layui-hide");
        }else if(2==repairBackPackagePopupType){
            popupTitle = "修改返客包";
            $("button[lay-filter='repairBackPackageFormRestFilter']").addClass("layui-hide");
        }
        
        //repairBackPackageFromPage
        repairBackPackageFormPagePopupIdx = layer.open({
            id:"repairBackPackageFromPagePopup",
            type: 1,
            title:  [popupTitle, 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width, areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairBackPackageFromPage'),
            success: function(layero, index){
                if ( undefined != udid) {
                    loadRepairBackPackageInfo(udid);
                }
            }
        });
    }
        
    function loadRepairBackPackageInfo(udid){
        $.ajax({
            "async" : false,
            "url" : "api/browser/repair/back/package/get?id="+udid,
            "type" : "GET",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                if(100==resp.code){
                    var repairBackPackageObj = resp.rtn;
                    form.val("repairBackPackageFormFilter",repairBackPackageObj);
                }else{
                    layer.msg("数据加载失败!（"+resp.message+")");
                }
            },
            "error":function(XMLHttpRequest, textStatus, errorThrown){
            }
        });
    }
    
    $("#repairBackPackageForm").submit(function(){
        
        $("button[type='submit']").attr("disabled","disabled");
        $("button[lay-filter='repairBackPackageFormRestFilter']").attr("disabled","disabled");
        
        var repairBackPackageInfo = aform.toObject($("#repairBackPackageForm"));
        var userUdid = aUser.getId();
        
        $.ajax({
            "url" : "api/browser/repair/back/package/save",
            "type" : "POST",
            "data" : JSON.stringify({"backPackage":repairBackPackageInfo,"userId":userUdid}),
            "contentType" : "application/json",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                $("button[lay-filter='repairBackPackageFormSumitFilter']").removeAttr("disabled");
                $("button[lay-filter='repairBackPackageFormRestFilter']").removeAttr("disabled");
                var code = resp.code;
                if (100==code&&resp.rtn) {
                    if(0==repairBackPackagePopupType){
                        layer.msg("返客包添加成功!");
                    }else if(2==repairBackPackagePopupType){
                        layer.msg("返客包修改成功!");
                    }
                    alog.d("repairBackPackageFormPagePopupIdx:"+repairBackPackageFormPagePopupIdx);
                    alog.d("layer.index:"+layer.index);
                    layer.close(repairBackPackageFormPagePopupIdx);
                    reloadRepairBackPackageListTable();
                }else {
                    if(0==repairBackPackagePopupType){
                        layer.msg("返客包添加失败！");
                    }else if(2==repairBackPackagePopupType){
                        layer.msg("返客包修改失败!");
                    }
                }
            },
            "error":function(XMLHttpRequest, textStatus, errorThrown){
            }
        });
        
        return false;
    });
    
    var aSystemCustomer = layui.aSystemCustomer;
    
    $("a[name=searchSystemCustomerBtn]").click(function(){
        aSystemCustomer.showSelectPage(function(data){
            form.val("repairBackPackageFormFilter",data);
        });
    });
    
    
    exports('repairBackPackageList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

