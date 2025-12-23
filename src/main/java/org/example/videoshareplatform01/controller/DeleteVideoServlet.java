package org.example.videoshareplatform01.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.videoshareplatform01.model.User;
import org.example.videoshareplatform01.service.VideoService;

import java.io.IOException;

@WebServlet("/deleteVideo")
public class DeleteVideoServlet extends HttpServlet {
    private VideoService videoService;

    @Override
    public void init() throws ServletException {
        videoService = new VideoService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String videoIdParam = request.getParameter("videoId");
        if (videoIdParam != null) {
            try {
                int videoId = Integer.parseInt(videoIdParam);
                videoService.deleteVideoAndFile(videoId, user.getId());
            } catch (NumberFormatException ignored) {
                // ignore malformed id, nothing to delete
            }
        }

        response.sendRedirect("profile");
    }
}

