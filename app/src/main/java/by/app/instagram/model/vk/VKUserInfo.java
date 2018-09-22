package by.app.instagram.model.vk;

import com.google.gson.annotations.SerializedName;

import by.app.instagram.model.Meta;

public class VKUserInfo {

    @SerializedName("data")
    public Data mData;


    @SerializedName("meta")
    private Meta mMeta;


    public Data getmData() {
        return mData;
    }

    public void setmData(Data mData) {
        this.mData = mData;
    }

    public Meta getmMeta() {
        return mMeta;
    }

    public void setmMeta(Meta mMeta) {
        this.mMeta = mMeta;
    }
}
