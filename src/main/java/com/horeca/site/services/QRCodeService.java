package com.horeca.site.services;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class QRCodeService {

    public byte[] generate(String text) {
        ByteArrayOutputStream qrStream = QRCode.from(text).withSize(160, 160).to(ImageType.JPG).stream();
        return qrStream.toByteArray();
    }
}
