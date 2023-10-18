package com.travel.travellingbug.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.travel.travellingbug.BuildConfig;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.consts.AutoCompleteAdapter;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.models.PlacePredictions;
import com.travel.travellingbug.utills.Utilities;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
public class SetAddressActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_LOC = 30;
    double latitude;
    double longitude;
    TextView txtPickLocation,cursorTextView;
    Utilities utils = new Utilities();
    ImageView backArrow, imgDestClose, imgSourceClose;
    Activity thisActivity;
    String strSelected = "";
    private ListView mAutoCompleteList;
    private EditText txtDestination, txtaddressSource;
    private PlacePredictions predictions = new PlacePredictions();
    private Location mLastLocation;

    private AutoCompleteAdapter mAutoCompleteAdapter;
    private Handler handler;
    private GoogleApiClient mGoogleApiClient;
    private PlacePredictions placePredictions = new PlacePredictions();

    private ShimmerFrameLayout mFrameLayout;


    PlacesClient placesClient;
    String TAG = "CustomGoogleplacesearch";

    private Button btnSetLocation;
    private ProgressBar progressBar;
    private TextView textViewLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_set_address);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        thisActivity = this;

        // Initialize the SDK
        com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), BuildConfig.API_KEY);

        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
//        txtDestination = findViewById(R.id.txtDestination);
        txtaddressSource = findViewById(R.id.txtaddressSource);
        mAutoCompleteList = findViewById(R.id.searchResultLV);


        backArrow = findViewById(R.id.backArrow);
//        imgDestClose = findViewById(R.id.imgDestClose);
        imgSourceClose = findViewById(R.id.imgSourceClose);

        txtPickLocation = findViewById(R.id.txtPickLocation);
        cursorTextView = findViewById(R.id.cursorTextView);




//        Current Loacation Process
        btnSetLocation = findViewById(R.id.btnSetLocation);
        progressBar = findViewById(R.id.progressBar);
        textViewLocation = findViewById(R.id.textViewLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });





        try {
            //permission to access location
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                // Android M Permission check
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                System.out.println("location Permission granted ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String cursor = getIntent().getExtras().getString("cursor");


        strSelected = getIntent().getStringExtra("cursor");

        if (cursor.equalsIgnoreCase("source")) {
            strSelected = "source";

            txtaddressSource.requestFocus();
            cursorTextView.setText(strSelected);

        } else {
//            txtDestination.requestFocus();
            strSelected = "destination";
            cursorTextView.setText(strSelected);

        }

        txtaddressSource.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    strSelected = "source";
                    imgSourceClose.setVisibility(View.VISIBLE);
                } else {
                    imgSourceClose.setVisibility(View.GONE);
                }
            }
        });



        imgSourceClose.setOnClickListener(v -> {
            txtaddressSource.setText("");
            mAutoCompleteList.setVisibility(View.GONE);
            imgSourceClose.setVisibility(View.GONE);
            txtaddressSource.requestFocus();
        });

        txtPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });

        //get permission for Android M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            fetchLocation();
        } else {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOC);
            } else {
                fetchLocation();
            }
        }



        //Add a text change listener to implement autocomplete functionality
        txtaddressSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                imgSourceClose.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // optimised way is to start searching for laction after user has typed minimum 3 chars
