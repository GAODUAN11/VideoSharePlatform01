<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.videoshareplatform01.model.Video" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.categoryName} - 视频分享平台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>视频分享平台</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/home">首页</a>
                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                        <a href="${pageContext.request.contextPath}/profile">个人中心</a>
                        <a href="${pageContext.request.contextPath}/upload">上传视频</a>
                        <a href="${pageContext.request.contextPath}/logout">退出登录</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login">登录</a>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
    </header>
    
    <main>
        <div class="container">
            <h2>${param.categoryName} 视频</h2>
            
            <div class="video-grid">
                <c:choose>
                    <c:when test="${not empty videos && fn:length(videos) > 0}">
                        <c:forEach items="${videos}" var="video">
                            <div class="video-card">
                                <a href="${pageContext.request.contextPath}/video?id=${video.id}">
                                    <video width="100%" height="200" controls>
                                        <source src="${pageContext.request.contextPath}/uploads/${video.filePath}" type="video/mp4">
                                        您的浏览器不支持视频播放。
                                    </video>
                                </a>
                                <div class="video-info">
                                    <h3><a href="${pageContext.request.contextPath}/video?id=${video.id}">${video.title}</a></h3>
                                    <p>${video.description}</p>
                                    <div class="video-meta">
                                        <span>上传时间: ${video.uploadTime}</span>
                                        <c:if test="${not empty video.categories}">
                                            <div class="video-categories">
                                                <c:forEach items="${video.categories}" var="category">
                                                    <span class="category-tag">${category}</span>
                                                </c:forEach>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>该分类下暂无视频。</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>
</body>
</html>