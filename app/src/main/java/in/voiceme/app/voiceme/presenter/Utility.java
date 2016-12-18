package in.voiceme.app.voiceme.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {
    public static void saveFiles(Context c){
        SharedPreferences prefs = c.getSharedPreferences("name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email","yar@mail.ru");
        editor.putString("name", "User User");
        editor.putString("bDay", "12/12/2014");
        editor.putString("position", "12.123,13.3456");
        editor.apply();
    }

    public String formatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("mm/DD/yyyy", Locale.getDefault());
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

}