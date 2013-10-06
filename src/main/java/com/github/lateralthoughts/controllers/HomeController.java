package com.github.lateralthoughts.controllers;

import static java.lang.String.format;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.lateralthoughts.domain.Beer;

@Controller
public class HomeController {

    private final DataSource dataSource;

    @Autowired
    public HomeController(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @RequestMapping(value = "/beers", method = GET)
    @ResponseBody
    public String welcome() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Beer beer : findAllBeers()) {
            stringBuilder.append(
                format("[%s] %s<br />\n", beer.getBrand(), beer.getDescription())
            );
        }
        return stringBuilder.toString();
    }

    private List<Beer> findAllBeers() {
        return new JdbcTemplate(dataSource).query(
            "SELECT brand, description FROM Beers",
            new BeanPropertyRowMapper<Beer>(Beer.class)
        );
    }
}
