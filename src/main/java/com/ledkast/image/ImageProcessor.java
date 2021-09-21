package com.ledkast.image;

import net.sf.image4j.codec.bmp.BMPDecoder;
import net.sf.image4j.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {
    private static final int RGB_BLACK = -16777216;
    private static final int RGB_WHITE = -1;
    private static final boolean DEBUG_MODE = true;

    public ImageProcessor() {
    }

    public static BufferedImage loadImage(String name) throws IOException {
        return BMPDecoder.read(new File(name));
    }

    public static int[] prepareImage(BufferedImage image, int w, int h) {
//        if (DEBUG_MODE) {
//            System.out.println("Original image (" + image.getWidth() + " x " + image.getHeight() + ")");
//            printImage(image);
//            System.out.println();
//        }

        image = trimPixels(image);
//        if (DEBUG_MODE) {
//            System.out.println("Trimmed image (" + image.getWidth() + " x " + image.getHeight() + ")");
//            printImage(image);
//            System.out.println();
//        }

        image = ImageUtil.scaleImage(image, w, h);
        if (DEBUG_MODE) {
            System.out.println("Scaled image (" + image.getWidth() + " x " + image.getHeight() + ")");
            printImage(image);
            System.out.println();
        }

        return toMonochrome(convertToArray(image));
    }

    public static void printImage(BufferedImage image) {
        printImage(toMonochrome(image.getRGB(
                    0,
                    0,
                    image.getWidth(),
                    image.getHeight(),
                    null,
                    0,
                    image.getWidth())),
                image.getWidth(),
                image.getHeight());
    }

    public static void printImage(int[] rgb, int w, int h) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                System.out.print((rgb[i*w + j] == 1 ? 'G' : '•') + " ");
            }
            System.out.println();
        }
    }

    private static BufferedImage trimPixels(BufferedImage image) {
        int offsetX = Integer.MAX_VALUE, offsetY = Integer.MAX_VALUE, rightX = 0, bottomY = 0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getRGB(x, y) == RGB_BLACK) {
                    offsetX = Math.min(x, offsetX);
                    offsetY = Math.min(y, offsetY);
                    rightX = Math.max(x, rightX);
                    bottomY = Math.max(y, bottomY);
                }
            }
        }

        int maxBorder = Math.max(rightX - offsetX, bottomY - offsetY) + 1;
        return image.getSubimage(
                offsetX,
                offsetY,
                Math.min(maxBorder, image.getWidth() - offsetX),
                Math.min(maxBorder, image.getHeight() - offsetY));
    }

    private static int[] toMonochrome(int[] rgb) {
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = rgb[i] > (RGB_BLACK - RGB_WHITE) / 2 ? 0 : 1;
        }

        return rgb;
    }


    private static int[] convertToArray(BufferedImage image) {
        return image.getRGB(
                0,
                0,
                image.getWidth(),
                image.getHeight(),
                null,
                0,
                image.getWidth());
    }
}
