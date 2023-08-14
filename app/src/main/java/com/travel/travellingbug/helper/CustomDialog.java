package com.travel.travellingbug.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.travel.travellingbug.R;


public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);

        // Set a transparent background for the dialog
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Remove the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the custom layout
        setContentView(R.layout.custom_dialog);

        // Set rounded corners for the dialog's window
        getWindow().setBackgroundDrawableResource(R.drawable.background_blur);
    }


//    public CustomDialog(Context context) {
//        super(context);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.custom_dialog);
////        android:background="@color/transparent_black"
//    }
}
