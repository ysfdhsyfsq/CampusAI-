package com.ecommerce.campusai.service;

import com.ecommerce.campusai.entity.Merchant;
import com.ecommerce.campusai.mapper.MerchantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MerchantService {
    
    @Autowired
    private MerchantMapper merchantMapper;
    
    public List<Merchant> findAll(Integer status) {
        return merchantMapper.findAll(status);
    }
    
    public Merchant findById(Integer id) {
        return merchantMapper.findById(id);
    }
    
    public int addMerchant(Merchant merchant) {
        if (merchant.getStatus() == null) {
            merchant.setStatus(0);
        }
        return merchantMapper.addMerchant(merchant);
    }
    
    public int updateMerchant(Merchant merchant) {
        return merchantMapper.updateMerchant(merchant);
    }
    
    public int updateStatus(Integer id, Integer status, String rejectReason) {
        return merchantMapper.updateStatus(id, status, rejectReason);
    }
    
    public int deleteMerchant(Integer id) {
        return merchantMapper.deleteMerchant(id);
    }
}
