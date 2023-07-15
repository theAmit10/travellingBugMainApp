package com.travel.travellingbug;

import android.telephony.SmsManager;

public class TwilioSMSHelper {
    private static final String ACCOUNT_SID = "ACfe83ce9330faf47eb0a246121dfb8fbf";
    private static final String AUTH_TOKEN = "11c78183aa4dddc0cc680e711398d7cd";
    private static final String TWILIO_PHONE_NUMBER = "+18149046843";

    public static void sendVerificationCode(String phoneNumber, String verificationCode) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            String message = "Your verification code is: " + verificationCode;
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            System.out.println("SEND : "+message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
