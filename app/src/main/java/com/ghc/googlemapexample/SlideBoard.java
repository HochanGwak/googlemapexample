package com.ghc.googlemapexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.media.MediaMetadata;
import android.media.RemoteController;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.time.DayOfWeek;
import java.util.ArrayList;

public class SlideBoard extends BottomSheetDialogFragment {

    ClipData clipData;
    ContentResolver contentResolver;
    TextView textView_date;
    EditText edt_coment;
    DatePickerDialog.OnDateSetListener callbackMethod;
    Button btn_changedate, btn_save;
    Context context;
    RequestQueue requestQueue;
    ArrayList<String>latList,lngList;

    ImageView sel;




    public SlideBoard(ClipData clipData, ContentResolver contentResolver) throws IOException {
        this.clipData = clipData;
        this.contentResolver = contentResolver;

        ArrayList<Uri> uris = new ArrayList<>();

        latList = new ArrayList<String>();
        lngList = new ArrayList<String>();



        for (int i = 0; i < clipData.getItemCount(); i++) {

            ClipData.Item item = clipData.getItemAt(i);

            Uri uri = item.getUri();

            String path = getPathFromUri(uri);


            ExifInterface exif = new ExifInterface(new File(path).getAbsoluteFile());

            try {
                String date = exif.getAttribute(ExifInterface.TAG_DATETIME);
                String lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                String lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);

                float latitude = convertToDegree(lat);
                float longitude = convertToDegree(lon);

                latList.add(String.valueOf(latitude));
                lngList.add(String.valueOf(longitude));
                Log.v("asdf", date + " / " + latList.get(i) + " : " + lngList.get(i));

                uris.add(uri);

            } catch (Exception e) {
                Log.v("asdf", "SlideBoard: 위치정보 없음");
                e.printStackTrace();
            }

        }



    }
    //bottomslideboard생성
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_slide_board, container, false);

        ImageView img_picture = v.findViewById(R.id.img_picture);
        Button btn_save = v.findViewById(R.id.btn_save);
       // Button btn_changedate = v.findViewById(R.id.btn_changedate);
        img_picture.setImageURI(clipData.getItemAt(0).getUri());
        EditText edt_coment = v.findViewById(R.id.edt_coment);
//        callbackMethod = new DatePickerDialog.OnDateSetListener()
//        {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
//            {
//                textView_date.setText(year + "년" + monthOfYear + "월" + dayOfMonth + "일");
//            }
//        };
//
//        DatePickerDialog dialog = new DatePickerDialog(getContext(), callbackMethod, 2019, 5, 24);

      //  btn_changedate.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
      //          Log.v("asd", "dssda");
       //         dialog.show();

       //     }
      //   });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("asdf", "SlideBoard저장: "+latList.size()+" / "+lngList.size());

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("lat", latList);
                intent.putExtra("lng", lngList);
                intent.putExtra("et_tx", edt_coment.getText().toString());
                intent.putExtra("pic", clipData);

                String edt = edt_coment.getText().toString();

                for (int i = 0; i < latList.size(); i++) {
                            if (requestQueue == null){
                                requestQueue = Volley.newRequestQueue(getContext());
                    }
                    String url = "http://172.30.1.34:3003/insertphoto";
                    url+= "?memo="+edt;
                    url+= "?memo="+latList.get(i);
                    url+= "?memo="+lngList.get(i);

                    StringRequest request = new StringRequest(
                            Request.Method.GET,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.v("asd", "ddd");
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.v("asd", "Error");
                                }
                            }
                    );
                    requestQueue.add(request);
                    startActivity(intent);
                }


            }

            });



        return v;
    }

    public String getPathFromUri(Uri uri){

        Cursor cursor = contentResolver.query(uri, null, null, null, null );

        cursor.moveToNext();

        @SuppressLint("Range") String path = cursor.getString( cursor.getColumnIndex( "_data" ));


        return path;
    }

    private float convertToDegree(String stringDMS) {
        Float result = null;
        String [] DMS = stringDMS.split(",",3);

        String[] stringD = DMS[0].split("/",2);
        Double D0 = Double.valueOf(stringD[0]);
        Double D1 = Double.valueOf(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/",2);
        Double M0 = Double.valueOf(stringM[0]);
        Double M1 = Double.valueOf(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/",2);
        Double S0 = Double.valueOf(stringS[0]);
        Double S1 = Double.valueOf(stringS[1]);
        Double FloatS = S0/S1;

        result = (float) (FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;
    }

}