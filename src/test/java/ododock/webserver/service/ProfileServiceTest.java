package ododock.webserver.service;

import jakarta.persistence.EntityManager;
import ododock.webserver.common.CleanUp;
import ododock.webserver.repository.ProfileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProfileServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private CleanUp cleanup;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @AfterEach
    void tearDown() {
        cleanup.all();
    }

    @Test
    void given_image_creation_request_has_proper_fields_then_should_create_new_image() {
        // given


        // when


        // then

    }

}
