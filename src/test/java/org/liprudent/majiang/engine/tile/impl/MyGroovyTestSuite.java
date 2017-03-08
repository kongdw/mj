package org.liprudent.majiang.engine.tile.impl;
import groovy.util.GroovyTestSuite;
import junit.framework.TestSuite;
/**
 *
 * @author jerome
 */
public class MyGroovyTestSuite extends TestSuite {
     // Since Eclipse launches tests relative to the project root,
     // declare the relative path to the test scripts for convenience
     private static final String TEST_ROOT = "src/test/groovy/org/liprudent/majiang/engine/tile/impl/";
     public static GroovyTestSuite suite() throws Exception {
         GroovyTestSuite suite = new GroovyTestSuite();
         GroovyTestSuite gsuite = new GroovyTestSuite();
         suite.addTestSuite(gsuite.compile(TEST_ROOT + "TileComputerTest.groovy"));
         suite.addTestSuite(gsuite.compile(TEST_ROOT + "TileSorterTest.groovy"));
         return suite;
     }
}
