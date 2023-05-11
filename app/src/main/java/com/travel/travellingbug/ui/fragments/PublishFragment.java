package com.travel.travellingbug.ui.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.travel.travellingbug.R;
import com.travel.travellingbug.ui.activities.CustomGooglePlacesSearch;
import com.travel.travellingbug.utills.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PublishFragment extends Fragment  {


    TextView calendertv,timetv,frmSource,frmDest;

    private static final String TAG = "PublishFragment";
    DatePickerDialog datePickerDialog;

    String scheduledDate = "";
    String scheduledTime = "";

    Utilities utils = new Utilities();
    boolean afterToday = false;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST = 18945;

    public PublishFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_publish, container, false);


        initComponent(view);
        onClickHandler();



        return view;
    }

    private void onClickHandler() {
        calendertv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
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
                            calendertv.setText(choosedDate + " " + choosedMonth + " " + year);
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() - 1000) + (1000 * 60 * 60 * 24 * 7));
                datePickerDialog.show();
            }
        });

        timetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
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
                                    timetv.setText(choosedTime);
                                } else {
                                    if (utils.checktimings(scheduledTime)) {
                                        timetv.setText(choosedTime);
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
            }
        });

        frmSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                intent.putExtra("cursor", "source");
                intent.putExtra("s_address", frmSource.getText().toString());
                intent.putExtra("d_address", frmDest.getText().toString());
                intent.putExtra("d_address", frmDest.getText().toString());
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
            }
        });

        frmDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), CustomGooglePlacesSearch.class);
                intent2.putExtra("cursor", "destination");
                intent2.putExtra("s_address", frmSource.getText().toString());
                intent2.putExtra("d_address", frmDest.getText().toString());
                intent2.putExtra("d_address", frmDest.getText().toString());
                startActivityForResult(intent2, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
            }
        });
    }

    private void initComponent(View view) {
        calendertv = view.findViewById(R.id.calendertv);
        timetv = view.findViewById(R.id.timetv);

        frmSource = view.findViewById(R.id.frmSource);
        frmDest = view.findViewById(R.id.frmDest);

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.d(TAG, "onActivityResult: " + requestCode + " Result Code " + resultCode);
////        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST) {
////            if (parserTask != null) {
////                parserTask = null;
////            }
////            if (resultCode == Activity.RESULT_OK) {
////                if (marker != null) {
////                    marker.remove();
////                }
////
////                placePredictions = (PlacePredictions) data.getSerializableExtra("Location Address");
////                strPickLocation = data.getExtras().getString("pick_location");
////                strPickType = data.getExtras().getString("type");
////
////
////                if (strPickLocation.equalsIgnoreCase("yes")) {
////                    pick_first = true;
////                    mMap.clear();
////                    flowValue = 9;
////                    layoutChanges();
////                    float zoomLevel = 16.0f; //This goes up to 21
////                    stopAnim();
////                } else {
////                    if (placePredictions != null) {
////                        if (!placePredictions.strSourceAddress.equalsIgnoreCase("")) {
////                            source_lat = "" + placePredictions.strSourceLatitude;
////                            source_lng = "" + placePredictions.strSourceLongitude;
////                            source_address = placePredictions.strSourceAddress;
////
////                            if (!placePredictions.strSourceLatitude.equalsIgnoreCase("")
////                                    && !placePredictions.strSourceLongitude.equalsIgnoreCase("")) {
////                                double latitude = Double.parseDouble(placePredictions.strSourceLatitude);
////                                double longitude = Double.parseDouble(placePredictions.strSourceLongitude);
////                                LatLng location = new LatLng(latitude, longitude);
////
////                                //mMap.clear();
////                                try {
////                                    MarkerOptions markerOptions = new MarkerOptions()
////                                            .anchor(0.5f, 0.75f)
////                                            .position(location)
////                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
////                                    marker = mMap.addMarker(markerOptions);
////                                    sourceMarker = mMap.addMarker(markerOptions);
////                                } catch (Exception e) {
////                                    e.printStackTrace();
////                                }
////                               /* CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(16).build();
////                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
////                            }
////
////                        }
////                        if (!placePredictions.strDestAddress.equalsIgnoreCase("")) {
////                            dest_lat = "" + placePredictions.strDestLatitude;
////                            dest_lng = "" + placePredictions.strDestLongitude;
////                            dest_address = placePredictions.strDestAddress;
////                            dropLocationName = dest_address;
////
////                            SharedHelper.putKey(context, "current_status", "2");
////                            if (source_lat != null && source_lng != null && !source_lng.equalsIgnoreCase("")
////                                    && !source_lat.equalsIgnoreCase("")) {
////                                String url = getUrl(Double.parseDouble(source_lat), Double.parseDouble(source_lng)
////                                        , Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
////
////                                current_lat = source_lat;
////                                current_lng = source_lng;
////                                //  getNewApproximateFare("1");
////                                //  getNewApproximateFare2("2");
////                                SearchFragment.FetchUrl fetchUrl = new SearchFragment.FetchUrl();
////                                fetchUrl.execute(url);
////                                LatLng location = new LatLng(Double.parseDouble(current_lat), Double.parseDouble(current_lng));
////
////
////                                //mMap.clear();
////                                if (sourceMarker != null)
////                                    sourceMarker.remove();
////                                MarkerOptions markerOptions = new MarkerOptions()
////                                        .anchor(0.5f, 0.75f)
////                                        .position(location)
////                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
////                                marker = mMap.addMarker(markerOptions);
////                                sourceMarker = mMap.addMarker(markerOptions);
////                               /* CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(14).build();
////                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
////                            }
////                            if (!dest_lat.equalsIgnoreCase("") && !dest_lng.equalsIgnoreCase("")) {
////                                destLatLng = new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
////                                if (destinationMarker != null)
////                                    destinationMarker.remove();
////                                MarkerOptions destMarker = new MarkerOptions()
////                                        .position(destLatLng)
////                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_marker));
////                                destinationMarker = mMap.addMarker(destMarker);
////                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
////                                builder.include(sourceMarker.getPosition());
////                                builder.include(destinationMarker.getPosition());
////                                LatLngBounds bounds = builder.build();
////                                int padding = 200; // offset from edges of the map in pixels
////                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
////                                mMap.moveCamera(cu);
////
////                                /*LatLng myLocation = new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
////                                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
////                                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
////                            }
////                        }
////
////                        if (dest_address.equalsIgnoreCase("")) {
////                            flowValue = 1;
////                            frmSource.setText(source_address);
////                            getValidZone();
//////                            getServiceList();
////                        } else {
////                            flowValue = 1;
////
////                            if (cardInfoArrayList.size() > 0) {
////                                getCardDetailsForPayment(cardInfoArrayList.get(0));
////                                sourceDestLayout.setVisibility(View.GONE);
////                            }
////                            getValidZone();
////                            paymentLayout.setVisibility(View.VISIBLE);
//////                            getServiceList();
////                        }
////
////                        layoutChanges();
////                    }
////                }
////            } else if (resultCode == Activity.RESULT_CANCELED) {
////                // The user canceled the operation.
////            }
////        }
////        if (requestCode == ADD_CARD_CODE) {
////            if (resultCode == Activity.RESULT_OK) {
////                boolean result = data.getBooleanExtra("isAdded", false);
////                if (result) {
////                    getCards();
////                }
////            }
////        }
////        if (requestCode == 0000) {
////            if (resultCode == Activity.RESULT_OK) {
////                lblPromo.setText(getString(R.string.promocode_applied));
////            }
////        }
////        if (requestCode == 5555) {
////            if (resultCode == Activity.RESULT_OK) {
////                CardInfo cardInfo = data.getParcelableExtra("card_info");
////                getCardDetailsForPayment(cardInfo);
////            }
////        }
////
////        if (requestCode == 10) {
////            String result = data.getStringExtra("paymentSuccessful");
////        }
////        if (requestCode == REQUEST_LOCATION) {
////
////        } else {
////
////        }
//    }
//
//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//        if (marker != null) {
//            marker.remove();
//        }
//        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
//
//            MarkerOptions markerOptions = new MarkerOptions()
//                    .anchor(0.5f, 0.75f)
//                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
//                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_currentlocation));
//            marker = mMap.addMarker(markerOptions);
//
//
//            current_lat = "" + location.getLatitude();
//            current_lng = "" + location.getLongitude();
//
//            if (source_lat.equalsIgnoreCase("") || source_lat.length() < 0) {
//                source_lat = current_lat;
//            }
//            if (source_lng.equalsIgnoreCase("") || source_lng.length() < 0) {
//                source_lng = current_lng;
//            }
//
//            if (value == 0) {
//                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
//                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                mMap.setPadding(0, 0, 0, 0);
//                mMap.getUiSettings().setZoomControlsEnabled(false);
//                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//                mMap.getUiSettings().setMapToolbarEnabled(false);
//                mMap.getUiSettings().setCompassEnabled(false);
//
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//                currentAddress = utils.getCompleteAddressString(context, latitude, longitude);
//                source_lat = "" + latitude;
//                source_lng = "" + longitude;
//                source_address = currentAddress;
//                current_address = currentAddress;
//                frmSource.setTextColor(getResources().getColor(R.color.dark_gray));
//                frmSource.setText(currentAddress);
//
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
//                }else{
//                    frmDest.setText("Going to");
//                }
//
//                getProvidersList("");
//                value++;
//                if ((customDialog != null) && (customDialog.isShowing()))
//                    customDialog.dismiss();
//            }
//
//            updateLocationToAdmin(location.getLatitude() + "", location.getLongitude() + "");
//        }
//    }
}




