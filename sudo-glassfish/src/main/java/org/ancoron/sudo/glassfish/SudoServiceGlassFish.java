/*
 * Copyright 2011 ancoron.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ancoron.sudo.glassfish;

import org.ancoron.sudo.SudoAction;
import com.sun.enterprise.security.SecurityContext;
import com.sun.enterprise.security.auth.login.common.PasswordCredential;
import com.sun.enterprise.security.auth.login.common.X509CertificateCredential;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.ancoron.sudo.SudoService;

/**
 * This class executes a given {@link SudoAction}.
 *
 * @author ancoron
 */
public class SudoServiceGlassFish implements SudoService {
    
    private Logger log = Logger.getLogger(SudoService.class.getName());

    @Override
    public <T> T sudo(final SudoAction<T> c) throws LoginException {
        Set<Object> credsPrivate = new HashSet<Object>();
        
        
        switch(c.getType()) {
            case USERNAME_PASSWORD: {
                if(log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "[SUDO] Request: {0} (username={1}, password=********, realm={2})",
                            new Object[]{c.getType().name(), c.getUsername(), c.getRealm()});
                }
                PasswordCredential pwd = new PasswordCredential(
                        c.getUsername(),
                        c.getPassword(),
                        c.getRealm());

                credsPrivate.add(pwd);
                break;
            }
            case CLIENT_CERT: {
                if(log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "[SUDO] Request: {0} (cert-chain-length={1}, alias={2}, realm={3})",
                            new Object[]{c.getType().name(), c.getCertChain().length, c.getAlias(), c.getRealm()});
                }
                X509CertificateCredential cert = new X509CertificateCredential(
                        c.getCertChain(),
                        c.getAlias(),
                        c.getRealm());

                credsPrivate.add(cert);
                break;
            }
            default:
                throw new IllegalArgumentException(
                        "[SUDO] Illegal return value for SudoAction.getType() --> "
                        + c.getType());
        }

        // note the "final" here...
        final Subject s = new Subject(false,
                new HashSet<Principal>(),
                new HashSet<Object>(),
                credsPrivate);

        // this issues the real login and fills the subject with user roles...
        LoginContext lc = new LoginContext(c.getRealm(), s);
        lc.login();

        // save the current security context...
        final SecurityContext secCtx = SecurityContext.getCurrent();

        try {
            // establish a new security context for the "sudo"...
            final SecurityContext newCtx = AccessController.doPrivileged(
                    new PrivilegedAction<SecurityContext>() {

                        @Override
                        public SecurityContext run() {
                            return new SecurityContext(c.getUsername(), s);
                        }
                    });
            SecurityContext.setCurrent(newCtx);
            if(log.isLoggable(Level.FINE)) {
                log.fine("[SUDO] New SecurityContext established");
            }

            // execute whatever you want...
            return c.run();
        } finally {
            // restore the original security context to not break anything...
            SecurityContext.setCurrent(secCtx);
            if(log.isLoggable(Level.FINE)) {
                log.fine("[SUDO] Original SecurityContext restored");
            }
        }
    }
}
