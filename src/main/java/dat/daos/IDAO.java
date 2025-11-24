package dat.daos;

import dat.entities.Plant;
import dat.exceptions.ApiException;

import java.util.List;

public interface IDAO<T, I> {

    T readById(I i) throws ApiException;
    List<T> readAll();
//    List<T> readByType(T s);
    T create(T t);
    T update(I i, T t) throws ApiException;
    void delete(I i) throws ApiException;

}
