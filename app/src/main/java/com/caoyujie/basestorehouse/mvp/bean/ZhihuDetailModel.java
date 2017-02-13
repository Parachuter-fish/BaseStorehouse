package com.caoyujie.basestorehouse.mvp.bean;

import java.util.List;

/**
 * Created by caoyujie on 17/1/20.
 * 知乎详情model
 */

public class ZhihuDetailModel {
    private String body;
    private String title;
    private String image;
    private String id;
    private List<String> css;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    @Override
    public String toString() {
        return "ZhihuDetailModel{" +
                "body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", id='" + id + '\'' +
                ", css=" + css +
                '}';
    }
}
