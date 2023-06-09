package com.travel.travellingbug.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.ConnectionHelper;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.ChatAppMsgDTO;
import com.travel.travellingbug.models.ChatList;
import com.travel.travellingbug.ui.adapters.ChatAppMsgAdapter;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;



public class UserChatActivity extends AppCompatActivity {

    public Context context = UserChatActivity.this;
    public Activity activity = UserChatActivity.this;
    List<ChatAppMsgDTO> msgDtoList;
    ChatAppMsgAdapter chatAppMsgAdapter;
    ImageView emojiButton;
    EmojiPopup emojiPopup;
    ViewGroup rootView;
    EmojiEditText msgInputText;
    String TAG = "USERCHATACTIVITY";
    private RecyclerView recyclerChat;
    private TextView lblTitle;
    private String requestId;
    private String providerId;
    private String messageType;

    private String userName;
    //    private CustomDialog customDialog;
    private ConnectionHelper helper;
    private boolean isInternet;
    private ArrayList<ChatList> chatListArrayList;
    private ImageView backArrow;
    private String userID;
    private MyBroadcastReceiver receiver;
    private String messageBackGround;
    private String messageRecieveFrombackground;
    private IntentFilter intentFilter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        rootView = findViewById(R.id.main_activity_root_view);
        initViews();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("requestId")) {
                requestId = intent.getExtras().getString("requestId");
                providerId = intent.getExtras().getString("providerId");
                messageType = intent.getExtras().getString("messageType");
                userName = intent.getExtras().getString("userName");
                userID = intent.getExtras().getString("userId");
                if (userName != null) {
                    lblTitle.setText(userName);
                }
            } else {
                messageBackGround = intent.getStringExtra("message");
                if (messageBackGround != null) {
                    Log.e("messsgae", messageBackGround + "messsage");
                    String requestIdBew = intent.getStringExtra("request_id");
                    requestId = requestIdBew;
                    String userNameChat = intent.getStringExtra("userName");
                    lblTitle.setText(userNameChat);
                    //updateView(messageBackGround);// update your textView in the main layout
                }
            }
        }
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.my.app.onMessageReceived");
        receiver = new MyBroadcastReceiver();


    }

    private void initViews() {
        recyclerChat = findViewById(R.id.recyclerChat);
        lblTitle = findViewById(R.id.lblTitle);
        backArrow = findViewById(R.id.backArrow);
        msgInputText = findViewById(R.id.editWriteMessage);
        emojiButton = findViewById(R.id.main_activity_emoji);
        emojiButton.setOnClickListener(ignore -> emojiPopup.toggle());
        // Set RecyclerView layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerChat.setLayoutManager(linearLayoutManager);
        // Create the initial data list.
        msgDtoList = new ArrayList<ChatAppMsgDTO>();

        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (userName != null) {
            lblTitle.setText(userName);
        }


        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                msgDtoList.clear();
                getChatDetails();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        msgInputText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                if (Utilities.keyboardShown(msgInputText.getRootView())) {
//                    Log.d("keyboard", "keyboard UP");
//
//                    recyclerChat.scrollToPosition(chatAppMsgAdapter.getItemCount() - 1);
//                } else {
//                    Log.d("keyboard", "keyboard Down");
//                }
            }
        });


    /*    msgInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerChat.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerChat.scrollToPosition(chatAppMsgAdapter.getItemCount() - 1);
                    }
                }, 10);
            }
        });
*/
        ImageButton msgSendButton = findViewById(R.id.btnSend);

        msgSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msgContent = msgInputText.getText().toString();
                if (!TextUtils.isEmpty(msgContent)) {
                    // Add a new sent message to the list.
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => " + c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    if(messageType.equalsIgnoreCase("pu")){
                        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, msgContent, formattedDate + "");
                        msgDtoList.add(msgDto);
                        getChatDetailsp_u(msgContent);
                        int newMsgPosition = msgDtoList.size() - 1;
                        // Notify recycler view insert one new data.
                        try {
                            chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList);

                            // Set data adapter to RecyclerView.
                            recyclerChat.setAdapter(chatAppMsgAdapter);
                            recyclerChat.scrollToPosition(chatAppMsgAdapter.getItemCount() - 1);
                            // Notify recycler view insert one new data.
                            chatAppMsgAdapter.notifyItemInserted(newMsgPosition);
                            // Scroll RecyclerView to the last message.
                            recyclerChat.scrollToPosition(newMsgPosition);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, msgContent, formattedDate + "");
                        msgDtoList.add(msgDto);
                        getChatDetailsp_u(msgContent);
                        int newMsgPosition = msgDtoList.size() - 1;
                        // Notify recycler view insert one new data.
                        try {
                            chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList);

                            // Set data adapter to RecyclerView.
                            recyclerChat.setAdapter(chatAppMsgAdapter);
                            recyclerChat.scrollToPosition(chatAppMsgAdapter.getItemCount() - 1);
                            // Notify recycler view insert one new data.
                            chatAppMsgAdapter.notifyItemInserted(newMsgPosition);
                            // Scroll RecyclerView to the last message.
                            recyclerChat.scrollToPosition(newMsgPosition);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    // Empty the input edit text box.
                    msgInputText.setText("");
                }
            }
        });

        setUpEmojiPopup();
    }

    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiBackspaceClickListener(ignore -> Log.d(TAG, "Clicked on Backspace"))
                .setOnEmojiClickListener((ignore, ignore2) -> Log.d(TAG, "Clicked on emoji"))
                .setOnEmojiPopupShownListener(() -> emojiButton.setImageResource(R.drawable.ic_keyboard))
                .setOnSoftKeyboardOpenListener(ignore -> Log.d(TAG, "Opened soft keyboard"))
                .setOnEmojiPopupDismissListener(() -> emojiButton.setImageResource(R.drawable.emoji_ios_category_smileysandpeople))
                .setOnSoftKeyboardCloseListener(() -> Log.d(TAG, "Closed soft keyboard"))
                .setKeyboardAnimationStyle(R.style.emoji_fade_animation_style)
                .setPageTransformer(new PageTransformer())
                .build(msgInputText);
    }

    private void updateView(String message) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, message, formattedDate + "");
        msgDtoList.add(msgDto);
        try {
            int newMsgPosition = msgDtoList.size() - 1;
            // Notify recycler view insert one new data.
            chatAppMsgAdapter.notifyItemInserted(newMsgPosition);
            // Scroll RecyclerView to the last message.
            recyclerChat.scrollToPosition(newMsgPosition);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateViewBackground(String message) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, message, formattedDate + "");
        msgDtoList.add(msgDto);
        int newMsgPosition = msgDtoList.size() - 1;

        // Notify recycler view insert one new data.
        chatAppMsgAdapter.notifyItemInserted(newMsgPosition);

        // Scroll RecyclerView to the last message.
        recyclerChat.scrollToPosition(newMsgPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        msgDtoList.clear();
        if (requestId != "" && requestId != null) {
            getChatDetails();
        }
        // put your code here...
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void getChatDetails() {
        if (isInternet) {
//            customDialog = new CustomDialog(UserChatActivity.this);
//            customDialog.setCancelable(true);
//            if (customDialog != null)
//                customDialog.show();
//            https://tejratidukan.com/carpool/api/provider/firebase/getChat?request_id=
//            https://tejratidukan.com/carpool/api/provider/firebase/allchatHistory?request_id=172&user_id=33

//            https://tejratidukan.com/carpool/api/provider/firebase/sendchat?request_id=172&send_to=40&message=second message

            String url;
//            if (requestId != "" && requestId != null) {
//                url = URLHelper.ChatGetMessage + requestId;
//            } else {
//                url = URLHelper.ChatGetMessage + userID + "@" + providerId;
//            }


            if (requestId != "" && requestId != null) {
                url = URLHelper.CHAT_API +"?request_id=" + requestId+"&chattype="+messageType;
            } else {
                url = URLHelper.CHAT_API + userID + "@" + providerId;
            }


            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object, response -> {
//                    if ((customDialog != null) && (customDialog.isShowing()))
//                        customDialog.dismiss();
                Log.d("TAG", "chatListResponse" + response.toString());
                if (response.toString() != null) {
                    JSONObject object1 = null;
                    try {
                        object1 = new JSONObject(response.toString());
                        String status = object1.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            JSONArray jsonArray = object1.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //   msgDtoList.clear();
                                JSONObject jo = jsonArray.getJSONObject(i);
                                SharedHelper.putKey(context, "current_chat_provider_id", "" + jo.getString("provider_id"));
                                SharedHelper.putKey(context, "current_chat_user_id", "" + jo.getString("user_id"));
                                SharedHelper.putKey(context, "current_chat_request_id", "" + jo.getString("request_id"));
                                ChatList chatList = new ChatList();
                                chatList.setProviderId(jo.getString("provider_id"));
                                chatList.setUserId(jo.getString("user_id"));
                                chatList.setRequestId(jo.getString("request_id"));
                                chatList.setMessage(jo.getString("message"));
                                chatList.setType(jo.getString("type"));
                                if (!jo.getString("created_at").contains("null")) {
                                    if (jo.getString("type").contains("up")) {
                                        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, jo.getString("message"), jo.getString("created_at"));
                                        msgDtoList.add(msgDto);
                                    } else {
                                        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, jo.getString("message"), jo.getString("created_at"));
                                        msgDtoList.add(msgDto);
                                    }
                                }
                                // Create the data adapter with above data list.

                            }
                            chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList);

                            // Set data adapter to RecyclerView.
                            recyclerChat.setAdapter(chatAppMsgAdapter);
                            recyclerChat.scrollToPosition(chatAppMsgAdapter.getItemCount() - 1);
                            chatAppMsgAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, error -> {
                displayMessage(getString(R.string.something_went_wrong));
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                    return headers;
                }
            };

            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            displayMessage(getString(R.string.something_went_wrong_net));
        }
    }

    public void getChatHistory() {
        if (isInternet) {


            String url = URLHelper.CHAT_API + "?request_id="+requestId + "&send_to=" +userID+"&message="+msgInputText.getText().toString();
//            if (requestId != "" && requestId != null) {
//                url = URLHelper.ChatGetMessage + requestId;
//            } else {
//                url = URLHelper.ChatGetMessage + userID + "@" + providerId;
//            }
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object, response -> {
//                    if ((customDialog != null) && (customDialog.isShowing()))
//                        customDialog.dismiss();
                Log.d("TAG", "chatListResponse" + response.toString());
                if (response.toString() != null) {
                    JSONObject object1 = null;
                    try {
                        object1 = new JSONObject(response.toString());
                        String status = object1.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            JSONArray jsonArray = object1.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //   msgDtoList.clear();
                                JSONObject jo = jsonArray.getJSONObject(i);
                                SharedHelper.putKey(context, "current_chat_provider_id", "" + jo.getString("provider_id"));
                                SharedHelper.putKey(context, "current_chat_user_id", "" + jo.getString("user_id"));
                                SharedHelper.putKey(context, "current_chat_request_id", "" + jo.getString("request_id"));
                                ChatList chatList = new ChatList();
                                chatList.setProviderId(jo.getString("provider_id"));
                                chatList.setUserId(jo.getString("user_id"));
                                chatList.setRequestId(jo.getString("request_id"));
                                chatList.setMessage(jo.getString("message"));
                                chatList.setType(jo.getString("type"));
                                if (!jo.getString("created_at").contains("null")) {
                                    if (jo.getString("type").contains("pu")) {
                                        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_SENT, jo.getString("message"), jo.getString("created_at"));
                                        msgDtoList.add(msgDto);
                                    } else {
                                        ChatAppMsgDTO msgDto = new ChatAppMsgDTO(ChatAppMsgDTO.MSG_TYPE_RECEIVED, jo.getString("message"), jo.getString("created_at"));
                                        msgDtoList.add(msgDto);
                                    }
                                }
                                // Create the data adapter with above data list.

                            }
                            chatAppMsgAdapter = new ChatAppMsgAdapter(msgDtoList);

                            // Set data adapter to RecyclerView.
                            recyclerChat.setAdapter(chatAppMsgAdapter);
                            recyclerChat.scrollToPosition(chatAppMsgAdapter.getItemCount() - 1);
                            chatAppMsgAdapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, error -> {
                displayMessage(getString(R.string.something_went_wrong));
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                    return headers;
                }
            };

            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            displayMessage(getString(R.string.something_went_wrong_net));
        }
    }



    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void getChatDetailsp_u(String message) {

        String url;
//        if (providerId != null && userID != null) {
//            if (requestId != null) {
////                url = URLHelper.ChatGetMessage + requestId + "&message=" + message + "&provider_id=" + providerId + "&user_id=" + Integer.parseInt(userID) + "&type=pu";
//                url = URLHelper.CHAT_API+"request_id=" + requestId +"&send_to=" +Integer.parseInt(userID)+ "&message=" + message  + "&chattype="+messageType;
//            } else {
////                url = URLHelper.ChatGetMessage + userID + "@" + providerId + "&message=" + message + "&provider_id=" + providerId + "&user_id=" + Integer.parseInt(userID) + "&type=pu";
//                url = URLHelper.ChatGetMessage + userID + "@" + providerId + "&message=" + message + "&provider_id=" + providerId + "&user_id=" + Integer.parseInt(userID) + "&type=pu";
//            }
//        }
////        else {
////            url = URLHelper.ChatGetMessage + SharedHelper.getKey(context, "current_chat_request_id") + "&message=" + message + "&provider_id=" + SharedHelper.getKey(context, "current_chat_provider_id") +
////                    "&user_id=" + SharedHelper.getKey(context, "current_chat_user_id") + "&type=pu";
////        }

        String message_formate = message.replace(" ","_");
        System.out.println("Message : "+message_formate );


        if(requestId != null && userID != null && messageType != null ){

            url = URLHelper.CHAT_API+"?request_id=" + requestId +"&send_to=" +Integer.parseInt(userID)+ "&message=" + message_formate  + "&chattype="+messageType;
        }else {
            url = URLHelper.ChatGetMessage + SharedHelper.getKey(context, "current_chat_request_id") + "&message=" + message_formate + "&provider_id=" + SharedHelper.getKey(context, "current_chat_provider_id") +
                    "&user_id=" + SharedHelper.getKey(context, "current_chat_user_id") + "&type=pu";
        }

        Log.v("chatUrl", url + " ");
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object, response -> {

            Log.e("TAG+pu", "chatListResponse+pu" + response.toString());

        }, error -> {
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    @Override
    protected void onStop() {
        if (emojiPopup != null) {
            emojiPopup.dismiss();
        }

        super.onStop();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String message = extras.getString("message");
            Log.e("messsgae", message + "messsage");
            if (message != null) {
                updateView(message);// update your textView in the main layout
            }
        }
    }

}
