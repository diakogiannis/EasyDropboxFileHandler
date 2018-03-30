/*
 * Copyright {2017} {Alexius Diakogiannis}
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
package com.diakogiannis.alexius.dropbox.files.connection;

import com.diakogiannis.alexius.dropbox.files.FileHandlerImpl;
import com.dropbox.core.DbxException;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * @author DiakogiannisA
 */
public class DxIntegrationTest {

    Logger LOG = LoggerFactory.getLogger(this.getClass());

    String appId;
    String accessTokens;

    public DxIntegrationTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();

        try {
            InputStream stream = classLoader.getResourceAsStream("test.properties");
            Properties properties = new Properties();
            properties.load(stream);
            appId = properties.getProperty("dropbox.appId");
            accessTokens = properties.getProperty("dropbox.accessTokens");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void connect() throws DbxException, IOException {
        if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(accessTokens)) {
            LOG.error("test.properties has no credentials for both appId and accessToken, skiping test...");
            Assert.assertTrue(true);
        } else {
            DropboxDbxClient dropboxDbxClient = new DropboxDbxClient(appId, accessTokens);

            FileHandlerImpl fileHandler = new FileHandlerImpl(dropboxDbxClient.getClient());

            String filename = "test-" + UUID.randomUUID().toString();
            File temp = File.createTempFile(filename, ".txt");

            // Upload "test.txt" to Dropbox
            fileHandler.uploadFile(temp.getAbsolutePath(), "/", filename + ".txt");

            // Get files and folder metadata from Dropbox root directory
            List<String> fileList = fileHandler.listFiles("", false);

            Assert.assertEquals(
                    fileList.stream()
                            .filter(m -> ("/" + filename + ".txt").equals(m))
                            .count(),
                    1);
            //cleanup
            fileHandler.deleteFile("/" + filename + ".txt");

            //verify
            fileList = fileHandler.listFiles("", false);
            Assert.assertEquals(
                    fileList.stream()
                            .filter(m -> ("/" + filename + ".txt").equals(m))
                            .count(),
                    0);
        }
    }
}
