package org.example.videoshareplatform01.dao;

import org.example.videoshareplatform01.model.Video;
import org.example.videoshareplatform01.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideoDaoImpl implements VideoDao {

    @Override
    public void save(Video video) {
        String sql = "INSERT INTO videos (title, description, file_path, user_id, is_public, upload_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, video.getTitle());
            stmt.setString(2, video.getDescription());
            stmt.setString(3, video.getFilePath());
            stmt.setInt(4, video.getUserId());
            stmt.setBoolean(5, video.isPublic());
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                video.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Video findById(int id) {
        String sql = "SELECT * FROM videos WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Video video = new Video();
                    video.setId(rs.getInt("id"));
                    video.setTitle(rs.getString("title"));
                    video.setDescription(rs.getString("description"));
                    video.setFilePath(rs.getString("file_path"));
                    video.setUserId(rs.getInt("user_id"));
                    video.setPublic(rs.getBoolean("is_public"));
                    video.setUploadTime(rs.getTimestamp("upload_time"));
                    // 设置视频分类
                    video.setCategories(getVideoCategories(rs.getInt("id")));
                    return video;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Video> findAllPublicVideos() {
        String sql = "SELECT * FROM videos WHERE is_public = true ORDER BY upload_time DESC";
        List<Video> videos = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Video video = new Video();
                video.setId(rs.getInt("id"));
                video.setTitle(rs.getString("title"));
                video.setDescription(rs.getString("description"));
                video.setFilePath(rs.getString("file_path"));
                video.setUserId(rs.getInt("user_id"));
                video.setPublic(rs.getBoolean("is_public"));
                video.setUploadTime(rs.getTimestamp("upload_time"));
                // 设置视频分类
                video.setCategories(getVideoCategories(rs.getInt("id")));
                videos.add(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return videos;
    }

    @Override
    public List<Video> findUserVideos(int userId) {
        String sql = "SELECT * FROM videos WHERE user_id = ? ORDER BY upload_time DESC";
        List<Video> videos = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Video video = new Video();
                    video.setId(rs.getInt("id"));
                    video.setTitle(rs.getString("title"));
                    video.setDescription(rs.getString("description"));
                    video.setFilePath(rs.getString("file_path"));
                    video.setUserId(rs.getInt("user_id"));
                    video.setPublic(rs.getBoolean("is_public"));
                    video.setUploadTime(rs.getTimestamp("upload_time"));
                    // 设置视频分类
                    video.setCategories(getVideoCategories(rs.getInt("id")));
                    videos.add(video);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return videos;
    }

    @Override
    public List<Video> findUserPublicVideos(int userId) {
        String sql = "SELECT * FROM videos WHERE user_id = ? AND is_public = true ORDER BY upload_time DESC";
        List<Video> videos = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Video video = new Video();
                    video.setId(rs.getInt("id"));
                    video.setTitle(rs.getString("title"));
                    video.setDescription(rs.getString("description"));
                    video.setFilePath(rs.getString("file_path"));
                    video.setUserId(rs.getInt("user_id"));
                    video.setPublic(rs.getBoolean("is_public"));
                    video.setUploadTime(rs.getTimestamp("upload_time"));
                    // 设置视频分类
                    video.setCategories(getVideoCategories(rs.getInt("id")));
                    videos.add(video);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return videos;
    }
    
    @Override
    public void updateVideoFilePath(int videoId, String newFilePath) {
        String sql = "UPDATE videos SET file_path = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newFilePath);
            stmt.setInt(2, videoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteVideo(int videoId) {
        // 先删除视频与分类的关联记录
        String deleteVideoCategoriesSql = "DELETE FROM video_categories WHERE video_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement deleteVideoCategoriesStmt = conn.prepareStatement(deleteVideoCategoriesSql)) {
            deleteVideoCategoriesStmt.setInt(1, videoId);
            deleteVideoCategoriesStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // 再删除视频记录
        String sql = "DELETE FROM videos WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, videoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void updateVideo(Video video) {
        String sql = "UPDATE videos SET title = ?, description = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, video.getTitle());
            stmt.setString(2, video.getDescription());
            stmt.setInt(3, video.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void saveVideoCategories(int videoId, List<Integer> categoryIds) {
        // 先删除该视频的现有分类
        String deleteSql = "DELETE FROM video_categories WHERE video_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setInt(1, videoId);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // 添加新的分类
        if (categoryIds != null && !categoryIds.isEmpty()) {
            String insertSql = "INSERT INTO video_categories (video_id, category_id) VALUES (?, ?)";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (Integer categoryId : categoryIds) {
                    insertStmt.setInt(1, videoId);
                    insertStmt.setInt(2, categoryId);
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public List<String> getVideoCategories(int videoId) {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT c.name FROM categories c " +
                     "JOIN video_categories vc ON c.id = vc.category_id " +
                     "WHERE vc.video_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, videoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    categories.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    @Override
    public List<Video> findVideosByCategory(String categoryName) {
        List<Video> videos = new ArrayList<>();
        String sql = "SELECT v.* FROM videos v " +
                     "JOIN video_categories vc ON v.id = vc.video_id " +
                     "JOIN categories c ON c.id = vc.category_id " +
                     "WHERE c.name = ? AND v.is_public = true ORDER BY v.upload_time DESC";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Video video = new Video();
                    video.setId(rs.getInt("id"));
                    video.setTitle(rs.getString("title"));
                    video.setDescription(rs.getString("description"));
                    video.setFilePath(rs.getString("file_path"));
                    video.setUserId(rs.getInt("user_id"));
                    video.setPublic(rs.getBoolean("is_public"));
                    video.setUploadTime(rs.getTimestamp("upload_time"));
                    // 设置视频分类
                    video.setCategories(getVideoCategories(rs.getInt("id")));
                    videos.add(video);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return videos;
    }
    
    @Override
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT name FROM categories ORDER BY name";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    @Override
    public Integer getCategoryIdByName(String categoryName) {
        String sql = "SELECT id FROM categories WHERE name = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}