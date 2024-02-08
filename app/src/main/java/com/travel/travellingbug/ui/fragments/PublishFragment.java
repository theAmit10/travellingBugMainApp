package com.travel.travellingbug.ui.fragments;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.ui.IconGenerator;
import com.koushikdutta.ion.Ion;
import com.skyfishjy.library.RippleBackground;
import com.travel.travellingbug.BuildConfig;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.DataParser;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.CardInfo;
import com.travel.travellingbug.models.Driver;
import com.travel.travellingbug.models.GetUserRate;
import com.travel.travellingbug.models.PlacePredictions;
import com.travel.travellingbug.models.RestInterface;
import com.travel.travellingbug.models.ServiceGenerator;
import com.travel.travellingbug.models.StopOverModel;
import com.travel.travellingbug.ui.activities.CouponActivity;
import com.travel.travellingbug.ui.activities.CustomGooglePlacesSearch;
import com.travel.travellingbug.ui.activities.CustomStopOverLocation;
import com.travel.travellingbug.ui.activities.DocUploadActivity;
import com.travel.travellingbug.ui.activities.HomeScreenActivity;
import com.travel.travellingbug.ui.activities.Payment;
import com.travel.travellingbug.ui.activities.ShowProfile;
import com.travel.travellingbug.ui.activities.UpdateProfile;
import com.travel.travellingbug.ui.activities.WaitingForApproval;
import com.travel.travellingbug.ui.adapters.StepOverAdapter;
import com.travel.travellingbug.ui.adapters.StepOverOrgAdapter;
import com.travel.travellingbug.utills.MapAnimator;
import com.travel.travellingbug.utills.MapRipple;
import com.travel.travellingbug.utills.MyTextView;
import com.travel.travellingbug.utills.ResponseListener;
import com.travel.travellingbug.utills.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class PublishFragment extends Fragment implements OnMapReadyCallback, LocationListener,
        GoogleMap.OnMarkerDragListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResponseListener, GoogleMap.OnCameraMoveListener {


    TextView calendertv, timetv, frmSource, frmDest;

    private static final String TAG = "PublishFragment";
    DatePickerDialog datePickerDialog;

    String scheduledDate = "";
    String scheduledTime = "";

    Utilities utils = new Utilities();
    boolean afterToday = false;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST = 18945;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST_STOPOVER = 18946;


//    #########################

    //    private static final String TAG = "UserMapFragment";
    private static final int REQUEST_LOCATION = 1450;
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private final int ADD_CARD_CODE = 435;
    public String PreviousStatus = "";
    Activity activity;
    Context context;
    View rootView;
    SearchFragment.HomeFragmentListener listener;

    CheckBox privateCarCheckBox;
    String privateCarCheckBoxVal = "0";
    double wallet_balance;
    String ETA;
    TextView txtSelectedAddressSource;
    String isPaid = "", paymentMode = "";
    //    Utilities utils = new Utilities();
    int flowValue = 0;

    String etaDur = "0";
    DrawerLayout drawer;
    int NAV_DRAWER = 0;
    String reqStatus = "";
    //    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST = 18945;
    String feedBackRating;
    double height;
    double width;

    String strPickForStopOverOrNot = "";
    String strPickLocation = "", strPickType = "";
    String strPickLocationSO = "", strPickTypeSO = "";
    int click = 1;
    //    boolean afterToday = false;
    boolean pick_first = true;
    boolean pick_firstSO = true;
    Driver driver;
    //        <!-- Map frame -->
    LinearLayout mapLayout;
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    int value;
    Marker marker;
    Double latitude, longitude;
    String currentAddress;
    GoogleApiClient mGoogleApiClient;
    //        <!-- Source and Destination Layout-->
    LinearLayout sourceAndDestinationLayout;
    FrameLayout frmDestination;
    TextView destination;
    TextView addStopOverTv;
    ImageView imgMenu, mapfocus, imgBack, shadowBack;
    View tripLine;
    ImageView destinationBorderImg;
    //    TextView frmSource, frmDest;
    CardView srcDestLayout;

    TextView stopOverTitleTv;

    ImageView backarrow,cross_icon;

    RecyclerView stopoverRv;
    StepOverAdapter stepOverAdapter;
    StepOverOrgAdapter stepOverOrgAdapter;
    ArrayList<StopOverModel> stopOverModelArrayList = new ArrayList<>();

    LinearLayout sourceDestLayout;
    LinearLayout lnrRequestProviders;
    RecyclerView rcvServiceTypes;
    ImageView imgPaymentType;


    //       <!--1. Request to providers -->
    ImageView imgSos;
    ImageView imgShareRide;
    TextView lblPaymentType, lblPromo, booking_id;
    //    Button btnRequestRides;
    TextView btnRequestRides;

    LinearLayout addStopOverLL;

    String scheduledTimeR = "";

    //    String scheduledDate = "";
//    String scheduledTime = "";
    String cancalReason = "";
    LinearLayout lnrHidePopup, lnrProviderPopup, lnrPriceBase, lnrPricemin, lnrPricekm;
    RelativeLayout lnrSearchAnimation;
    ImageView imgProviderPopup;

    //        <!--1. Driver Details-->
    TextView lblPriceMin, lblBasePricePopup, lblCapacity,
            lblServiceName, lblPriceKm, lblCalculationType, lblProviderDesc;
    Button btnDonePopup;
    LinearLayout lnrApproximate;
    Button btnRequestRideConfirm;
    Button imgSchedule;
    TextView tvPickUpAddres, tvDropAddres;
    LinearLayout layoutSrcDest;

    //         <!--2. Approximate Rate ...-->
    CheckBox chkWallet;
    TextView lblEta, lblDis;
    TextView lblType;
    TextView lblApproxAmount, surgeDiscount, surgeTxt;
    View lineView;
    LinearLayout ScheduleLayout;
    TextView scheduleDate;
    TextView scheduleTime;
    Button scheduleBtn;
    //    DatePickerDialog datePickerDialog;
    LocationRequest mLocationRequest;
    RelativeLayout lnrWaitingForProviders;
    TextView lblNoMatch;
    ImageView imgCenter;

    String scheduledDateR = "";

    RelativeLayout paymentLayout;

    //         <!--3. Waiting For Providers ...-->
    Button btnCancelRide;
    RippleBackground rippleBackground;
    LinearLayout lnrProviderAccepted, lnrAfterAcceptedStatus, AfterAcceptButtonLayout;
    ImageView imgProvider, imgServiceRequested;
    TextView lblProvider, lblStatus, lblETA, lblServiceRequested, lblModelNumber, lblSurgePrice;
    RatingBar ratingProvider;
    Button btnCall, btnCancelTrip;
    LinearLayout lnrInvoice;

    //         <!--4. Driver Accepted ...-->
    TextView lblBasePrice, lblExtraPrice, lblDistancePrice,
            lblTaxPrice, lblTotalPrice, lblPaymentTypeInvoice;
    ImageView imgPaymentTypeInvoice;
    Button btnPayNow;
    LinearLayout lnrRateProvider;
    TextView lblProviderNameRate;

    //          <!--5. Invoice Layout ...-->
    ImageView imgProviderRate;
    RatingBar ratingProviderRate;
    EditText txtCommentsRate;
    Button btnSubmitReview;

    //          <!--6. Rate provider Layout ...-->
    RelativeLayout rtlStaticMarker;
    ImageView imgDestination;
    TextView btnDone;
    CameraPosition cmPosition;
    String current_lat = "", current_lng = "", current_address = "", source_lat = "",
            source_lng = "", source_address = "",
            dest_lat = "", dest_lng = "", dest_address = "";

    String current_latSO = "", current_lngSO = "", current_addressSO = "", source_latSO = "",
            source_lngSO = "", source_addressSO = "",
            dest_latSO = "", dest_lngSO = "", dest_addressSO = "";


    JSONArray jsonArrayStopOver = new JSONArray();
    //Internet

    TextView addRideTitle;
    ConnectionHelper helper;

    //            <!-- Static marker-->
    Boolean isInternet;
    //RecylerView
    int currentPostion = 0;
    CustomDialog customDialog;
    TextView tvZoneMsg;
    //MArkers
    Marker availableProviders;
    ArrayList<LatLng> points = new ArrayList<LatLng>();
    ArrayList<Marker> lstProviderMarkers = new ArrayList<Marker>();
    androidx.appcompat.app.AlertDialog alert;
    ImageButton imgNavigate;
    //Animation
    Animation slide_down, slide_up, slide_up_top, slide_up_down;
    PublishFragment.ParserTask parserTask;
    String notificationTxt;
    boolean scheduleTrip = false;
    MapRipple mapRipple;
    //        Button schedule_ride;
    TextView schedule_ride;
    //  MY INITIALIZATION
    MyTextView lblCmfrmSourceAddress, lblCmfrmDestAddress;
    ImageView ImgConfrmCabType;
    ImageView ivNavigation;
    ImageView ivTopFav;
    @BindView(R.id.llFlow)
    FrameLayout llFlow;
    boolean isRequestProviderScreen;
    CharSequence pickUpLocationName = "";
    CharSequence dropLocationName = "";
    Call<ResponseBody> responseBodyCall;
    RestInterface restInterface;
    Call<GetUserRate> getUserRateCall;
    String cancaltype = "";
    PlacePredictions placePredictions;
    PlacePredictions placePredictionsSO;
    String requestWith = "XMLHttpRequest";
    Dialog userRateDialog;
    private ArrayList<CardInfo> cardInfoArrayList = new ArrayList<>();
    private boolean mIsShowing;
    private boolean mIsHiding;
    private LatLng sourceLatLng;
    private LatLng sourceLatLngSO;
    private LatLng destLatLng;
    private LatLng destLatLngSO;
    MyTextView serviceItemPrice;
    private Marker sourceMarker;
    private Marker destinationMarker;
    private Marker providerMarker;
    private boolean isDragging;
    EditText addFareInputETL;
    String fare_amount;


    public PublishFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCameraMove() {
        Utilities.print("Current marker", "Zoom Level " + mMap.getCameraPosition().zoom);
        cmPosition = mMap.getCameraPosition();
        if (marker != null) {
            if (!mMap.getProjection().getVisibleRegion().latLngBounds.contains(marker.getPosition())) {
                Utilities.print("Current marker", "Current Marker is not visible");
                if (mapfocus.getVisibility() == View.INVISIBLE) {
                    mapfocus.setVisibility(View.VISIBLE);
                }
            } else {
                Utilities.print("Current marker", "Current Marker is visible");
                if (mapfocus.getVisibility() == View.VISIBLE) {
                    mapfocus.setVisibility(View.INVISIBLE);
                }
                if (mMap.getCameraPosition().zoom < 16.0f) {
                    if (mapfocus.getVisibility() == View.INVISIBLE) {
                        mapfocus.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            notificationTxt = bundle.getString("Notification");

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getContext() != null) {
            getDocList();
        }

        try {

        }catch (Exception e){
            e.printStackTrace();
        }

        if (SharedHelper.getKey(getContext(), "Old_User").equalsIgnoreCase("yes")) {
            // Setting Name First
            if (SharedHelper.getKey(getContext(), "first_name").equalsIgnoreCase("null") || SharedHelper.getKey(getContext(), "first_name").equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Add your Name to Continue", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), UpdateProfile.class);
                intent.putExtra("parameter", "first_name");
                intent.putExtra("value", "");
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
            } else if (SharedHelper.getKey(getContext(), "DocumentStatus").equalsIgnoreCase("no")) {
                Intent intent = new Intent(getContext(), DocUploadActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
            }


        } else {
            if (SharedHelper.getKey(getContext(), "first_name").equalsIgnoreCase("null") || SharedHelper.getKey(getContext(), "first_name").equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Add your Name to Continue", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), UpdateProfile.class);
                intent.putExtra("parameter", "first_name");
                intent.putExtra("value", "");
                startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
            } else if (SharedHelper.getKey(getContext(), "DocumentStatus").equalsIgnoreCase("no")) {
                Intent intent = new Intent(getContext(), DocUploadActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
            }
        }

        if(SharedHelper.getKey(getContext(),"status").equalsIgnoreCase("onboarding") && SharedHelper.getKey(getContext(), "DocumentStatus").equalsIgnoreCase("yes") ){
            Intent intent = new Intent(getContext(), WaitingForApproval.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
        }

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_publish, container, false);
            ButterKnife.bind(this, rootView);
//            getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        mapLayout = rootView.findViewById(R.id.mapLayout);

        backarrow = rootView.findViewById(R.id.backarrow);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lnrRequestProviders.getVisibility() == View.VISIBLE) {
                    flowValue = 0;
                    isRequestProviderScreen = false;
                    sourceDestLayout.setVisibility(View.VISIBLE);
//                        getProvidersList("");
                    frmSource.setOnClickListener(new PublishFragment.OnClick());
                    frmDest.setOnClickListener(new PublishFragment.OnClick());
                    sourceDestLayout.setOnClickListener(null);
                    if (!current_lat.equalsIgnoreCase("") && !current_lng.equalsIgnoreCase("")) {
                        destinationBorderImg.setVisibility(View.VISIBLE);
                        //verticalView.setVisibility(View.GONE);
                        LatLng myLocation = new LatLng(Double.parseDouble(current_lat), Double.parseDouble(current_lng));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                            getProvidersList("");
                        sourceDestLayout.setVisibility(View.VISIBLE);
                    }
                } else if (lnrApproximate.getVisibility() == View.VISIBLE) {
                    isRequestProviderScreen = true;
                    frmSource.setOnClickListener(new PublishFragment.OnClick());
                    frmDest.setOnClickListener(new PublishFragment.OnClick());
                    sourceDestLayout.setOnClickListener(null);
                    flowValue = 1;
                } else if (lnrWaitingForProviders.getVisibility() == View.VISIBLE) {
                    sourceDestLayout.setVisibility(View.GONE);
                    isRequestProviderScreen = false;
                    flowValue = 1;
                } else if (ScheduleLayout.getVisibility() == View.VISIBLE) {
                    isRequestProviderScreen = false;
                    flowValue = 1;
                }
                layoutChanges();
            }
        });


        addFareInputETL = rootView.findViewById(R.id.addFareInputETL);


        // Adding Fare Amount
        fare_amount = addFareInputETL.getText().toString();
        addFareInputETL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                fare_amount = addFareInputETL.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fare_amount = addFareInputETL.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cross_icon = rootView.findViewById(R.id.cross_icon);
        cross_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtSelectedAddressSource != null ){
                    txtSelectedAddressSource.setText("");
                }
            }
        });



        initComponent(rootView);






        onClickHandler();
        restInterface = ServiceGenerator.createService(RestInterface.class);
        customDialog = new CustomDialog(getActivity());
        if (activity != null && isAdded()) {
            if (customDialog != null) {
                customDialog.show();
                new Handler().postDelayed(() -> {
                    init(rootView);
                    //permission to access location
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                            ActivityCompat.checkSelfPermission(getActivity(),
                                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                                    PackageManager.PERMISSION_GRANTED) {
                        // Android M Permission check
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        initMap();
                        MapsInitializer.initialize(getActivity());
                        customDialog.dismiss();
                        customDialog.cancel();
                    }
                }, 500);
            }
        }

        reqStatus = SharedHelper.getKey(context, "req_status");
        if (reqStatus != null && !reqStatus.equalsIgnoreCase("null") && reqStatus.length() > 0) {
            if (reqStatus.equalsIgnoreCase("SEARCHING")) {
//                Toast.makeText(context, "You have already requested to a trip", Toast.LENGTH_SHORT).show();
            }
        }


        return rootView;

    }

    private void onClickHandler() {
//        calendertv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                int mYear = c.get(Calendar.YEAR); // current year
//                int mMonth = c.get(Calendar.MONTH); // current month
//                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
//                // date picker dialog
//                datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
//                        (view, year, monthOfYear, dayOfMonth) -> {
//
//                            // set day of month , month and year value in the edit text
//                            String choosedMonth = "";
//                            String choosedDate = "";
//                            String choosedDateFormat = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
//                            scheduledDate = choosedDateFormat;
//                            try {
//                                choosedMonth = utils.getMonth(choosedDateFormat);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            if (dayOfMonth < 10) {
//                                choosedDate = "0" + dayOfMonth;
//                            } else {
//                                choosedDate = "" + dayOfMonth;
//                            }
//                            afterToday = Utilities.isAfterToday(year, monthOfYear, dayOfMonth);
//                            calendertv.setText(choosedDate + " " + choosedMonth + " " + year);
//                        }, mYear, mMonth, mDay);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() - 1000) + (1000 * 60 * 60 * 24 * 7));
//                datePickerDialog.show();
//            }
//        });
//
//        timetv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
//                    int callCount = 0;   //To track number of calls to onTimeSet()
//
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//
//                        if (callCount == 0) {
//                            String choosedHour = "";
//                            String choosedMinute = "";
//                            String choosedTimeZone = "";
//                            String choosedTime = "";
//
//                            scheduledTime = selectedHour + ":" + selectedMinute;
//
//                            if (selectedHour > 12) {
//                                choosedTimeZone = "PM";
//                                selectedHour = selectedHour - 12;
//                                if (selectedHour < 10) {
//                                    choosedHour = "0" + selectedHour;
//                                } else {
//                                    choosedHour = "" + selectedHour;
//                                }
//                            } else {
//                                choosedTimeZone = "AM";
//                                if (selectedHour < 10) {
//                                    choosedHour = "0" + selectedHour;
//                                } else {
//                                    choosedHour = "" + selectedHour;
//                                }
//                            }
//
//                            if (selectedMinute < 10) {
//                                choosedMinute = "0" + selectedMinute;
//                            } else {
//                                choosedMinute = "" + selectedMinute;
//                            }
//                            choosedTime = choosedHour + ":" + choosedMinute + " " + choosedTimeZone;
//
//                            if (scheduledDate != "" && scheduledTime != "") {
//                                Date date = null;
//                                try {
//                                    date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(scheduledDate);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                long milliseconds = date.getTime();
//                                if (!DateUtils.isToday(milliseconds)) {
//                                    timetv.setText(choosedTime);
//                                } else {
//                                    if (utils.checktimings(scheduledTime)) {
//                                        timetv.setText(choosedTime);
//                                    } else {
//                                        Toast toast = new Toast(getActivity());
//                                        Toast.makeText(getActivity(), getString(R.string.different_time), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            } else {
//                                Toast.makeText(getActivity(), getString(R.string.choose_date_time), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        callCount++;
//                    }
//                }, hour, minute, false);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//            }
//        });

//        frmSource.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), CustomGooglePlacesSearch.class);
//                intent.putExtra("cursor", "source");
//                intent.putExtra("s_address", frmSource.getText().toString());
//                intent.putExtra("d_address", frmDest.getText().toString());
//                intent.putExtra("d_address", frmDest.getText().toString());
//                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
//            }
//        });
//
//        frmDest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent2 = new Intent(getActivity(), CustomGooglePlacesSearch.class);
//                intent2.putExtra("cursor", "destination");
//                intent2.putExtra("s_address", frmSource.getText().toString());
//                intent2.putExtra("d_address", frmDest.getText().toString());
//                intent2.putExtra("d_address", frmDest.getText().toString());
//                startActivityForResult(intent2, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
//            }
//        });
    }

    private void getDocList() {


        try{
            if(getContext() != null){

                JsonArrayRequest jsonArrayRequest = new
                        JsonArrayRequest(URLHelper.BASE + "api/provider/document/status",
                                response -> {

                                    if (response != null) {
                                        Log.v("response doc", response + "doc");
                                        Log.v("response doc length", String.valueOf(+response.length()));

                                        if (response.length() == 0) {
                                            SharedHelper.putKey(getContext(), "DocumentStatus", "no");
                                        } else {
                                            SharedHelper.putKey(getContext(), "DocumentStatus", "yes");
                                        }
                                    }



                                }, error -> {
                            Log.v("DocumentsStatus Error", error.getMessage() + "");


//                            displayMessage(getContext().getString(R.string.something_went_wrong));
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("X-Requested-With", "XMLHttpRequest");
                                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
                                return headers;
                            }
                        };

                ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initComponent(View view) {


        frmSource = view.findViewById(R.id.frmSource);
        frmDest = view.findViewById(R.id.frmDest);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        try {
            listener = (SearchFragment.HomeFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
//            throw new ClassCastException(context.toString()
//                    + " must implement HomeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    private void init(View rootView) {

        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();

        statusCheck();
//        <!-- Map frame -->
        schedule_ride = rootView.findViewById(R.id.schedule_ride);

        mapLayout = rootView.findViewById(R.id.mapLayout);
        drawer = rootView.findViewById(R.id.drawer_layout);
        drawer = activity.findViewById(R.id.drawer_layout);

//        <!-- Source and Destination Layout-->
        sourceAndDestinationLayout = rootView.findViewById(R.id.sourceAndDestinationLayout);
        sourceDestLayout = rootView.findViewById(R.id.sourceDestLayout);
        frmSource = rootView.findViewById(R.id.frmSource);
        frmDest = rootView.findViewById(R.id.frmDest);
        frmDestination = rootView.findViewById(R.id.frmDestination);
        destination = rootView.findViewById(R.id.destination);
        imgMenu = rootView.findViewById(R.id.imgMenu);
        imgSos = rootView.findViewById(R.id.imgSos);
        imgShareRide = rootView.findViewById(R.id.imgShareRide);
        mapfocus = rootView.findViewById(R.id.mapfocus);
        imgBack = rootView.findViewById(R.id.imgBack);
        shadowBack = rootView.findViewById(R.id.shadowBack);
//        tripLine = (View) rootView.findViewById(R.id.trip_line);
        destinationBorderImg = rootView.findViewById(R.id.dest_border_img);
        imgNavigate = rootView.findViewById(R.id.imgNavigate);
        ivNavigation = rootView.findViewById(R.id.ivNavigation);
        txtSelectedAddressSource = rootView.findViewById(R.id.txtSelectedAddressSource);
        ivTopFav = rootView.findViewById(R.id.ivTopFav);
        addStopOverLL = rootView.findViewById(R.id.addStopOverLL);
        privateCarCheckBox = rootView.findViewById(R.id.privateCarCheckBox);

//        <!-- Request to providers-->

//        secondLayoutAlpha = rootView.findViewById(R.id.secondLayoutAlpha);
        lnrRequestProviders = rootView.findViewById(R.id.lnrRequestProviders);
//        secondLayoutAlpha.setAlpha((float) 0.7);
        rcvServiceTypes = rootView.findViewById(R.id.rcvServiceTypes);
        imgPaymentType = rootView.findViewById(R.id.imgPaymentType);
        lblPaymentType = rootView.findViewById(R.id.lblPaymentType);
        lblPromo = rootView.findViewById(R.id.lblPromo);
        booking_id = rootView.findViewById(R.id.booking_id);
        btnRequestRides = rootView.findViewById(R.id.btnRequestRides);

//        <!--  Driver and service type Details-->

        lnrSearchAnimation = rootView.findViewById(R.id.lnrSearch);
        lnrProviderPopup = rootView.findViewById(R.id.lnrProviderPopup);
        lnrPriceBase = rootView.findViewById(R.id.lnrPriceBase);
        lnrPricekm = rootView.findViewById(R.id.lnrPricekm);
        lnrPricemin = rootView.findViewById(R.id.lnrPricemin);
        lnrHidePopup = rootView.findViewById(R.id.lnrHidePopup);
        imgProviderPopup = rootView.findViewById(R.id.imgProviderPopup);

        lblServiceName = rootView.findViewById(R.id.lblServiceName);
        lblCapacity = rootView.findViewById(R.id.lblCapacity);
        lblPriceKm = rootView.findViewById(R.id.lblPriceKm);
        lblPriceMin = rootView.findViewById(R.id.lblPriceMin);
        lblCalculationType = rootView.findViewById(R.id.lblCalculationType);
        lblBasePricePopup = rootView.findViewById(R.id.lblBasePricePopup);
        lblProviderDesc = rootView.findViewById(R.id.lblProviderDesc);

        btnDonePopup = rootView.findViewById(R.id.btnDonePopup);
        addStopOverTv = rootView.findViewById(R.id.addStopOverTv);
        stopoverRv = rootView.findViewById(R.id.stopoverRv);


//         <!--2. Approximate Rate ...-->

        lnrApproximate = rootView.findViewById(R.id.lnrApproximate);
        imgSchedule = rootView.findViewById(R.id.imgSchedule);
        tvPickUpAddres = rootView.findViewById(R.id.tvSourcePoint);
        tvDropAddres = rootView.findViewById(R.id.tvDroppoint);
        layoutSrcDest = rootView.findViewById(R.id.layoutSrcDest);
        chkWallet = rootView.findViewById(R.id.chkWallet);
        lblEta = rootView.findViewById(R.id.lblEta);
        lblDis = rootView.findViewById(R.id.lblDis);
        lblType = rootView.findViewById(R.id.lblType);
        lblApproxAmount = rootView.findViewById(R.id.lblApproxAmount);
        surgeDiscount = rootView.findViewById(R.id.surgeDiscount);
        surgeTxt = rootView.findViewById(R.id.surge_txt);
        btnRequestRideConfirm = rootView.findViewById(R.id.btnRequestRideConfirm);
        lineView = rootView.findViewById(R.id.lineView);

        //Schedule Layout
        ScheduleLayout = rootView.findViewById(R.id.ScheduleLayout);
        scheduleDate = rootView.findViewById(R.id.scheduleDate);
        scheduleTime = rootView.findViewById(R.id.scheduleTime);
        scheduleBtn = rootView.findViewById(R.id.scheduleBtn);

//         <!--3. Waiting For Providers ...-->

        lnrWaitingForProviders = rootView.findViewById(R.id.lnrWaitingForProviders);
        lblNoMatch = rootView.findViewById(R.id.lblNoMatch);
        //imgCenter =  rootView.findViewById(R.id.imgCenter);
        btnCancelRide = rootView.findViewById(R.id.btnCancelRide);
        rippleBackground = rootView.findViewById(R.id.content);

//          <!--4. Driver Accepted ...-->

        lnrProviderAccepted = rootView.findViewById(R.id.lnrProviderAccepted);
        lnrAfterAcceptedStatus = rootView.findViewById(R.id.lnrAfterAcceptedStatus);
        AfterAcceptButtonLayout = rootView.findViewById(R.id.AfterAcceptButtonLayout);
        imgProvider = rootView.findViewById(R.id.imgProvider);
        imgServiceRequested = rootView.findViewById(R.id.imgServiceRequested);
        lblProvider = rootView.findViewById(R.id.lblProvider);
        lblStatus = rootView.findViewById(R.id.lblStatus);
        lblETA = rootView.findViewById(R.id.lblETA);
        lblSurgePrice = rootView.findViewById(R.id.lblSurgePrice);
        lblServiceRequested = rootView.findViewById(R.id.lblServiceRequested);
        lblModelNumber = rootView.findViewById(R.id.lblModelNumber);
        ratingProvider = rootView.findViewById(R.id.ratingProvider);
        btnCall = rootView.findViewById(R.id.btnCall);
        btnCancelTrip = rootView.findViewById(R.id.btnCancelTrip);

        paymentLayout = rootView.findViewById(R.id.paymentLayout);
        addRideTitle = rootView.findViewById(R.id.addRideTitle);

//           <!--5. Invoice Layout ...-->

        lnrInvoice = rootView.findViewById(R.id.lnrInvoice);
        lblBasePrice = rootView.findViewById(R.id.lblBasePrice);
        lblExtraPrice = rootView.findViewById(R.id.lblExtraPrice);
        lblDistancePrice = rootView.findViewById(R.id.lblDistancePrice);
        //lblCommision =  rootView.findViewById(R.id.lblCommision);
        lblTaxPrice = rootView.findViewById(R.id.lblTaxPrice);
        lblTotalPrice = rootView.findViewById(R.id.lblTotalPrice);
        lblPaymentTypeInvoice = rootView.findViewById(R.id.lblPaymentTypeInvoice);
        imgPaymentTypeInvoice = rootView.findViewById(R.id.imgPaymentTypeInvoice);
        btnPayNow = rootView.findViewById(R.id.btnPayNow);

//          <!--6. Rate provider Layout ...-->

        lnrRateProvider = rootView.findViewById(R.id.lnrRateProvider);
        lblProviderNameRate = rootView.findViewById(R.id.lblProviderName);
        imgProviderRate = rootView.findViewById(R.id.imgProviderRate);
        txtCommentsRate = rootView.findViewById(R.id.txtComments);
        ratingProviderRate = rootView.findViewById(R.id.ratingProviderRate);
        btnSubmitReview = rootView.findViewById(R.id.btnSubmitReview);

//            <!--Static marker-->

        rtlStaticMarker = rootView.findViewById(R.id.rtlStaticMarker);
        imgDestination = rootView.findViewById(R.id.imgDestination);
        btnDone = rootView.findViewById(R.id.btnDone);
        stopOverTitleTv = rootView.findViewById(R.id.stopOverTitleTv);

        /* MY INITIALIZATION*/

        lblCmfrmSourceAddress = rootView.findViewById(R.id.lblCmfrmSourceAddress);
        lblCmfrmDestAddress = rootView.findViewById(R.id.lblCmfrmDestAddress);
        ImgConfrmCabType = rootView.findViewById(R.id.ImgConfrmCabType);
        tvZoneMsg = rootView.findViewById(R.id.tvZoneMsg);
        // serviceItemPrice =  rootView.findViewById(R.id.serviceItemPrice);

       privateCarCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
           if(privateCarCheckBox.isChecked()){

               try {
                   Calendar mcurrentTime = Calendar.getInstance();
                   int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                   int minute = mcurrentTime.get(Calendar.MINUTE);
                   TimePickerDialog mTimePicker;


                   mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                       int callCount = 0;   //To track number of calls to onTimeSet()

                       @Override
                       public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                           if (callCount == 0) {
                               String choosedHour = "";
                               String choosedMinute = "";
                               String choosedTimeZone = "";
                               String choosedTime = "";

                               scheduledTimeR = selectedHour + ":" + selectedMinute;

                               if (selectedHour > 12) {
                                   choosedTimeZone = "PM";
                                   selectedHour = selectedHour - 12;
                                   if (selectedHour < 10) {
                                       choosedHour = "0" + selectedHour;
                                   } else {
                                       choosedHour = "" + selectedHour;
                                   }
                               } else {
                                   choosedTimeZone = "AM";
                                   if (selectedHour < 10) {
                                       choosedHour = "0" + selectedHour;
                                   } else {
                                       choosedHour = "" + selectedHour;
                                   }
                               }

                               if (selectedMinute < 10) {
                                   choosedMinute = "0" + selectedMinute;
                               } else {
                                   choosedMinute = "" + selectedMinute;
                               }
                               choosedTime = choosedHour + ":" + choosedMinute + " " + choosedTimeZone;


                               if (scheduledDateR != "" && scheduledTimeR != "") {
                                   Date date = null;
                                   try {
                                       date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(scheduledDateR);
                                   } catch (ParseException e) {
                                       e.printStackTrace();
                                   }
                                   long milliseconds = date.getTime();
                                   if (!DateUtils.isToday(milliseconds)) {
//                                    timetv.setText(choosedTime);
                                       System.out.println("time : " + choosedTime);
                                   } else {
                                       if (utils.checktimings(scheduledTimeR)) {
//                                        timetv.setText(choosedTime);
                                           privateCarCheckBoxVal = "1";
                                           System.out.println("time : " + choosedTime);
                                       } else {
                                           Toast toast = new Toast(getActivity());
                                           Toast.makeText(getActivity(), getString(R.string.different_time), Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               } else {
                                   Toast.makeText(getActivity(), getString(R.string.choose_date_time), Toast.LENGTH_SHORT).show();
                               }
                           }
                           callCount++;
                       }
                   }, hour, minute, false);//Yes 24 hour time
                   mTimePicker.setTitle("Select Time");
                   mTimePicker.show();


                   final Calendar c = Calendar.getInstance();
                   int mYear = c.get(Calendar.YEAR); // current year
                   int mMonth = c.get(Calendar.MONTH); // current month
                   int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                   // date picker dialog
                   datePickerDialog = new DatePickerDialog(getActivity(),
                           (view, year, monthOfYear, dayOfMonth) -> {

                               // set day of month , month and year value in the edit text
                               String choosedMonth = "";
                               String choosedDate = "";
                               String choosedDateFormat = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                               scheduledDateR = choosedDateFormat;
                               try {
                                   choosedMonth = utils.getMonth(choosedDateFormat);
                               } catch (ParseException e) {
                                   e.printStackTrace();
                               }
                               if (dayOfMonth < 10) {
                                   choosedDate = "0" + dayOfMonth;
                               } else {
                                   choosedDate = "" + dayOfMonth;
                               }
                               afterToday = Utilities.isAfterToday(year, monthOfYear, dayOfMonth);
                               System.out.println("RETURN TIME : "+choosedDate + " " + choosedMonth + " " + year);
//                        calendertv.setText(choosedDate + " " + choosedMonth + " " + year);
                           }, mYear, mMonth, mDay);
                   datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                   datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() - 1000) + (1000 * 60 * 60 * 24 * 7));
                   datePickerDialog.show();

               }catch (Exception e){
                   System.out.println("ERROR ON RETURN DATE");
               }


           }
       });


        privateCarCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(privateCarCheckBox.isChecked()){
                    System.out.println("Checked");
                } else {
                    System.out.println("Un-Checked");
                }
            }
        });



        // setting stopOver location
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        stopoverRv.setLayoutManager(linearLayoutManager);
//        stopOverTitleTv.setVisibility(View.VISIBLE);
//        stepOverOrgAdapter = new StepOverOrgAdapter(getContext(), stopOverModelArrayList);
//        stopoverRv.setAdapter(stepOverOrgAdapter);
        stopoverRv.setNestedScrollingEnabled(false);




