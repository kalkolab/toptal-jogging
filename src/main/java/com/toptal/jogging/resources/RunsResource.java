package com.toptal.jogging.resources;

import com.toptal.jogging.domain.Run;
import com.toptal.jogging.domain.User;
import com.toptal.jogging.domain.service.RunsService;
import com.toptal.jogging.model.Representation;
import io.dropwizard.auth.Auth;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * REST API for users control
 *
 * Created by Artem on 26.06.2017.
 */
@Path("/runs")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
public class RunsResource {
    private final RunsService runsService;

    public RunsResource(RunsService runsService) {
        this.runsService = runsService;
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
    public Representation<Run> createRun(@Auth User user, @NotNull final Run run) {
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
    public Representation<List<Run>> list(@Auth User user) {
        return new Representation<>(Response.Status.OK, runsService.getRuns(user.getId()));
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
        if (run.getUserId() == user.getId())
            return new Representation<>(Response.Status.OK, run);
        else return new Representation<>(Response.Status.UNAUTHORIZED, null);
    }

    /**
     * <i>GET /users/id</i>
     * <br>
     * You must have Manager role to call this method
     *
     * @param user Run object as JSON
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
}