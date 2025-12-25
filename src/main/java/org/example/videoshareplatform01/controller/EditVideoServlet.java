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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/editVideo")
public class EditVideoServlet extends HttpServlet {
    private VideoService videoService;

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

        // 获取视频ID参数
        String videoIdParam = request.getParameter("id");
        if (videoIdParam == null || videoIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int videoId = Integer.parseInt(videoIdParam);
            Video video = videoService.getVideoById(videoId);

            if (video == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 检查视频是否属于当前用户
            if (video.getUserId() != user.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // 获取所有分类供编辑页面使用
            List<String> allCategories = videoService.getAllCategories();
            request.setAttribute("allCategories", allCategories);
            
            // 获取视频当前的分类ID列表
            List<Integer> currentCategoryIds = new ArrayList<>();
            List<String> currentCategoryNames = video.getCategories();
            if (currentCategoryNames != null && !currentCategoryNames.isEmpty()) {
                for (String categoryName : currentCategoryNames) {
                    Integer categoryId = videoService.getCategoryIdByName(categoryName);
                    if (categoryId != null) {
                        currentCategoryIds.add(categoryId);
                    }
                }
            }
            request.setAttribute("currentCategoryIds", currentCategoryIds);

            request.setAttribute("video", video);
            request.getRequestDispatcher("/WEB-INF/views/editVideo.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // 获取表单参数
        String videoIdParam = request.getParameter("videoId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        if (videoIdParam == null || videoIdParam.isEmpty() || title == null || title.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int videoId = Integer.parseInt(videoIdParam);
            Video video = videoService.getVideoById(videoId);

            if (video == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 检查视频是否属于当前用户
            if (video.getUserId() != user.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            // 更新视频信息
            video.setTitle(title);
            video.setDescription(description);
            videoService.updateVideo(video);
            
            // 获取选中的分类名称
            String[] categoryNames = request.getParameterValues("categories");
            List<Integer> selectedCategoryIds = new ArrayList<>();
            if (categoryNames != null) {
                for (String categoryName : categoryNames) {
                    // 根据分类名称获取分类ID
                    Integer categoryId = videoService.getCategoryIdByName(categoryName);
                    if (categoryId != null) {
                        selectedCategoryIds.add(categoryId);
                    }
                }
            }
            
            // 更新视频分类
            videoService.updateVideoCategories(videoId, selectedCategoryIds);

            // 重定向回个人页面
            response.sendRedirect("profile");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}