package com.example.testthread.shadow;

import com.example.testthread.ThreadCanary;
import com.example.testthread.model.ThreadTrace;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;


class ProxyForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {

  private final ForkJoinPool.ForkJoinWorkerThreadFactory originalThreadFactory;
  private String threadPoolName;

  ProxyForkJoinWorkerThreadFactory() {
    this(ForkJoinPool.defaultForkJoinWorkerThreadFactory);
  }

  ProxyForkJoinWorkerThreadFactory(ForkJoinPool.ForkJoinWorkerThreadFactory originalThreadFactory) {
    this.originalThreadFactory = originalThreadFactory;
  }

  public void attachThreadPool(String threadPoolName) {
    this.threadPoolName = threadPoolName;
  }

  @Override
  public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
    ForkJoinWorkerThread t = originalThreadFactory.newThread(pool);
    if (threadPoolName != null) {
      ThreadCanary.addThreadTrace(threadPoolName, ThreadTrace.newInstance(
          t, new Throwable().getStackTrace(), threadPoolName));
    }
    return t;
  }
}
