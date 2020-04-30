package ru.demkin.esb.configserver.controllers;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.demkin.esb.configserver.services.ConfigService;

import java.util.List;

@RestController
public class ConfigController {

    @Autowired
    private ConfigService service;

    @Operation(summary = "Записать тестовое сообщение")
    @PostMapping("/save")
    public void save(@RequestBody String value) {
        service.save(value);
    }

    @Operation(summary = "Прочитать все сообщения")
    @GetMapping("/load")
    public List<String> load() {
        return service.load();
    }
}
