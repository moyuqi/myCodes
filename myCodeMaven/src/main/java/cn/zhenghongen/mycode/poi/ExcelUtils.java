package cn.zhenghongen.mycode.poi;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ss.util.Region;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * PROJECT: myCodes
 * File Description:
 * Author: ZhengHongEn
 * Revision History:
 * 2014/09/15             ZhengHongEn              Create
 */
public class ExcelUtils {

    /**
     * 获取工作簿
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(File file) throws Exception {
        FileInputStream finput = null;
        Workbook workbook = null;
        try {
            String fileName = file.getName();

            finput = new FileInputStream(file);
            //获取工作簿
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(finput);
            } else {
                workbook = new HSSFWorkbook(finput);
            }
        } finally {
            if (finput != null) {
                finput.close();
            }
        }

        return workbook;
    }

    /**
     * 获取表单
     *
     * @param file       excel文件
     * @param sheetIndex sheet索引，默认从0开始
     * @return
     * @throws Exception
     */
    public static Sheet getSheet(File file, int sheetIndex) throws Exception {
        FileInputStream finput = null;
        Sheet sheet = null;
        try {
            //获取工作簿
            Workbook wb = ExcelUtils.getWorkbook(file);
            //获取表
            sheet = wb.getSheetAt(sheetIndex);
        } finally {
            if (finput != null) {
                finput.close();
            }
        }

        return sheet;
    }

    /**
     * 获取表单
     *
     * @param workbook   工作簿
     * @param sheetIndex sheet索引，默认从0开始
     * @return
     * @throws Exception
     */
    public static HSSFSheet getSheet(HSSFWorkbook workbook, int sheetIndex) throws Exception {
        HSSFSheet sheet = null;
        if (workbook != null) {
            //获取表
            sheet = workbook.getSheetAt(sheetIndex);
        }
        return sheet;
    }

