package com.toptal.jogging.domain.dao;

import com.toptal.jogging.domain.Run;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Define;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;

import java.util.List;

@RegisterMapper(RunsMapper.class)
@UseStringTemplate3StatementLocator
public interface RunsDao {
    @SqlQuery("select * from runs order by date;")
    List<Run> getRuns();

    @SqlQuery("select * from runs order by date limit :skip, :perPage;")
    List<Run> getRuns(@Bind("skip") final int skip, @Bind("perPage") final int perPage);

    @SqlQuery("select * from runs where id = :id order by rundate")
    Run getRun(@Bind("id") final long id);

    @SqlQuery("select * from runs where userid = :userid and <clause> order by date ")
    List<Run> getRuns(@Bind("userid") final long userid, @Define("clause") final String clause);

    @SqlQuery("select * from runs where userid = :userid AND <clause> order by date limit :skip, :perPage;")
    List<Run> getRuns(@Bind("userid") final long userid, @Bind("skip") final int skip, @Bind("perPage") final int perPage, @Define("clause") final String clause);

    @SqlUpdate("INSERT INTO runs(`userid`,`duration`,`distance`, `date`, `latitude`, `longitude`, `weather`) " +
            "VALUES (:userId, :duration, :distance, :date, :latitude, :longitude, :weather)")
    void createRun(@BindBean final Run run);

    @SqlUpdate("UPDATE runs SET runtime = coalesce(:duration, duration), date = coalesce(:date, date)" +
            ", lat = coalesce(:latitude, latitude), lon = coalesce(:longitude, longitude), weather = coalesce(:weather, weather))" +
            "WHERE id = :id")
    void editRun(@BindBean final Run run);

    @SqlUpdate("delete from runs where id = :id")
    int deleteRun(@Bind("id") final int id);

    @SqlQuery("select last_insert_id();")
    int lastInsertId();
}