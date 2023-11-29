package id.co.pat.ticketapp.service;

import java.io.IOException;

public interface FtpService {

    void uploadFile(byte[] pdfBytes, String fileName) throws IOException;
}
