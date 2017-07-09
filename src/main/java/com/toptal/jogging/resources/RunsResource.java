package com.toptal.jogging.resources;

import com.toptal.jogging.domain.Location;
import com.toptal.jogging.domain.Run;
import com.toptal.jogging.domain.User;
import com.toptal.jogging.domain.WeeklyRuns;
import com.toptal.jogging.domain.service.RunsService;
import com.toptal.jogging.model.Representation;
import io.dropwizard.auth.Auth;
import org.apache.commons.lang3.tuple.Pair;
import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Forecast;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * REST API for runs control
 *
 * Created by Artem on 26.06.2017.
 */
@Path("/runs")
@RolesAllowed({"USER", "ADMIN"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RunsResource {
    private final RunsService runsService;
    private final DarkSkyJacksonClient weatherClient = new DarkSkyJacksonClient();
    private final String forecastKey;

    public RunsResource(RunsService runsService, String forecastKey) {
        this.runsService = runsService;
        this.forecastKey = forecastKey;
    }

    /**
     * <i>POST /runs/new</i>
     * <br>
     * Create new run
     *
     * @param run Run object as JSON
     *
     * @return created run
     */
    @POST
    @Path("/new")
    @RolesAllowed("USER")
    public Representation<Run> createRun(@Auth User user, @NotNull final Run run) {
        run.setUserId(user.getId());
        run.setWeather(getWeather(run.getLocation(), run.getDate()));
        Run created = runsService.createRun(user, run);
        if (created != null) {
            return new Representation<>(Response.Status.OK, created);
        } else {
            return new Representation<>(Response.Status.INTERNAL_SERVER_ERROR, null);
        }
    }

    /**
     * <i>GET /runs</i>
     * <br>
     *
     * @return List of all runs for user
     */
    @GET
    public Representation<List<Run>> list(@Auth User user, @QueryParam("page") Integer page, @QueryParam("per_page") Integer perPage, @QueryParam("filter") String filter) {
        if (user.getRole() == User.Role.USER) {
            List<Run> runs = runsService.getRuns(user.getId(), page, perPage, filter);
            return new Representation<>(Response.Status.OK, runs);
        } else if (user.getRole() == User.Role.ADMIN) {
            return new Representation<>(Response.Status.OK, runsService.getRuns(page, perPage));
        } else {
            return new Representation<>(Response.Status.METHOD_NOT_ALLOWED, null);
        }
    }

    /**
     * <i>GET /runs/weekly</i>
     * <br>
     *
     * @return List of weely data including total distance and average speed, sorted by date
     */
    @GET
    @Path("weekly")
    public Representation<List<WeeklyRuns>> listWeekly(@Auth User user, @QueryParam("page") Integer page, @QueryParam("per_page") Integer perPage, @QueryParam("filter") String filter) {
        List<Run> allRuns = runsService.getRuns(user.getId(), null, null, filter);
        Map<Pair<LocalDate, LocalDate>, List<Run>> grouppedRuns = allRuns.stream().collect(Collectors.groupingBy(run -> dateToWeek(run.getDate())));

        Stream<WeeklyRuns> weeklyAverages = grouppedRuns.entrySet().stream().map(entry -> {
            float dist = 0;
            long duration = 0;
            for (Run run : entry.getValue()) {
                dist += run.getDistance();
                duration += run.getDuration();
            }
            return new WeeklyRuns(user.getId(), entry.getKey().getLeft(), entry.getKey().getRight(), entry.getValue().size(), duration, dist);
        }).sorted(Comparator.comparing(WeeklyRuns::getWeekStart));

        if (perPage != null) {
            if (page != null) {
                weeklyAverages = weeklyAverages.skip((page-1) * perPage);
            }
            weeklyAverages = weeklyAverages.limit(perPage);
        }
        return new Representation<>(Response.Status.OK, weeklyAverages.collect(Collectors.toList()));
    }

    private static Pair<LocalDate, LocalDate> dateToWeek(LocalDate date) {
        return Pair.of(date.with(DayOfWeek.MONDAY), date.with(DayOfWeek.SUNDAY));
    }


    /**
     * <i>GET /runs/userId</i>
     * <br>
     *
     * @return List of all runs for user with userId
     */
    @GET
    @RolesAllowed({"ADMIN"})
    @Path("/user/{userId}")
    public Representation<List<Run>> list(@PathParam("userId") int userId, @QueryParam("page") Integer page, @QueryParam("per_page") Integer perPage, @QueryParam("filter") String filter) {
        return new Representation<>(Response.Status.OK, runsService.getRuns(userId, page, perPage, filter));
    }


    /**
     * <i>GET /runs/id</i>
     * <br>
     *
     * @param id run id
     *
     * @return run info as JSON
     */
    @GET
    @Path("{id}")
    public Representation<Run> getRun(@Auth User user, @PathParam("id") int id) {
        Run run = runsService.getRun(id);
        if (run.getUserId() == user.getId() || user.getRole() == User.Role.ADMIN)
            return new Representation<>(Response.Status.OK, run);
        else return new Representation<>(Response.Status.UNAUTHORIZED, null);
    }


    /**
     * <i>PUT /runs/id</i>
     * <br>
     * You must have Manager role to call this method
     *
     * @param user Run object as JSON
     *
     * @return Get user info as JSON
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Representation<Run> editRun(@Auth User user, final Run run) {
        return new Representation<>(Response.Status.OK, runsService.editRun(user, run));
    }

    /**
     * <i>DELETE /runs/id</i>
     * <br>
     * Delete one of user's runs
     *
     * @param id run id
     *
     * @return result string
     */
    @DELETE
    @Path("{id}")
    public Representation<String> delete(@Auth User user, @PathParam("id") final int id) {
        return new Representation<>(Response.Status.OK, runsService.deleteRun(user, id));
    }

    private String getWeather(Location location, LocalDate date) {
        try {
            ForecastRequest request = new ForecastRequestBuilder()
                    .key(new APIKey(forecastKey))
                    .location(new GeoCoordinates(new Longitude(location.getLongitude()), new Latitude(location.getLatitude())))
                    .time(date.atStartOfDay().toInstant(ZoneOffset.UTC))
                    .language(ForecastRequestBuilder.Language.en)
                    .units(ForecastRequestBuilder.Units.si)
                    .build();

            Forecast forecast = weatherClient.forecast(request);
            return forecast.getCurrently().getSummary() + ", " + forecast.getCurrently().getTemperature();
        } catch (ForecastException e) {
            return null;
        }
    }
}