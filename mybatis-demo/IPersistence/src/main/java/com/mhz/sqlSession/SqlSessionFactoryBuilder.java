package com.mhz.sqlSession;

import com.mhz.config.XMLConfigBuilder;
import com.mhz.pojo.Configuration;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory builder(InputStream inputStream) throws PropertyVetoException, DocumentException {
        //第一步  使用dom4j解析配置文件，将解析出来的配置文件封装到Configuration中
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfig(inputStream);

        //第二部  使用sqlSessionFactory对象  工厂类：生产sqlSession对象
        DefaultSqlSessionFactory sqlSession = new DefaultSqlSessionFactory(configuration);
        return sqlSession;
    }
}
