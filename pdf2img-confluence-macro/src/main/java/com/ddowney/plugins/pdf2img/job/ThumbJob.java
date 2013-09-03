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
public class ThumbJob implements Job {

	//run thumbnail generator
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("Hello World");
		//job start
		//read txt file get attachment names + attached page ID
		//search confluence for attachments + filter out the unsupported file types
		//compare the two lists
		//'convert' the unique attachments from the 'confluence' list
		//write to the file the newly 'converted' attachment names.
		//job done
		
		throw new JobExecutionException("Testing Exception");
	}

}
