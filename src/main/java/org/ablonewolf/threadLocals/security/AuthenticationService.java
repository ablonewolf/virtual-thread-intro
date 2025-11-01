package org.ablonewolf.threadLocals.security;

import java.util.Map;
import java.util.Objects;

/**
 * Service for authenticating users and executing actions within their security context.
 */
public class AuthenticationService {

	private static final String VALID_PASSWORD = "password";
	private static final Map<Integer, UserRole> USER_ROLES = Map.of(
			1, UserRole.ADMIN,
			2, UserRole.EDITOR,
			3, UserRole.VIEWER
	);

	public static void loginAndExecute(Integer userId, String password, Runnable runnable) {
		if (!Objects.equals(VALID_PASSWORD, password)) {
			throw new SecurityException("Invalid password for userId: " + userId);
		}

		try {
			var securityContext = new SecurityContext(userId, USER_ROLES.getOrDefault(userId, UserRole.ANONYMOUS));
			SecurityContextHolder.setContext(securityContext);
			runnable.run();
		} finally {
			SecurityContextHolder.clear();
		}
	}
}
