package com.octo.captcha.component.image.backgroundgenerator;

import com.octo.captcha.CaptchaException;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Random;



public class FileReaderRandomBackgroundGenerator extends AbstractBackgroundGenerator {

    private List<BufferedImage> images = new ArrayList<>();
    private String rootPath = ".";

    public FileReaderRandomBackgroundGenerator(Integer width, Integer height, String rootPath) {
        super(width, height);

        if (rootPath != null)
            this.rootPath = rootPath;

        File dir = findDirectory(this.rootPath);
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    BufferedImage img = getImage(file);
                    if (img != null) {
                        images.add(img);
                    }
                }
            }

            if (!images.isEmpty()) {
                for (int i = 0; i < images.size(); i++) {
                    images.set(i, tile(images.get(i)));
                }
            } else {
                throw new CaptchaException("Root path directory is valid but does not contain any image (jpg) files");
            }
        }
    }

    protected static Map<String, File> cachedDirectories = new HashMap<>();

    protected File findDirectory(String rootPath) {
        if (cachedDirectories.containsKey(rootPath)) {
            return cachedDirectories.get(rootPath);
        }

        File dir = new File(rootPath);
        StringBuilder triedPath = new StringBuilder();
        appendFilePath(triedPath, dir);

        if (!dir.canRead() || !dir.isDirectory()) {
            dir = new File(".", rootPath);
            appendFilePath(triedPath, dir);

            if (!dir.canRead() || !dir.isDirectory()) {
                dir = new File("/", rootPath);
                appendFilePath(triedPath, dir);

                if (!dir.canRead() || !dir.isDirectory()) {
                    URL url = getClass().getClassLoader().getResource(rootPath);
                    if (url != null) {
                        dir = new File(url.getFile());
                        appendFilePath(triedPath, dir);
                    } else {
                        url = ClassLoader.getSystemClassLoader().getResource(rootPath);
                        if (url != null) {
                            dir = new File(url.getFile());
                            appendFilePath(triedPath, dir);
                        }
                    }
                }
            }
        }

        if (!dir.canRead() || !dir.isDirectory()) {
            StringTokenizer token = getClasspathFromSystemProperty();
            while (token.hasMoreElements()) {
                String path = token.nextToken();
                if (!path.endsWith(".jar")) {
                    dir = new File(path, rootPath);
                    appendFilePath(triedPath, dir);
                    if (dir.canRead() && dir.isDirectory()) {
                        break;
                    }
                }
            }
        }

        if (!dir.canRead() || !dir.isDirectory()) {
            throw new CaptchaException("All tried paths:\n" + triedPath + "\nare not readable or not directories");
        }

        cachedDirectories.put(rootPath, dir);
        return dir;
    }

    private StringTokenizer getClasspathFromSystemProperty() {
        String classpath = System.getProperty("java.class.path");
        return new StringTokenizer(classpath, File.pathSeparator);
    }

    private void appendFilePath(StringBuilder triedPath, File dir) {
        triedPath.append(dir.getAbsolutePath()).append("\n");
    }

    private BufferedImage tile(BufferedImage tileImage) {
        BufferedImage image = new BufferedImage(getImageWidth(), getImageHeight(), tileImage.getType());
        Graphics2D g2 = image.createGraphics();
        int NumberX = (getImageWidth() / tileImage.getWidth());
        int NumberY = (getImageHeight() / tileImage.getHeight());

        for (int y = 0; y <= NumberY; y++) {
            for (int x = 0; x <= NumberX; x++) {
                g2.drawImage(tileImage, x * tileImage.getWidth(), y * tileImage.getHeight(),
                        Math.min(tileImage.getWidth(), getImageWidth()),
                        Math.min(tileImage.getHeight(), getImageHeight()), null);
            }
        }

        g2.dispose();
        return image;
    }

    private static BufferedImage getImage(File file) {
        try (InputStream is = new FileInputStream(file)) {
            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                return null; // formato non supportato
            }
            return image;
        } catch (IOException e) {
            throw new CaptchaException("Errore nella lettura del file immagine: " + file.getAbsolutePath(), e);
        }
    }

    @Override
    public BufferedImage getBackground() {
        return images.get(myRandom.nextInt(images.size()));
    }
}
