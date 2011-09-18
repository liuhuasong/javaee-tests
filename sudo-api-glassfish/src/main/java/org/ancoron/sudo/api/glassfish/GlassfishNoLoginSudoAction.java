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
package org.ancoron.sudo.api.glassfish;

import com.sun.enterprise.security.auth.login.DistinguishedPrincipalCredential;
import java.security.Principal;
import java.security.acl.Group;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.security.auth.Subject;
import org.ancoron.sudo.AbstractNoLoginSudoAction;

/**
 * An implementation for sudo without real authentication for Glassfish.
 * 
 * <p>
 * This implementation can be used with the Glassfish Application Server to 
 * use the sudo mechanism in a convenient manner. The only methods left to be 
 * implemented in an application are:
 * <ul>
 * <li>{@link #getCallerPrincipal() }</li>
 * <li>{@link #getGroups() }</li>
 * <li>{@link #getRealm() }</li>
 * </ul>
 * Additionally this implementation makes sure, that the application developer
 * doesn't have to deal with Glassfish specific internals and sets up the
 * {@link Subject} appropriately.
 * </p>
 * 
 * <p>
 * However, it is not possible to use custom {@link Group} implementations
 * inside the application. This is a limitation of the application server 
 * internals. Also the {@link Principal} provided by application code is not
 * something that you can rely on being returned as the exact same object as 
 * some other abstraction layers may replace it with their own implementation.
 * But at least this implementation makes sure that the provided {@link Principal}
 * is supplied as is to the application server.
 * </p>
 *
 * @author ancoron
 * 
 * @see Subject
 * 
 * @since 1.0.1
 */
public abstract class GlassfishNoLoginSudoAction<T> extends AbstractNoLoginSudoAction<T> {

    @Override
    public Subject getSubject() {
        Set<Principal> principals = new LinkedHashSet<Principal>();
        Set<Object> priv = new LinkedHashSet<Object>();
        Set<Object> pub = new LinkedHashSet<Object>();

        Principal p = getCallerPrincipal();
        principals.add(p);

        Group[] groups = getGroups();
        if(groups != null) {
            for(Group g : groups) {
                // this is the key - groups must be of type:
                // org.glassfish.security.common.Group
                org.glassfish.security.common.Group gg = new org.glassfish.security.common.Group(g.getName());
                principals.add(gg);
            }
        }

        // this is another key for the Glassfish-internal SecurityContext.initiator
        DistinguishedPrincipalCredential dpc = new DistinguishedPrincipalCredential(p);
        pub.add(dpc);

        return new Subject(false, principals, pub, priv);
    }
}
