package cn.zhenghongen.springmvc.controller;

import cn.zhenghongen.springmvc.model.UserModel;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * PROJECT: myCodes
 * File Description:
 * Author: ZhengHongEn
 * Revision History:
 * 2013/06/04             ZhengHongEn              Create
 */
public class InfoFillWizardFormController  extends AbstractWizardFormController{
    public InfoFillWizardFormController() {
        setCommandClass(UserModel.class);
        setCommandName("user");
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        return super.referenceData(request, command, errors, page);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected Map referenceData(HttpServletRequest request, int page) throws Exception {
        Map map = new HashMap();
        if (page == 1) { //如果是填写学校信息页， 需要学校类型信息
            map.put("schoolTypeList", Arrays.asList("大学", "中专", "高中"));
        } else if (page == 2) {  //如果填写工作信息也，需要返回城市信息
            map.put("cityList", Arrays.asList("北京", "上海", "深圳"));
        }

        return map;
        //return super.referenceData(request, page);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void validatePage(Object command, Errors errors, int page, boolean finish) {
        super.validatePage(command, errors, page, finish);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void validatePage(Object command, Errors errors, int page) {
        super.validatePage(command, errors, page);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void postProcessPage(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        //提供给每一页完成时的后处理方式
        super.postProcessPage(request, command, errors, page);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        System.out.println(command);
        return new ModelAndView("redirect:/cancel");
        //return super.processCancel(request, response, command, errors);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        System.out.println(command);
        return new ModelAndView("redirect:/success");
        //return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
