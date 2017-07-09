package com.toptal.jogging.resources;

import com.toptal.jogging.domain.User;
import com.toptal.jogging.domain.service.UsersService;
import com.toptal.jogging.model.Representation;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
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
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {
    private final UsersService usersService;

    public UsersResource(UsersService usersDAO) {
        this.usersService = usersDAO;
    }

    /**
     * <i>POST /users/new</i>
     * <br>
     * Create new user
     *
     * @param user User object as JSON containing name and password
     *
     * @return created user with id, name, role
     */
    @POST
    @Path("/new")
    public Representation<User> createUser(@NotNull final User user) {
        if (User.Role.USER != user.getRole()) {
            return new Representation<User>(Response.Status.UNAUTHORIZED, null);
        }
        User created = usersService.createUser(user);
        if (created != null) {
            return new Representation<>(Response.Status.OK, created);
        } else {
            return new Representation<>(Response.Status.INTERNAL_SERVER_ERROR, null);
        }
    }

    /**
     * <i>POST /users/new</i>
     * <br>
     * Create new user
     *
     * @param user User object as JSON containing name and password
     *
     * @return created user with id, name, role
     */
    @POST
    @RolesAllowed("ADMIN")
    @Path("new/{role: admin|manager}")
    public Representation<User> createUser(@PathParam("role") String role, @NotNull final User user) {
        User created = usersService.createUser(user);
        if (created != null) {
            return new Representation<>(Response.Status.OK, created);
        } else {
            return new Representation<>(Response.Status.INTERNAL_SERVER_ERROR, null);
        }
    }

    /**
     * <i>GET /users</i>
     * <br>
     * You must have Manager role to call this method
     *
     * @return List of all users as JSON
     */
    @GET
    @RolesAllowed({"MANAGER", "ADMIN"})
    public Representation<List<User>> list(@QueryParam("page") Integer page, @QueryParam("per_page") Integer perPage) {
        return new Representation<>(Response.Status.OK, usersService.getUsers(page, perPage));
    }

    /**
     * <i>GET /users/id</i>
     * <br>
     * You must have Manager role to call this method
     *
     * @param id user id
     *
     * @return Get user info as JSON
     */
    @GET
    @RolesAllowed({"MANAGER", "ADMIN"})
    @Path("{id}")
    public Representation<User> getUser(@PathParam("id") int id) {
        return new Representation<>(Response.Status.OK, usersService.getUser(id));
    }

    /**
     * <i>GET /users/me</i>
     * <br>
     * You must have Manager role to call this method
     *
     * @return Get user info as JSON
     */
    @GET
    @RolesAllowed("USER")
    @Path("me")
    public Representation<User> getUser(@Auth User user) {
        User userById = usersService.getUser(user.getName());
        return new Representation<>(Response.Status.OK, userById);
    }

    /**
     * <i>PUT /users</i>
     * <br>
     * You must have Manager role to call this method
     *
     * @param user User object as JSON
     *
     * @return Get user info as JSON
     */
    @PUT
    @RolesAllowed({"MANAGER", "ADMIN"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Representation<User> editUser(final User user) {
        return new Representation<>(Response.Status.OK, usersService.editUser(user));
    }

    /**
     * <i>DELETE /users/id</i>
     * <br>
     * You must have Manager role to call this method
     *
     * @param id user id
     *
     * @return result string
     */
    @DELETE
    @Path("{id}")
    @RolesAllowed({"MANAGER", "ADMIN"})
    public Representation<String> delete(@PathParam("id") final int id) {
        return new Representation<>(Response.Status.OK, usersService.deleteUser(id));
    }
}