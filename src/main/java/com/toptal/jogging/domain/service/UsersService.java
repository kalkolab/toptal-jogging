package com.toptal.jogging.domain.service;

import com.toptal.jogging.domain.User;
import com.toptal.jogging.domain.dao.UsersDao;
import org.apache.commons.lang3.StringUtils;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

public abstract class UsersService {
  private static final String USER_NOT_FOUND = "User id %s not found.";
  private static final String USER_EXISTS = "User %s already exists.";
  private static final String DATABASE_REACH_ERROR =
      "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
  private static final String DATABASE_CONNECTION_ERROR =
      "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
  private static final String DATABASE_UNEXPECTED_ERROR =
      "Unexpected error occurred while attempting to reach the database. Details: ";
  private static final String SUCCESS = "Success...";
  private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting User.";

  @CreateSqlObject
  abstract UsersDao usersDao();

  public List<User> getUsers(Integer page, Integer perPage, String filter) {
    if (page == null) page = 1;

    String clause = StringUtils.defaultIfEmpty(filter, "true");
    return perPage != null
            ? usersDao().getUsers((page-1)*perPage, perPage, clause)
            : usersDao().getUsers(clause);
  }

  public User getUser(int id) {
    User user = usersDao().getUser(id);
    if (Objects.isNull(user)) {
      throw new WebApplicationException(String.format(USER_NOT_FOUND, id), Response.Status.NOT_FOUND);
    }
    return user;
  }

  public User getUser(String login) {
    User user = usersDao().getUser(login);
    if (Objects.isNull(user)) {
      throw new WebApplicationException(String.format(USER_NOT_FOUND, login), Response.Status.NOT_FOUND);
    }
    return user;
  }

  public User createUser(User user) {
    if (usersDao().getUser(user.getName()) != null) {
      throw new WebApplicationException(String.format(USER_EXISTS, user.getName()), Response.Status.BAD_REQUEST);
    }
    usersDao().createUser(user);
    return usersDao().getUser(usersDao().lastInsertId());
  }

  public User editUser(User user) {
    if (Objects.isNull(usersDao().getUser(user.getId()))) {
      throw new WebApplicationException(String.format(USER_NOT_FOUND, user.getId()),
          Response.Status.NOT_FOUND);
    }
    usersDao().editUser(user);
    return usersDao().getUser(user.getId());
  }

  public String deleteUser(final int id) {
    User user = usersDao().getUser(id);

    if (user == null) {
      throw new WebApplicationException(String.format(USER_NOT_FOUND, id), Response.Status.NOT_FOUND);
    }

    if (user.getRole() == User.Role.ADMIN) {
      throw new WebApplicationException("Can't delete admin", Response.Status.METHOD_NOT_ALLOWED);
    }

    int result = usersDao().deleteUser(id);
    switch (result) {
      case 1:
        return SUCCESS;
      case 0:
        throw new WebApplicationException(String.format(USER_NOT_FOUND, id), Response.Status.NOT_FOUND);
      default:
        throw new WebApplicationException(UNEXPECTED_ERROR, Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

//  public String performHealthCheck() {
//    try {
//      runsDao().getUsers();
//    } catch (UnableToObtainConnectionException ex) {
//      return checkUnableToObtainConnectionException(ex);
//    } catch (UnableToExecuteStatementException ex) {
//      return checkUnableToExecuteStatementException(ex);
//    } catch (Exception ex) {
//      return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
//    }
//    return null;
//  }
//
//  private String checkUnableToObtainConnectionException(UnableToObtainConnectionException ex) {
//    if (ex.getCause() instanceof java.sql.SQLNonTransientConnectionException) {
//      return DATABASE_REACH_ERROR + ex.getCause().getLocalizedMessage();
//    } else if (ex.getCause() instanceof java.sql.SQLException) {
//      return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
//    } else {
//      return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
//    }
//  }
//
//  private String checkUnableToExecuteStatementException(UnableToExecuteStatementException ex) {
//    if (ex.getCause() instanceof java.sql.SQLSyntaxErrorException) {
//      return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
//    } else {
//      return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
//    }
//  }
}