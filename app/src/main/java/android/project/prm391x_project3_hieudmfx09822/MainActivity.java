package android.project.prm391x_project3_hieudmfx09822;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ControlFragment {
    CustomDialog customDialog = new CustomDialog();
    private static final String TAG = MainActivity.class.getName();
    UsersPermission usersPermission = new UsersPermission();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;
    View headerLayout;
    FrmSMS frmSMS;
    FrmCall frmCall;
    FrmAlarm frmAlarm;
    FrmListAlarm frmListAlarm;
    ImageView imgAvarta;
    //get all Components in this activity
    public void getComponents(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        frmSMS = new FrmSMS();
        frmCall = new FrmCall();
        frmAlarm = new FrmAlarm();
        frmListAlarm = new FrmListAlarm();
    }
    //Initiate fragment when user active app
    public void initFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content_layout,fragment);
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.commit();
    }
    //Swap each other fragments on specific container
    public void activeFragmentsUtilities(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //use specific animation when any fragment was used
        ft.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
        ft.replace(R.id.content_layout,fragment);
        ft.commit();
    }
    //Handle open drawer layout by Menu button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.item_add_alarm:
                activeFragmentsUtilities(frmAlarm);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //Handle when user click on Items on Menu Board
    @SuppressLint("NonConstantResourceId")
    public void handleClickItemsListener(){
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                ////Active frmSms layout
                case R.id.item_sms:
                    item.setChecked(true);
                    activeFragmentsUtilities(frmSMS);
                    drawerLayout.closeDrawers();
                    return true;
                //Active frmCall layout
                case R.id.item_call:
                    item.setChecked(true);
                    activeFragmentsUtilities(frmCall);
                    drawerLayout.closeDrawers();
                    return true;
                //Active frmAlarm layout
                case R.id.item_alarm:
                    item.setChecked(true);
                    activeFragmentsUtilities(frmListAlarm);
                    drawerLayout.closeDrawers();
                    return true;
                default:
                    throw new ActivityNotFoundException();
            }
            //return false;
        });
    }
    //get intents from JobService to MainActivity
    private void getBundleService(){
        //Get intent from JobService
        Intent intentAlarm = getIntent();
        int alarmID = intentAlarm.getIntExtra("ALARM_ID",0);
        String alarmSms = intentAlarm.getStringExtra("ALARM_SMS");
        String alarmDesTime = intentAlarm.getStringExtra("ALARM_TIME");
        //Check whether if any alarm sms exists
        if(alarmSms!= null ){
            //Active frmAlarm layout if our alarm time up!
            activeFragmentsUtilities(frmAlarm);
            //put alarm sms to new Bundle and pass it to FrmAlarm
            Bundle showBundle = new Bundle();
            showBundle.putInt("showID",alarmID);
            showBundle.putString("showAlarm",alarmSms);
            showBundle.putString("showDesTime",alarmDesTime);
            frmAlarm.setArguments(showBundle);
            Log.i(TAG,"we got any Alarms now");
            getIntent().removeExtra("ALARM_SMS");
        }else {
            Log.i(TAG,"Not any Alarms yet");
        }
    }
    //Get avarta from Android Gallery
    @SuppressLint("QueryPermissionsNeeded")
    private void getAvartaFromGallery(){
        headerLayout = navigationView.getHeaderView(0);
        imgAvarta = headerLayout.findViewById(R.id.img_avarta);
        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),result -> {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Uri imageUri =result.getData().getData();
                        try {
                            Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                            imgAvarta.setImageBitmap(selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        imgAvarta.setOnClickListener(v->{
            usersPermission.checkReadStoragePermission(this,this);
            Intent intentImagePicker = new Intent(Intent.ACTION_PICK);
            intentImagePicker.setType("image/*");
            if(intentImagePicker.resolveActivity(getPackageManager()) != null){
                resultLauncher.launch(intentImagePicker);
            }
        });
    }
    //Get avarta from Camera
    @SuppressLint("QueryPermissionsNeeded")
    private void getAvarta(){
        headerLayout = navigationView.getHeaderView(0);
        imgAvarta = headerLayout.findViewById(R.id.img_avarta);
        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK && result.getData()!= null){
                        Bundle bundle = result.getData().getExtras();
                        Bitmap capturedImage = (Bitmap)bundle.get("data");
                        imgAvarta.setImageBitmap(capturedImage);
                    }else{
                        Toast.makeText(this,"Cant get image from bundle",Toast.LENGTH_LONG).show();
                    }
                });
        imgAvarta.setOnClickListener(view->{
            usersPermission.checkCameraPermission(this,this);
            Intent intentImageCaptured = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intentImageCaptured.resolveActivity(getPackageManager()) != null){
                resultLauncher.launch(intentImageCaptured);
            }else{
                Toast.makeText(this,"No activity is satisfy",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getComponents();
        setSupportActionBar(toolbar);
        handleClickItemsListener();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        initFragment(frmListAlarm);
        getBundleService();
        //getAvarta();
        getAvartaFromGallery();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public void backFrmSMS() {
        activeFragmentsUtilities(frmSMS);
    }
}