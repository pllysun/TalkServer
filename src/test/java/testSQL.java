import TalkBasic.User;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class testSQL {
    @Test
    public void testSql1() {
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            properties.load(resourceAsStream);
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            connection = dataSource.getConnection();
            System.out.println(connection);
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("请输入账户：");
//        String user = scanner.next();
//        System.out.println("请输入密码：");
//        String password = scanner.next();
            String sql = "insert into user_table(user,password)values(?,?)";
            ps = connection.prepareStatement(sql);
            for (int i = 5; i < 15; i++) {
                String values = String.valueOf(i);
                ps.setString(1, values);
                ps.setString(2, values);
            }
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            try {
                if (ps != null) ps.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            try {
                if (connection != null) connection.close();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @Test
    public void testsql2() throws Exception {
        Properties properties = new Properties();
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        properties.load(resourceAsStream);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账户：");
        String user = scanner.next();
        System.out.println("请输入密码：");
        String password = scanner.next();
        String sql="SELECT * FROM user_table WHERE USER=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,user);
        //执行，并返回结果集
        ResultSet query = preparedStatement.executeQuery();
        if(query.next())//判断结果集中的下一条是否有数据，如果有数据返回true，并指针下移，如果没有返回false
        {
            //获取当前数据各个字段的值
            String query_user = query.getString(2);
            String query_password = query.getString(3);
            if (user.equals(query_user) && password.equals(query_password)) System.out.println("验证通过！");
            else System.out.println("验证失败！");
        }

    }
    @Test
    public void testsql3() throws Exception {
        Properties properties = new Properties();
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        properties.load(resourceAsStream);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入账户：");
        String user = scanner.next();
        System.out.println("请输入密码：");
        String password = scanner.next();
        String sql="SELECT * FROM user_table WHERE USER=?";

        BeanHandler<SqlUser>handler=new BeanHandler<>(SqlUser.class);
        QueryRunner queryRunner = new QueryRunner();
        SqlUser query = queryRunner.query(connection, sql, handler, user);
        System.out.println(query.getUser());
        System.out.println(query.getPassword());

    }
    @Test
    public void UserSQL()
    {
        Connection connection=null;
        SqlUser query=null;
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            properties.load(resourceAsStream);
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            connection = dataSource.getConnection();
            System.out.println(connection);
            String sql="insert into user_table(user,password)values(?,?)";

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(connection);
        }

    }
}
