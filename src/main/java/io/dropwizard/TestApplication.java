package io.dropwizard;


import io.dropwizard.core.Person;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PersonDAO;
import io.dropwizard.health.TestHealthCheck;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.resources.PersonResource;
import io.dropwizard.resources.TestResources;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import liquibase.exception.LiquibaseException;


import javax.servlet.ServletRegistration;


/*
 * Application file where we initialize the application
 */
public class TestApplication extends Application<TestConfiguration> {

    private final HibernateBundle<TestConfiguration> hibernateBundle =
            new HibernateBundle<TestConfiguration>(Person.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(TestConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };


    //3. Finally when all configuration is done the main application is initialized here
    public static void main(final String[] args) throws Exception {
        new TestApplication().run(args);
    }

    //4. This is the data that is returned when the application runs
    @Override
    public String getName() {
        return "Test";
    }

    // 1. All packages and bundles are initialized here
    @Override
    public void initialize(final Bootstrap<TestConfiguration> bootstrap) {
        // TODO: application initialization
//        bootstrap.setConfigurationSourceProvider(
//                new SubstitutingSourceProvider(
//                        bootstrap.getConfigurationSourceProvider(),
//                        new EnvironmentVariableSubstitutor(false)
//                )
//        );

        //bundle for liquibase migration
        bootstrap.addBundle(new MigrationsBundle<TestConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(TestConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        //bundle for hibernate config
        bootstrap.addBundle(hibernateBundle);
    }


    //2. Anything that should be done before the application initialized-it is done here

    @Override
    public void run(final TestConfiguration configuration,
                    final Environment environment) throws LiquibaseException {
        // TODO: implement application
//        final JdbiFactory factory = new JdbiFactory();
//        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "h2");
//        environment.jersey().register(new PersonResource(jdbi));

//        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
//        Database database = (Database) dataSourceFactory.build(environment.metrics(), "h2");
//
//        // Run Liquibase migration
//        Liquibase migrator = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), database);
//        migrator.update("");


//        This will create a new managed connection pool to the database
        final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());


        final PersonResource personResource = new PersonResource(dao);
       // add it to the application’s Jersey environment:
        final TestResources resource = new TestResources(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );

        final TestHealthCheck healthCheck = new TestHealthCheck(
                configuration.getTemplate()
        );

        //registering h2
        // Register the H2 console servlet
        final ServletRegistration.Dynamic h2Servlet = environment.servlets()
                .addServlet("H2Console", new org.h2.server.web.WebServlet());

        // Configure the H2 console servlet
        h2Servlet.addMapping("/h2/*");
        h2Servlet.setInitParameter("webAllowOthers", "true");

        environment.healthChecks().register("template",healthCheck);
        environment.jersey().register(resource);
        environment.jersey().register(personResource);
    }

}
