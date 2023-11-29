package co.id.pat.clientapp.service;

import java.io.IOException;

public interface FtpService {

    void downloadFile(String remoteFileName, String localFileName) throws IOException;
}
