<%--
    Title:
    Description:
    Parameter:1、
    Procedure:1、
    Author: ZhengHongEn
    Revision History:
    2013/03/24             ZhengHongEn             Create
--%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.io.File" %>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
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

    //out.flush();
    //out.close();
%>