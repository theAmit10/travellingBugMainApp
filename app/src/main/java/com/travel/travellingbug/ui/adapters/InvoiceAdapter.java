package com.travel.travellingbug.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.travel.travellingbug.ClassLuxApp;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.SharedHelper;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.InvoiceModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    // for generating pdf
    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;
    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;


    Context context;
    ArrayList<InvoiceModel> list;

    public InvoiceAdapter(Context context, ArrayList<InvoiceModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_invoice,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InvoiceModel invoiceModel = list.get(position);



        holder.datetime.setText(invoiceModel.getTime());
        holder.txtSource.setText(invoiceModel.getFromAddress());

        holder.txtDestination.setText(invoiceModel.getDestAddress());
        holder.status.setText(invoiceModel.getStatus());
        holder.userName.setText(invoiceModel.getUsername());

        holder.ratingVal.setText(invoiceModel.getRatingVal());
        holder.availableSeat.setText(invoiceModel.getSeat());
//        holder.fare.setText(invoiceModel.getFare());
        holder.carTypeVal.setText(invoiceModel.getVehicleDetails());

        holder.listitemrating.setRating(Float.parseFloat(invoiceModel.getRating()));

        Picasso.get().load(invoiceModel.getImage()).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);

        // for fare details
        try {
            StringRequest request = new StringRequest(Request.Method.GET, URLHelper.ESTIMATED_FARE_AND_DISTANCE + "?s_latitude=" + invoiceModel.getSlat() + "&s_longitude=" + invoiceModel.getSlong() + "&d_latitude=" + invoiceModel.getDlat() + "&d_longitude=" + invoiceModel.getDlong() + "&service_type=2", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (response != null) {
                            System.out.println("payment details estimated data : " + jsonObject.toString());
                            jsonObject.optString("estimated_fare");
                            jsonObject.optString("distance");
                            jsonObject.optString("time");
                            jsonObject.optString("tax_price");
                            jsonObject.optString("base_price");
                            jsonObject.optString("discount");
                            jsonObject.optString("currency");

                            String con = jsonObject.optString("currency") + " ";


                            System.out.println("ESTIMATED FARE STATUS :" + response.toString());




                            try {
                                System.out.println("Fare : "+con + jsonObject.optString("estimated_fare"));

                                Double fares = Double.valueOf(jsonObject.optString("estimated_fare"));
                                String no_of_seat_string = String.valueOf(invoiceModel.getSeat().charAt(0));
                                System.out.println(no_of_seat_string);
                                int no_of_seat = Integer.parseInt(no_of_seat_string);
                                Double c_fare = fares * no_of_seat;
                                String calculated_fare = con + c_fare;

                                holder.fare.setText(calculated_fare);

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }) {




                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                    return headers;
                }

            };

            ClassLuxApp.getInstance().addToRequestQueue(request);



        }catch (Exception e){
            e.printStackTrace();
        }


        holder.invoiceDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(context, "Processing", Toast.LENGTH_SHORT).show();

                bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_logo_org);
                scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);



                StringRequest request = new StringRequest(Request.Method.GET, URLHelper.ESTIMATED_FARE_AND_DISTANCE + "?s_latitude=" + invoiceModel.getSlat() + "&s_longitude=" + invoiceModel.getSlong() + "&d_latitude=" + invoiceModel.getDlat() + "&d_longitude=" + invoiceModel.getDlong() + "&service_type=2", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (response != null) {
                                System.out.println("payment details estimated data : " + jsonObject.toString());
                                jsonObject.optString("estimated_fare");
                                jsonObject.optString("distance");
                                jsonObject.optString("time");
                                jsonObject.optString("tax_price");
                                jsonObject.optString("base_price");
                                jsonObject.optString("discount");
                                jsonObject.optString("currency");

                                String con = jsonObject.optString("currency") + " ";


//                                    txt04InvoiceId.setText(jsonArray.optJSONObject(position).optString("booking_id"));
//                                    txt04BasePrice.setText(con + jsonObject.optString("base_price"));
//                                    txt04Distance.setText(jsonObject.optString("distance") + " KM");
//                                    txt04Tax.setText(con + jsonObject.optString("tax_price"));
//                                    txt04Total.setText(con + jsonObject.optString("estimated_fare"));
//                                    txt04PaymentMode.setText("CASH");
//                                    txt04Commision.setText(con + jsonObject.optString("discount"));
//                                    txtTotal.setText(con + jsonObject.optString("estimated_fare"));
//                                    paymentTypeImg.setImageResource(R.drawable.money1);
//                                    btn_confirm_payment.setVisibility(View.VISIBLE);

                                System.out.println("ESTIMATED FARE STATUS :" + response.toString());



                                // creating an object variable
                                // for our PDF document.
                                PdfDocument pdfDocument = new PdfDocument();

                                // two variables for paint "paint" is used
                                // for drawing shapes and we will use "title"
                                // for adding text in our PDF file.
                                Paint paint = new Paint();
                                Paint title = new Paint();

                                // we are adding page info to our PDF file
                                // in which we will be passing our pageWidth,
                                // pageHeight and number of pages and after that
                                // we are calling it to create our PDF.
                                PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

                                // below line is used for setting
                                // start page for our PDF file.
                                PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

                                // creating a variable for canvas
                                // from our page of PDF.
                                Canvas canvas = myPage.getCanvas();

                                // below line is used to draw our image on our PDF file.
                                // the first parameter of our drawbitmap method is
                                // our bitmap
                                // second parameter is position from left
                                // third parameter is position from top and last
                                // one is our variable for paint.
                                canvas.drawBitmap(scaledbmp, 56, 40, paint);

                                // below line is used for adding typeface for
                                // our text which we will be adding in our PDF file.
                                title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

                                // below line is used for setting text size
                                // which we will be displaying in our PDF file.
                                title.setTextSize(15);

                                // below line is sued for setting color
                                // of our text inside our PDF file.
                                title.setColor(ContextCompat.getColor(context, R.color.black));

                                // below line is used to draw text in our PDF file.
                                // the first parameter is our text, second parameter
                                // is position from start, third parameter is position from top
                                // and then we are passing our variable of paint which is title.
                                canvas.drawText("Travelling Bug", 209, 80, title);
                                canvas.drawText("", 209, 100, title);
                                canvas.drawText("We believe that travel is not just a hobby, it's a way of life.", 209, 120, title);
                                canvas.drawText("Our mission is to provide exceptional  travel experiences ", 209, 140, title);
                                canvas.drawText("that inspire and enrich our clients'lives.", 209, 160, title);
                                canvas.drawText("With our expertise and passion for travel,", 209, 180, title);
                                canvas.drawText("we make your dream vacation a reality.", 209, 200, title);


                                // similarly we are creating another text and in this
                                // we are aligning this text to center of our PDF file.
                                title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                title.setColor(ContextCompat.getColor(context, R.color.black));
                                title.setTextSize(24);


                                // below line is used for setting
                                // our text to center of PDF.
                                title.setTextAlign(Paint.Align.CENTER);

                                canvas.drawText("--------------------------------------------", 396, 540, title);
                                canvas.drawText("INVOICE", 396, 560, title);
                                canvas.drawText("--------------------------------------------", 396, 600, title);
                                canvas.drawText("Booking ID             "+invoiceModel.getBookingId(), 396, 630, title);
                                canvas.drawText("Base fare              "+con + jsonObject.optString("base_price"), 396, 670, title);
                                canvas.drawText("Distance               "+jsonObject.optString("distance") + " KM", 396, 710, title);
                                canvas.drawText("Tax                    "+con + jsonObject.optString("tax_price"), 396, 750, title);
                                canvas.drawText("--------------------------------------------", 396, 790, title);
                                canvas.drawText("Total                  "+con + jsonObject.optString("estimated_fare"), 396, 830, title);
                                canvas.drawText("--------------------------------------------", 396, 870, title);

                                // after adding all attributes to our
                                // PDF file we will be finishing our page.
                                pdfDocument.finishPage(myPage);

                                // below line is used to set the name of
                                // our PDF file and its path.
                                File file = new File(Environment.getExternalStorageDirectory(), "TravellingBug"+invoiceModel.getBookingId()+".pdf");

                                try {
                                    // after creating a file name we will
                                    // write our PDF file to that location.
                                    pdfDocument.writeTo(new FileOutputStream(file));

                                    // below line is to print toast message
                                    // on completion of PDF generation.
                                    Toast.makeText(context, "PDF file saved successfully.", Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    // below line is used
                                    // to handle error
                                    e.printStackTrace();
                                }
                                // after storing our pdf to that
                                // location we are closing our PDF file.
                                pdfDocument.close();



                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }) {




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






    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView datetime, txtSource, txtDestination, status, userName, ratingVal, availableSeat, fare, carTypeVal;

        RatingBar listitemrating;

        ImageView profileImgeIv,invoiceDownload;
        Button rateRider;

        LinearLayout historyContainerLL;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            datetime = itemView.findViewById(R.id.datetime);
            txtSource = itemView.findViewById(R.id.txtSource);
            txtDestination = itemView.findViewById(R.id.txtDestination);
            status = itemView.findViewById(R.id.status);
            rateRider = itemView.findViewById(R.id.rateRider);
            userName = itemView.findViewById(R.id.userName);
            ratingVal = itemView.findViewById(R.id.ratingVal);
            listitemrating = itemView.findViewById(R.id.listitemrating);
            profileImgeIv = itemView.findViewById(R.id.profileImgeIv);
//                historyContainerLL = itemView.findViewById(R.id.historyContainerLL);

            carTypeVal = itemView.findViewById(R.id.carTypeVal);
            fare = itemView.findViewById(R.id.fare);
            availableSeat = itemView.findViewById(R.id.availableSeat);
            invoiceDownload = itemView.findViewById(R.id.invoiceDownload);
        }
    }
}
