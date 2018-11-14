package com.leadiq.imguruploader.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.leadiq.imguruploader.ImguruploaderWebApplicationTests;
import com.leadiq.imguruploader.model.Job;
import com.leadiq.imguruploader.model.UploadedImages;
import com.leadiq.imguruploader.repository.ImgurRepository;
import com.leadiq.imguruploader.service.JobExecutor;

public class ImgurRepositoryTest extends ImguruploaderWebApplicationTests {

    @InjectMocks
    @Autowired
    private ImgurRepository subject;

    @Mock
    private JobExecutor executor;

    @Test
    public void testUploadImage() {
	doNothing().when(executor).executeJob(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
	String jobiId = subject.submitJob(getMockJob());
	assertEquals(jobiId, "jobid");
	assertEquals(subject.fetchJobStatus("jobid").getStatus(), "pending");
	assertEquals(subject.fetchJobStatus("jobid").getPending().size(), 2);

    }

    @Test
    public void testFetchAllImages() {
	Job job = getMockJob();
	subject.submitJob(job);

	UploadedImages images = subject.fetchAllImages();
	assertEquals(images.getUploaded().size(), 0);

	job.getPending().remove("image1");
	job.getPending().remove("image2");

	job.getComplete().add("image1");
	job.getComplete().add("image2");

	images = subject.fetchAllImages();
	assertEquals(images.getUploaded().size(), 2);
    }
}
