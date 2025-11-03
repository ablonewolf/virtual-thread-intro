package org.ablonewolf.scopedValues;

import org.ablonewolf.scopedValues.security.AuthenticationService;
import org.ablonewolf.scopedValues.security.SecurityContextHolder;

import org.ablonewolf.util.ThreadUtils;

import java.time.Duration;

public class DocumentAccessWithScopedValues {

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
