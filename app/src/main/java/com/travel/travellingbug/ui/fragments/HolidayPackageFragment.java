package com.travel.travellingbug.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.CustomDialog;


public class HolidayPackageFragment extends Fragment {

    WebView webView ;
    String url = "";

    CustomDialog customDialog;

    boolean doubleBackToExitPressedOnce = false;

    public HolidayPackageFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            customDialog = new CustomDialog(getContext());
            customDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_holiday_package, container, false);

        webView = view.findViewById(R.id.webviewOrg);
        url = "https://travellingbug.in/";




//        WebViewClient webViewClient  = new WebViewClient();
//        webView.setWebViewClient(webViewClient);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);

                customDialog.setCancelable(false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
                customDialog.dismiss();
            }
        });

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return true;
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (doubleBackToExitPressedOnce) {
                    getActivity().finish();
                    return false;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(getActivity(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 5000);
                return true;
            }
            return false;
        });


        return view;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        if(customDialog.isShowing()){
            customDialog.dismiss();
        }
    }
}