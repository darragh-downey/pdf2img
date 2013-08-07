/**
 * 
 */
package com.ddowney.plugins.pdf2img.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author ddowney
 *
 */
public class ThumbGen implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		
		throw new JobExecutionException("Testing Exception");
	}

}
