package com.mhz;

import com.mhz.dao.IUserDao;
import com.mhz.io.Resources;
import com.mhz.pojo.User;
import com.mhz.sqlSession.SqlSession;
import com.mhz.sqlSession.SqlSessionFactory;
import com.mhz.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

public class test {

    @Test
    public void test() throws Exception {
        InputStream resouceAsStream = Resources.getResouceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().builder(resouceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        IUserDao iUserDao = sqlSession.getMapper(IUserDao.class);
        User zhangsan = new User(1, "zhangsan");
        User user = iUserDao.selectOne(zhangsan);
        System.out.println(user);

        List<User> users = iUserDao.selectList();
        for (User user1 : users) {
            System.out.println(user1);
        }
    }
}
