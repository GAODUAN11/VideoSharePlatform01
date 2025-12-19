package org.example.videoshareplatform01.model;

import java.sql.Timestamp;

public class Video {
    private int id;
    private String title;
    private String description;
    private String filePath;
    private int userId;
    private boolean isPublic;
    private Timestamp uploadTime;

    public Video() {}

    public Video(String title, String description, String filePath, int userId, boolean isPublic) {
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.userId = userId;
        this.isPublic = isPublic;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }
    
    // 添加一个兼容JSP EL表达式的getter方法
    public boolean getPublic() {
        return isPublic;
    }
}