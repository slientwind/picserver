package com.uniwise.rest;

import com.uniwise.service.PicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/core")
public class RestService {

    @Autowired
    private PicService picService;


    @RequestMapping("/download")
    public void downloadPic(String name, HttpServletResponse response){
           try{
               File file=picService.download(name);
               OutputStream os=response.getOutputStream();
               byte[] data=new byte[1024];
               InputStream is=new FileInputStream(file);
               int len=0;
               while(-1!=(len=is.read(data,0,1024))){
                   os.write(data,0,len);
               }
               os.flush();
               os.close();
           }catch (Exception e){
               e.printStackTrace();
           }
    }
}
