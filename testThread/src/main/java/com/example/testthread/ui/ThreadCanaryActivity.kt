package com.example.testthread.ui

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.example.testthread.ui.navigation.NavigatingActivity
import com.example.testthread.ui.navigation.Screen
import com.example.testthread.ui.screen.SettingScreen
import com.example.testthread.ui.screen.ThreadsScreen
import com.example.testthread.R



internal class ThreadCanaryActivity : NavigatingActivity() {

  private val bottomNavigationBar by lazy {
    findViewById<View>(R.id.thread_canary_bottom_navigation_bar)
  }

  private val threadsButton by lazy {
    findViewById<View>(R.id.thread_canary_navigation_button_threads)
  }

  private val threadsIcon by lazy {
    findViewById<View>(R.id.thread_canary_navigation_button_threads_icon)
  }

  private val settingButton by lazy {
    findViewById<View>(R.id.thread_canary_navigation_button_setting)
  }

  private val settingIcon by lazy {
    findViewById<View>(R.id.thread_canary_navigation_button_setting_icon)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.thread_canary_activity_main)

    installNavigation(savedInstanceState, findViewById(R.id.thread_canary_main_container))

    threadsButton.setOnClickListener { resetTo(ThreadsScreen()) }
    settingButton.setOnClickListener { resetTo(SettingScreen()) }
  }

  override fun getLauncherScreen(): Screen {
    return ThreadsScreen()
  }

  override fun onNewScreen(screen: Screen) {
    when (screen) {
      is ThreadsScreen -> {
        bottomNavigationBar.visibility = View.VISIBLE
        threadsButton.isSelected = true
        threadsIcon.alpha = 1.0f

        settingButton.isSelected = false
        settingIcon.alpha = 0.4f
      }
      is SettingScreen -> {
        bottomNavigationBar.visibility = View.VISIBLE
        threadsButton.isSelected = false
        threadsIcon.alpha = 0.4f

        settingButton.isSelected = true
        settingIcon.alpha = 1.0f
      }
      else -> {
        bottomNavigationBar.visibility = View.GONE
      }
    }
  }

  override fun setTheme(resid: Int) {
    // We don't want this to be called with an incompatible theme.
    // This could happen if you implement runtime switching of themes
    // using ActivityLifecycleCallbacks.
    if (resid != R.style.thread_canary_Theme_Base) {
      return
    }
    super.setTheme(resid)
  }

  companion object {
    fun createPendingIntent(context: Context, screens: ArrayList<Screen>? = null): PendingIntent {
      val intent = Intent(context, ThreadCanaryActivity::class.java)
      screens?.let { intent.putExtra("screens", screens) }
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
      val flags = if (Build.VERSION.SDK_INT >= 23) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      } else {
        PendingIntent.FLAG_UPDATE_CURRENT
      }
      return PendingIntent.getActivity(context, 1, intent, flags)
    }

    fun createIntent(context: Context): Intent {
      val intent = Intent(context, ThreadCanaryActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
      return intent
    }
  }
}