package com.miaosha.dao;

import com.miaosha.dataobject.PromoInfoDO;

public interface PromoInfoDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_info
     *
     * @mbg.generated Tue Mar 07 14:57:07 AWST 2023
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_info
     *
     * @mbg.generated Tue Mar 07 14:57:07 AWST 2023
     */
    int insert(PromoInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_info
     *
     * @mbg.generated Tue Mar 07 14:57:07 AWST 2023
     */
    int insertSelective(PromoInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_info
     *
     * @mbg.generated Tue Mar 07 14:57:07 AWST 2023
     */
    PromoInfoDO selectByPrimaryKey(Integer id);
    PromoInfoDO selectByItemId(Integer itemId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_info
     *
     * @mbg.generated Tue Mar 07 14:57:07 AWST 2023
     */
    int updateByPrimaryKeySelective(PromoInfoDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table promo_info
     *
     * @mbg.generated Tue Mar 07 14:57:07 AWST 2023
     */
    int updateByPrimaryKey(PromoInfoDO record);
}