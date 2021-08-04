package com.example.testthread.shadow;

import com.example.testthread.ThreadCanary;
import com.example.testthread.model.ThreadTrace;

import java.lang.reflect.Field;
import java.util.Timer;


public class ShadowTimer extends Timer {
  public ShadowTimer() {
    super();
    addThreadTrace();
  }

  public ShadowTimer(boolean isDaemon) {
    super(isDaemon);
    addThreadTrace();
  }

  public ShadowTimer(String name) {
    super(name);
    addThreadTrace();
  }

  public ShadowTimer(String name, boolean isDaemon) {
    super(name, isDaemon);
    addThreadTrace();
  }

  private void addThreadTrace() {
    Thread thread = reflectionThread();
    if (thread != null) {
      ThreadCanary.addThreadTrace(ThreadTrace.newInstance(thread, new Throwable().getStackTrace()));
    }
  }

  private Thread reflectionThread() {
    try {
      Field threadField = Timer.class.getDeclaredField("thread");
      threadField.setAccessible(true);
      return (Thread) threadField.get(this);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void cancel() {
    super.cancel();
    Thread thread = reflectionThread();
    if (thread != null) {
      ThreadCanary.removeThreadTrace(thread.getId());
    }
  }
}
