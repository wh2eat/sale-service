package com.idata.sale.service.web.rest;

import java.util.List;

public class RestResultFactory {

    public static RestResult getResult(Object rtn, long millis) {
        return getResult(RestCode.Success.getCode(), rtn, "", millis);
    }

    public static RestResult getResult(RestCode code, String msg, long millis) {
        return getResult(code.getCode(), null, msg, millis);
    }

    public static RestResult getResult(RestCode code, long millis) {
        return getResult(code.getCode(), null, code.getDesc(), millis);
    }

    public static RestPageResult getPageResult(List list, long count) {
        RestPageResult restPageResult = new RestPageResult();
        restPageResult.setCode(RestCode.SuccessPage.getCode());
        restPageResult.setCount(count);
        restPageResult.setMsg("success");
        restPageResult.setData(list);
        return restPageResult;
    }

    private static RestResult getResult(int code, Object rtn, String msg, long millis) {

        RestResult result = new RestResult();
        result.setCode(code);
        result.setMessage(msg);
        result.setUseMillis(millis);
        result.setRtn(rtn);
        return result;

    }

}
