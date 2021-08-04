package com.example.testthread.shadow;

import com.example.testthread.ThreadCanary;
import com.example.testthread.model.ThreadPoolTrace;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ShadowThreadPoolExecutor extends ThreadPoolExecutor {

  public ShadowThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory());
  }

  public ShadowThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ProxyThreadFactory(threadFactory));
    addThreadPoolTrace();
  }

  public ShadowThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
        Executors.defaultThreadFactory(), handler);
  }

  public ShadowThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ProxyThreadFactory(threadFactory), handler);
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
    return ThreadPoolExecutor.class.getName() + "@" + Integer.toHexString(hashCode());
  }
}
