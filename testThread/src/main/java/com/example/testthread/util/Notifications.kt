/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.testthread.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import com.example.testthread.R

/**
 * @author airsaid
 */
internal object Notifications {

  private const val CHANNEL_ID = "ThreadCanary"

  fun showNotification(
    context: Context, title: CharSequence, content: CharSequence,
    pendingIntent: PendingIntent?, notificationId: Int
  ) {
    createNotificationChannel(context)

    val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.drawable.thread_canary_notification)
      .setContentTitle(title)
      .setContentText(content)
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)
      .setPriority(NotificationCompat.DEFAULT_ALL)

    val notificationManager =
      context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, notificationBuilder.build())
  }

  private fun createNotificationChannel(context: Context) {
    if (SDK_INT >= O) {
      val name = context.getString(R.string.thread_canary_notification_channel_name)
      val descriptionText = context.getString(R.string.thread_canary_notification_channel_desc)
      val importance = NotificationManager.IMPORTANCE_DEFAULT
      val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
        description = descriptionText
      }
      val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

}
