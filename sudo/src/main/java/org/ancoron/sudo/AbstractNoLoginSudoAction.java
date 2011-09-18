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
 * A basic abstract implementation for sudo without real authentication.
 *
 * @author ancoron
 * 
 * @see Subject
 * 
 * @since 1.0.1
 */
public abstract class AbstractNoLoginSudoAction<T> implements SudoAction<T> {

    @Override
    public LoginType getType() {
        return LoginType.NO_LOGIN;
    }

    @Override
    public X509Certificate[] getCertChain() {
        return null;
    }

    @Override
    public String getAlias() {
        return null;
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
    public String getUsername() {
        return null;
    }

    @Override
    public char[] getPassword() {
        return null;
    }

    @Override
    public String getContext() {
        return getRealm();
    }
}
