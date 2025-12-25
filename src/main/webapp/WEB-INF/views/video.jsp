<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${video.title} - 视频分享平台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>视频分享平台</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/home">首页</a>
                <c:if test="${currentUser != null}">
                    <a href="${pageContext.request.contextPath}/profile">个人中心</a>
                    <a href="${pageContext.request.contextPath}/upload">上传视频</a>
                    <a href="${pageContext.request.contextPath}/logout">退出登录</a>
                </c:if>
                <c:if test="${currentUser == null}">
                    <a href="${pageContext.request.contextPath}/login">登录</a>
                </c:if>
            </nav>
        </div>
    </header>
    
    <main>
        <div class="container">
            <div class="video-detail">
                <div class="video-player">
                    <video width="100%" height="500" controls autoplay>
                        <source src="${pageContext.request.contextPath}/videos/${video.filePath}" type="video/mp4">
                        您的浏览器不支持视频播放。
                    </video>
                </div>
                
                <div class="video-info-detail">
                    <h2>${video.title}</h2>
                    <p class="video-description">${video.description}</p>
                    
                    <c:if test="${not empty video.categories}">
                        <div class="video-categories">
                            <h4>分类:</h4>
                            <c:forEach items="${video.categories}" var="category">
                                <a href="${pageContext.request.contextPath}/category?name=${category}" class="category-tag">${category}</a>
                            </c:forEach>
                        </div>
                    </c:if>
                    
                    <div class="video-meta">
                        <c:if test="${!video.getPublic()}">
                            <span class="privacy-status private">私有视频</span>
                        </c:if>
                        <c:if test="${video.getPublic()}">
                            <span class="privacy-status public">公开视频</span>
                        </c:if>
                        <span class="upload-time">上传时间: ${video.uploadTime}</span>
                    </div>
                </div>
            </div>
        </div>
    </main>
</body>
</html>