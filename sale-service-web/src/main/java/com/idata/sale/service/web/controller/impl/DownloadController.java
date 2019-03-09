package com.idata.sale.service.web.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idata.sale.service.web.base.dao.dbo.SystemExportTaskDbo;
import com.idata.sale.service.web.base.service.ISystemExportTaskService;
import com.idata.sale.service.web.controller.LoginUserCheck;
import com.idata.sale.service.web.util.FileUtils;

@Controller
public class DownloadController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);

    public DownloadController() {
    }

    @Autowired
    private ISystemExportTaskService exportTaskService;

    @Value("${com.idt.ss.dir.upload.suggestion.attachement}")
    private String suggestionAttachementDir;

    @LoginUserCheck
    @RequestMapping(path = { "/download" }, method = RequestMethod.GET)
    public @ResponseBody ResponseEntity download(@RequestParam(name = "id") String downloadId,
            @RequestParam(name = "category", required = false) String category) {

        String storePath = "";
        String fileSuffix = "";
        String fileName = "";

        if ("customerSuggestionAttachment".equals(category)) {
            storePath = suggestionAttachementDir + downloadId;
            fileSuffix = FileUtils.getFileSuffix(downloadId);

        }
        else {
            SystemExportTaskDbo exportTaskDbo = exportTaskService.get(downloadId);
            if (null == exportTaskDbo || StringUtils.isEmpty(exportTaskDbo.getStorePath())) {
                return new ResponseEntity("没有找到资源", HttpStatus.OK);
            }
            storePath = exportTaskDbo.getStorePath();
            fileSuffix = FileUtils.getFileSuffix(storePath);
            fileName = exportTaskDbo.getDownloadFileName() + fileSuffix;
        }

        try {

            File file = new File(storePath);
            if (!file.exists()) {
                return new ResponseEntity("没有找到资源", HttpStatus.OK);
            }
            if ("".equals(fileName)) {
                fileName = file.getName();
            }
            InputStream inputStream = new FileInputStream(file);
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(Files.size(Paths.get(storePath)));
            headers.set(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment;filename=" + (URLEncoder.encode(fileName, "utf-8")));
            return new ResponseEntity(inputStreamResource, headers, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity("系统异常：" + e.getMessage(), HttpStatus.OK);
        }
    }

}
