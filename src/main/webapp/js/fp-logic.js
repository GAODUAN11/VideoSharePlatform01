(function() {
    // 1. 检查依赖库
    if (typeof FingerprintJS === 'undefined') {
        console.error('FingerprintJS library not found.');
        return;
    }

    // 2. 初始化并获取指纹
    FingerprintJS.load()
        .then(fp => fp.get())
        .then(result => {
            // 3. 核心功能：将生成的 ID 挂载到全局变量 window.visitorId
            window.visitorId = result.visitorId;
            
            console.log('Fingerprint generated and set to global variable: window.visitorId');
        })
        .catch(error => {
            console.error('Fingerprint generation failed:', error);
        });
})();