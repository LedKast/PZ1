package com.ledkast;

import com.ledkast.image.ImageProcessor;
import com.ledkast.perceptron.RosenblattPerceptron;
import com.ledkast.perceptron.WeightStore;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Collection;

public class Main {
    private static final String BASE_RESOURCE_PATH = "src/main/resources/";
    private static final int TARGET_IMAGE_SIZE = 15;
    private static final double LEARNING_SPEED = 0.25;
    public static final String ASTERISK_STING = "asterisk ✱";
    public static final String TRIANGLE_STRING = "triangle ▲";
    public static final String WEIGHTS_TXT = "weights.txt";
    public static final String LEARNING_SET_DIR = "/learning-set/";
    public static final String RECOGNIZE_DIR = "/recognize/";

    public static void main(String[] args) throws Exception {
        int width = TARGET_IMAGE_SIZE, height = TARGET_IMAGE_SIZE;
        WeightStore weightStore = new WeightStore(BASE_RESOURCE_PATH + WEIGHTS_TXT, width*height);
        RosenblattPerceptron perceptron = new RosenblattPerceptron(weightStore, LEARNING_SPEED, TRIANGLE_STRING, ASTERISK_STING);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Collection<File> files;
        int[] image;
        String filepath;
        boolean output;

        while (true) {
            System.out.print("Make choice - [0] learn, [1] recognize, [q] quit: ");
            String input = reader.readLine();

            switch (input) {
                case "0":
                    filepath = BASE_RESOURCE_PATH + LEARNING_SET_DIR;
                    files = FileUtils.listFiles(
                            new File(filepath),
                            new WildcardFileFilter("*.bmp"),
                            null);

                    for (File file : files) {
                        image = ImageProcessor.prepareImage(ImageProcessor.loadImage(filepath + file.getName()), width, height);
                        perceptron.learn(image, file.getName().contains("asterisk"));
                    }
                    break;
                case "1":
                    filepath = BASE_RESOURCE_PATH + RECOGNIZE_DIR;
                    files = FileUtils.listFiles(
                            new File(filepath),
                            new WildcardFileFilter("*.bmp"),
                            null);

                    for (File file : files) {
                        image = ImageProcessor.prepareImage(ImageProcessor.loadImage(filepath + file.getName()), width, height);
                        output = perceptron.recognizeImage(image);

                        System.out.println("This is " + (output ? (ASTERISK_STING + " [1]!") :  (TRIANGLE_STRING + " [0]!")));
                    }
                    break;
                case "q":
                    return;
                default:
                    System.out.println("Wrong input");
                    break;
            }
        }
    }
}
