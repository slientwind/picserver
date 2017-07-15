package com.uniwise.service;

import java.io.File;

/**
 * Created by wangchongyu on 2017/7/15.
 */
public interface PicService {

    public String savePic(File file);

    public String savePic(File file,String attr);

    public File download(String picName);

    public String searchPic(String param);

    public String delete(String picName);
}
