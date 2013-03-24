package cn.zhe.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Created with IntelliJ IDEA.
 * PROJECT: myCodes
 * File Description:
 * Author: ZhengHongEn
 * Revision History:
 * 2013/03/23             ZhengHongEn              Create
 */
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String EVENT_SOURCE = request.getParameter("EVENT_SOURCE");
        Map result = new HashMap();
        try{
            if(EVENT_SOURCE.equalsIgnoreCase("UploadToTemp")){
                long token = System.currentTimeMillis();
                String tempPath = System.getProperty("java.io.tmpdir") + "/" + token;
                File baseDir = new File(tempPath);
                if (!baseDir.exists()) {
                    baseDir.mkdir();
                }
                Map data = new HashMap();
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload fileUpload = new ServletFileUpload(factory);
                fileUpload.setHeaderEncoding("UTF-8");
                List items = fileUpload.parseRequest(request);
                for (int i = 0; i < items.size(); i++) {
                    FileItem item = (FileItem) items.get(i);
                    if (item == null) continue;
                    if (!item.isFormField() && item.getSize() > 0) {
                        data.put("id", i);
                        item.write(new File(tempPath + "/" + item.getFieldName()));
                    }
                }
                result.put("success", true);
                result.put("data", data);
            }
        }catch (Exception e){
            result.put("success", "-1");
            result.put("note", e.getMessage());
        }

        //JSONObject outStr = JSONObject.fromObject(result);
        //out.print(outStr.toString());

        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
