package com.miaosha.service.impl;
import com.miaosha.dao.UserDOMapper;
import com.miaosha.dao.UserPasswordDOMapper;
import com.miaosha.dataobject.UserDO;
import com.miaosha.dataobject.UserPasswordDO;
import com.miaosha.error.BusinessException;
import com.miaosha.error.EmBussineseError;
import com.miaosha.service.model.ItemModel;
import com.miaosha.service.model.UserModel;
import com.miaosha.service.UserService;
import com.miaosha.validator.ValidationResult;
import com.miaosha.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserModel getUserById(Integer id) {

        UserDO userDO=userDOMapper.selectByPrimaryKey(id);
        if(userDO==null){
            return null;
        }
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(id);
        if(userPasswordDO==null){
            return null;
        }
        return convertFromDataObject(userDO,userPasswordDO);
    }

    @Override
    public UserModel getUserByIdInCache(Integer id){
        UserModel userModel =(UserModel) redisTemplate.opsForValue().get("user_validate_"+id);
        if(userModel == null){
            userModel=this.getUserById(id);
            redisTemplate.opsForValue().set("user_validate_"+id,userModel);
            redisTemplate.expire("user_validate_"+id,10, TimeUnit.MINUTES);
        }
        return userModel;

    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBussineseError.PARAMETER_VALIDATION_ERROR);
        }
        //判断各属性是否为空 用StringUtils.isEmpty
        if(StringUtils.isEmpty(userModel.getName())|| StringUtils.isEmpty(userModel.getTelphone())||
        StringUtils.isEmpty(userModel.getEncrptPassword())|| userModel.getAge()==null || userModel.getGender()==null){
            throw new BusinessException(EmBussineseError.PARAMETER_VALIDATION_ERROR);
        }
        ValidationResult result=validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBussineseError.USER_LOGIN_ERROR,result.getErrMsg());
        }


        UserDO userDO =convertFromModel(userModel);
        try{
            userDOMapper.insertSelective(userDO);
        }
        catch (DuplicateKeyException ex){
            throw new BusinessException(EmBussineseError.PARAMETER_VALIDATION_ERROR,"手机号已被注册");
        }

        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO=convertFromPasswordModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);


    }

    @Override
    public UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException {
        UserDO userDO=userDOMapper.selectByTelphone(telphone);
        if(userDO == null) {
            throw new BusinessException(EmBussineseError.USER_LOGIN_ERROR);
        }
        UserPasswordDO userPasswordDO=userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel= convertFromDataObject(userDO,userPasswordDO);
        if(!StringUtils.equals(userModel.getEncrptPassword(),encrptPassword)){
            throw new BusinessException(EmBussineseError.USER_LOGIN_ERROR);
        }
        return userModel;
    }


    private UserPasswordDO convertFromPasswordModel(UserModel userModel){
        UserPasswordDO userPasswordDO=new UserPasswordDO();
        userPasswordDO.setUserId(userModel.getId());
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        return userPasswordDO;
    }
    private UserDO convertFromModel(UserModel userModel){
        UserDO userDO=new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        UserModel userModel=new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        return userModel;

    }
}
