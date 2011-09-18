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

package org.ancoron.sudo;

import java.security.Principal;
import java.security.acl.Group;
import java.security.cert.X509Certificate;
import javax.security.auth.Subject;

/**
 * This interface is designed to enable security session switching between users.
 *
 * <p>Implementations of this interface must ensure that all required data is
 * available. This depends on the return value of {@link #getType() }:</p>
 * 
 * <ol>
 * <li>
 * <p>For a username/password login ({@link LoginType#USERNAME_PASSWORD}) method
 * this will be the following:
 * 
 * <ul>
 * <li>{@link #getUsername() }</li>
 * <li>{@link #getPassword() }</li>
 * </ul>
 * </p>
 * </li>
 * <li>
 * <p>For client certificate authentication ({@link LoginType#USERNAME_PASSWORD})
 * the following will be used:
 * 
 * <ul>
 * <li>{@link #getCertChain() }</li>
 * <li>{@link #getAlias() }</li>
 * </ul>
 * </p>
 * </li>
 * <li>
 * <p>For something without authentication ({@link LoginType#NO_LOGIN})
 * the following will be used:
 * 
 * <ul>
 * <li>{@link #getSubject() }</li>
 * </ul>
 * </p>
 * </li>
 * </ol>
 * 
 * <p>
 * However, for the first two cases you may return something that is meaningful
 * to you using the following methods:
 * <ul>
 * <li>{@link #getCallerPrincipal() }</li>
 * <li>{@link #getGroups() }</li>
 * </ul>
 * </p>
 * 
 * @author ancoron
 */
public interface SudoAction<T> {
    
    /**
     * The authentication scheme to be used for this action.
     * 
     * @return The authentication scheme
     */
    public LoginType getType();

    /**
     * The business logic that is to be executed as the authenticated user /
     * principal.
     * 
     * @return The result of the business logic
     * 
     * @throws Exception Any exception is allowed here
     */
    public T run() throws Exception;

    /**
     * The username to authenticate.
     * 
     * @return The username
     */
    public String getUsername();

    /**
     * The clear-text password for authentication.
     * 
     * @return The clear-text password
     */
    public char[] getPassword();

    /**
     * The JAAS realm to authenticate against.
     * 
     * @return The name of the target JAAS realm
     */
    public String getRealm();

    /**
     * The JAAS context to login to.
     * 
     * @return The name of the target JAAS context
     * 
     * @since 1.0.1
     */
    public String getContext();

    /**
     * The certificate chain to use for authentication.
     * 
     * @return The certificate chain
     */
    public X509Certificate[] getCertChain();

    /**
     * The alias of the certificate to use for authentication.
     * 
     * @return The name of the certificate alias
     */
    public String getAlias();
    
    /**
     * A custom principal to use as the "caller" {@link Principle}.
     * 
     * Usually the first principal inside a {@link Subject} is used as the 
     * calling principal inside application containers. So you can specify one
     * here to be set into the created {@link Subject}.
     * 
     * @return The {@link Principal} to be used as the calling one
     * 
     * @since 1.0.1
     */
    public Principal getCallerPrincipal();

    /**
     * The {@link Group}s to use for the {@link Subject}.
     * 
     * In case this method returns not <tt>null</tt> all containing groups are
     * made available preserving the order inside the established Subject.
     * 
     * @return A non-empty array of {@link Group} instances
     * 
     * @since 1.0.1
     */
    public Group[] getGroups();

    /**
     * The {@link Subject} that should be used instead of creating a new one.
     * 
     * This has to be prefilled for the requirements of the application server.
     * 
     * Furthermore in case this method returns anything other than <tt>null</tt>
     * it is being used as is and the following methods are not going to be used:
     * <ul>
     * <li>{@link #getUsername() }</li>
     * <li>{@link #getPassword() }</li>
     * <li>{@link #getCertChain() }</li>
     * <li>{@link #getAlias() }</li>
     * <li>{@link #getCallerPrincipal() }</li>
     * <li>{@link #getGroups() }</li>
     * </ul>
     * 
     * @return The {@link Subject} used for authentication and authorization
     * 
     * @since 1.0.1
     */
    public Subject getSubject();
}
