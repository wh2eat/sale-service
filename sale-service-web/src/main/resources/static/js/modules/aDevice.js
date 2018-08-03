/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer','alog' ],function(exports){
    
    var $ = layui.$;
    var alog = layui.alog;
    
    var obj = {
      getCurrency:function(currency){
                if(undefined !== currency && null !== currency){
                    if(1==currency){
                        return "美元";
                    }
                }   
                return "人民币";
      },
      getUserName:function(obj){
          alog.d(obj);
          if(undefined !== obj && null !== obj){
              var un = obj.userName;
              alog.d(un);
              if(undefined !== un && null !== un && ""!==un){
                    return un;
              }
          }   
          return "";
      },
      getConfirmStatus:function(qi){
          if(undefined !== qi && null !== qi){
              var cd = qi.confirmStatus;
              if(1==cd){
                  return "用户拒绝";
              }
          }
          return "是";
      },
      getWarrantyText:function(code){

          if(0==code||"OutWarranty"==code){
              return "保外";
          }

          if(10==code ||"UnderWarranty"==code){
              return "保内";
          } 
          
          if(20==code || "AgreementWarranty"==code){
              return "协议延保";
          }
          if(30==code ||"AgreementWholeWarranty"==code){
              return "协议全保";
          }
          
          return "未知状态（"+code+"）";
      },
      getPayTypeText:function(code){
          
          if(0==code){
              return "现付";
          }

          if(1==code ){
              return "月付";
          } 

          return "未知类型（"+code+"）";
      },
      isCharge:function(charge){
          
          if(1==charge){
              return "是";
          }

          return "否";
      },
      formatCostTotal:function(rd){
          
          var costTotal = rd.costTotal;
          if (undefined === costTotal||null === costTotal) {
              costTotal = "0";
          }
          var currency = rd.currency;
          if(1===currency){
              currency = "美元";
          }else{
              currency = "人民币";
          }
          
          return costTotal+"（"+currency+"）";
      }
    };
  
  exports('aDevice', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
