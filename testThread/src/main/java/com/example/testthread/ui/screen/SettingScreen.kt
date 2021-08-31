package com.example.testthread.ui.screen

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Switch
import com.example.testthread.ThreadCanary
import com.example.testthread.ui.navigation.Screen
import com.example.testthread.ui.navigation.activity
import com.example.testthread.ui.navigation.inflate
import com.example.testthread.R


internal class SettingScreen : Screen() {
  override fun createView(container: ViewGroup) =
    container.inflate(R.layout.thread_canary_setting).apply {
      activity.title = resources.getString(R.string.thread_canary_tab_settings)

      val notificationEnable = findViewById<Switch>(R.id.thread_canary_notification)
      notificationEnable.isChecked = ThreadCanary.enableWarningNotification
      notificationEnable.setOnCheckedChangeListener { _, isChecked ->
        ThreadCanary.enableWarningNotification = isChecked
      }

      val warningThreadsSpinner = findViewById<Spinner>(R.id.thread_canary_warning_threads)
      val warningThreads = resources.getStringArray(R.array.thread_canary_warning_threads)
      warningThreadsSpinner.setSelection(warningThreads.indexOf(
        ThreadCanary.warningNotificationThreads.toString()))
      warningThreadsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
          ThreadCanary.warningNotificationThreads = warningThreads[position].toInt()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
      }
    }
}