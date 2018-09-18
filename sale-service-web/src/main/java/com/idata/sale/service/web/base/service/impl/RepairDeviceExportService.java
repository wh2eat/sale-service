package com.idata.sale.service.web.base.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idata.sale.service.web.base.dao.impl.RepairBackPackageDao;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceDao;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceDetectInvoiceDao;
import com.idata.sale.service.web.base.dao.impl.RepairDeviceQuotationInvoiceDao;
import com.idata.sale.service.web.base.dao.impl.RepairPackageDao;
import com.idata.sale.service.web.base.dao.impl.SystemRepairStationDao;
import com.idata.sale.service.web.base.dao.impl.SystemUserDao;
import com.idata.sale.service.web.base.service.IDirService;
import com.idata.sale.service.web.base.service.IRepairDeviceExportService;
import com.idata.sale.service.web.util.FileUtils;
import com.idata.sale.service.web.util.TimeUtil;;

@Service
public class RepairDeviceExportService implements IRepairDeviceExportService {

    private final static Logger logger = LoggerFactory.getLogger(RepairDeviceExportService.class);

    public RepairDeviceExportService() {
        // TODO Auto-generated constructor stub
    }

    @Autowired
    private RepairDeviceDao repairDeviceDao;

    @Override
    public String export(Date startTime, Date endTime) {

        int count = repairDeviceDao.count(startTime, endTime);
        logger.info("[][export][start:" + TimeUtil.formatByYYYYmmDDhhMMSS(startTime) + ";end:"
                + TimeUtil.formatByYYYYmmDDhhMMSS(endTime) + ",total:" + count + "]");

        int pageSize = 1000;
        int allPage = count / pageSize;
        if (count % pageSize > 0) {
            allPage += 1;
        }

        int pageNum = 1;
        List<Map<String, Object>> allDevices = new ArrayList<>(count);
        try {
            for (int i = 1; i <= allPage; i++) {
                List<Map<String, Object>> repairDevices = getRepairDevice(startTime, endTime, pageNum, pageSize);
                if (CollectionUtils.isNotEmpty(repairDevices)) {
                    allDevices.addAll(repairDevices);
                }
                else {
                    break;
                }
            }

        }
        catch (SQLException e) {
            logger.error("[][][]", e);
        }

        try {
            return export(allDevices);
        }
        catch (IOException e) {
            logger.error("[][][]", e);
        }

        return null;

    }

    @Autowired
    private RepairPackageDao repairPackageDao;

    @Autowired
    private RepairBackPackageDao repairBackPackageDao;

    @Autowired
    private SystemUserDao systemUserDao;

    @Autowired
    private SystemRepairStationDao repairStationDao;

    @Autowired
    private RepairDeviceDetectInvoiceDao deviceDetectInvoiceDao;

    @Autowired
    private RepairDeviceQuotationInvoiceDao deviceQuotationInvoiceDao;

