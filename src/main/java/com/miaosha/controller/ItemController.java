package com.miaosha.controller;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.miaosha.controller.viewobject.ItemVO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBussineseError;
import com.miaosha.response.CommonReturnType;
import com.miaosha.service.CacheService;
import com.miaosha.service.ItemService;
import com.miaosha.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@CrossOrigin(allowCredentials = "true", allowedHeaders ="*",originPatterns ="*")
@Controller("item")
@RequestMapping("/item")
public class ItemController extends  BaseController{

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name="title") String title,
                                       @RequestParam(name="price") BigDecimal price,
                                       @RequestParam(name="stock") Integer stock,
                                       @RequestParam(name="description") String description,
                                       @RequestParam(name="imgurl") String imgurl) throws BusinessException {

        ItemModel itemModel=new ItemModel();
        itemModel.setTitle(title);
        itemModel.setPrice(price);
        itemModel.setDescription(description);
        itemModel.setStock(stock);
        itemModel.setImgurl(imgurl);
        ItemModel itemModelReturn=itemService.createItem(itemModel);

        return CommonReturnType.create(convertFromModel(itemModelReturn));
    }

    @RequestMapping("/list")
    @ResponseBody
    public CommonReturnType listItem () {
        List<ItemModel> itemModelList=itemService.listItem();
        List<ItemVO> itemVOList=itemModelList.stream().map(itemModel ->
        {
            ItemVO itemVO=convertFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVOList);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name="id") Integer id) throws BusinessException {
        ItemModel itemModel= (ItemModel) cacheService.getFromCommonCache("item_"+id);

        if(itemModel==null){
            itemModel= (ItemModel) redisTemplate.opsForValue().get("item_"+id);
        }

        if(itemModel==null){
            itemModel=itemService.getItemById(id);
            redisTemplate.opsForValue().set("item_"+id,itemModel);
            redisTemplate.expire("item_"+id,10, TimeUnit.MINUTES);
        }
        if(itemModel==null){
            throw new BusinessException(EmBussineseError.ITEM_NOT_EXIST,"商品不存在");
        }

        return CommonReturnType.create(convertFromModel(itemModel));
    }




    private ItemVO convertFromModel(ItemModel itemModel){
        if(itemModel==null){
            return null;
        }
        ItemVO itemVO =new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        if(itemModel.getPromoModel()!=null){
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:MM:ss")));
            itemVO.setEndDate(itemModel.getPromoModel().getEndDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:MM:ss")));
        }
        else{
            itemVO.setPromoStatus(0);

        }
        return itemVO;
    }




}
