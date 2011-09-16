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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJBAccessException;
import javax.ejb.embeddable.EJBContainer;
import javax.security.auth.Subject;
import org.ancoron.sudo.AbstractNoLoginSudoAction;
import org.ancoron.sudo.AbstractPasswordSudoAction;
import org.ancoron.sudo.SudoAction;
import org.ancoron.sudo.SudoService;
import org.ancoron.sudo.glassfish.SudoServiceGlassFish;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ancoron
 */
public class SecuredEJBTest {
    
    private static EJBContainer container;
    
    public SecuredEJBTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Map props = new HashMap();
        
        File gfInstallDir = new File(System.getProperty("basedir") + "/target/glassfish");
        props.put("org.glassfish.ejb.embedded.glassfish.installation.root",
                gfInstallDir.getAbsolutePath());
        props.put("org.glassfish.ejb.embedded.glassfish.instance.root",
                gfInstallDir.getAbsolutePath() + "/domains/domain1");
        props.put("org.glassfish.ejb.embedded.glassfish.configuration.file",
                gfInstallDir.getAbsolutePath() + "/domains/domain1/config/domain.xml");
        props.put("org.glassfish.ejb.embedded.glassfish.keep-temporary-files",
                "true");
        
        container = EJBContainer.createEJBContainer(props);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        if(container != null) {
            container.close();
        }
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of sayHello method, of class SecuredEJB.
     */
    @Test
    public void testSayHelloPre() throws Exception {
        System.out.println("sayHello");

        String name = "bob";

        SecuredEJB instance = (SecuredEJB) container.getContext().lookup("java:global/sudo/glassfish/SecuredEJB");

        try {
            String result = instance.sayHello(name);

            fail("The security restrictions have not been applied.");
        } catch(EJBAccessException x) {
            System.out.println("Plain EJB security test OK: " + x.getMessage());
        }
    }

    @Test
    public void testSayHello() throws Exception {
        System.out.println("sayHello");

        final String name = "bob";

        SudoAction<String> action = new AbstractPasswordSudoAction<String>() {

            @Override
            public String run() throws Exception {
                SecuredEJB instance = (SecuredEJB) container.getContext().lookup("java:global/sudo/glassfish/SecuredEJB");
                
                return instance.sayHello(name);
            }

            @Override
            public String getUsername() {
                return "alice";
            }

            @Override
            public char[] getPassword() {
                return "test".toCharArray();
            }

            @Override
            public String getRealm() {
                return "file";
            }

            @Override
            public String getContext() {
                return "fileRealm";
            }
        };

        SudoService sudo = new SudoServiceGlassFish();
        String result = sudo.sudo(action);

        System.out.println("SUDO (username/password) invocation OK: " + result);
    }
}
