/**
  项目JS主入口
  以依赖layui的layer和form模块为例
**/    
layui.define([ 'jquery', 'layer' ],function(exports){
    
    var $ = layui.$;
    
    var obj = {
            getUserDisplayText:function(code){
                
                if("---"==code||""==code){
                    return "---";
                }

                // 初始状态
                if(code>=10&&code <20){
                    return "已接收";
                }

                // 检测状态
                if(code>=20&&code <30){
                    return "检测中";
                }

                // 报价状态&用户确认状态
                if(code>=30&&code <50){
                    return "报价确认中";
                }


                // 支付状态
                if(code>=50&&code <60){
                    return "等待付款";
                }
                

                // 维修状态
                if(code>=60&&code <=70){
                    return "维修中";
                }

                // 返客状态
                if(code>70){
                    return "维修完成，已返客";
                }
                
                return "未知状态（"+code+"）";
            },
      getDisplayText:function(code){
          
          if("---"==code||""==code){
              return "---";
          }

          // 初始状态
          if(10==code||"received"==code){
              return "已接收";
          }

          // 检测状态
          if(20==code||"checkWait"==code){
              return "等待检测";
          }
          if(21==code||"checkAgain"==code){
              return "重新检测";
          }
          if(22==code||"checking"==code){
              return "正在检测";
          }
          if(23==code||"checked"==code){
              return "已检测";
          }
          if(29==code||"checkFinish"==code){
              return "检测完成";
          }

          // 报价状态
          if(30==code||"QuotationWait"==code){
              return "等待报价";
          }
          if(39==code||"QuotationFinish"==code){
              return "报价完成";
          }

          // 用户确认状态
          if(40==code||"customerConfirmWait"==code){
              return "等待客户确认";
          }
          if(49==code||"CustomerConfirmFinish"==code){
              return "客户确认完成";
          }

          // 支付状态
          if(50==code||"payWait"==code){
              return "等待支付";
          }
          if(59==code||"payFinish"==code){
              return "支付完成";
          }

          // 维修状态
          if(60==code||"repairWait"==code){
              return "等待维修";
          }
          if(61==code||"repairAgain"==code){
              return "重新维修";
          }
          if(62==code||"repairing"==code){
              return "正在维修";
          } 
          if(69==code||"repairFinish"==code){
              return "维修完成";
          }

          // 返客状态
          if(70==code||"backWait"==code){
              return "等待返客";
          }
          if(code>=71||"backing"==code){
              return "已返客，已完成";
          }
          if(71==code||"backing"==code){
              return "返客中";
          }
          if(79==code||"backFinish"==code){
              return "返客完成";
          }

          // 终态
          if(100==code||"finish"==code){
              return "已完成";
          }
          
          return "未知状态（"+code+"）";
      },
      getBigDisplayText:function(code){
          
          if("---"==code||""==code){
              return "---";
          }

          // 初始状态
          if(code>=10&&code<20){
              return "已接收";
          }

          // 检测状态
          if(code>=20&&code<60){
              return "处理中";
          }
          
          if(code>=60&&code<=70){
              return "维修中";
          }
          
          if(code>70){
              return "维修完成，已返客";
          }
          
          
          return "未知状态（"+code+"）";
      }
    };
  
  exports('aRepairStatus', obj); //注意，这里是模块输出的核心，模块名必须和use时的模块名一致
});    
