
package com.example.testwork.morewindow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.testwork.R;

/**
 * An {@link Activity} that shows an editable text and gives suggestions to the user.
 */
public class SuggestActivity extends Activity {


    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_activity);
        AutoCompleteTextView view = findViewById(R.id.auto_complete_text_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        view.setAdapter(adapter);
//        setUpAutoCompleteTextView();
    }

    /**
     * Creates an adapter and sets it to an {@link AutoCompleteTextView} to enable suggestions.
     */
    private void setUpAutoCompleteTextView() {
        String[] completions = getResources().getStringArray(R.array.bodies_of_water);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                completions);

        AutoCompleteTextView autoComplete =
                (AutoCompleteTextView) findViewById(R.id.auto_complete_text_view);
        autoComplete.setAdapter(adapter);
    }
}
