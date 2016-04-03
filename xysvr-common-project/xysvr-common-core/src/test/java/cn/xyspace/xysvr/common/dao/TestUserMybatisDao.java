package cn.xyspace.xysvr.common.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import cn.xyspace.xysvr.common.core.utils.BeanUtils;
import cn.xyspace.xysvr.common.entity.User;
import cn.xyspace.xysvr.common.test.util.AbstractDbUnitTestCase;
import cn.xyspace.xysvr.common.test.util.EntitiesHelper;

/**
 * 
 * @author ChenFangjie(2014/11/30 14:51:24)
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestUserMybatisDao extends AbstractDbUnitTestCase {

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    @Inject
    private IUserMybatisDao userMybatisDao;

    @Before
    public void setUp() throws DataSetException, SQLException, IOException {
        SqlSession session = this.sqlSessionFactory.openSession();
        TransactionSynchronizationManager.bindResource(this.sqlSessionFactory, new SqlSessionHolder(session, ExecutorType.REUSE, null));

        super.backupAllTable();
    }

    @After
    public void tearDown() throws FileNotFoundException, DatabaseUnitException, SQLException {
        SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(this.sqlSessionFactory);
        SqlSession session = holder.getSqlSession();
        session.flushStatements();
        TransactionSynchronizationManager.unbindResource(this.sqlSessionFactory);

        super.resumeTable();
    }

    @Test
    public void testSelect() throws DatabaseUnitException, SQLException {
        IDataSet ds = super.createDateSet("t_user");
        DatabaseOperation.CLEAN_INSERT.execute(AbstractDbUnitTestCase.dbunitCon, ds);

        Map<String, Object> params = BeanUtils.toMap(new User("1", null));
        User user = this.userMybatisDao.select(params);

        System.out.println("toString: " + user);

        EntitiesHelper.assertUser(user);
    }

    @Test
    public void testSelectById() throws DatabaseUnitException, SQLException {
        IDataSet ds = super.createDateSet("t_user");
        DatabaseOperation.CLEAN_INSERT.execute(AbstractDbUnitTestCase.dbunitCon, ds);

        User user = this.userMybatisDao.selectById("1");

        System.out.println("toString: " + user);

        EntitiesHelper.assertUser(user);
    }

}
