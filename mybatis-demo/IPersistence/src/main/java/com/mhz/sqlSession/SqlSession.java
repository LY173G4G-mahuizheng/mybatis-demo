package com.mhz.sqlSession;

import java.util.List;

public interface SqlSession {


    //查询所有
    public <E> List <E> selectList(String statementid, Object ... args) throws Exception;

    //查询单个
    public <T> T selectOne(String statementid, Object ... args) throws Exception;

    public <T> T getMapper(Class<?> mapperClass) throws Exception;
}
