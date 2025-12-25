package org.example.videoshareplatform01.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.videoshareplatform01.model.Video;
import org.example.videoshareplatform01.service.VideoService;

import java.io.IOException;
import java.util.List;

@WebServlet("/category")
public class CategoryServlet extends HttpServlet {
    private VideoService videoService;

    @Override
    public void init() throws ServletException {
        videoService = new VideoService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryName = request.getParameter("name");
        
        if (categoryName == null || categoryName.trim().isEmpty()) {
            response.sendRedirect("categories");
            return;
        }

        // 获取该分类下的所有视频
        List<Video> videos = videoService.getVideosByCategory(categoryName);
        request.setAttribute("videos", videos);
        
        // 设置分类名称
        request.setAttribute("categoryName", categoryName);

        request.getRequestDispatcher("/WEB-INF/views/category.jsp").forward(request, response);
    }
}