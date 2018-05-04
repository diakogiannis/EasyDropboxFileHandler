/*
 * Copyright {2018} {Alexius Diakogiannis}
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
package com.diakogiannis.alexius.dropbox.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diakogiannis
 */
public class Utils {

    public static final Logger logger = Logger.getLogger(Utils.class.getCanonicalName());

    public Boolean compaireFiles(final File... files) {
        Boolean identical = Boolean.TRUE;
        if (files.length < 2) {
            return Boolean.FALSE;
        } else {
            try {
                
                String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(files[0]));

                for (File f : files) {
                    if(!md5.equals(org.apache.commons.codec.digest.DigestUtils.md5Hex(new FileInputStream(f)))){
                        identical = Boolean.FALSE;
                    }
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error compering files", e);
            }
        }
        return identical;
    }

}
