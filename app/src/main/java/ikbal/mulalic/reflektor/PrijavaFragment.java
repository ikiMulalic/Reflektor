package ikbal.mulalic.reflektor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


import ikbal.mulalic.reflektor.model.Report;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PrijavaFragment extends Fragment {

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

    SharedPreferences sharedPreferences;
    static  final int REQUEST_TAKE_PHOTO = 1;
    static  final int REQUEST_VIDEO_CAPTURE = 2;

    private View view;
    private Uri file = null;
    private SharedPreferences.Editor editor;
    private boolean checkIfFormIsValid = false;



    public PrijavaFragment() {
        // Required empty public constructor
    }

    public static PrijavaFragment newInstance(String param1, String param2) {
        PrijavaFragment fragment = new PrijavaFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if(savedInstanceState != null)
        {
            savedInstanceState.getString("kategorija","");
            savedInstanceState.getString("lokacija","");
            savedInstanceState.getString("putSlike","");
            savedInstanceState.getString("putVidea","");
            videoView.setVideoPath(savedInstanceState.getString("putVidea",""));
            videoView.setVisibility(View.VISIBLE);
            videoView.start();

        }*/
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
/*
        outState.putString("kategorija",categoryText);
        outState.putString("lokacija",locationText);
        outState.putString("putSlike",currentPhotoPath);
        outState.putString("putVidea",currentVideoPath);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        context = getContext();
        currentVideoPath = "";
        currentPhotoPath = "";
        view =inflater.inflate(R.layout.fragment_prijava, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        textView = (TextView) view.findViewById(R.id.textView);



        descriptionOfItems = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("ikbal.mukakic.reflektor", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

        descriptionOfItems.add("");
        descriptionOfItems.add("Podjela materijalne koristi glasačima sa namjerom da ih se navede da" +
                " glasaju za ili protiv određenog prijedloga, da uopšte ne glasaju ili da glasaju u " +
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
        saveButton = (ImageButton) view.findViewById(R.id.snimiButton);
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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryText = spinner.getSelectedItem().toString();
                descriptionText = opisPrijaveEditText.getText().toString();
                locationText = lokacijaEditText.getText().toString();
                String realLocation = MainActivity.getLatitude + "," + MainActivity.getLongitude;

                /*if(checkForm() == false)
                {
                    System.out.println("Cehic");
                    return;
                }*/

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("category",categoryText);
                jsonObject.put("description",descriptionText);
                jsonObject.put("location",locationText);
                jsonObject.put("photoPath",currentPhotoPath);
                jsonObject.put("videoPath",currentVideoPath);
                jsonObject.put("realLocation",realLocation);

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

                System.out.println(sharedPreferences.getString(String.valueOf(i), ""));

                // convert string to json object
                JSONObject jsonObject1 = (JSONObject)JSONValue.parse("");

                Toast.makeText(context, "Sacuvano u \"Moje Prijave\"", Toast.LENGTH_SHORT).show();
                spinner.setSelection(0);
                opisPrijaveEditText.setText("");
                opisPrijaveEditText.setHint("Opis");
                lokacijaEditText.setText("");
                lokacijaEditText.setHint("Lokacija");
                imageView.setVisibility(View.GONE);
                videoView.setVisibility(View.GONE);
                currentPhotoPath = "";
                currentVideoPath = "";


            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    public boolean checkForm()
    {
        if((categoryText != null && locationText != null && descriptionText != null &&
                MainActivity.getLongitude != 0 && MainActivity.getLatitude != 0)
                &&(currentPhotoPath != null || currentVideoPath != null))
        {
            checkIfFormIsValid = true;
        }
        /*else
            checkIfFormIsValid = false;*/
        return checkIfFormIsValid;
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
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK)
        {
           setPic();
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

}