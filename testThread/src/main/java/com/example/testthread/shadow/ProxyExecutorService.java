package com.example.testthread.shadow;

import com.example.testthread.ThreadCanary;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ProxyExecutorService implements ExecutorService {

  private final ExecutorService originalExecutorService;

  private String threadPoolName;

  public ProxyExecutorService(ExecutorService executorService) {
    this.originalExecutorService = executorService;
  }

  public void attachThreadPool(String threadPoolName) {
    this.threadPoolName = threadPoolName;
  }

  @Override
  public void shutdown() {
    originalExecutorService.shutdown();
    if (threadPoolName != null) {
      ThreadCanary.removeThreadPoolTrace(threadPoolName);
    }
  }

  @Override
  public List<Runnable> shutdownNow() {
    List<Runnable> result = originalExecutorService.shutdownNow();
    if (threadPoolName != null) {
      ThreadCanary.removeThreadPoolTrace(threadPoolName);
    }
    return result;
  }

  @Override
  public boolean isShutdown() {
    return originalExecutorService.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return originalExecutorService.isTerminated();
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    return originalExecutorService.awaitTermination(timeout, unit);
  }

  @Override
  public <T> Future<T> submit(Callable<T> task) {
    return originalExecutorService.submit(task);
  }

  @Override
  public <T> Future<T> submit(Runnable task, T result) {
    return originalExecutorService.submit(task, result);
  }

  @Override
  public Future<?> submit(Runnable task) {
    return originalExecutorService.submit(task);
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
    return originalExecutorService.invokeAll(tasks);
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
    return originalExecutorService.invokeAll(tasks, timeout, unit);
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws ExecutionException, InterruptedException {
    return originalExecutorService.invokeAny(tasks);
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
    return originalExecutorService.invokeAny(tasks, timeout, unit);
  }

  @Override
  public void execute(Runnable command) {
    originalExecutorService.execute(command);
  }
}
