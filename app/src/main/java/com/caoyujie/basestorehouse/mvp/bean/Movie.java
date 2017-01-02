package com.caoyujie.basestorehouse.mvp.bean;

import java.util.List;

/**
 * Created by caoyujie on 16/12/15.
 * 网络请求实体类
 */
public class Movie{
    private int count;
    private int start;
    private int total;
    private List<Subjects> subjects;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Subjects> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subjects> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "count=" + count +
                ", start=" + start +
                ", total=" + total +
                ", subjects=" + subjects +
                '}';
    }

    public class Subjects{
        private List<String> genres;
        private String title;
        private List<Casts> casts;
        private Rating rating;
        private int collect_count;
        private String original_title;
        private String subtype;
        private String year;
        private String alt;
        private String id;
        private List<Directors> directors;
        private Images images;

        public List<String> getGenres() {
            return genres;
        }

        public void setGenres(List<String> genres) {
            this.genres = genres;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Casts> getCasts() {
            return casts;
        }

        public void setCasts(List<Casts> casts) {
            this.casts = casts;
        }

        public Rating getRating() {
            return rating;
        }

        public void setRating(Rating rating) {
            this.rating = rating;
        }

        public int getCollect_count() {
            return collect_count;
        }

        public void setCollect_count(int collect_count) {
            this.collect_count = collect_count;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getSubtype() {
            return subtype;
        }

        public void setSubtype(String subtype) {
            this.subtype = subtype;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Directors> getDirectors() {
            return directors;
        }

        public void setDirectors(List<Directors> directors) {
            this.directors = directors;
        }

        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "Subjects{" +
                    "genres=" + genres +
                    ", title='" + title + '\'' +
                    ", casts=" + casts +
                    ", rating=" + rating +
                    ", collect_count=" + collect_count +
                    ", original_title='" + original_title + '\'' +
                    ", subtype='" + subtype + '\'' +
                    ", year='" + year + '\'' +
                    ", alt='" + alt + '\'' +
                    ", id='" + id + '\'' +
                    ", directors=" + directors +
                    ", images=" + images +
                    '}';
        }
    }

    public class Rating{
        private String max;
        private String average;
        private String stars;
        private String min;

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }

        public String getStars() {
            return stars;
        }

        public void setStars(String stars) {
            this.stars = stars;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        @Override
        public String toString() {
            return "Rating{" +
                    "max='" + max + '\'' +
                    ", average='" + average + '\'' +
                    ", stars='" + stars + '\'' +
                    ", min='" + min + '\'' +
                    '}';
        }
    }

    public class Casts{
        private String alt;
        private String name;
        private String id;
        private Avatars avatars;

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Avatars getAvatars() {
            return avatars;
        }

        public void setAvatars(Avatars avatars) {
            this.avatars = avatars;
        }

        @Override
        public String toString() {
            return "Casts{" +
                    "alt='" + alt + '\'' +
                    ", name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    ", avatars=" + avatars +
                    '}';
        }
    }

    public class Avatars{
        private String small;
        private String large;
        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        @Override
        public String toString() {
            return "Avatars{" +
                    "small='" + small + '\'' +
                    ", large='" + large + '\'' +
                    ", medium='" + medium + '\'' +
                    '}';
        }
    }

    public class Directors{
        private String alt;
        private Avatars avatars;
        private String name;
        private String id;

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public Avatars getAvatars() {
            return avatars;
        }

        public void setAvatars(Avatars avatars) {
            this.avatars = avatars;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Directors{" +
                    "alt='" + alt + '\'' +
                    ", avatars=" + avatars +
                    ", name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }

    public class Images{
        private String small;
        private String large;
        private String medium;

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        @Override
        public String toString() {
            return "Images{" +
                    "small='" + small + '\'' +
                    ", large='" + large + '\'' +
                    ", medium='" + medium + '\'' +
                    '}';
        }
    }
}
