package ru.demkin.esb.configserver;

public class Utils {

  public static String user(String username) {
    return username == null ? "[type = ADMIN]" : "[type = USER] name = " + username;
  }

}
