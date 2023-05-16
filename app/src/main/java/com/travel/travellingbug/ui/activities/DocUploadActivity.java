package com.travel.travellingbug.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.travel.travellingbug.BuildConfig;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.AppHelper;
import com.travel.travellingbug.helper.BitmapWorkerTask;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.helper.VolleyMultipartRequest;
import com.travel.travellingbug.utills.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class DocUploadActivity extends AppCompatActivity {

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int TAKE_PICTURE = 0;
    private static final int GET_PICTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String TAG = "DocUploadActivity";
    public static int deviceHeight;
    public static int deviceWidth;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };
    Button btMotorInspectCertificate, btPsvInsurenceCertificate, btGoodConduct,
            btPSVLicense, btpersonalId, btPersonalPic;
    ImageView imgPersonal, imgPersonalId, imgPSVLicense, imgGoodConduct,
            imgMotorInspectCertificate, imgPsvInsurenceCertificate;
    Button btnDone;
    CheckBox chkTerm;
    String uploadTag;
    ProgressDialog pDialog;
    RecyclerView recDoc;
    int UploadPosition = 0;
    JSONArray responseArray;
    ImageView adapterImageView;
    Button adapterButton;
    int totalDocUpload = 0;
    String Profile;

    LinearLayout documentNameLL;

    ImageView backArrow;
    TextView txtUploa;
    Uri documentUri;
    private String first = "", boring_depth = "";
    private String fileExt = "";
    private byte[] b, b1, b2;
    private File file;
    private Bitmap bmp = null;
    private String token;
    private Uri cameraImageUri = null;

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private static Bitmap getBitmapFromUri(@NonNull Context context, @NonNull Uri uri) throws IOException {
        Log.e(TAG, "getBitmapFromUri: Resize uri" + uri);
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        assert parcelFileDescriptor != null;
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        Log.e(TAG, "getBitmapFromUri: Height" + deviceHeight);
        Log.e(TAG, "getBitmapFromUri: width" + deviceWidth);
        int maxSize = Math.min(deviceHeight, deviceWidth);
        if (image != null) {
            Log.e(TAG, "getBitmapFromUri: Width" + image.getWidth());
            Log.e(TAG, "getBitmapFromUri: Height" + image.getHeight());
            int inWidth = image.getWidth();
            int inHeight = image.getHeight();
            int outWidth;
            int outHeight;
            if (inWidth > inHeight) {
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }
            return Bitmap.createScaledBitmap(image, outWidth, outHeight, false);
        } else {
            Toast.makeText(context, context.getString(R.string.valid_image), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_doc_upload);


        Toast.makeText(getApplicationContext(), "Welcome  "+SharedHelper.getKey(getApplicationContext(),"first_name"), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Please upload a document to continue", Toast.LENGTH_LONG).show();
        System.out.println("First Name : "+SharedHelper.getKey(getApplicationContext(),"first_name"));
        // Setting Name First
        if(SharedHelper.getKey(getApplicationContext(),"first_name").equalsIgnoreCase("null") || SharedHelper.getKey(getApplicationContext(),"first_name").equalsIgnoreCase("") || SharedHelper.getKey(getApplicationContext(),"first_name").equalsIgnoreCase(null)){
            Toast.makeText(getApplicationContext(), "Add your Name to Continue", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), UpdateProfile.class);
            intent.putExtra("parameter", "first_name");
            intent.putExtra("value", "");
            startActivityForResult(intent, 1);
        }

//        getSupportActionBar().setTitle("Documents");
//
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        verifyStoragePermissions(DocUploadActivity.this);
        token = SharedHelper.getKey(DocUploadActivity.this, "access_token");
        imgPersonal = findViewById(R.id.imgPersonal);
        imgPersonalId = findViewById(R.id.imgPersonalId);
        imgPSVLicense = findViewById(R.id.imgPSVLicense);
        imgGoodConduct = findViewById(R.id.imgGoodConduct);
        imgMotorInspectCertificate = findViewById(R.id.imgMotorInspectCertificate);
        imgPsvInsurenceCertificate = findViewById(R.id.imgPsvInsurenceCertificate);
        txtUploa = findViewById(R.id.txtUploa);
        btnDone = findViewById(R.id.btnDone);
        chkTerm = findViewById(R.id.chkTerm);
        backArrow = findViewById(R.id.backArrow);

        btMotorInspectCertificate = findViewById(R.id.btMotorInspectCertificate);
        btPsvInsurenceCertificate = findViewById(R.id.btPsvInsurenceCertificate);
        btGoodConduct = findViewById(R.id.btGoodConduct);
        btPSVLicense = findViewById(R.id.btPSVLicense);
        btpersonalId = findViewById(R.id.btpersonalId);
        btPersonalPic = findViewById(R.id.btPersonalPic);
        recDoc = findViewById(R.id.recDoc);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;

        getDocument();

        if (getIntent().getStringExtra("profile") != null) {
            Profile = "true";
            txtUploa.setVisibility(View.GONE);
            btnDone.setVisibility(View.GONE);
            chkTerm.setVisibility(View.GONE);
        } else {
            Profile = "false";
        }

        btpersonalId.setOnClickListener(v -> {
            uploadTag = "National ID copy";
            if (ContextCompat.checkSelfPermission(DocUploadActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            5);
                }
            }
            Dialog d = ImageChoose();
            d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            d.show();
        });


        btPSVLicense.setOnClickListener(v -> {
            uploadTag = "Bank Information (Bank name and account number)";
            if (ContextCompat.checkSelfPermission(DocUploadActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            5);
                }
            }
            Dialog d = ImageChoose();
            d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            d.show();
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);
            }
        });


        btGoodConduct.setOnClickListener(v -> {
            uploadTag = "Registration Certificate";
            if (ContextCompat.checkSelfPermission(DocUploadActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            5);
                }
            }

            Dialog d = ImageChoose();
            d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            d.show();
        });

        btPsvInsurenceCertificate.setOnClickListener(v -> {
            uploadTag = "Vehicle Photo including plate number";
            if (ContextCompat.checkSelfPermission(DocUploadActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            5);
                }
            }
            Dialog d = ImageChoose();
            d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            d.show();
        });

        btMotorInspectCertificate.setOnClickListener(v -> {
            uploadTag = "Driving License";
            if (ContextCompat.checkSelfPermission(DocUploadActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            5);
                }
            }
            Dialog d = ImageChoose();
            d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            d.show();


        });

        btnDone.setOnClickListener(v -> {

            if (totalDocUpload >= 1) {
                if (chkTerm.isChecked()) {
                    completeDocumentStatus();
                } else {
                    Toast.makeText(DocUploadActivity.this, "Please check document certify",
                            Toast.LENGTH_LONG).show();
                    displayMessage("Please check document certify");
                }
            } else {
                Toast.makeText(DocUploadActivity.this, "Upload atleast one documents",
                        Toast.LENGTH_LONG).show();
                displayMessage(DocUploadActivity.this.getString(R.string.upload_all_documents_and_check_term));
            }



//            if (totalDocUpload == responseArray.length()) {
//                if (chkTerm.isChecked()) {
//                    completeDocumentStatus();
//                } else {
//                    Toast.makeText(DocUploadActivity.this, "Please check document certify",
//                            Toast.LENGTH_LONG).show();
//                    displayMessage("Please check document certify");
//                }
//            } else {
//                Toast.makeText(DocUploadActivity.this, "Upload all documents",
//                        Toast.LENGTH_LONG).show();
//                displayMessage(DocUploadActivity.this.getString(R.string.upload_all_documents_and_check_term));
//            }


        });
    }

    private void getDocument() {

        try {


            String url = URLHelper.CHECK_DOCUMENT;
            final JsonObjectRequest jsonObjectRequest = new
                    JsonObjectRequest(Request.Method.GET, url,
                            null, response -> {
                        Log.v("checkDocumentStatus", response + "Document");
                        try {

                            JSONArray jsonArray = response.getJSONArray("data");
                            responseArray = jsonArray;
                            DocAdapter docAdapter = new DocAdapter(jsonArray);
                            recDoc.setHasFixedSize(true);
                            recDoc.setNestedScrollingEnabled(false);
                            recDoc.setLayoutManager(new LinearLayoutManager(DocUploadActivity.this) {
                                @Override
                                public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                                    return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                }
                            });
                            recDoc.setAdapter(docAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }, error -> Log.v("DocUploadActivity", error.getMessage())) {
                        @Override
                        public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<>();
                            headers.put("X-Requested-With", "XMLHttpRequest");
                            headers.put("Authorization", "Bearer " + token);
                            return headers;
                        }
                    };
            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayMessage(String toastString) {
//        utils.print("displayMessage", "" + toastString);
       /* Snackbar.make(Objects.requireNonNull(getCurrentFocus()), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();*/
        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
    }

    private Dialog ImageChoose() {
        androidx.appcompat.app.AlertDialog.Builder builder = new
                androidx.appcompat.app.AlertDialog.Builder(DocUploadActivity.this);

        if (bmp == null) {
            CharSequence[] ch = {};
            ch = new CharSequence[]{"Gallery", "Camera"};
            builder.setTitle("Choose Image :").setItems(
                    ch,
                    (dialog, which) -> {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                getPhoto();
                                break;
                            case 1:
                                if (ContextCompat.checkSelfPermission(DocUploadActivity.this,
                                        Manifest.permission.CAMERA) ==
                                        PackageManager.PERMISSION_GRANTED) {
                                    takePhoto();
                                } else {
                                    Toast.makeText(DocUploadActivity.this, "You have denied camera access permission.",
                                            Toast.LENGTH_LONG).show();
                                }
                                break;
                            default:
                                break;
                        }
                    });
        } else {
            builder.setTitle("Choose Image :").setItems(
                    new CharSequence[]{"Gallery", "Camera"},
                    (dialog, which) -> {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                getPhoto();
                                break;
                            case 1:
                                if (ContextCompat.checkSelfPermission(DocUploadActivity.this,
                                        Manifest.permission.CAMERA) ==
                                        PackageManager.PERMISSION_GRANTED) {
                                    takePhoto();
                                } else {
                                    Toast.makeText(DocUploadActivity.this, "You have denied camera access permission.",
                                            Toast.LENGTH_LONG).show();
                                }
                                break;


                            default:
                                break;
                        }
                    });
        }


        return builder.create();
    }

    private void getPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GET_PICTURE);
    }

    private void takePhoto() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraImageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    private Uri getOutputMediaFileUri(int type) {
        Uri uri = FileProvider.getUriForFile(DocUploadActivity.this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type));
