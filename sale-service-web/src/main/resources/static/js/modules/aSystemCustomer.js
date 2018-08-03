/**
 * 项目JS主入口 以依赖layui的layer和form模块为例
 */
layui.define([ 'jquery', 'layer', 'table', 'element', 'layout', 'aform',
        'apopup', 'alog' ], function(exports) {

    var $ = layui.$;

    var form = layui.form;

    var aform = layui.aform;

    var table = layui.table;

    var apopup = layui.apopup;

    var alog = layui.alog;

    function _renderTable(type) {

        var colDes = [ [ {
            title : 'ID',
            fixed : 'left',
            width : 60,
            templet : function(row) {
                return row.LAY_INDEX;
            }
        }, {
            field : 'name',
            title : '联系人'
        }, {
            field : 'phone',
            title : '联系电话'
        }, {
            field : 'email',
            title : '邮箱'
        }, {
            field : 'address',
            title : '联系地址'
        } ] ];

        if (1 == type) {
            colDes = [ [ {checkbox: true}, {
                field : 'name',
                title : '联系人'
            }, {
                field : 'phone',
                title : '联系电话'
            }, {
                field : 'email',
                title : '邮箱'
            }, {
                field : 'address',
                title : '联系地址'
            } ] ];
        }

        table.render({
            id : 'systemCustomerListTableId',
            elem : '#systemCustomerListTable',
            page : true,
            loading : true,
            skin : 'row',
            even : false,
            limits : [ 10, 20, 50, 100 ],
            cols : colDes,
            url : 'api/browser/sys/customer/find/list',
            method : "POST",
            where : {
                params : JSON.stringify({})
            },
            done : function(res, curr, count) {
                alog.d("done");
            }
        });
        _setSearchAction();
    }
    
    function _setSearchAction(){
        $("button[name=customerSearchBtn]").click(function(){
            _reloadTable(1);
            return false;
        });
    }
    
    function _reloadTable(type){
        var sd = {};
        if(1===type){
            sd = _getConditions();
        }
        alog.d(sd);
        table.reload('systemCustomerListTableId', {
            url : 'api/browser/sys/customer/find/list',
            method : "POST",
            where : {
                params : JSON.stringify(sd)
            }
        });
    }

    function _getConditions() {
        return aform.toObject("form[name=customerSearchForm]");
    }
    
    function _getSData() {
        var checkStatus = table.checkStatus('systemCustomerListTableId');
        console.log(checkStatus);
        var data = checkStatus.data;
        if(data.length==1){
            return data[0];
        }else if(data.length==0){
            layer.msg("请选择客户信息！");
            return null;
        }else{
            layer.msg("当前为单选模式，请选择单个客户！");
            return null;
        }
    }
    
    function _getConstactData(){
        var data = _getSData();
        if (null!=data) {
            return {"contacts":data.name,"contactAddress":data.address,"contactNumber":data.phone,"email":data.email};
        }
        return null;
    }
    
    function _popupSelectPage(cb){
        var areaInfo = apopup.getArea($("#systemCustomerSelectPage"));
        alog.d(areaInfo);
                
        var systemCustomerSelectPagePopupIdx;
        //repairInvoicePage
        repairInvoicePagePopupIdx = layer.open({
            id:"systemCustomerSelectPagePopup",
            type: 1,
            title:  ["查找联系人", 'font-size:18px;'], //不显示标题栏
            skin: 'layui-layer-rim', //加上边框
            offset: [areaInfo.top, areaInfo.left],
            area: [areaInfo.width, areaInfo.height], //宽高
            shade: true,
            offset: 'auto',
            content: $('#systemCustomerSelectPage'),
            success: function(layero, index){
                systemCustomerSelectPagePopupIdx = index;
                _renderTable(1);
            },
            btn: ['选择', '关闭'],
            yes:function(){
                var sd = _getConstactData();
                alog.d(sd);
                if (null!=sd) {
                    cb(sd);
                    layer.close(systemCustomerSelectPagePopupIdx);
                }
            }
        });
    }
    
    var rtnObj = {
        renderTable : function() {
            _renderTable();
        },
        reloadTable:function(){
            _reloadTable(1);
        },
        renderSTable : function() {
            _renderTable(1);
        },
        reloadSTable:function(){
            _reloadTable(1);
        },
        getSData:function(){
            return _getSData();
        },
        getConstactData:function(){
            return _getConstactData();
        },
        showSelectPage:function(cb){
            _popupSelectPage(cb);
        }
    };

    exports('aSystemCustomer', rtnObj); // 注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});
