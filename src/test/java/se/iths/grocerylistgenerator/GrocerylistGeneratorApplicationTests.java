package se.iths.grocerylistgenerator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import se.iths.grocerylistgenerator.dto.AddPersonDto;
import se.iths.grocerylistgenerator.dto.PersonDto;
import se.iths.grocerylistgenerator.entity.Person;
import se.iths.grocerylistgenerator.exception.BadRequestException;
import se.iths.grocerylistgenerator.repository.PersonRepository;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GrocerylistGeneratorApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    PersonRepository personRepository;

    @BeforeEach
    void setUp() {

        Person person1 = new Person("Kalle", "123");
        Person person2 = new Person("Mia", "456");
        Person person3 = new Person("Nisse", "789");

        personRepository.save(person1);
        personRepository.save(person2);
        personRepository.save(person3);
    }

    @AfterEach
    void tearDown() {
        personRepository.deleteAll();
    }

    @Test
    void testSignupNewPersonReturnsPersonDtoAndStatusCreated() {
        AddPersonDto addPersonDto = new AddPersonDto("Anna", "147");
        var createPersonResult = testRestTemplate.
                postForEntity("http://localhost:" + port + "/signup", addPersonDto, PersonDto.class);
        assertThat(createPersonResult.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(createPersonResult.getBody()).getUsername()).isEqualTo(addPersonDto.getUsername());

        List<Person> all = personRepository.findAll();
        assertThat(all.size()).isEqualTo(4);

    }

    @Test
    void testSignupNewPersonWithExistingUsernameReturnsBadRequest() {
        AddPersonDto addPersonDto = new AddPersonDto("Kalle", "147");
        var result = testRestTemplate.
                postForEntity("http://localhost:" + port + "/signup", addPersonDto, BadRequestException.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        List<Person> all = personRepository.findAll();
        assertThat(all.size()).isEqualTo(3);
    }
}
