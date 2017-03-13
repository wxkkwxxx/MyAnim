package com.wxk.leads.anim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

public class SignUpActivity extends AppCompatActivity {

    private FrameLayout layout_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);
        layout_sign_up = (FrameLayout) findViewById(R.id.layout_sign_up);
    }

    public void singUp(View view){

        //获取view在屏幕中的位置以及宽高做为参数传到第二个activity
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("left", location[0]);
        intent.putExtra("top", location[1]);
        intent.putExtra("width", view.getWidth());
        intent.putExtra("height", view.getHeight());
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
