package com.ghc.googlemapexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    EditText reg_name, reg_mail, reg_pw, repw;
    Button btn_join, btn_check;
    RequestQueue requestQueue;
    boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_join = findViewById(R.id.btn_join);
        btn_check = findViewById(R.id.btn_check);
        reg_name = findViewById(R.id.reg_name);
        reg_mail = findViewById(R.id.reg_mail);
        reg_pw = findViewById(R.id.reg_pw);
        repw = findViewById(R.id.repw);


        check = false;


        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_name = reg_name.getText().toString();
                String id = reg_mail.getText().toString();
                String pw = reg_pw.getText().toString();
                String pwck = repw.getText().toString();

                if(reg_mail.equals("")|reg_name.equals("")|reg_pw.equals("")|repw.equals("")){
                    Toast.makeText(getApplicationContext(), "빈칸이 없나 확인해주세요", Toast.LENGTH_SHORT).show();

                }else{
                    if(check){
                        if(pw.equals(pwck)){
                            String url = "http://172.30.1.34:3002/insert?id="+id+"&user_name="+user_name+"&pw="+pw;
                            Log.d("Test", "onClick: Test");


                            if (requestQueue == null) {
                                requestQueue = Volley.newRequestQueue(getApplicationContext());
                            }


                            StringRequest request = new StringRequest(
                                    Request.Method.GET,
                                    url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.d("Test", "onClick: Test" + response);
                                            //tv_confirm.setText(response);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Test", "onClick: Test" + error);
                                }

                            });

                            requestQueue.add(request);



                            //로그인 화면으로 이동
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "password를 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "id중복체크를 해주세요", Toast.LENGTH_SHORT).show();
                    }
                }







            }

        });

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = reg_mail.getText().toString();

                String url = "http://172.30.1.34:3002/idcheck?id="+id;
                Log.d("Test_id", "onClick: Test");


                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                }


                StringRequest request = new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("Test22", "aaaaa");

                                try {
                                    JSONArray arr = new JSONArray(response);
                                    // json배열만들어주기
                                    Log.d("Test22", "onClick: Test11" + arr.toString());

                                    String user_id="";
                                    for (int i = 0; i < arr.length(); i++){
                                        JSONObject jObject = (JSONObject) arr.get(i);

                                        user_id = jObject.getString("id");
                                        String user_pw = jObject.getString("pw");
                                        //String user_name = jObject.getString("user_name");

                                        Log.d("zzzz", "onClick: zzz : " + user_id);
                                        Log.d("zzzz", "onClick: zzz : " + user_pw);


                                        //}

                                    }
                                    if(user_id.equals(id)){
                                        Toast.makeText(getApplicationContext(), "id중복", Toast.LENGTH_SHORT).show();

                                    }else {
                                        check = true;
                                        Toast.makeText(getApplicationContext(), "사용가능id", Toast.LENGTH_SHORT).show();

                                    }

                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //tv_confirm.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Test", "onClick: Test" + error);
                    }

                });

                requestQueue.add(request);
            }
        });
    }

}