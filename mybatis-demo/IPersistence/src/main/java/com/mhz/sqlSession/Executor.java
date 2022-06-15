package com.mhz.sqlSession;

import com.mhz.pojo.Configuration;
import com.mhz.pojo.MappedStatement;

import java.util.List;

public interface Executor {

    public <E>List<E> query(Configuration configuration, MappedStatement mappedStatement, Object ... args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, Exception;
}
