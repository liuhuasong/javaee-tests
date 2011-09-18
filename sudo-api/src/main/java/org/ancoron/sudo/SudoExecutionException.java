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

/**
 * This exception denotes that something went wrong during execution of a given
 * {@link SudoAction#run() } implementation.
 * 
 * <p>
 * The original exception is preserved and available using {@link #getCause() }.
 * </p>
 *
 * @author ancoron
 * 
 * @since 1.0.1
 */
public class SudoExecutionException extends Exception {

    /**
     * Creates a new instance of <code>SudoExecutionException</code> without detail message.
     */
    public SudoExecutionException() {
        this(null, "The requested SUDO action failed", null);
    }

    /**
     * Constructs an instance of <code>SudoExecutionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SudoExecutionException(String msg) {
        this(null, msg, null);
    }

    /**
     * Constructs an instance of <code>SudoExecutionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SudoExecutionException(String msg, Throwable cause) {
        this(null, msg, cause);
    }

    /**
     * Constructs an instance of <code>SudoExecutionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SudoExecutionException(Principal sudoer, String msg) {
        this(sudoer, msg, null);
    }

    /**
     * Constructs an instance of <code>SudoExecutionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SudoExecutionException(Principal sudoer, String msg, Throwable cause) {
        this(sudoer, null, msg, cause);
    }

    /**
     * Constructs an instance of <code>SudoExecutionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SudoExecutionException(Principal sudoer, Throwable cause) {
        this(sudoer, null, null, cause);
    }

    /**
     * Constructs an instance of <code>SudoExecutionException</code> with the specified detail message.
     *
     * @param sudoer the {@link Principal} of the sudo'er that invoked the sudo
     * @param action the {@link SudoAction} that has been tried
     * @param msg the detail message
     * @param cause the original exception that was thrown by executing the sudo
     */
    public SudoExecutionException(Principal sudoer, SudoAction action, String msg, Throwable cause) {
        super(msg, cause);
        this.sudoer = sudoer;
        this.action = action;
    }

    private Principal sudoer;

    /**
     * Get the value of sudoer
     *
     * @return the value of sudoer
     */
    public Principal getSudoer() {
        return sudoer;
    }

    /**
     * Set the value of sudoer
     *
     * @param sudoer new value of sudoer
     */
    public void setSudoer(Principal sudoer) {
        this.sudoer = sudoer;
    }

    private SudoAction action;

    /**
     * Get the value of action
     *
     * @return the value of action
     */
    public SudoAction getAction() {
        return action;
    }

    /**
     * Set the value of action
     *
     * @param action new value of action
     */
    public void setAction(SudoAction action) {
        this.action = action;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        
        if(sudoer != null) {
            msg = "The SUDO action requested by " + sudoer.getName() + " failed";
        } else {
            msg = "The requested SUDO action failed";
        }
        
        return msg;
    }

}
