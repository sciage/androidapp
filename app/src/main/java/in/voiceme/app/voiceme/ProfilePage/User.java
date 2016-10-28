package in.voiceme.app.voiceme.ProfilePage;

import java.util.Date;

/**
 * Created by Harish on 7/20/2016.
 */
public class User {

    private int idUserName;
    private String avatarPics;
    private String userNickName;
    private Date userDateOfBirth;
    private String gender;
    private String email;
    private String userLanguage;
    private String userLocation;
    private boolean hasPassword;
    private boolean isLoggedIn;

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public int getIdUserName() {
        return idUserName;
    }

    public void setIdUserName(int idUserName) {
        this.idUserName = idUserName;
    }

    public String getAvatarPics() {
        return avatarPics;
    }

    public void setAvatarPics(String avatarPics) {
        this.avatarPics = avatarPics;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public Date getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public void setUserDateOfBirth(Date userDateOfBirth) {
        this.userDateOfBirth = userDateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
