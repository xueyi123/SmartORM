package com.iih5.smartorm.model;

import java.util.HashMap;
import java.util.Map;

public class Model {
    private String table;//表名
    private Map<String,String> map = new HashMap<String, String>();
    //设置+ 或- 或 * 或 /
    public String calPrefix(String para){
        return map.get(para);
    }
    //返回 + 或- 或 * 或 /
    public void addCalPrefix(String field,String value){
        map.put(field, value);
    }

    public Map<String,String> findMap(){
        return map;
    }
    public void setTableName(String table){
       this.table = table;
   }
    public String tableName(){
        return table;
    }
}
