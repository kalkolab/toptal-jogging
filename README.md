# Task

### Write a REST API that tracks jogging times of users

* API Users must be able to create an account and log in.
* All API calls must be authenticated.
* Implement at least three roles with different permission levels: a regular user would only be able to CRUD on their owned records, a user manager would be able to CRUD only users, and an admin would be able to CRUD all records and users.
* Each time entry when entered has a date, distance, time, and location.
* Based on the provided date and location, API should connect to a weather API provider and get the weather conditions for the run, and store that with each run.
* The API must create a report on average speed & distance per week.
* The API must be able to return data in the JSON format.
* The API should provide filter capabilities for all endpoints that return a list of elements, as well should be able to support pagination.
* The API filtering should allow using parenthesis for defining operations precedence and use any combination of the available fields. The supported operations should at least include or, and, eq (equals), ne (not equals), gt (greater than), lt (lower than).
Example -> (date eq '2016-05-01') AND ((distance gt 20) OR (distance lt 10)).

### Usage
* HTTPS connection is used on port 8443
* basic HTTP authentication
* two resources available


#### /users
* POST /users/new - consumes JSON, create new user
* POST /users/new/admin or /users/new/manager - create new manager or admin user, can be accessed by admin only
* GET /users?page=X&per_page=Y&filter=QUERY - list existing users, accessed by admin or manager
* GET /users/{id} - get info about user with id, accessed by admin or manager
* GET /users/me - get info about current user
* PUT /users - consumes JSON, update info of user, accessed by admin or manager
* DELETE /users/{id} - delete user with id, accessed by admin or manager

#### /runs
* POST /runs/new - consumes JSON, create new run
* GET /runs?page=X&per_page=Y - list existing runs, accessed by admin (see runs for all users) or user
* GET /runs/{id} - get info about run with id, accessed by admin or user
* GET /runs/{userId} - get info about runs for user with id, accessed by admin
* GET /runs/weekly?page=X&per_page=Y&filter=QUERY - get weekly report for current user
* PUT /runs - consumes JSON, update info of the run owned by current user
* DELETE /runs/{id} - delete run with id

QUERY implements usual SQL syntax with OR, AND, > , <, =, <>