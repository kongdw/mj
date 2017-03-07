/**
 * Majiang is a library that implements Mahjong game rules.
 *
 * Copyright 2009 Prudent Jérome
 *
 *     This file is part of Majiang.
 *
 *     Majiang is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Majiang is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * You can contact me at jprudent@gmail.com
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
