package util;

public class MyPair<T1, T2> {

  private T1 tipo1;
  private T2 tipo2;

  public MyPair(T1 tipo1, T2 tipo2) {
    this.tipo1 = tipo1;
    this.tipo2 = tipo2;
  }

  public T1 getFrist() {
    return tipo1;
  }

  public void setFrist(T1 tipo1) {
    this.tipo1 = tipo1;
  }

  public T2 getSecond() {
    return tipo2;
  }

  public void setSecond(T2 tipo2) {
    this.tipo2 = tipo2;
  }
}
