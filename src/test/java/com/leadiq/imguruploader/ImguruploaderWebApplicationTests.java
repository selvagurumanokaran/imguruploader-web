package com.leadiq.imguruploader;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.leadiq.imguruploader.model.Job;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class ImguruploaderWebApplicationTests {

    @Before
    public void injectMocks() {
	MockitoAnnotations.initMocks(this);
    }

    protected Job getMockJob() {
	Set<String> urls = new HashSet<>();
	urls.add("image1");
	urls.add("image2");
	Job job = new Job("jobid", new Date(), urls);
	return job;
    }
}
