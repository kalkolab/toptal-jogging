package com.toptal;

import com.toptal.dao.UsersDAO;
import com.toptal.dropwizard.JoggingAuthenticator;
import com.toptal.dropwizard.JoggingConfiguration;
import com.toptal.dropwizard.JoggingUserAuthorizer;
import com.toptal.model.User;
import com.toptal.resources.UsersResource;
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
