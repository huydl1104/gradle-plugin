package com.example.testthread.shadow;

import com.example.testthread.ThreadCanary;
import com.example.testthread.model.ThreadPoolTrace;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;


public class ShadowScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {
  public ShadowScheduledThreadPoolExecutor(int corePoolSize) {
    this(corePoolSize, Executors.defaultThreadFactory());
  }

  public ShadowScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory) {
    super(corePoolSize, new ProxyThreadFactory(threadFactory));
    addThreadPoolTrace();
  }

  public ShadowScheduledThreadPoolExecutor(int corePoolSize, RejectedExecutionHandler handler) {
    this(corePoolSize, Executors.defaultThreadFactory(), handler);
  }

  public ShadowScheduledThreadPoolExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
    super(corePoolSize, new ProxyThreadFactory(threadFactory), handler);
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
    ((ProxyThreadFactory) getThreadFactory()).attachThreadPool(getThreadPoolName());
    ThreadCanary.addThreadPoolTrace(ThreadPoolTrace.newInstance(getThreadPoolName(), new Throwable().getStackTrace()));
  }

  private void removeThreadPoolTrace() {
    ThreadCanary.removeThreadPoolTrace(getThreadPoolName());
  }

  private String getThreadPoolName() {
    return ScheduledThreadPoolExecutor.class.getName() + "@" + Integer.toHexString(hashCode());
  }
}
