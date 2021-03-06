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

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 *
 * @author ancoron
 */
@Stateless
@EJB(name="java:global/sudo/glassfish/SecuredEJB", beanInterface=SecuredEJB.class)
@Local
public class SecuredEJBImpl implements SecuredEJB {
    
    @Resource
    private EJBContext context;

    @RolesAllowed("testGroup")
    @Override
    public String sayHello(String name) {
        return "Hello " + name + " from " + context.getCallerPrincipal().getName() + "!";
    }
}
