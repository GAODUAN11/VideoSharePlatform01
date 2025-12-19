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

@WebServlet("/video")
public class VideoServlet extends HttpServlet {
    private VideoService videoService;

    @Override
    public void init() throws ServletException {
        videoService = new VideoService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

            // 检查访问权限
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            
            // 如果视频是私有的，检查用户是否有权限访问
            if (!video.isPublic()) {
                if (currentUser == null || currentUser.getId() != video.getUserId()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }

            request.setAttribute("video", video);
            request.setAttribute("currentUser", currentUser);
            request.getRequestDispatcher("/WEB-INF/views/video.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}