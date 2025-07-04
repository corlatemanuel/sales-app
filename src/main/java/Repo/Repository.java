package Repo;

import Entity.*;
import java.util.List;
import java.util.Optional;

public interface Repository<T extends Entity> {
    void add(T entity) throws Exception;
    Optional<T> findById(int id);
    List<T> findAll();
    void update(T entity) throws Exception;
    void delete(int id) throws Exception;
}