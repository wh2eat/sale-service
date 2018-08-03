/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table' , 'aUser','element','layout','aRepairStation','aform','apopup','alog','aSystemUser' ], function(exports) {
    
    var $ = layui.$;
    
    var form = layui.form;
    
    var aform = layui.aform;

    var table = layui.table;
    
    var apopup = layui.apopup;
    
    var alog = layui.alog; 
    
    var aSystemUser = layui.aSystemUser;
    
    var aUser = layui.aUser;
    
    var repairStationId = aUser.getRepairStationId();
    
    var aRepairStation = layui.aRepairStation;
    
    var userRepairStationId = $("#userRepairStationId").attr("id");
    alog.d("userRepairStationId:"+userRepairStationId);
    if('userRepairStationId'===userRepairStationId){
        aRepairStation.setOptions("#userRepairStationId");
    }
    
    table.render({
        id : 'userListTableId',
        elem : '#userListTable',
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
            field : 'loginName',
            title : '登陆名称'
        },
        {
            field : 'userName',
            title : '用户名'
        }, 
        {
            field : 'type',
            title : '类型',
            templet: function(row){
                return aSystemUser.getDisplay(row.type);
            }
        }, {
            field : 'email',
            title : '邮箱'
        }, {
            field : 'lastLoginTime',
            title : '上次登陆时间'
        }, {
            field : 'repairStation',
            title : '所属维修站',
            templet: function(row){
                return row.repairStation.name;
            }
        }
        ,{
            fixed: 'right', 
            width:150, 
            align:'center', 
            toolbar: '#userListTableToolTpl'
        }  ] ],
        url : 'api/browser/user/get/list',
        method : "POST",
        where:{params:JSON.stringify({"repairStationId":repairStationId})},
        done: function(res, curr, count){
            alog.d("done");
        }
    
    });
    
    function reloadUserListTable(){
        table.reload('userListTableId', {
            url: 'api/browser/user/get/list',
            method : "POST",
            where:{params:JSON.stringify({"repairStationId":repairStationId})}
          });
    }

    var layer = layui.layer;
    var userAddPagePopupIdx ;
    
    var userPopupType=0;//0:添加用户；1:查看用户；2:修改用户
    var userPasswordChange = false;
    
    table.on('tool(userListTableFilter)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        
        alog.d(obj);
        
        var data = obj.data; //获得当前行数据
        var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
        var tr = obj.tr; //获得当前行 tr 的DOM对象
        
        var userId = data.id;
        if(layEvent === 'detail'){ //查看
            userPopupType = 1;
            showUserPage(userId);
        } else if(layEvent === 'del'){ //删除
          alog.d("del");
          deleteUser(data.loginName,userId);
        } else if(layEvent === 'edit'){ //编辑
            userPopupType = 2;
            showUserPage(userId);
        }
        return false;
      });
    
    function deleteUser(loginName,id){
        
        var deleteTip = "是否确定删除用户："+loginName+"?";
        
        layer.confirm(deleteTip, function(index){
            layer.close(index);
            $.ajax({
                "async" : false,
                "url" : "api/browser/user/delete?loginName="+loginName+"&id="+id,
                "type" : "DELETE",
                "contentType" : "application/json",
                'beforeSend' : function(request) {
                },
                "success" : function(resp) {
                    if(100==resp.code&&resp.rtn){
                        layer.msg("用户删除成功!");
                        reloadUserListTable();
                    }else{
                        layer.msg("用户删除失败!");
                    }
                },
                "error":function(XMLHttpRequest, textStatus, errorThrown){
                }
            });
          });
    }
    
    $("button[name=addUserBtn]").click(function(){
        userPopupType = 0;
        showUserPage();
    });
    
    var emptyFormData = aform.toObject($("#userAddForm"));
    alog.d(emptyFormData);
    
    function restUserForm(){
        form.val("userFormFilter", emptyFormData);
    }
    
    function showUserPage(udid){
        
        restUserForm();
        
        var areaInfo = apopup.getArea($("#userFromPage"),(1==userPopupType));
        alog.d(areaInfo);
        
        $("button[lay-filter='userAddFormSumitFilter']").removeClass("layui-hide");;
        $("button[lay-filter='userAddFormRestFilter']").removeClass("layui-hide");
        
        alog.d("userPopupType:"+userPopupType);
        
        var popupTitle="添加系统用户";
        if(0==userPopupType){
            popupTitle = "添加用户";
        }else if(1==userPopupType){
            popupTitle = "查看用户";
            $("button[lay-filter='userAddFormSumitFilter']").addClass("layui-hide");;
            $("button[lay-filter='userAddFormRestFilter']").addClass("layui-hide");
        }else if(2==userPopupType){
            popupTitle = "修改用户";
            $("button[lay-filter='userAddFormRestFilter']").addClass("layui-hide");
        }
        
        //userFromPage
        userAddPagePopupIdx = layer.open({
            id:"userFromPagePopup",
            type: 1,
            title:  [popupTitle, 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width, areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#userFromPage'),
            success: function(layero, index){
                if ( undefined != udid) {
                    loadUserInfo(udid);
                }
            }
        });
    }
    
    var localPassword = '#$%123ASF';
    
    function loadUserInfo(udid){
        $.ajax({
            "async" : false,
            "url" : "api/browser/user/get?fieldName=id&fieldValue="+udid,
            "type" : "GET",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                if(100==resp.code){
                    var userObj = resp.rtn;
                    userObj.password=localPassword;
                    userObj.confirmPassword=localPassword;
                    form.val("userFormFilter",userObj);
                }else{
                    layer.msg("数据加载失败!（"+resp.message+")");
                }
            },
            "error":function(XMLHttpRequest, textStatus, errorThrown){
            }
        });
    }
    
    var passwordPattern = /^[\S]{6,12}$/;
    
    form.verify({
        loginName: function(value, item){ //value：表单的值、item：表单的DOM对象
          if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
            return '登陆名称不能有特殊字符';
          }
          if(/(^\_)|(\__)|(\_+$)/.test(value)){
            return '登陆名称首尾不能出现下划线\'_\'';
          }
          if(/^\d+\d+\d$/.test(value)){
            return '登陆名称不能全为数字';
          }
          
//          if(existByLoginName(value)){
//              return '登陆名称已经存在';
//          }
          
        }
        //我们既支持上述函数式的方式，也支持下述数组的形式
        //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
        ,pass: function(value,item){
            if (value!=localPassword && null==value.match(passwordPattern)) {
                return '密码必须6到12位，且不能出现空格';
            }   
        },confirmPass:function(value,item){
            var password = $("#userAddForm input[name='password']").val();
            if(value!=password){
                return "两次密码输入不一致";
            }
        } 
      }); 
    
    //动态校验登陆名称是否存在
    function existByLoginName(loginName){
        return existByField("loginName",loginName);
    }
    
    function existByField(fieldName,fieldValue){
        
        var udid = $("#userAddForm input[name=id]").val();
        
        var postData={"fieldName":fieldName,"fieldValue":fieldValue,"udid":udid};
        alog.d(postData);
        var exist = false;
        $.ajax({
            "async" : false,
            "url" : "api/browser/user/exist",
            "type" : "POST",
            "data" : JSON.stringify(postData),
            "contentType" : "application/json",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                exist = resp.rtn;
            },
            "error":function(XMLHttpRequest, textStatus, errorThrown){
            }
        });
        return exist;
    }
    
    form.on('submit(userAddFormSumitFilter)',function(data){
        
        $("button[lay-filter='userAddFormSumitFilter']").attr("disabled","disabled");
        $("button[lay-filter='userAddFormRestFilter']").attr("disabled","disabled");
        
        alog.d('userAddFormSumitFilter');
        alog.d(data);
        alog.d(data.field);
        var userInfo = data.field;
        var userType = userInfo.type;
                
        userInfo.type=undefined;
        
        if(localPassword==userInfo.password){
            userInfo.password=undefined;
        }
        
        $.ajax({
            "url" : "api/browser/user/save",
            "type" : "POST",
            "data" : JSON.stringify({"user":userInfo,"userType":userType}),
            "contentType" : "application/json",
            'beforeSend' : function(request) {
            },
            "success" : function(resp) {
                alog.d(resp);
                $("button[lay-filter='userAddFormSumitFilter']").removeAttr("disabled");
                $("button[lay-filter='userAddFormRestFilter']").removeAttr("disabled");
                var code = resp.code;
                if (100==code&&resp.rtn) {
                    if(0==userPopupType){
                        layer.msg("用户添加成功!");
                    }else if(2==userPopupType){
                        layer.msg("用户修改成功!");
                    }
                    alog.d("userAddPagePopupIdx:"+userAddPagePopupIdx);
                    alog.d("layer.index:"+layer.index);
                    layer.close(userAddPagePopupIdx);
                    reloadUserListTable();
                }else {
                    if(0==userPopupType){
                        layer.msg("用户添加失败！");
                    }else if(2==userPopupType){
                        layer.msg("用户修改失败!");
                    }
                }
            },
            "error":function(XMLHttpRequest, textStatus, errorThrown){
            }
        });
        
        return false;
    });
    
    
    alog.d("userList start");
    exports('userList', {}); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});

