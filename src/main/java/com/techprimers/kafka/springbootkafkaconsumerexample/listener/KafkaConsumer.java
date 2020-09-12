package com.techprimers.kafka.springbootkafkaconsumerexample.listener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techprimers.kafka.springbootkafkaconsumerexample.model.User;

@Service
public class KafkaConsumer {

	String outputFilePath = "C:\\Users\\AnupS\\Downloads\\NYSOH_FunctionalKT_test.mp4";
	final int BUFFERSIZE = 1000000;//40 * 1024;
	

	List<Integer> fileBytes = new ArrayList<Integer>();

	@KafkaListener(topics = "Kafka_Example", group = "group_id", containerFactory = "userKafkaListenerFactory")
	public void consume(User message) throws JsonProcessingException, IOException {
		//System.out.println("Consumed message: " + message);
		//System.out.println("buffer received: " + message.getSalary());

		// ObjectMapper mapper = new ObjectMapper();
		// JsonNode actualObj = mapper.readTree(message);
		// JsonNode jsonNode1 = actualObj.get("salary");

		// System.out.println("buffer receivedc" + jsonNode1.intValue());

		if (message.getSalary() > 0) {
			System.out.println("sequence received "+message.getDept());
			fileBytes.add(message.getSalary());

		} else {
			try (FileOutputStream fout = new FileOutputStream(new File(outputFilePath));) 
			{
				byte[] buffer = new byte[BUFFERSIZE];
				System.out.println("filebytes : "+fileBytes);
				for (int i = 0; i < fileBytes.size(); i++) {
					 System.out.println(""+i+"items :" + fileBytes.get(i) + "index:"+ fileBytes.indexOf(fileBytes.get(i)));
				    fout.write(buffer, 0, fileBytes.get(i));
				}
				/*
				 * for (Integer item : fileBytes) { System.out.println("items :" + item +
				 * "count"+ fileBytes.indexOf(item)); fout.write(buffer, 0, item);
				 * 
				 * }
				 */
				fileBytes.clear();

			} catch (Exception e) {
				System.out.println("Something went wrong! Reason: " + e.getMessage());
			}
		}

	}

	@KafkaListener(topics = "Kafka_Example_json", group = "group_json", containerFactory = "userKafkaListenerFactory")
	public void consumeJson(User user) {
		System.out.println("Consumed JSON Message: " + user);
	}
}
