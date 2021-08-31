package com.example.testthread.shadow;

import com.example.testthread.ThreadCanary;
import com.example.testthread.model.ThreadTrace;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


class ProxyThreadFactory implements ThreadFactory {

  private final ThreadFactory originalThreadFactory;
  private String threadPoolName;

  ProxyThreadFactory() {
    this(Executors.defaultThreadFactory());
  }

  ProxyThreadFactory(ThreadFactory originalThreadFactory) {
    this.originalThreadFactory = originalThreadFactory;
  }

  public void attachThreadPool(String threadPoolName) {
    this.threadPoolName = threadPoolName;
  }

  public Thread newThread(Runnable r) {
    Thread t = originalThreadFactory.newThread(r);
    if (threadPoolName != null) {
      ThreadCanary.addThreadTrace(threadPoolName, ThreadTrace.newInstance(
          t, new Throwable().getStackTrace(), threadPoolName));
    }
    return t;
  }
}