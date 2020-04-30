package ru.demkin.esb.configserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.demkin.esb.configserver.repository.ConfigRepositoty;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigService {
  private List<String> cache = new ArrayList<>();

  @Autowired
  private ConfigRepositoty repositoty;

  public void save(String value) {
    cache.add(value);
  }

  public List<String> load() {
    return cache;
  }

}
