package com.example.testthread.shadow;

import com.example.testthread.ThreadCanary;
import com.example.testthread.model.ThreadPoolTrace;

import java.util.List;
import java.util.concurrent.ForkJoinPool;


public class ShadowForkJoinPool extends ForkJoinPool {

  public ShadowForkJoinPool() {
    this(Math.min(0x7fff, Runtime.getRuntime().availableProcessors()), defaultForkJoinWorkerThreadFactory, null, false);
  }

  public ShadowForkJoinPool(int parallelism) {
    this(parallelism, defaultForkJoinWorkerThreadFactory, null, false);
  }

  public ShadowForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, Thread.UncaughtExceptionHandler handler, boolean asyncMode) {
    super(parallelism, new ProxyForkJoinWorkerThreadFactory(factory), handler, asyncMode);
    addThreadPoolTrace();
  }

  @Override
  public void shutdown() {
    super.shutdown();
    removeThreadPoolTrace();
  }

  @Override
  public List<Runnable> shutdownNow() {
    List<Runnable> result = super.shutdownNow();
    removeThreadPoolTrace();
    return result;
  }

  private void addThreadPoolTrace() {
    ((ProxyForkJoinWorkerThreadFactory) getFactory()).attachThreadPool(getThreadPoolName());
    ThreadCanary.addThreadPoolTrace(ThreadPoolTrace.newInstance(getThreadPoolName(), new Throwable().getStackTrace()));
  }

  private void removeThreadPoolTrace() {
    ThreadCanary.removeThreadPoolTrace(getThreadPoolName());
  }

  private String getThreadPoolName() {
    return ForkJoinPool.class.getName() + "@" + Integer.toHexString(hashCode());
  }
}