    private List<Map<String, Object>> getRepairDevice(Date startTime, Date endTime, int pageNum, int pageSize)
            throws SQLException {

        List<Map<String, Object>> rdMaps = repairDeviceDao.getMapList(TimeUtil.formatByYYYYmmDDhhMMSS(startTime),
                TimeUtil.formatByYYYYmmDDhhMMSS(endTime), pageNum, pageSize);

        if (CollectionUtils.isEmpty(rdMaps)) {
            return null;
        }

        Set<String> repairDeviceIds = new HashSet<String>();
        Set<String> deviceIds = new HashSet<>();
        Set<String> userIds = new HashSet<>();
        Set<String> repairStationIds = new HashSet<>();
        Set<String> repairPacakgeIds = new HashSet<>();
        Set<String> repairBackPackageIds = new HashSet<>();
        for (Map<String, Object> map : rdMaps) {
            String rdid = String.valueOf(map.get("id"));
            if (StringUtils.isNotEmpty(rdid)) {
                repairDeviceIds.add(rdid);
            }
            String did = String.valueOf(map.get("device_id"));
            if (StringUtils.isNotEmpty(did) && !deviceIds.contains(did)) {
                deviceIds.add(did);
            }

            Object ruIdObj = map.get("detect_user_id");
            if (null != ruIdObj) {
                String ruId = String.valueOf(ruIdObj);
                if (StringUtils.isNotEmpty(ruId) && !userIds.contains(ruId)) {
                    userIds.add(ruId);
                }
            }

            Object rsidObj = map.get("repair_station_id");
            if (null != rsidObj) {
                String rsid = String.valueOf(rsidObj);
                if (StringUtils.isNotEmpty(rsid) && !repairStationIds.contains(rsid)) {
                    repairStationIds.add(rsid);
                }
            }

            Object rpidObj = map.get("repair_package_id");
            if (null != rpidObj) {
                String rpid = String.valueOf(rpidObj);
                if (StringUtils.isNotEmpty(rpid) && !repairPacakgeIds.contains(rpid)) {
                    repairPacakgeIds.add(rpid);
                }
            }

            Object rbpidObj = map.get("repair_back_package_id");
            if (null != rbpidObj) {
                String rbpid = String.valueOf(rbpidObj);
                if (StringUtils.isNotEmpty(rbpid) && !repairBackPackageIds.contains(rbpid)) {
                    repairBackPackageIds.add(rbpid);
                }
            }

        }

        Map<String, Map<String, Object>> repairPackageMap = repairPackageDao.getIdMMap(repairPacakgeIds);

        Map<String, Map<String, Object>> repairBackPackageMap = repairBackPackageDao.getIdMMap(repairBackPackageIds);

        Map<String, Map<String, Object>> userMap = systemUserDao.getStringIdMaps(userIds);

        Map<String, Map<String, Object>> repairStationMap = repairStationDao.getRepairStationMap(repairStationIds);

        Map<String, List<Map<String, Object>>> repairDetectMaps = deviceDetectInvoiceDao
                .getRepairDeviceDetectInvoiceMap(repairDeviceIds);
        System.out.println("[][][repairDetectMaps:" + repairDetectMaps.toString() + "]");

        Map<String, List<Map<String, Object>>> repairQuotationMaps = deviceQuotationInvoiceDao
                .getRepairDeviceQuotationInvoiceMap(repairDeviceIds);
        System.out.println("[][][repairQuotationMaps:" + repairQuotationMaps.toString() + "]");

        List<Map<String, Object>> exportMaps = new ArrayList<>();

        for (Map<String, Object> map : rdMaps) {
            String rdid = String.valueOf(map.get("id"));
            if (StringUtils.isNotEmpty(rdid)) {
                repairDeviceIds.add(rdid);
            }

            String ruId = null == map.get("detect_user_id") ? "" : String.valueOf(map.get("detect_user_id"));
            if (StringUtils.isNotEmpty(ruId) && userMap.containsKey(ruId)) {
                map.put("repair_user_name", userMap.get(ruId).get("user_name"));
            }

            String rsid = null == map.get("repair_station_id") ? "" : String.valueOf(map.get("repair_station_id"));
            if (StringUtils.isNotEmpty(rsid) && repairStationMap.containsKey(rsid)) {
                map.put("repair_station_name", repairStationMap.get(rsid).get("name"));
            }

            String rpid = null == map.get("repair_package_id") ? "" : String.valueOf(map.get("repair_package_id"));
            if (StringUtils.isNotEmpty(rpid) && repairPackageMap.containsKey(rpid)) {
                map.put("repair_package_serial_number", repairPackageMap.get(rpid).get("serial_number"));
                map.put("repair_package_express_name", repairPackageMap.get(rpid).get("express_name"));
                map.put("repair_package_express_number", repairPackageMap.get(rpid).get("express_number"));
            }

            String rbpid = null == map.get("repair_back_package_id") ? ""
                    : String.valueOf(map.get("repair_back_package_id"));
            if (StringUtils.isNotEmpty(rbpid) && repairBackPackageMap.containsKey(rbpid)) {
                map.put("repair_back_pacakge_status", repairBackPackageMap.get(rbpid).get("status"));
                map.put("repair_back_pacakge_express_name", repairBackPackageMap.get(rbpid).get("express_name"));
                map.put("repair_back_pacakge_express_number", repairBackPackageMap.get(rbpid).get("express_number"));
                map.put("repair_back_pacakge_send_time", repairBackPackageMap.get(rbpid).get("delivery_time"));
            }

            Map<String, Object> exprotMap = new HashMap<>();
            exprotMap.putAll(map);

            if (repairDetectMaps.containsKey(rdid)) {
                exprotMap.put("repair_detect_invoices", repairDetectMaps.get(rdid));
            }
            else {
                System.err.println("[][][repairDetectMaps not contains:" + rdid + "]");
            }

            if (repairQuotationMaps.containsKey(rdid)) {
                exprotMap.put("repair_quotation_invoices", repairQuotationMaps.get(rdid));
            }
            else {
                System.err.println("[][][repairQuotationMaps not contains:" + rdid + "]");
            }

            exportMaps.add(exprotMap);
        }

        return exportMaps;
    }

