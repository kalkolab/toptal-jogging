package com.toptal.jogging.resources;

import com.toptal.jogging.domain.Location;
import com.toptal.jogging.domain.Run;
import com.toptal.jogging.domain.User;
import com.toptal.jogging.domain.service.RunsService;
import com.toptal.jogging.model.Representation;
import io.dropwizard.auth.Auth;
import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.*;
import tk.plogitech.darksky.forecast.model.Forecast;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * REST API for runs control
 *
 * Created by Artem on 26.06.2017.
 */
@Path("/runs")
@RolesAllowed({"USER", "MANAGER"})
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
    public Representation<List<Run>> list(@Auth User user, @QueryParam("page") Integer page, @QueryParam("per_page") Integer perPage) {
        if (user.getRole() == User.Role.USER) {
            return new Representation<>(Response.Status.OK, runsService.getRuns(user.getId(), page, perPage));
        } else if (user.getRole() == User.Role.ADMIN) {
            return new Representation<>(Response.Status.OK, runsService.getRuns(page, perPage));
        } else {
            return new Representation<>(Response.Status.METHOD_NOT_ALLOWED, null);
        }
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
    public Representation<List<Run>> list(@PathParam("userId") int userId, @QueryParam("page") Integer page, @QueryParam("per_page") Integer perPage) {
        return new Representation<>(Response.Status.OK, runsService.getRuns(userId, page, perPage));
    }


    /**
     * <i>GET /runs/id</i>
     * <br>
     *
     * @param id run id
     *
     * @return Get user info as JSON
     */
    @GET
    @PermitAll
    @Path("{id}")
    public Representation<Run> getRun(@Auth User user, @PathParam("id") int id) {
        Run run = runsService.getRun(id);
        if (run.getUserId() == user.getId() || user.getRole() == User.Role.ADMIN)
            return new Representation<>(Response.Status.OK, run);
        else return new Representation<>(Response.Status.UNAUTHORIZED, null);
    }

    /**
     * <i>GET /users/id</i>
     * <br>
     * You must have Manager role to call this method
     *
     *
     * @return Get user info as JSON
     */
//    @GET
//    @RolesAllowed("USER")
//    @Path("me")
//    public Representation<Run> getRun(@Auth Run user) {
//        Run userById = runsService.getRun(user.getName());
//        return new Representation<>(Response.Status.OK, userById);
//    }
//
//    /**
//     * <i>GET /users/id</i>
//     * <br>
//     * You must have Manager role to call this method
//     *
//     * @param user Run object as JSON
//     *
//     * @return Get user info as JSON
//     */
//    @PUT
//    @RolesAllowed({"MANAGER", "ADMIN"})
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Representation<Run> editRun(final Run user) {
//        return new Representation<>(Response.Status.OK, runsService.editRun(user));
//    }
//
//    /**
//     * <i>DELETE /users/id</i>
//     * <br>
//     * You must have Manager role to call this method
//     *
//     * @param id user id
//     *
//     * @return result string
//     */
//    @DELETE
//    @Path("{id}")
//    @RolesAllowed({"MANAGER", "ADMIN"})
//    public Representation<String> delete(@PathParam("id") final int id) {
//        return new Representation<>(Response.Status.OK, runsService.deleteRun(id));
//    }

    private String getWeather(Location location, Date date) {
        try {
            ForecastRequest request = new ForecastRequestBuilder()
                    .key(new APIKey(forecastKey))
                    .location(new GeoCoordinates(new Longitude(location.getLongitude()), new Latitude(location.getLatitude())))
                    .time(date.toInstant())
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