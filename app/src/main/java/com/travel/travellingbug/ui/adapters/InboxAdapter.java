package com.travel.travellingbug.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.chat.InboxChatActivity;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.InboxModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    Context context;
    ArrayList<InboxModel> list;

    public InboxAdapter(Context context, ArrayList<InboxModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_inbox_fragment,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        InboxModel inboxModel = list.get(position);

        try {

            holder.userName.setText(inboxModel.getUsername());
            try {
                Picasso.get().load(inboxModel.getProfileImage()).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);
            }catch (Exception e){
                e.printStackTrace();
            }

            holder.historyContainerLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println("TYPE : "+inboxModel.getType());

                    Toast.makeText(context, "Loading... ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, InboxChatActivity.class);




                    StringRequest request = new StringRequest(Request.Method.POST, URLHelper.UPCOMMING_TRIPS_DETAILS_ONE, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                if (jsonArray.length() > 0) {

                                    if(jsonArray.getJSONObject(0).optString("provider_id").equalsIgnoreCase(SharedHelper.getKey(context,"id")))
                                    {
                                        intent.putExtra("requestId",inboxModel.getRequestId());
                                        intent.putExtra("providerId", inboxModel.getProviderId());
                                        intent.putExtra("userId", inboxModel.getUserId());
                                        intent.putExtra("userName", inboxModel.getUsername());
                                        intent.putExtra("messageType", "up");
                                        context.startActivity(intent);
                                    }else {
                                        intent.putExtra("requestId",inboxModel.getRequestId());
                                        intent.putExtra("providerId", inboxModel.getProviderId());
                                        intent.putExtra("userId", inboxModel.getUserId());
                                        intent.putExtra("userName", inboxModel.getUsername());
                                        intent.putExtra("messageType", "pu");
                                        context.startActivity(intent);

                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }

                    }) {


                        @Override
                        public Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("request_id", inboxModel.getRequestId());
                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("X-Requested-With", "XMLHttpRequest");
                            headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                            return headers;
                        }

                    };

                    ClassLuxApp.getInstance().addToRequestQueue(request);

                }
            });


//            holder.historyContainerLL.setOnClickListener(view -> {
//
//                System.out.println("TYPE : "+inboxModel.getType());
////                // Create a notification channel.
////                NotificationChannel channel = null;
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                    channel = new NotificationChannel(context.getString(R.string.default_notification_channel_id), "My Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
////                    channel.setDescription("This is my notification channel.");
////                    // Get the notification manager.
////                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
////                    notificationManager.createNotificationChannel(channel);
////
////                    // Build a notification.
////                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id));
////                    builder.setContentTitle("My Notification");
////                    builder.setContentText("This is a notification from my app.");
////                    builder.setSmallIcon(R.drawable.ic_notification);
////
////
////                    // Show the notification.
////                    notificationManager.notify(Integer.parseInt(context.getString(R.string.default_notification_channel_id)), builder.build());
////                }
//
////                     for Android 13
////                // Request the POST_NOTIFICATIONS permission.
////                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
////
////                // Check if the user granted the permission.
////                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
////                    // Send the notification.
////                    // ...
////                }
//
//
////                // Check if the user granted the permission.
////                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
////                    // Send the notification.
////                    // ...
////                    NotificationChannel channel = null;
////                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////                        channel = new NotificationChannel(context.getString(R.string.default_notification_channel_id), "My Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
////                        channel.setDescription("This is my notification channel.");
////                        // Get the notification manager.
////                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
////                        // Register the notification channel.
////                        notificationManager.createNotificationChannel(channel);
////
////                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id));
////                        builder.setContentTitle("My Notification");
////                        builder.setContentText("This is a notification from my app.");
////                        builder.setSmallIcon(R.drawable.ic_notification);
////
////
////                        notificationManager.notify(Integer.parseInt(context.getString(R.string.default_notification_channel_id)), builder.build());
////
////                    }
////
////                }
//
//
//                Toast.makeText(context, "Loading... ", Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(context, InboxChatActivity.class);
//
//
//
//
//                    StringRequest request = new StringRequest(Request.Method.POST, URLHelper.UPCOMMING_TRIPS_DETAILS_ONE, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            try {
//                                JSONArray jsonArray = new JSONArray(response);
//
//                                if (jsonArray.length() > 0) {
//
//                                    if(jsonArray.getJSONObject(0).optString("provider_id").equalsIgnoreCase(SharedHelper.getKey(context,"id")))
//                                    {
//                                        intent.putExtra("requestId",inboxModel.getRequestId());
//                                        intent.putExtra("providerId", inboxModel.getProviderId());
//                                        intent.putExtra("userId", inboxModel.getUserId());
//                                        intent.putExtra("userName", inboxModel.getUsername());
//                                        intent.putExtra("messageType", "up");
//                                        context.startActivity(intent);
//                                    }else {
//                                        intent.putExtra("requestId",inboxModel.getRequestId());
//                                        intent.putExtra("providerId", inboxModel.getProviderId());
//                                        intent.putExtra("userId", inboxModel.getUserId());
//                                        intent.putExtra("userName", inboxModel.getUsername());
//                                        intent.putExtra("messageType", "pu");
//                                        context.startActivity(intent);
//
//                                    }
//
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            error.printStackTrace();
//                        }
//
//                    }) {
//
//
//                        @Override
//                        public Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<>();
//                            params.put("request_id", inboxModel.getRequestId());
//                            return params;
//                        }
//
//                        @Override
//                        public Map<String, String> getHeaders() {
//                            HashMap<String, String> headers = new HashMap<String, String>();
//                            headers.put("X-Requested-With", "XMLHttpRequest");
//                            headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
//                            return headers;
//                        }
//
//                    };
//
//                    ClassLuxApp.getInstance().addToRequestQueue(request);
//
////                intent.putExtra("requestId",inboxModel.getRequestId());
////                intent.putExtra("providerId", inboxModel.getProviderId());
////                intent.putExtra("userId", inboxModel.getUserId());
////                intent.putExtra("userName", inboxModel.getUsername());
////                intent.putExtra("messageType", inboxModel.getType());
////
////                context.startActivity(intent);
//
//
//            });

        }catch (Exception e){
            e.printStackTrace();
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView  userName;
        ImageView profileImgeIv;
        LinearLayout historyContainerLL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            profileImgeIv = itemView.findViewById(R.id.profileImgeIv);
            historyContainerLL = itemView.findViewById(R.id.historyContainerLL);



        }
    }
}



