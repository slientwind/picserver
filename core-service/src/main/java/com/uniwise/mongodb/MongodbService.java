package com.uniwise.mongodb;

import com.mongodb.gridfs.GridFSFile;
import com.uniwise.service.PicService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by wangchongyu on 2017/7/15.
 */
public class MongodbService implements PicService {

    private MongoTemplate mongoTemplate;
    private GridFsTemplate gridFsTemplate;


    public MongodbService(MongoTemplate mongoTemplate, GridFsTemplate gridFsTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public String savePic(File file) {
        String result=null;
        try {
           GridFSFile fs= gridFsTemplate.store(new FileInputStream(file), file.getName());
           result=fs.getFilename();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String savePic(File file, String attr) {
        return null;
    }

    @Override
    public File download(String picName) {
        try {
            GridFsResource resource = gridFsTemplate.getResource(picName);
            return resource.getFile();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
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
