package com.toptal.jogging.domain.service;

import com.toptal.jogging.domain.Run;
import com.toptal.jogging.domain.User;
import com.toptal.jogging.domain.dao.RunsDao;
import org.apache.commons.lang3.StringUtils;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

public abstract class RunsService {
  private static final String RUN_NOT_FOUND = "Run id %s not found.";
  private static final String EDIT_NOT_ALLOWED = "You can only edit your own entries";
  private static final String DATABASE_REACH_ERROR =
          "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
  private static final String DATABASE_CONNECTION_ERROR =
          "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
  private static final String DATABASE_UNEXPECTED_ERROR =
          "Unexpected error occurred while attempting to reach the database. Details: ";
  private static final String SUCCESS = "Success...";
  private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting User.";

  @CreateSqlObject
  abstract RunsDao runsDao();

  public List<Run> getRuns(Integer page, Integer perPage) {
    if (page == null) page = 1;

    return perPage != null
            ? runsDao().getRuns(perPage*(page-1), perPage)
            : runsDao().getRuns();
  }

  public Run getRun(int id) {
    Run run = runsDao().getRun(id);
    if (Objects.isNull(run)) {
      throw new WebApplicationException(String.format(RUN_NOT_FOUND, id), Response.Status.NOT_FOUND);
    }
    return run;
  }

  public List<Run> getRuns(int userId, Integer page, Integer perPage, String filter) {
    if (page == null) page = 1;

    String clause = StringUtils.defaultIfEmpty(filter, "true");
    return perPage != null
            ? runsDao().getRuns(userId, perPage*(page-1), perPage, clause)
            : runsDao().getRuns(userId, clause);
  }

  public Run createRun(User user, Run run) {
    runsDao().createRun(run);
    return runsDao().getRun(runsDao().lastInsertId());
  }

  public Run editRun(User user, Run run) {
    Run existingRun = runsDao().getRun(run.getId());
    if (existingRun == null) {
      throw new WebApplicationException(String.format(RUN_NOT_FOUND, run.getId()),
              Response.Status.NOT_FOUND);
    }
    if (existingRun.getUserId() != user.getId() && user.getRole() != User.Role.ADMIN) {
      throw new WebApplicationException(EDIT_NOT_ALLOWED, Response.Status.UNAUTHORIZED);
    }
    runsDao().editRun(run);
    return runsDao().getRun(run.getId());
  }
//
  public String deleteRun(User user, final int runId) {
    Run run = runsDao().getRun(runId);

    if (run == null) {
      throw new WebApplicationException(String.format(RUN_NOT_FOUND, runId), Response.Status.NOT_FOUND);
    }

    if (run.getUserId() != user.getId() && user.getRole() != User.Role.ADMIN) {
      throw new WebApplicationException(EDIT_NOT_ALLOWED, Response.Status.METHOD_NOT_ALLOWED);
    }

    int result = runsDao().deleteRun(runId);
    switch (result) {
      case 1:
        return SUCCESS;
      case 0:
        throw new WebApplicationException(String.format(RUN_NOT_FOUND, runId), Response.Status.NOT_FOUND);
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