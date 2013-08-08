package com.ddowney.plugins.pdf2img.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.atlassian.quartz.jobs.AbstractJob;

public class GenJob extends AbstractJob {

	@Override
	public void doExecute(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("ouch...");
	}

}
