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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/deleteVideo")
public class DeleteVideoServlet extends HttpServlet {
    private VideoService videoService;
    private static final String UPLOAD_DIR = ConfigUtil.getProperty("video.upload.dir", "D:/video_uploads");

    @Override
    public void init() throws ServletException {
        videoService = new VideoService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // 获取视频ID参数
        String videoIdParam = request.getParameter("videoId");
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

            // 删除视频文件
            Path videoPath = Paths.get(UPLOAD_DIR, video.getFilePath());
            if (Files.exists(videoPath)) {
                Files.delete(videoPath);
            }

            // 从数据库中删除视频记录
            videoService.deleteVideo(videoId);

            // 重定向回个人页面
            response.sendRedirect("profile");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}