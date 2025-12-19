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

import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
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

        List<Video> userVideos = videoService.getUserVideos(user.getId());
        request.setAttribute("userVideos", userVideos);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
}