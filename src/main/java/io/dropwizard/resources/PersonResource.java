package io.dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.api.PersonRequest;
import io.dropwizard.api.PersonResponse;
import io.dropwizard.core.Person;
import io.dropwizard.db.PersonDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.util.DataConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/persons")

// providing application/json representations
// Add this to set the consume type @Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    private final PersonDAO personDAO;

    public PersonResource(PersonDAO peopleDAO) {
        this.personDAO = peopleDAO;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);

    //GET method
    @GET //get endpoint
    @Path("/test")
    // the method execution time will be recorded
    @Timed(name = "get-requests-timed")
    //Caching
    @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.DAYS)
    public String checkPerson() {

        LOGGER.info("Inside checkPerson");
        return "Person Resource is working!!";
    }

    @GET
    @UnitOfWork //since the method is transactional
    public List<PersonResponse> getAllPersons() {
        return DataConvertor.getPersonResponseDto(personDAO.findAll());
    }

    //POST method
    @POST
    @UnitOfWork
    public Person createPerson(@Valid PersonRequest personRequester) {
        return personDAO.create(DataConvertor.getPersonObject(personRequester));
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Map<String, Object> deletePerson(@PathParam("id") Long id) {

         boolean response = personDAO.deleteById(id);

         if (!response) {
             return DataConvertor.buildErrorResponse(id);
         }

        return DataConvertor.buildSuccessResponse(id);
    }
}
