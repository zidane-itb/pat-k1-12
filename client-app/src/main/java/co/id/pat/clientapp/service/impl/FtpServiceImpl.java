package co.id.pat.clientapp.service.impl;

import co.id.pat.clientapp.service.FtpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Component
@Slf4j
public class FtpServiceImpl implements FtpService {

    @Value("${app.config.ftp.server}")
    private String server;
    @Value("${app.config.ftp.username}")
    private String username;
    @Value("${app.config.ftp.password}")
    private String password;
    @Value("${app.config.ftp.download.directory}")
    private String downloadDirectory;

    @Override
    public void downloadFile(String remoteFileName, String localFileName) throws IOException {
        FTPClient ftpClient = new FTPClient();

        try (FileOutputStream outputStream = new FileOutputStream(downloadDirectory + localFileName)) {
            ftpClient.connect(server);
            ftpClient.login(username, password);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            boolean success = ftpClient.retrieveFile(remoteFileName, outputStream);

            if (!success) {
                log.error("Failed to download file from FTP server with filename: {}", remoteFileName);
            }
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                }
            } finally {
                ftpClient.disconnect();
            }
        }
    }
}
