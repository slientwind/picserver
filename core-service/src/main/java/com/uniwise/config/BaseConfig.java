package com.uniwise.config;

import com.uniwise.mongodb.MongodbService;
import com.uniwise.service.PicService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.RestController;

@Configuration
public class BaseConfig {

    @Bean
    public PicService picService(MongoTemplate mongoTemplate, GridFsTemplate gridFsTemplate){
        return new MongodbService(mongoTemplate,gridFsTemplate);
    }
}