//        Uri contentUri = FileProvider.getUriForFile(DocUploadActivity.this, BuildConfig.APPLICATION_ID, getOutputMediaFile(type));
        return uri;
    }

    @SuppressLint("SimpleDateFormat")
    private File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "APP");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    //  cameraImageUri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                    if (cameraImageUri != null) {
                        DocUploadActivity.this.getContentResolver()
                                .notifyChange(cameraImageUri, null);

                        fileExt = ".png";
                        file = new File(cameraImageUri.getPath());
                        first = file.getName() + fileExt;
//                        galleryImageUri = null;

                        try {

                            showImage(cameraImageUri);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case GET_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    {
                        Uri uri = data.getData();
                        File f1 = new File(uri.getPath());
                        Log.e("uploadTag", uploadTag + "tag");
                        try {
                            Bitmap resizeImg = getBitmapFromUri(DocUploadActivity.this, uri);
                            Log.e("resizeImg", resizeImg + "calllogbook");
                            if (resizeImg != null) {
                                documentUri = uri;
                                Log.e("calllogbook", "calllogbook");
                                Bitmap reRotateImg = AppHelper.modifyOrientation(resizeImg,
                                        AppHelper.getPath(DocUploadActivity.this, uri));
                                showImage1(reRotateImg);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                break;

        }
    }

    public void saveProfileAccount(String filename, byte[] bytes, String docid, String expdate) {
        if (Utils.isConnectingToInternet(DocUploadActivity.this)) {
            pDialog = new ProgressDialog(DocUploadActivity.this);
            // pDialog.setTitle("Loading...");
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
            pDialog.show();
            final ProgressDialog finalPDialog = pDialog;
            VolleyMultipartRequest multipartRequest = new
                    VolleyMultipartRequest(Request.Method.POST,
                            URLHelper.BASE + "api/provider/document/upload",
                            response -> {
                                pDialog.dismiss();
                                totalDocUpload = totalDocUpload + 1;
                               /* if (uploadTag == "PSV Insurance Certificate") {
                                    SharedHelperImage.putKey(DocUploadActivity.this,
                                            "PSVInsuranceCertificate", String.valueOf(documentUri));
                                }*/
                                String resultResponse = new String(response.data);
                                Log.e("uploadtest", resultResponse + "");
                                Toast.makeText(DocUploadActivity.this, "File is Uploaded Successfully",
                                        Toast.LENGTH_LONG).show();
                                pDialog.dismiss();

                            }, error -> {
                        displayMessage(getString(R.string.something_went_wrong));
                    }) {
                        @Override
                        protected java.util.Map<String, String> getParams() {
                            java.util.Map<String, String> param = new HashMap<>();
                            param.put("document_id", docid);
                            param.put("expires_at", expdate);
                            param.put("provider_id", SharedHelper.getKey(DocUploadActivity.this, "id"));
                            Log.v("docparam", param + " ");
                            return param;
                        }

                        @Override
                        public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("X-Requested-With", "XMLHttpRequest");
                            headers.put("Authorization", "Bearer " + SharedHelper.getKey(DocUploadActivity.this, "access_token"));

                            return headers;
                        }


                        @Override
                        protected java.util.Map<String, DataPart> getByteData() {
                            java.util.Map<String, DataPart> params = new HashMap<>();
                            // file name could found file BASE or direct access from real path
                            // for now just get bitmap data from ImageView

                            params.put("document", new DataPart(filename, bytes, "image/jpeg"));
                            return params;
                        }
                    };


            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue = Volley.newRequestQueue(DocUploadActivity.this);
            queue.add(multipartRequest);
        } else {

            Toast.makeText(DocUploadActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

        }

    }

    private void completeDocumentStatus() {
        try {
            String url = URLHelper.COMPLETE_DOCUMENT;
            final JsonObjectRequest jsonObjectRequest = new
                    JsonObjectRequest(Request.Method.GET,
                            url,
                            null,
                            response -> {
                                Log.e("completeDoc", response.toString());
//                                        Waintingdialog.dismiss();
                                Intent intent1 = new Intent(DocUploadActivity.this, WaitingForApproval.class);
                                intent1.putExtra("account_status", "account_status_new");
                                startActivity(intent1);
                                finish();
                            }, error -> {
                        Log.e("completeDoc", error.getLocalizedMessage());
                        //                                utils.print("Error", error.toString());

                    }) {
                        @Override
                        public java.util.Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<>();
                            headers.put("X-Requested-With", "XMLHttpRequest");
                            headers.put("Authorization", "Bearer " + token);
                            return headers;
                        }
                    };
            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showImage(Uri uri) {
        final Calendar myCalendar = Calendar.getInstance();
        Dialog dialog = new Dialog(DocUploadActivity.this);
        dialog.setContentView(R.layout.image_show_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView ivShow = dialog.findViewById(R.id.ivShow);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        Button btOk = dialog.findViewById(R.id.btOk);
        TextView etDate = dialog.findViewById(R.id.etDate);
        TextView txtDocumentName = dialog.findViewById(R.id.txtDocumentName);
        TextView txtDocumentType = dialog.findViewById(R.id.txtDocumentType);

        try {
            txtDocumentType.setText(responseArray.getJSONObject(UploadPosition).getString("type"));
            txtDocumentName.setText(responseArray.getJSONObject(UploadPosition).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            etDate.setText(sdf.format(myCalendar.getTime()));

        };

        dialog.show();


        etDate.setOnClickListener(v -> new DatePickerDialog(DocUploadActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        new BitmapWorkerTask(DocUploadActivity.this, ivShow,
                "add_revenue").execute(uri);
        btCancel.setOnClickListener(v -> {
            dialog.dismiss();
            browseanother();
        });


        btOk.setOnClickListener(v -> {

            if (etDate.getText().toString().isEmpty()) {
                etDate.setError("Please select expiry date");
            } else {
                documentUri = cameraImageUri;
                new BitmapWorkerTask(DocUploadActivity.this, adapterImageView,
                        "add_revenue").execute(cameraImageUri);

                try {
                    adapterButton.setVisibility(View.GONE);
                    adapterImageView.setVisibility(View.VISIBLE);
                    saveProfileAccount(responseArray.getJSONObject(UploadPosition).getString("name"),
                            AppHelper.getFileDataFromDrawable(ivShow.getDrawable()),
                            responseArray.getJSONObject(UploadPosition).getString("id"), etDate.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }


        });

    }


    private void browseanother() {
        if (ContextCompat.checkSelfPermission(DocUploadActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        5);
            }
        }
        Dialog d = ImageChoose();
        d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        d.show();
    }

    void showImage1(Bitmap bitmap) {
        final Calendar myCalendar = Calendar.getInstance();
        Log.e("dialogcallk", "dialogcall");
        final Dialog dialog = new Dialog(DocUploadActivity.this);
        dialog.setContentView(R.layout.image_show_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView ivShow = dialog.findViewById(R.id.ivShow);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        Button btOk = dialog.findViewById(R.id.btOk);
        ivShow.setImageBitmap(bitmap);

        dialog.show();


        TextView etDate = dialog.findViewById(R.id.etDate);
        TextView txtDocumentName = dialog.findViewById(R.id.txtDocumentName);
        TextView txtDocumentType = dialog.findViewById(R.id.txtDocumentType);

        try {
            txtDocumentType.setText(responseArray.getJSONObject(UploadPosition).getString("type"));
            txtDocumentName.setText(responseArray.getJSONObject(UploadPosition).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            etDate.setText(sdf.format(myCalendar.getTime()));

        };

        dialog.show();


        etDate.setOnClickListener(v -> new DatePickerDialog(DocUploadActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        btOk.setOnClickListener(v -> {

            if (etDate.getText().toString().isEmpty()) {
                etDate.setError("Please select expiry date");
            } else {
                documentUri = cameraImageUri;
                new BitmapWorkerTask(DocUploadActivity.this, adapterImageView,
                        "add_revenue").execute(cameraImageUri);

                try {
                    adapterButton.setVisibility(View.GONE);
                    adapterImageView.setVisibility(View.VISIBLE);
                    saveProfileAccount(responseArray.getJSONObject(UploadPosition).getString("name"),
                            AppHelper.getFileDataFromDrawable(ivShow.getDrawable()),
                            responseArray.getJSONObject(UploadPosition).getString("id"), etDate.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }


        });

        btOk.setOnClickListener(v -> {
            if (etDate.getText().toString().isEmpty()) {
                etDate.setError("Please select expiry date");
            } else {


                try {
                    adapterImageView.setVisibility(View.VISIBLE);
                    adapterImageView.setImageBitmap(bitmap);
                    adapterButton.setVisibility(View.GONE);
                    saveProfileAccount(responseArray.getJSONObject(UploadPosition).getString("name"),
                            AppHelper.getFileDataFromDrawable(adapterImageView.getDrawable()),
                            responseArray.getJSONObject(UploadPosition).getString("id"), etDate.getText().toString());
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
        btCancel.setOnClickListener(v -> {
            dialog.dismiss();
            browseanother();
        });
    }


    private class DocAdapter extends RecyclerView.Adapter<DocAdapter.MyViewHolder> {
        JSONArray jsonArray;
        String type;

        public DocAdapter(JSONArray array) {
            this.jsonArray = array;
        }

        public void append(JSONArray array) {
            try {
                for (int i = 0; i < array.length(); i++) {
                    this.jsonArray.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public DocAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.doc_upload_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DocAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Log.v("dpcArray", jsonArray + "");
            try {
                holder.txtDocomentName.setText(jsonArray.optJSONObject(position).optString("name"));


                if (Profile.contains("true")) {
                    if (jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("Active")) {
                        holder.btnUpload.setBackgroundColor(Color.GREEN);
                    } else {
                        holder.btnUpload.setBackgroundColor(Color.RED);
                    }

                    holder.btnUpload.setText(jsonArray.optJSONObject(position).optString("status"));
                } else {
                    holder.btnUpload.setOnClickListener(v -> {
                        UploadPosition = position;
                        adapterImageView = holder.imgShow;
                        adapterButton = holder.btnUpload;
                        if (ContextCompat.checkSelfPermission(DocUploadActivity.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        5);
                            }
                        }
                        Dialog d = ImageChoose();
                        d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                        d.show();
                    });

                    holder.documentNameLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UploadPosition = position;
                            adapterImageView = holder.imgShow;
                            adapterButton = holder.btnUpload;
                            if (ContextCompat.checkSelfPermission(DocUploadActivity.this, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            5);
                                }
                            }
                            Dialog d = ImageChoose();
                            d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                            d.show();
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtDocomentName;
            Button btnUpload;
            CheckBox chkDocument;
            CircleImageView imgShow;
            TextView txtDocumentType;

            LinearLayout documentNameLL;

            public MyViewHolder(View itemView) {
                super(itemView);

                txtDocomentName = itemView.findViewById(R.id.txtDocomentName);
                btnUpload = itemView.findViewById(R.id.btnUpload);
                chkDocument = itemView.findViewById(R.id.chkDocument);
                imgShow = itemView.findViewById(R.id.imgShow);
                txtDocumentType = itemView.findViewById(R.id.txtDocumentType);
                documentNameLL = itemView.findViewById(R.id.documentNameLL);


            }
        }


    }

}


