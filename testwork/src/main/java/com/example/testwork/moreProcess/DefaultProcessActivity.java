package com.example.testwork.moreProcess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.testwork.R;

import static com.example.testwork.moreProcess.Util.setCurrentRunningProcess;


public class DefaultProcessActivity extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_process);
        textView = (TextView) findViewById(R.id.textNamedProcess);
        setCurrentRunningProcess(textView, this);
    }

    public void onStartActivityBtnClick(View view) {
        Intent intent = new Intent(this, PrivateProcessActivity.class);
        startActivity(intent);
    }
}
