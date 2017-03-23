package com.caoyujie.basestorehouse.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by caoyujie on 17/3/23.
 */

public class MeinvPcture implements Parcelable {
    private String digest;
    private String img;
    private String imgsrc;
    private String pixel;

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.digest);
        dest.writeString(this.img);
        dest.writeString(this.imgsrc);
        dest.writeString(this.pixel);
    }

    protected MeinvPcture(Parcel in) {
        this.digest = in.readString();
        this.img = in.readString();
        this.imgsrc = in.readString();
        this.pixel = in.readString();
    }

    public static final Parcelable.Creator<MeinvPcture> CREATOR = new Parcelable.Creator<MeinvPcture>()
    {
        public MeinvPcture createFromParcel(Parcel in)
        {
            return new MeinvPcture(in);
        }

        public MeinvPcture[] newArray(int size)
        {
            return new MeinvPcture[size];
        }
    };
}

