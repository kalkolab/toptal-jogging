package com.toptal.jogging.domain.dao;

import com.toptal.jogging.domain.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@RegisterMapper(UsersMapper.class)
@UseStringTemplate3StatementLocator
public interface UsersDao {
    @SqlQuery("select * from users where <clause> order by id;")
    List<User> getUsers(@Define("clause") String clause);

    @SqlQuery("select * from users where <clause> order by id limit :skip, :perPage;")
    List<User> getUsers(@Bind("skip") final int skip, @Bind("perPage") final int perPage, @Define("clause") String clause);

    @SqlQuery("select * from users where id = :id")
    User getUser(@Bind("id") final long id);

    @SqlQuery("select * from users where name = :name")
    User getUser(@Bind("name") final String name);

    @SqlUpdate("insert into users(name, password, role) values(:name, :password, :role)")
    void createUser(@BindBean final User user);

    @SqlUpdate("update users set name = coalesce(:name, name), password = coalesce(:password, password), role = coalesce(:role, role) where id = :id")
    void editUser(@BindBean final User user);

    @SqlUpdate("delete from users where id = :id")
    int deleteUser(@Bind("id") final int id);

    @SqlQuery("select last_insert_id();")
    int lastInsertId();
}