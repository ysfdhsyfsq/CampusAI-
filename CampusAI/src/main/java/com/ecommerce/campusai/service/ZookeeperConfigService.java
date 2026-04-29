package com.ecommerce.campusai.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZookeeperConfigService {
    
    @Autowired
    private CuratorFramework curatorFramework;
    
    private static final String CONFIG_PATH = "/campusai/config";
    
    public void setConfig(String key, String value) throws Exception {
        String path = CONFIG_PATH + "/" + key;
        byte[] data = value.getBytes();
        
        if (curatorFramework.checkExists().forPath(path) != null) {
            curatorFramework.setData().forPath(path, data);
        } else {
            curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, data);
        }
    }
    
    public String getConfig(String key) throws Exception {
        String path = CONFIG_PATH + "/" + key;
        if (curatorFramework.checkExists().forPath(path) != null) {
            byte[] data = curatorFramework.getData().forPath(path);
            return new String(data);
        }
        return null;
    }
    
    public void deleteConfig(String key) throws Exception {
        String path = CONFIG_PATH + "/" + key;
        if (curatorFramework.checkExists().forPath(path) != null) {
            curatorFramework.delete().forPath(path);
        }
    }
}
