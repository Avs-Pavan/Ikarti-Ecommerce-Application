package com.webinfrasolutions.ikarti.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.webinfrasolutions.ikarti.API.SeeNOtificationApi;
import com.webinfrasolutions.ikarti.Login;
import com.webinfrasolutions.ikarti.Orders;
import com.webinfrasolutions.ikarti.Pojo.MyPojo;
import com.webinfrasolutions.ikarti.Pojo.OrderItem;
import com.webinfrasolutions.ikarti.R;
import com.webinfrasolutions.ikarti.StoreOrders;
import com.webinfrasolutions.ikarti.util.Config;
import com.webinfrasolutions.ikarti.util.NotificationUtils;
import com.webinfrasolutions.ikarti.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
switch (remoteMessage.getData().get("id")){
    case  "0" :
    {
        newOrderNOtification(remoteMessage);break;
    }case  "1" :
    {
        acceptedOrderNotification(remoteMessage);break;
    }case  "2" :
    {
        declinedOrderNotification(remoteMessage);break;
    }
    case "3" : {
        dispatchedOrderNotification(remoteMessage);break;
    }
    case "5":{
deliveredOrderNotification(remoteMessage);break;

    }
}
        }
    }


    private void showNotification() {
        Intent notificationIntent = new Intent(getApplicationContext(), StoreOrders.class);
        notificationIntent.putExtra("from","notification");
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int num = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this, num, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.location_pin_destination);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                .setSmallIcon(R.drawable.destination_location_pin)
                .setLargeIcon(bm)
                .setContentTitle("New Order Requested")
                .setTicker("setTicker")
                .setContentText("Please Open Application And Respond To Order")
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(num, noBuilder.build()); //0 = ID of notification
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), Login.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
public  void seeNotification(){

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    // Change base URL to your upload server URL.
    SeeNOtificationApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(SeeNOtificationApi.class);
    retrofit2.Call<MyPojo> mService = service.change("3");
    mService.enqueue(new Callback<MyPojo>() {
        @Override
        public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {

        }

        @Override
        public void onFailure(Call<MyPojo> call, Throwable t) {

        }
    });
}


public void newOrderNOtification(RemoteMessage data){

    Gson gson=new Gson();
    OrderItem obj=gson.fromJson(data.getData().get("body"),OrderItem.class);



// Using RemoteViews to bind custom layouts into Notification
    RemoteViews remoteViews = new RemoteViews(getPackageName(),
            R.layout.customnotification);
    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    // Set Notification Title
    String strtitle = getString(R.string.customnotificationtitle);
    // Set Notification Text
    String strtext = getString(R.string.customnotificationtext);

    // Open NotificationView Class on Notification Click
    Intent intent = new Intent(this, StoreOrders.class);
    // Send data to NotificationView Class
    intent.putExtra("title", strtitle);
    intent.putExtra("text", strtext);
    // Open NotificationView.java Activity
    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
            // Set Icon
            .setSmallIcon(R.drawable.ic_add_shopping_cart_black_24dp)
            // Set Ticker Message
            .setTicker(getString(R.string.customnotificationticker))
            // Dismiss Notification
            .setAutoCancel(true)
            .setSound(sound)

            // Set PendingIntent into Notification
            .setContentIntent(pIntent)
            // Set RemoteViews into Notification
            .setContent(remoteViews);

    // Locate and set the Image into customnotificationtext.xml ImageViews
    remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.ic_add_shopping_cart_black_24dp);
    //remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.iphone);

    // Locate and set the Text into customnotificationtext.xml TextViews
    remoteViews.setTextViewText(R.id.title,"New Order For "+obj.getProduct().get(0).getProductName());
    remoteViews.setTextViewText(R.id.text,"Qt "+obj.getQuantity()+" Pieces");

    // Create Notification Manager
    NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    // Build Notification with Notification Manager
    notificationmanager.notify(0, builder.build());

}

private void acceptedOrderNotification(RemoteMessage data) {
        Gson gson=new Gson();
        OrderItem obj=gson.fromJson(data.getData().get("body"),OrderItem.class);



// Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Set Notification Title
        String strtitle = getString(R.string.customnotificationtitle);
        // Set Notification Text
        String strtext = getString(R.string.customnotificationtext);

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, Orders.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.ic_add_shopping_cart_black_24dp)
                // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                // Dismiss Notification
                .setAutoCancel(true)
                .setSound(sound)

                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.ic_add_shopping_cart_black_24dp);
    //    remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.iphone);

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title,"Your Order Accepted BY StoreKeeper");
        remoteViews.setTextViewText(R.id.text,obj.getQuantity()+" X "+obj.getProduct().get(0).getProductName());

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }

private void declinedOrderNotification(RemoteMessage data) {
        Gson gson=new Gson();
        OrderItem obj=gson.fromJson(data.getData().get("body"),OrderItem.class);



// Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Set Notification Title
        String strtitle = getString(R.string.customnotificationtitle);
        // Set Notification Text
        String strtext = getString(R.string.customnotificationtext);

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, Orders.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.order_declined)
                // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                // Dismiss Notification
                .setAutoCancel(true)
                .setSound(sound)

                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.order_declined);
      //  remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.iphone);

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title,"Your Order Declined  BY StoreKeeper");
        remoteViews.setTextViewText(R.id.text,obj.getQuantity()+" X "+obj.getProduct().get(0).getProductName()+"\n"+"Reason : "+"My Reason");

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }


    private void dispatchedOrderNotification(RemoteMessage data) {
        Gson gson=new Gson();
        OrderItem obj=gson.fromJson(data.getData().get("body"),OrderItem.class);



// Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Set Notification Title
        String strtitle = getString(R.string.customnotificationtitle);
        // Set Notification Text
        String strtext = getString(R.string.customnotificationtext);

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, Orders.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.order_dispatched)
                // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                // Dismiss Notification
                .setAutoCancel(true)
                .setSound(sound)

                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.order_dispatched);
       // remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.iphone);

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title,"Your Order Has Been Dispatched");
        remoteViews.setTextViewText(R.id.text,obj.getQuantity()+" X "+obj.getProduct().get(0).getProductName());

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }

    private void deliveredOrderNotification(RemoteMessage data) {
        int utype=new SessionManager().getUserType(getApplicationContext());
        Gson gson=new Gson();
        OrderItem obj=gson.fromJson(data.getData().get("body"),OrderItem.class);



// Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Set Notification Title
        String strtitle = getString(R.string.customnotificationtitle);
        // Set Notification Text
        String strtext = getString(R.string.customnotificationtext);

        // Open NotificationView Class on Notification Click

        Intent intent = null;
        if (utype==2){
            intent= new Intent(this, Orders.class);

        }else {
            intent= new Intent(this, StoreOrders.class);

        }
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.order_delivered)
                // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                // Dismiss Notification
                .setAutoCancel(true)
                .setSound(sound)

                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.order_delivered);
       // remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.iphone);

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title,"Your Order Has Been Delivered");
        remoteViews.setTextViewText(R.id.text,obj.getQuantity()+" X "+obj.getProduct().get(0).getProductName());
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }

}
