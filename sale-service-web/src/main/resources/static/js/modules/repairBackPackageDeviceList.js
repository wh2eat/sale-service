/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table' ,'element','layout','aform','apopup','alog','ajx','aDevice','aRepairStatus','aUser' ], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog; 
    
    var aUser = layui.aUser;
    
    var aDevice = layui.aDevice;
    
    var ajx = layui.ajx;
    
    var aRepairStatus = layui.aRepairStatus;
    
    var _repairStationUdid = aUser.getRepairStationId();
    
    var bpstatus=71;
    
    initRepairBackPackageData();
    
    function initRepairBackPackageData(){
        var rbpsn = getBackPacakgeSerialNumber();
        ajx.get({
            "url":"api/browser/repair/back/package/get/last?id="+rbpsn+"&repairStationId="+_repairStationUdid
        },function(rsp){
            alog.d("initRepairBackPackageData");
            alog.d(rsp);
            if(null==rsp){
                setRepairBackPackageSerialNumber("-1");
                setRepairBackPackageData([]);
            }else{
                bpstatus = rsp.status;
                setRepairBackPackageSerialNumber(rsp.serialNumber);
                setRepairBackPackageData([rsp]);
            }
            renderRepairBackPackageDeviceTable();
        });
    }
    
    function setRepairBackPackageData(rbpdata){
        table.render({
            id : 'repairBackPackageListTableId',
            elem : '#repairBackPackageListTable',
            page : false,
            loading: true,
            skin:'row',
            even: false,
            data:rbpdata,
            limits:[10,20,50,100],
            cols : [ [  {
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
            }] ],
            done: function(res, curr, count){
                
            }
        });
    }
    
    function renderAllRepairBackPackageTable(){
        table.render({
            id : 'allRepairBackPackageListTableId',
            elem : '#allRepairBackPackageListTable',
            page : true,
            loading: true,
            skin:'row',
            even: false,
            limits:[10,20,50,100],
            cols : [ [  {checkbox: true},{
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
            }] ],
            url : 'api/browser/repair/back/package/get/list',
            method : "POST",
            where:{params:JSON.stringify(getAllRepairBackPackageData())},
            done: function(res, curr, count){
                
            }
        });
    }
    
    
    function getRepairBackPackageData(){
        var fdata = aform.toObject("form[name=repairBackPackageForm]");
        alog.d(fdata);
        return fdata;
    }
    
    function getAllRepairBackPackageData(){
        var fdata = aform.toObject("form[name=repairBackPackageForm]");
        fdata.serialNumber = undefined;
        alog.d(fdata);
        return fdata;
    }
    
    function getBackPacakgeSerialNumber(){
        var dd = getRepairBackPackageData();
        return dd.serialNumber;
    }
    
    function getRepairBackPackageDeviceData(){
        var data = getRepairBackPackageData();
        return {"repairStationUdid":data.repairStationUdid,"repairBackPackageSerialNumber":data.serialNumber};
    }
    
    function getRepairBackPackageDeviceManageData(){
        var repairPackageSerialNumber = $("form[name=repairDeviceSearchForm]").find("input[name=repairPackageSerialNumber]").val();
        var sns = $("form[name=repairDeviceSearchForm]").find("input[name=sn]").val();
        alog.d("repairPackageSerialNumber:"+repairPackageSerialNumber);
        var data = getRepairBackPackageData();
        return {"repairStationUdid":data.repairStationUdid,"repairBackPackageSerialNumber":data.serialNumber,"allWaitBack":"1","repairPackageSerialNumber":repairPackageSerialNumber,"sn":sns};
    }
    
    
    var repairDeviceTableUrl='api/browser/repair/device/list';
    
    function renderRepairBackPackageDeviceTable(){
        table.render({
            id : 'repairBackPackageDeviceListTableId',
            elem : '#repairBackPackageDeviceListTable',
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
                field : 'endCustomerName',
                title : '终端客户名称'
            }, {
                field : 'sn',
                title : 'SN',
                width : 90,
            },{
                field : 'status',
                title : '状态',
                width : 130,
                templet: function(d){
                    return aRepairStatus.getDisplayText(d.status);
                }
            },{
                field : 'result',
                title : '维修结果',
                width : 130,
                templet: function(d){
                    if(1==d.result){
                        return "未维修，客户拒绝报价";
                    }else if(100==d.result){
                        return "维修成功";
                    }
                    return "处理中";
                }
            },{
                field : 'payStatus',
                title : '支付状态',
                width : 140,
                templet: function(d){
                    var charge = d.charge;
                    if(0==charge){
                        return "免费维修";
                    }else{
                        if(0==d.payStatus){
                            return "收费维修(未支付)";
                         }else if(1==d.payStatus){
                             return "收费维修(用户拒绝)";
                         }else if(9==d.payStatus){
                             return "收费维修(已支付)";
                         }
                    }
                    return "处理中";
                }
            },{
                field : 'attachment',
                title : '附件'
            }, {
                field : 'deliveryTime',
                title : '送修日期'
            },{
                field : 'shipRemark',
                title : '备注'
            },{
                fixed: 'right', 
                width : 80, 
                align:'center', 
                toolbar: '#repairBackPackageDeviceListTableToolTpl'
            }  ] ],
            url : repairDeviceTableUrl,
            method : "POST",
            where:{params:JSON.stringify(getRepairBackPackageDeviceData())},
            done: function(res, curr, count){
                
            }
        });
    }
    
    function reloadRepairBackPackageDeviceListTable(){
        table.reload('repairBackPackageDeviceListTableId', {
            url: repairDeviceTableUrl,
            method : "POST",
            where:{params:JSON.stringify(getRepairBackPackageDeviceData())}
        });
    }
    
    function initDeviceManageListTable(){
        table.render({
            id : 'repairBackPackageDeviceManageListTableId',
            elem : '#repairBackPackageDeviceManageListTable',
            page : true,
            loading: true,
            skin:'row',
            even: false,
            limits:[10,20,50,100],
            cols : [ [ 
            {checkbox: true}, {
                field : 'repairPackage',
                title : '维修包',
                templet: function(d){
                    return d.repairPackage.serialNumber;
                }
            }, {
                field : 'sn',
                title : 'SN',
                width : 90,
            },{
                field : 'status',
                title : '状态',
                width : 130,
                templet: function(d){
                    return aRepairStatus.getDisplayText(d.status);
                }
            },{
                field : 'result',
                title : '维修结果',
                width : 130,
                templet: function(d){
                    if(1==d.result){
                        return "未维修，客户拒绝报价";
                    }else if(100==d.result){
                        return "维修成功";
                    }
                    return "处理中";
                }
            },{
                field : 'payStatus',
                title : '支付状态',
                width : 140,
                templet: function(d){
                    var charge = d.charge;
                    if(0==charge){
                        return "免费维修";
                    }else{
                        if(0==d.payStatus){
                            return "收费维修(未支付)";
                         }else if(1==d.payStatus){
                             return "收费维修(用户拒绝)";
                         }else if(9==d.payStatus){
                             return "收费维修(已支付)";
                         }
                    }
                    return "处理中";
                }
            },{
                field : 'attachment',
                title : '附件'
            }, {
                field : 'deliveryTime',
                title : '送修日期'
            } ] ],
            url : repairDeviceTableUrl,
            method : "POST",
            where:{params:JSON.stringify(getRepairBackPackageDeviceManageData())},
            done: function(res, curr, count){
            }
        });
    }
    
    function reloadDeviceManageListTable(){
        var sd = getRepairBackPackageDeviceManageData();
        alog.d(sd);
        table.reload('repairBackPackageDeviceManageListTableId', {
            url: repairDeviceTableUrl,
            method : "POST",
            where:{params:JSON.stringify(sd)}
        });
    }
    
    $("button[name=repairDeviceSearchBtn]").click(function(){
        reloadDeviceManageListTable();
        return false;
    });
    
    function getSelectDatas(){
        var checkStatus = table.checkStatus('repairBackPackageDeviceManageListTableId');
        alog.d(checkStatus);
        return checkStatus.data;
    }
    
    function confirmAddDevice(){
        var datas = getSelectDatas();
        if(null===datas||datas.length==0){
            layer.msg("请选择设备！");
            return ;
        }
        
        alog.d(datas);
        
        var deviceIds=[];
        var length = datas.length;
        alog.d(length);
        
        for(var ii=0;ii<length;ii++){
            var dt = datas[ii];
            alog.d(dt);
            deviceIds.push(dt.id);
        }
        alog.d(deviceIds);
        
        layer.confirm("是否确定将选择设备加入返客包?",function(idx){
            ajx.post({
                "url":"api/browser/repair/device/add/back/package",
                "data":JSON.stringify({"backPackageSerialNumber":getBackPacakgeSerialNumber(),"ids":deviceIds}),
            },function(data){
                alog.d(data);
                reloadDeviceManageListTable();
                reloadRepairBackPackageDeviceListTable();
            });
            layer.close(idx);
        });
        return false;
    }
    
    
    var layer = layui.layer;
    var repairBackPackageFormPagePopupIdx ;
    
    var repairBackPackagePopupType=0;//0:添加用户；1:查看用户；2:修改用户
    var userPasswordChange = false;
    
    table.on('tool(repairBackPackageDeviceListTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        alog.d(data);
        
        if(layEvent === 'del'){ //删除
            if (bpstatus>=71) {
                layer.msg("返客包已发货，不能操作!");
                return false;
            }
          alog.d("del");
          deleteRepairBackPackage(data.sn,data.id);
        } 
        return false;
      });
    
    function deleteRepairBackPackage(name,id){
        
        var deleteTip = "是否确定将设备移除返客包?(SN："+name+")";
        var data = {"backPackageSerialNumber":getBackPacakgeSerialNumber(),"ids":[id]}
        
        layer.confirm(deleteTip, function(index){
            layer.close(index);
            ajx.post({
                "url" : "api/browser/repair/device/remove/back/package",
                "data" : JSON.stringify(data)
                },
                function(rdata){
                    alog.d(rdata);
                    layer.msg("操作成功!",function(){
                        reloadRepairBackPackageDeviceListTable();
                    });                    
                });
          });
    }
    
    $("a[name=addDeviceToBackPacakgeBtn]").click(function(){
        showBackPackageDeviceManagePage();
    });
    
    function showBackPackageDeviceManagePage(){
        
        if (bpstatus>=71) {
            layer.msg("返客包已发货，不能操作!");
            return false;
        }
        
        var areaInfo = apopup.getArea($("#repairBackPackageDeviceManagePage"));
        alog.d(areaInfo);
        
        //repairBackPackageFromPage
        repairBackPackageFormPagePopupIdx = layer.open({
            id:"repairBackPackageDeviceManagePagePopup",
            type: 1,
            title:  ["添加返客设备", 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width, areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairBackPackageDeviceManagePage'),
            btn: ['添加', '关闭'] //可以无限个按钮
            ,yes: function(index, layero){
                confirmAddDevice();
            },btn2: function(index, layero){
                reloadDeviceManageListTable();
                layer.close(index);
            },
            success: function(layero, index){
                initDeviceManageListTable();
            }
        });
    }
    
    
    $("a[name=switchRepairBackPackageBtn]").click(function(){
        showSwitchBackPackagePage();
    });
    
    function showSwitchBackPackagePage(){
        
        var areaInfo = apopup.getArea($("#repairBackPackageSwitchPage"));
        alog.d(areaInfo);
        
        //repairBackPackageFromPage
        repairBackPackageFormPagePopupIdx = layer.open({
            id:"repairBackPackageSwitchPagePopup",
            type: 1,
            title:  ["切换返客包", 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width, areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#repairBackPackageSwitchPage'),
            btn: ['切换', '取消'] //可以无限个按钮
            ,yes: function(index, layero){
                var rs = switchRepairBackPackage();
                if (rs) {
                    layer.close(index);
                    initRepairBackPackageData();
                }
            },btn2: function(index, layero){
                layer.close(index);
            },
            success: function(layero, index){
                renderAllRepairBackPackageTable();
            }
        });
    }
    
    function switchRepairBackPackage(){
        var sdata = getSelectRepairBackPackage();
        alog.d(sdata);
        if(null==sdata||sdata.length==0){
            layer.msg("请选择返客包!");
            return false;            
        }
        
        if(sdata.length>1){
            layer.msg("请选择单个返客包!");
            return false;                        
        }
        
        var rbpsn = sdata[0].serialNumber;
        setRepairBackPackageSerialNumber(rbpsn);
        return true;
    }
    
    function setRepairBackPackageSerialNumber(rbpsn){
        $("form[name=repairBackPackageForm]").find("input[name=serialNumber]").val(rbpsn);
    }
    
    function getRepairBackPackageSerialNumber(){
        return $("form[name=repairBackPackageForm]").find("input[name=serialNumber]").val();
    }
    
    function getSelectRepairBackPackage(){
        var checkStatus = table.checkStatus('allRepairBackPackageListTableId');
        alog.d(checkStatus);
        return checkStatus.data;
    }
    
    $("a[name=shipBtn]").click(function(){
        if(bpstatus>=71){
            layer.msg("返客包已发货，禁止操作!")
            return false;
        }
        var sern = getRepairBackPackageSerialNumber();
        if(""===sern||"-1"===sern){
            layer.msg("数据错误，操作失败！");
            return false;
        }
        ship(sern);
    });
    
    function ship(sern){
        ajx.get({
            "url":"api/browser/repair/back/package/get/ship/device/list?id="+sern
        },function(rsd){
            alog.d(rsd);
            var datas = rsd;
            var tip = null;
            if(null!=datas){
                tip = '<span class="layui-text">请检查设备附件以及配件是否完整！</span>';
                tip += "<table class='layui-table'>";
                tip += "<tr><td>SN</td><td>附件</td><td>备注</td></tr>";
                var llength = datas.length;
                for(var ii=0;ii<llength;ii++){
                    tip +="<tr>";
                    tip +="<td>"+datas[ii].sn+"</td>";
                    tip +="<td>"+datas[ii].attachment+"</td>";
                    tip +="<td>"+datas[ii].shipRemark+"</td>";
                    tip +="</tr>";
                }
                tip += "</table>";
            }else{
                tip='<span class="layui-text">请确认设备已打包并完成发货！</span>';
            }
            alog.d("open ship");
            layer.open({
                id:"repairBackPackageShipConfirmPagePopup",
                type: 1,
                title:  ["发货确认", 'font-size:18px;'], //不显示标题栏
                skin: 'layui-layer-rim', //加上边框
                shade: true,
                area:'500px',
                offset: 'auto',
                content: tip,
                btn: ['完成发货', '取消'] //可以无限个按钮
                ,yes: function(index, layero){
                  layer.close(index);
                  ajx.get({
                      "url":"api/browser/repair/back/package/ship?id="+sern
                  },function(rsd11){
                      alog.d(rsd11);
                      initRepairBackPackageData();
                  },function(code,msg){
                      layer.msg("操作失败，请确定返客包中设备完成维修（设备状态：待返客）！");
                  });
                },btn2: function(index, layero){
                    layer.close(index);
                },
                success: function(layero, index){
                    alog.d("success");
                }
            });
        },function(code,msg){
            layer.msg("数据加载失败！");
        });
    }
    exports('repairBackPackageDeviceList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

