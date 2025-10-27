package org.ablonewolf.executorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;

public class DemonstrateMapConcurrent {

	private static final Logger log = LoggerFactory.getLogger(DemonstrateMapConcurrent.class);

	static void main() {
		var virtualThreadFactory = Thread.ofVirtual().name("Virtual Thread - ", 1).factory();
		var executorService = Executors.newThreadPerTaskExecutor(virtualThreadFactory);
		var aggregatorService = new AggregatorService(executorService);

		LocalDateTime startTime = LocalDateTime.now();

		var productList = IntStream.rangeClosed(1, 500000)
				.boxed()
				.gather(Gatherers.mapConcurrent(8, aggregatorService::getProductInfo))
				.toList();

		LocalDateTime endTime = LocalDateTime.now();

		for (var product : productList) {
			log.info("product info: {}", product);
		}

		log.info("Total duration: {} seconds", java.time.Duration.between(startTime, endTime).toSeconds());
	}
}
