package com.uniwise.tool;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Created by wangchongyu on 2017/7/12.
 */
public class ByteTool {

    public static Object byteToObject(byte[] bytes,Object o){
        try{
            ByteArrayInputStream bis=new ByteArrayInputStream(bytes);
            ObjectInputStream ois=new ObjectInputStream(bis);
            o=ois.readObject();
            ois.close();
            bis.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return o;
    }
}
