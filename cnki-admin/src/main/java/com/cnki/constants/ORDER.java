package com.cnki.constants;

public enum ORDER {
  desc, asc;
  public ORDER reverse() {
    return (this == asc) ? desc : asc;
  }
}
