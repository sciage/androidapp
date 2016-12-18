package in.voiceme.app.voiceme.presenter.model.data;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginUser implements Parcelable {

    String email;
    String fullName;
    String birthday;
    String picUrl;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LoginUser(){}
    public LoginUser(String email, String fullName, String birthday, String picUrl) {
        this.email = email;
        this.fullName = fullName;
        this.birthday = birthday;
        this.picUrl = picUrl;
    }

    public int describeContents() {
        return 0;
    }



    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(new String[]{email, fullName, birthday, picUrl});
    }

    public static final Parcelable.Creator<LoginUser> CREATOR = new Parcelable.Creator<LoginUser>() {
        public LoginUser createFromParcel(Parcel in) {
            return new LoginUser(in);
        }

        public LoginUser[] newArray(int size) {
            return new LoginUser[size];
        }
    };

    private LoginUser(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        email = data[0];
        fullName = data[1];
        birthday = data[2];
        picUrl = data[3];
    }
}
