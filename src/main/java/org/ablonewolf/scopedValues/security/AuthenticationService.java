package org.ablonewolf.scopedValues.security;

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
		var securityContext = new SecurityContext(userId, USER_ROLES.getOrDefault(userId, UserRole.ANONYMOUS));
		ScopedValue.where(SecurityContextHolder.getScopedValue(), securityContext)
				.run(runnable);

	}

	public static void runAsAdmin(Runnable runnable) {
		var securityContext = SecurityContextHolder.getScopedValue()
				.orElseThrow(() -> new SecurityException("Invalid security context"));
		var elevatedContext = new SecurityContext(securityContext.userId(), UserRole.ADMIN);
		ScopedValue.where(SecurityContextHolder.getScopedValue(), elevatedContext)
				.run(runnable);
	}
}
