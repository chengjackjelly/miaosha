package com.miaosha.controller;


import com.miaosha.controller.viewobject.OrderVO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBussineseError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.OrderService;
import com.miaosha.service.model.OrderModel;
import com.miaosha.service.model.UserModel;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/createorder")
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId") Integer itemId,@RequestParam(name="amount") Integer amount,@RequestParam(name="promoId",required = false) Integer promoId) throws BusinessException {
        String token = httpServletRequest.getParameterMap().get("token")[0];
        if(StringUtils.isEmpty(token)){
            throw new BusinessException(EmBussineseError.USER_NOT_LOGIN,"未登录");
        }
        UserModel userModel=(UserModel) redisTemplate.opsForValue().get(token);
        if(userModel == null ){
            throw  new BusinessException(EmBussineseError.USER_NOT_LOGIN,"未登录");
        }

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
