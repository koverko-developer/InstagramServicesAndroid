package by.app.instagram.model;

import com.google.gson.annotations.SerializedName;

import by.app.instagram.model.vk.ErrorAccessToken;

public class ResponseApi {

    @SerializedName("type")
    private String type;

    @SerializedName("code")
    private int code;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
