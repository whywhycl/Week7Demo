package com.why.week7demo.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by my on 2016/11/12.
 */

public class Tea implements Parcelable {

    /**
     * id : 8170
     * title : 2015年最后十天，开不开心都过来了。愿你的2016只有好运，没有遗憾。
     * source : 原创
     * description :
     * wap_thumb : http://s1.sns.maimaicha.com/images/2015/12/22/20151222141513_37966_suolue3.jpg
     * create_time : 12月22日14:18
     * nickname : bubu123
     */

    private String id;
    private String title;
    private String source;
    private String description;
    private String wap_thumb;
    private String create_time;
    private String nickname;

    public Tea(){

    }

    protected Tea(Parcel in) {
        id = in.readString();
        title = in.readString();
        source = in.readString();
        description = in.readString();
        wap_thumb = in.readString();
        create_time = in.readString();
        nickname = in.readString();
    }

    public static final Creator<Tea> CREATOR = new Creator<Tea>() {
        @Override
        public Tea createFromParcel(Parcel in) {
            return new Tea(in);
        }

        @Override
        public Tea[] newArray(int size) {
            return new Tea[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWap_thumb() {
        return wap_thumb;
    }

    public void setWap_thumb(String wap_thumb) {
        this.wap_thumb = wap_thumb;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Tea{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", description='" + description + '\'' +
                ", wap_thumb='" + wap_thumb + '\'' +
                ", create_time='" + create_time + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(source);
        dest.writeString(description);
        dest.writeString(wap_thumb);
        dest.writeString(create_time);
        dest.writeString(nickname);
    }
}