//                strSelected = "source";
                if (txtaddressSource.getText().length() > 0) {
//                    txtPickLocation.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    imgSourceClose.setVisibility(View.VISIBLE);
                    txtPickLocation.setText(getString(R.string.pin_location));
                    txtPickLocation.setVisibility(View.GONE);
                    if (mAutoCompleteAdapter == null)
                        mAutoCompleteList.setVisibility(View.VISIBLE);
                    Runnable run = new Runnable() {

                        @Override
                        public void run() {

                            // cancel all the previous requests in the queue to optimise your network calls during autocomplete search
//                            ClassLuxApp.getInstance().cancelRequestInQueue(GETPLACESHIT);

                            JSONObject object = new JSONObject();
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getPlaceAutoCompleteUrl(txtaddressSource.getText().toString()),
                                    object, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.v("PayNowRequestResponse", response.toString());
                                    Log.v("PayNowRequestResponse", response.toString());
                                    Gson gson = new Gson();
                                    predictions = gson.fromJson(response.toString(), PlacePredictions.class);
                                    if (mAutoCompleteAdapter == null) {
                                        mAutoCompleteAdapter = new AutoCompleteAdapter(SetAddressActivity.this, predictions.getPlaces(), SetAddressActivity.this);
                                        mAutoCompleteList.setAdapter(mAutoCompleteAdapter);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        mAutoCompleteList.setVisibility(View.VISIBLE);
                                        mAutoCompleteAdapter.clear();
                                        mAutoCompleteAdapter.addAll(predictions.getPlaces());
                                        mAutoCompleteAdapter.notifyDataSetChanged();
                                        mAutoCompleteList.invalidate();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.v("PayNowRequestResponse", error.toString());
                                }
                            });
                            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

                        }

                    };

                    // only canceling the network calls will not help, you need to remove all callbacks as well
                    // otherwise the pending callbacks and messages will again invoke the handler and will send the request
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    } else {
                        handler = new Handler();
                    }
                    handler.postDelayed(run, 1000);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgSourceClose.setVisibility(View.VISIBLE);
            }

        });


        mAutoCompleteList.setOnItemClickListener((parent, view, position, id) -> {

            try {
                Toast.makeText(getApplicationContext(), "Processing", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }

            System.out.println("Postion : " + position);
            System.out.println("view : " + view);
            System.out.println("id : " + id);

            setGoogleAddress(position);


        });
        backArrow.setOnClickListener(v -> {
            finish();
        });

    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, get the location
            progressBar.setVisibility(View.VISIBLE);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }



    private void setGoogleAddress(int position) {
        if (mGoogleApiClient != null) {

            Geocoder coder = new Geocoder(this);
            List<Address> address;
            LatLng queriedLocation = null;

            try {
                address = coder.getFromLocationName(predictions.getPlaces().get(position).getPlaceDesc(), 5);
                for (int i=0;i<address.size(); i++){
                    System.out.println("cursor address : "+i +" : " +address.get(i));
                    System.out.println("cursor lat : "+i +" : " +address.get(i).getLatitude());
                    System.out.println("cursor long : "+i +" : " +address.get(i).getLongitude());

                }

                if(address.size() > 0){

                    queriedLocation = new LatLng(address.get(0).getLatitude(),address.get(0).getLongitude());
                    System.out.println("cursor : " + strSelected);
                    System.out.println("cursor Address : " +address.get(0).getAddressLine(0) );
                    Log.v("cursorLatitude is", "" + queriedLocation.latitude);
                    Log.v("cursorLongitude is", "" + queriedLocation.longitude);
                    if (strSelected.equalsIgnoreCase("destination")) {
                        placePredictions.strDestAddress = address.get(0).getAddressLine(0).toString();
                        placePredictions.strDestLatLng = queriedLocation.toString();
                        placePredictions.strDestLatitude = queriedLocation.latitude + "";
                        placePredictions.strDestLongitude = queriedLocation.longitude + "";
//                        txtDestination.setText(placePredictions.strDestAddress);
                        txtaddressSource.setText(placePredictions.strDestAddress);
                        System.out.println("WASUU destination : "+placePredictions.strDestAddress);
//                        txtDestination.setSelection(0);
                        mAutoCompleteAdapter = null;
                        setAddress();
                    } else {
                        placePredictions.strSourceAddress = address.get(0).getAddressLine(0).toString();
                        placePredictions.strSourceLatLng = queriedLocation.toString();
                        placePredictions.strSourceLatitude = queriedLocation.latitude + "";
                        placePredictions.strSourceLongitude = queriedLocation.longitude + "";
                        txtaddressSource.setText(placePredictions.strSourceAddress);
                        System.out.println("WASUU source : "+placePredictions.strSourceAddress);
//                        txtDestination.requestFocus();
                        txtaddressSource.setSelection(0);
                        mAutoCompleteAdapter = null;
                        setAddress();
                    }

                    mAutoCompleteList.setVisibility(View.GONE);
                    System.out.println("cursor set google address ended : " + strSelected);
                    System.out.println("cursor pridiction desc : " + predictions.getPlaces().get(position).getPlaceDesc());
                    System.out.println("cursor pridiction id : " + predictions.getPlaces().get(position).getPlaceID());



                }



            } catch (IOException e) {
                e.printStackTrace();
            }








        }
    }

    public String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json");
        urlString.append("?input=");
        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlString.append("&location=");
        urlString.append(latitude + "," + longitude); // append lat long of current location to show nearby results.
        urlString.append("&radius=500&language=en");
