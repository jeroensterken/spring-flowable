package com.example.springflowable;

import com.example.springflowable.services.EmailService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.mail.Email;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* TODO-04 interacting with the signup-process flow
	run the signupProcess test, you should see some console output. Now go over the test code in detail
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class SpringFlowableApplicationTests {
	private static final String CUSTOMER_ID_PV = "customerId";
	private static final String EMAIL_PV = "email";

	public static final String SIGNUP_PROCESS = "signup-process";
	public static final String CONFIRM_EMAIL_TASK = "confirm-email-task";

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private EmailService emailService;

	@Test
	public void signupProcess() throws Exception {
		String customerId = "1";
		String customerEmail = "email@email.com";

		String processInstanceId = this.beginCustomerEnrollmentProcess(customerId, customerEmail);
		Assert.assertNotNull("** The process instance ID should not be null", processInstanceId);

		log.info("** process instance ID: " + processInstanceId);

		// get outstanding tasks
		List<Task> outstandingTasks = this.taskService
				.createTaskQuery()
				.taskDefinitionKey(CONFIRM_EMAIL_TASK)
				.includeProcessVariables()
				.processVariableValueEquals(CUSTOMER_ID_PV, customerId)
				.list();

		Assert.assertEquals("** There should be 1 outstanding task", 1, outstandingTasks.size());

		Task task = outstandingTasks.get(0);
		log.info("** task.getId() : " + task.getId() +
				" / task.getTaskDefinitionId(): " + task.getTaskDefinitionId() +
				" / task.getTaskDefinitionKey(): " + task.getTaskDefinitionKey() +
				" / task.getName: " + task.getName() +
				" / task.getProcessDefinitionId(): " + task.getProcessDefinitionId() +
				" / task.getProcessInstanceId(): " + task.getProcessInstanceId()
		);

		/*
			task.getId() : 29275eba-1998-11e9-b775-9cb6d0f84e13
			task.getTaskDefinitionId(): null
			task.getTaskDefinitionKey(): confirm-email-task
			task.getName: confirm-email-task name

			task.getProcessDefinitionId(): signup-process:1:0c374450-1999-11e9-a2d1-9cb6d0f84e13
			task.getProcessInstanceId(): 0c4af361-1999-11e9-a2d1-9cb6d0f84e13
		*/

		// complete outstanding tasks
		outstandingTasks.forEach(t -> {
					this.taskService.claim(t.getId(), "sterkje");
					this.taskService.complete(t.getId());
				}
		);

		// confirm that the email has been sent
		Assert.assertEquals(this.emailService.sends.get(customerEmail).get(), 1);
	}

	String beginCustomerEnrollmentProcess (String customerId, String email) {
		Map<String, Object> vars = new HashMap<>();
		vars.put(CUSTOMER_ID_PV, customerId);
		vars.put(EMAIL_PV, email);

		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(SIGNUP_PROCESS, vars);
		return processInstance.getProcessInstanceId();
	}

}