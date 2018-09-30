package by.app.instagram.model.firebase;

import com.google.gson.annotations.SerializedName;

public class Progress {

    @SerializedName("value")
    boolean value;

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
