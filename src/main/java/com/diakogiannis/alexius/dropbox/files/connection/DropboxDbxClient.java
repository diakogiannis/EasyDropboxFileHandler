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

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.util.Locale;

/**
 * @author Alexius Diakogiannis [alexius at jee.gr]
 */
public class DropboxDbxClient implements DropboxDbxClientInterface {

    private String appId;
    private String accessToken;

    /**
     * @param appId       The Dropbox application ID
     * @param accessToken The access token
     */
    public DropboxDbxClient(String appId, String accessToken) {
        this.appId = appId;
        this.accessToken = accessToken;
    }

    public DropboxDbxClient() {
        //Nothing
    }

    /**
     * get connection client
     */
    @Override
    public DbxClientV2 getClient() {

        DbxRequestConfig config = DbxRequestConfig.newBuilder(appId).withAutoRetryEnabled(3).withUserLocaleFrom(Locale.ITALY).build();
        return new DbxClientV2(config, accessToken);
    }

}
