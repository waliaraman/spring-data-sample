package com.emc.documentum.sample.setup;

import com.emc.documentum.sample.TestConfig;
import com.emc.documentum.sample.domain.Contact;
import com.emc.documentum.sample.repositories.ContactRepository;
import com.emc.documentum.springdata.core.Documentum;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Integration test setting up sample contact data using Spring Data Contact Repository
 *
 * @author Simon O'Brien
 */
@RunWith(JUnitParamsRunner.class)
@SpringApplicationConfiguration(classes = {TestConfig.class})
public class SetupRepositoryContactsTest {

    private TestContextManager testContextManager;

    @Autowired
    @Qualifier("repositoryName")
    private String repositoryName;

    @Autowired
    @Qualifier("repositoryUsername")
    private String repositoryUsername;

    @Autowired
    @Qualifier("repositoryPassword")
    private String repositoryPassword;

    @Autowired
    private Documentum documentum;

    @Autowired
    private ContactRepository contactRepository;

    /**
     * Setup Documentum Credentials and Spring
     */
    @Before
    public void setup() throws Exception {

        /*
         * As class can only have a single @RunWith annotation, we cannot use both:
         * @RunWith(SpringJUnit4ClassRunner.class)
         * and
         * @RunWith(JUnitParamsRunner.class)
         *
         * To ensure Spring Context load we need to manually do what the spring runner would do for us ...
         */
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);

        documentum.setCredentials(new UserCredentials(repositoryUsername, repositoryPassword));
        documentum.setDocBase(repositoryName);
    }

    /*
     * generate dummy groups
     */
    private List<String> generateGroups() {

        List<String> groups = new ArrayList<String>();

        // setup available groups
        List<String> availableGroups = new ArrayList<String>(Arrays.asList("Friends", "Family", "Work"));

        // decide how many groups to select (between 0 and number of available groups)
        int numGroups = new Random().nextInt(availableGroups.size());

        for(int i=0; i<numGroups; i++) {
            int index = new Random().nextInt(availableGroups.size());
            groups.add(availableGroups.remove(index));
        }

        return groups;
    }

    /*
     * generate a dummy email address
     */
    private String generateEmailAddress(String name) {

        String email = name.toLowerCase().replace(" ", ".");

        String[] dummyDomains = {
                "thecloud.com",
                "coldmail.net",
                "boohoo.com",
                "jungle.biz",
                "emailme.com",
                "dontsendmespam.net",
                "emailisso1990s.com"
        };

        int index = new Random().nextInt(dummyDomains.length);

        return email + "@" + dummyDomains[index];
    }

    /**
     * Delete all repository contacts
     */
    @Test
    public void deleteAllRepositoryContacts() {

        // find all contacts
        Iterable<Contact> contactIterator = contactRepository.findAll();
        assertThat(contactIterator, is(notNullValue()));

        // iterate through the contacts and delete them
        for (Contact contact : contactIterator) {
            contactRepository.delete(contact);
        }
    }

    /**
     * Setup of repository contacts
     */
    @Test
    @FileParameters("classpath:addresses.csv")
    public void setupRepositoryContacts(String name, String telephone) {

        assertThat(name, not(isEmptyOrNullString()));
        assertThat(telephone, not(isEmptyOrNullString()));

        // create the contact
        Contact sampleContact = new Contact();
        sampleContact.setName(name);
        sampleContact.setEmail(generateEmailAddress(name));
        sampleContact.setTelephone(telephone);

        // save the contact to the repository
        contactRepository.save(sampleContact);
    }
}