//        urlString.append("&key=" + getResources().getString(R.string.google_map_api));
        urlString.append("&key=" + BuildConfig.API_KEY);

        Log.d("FINAL URL:::   ", urlString.toString());

        return urlString.toString();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void fetchLocation() {
        //Build google API client to use fused location
        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted!
                    fetchLocation();
                } else {
                    // permission denied!
                    Toast.makeText(this, "Please grant permission for using this app!", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, get the location
                    getLocation();
                } else {
                    // Permission denied
                    progressBar.setVisibility(View.GONE);
                    textViewLocation.setText("Location permission denied.");
                }
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // Update location every 10 seconds

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        // Display the location in the TextView
                        String locationStr = "Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude();
                        String currentAddress = utils.getCompleteAddressString(SetAddressActivity.this, location.getLatitude(), location.getLongitude());

                        if (strSelected.equalsIgnoreCase("destination")) {
                            placePredictions.strDestAddress = currentAddress;
//                            placePredictions.strDestLatLng = location.getLatitude();
                            placePredictions.strDestLatitude = location.getLatitude() + "";
                            placePredictions.strDestLongitude =  location.getLongitude() + "";
//                        txtDestination.setText(placePredictions.strDestAddress);
                            txtaddressSource.setText(placePredictions.strDestAddress);
                            System.out.println("WASUU destination : "+placePredictions.strDestAddress);
//                        txtDestination.setSelection(0);
                            mAutoCompleteAdapter = null;
                            setAddress();
                        } else {
                            placePredictions.strSourceAddress = currentAddress;
//                            placePredictions.strSourceLatLng = queriedLocation.toString();
                            placePredictions.strSourceLatitude = location.getLatitude() + "";
                            placePredictions.strSourceLongitude = location.getLongitude() + "";
                            txtaddressSource.setText(placePredictions.strSourceAddress);
                            System.out.println("WASUU source : "+placePredictions.strSourceAddress);

                            txtaddressSource.setSelection(0);
                            mAutoCompleteAdapter = null;
                            setAddress();
                        }
                        textViewLocation.setText(currentAddress);

                    } else {
                        textViewLocation.setText("Location not found.");
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
    }








@Override
    public void onBackPressed() {
        setAddress();
        super.onBackPressed();
    }

    void setAddress() {
        //  utils.hideKeypad(thisActivity, getCurrentFocus());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                if (placePredictions != null) {
                    intent.putExtra("Location Address", placePredictions);
                    intent.putExtra("pick_location", "no");
                    intent.putExtra("type", strSelected);
                    System.out.println("WASU intent type strSelected : "+strSelected);
                    System.out.println("WASU intent type cursorTextView : "+cursorTextView.getText().toString());
                    intent.putExtra("strPickForStopOverOrNot", "no");
//                    Toast.makeText(CustomGooglePlacesSearch.this, "" + placePredictions.getPlaces(), Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, intent);
                    overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
                } else {
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
            }
        }, 500);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
}
