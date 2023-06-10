package io.dropwizard.util;

import io.dropwizard.api.PersonRequest;
import io.dropwizard.api.PersonResponse;
import io.dropwizard.core.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataConvertor {

    public static final Logger LOGGER = LoggerFactory.getLogger(DataConvertor.class);

    public static List<PersonResponse> getPersonResponseDto(List<Person> personList) {

        LOGGER.info("Inside getPersonResponseDto");
        return personList.stream()
                .map(DataConvertor::getPersonResponse)
                .collect(Collectors.toList());
    }
    public static PersonResponse getPersonResponse(Person person) {

        return PersonResponse.builder()
                .fullName(person.getFullName())
                .jobTitle(person.getJobTitle())
                .yearBorn(person.getYearBorn())
                .build();
    }

    public static Person getPersonObject(PersonRequest personRequest) {
        return new Person(personRequest.getFullName(), personRequest.getJobTitle(), personRequest.getYearBorn());
    }

    public static Map<String, Object> buildSuccessResponse (Long result) {
        Map<String, Object> successMap = new HashMap<>();
        successMap.put("success", true);
        successMap.put("results", result);

        return successMap;
    }

    public static Map<String, Object> buildErrorResponse(Long result) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("success", false);
        errorMap.put("reasons", String.format("The requested id %s doesn't exist",result.toString()));

        return errorMap;
    }
}
