package in.voiceme.app.voiceme.ProfilePage;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.infrastructure.Account;
import in.voiceme.app.voiceme.infrastructure.BaseAuthenticatedActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.services.PostsModel;
import rx.android.schedulers.AndroidSchedulers;

public class ProfileActivity extends BaseAuthenticatedActivity implements View.OnClickListener {
    private static final int REQUEST_SELECT_IMAGE = 100;
    private CircleImageView avatarView;
    private View avatarProgressFrame;
    private File tempOutputFile; //storing the image temporarily while we crop it.

    // keep track of state of the activity
    private static final int STATE_VIEWING = 1;
    private static final int STATE_EDITING = 2;

    // activity gets destroyed during rotation. we can save the state of the activity before its destroyed
    private static final String BUNDLE_STATE = "BUNDLE_STATE";

    private int currentState;
    // keep track of contextual action bar
    private ActionMode editProfileActionMode;

    private EditText username;
    private EditText about;

    private TextView age;
    private TextView gender;
    private TextView location;

    @Override
    protected void onVoicemeCreate(Bundle savedState) {
        setContentView(R.layout.activity_profile);
        setNavDrawer(new MainNavDrawer(this));

        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }


        avatarView = (CircleImageView) findViewById(R.id.image);
        avatarProgressFrame = findViewById(R.id.activity_profile_avatarProgressFrame);
        tempOutputFile = new File(getExternalCacheDir(), "temp-image.jpg");

        username = (EditText) findViewById(R.id.name);
        about = (EditText) findViewById(R.id.about);

        age = (TextView) findViewById(R.id.age);
        gender = (TextView) findViewById(R.id.gender);
        location = (TextView) findViewById(R.id.location);

        age.setOnClickListener(this);
        gender.setOnClickListener(this);
        location.setOnClickListener(this);

        avatarView.setOnClickListener(this);

        avatarProgressFrame.setVisibility(View.GONE);






        //   if (isProgressBarVisible)
        //     setProgressBarVisible(true);

    }

    @Subscribe
    public void onUserDetailsUpdated(Account.UserDetailsUpdatedEvent event) {
        getSupportActionBar().setTitle(event.User.getUserNickName());
        Picasso.with(this).load(event.User.getAvatarPics()).into(avatarView);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putInt(BUNDLE_STATE, currentState);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.activity_profile_avatar)
            changeAvatar();
    }

    private void changeAvatar() {
        List<Intent> otherImageCaptureIntent = new ArrayList<>();
        List<ResolveInfo> otherImageCaptureActivities = getPackageManager()
                .queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0); // finding all intents in apps which can handle capture image
        // loop through all these intents and for each of these activities we need to store an intent
        for (ResolveInfo info: otherImageCaptureActivities){ // Resolve info represents an activity on the system that does our work
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name); // declaring explicitly the class where we will go
            // where the picture activity dump the image
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempOutputFile));
            otherImageCaptureIntent.add(captureIntent);
        }

        // above code is only for taking picture and letting it go through another app for cropping before setting to imageview
        // now below is for choosing the image from device

        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");

        Intent chooser = Intent.createChooser(selectImageIntent, "Choose Avatar");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                otherImageCaptureIntent.toArray(new Parcelable[otherImageCaptureActivities.size()]));  // add 2nd para as intent of parcelables.

        startActivityForResult(chooser, REQUEST_SELECT_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK){
            tempOutputFile.delete();
            return;
        }

        if (resultCode == RESULT_OK ){
            if ( requestCode == REQUEST_SELECT_IMAGE) {
                // user selected an image off their device. other condition they took the image and that image is in our tempoutput file
                Uri outputFile;
                Uri tempFileUri = Uri.fromFile(tempOutputFile);
                // if statement will detect if the user selected an image from the device or took an image
                if (data != null && (data.getAction() == null || !data.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE))){
                    //then it means user selected an image off the device
                    // so we can get the Uri of that image using data.getData
                    outputFile = data.getData();
                    // Now we need to do the crop
                } else {
                    // image was out temp file. user took an image using camera
                    outputFile = tempFileUri;
                    // Now we need to do the crop
                }
                startCropActivity(outputFile);

            } else if (requestCode == UCrop.REQUEST_CROP){
                avatarView.setImageResource(0);

                avatarView.setImageURI(Uri.fromFile(tempOutputFile));

                // avatarProgressFrame.setVisibility(View.VISIBLE);
                // bus.post(new Account.ChangeAvatarRequest(Uri.fromFile(tempOutputFile)));
            }

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.activity_profile_menuEdit) {
            changeState(STATE_EDITING);
            return true;
        } else if (itemId == R.id.activity_profile_menuChangePassword) {
            //  startActivity(new Intent(this, AppConfigsActivity.class));
            return true;
        }

        return false;
    }

    private void startCropActivity(@NonNull Uri uri) {

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(tempOutputFile));
        uCrop.start(ProfileActivity.this);
    }

    private void changeState (int state){
        // if we switching back to current state, then donot do anything
        if (state == currentState){
            return;
        }

        currentState = state;

        if (state == STATE_VIEWING){
            username.setEnabled(false);
            about.setEnabled(false);
            age.setEnabled(false);
            location.setEnabled(false);
            gender.setEnabled(false);


        } else if (state == STATE_EDITING){
            username.setEnabled(true);
            about.setEnabled(true);
            age.setEnabled(true);
            location.setEnabled(true);
            gender.setEnabled(true);

        } else throw new IllegalArgumentException("invalid state: " + state);

    }

    private void getData() throws Exception {
        ((VoicemeApplication) getApplication()).getWebService()
                .getFollowers("2")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        Log.e("RESPONSE:::", "Size===" + response.size());
                        //     followers.setText(String.valueOf(response.size()));
                    }
                });
    }

//   http://54.164.58.140/posts.php?follower=2

    private class EditProfileActionCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.activity_profile_edit, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();

            if (itemId == R.id.activity_profile_edit_menuDone) {
                // setProgressBarVisible(true);
                /*User user = application.getAuth().getUser();
                user.setUserNickName(userName.getText().toString());
                user.setGender(gender.getText().toString()); */

                changeState(STATE_VIEWING);
                bus.post(new Account.UpdateProfileRequest(
                        username.getText().toString(),
                        gender.getText().toString()
                ));
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (currentState != STATE_VIEWING)
                changeState(STATE_VIEWING);
        }
    }


}