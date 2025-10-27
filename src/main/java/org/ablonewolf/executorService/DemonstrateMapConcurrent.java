package org.ablonewolf.executorService;

import org.ablonewolf.models.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

		List<ProductDTO> productList = new ArrayList<>();

		productList = IntStream.rangeClosed(1, 500000)
				.boxed()
				.gather(Gatherers.mapConcurrent(8, aggregatorService::getProductInfo))
				.toList();

		/*for (int i = 1; i <= 500000; i++) {
			var productInfo = aggregatorService.getProductInfo(i);
			productList.add(productInfo);
		}*/

		LocalDateTime endTime = LocalDateTime.now();

		for (var product : productList) {
			log.info("product info: {}", product);
		}

		log.info("Total duration: {} seconds", java.time.Duration.between(startTime, endTime).toSeconds());
	}
}
