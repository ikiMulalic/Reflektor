package ikbal.mulalic.reflektor;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {

    private Button cameraButton,posaljiButton;
    private EditText nazivPrijaveEditText,opisPrijaveEditText,lokacijaEditText;
    private Spinner spinner;
    private ArrayAdapter <String> spinnerAdapter;
    private ArrayList<String> listOfItems;
    private ImageView imageView;
    private VideoView videoView;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 1;

    private LinearLayout slideLayoutConstraint;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private TabItem prijava,mojePrijave;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private Bitmap bitmap;
    private GpsTracker gpsTracker;

    public static double getLatitude;
    public static double getLongitude;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if(savedInstanceState!=null)
        {
            videoView.setVideoPath(savedInstanceState.getString("putVidea",""));
            videoView.setVisibility(View.VISIBLE);
            videoView.start();
        }*/

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE }, 101);

            }
        } catch (Exception e){
            e.printStackTrace();
        }



        getLocation();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.reflektor_logo_icon);
        tabLayout = findViewById(R.id.tablayout);
        prijava = findViewById(R.id.prijava);
        mojePrijave = findViewById(R.id.mojePrijave);
        viewPager = findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    public void getLocation(){

       /* if(ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE }, 0);
        }*/
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            getLatitude = gpsTracker.getLatitude();
            getLongitude = gpsTracker.getLongitude();
            System.out.println("Latitude = " + getLatitude + "\t" + " Longitude = " + getLongitude);

        }else{
            gpsTracker.showSettingsAlert();
        }
    }


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            System.out.println("Huuuuuuuuuusain");
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("output");
            //imageView.setImageBitmap(bitmap);
            //imageView.setVisibility(View.VISIBLE);
            System.out.println("=========================================================/1");
            saveImage();
        }
    }*/

    /////////////////////////////////////////////// hussain


    private void takeScreenshot() {
        System.out.println("========================================================1");
        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + UUID.randomUUID().toString() + ".jpg";
            System.out.println("========================================================2");
            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            System.out.println("========================================================3");
            File newFle = new File(mPath);
            System.out.println("=========================================================");
            System.out.println(newFle.getName());

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }


    public void saveImage() {
        System.out.println("========================================================0");
        if (checkPermission()) {
            takeScreenshot();
            Toast.makeText(this, "Image saved successfully", Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE}, 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission) {
                        takeScreenshot();
                    } else {
                        finish();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}