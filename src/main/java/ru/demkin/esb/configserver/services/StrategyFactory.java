package ru.demkin.esb.configserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategyFactory {
  private static final Logger log = LoggerFactory.getLogger(StrategyFactory.class);

  @Autowired
  private ConfigurationUpdateStrategy user;

  @Autowired
  private ConfigurationUpdateStrategy admin;

  @Autowired
  private ConfigurationUpdateStrategy base;

  public ConfigurationUpdateStrategy of(String username) {
    return username == null ? admin : user;
  }

  public ConfigurationUpdateStrategy admin() {
    return admin;
  }

  public ConfigurationUpdateStrategy base() {
    return base;
  }
}
