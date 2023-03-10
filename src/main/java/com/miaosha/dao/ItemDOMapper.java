package com.miaosha.dao;

import com.miaosha.dataobject.ItemDO;

import java.util.List;

public interface ItemDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Mar 04 15:05:58 AWST 2023
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Mar 04 15:05:58 AWST 2023
     */
    int insert(ItemDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Mar 04 15:05:58 AWST 2023
     */
    int insertSelective(ItemDO record);
    int increaseSales(Integer id, Integer amount);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Mar 04 15:05:58 AWST 2023
     */
    ItemDO selectByPrimaryKey(Integer id);
    List<ItemDO> listItem();


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Mar 04 15:05:58 AWST 2023
     */
    int updateByPrimaryKeySelective(ItemDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat Mar 04 15:05:58 AWST 2023
     */
    int updateByPrimaryKey(ItemDO record);
}