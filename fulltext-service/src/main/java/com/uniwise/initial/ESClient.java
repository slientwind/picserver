package com.uniwise.initial;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchongyu on 2017/7/10.
 */
public class ESClient {

     private Settings settings;
     private String ip;
     private Integer port;
     private Client client;

     private static Logger logger= LoggerFactory.getLogger(ESClient.class);

    public ESClient(Settings settings, String ip, Integer port) {
        this.settings = settings;
        this.ip = ip;
        this.port = port;
    }

    public void initClient(){
        try {
            client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean deleteIndex(String index){
        boolean result=true;
        try {
            IndicesExistsResponse response = client.admin().indices().prepareExists(index).execute().actionGet();
            if (response.isExists()) {
                DeleteIndexResponse deleteIndexResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
                result = deleteIndexResponse.isAcknowledged();
                logger.info("删除原索引成功"+result);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result=false;
        }
        return result;
    }

    public boolean initialIndex(String index,Settings settings){
        boolean result=true;
        IndicesAdminClient adminClient=client.admin().indices();
        try{
            CreateIndexResponse indexResponse=adminClient.prepareCreate(index).setSettings(settings).get();
            logger.info("创建索引["+index+"]成功！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result=false;
        }
        return result;
    }

    public boolean initailType(String index,String type,String settings){
        boolean result=true;
        IndicesAdminClient adminClient=client.admin().indices();
        try{
            PutMappingRequest request= Requests.putMappingRequest(index).type(type).source(mapBuilder(type,settings), XContentType.JSON);
            PutMappingResponse MapResponse=adminClient.putMapping(request).actionGet();
            logger.info("创建type["+type+"]成功！");
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result=false;
        }
        return result;
    }

    public void insertDoc(String index,String type,String content,String id){
        IndexResponse response=client.prepareIndex(index,type,id).setSource(content,XContentType.JSON).get();
    }

    public <T> List<T> get(String index, String type, String query, Class<T> T){
        QueryBuilder builder= QueryBuilders.termQuery("key.pinyin",query);
        SearchResponse response=client.prepareSearch(index).setTypes(type).setQuery(builder).execute().actionGet();
        List<T> list=new ArrayList<>();
        SearchHits hits=response.getHits();
        if(hits!=null&&hits.getTotalHits()>0) {
            for (SearchHit hit : response.getHits()) {
                T object = JSON.parseObject((String)hit.getSourceAsMap().get("content"), T);
                list.add(object);
            }
        }
        return list;
    }

    private String mapBuilder(String type,String setting){
        JSONObject object=new JSONObject();
        JSONObject map=JSONObject.parseObject(setting);
        object.put(type,map);
        return object.toJSONString();
    }

}
