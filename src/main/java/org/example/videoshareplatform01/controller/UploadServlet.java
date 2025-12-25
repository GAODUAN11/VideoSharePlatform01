package org.example.videoshareplatform01.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.videoshareplatform01.model.User;
import org.example.videoshareplatform01.service.VideoService;
import org.example.videoshareplatform01.util.ConfigUtil;
import org.example.videoshareplatform01.util.FileUploadUtil;

import java.io.IOException;

@WebServlet("/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 10,  // 10 MB
    maxFileSize = 1024 * 1024 * 1024,      // 1 GB
    maxRequestSize = 1024 * 1024 * 1024    // 1 GB
)
public class UploadServlet extends HttpServlet {
    private VideoService videoService;

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

        try {
            // 使用优化的FileUploadUtil处理文件上传
            String finalFileName = FileUploadUtil.processVideoUpload(request, videoService, user.getId());

            response.sendRedirect("profile");
        } catch (Exception e) {
            // 处理上传异常，提供更详细的错误信息
            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                errorMessage = "上传失败: " + e.getClass().getSimpleName();
            }
            
            if (errorMessage.contains("size") || errorMessage.contains("超出") || errorMessage.toLowerCase().contains("limit")) {
                errorMessage = "文件大小超出限制，请上传小于1GB的文件";
            } else {
                errorMessage = "上传失败: " + errorMessage;
            }
            
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/views/upload.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/upload.jsp").forward(request, response);
    }
}