
package by.app.instagram.model.vk;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ErrorAccessToken {

    @SerializedName("code")
    private Long mCode;

    @SerializedName("error_message")
    private String mErrorMessage;

    @SerializedName("error_type")
    private String mErrorType;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    public String getErrorType() {
        return mErrorType;
    }

    public void setErrorType(String errorType) {
        mErrorType = errorType;
    }

}