    private final static String[] titles = new String[] { "维修包号", "条码", "型号", "出厂日期", "故障现象（客户）", "故障现象（检测）", "故障原因",
            "修复结果", "送修日期", "送修单号", "送修快递名称", "维修日期", "返回时期", "返回单号", "返回快递名称", "是否报价", "报价项目", "付款类别", "是否付费", "付款金额 ",
            "维修费金额", "维修站 ", "维修人 ", "责任方", "代理商名称", "终端客户名称" };

    @Autowired
    private IDirService dirService;

    private String export(List<Map<String, Object>> repairDeviceMaps) throws IOException {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("维修数据");

        HSSFCellStyle leftCenterCellStyle = wb.createCellStyle();
        leftCenterCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        leftCenterCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        HSSFCellStyle style = wb.createCellStyle(); // 样式对象
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平

        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = null;

        int columnSize = titles.length;
        for (int i = 0; i < columnSize; i++) {
            cell = row.createCell(i);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(titles[i]);
        }

        int rowIdx = 1;

        int rdsize = repairDeviceMaps.size();
        for (int i = 0; i < rdsize; i++) {

            Map<String, Object> rdMap = repairDeviceMaps.get(i);

            List<Map<String, Object>> detectMaps = (List<Map<String, Object>>) rdMap.get("repair_detect_invoices");
            List<Map<String, Object>> quotationMaps = (List<Map<String, Object>>) rdMap
                    .get("repair_quotation_invoices");

            if (CollectionUtils.isEmpty(detectMaps)) {
                detectMaps = new ArrayList<>();
                Map<String, Object> emptyMap = new HashMap<>();
                detectMaps.add(emptyMap);
            }

            int crow = 0;

            for (Map<String, Object> detectMap : detectMaps) {

                row = sheet.createRow(rowIdx);
                rowIdx++;

                crow = crow + 1;

                String detectId = String.valueOf(detectMap.get("id"));

                Map<String, Object> quotationMap = null;
                if (CollectionUtils.isNotEmpty(quotationMaps)) {
                    for (Map<String, Object> qMap : quotationMaps) {
                        String diid = String.valueOf(qMap.get("detect_invoice_id"));
                        if (diid.equals(detectId)) {
                            quotationMap = qMap;
                        }
                    }
                }

                cell = row.createCell(0);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("repair_package_serial_number"));

                cell = row.createCell(1);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("sn"));

                cell = row.createCell(2);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("model"));

                cell = row.createCell(3);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                Object mto = rdMap.get("manufacture_time");
                if (null == mto) {
                    cell.setCellValue("");
                }
                else {
                    cell.setCellValue(TimeUtil.formatByYYYYmmDDhhMMSS((Date) mto));
                }

