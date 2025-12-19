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
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Video findById(int id) {
        String sql = "SELECT * FROM videos WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Video video = new Video();
                video.setId(rs.getInt("id"));
                video.setTitle(rs.getString("title"));
                video.setDescription(rs.getString("description"));
                video.setFilePath(rs.getString("file_path"));
                video.setUserId(rs.getInt("user_id"));
                video.setPublic(rs.getBoolean("is_public"));
                video.setUploadTime(rs.getTimestamp("upload_time"));
                return video;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<Video> findAllPublicVideos() {
        String sql = "SELECT * FROM videos WHERE is_public = true ORDER BY upload_time DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Video> videos = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Video video = new Video();
                video.setId(rs.getInt("id"));
                video.setTitle(rs.getString("title"));
                video.setDescription(rs.getString("description"));
                video.setFilePath(rs.getString("file_path"));
                video.setUserId(rs.getInt("user_id"));
                video.setPublic(rs.getBoolean("is_public"));
                video.setUploadTime(rs.getTimestamp("upload_time"));
                videos.add(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return videos;
    }

    @Override
    public List<Video> findUserVideos(int userId) {
        String sql = "SELECT * FROM videos WHERE user_id = ? ORDER BY upload_time DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Video> videos = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Video video = new Video();
                video.setId(rs.getInt("id"));
                video.setTitle(rs.getString("title"));
                video.setDescription(rs.getString("description"));
                video.setFilePath(rs.getString("file_path"));
                video.setUserId(rs.getInt("user_id"));
                video.setPublic(rs.getBoolean("is_public"));
                video.setUploadTime(rs.getTimestamp("upload_time"));
                videos.add(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return videos;
    }

    @Override
    public List<Video> findUserPublicVideos(int userId) {
        String sql = "SELECT * FROM videos WHERE user_id = ? AND is_public = true ORDER BY upload_time DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Video> videos = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Video video = new Video();
                video.setId(rs.getInt("id"));
                video.setTitle(rs.getString("title"));
                video.setDescription(rs.getString("description"));
                video.setFilePath(rs.getString("file_path"));
                video.setUserId(rs.getInt("user_id"));
                video.setPublic(rs.getBoolean("is_public"));
                video.setUploadTime(rs.getTimestamp("upload_time"));
                videos.add(video);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return videos;
    }
    
    @Override
    public void updateVideoFilePath(int videoId, String newFilePath) {
        String sql = "UPDATE videos SET file_path = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newFilePath);
            stmt.setInt(2, videoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void deleteVideo(int videoId) {
        String sql = "DELETE FROM videos WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, videoId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void updateVideo(Video video) {
        String sql = "UPDATE videos SET title = ?, description = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, video.getTitle());
            stmt.setString(2, video.getDescription());
            stmt.setInt(3, video.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}