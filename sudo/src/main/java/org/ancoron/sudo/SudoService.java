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

import javax.security.auth.login.LoginException;

/**
 * This interface is describes all functionalities provided by an implementation.
 * 
 * <p>
 * Within an OSGi environment implementations are recommended to register a
 * service using this interface.
 * </p>
 *
 * @author ancoron
 */
public interface SudoService {

    /**
     * Do something by the means of a target user/identity.
     * 
     * @param c The {@link SudoAction} to execute
     * 
     * @return The return value of the business logic implemented by the given
     *          {@link SudoAction}
     * 
     * @throws LoginException if the authentication fails
     * @throws SudoExecutionException if the execution of 
     * {@link SudoAction#run() } fails
     */
    public <T> T sudo(final SudoAction<T> c) throws LoginException, SudoExecutionException;
}
