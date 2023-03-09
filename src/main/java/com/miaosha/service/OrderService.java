package com.miaosha.service;

import com.miaosha.error.BusinessException;
import com.miaosha.service.model.OrderModel;

public interface OrderService {
    OrderModel createOrder(Integer itemId,Integer userId,Integer amount,Integer promoId) throws BusinessException;
    boolean decreaseStock(Integer itemId,Integer amount);
    public String generateOrderNo();
}
