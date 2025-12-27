document.addEventListener('DOMContentLoaded', async function() {
    try {
        const uuid = await getOrCreateUserId();
        if (!uuid) {
            throw new Error('无法获取用户指纹 ID');
        }

        const tag = resolvePageTag();
        if (tag) {
            await reportInterest(uuid, tag);
        }
        await loadVideoAd(uuid, 'video');

    } catch (error) {
        showError(error.message);
    }
});

function resolvePageTag() {
    const url = new URL(window.location.href);
    const pathname = url.pathname.replace(/\/+$/, '');
    const segments = pathname.split('/').filter(Boolean);
    const ctxless = segments.length > 1 ? segments.slice(1) : segments; // 去掉可能的上下文前缀
    // 判断路径段数量是否大于1
    // 如果大于1，则使用slice(1)去掉第一个元素（通常是应用上下文路径）
    // 如果不大于1，则保持原样
    // 例如：['shop', 'home'] → ['home']

    // /home?category=xxx 上报，缺参则不上报
    if (ctxless[0] === 'home') {
        const category = url.searchParams.get('category');
        return category ? category : null;
    }

    // /video... 从页面读取视频分类
    if (ctxless[0] === 'video') {
        // 尝试从页面元素中获取视频分类
        const categoryTags = document.querySelectorAll('.video-categories .category-tag');
        if (categoryTags && categoryTags.length > 0) {
            // 返回第一个分类作为兴趣标签
            return categoryTags[0].textContent.trim();
        }
        return null;
    }

    // 其他不上报
    return null;
}

// 上报兴趣函数
async function reportInterest(userId, tag) {
    if (!userId) {
        throw new Error('缺少用户 ID，已取消上报');
    }
    if (!tag) {
        return;
    }
    const res = await fetch('http://8.136.38.185:8080/ad-site/api/collectInterest', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: userId, tag })
    });

    if (!res.ok) {
        const text = await res.text();
        throw new Error(`上报失败 [${res.status}]: ${text}`);
    }
}

// 加载视频广告函数
async function loadVideoAd(userId, contentType) {
    const res = await fetch(`http://8.136.38.185:8080/ad-site/api/ad?id=${encodeURIComponent(userId)}&contentType=${encodeURIComponent(contentType)}`);

    if (!res.ok) {
        // 即使广告加载失败，也要确保原视频可以正常播放
        console.warn('广告加载失败，直接播放原视频');
        const videoPlayer = document.querySelector('.video-player video');
        if (videoPlayer) {
            videoPlayer.load();
        }
        return;
    }

    // 处理二进制流
    const blob = await res.blob();
    const adVideoUrl = URL.createObjectURL(blob);

    // 获取原始视频源
    const videoElement = document.querySelector('.video-player video');
    const sourceElement = videoElement.querySelector('source');
    const originalVideoUrl = sourceElement.src;

    // 设置组合播放
    setupAdAndVideoPlayback(videoElement, adVideoUrl, originalVideoUrl);
}

// 设置广告和视频的组合播放
function setupAdAndVideoPlayback(videoElement, adVideoUrl, originalVideoUrl) {
    // 首先播放广告
    const sourceElement = videoElement.querySelector('source');
    sourceElement.src = adVideoUrl;
    videoElement.load();
    
    // 监听广告播放结束事件
    videoElement.addEventListener('ended', function onAdEnded() {
        // 移除广告结束事件监听器
        videoElement.removeEventListener('ended', onAdEnded);
        
        // 切换到原视频
        sourceElement.src = originalVideoUrl;
        videoElement.load();
        
        // 自动播放原视频（如果用户之前点击了播放）
        if (!videoElement.paused) {
            videoElement.play().catch(e => console.error('播放原视频时出错:', e));
        }
    });
    
    // 开始播放广告
    videoElement.play().catch(e => console.error('播放广告时出错:', e));
}

// 错误提示函数
function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error';
    errorDiv.textContent = message;
    document.querySelector('.container').appendChild(errorDiv);
}

async function waitForVisitorId(timeoutMs = 5000, intervalMs = 100) {
    const start = Date.now();
    return new Promise((resolve, reject) => {
        const timer = setInterval(() => {
            if (window.visitorId) {
                clearInterval(timer);
                resolve(window.visitorId);
            } else if (Date.now() - start > timeoutMs) {
                clearInterval(timer);
                reject(new Error('无法获取用户指纹 ID'));
            }
        }, intervalMs);
    });
}

async function getOrCreateUserId() {
    // 只等待 FingerprintJS，失败则抛出错误
    return waitForVisitorId();
}