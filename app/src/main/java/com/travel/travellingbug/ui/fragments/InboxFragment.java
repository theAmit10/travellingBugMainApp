package com.travel.travellingbug.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.CustomDialog;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.InboxModel;
import com.travel.travellingbug.ui.activities.SplashScreen;
import com.travel.travellingbug.ui.adapters.InboxAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class InboxFragment extends Fragment {

    Boolean isInternet;
    RecyclerView recyclerView;

    private ShimmerFrameLayout mFrameLayout;
    RelativeLayout errorLayout;
    ConnectionHelper helper;
    CustomDialog customDialog;
    View rootView;

    ImageView backImg;
    LinearLayout toolbar;

    String providerFirstName = "";
    String providerId = "";

    ArrayList<InboxModel> providerChatList = new ArrayList<>();
    ArrayList<InboxModel> userChatList = new ArrayList<>();



    public InboxFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        findViewByIdAndInitialize();

        if (isInternet) {
            getHistoryList();
            userChatListData();



        }

        backImg.setOnClickListener(v -> getFragmentManager().popBackStack());
        Bundle bundle = getArguments();
        String toolbar = null;
        if (bundle != null)
            toolbar = bundle.getString("toolbar");

        if (toolbar != null && toolbar.length() > 0) {
            this.toolbar.setVisibility(View.VISIBLE);
        }

        return rootView;
    }



    public void getHistoryList() {
        mFrameLayout.startShimmer();
        customDialog = new CustomDialog(getActivity());
        customDialog.setCancelable(false);
        customDialog.show();

        // Getting Provider Chatlist
        StringRequest request = new StringRequest(Request.Method.GET, URLHelper.PROVIDER_CHATLIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        System.out.println("data : "+jsonObject.toString());

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if(jsonArray.length() > 0){
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject dataJsonObject = jsonArray.getJSONObject(i);

                                if(!dataJsonObject.getString("id").equalsIgnoreCase(SharedHelper.getKey(getContext(),"id"))){
                                    InboxModel inboxModel = new InboxModel();

                                    try {
                                        inboxModel.setId(Integer.parseInt(dataJsonObject.getString("id")));
                                        inboxModel.setUsername(dataJsonObject.getString("first_name"));

                                        if(!dataJsonObject.getString("avatar").equalsIgnoreCase("null")){
                                            inboxModel.setProfileImage(URLHelper.BASE + "storage/app/public/" +dataJsonObject.getString("avatar"));
                                        }else{
                                            inboxModel.setProfileImage("");
                                        }

                                        JSONArray chatdetailJsonArray = dataJsonObject.getJSONArray("chatdetail");
                                        if(chatdetailJsonArray.length() > 0){
                                            JSONObject chatDetailJsonObject  = chatdetailJsonArray.getJSONObject(0);
                                            inboxModel.setRequestId(chatDetailJsonObject.getString("request_id"));
                                            inboxModel.setProviderId(chatDetailJsonObject.getString("provider_id"));
                                            inboxModel.setUserId(chatDetailJsonObject.getString("user_id"));
                                        }else {
                                            inboxModel.setRequestId("");
                                            inboxModel.setProviderId("");
                                            inboxModel.setUserId("");
                                        }




                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    providerChatList.add(inboxModel);
                                }

                            }

                            if(customDialog.isShowing()){
                                customDialog.dismiss();
                            }
//                            if(providerChatList.size() > 0){
//                                customDialog.dismiss();
//                                InboxAdapter inboxAdapter = new InboxAdapter(getContext(),providerChatList);
//                                recyclerView.setAdapter(inboxAdapter);
//                            }
//                            else {
//                                customDialog.dismiss();
//                                errorLayout.setVisibility(View.VISIBLE);
//                                recyclerView.setVisibility(View.GONE);
//                            }

                        }else {
                            if(customDialog.isShowing()){
                                customDialog.dismiss();
                            }
//                            errorLayout.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    if(customDialog.isShowing()){
                        customDialog.dismiss();
                    }
                    displayMessage("Something went wrong");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }

        }) {



            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
                return headers;
            }

        };

        ClassLuxApp.getInstance().addToRequestQueue(request);


    }


    private void userChatListData(){

        // Getting User Chatlist
        StringRequest userRequest = new StringRequest(Request.Method.GET, URLHelper.USER_CHATLIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        System.out.println("data : "+jsonObject.toString());

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if(jsonArray.length() > 0){
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject dataJsonObject = jsonArray.getJSONObject(i);

                                if(!dataJsonObject.getString("id").equalsIgnoreCase(SharedHelper.getKey(getContext(),"id"))){
                                    InboxModel inboxModel = new InboxModel();
                                    try {

                                        inboxModel.setId(Integer.parseInt(dataJsonObject.getString("id")));
                                        inboxModel.setUsername(dataJsonObject.getString("first_name"));
                                        if(!dataJsonObject.getString("avatar").equalsIgnoreCase("null")){
                                            inboxModel.setProfileImage(URLHelper.BASE + "storage/app/public/" +dataJsonObject.getString("avatar"));
                                        }else{
                                            inboxModel.setProfileImage("");
                                        }

                                        JSONArray chatdetailJsonArray = dataJsonObject.getJSONArray("chatdetail");
                                        if(chatdetailJsonArray.length() > 0){
                                            JSONObject chatDetailJsonObject  = chatdetailJsonArray.getJSONObject(0);
                                            inboxModel.setRequestId(chatDetailJsonObject.getString("request_id"));
                                            inboxModel.setProviderId(chatDetailJsonObject.getString("provider_id"));
                                            inboxModel.setUserId(chatDetailJsonObject.getString("user_id"));
                                        }else {
                                            inboxModel.setRequestId("");
                                            inboxModel.setProviderId("");
                                            inboxModel.setUserId("");
                                        }



                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    userChatList.add(inboxModel);
                                }


                            }


                            // if both list have data
                            if(userChatList.size() > 0 && providerChatList.size() > 0){
                                System.out.println("Start combining...");
                                System.out.println("Start combining pl : "+ providerChatList.size());
                                System.out.println("Start combining ul : "+ userChatList.size());
                                // Combine the lists
                                ArrayList<InboxModel> combinedArray = combineArrays(providerChatList, userChatList);

                                for (InboxModel obj : combinedArray) {
                                    System.out.println(obj.getId() + ": " + obj.getUsername());
                                }

                                // Print the combined and de-duplicated list
                                System.out.println("combinedArray : "+combinedArray.size());

                                InboxAdapter inboxAdapter = new InboxAdapter(getContext(),combinedArray);
                                recyclerView.setAdapter(inboxAdapter);
                                mFrameLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
//                                inboxAdapter.notifyDataSetChanged();

                            }else{
                                InboxAdapter inboxAdapter = new InboxAdapter(getContext(),userChatList);
                                mFrameLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(inboxAdapter);


                            }


                        }
                        else {
                            if(providerChatList.size() > 0){
                                customDialog.dismiss();
                                InboxAdapter inboxAdapter = new InboxAdapter(getContext(),providerChatList);
                                mFrameLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerView.setAdapter(inboxAdapter);
                            }else {
                                mFrameLayout.setVisibility(View.GONE);
                                errorLayout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }

                        }

                    }
                } catch (JSONException e) {
                    displayMessage("Something went wrong");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }

        }) {



            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
                return headers;
            }

        };

        ClassLuxApp.getInstance().addToRequestQueue(userRequest);


    }

    public  ArrayList<InboxModel> combineArrays(ArrayList<InboxModel> array1, ArrayList<InboxModel> array2) {
        Map<Integer, InboxModel> combinedMap = new HashMap<>();

        // Add elements from array1 to the map
        for (InboxModel obj : array1) {
            if(!SharedHelper.getKey(getContext(),"id").equalsIgnoreCase(String.valueOf(obj.getId()))){
                combinedMap.put(obj.getId(), obj);
            }
        }

        // Add elements from array2 to the map, avoiding duplicates
        for (InboxModel obj : array2) {
            if(!SharedHelper.getKey(getContext(),"id").equalsIgnoreCase(String.valueOf(obj.getId()))){
                if (!combinedMap.containsKey(obj.getId())) {
                    combinedMap.put(obj.getId(), obj);
                }
            }

        }

        // Convert the map values back to a list
        return new ArrayList<>(combinedMap.values());
    }

    @Override
    public void onPause() {
        mFrameLayout.stopShimmer();
        super.onPause();
    }


    public void findViewByIdAndInitialize() {
        mFrameLayout = rootView.findViewById(R.id.shimmerLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        errorLayout = rootView.findViewById(R.id.errorLayout);
        errorLayout.setVisibility(View.GONE);
        helper = new ConnectionHelper(getActivity());
        isInternet = helper.isConnectingToInternet();
        toolbar = rootView.findViewById(R.id.lnrTitle);
        backImg = rootView.findViewById(R.id.backArrow);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void displayMessage(String toastString) {
        Toasty.info(getActivity(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(getActivity(), "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(getActivity(), SplashScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        getActivity().finish();
    }





}