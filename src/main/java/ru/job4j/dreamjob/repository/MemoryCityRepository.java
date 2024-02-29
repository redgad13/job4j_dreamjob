package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.City;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
@Repository
public class MemoryCityRepository implements CityRepository {

    private final Map<Integer, City> cities = new ConcurrentHashMap<>() {
        {
            put(1, new City(1, "Москва"));
            put(2, new City(2, "Санкт-Петербург"));
            put(3, new City(3, "Екатеринбург"));
            put(4, new City(3, "Псков"));
            put(5, new City(3, "Порхов"));
            put(6, new City(3, "Астана"));
        }
    };

    @Override
    public Collection<City> findAll() {
        return cities.values();
    }

}