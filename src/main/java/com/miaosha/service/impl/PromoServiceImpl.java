package com.miaosha.service.impl;

import com.miaosha.dao.PromoInfoDOMapper;
import com.miaosha.dataobject.PromoInfoDO;
import com.miaosha.service.PromoService;
import com.miaosha.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoInfoDOMapper promoInfoDOMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        PromoInfoDO promoInfoDO=promoInfoDOMapper.selectByItemId(itemId);
        PromoModel promoModel=convertFromDataObject(promoInfoDO);
        if(promoModel==null){
            return null;
        }
        DateTime startDate=promoModel.getStartDate();
        DateTime endDate=promoModel.getEndDate();
        if(startDate.isAfterNow()){
            promoModel.setStatus(1);
        }
        if(startDate.isBeforeNow() && endDate.isAfterNow()){
            promoModel.setStatus(2);
        }
        if(endDate.isBeforeNow()){
            promoModel.setStatus(3);
        }
        return promoModel;
    }

    private PromoModel convertFromDataObject(PromoInfoDO promoInfoDO){
        if(promoInfoDO == null){
            return null;
        }
        PromoModel promoModel=new PromoModel();
        BeanUtils.copyProperties(promoInfoDO,promoModel);
        promoModel.setStartDate(new DateTime(promoInfoDO.getStartDate()));
        promoModel.setEndDate(new DateTime(promoInfoDO.getEndDate()));
        return promoModel;
    }
}
