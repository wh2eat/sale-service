/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table' ,'element','layout','aform','apopup','alog' ], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog; 
    
    table.render({
        id : 'repairStationListTableId',
        elem : '#repairStationListTable',
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
            field : 'name',
            title : '维修站'
        },
        {
            field : 'contacts',
            title : '联系人'
        }, {
            field : 'contactNumber',
            title : '联系电话',
            templet: function(row){
                return row.contactNumber+"/"+row.contactPhone;
            }
        }, 
        {
            field : 'company',
            title : '所属公司'
        },{
            field : 'address',
            title : '地址'
        },{
            fixed: 'right', 
            width:180, 
            align:'center', 
            toolbar: '#repairStationListTableToolTpl'
        }  ] ],
        url : 'api/browser/repair/station/get/list',
        method : "POST",
        where:{params:JSON.stringify({})},
        done: function(res, curr, count){
            alog.d("done");
        }
    });
    
    function reloadRepairStationListTable(){
      table.reload('repairStationListTableId', {
         url: 'api/browser/repair/station/get/list',
         method : "POST",
         where:{params:JSON.stringify({})}
      });
    }

    var layer = layui.layer;
    var repairStationFormPagePopupIdx ;
    
    var repairStationPopupType=0;//0:添加用户；1:查看用户；2:修改用户
    var userPasswordChange = false;
    
    table.on('tool(repairStationListTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var udid = data.udid;
        if(layEvent === 'detail'){ //查看
            repairStationPopupType = 1;
            showRepairStationPage(udid);
        } else if(layEvent === 'del'){ //删除
          alog.d("del");
          deleteRepairStation(data.name,udid);
        } else if(layEvent === 'edit'){ //编辑
            repairStationPopupType = 2;
            showRepairStationPage(udid);
        }
        return false;
      });
    
    function deleteRepairStation(name,id){
        
        var deleteTip = "是否确定删除维修站："+name+"?";
        
        layer.confirm(deleteTip, function(index){
            layer.close(index);
            $.ajax({
                "async" : false,
                "url" : "api/browser/repair/station/delete?id="+id,
                "type" : "DELETE",
                "contentType" : "application/json",
                'beforeSend' : function(request) {
                },
                "success" : function(resp) {
                    if(100==resp.code&&resp.rtn){
                        layer.msg("维修站删除成功!");
                        reloadRepairStationListTable();
                    }else{
                        layer.msg("维修站删除失败!");
                    }
                },
                "error":function(XMLHttpRequest, textStatus, errorThrown){
                }
            });
          });
    }
    
    $("button[name=addRepairStationBtn]").click(function(){
        repairStationPopupType = 0;
        showRepairStationPage();
    });
    
    var emptyFormData = aform.toObject($("#repairStationForm"));
    alog.d(emptyFormData);
    
    function restRepairStationForm(){
        form.val("repairStationFormFilter", emptyFormData);
    }
    
    function showRepairStationPage(udid){
        
        alog.d("udid:"+udid);
        
        restRepairStationForm();
        
        var areaInfo = apopup.getArea($("#repairStationFromPage"),(1==repairStationPopupType));
        alog.d(areaInfo);
        
        $("button[lay-filter='repairStationFormSumitFilter']").removeClass("layui-hide");;
        $("button[lay-filter='repairStationFormRestFilter']").removeClass("layui-hide");
        
        alog.d("repairStationPopupType:"+repairStationPopupType);
        
        var popupTitle="添加维修站";
        if(0==repairStationPopupType){
            popupTitle = "添加维修站";
        }else if(1==repairStationPopupType){
            popupTitle = "查看维修站";
            $("button[lay-filter='repairStationFormSumitFilter']").addClass("layui-hide");
            $("button[lay-filter='repairStationFormRestFilter']").addClass("layui-hide");
        }else if(2==repairStationPopupType){
            popupTitle = "修改维修站";
            $("button[lay-filter='repairStationFormRestFilter']").addClass("layui-hide");
        }
        
        //repairStationFromPage
        repairStationFormPagePopupIdx = layer.open({
            id:"repairStationFromPagePopup",
            type: 1,
            title:  [popupTitle, 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width, areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairStationFromPage'),
            success: function(layero, index){
                if ( undefined != udid) {
                    loadRepairStationInfo(udid);
                }
            }
        });
    }
        
    function loadRepairStationInfo(udid){
        $.ajax({
            "async" : false,
            "url" : "api/browser/repair/station/get?id="+udid,
            "type" : "GET",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                if(100==resp.code){
                    var repairStationObj = resp.rtn;
                    form.val("repairStationFormFilter",repairStationObj);
                }else{
                    layer.msg("数据加载失败!（"+resp.message+")");
                }
            },
            "error":function(XMLHttpRequest, textStatus, errorThrown){
            }
        });
    }
    
    form.on('submit(repairStationFormSumitFilter)',function(data){
        
        $("button[lay-filter='repairStationFormSumitFilter']").attr("disabled","disabled");
        $("button[lay-filter='repairStationFormRestFilter']").attr("disabled","disabled");
        
        alog.d('repairStationFormSumitFilter');
        alog.d(data);
        alog.d(data.field);
        var repairStationInfo = data.field;
        
        $.ajax({
            "url" : "api/browser/repair/station/save",
            "type" : "POST",
            "data" : JSON.stringify({"repairStation":repairStationInfo}),
            "contentType" : "application/json",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                $("button[lay-filter='repairStationFormSumitFilter']").removeAttr("disabled");
                $("button[lay-filter='repairStationFormRestFilter']").removeAttr("disabled");
                var code = resp.code;
                if (100==code&&resp.rtn) {
                    if(0==repairStationPopupType){
                        layer.msg("维修站添加成功!");
                    }else if(2==repairStationPopupType){
                        layer.msg("维修站修改成功!");
                    }
                    alog.d("repairStationFormPagePopupIdx:"+repairStationFormPagePopupIdx);
                    alog.d("layer.index:"+layer.index);
                    layer.close(repairStationFormPagePopupIdx);
                    reloadRepairStationListTable();
                }else {
                    if(0==repairStationPopupType){
                        layer.msg("维修站添加失败！");
                    }else if(2==repairStationPopupType){
                        layer.msg("维修站修改失败!");
                    }
                }
            },
            "error":function(XMLHttpRequest, textStatus, errorThrown){
            }
        });
        
        return false;
    });
    
    
    exports('repairStationList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

