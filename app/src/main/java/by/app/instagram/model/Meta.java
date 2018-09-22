package by.app.instagram.model;

import com.google.gson.annotations.SerializedName;

import by.app.instagram.model.vk.ErrorAccessToken;

public class Meta {

    @SerializedName("meta")
    private ErrorAccessToken meta;

    public ErrorAccessToken getMeta() {
        return meta;
    }

    public void setMeta(ErrorAccessToken meta) {
        this.meta = meta;
    }
}
