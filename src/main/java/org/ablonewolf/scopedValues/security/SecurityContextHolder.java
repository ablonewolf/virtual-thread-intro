package org.ablonewolf.scopedValues.security;

public class SecurityContextHolder {

	private static final SecurityContext ANONYMOUS_CONTEXT = new SecurityContext(0, UserRole.ANONYMOUS);
	private static final ScopedValue<SecurityContext> CONTEXT = ScopedValue.newInstance();

	static ScopedValue<SecurityContext> getScopedValue() {
		return CONTEXT;
	}

	public static SecurityContext getContext() {
		return CONTEXT.orElse(ANONYMOUS_CONTEXT);
	}
}
