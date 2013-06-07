package cn.zhenghongen.springmvc.controller;

import cn.zhenghongen.springmvc.model.UserModel;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
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
public class RegisterCanCancelSimpleFormController extends SimpleFormController {
    public RegisterCanCancelSimpleFormController() {
        setCommandClass(UserModel.class);//设置命令对象实现类
        setCommandName("user"); //设置命令对象的名字
    }
    //form object 表单对象，提供展示表单时的表单数据(使用commandName放入请求)
    protected Object formBackingObject(HttpServletRequest request) throws Exception{
        UserModel user = new UserModel();
        user.setUsername("请输入用户名");
        return user;
    }

    //提供展示表单时需要的一些其他数据
    protected Map referenceData(HttpServletRequest request) throws Exception{
        Map map = new HashMap();
        map.put("cityList", Arrays.asList("北京","上海","深圳"));
        return map;
    }

    @Override
    protected void doSubmitAction(Object command) throws Exception {
        UserModel user = (UserModel)command;
        System.out.println(user);
    }


   /* protected ModelAndView onCancel(Object command) throws Exception{
        UserModel user = (UserModel)command;
        System.out.println(user);
        return  super.onCancel(command);
    }*/
}
