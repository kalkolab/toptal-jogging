package com.toptal.jogging;

import com.toptal.jogging.dao.UsersDAO;
import com.toptal.jogging.dropwizard.JoggingAuthenticator;
import com.toptal.jogging.dropwizard.JoggingConfiguration;
import com.toptal.jogging.dropwizard.JoggingUserAuthorizer;
import com.toptal.jogging.model.User;
import com.toptal.jogging.resources.UsersResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 * Hello world!
 *
 */
public class JoggingApp extends Application<JoggingConfiguration> {

    public static void main(String[] args) throws Exception {
        new JoggingApp().run(args);
    }

    public void run(JoggingConfiguration joggingConfiguration, Environment environment) throws Exception {
        AuthFilter basicAuthFilter = new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new JoggingAuthenticator())
                .setAuthorizer(new JoggingUserAuthorizer())
                .setRealm("shire")
                .setPrefix("Basic")
                .buildAuthFilter();

        environment.jersey().register(new AuthDynamicFeature(basicAuthFilter));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder(User.class));

        environment.jersey().register(new UsersResource(new UsersDAO()));

//        environment.jersey().register(unauthorizedHandler);
    }
}
