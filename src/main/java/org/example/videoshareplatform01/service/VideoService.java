package org.example.videoshareplatform01.service;

import org.example.videoshareplatform01.dao.VideoDao;
import org.example.videoshareplatform01.dao.VideoDaoImpl;
import org.example.videoshareplatform01.model.Video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.example.videoshareplatform01.util.ConfigUtil;
import org.example.videoshareplatform01.util.FileDeletionManager;
import java.time.Duration;

public class VideoService {
    private VideoDao videoDao;

    private static final int DELETE_RETRY_TIMES = 3;
    private static final Duration DELETE_RETRY_INTERVAL = Duration.ofMillis(600);
    private static final int ASYNC_DELETE_ATTEMPTS = 10;
    private static final Duration ASYNC_DELETE_INTERVAL = Duration.ofSeconds(2);

    public VideoService() {
        this.videoDao = new VideoDaoImpl();
    }

    public void uploadVideo(String title, String description, String filePath, int userId, boolean isPublic) {
        Video video = new Video(title, description, filePath, userId, isPublic);
        videoDao.save(video);
    }
    
    public int uploadVideoAndGetId(String title, String description, String filePath, int userId, boolean isPublic) {
        Video video = new Video(title, description, filePath, userId, isPublic);
        videoDao.save(video);
        return video.getId();
    }
    
    public void updateVideoFilePath(int videoId, String newFilePath) {
        videoDao.updateVideoFilePath(videoId, newFilePath);
    }

    public List<Video> getAllPublicVideos() {
        return videoDao.findAllPublicVideos();
    }

    public List<Video> getUserVideos(int userId) {
        return videoDao.findUserVideos(userId);
    }

    public List<Video> getUserPublicVideos(int userId) {
        return videoDao.findUserPublicVideos(userId);
    }

    public Video getVideoById(int id) {
        return videoDao.findById(id);
    }
    
    public void deleteVideo(int videoId) {
        videoDao.deleteVideo(videoId);
    }
    
    public void updateVideo(Video video) {
        videoDao.updateVideo(video);
    }

    public void deleteVideoAndFile(int videoId, int requesterId) {
        Video video = videoDao.findById(videoId);
        if (video == null || video.getUserId() != requesterId) {
            return;
        }

        String fileName = video.getFilePath();
        if (fileName == null || fileName.isBlank()) {
            videoDao.deleteVideo(videoId);
            return;
        }

        String uploadDir = ConfigUtil.getProperty("video.upload.dir", "D:/video_uploads");
        Path primaryPath = resolvePath(uploadDir, fileName);

        String legacyDir = ConfigUtil.getProperty("video.upload.legacy.dir");
        Path legacyPath = legacyDir == null || legacyDir.isBlank()
            ? null
            : resolvePath(legacyDir, fileName);

        videoDao.deleteVideo(videoId);

        if (!deleteWithRetry(primaryPath) && legacyPath != null) {
            if (!deleteWithRetry(legacyPath)) {
                scheduleAsyncDeletion(primaryPath);
                scheduleAsyncDeletion(legacyPath);
            }
            return;
        }

        if (!deleteWithRetry(primaryPath)) {
            scheduleAsyncDeletion(primaryPath);
        }
    }

    private void scheduleAsyncDeletion(Path path) {
        FileDeletionManager.schedule(path, ASYNC_DELETE_ATTEMPTS, ASYNC_DELETE_INTERVAL);
    }

    private Path resolvePath(String baseDir, String fileName) {
        if (baseDir == null || baseDir.isBlank()) {
            return Paths.get(fileName).normalize();
        }
        Path basePath = Paths.get(baseDir);
        if (basePath.isAbsolute() || Paths.get(fileName).isAbsolute()) {
            return Paths.get(fileName).isAbsolute()
                ? Paths.get(fileName).normalize()
                : basePath.resolve(fileName).normalize();
        }
        return basePath.resolve(fileName).normalize();
    }

    private boolean deleteSilently(Path path) {
        if (path == null) {
            return false;
        }
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + path + ", reason: " + e.getMessage());
            return false;
        }
    }

    private boolean deleteWithRetry(Path path) {
        if (path == null) {
            return false;
        }
        for (int attempt = 1; attempt <= DELETE_RETRY_TIMES; attempt++) {
            if (deleteSilently(path)) {
                return true;
            }
            try {
                Thread.sleep(DELETE_RETRY_INTERVAL.toMillis());
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.err.println("Unable to delete file after retries: " + path);
        return false;
    }
}

