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
    @SqlQuery("select * from runs order by id;")
    List<Run> getRuns();

    @SqlQuery("select * from runs order by id limit :skip, :perPage;")
    List<Run> getRuns(@Bind("skip") final int skip, @Bind("perPage") final int perPage);

    @SqlQuery("select * from runs where id = :id")
    Run getRun(@Bind("id") final long id);

    @SqlQuery("select * from runs where userid = :userid order by id")
    List<Run> getRuns(@Bind("userid") final long userid);

    @SqlQuery("select * from runs where userid = :userid order by id limit :skip, :perPage;")
    List<Run> getRuns(@Bind("userid") final long userid, @Bind("skip") final int skip, @Bind("perPage") final int perPage);

    @SqlUpdate("INSERT INTO runs(`userid`,`runtime`,`distance`, `rundate`, `lat`, `lon`, `weather`) " +
            "VALUES (:userId, :duration, :distance, :date, :latitude, :longitude, :weather)")
    void createRun(@BindBean final Run run);

    @SqlUpdate("UPDATE runs SET runtime = coalesce(:runtime, runtime), rundate = coalesce(:rundate, rundate)" +
            ", lat = coalesce(:lat, lat), lon = coalesce(:lon, lon), weather = coalesce(:weather, weather))" +
            "WHERE id = :id")
    void editRun(@BindBean final Run run);

//    @SqlUpdate("delete from users where id = :id")
//    int deleteUser(@Bind("id") final int id);

    @SqlQuery("select last_insert_id();")
    int lastInsertId();
}