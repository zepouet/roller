package org.rollercrash;

import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.UserManager;
import org.apache.roller.weblogger.business.Weblogger;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.config.WebloggerConfig;
import org.apache.roller.weblogger.pojos.GlobalPermission;
import org.apache.roller.weblogger.pojos.User;
import org.apache.roller.weblogger.util.Utilities;
import org.apache.roller.weblogger.webservices.adminprotocol.InternalException;
import org.apache.roller.weblogger.webservices.adminprotocol.NotFoundException;
import org.apache.roller.weblogger.webservices.adminprotocol.UnauthorizedException;
import org.crsh.auth.AuthenticationPlugin;
import org.crsh.plugin.CRaSHPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Roller authentication for CRaSH. Most of the code is copied from {@link org.apache.roller.weblogger.webservices.adminprotocol.Authenticator}.
 *
 * @author Julien Viet
 */
public class RollerAuthenticationPlugin extends CRaSHPlugin<AuthenticationPlugin> implements AuthenticationPlugin<String> {

  public String getName() {
    return "rollercrash";
  }

  public Class<String> getCredentialType() {
    return String.class;
  }

  @Override
  public AuthenticationPlugin getImplementation() {
    return this;
  }

  private Weblogger getRoller() {
    return WebloggerFactory.getWeblogger();
  }

  public boolean authenticate(String username, String credential) throws Exception {
    try {
      User ud = getUserData(username);
      String realpassword = ud.getPassword();

      boolean encrypted = Boolean.valueOf(WebloggerConfig.getProperty("passwds.encryption.enabled"));
      if (encrypted) {
        credential = Utilities.encodePassword(credential, WebloggerConfig.getProperty("passwds.encryption.algorithm"));
      }

      if (!username.trim().equals(ud.getUserName())) {
        throw new UnauthorizedException("ERROR: User is not authorized: " + username);
      }
      if (!credential.trim().equals(realpassword)) {
        throw new UnauthorizedException("ERROR: User is not authorized: " + username);
      }
      List<String> adminActions = new ArrayList<String>();
      adminActions.add("admin");
      GlobalPermission adminPerm = new GlobalPermission(ud, adminActions);
      if (!WebloggerFactory.getWeblogger().getUserManager().checkPermission(adminPerm, ud)) {
        throw new UnauthorizedException("ERROR: User must have the admin role to use the RAP endpoint: " + username);
      }
      if (!ud.getEnabled()) {
        throw new UnauthorizedException("ERROR: User is disabled: " + username);
      }
    } catch (WebloggerException ex) {
      throw new UnauthorizedException("ERROR: User must have the admin role to use the RAP endpoint: " + username);
    }
    return true;
  }

  protected User getUserData(String name) throws NotFoundException, InternalException {
    try {
      UserManager mgr = getRoller().getUserManager();
      User ud = mgr.getUserByUserName(name, Boolean.TRUE);
      if (ud == null) {
        ud = mgr.getUserByUserName(name, Boolean.FALSE);
      }
      if (ud == null) {
        throw new NotFoundException("ERROR: Unknown user: " + name);
      }

      return ud;
    } catch (WebloggerException re) {
      throw new InternalException("ERROR: Could not get user: " + name, re);
    }
  }
}