    public static HSSFWorkbook createNewWorkBook(HSSFWorkbook sourceWb) {
        HSSFSheet sourceSheet = sourceWb.getSheetAt(0);
        HSSFWorkbook targetWb = new HSSFWorkbook();
        HSSFSheet targetSheet = targetWb.createSheet();
        HSSFRow sourceRow = null;
        HSSFRow targetRow = null;
        HSSFCell sourceCell = null;
        HSSFCell targetCell = null;
        Region region = null;
        int pEndRow = sourceSheet.getLastRowNum();
        // 拷贝合并的单元格
        for (int i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
            region = sourceSheet.getMergedRegionAt(i);
            if ((region.getRowFrom() >= 0) && (region.getRowTo() <= pEndRow)) {
                targetSheet.addMergedRegion(Region.convertToCellRangeAddress(region));
            }
        }
        // 拷贝行并填充数据
        for (int i = 0; i <= pEndRow; i++) {
            sourceRow = sourceSheet.getRow(i);
            if (sourceRow == null) {
                continue;
            }
            int targetRowIndex = i;
            targetRow = targetSheet.createRow(targetRowIndex);
            targetRow.setHeight(sourceRow.getHeight());
            for (int j = sourceRow.getFirstCellNum(); j <= sourceRow.getPhysicalNumberOfCells(); j++) {
                sourceCell = sourceRow.getCell(j);
                if (sourceCell == null) {
                    continue;
                }
                targetSheet.setColumnWidth((int) j, sourceSheet.getColumnWidth((int) j));
                targetSheet.setActive(sourceSheet.isActive());
                targetSheet.setColumnHidden(j, sourceSheet.isColumnHidden(j));
                targetCell = targetRow.createCell(j);
                int cType = sourceCell.getCellType();
                targetCell.setCellType(cType);
                if (sourceCell.getHyperlink() != null)
                    targetCell.setHyperlink(sourceCell.getHyperlink());
                if (sourceCell.getCellComment() != null)
                    targetCell.setCellComment(sourceCell.getCellComment());
                HSSFFont srcFont = sourceCell.getCellStyle().getFont(sourceWb);
                //targetCell.setCellStyle(this.copyCellStyle(targetWb, sourceCell.getCellStyle(), srcFont));
                targetCell.getCellStyle().cloneStyleFrom(sourceCell.getCellStyle());
                switch (cType) {
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        targetCell.setCellValue(sourceCell.getBooleanCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        String s = sourceCell.getCellFormula();
                        s = s.replaceAll(String.valueOf(i + 1), String.valueOf(targetRowIndex + 1));
                        targetCell.setCellFormula(s);
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        targetCell.setCellValue(sourceCell.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        targetCell.setCellValue(sourceCell.getRichStringCellValue());
                        break;
                }
            }
        }
        return targetWb;
    }

    /**
     * 导出excel
     *
     * @param name         sheet名称
     * @param filePath     文件存储路径， 如：f:/a.xls
     * @param headInfoList List<Map<String, Object>>
     *                     key: title         列标题
     *                     columnWidth   列宽
     *                     dataKey       列对应的 dataList item key
     * @param dataList     List<Map<String, Object>> 导出的数据
     * @throws java.io.IOException
     */
    public static void exportExcel(String name, String filePath,
                                   List<Map<String, Object>> headInfoList,
                                   List<Map<String, Object>> dataList) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet(name);

        HSSFCellStyle cs = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(font.BOLDWEIGHT_BOLD);
        cs.setFont(font);
        cs.setAlignment(cs.ALIGN_CENTER);

        HSSFRow r = sheet1.createRow(0);
        HSSFCell c = null;
        Map<String, Object> headInfo = null;
        //处理excel表头
        for (int i = 0, len = headInfoList.size(); i < len; i++) {
            headInfo = headInfoList.get(i);
            c = r.createCell(i);
            c.setCellValue(headInfo.get("title").toString());
            c.setCellStyle(cs);
            if (headInfo.containsKey("columnWidth")) {
                sheet1.setColumnWidth(i, (short) (((Integer) headInfo.get("columnWidth") * 8) / ((double) 1 / 20)));
            }
        }

        //处理数据
        Map<String, Object> dataItem = null;
        Object v = null;
        for (int rownum = 1, len = dataList.size(); rownum <= len; rownum++) {
            r = sheet1.createRow(rownum);
            r.setHeightInPoints(16);
            dataItem = dataList.get(rownum - 1);
            for (int j = 0, jlen = headInfoList.size(); j < jlen; j++) {
                headInfo = headInfoList.get(j);
                c = r.createCell(j);
                v = dataItem.get(headInfo.get("dataKey").toString());

                if (v instanceof String) {
                    c.setCellValue((String) v);
                } else if (v instanceof Boolean) {
                    c.setCellValue((Boolean) v);
                } else if (v instanceof Calendar) {
                    c.setCellValue((Calendar) v);
                } else if (v instanceof Double) {
                    c.setCellValue((Double) v);
                } else if (v instanceof Integer
                        || v instanceof Long
                        || v instanceof Short
                        || v instanceof Float) {
                    c.setCellValue(Double.parseDouble(v.toString()));
                } else if (v instanceof HSSFRichTextString) {
                    c.setCellValue((HSSFRichTextString) v);
                } else {
                    c.setCellValue(v.toString());
                }
            }
        }

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(filePath);
            wb.write(fileOut);
        } finally {
            if (fileOut != null) {
                fileOut.close();
            }
        }
    }

    /**
     * 根据key的值从工作簿中找到对应的工作单元，然后设值
     *
     * @param wb    工作簿
     * @param key   key
     * @param value 值
     */
    private static void setTemplateExcelData(HSSFWorkbook wb, String key, String value) {
        Sheet sheet1 = wb.getSheetAt(0);
        Name name = wb.getName(key);
        if (name != null) {
            name.getRefersToFormula();
            AreaReference[] areRef = AreaReference.generateContiguous(name.getRefersToFormula());
            for (int i = 0; i < areRef.length; i++) {
                CellReference cellR = areRef[i].getFirstCell();
                Row row = sheet1.getRow(cellR.getRow());
                Cell cell = row.getCell(cellR.getCol());
                cell.setCellValue(value);
                break;
            }
        }
    }

