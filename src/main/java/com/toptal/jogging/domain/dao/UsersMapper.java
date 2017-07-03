package com.toptal.jogging.domain.dao;

import com.toptal.jogging.domain.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Artem on 03.07.2017.
 */
public class UsersMapper implements ResultSetMapper<User> {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";

    public User map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {
        return new User(resultSet.getInt(ID), resultSet.getString(NAME), resultSet.getString(PASSWORD), User.Role.valueOf(resultSet.getString(ROLE)));
    }
}
