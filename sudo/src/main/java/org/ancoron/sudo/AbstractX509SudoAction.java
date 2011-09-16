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
 * A basic abstract implementation for X509 certificate authenticated sudo.
 *
 * @author ancoron
 * 
 * @see X509Certificate
 * 
 * @since 1.0.1
 */
public abstract class AbstractX509SudoAction<T> implements SudoAction<T> {

    @Override
    public LoginType getType() {
        return LoginType.CLIENT_CERT;
    }

    @Override
    public Principal getCallerPrincipal() {
        return null;
    }

    @Override
    public Group[] getGroups() {
        return null;
    }

    @Override
    public Subject getSubject() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public char[] getPassword() {
        return null;
    }
}
