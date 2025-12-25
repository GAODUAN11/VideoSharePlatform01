package org.example.videoshareplatform01.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.example.videoshareplatform01.service.VideoService;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileUploadUtil {
    private static final String UPLOAD_DIR = ConfigUtil.getProperty("video.upload.dir", "D:/video_uploads");
    private static final long MAX_FILE_SIZE = 1024L * 1024L * 1024L; // 1 GB

    /**
     * 处理文件上传请求
     * @param request HTTP请求对象
     * @return 包含表单字段和文件信息的映射
     */
    public static Map<String, String> handleFileUpload(HttpServletRequest request) throws Exception {
        Map<String, String> result = new HashMap<>();
        
        // 遍历所有parts
        for (Part part : request.getParts()) {
            String fieldName = part.getName();
            
            // 检查是否是文件字段
            String submittedFileName = part.getSubmittedFileName();
            if (submittedFileName != null && !submittedFileName.isEmpty()) {
                // 这是一个文件字段
                String fileName = Paths.get(submittedFileName).getFileName().toString();
                
                // 创建上传目录（如果不存在）
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // 生成临时文件名
                long uploadTimestamp = System.currentTimeMillis();
                String tempFileName = uploadTimestamp + "_0_" + fileName;

                // 保存文件
                String filePath = UPLOAD_DIR + File.separator + tempFileName;
                part.write(filePath);
                
                result.put("fileName", tempFileName);
            } else {
                // 这是一个普通表单字段
                String fieldValue = request.getParameter(fieldName);
                result.put(fieldName, fieldValue);
            }
        }

        return result;
    }
    
    /**
     * 处理视频上传的完整流程
     * @param request HTTP请求对象
     * @param videoService 视频服务
     * @param userId 用户ID
     * @return 最终的文件名
     */
    public static String processVideoUpload(HttpServletRequest request, VideoService videoService, int userId) throws Exception {
        Map<String, String> uploadData = handleFileUpload(request);
        
        String title = uploadData.get("title");
        String description = uploadData.get("description");
        String fileName = uploadData.get("fileName");
        String isPublicParam = uploadData.get("isPublic");
        boolean isPublic = "on".equals(isPublicParam) || "true".equals(isPublicParam);

        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("未上传视频文件");
        }

        // 先保存到数据库获取videoId
        int videoId = videoService.uploadVideoAndGetId(title, description, fileName, userId, isPublic);

        // 重新命名文件，加入真实的videoId，并复用最初的时间戳
        String oldFilePath = UPLOAD_DIR + File.separator + fileName;
        String[] parts = fileName.split("_", 3);
        if (parts.length >= 3) {
            String timestamp = parts[0];
            String newFileName = timestamp + "_" + videoId + "_" + parts[2];
            String newFilePath = UPLOAD_DIR + File.separator + newFileName;
            
            // 重命名文件
            File oldFile = new File(oldFilePath);
            File newFile = new File(newFilePath);
            if (!oldFile.renameTo(newFile)) {
                // 如果renameTo失败，尝试使用copy和delete方式
                java.nio.file.Files.copy(oldFile.toPath(), newFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                oldFile.delete();
            }
            
            // 更新数据库中的文件路径
            videoService.updateVideoFilePath(videoId, newFileName);
            
            return newFileName;
        }
        
        return fileName;
    }
    
    /**
     * 验证上传的文件
     * @param part 文件部分
     * @return 是否有效
     */
    public static boolean isValidVideoFile(Part part) {
        if (part == null || part.getSubmittedFileName() == null || part.getSubmittedFileName().isEmpty()) {
            return false;
        }
        
        String fileName = part.getSubmittedFileName();
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();
        
        // 检查文件扩展名是否为视频格式
        String[] validExtensions = {"mp4", "avi", "mov", "wmv", "flv", "webm", "mkv", "m4v"};
        for (String ext : validExtensions) {
            if (ext.equals(extension)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 获取文件大小
     * @param part 文件部分
     * @return 文件大小（字节）
     */
    public static long getFileSize(Part part) {
        return part.getSize();
    }
}