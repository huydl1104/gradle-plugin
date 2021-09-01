package com.example.testwork.basicSimiple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.testwork.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author yudongliang
 * create time 2021-09-01
 * describe : BasicSimple
 */
public class BasicSimpleActivity extends AppCompatActivity implements View.OnClickListener {
    // The TextView used to display the message inside the Activity.
    private TextView mTextView;

    // The EditText where the user types the message.
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_simple);

        // Set the listeners for the buttons.
        findViewById(R.id.changeTextBt).setOnClickListener(this);
        findViewById(R.id.activityChangeTextBtn).setOnClickListener(this);

        mTextView = (TextView) findViewById(R.id.textToBeChanged);
        mEditText = (EditText) findViewById(R.id.editTextUserInput);
    }

    @Override
    public void onClick(View view) {
        // Get the text from the EditText view.
        final String text = mEditText.getText().toString();

        final int changeTextBtId = R.id.changeTextBt;
        final int activityChangeTextBtnId = R.id.activityChangeTextBtn;

        if (view.getId() == changeTextBtId) {
            // First button's interaction: set a text in a text view.
            mTextView.setText(text);
        } else if (view.getId() == activityChangeTextBtnId) {
            // Second button's interaction: start an activity and send a message to it.
            Intent intent = ShowTextActivity.newStartIntent(this, text);
            startActivity(intent);
        }
    }

}
