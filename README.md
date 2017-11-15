# How to Use

First of all you will need to create a Dropbox App by navigating to https://www.dropbox.com/developers > create your app

### Code Samples

```java
public void doStuff() throws DbxException, IOException {
//First of all create a connection by providing the app Id and the Access Token
    DropboxDbxClient dropboxDbxClient = new DropboxDbxClient(appId, accessTokens);
    FileHandler fileHandler = new FileHandler(dropboxDbxClient.getClient());
    
    //create a random test  file
    String filename = "test-" + UUID.randomUUID().toString();
    File temp = File.createTempFile(filename, ".txt");
    
    // Upload "test.txt" to Dropbox
    fileHandler.uploadFile(temp.getAbsolutePath(), "/", filename + ".txt");
    
    // Get files and folder metadata from Dropbox root directory
    List<String> fileList = fileHandler.listFiles("", false);

    //cleanup - aka delete the created file
    fileHandler.deleteFile("/" + filename + ".txt");
}
```