//        getCards();
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardId("CASH");
        cardInfo.setCardType("CASH");
        cardInfo.setLastFour("CASH");
        cardInfoArrayList.add(cardInfo);

        schedule_ride.setOnClickListener(new PublishFragment.OnClick());
        btnRequestRides.setOnClickListener(new PublishFragment.OnClick());
        btnDonePopup.setOnClickListener(new PublishFragment.OnClick());
        lnrHidePopup.setOnClickListener(new PublishFragment.OnClick());
        btnRequestRideConfirm.setOnClickListener(new PublishFragment.OnClick());
        btnCancelRide.setOnClickListener(new PublishFragment.OnClick());
        btnCancelTrip.setOnClickListener(new PublishFragment.OnClick());
        btnCall.setOnClickListener(new PublishFragment.OnClick());
        btnPayNow.setOnClickListener(new PublishFragment.OnClick());
        btnSubmitReview.setOnClickListener(new PublishFragment.OnClick());
        btnDone.setOnClickListener(new PublishFragment.OnClick());
        frmDestination.setOnClickListener(new PublishFragment.OnClick());
        sourceDestLayout.setOnClickListener(new PublishFragment.OnClick());
        frmDest.setOnClickListener(new PublishFragment.OnClick());
        lblPaymentType.setOnClickListener(new PublishFragment.OnClick());
        imgPaymentType.setOnClickListener(new PublishFragment.OnClick());
        frmSource.setOnClickListener(new PublishFragment.OnClick());

        imgMenu.setOnClickListener(new PublishFragment.OnClick());

        mapfocus.setOnClickListener(new PublishFragment.OnClick());
        imgSchedule.setOnClickListener(new PublishFragment.OnClick());
        imgBack.setOnClickListener(new PublishFragment.OnClick());
        scheduleBtn.setOnClickListener(new PublishFragment.OnClick());
        scheduleDate.setOnClickListener(new PublishFragment.OnClick());
        scheduleTime.setOnClickListener(new PublishFragment.OnClick());
        imgProvider.setOnClickListener(new PublishFragment.OnClick());
        imgProviderRate.setOnClickListener(new PublishFragment.OnClick());
        imgSos.setOnClickListener(new PublishFragment.OnClick());
        imgShareRide.setOnClickListener(new PublishFragment.OnClick());
        lblPromo.setOnClickListener(new PublishFragment.OnClick());
        lnrRequestProviders.setOnClickListener(new PublishFragment.OnClick());
        lnrProviderPopup.setOnClickListener(new PublishFragment.OnClick());
        ScheduleLayout.setOnClickListener(new PublishFragment.OnClick());
        lnrApproximate.setOnClickListener(new PublishFragment.OnClick());
        lnrProviderAccepted.setOnClickListener(new PublishFragment.OnClick());
        lnrInvoice.setOnClickListener(new PublishFragment.OnClick());
        lnrRateProvider.setOnClickListener(new PublishFragment.OnClick());
        lnrWaitingForProviders.setOnClickListener(new PublishFragment.OnClick());




        addStopOverTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CustomStopOverLocation.class);
                intent.putExtra("cursor", "source");
                intent.putExtra("s_address", frmSource.getText().toString());
                intent.putExtra("d_address", destination.getText().toString());
                intent.putExtra("d_address", frmDest.getText().toString());
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST_STOPOVER);
            }
        });








//        ivNavigation.setOnClickListener(view -> {
//            if ((source_lat != null && source_lng != null) &&
//                    (dest_lat != null && dest_lng != null)) {
//                redirectMap(source_lat, source_lng, dest_lat, dest_lng);
//            }
//        });


