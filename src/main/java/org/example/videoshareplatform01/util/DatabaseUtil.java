package org.example.videoshareplatform01.util;

import com.mysql.cj.jdbc.Driver;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final BasicDataSource dataSource = createDataSource();
    
    private static BasicDataSource createDataSource() {
        BasicDataSource ds = new BasicDataSource();
        
        // 显式设置MySQL驱动
        try {
            ds.setDriver(new Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        ds.setUrl(ConfigUtil.getProperty("db.url", "jdbc:mysql://localhost:3306/video_share?useSSL=false&serverTimezone=UTC"));
        ds.setUsername(ConfigUtil.getProperty("db.username", "root"));
        ds.setPassword(ConfigUtil.getProperty("db.password", "password"));
        
        // 设置连接池参数
        ds.setInitialSize(5);           // 初始化连接数
        ds.setMaxTotal(50);             // 最大连接数
        ds.setMaxIdle(20);              // 最大空闲连接数
        ds.setMinIdle(5);               // 最小空闲连接数
        ds.setMaxWaitMillis(10000);     // 获取连接的最大等待时间(毫秒)
        
        // 设置连接验证
        ds.setTestOnBorrow(true);       // 借用连接时测试
        ds.setTestOnReturn(false);      // 归还连接时测试
        ds.setTestWhileIdle(true);      // 空闲时测试
        ds.setValidationQuery("SELECT 1");
        ds.setTimeBetweenEvictionRunsMillis(30000); // 空闲连接回收线程运行间隔
        
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    /**
     * 应用关闭时调用此方法清理资源
     */
    public static void shutdown() {
        try {
            dataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}