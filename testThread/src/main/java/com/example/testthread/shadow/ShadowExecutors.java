package com.example.testthread.shadow;

import com.example.testthread.ThreadCanary;
import com.example.testthread.model.ThreadPoolTrace;

import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ShadowExecutors {

  public static ExecutorService newFixedThreadPool(int nThreads) {
    return new ShadowThreadPoolExecutor(nThreads, nThreads,
        0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>());
  }

  public static ExecutorService newWorkStealingPool(int parallelism) {
    return new ShadowForkJoinPool
        (parallelism,
            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            null, true);
  }

  public static ExecutorService newWorkStealingPool() {
    return new ShadowForkJoinPool
        (Runtime.getRuntime().availableProcessors(),
            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
            null, true);
  }

  public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
    return new ShadowThreadPoolExecutor(nThreads, nThreads,
        0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(),
        threadFactory);
  }

  public static ExecutorService newSingleThreadExecutor() {
    return newSingleThreadExecutor(Executors.defaultThreadFactory());
  }

  public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
    ProxyThreadFactory proxyThreadFactory = new ProxyThreadFactory(threadFactory);
    ExecutorService executorService = Executors.newSingleThreadExecutor(proxyThreadFactory);
    ProxyExecutorService proxyExecutorService = new ProxyExecutorService(executorService);
    String threadPoolName = ThreadPoolExecutor.class.getName() + "@" + Integer.toHexString(executorService.hashCode());
    proxyExecutorService.attachThreadPool(threadPoolName);
    proxyThreadFactory.attachThreadPool(threadPoolName);
    ThreadCanary.addThreadPoolTrace(ThreadPoolTrace.newInstance(threadPoolName, new Throwable().getStackTrace()));
    return proxyExecutorService;
  }

  public static ExecutorService newCachedThreadPool() {
    return new ShadowThreadPoolExecutor(0, Integer.MAX_VALUE,
        60L, TimeUnit.SECONDS,
        new SynchronousQueue<>());
  }

  public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
    return new ShadowThreadPoolExecutor(0, Integer.MAX_VALUE,
        60L, TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        threadFactory);
  }

  public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
    return newSingleThreadScheduledExecutor(Executors.defaultThreadFactory());
  }

  public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
    ProxyThreadFactory proxyThreadFactory = new ProxyThreadFactory(threadFactory);
    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(proxyThreadFactory);
    ProxyScheduledExecutorService proxyScheduledExecutorService = new ProxyScheduledExecutorService(scheduledExecutorService);
    String threadPoolName = ScheduledThreadPoolExecutor.class.getName() + "@" + Integer.toHexString(scheduledExecutorService.hashCode());
    proxyScheduledExecutorService.attachThreadPool(threadPoolName);
    proxyThreadFactory.attachThreadPool(threadPoolName);
    ThreadCanary.addThreadPoolTrace(ThreadPoolTrace.newInstance(threadPoolName, new Throwable().getStackTrace()));
    return proxyScheduledExecutorService;
  }

  public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
    return new ShadowScheduledThreadPoolExecutor(corePoolSize);
  }

  public static ScheduledExecutorService newScheduledThreadPool(
      int corePoolSize, ThreadFactory threadFactory) {
    return new ShadowScheduledThreadPoolExecutor(corePoolSize, threadFactory);
  }

  public static ExecutorService unconfigurableExecutorService(ExecutorService executor) {
    return Executors.unconfigurableExecutorService(executor);
  }

  public static ScheduledExecutorService unconfigurableScheduledExecutorService(ScheduledExecutorService executor) {
    return Executors.unconfigurableScheduledExecutorService(executor);
  }

  public static ThreadFactory defaultThreadFactory() {
    return Executors.defaultThreadFactory();
  }

  public static ThreadFactory privilegedThreadFactory() {
    return Executors.privilegedThreadFactory();
  }

  public static <T> Callable<T> callable(Runnable task, T result) {
    return Executors.callable(task, result);
  }

  public static Callable<Object> callable(Runnable task) {
    return Executors.callable(task);
  }

  public static Callable<Object> callable(final PrivilegedAction<?> action) {
    return Executors.callable(action);
  }

  public static Callable<Object> callable(final PrivilegedExceptionAction<?> action) {
    return Executors.callable(action);
  }

  public static <T> Callable<T> privilegedCallable(Callable<T> callable) {
    return Executors.privilegedCallable(callable);
  }

  public static <T> Callable<T> privilegedCallableUsingCurrentClassLoader(Callable<T> callable) {
    return Executors.privilegedCallableUsingCurrentClassLoader(callable);
  }
}
