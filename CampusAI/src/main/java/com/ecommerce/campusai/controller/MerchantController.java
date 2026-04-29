package com.ecommerce.campusai.controller;

import com.ecommerce.campusai.entity.Merchant;
import com.ecommerce.campusai.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/merchant")
public class MerchantController {
    
    @Autowired
    private MerchantService merchantService;
    
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) Integer status) {
        Map<String, Object> result = new HashMap<>();
        List<Merchant> merchants = merchantService.findAll(status);
        result.put("code", 200);
        result.put("data", merchants);
        result.put("msg", "查询成功");
        return result;
    }
    
    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        Merchant merchant = merchantService.findById(id);
        if (merchant != null) {
            result.put("code", 200);
            result.put("data", merchant);
            result.put("msg", "查询成功");
        } else {
            result.put("code", 404);
            result.put("msg", "商家不存在");
        }
        return result;
    }
    
    @PostMapping("/apply")
    public Map<String, Object> apply(@RequestBody Merchant merchant) {
        Map<String, Object> result = new HashMap<>();
        int rows = merchantService.addMerchant(merchant);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "申请提交成功，等待审核");
        } else {
            result.put("code", 500);
            result.put("msg", "申请提交失败");
        }
        return result;
    }
    
    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody Merchant merchant) {
        Map<String, Object> result = new HashMap<>();
        int rows = merchantService.updateMerchant(merchant);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "更新成功");
        } else {
            result.put("code", 500);
            result.put("msg", "更新失败");
        }
        return result;
    }
    
    @PostMapping("/audit/{id}/{status}")
    public Map<String, Object> audit(@PathVariable Integer id, 
                                     @PathVariable Integer status,
                                     @RequestParam(required = false) String rejectReason) {
        Map<String, Object> result = new HashMap<>();
        int rows = merchantService.updateStatus(id, status, rejectReason);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", status == 1 ? "审核通过" : "审核拒绝");
        } else {
            result.put("code", 500);
            result.put("msg", "审核操作失败");
        }
        return result;
    }
    
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        int rows = merchantService.deleteMerchant(id);
        if (rows > 0) {
            result.put("code", 200);
            result.put("msg", "删除成功");
        } else {
            result.put("code", 500);
            result.put("msg", "删除失败");
        }
        return result;
    }
}
