package org.ablonewolf.threadLocals;

import lombok.RequiredArgsConstructor;
import org.ablonewolf.threadLocals.security.SecurityContext;
import org.ablonewolf.threadLocals.security.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * REST controller for document operations with role-based access control.
 */
@RequiredArgsConstructor
public class DocumentRestController {

	private static final Logger log = LoggerFactory.getLogger(DocumentRestController.class);
	private final Supplier<SecurityContext> securityContextSupplier;

	//@hasRole(VIEWER)
	public void read() {
		this.validateUserRole(UserRole.VIEWER);
		log.info("Reading document...");
	}

	//@hasRole(EDITOR)
	public void edit() {
		this.validateUserRole(UserRole.EDITOR);
		log.info("Editing document...");
	}

	//@hasRole(ADMIN)
	public void delete() {
		this.validateUserRole(UserRole.ADMIN);
		log.info("Deleting document...");
	}

	private void validateUserRole(UserRole requiredRole) {
		var securityContext = this.securityContextSupplier.get();
		if (!securityContext.hasPermission(requiredRole)) {
			log.error("user {} does not have {} permission", securityContext.userId(), requiredRole);
			throw new SecurityException("Unauthorized access, Required role: " + requiredRole);
		}
	}
}
