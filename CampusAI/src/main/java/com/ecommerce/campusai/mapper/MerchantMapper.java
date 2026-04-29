package com.ecommerce.campusai.mapper;

import com.ecommerce.campusai.entity.Merchant;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MerchantMapper {
    List<Merchant> findAll(@Param("status") Integer status);
    
    Merchant findById(@Param("id") Integer id);
    
    int addMerchant(Merchant merchant);
    
    int updateMerchant(Merchant merchant);
    
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status, @Param("rejectReason") String rejectReason);
    
    int deleteMerchant(@Param("id") Integer id);

    @Select("SELECT DISTINCT logo FROM merchant WHERE logo IS NOT NULL AND logo != ''")
    List<String> getAllLogoPaths();
}
