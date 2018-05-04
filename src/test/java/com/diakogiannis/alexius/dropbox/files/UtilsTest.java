package com.diakogiannis.alexius.dropbox.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author diakogiannis
 */
public class UtilsTest {

    public UtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of compaireFiles method, of class Utils.
     */
    @Test
    public void testCompaireFiles() throws IOException {
        System.out.println("compaireFiles");
        ClassLoader classLoader = getClass().getClassLoader();

        InputStream stream;
        File file1 = File.createTempFile("testdbh-file1-" + System.currentTimeMillis(), ".tmp");
        File file2 = File.createTempFile("testdbh-file2-" + System.currentTimeMillis(), ".tmp");

        stream = classLoader.getResourceAsStream("diakogiannis.png");
        FileUtils.copyInputStreamToFile(stream, file1);

        stream = classLoader.getResourceAsStream("diakogiannis.png");
        FileUtils.copyInputStreamToFile(stream, file2);

        Utils instance = new Utils();

        assertEquals(Boolean.TRUE, instance.compaireFiles(file1, file2));

    }

    /**
     * Test of compaireFiles method, of class Utils.
     */
    @Test(expected = IOException.class)
    public void testCompaireBrokenFiles() throws IOException {
        System.out.println("compaireFiles");
        ClassLoader classLoader = getClass().getClassLoader();

        InputStream stream;
        File file1 = File.createTempFile("testdbh-file1-" + System.currentTimeMillis(), ".tmp");
        File file2 = File.createTempFile("testdbh-file2-" + System.currentTimeMillis(), ".tmp");

        stream = classLoader.getResourceAsStream("diakogiannis.png");
        FileUtils.copyInputStreamToFile(stream, file1);

        Utils instance = new Utils();

        instance.compaireFiles(file1, file2);

    }

    /**
     * Test of compaireFiles method, of class Utils.
     */
    public void testCompaireOneFile() throws IOException {
        System.out.println("compaireFiles");
        ClassLoader classLoader = getClass().getClassLoader();

        InputStream stream;
        File file1 = File.createTempFile("testdbh-file1-" + System.currentTimeMillis(), ".tmp");

        stream = classLoader.getResourceAsStream("diakogiannis.png");
        FileUtils.copyInputStreamToFile(stream, file1);

        Utils instance = new Utils();

        instance.compaireFiles(file1);

    }

}
