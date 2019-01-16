package com.example.springflowable;

import lombok.extern.log4j.Log4j2;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/*
	Based on Spring Tips: Business Process Management with Flowable
	https://www.youtube.com/watch?v=43_OLrxU3so
*/

/*	TODO-02 processes folder & Flowable modeler
		- You can create a 'processes' folder under resources to put your process definintions and they will automatically being deployed
		to the process engine. There's already a process definition defined. By convention every xml file must end with '.bmpn20.xml'.

		- BPMN is not the easiest language to write your processes by hand, but there's a graphical modeler available that you can use

		- Download flowable as a zip file from https://www.flowable.org/downloads.html and start both the idm & modeler executable wars:
			\flowable-6.4.1\wars\java -jar flowable-idm.war (API, runs on :8080)
			\flowable-6.4.1\wars\java -jar flowable-modeler.war (visual modeler, runs on :8888)

		- Go to http://localhost:8888/flowable-modeler to open the modeler and login with admin/test
		- Import the signup-process.bpmn20.xml file into the modeler to get a graphical representation of your process flow
 */
@SpringBootApplication
public class SpringFlowableApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFlowableApplication.class, args);
	}

}