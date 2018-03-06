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

package io.znk.dropbox.files.io.znk.dropbox.security;

/**
 * Created by Alexius Diakogiannis alexius [at] jee.gr on 3/6/2018.
 */


import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class EncryptionFactory {

    private static final Logger LOG = LoggerFactory.getLogger(EncryptionFactory.class);


    public static String encrypt(String key, String value) {
            String encryptedString = null;

            try {
                String randomIV = randomIV();
                IvParameterSpec iv = new IvParameterSpec(Base64.decodeBase64(randomIV));
                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

                byte[] encrypted = cipher.doFinal(value.getBytes());
                LOG.debug("encrypted string: "
                        + Base64.encodeBase64String(encrypted));
                encryptedString =  Base64.encodeBase64String(encrypted).concat("-").concat(randomIV);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(),ex);
            }

            return encryptedString;
        }

        public static String decrypt(String key, String encrypted) {
            String decryptedString = null;
            try {
                byte[] iv = Base64.decodeBase64(encrypted.split("-")[1]);
                byte[] encryptedBytesSplited=Base64.decodeBase64(encrypted.split("-")[0]);

                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

                byte[] encryptedBytes = cipher.doFinal(encryptedBytesSplited);

                decryptedString = new String(encryptedBytes);

                LOG.debug("decrypted string: "+decryptedString);

            } catch (Exception ex) {
                LOG.error(ex.getMessage(),ex);
            }

            return decryptedString;
        }


        private static String randomIV() throws NoSuchAlgorithmException {
            SecureRandom random = SecureRandom.getInstance( "SHA1PRNG" );
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            return Base64.encodeBase64String(iv);
        }

    }

