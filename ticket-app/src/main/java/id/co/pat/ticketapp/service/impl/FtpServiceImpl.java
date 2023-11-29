package id.co.pat.ticketapp.service.impl;

import id.co.pat.ticketapp.service.FtpService;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class FtpServiceImpl implements FtpService {

    @Value("${app.config.ftp.server}")
    private String server;

    @Value("${app.config.ftp.username}")
    private String username;

    @Value("${app.config.ftp.password}")
    private String password;

    public void uploadFile(byte[] pdfBytes, String fileName) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes)) {
            ftpClient.connect(server);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ftpClient.storeFile(fileName, inputStream);
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
    }
}
