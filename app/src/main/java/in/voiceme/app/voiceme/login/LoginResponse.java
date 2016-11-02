package in.voiceme.app.voiceme.login;

import com.google.gson.annotations.SerializedName;

import in.voiceme.app.voiceme.infrastructure.AbstractResponse;

/*
    EXAMPLE RESPONSE:

    {
        "status": 1
        "info": {
            "id": "1"
            "name": ""
            "location": "Barmer"
            "email": "email@gmail.com2"
            "gender": "Mf2w"
            "user_id": ""
            "dob": "12/02/2011"
            "age": 5
            "imageurl": "https://s3-us-west-2.amazonaws.com/voice-me-images/dribbble_1x.jpg"
            "rrr": "123214"
        }
    }
 */
public class LoginResponse extends AbstractResponse {
    public Info info;

    public class Info {
        String id;
        String name;
        String location;
        String email;
        String gender;
        @SerializedName("user_id")
        String userId;
        @SerializedName("dob")
        String dateOfBirth;
        int age;
        @SerializedName("imageurl")
        String imageUrl;
        String rrr;
    }
}
