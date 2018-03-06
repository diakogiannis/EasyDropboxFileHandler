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

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alexius Diakogiannis alexius [at] jee.gr on 3/6/2018.
 */
public class EncryptionFactoryTest {

    @Test
    public void encryptDecrypt(){
        String key = "Foo12345Foo12345";
        String superSecret = "Hello World";
        String encrypted = EncryptionFactory.encrypt(key, superSecret);

        Assert.assertEquals(superSecret,EncryptionFactory.decrypt(key, encrypted));
    }

    @Test
    public void encryptWithNull(){
        String key = null;
        String superSecret = "Hello World";
        String encrypted = EncryptionFactory.encrypt(key, superSecret);
        Assert.assertEquals(null,encrypted);
    }

    @Test
    public void decryptWithNull(){
        String encrypted = EncryptionFactory.encrypt("foo", null);
        Assert.assertEquals(null,encrypted);
    }

}
