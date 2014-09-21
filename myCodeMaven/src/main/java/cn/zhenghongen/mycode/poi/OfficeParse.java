package cn.zhenghongen.mycode.poi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.Exception;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.System;
import java.util.List;


import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.PictureData;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;

/**
 * 使用poi 解析office文档
 *
 * @author longhuiping
 */
public class OfficeParse {

    public static int count = 1;

    /**
     * 解析word文档
     *
     * @param docPath     .doc文档路径
     * @param imgSavePath 图片的保存地址
     * @throws Exception
     */
    public static void docParse(String docPath, String imgSavePath) throws Exception {
        InputStream input = null;
        File docFile = new File(docPath);
        HWPFDocument document = null;
        try {
            //加载 doc 文档
            input = new FileInputStream(docFile);
            document = new HWPFDocument(input);

            DocumentSummaryInformation docInfo = document.getDocumentSummaryInformation();
            SummaryInformation sumInfo = document.getSummaryInformation();
            showInfo(sumInfo, docInfo);

            //内容
            Range range = document.getRange();
            String content = range.text();
            System.out.println("内容:" + content);

            //获取所有的图片信息
            List pics = document.getPicturesTable().getAllPictures();
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = (Picture) pics.get(i);
                if (null != pic) {
                    FileOutputStream output = new FileOutputStream(new File(imgSavePath + count + "." + pic.suggestFileExtension()));
                    pic.writeImageContent(output);
                    output.close();
                    count++;
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (null != input)
                input.close();
        }
    }

    /**
     * 解析ppt文档
     *
     * @param pptPath     文档路径
     * @param imgSavePath 图片保存路径
     * @throws Exception
     */
    public static void pptParse(String pptPath, String imgSavePath) throws Exception {
        InputStream input = null;
        HSLFSlideShow document = null;
        try {

            //加载ppt文档
            input = new FileInputStream(pptPath);
            document = new HSLFSlideShow(input);

            /** 获取ppt属性信息 **/
            DocumentSummaryInformation docInfo = document.getDocumentSummaryInformation();
            SummaryInformation sumInfo = document.getSummaryInformation();
            showInfo(sumInfo, docInfo);

            /** 获取ppt内容 **/
            StringBuilder pptContent = new StringBuilder();
            SlideShow slideShow = new SlideShow(document);
            Slide[] slides = slideShow.getSlides();
            int slideLength = slides.length;
            for (int i = 0; i < slideLength; i++) {
                //获取每张ppt页面的标题
                Slide slide = slides[i];
                pptContent.append(slide.getTitle());
                //获取每张ppt页面的内容
                TextRun[] trs = slide.getTextRuns();
                if (null != trs && 0 != trs.length) {
                    int trsLength = trs.length;
                    for (int j = 0; j < trsLength; j++) {
                        TextRun tr = trs[j];
                        pptContent.append(tr.getText());
                    }
                }
            }
            System.out.println("内容:" + pptContent.toString());

            /** 获取 ppt中的图片 **/
            PictureData[] picDatas = slideShow.getPictureData();
            int picDatasLength = picDatas.length;
            for (int i = 0; i < picDatasLength; i++) {
                PictureData picData = picDatas[i];
                byte[] bytes = picData.getData();
                FileOutputStream output = new FileOutputStream(imgSavePath + count + "." + getPictureSuffix(picData));
                BufferedOutputStream writer = new BufferedOutputStream(output);
                writer.write(bytes);
                writer.flush();
                writer.close();
                output.close();
                count++;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != input)
                input.close();

        }
    }

    /**
     * 解析excel文档
     *
     * @param xlsPath     文档路径
     * @param imgSavePath 图片保存路径
     * @throws Exception
     */
    public static void xlsParse(String xlsPath, String imgSavePath) throws Exception {
        InputStream input = null;
        HSSFWorkbook workbook = null;
        try {
            //加载文档
            input = new FileInputStream(xlsPath);
            workbook = new HSSFWorkbook(input);
            /** 获取文档属性 **/
            DocumentSummaryInformation docInfo = workbook.getDocumentSummaryInformation();
            SummaryInformation sumInfo = workbook.getSummaryInformation();
            showInfo(sumInfo, docInfo);

            /** 获取文档内容 因为excel采用的是单元格格式 所以采用循环取单元格的值**/
            StringBuilder xlsContent = new StringBuilder();
            //获取工作表数量
            int sheetTotal = workbook.getNumberOfSheets();
            //获取工作表信息
            for (int i = 0; i < sheetTotal; i++) {

                HSSFSheet sheet = workbook.getSheetAt(i);
                if (null == sheet)
                    continue;
                int rowTotal = sheet.getLastRowNum();
                //获取 行信息
                for (int j = 0; j < rowTotal; j++) {
                    HSSFRow row = sheet.getRow(j);
                    if (null == row)
                        continue;
                    int cellTotal = row.getLastCellNum();
                    //获取单元格信息
                    for (int k = 0; k < cellTotal; k++) {
                        HSSFCell cell = row.getCell(k);
                        if (null == cell)
                            continue;
                        xlsContent.append(cell.toString());
                    }
                }
            }
            System.out.println("内容：" + xlsContent.toString());

            /** 获取图片信息 **/
            List<HSSFPictureData> picDatas = workbook.getAllPictures();
            int picDatasSize = picDatas.size();
            for (int i = 0; i < picDatasSize; i++) {
                HSSFPictureData picData = picDatas.get(i);
                if (null == picData) continue;
                byte[] bytes = picData.getData();
                FileOutputStream output = new FileOutputStream(imgSavePath + count + "." + picData.suggestFileExtension());
                BufferedOutputStream writer = new BufferedOutputStream(output);
                writer.write(bytes);
                writer.flush();
                writer.close();
                output.close();
                count++;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != input)
                input.close();

        }
    }

    public static void showInfo(SummaryInformation sumInfo, DocumentSummaryInformation docInfo) throws Exception {
        /** 摘要信息 **/
        System.out.println("标题:" + sumInfo.getTitle());
        System.out.println("主题:" + sumInfo.getSubject());
        System.out.println("作者:" + sumInfo.getAuthor());
        System.out.println("关键字:" + sumInfo.getKeywords());

        System.out.println("备注:" + sumInfo.getComments());
        System.out.println("模板:" + sumInfo.getTemplate());
        System.out.println("上次保存用户:" + sumInfo.getLastAuthor());
        System.out.println("修订次数:" + sumInfo.getRevNumber());

        System.out.println("编辑文档的时间:" + sumInfo.getEditTime());
        System.out.println("打印时间:" + sumInfo.getLastPrinted());
        System.out.println("创建时间:" + sumInfo.getCreateDateTime());
        System.out.println("上一次保存时间:" + sumInfo.getLastSaveDateTime());

        System.out.println("页面数量:" + sumInfo.getPageCount());
        System.out.println("字数:" + sumInfo.getWordCount());
        System.out.println("字符数:" + sumInfo.getCharCount());
        System.out.println("应用软件名称:" + sumInfo.getApplicationName());

        /** 文档信息 部分属性属于个别office文档类型特有的属性 **/
        System.out.println("类别:" + docInfo.getCategory());
        System.out.println("显示的格式:" + docInfo.getPresentationFormat());
        System.out.println("字节数:" + docInfo.getByteCount());
        System.out.println("行数:" + docInfo.getLineCount());


        System.out.println("段落数:" + docInfo.getParCount());
        System.out.println("幻灯片的数量:" + docInfo.getSlideCount());
        System.out.println("备注数量:" + docInfo.getNoteCount());
        System.out.println("隐藏文件的数量:" + docInfo.getHiddenCount());

        System.out.println("多媒体剪辑数量:" + docInfo.getMMClipCount());
        System.out.println("经理:" + docInfo.getManager());
        System.out.println("单位:" + docInfo.getCompany());
        System.out.println("链接:" + docInfo.getLineCount());
    }

    /**
     * 获取ppt文档中的图片格式
     *
     * @param pictureData
     * @return
     * @throws Exception
     */
    public static String getPictureSuffix(PictureData pictureData) throws Exception {
        String suffix = "";
        int tp = pictureData.getType();
        switch (tp) {
            case org.apache.poi.hslf.model.Picture.DIB:
                suffix = "dib";
                break;
            case org.apache.poi.hslf.model.Picture.EMF:
                suffix = "emf";
                break;
            case org.apache.poi.hslf.model.Picture.JPEG:
                suffix = "jpeg";
                break;
            case org.apache.poi.hslf.model.Picture.PICT:
                suffix = "pict";
                break;
            case org.apache.poi.hslf.model.Picture.PNG:
                suffix = "png";
                break;
            case org.apache.poi.hslf.model.Picture.WMF:
                suffix = "wmf";
                break;

        }
        return suffix;
    }

    public static void main(String[] args) throws Exception {
        String imgSavePath = "f:/pdfimg/";
        docParse("f:/1.doc", imgSavePath);
        pptParse("f:/ppt/1.ppt", imgSavePath);
        xlsParse("f:/xls/1.xls", imgSavePath);
    }
}
