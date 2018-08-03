package com.idata.sale.service.web.base.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.service.ISystemUserTokenService;
import com.idata.sale.service.web.base.service.dto.SystemUserTokenDto;

@Service
public class SystemUserTokenService implements ISystemUserTokenService {

    private Map<String, SystemUserTokenDto> userTokenMap = new ConcurrentHashMap<>();

    private Map<String, String> tokenUserMap = new ConcurrentHashMap<>();

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemUserTokenService.class);

    public SystemUserTokenService() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean exist(String token) {
        if (tokenUserMap.containsKey(token)) {
            return true;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[SystemUserTokenService][][not found token:" + token + "]");
        }
        return false;
    }

    @Override
    public boolean hasTimeout(String token) {
        if (!tokenUserMap.containsKey(token)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[SystemUserTokenService][][not found token:" + token + "]");
            }
            return true;
        }
        String loginName = tokenUserMap.get(token);
        SystemUserTokenDto userTokenDto = userTokenMap.get(loginName);
        if (null != userTokenDto) {
            long expiredMillis = userTokenDto.getExpiredMillis();
            if (System.currentTimeMillis() < expiredMillis) {
                return false;
            }
            else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("[SystemUserTokenService][][token has expire,loginName:" + loginName + ";token:"
                            + token + "]");
                }
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[SystemUserTokenService][][not found loginName by token,loginName:" + loginName + ";token:"
                    + token + "]");
        }
        return true;

    }

    @Override
    public SystemUserTokenDto getUserToken(String loginName) {

        SystemUserTokenDto userTokenDto = null;
        if (!userTokenMap.containsKey(loginName)) {
            userTokenDto = new SystemUserTokenDto();
            userTokenDto.setLoginName(loginName);
        }
        else {
            userTokenDto = userTokenMap.get(loginName);
        }

        long nowMillis = System.currentTimeMillis();
        userTokenDto.setGetMillis(nowMillis);
        userTokenDto.setExpiredMillis((nowMillis + 30 * 60 * 1000l));

        String token = generateToken(loginName, nowMillis);
        while (tokenUserMap.containsKey(token)) {
            token = generateToken(loginName, nowMillis);
        }
        tokenUserMap.put(token, loginName);

        userTokenDto.setToken(token);
        String key = generateKey();
        userTokenDto.setKey(key);

        userTokenMap.put(loginName, userTokenDto);

        return new SystemUserTokenDto(token, key);
    }

    @Override
    public SystemUserTokenDto get(String token) {

        if (!tokenUserMap.containsKey(token)) {
            return null;
        }

        String loginName = tokenUserMap.get(token);

        if (!userTokenMap.containsKey(loginName)) {
            return null;
        }

        SystemUserTokenDto tokenDto = userTokenMap.get(loginName);
        if (null == tokenDto) {
            return null;
        }

        return tokenDto;
    }

    private String generateToken(String loginName, long millis) {
        StringBuilder builder = new StringBuilder();
        builder.append(RandomUtils.nextInt(100)).append("&").append(loginName).append("&").append(millis);
        return DigestUtils.md5Hex(builder.toString());
    }

    private String generateKey() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    public static void main(String[] args) {

    }

}
