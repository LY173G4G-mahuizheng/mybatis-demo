package com.mhz.sqlSession;

import com.mhz.pojo.Configuration;
import com.mhz.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementid, Object... args) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMapperStatementMap().get(statementid);
        List<Object> query = simpleExecutor.query(configuration, mappedStatement, args);
        return (List<E>) query;
    }

    @Override
    public <T> T selectOne(String statementid, Object... args) throws Exception {
        List<Object> objects = selectList(statementid, args);
        if (objects.size() == 1){
            return (T) objects.get(0);
        }
        return null;
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) throws Exception {
        //使用jdk代理围为dao层生成代理对象
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //底层还是执行jdbc代码
                //准备参数 1、statementid：sql唯一标识   namespace.id=接口全限定名加方法名
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementid = className+"."+methodName;

                //准备参数2 params：args
                //获取被调用方法的返回值类型
                Type genericReturnType = method.getGenericReturnType();
                //判断是否进行了参数类型泛型化
                if (genericReturnType instanceof ParameterizedType){
                    return selectList(statementid, args);
                }
                return selectOne(statementid, args);
            }
        });

        return (T) proxyInstance;
    }
}
