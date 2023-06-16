package com.travel.travellingbug.helper;


public interface URLHelper {
    String APP_URL = "";
    int client_id = 2;
    String client_secret = "WifS1rMi3LvuorP1G2UdtKZairUNSH2iMqrKivPf";

//    String BASE = "https://pintaxi.xyz/";
    String BASE = "https://tejratidukan.com/carpool/";
    String STRIPE_TOKEN = "pk_test_LTXZTPA9yepu9dEodKsJm6GA";
    String GET_USERREVIEW = BASE + "api/provider/review";
    String GET_NOTIFICATIONS = BASE + "api/provider/notification";
    String UPCOMING_TRIP_DETAILS = BASE + "api/provider/requests/upcoming/details";



    String UPCOMING_TRIPS = BASE + "api/provider/requests/upcoming";

    String MY_PUBLISH_UPCOMMING_TRIPS = BASE + "api/provider/upcoming/trips";

    String MY_PUBLISH_UPCOMMING_TRIPS_DETAILS = BASE + "api/provider/myupcoming/details";

    String CANCEL_REQUEST_API = BASE + "api/provider/cancel";
    String TARGET_API = BASE + "api/provider/target";
    String RESET_PASSWORD = BASE + "api/provider/reset/password";
    String FORGET_PASSWORD = BASE + "api/provider/forgot/password";
    String FACEBOOK_LOGIN = BASE + "api/provider/auth/facebook";
//    https://travellingbug-cb35b.firebaseapp.com/__/auth/handler
    String GOOGLE_LOGIN = BASE + "api/provider/auth/google";
    String LOGOUT = BASE + "api/provider/logout";
    String SUMMARY = BASE + "api/provider/summary";
    String HELP = BASE + "api/provider/help";
    String COMPLAINT = BASE + "api/user/createComplaint";
    String SAVE_LOCATION = BASE + "api/user/createDefaultLocation";
    String ADD_COUPON_API = BASE + "api/user/promocode/add";
    String COUPON_LIST_API = BASE + "api/user/promocodes";
    String GET_WITHDRAW_LIST = BASE + "api/provider/withdrawaList";

    String GET_CARD_LIST_DETAILS = BASE + "api/provider/BankList";
    String GET_ADD_BANK_DETAILS = BASE + "api/provider/addBank?account_name=";
    String WITHDRAW_REQUEST = BASE + "api/provider/withdrawalRequest?provider_id=";
    String HELP_URL = BASE;

    String login = BASE + "api/provider/oauth/token";

    String register = BASE + "api/provider/register";

//    String login = BASE + "oauth/token";
//    String register = BASE + "api/user/signup";
    String email_check = BASE + "api/provider/check-email";

    String USER_PROFILE_API = BASE + "api/provider/profile";
    String UPDATE_AVAILABILITY_API = BASE + "api/provider/profile/available";
    String GET_HISTORY_API = BASE + "api/provider/requests/history";
    String GET_HISTORY_DETAILS_API = BASE + "api/provider/requests/history/details";
    String CHANGE_PASSWORD_API = BASE + "api/provider/profile/password";

    String CHECK_DOCUMENT = BASE + "api/provider/document/checkDocument";
    String COMPLETE_DOCUMENT = BASE + "api/provider/document/checkDocument?term_n=1";

    String ChatGetMessage = BASE + "api/provider/firebase/getChat?request_id=";

    String CARD_PAYMENT_LIST = BASE + "api/user/card";

    String GET_SERVICE_LIST_API = BASE + "api/user/services";

    String ESTIMATED_FARE_DETAILS_API = BASE + "api/user/estimated/fare";
    String GET_PROVIDERS_LIST_API = BASE + "api/user/show/providers";

    String SEND_REQUEST_API = BASE + "api/user/send/request";
    String SEND_REQUEST_API_PROVIDER = BASE + "api/provider/send/request";



//    #####################################


    String REDIRECT_URL = BASE;
    String REDIRECT_SHARE_URL = "http://maps.google.com/maps?q=loc:";

    String image_url_signature = BASE + "public/";
    String CURRENT_TRIP = BASE + "api/user/trips/current";


    String UserProfile = BASE + "api/user/details";
    String UseProfileUpdate = BASE + "api/user/update/profile";
    String getUserProfileUrl = BASE + "api/user/details";

    String REQUEST_STATUS_CHECK_API = BASE + "api/user/request/check";

    String REQUEST_STATUS_CHECK_API_PROVIDER = BASE + "api/provider/request/check";


    String VALID_ZONE = BASE + "api/user/getvalidzone";

    String PAY_NOW_API = BASE + "api/user/payment";
    String RATE_PROVIDER_API = BASE + "api/user/rate/provider";


    String DELETE_CARD_FROM_ACCOUNT_API = BASE + "api/user/card/destory";


    String addCardUrl = BASE + "api/user/add/money";


    //    Safaricom Payment
    String PAYMENT_TOKEN = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";


    String ADD_CARD_TO_ACCOUNT_API = BASE + "api/user/card";

    String GET_PAYMENT_CONFIRMATION = "api/user/payment/now?total_amount=";


    String NOTIFICATION_URL = BASE + "api/user/notification";

    // for preference
    String PREFERENCES = BASE + "api/provider/profile/preference";

    String PUBLISH_A_RIDE = BASE + "api/provider/publishtrip";

    String UPDATE_LOCATION_ADMIN = BASE + "api/provider/profile/location";

    String BOOK_FOR_UPCOMMING_TRIPS = BASE +  "api/provider/upcoming/checkupcoming";

    String UPCOMMING_TRIPS_DETAILS_ONE = BASE +  "api/provider/myupcoming/details";

    String ACCEPT_REQUEST = BASE +  "api/provider/accepttrip";

    String CANCEL_REQUEST = BASE +  "api/provider/canceltrip";

    String GET_DETAILS_OF_ONE_USER = BASE + "api/provider/userprofile";

    String CANCEL_PUBLISHED_RIDE = BASE + "api/provider/cancel";

    String GET_ALL_RIDES = BASE + "api/provider/myallrides";

    String GET_VEHICLE_DETAILS = BASE + "api/provider/profile/vehicle";

    String UPDATE_SINGLE_USER_RIDE_STATUS_BY_PROVIDER = BASE + "api/provider/changestatusbyprovider";

    String UPDATE_ALL_USER_RIDE_STATUS_BY_PROVIDER = BASE + "api/provider/ride_started";

    String ADD_VERIFICATION_CODE = BASE + "api/provider/addverificationcode";

    String CHECK_VERIFICATION_CODE = BASE + "api/provider/checkverificationcode";

    String PAYMENT_REQUEST_BY_USER = BASE + "api/provider/payment/now";

    String PAYMENT_APPROVED_BY_PROVIDER = BASE + "api/provider/changepaymentstatus";

    String ESTIMATED_FARE_AND_DISTANCE = BASE + "api/provider/estimated/fare";

    String RATE_TO_USER = BASE + "api/provider/rate/user";

    String RATE_TO_PROVIDER = BASE + "api/provider/rate/provider";

    String CHAT_API = BASE + "api/provider/firebase/sendchat";

    String CHANGE_STATUS_BY_USER = BASE +  "api/provider/changestatusbyuser";

    String PREFERENCES_TITLE = BASE +  "api/provider/profile/preferencetitle";









}
