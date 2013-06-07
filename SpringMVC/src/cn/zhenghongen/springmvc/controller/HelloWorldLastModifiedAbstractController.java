package cn.zhenghongen.springmvc.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.LastModified;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * PROJECT: myCodes
 * File Description:
 * Author: ZhengHongEn
 * Revision History:
 * 2013/06/04             ZhengHongEn              Create
 */
public class HelloWorldLastModifiedAbstractController extends AbstractController implements LastModified {
    private long lastModified;
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.getWriter().write("hello world!Without Model and View");
        return  null;
    }
    public long getLastModified(HttpServletRequest request){
        if(lastModified == 0L){
            //TODO 此处更新条件：如果内容更新，应该重新返回内容修改的时间戳
            lastModified = System.currentTimeMillis();
        }
        return  lastModified;
    }

}
