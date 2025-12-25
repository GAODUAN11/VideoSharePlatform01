package org.example.videoshareplatform01.dao;

import org.example.videoshareplatform01.model.Video;

import java.util.List;

public interface VideoDao {
    void save(Video video);
    Video findById(int id);
    List<Video> findAllPublicVideos();
    List<Video> findUserVideos(int userId);
    List<Video> findUserPublicVideos(int userId);
    void updateVideoFilePath(int videoId, String newFilePath);
    void deleteVideo(int videoId);
    void updateVideo(Video video);
    
    // 添加分类相关的方法
    void saveVideoCategories(int videoId, List<Integer> categoryIds);
    List<String> getVideoCategories(int videoId);
    List<Video> findVideosByCategory(String categoryName);
    List<String> getAllCategories();
    Integer getCategoryIdByName(String categoryName);
}