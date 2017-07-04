package com.toptal.jogging.domain.dao;

import com.toptal.jogging.domain.Run;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(RunsMapper.class)
public interface RunsDao {
    @SqlQuery("select * from runs;")
    List<Run> getRuns();

    @SqlQuery("select * from runs where id = :id")
    Run getRun(@Bind("id") final long id);

    @SqlQuery("select * from runs where userid = :userid")
    List<Run> getRuns(@Bind("userid") final long userid);

    @SqlUpdate("INSERT INTO runs(`userid`,`runtime`,`distance`, `rundate`, `lat`, `lon`, `weather`) " +
            "VALUES (:userId, :duration, :distance, :date, :latitude, :longitude, :weather)")
    void createRun(@BindBean final Run run);

//    @SqlUpdate("update users set name = coalesce(:name, name), password = coalesce(:password, password), role = coalesce(:role, role) where id = :id")
//    void editUser(@BindBean final User user);
//
//    @SqlUpdate("delete from users where id = :id")
//    int deleteUser(@Bind("id") final int id);

    @SqlQuery("select last_insert_id();")
    int lastInsertId();
}