package com.uniwise.hbase.bo;

import java.util.List;
import java.util.Map;

/**
 * Created by wangchongyu on 2017/7/11.
 */
public class HBaseCol {

    private String rowKey;//行主键
    private Map<String,List<ColVal>> fvMap;//列族，列名，值得键值对

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public Map<String, List<ColVal>> getFvMap() {
        return fvMap;
    }

    public void setFvMap(Map<String, List<ColVal>> fvMap) {
        this.fvMap = fvMap;
    }

    public class ColVal{
        private String qualifier;
        private byte[] value;

        public ColVal(String qualifier, byte[] value) {
            this.qualifier = qualifier;
            this.value = value;
        }

        public String getQualifier() {
            return qualifier;
        }

        public void setQualifier(String qualifier) {
            this.qualifier = qualifier;
        }

        public byte[] getValue() {
            return value;
        }

        public void setValue(byte[] value) {
            this.value = value;
        }
    }
}
