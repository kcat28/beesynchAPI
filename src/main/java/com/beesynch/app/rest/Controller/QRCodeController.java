package com.beesynch.app.rest.Controller;

import com.beesynch.app.rest.Models.Hive;
import com.beesynch.app.rest.Models.User;
import com.beesynch.app.rest.Repo.HiveRepo;
import com.beesynch.app.rest.Repo.UserRepo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/qr")
public class QRCodeController {

    private final UserRepo userRepo;
    private final HiveRepo hiveRepo;

    public QRCodeController(UserRepo userRepo, HiveRepo hiveRepo) {
        this.userRepo = userRepo;
        this.hiveRepo = hiveRepo;
    }

    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateQRCode(@RequestParam Long adminUserId) throws WriterException, IOException {
        // Step 1: Finds the user
        User adminUser = userRepo.findById(adminUserId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Step 2: Check if the user is an admin
        if (!Boolean.TRUE.equals(adminUser.getIsAdmin())) {
            throw new RuntimeException("User is not an admin and cannot generate a QR code for a hive.");
        }

        // Step 3: Finds the hive created by this admin
        Hive hive = hiveRepo.findByCreatedBy(adminUser)
                .orElseThrow(() -> new RuntimeException("No hive found for this admin."));

        Long hiveId = hive.getHive_id();
        System.out.println("Generating QR Code for Hive ID: " + hiveId);

        // Step 4: Generates QR code containing the hive join link
        int width = 200;
        int height = 200;
        String qrContent = "https://bee_sync.com/join?hiveId=" + hiveId;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(bufferedImage, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }
}