package org.ablonewolf.threadLocals.security;

/**
 * Holder for the SecurityContext using ThreadLocal storage.
 */
public class SecurityContextHolder {

	private static final SecurityContext ANONYMOUS_CONTEXT = new SecurityContext(0, UserRole.ANONYMOUS);
	private static final ThreadLocal<SecurityContext> contextHolder = ThreadLocal.withInitial(() -> ANONYMOUS_CONTEXT);

	// package-private methods
	static void setContext(SecurityContext context) {
		contextHolder.set(context);
	}

	static void clear() {
		contextHolder.remove();
	}

	// public method
	public static SecurityContext getContext() {
		return contextHolder.get();
	}
}
