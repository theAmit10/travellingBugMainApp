package com.travel.travellingbug.fcm;


import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.travel.travellingbug.R;
import com.travel.travellingbug.chat.UserChatActivity;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.ui.activities.HomeScreenActivity;

import java.util.List;


public class FCMService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        SharedHelper.putKey(getApplicationContext(), "device_token", "" + s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.v("remoteMessage", remoteMessage.getNotification().getBody() + " ");
        Log.v("firebaseImage", remoteMessage.getNotification().getImageUrl() + " ");


        try {
            String msg_type = "";
            if (remoteMessage.getData().get("msg_type") != null) {
                msg_type = remoteMessage.getData().get("msg_type");
            }
            if (msg_type.contains("chat")) {

                if (getTopAppName().equals(UserChatActivity.class.getName())) {

                    Log.e(TAG, "FCM IF UserChatActivity");
                    Intent intent = new Intent();
                    intent.putExtra("message", remoteMessage.getNotification().getBody());
                    intent.setAction("com.travel.travellingbug.onMessageReceived");
//                    intent.setAction("com.my.app.onMessageReceived");
                    sendBroadcast(intent);
                    System.out.println("FCM IF ");


                } else {
                    Log.e(TAG, "FCM else UserChatActivity");
                    System.out.println("FCM ");
                    System.out.println("FCM MessageType "+remoteMessage.getMessageType());
                    System.out.println("FCM MessageData "+remoteMessage.getData());
                    handleNotification(remoteMessage);
                }
            } else if (msg_type.contains("admin")) {

                Log.e(TAG, "FCM IF ADMIN");
                System.out.println("FCM IF ADMIN ");

                // Create a notification channel.
                NotificationChannel channel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    String title = remoteMessage.getNotification().getTitle();
                    String message = remoteMessage.getNotification().getBody();
                    String click_action = "com.travel.travellingbug.TARGETNOTIFICATION";

                    channel = new NotificationChannel(getString(R.string.default_notification_channel_id), title, NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription(message);
                    // Get the notification manager.
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.createNotificationChannel(channel);


                    Intent intent = new Intent(click_action);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

                    // Build a notification.
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));
                    builder.setContentTitle(title);
                    builder.setContentText(message);
                    builder.setSmallIcon(R.drawable.app_logo_org);
                    builder.setAutoCancel(true);
                    builder.setContentIntent(pendingIntent);

                    // Show the notification.
                    notificationManager.notify(Integer.parseInt(getString(R.string.default_notification_channel_id)), builder.build());


                }else {

                    String title = remoteMessage.getNotification().getTitle();
                    String message = remoteMessage.getNotification().getBody();
                    System.out.println("MESSSAGE admin  : "+message);
                    System.out.println("MESSSAGE admin TITLE  : "+title);
//                String click_action = "com.classluxdrive.providers.TARGETNOTIFICATION";
                    String click_action = "com.travel.travellingbug.TARGETNOTIFICATION";
                    Intent intent = new Intent(click_action);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);





                    NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));
                    notifiBuilder.setContentTitle(title);
                    notifiBuilder.setContentText(message);
                    notifiBuilder.setSmallIcon(R.drawable.app_logo_org);
                    notifiBuilder.setAutoCancel(true);
                    notifiBuilder.setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(0, notifiBuilder.build());

                }



            } else if (remoteMessage.getNotification().getBody().trim().contains("New Incoming Ride")) {

                Log.e(TAG, "FCM IF New Incoming Ride ");
                Log.v("callBroadcast", "callbroadcast");
                Intent i = new Intent(this, MyBroadcastReceiver.class);
                sendBroadcast(i);

                Intent notifyIntent = new Intent(this, HomeScreenActivity.class);
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                        this, 0, notifyIntent, PendingIntent.FLAG_IMMUTABLE
                );
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));
                builder.setSmallIcon(R.drawable.app_logo_org);
                builder.setContentIntent(notifyPendingIntent);
                builder.setContentTitle(getString(R.string.app_name));
                builder.setContentText("Ride Request");

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(0, builder.build());
            } else {

                Log.e(TAG, "FCM ELSE ADMIN NORMAL ");
                System.out.println("FCM ELSE ADMIN NORMAL ");
                Log.v("generalnotification", "generalnotification");
                sendNotification(remoteMessage.getData().get("message"));
            }
        } catch (Exception ne) {

            Log.e(TAG, "FCM EXCEPTION " + ne.getMessage());
            Log.v("Exceptionnotification", "Exceptionnotification");
            ne.printStackTrace();
            sendNotification(remoteMessage.getData().get("message"));
        }


    }

    public String getTopAppName() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        Log.i("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getShortClassName());
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        componentInfo.getPackageName();
        return taskInfo.get(0).topActivity.getClassName();
    }

    private void handleNotification(RemoteMessage remoteMessage) {
        String requestId = remoteMessage.getData().get("request_id");
        sendNotification(getString(R.string.app_name), remoteMessage.getNotification().getBody(), requestId, remoteMessage.getData().get("user_name"));
    }

    private void sendNotification(String notificationTitle, String notificationBody, String requestId, String userName) {
        Intent intent = new Intent(this, HomeScreenActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.e(TAG, "Notification JSON " + requestId + userName + notificationBody);
        try {

            // Create a notification channel.
            NotificationChannel channel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                String title = notificationTitle;
                String message = notificationBody;
                String click_action = "com.travel.travellingbug.TARGETNOTIFICATION";

                channel = new NotificationChannel(getString(R.string.default_notification_channel_id), title, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(message);
                // Get the notification manager.
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);


                Intent intent2 = new Intent(click_action);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

                // Build a notification.
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));
                builder.setContentTitle(title);
                builder.setContentText(message);
                builder.setSmallIcon(R.drawable.app_logo_org);
                builder.setAutoCancel(true);
                builder.setContentIntent(pendingIntent);

                // Show the notification.
                notificationManager.notify(Integer.parseInt(getString(R.string.default_notification_channel_id)), builder.build());
            }else {
                String title = notificationTitle;
                String message = notificationBody;

                System.out.println("MESSSAGE send notification  : "+message);
                System.out.println("MESSSAGE send notification TITLE  : "+title);
                intent.putExtra("message", message);
                intent.putExtra("request_id", requestId);
                intent.putExtra("userName", userName);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* TripRequest code */, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.drawable.app_logo_org)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            // check for orio 8
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), title, importance);
//                notificationChannel.enableLights(true);
//                notificationChannel.setLightColor(Color.RED);
//                notificationChannel.enableVibration(true);
//                notificationBuilder.setChannelId(getString(R.string.default_notification_channel_id));
//                notificationManager.createNotificationChannel(notificationChannel);
//            }

