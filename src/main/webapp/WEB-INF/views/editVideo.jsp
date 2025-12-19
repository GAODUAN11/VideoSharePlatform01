<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑视频 - 视频分享平台</title>
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
            <div class="form-container">
                <h2>编辑视频信息</h2>
                
                <form action="${pageContext.request.contextPath}/editVideo" method="post">
                    <input type="hidden" name="videoId" value="${video.id}">
                    
                    <div class="form-group">
                        <label for="title">视频标题:</label>
                        <input type="text" id="title" name="title" value="${video.title}" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="description">视频描述:</label>
                        <textarea id="description" name="description" rows="4">${video.description}</textarea>
                    </div>
                    
                    <button type="submit" class="btn">保存修改</button>
                    <a href="${pageContext.request.contextPath}/profile" class="btn-cancel">取消</a>
                </form>
            </div>
        </div>
    </main>
</body>
</html>