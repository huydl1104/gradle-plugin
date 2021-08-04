package com.example.testthread.shadow;

import android.os.HandlerThread;

import com.example.testthread.ThreadCanary;
import com.example.testthread.model.ThreadTrace;


public class ShadowHandlerThread extends HandlerThread {
  public ShadowHandlerThread(String name) {
    super(name);
  }

  public ShadowHandlerThread(String name, int priority) {
    super(name, priority);
  }

  @Override
  public synchronized void start() {
    ThreadCanary.addThreadTrace(ThreadTrace.newInstance(this, new Throwable().getStackTrace()));
    super.start();
  }

  @Override
  public void run() {
    super.run();
    ThreadCanary.removeThreadTrace(getId());
  }
}
