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

/**
 * The login type specified for a specific {@link SudoAction} implementation.
 *
 * @author ancoron
 */
public enum LoginType {

    /**
     * Designates the {@link SudoAction} to authenticate using a username/password
     * scheme.
     */
    USERNAME_PASSWORD,
    
    /**
     * Designates the {@link SudoAction} to authenticate using a client certificate
     * scheme.
     */
    CLIENT_CERT,
    
    /**
     * Designates the {@link SudoAction} to <b>not</b> authenticate.
     * 
     * @since 1.0.1
     */
    NO_LOGIN;
}
