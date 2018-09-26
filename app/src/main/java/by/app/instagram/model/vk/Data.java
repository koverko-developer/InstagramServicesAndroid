
package by.app.instagram.model.vk;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("bio")
    public String mBio;
    @SerializedName("counts")
    public Counts mCounts;
    @SerializedName("full_name")
    public String mFullName;
    @SerializedName("id")
    public String mId;
    @SerializedName("is_business")
    public Boolean mIsBusiness;
    @SerializedName("profile_picture")
    public String mProfilePicture;
    @SerializedName("username")
    public String mUsername;
    @SerializedName("website")
    public String mWebsite;

    public String getBio() {
        return mBio;
    }

    public void setBio(String bio) {
        mBio = bio;
    }

    public Counts getmCounts() {
        return mCounts;
    }

    public void setmCounts(Counts mCounts) {
        this.mCounts = mCounts;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Boolean getIsBusiness() {
        return mIsBusiness;
    }

    public void setIsBusiness(Boolean isBusiness) {
        mIsBusiness = isBusiness;
    }

    public String getProfilePicture() {
        return mProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        mProfilePicture = profilePicture;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

}
