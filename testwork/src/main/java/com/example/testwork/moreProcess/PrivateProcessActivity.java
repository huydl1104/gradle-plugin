package com.example.testwork.moreProcess;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testwork.R;

import static com.example.testwork.moreProcess.Util.setCurrentRunningProcess;


/**
 * Activity running in a private process which is manually defined in the AndroidManifest.xml using
 * the android:process attribute.
 * <p>
 * <p>From D.A.C: "If the name assigned to this attribute begins with a colon (':'), a new process,
 * private to the application, is created when it's needed and the activity runs in that process."
 */
public class PrivateProcessActivity extends Activity implements OnItemClickListener {

    private TextView privateProcessNameTextView;
    private TextView selectedListItemTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_process);
        privateProcessNameTextView = findViewById(R.id.textPrivateProcessName);
        setCurrentRunningProcess(privateProcessNameTextView, this);

        selectedListItemTextView = findViewById(R.id.selectedListItemText);
        ListView listView = findViewById(R.id.list);
        String[] listItems = getResources().getStringArray(R.array.list_items);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    public void onBtnClick(View view) {
        TextView v = findViewById(R.id.displayTextView);
        v.setText(R.string.button_clicked);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedListItemTextView.setText(
                String.format(getString(R.string.list_selection), ((TextView) view).getText()));
    }
}
