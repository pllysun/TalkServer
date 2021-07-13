package TalkService;

import TalkBasic.User;
import TalkBasic.UserSQL;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class LoginSQL {
    private boolean login;
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

            String sql="SELECT * FROM user_table WHERE USER=?";

            BeanHandler<UserSQL>handler=new BeanHandler<>(UserSQL.class);
            QueryRunner queryRunner = new QueryRunner();
            query = queryRunner.query(connection, sql, handler, user);
            login = query.getUser().equals(user) && query.getPassword().equals(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DbUtils.closeQuietly(connection);
        }

    }

    public boolean CheckLogin()
    {
        return login;
    }

}
