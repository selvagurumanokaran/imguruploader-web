package com.leadiq.imguruploader.service;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.leadiq.imguruploader.ImguruploaderWebApplicationTests;
import com.leadiq.imguruploader.model.Job;
import com.leadiq.imguruploader.model.JobStatus;
import com.leadiq.imguruploader.repository.JobRepository;

public class JobExecutorTest extends ImguruploaderWebApplicationTests {

    @InjectMocks
    @Autowired
    private JobExecutor subject;

    @Autowired
    private JobRepository repository;

    @Test
    public void testExecuteJob() throws InterruptedException, ExecutionException {

	Set<String> urls = new HashSet<>();
	urls.add("dummyimageurl1");
	urls.add("dummyimageurl2");
	Job job = repository.createJob(urls);

	assertEquals(job.getStatus(), JobStatus.PENDING.getStatus());
	assertEquals(job.getCreated() != null, true);
	assertEquals(job.getFinished() == null, true);

	Future<Boolean> future = subject.executeJob(job.getId(), "dummyimageurl1");
	Job updatedJob = repository.fetchJobStatus(job.getId());
	assertEquals(updatedJob.getPending().size(), 2);

	future.get();
	assertEquals(updatedJob.getStatus(), JobStatus.INPROGRESS.getStatus());
	assertEquals(updatedJob.getPending().size(), 1);
	assertEquals(updatedJob.getFailed().size(), 1);
	assertEquals(job.getFinished() == null, true);

	future = subject.executeJob(job.getId(), "dummyimageurl2");

	future.get();
	updatedJob = repository.fetchJobStatus(job.getId());
	assertEquals(updatedJob.getStatus(), JobStatus.COMPLETE.getStatus());
	assertEquals(updatedJob.getPending().size(), 0);
	assertEquals(updatedJob.getFailed().size(), 2);
	assertEquals(updatedJob.getFinished() == null, false);
    }
}
