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

import com.diakogiannis.alexius.aesencryptor.encryption.EncryptionFactory;
import com.diakogiannis.alexius.aesencryptor.encryption.EncryptionFactoryImpl;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.UploadErrorException;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 * @author Alexius Diakogiannis [alexius at jee.gr]
 */
public class FileHandlerImpl implements FileHandler {

    public final Logger logger = Logger.getLogger(FileHandlerImpl.class.getCanonicalName());

    private final DbxClientV2 client;
    private String secureKey;

    public FileHandlerImpl(DbxClientV2 client) {
        this.client = client;
    }

    public FileHandlerImpl(DbxClientV2 client, String secureKey) {
        this.client = client;
        this.secureKey = secureKey;
    }

    /**
     * @param path The path in dropbox there the file exists
     * @return java.io.InputStream with file contents
     * @throws DbxException
     */
    @Override
    public InputStream readFile(String path)
            throws DbxException {
        DbxDownloader downloader = client.files().downloadBuilder(path).start();
        InputStream in = downloader.getInputStream();
        return in;
    }

    /**
     * reads an encrypted file
     *
     * @param path The path in dropbox there the file exists
     * @return java.io.InputStream with file contents
     * @throws DbxException
     */
    @Override
    public InputStream readEncryptedFile(String path) throws DbxException {
        return null;
    }

    /**
     * @param folder folder location
     * @param recursively recursive list flag
     * @return List with file paths
     * @throws DbxException
     */
    @Override
    public List<String> listFiles(String folder, boolean recursively)
            throws DbxException {
        if (folder.endsWith("/")) {
            folder = folder.substring(0, folder.length() - 1);
        }
        ListFolderResult result = client.files().listFolderBuilder(folder).withRecursive(recursively).start();
        List<String> filepaths = new ArrayList<>();
        result.getEntries().forEach((metadata) -> {
            filepaths.add(metadata.getPathLower());
        });
        return filepaths;
    }

    /**
     * @param remotePath The path in dropbox there the file will be uploaded
     * @param in java.io.InputStream with file contents
     * @throws DbxException
     * @throws UploadErrorException
     * @throws IOException
     */
    private void uploadFile(String remotePath, InputStream in) throws DbxException, IOException {
        client.files().uploadBuilder(remotePath).uploadAndFinish(in);
    }

    /**
     * @param path The local path with the file to be uploaded
     * @param remotePath The path in dropbox there the file will be uploaded
     * @param name filename for remote use
     * @throws FileNotFoundException
     * @throws DbxException
     * @throws IOException
     */
    @Override
    public void uploadFile(String path, String remotePath, String name)
            throws DbxException, IOException {
        uploadFile(remotePath + name, new FileInputStream(path));
    }

    /**
     * Encrypts and uploads a file
     *
     * @param path The local path with the file to be uploaded
     * @param remotePath The path in dropbox there the file will be uploaded
     * @throws FileNotFoundException
     * @throws DbxException
     * @throws IOException
     */
    @Override
    public void uploadEncryptedFile(final String path, final String remotePath) throws DbxException, IOException {
        EncryptionFactory encryptionFactory = new EncryptionFactoryImpl();
        String destName="{enc}"+remotePath;
        FileInputStream inputStream = new FileInputStream(path);

            // read fills buffer with data and ret
        byte[] bytes =  IOUtils.toByteArray(inputStream);
        byte[] encbytes = encryptionFactory.encryptBytes(this.secureKey, bytes);
        File tmpEnc = File.createTempFile(System.nanoTime()+"edbh", "tmp");
        client.files().uploadBuilder(remotePath).uploadAndFinish(new FileInputStream(tmpEnc));

    }

    /**
     * Method created for use with Spring MVC Framework that uses
     * org.springframework.web.multipart.MiltipartFile
     *
     * @param multipartFile The file contents
     * @param remotePath The path in dropbox there the file will be uploaded
     * @throws IOException
     * @throws DbxException
     * @see org.springframework.web.multipart
     */
    @Override
    public void uploadFile(MultipartFile multipartFile, String remotePath)
            throws IOException, DbxException {
        uploadFile(remotePath + multipartFile.getName(), multipartFile.getInputStream());
    }

    /**
     * Method created for use with Spring MVC Framework that uses
     * org.springframework.web.multipart.MiltipartFile for encrypted files
     *
     * @param multipartFile The file contents
     * @param remotePath The path in dropbox there the file will be uploaded
     * @throws IOException
     * @throws DbxException
     * @see org.springframework.web.multipart
     */
    @Override
    public void uploadEncryptedFile(MultipartFile multipartFile, String remotePath) throws IOException, DbxException {

    }

    /**
     * @param path The path in dropbox there the file will be deleted
     * @throws DbxException
     */
    @Override
    public void deleteFile(String path)
            throws DbxException {
        client.files().delete(path);
    }

    /**
     * Creates a directory using a default Locale the Itallian
     *
     * @param path The path in dropbox there the directory will be created
     * @param name Directory name
     * @return The directory path
     * @throws DbxException
     */
    @Override
    public String createDirectory(String path, String name)
            throws DbxException {
        return createDirectory(path, name, Locale.ITALY);
    }

    /**
     * @param path The path in dropbox there the directory will be created
     * @param name Directory name
     * @param locale The locale
     * @return The directory path
     * @throws DbxException
     */
    @Override
    public String createDirectory(String path, String name, Locale locale)
            throws DbxException {
        FolderMetadata meta = client.files().createFolder(path.toLowerCase(locale) + name.toLowerCase(locale));
        return meta.getPathLower();
    }
}
