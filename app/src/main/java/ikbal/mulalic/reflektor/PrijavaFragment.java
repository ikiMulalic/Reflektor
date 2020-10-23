package ikbal.mulalic.reflektor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


import ikbal.mulalic.reflektor.model.Report;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PrijavaFragment extends Fragment{



    public static final MediaType JSON
            = MediaType.parse("image/png");
    private Spinner spinner;
    private ArrayList<String> listOfItems,descriptionOfItems;
    private ArrayAdapter<String> spinnerAdapter;
    private VideoView videoView;
    private ImageView imageView;
    private ImageButton fotografijaButton,videoButton,saveButton;
    private Button sendButton;
    private EditText opisPrijaveEditText,lokacijaEditText;
    private TextView textView;
    private Context context;
    private Uri uri;
    private String currentPhotoPath,currentVideoPath,categoryText,descriptionText,locationText;
    private Report report = new Report();
    private Bitmap bitmapPhoto;
    private ArrayList<Report> listOfReports;

    private final String url = "https://reflektor.live/reports/create";

    SharedPreferences sharedPreferences;
    static  final int REQUEST_TAKE_PHOTO = 1;
    static  final int REQUEST_VIDEO_CAPTURE = 2;

    private File nekakav;
    private View view;
    private Uri file = null;
    private SharedPreferences.Editor editor;
    private boolean checkIfFormIsValid = false;
    private boolean isSent = false;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;



    public PrijavaFragment() {
        // Required empty public constructor
    }

    public static PrijavaFragment newInstance(String param1, String param2) {
        PrijavaFragment fragment = new PrijavaFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        context = getContext();
        categoryText = "";
        currentVideoPath = null;
        currentPhotoPath = null;
        
        view =inflater.inflate(R.layout.fragment_prijava, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        textView = (TextView) view.findViewById(R.id.textView);



        descriptionOfItems = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("ikbal.mukakic.reflektor", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

        descriptionOfItems.add("");
        descriptionOfItems.add("Podjela materijalne koristi glasačima sa namjerom da ih se navede da" +
                " glasaju za ili protiv određenog kandidata/stranke, da uopšte ne glasaju ili da glasaju u " +
                "određenom smislu (npr. politička stranka ili kandidat dodjeljuju novac, promotivni " +
                "materijal u vidu paketa sa životnim namirnicama ili osnovnim higijenskim priborom, itd.) ");

        descriptionOfItems.add("Državni službenici, kandidati i stranke na vlasti koji koriste svoj" +
                " položaj i veze u institucijama vlasti u svrhu izborne kampanje (npr. korišćenje" +
                " službenih automobila i opreme, zaposlenih u javnom sektoru, javnih funkcija i " +
                "institucija u svrhu izborne kampanje, javna pojavljivanja izabranih zvaničnika u " +
                "svrhu promocije kandidata i/ili stranke, i slično)");

        descriptionOfItems.add("Ukoliko neko silom, ozbiljnom prijetnjom, prinudom, podmićivanjem," +
                " obmanom, korišćenjem teškog imovinskog stanja ili na drugi protivpravan način" +
                " utiče na birača da na izborima glasa za ili protiv određenog prijedloga, da uopšte" +
                " ne glasa ili da glasa u određenom smislu (npr. uslovljavanje otkazima, pravljenje " +
                "spiskova zaposlenih za glasanje za određenu političku opciju, smanjenjem penzija/plata i slično)");

        descriptionOfItems.add("Nova zaposlenja u zamjenu za glas u periodu prije, tokom i nakon" +
                " izborne kampanje u javnim institucijama i javnim preduzećima (npr. povećano " +
                "zapošljavanje u vrijeme od raspisivanja izbora ili sl.)");

        descriptionOfItems.add("U okviru informativnih programa elektronskih medija tokom informisanja" +
                " o redovnim aktivnostima funkcionera na svim nivoima vlasti nije dopušteno navođenje" +
                " njihove kandidature na izborima i stranačke pripadnosti, kad god se radi o " +
                "aktivnostima koje proizilaze iz zakonom utvrđenog djelokruga organa kojima" +
                " pripadaju (npr. gradonačelnik se pojavljuje u dnevniku u svojstvu kandidata i sl.)");

        descriptionOfItems.add("Postavljanje oglasa/plakata/postera, pisanje imena ili slogana koji " +
                "su u vezi sa izbornom kampanjom od strane političkih stranka/kandidata unutar ili" +
                " na zgradama u kojima su smješteni organi vlasti na svim nivoima, javna preduzeća, " +
                "javne ustanove i mjesne zajednice, na vjerskim objektima, na javnim putevima i " +
                "javnim površinama. Ovo se ne odnosi na mjesta predviđena za plakatiranje i " +
                "oglašavanje. (npr. plakat političke stranke/kandidata u pošti, opštini i slično)");

        descriptionOfItems.add("Različite aktivnosti političkih stranaka/kandidata koje se mogu" +
                " protumačiti kao izborna kampanja prije njenog zvaničnog početka " +
                "(npr. oglašavanje na društvenim mrežama, javnim tribinama, dijeljenje letaka i sl.)");

        descriptionOfItems.add("Javne ustanove/preduzeća/institucije vrše otpis dugova građanima," +
                " uključujući račune za utrošenu električnu energiju, vodu, kao i račune za sve " +
                "vrste javnih usluga ili nude npr. besplatne zdravstvene preglede i slično." +
                " Ovdje je uključeno i povećanje penzija, socijalnih naknada i slično.");

        descriptionOfItems.add("Sve ostale pojave koje bi mogle podrazumijevati zloupotrebu javnih " +
                "sredstava (finansijsku, institucionalnu, regulatornu) u izborne svrhe, bilo u korist" +
                " ili protiv neke političke opcije, kao i druge vrste manipulacija izbornog procesa.");


        listOfItems = new ArrayList<>();


        listOfItems.add("Izaberite kategoriju");
        listOfItems.add("Kupovina glasova");
        listOfItems.add("Korišćenje javnih resursa u izborne svrhe");
        listOfItems.add("Pritisci na birače");
        listOfItems.add("Predizborno zapošljavanje");
        listOfItems.add("Medijsko predstavljanje");
        listOfItems.add("Oglašavanje na zabranjenim mjestima");
        listOfItems.add("Preuranjena kampanja");
        listOfItems.add("Javne usluge u zamjenu za glas");
        listOfItems.add("Ostalo");

        spinnerAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,listOfItems);

        videoView = (VideoView)view.findViewById(R.id.videoView);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        fotografijaButton = (ImageButton)view.findViewById(R.id.fotografijaButton);
        sendButton = (Button)view.findViewById(R.id.posaljiButton);
        videoButton = (ImageButton) view.findViewById(R.id.videoButton);

        opisPrijaveEditText = (EditText) view.findViewById(R.id.opisPrijaveEditText);
        lokacijaEditText = (EditText) view.findViewById(R.id.lokacijaEditText);


        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                        textView.setText(descriptionOfItems.get(0));
                        break;
                    case 1:
                        textView.setText(descriptionOfItems.get(1));
                        break;
                    case 2:
                        textView.setText(descriptionOfItems.get(2));
                        break;
                    case 3:
                        textView.setText(descriptionOfItems.get(3));
                        break;
                    case 4:
                        textView.setText(descriptionOfItems.get(4));
                        break;
                    case 5:
                        textView.setText(descriptionOfItems.get(5));
                        break;
                    case 6:
                        textView.setText(descriptionOfItems.get(6));
                        break;
                    case 7:
                        textView.setText(descriptionOfItems.get(7));
                        break;
                    case 8:

                        textView.setText(descriptionOfItems.get(8));
                        break;
                    case 9:
                        textView.setText(descriptionOfItems.get(9));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.start();
            }
        });



        fotografijaButton.setOnClickListener(new View.OnClickListener() {
/*
            @RequiresApi(api = Build.VERSION_CODES.O)
*/
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);




                    // Ensure that there's a camera activity to handle the intent
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();


                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            uri = FileProvider.getUriForFile(getContext(), "ikbal.mulalic.reflektor.provider", photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                        }

                    }


                }
        });


        opisPrijaveEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                opisPrijaveEditText.setHint("");
            }
        });

        lokacijaEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                lokacijaEditText.setHint("");
            }
        });
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                // Ensure that there's a camera activity to handle the intent
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File videoFile = null;
                    try {
                        videoFile = createVideoFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }

                    // Continue only if the File was successfully created
                    if (videoFile != null) {
                        uri = FileProvider.getUriForFile(getContext(), "ikbal.mulalic.reflektor.provider", videoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                    }

                }

            }
        });

        listOfReports = new ArrayList<>();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkForm()) {
                    Toast.makeText(getActivity(), "Forma mora sadrzavati kategoriju i sliku ili video", Toast.LENGTH_SHORT).show();
                    return;
                }

                startLocationUpdates();
            }

        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Location Request
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        // Location Callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        stopLocationUpdates();

                        sendRequest(latitude, longitude);

                        return;
                    }
                }
            }
        };

    }


    private void sendRequest(double latitude, double longitude) {
        String categoryText = spinner.getSelectedItem().toString();
        String descriptionText = opisPrijaveEditText.getText().toString();
        String locationText = lokacijaEditText.getText().toString();
        String realLocation = latitude + "," + longitude;


        final String url = "https://reflektor.live/reports/create";

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("category",categoryText);
        jsonObject.put("createdAt",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        jsonObject.put("description",descriptionText);
        jsonObject.put("id", UUID.randomUUID().toString());
        jsonObject.put("imageName",currentPhotoPath);
        jsonObject.put("location",locationText);
        jsonObject.put("locationCode",realLocation);
        jsonObject.put("userId","istoNekiString");
        jsonObject.put("videoName",currentVideoPath);


        new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    MultipartBody.Builder multiPartBody = new MultipartBody.Builder();

                    multiPartBody.addFormDataPart("report", jsonObject.toJSONString());
                    if(currentPhotoPath != null)
                    {
                        multiPartBody.addFormDataPart("Content-Type", "image/png")
                                .addFormDataPart("image", UUID.randomUUID().toString() + ".png",
                                        RequestBody.create(JSON, new File(currentPhotoPath)));
                    }
                    if (currentVideoPath != null)

                    {
                        multiPartBody.addFormDataPart("Content-Type", "video/mp4")
                                .addFormDataPart("video",UUID.randomUUID().toString() + ".mp4",RequestBody.create(JSON, new File(currentVideoPath)));
                    }

                    RequestBody requestBody = multiPartBody.build();

                    Request request = new Request.Builder()
                            .url(url)
                            .header("Content-Type", "multipart/form-data;")
                            .post(requestBody)
                            .build();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendButton.setEnabled(false);
                            Toast.makeText(context, "Slanje u toku...", Toast.LENGTH_SHORT).show();
                            //resetData();
                        }
                    });
                    final OkHttpClient client = new OkHttpClient();
                    try  {
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            jsonObject.put("isSent",false);
                            int i = 0;

                            while (true) {
                                String x = sharedPreferences.getString(i + "", "");
                                if (x == null || x.equals("")) {
                                    break;
                                }
                                i++;
                            }

                            editor.putString(String.valueOf(i), jsonObject.toJSONString());
                            editor.apply();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Doslo je do greske, prijava je sacuvana u \"Moje prijave\"", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    getActivity().finish();
                                    getActivity().overridePendingTransition(0, 0);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(0, 0);
                                    //sendButton.setEnabled(true);
                                    //resetData();
                                }
                            });
                        }
                        else
                        {
                            jsonObject.put("isSent",true);
                            int i = 0;

                            while (true) {
                                String x = sharedPreferences.getString(i + "", "");
                                if (x == null || x.equals("")) {
                                    break;
                                }
                                i++;
                            }

                            editor.putString(String.valueOf(i), jsonObject.toJSONString());
                            editor.apply();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Uspjesno kreirana prijava", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    getActivity().finish();
                                    getActivity().overridePendingTransition(0, 0);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(0, 0);
                                    //sendButton.setEnabled(true);
                                    //resetData();
                                }
                            });
                        }

                    }
                    catch (IOException e)
                    {
                        jsonObject.put("isSent",false);
                        int i = 0;

                        while (true) {
                            String x = sharedPreferences.getString(i + "", "");
                            if (x == null || x.equals("")) {
                                break;
                            }
                            i++;
                        }

                        editor.putString(String.valueOf(i), jsonObject.toJSONString());
                        editor.apply();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(context, "Doslo je do greske, prijava je sacuvana u \"Moje prijave\"", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                getActivity().finish();
                                getActivity().overridePendingTransition(0, 0);
                                startActivity(intent);
                                getActivity().overridePendingTransition(0, 0);
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check is GPS ON
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(getActivity(), locationSettingsResponse -> {
                    // If GPS is ON, then start getting location.
                    Toast.makeText(getActivity(), "U toku je potražnja Vaše lokacije...", Toast.LENGTH_LONG).show();
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                })
                .addOnFailureListener(getActivity(), e -> {

                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Open a dialog to ask the user to turn GPS on.
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

                            } catch (IntentSender.SendIntentException sie) {
                                Toast.makeText(getActivity(), "Uklučite Vaš GPS da bi mogli kreirati hitan slucaj!", Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Toast.makeText(getActivity(), "Uklučite Vaš GPS da bi mogli kreirati hitan slucaj!", Toast.LENGTH_LONG).show();
                            getActivity().finish();
                    }

                });
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    public boolean checkForm()
    {
        if((imageView.getVisibility() == View.VISIBLE || videoView.getVisibility() == View.VISIBLE ) && spinner.getSelectedItemId() != 0)
            return true;
        else
             return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_upute, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.prijava_knjiga) {
            Intent intent = new Intent (getContext(),InstructionsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Toast.makeText(getActivity(), "U toku je potražnja Vaše lokacije...", Toast.LENGTH_LONG).show();
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(getActivity(), "Ne možete kreirati hitan slučaj bez lokacije!", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    break;
            }
        } else if(requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK)
        {
                File neki = compressImageAndOverride(nekakav,currentPhotoPath);
                Bitmap bitmap = BitmapFactory.decodeFile(neki.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
        }

        else if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == getActivity().RESULT_OK)
        {
            videoView.setVideoPath(currentVideoPath);
            videoView.setVisibility(View.VISIBLE);
            videoView.start();
        }

    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        nekakav = image;

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "VIDEO" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File video = File.createTempFile(
                videoFileName,  /* prefix */
                ".vid",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentVideoPath = video.getAbsolutePath();
        return video;
    }

    private void setPic() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);

    }

    private File compressImageAndOverride(File file , String currentPhotoPath){
        try {
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image
            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            // The new size we want to scale to
            final int REQUIRED_SIZE=75;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();
            //      check the rotation of the image and display it properly
            ExifInterface exif;
            try {
                exif = new ExifInterface(currentPhotoPath);
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8 ) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                assert selectedBitmap != null;
                selectedBitmap = Bitmap.createBitmap(selectedBitmap, 0, 0,
                        selectedBitmap.getWidth(), selectedBitmap.getHeight(), matrix,
                        true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // here i override the original image file
            //   file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            assert selectedBitmap != null;
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70 , outputStream);
            return file;
        } catch (Exception e) {
            return null;
        }
    }

}