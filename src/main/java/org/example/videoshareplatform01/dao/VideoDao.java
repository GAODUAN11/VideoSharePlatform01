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
}