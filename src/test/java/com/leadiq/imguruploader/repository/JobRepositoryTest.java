package com.leadiq.imguruploader.repository;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.leadiq.imguruploader.ImguruploaderWebApplicationTests;
import com.leadiq.imguruploader.model.Job;
import com.leadiq.imguruploader.model.JobStatus;
import com.leadiq.imguruploader.model.UploadedImages;
import com.leadiq.imguruploader.repository.JobRepository;
import com.leadiq.imguruploader.service.JobExecutor;

public class JobRepositoryTest extends ImguruploaderWebApplicationTests {

    @InjectMocks
    private JobRepository subject;

    @Mock
    private JobExecutor executor;

    @Before
    public void setUp() {
	subject = new JobRepository();
    }

    @Test
    public void testCreateJob() {
	Job job = subject.createJob(getMockJob().getPending());
	assertEquals(job.getId() != null, true);
	assertEquals(subject.fetchJobStatus(job.getId()).getStatus(), JobStatus.PENDING.getStatus());
	assertEquals(subject.fetchJobStatus(job.getId()).getPending().size(), 2);
    }

    @Test
    public void testFetchAllImages() {
	Job job = subject.createJob(getMockJob().getPending());
	UploadedImages images = subject.fetchAllImages();
	assertEquals(images.getUploaded().size(), 0);

	job.getPending().remove("image1");
	job.getPending().remove("image2");

	job.getComplete().add("image1");
	job.getComplete().add("image2");

	images = subject.fetchAllImages();
	assertEquals(images.getUploaded().size(), 2);
    }

    @Test
    public void testUpdateJobSuccess() {
	Job job = subject.createJob(getMockJob().getPending());
	subject.updateJobSuccess(job.getId(), "image1", "imgurl1");

	Job updatedJob = subject.fetchJobStatus(job.getId());
	assertEquals(updatedJob.getStatus(), JobStatus.INPROGRESS.getStatus());
	assertEquals(updatedJob.getPending().size(), 1);
	assertEquals(updatedJob.getComplete().size(), 1);

	subject.updateJobFailure(job.getId(), "image2");

	updatedJob = subject.fetchJobStatus(job.getId());
	assertEquals(updatedJob.getStatus(), JobStatus.COMPLETE.getStatus());
	assertEquals(updatedJob.getPending().size(), 0);
	assertEquals(updatedJob.getComplete().size(), 1);
	assertEquals(updatedJob.getFailed().size(), 1);
	assertEquals(updatedJob.getFinished() != null, true);
    }

    @Test
    public void testUpdateJobFailure() {
	Job job = subject.createJob(getMockJob().getPending());
	subject.updateJobFailure(job.getId(), "image1");

	Job updatedJob = subject.fetchJobStatus(job.getId());
	assertEquals(updatedJob.getStatus(), JobStatus.INPROGRESS.getStatus());
	assertEquals(updatedJob.getPending().size(), 1);
	assertEquals(updatedJob.getFailed().size(), 1);

	subject.updateJobFailure(job.getId(), "image2");

	updatedJob = subject.fetchJobStatus(job.getId());
	assertEquals(updatedJob.getStatus(), JobStatus.COMPLETE.getStatus());
	assertEquals(updatedJob.getPending().size(), 0);
	assertEquals(updatedJob.getFailed().size(), 2);
	assertEquals(updatedJob.getFinished() != null, true);
    }
}
