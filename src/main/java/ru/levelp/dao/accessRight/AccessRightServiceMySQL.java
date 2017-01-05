package ru.levelp.dao.accessRight;

import ru.levelp.dao.BaseServiceMySQL;
import ru.levelp.entities.AccessRight;

/**
 * Created by кайрат on 28.12.2016.
 */
public class AccessRightServiceMySQL extends BaseServiceMySQL<AccessRight,String> {

    public AccessRightServiceMySQL(Class<AccessRight> entityType) {
        super(entityType);
    }

    @Override
    public void delete(String note) {

    }
}
