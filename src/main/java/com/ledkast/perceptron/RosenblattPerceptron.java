package com.ledkast.perceptron;

public class RosenblattPerceptron {
    private final WeightStore weightStore;
    private final double learningSpeed;
    private final String oneString;
    private final String zeroString;

    /**
     * @param weightStore configured store object
     * @param learningSpeed (0; 1]
     * @param zeroString object name for FALSE (0) output
     * @param oneString object name for TRUE (1) output
     */
    public RosenblattPerceptron(WeightStore weightStore, double learningSpeed, String zeroString, String oneString) {
        this.weightStore = weightStore;
        this.learningSpeed = learningSpeed;
        this.oneString = oneString;
        this.zeroString = zeroString;
    }

    public void learn(int[] image, boolean answer) throws Exception {
        boolean output = recognizeImage(image);

        System.out.println("This is " + (output ? (oneString + " [1]!") :  (zeroString + " [0]!")));

        if (answer != output) {
            updateWeights(image, weightStore.getWeights(), output ? -1 : 1);
            System.out.println("[---] I guess wrong, I'll write it down in a notebook...\n");
        } else {
            System.out.println("[+++] Yes, I'm right :)\n");
        }
        System.out.println("=======================================");
    }

    public boolean recognizeImage(int[] image) throws Exception {
        double[] weights = weightStore.getWeights();
        double sum = 0;
        boolean output;

        if (weights.length != image.length) {
            throw new Exception("Image and weights sizes not equals!");
        }

        for (int i = 0; i < image.length; i++) {
            sum += image[i] * weights[i];
        }
        output = sum > 0;
        return output;

    }

    private void updateWeights(int[] image, double[] weights, double delta) {
        for (int i = 0; i < image.length; i++) {
            weights[i] += learningSpeed * delta * image[i];
        }
        weightStore.save(weights);
    }
}
