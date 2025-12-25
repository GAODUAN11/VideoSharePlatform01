<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>视频分类 - 视频分享平台</title>
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
            <h2>视频分类</h2>
            
            <div class="category-grid">
                <c:choose>
                    <c:when test="${not empty categories && fn:length(categories) > 0}">
                        <c:forEach items="${categories}" var="category">
                            <div class="category-card">
                                <a href="${pageContext.request.contextPath}/category?name=${category}">
                                    <h3>${category}</h3>
                                </a>
                                <p><a href="${pageContext.request.contextPath}/category?name=${category}">查看 ${category} 视频</a></p>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p>暂无分类。</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>
</body>
</html>