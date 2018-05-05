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
package com.diakogiannis.alexius.dropbox.files;

import com.dropbox.core.DbxException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * @author Alexius Diakogiannis [alexius at jee.gr]
 */
public interface FileHandler {

    /**
     * Creates a directory using a default Locale the Itallian
     *
     * @param path The path in dropbox there the directory will be created
     * @param name Directory name
     * @return The directory path
     * @throws DbxException
     */
    String createDirectory(String path, String name) throws DbxException;

    /**
     * @param path   The path in dropbox there the directory will be created
     * @param name   Directory name
     * @param locale The locale
     * @return The directory path
     * @throws DbxException
     */
    String createDirectory(String path, String name, Locale locale) throws DbxException;

    /**
     * @param path The path in dropbox there the file will be deleted
     * @throws DbxException
     */
    void deleteFile(String path) throws DbxException;

    /**
     * @param folder      folder location
     * @param recursively recursive list flag
     * @return List with file paths
     * @throws DbxException
     */
    List<String> listFiles(String folder, boolean recursively) throws DbxException;

    /**
     * @param path The path in dropbox there the file exists
     * @return java.io.InputStream with file contents
     * @throws DbxException
     */
    InputStream readFile(String path) throws DbxException;

    /**
     * reads an encrypted file
     * @param path The path in dropbox there the file exists
     * @return java.io.InputStream with file contents
     * @throws DbxException
     */
    InputStream readEncryptedFile(String path) throws DbxException;

    /**
     * @param path       The local path with the file to be uploaded
     * @param remotePath The path in dropbox there the file will be uploaded
     * @param name       filename for remote use
     * @throws FileNotFoundException
     * @throws DbxException
     * @throws IOException
     */
    void uploadFile(String path, String remotePath, String name) throws DbxException, IOException;

    /**
     * Encrypts and uploads a file
     * @param path       The local path with the file to be uploaded
     * @param remotePath The path in dropbox there the file will be uploaded
     * @throws FileNotFoundException
     * @throws DbxException
     * @throws IOException
     */
    void uploadEncryptedFile(String path, String remotePath) throws DbxException, IOException;

    /**
     * Method created for use with Spring MVC Framework that uses org.springframework.web.multipart.MiltipartFile
     *
     * @param multipartFile The file contents
     * @param remotePath    The path in dropbox there the file will be uploaded
     * @throws IOException
     * @throws DbxException
     * @see org.springframework.web.multipart
     */
    void uploadFile(MultipartFile multipartFile, String remotePath) throws IOException, DbxException;

    /**
     * Method created for use with Spring MVC Framework that uses org.springframework.web.multipart.MiltipartFile for encrypted files
     *
     * @param multipartFile The file contents
     * @param remotePath    The path in dropbox there the file will be uploaded
     * @throws IOException
     * @throws DbxException
     * @see org.springframework.web.multipart
     */
    void uploadEncryptedFile(MultipartFile multipartFile, String remotePath) throws IOException, DbxException;

}
