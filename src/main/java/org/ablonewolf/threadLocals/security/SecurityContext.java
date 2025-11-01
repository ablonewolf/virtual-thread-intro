package org.ablonewolf.threadLocals.security;

/**
 * Security context holding user identity and role information.
 *
 * @param userId the unique identifier of the user
 * @param role   the role assigned to the user
 */
public record SecurityContext(Integer userId,
							  UserRole role) {

	public boolean hasPermission(UserRole requiredRole) {
		return this.role.ordinal() <= requiredRole.ordinal();
	}
}
