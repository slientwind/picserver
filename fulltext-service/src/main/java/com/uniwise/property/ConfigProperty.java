package com.uniwise.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by wangchongyu on 2017/7/10.
 */
@Component
@ConfigurationProperties(prefix = "uniwise.elastic")
public class ConfigProperty {

    private String clusterName;
    private String ip;
    private Integer port;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
