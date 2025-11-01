package org.ablonewolf.threadLocals;

import org.ablonewolf.threadLocals.security.AuthenticationService;
import org.ablonewolf.threadLocals.security.SecurityContextHolder;
import org.ablonewolf.util.ThreadUtils;

import java.time.Duration;

/**
 * Demonstrates document access control using ThreadLocal security contexts.
 */
public class DocumentAccessWithThreadLocal {

	private static final DocumentRestController documentController =
			new DocumentRestController(SecurityContextHolder::getContext);

	static void main() {
		Thread.ofVirtual().name("admin").start(() -> documentAccessWorkflow(1, "password"));
		Thread.ofVirtual().name("editor").start(() -> documentAccessWorkflow(2, "password"));
		Thread.ofVirtual().name("viewer").start(() -> documentAccessWorkflow(3, "password"));
		Thread.ofVirtual().name("anonymous").start(() -> documentAccessWorkflow(4, "password"));

		ThreadUtils.sleep(Duration.ofSeconds(3));
	}

	private static void documentAccessWorkflow(Integer userId, String password) {
		AuthenticationService.loginAndExecute(userId, password, () -> {
			documentController.read();
			documentController.edit();
			documentController.delete();
		});
	}
}
