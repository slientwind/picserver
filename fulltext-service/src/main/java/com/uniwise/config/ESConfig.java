package com.uniwise.config;

import com.uniwise.initial.ESClient;
import com.uniwise.property.ConfigProperty;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangchongyu on 2017/7/10.
 */
@Configuration
@EnableConfigurationProperties
public class ESConfig {

    @Autowired
    private ConfigProperty configProperty;

    @Bean
    public ESClient esClient(){
        Settings settings=Settings.builder()
                .put("cluster.name",configProperty.getClusterName())
                .put("client.transport.sniff",true).build();
        ESClient esClient=new ESClient(settings,configProperty.getIp(),configProperty.getPort());
        esClient.initClient();
        return esClient;
    }
}
