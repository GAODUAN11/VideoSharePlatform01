package org.example.videoshareplatform01.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.videoshareplatform01.model.User;
import org.example.videoshareplatform01.model.Video;
import org.example.videoshareplatform01.service.VideoService;
import org.example.videoshareplatform01.util.ConfigUtil;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.nio.charset.StandardCharsets;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private VideoService videoService;
    private static final String UPLOAD_DIR = ConfigUtil.getProperty("video.upload.dir", "D:/video_uploads");

    @Override
    public void init() throws ServletException {
        videoService = new VideoService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // 获取分类参数
        String categoryName = request.getParameter("category");
        
        List<Video> videos;
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            // 按分类获取视频
            videos = videoService.getVideosByCategory(categoryName);
            request.setAttribute("selectedCategory", categoryName);
        } else {
            // 获取所有公共视频
            videos = videoService.getAllPublicVideos();
        }
        
        videos.forEach(video -> {
            String filePath = video.getFilePath();
            if (filePath != null) {
                video.setFilePath(URLEncoder.encode(filePath, StandardCharsets.UTF_8));
            }
        });
        request.setAttribute("publicVideos", videos);
        
        // 添加所有分类列表，用于下拉菜单
        request.setAttribute("allCategories", videoService.getAllCategories());
        
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}