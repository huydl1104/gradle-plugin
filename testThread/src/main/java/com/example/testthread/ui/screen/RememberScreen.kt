package com.example.testthread.ui.screen

import android.view.View
import android.view.ViewGroup
import com.example.testthread.ui.navigation.Screen


internal abstract class RememberScreen : Screen() {

  @Transient
  protected lateinit var contentView: View

  override fun createView(container: ViewGroup): View {
    if (!this::contentView.isInitialized) {
      contentView = createContentView(container)
    }
    onScreenShowing(contentView)
    return contentView
  }

  abstract fun createContentView(container: ViewGroup): View

  abstract fun onScreenShowing(contentView: View)

}