package ru.levelp.dao;

import ru.levelp.entities.BaseEntity;

public interface BaseDAO<E extends BaseEntity,ID> {

    void add(E note);

    void delete(ID note);

    E get(ID id);

    void update(E note);

}
