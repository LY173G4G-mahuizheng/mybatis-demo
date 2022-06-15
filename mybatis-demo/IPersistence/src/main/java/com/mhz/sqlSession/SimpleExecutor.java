package com.mhz.sqlSession;

import com.mhz.config.BoundSql;
import com.mhz.pojo.Configuration;
import com.mhz.pojo.MappedStatement;
import com.mhz.util.GenericTokenParser;
import com.mhz.util.ParameterMapping;
import com.mhz.util.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... args) throws Exception {

        //1.注册驱动 获取连接
        Connection connection = configuration.getDataSource().getConnection();

        //2.获取sql语句
        //转换sql语句  转换的过程中需要对值进行存储
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);

        //3.获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlText());

        //4.设置参数
        String paramterType = mappedStatement.getParamterType();
        Class<?> aclass = getClassType(paramterType);

        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList();
        for (int i = 0; i < parameterMappingList.size(); i++) {
            ParameterMapping parameterMapping = parameterMappingList.get(i);
            String content = parameterMapping.getContent();

            //反射
            Field declaredField = aclass.getDeclaredField(content);
            declaredField.setAccessible(true);
            Object o = declaredField.get(args[0]);
            preparedStatement.setObject(i+1, o);
        }

        //5.执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        String resultType = mappedStatement.getResultType();
        Class<?> resultClass = getClassType(resultType);

        ArrayList<Object> objects = new ArrayList<>();

        //6.封装返回结果
        while (resultSet.next()){
            Object o = resultClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //字段名
                String columnName = metaData.getColumnName(i);
                //字段值
                Object object = resultSet.getObject(columnName);

                //使用反射或内省 根据数据库表和实体的对应关系 完成封装
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, object);
            }
            objects.add(o);
        }

        return (List<E>) objects;
    }

    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if (null != paramterType){
            return Class.forName(paramterType);
        }

        return null;
    }

    /**
     * 1. 将#{}使用？进行代替
     * 2.解析出#{}里面的值进行存储
     * @param sql
     * @return
     */
    private BoundSql getBoundSql(String sql) {
        //标记处理类 配置解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        String parseSql = genericTokenParser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new BoundSql(parseSql, parameterMappings);
    }
}
