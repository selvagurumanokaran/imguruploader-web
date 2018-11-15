package com.leadiq.imguruploader.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.HashSet;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.leadiq.imguruploader.ImguruploaderWebTests;
import com.leadiq.imguruploader.model.JobRequest;

public class ImgurServiceTest extends ImguruploaderWebTests {

    @Autowired
    private ImgurService subject;

    @Mock
    private JobExecutor executor;

    @Test
    public void testCreateJob() {
	JobRequest jobRequest = new JobRequest();
	HashSet<String> urls = new HashSet<>();
	urls.add("imgurl1");
	jobRequest.setUrls(urls);

	doReturn(null).when(executor).executeJob(ArgumentMatchers.any(), ArgumentMatchers.any());
	String jobId = subject.createJob(jobRequest);
	assertEquals(jobId != null, true);
    }
}
