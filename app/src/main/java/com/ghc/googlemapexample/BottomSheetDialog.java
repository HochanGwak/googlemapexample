package com.ghc.googlemapexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    // private BottomSheetListener mListener;
    Button gallery;
    ImageView imageView3;
    EditText edt_title;
    Uri selectedImageUri;
    RequestQueue requestQueue;
    Button btn_ok;
    private static final int REQUEST_IMAGE_CODE = 1001;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheet, container, false);

        Log.v("asdf", "slide");
        gallery = v.findViewById(R.id.btn2);
        imageView3 = v.findViewById(R.id.imageView3);
        imageView3.setVisibility(View.GONE);
        edt_title = v.findViewById(R.id.edt_title);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, REQUEST_IMAGE_CODE);

            }
        });
        btn_ok = v.findViewById(R.id.btn_ok);


        requestQueue = Volley.newRequestQueue(getContext());

        btn_ok.setOnClickListener(new View.OnClickListener() { // 완료버튼 눌렀을 때!
            @Override
            public void onClick(View v) {

                BasicActivity.data.add(new GridVO(edt_title.getText().toString(), selectedImageUri, -1)); // BasicActivity에 있는 data(ArrayList)에 사진이랑 글자 추가!

                String edt = edt_title.getText().toString();


                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                String url = "http://172.30.1.34:3003/insertalbum";
                url += "?title="+edt;
                url += "&photo_add="+BitmapToString(bitmap);
                url += "&user_id"+"test1";


                StringRequest request = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("asdf", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams(){
                        Log.v("asdf", "getParams");
//                        Map<String, String> params = new HashMap<String,String>();
//
//                        params.put("title", edt);
//                        params.put("photo_add", "dd");
//                        params.put("user_id", "test1");

                        return null;
                    }

                };



                request.setTag("MAIN");
                requestQueue.add(request);


                BasicActivity.adapter.notifyDataSetChanged(); // BasicActivity에 있는 adapter 새로고침
                dismiss(); // Fragment 지우는 명령어! => finish랑 똑같음!

            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

            selectedImageUri = data.getData();
            imageView3.setImageURI(selectedImageUri);
            imageView3.setVisibility(View.VISIBLE);

        }

    }

    public static String BitmapToString(Bitmap bitmap) {
        if (bitmap == null) {
            return "디폴트 이미지";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 1, baos);
        byte[] bytes = baos.toByteArray();
        String bitString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return bitString;
    }


//    public interface BottomSheetListener {
//        void onButtonClicked(String text);
//    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            mListener = (BottomSheetListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement BottomSheetListener");
//        }
//    }
}