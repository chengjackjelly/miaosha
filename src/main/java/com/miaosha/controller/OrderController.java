package com.miaosha.controller;


import com.miaosha.controller.viewobject.OrderVO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBussineseError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.OrderService;
import com.miaosha.service.model.OrderModel;
import com.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(allowCredentials = "true", allowedHeaders ="*",originPatterns ="*")
@Controller("order")
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("/createorder")
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId") Integer itemId,@RequestParam(name="promoId") Integer promoId,@RequestParam(name="amount") Integer amount) throws BusinessException {
        Boolean isLogin=(Boolean)httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin==null){
            System.out.println("here");
            throw new BusinessException(EmBussineseError.USER_NOT_EXIST);
//            throw new BusinessException(EmBussineseError.USER_NOT_LOGIN,"test");
        }
        UserModel userModel= (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel orderModel=orderService.createOrder(itemId,userModel.getId(),amount,promoId);

        return CommonReturnType.create(null);
    }

    private OrderVO convertFromModel(OrderModel orderModel){
        if(orderModel==null){
            return null;
        }
        OrderVO orderVO=new OrderVO();
        BeanUtils.copyProperties(orderModel,orderVO);
        return orderVO;
    }

}
