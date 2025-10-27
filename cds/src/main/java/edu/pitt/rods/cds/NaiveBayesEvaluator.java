package edu.pitt.rods.cds;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.estimators.Estimator;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.io.*;

public class NaiveBayesEvaluator {

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println("Usage: java NaiveBayesEvaluator <modelDirectory> <modelYear 2016-2020> <inputFile> <outputFile>");
            System.exit(1);
        }

        String modelDirectory = args[0];
        String modelYear = args[1];
        String inputFile = args[2];
        String outputFile = args[3];


        Map<String, List<String>> modelFilesByYear = new HashMap<>();

        String[] modelDiseases = {"INFLUENZA","RSV","HMPV","PARAINFLUENZA","ADENOVIRUS","ENTEROVIRUS","OTHER"};


        String[] files = {
                "ADENOVIRUS_2012-2016.model",
                "ADENOVIRUS_2013-2017.model",
                "ADENOVIRUS_2014-2018.model",
                "ADENOVIRUS_2015-2019.model",
                "ADENOVIRUS_2016-2020.model",
                "COV_2019-2020.model",
                "ENTEROVIRUS_2012-2016.model",
                "ENTEROVIRUS_2013-2017.model",
                "ENTEROVIRUS_2014-2018.model",
                "ENTEROVIRUS_2015-2019.model",
                "ENTEROVIRUS_2016-2020.model",
                "HMPV_2012-2016.model",
                "HMPV_2013-2017.model",
                "HMPV_2014-2018.model",
                "HMPV_2015-2019.model",
                "HMPV_2016-2020.model",
                "INFLUENZA_2012-2016.model",
                "INFLUENZA_2013-2017.model",
                "INFLUENZA_2014-2018.model",
                "INFLUENZA_2015-2019.model",
                "INFLUENZA_2016-2020.model",
                "OTHER_2015-2016.model",
                "OTHER_2016-2017.model",
                "OTHER_2017-2018.model",
                "OTHER_2018-2019.model",
                "OTHER_2019-2020.model",
                "PARAINFLUENZA_2012-2016.model",
                "PARAINFLUENZA_2013-2017.model",
                "PARAINFLUENZA_2014-2018.model",
                "PARAINFLUENZA_2015-2019.model",
                "PARAINFLUENZA_2016-2020.model",
                "RSV_2012-2016.model",
                "RSV_2013-2017.model",
                "RSV_2014-2018.model",
                "RSV_2015-2019.model",
                "RSV_2016-2020.model"
        };

        // First, group files by year
        for (String file : files) {
            String year = file.substring(file.lastIndexOf('-') + 1, file.lastIndexOf('.'));
            modelFilesByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(file);
        }

        // Then, reorder each list according to modelDiseases
        for (String year : modelFilesByYear.keySet()) {
            List<String> yearFiles = modelFilesByYear.get(year);
            yearFiles.sort(Comparator.comparingInt(f -> {
                String disease = f.substring(0, f.indexOf('_'));
                for (int i = 0; i < modelDiseases.length; i++) {
                    if (modelDiseases[i].equals(disease)) {
                        return i;
                    }
                }
                return Integer.MAX_VALUE; // If not found, put at end
            }));
        }


        // Remove single quotes from the file before loading
        String cleanedTestFile = removeSingleQuotesFromFile(inputFile);

        // Load the input arff
        Instances testData = loadTestData(cleanedTestFile);
        System.out.println("Loaded test data from: " + cleanedTestFile);

        String attributeIndices = "";
        String attributesToRemoveFile = "/attributes_to_remove-" + modelYear  + ".txt";
        try (InputStream is = NaiveBayesEvaluator.class.getResourceAsStream(attributesToRemoveFile)) {
            assert is != null;
            try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8)) {
                attributeIndices = scanner.useDelimiter("\\A").next();
                // use the longString
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        testData = removeAttributes(testData, attributeIndices);
        System.out.println("Filtered out attributes using " + attributesToRemoveFile) ;




        List<String> modelFilesForYear = modelFilesByYear.get(modelYear);

        int nModels = modelFilesForYear.size();
        int nInstances = testData.numInstances();
        int nNumClasses = testData.numClasses();
        double[][][] log_likelihoods = new double[nModels][nInstances][nNumClasses];
        double[][][] probabilities = new double[nModels][nInstances][nNumClasses];

        System.out.println("Number of models: " + modelFilesForYear.size());
        for (int i = 0; i < modelFilesForYear.size(); i++) {
            // Load the Naive Bayes model
            String modelFile = modelDirectory + File.separator + modelFilesForYear.get(i);
            NaiveBayes classifier = loadModel(modelFile);

            System.out.println("Loaded NaiveBayes classifier from: " + modelFile);

            Estimator[][] m_Distributions = classifier.getConditionalEstimators();

            for (int iInstance = 0; iInstance < testData.numInstances(); iInstance++) {
                Instance instance = testData.instance(iInstance);
                log_likelihoods[i][iInstance] = getLogLikelihoodForInstance(m_Distributions, testData, instance, nNumClasses);
                probabilities[i][iInstance] = classifier.distributionForInstance(instance);
//                System.out.println("Model: " + modelDiseases[i] + ", likelihoods for instance " + (iInstance) + ": " + Arrays.toString(log_likelihoods[i][iInstance]));
//                System.out.println("Model: " + modelDiseases[i] + ", probabilities for instance " + (iInstance) + ": " + Arrays.toString(probabilities[i][iInstance]));
            }


        }
        // Output the predictions
        outputPredictions(outputFile, nInstances, nModels, nNumClasses, log_likelihoods, probabilities);

    }

    public static double[] getLogLikelihoodForInstance(Estimator[][] m_Distributions, Instances train, Instance instance, int nNumClasses) throws Exception {
        Instances instances = train;
        double[] log_likelihoods = new double[nNumClasses];

        for (int iClass = 0; iClass < nNumClasses; iClass++) {
            log_likelihoods[iClass] = 0;
        }

        for (int iClass = 0; iClass < nNumClasses; iClass++) {
            double logfP = 0;

            for (int iAttribute = 0; iAttribute < instances.numAttributes(); iAttribute++) {
                double iCPT = 0;
                iCPT = iCPT * nNumClasses + iClass;
                if (iAttribute == instances.classIndex()) {
                } else {
                    String name_iAttribute = train.attribute(iAttribute).name();
                    //System.out.println(name_iAttribute);
                    int tempIndex = -1;
                    for (int index=0; index<instance.numAttributes(); index++){
                        if (instance.attribute(index).name().equals(name_iAttribute)){
                            tempIndex = index;
                            break;
                        }
                    }
                    logfP
                            += Math.log10(m_Distributions[iAttribute][(int) iCPT].getProbability(instance.value(tempIndex)));
                }
            }
            log_likelihoods[iClass] += logfP;
        }
        return log_likelihoods;
    }


    private static NaiveBayes loadModel(String modelFile) throws Exception {
        FileInputStream fileIn = new FileInputStream(modelFile);
        ObjectInputStream objIn = new ObjectInputStream(fileIn);
        NaiveBayes classifier = (NaiveBayes) objIn.readObject();
//        System.out.println("Loaded NaiveBayes classifier" + classifier.toString());
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


private static void outputPredictions(String predictionsFile, int nInstances, int nModels, int nClasses, double[][][] log_likelihoods, double[][][] probabilities) throws Exception {
        File file = new File(predictionsFile);
        boolean fileExists = file.exists();

        PrintWriter writer = new PrintWriter(new FileWriter(predictionsFile, fileExists)); // Set append mode if file exists

        // Only write header if creating a new file
        if (!fileExists) {
            writer.println("ID,SEASON,ICD_INFLUENZA,LAB_INFLUENZA,LABEL_INFLUENZA,ICD_RSV,LAB_RSV,LABEL_RSV,ICD_HMPV,LAB_HMPV,LABEL_HMPV,ICD_ADENOVIRUS,LAB_ADENOVIRUS,LABEL_ADENOVIRUS,ICD_ENTEROVIRUS,LAB_ENTEROVIRUS,LABEL_ENTEROVIRUS,ICD_PARAINFLUENZA,LAB_PARAINFLUENZA,LABEL_PARAINFLUENZA,ICD_COV,LAB_COV,LABEL_COV,LAB_INFLUENZA_ADDITIONAL,LAB_RSV_ADDITIONAL,LAB_HMPV_ADDITIONAL,LAB_PARAINFLUENZA_ADDITIONAL,LAB_ENTEROVIRUS_ADDITIONAL,LAB_ADENOVIRUS_ADDITIONAL,LABEL_INFLUENZA_NEW,LABEL_RSV_NEW,LABEL_PARAINFLUENZA_NEW,LABEL_HMPV_NEW,LABEL_ENTEROVIRUS_NEW,LABEL_ADENOVIRUS_NEW," +
                    "Admit_date_time," +
                    "INFLUENZA_loglikelihood_M,INFLUENZA_loglikelihood_T," +
                    "RSV_loglikelihood_M,RSV_loglikelihood_T," +
                    "HMPV_loglikelihood_M,HMPV_loglikelihood_T," +
                    "PARAINFLUENZA_loglikelihood_M,PARAINFLUENZA_loglikelihood_T," +
                    "ADENOVIRUS_loglikelihood_M,ADENOVIRUS_loglikelihood_T," +
                    "ENTEROVIRUS_loglikelihood_M,ENTEROVIRUS_loglikelihood_T," +
                    "OTHER_loglikelihood_M,OTHER_loglikelihood_T," +
                    "INFLUENZA_Prob_M,INFLUENZA_Prob_T,RSV_Prob_M,RSV_Prob_T,HMPV_Prob_M,HMPV_Prob_T,PARAINFLUENZA_Prob_M,PARAINFLUENZA_Prob_T,ADENOVIRUS_Prob_M,ADENOVIRUS_Prob_T,ENTEROVIRUS_Prob_M,ENTEROVIRUS_Prob_T,OTHER_Prob_M,OTHER_Prob_T");
        }


        for (int iInstance = 0; iInstance < nInstances; iInstance++) {
            writer.print(iInstance);
            writer.print(",");

            // these are missing from the data
            for (int i = 0; i < 34; i++) {
                writer.print("M,");
            }

            // link this placehoder
            // todo: link this somehow to input data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);
            writer.print(formattedDate);
            writer.print(",");

            for (int iModel = 0; iModel < nModels; iModel++) {
                for (int iClass = 0; iClass < nClasses; iClass++) {
                    writer.print(log_likelihoods[iModel][iInstance][iClass]);
                    writer.print(",");
                }
            }

            for (int iModel = 0; iModel < nModels; iModel++) {
                for (int iClass = 0; iClass < nClasses; iClass++) {
                    writer.print(probabilities[iModel][iInstance][iClass]);
                    if (iClass != nClasses - 1 || iModel != nModels - 1) {
                        writer.print(",");
                    }
                }
            }

            writer.println();
        }
        writer.close();

        System.out.println("Predictions written to: " + predictionsFile);
    }

    private static double[] convertToLogLikelihood(double[] distribution) {
        double[] logLikelihood = new double[distribution.length];

        for (int i = 0; i < distribution.length; i++) {
            // Handle zero probabilities by using a small value instead
            double prob = distribution[i];
            if (prob == 0) {
                prob = 1e-10; // Small value to avoid log(0)
            }

            // Convert to natural logarithm
            logLikelihood[i] = Math.log(prob);
        }

        return logLikelihood;
    }

    // Utility method to remove single quotes from a file and write to a temp file
    private static String removeSingleQuotesFromFile(String inputFile) throws IOException {
        File tempFile = File.createTempFile("cleaned_test", ".arff");
        tempFile.deleteOnExit();
        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line.replace("'", ""));
                writer.newLine();
            }
        }
        return tempFile.getAbsolutePath();
    }

    /**
     * Removes attributes from the given Instances using WEKA Remove filter syntax.
     * @param data The Instances object to filter.
     * @param attributeIndices The attribute indices string (e.g., "1,3-5").
     * @return Filtered Instances with specified attributes removed.
     * @throws Exception if filtering fails.
     */
    public static Instances removeAttributes(Instances data, String attributeIndices) throws Exception {
        Remove remove = new Remove();
        remove.setAttributeIndices(attributeIndices);
        remove.setInputFormat(data);
        Instances filteredData = Filter.useFilter(data, remove);
        // Ensure class index is preserved if it was set
        if (data.classIndex() >= 0 && data.classIndex() < filteredData.numAttributes()) {
            filteredData.setClassIndex(data.classIndex());
        } else if (filteredData.numAttributes() > 0) {
            filteredData.setClassIndex(filteredData.numAttributes() - 1);
        }
        return filteredData;
    }
}