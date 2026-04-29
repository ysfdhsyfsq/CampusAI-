<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${act.title}</title>
    <style>
        body {
            font-family: 'Microsoft YaHei', Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            margin: 0;
            padding: 20px;
            min-height: 100vh;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
            background: white;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            padding: 40px;
        }
        .back-btn {
            display: inline-block;
            margin-bottom: 20px;
            color: #667eea;
            text-decoration: none;
            font-size: 16px;
        }
        .back-btn:hover {
            text-decoration: underline;
        }
        h1 {
            color: #333;
            margin-bottom: 20px;
            font-size: 32px;
        }
        .info-section {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .info-item {
            display: flex;
            margin-bottom: 12px;
            font-size: 16px;
        }
        .info-label {
            font-weight: bold;
            color: #555;
            width: 100px;
            flex-shrink: 0;
        }
        .info-value {
            color: #333;
            flex: 1;
        }
        .content {
            line-height: 1.8;
            color: #444;
            font-size: 16px;
            margin-top: 20px;
        }
        .status-badge {
            display: inline-block;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 14px;
            font-weight: bold;
        }
        .status-active {
            background: #d4edda;
            color: #155724;
        }
        .status-expired {
            background: #f8d7da;
            color: #721c24;
        }
        .type-tag {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 4px;
            font-size: 14px;
            margin-left: 10px;
        }
        .type-lecture { background: #cce5ff; color: #004085; }
        .type-competition { background: #d4edda; color: #155724; }
        .type-club { background: #fff3cd; color: #856404; }
        .progress-section {
            margin-top: 20px;
        }
        .progress-bar {
            width: 100%;
            height: 12px;
            background: #e0e0e0;
            border-radius: 6px;
            overflow: hidden;
            margin-top: 8px;
        }
        .progress-fill {
            height: 100%;
            background: linear-gradient(90deg, #667eea, #764ba2);
        }
    </style>
</head>
<body>
<div class="container">
    <a href="activity-list.html" class="back-btn">← 返回活动列表</a>

    <h1>
        ${act.title}
        <#if act.type == 'lecture'>
            <span class="type-tag type-lecture">📚 讲座</span>
        <#elseif act.type == 'competition'>
            <span class="type-tag type-competition">🏆 比赛</span>
        <#elseif act.type == 'club'>
            <span class="type-tag type-club">🎭 社团</span>
        </#if>
    </h1>

    <div class="info-section">
        <div class="info-item">
            <span class="info-label">报名情况：</span>
            <span class="info-value">${(act.enrollNum)!0} / ${(act.maxNum)!0} 人</span>
        </div>
        <div class="info-item">
            <span class="info-label">活动状态：</span>
            <span class="info-value">
                <span class="status-badge <#if act.status == 1>status-active<#else>status-expired</#if>">
                    <#if act.status == 1>🟢 报名中<#else>🔴 已截止</#if>
                </span>
            </span>
        </div>
    </div>

    <div class="progress-section">
        <div class="info-label">报名进度</div>
        <div class="progress-bar">
            <div class="progress-fill" style="width: <#if (act.maxNum?default(0)) gt 0>${(act.enrollNum?default(0)) * 100 / (act.maxNum?default(0))}<#else>0</#if>%"></div>
        </div>
    </div>

    <div class="content">
        <h3>活动详情</h3>
        <p>${act.content}</p>
    </div>
</div>
</body>
</html>