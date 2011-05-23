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

import java.security.cert.X509Certificate;

/**
 * This interface is designed to enable security session switching between users.
 *
 * <p>Implementations of this interface must ensure that all required data is
 * available. This depends on the return value of {@link #getType() }:</p>
 * 
 * <p>For a username/password login ({@link LoginType#USERNAME_PASSWORD}) method
 * this will be the following:
 * 
 * <ul>
 * <li>{@link #getUsername() }</li>
 * <li>{@link #getPassword() }</li>
 * </ul>
 * </p>
 * 
 * <p>For client certificate authentication ({@link LoginType#USERNAME_PASSWORD})
 * the following will be used:
 * 
 * <ul>
 * <li>{@link #getCertChain() }</li>
 * <li>{@link #getAlias() }</li>
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
     */
    public T run();

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
}
