package com.miaosha.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaosha.controller.viewobject.UserVO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBussineseError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.model.UserModel;
import com.miaosha.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@CrossOrigin(allowCredentials = "true", allowedHeaders ="*",originPatterns ="*")
@Controller("user")
@RequestMapping("/user")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        UserModel userModel=userService.getUserById(id);
        if(userModel==null){
            throw new BusinessException(EmBussineseError.USER_NOT_EXIST);
        }
        return CommonReturnType.create(convertFromModel(userModel));
    }

    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone") String telphone){

        Random random = new Random();
        int randomInt=random.nextInt(9999);
        randomInt+=10000;
        String optCode=String.valueOf(randomInt);
        System.out.println("手机号："+telphone);
        System.out.println("验证码:"+optCode);
        //当前用户的http请求
        httpServletRequest.getSession().setAttribute(telphone,optCode);
        return CommonReturnType.create(null);
    }


    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telphone") String telphone,@RequestParam(name="password") String password) throws BusinessException, NoSuchAlgorithmException {
        String encrptPassword=EncodeByMd5(password);
        UserModel userModel=userService.validateLogin(telphone,encrptPassword);

        //将登陆凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);



        return CommonReturnType.create(null);

    }
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone") String telphone,@RequestParam(name="name") String name,@RequestParam(name="gender") Integer gender,@RequestParam(name="age") Integer age,@RequestParam(name="password") String password,@RequestParam(name="optCode") String optCode) throws BusinessException, NoSuchAlgorithmException {


        //验证手机号和对应otpcode
        String inSessionOtpCode=(String)this.httpServletRequest.getSession().getAttribute(telphone);
        System.out.println("短信验证为"+inSessionOtpCode);
        System.out.println("实际接受为"+optCode);
        if(!StringUtils.equals(inSessionOtpCode,optCode)){
            throw new BusinessException(EmBussineseError.PARAMETER_VALIDATION_ERROR,"短信验证不符合");
        }

        UserModel userModel=new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(gender.byteValue());
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("ByPhone");
        String encrptPassword=EncodeByMd5(password);
        userModel.setEncrptPassword(encrptPassword);
        userService.register(userModel);

        return CommonReturnType.create(null);
    }
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5= MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder=new BASE64Encoder();

        String newstr=base64Encoder.encode(md5.digest(str.getBytes(StandardCharsets.UTF_8)));
        return newstr;
    }

    private UserVO convertFromModel(UserModel userModel) {
        if(userModel==null){
            return null;
        }
        UserVO userVO =new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }
}
