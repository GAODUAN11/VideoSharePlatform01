<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人中心 - 视频分享平台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>视频分享平台</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/home">首页</a>
                <a href="${pageContext.request.contextPath}/profile" class="active">个人中心</a>
                <a href="${pageContext.request.contextPath}/upload">上传视频</a>
                <a href="${pageContext.request.contextPath}/logout">退出登录</a>
            </nav>
        </div>
    </header>
    
    <main>
        <div class="container">
            <h2>我的视频</h2>
            
            <c:if test="${empty userVideos}">
                <p>您还没有上传任何视频。</p>
            </c:if>
            
            <div class="video-grid">
                <c:forEach var="video" items="${userVideos}">
                    <div class="video-card">
                        <a href="${pageContext.request.contextPath}/video?id=${video.id}" class="video-link">
                            <div class="video-thumbnail">
                                <video width="100%" height="150" controls>
                                    <source src="${pageContext.request.contextPath}/videos/${video.filePath}" type="video/mp4">
                                    您的浏览器不支持视频播放。
                                </video>
                            </div>
                            <div class="video-info">
                                <h3>${video.title}</h3>
                                <p>${video.description}</p>
                                <span class="privacy-status ${video.getPublic() ? 'public' : 'private'}">
                                    ${video.getPublic() ? '公开' : '私有'}
                                </span>
                            </div>
                        </a>
                        <div class="video-actions">
                            <a href="${pageContext.request.contextPath}/editVideo?id=${video.id}" class="edit-btn">编辑</a>
                            <form action="${pageContext.request.contextPath}/deleteVideo" method="post" onsubmit="return confirm('确定要删除这个视频吗？');">
                                <input type="hidden" name="videoId" value="${video.id}">
                                <button type="submit" class="delete-btn">删除</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>
</body>
</html>