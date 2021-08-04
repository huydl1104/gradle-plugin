package com.example.testthread.shadow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testthread.ThreadCanary;
import com.example.testthread.model.ThreadTrace;


public class ShadowThread extends Thread {

  public ShadowThread() {
  }

  public ShadowThread(@Nullable Runnable target) {
    super(target);
  }

  public ShadowThread(@Nullable ThreadGroup group, @Nullable Runnable target) {
    super(group, target);
  }

  public ShadowThread(@NonNull String name) {
    super(name);
  }

  public ShadowThread(@Nullable ThreadGroup group, @NonNull String name) {
    super(group, name);
  }

  public ShadowThread(@Nullable Runnable target, @NonNull String name) {
    super(target, name);
  }

  public ShadowThread(@Nullable ThreadGroup group, @Nullable Runnable target, @NonNull String name) {
    super(group, target, name);
  }

  public ShadowThread(@Nullable ThreadGroup group, @Nullable Runnable target, @NonNull String name, long stackSize) {
    super(group, target, name, stackSize);
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