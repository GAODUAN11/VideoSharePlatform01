package org.example.videoshareplatform01.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.videoshareplatform01.util.DatabaseUtil;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 应用启动时的初始化操作
        System.out.println("VideoSharePlatform应用正在启动...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 应用关闭时的清理操作
        System.out.println("VideoSharePlatform应用正在关闭...");
        // 清理数据库连接资源
        DatabaseUtil.shutdown();
        System.out.println("数据库资源已清理完成");
    }
}