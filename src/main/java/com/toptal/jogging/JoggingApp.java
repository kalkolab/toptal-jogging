package com.toptal.jogging;

import com.toptal.jogging.domain.User;
import com.toptal.jogging.domain.service.RunsService;
import com.toptal.jogging.domain.service.UsersService;
import com.toptal.jogging.resources.RunsResource;
import com.toptal.jogging.resources.UsersResource;
import com.toptal.jogging.security.JoggingAuthenticator;
import com.toptal.jogging.security.JoggingUserAuthorizer;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

import javax.sql.DataSource;

/**
 * Hello world!
 *
 */
public class JoggingApp extends Application<JoggingConfiguration> {

    public static void main(String[] args) throws Exception {
        new JoggingApp().run(args);
    }

    public void run(JoggingConfiguration configuration, Environment environment) throws Exception {
        final DataSource dataSource =
                configuration.getDataSourceFactory().build(environment.metrics(), "sql");
        DBI dbi = new DBI(dataSource);

        AuthFilter basicAuthFilter = new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new JoggingAuthenticator(dbi.onDemand(UsersService.class)))
                .setAuthorizer(new JoggingUserAuthorizer())
                .setPrefix("Basic")
                .buildAuthFilter();

        environment.jersey().register(new AuthDynamicFeature(basicAuthFilter));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder(User.class));

        environment.jersey().register(new UsersResource(dbi.onDemand(UsersService.class)));
        environment.jersey().register(new RunsResource(dbi.onDemand(RunsService.class)));

//        environment.jersey().register(unauthorizedHandler);
    }
}
