package ru.demkin.esb.configserver;

public class Utils {

  public static String user(String username) {
    return username == null ? "ADMIN" : "USER = " + username;
  }



}
