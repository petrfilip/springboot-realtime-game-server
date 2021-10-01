package cz.petrfilip.server;

public class ObjectHolder<T> {

  private Object lock = new Object();

  private T value;


  public void set(T value) {
    synchronized(lock) {
      this.value = value;
      lock.notify();
    }
  }

  public T get() {
    synchronized (lock) {
      while (value == null) {
        try {
          lock.wait();
        } catch (InterruptedException e) {
          // Ignore the exception
        }
      }
      T result = value;
      value = null;
      return result;
    }
  }
}