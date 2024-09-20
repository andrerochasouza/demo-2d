package br.com.andre.utils;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class ResourceLoader {

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(Objects.requireNonNull(ResourceLoader.class.getResource(path)));
        } catch (IOException e) {
            System.err.println("Erro ao carregar a imagem: " + path);
            e.printStackTrace();
            return null;
        }
    }
}