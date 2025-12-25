<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>首页 - 视频分享平台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>视频分享平台</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/home" class="active">首页</a>
                <a href="${pageContext.request.contextPath}/profile">个人中心</a>
                <a href="${pageContext.request.contextPath}/upload">上传视频</a>
                <a href="${pageContext.request.contextPath}/logout">退出登录</a>
            </nav>
        </div>
    </header>
    
    <main>
        <div class="container">
            <div class="category-selector">
                <label for="categoryFilter">选择分类:</label>
                <select id="categoryFilter" onchange="filterByCategory()">
                    <option value="">全部视频</option>
                    <c:choose>
                        <c:when test="${not empty allCategories && fn:length(allCategories) > 0}">
                            <c:forEach items="${allCategories}" var="category">
                                <option value="${category}" 
                                    ${param.category eq category ? 'selected' : ''}>
                                    ${category}
                                </option>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                </select>
            </div>
            
            <c:choose>
                <c:when test="${not empty selectedCategory}">
                    <h2>${selectedCategory} 视频</h2>
                </c:when>
                <c:otherwise>
                    <h2>公共视频</h2>
                </c:otherwise>
            </c:choose>
            
            <c:if test="${empty publicVideos}">
                <p>暂无视频。</p>
            </c:if>
            
            <div class="video-grid">
                <c:forEach var="video" items="${publicVideos}">
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
                                <c:if test="${not empty video.categories}">
                                    <div class="video-categories">
                                        <c:forEach items="${video.categories}" var="category">
                                            <span class="category-tag">${category}</span>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </main>
    
    <script>
        function filterByCategory() {
            const category = document.getElementById('categoryFilter').value;
            if (category) {
                window.location.href = '${pageContext.request.contextPath}/home?category=' + encodeURIComponent(category);
            } else {
                window.location.href = '${pageContext.request.contextPath}/home';
            }
        }
    </script>
</body>
</html>