package in.voiceme.app.voiceme.main;

/**
 * Created by Harish on 7/31/2016.
 */
public class UserPostDetails {

    private int avatarUrl;
    private String username;
    private String isFeeling;
    private String feeling;
    private String category;
    private String timeStamp;
    private String postMessage;
    private String readMore;
    private int playButton;
    private int likeCounter;


    public UserPostDetails(int avatarUrl,
                           String username,
                           String isFeeling,
                           String feeling,
                           String category,
                           String timeStamp,
                           String postMessage,
                           String readMore,
                           int playButton) {
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.isFeeling = isFeeling;
        this.feeling = feeling;
        this.category = category;
        this.timeStamp = timeStamp;
        this.postMessage = postMessage;
        this.readMore = readMore;
        this.playButton = playButton;
    }

    public int getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(int avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIsFeeling() {
        return isFeeling;
    }

    public void setIsFeeling(String isFeeling) {
        this.isFeeling = isFeeling;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public String getReadMore() {
        return readMore;
    }

    public void setReadMore(String readMore) {
        this.readMore = readMore;
    }

    public int getPlayButton() {
        return playButton;
    }

    public void setPlayButton(int playButton) {
        this.playButton = playButton;
    }


}
