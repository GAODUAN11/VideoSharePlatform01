<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>上传视频 - 视频分享平台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header>
        <div class="container">
            <h1>视频分享平台</h1>
            <nav>
                <a href="${pageContext.request.contextPath}/home">首页</a>
                <a href="${pageContext.request.contextPath}/profile">个人中心</a>
                <a href="${pageContext.request.contextPath}/upload" class="active">上传视频</a>
                <a href="${pageContext.request.contextPath}/logout">退出登录</a>
            </nav>
        </div>
    </header>
    
    <main>
        <div class="container">
            <h2>上传视频</h2>
            
            <% if (request.getAttribute("errorMessage") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("errorMessage") %>
                </div>
            <% } %>
            
            <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="title">视频标题:</label>
                    <input type="text" id="title" name="title" required>
                </div>
                
                <div class="form-group">
                    <label for="description">视频描述:</label>
                    <textarea id="description" name="description" rows="4"></textarea>
                </div>
                
                <div class="form-group">
                    <label>视频分类:</label>
                    <div class="category-selection" id="categorySelection">
                        <%
                            @SuppressWarnings("unchecked")
                            List<String> categories = (List<String>) request.getAttribute("categories");
                            if (categories != null && !categories.isEmpty()) {
                                for (String category : categories) {
                        %>
                                    <span class="category-tag selectable" onclick="toggleCategory(this, '<%= category %>')">
                                        <input type="checkbox" name="categories" value="<%= category %>" style="display: none;">
                                        <%= category %>
                                    </span>
                        <%
                                }
                            }
                        %>
                    </div>
                </div>
                
                <div class="form-group">
                    <label for="videoFile">选择视频文件 (最大支持1GB):</label>
                    <input type="file" id="videoFile" name="videoFile" accept="video/*" required>
                </div>
                
                <div class="form-group">
                    <input type="checkbox" id="isPublic" name="isPublic" checked>
                    <label for="isPublic">公开视频</label>
                </div>
                
                <button type="submit" class="btn">上传</button>
            </form>
        </div>
    </main>
    
    <script>
        function toggleCategory(element, categoryValue) {
            // 切换选中状态
            element.classList.toggle('selected');
            
            // 获取隐藏的复选框并切换其状态
            const checkbox = element.querySelector('input[type="checkbox"]');
            checkbox.checked = !checkbox.checked;
        }
    </script>
</body>
</html>