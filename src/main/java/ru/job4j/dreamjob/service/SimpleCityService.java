package ru.job4j.dreamjob.service;

import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.repository.CityRepository;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;

@ThreadSafe
@Service
public class SimpleCityService implements CityService {
    private final CityRepository cityRepository;

    public SimpleCityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public Collection<City> findAll() {
        return cityRepository.findAll();
    }
}
