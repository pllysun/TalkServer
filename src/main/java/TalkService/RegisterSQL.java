package TalkService;

import TalkBasic.UserSQL;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class RegisterSQL {
    public void UserSQL(String user, String password)
    {
        Connection connection=null;
        UserSQL query=null;
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            properties.load(resourceAsStream);
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            connection = dataSource.getConnection();

            String sql="INSERT INTO user_table(USER,PASSWORD)VALUES(?,?)";
            QueryRunner queryRunner=new QueryRunner();
            queryRunner.update(connection,sql,user,password);

            System.out.println("用户："+user+"注册成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(connection);
        }
    }
    public boolean CheckRegister(String user)
    {
        Connection connection=null;
        UserSQL query=null;
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            properties.load(resourceAsStream);
            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            connection = dataSource.getConnection();
            String sql="SELECT * FROM user_table WHERE USER=?";

            BeanHandler<UserSQL>handler=new BeanHandler<>(UserSQL.class);
            QueryRunner queryRunner = new QueryRunner();
            query = queryRunner.query(connection, sql, handler, user);


        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(connection);
        }
        return query == null;
    }
}
