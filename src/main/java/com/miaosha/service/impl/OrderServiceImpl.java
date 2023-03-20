package com.miaosha.service.impl;

import com.miaosha.dao.*;
import com.miaosha.dataobject.ItemDO;
import com.miaosha.dataobject.ItemStockDO;
import com.miaosha.dataobject.OrderDO;
import com.miaosha.dataobject.SequenceInfoDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBussineseError;
import com.miaosha.service.ItemService;
import com.miaosha.service.OrderService;
import com.miaosha.service.UserService;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.OrderModel;
import com.miaosha.service.model.UserModel;
import org.omg.PortableServer.THREAD_POLICY_ID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceInfoDOMapper sequenceInfoDOMapper;

    @Autowired
    private PromoInfoDOMapper promoInfoDOMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public OrderModel createOrder(Integer itemId,Integer userId,Integer amount,Integer promoId) throws BusinessException {
        //validate
//        ItemModel itemModel = itemService.getItemById(itemId); //7章前的数据库查询实现方式
        //7章交易优化，用redis cache
        ItemModel itemModel = itemService.getItemByIdInCache(itemId);

//        if(itemDOMapper.selectByPrimaryKey(itemId)==null
//            || userDOMapper.selectByPrimaryKey(userId)==null){
//            throw new BusinessException(EmBussineseError.PARAMETER_VALIDATION_ERROR,"用户和商品ID不能为空");
//        }
        if(itemModel == null){
            throw new BusinessException(EmBussineseError.PARAMETER_VALIDATION_ERROR,"商品不存在");
        }

        UserModel userModel = userService.getUserByIdInCache(userId);

        if(userModel==null){
            throw new BusinessException(EmBussineseError.PARAMETER_VALIDATION_ERROR,"用户不存在");
        }

        if(amount<=0 || amount>=999){
            throw new BusinessException(EmBussineseError.PARAMETER_VALIDATION_ERROR,"交易数量不在规定范围[0,999]");
        }
        if(promoId!=null){
            if(!promoInfoDOMapper.selectByItemId(itemId).getId().equals(promoId)){
                throw new BusinessException(EmBussineseError.PARAMETER_VALIDATION_ERROR,"promoId 有误");
            }
            else if(itemModel.getPromoModel().getStatus()!=2){
                throw new BusinessException(EmBussineseError.PARAMETER_VALIDATION_ERROR);
            }
        }
        //stock decrease
        //mysql数据库默认用的innoDB索引，innoDB会在update，delete，insert时自动加X锁
        //this method assured its ATOM  by database.
        //the decreasing calculation is down by mysql which improve the performance of system.
        if(!decreaseStock(itemId,amount)){
            throw new BusinessException(EmBussineseError.ITEM_STOCK_LIMIT);
        }

        //order create
        OrderModel orderModel=new OrderModel();

        BigDecimal price=itemModel.getPrice();




        if(promoId!=null){
            price=promoInfoDOMapper.selectByPrimaryKey(promoId).getPromoItemPrice();
        }


        orderModel.setItemId(itemId);
        orderModel.setUserId(userId);
        orderModel.setAmount(amount);
        orderModel.setId(generateOrderNo());
        orderModel.setItemPrice(price);
        orderModel.setOrderPrice(price.multiply(BigDecimal.valueOf(amount)));

        OrderDO orderDO=convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);
        itemDOMapper.increaseSales(itemId,amount);
        return orderModel;




    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId,Integer amount) {
        int afftectedRow= itemStockDOMapper.decreaseStock(itemId,amount);
        return afftectedRow != 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo(){
        StringBuilder stringBuilder=new StringBuilder();
        //0-8 is time related yyyymmdd
        LocalDateTime now= LocalDateTime.now();
        String nowDate=now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);

        //8-14 is autoincrement sequence
        SequenceInfoDO sequenceInfoDO = sequenceInfoDOMapper.getSequenceByName("order_info");
        String sequenceStr=String.valueOf(sequenceInfoDO.getCurrentValue());
        sequenceInfoDO.setCurrentValue(sequenceInfoDO.getCurrentValue()+sequenceInfoDO.getStep());
        sequenceInfoDOMapper.updateByPrimaryKeySelective(sequenceInfoDO);

        for(int i=0;i<6-sequenceStr.length();i++){
            stringBuilder.append('0');
        }
        stringBuilder.append(sequenceInfoDO.getCurrentValue().toString());



        //14-16 is for Partition databases and tables
        stringBuilder.append("00");
        return stringBuilder.toString();

    }
    private OrderDO convertFromOrderModel(OrderModel orderModel){
        OrderDO orderDO=new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        return orderDO;
    }

}
