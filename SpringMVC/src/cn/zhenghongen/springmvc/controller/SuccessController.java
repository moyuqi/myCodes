package cn.zhenghongen.springmvc.controller;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

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
public class SuccessController  extends AbstractController {
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.getWriter().write("success!");
        return  null;
    }
}