    /**
     * 设置excel模板的值，文件另存为filePath指定的地方
     *
     * @param templateFilePath 模板路径
     * @param filePath         另存为路径
     * @param data             数据
     * @throws java.io.IOException
     */
    public static void exportTemplateExcel(String templateFilePath, String filePath, Map data) throws IOException {
        File template = new File(templateFilePath);
        FileInputStream templateIs = null;
        try {
            templateIs = new FileInputStream(template);
            HSSFWorkbook wb = new HSSFWorkbook(templateIs);
            if (data != null && data.keySet() != null) {
                Iterator itr = data.keySet().iterator();
                String key = null;
                while (itr.hasNext()) {
                    key = (String) itr.next();
                    setTemplateExcelData(wb, key, (String) data.get(key));
                }
            }
            FileOutputStream fileOut = null;
            try {
                fileOut = new FileOutputStream(filePath);
                wb.write(fileOut);
            } finally {
                if (fileOut != null) {
                    fileOut.close();
                }
            }
        } finally {
            if (templateIs != null) {
                templateIs.close();
            }
        }

    }

    /**
     * HTML转Word
     *
     * @param html     html内容
     * @param filePath 文件路径
     * @throws java.io.IOException
     */
    public static void exportWordFromHtml(String html, String filePath) throws IOException {
        FileOutputStream fos = null;
        ByteArrayInputStream bis = null;
        File tempWord = new File(filePath);
        try {
            if (html != null) {
                bis = new ByteArrayInputStream(html.getBytes());
                fos = new FileOutputStream(tempWord);
                POIFSFileSystem fs = new POIFSFileSystem();
                DirectoryEntry directoryEn = fs.getRoot();
                directoryEn.createDocument("WordDocument", bis);
                fs.writeFilesystem(fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
//		List<Map<String, Object>> headInfoList = new ArrayList<Map<String,Object>>();
//		Map<String, Object> itemMap = new HashMap<String, Object>();
//		itemMap.put("title", "序号1");
//		itemMap.put("columnWidth", 25);
//		itemMap.put("dataKey", "XH1");
//		headInfoList.add(itemMap);
//
//		itemMap = new HashMap<String, Object>();
//		itemMap.put("title", "序号2");
//		itemMap.put("columnWidth", 50);
//		itemMap.put("dataKey", "XH2");
//		headInfoList.add(itemMap);
//
//		itemMap = new HashMap<String, Object>();
//		itemMap.put("title", "序号3");
//		itemMap.put("columnWidth", 25);
//		itemMap.put("dataKey", "XH3");
//		headInfoList.add(itemMap);
//
//		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
//		Map<String, Object> dataItem = null;
//		for(int i=0; i < 100; i++){
//			dataItem = new HashMap<String, Object>();
//			dataItem.put("XH1", "data" + i);
//			dataItem.put("XH2", 88888888f);
//			dataItem.put("XH3", "脉兜V5..");
//			dataList.add(dataItem);
//		}
//
//
//		POIUtil.exportExcel("test sheet 1","F:\\temp\\customer.xls", headInfoList, dataList);

//        Map data  = new HashMap();
//        data.put("jgmc", "测试机构名称");
//        data.put("hybh", "测试会员编码");
//        data.put("jgmcbgq", "测试机构名称1测试机构名称1测试机构名称1测试机构名称1测试机构名称1测试机构名称1");
//        data.put("jgmcbgh", "测试机构名称2");
//        String templateFilePath = "F:\\Workspace\\idea\\OTC\\ROOT\\web\\WEB-INF\\exportXLSTemplate\\bgsq.xls";
//        String filePath = "F:\\Workspace\\idea\\OTC\\ROOT\\web\\WEB-INF\\exportXLSTemplate\\bgsqdata.xls";
//        exportTemplateExcel(templateFilePath, filePath, data);

        File fileDir = new File("F:/");
        if (fileDir.exists()) {
            // 生成临时文件名称
            String fileName = "a.doc";
            // 正确
            String content = "<html><div style=\"text-align: center\"><span style=\"font-size: 28px\"><span style=\"font-family: 黑体\">" +
                    "制度发布通知<br /> <br /> </span></span></div></html>";
            // 错误
//            String content = "<div style=\"text-align: center\"><span style=\"font-size: 28px\"><span style=\"font-family: 黑体\">" +
//                    "制度发布通知<br /> <br /> </span></span></div>";
            exportWordFromHtml(content, "F:/" + fileName);
        }
    }
}
