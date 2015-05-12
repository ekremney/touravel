package com.example.touravel.app;

/**
 * Created by gokhancs on 17/03/15.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.async.AsyncChangeEmail;
import com.android.async.AsyncChangePassword;
import com.android.async.AsyncEditProfile;
import com.android.async.AsyncPostAvatar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class SettingsActivity extends Activity {


    protected static EditText tvChangeEmail, tvChangeEmailAgain;
    protected static EditText tvOldPassword, tvNewPassword, tvNewPasswordAgain;
    protected static EditText tvName, tvLocation, tvAboutMe;
    protected static Switch switchService;
    protected Context cnt;
    protected static String selectedIMG, selectedIMG_thumb;
    private static final int SELECT_PHOTO = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footer_layout);
        cnt = getApplicationContext();

        switchService = (Switch) findViewById(R.id.switchBS);
        boolean isBSrunning = false;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BackgroundService.class.getName().equals(service.service.getClassName())) {
                isBSrunning = true;
            }
        }
        switchService.setChecked(isBSrunning);
        switchService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(new Intent(cnt, BackgroundService.class));
                    Toast.makeText(cnt, "Service is started.", Toast.LENGTH_SHORT).show();
                }
                else {
                    stopService(new Intent(cnt, BackgroundService.class));
                    Toast.makeText(cnt, "Service is stopped.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvChangeEmail = (EditText) findViewById(R.id.tvChangeEmail);
        tvChangeEmailAgain = (EditText) findViewById(R.id.tvChangeEmailAgain);
        tvOldPassword = (EditText) findViewById(R.id.textOldPassword);
        tvNewPassword = (EditText) findViewById(R.id.textNewPassword);
        tvNewPasswordAgain = (EditText) findViewById(R.id.textNewPasswordV);
        tvName = (EditText) findViewById(R.id.textName);
        tvLocation = (EditText) findViewById(R.id.textLocation);
        tvAboutMe = (EditText) findViewById(R.id.textAboutMe);

        tvName.setText(SplashScreen.user.getName());
        tvLocation.setText(SplashScreen.user.getLocation());
        tvAboutMe.setText(SplashScreen.user.getLocation());

    }

    @Override
    public void onResume() {
        super.onResume();

        tvName.setText(SplashScreen.user.getName());
        tvLocation.setText(SplashScreen.user.getLocation());
        tvAboutMe.setText(SplashScreen.user.getAbout_me());
    }

    public static void clearEmailForm()
    {
        tvChangeEmail.setText("");
        tvChangeEmailAgain.setText("");
    }

    public static void clearPasswordForm()
    {
        tvOldPassword.setText("");
        tvNewPassword.setText("");
        tvNewPasswordAgain.setText("");
    }

    public static void clearEditProfileForm()
    {
        tvName.setText("");
        tvLocation.setText("");
        tvAboutMe.setText("");
    }

    public void btnOnClick(View v) {

        switch(v.getId())
        {
            case R.id.logoutButton:
                SplashScreen.auth = null;
                Intent logout_intent = new Intent(this, LoginActivity.class);
                startActivity(logout_intent);
                finish();
                break;

            case R.id.searchButton:
                Intent intent = new Intent(this, ListUsersActivity.class);
                startActivity(intent);
                break;
            case R.id.btnChangeEmail:
            {
                String url = getResources().getString(R.string.url_change_email);
                String email = tvChangeEmail.getText().toString();
                String emailV = tvChangeEmailAgain.getText().toString();

                new AsyncChangeEmail().execute(url, SplashScreen.auth, email, emailV);
                break;
            }
            case R.id.btnChangePassword:
            {
                String url = getResources().getString(R.string.url_change_password);
                String oldPassword = tvOldPassword.getText().toString();
                String newPassword = tvNewPassword.getText().toString();
                String newPasswordAgain = tvNewPasswordAgain.getText().toString();

                new AsyncChangePassword().execute(url, SplashScreen.auth, oldPassword, newPassword, newPasswordAgain);
                break;
            }
            case R.id.btnEditProfile:
            {
                String url = getResources().getString(R.string.url_edit_profile);
                String name = tvName.getText().toString();
                String location = tvLocation.getText().toString();
                String aboutMe = tvAboutMe.getText().toString();

                new AsyncEditProfile().execute(url, SplashScreen.auth, name, location, aboutMe);
                break;
            }
            case R.id.uploadAvatar:
            {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
            default:
                break;


        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:

                String url = getResources().getString(R.string.url_post_avatar);

                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    Bitmap yourSelectedImage = null, resized = null, selectedIMG_thumb_data = null;
                    try
                    {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                        double ratio = (double)320/(double)yourSelectedImage.getWidth();
                        double ratio_thumb = (double)240/(double)yourSelectedImage.getWidth();
                        Log.e("ratio", "" + ratio);
                        if (ratio < 1) {
                            Log.e("whaddup", "biatcheez");
                            resized = Bitmap.createScaledBitmap(yourSelectedImage, (int) (yourSelectedImage.getWidth() * ratio), (int) (yourSelectedImage.getHeight() * ratio), true);
                        }
                        else {
                            resized = yourSelectedImage;
                        }
                        selectedIMG_thumb_data = Bitmap.createScaledBitmap(yourSelectedImage, (int) (yourSelectedImage.getWidth() * ratio_thumb), (int) (yourSelectedImage.getHeight() * ratio_thumb), true);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        resized.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                        selectedIMG_thumb_data.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream2);
                        byte[] byteArray2 = byteArrayOutputStream.toByteArray();

                        selectedIMG = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        selectedIMG_thumb = Base64.encodeToString(byteArray2, Base64.DEFAULT);

                        new AsyncPostAvatar().execute(url, SplashScreen.auth, selectedIMG, selectedIMG_thumb);

                    }
                    catch (IOException e)
                    {
                        Log.e("capture img", ""+e);
                    }


                }
            default:
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
