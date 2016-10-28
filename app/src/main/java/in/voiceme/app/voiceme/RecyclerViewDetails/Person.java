package in.voiceme.app.voiceme.RecyclerViewDetails;

/**
 * Created by Harish on 9/2/2016.
 */
public class Person {
    String name;
    int photoId;

    public Person(String name, int photoId) {
        this.name = name;
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
