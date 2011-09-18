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
package org.ancoron.sudo.glassfish.test;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ancoron
 */
public class MyGroup implements Group {
    
    private Set<Principal> members = new HashSet<Principal>();
    private String name;
    
    public MyGroup(String name) {
        this.name = name;
    }

    @Override
    public boolean addMember(Principal user) {
        boolean added = false;
        
        if(!members.contains(user)) {
            members.add(user);
        }
        
        return added;
    }

    @Override
    public boolean removeMember(Principal user) {
        return members.remove(user);
    }

    @Override
    public boolean isMember(Principal member) {
        return members.contains(member);
    }

    @Override
    public Enumeration<? extends Principal> members() {
        return Collections.enumeration(members);
    }

    @Override
    public String getName() {
        return name;
    }
}
