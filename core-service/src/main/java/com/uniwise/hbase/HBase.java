package com.uniwise.hbase;

import com.uniwise.hbase.bo.HBaseCol;
import com.uniwise.tool.ByteTool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.file.tfile.ByteArray;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by wangchongyu on 2017/7/11.
 */
public class HBase {

    private  Configuration conf=null;
    private Connection conn=null;

    public HBase(String hdName){
        conf=HBaseConfiguration.create();
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.zookeeper.quorum", hdName);
    }

    /**
    *@Author wangchongyu
    *@Date 2017/7/11 10:10
    *@Description 创建表
    */
    public void createTable(String tableName ,Map<String,Integer> families) throws Exception{
        init();
        HBaseAdmin admin=(HBaseAdmin)conn.getAdmin();
        HTableDescriptor htd=new HTableDescriptor(TableName.valueOf(tableName));
        if(!CollectionUtils.isEmpty(families)){
            for(String family:families.keySet()){
                HColumnDescriptor hcd=new HColumnDescriptor(family);
                hcd.setBlocksize(families.get(family));
                htd.addFamily(hcd);
            }
        }
        admin.createTable(htd);
        admin.close();
    }

    /**
    *@Author wangchongyu
    *@Date 2017/7/11 13:43
    *@Description 批量插入
    */
    public void putAll(List<HBaseCol> cols, String tableName) throws Exception{
        init();
        HTable hTable=(HTable)conn.getTable(TableName.valueOf(tableName));
        List<Put> puts=new ArrayList<>();
        for(HBaseCol col:cols){
            Put put=new Put(Bytes.toBytes(col.getRowKey()));
            Map<String,List<HBaseCol.ColVal>> families=col.getFvMap();
            Set<String> keySet=families.keySet();
            for(String family:keySet){
                List<HBaseCol.ColVal> list=families.get(family);
                for(HBaseCol.ColVal colVal:list){
                    put.addImmutable(Bytes.toBytes(family),Bytes.toBytes(colVal.getQualifier()),colVal.getValue());
                }
            }
            puts.add(put);
        }
        hTable.put(puts);
        hTable.close();
    }

    /**
    *@Author wangchongyu
    *@Date 2017/7/11 13:52
    *@Description 单行插入
    */
    public void putSingle(String tableName,HBaseCol col) throws Exception{
        init();
        HTable hTable=(HTable)conn.getTable(TableName.valueOf(tableName));
        Put put=new Put(Bytes.toBytes(col.getRowKey()));
        Map<String,List<HBaseCol.ColVal>> families=col.getFvMap();
        Set<String> keySet=families.keySet();
        for(String family:keySet){
            List<HBaseCol.ColVal> list=families.get(family);
            for(HBaseCol.ColVal colVal:list){
                put.addColumn(Bytes.toBytes(family),Bytes.toBytes(colVal.getQualifier()),colVal.getValue());
            }
        }
        hTable.put(put);
        hTable.close();
    }

    /**
    *@Author wangchongyu
    *@Date 2017/7/12 10:40
    *@Description 根据RowKey查询 返回给定类的对象
    *param
    */
    public <T>  T getByRowKey(Class<T> c,String rowKey,String tableName) throws Exception{
        init();
        HTable hTable=(HTable)conn.getTable(TableName.valueOf(tableName));
        Get get=new Get(Bytes.toBytes(rowKey));
        Result result=hTable.get(get);
        hTable.close();
        return resultToObject(result,c);
    }

    /**
    *@Author wangchongyu
    *@Date 2017/7/12 10:46
    *@Description
    *@param 
    */
    private <T> T resultToObject(Result result,Class<T> t) throws Exception{
        Cell[] cells=result.rawCells();
        T o=t.newInstance();
        for(Cell cell:cells){
           String name=new String(cell.getQualifierArray());
           Field field=t.getDeclaredField(name);
           Class targetType=field.getType();
           Object value= ByteTool.byteToObject(cell.getValueArray(),targetType.newInstance());
           field.set(o,value);
        }
        return o;
    }

    /**
    *@Author wangchongyu
    *@Date 2017/7/11 10:10
    *@Description 初始化连接
    */
    private void init(){
        try {
            if(conn==null)
               conn = ConnectionFactory.createConnection(conf);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
