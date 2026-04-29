package com.ecommerce.campusai.entity;

public class Notice {
    private Long id;
    private String title;    // 公告标题
    private String content;  // 公告内容
    private String createTime; // 创建时间

    // getter 和 setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}