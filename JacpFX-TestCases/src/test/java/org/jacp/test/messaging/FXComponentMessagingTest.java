package org.jacp.test.messaging;

import javafx.application.Platform;
import junit.framework.Assert;
import org.jacp.test.AllTests;
import org.jacp.test.components.ComponentMessagingTest1Component1;
import org.jacp.test.components.ComponentMessagingTest1Component2;
import org.jacp.test.main.ApplicationLauncherComponentMessaginTest1;
import org.jacp.test.main.ApplicationLauncherPerspectiveMessaginTest;
import org.jacp.test.perspectives.PerspectiveComponentMessagingTest1;
import org.jacp.test.perspectives.PerspectiveMessagingTestP1;
import org.jacp.test.perspectives.PerspectiveMessagingTestP2;
import org.jacp.test.perspectives.PerspectiveMessagingTestP3;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: amo
 * Date: 10.09.13
 * Time: 21:48
 * To change this template use File | Settings | File Templates.
 */
public class FXComponentMessagingTest {
    static Thread t;
    @AfterClass
    public static void exitWorkBench() {
        Platform.exit();
        AllTests.resetApplication();


    }

    @BeforeClass
    public static void initWorkbench() {


        t = new Thread("JavaFX Init Thread") {
            public void run() {

                ApplicationLauncherComponentMessaginTest1.main(new String[0]);

            }
        };
        t.setDaemon(true);
        t.start();
        // Pause briefly to give FX a chance to start
        try
        {
            ApplicationLauncherComponentMessaginTest1.latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void executeMessaging() throws InterruptedException {
        ComponentMessagingTest1Component1.wait= new CountDownLatch(1);
        ComponentMessagingTest1Component2.wait= new CountDownLatch(1);

        ComponentMessagingTest1Component1.counter = new AtomicInteger(10000);
        ComponentMessagingTest1Component2.counter = new AtomicInteger(10000);

        PerspectiveComponentMessagingTest1.fireMessage();

        ComponentMessagingTest1Component1.wait.await();
        ComponentMessagingTest1Component2.wait.await();

    }

    private void warmUp() throws InterruptedException {
        executeMessaging();
    }

    @Test
    // default Execution with ui time was 82442/82854/79245 ms.. with ui --  without ui 41751/41750/40689 linux ... / mac ui 26120 ... non ui 21269ms    ---windows ui 121747ms  --- nonui 65836ms
    public void testComponentMessaging() throws InterruptedException {
        warmUp();
        withUI();
        withoutUI();
    }

    private void withUI() throws InterruptedException {
        long start = System.currentTimeMillis();
        int i=0;
        ComponentMessagingTest1Component1.ui=true;
        ComponentMessagingTest1Component2.ui=true;
        while(i<10){
            executeMessaging();
            Assert.assertTrue(true);
            i++;
        }

        long end = System.currentTimeMillis();

        System.out.println("Execution with ui time was "+(end-start)+" ms.");
    }

    private void withoutUI() throws InterruptedException {
        long start = System.currentTimeMillis();
        int i=0;
        ComponentMessagingTest1Component1.ui=false;
        ComponentMessagingTest1Component2.ui=false;
        while(i<10){
            executeMessaging();
            Assert.assertTrue(true);
            i++;
        }

        long end = System.currentTimeMillis();

        System.out.println("Execution without ui time was "+(end-start)+" ms.");
    }
}