// commenting below line for testing
        if (ivTopFav != null) {
            ivTopFav.setOnClickListener(view -> saveAddressDialog());
        }


        flowValue = 0;
        layoutChanges();


        try {
            //Load animation
            slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
            slide_up = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
            slide_up_top = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_top);
            slide_up_down = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_down);

        }catch (Exception e){
            e.printStackTrace();
        }


        imgNavigate.setOnClickListener(v -> {
            Uri naviUri2 = Uri.parse("http://maps.google.com/maps?"
                    + "saddr=" + source_lat + "," + source_lng
                    + "&daddr=" + dest_lat + "," + dest_lng);

            Intent intent = new Intent(Intent.ACTION_VIEW, naviUri2);
            intent.setClassName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity");
            startActivity(intent);


        });


        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return true;

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (!reqStatus.equalsIgnoreCase("SEARCHING")) {
                    Utilities.print("", "Back key pressed!");
                    if (lnrRequestProviders.getVisibility() == View.VISIBLE) {

                        if (!dest_lat.equalsIgnoreCase("") && !dest_lng.equalsIgnoreCase("")) {


                            if (!current_lat.equalsIgnoreCase("") &&
                                    !current_lng.equalsIgnoreCase("")) {
                                LatLng myLocation = new LatLng(Double.parseDouble(current_lat),
                                        Double.parseDouble(current_lng));
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(myLocation).zoom(16).build();
                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                flowValue = 0;
                            }
                        } else {


                            exitConfirmation();


                        }
                    } else if (lnrApproximate.getVisibility() == View.VISIBLE) {
                        mMap.setPadding(50, 50, 50, 50);
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(sourceMarker.getPosition());
                        builder.include(destinationMarker.getPosition());
                        LatLngBounds bounds = builder.build();
                        int padding = 0; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                        mMap.moveCamera(cu);
                        flowValue = 1;
                    } else if (lnrWaitingForProviders.getVisibility() == View.VISIBLE) {
                        sourceDestLayout.setVisibility(View.GONE);
                        flowValue = 1;
                    } else if (ScheduleLayout.getVisibility() == View.VISIBLE) {
                        flowValue = 1;
                    } else {
                        exitConfirmation();
                    }
                    layoutChanges();
                    return true;
                }
            }
            return false;
        });

    }

    private  void showAddStopOverDialog(){
        Dialog confirmDialog = new Dialog(getContext());
        confirmDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        confirmDialog.setContentView(R.layout.design_stopover);


        TextView editText = confirmDialog.findViewById(R.id.editText);
        ImageView backArrow = confirmDialog.findViewById(R.id.backArrow);
        FloatingActionButton nextBtn = confirmDialog.findViewById(R.id.nextBtn);



        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });


        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CustomStopOverLocation.class);
                intent.putExtra("cursor", "source");
                intent.putExtra("s_address", frmSource.getText().toString());
                intent.putExtra("d_address", destination.getText().toString());
                intent.putExtra("d_address", frmDest.getText().toString());
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST_STOPOVER);
            }
        });



        confirmDialog.show();
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();

            }
        });
    }

    private void exitConfirmation() {
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle("Confirmation")
                .setMessage("Do you really want to Exit Cab Services?")
                .setIcon(R.drawable.app_logo_org)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> System.exit(0))
                .setNegativeButton(android.R.string.no, null).show();
    }

    @SuppressWarnings("MissingPermission")
    void initMap() {
        if (mMap == null) {
            FragmentManager fm = getChildFragmentManager();
            mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.provider_map);
            mapFragment.getMapAsync(this);
        }
        if (mMap != null) {
            setupMap();
        }

//        getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                new Handler().postDelayed(() -> getServiceList(), 500);
//            }
//        });


