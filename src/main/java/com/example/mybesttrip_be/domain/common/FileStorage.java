package com.example.mybesttrip_be.domain.common;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class FileStorage {
    // base: ./uploads/
    public String save(String subdir, MultipartFile file) throws IOException {
        String dir = "./uploads/" + subdir;
        new File(dir).mkdirs();
        String ext = getExt(file.getOriginalFilename());
        String name = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path path = Path.of(dir, name);
        file.transferTo(path.toFile());
        return path.toString().replace("\\", "/");
    }

    private String getExt(String fname) {
        if (fname == null) return "";
        int i = fname.lastIndexOf('.');
        return (i < 0) ? "" : fname.substring(i + 1).toLowerCase();
    }
}
