package com.toptal.jogging.domain.dao;

import com.toptal.jogging.domain.Location;
import com.toptal.jogging.domain.Run;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Artem on 03.07.2017.
 */
public class RunsMapper implements ResultSetMapper<Run> {
    private static final String ID = "id";
    private static final String USER_ID = "userid";
    private static final String RUNTIME = "runtime";
    private static final String DISTANCE = "distance";
    private static final String RUNDATE = "rundate";
    private static final String LATITUDE = "lat";
    private static final String LONGITUDE = "lon";
    private static final String WEATHER = "weather";

    public Run map(int i, ResultSet resultSet, StatementContext statementContext)
            throws SQLException {
        return new Run(
                resultSet.getInt(ID),
                resultSet.getInt(USER_ID),
                resultSet.getLong(RUNTIME),
                resultSet.getFloat(DISTANCE),
                resultSet.getDate(RUNDATE).toLocalDate(),
                new Location(resultSet.getFloat(LATITUDE), resultSet.getFloat(LONGITUDE)),
                resultSet.getString(WEATHER));
    }
}
