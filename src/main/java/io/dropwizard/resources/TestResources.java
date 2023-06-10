package io.dropwizard.resources;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import io.dropwizard.api.Saying;
import io.dropwizard.api.SuccessResponse;
import io.dropwizard.jersey.jsr310.LocalDateTimeParam;
import io.dropwizard.jersey.caching.CacheControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/hello-world")

// providing and consuming application/json representations
@Produces(MediaType.APPLICATION_JSON)
public class TestResources {

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TestResources.class);


    public TestResources(String template, String defaultName) {

        this.template = template;
        this.defaultName = defaultName;
        this.counter  = new AtomicLong();
    }
    

    //GET method
    @GET //get endpoint
    // the method execution time will be recorded
    @Timed(name = "get-requests-timed") 
    //Caching
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
    public Saying sayHello(@QueryParam("name") Optional<String> name) {

        LOGGER.info("Inside sayHello");
        final String value = String.format(template, name.orElse(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }

    //POST method

    @POST
    public SuccessResponse receiveHello(Saying saying) {
        LOGGER.info("Received a saying: {}", saying);

        SuccessResponse successResponse = new SuccessResponse();

        if (saying.getId() != 0) {
            successResponse.setId(String.valueOf(saying.getId()));
            successResponse.setSuccess(true);

            return successResponse;
        }
        else {
            successResponse.setId("400");
            successResponse.setSuccess(false);
            return successResponse;
        }
    }

    //Date endpoint

    @GET
    @Path("/date")
    public String receiveDate(@QueryParam("date") Optional<LocalDateTimeParam> dateTimeParam) {

        if (dateTimeParam.isPresent()) {
            final LocalDateTimeParam actualDateTimeParam = dateTimeParam.get();
            LOGGER.info("Receive a date: {}", actualDateTimeParam);

            return actualDateTimeParam.toString();
        }
        else {
            LOGGER.warn("No received date");
            return null;
        }
    }
}