//        new Handler().postDelayed(() -> getServiceList(), 500);
//        // checkStatus();

    }

    @SuppressWarnings("MissingPermission")
    void setupMap() {
        if (mMap != null) {
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.setMyLocationEnabled(false);
            mMap.setOnMarkerDragListener(this);
            mMap.setOnCameraMoveListener(this);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
        } else {

            Toast.makeText(activity, "No Map", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onLocationChanged(Location location) {

        if (marker != null) {
            marker.remove();
        }
        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {

            MarkerOptions markerOptions = new MarkerOptions()
                    .anchor(0.5f, 0.75f)
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_currentlocation));
            marker = mMap.addMarker(markerOptions);


            current_lat = "" + location.getLatitude();
            current_lng = "" + location.getLongitude();

            if (source_lat.equalsIgnoreCase("") || source_lat.length() < 0) {
                source_lat = current_lat;
            }
            if (source_lng.equalsIgnoreCase("") || source_lng.length() < 0) {
                source_lng = current_lng;
            }

            if (value == 0) {
                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.setPadding(0, 0, 0, 0);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.getUiSettings().setCompassEnabled(false);

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                currentAddress = utils.getCompleteAddressString(context, latitude, longitude);
                source_lat = "" + latitude;
                source_lng = "" + longitude;
                source_address = currentAddress;
                current_address = currentAddress;

                try {
                    if (frmSource.getContext() != null) {
                        frmSource.setTextColor(requireActivity().getResources().getColor(R.color.dark_gray));
                        frmSource.setText(currentAddress);
                    }
                } catch (Exception e) {
                    System.out.println("Error : " + e.getCause());
                    e.printStackTrace();
                }


//                // setting previous destination data
//                if (!SharedHelper.getKey(getContext(), "destination_latitude").equalsIgnoreCase("")) {
//                    String destination_latitude = SharedHelper.getKey(getContext(), "destination_latitude");
//                    String destination_longitude = SharedHelper.getKey(getContext(), "destination_longitude");
//
//                    Double dlat = Double.valueOf(destination_latitude);
//                    Double dlong = Double.valueOf(destination_longitude);
//
//                    String destination_address = utils.getCompleteAddressString(context, dlat, dlong);
//                    frmDest.setText(destination_address);
//                } else {
//                frmDest.setText("Going to");
//            }

//                getProvidersList("");
                value++;
                if ((customDialog != null) && (customDialog.isShowing()))
                    customDialog.dismiss();
            }

            updateLocationToAdmin(location.getLatitude() + "", location.getLongitude() + "");
        }
    }

    private void updateLocationToAdmin(String latitude, String longitude) {
        JSONObject object = new JSONObject();
        try {
            object.put("latitude", latitude);
            object.put("longitude", longitude);
            Utilities.print("SendRequestUpdate", "" + object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassLuxApp.getInstance().cancelRequestInQueue("send_request");
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST,
                        URLHelper.UPDATE_LOCATION_ADMIN,
                        object,
                        response -> {
                            Log.v("uploadRes", response + " ");
                        }, error -> {

                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
//                        headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void navigateToShareScreen(String shareUrl) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String name = SharedHelper.getKey(context, "first_name") + " " + SharedHelper.getKey(context, "last_name");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "TRANXIT-" + "Mr/Mrs." + name + " would like to share a trip with you at " +
                    shareUrl + current_lat + "," + current_lng);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Share applications not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSosPopUp() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setTitle(context.getString(R.string.app_name))
                .setIcon(R.drawable.ic_launcher_web)
                .setMessage(getString(R.string.emaergeny_call))
                .setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {

            Intent intentCall = new Intent(Intent.ACTION_DIAL);
            intentCall.setData(Uri.parse("tel:" + SharedHelper.getKey(context, "sos")));
            startActivity(intentCall);

        });
        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showCancelRideDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setTitle(context.getString(R.string.app_name))
                .setIcon(R.drawable.app_logo_org)
                .setMessage(getString(R.string.cancel_ride_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> showreasonDialog());
        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss());
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showreasonDialog() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.cancel_dialog, null);
        final EditText reasonEtxt = view.findViewById(R.id.reason_etxt);
        Button submitBtn = view.findViewById(R.id.submit_btn);
        RadioGroup radioCancel = view.findViewById(R.id.radioCancel);
        radioCancel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.driver) {
                    reasonEtxt.setVisibility(View.VISIBLE);
                    cancaltype = getContext().getString(R.string.plan_changed);
                }
                if (checkedId == R.id.vehicle) {
                    reasonEtxt.setVisibility(View.VISIBLE);
                    cancaltype = getContext().getString(R.string.booked_another_cab);
                }
                if (checkedId == R.id.app) {
                    reasonEtxt.setVisibility(View.VISIBLE);
                    cancaltype = getContext().getString(R.string.my_reason_is_not_listed);
                }
                if (checkedId == R.id.denied) {
                    reasonEtxt.setVisibility(View.VISIBLE);
                    cancaltype = getContext().getString(R.string.driver_denied_to_come);
                }
                if (checkedId == R.id.moving) {
                    reasonEtxt.setVisibility(View.VISIBLE);
                    cancaltype = getContext().getString(R.string.driver_is_not_moving);
                }
            }
        });
        builder.setView(view)
                .setCancelable(true);
        final androidx.appcompat.app.AlertDialog dialog = builder.create();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cancaltype.isEmpty()) {
                    Toast.makeText(getActivity(), getContext().getString(R.string.please_select_reason), Toast.LENGTH_SHORT).show();

                } else {
                    cancalReason = reasonEtxt.getText().toString();
                    if (cancalReason.isEmpty()) {
                        reasonEtxt.setError(getContext().getString(R.string.please_specify_reason));
                    } else {
                        cancelRequest();
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();

//        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View view = LayoutInflater.from(context).inflate(R.layout.cancel_dialog, null);
//        final EditText reasonEtxt = view.findViewById(R.id.reason_etxt);
//        Button submitBtn = view.findViewById(R.id.submit_btn);
//        builder.setIcon(R.drawable.appicon)
//                .setTitle(R.string.app_name)
//                .setView(view)
//                .setCancelable(true);
//        final AlertDialog dialog = builder.create();
//        submitBtn.setOnClickListener(v -> {
//            cancalReason = reasonEtxt.getText().toString();
//            cancelRequest();
//            dialog.dismiss();
//        });
//        dialog.show();
    }

    void layoutChanges() {
        try {
            utils.hideKeypad(getActivity(), getActivity().getCurrentFocus());
            if (lnrApproximate.getVisibility() == View.VISIBLE) {
                lnrApproximate.startAnimation(slide_down);
            } else if (ScheduleLayout.getVisibility() == View.VISIBLE) {
                ScheduleLayout.startAnimation(slide_down);
//                lnrRequestProviders.setVisibility(View.GONE);
            } else if (lnrProviderPopup.getVisibility() == View.VISIBLE) {
                lnrProviderPopup.startAnimation(slide_down);
                lnrSearchAnimation.startAnimation(slide_up_down);
                lnrSearchAnimation.setVisibility(View.VISIBLE);
            } else if (lnrInvoice.getVisibility() == View.VISIBLE) {
                lnrInvoice.startAnimation(slide_down);
            } else if (lnrRateProvider.getVisibility() == View.VISIBLE) {
                lnrRateProvider.startAnimation(slide_down);
            } else if (lnrInvoice.getVisibility() == View.VISIBLE) {
                lnrInvoice.startAnimation(slide_down);
            }
            if (lnrWaitingForProviders.getVisibility() == View.VISIBLE) {
                lnrRequestProviders.setVisibility(View.GONE);
            } else {
                lnrRequestProviders.setVisibility(View.GONE);
            }
            lnrProviderPopup.setVisibility(View.GONE);
            lnrApproximate.setVisibility(View.GONE);
            lnrWaitingForProviders.setVisibility(View.GONE);
            lnrProviderAccepted.setVisibility(View.GONE);
            lnrInvoice.setVisibility(View.GONE);
            lnrRateProvider.setVisibility(View.GONE);
            ScheduleLayout.setVisibility(View.GONE);
            rtlStaticMarker.setVisibility(View.GONE);
//            frmDestination.setVisibility(View.GONE);
            sourceDestLayout.setVisibility(View.GONE);
            imgMenu.setVisibility(View.GONE);
            imgBack.setVisibility(View.GONE);
            layoutSrcDest.setVisibility(View.GONE);
            shadowBack.setVisibility(View.GONE);
            txtCommentsRate.setText("");
            scheduleDate.setText("" + context.getString(R.string.sample_date));
            scheduleTime.setText("" + context.getString(R.string.sample_time));
            if (flowValue == 0) {
                if (imgMenu.getVisibility() == View.GONE) {

                    lnrRequestProviders.setVisibility(View.VISIBLE);
                    frmSource.setOnClickListener(new PublishFragment.OnClick());
                    frmDest.setOnClickListener(new PublishFragment.OnClick());
                    sourceDestLayout.setOnClickListener(null);
                    if (mMap != null) {
                        mMap.clear();
                        stopAnim();
                        setupMap();
                    }
                    sourceDestLayout.setVisibility(View.GONE);
                }


//                frmDestination.setVisibility(View.VISIBLE);
                sourceDestLayout.setVisibility(View.VISIBLE);
                imgMenu.setVisibility(View.VISIBLE);
                destination.setText("");
                frmDest.setText("");
                frmSource.setText("" + current_address);
                dest_address = "";
                dest_lat = "";
                dest_lng = "";
                source_lat = "" + current_lat;
                source_lng = "" + current_lng;
                source_address = "" + current_address;
                sourceAndDestinationLayout.setVisibility(View.VISIBLE);
                paymentLayout.setVisibility(View.GONE);
            } else if (flowValue == 1) {
                frmSource.setVisibility(View.VISIBLE);
                destinationBorderImg.setVisibility(View.GONE);
                sourceDestLayout.setVisibility(View.GONE);


                imgBack.setVisibility(View.VISIBLE);
                layoutSrcDest.setVisibility(View.GONE);
                try {
                    System.out.println("tracking path Started :");
                    trackPickToDest();
                } catch (Exception e) {
                    System.out.println("tracking path error :" + e.getMessage());
                }
                addStopOverLL.setVisibility(View.VISIBLE);
                lnrRequestProviders.startAnimation(slide_up);
                lnrRequestProviders.setVisibility(View.VISIBLE);
                if (!Double.isNaN(wallet_balance) && wallet_balance > 0) {
                    if (lineView != null && chkWallet != null) {
                        lineView.setVisibility(View.GONE);
                        chkWallet.setVisibility(View.GONE);
                    }
                } else {
                    if (lineView != null && chkWallet != null) {
                        lineView.setVisibility(View.GONE);
                        chkWallet.setVisibility(View.GONE);
                    }
                }
//                chkWallet.setChecked(false);
                if (sourceMarker != null && destinationMarker != null) {
                    sourceMarker.setDraggable(true);
                    destinationMarker.setDraggable(true);
                }
            } else if (flowValue == 2) {

                lnrRequestProviders.setVisibility(View.GONE);
//                frmDestination.setVisibility(View.GONE);
                sourceDestLayout.setVisibility(View.GONE);
                imgBack.setVisibility(View.VISIBLE);
                layoutSrcDest.setVisibility(View.GONE);
//                chkWallet.setChecked(false);
                lnrApproximate.startAnimation(slide_up);
                lnrApproximate.setVisibility(View.VISIBLE);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mMap.setPadding(0, 0, 0, lnrApproximate.getHeight());
                    }
                });
                if (sourceMarker != null && destinationMarker != null) {
                    sourceMarker.setDraggable(false);
                    destinationMarker.setDraggable(false);
                }
            } else if (flowValue == 3) {
                lnrRequestProviders.setVisibility(View.GONE);
                imgBack.setVisibility(View.VISIBLE);
                layoutSrcDest.setVisibility(View.GONE);
                lnrWaitingForProviders.setVisibility(View.VISIBLE);
                sourceDestLayout.setVisibility(View.GONE);
                //sourceAndDestinationLayout.setVisibility(View.GONE);
            } else if (flowValue == 4) {
                lnrRequestProviders.setVisibility(View.GONE);
                imgMenu.setVisibility(View.VISIBLE);
                sourceDestLayout.setVisibility(View.GONE);
                lnrProviderAccepted.startAnimation(slide_up);


                lnrProviderAccepted.setVisibility(View.VISIBLE);
            } else if (flowValue == 5) {
                sourceDestLayout.setVisibility(View.GONE);
                imgMenu.setVisibility(View.VISIBLE);
                lnrInvoice.startAnimation(slide_up);
                lnrInvoice.setVisibility(View.VISIBLE);
            } else if (flowValue == 6) {
                imgMenu.setVisibility(View.VISIBLE);
                lnrRateProvider.startAnimation(slide_up);
                lnrRateProvider.setVisibility(View.VISIBLE);
                sourceDestLayout.setVisibility(View.GONE);
                LayerDrawable drawable = (LayerDrawable) ratingProviderRate.getProgressDrawable();
                drawable.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
                drawable.getDrawable(1).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
                drawable.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
                ratingProviderRate.setRating(1.0f);
                feedBackRating = "1";
                ratingProviderRate.setOnRatingBarChangeListener((ratingBar, rating, b) -> {
                    if (rating < 1.0f) {
                        ratingProviderRate.setRating(1.0f);
                        feedBackRating = "1";
                    }
                    feedBackRating = String.valueOf((int) rating);
                });
            } else if (flowValue == 7) {
                imgBack.setVisibility(View.VISIBLE);
                layoutSrcDest.setVisibility(View.GONE);
                ScheduleLayout.startAnimation(slide_up);
                ScheduleLayout.setVisibility(View.VISIBLE);
                sourceDestLayout.setVisibility(View.GONE);
                lnrRequestProviders.setVisibility(View.GONE);
            } else if (flowValue == 8) {
                // clear all views
                shadowBack.setVisibility(View.GONE);
            } else if (flowValue == 9) {
                sourceDestLayout.setVisibility(View.GONE);
                rtlStaticMarker.setVisibility(View.VISIBLE);
                shadowBack.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_json));

            if (!success) {
                Utilities.print("Map:Style", "Style parsing failed.");
            } else {
                Utilities.print("Map:Style", "Style Applied.");
            }
        } catch (Resources.NotFoundException e) {
            Utilities.print("Map:Style", "Can't find style. Error: ");
        }

        mMap = googleMap;

        setupMap();
        googleMap.setOnCameraChangeListener(cameraPosition -> {
            LatLng target = cameraPosition.target;
            if (pick_first) {
                cmPosition = cameraPosition;
                updateLocation(target);
            }
        });
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
        }
    }

    private void updateLocation(LatLng centerLatLng) {
        if (centerLatLng != null) {
            Geocoder geocoder = new Geocoder(context,
                    Locale.getDefault());

            List<Address> addresses = new ArrayList<Address>();
            try {
                addresses = geocoder.getFromLocation(centerLatLng.latitude,
                        centerLatLng.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {

                String addressIndex0 = addresses
                        .get(0).getAddressLine(0);
                String addressIndex1 = addresses
                        .get(0).getAddressLine(1);
                String addressIndex2 = addresses
                        .get(0).getAddressLine(2);
                String addressIndex3 = addresses
                        .get(0).getAddressLine(3);
                String completeAddress = addressIndex0 + "," + addressIndex1;

                if (addressIndex2 != null) {
                    completeAddress += "," + addressIndex2;
                }
                if (addressIndex3 != null) {
                    completeAddress += "," + addressIndex3;
                }
                if (completeAddress != null) {
                    //mLocationTextView.setText(completeAddress);
                    txtSelectedAddressSource.setText(completeAddress);
                }
            }
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    1);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        String title = "";

        if (marker != null && marker.getTitle() != null) {
            title = marker.getTitle();

            if (sourceMarker != null && title.equalsIgnoreCase("Source")) {
                LatLng markerLocation = sourceMarker.getPosition();
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                source_lat = markerLocation.latitude + "";
                source_lng = markerLocation.longitude + "";

                try {
                    addresses = geocoder.getFromLocation(markerLocation.latitude, markerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    if (addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        SharedHelper.putKey(context, "source", "" + address + "," + city + "," + state);
                        source_address = "" + address + "," + city + "," + state;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (destinationMarker != null && title.equalsIgnoreCase("Destination")) {
                LatLng markerLocation = destinationMarker.getPosition();
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                dest_lat = "" + markerLocation.latitude;
                dest_lng = "" + markerLocation.longitude;

                try {
                    addresses = geocoder.getFromLocation(markerLocation.latitude, markerLocation.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    if (addresses.size() > 0) {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        SharedHelper.putKey(context, "destination", "" + address + "," + city + "," + state);
                        dest_address = "" + address + "," + city + "," + state;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            mMap.clear();
            setValuesForSourceAndDestination();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //Toast.makeText(SignInActivity.this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
                    initMap();
                    MapsInitializer.initialize(getActivity());
                } /*else {
                    showPermissionReqDialog();
                }*/
                break;
            case 2:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + SharedHelper.getKey(context, "provider_mobile_no")));
                startActivity(intent);
                break;
            case 3:
                Intent intentDial = new Intent(Intent.ACTION_DIAL);
                intentDial.setData(Uri.parse("tel:" + SharedHelper.getKey(context, "sos")));
                startActivity(intentDial);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showDialogForGPSIntent() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setTitle(context.getString(R.string.app_name))
                .setIcon(R.drawable.app_logo_org)
                .setMessage("GPS is disabled in your device. Enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivity(callGPSSettingIntent);
                        });
        builder.setNegativeButton("Cancel",
                (dialog, id) -> dialog.cancel());
        androidx.appcompat.app.AlertDialog alert1 = builder.create();
        alert1.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: " + requestCode + " Result Code " + resultCode);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST) {
            if (parserTask != null) {
                parserTask = null;
            }
            if (resultCode == Activity.RESULT_OK) {
                if (marker != null) {
                    marker.remove();
                }

                placePredictions = (PlacePredictions) data.getSerializableExtra("Location Address");
                strPickLocation = data.getExtras().getString("pick_location");
                strPickType = data.getExtras().getString("type");
                strPickForStopOverOrNot = data.getExtras().getString("strPickForStopOverOrNot");

                Toast.makeText(activity, "Processing", Toast.LENGTH_SHORT).show();
                rootView.findViewById(R.id.mapLayout).setAlpha(1);



                if (strPickLocation.equalsIgnoreCase("yes")) {
                    pick_first = true;
                    mMap.clear();
                    flowValue = 9;
                    layoutChanges();
                    float zoomLevel = 16.0f; //This goes up to 21
                    stopAnim();
                } else {
                    if (placePredictions != null) {
                        if (!placePredictions.strSourceAddress.equalsIgnoreCase("")) {
                            source_lat = "" + placePredictions.strSourceLatitude;
                            source_lng = "" + placePredictions.strSourceLongitude;
                            source_address = placePredictions.strSourceAddress;

                            if (!placePredictions.strSourceLatitude.equalsIgnoreCase("")
                                    && !placePredictions.strSourceLongitude.equalsIgnoreCase("")) {

                                System.out.println("SOURCE : "+source_address);
                                double latitude = Double.parseDouble(placePredictions.strSourceLatitude);
                                double longitude = Double.parseDouble(placePredictions.strSourceLongitude);
                                LatLng location = new LatLng(latitude, longitude);

                                //mMap.clear();
                                try {
                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .anchor(0.5f, 0.75f)
                                            .position(location)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
                                    marker = mMap.addMarker(markerOptions);
                                    sourceMarker = mMap.addMarker(markerOptions);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                               /* CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(16).build();
                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
                            }

                        }
                        if (!placePredictions.strDestAddress.equalsIgnoreCase("")) {
                            dest_lat = "" + placePredictions.strDestLatitude;
                            dest_lng = "" + placePredictions.strDestLongitude;
                            dest_address = placePredictions.strDestAddress;
                            dropLocationName = dest_address;

                            SharedHelper.putKey(context, "current_status", "2");
                            if (source_lat != null && source_lng != null && !source_lng.equalsIgnoreCase("")
                                    && !source_lat.equalsIgnoreCase("")) {
                                String url = getUrl(Double.parseDouble(source_lat), Double.parseDouble(source_lng)
                                        , Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));

                                current_lat = source_lat;
                                current_lng = source_lng;
                                //  getNewApproximateFare("1");
                                //  getNewApproximateFare2("2");
                                PublishFragment.FetchUrl fetchUrl = new PublishFragment.FetchUrl();
                                fetchUrl.execute(url);
                                LatLng location = new LatLng(Double.parseDouble(current_lat), Double.parseDouble(current_lng));


                                //mMap.clear();
                                if (sourceMarker != null)
                                    sourceMarker.remove();
//                                MarkerOptions markerOptions = new MarkerOptions()
//                                        .anchor(0.5f, 0.75f)
//                                        .position(location)
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
//                                marker = mMap.addMarker(markerOptions);
//                                sourceMarker = mMap.addMarker(markerOptions);
                               /* CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(14).build();
                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
                            }

                            try {
                                if (!dest_lat.equalsIgnoreCase("") && !dest_lng.equalsIgnoreCase("")) {
                                destLatLng = new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
                                if (destinationMarker != null)
                                    destinationMarker.remove();
//                                MarkerOptions destMarker = new MarkerOptions()
//                                        .position(destLatLng)
//                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_marker));
//                                destinationMarker = mMap.addMarker(destMarker);
//                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                                builder.include(sourceMarker.getPosition());
//                                builder.include(destinationMarker.getPosition());
//                                LatLngBounds bounds = builder.build();
//                                int padding = 200; // offset from edges of the map in pixels
//                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//                                mMap.moveCamera(cu);

                                /*LatLng myLocation = new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
                                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
                            }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        if (dest_address.equalsIgnoreCase("")) {
                            flowValue = 1;
                            frmSource.setText(source_address);
//                            getValidZone();
//                            getServiceList();
                        } else {
                            flowValue = 1;

                            if (cardInfoArrayList.size() > 0) {
                                getCardDetailsForPayment(cardInfoArrayList.get(0));
                                sourceDestLayout.setVisibility(View.VISIBLE);
                            }
//                            getValidZone();
//                            paymentLayout.setVisibility(View.VISIBLE);
//                            getServiceList();
                        }

                        if(!dest_address.equalsIgnoreCase("") && !source_address.equalsIgnoreCase("")){
                            System.out.println("setting dest and source address");
                            setValuesForSourceAndDestination();
                        }

                        layoutChanges();
                    }
                }




            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST_STOPOVER) {
            if (parserTask != null) {
                parserTask = null;
            }
            if (resultCode == Activity.RESULT_OK) {
                if (marker != null) {
                    marker.remove();
                }

                placePredictionsSO = (PlacePredictions) data.getSerializableExtra("Location Address");
//                strPickLocationSO = data.getExtras().getString("pick_location");
//                strPickTypeSO = data.getExtras().getString("type");

                strPickLocationSO = data.getExtras().getString("pick_locationSO");
                strPickTypeSO = data.getExtras().getString("typeSO");
                strPickForStopOverOrNot = data.getExtras().getString("strPickForStopOverOrNot");


                Toast.makeText(activity, "Processing", Toast.LENGTH_SHORT).show();

                rootView.findViewById(R.id.mapLayout).setAlpha(1);


                if (strPickLocationSO.equalsIgnoreCase("yes")) {
                    pick_firstSO = true;
//                    commenting below line for testing
//                    mMap.clear();
                    flowValue = 9;
                    layoutChanges();
                    float zoomLevel = 16.0f; //This goes up to 21
                    stopAnim();
                } else {
                    System.out.println("placePredictionsSO : "+placePredictionsSO);
                    if (placePredictionsSO != null) {
                        System.out.println("placePredictionsSO strDestAddress : "+placePredictionsSO.strDestAddress);
                        System.out.println("placePredictionsSO strDestAddress : "+placePredictionsSO.strSourceAddress);

                        if (!placePredictionsSO.strDestAddress.equalsIgnoreCase("")) {
                            source_latSO = "" + placePredictionsSO.strDestLatitude;
                            source_lngSO = "" + placePredictionsSO.strDestLongitude;
                            source_addressSO = placePredictionsSO.strDestAddress;

                            System.out.println("source_latSO : "+source_latSO);
                            System.out.println("source_lngSO : "+source_lngSO);
                            System.out.println("source_addressSO : "+source_addressSO);
                            System.out.println("placePredictionsSO.strSourceAddress : "+placePredictionsSO.strSourceAddress);
                            System.out.println("placePredictionsSO.strDestAddress : "+placePredictionsSO.strDestAddress);


//                            Toast.makeText(getContext(), "stopoverlocation : " + source_addressSO, Toast.LENGTH_SHORT).show();
                            System.out.println("stopoverlocation : " + source_addressSO);

                            if (!placePredictionsSO.strDestLatitude.equalsIgnoreCase("")
                                    && !placePredictionsSO.strDestLongitude.equalsIgnoreCase("")) {
                                double latitude = Double.parseDouble(placePredictionsSO.strDestLatitude);
                                double longitude = Double.parseDouble(placePredictionsSO.strDestLongitude);
                                LatLng location = new LatLng(latitude, longitude);

                                //mMap.clear();
//                                try {
//                                    MarkerOptions markerOptions = new MarkerOptions()
//                                            .anchor(0.5f, 0.75f)
//                                            .position(location)
//                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
//                                    marker = mMap.addMarker(markerOptions);
//                                    sourceMarker = mMap.addMarker(markerOptions);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                               /* CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(16).build();
                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/


                                stopOverModelArrayList.add(new StopOverModel(source_latSO,source_lngSO,source_addressSO));

                                try {
                                    JSONObject jsonObjectStopOver = new JSONObject();
                                    jsonObjectStopOver.put("lat",source_latSO);
                                    jsonObjectStopOver.put("lng",source_lngSO);
                                    jsonObjectStopOver.put("area",source_addressSO);

                                    jsonArrayStopOver.put(jsonObjectStopOver);

//                                trackPickToDest(source_lat,source_lng,source_latSO,source_lngSO);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }





//                            stepOverAdapter = new StepOverAdapter(getContext(), stopOverModelArrayList);
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//                            stopoverRv.setLayoutManager(linearLayoutManager);
//                            stopoverRv.setNestedScrollingEnabled(false);

                                stopOverTitleTv.setVisibility(View.VISIBLE);
                                stepOverOrgAdapter = new StepOverOrgAdapter(getContext(), stopOverModelArrayList);
                                stopoverRv.setAdapter(stepOverOrgAdapter);



                                System.out.println("stopover : "+jsonArrayStopOver.toString() );
                                try {
                                    trackPickToDest(source_lat,source_lng,source_latSO,source_lngSO);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                        }

//                        if (!placePredictionsSO.strDestAddress.equalsIgnoreCase("")) {
//                            dest_latSO = "" + placePredictionsSO.strDestLatitude;
//                            dest_lngSO = "" + placePredictionsSO.strDestLongitude;
//                            dest_addressSO = placePredictionsSO.strDestAddress;
//                            dropLocationName = dest_addressSO;
//
//
//
//
//                            Toast.makeText(getContext(), "stopoverlocation dest : " + dest_addressSO, Toast.LENGTH_SHORT).show();
//                            System.out.println("stopoverlocation : " + dest_addressSO);
//
//                            stopOverModelArrayList.add(new StopOverModel(source_latSO,source_lngSO,source_addressSO));
//
//                            try {
//                                JSONObject jsonObjectStopOver = new JSONObject();
//                                jsonObjectStopOver.put("lat",source_latSO);
//                                jsonObjectStopOver.put("lng",source_lngSO);
//                                jsonObjectStopOver.put("area",source_addressSO);
//
//                                jsonArrayStopOver.put(jsonObjectStopOver);
//
////                                trackPickToDest(source_lat,source_lng,source_latSO,source_lngSO);
//
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//
//
//                            stopOverTitleTv.setVisibility(View.VISIBLE);
//                            stepOverOrgAdapter = new StepOverOrgAdapter(getContext(), stopOverModelArrayList);
//                            stopoverRv.setAdapter(stepOverOrgAdapter);
//
//
//
//                            System.out.println("stopover : "+jsonArrayStopOver.toString() );
//                            try {
//                                trackPickToDest(source_lat,source_lng,dest_latSO,dest_lngSO);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//
//
//                            SharedHelper.putKey(context, "current_status", "2");
//                            if (source_latSO != null && source_lngSO != null && !source_lngSO.equalsIgnoreCase("")
//                                    && !source_latSO.equalsIgnoreCase("")) {
//                                String url = getUrl(Double.parseDouble(source_latSO), Double.parseDouble(source_lngSO)
//                                        , Double.parseDouble(dest_latSO), Double.parseDouble(dest_lngSO));
//
//                                current_latSO = source_latSO;
//                                current_lngSO = source_lngSO;
//                                //  getNewApproximateFare("1");
//                                //  getNewApproximateFare2("2");
//                                PublishFragment.FetchUrl fetchUrl = new PublishFragment.FetchUrl();
//                                fetchUrl.execute(url);
//                                LatLng location = new LatLng(Double.parseDouble(current_latSO), Double.parseDouble(current_lngSO));
//
//
//                                //mMap.clear();
//                                if (sourceMarker != null)
//                                    sourceMarker.remove();
////                                MarkerOptions markerOptions = new MarkerOptions()
////                                        .anchor(0.5f, 0.75f)
////                                        .position(location)
////                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
////                                marker = mMap.addMarker(markerOptions);
////                                sourceMarker = mMap.addMarker(markerOptions);
//                               /* CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(14).build();
//                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
//                            }
//
//                            if (!dest_latSO.equalsIgnoreCase("") && !dest_lngSO.equalsIgnoreCase("")) {
//                                destLatLngSO = new LatLng(Double.parseDouble(dest_latSO), Double.parseDouble(dest_lngSO));
//
//                                if (destinationMarker != null)
//                                    destinationMarker.remove();
////                                MarkerOptions destMarker = new MarkerOptions()
////                                        .position(destLatLngSO)
////                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_marker));
////                                destinationMarker = mMap.addMarker(destMarker);
////                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
////                                builder.include(sourceMarker.getPosition());
////                                builder.include(destinationMarker.getPosition());
////                                LatLngBounds bounds = builder.build();
////                                int padding = 200; // offset from edges of the map in pixels
////                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
////                                try {
////                                    mMap.moveCamera(cu);
////                                }catch (Exception e){
////                                    e.printStackTrace();
////                                }
//
//
//                                /*LatLng myLocation = new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
//                                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
//                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
//                            }
//                        }

                        if (dest_address.equalsIgnoreCase("")) {
                            flowValue = 1;
                            frmSource.setText(source_address);
//                            getValidZone();
//                            getServiceList();
                        } else {
                            flowValue = 1;

                            if (cardInfoArrayList.size() > 0) {
                                getCardDetailsForPayment(cardInfoArrayList.get(0));
//                                sourceDestLayout.setVisibility(View.GONE);
                            }
//                            getValidZone();
//                            paymentLayout.setVisibility(View.VISIBLE);
//                            getServiceList();
                        }

                        if(!dest_addressSO.equalsIgnoreCase("") && !source_addressSO.equalsIgnoreCase("")){
                            System.out.println("setting dest and source address");
                            setValuesForSourceAndDestination();
                        }

                        layoutChanges();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == ADD_CARD_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean result = data.getBooleanExtra("isAdded", false);
                if (result) {
                    getCards();
                }
            }
        }
        if (requestCode == 0000) {
            if (resultCode == Activity.RESULT_OK) {
                lblPromo.setText(getString(R.string.promocode_applied));
            }
        }
        if (requestCode == 5555) {
            if (resultCode == Activity.RESULT_OK) {
                CardInfo cardInfo = data.getParcelableExtra("card_info");
                getCardDetailsForPayment(cardInfo);
            }
        }

        if (requestCode == 10) {
            String result = data.getStringExtra("paymentSuccessful");
        }
        if (requestCode == REQUEST_LOCATION) {

        } else {

        }
    }

    public void setValuesForApproximateLayout() {
        if (isInternet) {
            String surge = SharedHelper.getKey(context, "surge");
            if (surge.equalsIgnoreCase("1")) {
                surgeDiscount.setVisibility(View.VISIBLE);
                surgeTxt.setVisibility(View.VISIBLE);
                surgeDiscount.setText(SharedHelper.getKey(context, "surge_value"));
            } else {
                surgeDiscount.setVisibility(View.GONE);
                surgeTxt.setVisibility(View.GONE);
            }


            lblCmfrmSourceAddress.setText(source_address);
            lblCmfrmDestAddress.setText(dest_address);
            lblApproxAmount.setText(SharedHelper.getKey(context, "currency") + "" + SharedHelper.getKey(context, "estimated_fare"));
            lblEta.setText(SharedHelper.getKey(context, "eta_time").replace("mins", "Min"));
            lblDis.setText(SharedHelper.getKey(context, "distance") + " KM");
            if (!SharedHelper.getKey(context, "name").equalsIgnoreCase("")
                    && !SharedHelper.getKey(context, "name").equalsIgnoreCase(null)
                    && !SharedHelper.getKey(context, "name").equalsIgnoreCase("null")) {
                lblType.setText(SharedHelper.getKey(context, "name"));
            } else {
                lblType.setText("" + "Sedan");
            }

            if ((customDialog != null) && (customDialog.isShowing()))
                customDialog.dismiss();
        }
    }

    private void getCards() {
        Ion.with(getActivity())
                .load(URLHelper.CARD_PAYMENT_LIST)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization",
                        SharedHelper.getKey(context, "token_type") + " "
                                + SharedHelper.getKey(context, "access_token"))
                .asString()
                .withResponse()
                .setCallback((e, response) -> {
                    // response contains both the headers and the string result
                    e.printStackTrace();
                    try {
                        if (response.getHeaders().code() == 200) {
                            try {
                                JSONArray jsonArray = new JSONArray(response.getResult());
                                if (jsonArray.length() > 0) {
                                    CardInfo cardInfo = new CardInfo();
                                    cardInfo.setCardId("CASH");
                                    cardInfo.setCardType("CASH");
                                    cardInfo.setLastFour("CASH");
                                    cardInfoArrayList.add(cardInfo);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject cardObj = jsonArray.getJSONObject(i);
                                        cardInfo = new CardInfo();
                                        cardInfo.setCardId(cardObj.optString("card_id"));
                                        cardInfo.setCardType(cardObj.optString("brand"));
                                        cardInfo.setLastFour(cardObj.optString("last_four"));
                                        cardInfoArrayList.add(cardInfo);
                                    }
                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        CardInfo cardInfo = new CardInfo();
                        cardInfo.setCardId("CASH");
                        cardInfo.setCardType("CASH");
                        cardInfo.setLastFour("CASH");
                        cardInfoArrayList.add(cardInfo);
                    }
                });

    }



    public void getApproximateFare() {

        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();
        JSONObject object = new JSONObject();
        String constructedURL = URLHelper.ESTIMATED_FARE_DETAILS_API + "" +
                "?s_latitude=" + source_lat
                + "&s_longitude=" + source_lng
                + "&d_latitude=" + dest_lat
                + "&d_longitude=" + dest_lng
                + "&service_type=" + SharedHelper.getKey(context, "service_type");
        System.out.println("getNewApproximateFare getNewApproximateFare " + constructedURL);
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        constructedURL,
                        object,
                        response -> {
                            if (response != null) {
                                if ((customDialog != null) && (customDialog.isShowing()))
                                    customDialog.dismiss();
                                if (!response.optString("estimated_fare").equalsIgnoreCase("")) {
                                    Utilities.print("ApproximateResponse", response.toString());
                                    SharedHelper.putKey(context, "estimated_fare", response.optString("estimated_fare"));
                                    SharedHelper.putKey(context, "distance", response.optString("distance"));
                                    SharedHelper.putKey(context, "eta_time", response.optString("time"));
                                    SharedHelper.putKey(context, "surge", response.optString("surge"));
                                    SharedHelper.putKey(context, "surge_value", response.optString("surge_value"));
                                    setValuesForApproximateLayout();
                                    double wallet_balance = response.optDouble("wallet_balance");
                                    SharedHelper.putKey(context, "wallet_balance", "" + response.optDouble("wallet_balance"));

                                    if (!Double.isNaN(wallet_balance) && wallet_balance > 0) {
                                        lineView.setVisibility(View.GONE);
                                        chkWallet.setVisibility(View.GONE);
                                    } else {
                                        lineView.setVisibility(View.GONE);
                                        chkWallet.setVisibility(View.GONE);
                                    }
                                    flowValue = 2;
                                    layoutChanges();
                                }
                            }
                        }, error -> {
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    displayMessage(getActivity().getString(R.string.something_went_wrong));

                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

    }



    public void sendRequest() {


        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

        JSONObject object = new JSONObject();
        try {
            object.put("s_latitude", source_lat);
            object.put("s_longitude", source_lng);
            object.put("d_latitude", dest_lat);
            object.put("d_longitude", dest_lng);
            object.put("s_address", source_address);
            object.put("d_address", dest_address);
            object.put("service_type", "2");
//            object.put("distance", "0");
            if(SharedHelper.getKey(getContext(),"PUBLISHED_DISTANCE") != null){
                object.put("distance", SharedHelper.getKey(getContext(),"PUBLISHED_DISTANCE"));
            }else{
                object.put("distance", "0");
            }

            object.put("schedule_date", scheduledDate);
            object.put("schedule_time", scheduledTime);
            object.put("use_wallet", "0");
            object.put("payment_mode", "CASH");

            if(privateCarCheckBox.isChecked()){
                object.put("isreturn", "1");
            }else{
                object.put("isreturn", "0");
            }
            object.put("returnschedule_date", scheduledDateR);
            object.put("returnschedule_time", scheduledTimeR);
            object.put("stopover",jsonArrayStopOver);
            object.put("fare", fare_amount);
            object.put("passenger", 1);
            object.put("totalFare", 1);
            Utilities.print("SendPublishRequestInput", "" + object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassLuxApp.getInstance().cancelRequestInQueue("send_request");

        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST,
                        URLHelper.PUBLISH_A_RIDE,
                        object,
                        response -> {
                            Utilities.print("SendPublishRequestResponse", "Starting");
                            Utilities.print("SendPublishRequestResponse", response.toString());
                            btnRequestRideConfirm.setEnabled(true);
                            if (response != null) {
                                Utilities.print("SendPublishRequestResponse", response.toString());
                                if ((customDialog != null) && (customDialog.isShowing()))
                                    customDialog.dismiss();

                                if (response.optString("message").equalsIgnoreCase("Ride has been published. ")) {
                                    flowValue = 0;
                                    layoutChanges();
                                    showConfirmDialog();
                                } else {
                                    displayMessage(response.optString("message"));
                                }

//                                if (response.toString().contains("error")) {
//                                    Toast.makeText(getActivity(), response.optString("error"), Toast.LENGTH_LONG).show();
//                                } else if (response.optString("request_id", "").equals("")) {
//                                    if (response.optString("message").equalsIgnoreCase("Ride has been published. ")) {
//                                        flowValue = 0;
//                                        layoutChanges();
//
//                                        showConfirmDialog();
////                                       utils.showAlert(getActivity(),"Your booking has been confirmed ," +
////                                               " you will get driver details 30 Min before of your booking");
//                                    } else {
//                                        displayMessage(response.optString("message"));
//                                    }
//                                } else {
//                                    SharedHelper.putKey(context, "current_status", "");
//                                    SharedHelper.putKey(context, "request_id", "" + response.optString("request_id"));
//                                    scheduleTrip = !scheduledDate.equalsIgnoreCase("") && !scheduledTime.equalsIgnoreCase("");
//                                    // flowValue = 3;
//                                    //layoutChanges();
//                                    flowValue = 0;
//                                    layoutChanges();
//
//                                    Intent intent = new Intent(getActivity(), TrackActivityDriver.class);
//                                    intent.putExtra("flowValue", 3);
//                                    startActivity(intent);
//                                }

                            }
                        }, error -> {
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    displayMessage(getString(R.string.something_went_wrong));
                    System.out.println("Error : " + error.toString());
                    System.out.println("Error : " + error.getMessage());
                    System.out.println("Error : " + error.getCause());

                }) {


                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
                        System.out.println("token type : " + SharedHelper.getKey(context, "token_type"));
                        System.out.println("access_token  : " + SharedHelper.getKey(context, "access_token"));

                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);


//        JsonObjectRequest jsonObjectRequest = new
//                JsonObjectRequest(Request.Method.POST,
//                        URLHelper.PUBLISH_A_RIDE,
//                        object,
//                        response -> {
//                            Utilities.print("SendPublishRequestResponse", "Starting");
//                            Utilities.print("SendPublishRequestResponse", response.toString());
//                            btnRequestRideConfirm.setEnabled(true);
//                            if (response != null) {
//                                Utilities.print("SendPublishRequestResponse", response.toString());
//                                if ((customDialog != null) && (customDialog.isShowing()))
//                                    customDialog.dismiss();
//
//                                if (response.toString().contains("error")) {
//                                    Toast.makeText(getActivity(), response.optString("error"), Toast.LENGTH_LONG).show();
//                                } else if (response.optString("request_id", "").equals("")) {
//                                    if (response.optString("message").equalsIgnoreCase("Ride has been published. ")) {
//                                        flowValue = 0;
//                                        layoutChanges();
//
//                                        showConfirmDialog();
////                                       utils.showAlert(getActivity(),"Your booking has been confirmed ," +
////                                               " you will get driver details 30 Min before of your booking");
//                                    } else {
//                                        displayMessage(response.optString("message"));
//                                    }
//                                } else {
//                                    SharedHelper.putKey(context, "current_status", "");
//                                    SharedHelper.putKey(context, "request_id", "" + response.optString("request_id"));
//                                    scheduleTrip = !scheduledDate.equalsIgnoreCase("") && !scheduledTime.equalsIgnoreCase("");
//                                    // flowValue = 3;
//                                    //layoutChanges();
//                                    flowValue = 0;
//                                    layoutChanges();
//
//                                    Intent intent = new Intent(getActivity(), TrackActivity.class);
//                                    intent.putExtra("flowValue", 3);
//                                    startActivity(intent);
//                                }
//                            }
//                        }, error -> {
//                    if ((customDialog != null) && (customDialog.isShowing()))
//                        customDialog.dismiss();
//                    displayMessage(getString(R.string.something_went_wrong));
//                    System.out.println("Error : "+error.toString());
//
//                }) {
//
//                    @Override
//                    public Map<String, String> getParams() {
//                        Map<String, String> params = new HashMap<>();
//
//
//                        params.put("s_latitude", source_lat);
//                        params.put("s_longitude", source_lng);
//                        params.put("d_latitude",dest_lat );
//                        params.put("d_longitude", dest_lng);
//                        params.put("s_address", source_address);
//                        params.put("d_address", dest_address);
//                        params.put("service_type","2" );
//                        params.put("distance","0" );
//                        params.put("schedule_date",scheduledDate );
//                        params.put("schedule_time",scheduledTime );
//                        params.put("use_wallet", "0");
//                        params.put("payment_mode","CASH" );
//
//                        return params;
//                    }
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        HashMap<String, String> headers = new HashMap<String, String>();
//                        headers.put("X-Requested-With", "XMLHttpRequest");
//                        headers.put("Authorization", "" + SharedHelper.getKey(context, "access_token"));
//                        System.out.println("token type : "+SharedHelper.getKey(context, "token_type"));
//                        System.out.println("access_token  : "+SharedHelper.getKey(context, "access_token"));
//
//                        return headers;
//                    }
//                };


//        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLHelper.PUBLISH_A_RIDE, response -> {
////            dialogCustom.dismiss();
//            String res = new String(response.data);
//
//            Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
//            //displayMessage(getString(R.string.update_success));
//
//
//        }, error -> {
//            displayMessage(getString(R.string.something_went_wrong));
//        }) {
//            @Override
//            public Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("s_latitude", source_lat);
//                params.put("s_longitude", source_lng);
//                params.put("d_latitude",dest_lat );
//                params.put("d_longitude", dest_lng);
//                params.put("s_address", source_address);
//                params.put("d_address", dest_address);
//                params.put("service_type","2" );
//                params.put("distance","0" );
//                params.put("schedule_date",scheduledDate );
//                params.put("schedule_time",scheduledTime );
//                params.put("use_wallet", "0");
//                params.put("payment_mode","CASH" );
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
//                return headers;
//            }
//        };
//        ClassLuxApp.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    private void showConfirmDialog() {
        Dialog confirmDialog = new Dialog(getActivity());
        confirmDialog.setContentView(R.layout.schedule_dialog);

        TextView tvDone = confirmDialog.findViewById(R.id.tvDone);
        TextView bookingStatusTitleTv = confirmDialog.findViewById(R.id.bookingStatusTitleTv);
        TextView bookingStatusSubTitleTv = confirmDialog.findViewById(R.id.bookingStatusSubTitleTv);
        TextView tvDriverMsg = confirmDialog.findViewById(R.id.tvDriverMsg);

        bookingStatusTitleTv.setText("Published Successful");

        bookingStatusSubTitleTv.setText("Your ride has been published successfully ");

        tvDriverMsg.setText("");

        confirmDialog.show();
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                Intent intent = new Intent(getContext(), HomeScreenActivity.class);
                startActivity(intent);
            }
        });
    }

    public void cancelRequest() {

        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("request_id", SharedHelper.getKey(context, "request_id"));
            object.put("cancel_reason", cancalReason);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST,
                        URLHelper.CANCEL_REQUEST_API,
                        object, response -> {
                    Utilities.print("CancelRequestResponse", response.toString());
                    Toast.makeText(context, getResources().getString(R.string.request_cancel), Toast.LENGTH_SHORT).show();
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    mapClear();
                    SharedHelper.putKey(context, "request_id", "");
                    flowValue = 0;
                    PreviousStatus = "";
                    layoutChanges();
                    setupMap();
                }, error -> {
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void setValuesForSourceAndDestination() {
        if (isInternet) {
            if (!source_lat.equalsIgnoreCase("")) {
                if (!source_address.equalsIgnoreCase("")) {
                    frmSource.setText(source_address);
                } else {
                    frmSource.setText(current_address);
                }
            } else {
                frmSource.setText(current_address);
            }

            /***************************************CHANGES HERE TO HIDE SOURCE ADDRESS AND DESTINATION ADDRESS TEXTVIEW***********************************************/

            if (!dest_lat.equalsIgnoreCase("")) {
                destination.setText(dest_address);
//                frmDestination.setVisibility(View.GONE);
//                sourceDestLayout.setVisibility(View.VISIBLE);
                frmDest.setText(dest_address);

            }

            /***************************************CHANGES HERE TO HIDE SOURCE ADDRESS AND DESTINATION ADDRESS TEXTVIEW***********************************************/

            if (!source_lat.equalsIgnoreCase("") && !source_lng.equalsIgnoreCase("")) {
                sourceLatLng = new LatLng(Double.parseDouble(source_lat), Double.parseDouble(source_lng));
            }
            if (!dest_lat.equalsIgnoreCase("") && !dest_lng.equalsIgnoreCase("")) {
                destLatLng = new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
            }

            if (sourceLatLng != null && destLatLng != null) {
                Utilities.print("LatLng", "Source:" + sourceLatLng + " Destination: " + destLatLng);
                // String url = getDirectionsUrl(sourceLatLng, destLatLng);
               /* DownloadTask downloadTask = new DownloadTask();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);*/
                pickUpLocationName = source_address;


                String url = getUrl(sourceLatLng.latitude, sourceLatLng.longitude, destLatLng.latitude, destLatLng.longitude);
                PublishFragment.FetchUrl fetchUrl = new PublishFragment.FetchUrl();
                fetchUrl.execute(url);
            }

        }
    }

    private void showChooser() {
        Intent intent = new Intent(getActivity(), Payment.class);
        startActivityForResult(intent, 5555);
    }

    private void getCardDetailsForPayment(CardInfo cardInfo) {
        if (cardInfo.getLastFour().equals("CASH")) {
            SharedHelper.putKey(context, "payment_mode", "CASH");
            //   imgPaymentType.setImageResource(R.drawable.money1);
            lblPaymentType.setText("CASH");
            chkWallet.setChecked(false);
        } else if (cardInfo.getLastFour().equals("PAYPAL")) {
            chkWallet.setChecked(false);
            SharedHelper.putKey(context, "payment_mode", "PAYPAL");
            //   imgPaymentType.setImageResource(R.drawable.money1);
            lblPaymentType.setText("PAYPAL");
        } else if (cardInfo.getLastFour().equals("RAZORPAY")) {
            chkWallet.setChecked(false);
            SharedHelper.putKey(context, "payment_mode", "RAZORPAY");
            //   imgPaymentType.setImageResource(R.drawable.money1);
            lblPaymentType.setText("RAZORPAY");
        } else if (cardInfo.getLastFour().equals("WALLET")) {
            chkWallet.setChecked(true);
            SharedHelper.putKey(context, "payment_mode", "CASH");
            //   imgPaymentType.setImageResource(R.drawable.money1);
            lblPaymentType.setText(getString(R.string.action_wallet));

        } else {

            SharedHelper.putKey(context, "card_id", cardInfo.getCardId());
//            SharedHelper.putKey(context, "payment_mode", "M-Pesa");
            SharedHelper.putKey(context, "payment_mode", "CARD");
            imgPaymentType.setImageResource(R.drawable.ic_launcher_web);
            lblPaymentType.setText("xxxx" + cardInfo.getLastFour());
        }
    }

    public void payNow() {
        Log.d(TAG, "payNow: " + lblTotalPrice.getText().toString());
//        confirmFinalPayment(lblTotalPrice.getText().toString());
    }

    private void mapClear() {
        if (parserTask != null)
            parserTask.cancel(true);
        mMap.clear();
        source_lat = "";
        source_lng = "";
        dest_lat = "";
        dest_lng = "";
        if (!current_lat.equalsIgnoreCase("") && !current_lng.equalsIgnoreCase("")) {
            LatLng myLocation = new LatLng(Double.parseDouble(current_lat), Double.parseDouble(current_lng));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void show(final View view) {
        mIsShowing = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(0)
                .setInterpolator(INTERPOLATOR)
                .setDuration(500);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mIsShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a show should hide the view
                mIsShowing = false;
                if (!mIsHiding) {
                    hide(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animator.start();
    }

    private void hide(final View view) {
        mIsHiding = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(view.getHeight())
                .setInterpolator(INTERPOLATOR)
                .setDuration(200);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Prevent drawing the View after it is gone
                mIsHiding = false;
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a hide should show the view
                mIsHiding = false;
                if (!mIsShowing) {
                    show(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animator.start();
    }

    public float getBearing(LatLng oldPosition, LatLng newPosition) {
        double deltaLongitude = newPosition.longitude - oldPosition.longitude;
        double deltaLatitude = newPosition.latitude - oldPosition.latitude;
        double angle = (Math.PI * .5f) - Math.atan(deltaLatitude / deltaLongitude);

        if (deltaLongitude > 0) {
            return (float) angle;
        } else if (deltaLongitude < 0) {
            return (float) (angle + Math.PI);
        } else if (deltaLatitude < 0) {
            return (float) Math.PI;
        }

        return 0.0f;
    }

    public void statusCheck() {
        if (getActivity() != null) {
            final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                enableLoc();
            }
        }
    }

    private void enableLoc() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d("Location error", "Connected");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        mGoogleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(connectionResult -> Log.d("Location error", "Location error " + connectionResult.getErrorCode())).build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();

            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;

                case LocationSettingsStatusCodes.CANCELED:
                    showDialogForGPSIntent();
                    break;
            }
        });
    }

    public void submitReviewCall() {

        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

        JSONObject object = new JSONObject();
        try {
            object.put("request_id", SharedHelper.getKey(context, "request_id"));
            object.put("rating", feedBackRating);
            object.put("comment", "" + txtCommentsRate.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.RATE_PROVIDER_API, object, response -> {
            Utilities.print("SubmitRequestResponse", response.toString());
            utils.hideKeypad(context, activity.getCurrentFocus());
            if ((customDialog != null) && (customDialog.isShowing()))
                customDialog.dismiss();
            destination.setText("");
            frmDest.setText("");
            mapClear();
            flowValue = 0;
//            getProvidersList("");
            layoutChanges();
            if (!current_lat.equalsIgnoreCase("") && !current_lng.equalsIgnoreCase("")) {
                LatLng myLocation = new LatLng(Double.parseDouble(current_lat), Double.parseDouble(current_lng));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }, error -> {
            if ((customDialog != null) && (customDialog.isShowing()))
                customDialog.dismiss();
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void getNewApproximateFare(String service_type1, final MyTextView view) {
        scheduledDate = "";
        scheduledTime = "";
        JSONObject object = new JSONObject();
        String constructedURL = URLHelper.ESTIMATED_FARE_DETAILS_API + "" +
                "?s_latitude=" + source_lat
                + "&s_longitude=" + source_lng
                + "&d_latitude=" + dest_lat
                + "&d_longitude=" + dest_lng
                + "&service_type=" + service_type1;

        System.out.println("getNewApproximateFare getNewApproximateFare " + constructedURL);
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        constructedURL,
                        object,
                        response -> {
                            if (response != null) {
                                if (!response.optString("estimated_fare").equalsIgnoreCase("")) {
                                    Utilities.print("NewApproximateResponse", response.toString());
                                    SharedHelper.putKey(context, "estimated_fare", response.optString("estimated_fare"));
                                    System.out.println("getNewApproximateFare getNewApproximateFare " + response.optString("estimated_fare"));
                                    SharedHelper.putKey(context, "distance", response.optString("distance"));
                                    SharedHelper.putKey(context, "eta_time", response.optString("time"));
                                    SharedHelper.putKey(context, "surge", response.optString("surge"));
                                    SharedHelper.putKey(context, "surge_value", response.optString("surge_value"));
                                    SharedHelper.putKey(context, "currency", response.optString("currency"));
                                    System.out.println("SURGE SURGE SURGE 123456 " + response.optString("surge"));
                                    //   setNewValuesForApproximateLayout();
                                    double wallet_balance = response.optDouble("wallet_balance");
                                    SharedHelper.putKey(context, "wallet_balance", "" + response.optDouble("wallet_balance"));
                                    view.setText(SharedHelper.getKey(context, "currency") + "" + SharedHelper.getKey(context, "estimated_fare"));
                                }
                            }
                        }, error -> {
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                        Log.i(TAG, "getHeaders param : " + headers.toString());
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void startAnim(ArrayList<LatLng> routeList) {
        if (mMap != null && routeList.size() > 1) {
            MapAnimator.getInstance().animateRoute(mMap, routeList);
        } else {
            Toast.makeText(context, "Map not ready", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        // handleCheckStatus.removeCallbacksAndMessages(null);
        if (mapRipple != null && mapRipple.isAnimationRunning()) {
            mapRipple.stopRippleMapAnimation();
        }
        super.onDestroy();
    }

    private void stopAnim() {
        if (mMap != null) {
            MapAnimator.getInstance().stopAnim();
        } else {
            Toast.makeText(context, "Map not ready", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data);
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private View getInfoWindow(String distance, String duration, boolean isMyLocation) {
        View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                getActivity().findViewById(R.id.provider_map), false);
        TextView tv_desc = infoWindow.findViewById(R.id.tv_desc);
        LinearLayout info_window_time = infoWindow.findViewById(R.id.info_window_time);
        TextView tv_distance = infoWindow.findViewById(R.id.tv_distance);
        TextView tv_title = infoWindow.findViewById(R.id.tv_title);
        TextView tv_duration = infoWindow.findViewById(R.id.tv_duration);
        ImageView imgNavigate = infoWindow.findViewById(R.id.imgNavigate);

        //  ImageView imageView =  infoWindow.findViewById(R.id.iv_scheduled_ride);
        // distance = distance.toUpperCase();


        if (isMyLocation) {

            info_window_time.setVisibility(View.VISIBLE);
            tv_title.setVisibility(View.VISIBLE);
            tv_duration.setText(duration);
            //   imageView.setImageResource(R.drawable.amu_bubble_mask);
            tv_distance.setText(distance);
            tv_title.setText(getString(R.string.my_location));
            tv_desc.setText(pickUpLocationName);
            tv_desc.setMaxLines(1);
        } else {
            tvDropAddres.setText(dropLocationName);
            tv_desc.setMaxLines(2);
            tv_desc.setText(dropLocationName);
            info_window_time.setVisibility(View.GONE);
            tv_title.setVisibility(View.GONE);
        }
        return infoWindow;
    }

    private void addIcon(View infoWindow, boolean isMyLocation,
                         LatLng pickUpCoordinates, LatLng dropCoordinates) {
        IconGenerator iconFactory = new IconGenerator(context);
        iconFactory.setContentView(infoWindow);
        iconFactory.setBackground(new RoundCornerDrawable());
        Bitmap icon = iconFactory.makeIcon();
        MarkerOptions markerOptions;
        boolean b = pickUpCoordinates.latitude > dropCoordinates.latitude &&
                pickUpCoordinates.longitude > dropCoordinates.longitude;
        if (isMyLocation) {
            markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(icon)).
                    position(pickUpCoordinates);
            Marker sourceMarker = mMap.addMarker(markerOptions);
            sourceMarker.setFlat(true);

            // sourceMarkerID = sourceMarker.getId();
            if (b) {
                sourceMarker.setAnchor(1.0f, 1.0f);
            } else {
                sourceMarker.setAnchor(0.0f, 0.0f);
            }
        } else {
            markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(icon)).
                    position(dropCoordinates);
            Marker destinationMarker = mMap.addMarker(markerOptions);
            destinationMarker.setFlat(true);
            // destinationMarkerID = destinationMarker.getId();
            if (b) {
                destinationMarker.setAnchor(0.0f, 0.0f);
            } else {
                destinationMarker.setAnchor(1.0f, 1.0f);
            }
        }

    }

    private String getUrl(double source_latitude, double source_longitude,
                          double dest_latitude, double dest_longitude) {

        // Origin of route
        String str_origin = "origin=" + source_latitude + "," + source_longitude;

        // Destination of route
        String str_dest = "destination=" + dest_latitude + "," + dest_longitude;


        // Sensor enabled
        String sensor = "sensor=false" + "&key=" + "AIzaSyD0gzQ43R7S8iiJLL-oUjesnc6hu-EvCII";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.e("url", url + "");
        return url;
    }

    public void redirectMap(String lat1, String lng1, String lat2, String lng2) {
//        String urls="http://maps.google.com/maps?saddr="+lat1+","+lng1+"&daddr="+lat2+","+lng2;
        String urls = "http://maps.google.com/maps?saddr=" + source_address + "&daddr=" + dest_address;
        Log.e("urls", urls + "urls");
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(urls));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SharedHelper.getKey(context, "wallet_balance").equalsIgnoreCase("")) {
            wallet_balance = Double.parseDouble(SharedHelper.getKey(context, "wallet_balance"));
        }

        if (!Double.isNaN(wallet_balance) && wallet_balance > 0) {
            if (lineView != null && chkWallet != null) {
                lineView.setVisibility(View.VISIBLE);
                chkWallet.setVisibility(View.VISIBLE);
            }
        } else {
            if (lineView != null && chkWallet != null) {
                lineView.setVisibility(View.GONE);
                chkWallet.setVisibility(View.GONE);
            }
        }

        try{
            getDocList();


            if (SharedHelper.getKey(getContext(), "Old_User").equalsIgnoreCase("yes")) {
                // Setting Name First
                if (SharedHelper.getKey(getContext(), "first_name").equalsIgnoreCase("null") || SharedHelper.getKey(getContext(), "first_name").equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Add your name to continue", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), UpdateProfile.class);
                    intent.putExtra("parameter", "first_name");
                    intent.putExtra("value", "");
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
                } else if (SharedHelper.getKey(getContext(), "DocumentStatus").equalsIgnoreCase("no")) {
                    Intent intent = new Intent(getContext(), DocUploadActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
                }


            } else {
                if (SharedHelper.getKey(getContext(), "first_name").equalsIgnoreCase("null") || SharedHelper.getKey(getContext(), "first_name").equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Add your name to continue", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), UpdateProfile.class);
                    intent.putExtra("parameter", "first_name");
                    intent.putExtra("value", "");
                    startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
                } else if (SharedHelper.getKey(getContext(), "DocumentStatus").equalsIgnoreCase("no")) {
                    Intent intent = new Intent(getContext(), DocUploadActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.emoji_slide_down, R.anim.emoji_slide_up);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }



//        getPastTripRate();
    }


//    void getPastTripRate() {
//        String auth = "Bearer " + SharedHelper.getKey(context, "access_token");
//        getUserRateCall = restInterface.getUserRate(requestWith, auth);
//        getUserRateCall.enqueue(new Callback<GetUserRate>() {
//            @Override
//            public void onResponse(Call<GetUserRate> call,
//                                   retrofit2.Response<GetUserRate> response) {
//                if (response.code() == 200) {
//                    String requestId = response.body().getRequest_id();
//                    String providerImage = response.body().getProvider_picture();
//                    String providerName = response.body().getProvider_name();
//                    if (response.body().getPaid().equals("1")) {
//                        if (response.body().getUser_rated().equals("0")) {
//                            showTripRateDialog(requestId, providerName, providerImage);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetUserRate> call, Throwable t) {
//
//            }
//        });
//    }

//    void showTripRateDialog(String requestId, String proName, String proImage) {
//        userRateDialog = new Dialog(getActivity());
//        userRateDialog.setContentView(R.layout.user_rate_dailog);
//
//        CircleImageView ivProviderImg;
//        TextView tvProviderName, tvRate, tvSkip;
//        RatingBar rbProvider;
//        EditText etComment;
//
//        userRateDialog.show();
//        userRateDialog.setCancelable(false);
//
//        ivProviderImg = userRateDialog.findViewById(R.id.ivProviderImg);
//        tvProviderName = userRateDialog.findViewById(R.id.tvProviderName);
//        tvRate = userRateDialog.findViewById(R.id.tvRate);
//        tvSkip = userRateDialog.findViewById(R.id.tvSkip);
//        rbProvider = userRateDialog.findViewById(R.id.rbProvider);
//        etComment = userRateDialog.findViewById(R.id.etComment);
//
//        if (proImage != null) {
//            Picasso.get().load(URLHelper.image_url_signature + proImage)
//                    .resize(100, 100)
//                    .into(ivProviderImg);
//        }
//
//        tvProviderName.setText(getString(R.string.rate_your_trip_with) + proName);
//
//        tvRate.setOnClickListener(v -> {
//            int rate = (int) rbProvider.getRating();
//            String com = etComment.getText().toString();
//            if (rbProvider.getRating() > 1) {
//                postPastTripRate(requestId, rate, com);
//            } else {
//                postPastTripRate(requestId, 1, com);
//            }
//        });
//        tvSkip.setOnClickListener(v -> {
//            String rate = String.valueOf(rbProvider.getRating());
//            String com = etComment.getText().toString();
//            postPastTripRate(requestId, 1, com);
//        });
//
//    }

//    void postPastTripRate(String requestId, int rating, String comment) {
//        customDialog.show();
//        customDialog.setCancelable(false);
//        String auth = "Bearer " + SharedHelper.getKey(context, "access_token");
//        PostUserRate postUserRate = new PostUserRate();
//        postUserRate.setRequest_id(requestId);
//        postUserRate.setRating(rating);
//        postUserRate.setComment(comment);
//        responseBodyCall = restInterface.postUserRate(requestWith, auth, postUserRate);
//        responseBodyCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                if (response.code() == 200) {
//                    if (customDialog != null && customDialog.isShowing()) {
//                        customDialog.dismiss();
//                        userRateDialog.cancel();
//                        mMap.clear();
//                        flowValue = 0;
//                        layoutChanges();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                customDialog.dismiss();
//            }
//        });
//    }

    @Override
    public void getJSONArrayResult(String strTag, JSONArray response) {
        if (strTag.equalsIgnoreCase("Get Services")) {
            Utilities.print("GetServices", response.toString());
            if (SharedHelper.getKey(context, "service_type").equalsIgnoreCase("")) {
                SharedHelper.putKey(context, "service_type", "" +
                        response.optJSONObject(0).optString("id"));
            }
            if ((customDialog != null) && (customDialog.isShowing()))
                customDialog.dismiss();
            if (response.length() > 0) {
                currentPostion = 0;

//                PublishFragment.ServiceListAdapter serviceListAdapter = new PublishFragment.ServiceListAdapter(response);
//                rcvServiceTypes.setLayoutManager(new LinearLayoutManager(activity,
//                        LinearLayoutManager.HORIZONTAL, false));
//                rcvServiceTypes.setAdapter(serviceListAdapter);
//                getProvidersList(SharedHelper.getKey(context, "service_type"));
            } else {
                displayMessage(getString(R.string.no_service));
            }
            mMap.clear();
            setValuesForSourceAndDestination();
        }
    }

    private void saveAddressDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.location_storage_home_dialog);
        RadioGroup rgLocationChooser;
        RadioButton rbHome, rbWork, rbOther;
        TextView tvAddressLocation, tvCancelDialog, tvSaveAddressLocation;

        rgLocationChooser = dialog.findViewById(R.id.rgLocationChooser);
        rbHome = dialog.findViewById(R.id.rbHome);
        rbWork = dialog.findViewById(R.id.rbWork);
        rbOther = dialog.findViewById(R.id.rbOther);

        rgLocationChooser.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.rbHome) {

            } else if (checkedId == R.id.rbWork) {

            } else {

            }
        });

        tvAddressLocation = dialog.findViewById(R.id.tvAddressLocation);
        tvCancelDialog = dialog.findViewById(R.id.tvCancelDialog);
        tvSaveAddressLocation = dialog.findViewById(R.id.tvSaveAddressLocation);

        tvAddressLocation.setText(source_address);
        tvCancelDialog.setOnClickListener(view -> dialog.dismiss());

        tvSaveAddressLocation.setOnClickListener(view -> {
            int selectedId = rgLocationChooser.getCheckedRadioButtonId();
            String locationType = "";
            if (selectedId == rbHome.getId()) {
                locationType = "Home";
            }
            if (selectedId == rbWork.getId()) {
                locationType = "Work";
            }
            if (selectedId == rbOther.getId()) {
                locationType = "Other";
            }
            saveLocationAddress(locationType);
            new Handler().postDelayed(() -> dialog.dismiss(), 300);
        });

        dialog.show();
    }

    private void saveLocationAddress(String locationType) {
        String addressLat = current_lat;
        String addressLng = current_lng;
        String addressLocation = current_address;
        String type = locationType;
        String id = SharedHelper.getKey(getActivity(), "id");

        if (isInternet) {
            customDialog = new CustomDialog(getContext());
            if (customDialog != null)
                customDialog.show();

            JSONObject object = new JSONObject();
            try {
                object.put("location_type", type);
                object.put("user_id", id);
                object.put("address", addressLocation);
                object.put("longitude", addressLat);
                object.put("latitude", addressLng);
                Utilities.print("Save Location", "" + object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST,
                    URLHelper.SAVE_LOCATION,
                    object,
                    response -> {
                        if ((customDialog != null) && customDialog.isShowing())
                            customDialog.dismiss();
                        Utilities.print("Save Location Response", response.toString());
                        SharedHelper.putKey(context, type + "_address", addressLocation);
                        // callSuccess();
                    }, error -> {
                if ((customDialog != null) && customDialog.isShowing())
                    customDialog.dismiss();
                displayMessage(getString(R.string.something_went_wrong));
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "" + SharedHelper.getKey(getActivity(), "token_type") + " "
                            + SharedHelper.getKey(getActivity(), "access_token"));
                    return headers;
                }
            };
            ClassLuxApp.getInstance().addToRequestQueue(objectRequest);
        }

    }

    public void displayMessage(String toastString) {

        Toasty.info(getContext(), toastString, Toasty.LENGTH_SHORT, true).show();

    }

//    private void confirmFinalPayment(String totalFee) {
//
//        customDialog.setCancelable(false);
//        if (customDialog != null)
//            customDialog.show();
//        JSONObject object = new JSONObject();
//
//        String constructedURL1 = URLHelper.BASE + URLHelper.GET_PAYMENT_CONFIRMATION + totalFee + "?req_id=" + SharedHelper.getKey(getActivity(), "request_id");
//        Log.e("paymentConfirmationApi:", constructedURL1);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, constructedURL1, object, response -> {
//            Log.e("payment_response_", response.toString());
//            if ((customDialog != null) && (customDialog.isShowing()))
//                customDialog.dismiss();
//            if (response != null) {
//                try {
//                    String status = response.getString("status");
//                    if (status.equalsIgnoreCase("1")) {
//                        //  paymentShowDialog(response.optString("message"));
//                    } else {
//                        //   paymentErrorShowDialog(response.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }, error -> {
//            if ((customDialog != null) && (customDialog.isShowing()))
//                customDialog.dismiss();
//            displayMessage(getString(R.string.something_went_wrong));
//
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("X-Requested-With", "XMLHttpRequest");
//                headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
//                return headers;
//            }
//        };
//        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
//    }

    public void getValidZone() {


        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

        JSONObject object = new JSONObject();
        try {
            object.put("s_latitude", source_lat);
            object.put("s_longitude", source_lng);
            object.put("d_latitude", dest_lat);
            object.put("d_longitude", dest_lng);

            Utilities.print("ValidZoneInput", "" + object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassLuxApp.getInstance().cancelRequestInQueue("send_request");
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST,
                        URLHelper.VALID_ZONE,
                        object,
                        response -> {
                            if ((customDialog != null) && (customDialog.isShowing()))
                                customDialog.dismiss();
                            if (response != null) {

                                Utilities.print("ValidZoneResponse", response.toString());
                                if (response.optString("status").equalsIgnoreCase("1")) {

                                    rcvServiceTypes.setVisibility(View.VISIBLE);
                                    tvZoneMsg.setVisibility(View.GONE);
//                                    getServiceList();
                                } else {
                                    rcvServiceTypes.setVisibility(View.GONE);
                                    tvZoneMsg.setVisibility(View.VISIBLE);
                                    tvZoneMsg.setText(response.optString("error"));
                                }
                            }
                        }, error -> {
                    if ((customDialog != null) && (customDialog.isShowing()))
                        customDialog.dismiss();
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "" + SharedHelper.getKey(context, "token_type") + " " + SharedHelper.getKey(context, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public interface HomeFragmentListener {
    }

    class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgPaymentType:
                case R.id.lblPaymentType:
//                    lblPaymentType
                    showChooser();
                    break;
                case R.id.frmSource:
                    Intent intent = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                    intent.putExtra("cursor", "source");
                    intent.putExtra("s_address", frmSource.getText().toString());
                    intent.putExtra("d_address", destination.getText().toString());
                    intent.putExtra("d_address", frmDest.getText().toString());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
                    break;

                case R.id.sourceDestLayout:
                case R.id.frmDest:
                    Intent intent2 = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                    intent2.putExtra("cursor", "destination");
                    intent2.putExtra("s_address", frmSource.getText().toString());
                    intent2.putExtra("d_address", destination.getText().toString());
                    intent2.putExtra("d_address", frmDest.getText().toString());
                    startActivityForResult(intent2, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
                    break;
                case R.id.lblPromo:
//                    lblPaymentType
                    Intent intentCouponActivity = new Intent(getActivity(), CouponActivity.class);
                    startActivityForResult(intentCouponActivity, 0000);

                    break;
                case R.id.btnRequestRides:
                    scheduledDate = "";
                    scheduledTime = "";
                    if (!frmSource.getText().toString().equalsIgnoreCase("") &&
                            !destination.getText().toString().equalsIgnoreCase("") &&
                            !frmDest.getText().toString().equalsIgnoreCase("")) {
                        getApproximateFare();
                        sourceDestLayout.setOnClickListener(new PublishFragment.OnClick());
                    } else {
                        Toast.makeText(context, "Please enter both pickup and drop locations", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.schedule_ride:
                    if (!frmSource.getText().toString().equalsIgnoreCase("") &&
                            !destination.getText().toString().equalsIgnoreCase("") &&
                            !frmDest.getText().toString().equalsIgnoreCase("")) {

//                        if(fare_amount.equalsIgnoreCase("")){
//                            Toast.makeText(context, "Please enter a valid fare",
//                                    Toast.LENGTH_SHORT).show();
//                        }else{
//                            flowValue = 7;
//                            layoutChanges();
//                        }

                        flowValue = 7;
                        layoutChanges();

                    } else {
                        Toast.makeText(context, "Please enter both pickup and drop locations",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnRequestRideConfirm:
                    frmDest.setOnClickListener(null);
                    frmSource.setOnClickListener(null);
//                    sourceDestLayout.setClickable(false);
                    SharedHelper.putKey(context, "name", "");
                    scheduledDate = "";
                    scheduledTime = "";
                    btnRequestRideConfirm.setEnabled(true);
                    sendRequest();
                    break;
                case R.id.btnPayNow:
//                    sourceDestLayout.setClickable(false);
                    payNow();
                    break;
                case R.id.btnSubmitReview:
//                    sourceDestLayout.setClickable(false);
                    submitReviewCall();
                    break;
                case R.id.lnrHidePopup:
                case R.id.btnDonePopup:
                    lnrHidePopup.setVisibility(View.GONE);
                    flowValue = 1;
                    layoutChanges();
                    click = 1;
                    break;
                case R.id.btnCancelRide:
//                    sourceDestLayout.setClickable(false);
                    showCancelRideDialog();
                    break;
                case R.id.btnCancelTrip:
                    if (btnCancelTrip.getText().toString().equals(getString(R.string.cancel_trip)))
                        showCancelRideDialog();
                    else {
                        String shareUrl = URLHelper.REDIRECT_SHARE_URL;
                        navigateToShareScreen(shareUrl);
                    }
                    break;
                case R.id.imgSos:
                    showSosPopUp();
                    break;
                case R.id.imgShareRide:
                    String url = "http://maps.google.com/maps?q=loc:";
                    navigateToShareScreen(url);
                    break;
                case R.id.imgProvider:
                case R.id.imgProviderRate:
                    Intent intent1 = new Intent(activity, ShowProfile.class);
                    intent1.putExtra("driver", driver);
                    startActivity(intent1);
                    break;
                case R.id.btnCall:

                    Intent intentCall = new Intent(Intent.ACTION_DIAL);
                    intentCall.setData(Uri.parse("tel:" + SharedHelper.getKey(context, "provider_mobile_no")));
                    startActivity(intentCall);

                    break;
                case R.id.btnDone:
                    pick_first = true;
                    pick_firstSO = true;

                    try {
                        Utilities.print("centerLat", cmPosition.target.latitude + "");
                        Utilities.print("centerLong", cmPosition.target.longitude + "");
                        rootView.findViewById(R.id.mapLayout).setAlpha(1);

                        Geocoder geocoder = null;
                        List<Address> addresses;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());

                        String city = "", state = "", address = "";

                        try {
                            addresses = geocoder.getFromLocation(cmPosition.target.latitude, cmPosition.target.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            address = addresses.get(0).getAddressLine(0);
                            city = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println(" strPickForStopOverOrNot from Btndone : "+strPickForStopOverOrNot);

                        if(strPickForStopOverOrNot.equalsIgnoreCase("no")){

                            if (strPickType.equalsIgnoreCase("source")) {
                                source_address = "" + address + "," + city + "," + state;
                                source_lat = "" + cmPosition.target.latitude;
                                source_lng = "" + cmPosition.target.longitude;
                                if (dest_lat.equalsIgnoreCase("")) {
                                    Toast.makeText(context, "Select destination", Toast.LENGTH_SHORT).show();
                                    Intent intentDest = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                                    intentDest.putExtra("cursor", "destination");
                                    intentDest.putExtra("s_address", source_address);
                                    startActivityForResult(intentDest, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
                                } else {

                                    source_lat = "" + cmPosition.target.latitude;
                                    source_lng = "" + cmPosition.target.longitude;

//                                mMap.clear();
                                    setValuesForSourceAndDestination();

                                    flowValue = 1;
                                    layoutChanges();
                                    strPickLocation = "";
                                    strPickType = "";
//                                getServiceList();

                                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(cmPosition.target.latitude,
                                            cmPosition.target.longitude));
                                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                                    mMap.moveCamera(center);
                                    mMap.moveCamera(zoom);

                                }
                            } else {
                                dest_lat = "" + cmPosition.target.latitude;
                                if (source_address.equalsIgnoreCase("" + address)) {
                                    flowValue = 0;
                                    layoutChanges();
                                    Toast.makeText(context, "Both source and destination are same", Toast.LENGTH_SHORT).show();
                                    Intent intentDest = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                                    intentDest.putExtra("cursor", "destination");
                                    intentDest.putExtra("s_address", frmSource.getText().toString());
                                    startActivityForResult(intentDest, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
                                } else {
                                    if (placePredictions != null) {
                                        if (!placePredictions.strSourceAddress.equalsIgnoreCase("")) {
                                            source_lat = "" + placePredictions.strSourceLatitude;
                                            source_lng = "" + placePredictions.strSourceLongitude;
                                            source_address = placePredictions.strSourceAddress;
                                        }
                                    }
                                    dest_address = "" + address + "," + city + "," + state;
                                    dest_lat = "" + cmPosition.target.latitude;
                                    dest_lng = "" + cmPosition.target.longitude;
                                    dropLocationName = dest_address;
//                                mMap.clear();
                                    setValuesForSourceAndDestination();
                                    flowValue = 1;
                                    layoutChanges();
                                    strPickLocation = "";
                                    strPickType = "";
//                                getServiceList();

                                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(cmPosition.target.latitude,
                                            cmPosition.target.longitude));
                                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                                    mMap.moveCamera(center);
                                    mMap.moveCamera(zoom);
                                }
                            }

                        }else {
                            // for stopover

                            System.out.println("else strPickTypeSO : "+strPickTypeSO);

                            if (strPickTypeSO.equalsIgnoreCase("destination")) {

                                source_addressSO = "" + address + "," + city + "," + state;
                                source_latSO = "" + cmPosition.target.latitude;
                                source_lngSO = "" + cmPosition.target.longitude;
                                dest_latSO = "" + cmPosition.target.latitude;
                                dest_lngSO = "" + cmPosition.target.longitude;

                                System.out.println("else strPickTypeSO stepOver lat : "+source_latSO);
                                System.out.println("else strPickTypeSO stepOver long : "+source_lngSO);
                                System.out.println("else strPickTypeSO stepOver add : "+source_addressSO);


                                stopOverModelArrayList.add(new StopOverModel(source_latSO,source_lngSO,source_addressSO));

                                try {
                                    JSONObject jsonObjectStopOver = new JSONObject();
                                    jsonObjectStopOver.put("lat",source_latSO);
                                    jsonObjectStopOver.put("lng",source_lngSO);
                                    jsonObjectStopOver.put("area",source_addressSO);

                                    jsonArrayStopOver.put(jsonObjectStopOver);

                                trackPickToDest(source_lat,source_lng,source_latSO,source_lngSO);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }





//                            stepOverAdapter = new StepOverAdapter(getContext(), stopOverModelArrayList);
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//                            stopoverRv.setLayoutManager(linearLayoutManager);
//                            stopoverRv.setNestedScrollingEnabled(false);

                                stopOverTitleTv.setVisibility(View.VISIBLE);
                                stepOverOrgAdapter = new StepOverOrgAdapter(getContext(), stopOverModelArrayList);
                                stopoverRv.setAdapter(stepOverOrgAdapter);


                                System.out.println("stopover : "+jsonArrayStopOver.toString() );




//                            Toast.makeText(activity, "stepOver add : "+source_addressSO, Toast.LENGTH_SHORT).show();

                                if (dest_latSO.equalsIgnoreCase("")) {
                                    Toast.makeText(context, "Select destination", Toast.LENGTH_SHORT).show();
                                    Intent intentDest = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                                    intentDest.putExtra("cursor", "destination");
                                    intentDest.putExtra("s_address", source_address);
                                    startActivityForResult(intentDest, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
                                } else {

                                    source_latSO = "" + cmPosition.target.latitude;
                                    source_lngSO = "" + cmPosition.target.longitude;

//                                mMap.clear();
//                                setValuesForSourceAndDestination();

                                    flowValue = 1;
                                    layoutChanges();
                                    strPickLocationSO = "";
                                    strPickTypeSO = "";
//                                getServiceList();

                                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(cmPosition.target.latitude,
                                            cmPosition.target.longitude));
                                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                                    mMap.moveCamera(center);
                                    mMap.moveCamera(zoom);

                                }
                            }
                            else {
                                dest_latSO = "" + cmPosition.target.latitude;
                                if (source_addressSO.equalsIgnoreCase("" + address)) {
                                    flowValue = 0;
                                    layoutChanges();
                                    Toast.makeText(context, "Both source and destination are same", Toast.LENGTH_SHORT).show();

                                    Intent intentDest = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                                    intentDest.putExtra("cursor", "destination");
                                    intentDest.putExtra("s_address", frmSource.getText().toString());
                                    startActivityForResult(intentDest, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
                                } else {
                                    if (placePredictionsSO != null) {
                                        if (!placePredictionsSO.strSourceAddress.equalsIgnoreCase("")) {
                                            source_latSO = "" + placePredictionsSO.strSourceLatitude;
                                            source_lngSO = "" + placePredictionsSO.strSourceLongitude;
                                            source_addressSO = placePredictionsSO.strSourceAddress;
                                        }
                                    }
                                    dest_addressSO = "" + address + "," + city + "," + state;
                                    dest_latSO = "" + cmPosition.target.latitude;
                                    dest_lngSO = "" + cmPosition.target.longitude;
                                    dropLocationName = dest_addressSO;
//                                mMap.clear();
//                                setValuesForSourceAndDestination();
                                    flowValue = 1;
                                    layoutChanges();
                                    strPickLocationSO = "";
                                    strPickTypeSO = "";

                                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(cmPosition.target.latitude,
                                            cmPosition.target.longitude));
                                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                                    mMap.moveCamera(center);
                                    mMap.moveCamera(zoom);
                                }
                            }

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Can't able to get the address!.Please try again", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.imgBack:
                    getActivity().runOnUiThread(() -> {
                        Fragment fragment = new PublishFragment();
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        @SuppressLint("CommitTransaction")
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.slide_out_right);
                        transaction.replace(R.id.content, fragment);
                        transaction.commit();
//                        fragmentManager = getSupportFragmentManager();
                    });
//                    GoToFragment();


//                    stopOverModelArrayList.clear();
//                    stopOverTitleTv.setVisibility(View.GONE);
//                    stepOverOrgAdapter = new StepOverOrgAdapter(getContext(), stopOverModelArrayList);
//                    stopoverRv.setAdapter(stepOverOrgAdapter);
//
//                    if (lnrRequestProviders.getVisibility() == View.VISIBLE) {
//                        flowValue = 0;
//                        isRequestProviderScreen = false;
//                        sourceDestLayout.setVisibility(View.VISIBLE);
////                        getProvidersList("");
//                        frmSource.setOnClickListener(new PublishFragment.OnClick());
//                        frmDest.setOnClickListener(new PublishFragment.OnClick());
//                        sourceDestLayout.setOnClickListener(null);
//                        if (!current_lat.equalsIgnoreCase("") && !current_lng.equalsIgnoreCase("")) {
//                            destinationBorderImg.setVisibility(View.VISIBLE);
//                            //verticalView.setVisibility(View.GONE);
//                            LatLng myLocation = new LatLng(Double.parseDouble(current_lat), Double.parseDouble(current_lng));
//                            CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
//                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
////                            getProvidersList("");
//                            sourceDestLayout.setVisibility(View.VISIBLE);
//                        }
//                    } else if (lnrApproximate.getVisibility() == View.VISIBLE) {
//                        isRequestProviderScreen = true;
//                        frmSource.setOnClickListener(new PublishFragment.OnClick());
//                        frmDest.setOnClickListener(new PublishFragment.OnClick());
//                        sourceDestLayout.setOnClickListener(null);
//                        flowValue = 1;
//                    } else if (lnrWaitingForProviders.getVisibility() == View.VISIBLE) {
//                        sourceDestLayout.setVisibility(View.GONE);
//                        isRequestProviderScreen = false;
//                        flowValue = 1;
//                    } else if (ScheduleLayout.getVisibility() == View.VISIBLE) {
//                        isRequestProviderScreen = false;
//                        flowValue = 1;
//                    }
//                    layoutChanges();
                    break;
                case R.id.imgMenu:
//                    if (NAV_DRAWER == 0) {
//                        if (drawer != null)
//                            drawer.openDrawer(GravityCompat.START);
//                    } else {
//                        NAV_DRAWER = 0;
//                        if (drawer != null)
//                            drawer.closeDrawers();
//                    }
                    break;
                case R.id.mapfocus:
                    Double crtLat, crtLng;
                    if (!current_lat.equalsIgnoreCase("") && !current_lng.equalsIgnoreCase("")) {
                        crtLat = Double.parseDouble(current_lat);
                        crtLng = Double.parseDouble(current_lng);

                        if (crtLat != null && crtLng != null) {
                            LatLng loc = new LatLng(crtLat, crtLng);
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(16).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mapfocus.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;
                case R.id.imgSchedule:

                    break;
                case R.id.scheduleBtn:
                    SharedHelper.putKey(context, "name", "");
//                    scheduleTime.setTextColor(getResources().getColor(R.color.green));
//                    scheduleDate.setTextColor(getResources().getColor(R.color.green));
                    if ((!scheduledDate.equals("")) && (!scheduledTime.equals(""))) {
                        if(fare_amount.equalsIgnoreCase("")){
                           try {
                               Toast.makeText(activity, "Entered fare amount", Toast.LENGTH_SHORT).show();

                           }catch (Exception e){
                              e.printStackTrace();
                           }
                        }else{
                            if(Integer.parseInt(fare_amount) < 10){
                                Toast.makeText(activity, "Entered fare amount must be greater than 10 ", Toast.LENGTH_SHORT).show();
                            }else {
                                Date date = null;
                                try {
                                    date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(scheduledDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                long milliseconds = date.getTime();
                                if (!DateUtils.isToday(milliseconds)) {
                                    sendRequest();
                                } else {
                                    if (utils.checktimings(scheduledTime)) {
                                        sendRequest();
                                    } else {
                                        Toast.makeText(activity, getString(R.string.different_time), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }

                    } else {
                        Toast.makeText(activity, getString(R.string.choose_date_time), Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.scheduleDate:
                    // calender class's instance and get current date , month and year from calender
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // current year
                    int mMonth = c.get(Calendar.MONTH); // current month
                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(activity,
                            (view, year, monthOfYear, dayOfMonth) -> {

                                // set day of month , month and year value in the edit text
                                String choosedMonth = "";
                                String choosedDate = "";
                                String choosedDateFormat = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                scheduledDate = choosedDateFormat;
                                try {
                                    choosedMonth = utils.getMonth(choosedDateFormat);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (dayOfMonth < 10) {
                                    choosedDate = "0" + dayOfMonth;
                                } else {
                                    choosedDate = "" + dayOfMonth;
                                }
                                afterToday = Utilities.isAfterToday(year, monthOfYear, dayOfMonth);
                                scheduleDate.setText(choosedDate + " " + choosedMonth + " " + year);
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() - 1000) + (1000 * 60 * 60 * 24 * 7));
                    datePickerDialog.show();
                    break;
                case R.id.scheduleTime:
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(activity,  new TimePickerDialog.OnTimeSetListener() {
                        int callCount = 0;   //To track number of calls to onTimeSet()

                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            if (callCount == 0) {
                                String choosedHour = "";
                                String choosedMinute = "";
                                String choosedTimeZone = "";
                                String choosedTime = "";

                                scheduledTime = selectedHour + ":" + selectedMinute;

                                if (selectedHour > 12) {
                                    choosedTimeZone = "PM";
                                    selectedHour = selectedHour - 12;
                                    if (selectedHour < 10) {
                                        choosedHour = "0" + selectedHour;
                                    } else {
                                        choosedHour = "" + selectedHour;
                                    }
                                } else {
                                    choosedTimeZone = "AM";
                                    if (selectedHour < 10) {
                                        choosedHour = "0" + selectedHour;
                                    } else {
                                        choosedHour = "" + selectedHour;
                                    }
                                }

                                if (selectedMinute < 10) {
                                    choosedMinute = "0" + selectedMinute;
                                } else {
                                    choosedMinute = "" + selectedMinute;
                                }
                                choosedTime = choosedHour + ":" + choosedMinute + " " + choosedTimeZone;

                                if (scheduledDate != "" && scheduledTime != "") {
                                    Date date = null;
                                    try {
                                        date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(scheduledDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    long milliseconds = date.getTime();
                                    if (!DateUtils.isToday(milliseconds)) {
                                        scheduleTime.setText(choosedTime);
                                    } else {
                                        if (utils.checktimings(scheduledTime)) {
                                            scheduleTime.setText(choosedTime);
                                        } else {
                                            Toast toast = new Toast(activity);
                                            Toast.makeText(activity, getString(R.string.different_time), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(activity, getString(R.string.choose_date_time), Toast.LENGTH_SHORT).show();
                                }
                            }
                            callCount++;
                        }
                    }, hour, minute, false);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                    break;
            }
        }
    }


    public String convertHours(int runtime) {
        int hours = runtime / 60;
        int minutes = runtime % 60; // 5 in this case.
        return hours + "h " + minutes + " m";
    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private int dpToPx(Context context, float dpValue) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dpValue * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void trackPickToDest() throws Exception {

        GoogleDirection.withServerKey(BuildConfig.API_KEY)
                .from(new LatLng(Double.parseDouble(source_lat), Double.parseDouble(source_lng)))
                .to(new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng)))
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {



                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        try {
                            if (direction != null) {
                                if (direction.isOK()) {
                                    Log.v("rawBody", rawBody + "");
                                    Log.v("direction", direction + "");

                                    float totalDistance = 0;
                                    int totalDuration = 0;
//                                    mMap.clear();
                                    Route route = direction.getRouteList().get(0);
                                    int legCount = route.getLegList().size();
                                    for (int index = 0; index < legCount; index++) {
                                        Leg leg = route.getLegList().get(index);
                                        try {
                                            totalDistance = totalDistance + Float.parseFloat(leg.getDistance().getText().replace("km", "").replace("m", "").trim());
                                            Toast.makeText(getContext(), "DISTANCE : "+totalDistance, Toast.LENGTH_SHORT).show();
                                            SharedHelper.putKey(getContext(),"PUBLISHED_DISTANCE",""+totalDistance);
                                            System.out.println("Distance : "+totalDistance);


                                        } catch (NumberFormatException ne) {
                                            ne.printStackTrace();
                                        }
//                                totalDistance =0;

                                        Log.v("ridetime", leg.getDuration().getText() + " ");
                                        if(leg.getDuration().getText().contains("day")){
                                            Log.v("splitday", leg.getDuration().getText().split("day")[0] + " ");
                                            totalDuration = totalDuration + 24 * Integer.parseInt(leg.getDuration().getText()
                                                    .split("day")[0].trim());
                                        }
                                        else if (leg.getDuration().getText().contains("hour")) {
                                            Log.v("splithour", leg.getDuration().getText().split("hour")[0] + " ");
                                            totalDuration = totalDuration + 60 * Integer.parseInt(leg.getDuration().getText()
                                                    .split("hour")[0].trim());

                                        } else if (leg.getDuration().getText().contains("hours")) {
                                            totalDuration = totalDuration + 60 * Integer.parseInt(leg.getDuration().getText()
                                                    .split("hours")[0].trim().replace("m", ""));
                                        } else if (leg.getDuration().getText().contains("mins")) {
                                            totalDuration = totalDuration + Integer.parseInt(leg.getDuration().getText()
                                                    .replace("hour", "").replace("mins", "").replace("m", "").trim());
                                        } else {
                                            totalDuration = totalDuration + 0;
                                        }

//                                        totalDuration = leg.getDuration().getText().toString();


                                        if (reqStatus.equals("PICKEDUP") || reqStatus.equals("DROPPED")) {
                                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.user_markers);
                                            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.provider);
                                            mMap.addMarker(new MarkerOptions()
                                                    .icon(BitmapDescriptorFactory.fromBitmap(icon1))
                                                    .rotation(360)
                                                    .flat(true)
                                                    .anchor(0.5f, 0.5f)
                                                    .position(leg.getStartLocation().getCoordination()));
                                            if (index == legCount - 1) {
                                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon)).position(leg.getEndLocation().getCoordination()));
                                            }
                                            List<Step> stepList = leg.getStepList();

                                            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(getContext(), stepList, 3, getResources().getColor(R.color.dark_green), 2, Color.GRAY);
                                            for (PolylineOptions polylineOption : polylineOptionList) {
                                                mMap.addPolyline(polylineOption);
                                            }
                                            if (pickUpLocationName != null) {
                                            }

                                            if (dest_address != null) {
                                                View marker_view2 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(com.gsanthosh91.decoderoutekey.R.layout.custom_marker, null);
                                                TextView addressDes = marker_view2.findViewById(com.gsanthosh91.decoderoutekey.R.id.addressTxt);
//                                                TextView addressDes = marker_view2.findViewById();
                                                TextView etaTxt = marker_view2.findViewById(com.gsanthosh91.decoderoutekey.R.id.etaTxt);
                                                etaTxt.setVisibility(View.VISIBLE);
                                                addressDes.setText(dropLocationName);
                                                if (totalDuration > 60) {
                                                    etaTxt.setText(convertHours(totalDuration));
                                                } else {
                                                    etaTxt.setText(totalDuration + " mins");
                                                }
                                                etaDur = totalDuration + "";
                                                MarkerOptions marker_opt_des = new MarkerOptions().position(new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng)));
                                                marker_opt_des.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker_view2))).anchor(0.00f, 0.20f);
                                                destinationMarker = mMap.addMarker(marker_opt_des);
                                            }
                                        } else {
//                                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.destination_marker);
                                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.destination_location);
                                            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.user_markers);
                                            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon1)).position(leg.getStartLocation().getCoordination()));
                                            if(destLatLngSO != null){
                                                System.out.println("LEG COOR DESTLATLONG : "+destLatLngSO);
                                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon1)).position(destLatLngSO));

                                            }
                                            System.out.println("LEG COOR :"+leg.getStartLocation().getCoordination());
                                            System.out.println("LEG COOR DESTLATLONG 2  : "+destLatLngSO);
                                            System.out.println("LEG COOR DESTLONG 2  : "+dest_lngSO);
                                            System.out.println("LEG COOR DESTLAT 2  : "+dest_latSO);

                                            if (index == legCount - 1) {
                                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon)).position(leg.getEndLocation().getCoordination()));
                                            }
                                            List<Step> stepList = leg.getStepList();
                                            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(getContext(), stepList, 3, getResources().getColor(R.color.dark_green), 2, Color.GRAY);
                                            for (PolylineOptions polylineOption : polylineOptionList) {
                                                mMap.addPolyline(polylineOption);
                                            }




                                            if (pickUpLocationName != null) {
                                            }

//                                            if (dest_address != null) {
//                                                View marker_view2 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(com.gsanthosh91.decoderoutekey.R.layout.custom_marker, null);
//                                                TextView addressDes = marker_view2.findViewById(com.gsanthosh91.decoderoutekey.R.id.addressTxt);
//                                                TextView etaTxt = marker_view2.findViewById(com.gsanthosh91.decoderoutekey.R.id.etaTxt);
//                                                etaTxt.setVisibility(View.VISIBLE);
//                                                addressDes.setText(pickUpLocationName);
//                                                if (totalDuration > 60) {
//                                                    etaTxt.setText(convertHours(totalDuration));
//                                                } else {
//                                                    etaTxt.setText(totalDuration + " mins");
//                                                }
//
////                                                ################## hiding time and location
//                                                etaTxt.setVisibility(View.GONE);
//                                                addressDes.setVisibility(View.GONE);
//                                                marker_view2.setVisibility(View.GONE);
//
//                                                etaDur = totalDuration + "";
//                                                MarkerOptions marker_opt_des = new MarkerOptions().position(new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng)));
//                                                marker_opt_des.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker_view2))).anchor(0.00f, 0.20f);
//                                                destinationMarker = mMap.addMarker(marker_opt_des);
//                                            }
                                        }

                                    }

                                    try {
                                        mMap.setOnCameraIdleListener(() -> {
                                            if (sourceMarker != null) {
                                                String lat = String.valueOf(sourceLatLng.latitude);
                                                String lng = String.valueOf(sourceLatLng.longitude);
                                                if (((lat != null) && !lat.equals("") && !lat.isEmpty() && !lat.equalsIgnoreCase("0,0")) &&
                                                        ((lng != null) && !lng.equals("") && !lng.isEmpty() && !lng.equalsIgnoreCase("0,0"))) {
                                                    Point PickupPoint = mMap.getProjection().toScreenLocation(new LatLng(sourceLatLng.latitude, sourceLatLng.longitude));
                                                    sourceMarker.setAnchor(PickupPoint.x < dpToPx(context, 200) ? 0.00f : 1.00f, PickupPoint.y < dpToPx(context, 100) ? 0.20f : 1.20f);
                                                }

                                            }
                                            if (destinationMarker != null) {
                                                if (((dest_lat != null) && !dest_lat.equals("") && !dest_lat.isEmpty() && !dest_lat.equalsIgnoreCase("0,0")) &&
                                                        ((dest_lng != null) && !dest_lng.equals("") && !dest_lng.isEmpty() && !dest_lng.equalsIgnoreCase("0,0"))) {
                                                    Point PickupPoint = mMap.getProjection().toScreenLocation(new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng)));
                                                    destinationMarker.setAnchor(PickupPoint.x < dpToPx(context, 200) ? 0.00f : 1.00f, PickupPoint.y < dpToPx(context, 100) ? 0.20f : 1.20f);
                                                }
                                            }
                                        });

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                    // just for testing
//                                    lblCmfrmSourceAddress.setText(pickUpLocationName);
//                                    lblDis.setText(totalDistance + " km");
//                                    lblEta.setText(totalDuration + " min");

                                    setCameraWithCoordinationBounds(route);
                                }
                            }
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                    }
                });

    }

    private void trackPickToDest(String source_lat,String source_lng,String dest_lat,String dest_lng) throws Exception {

        GoogleDirection.withServerKey(BuildConfig.API_KEY)
                .from(new LatLng(Double.parseDouble(source_lat), Double.parseDouble(source_lng)))
                .to(new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng)))
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {



                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        try {
                            if (direction != null) {
                                if (direction.isOK()) {
                                    Log.v("rawBody", rawBody + "");
                                    Log.v("direction", direction + "");

                                    float totalDistance = 0;
                                    int totalDuration = 0;
//                                    mMap.clear();
                                    Route route = direction.getRouteList().get(0);
                                    int legCount = route.getLegList().size();
                                    for (int index = 0; index < legCount; index++) {
                                        Leg leg = route.getLegList().get(index);
                                        try {
                                            totalDistance = totalDistance + Float.parseFloat(leg.getDistance().getText().replace("km", "").replace("m", "").trim());
                                            Toast.makeText(getContext(), "DISTANCE : "+totalDistance, Toast.LENGTH_SHORT).show();
                                            SharedHelper.putKey(getContext(),"PUBLISHED_DISTANCE",totalDistance+" KM");

                                        } catch (NumberFormatException ne) {
                                            ne.printStackTrace();
                                        }
//                                totalDistance =0;

                                        Log.v("ridetime", leg.getDuration().getText() + " ");
                                        if(leg.getDuration().getText().contains("day")){
                                            Log.v("splitday", leg.getDuration().getText().split("day")[0] + " ");
                                            totalDuration = totalDuration + 24 * Integer.parseInt(leg.getDuration().getText()
                                                    .split("day")[0].trim());
                                        }
                                        else if (leg.getDuration().getText().contains("hour")) {
                                            Log.v("splithour", leg.getDuration().getText().split("hour")[0] + " ");
                                            totalDuration = totalDuration + 60 * Integer.parseInt(leg.getDuration().getText()
                                                    .split("hour")[0].trim());

                                        } else if (leg.getDuration().getText().contains("hours")) {
                                            totalDuration = totalDuration + 60 * Integer.parseInt(leg.getDuration().getText()
                                                    .split("hours")[0].trim().replace("m", ""));
                                        } else if (leg.getDuration().getText().contains("mins")) {
                                            totalDuration = totalDuration + Integer.parseInt(leg.getDuration().getText()
                                                    .replace("hour", "").replace("mins", "").replace("m", "").trim());
                                        } else {
                                            totalDuration = totalDuration + 0;
                                        }

//                                        totalDuration = leg.getDuration().getText().toString();


                                        if (reqStatus.equals("PICKEDUP") || reqStatus.equals("DROPPED")) {
                                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.user_markers);
                                            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.provider);
                                            mMap.addMarker(new MarkerOptions()
                                                    .icon(BitmapDescriptorFactory.fromBitmap(icon1))
                                                    .rotation(360)
                                                    .flat(true)
                                                    .anchor(0.5f, 0.5f)
                                                    .position(leg.getStartLocation().getCoordination()));
                                            if (index == legCount - 1) {
                                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon)).position(leg.getEndLocation().getCoordination()));
                                            }
                                            List<Step> stepList = leg.getStepList();

                                            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(getContext(), stepList, 3, getResources().getColor(R.color.dark_green), 2, Color.GRAY);
                                            for (PolylineOptions polylineOption : polylineOptionList) {
                                                mMap.addPolyline(polylineOption);
                                            }
                                            if (pickUpLocationName != null) {
                                            }

                                            if (dest_address != null) {
                                                View marker_view2 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(com.gsanthosh91.decoderoutekey.R.layout.custom_marker, null);
                                                TextView addressDes = marker_view2.findViewById(com.gsanthosh91.decoderoutekey.R.id.addressTxt);
//                                                TextView addressDes = marker_view2.findViewById();
                                                TextView etaTxt = marker_view2.findViewById(com.gsanthosh91.decoderoutekey.R.id.etaTxt);
                                                etaTxt.setVisibility(View.VISIBLE);
                                                addressDes.setText(dropLocationName);
                                                if (totalDuration > 60) {
                                                    etaTxt.setText(convertHours(totalDuration));
                                                } else {
                                                    etaTxt.setText(totalDuration + " mins");
                                                }
                                                etaDur = totalDuration + "";
                                                MarkerOptions marker_opt_des = new MarkerOptions().position(new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng)));
                                                marker_opt_des.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker_view2))).anchor(0.00f, 0.20f);
                                                destinationMarker = mMap.addMarker(marker_opt_des);
                                            }
                                        } else {
//                                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.destination_marker);
                                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.destination_location);
                                            Bitmap icon1 = BitmapFactory.decodeResource(getResources(), R.drawable.user_markers);
                                            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon1)).position(leg.getStartLocation().getCoordination()));
                                            if(destLatLngSO != null){
                                                System.out.println("LEG COOR DESTLATLONG : "+destLatLngSO);
                                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon1)).position(destLatLngSO));

                                            }
                                            System.out.println("LEG COOR :"+leg.getStartLocation().getCoordination());
                                            System.out.println("LEG COOR DESTLATLONG 2  : "+destLatLngSO);
                                            System.out.println("LEG COOR DESTLONG 2  : "+dest_lngSO);
                                            System.out.println("LEG COOR DESTLAT 2  : "+dest_latSO);

                                            if (index == legCount - 1) {
                                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(icon)).position(leg.getEndLocation().getCoordination()));
                                            }
                                            List<Step> stepList = leg.getStepList();
                                            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(getContext(), stepList, 3, getResources().getColor(R.color.dark_green), 2, Color.GRAY);
                                            for (PolylineOptions polylineOption : polylineOptionList) {
                                                mMap.addPolyline(polylineOption);
                                            }
                                            if (pickUpLocationName != null) {
                                            }

//                                            if (dest_address != null) {
//                                                View marker_view2 = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(com.gsanthosh91.decoderoutekey.R.layout.custom_marker, null);
//                                                TextView addressDes = marker_view2.findViewById(com.gsanthosh91.decoderoutekey.R.id.addressTxt);
//                                                TextView etaTxt = marker_view2.findViewById(com.gsanthosh91.decoderoutekey.R.id.etaTxt);
//                                                etaTxt.setVisibility(View.VISIBLE);
//                                                addressDes.setText(pickUpLocationName);
//                                                if (totalDuration > 60) {
//                                                    etaTxt.setText(convertHours(totalDuration));
//                                                } else {
//                                                    etaTxt.setText(totalDuration + " mins");
//                                                }
//
////                                                ################## hiding time and location
//                                                etaTxt.setVisibility(View.GONE);
//                                                addressDes.setVisibility(View.GONE);
//                                                marker_view2.setVisibility(View.GONE);
//
//                                                etaDur = totalDuration + "";
//                                                MarkerOptions marker_opt_des = new MarkerOptions().position(new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng)));
//                                                marker_opt_des.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(context, marker_view2))).anchor(0.00f, 0.20f);
//                                                destinationMarker = mMap.addMarker(marker_opt_des);
//                                            }
                                        }

                                    }

                                    mMap.setOnCameraIdleListener(() -> {
                                        if (sourceMarker != null) {
                                            String lat = String.valueOf(sourceLatLng.latitude);
                                            String lng = String.valueOf(sourceLatLng.longitude);
                                            if (((lat != null) && !lat.equals("") && !lat.isEmpty() && !lat.equalsIgnoreCase("0,0")) &&
                                                    ((lng != null) && !lng.equals("") && !lng.isEmpty() && !lng.equalsIgnoreCase("0,0"))) {
                                                Point PickupPoint = mMap.getProjection().toScreenLocation(new LatLng(sourceLatLng.latitude, sourceLatLng.longitude));
                                                sourceMarker.setAnchor(PickupPoint.x < dpToPx(context, 200) ? 0.00f : 1.00f, PickupPoint.y < dpToPx(context, 100) ? 0.20f : 1.20f);
                                            }

                                        }
                                        if (destinationMarker != null) {
                                            if (((dest_lat != null) && !dest_lat.equals("") && !dest_lat.isEmpty() && !dest_lat.equalsIgnoreCase("0,0")) &&
                                                    ((dest_lng != null) && !dest_lng.equals("") && !dest_lng.isEmpty() && !dest_lng.equalsIgnoreCase("0,0"))) {
                                                Point PickupPoint = mMap.getProjection().toScreenLocation(new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng)));
                                                destinationMarker.setAnchor(PickupPoint.x < dpToPx(context, 200) ? 0.00f : 1.00f, PickupPoint.y < dpToPx(context, 100) ? 0.20f : 1.20f);
                                            }
                                        }
                                    });
                                    // just for testing
