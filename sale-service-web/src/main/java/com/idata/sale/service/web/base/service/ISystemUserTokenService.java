package com.idata.sale.service.web.base.service;

import com.idata.sale.service.web.base.service.dto.SystemUserTokenDto;

public interface ISystemUserTokenService {

    public boolean exist(String token);

    public boolean hasTimeout(String token);

    public SystemUserTokenDto getUserToken(String loginName);

    public SystemUserTokenDto get(String token);

}
