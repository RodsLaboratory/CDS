package edu.pitt.rods.cds;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.*;

public class NaiveBayesEvaluator {

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println("Usage: java NaiveBayesEvaluator <modelFile> <testFile> <outputFile> <predictionsFile>");
            System.exit(1);
        }

        String modelFile = args[0];
        String testFile = args[1];
        String outputFile = args[2];
        String predictionsFile = args[3];

        // Load the Naive Bayes model
        NaiveBayes classifier = loadModel(modelFile);

        // Load the test data
        Instances testData = loadTestData(testFile);

        // Evaluate the model
        Evaluation evaluation = evaluateModel(classifier, testData);

        // Output the evaluation results
        outputResults(evaluation, outputFile);

        // Output the predictions
        outputPredictions(classifier, testData, predictionsFile);
    }

    private static NaiveBayes loadModel(String modelFile) throws Exception {
        FileInputStream fileIn = new FileInputStream(modelFile);
        ObjectInputStream objIn = new ObjectInputStream(fileIn);
        NaiveBayes classifier = (NaiveBayes) objIn.readObject();
        objIn.close();
        return classifier;
    }

    private static Instances loadTestData(String testFile) throws Exception {
        DataSource source = new DataSource(testFile);
        Instances testData = source.getDataSet();
        if (testData.classIndex() == -1) {
            testData.setClassIndex(testData.numAttributes() - 1);
        }
        return testData;
    }

    private static Evaluation evaluateModel(NaiveBayes classifier, Instances testData) throws Exception {
        Evaluation evaluation = new Evaluation(testData);
        evaluation.evaluateModel(classifier, testData);
        return evaluation;
    }

    private static void outputResults(Evaluation evaluation, String outputFile) throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
        writer.println(evaluation.toSummaryString("\nSummary\n===\n", true));
        writer.println(evaluation.toClassDetailsString("\nDetailed Accuracy By Class\n===\n"));
        writer.println(evaluation.toMatrixString("\nConfusion Matrix\n===\n"));
        writer.close();
    }

    private static void outputPredictions(NaiveBayes classifier, Instances testData, String predictionsFile) throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(predictionsFile));
        writer.println("Instance,Actual,Predicted,Distribution");

        for (int i = 0; i < testData.numInstances(); i++) {
            double actualClass = testData.instance(i).classValue();
            double predictedClass = classifier.classifyInstance(testData.instance(i));
            double[] distribution = classifier.distributionForInstance(testData.instance(i));

            writer.print(i + 1);
            writer.print(",");
            writer.print(testData.classAttribute().value((int) actualClass));
            writer.print(",");
            writer.print(testData.classAttribute().value((int) predictedClass));
            writer.print(",");
            for (int j = 0; j < distribution.length; j++) {
                writer.print(distribution[j]);
                if (j < distribution.length - 1) {
                    writer.print(",");
                }
            }
            writer.println();
        }
        writer.close();
    }
}