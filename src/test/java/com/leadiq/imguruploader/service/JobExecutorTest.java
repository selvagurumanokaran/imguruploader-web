package com.leadiq.imguruploader.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.leadiq.imguruploader.ImguruploaderWebApplicationTests;
import com.leadiq.imguruploader.model.Job;

public class JobExecutorTest extends ImguruploaderWebApplicationTests {

    @InjectMocks
    @Autowired
    private JobExecutor subject;

    @Ignore
    @Test
    public void testExecuteJob() throws InterruptedException {
	ConcurrentMap<String, Job> jobMap = new ConcurrentHashMap<>();
	Set<String> urls = new HashSet<>();
	urls.add("https://www.gstatic.com/webp/gallery3/2.png");
	Job job = new Job("jobid", new Date(), urls);
	jobMap.put(job.getId(), job);

	subject.executeJob(jobMap, "jobid", "https://www.gstatic.com/webp/gallery3/2.png");

	assertEquals(jobMap.get("jobid").getPending().size(), 1);
	Thread.sleep(1000 * 10);
	assertEquals(jobMap.get("jobid").getPending().size(), 0);
    }
}