//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//            {
//                // Request the POST_NOTIFICATIONS permission.
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
//
//                // Check if the user granted the permission.
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//                    // Send the notification.
//                    // ...
//                }
//            }
                assert notificationManager != null;
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

            }




        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void sendNotification(String notificationBody) {
        System.out.println("FCM sendNotification ");
        Intent intent = new Intent(this, HomeScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {

//            String message = notificationBody;
//            intent.putExtra("message", message);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new
//                    NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
//                    .setSmallIcon(R.drawable.app_logo_org)
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText(message)
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent)
//                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//            System.out.println("MESSSAGE send notification only body : "+message);



            // check for orio 8
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                // String channelId = context.getString(R.string.default_notification_channel_id);
//                NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.default_notification_channel_id), notificationBody, importance);
//                notificationChannel.enableLights(true);
//                notificationChannel.setLightColor(Color.RED);
//                notificationChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(notificationChannel);
//                notificationBuilder.setChannelId(getString(R.string.default_notification_channel_id));
//            }

            // Create a notification channel.
            NotificationChannel channel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String title = "Travelling Bug";
                String message = notificationBody;
                String click_action = "com.travel.travellingbug.TARGETNOTIFICATION";

                channel = new NotificationChannel(getString(R.string.default_notification_channel_id), title, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(message);
                // Get the notification manager.
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);


                Intent intent2 = new Intent(click_action);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

                // Build a notification.
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));
                if(message != null && !message.equalsIgnoreCase("")){
                    builder.setContentText(message);
                }else {
                    builder.setContentText("A new message is waiting for you");
                }
                builder.setContentTitle(title);

                builder.setSmallIcon(R.drawable.app_logo_org);
                builder.setAutoCancel(true);
                builder.setContentIntent(pendingIntent);

                // Show the notification.
                notificationManager.notify(Integer.parseInt(getString(R.string.default_notification_channel_id)), builder.build());
            }else {
                String message = notificationBody;
                intent.putExtra("message", message);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new
                        NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.drawable.app_logo_org)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                System.out.println("MESSSAGE send notification only body : "+message);


                assert notificationManager != null;
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

            }





//
//            assert notificationManager != null;
//            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        } catch (Exception e) {
            Log.v(TAG, "Exception: " + e.getMessage());
        }
    }

}
