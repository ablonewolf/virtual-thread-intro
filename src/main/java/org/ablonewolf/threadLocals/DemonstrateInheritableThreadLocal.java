package org.ablonewolf.threadLocals;

import org.ablonewolf.util.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.UUID;

/**
 * Demonstrates the use of InheritableThreadLocal to propagate session tokens
 * to child threads, such as virtual threads used for service calls.
 */
public class DemonstrateInheritableThreadLocal {

	private static final Logger log = LoggerFactory.getLogger(DemonstrateInheritableThreadLocal.class);
	private static final ThreadLocal<String> sessionTokenHolder = new InheritableThreadLocal<>();

	static void main() {
		authFilter(DemonstrateInheritableThreadLocal::orderController);
		authFilter(DemonstrateInheritableThreadLocal::orderController);

		ThreadUtils.sleep(Duration.ofSeconds(1));
	}

	// WebFilter
	private static void authFilter(Runnable runnable) {
		try {
			var token = authenticate();
			sessionTokenHolder.set(token);
			// process the request
			runnable.run();
		} finally {
			sessionTokenHolder.remove();
		}
	}

	// Security
	private static String authenticate() {
		var token = UUID.randomUUID().toString();
		log.info("Generated session token: {}", token);
		return token;
	}

	// @Principal
	// POST /orders
	private static void orderController() {
		log.info("orderController: {}", sessionTokenHolder.get());
		orderService();
	}

	private static void orderService() {
		log.info("orderService: {}", sessionTokenHolder.get());
		Thread.ofVirtual().name("product-service-client").start(DemonstrateInheritableThreadLocal::callProductService);
		Thread.ofVirtual().name("inventory-service-client")
				.start(DemonstrateInheritableThreadLocal::callInventoryService);
	}

	// This is a client to call product service
	private static void callProductService() {
		log.info("calling product-service with header. Authorization: Bearer {}", sessionTokenHolder.get());
	}

	// This is a client to call inventory service
	private static void callInventoryService() {
		log.info("calling inventory-service with header. Authorization: Bearer {}", sessionTokenHolder.get());
	}
}
