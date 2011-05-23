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

package org.ancoron.javaee.sudo;

import com.sun.enterprise.security.SecurityContext;
import com.sun.enterprise.security.auth.login.common.PasswordCredential;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author ancoron
 */
public class SudoService {

    public <T> T sudo(final SudoAction<T> c) throws LoginException {
        Set<Object> credsPrivate = new HashSet<Object>();
        
        // this is just for a simple password test (could also be cert stuff)
        PasswordCredential pwd = new PasswordCredential(
                c.getUsername(),
                c.getPassword(),
                c.getRealm());

        credsPrivate.add(pwd);

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

            // execute whatever you want...
            return c.run();
        } finally {
            // restore the original security context to not break anything...
            SecurityContext.setCurrent(secCtx);
        }
    }
}
