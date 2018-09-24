package by.app.instagram.model.pui;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import by.app.instagram.model.fui.UserInfoMedia;

public class PostsInfo {

    @SerializedName("userInfoMedia")
    public UserInfoMedia userInfoMedia;

    @SerializedName("chartArr")
    public List<ChartItem> chartArr;

    public UserInfoMedia getUserInfoMedia() {
        return userInfoMedia;
    }

    public void setUserInfoMedia(UserInfoMedia userInfoMedia) {
        this.userInfoMedia = userInfoMedia;
    }

    public List<ChartItem> getChartArr() {
        return chartArr;
    }

    public void setChartArr(List<ChartItem> chartArr) {
        this.chartArr = chartArr;
    }
}
