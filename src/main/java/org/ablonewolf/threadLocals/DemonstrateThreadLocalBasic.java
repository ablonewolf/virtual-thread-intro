package org.ablonewolf.threadLocals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * A basic demonstration of ThreadLocal usage to hold session tokens in a web application context.
 */
public class DemonstrateThreadLocalBasic {

	private static final Logger log = LoggerFactory.getLogger(DemonstrateThreadLocalBasic.class);
	private static final ThreadLocal<String> sessionTokenHolder = new ThreadLocal<>();

	static void main() {
		authFilter(DemonstrateThreadLocalBasic::orderController);
		authFilter(DemonstrateThreadLocalBasic::orderController);
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
		callProductService();
		callInventoryService();
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
