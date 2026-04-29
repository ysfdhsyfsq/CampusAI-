<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>校园活动列表</title>
    <style>
        body {
            font-family: 'Microsoft YaHei', Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            margin: 0;
            padding: 20px;
            min-height: 100vh;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 12px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            padding: 30px;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-size: 28px;
        }
        .activity-card {
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            transition: all 0.3s ease;
            background: #f9f9f9;
        }
        .activity-card:hover {
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            transform: translateY(-2px);
        }
        .activity-title {
            font-size: 20px;
            color: #333;
            margin: 0 0 10px 0;
        }
        .activity-title a {
            color: #667eea;
            text-decoration: none;
        }
        .activity-title a:hover {
            text-decoration: underline;
        }
        .activity-meta {
            display: flex;
            justify-content: space-between;
            color: #666;
            font-size: 14px;
            margin-top: 10px;
        }
        .status {
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
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
            padding: 2px 8px;
            border-radius: 4px;
            font-size: 12px;
            margin-right: 8px;
        }
        .type-lecture { background: #cce5ff; color: #004085; }
        .type-competition { background: #d4edda; color: #155724; }
        .type-club { background: #fff3cd; color: #856404; }
        .progress-bar {
            width: 100%;
            height: 8px;
            background: #e0e0e0;
            border-radius: 4px;
            margin-top: 10px;
            overflow: hidden;
        }
        .progress-fill {
            height: 100%;
            background: linear-gradient(90deg, #667eea, #764ba2);
            transition: width 0.3s ease;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>🎉 校园活动</h1>

    <#list list as act>
        <div class="activity-card">
            <h3 class="activity-title">
                <#if act.type == 'lecture'>
                    <span class="type-tag type-lecture">📚 讲座</span>
                <#elseif act.type == 'competition'>
                    <span class="type-tag type-competition">🏆 比赛</span>
                <#elseif act.type == 'club'>
                    <span class="type-tag type-club">🎭 社团</span>
                </#if>
                <a href="activity-${act.id}.html">${act.title}</a>
            </h3>
            <div class="activity-meta">
                <span>已报名：${(act.enrollNum)!0}/${(act.maxNum)!0} 人</span>
                <span class="status <#if act.status == 1>status-active<#else>status-expired</#if>">
                    <#if act.status == 1>🟢 报名中<#else>🔴 已截止</#if>
                </span>
            </div>
            <div class="progress-bar">
                <div class="progress-fill" style="width: <#if (act.maxNum?default(0)) gt 0>${(act.enrollNum?default(0)) * 100 / (act.maxNum?default(0))}<#else>0</#if>%"></div>
            </div>
        </div>
    </#list>
</div>
</body>
</html>