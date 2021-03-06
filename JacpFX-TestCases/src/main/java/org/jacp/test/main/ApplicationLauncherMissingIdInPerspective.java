/************************************************************************
 *
 * Copyright (C) 2010 - 2014
 *
 * [ApplicationLauncher.java]
 * JACPFX Project (https://github.com/JacpFX/JacpFX/)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 *
 ************************************************************************/
package org.jacp.test.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jacp.test.workbench.WorkbenchMissingIdInPerspective;
import org.jacpfx.rcp.workbench.FXWorkbench;
import org.jacpfx.spring.launcher.AFXSpringXmlLauncher;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

/**
 * The application launcher containing the main method
 *
 * @author <a href="mailto:amo.ahcp@gmail.com"> Andy Moncsek</a>
 */
public class ApplicationLauncherMissingIdInPerspective extends AFXSpringXmlLauncher {
    private static final Logger log = Logger.getLogger(ApplicationLauncherMissingIdInPerspective.class
            .getName());
    public static final String[] STYLES = new String[2];
    public static CountDownLatch latch = new CountDownLatch(1);
    public static volatile ApplicationLauncherMissingIdInPerspective[] instance = new ApplicationLauncherMissingIdInPerspective[1];

    public ApplicationLauncherMissingIdInPerspective() {
    }

    public ApplicationLauncherMissingIdInPerspective(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public String getXmlConfig() {
        return "main.xml";
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    protected Class<? extends FXWorkbench> getWorkbenchClass() {
        return WorkbenchMissingIdInPerspective.class;
    }

    @Override
    protected String[] getBasePackages() {
        return new String[]{"org.jacp.test"};
    }

    /**
     * only for testing
     */
    public void startComponentScaning() {
        this.scanPackegesAndInitRegestry();
    }

    @Override
    public void postInit(final Stage stage) {

        instance[0] = this;
        ApplicationLauncherMissingIdInPerspective.latch.countDown();
    }




}
