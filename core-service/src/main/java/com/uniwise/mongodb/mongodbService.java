package com.uniwise.mongodb;

import com.uniwise.service.PicService;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;

/**
 * Created by wangchongyu on 2017/7/15.
 */
public class mongodbService implements PicService {

    private MongoTemplate mongoTemplate;

    @Override
    public String savePic(File file) {
        return null;
    }

    @Override
    public String savePic(File file, String attr) {
        return null;
    }

    @Override
    public File download(String picName) {
        return null;
    }

    @Override
    public String searchPic(String param) {
        return null;
    }

    @Override
    public String delete(String picName) {
        return null;
    }
}
