package com.leadiq.imguruploader.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.leadiq.imguruploader.ImguruploaderWebApplicationTests;
import com.leadiq.imguruploader.model.Job;
import com.leadiq.imguruploader.model.JobStatus;

public class JobExecutorTest extends ImguruploaderWebApplicationTests {

    @InjectMocks
    @Autowired
    private JobExecutor subject;

    @Test
    public void testExecuteJob() throws InterruptedException, ExecutionException {
	ConcurrentMap<String, Job> jobMap = new ConcurrentHashMap<>();
	Set<String> urls = new HashSet<>();
	urls.add("dummyimageurl1");
	urls.add("dummyimageurl2");
	Job job = new Job("jobid", new Date(), urls);
	jobMap.put(job.getId(), job);
	assertEquals(job.getStatus(), JobStatus.PENDING.getStatus());

	Future<Boolean> future = subject.executeJob(jobMap, "jobid", "dummyimageurl1");
	assertEquals(jobMap.get("jobid").getPending().size(), 2);

	future.get();
	assertEquals(jobMap.get("jobid").getStatus(), JobStatus.INPROGRESS.getStatus());
	assertEquals(jobMap.get("jobid").getPending().size(), 1);
	assertEquals(jobMap.get("jobid").getFailed().size(), 1);

	future = subject.executeJob(jobMap, "jobid", "dummyimageurl2");
	assertEquals(jobMap.get("jobid").getPending().size(), 1);

	future.get();
	assertEquals(jobMap.get("jobid").getStatus(), JobStatus.COMPLETE.getStatus());
	assertEquals(jobMap.get("jobid").getPending().size(), 0);
	assertEquals(jobMap.get("jobid").getFailed().size(), 2);
    }
}
