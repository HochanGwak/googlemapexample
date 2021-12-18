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

public class LoginActivity extends AppCompatActivity {

    EditText login_mail, login_pw;
    Button btn_login, btn_reg;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_mail = findViewById(R.id.login_mail);
        login_pw = findViewById(R.id.login_pw);
        btn_login = findViewById(R.id.btn_login);
        btn_reg = findViewById(R.id.btn_reg);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //회원가입 페이지로 이동
                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent);


            }
        });



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = login_mail.getText().toString();
                String pw = login_pw.getText().toString();

                String url = "http://172.30.1.34:3002/oneSelect?id="+id+"&pw="+pw;
                Log.d("Test", "onClick: Test");

                if(requestQueue == null){
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                }

                StringRequest request = new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray arr = new JSONArray(response);
                                    // json배열만들어주기
                                    Log.d("Test22", "onClick: Test11" + arr.toString());
                                    String user_id="";
                                    String user_pw="";
                                    for (int i = 0; i < arr.length(); i++){
                                        JSONObject jObject = (JSONObject) arr.get(i);



                                        user_id = jObject.getString("id");
                                        user_pw = jObject.getString("pw");
                                        //String user_name = jObject.getString("user_name");

                                        Log.d("zzzz", "onClick: zzz : " + user_id);
                                        Log.d("zzzz", "onClick: zzz : " + user_pw);



                                        //}

                                    }
                                    if(user_id.equals(id)&&user_pw.equals(pw)){
                                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(LoginActivity.this, BasicActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        startActivity(intent);
                                    }else{

                                        Toast.makeText(getApplicationContext(), "id,pw를 다시 확인해주세요", Toast.LENGTH_SHORT).show();

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