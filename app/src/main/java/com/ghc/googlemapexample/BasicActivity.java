package com.ghc.googlemapexample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BasicActivity extends AppCompatActivity {

    GridView gridView;
    Button btn1, btn_loginStart;
    static ArrayList<GridVO> data;
    static GridAdapter adapter;
    RequestQueue requestQueue;

    private static final int REQUEST_IMAGE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        gridView = findViewById(R.id.gridview);
        btn_loginStart = findViewById(R.id.btn_loginStart);


        data = new ArrayList<>();

        data.add(new GridVO("title", Uri.parse("android.resource://"+getPackageName()+"/" + R.drawable.first), -1));




        // new 하는게 아니라 서버에서 데이터 불러오기!


        adapter = new GridAdapter(getApplicationContext(), R.layout.grid, data, getSupportFragmentManager());

        gridView.setAdapter(adapter);

        btn_loginStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(this);
        }
        String url = "http://172.30.1.34:3003/inserttitle";
        String id = getIntent().getStringExtra("user_id");
        url+= "?red_id="+id;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarr = new JSONArray(response);
                            for (int i=0; i< jsonarr.length();i++){
                                String title = ((JSONObject)jsonarr.get(i)).getString("title");
                                String uri = ((JSONObject)jsonarr.get(i)).getString("file_uri");
                                int num = ((JSONObject)jsonarr.get(i)).getInt("album_num");
                                data.add(new GridVO(title, Uri.parse(uri), num));
                            }
                            adapter.notifyDataSetChanged();
                        }catch(JSONException e){
                            e.printStackTrace();
                        }


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
//        tv1 = findViewById(R.id.tv1);
//        imageView1 = findViewById(R.id.imageView1);
//        imageView2 = findViewById(R.id.imageView2);

//       cv = findViewById(R.id.cv);
//        scv = findViewById(R.id.scv);
//        gridlayout = findViewById(R.id.gridlayout);
        btn1 = findViewById(R.id.btn2);

//        imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BottomSheetDialog bottomSheet = new BottomSheetDialog();
//                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
//            }
//        });


//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, REQUEST_IMAGE_CODE);
//            }
//        });


    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri selectedImageUri = data.getData();
//            imageView2.setImageURI(selectedImageUri);
//
//        }
//
//    }


}