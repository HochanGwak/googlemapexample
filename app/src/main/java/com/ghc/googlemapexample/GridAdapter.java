package com.ghc.googlemapexample;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    private Context c;
    private int layout;
    private ArrayList<GridVO> data;
    private LayoutInflater inflater;
    private FragmentManager fragmentManager;

    public GridAdapter(Context c, int layout, ArrayList<GridVO> data, FragmentManager supportFragmentManager) {
        this.c = c;
        this.layout = layout;
        this.data = data;
        this.fragmentManager = supportFragmentManager; // 아래에서 올라오는 Dialog 호출하려고 Activity에서 가져옴
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(layout, viewGroup, false);
        }

        ImageView img = view.findViewById(R.id.gridimage); // 사진 보여지게 하는 부분
        img.setImageURI(data.get(i).getImageUri()); // i번째 이미지 보여지게 함!

        TextView tv_title = view.findViewById(R.id.tv_title); // 글자 보여지게 하는 부분!
        tv_title.setText(data.get(i).getTitle()); // i번째 글자 띄워줌

        if (i == 0) { // 첫번째 이미지라면 ( + 기능이 있는 이미지)
            tv_title.setVisibility(View.GONE); // 텍스트 가림
        } else {
            tv_title.setVisibility(View.VISIBLE);
        }

        img.setOnClickListener(new View.OnClickListener() { // 이미지 클릭했을 때!
            @Override
            public void onClick(View view) {
                if (i == 0) { // 첫번째 이미지면! + 이미지면
                    BottomSheetDialog bottomSheet = new BottomSheetDialog();
                    bottomSheet.show(fragmentManager, "exampleBottomSheet");
//                    bottomSheet.getDialog().setOnDismissListener( // 사라짐 감지
//
//                            new DialogInterface.OnDismissListener() {
//
//                                @Override
//
//                                public void onDismiss(DialogInterface dialogInterface) {
//
//
//                                    notifyDataSetChanged();
//                                }
//                            });

                } else {
                    Intent intent = new Intent(c, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 Task에서 Intent 실행시키겠다!
                    // 요기에서 흐음.. 흠..
                    c.startActivity(intent);
                }
            }
        });

        return view;
    }
}
