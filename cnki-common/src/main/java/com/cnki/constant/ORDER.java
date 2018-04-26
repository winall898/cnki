package com.cnki.constant;

public enum ORDER {
  desc, asc;
  public ORDER reverse() {
    return (this == asc) ? desc : asc;
  }
}
