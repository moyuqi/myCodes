<%--
    Title:  一个页面就只有一个文件上传控件
    Description:
    Parameter:1、
    Procedure:1、
    Author: ZhengHongEn
    Revision History:
    2013/03/23             ZhengHongEn             Create
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>单个SWFupload控件</title>
    <script type="text/javascript" src="<%=basePath%>_jslib/jQuery/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>_jslib/swfupload_2.2.0/swfupload.js"></script>
    <script type="text/javascript" src="<%=basePath%>_jslib/swfupload_2.2.0/handlers.js"></script>
</head>
<body>
<!--附件上传-->
<span id="spanButtonPlaceholder"></span>

<div id="divFileProgressContainer" style="width:200;display:none;"></div>
<div id="divSwfuploadInfoContainer">
    <table id="swfuploadInfoTable" border="0" width="50%"
           style="border: solid 1px #7FAAFF; background-color: #C5D9FF; padding: 2px;margin-top:8px;">
    </table>
</div>
<script type="text/javascript">
    var swfu;
    window.onload = function () {
        swfu = new SWFUpload({
            //upload_url: "<%=basePath%>UploadServlet?EVENT_SOURCE=UploadToTemp",
            upload_url: "<%=basePath%>swfupload/uploadService.jsp?EVENT_SOURCE=UploadToTemp",

            // File Upload Settings
            file_size_limit: "50 MB",	// 1000MB
            file_types: "*.*",//设置可上传的类型
            file_types_description: "所有文件",
            file_upload_limit: "10",

            file_queue_error_handler: fileQueueError,//选择文件后出错
            file_dialog_complete_handler: fileDialogComplete,//选择好文件后提交
            file_queued_handler: fileQueued,
            upload_progress_handler: uploadProgress,
            upload_error_handler: uploadError,
            upload_success_handler: uploadSuccess,
            upload_complete_handler: uploadComplete,

            // Button Settings
            button_image_url: "<%=basePath%>_jslib/swfupload_2.2.0/images/SmallSpyGlassWithTransperancy_17x18.png",
            button_placeholder_id: "spanButtonPlaceholder",
            button_width: 100,
            button_height: 18,
            button_text: '<span class="button">添加文件</span>',
            button_text_style: '.button { font-family: Helvetica, Arial, sans-serif; font-size: 12pt; } .buttonSmall { font-size: 10pt; }',
            button_text_top_padding: 0,
            button_text_left_padding: 18,
            button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
            button_cursor: SWFUpload.CURSOR.HAND,

            // Flash Settings
            flash_url: "<%=basePath%>_jslib/swfupload_2.2.0/swfupload.swf",

            custom_settings: {
                upload_target: "divFileProgressContainer"
            },
            // Debug Settings
            debug: false  //是否显示调试窗口
        });
    };
    function startUploadFile() {
        swfu.startUpload();
    }
</script>
</body>
</html>