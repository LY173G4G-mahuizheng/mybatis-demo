package com.mhz.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private DataSource dataSource;

    /**
     * key:statementid
     * value:封装好的mapperStatement对象
     */
    Map<String, MappedStatement> mapperStatementMap = new HashMap<>();

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Map<String, MappedStatement> getMapperStatementMap() {
        return mapperStatementMap;
    }

    public void setMapperStatementMap(Map<String, MappedStatement> mapperStatementMap) {
        this.mapperStatementMap = mapperStatementMap;
    }
}
