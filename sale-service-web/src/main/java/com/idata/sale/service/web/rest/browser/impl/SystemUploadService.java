package com.idata.sale.service.web.rest.browser.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.idata.sale.service.web.util.FileUtils;

@RestController
@RequestMapping(path = "/api/browser/upload")
public class SystemUploadService {

    private final static Set<String> prefixSet = new HashSet<>(1000);

    private volatile static long prefixSetTimeoutMillis = 0;

    @PostConstruct
    public void init() {
        initPrefixSet();
    }

    private void initPrefixSet() {
        LOGGER.info("[][initPrefixSet][start]");

        prefixSet.clear();
        int i = 0;
        while (true) {
            String prefix = RandomStringUtils.randomAlphabetic(8);
            prefix = prefix.toLowerCase();
            if (!prefixSet.contains(prefix)) {
                prefixSet.add(prefix);
                i = i + 1;
            }
            if (1000 == i) {
                break;
            }
        }

        prefixSetTimeoutMillis = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        LOGGER.info("[][initPrefixSet][finish]");
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(SystemUploadService.class);

    public SystemUploadService() {

    }

    @Value("${com.idt.ss.dir.upload.suggestion.attachement}")
    private String suggestionAttachementDir;

    @GetMapping(path = "get/prefix")
    public Object getPrefix() {

        if (prefixSet.isEmpty() || System.currentTimeMillis() > prefixSetTimeoutMillis) {
            synchronized (SystemUploadService.class) {
                if (prefixSet.isEmpty() || System.currentTimeMillis() > prefixSetTimeoutMillis) {
                    initPrefixSet();
                }
            }
        }

        String prefix = prefixSet.iterator().next();
        prefixSet.remove(prefix);

        return prefix;
    }

    @DeleteMapping(path = "suggestion/attachement/{storeUrl}")
    public Object removeSuggestionAttachement(@PathVariable("storeUrl") String storeUrl, HttpServletRequest request) {

        String url = request.getRequestURI();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][removeSuggestionAttachement][" + url + "]");
            LOGGER.debug("[][removeSuggestionAttachement][" + storeUrl + "]");
        }

        String path = getSuggestionAttachementStorePath(url.substring(1));
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            LOGGER.info("[][removeSuggestionAttachement][path:" + path + "]");
        }

        return true;

    }

    private String getSuggestionAttachementStorePath(String storeName) {
        return suggestionAttachementDir + storeName;
    }

    @PostMapping(path = "suggestion/attachement")
    public Object uploadSuggestionAttachement(@RequestParam("file") MultipartFile file,
            @RequestParam("prefix") String prefix, HttpServletResponse response) {

        String fileName = null;

        String uid = UUID.randomUUID().toString();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][uid:" + uid + "]");
        }
        uid = uid.substring(1, uid.length() - 1);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[][][uid:" + uid + "]");
        }
        try {

            String originalName = file.getOriginalFilename();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][fileName:" + originalName + "]");
            }
            int lastIdx = originalName.lastIndexOf(".");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][lastIdx:" + lastIdx + "]");
            }

            String fileSuffix = FileUtils.getFileSuffix(originalName).toLowerCase();

            if (StringUtils.isNotEmpty(fileSuffix)) {
                fileName = prefix + "_" + uid + "." + fileSuffix;
            }
            else {
                fileName = prefix + "_" + uid;
            }

            String path = suggestionAttachementDir + fileName;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[][][path:" + path + "]");
            }
            File dest = new File(path);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            LOGGER.info("[][uploadSuggestionAttachement][success,path:" + path + "]");

            Map<String, String> rtn = new HashMap<>(2);
            rtn.put("path", fileName);
            rtn.put("fileName", originalName);

            return rtn;
        }
        catch (IllegalStateException e) {
            LOGGER.error(" exception happens: " + e.getMessage());
        }
        catch (IOException e) {
            LOGGER.error(" exception happens: " + e.getMessage());
        }
        return null;
    }

}
