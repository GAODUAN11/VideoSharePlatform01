package org.example.videoshareplatform01.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.example.videoshareplatform01.model.User;
import org.example.videoshareplatform01.service.VideoService;
import org.example.videoshareplatform01.util.ConfigUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet("/upload")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 10,  // 10 MB
    maxFileSize = 1024 * 1024 * 1024,      // 1 GB
    maxRequestSize = 1024 * 1024 * 1024    // 1 GB
)
public class UploadServlet extends HttpServlet {
    private VideoService videoService;
    // 从配置文件读取视频存储目录
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

        try {
            // 获取上传的文件
            Part filePart = request.getPart("videoFile");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String isPublicParam = request.getParameter("isPublic");
            boolean isPublic = "on".equals(isPublicParam) || "true".equals(isPublicParam);

            // 获取文件名
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // 创建上传目录（如果不存在）
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 保存文件，文件名格式：timestamp_videoId_originalFilename
            long uploadTimestamp = System.currentTimeMillis();
            String tempFileName = uploadTimestamp + "_0_" + fileName;

            // 先保存到数据库获取videoId
            int videoId = videoService.uploadVideoAndGetId(title, description, tempFileName, user.getId(), isPublic);

            // 重新命名文件，加入真实的videoId，并复用最初的时间戳
            String finalFileName = uploadTimestamp + "_" + videoId + "_" + fileName;
            videoService.updateVideoFilePath(videoId, finalFileName); // 更新数据库中的文件路径
            
            filePart.write(UPLOAD_DIR + File.separator + finalFileName);

            response.sendRedirect("profile");
        } catch (IllegalStateException e) {
            // 处理文件大小超出限制的异常
            request.setAttribute("errorMessage", "文件大小超出限制，请上传小于1GB的文件");
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