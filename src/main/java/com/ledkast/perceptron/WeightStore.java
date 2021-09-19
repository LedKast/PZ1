package com.ledkast.perceptron;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class WeightStore {
    private static final double RANDOM_MIN = -0.3;
    private static final double RANDOM_MAX = 0.3;

    private final String filename;
    private final int size;
    private final Random random = new Random();

    private double[] weights;

    public WeightStore(String filename, int size) {
        this.filename = filename;
        this.size = size;
        loadOrCreateFile();
    }

    public void save(double[] weights) {
        this.weights = weights;

        try {
            File oldFile = new File(filename);
            oldFile.delete();

            FileOutputStream outputStream = new FileOutputStream(filename);
            outputStream.write(Arrays.toString(weights).getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[] getWeights() {
        return weights;
    }

    private void loadOrCreateFile() {
        File weightsFile = new File(filename);
        if (!weightsFile.exists()) {
            save(generateRandomWeights(size));
            return;
        }

        try {
            Scanner reader = new Scanner(weightsFile);
            if (reader.hasNextLine()) {
                weights = Arrays.stream(reader.nextLine().replaceAll("\\[|\\]", "").split(", "))
                        .mapToDouble(Double::valueOf)
                        .toArray();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private double[] generateRandomWeights(int size) {
        double[] weight = new double[size];
        for (int i = 0; i < size; i++) {
            weight[i] = RANDOM_MIN + (RANDOM_MAX - RANDOM_MIN) * random.nextDouble();
        }
        return weight;
    }
}
