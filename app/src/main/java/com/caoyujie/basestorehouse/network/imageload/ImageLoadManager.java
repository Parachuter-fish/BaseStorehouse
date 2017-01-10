package com.caoyujie.basestorehouse.network.imageload;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;

/**
 * Created by caoyujie on 16/12/13.
 * 图片加载工具类
 */
public class ImageLoadManager {
    private static ImageLoadManager INSTANCE;

    private ImageLoadManager(){}

    public static ImageLoadManager newInstance(){
        if(INSTANCE == null){
            INSTANCE = new ImageLoadManager();
        }
        return INSTANCE;
    }

    /**
     * 加载图片
     */
    public void loadImage(Context context, String url, ImageView view){
        Glide.with(context).load(url).crossFade().into(view);
    }

    /**
     * 加载圆角图片
     */
    public void loadImage(Context context, String url, ImageView view , int radius){
        Glide.with(context).load(url).crossFade().transform(new GlideRoundTransform(context,radius)).into(view);
    }

    /**
     * 加载图片
     * options: 参数内部类
     */
    public void loadImage(Context context, String url, ImageView view, Options options){
        DrawableTypeRequest<String> load = Glide.with(context).load(url);
        if(options != null){
            //设置默认图片
            if(options.getDefaultImage() != null){
                load.placeholder(options.getDefaultImage());
            }else if(options.getDefaultImageId() != 0){
                load.placeholder(options.getDefaultImageId());
            }
            //设置下载失败图片
            if(options.getErrorImage() != null){
                load.error(options.getErrorImage());
            }else if(options.getErrorImageId() != 0){
                load.error(options.getErrorImageId());
            }
            //设置是否禁止内存缓存
            if(options.skipMemoryCache){
                load.skipMemoryCache(options.skipMemoryCache);
            }
            //设置磁盘缓存类型
            if(options.getDiskCacheStrategy() != null){
                load.diskCacheStrategy(options.getDiskCacheStrategy());
            }
            //设置动画
            if(options.getAnimator() != null){
                load.animate(options.getAnimator());
            }else if(options.getAnimationId() != 0){
                load.animate(options.getAnimationId());
            }else{
                //淡入淡出的默认过度动画
                load.crossFade();
            }
            //设置图片圆角
            if(options.getGlideRoundTransform() != null ){
                load.transform(options.getGlideRoundTransform());
            }
            load.into(view);
        }
    }

    /**
     * 清除内存中的缓存 必须在UI线程中调用
     */
    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清除磁盘中的缓存 必须在后台线程中调用，建议同时clearMemory()
     */
    public static void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    /**
     * 图片参数类
     */
    public static class Options{
        private int defaultImageId;
        private int errorImageId;
        private Drawable defaultImage;
        private Drawable errorImage;
        private boolean skipMemoryCache;    //禁止内存缓存 true为禁止
        private DiskCacheStrategy diskCacheStrategy;     //缓存的类型 磁盘缓存全部 DiskCacheStrategy.ALL，磁盘禁止缓存DiskCacheStrategy.NONE
        private ViewPropertyAnimation.Animator animator;          //动画资源
        private int animationId;            //动画资源
        private GlideRoundTransform glideRoundTransform;        //圆角转换

        public int getDefaultImageId() {
            return defaultImageId;
        }

        public Options setDefaultImageId(int defaultImageId) {
            this.defaultImageId = defaultImageId;
            return this;
        }

        public int getErrorImageId() {
            return errorImageId;
        }

        public Options setErrorImageId(int errorImageId) {
            this.errorImageId = errorImageId;
            return this;
        }

        public Drawable getDefaultImage() {
            return defaultImage;
        }

        public Options setDefaultImage(Drawable defaultImage) {
            this.defaultImage = defaultImage;
            return this;
        }

        public Drawable getErrorImage() {
            return errorImage;
        }

        public Options setErrorImage(Drawable errorImage) {
            this.errorImage = errorImage;
            return this;
        }

        public boolean isSkipMemoryCache() {
            return skipMemoryCache;
        }

        public Options setSkipMemoryCache(boolean skipMemoryCache) {
            this.skipMemoryCache = skipMemoryCache;
            return this;
        }

        public DiskCacheStrategy getDiskCacheStrategy() {
            return diskCacheStrategy;
        }

        public Options setDiskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
            this.diskCacheStrategy = diskCacheStrategy;
            return this;
        }



        public int getAnimationId() {
            return animationId;
        }

        public Options setAnimationId(int animationId) {
            this.animationId = animationId;
            return this;
        }

        public ViewPropertyAnimation.Animator getAnimator() {
            return animator;
        }

        public void setAnimator(ViewPropertyAnimation.Animator animator) {
            this.animator = animator;
        }

        public GlideRoundTransform getGlideRoundTransform() {
            return glideRoundTransform;
        }

        public Options setGlideRoundTransform(GlideRoundTransform glideRoundTransform) {
            this.glideRoundTransform = glideRoundTransform;
            return this;
        }
    }
}