                cell = row.createCell(4);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("fault_description"));

                // 故障现象检测
                cell = row.createCell(5);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(detectMap.containsKey("malfunction_appearance")
                        ? String.valueOf(detectMap.get("malfunction_appearance")) : "");

                // 故障原因
                cell = row.createCell(6);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(detectMap.containsKey("malfunction_reason")
                        ? String.valueOf(detectMap.get("malfunction_reason")) : "");

                // 修复结果
                cell = row.createCell(7);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(
                        detectMap.containsKey("repair_suggest") ? String.valueOf(detectMap.get("repair_suggest")) : "");

                cell = row.createCell(8);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                Object cto = rdMap.get("create_time");
                if (null == cto) {
                    cell.setCellValue("");
                }
                else {
                    cell.setCellValue(TimeUtil.formatByYYYYmmDDhhMMSS((Date) cto));
                }

                cell = row.createCell(9);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("repair_package_express_number"));

                cell = row.createCell(10);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("repair_package_express_name"));

                cell = row.createCell(11);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                Object dsto = rdMap.get("detect_start_time");
                if (dsto == null) {
                    cell.setCellValue("");
                }
                else {
                    cell.setCellValue(TimeUtil.formatByYYYYmmDDhhMMSS((Date) dsto));
                }

                // 返回日期
                cell = row.createCell(12);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                Object psto = rdMap.get("repair_back_pacakge_send_time");
                if (null == psto) {
                    cell.setCellValue("");
                }
                else {
                    cell.setCellValue(TimeUtil.formatByYYYYmmDDhhMMSS((Date) psto));
                }

                // 返回单号
                cell = row.createCell(13);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("repair_back_pacakge_express_number"));

                // 返回快递名称
                cell = row.createCell(14);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("repair_back_pacakge_express_name"));

                // 是否报价
                cell = row.createCell(15);
                cell.setCellType(Cell.CELL_TYPE_STRING);

                String quotedPrice = String.valueOf(detectMap.get("quoted_price"));
                cell.setCellValue("1".equals(quotedPrice) ? "是" : "否");

                // 报价项目
                cell = row.createCell(16);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String qitme = null != quotationMap ? String.valueOf(quotationMap.get("item")) : "";
                cell.setCellValue(qitme);

                // 付款类型
                cell = row.createCell(17);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String payType = String.valueOf(rdMap.get("pay_type"));
                cell.setCellValue(null == payType ? "" : ("1".equals(payType) ? "月结" : "现付"));

                String charge = String.valueOf(rdMap.get("charge"));
                // 是否付费
                cell = row.createCell(18);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String payStatus = String.valueOf(rdMap.get("pay_status"));
                cell.setCellValue("1".equals(charge) ? ("9".equals(payStatus) ? "已付款" : "未付款") : "");

                // 付款金额
                cell = row.createCell(19);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue("9".equals(payStatus) ? ((String) rdMap.get("cost_total")) : "");

                // 维修费金额
                cell = row.createCell(20);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue("1".equals(charge) ? ((String) rdMap.get("cost_total")) : "");

                // 维系站
                cell = row.createCell(21);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("repair_station_name"));

                // 维系人
                cell = row.createCell(22);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("repair_user_name"));

                // 责任方
                cell = row.createCell(23);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(detectMap.containsKey("responsible_party")
                        ? String.valueOf(detectMap.get("responsible_party")) : "");

                // 代理商名称
                cell = row.createCell(24);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("agent_name"));

                // 终端客户名称
                cell = row.createCell(25);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue((String) rdMap.get("end_customer_name"));

            }

            if (crow > 1) {
                int length = 4;
                for (int j = 0; j < length; j++) {
                    CellRangeAddress cra = new CellRangeAddress(rowIdx - crow - 1, rowIdx - 1, j, j);
                    sheet.addMergedRegion(cra);
                    int tRowIdx = rowIdx - crow - 1;
                    int tColIdx = j;

                    HSSFCell nowCell = sheet.getRow(tRowIdx).getCell(tColIdx);
                    nowCell.setCellStyle(leftCenterCellStyle);
                }

                int start = 8;
                length = start + 7;
                for (int j = start; j < length; j++) {
                    sheet.addMergedRegion(new CellRangeAddress(rowIdx - crow - 1, rowIdx - 1, j, j));
                    int tRowIdx = rowIdx - crow - 1;
                    int tColIdx = j;

                    HSSFCell nowCell = sheet.getRow(tRowIdx).getCell(tColIdx);
                    nowCell.setCellStyle(leftCenterCellStyle);
                }

                start = 17;
                length = start + 6;
                for (int j = start; j < length; j++) {
                    sheet.addMergedRegion(new CellRangeAddress(rowIdx - crow - 1, rowIdx - 1, j, j));
                    int tRowIdx = rowIdx - crow - 1;
                    int tColIdx = j;

                    HSSFCell nowCell = sheet.getRow(tRowIdx).getCell(tColIdx);
                    nowCell.setCellStyle(leftCenterCellStyle);
                }

                start = 24;
                length = 26;

                for (int j = start; j < length; j++) {
                    sheet.addMergedRegion(new CellRangeAddress(rowIdx - crow - 1, rowIdx - 1, j, j));
                    int tRowIdx = rowIdx - crow - 1;
                    int tColIdx = j;

                    HSSFCell nowCell = sheet.getRow(tRowIdx).getCell(tColIdx);
                    nowCell.setCellStyle(leftCenterCellStyle);
                }
            }

        }

        String dirPath = dirService.getRepairDeviceRecordDir();
        String path = dirPath + FileUtils.getRandomFileName() + ".xls";
        logger.info("[][][export file path:" + path + "]");
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOut = new FileOutputStream(path);
        wb.write(fileOut);
        fileOut.close();

        return path;
    }

}
