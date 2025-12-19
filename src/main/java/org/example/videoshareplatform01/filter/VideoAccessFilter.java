package org.example.videoshareplatform01.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.videoshareplatform01.model.User;
import org.example.videoshareplatform01.model.Video;
import org.example.videoshareplatform01.service.VideoService;
import org.example.videoshareplatform01.util.ConfigUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebFilter("/videos/*")
public class VideoAccessFilter implements Filter {
    private static final String UPLOAD_DIR = ConfigUtil.getProperty("video.upload.dir", "D:/video_uploads");
    private VideoService videoService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        videoService = new VideoService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 获取请求的视频文件名
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String videoFileName = requestURI.substring(contextPath.length() + "/videos/".length());

        // 构建完整文件路径
        Path videoPath = Paths.get(UPLOAD_DIR, videoFileName);
        System.out.println("尝试访问视频文件: " + videoPath.toString());

        // 检查文件是否存在
        if (Files.exists(videoPath) && Files.isRegularFile(videoPath)) {
            System.out.println("文件存在，继续处理权限");
            
            // 检查视频访问权限
            if (!checkVideoAccessPermission(videoFileName, httpRequest)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            
            // 设置响应内容类型
            String mimeType = httpRequest.getServletContext().getMimeType(videoPath.toString());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            httpResponse.setContentType(mimeType);
            httpResponse.setContentLengthLong(videoPath.toFile().length());
            
            // 输出文件内容
            try (FileInputStream fis = new FileInputStream(videoPath.toFile());
                 OutputStream os = httpResponse.getOutputStream()) {
                
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }
        } else {
            System.out.println("文件不存在: " + videoPath.toString());
            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    /**
     * 检查视频访问权限
     */
    private boolean checkVideoAccessPermission(String filename, HttpServletRequest request) {
        try {
            // 从文件名中提取视频ID
            int videoId = extractVideoIdFromFilename(filename);
            if (videoId <= 0) {
                // 如果无法解析ID，可能是旧格式的文件名，允许访问（向后兼容）
                return true;
            }
            
            // 查询视频信息
            Video video = videoService.getVideoById(videoId);
            if (video == null) {
                return true; // 视频不存在，仍允许尝试访问文件
            }
            
            // 如果视频是公开的，任何人都可以访问
            if (video.isPublic()) {
                return true;
            }
            
            // 如果视频是私有的，检查用户是否有权限访问
            HttpSession session = request.getSession(false);
            User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
            
            // 只有视频所有者才能访问私有视频
            return currentUser != null && currentUser.getId() == video.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常时，默认允许访问（避免因权限检查问题导致无法访问）
            return true;
        }
    }
    
    /**
     * 从文件名中提取视频ID
     * 假设文件名格式为: timestamp_videoId_originalFilename
     */
    private int extractVideoIdFromFilename(String filename) {
        try {
            // 使用正则表达式匹配 timestamp_videoId_filename 格式
            String[] parts = filename.split("_");
            if (parts.length >= 2) {
                return Integer.parseInt(parts[1]);
            }
        } catch (Exception e) {
            // 解析失败，返回0
            System.out.println("无法从文件名解析视频ID: " + filename);
        }
        return 0;
    }
}