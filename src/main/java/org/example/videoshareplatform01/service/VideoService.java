package org.example.videoshareplatform01.service;

import org.example.videoshareplatform01.dao.VideoDao;
import org.example.videoshareplatform01.dao.VideoDaoImpl;
import org.example.videoshareplatform01.model.Video;

import java.util.List;

public class VideoService {
    private VideoDao videoDao;

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
}