//                                    lblCmfrmSourceAddress.setText(pickUpLocationName);
//                                    lblDis.setText(totalDistance + " km");
//                                    lblEta.setText(totalDuration + " min");

                                    setCameraWithCoordinationBounds(route);
                                }
                            }
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                    }
                });

    }

//    private class ServiceListAdapter extends RecyclerView.Adapter<PublishFragment.ServiceListAdapter.MyViewHolder> {
//        JSONArray jsonArray;
//        int selectedPosition;
//        private SparseBooleanArray selectedItems;
//
//        public ServiceListAdapter(JSONArray array) {
//            this.jsonArray = array;
//        }
//
//
//        @Override
//        public PublishFragment.ServiceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            @SuppressLint("InflateParams")
//            View view = LayoutInflater.from(getActivity())
//                    .inflate(R.layout.service_type_list_item, null);
//            return new PublishFragment.ServiceListAdapter.MyViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(PublishFragment.ServiceListAdapter.MyViewHolder holder, final int position) {
//            Utilities.print("Title: ", "" +
//                    jsonArray.optJSONObject(position).optString("name")
//                    + " Image: " + jsonArray.optJSONObject(position).optString("image")
//                    + " Grey_Image:" + jsonArray.optJSONObject(position).optString("grey_image"));
//
//            holder.serviceItem.setText(jsonArray.optJSONObject(position).optString("name"));
//            System.out.println("POSITION IS CALLEDD " + position);
//
//
//            if (position == 0) {
//                getNewApproximateFare(jsonArray.optJSONObject(position)
//                        .optString("id"), holder.serviceItemPrice);
//            }
//
//            if (position == 1) {
//                getNewApproximateFare(jsonArray.optJSONObject(position)
//                        .optString("id"), holder.serviceItemPrice);
//            }
//            if (position == 2) {
//                getNewApproximateFare(jsonArray.optJSONObject(position)
//                        .optString("id"), holder.serviceItemPrice);
//            }
//            if (position == 3) {
//                getNewApproximateFare(jsonArray.optJSONObject(position)
//                        .optString("id"), holder.serviceItemPrice);
//            }
//            if (position == 4) {
//                getNewApproximateFare(jsonArray.optJSONObject(position)
//                        .optString("id"), holder.serviceItemPrice);
//            }
//
//
//            if (position == currentPostion) {
//                SharedHelper.putKey(context, "service_type", "" +
//                        jsonArray.optJSONObject(position).optString("id"));
//                Picasso.get().load(URLHelper.BASE + jsonArray
//                                .optJSONObject(position).optString("image"))
//                        .placeholder(R.drawable.car_select)
//                        .error(R.drawable.car_select).into(holder.serviceImg);
//                holder.selector_background.setBackgroundResource(R.drawable.selected_service_item);
//                holder.serviceItem.setTextColor(getResources().getColor(R.color.text_color_white));
//                holder.serviceCapacity.setText(jsonArray.optJSONObject(position).optString("capacity"));
////                holder.serviceCapacity.setBackgroundResource(R.drawable.normal_service_item);
//                Picasso.get().load(URLHelper.BASE + jsonArray.optJSONObject(position).optString("image"))
//                        .placeholder(R.drawable.car_select)
//                        .error(R.drawable.car_select).into(ImgConfrmCabType);
////                getApproximateFareSchedule();
//
//            } else {
//                //SharedHelper.putKey(context, "service_type", "" + jsonArray.optJSONObject(position).optString("id"));
//                Picasso.get().load(URLHelper.BASE + jsonArray.optJSONObject(position).optString("image"))
//                        .placeholder(R.drawable.car_select)
//                        .error(R.drawable.car_select).into(holder.serviceImg);
//                holder.selector_background.setBackgroundResource(R.drawable.normal_service_item);
////                holder.selector_background.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                holder.serviceItem.setTextColor(getResources().getColor(R.color.black));
//                holder.serviceCapacity.setText(jsonArray.optJSONObject(position).optString("capacity"));
//
////                getApproximateFareSchedule();
//            }
//
//
//            holder.linearLayoutOfList.setTag(position);
//
//            holder.linearLayoutOfList.setOnClickListener(view -> {
//                if (position == currentPostion) {
//                    try {
//                        lnrHidePopup.setVisibility(View.VISIBLE);
//                        // showProviderPopup(jsonArray.getJSONObject(position));
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                currentPostion = Integer.parseInt(view.getTag().toString());
//                SharedHelper.putKey(context, "service_type", "" + jsonArray.optJSONObject(currentPostion).optString("id"));
//                SharedHelper.putKey(context, "name", "" + jsonArray.optJSONObject(currentPostion).optString("name"));
//                try {
//                    notifyDataSetChanged();
//                } catch (NullPointerException ne) {
//                    ne.printStackTrace();
//                }
//
//                Utilities.print("service_typeCurrentPosition", "" + SharedHelper.getKey(context, "service_type"));
//                Utilities.print("Service name", "" + SharedHelper.getKey(context, "name"));
////                getProvidersList(SharedHelper.getKey(context, "service_type"));
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return jsonArray.length();
//        }
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//
//            TextView serviceItem, serviceCapacity;
//            MyTextView serviceItemPrice;
//            CircleImageView serviceImg;
//            LinearLayout linearLayoutOfList;
//            FrameLayout selector_background;
//
//            public MyViewHolder(View itemView) {
//                super(itemView);
//                serviceItem = itemView.findViewById(R.id.serviceItem);
//                serviceCapacity = itemView.findViewById(R.id.serviceCapacity);
//                serviceImg = itemView.findViewById(R.id.serviceImg);
//                linearLayoutOfList = itemView.findViewById(R.id.LinearLayoutOfList);
//                selector_background = itemView.findViewById(R.id.selector_background);
//                serviceItemPrice = itemView.findViewById(R.id.serviceItemPrice);
//                height = itemView.getHeight();
//                width = itemView.getWidth();
//            }
//        }
//    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            PublishFragment.ParserTask parserTask = new PublishFragment.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0]);
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            String distance = "";
            String duration = "";
            isDragging = false;
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {
                        distance = point.get("distance");
                        continue;
                    } else if (j == 1) {
                        duration = point.get("duration");
                        continue;
                    }


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                    builder.include(position);
                }

                if (!source_lat.equalsIgnoreCase("") &&
                        !source_lng.equalsIgnoreCase("")) {
                    LatLng location = new LatLng(Double.parseDouble(source_lat),
                            Double.parseDouble(source_lng));
                    //mMap.clear();
                    if (sourceMarker != null)
                        sourceMarker.remove();
                    MarkerOptions markerOptions = new MarkerOptions()
                            .anchor(0.5f, 0.75f)
                            .position(location).draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.round_source));
                    marker = mMap.addMarker(markerOptions);
                    sourceMarker = mMap.addMarker(markerOptions);

                }
                if (!dest_lat.equalsIgnoreCase("") &&
                        !dest_lng.equalsIgnoreCase("")) {
                    destLatLng = new LatLng(Double.parseDouble(dest_lat),
                            Double.parseDouble(dest_lng));
                    if (destinationMarker != null)
                        destinationMarker.remove();
                    MarkerOptions destMarker = new MarkerOptions()
                            .position(destLatLng).draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.square_desti));
                    destinationMarker = mMap.addMarker(destMarker);
//                    if (sourceLatLng != null && destLatLng != null) {
//                        View infoWindow = getInfoWindow(distance, duration, true);
//                        addIcon(infoWindow, true, sourceLatLng, destLatLng);
//
//                        infoWindow = getInfoWindow(distance, duration, false);
//                        addIcon(infoWindow, false, sourceLatLng, destLatLng);
//                    }

                    mMap.setPadding(20, 20, 20, 20);

                    builder.include(sourceMarker.getPosition());
                    builder.include(destinationMarker.getPosition());
                    LatLngBounds bounds = builder.build();
                    int padding = 0; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                    mMap.moveCamera(cu);


                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLACK);


                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }
            tvPickUpAddres.setText(source_address);
            tvDropAddres.setText(dest_address);
            if (lineOptions != null && points != null) {
                mMap.addPolyline(lineOptions);
//                startAnim(points);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

}




