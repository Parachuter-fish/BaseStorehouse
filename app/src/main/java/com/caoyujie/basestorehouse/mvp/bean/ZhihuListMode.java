package com.caoyujie.basestorehouse.mvp.bean;

import java.util.List;

/**
 * Created by caoyujie on 17/1/15.
 * 知乎日报列表
 */

public class ZhihuListMode {
    private String date;
    private List<Stories> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Stories> getStories() {
        return stories;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "ZhihuListMode{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                '}';
    }

    public class Stories{
        private String title;
        private String ga_prefix;
        private List<String> images;
        private boolean multipic;
        private int type;
        private int id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Stories{" +
                    "title='" + title + '\'' +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", images=" + images +
                    ", multipic=" + multipic +
                    ", type=" + type +
                    ", id=" + id +
                    '}';
        }
    }
}
