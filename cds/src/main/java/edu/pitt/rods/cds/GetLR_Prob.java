// Copyright (c) 2024 University of Pittsburgh
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
// documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
// Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//


package edu.pitt.rods.cds.a_run1023;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import edu.pitt.rods.cds.utility.Configuration;
import weka.classifiers.bayes.NaiveBayes;
//import weka.classifiers.evaluation.output.prediction.AbstractOutput;
//import weka.core.BatchPredictor;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.estimators.Estimator;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class C2_1getLR {
	static PrintWriter printoutProb;
	static PrintWriter printoutLR;
	static String folderLoc;	
	static String disease;
	static String season;
	static String nextyear;
	static double threshold;
	static ArrayList<String> diseaseList;
	//static Instances originalTrainData;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// The  test data  must have the same  order as the train arff.
		String property_file_name = "properties10172023.txt";		
		folderLoc=Configuration.getExperimentFolderLocation(property_file_name);	
		ArrayList<Double> thresholdList = new ArrayList<Double>();
		thresholdList.add(0.001);
		
		diseaseList = new ArrayList<String>();
		diseaseList.add("INFLUENZA"); 
		diseaseList.add("RSV");	
		diseaseList.add("ADENOVIRUS"); diseaseList.add("PARAINFLUENZA"); 
		diseaseList.add("HMPV"); diseaseList.add("ENTEROVIRUS"); 
				
		
		ArrayList<String> thisYearList = new ArrayList<String>();		
		thisYearList.add("2012-2016"); 	
		thisYearList.add("2013-2017"); thisYearList.add("2014-2018"); 
		thisYearList.add("2015-2019"); thisYearList.add("2016-2020");

		ArrayList<String> nextYearList = new ArrayList<String>(); 			
		nextYearList.add("2016-2017"); 
		nextYearList.add("2017-2018"); nextYearList.add("2018-2019"); 
		nextYearList.add("2019-2020"); nextYearList.add("2020-2021");
		
		
			
		for (Double t:thresholdList) {
			threshold = t;	
			for (int i=0; i<nextYearList.size(); i++) {	
				String nextYearString = nextYearList.get(i);
				String thisYearString = thisYearList.get(i);
				System.out.println(threshold + "," + thisYearString + "," + nextYearString);
				saveLRProb(thisYearString, nextYearString);		
			}
		}

		
		diseaseList = new ArrayList<String>();
		diseaseList.add("OTHER"); 	
		ArrayList<String> featureUnionStringList = new ArrayList<String>();
		featureUnionStringList.add("2012-2016"); featureUnionStringList.add("2013-2017"); featureUnionStringList.add("2014-2018"); 
		featureUnionStringList.add("2015-2019"); featureUnionStringList.add("2016-2020");	
		
		ArrayList<String> trainYearList = new ArrayList<String>();
		trainYearList.add("2015-2016"); trainYearList.add("2016-2017"); trainYearList.add("2017-2018"); 
		trainYearList.add("2018-2019"); trainYearList.add("2019-2020");
		
		ArrayList<String> testYearList = new ArrayList<String>(); 			
		testYearList.add("2016-2017"); testYearList.add("2017-2018"); testYearList.add("2018-2019"); 
		testYearList.add("2019-2020"); testYearList.add("2020-2021");		
		
		for (Double t:thresholdList) {
			threshold = t;	
			for (int i=0; i< testYearList.size(); i++) {	
				String featureUnionString = featureUnionStringList.get(i);
				String trainYearString = trainYearList.get(i);
				String testYearString = testYearList.get(i);
				System.out.println(threshold + "," + featureUnionString + "," + trainYearString + "," + testYearString);
				saveLRProbOTHER(featureUnionString, trainYearString, testYearString );
			}
		}
		
		diseaseList = new ArrayList<String>();
		diseaseList.add("COV"); 	
		
		featureUnionStringList = new ArrayList<String>();
		featureUnionStringList.add("2016-2020");	
		
		trainYearList = new ArrayList<String>();
		trainYearList.add("2019-2020");
		
		testYearList = new ArrayList<String>(); 			
		testYearList.add("2020-2021");		
		
		for (Double t:thresholdList) {
			threshold = t;	
			for (int i=0; i< testYearList.size(); i++) {	
				String featureUnionString = featureUnionStringList.get(i);
				String trainYearString = trainYearList.get(i);
				String testYearString = testYearList.get(i);
				System.out.println(threshold + "," + featureUnionString + "," + trainYearString + "," + testYearString);
				saveLRProbOTHER(featureUnionString, trainYearString, testYearString );
			}
		}

	}
	
	
	public static void saveLRProbOTHER(String featureUnionString, String trainYearString, String testYearString ) throws Exception {
		season = trainYearString;
		nextyear = testYearString;
		
		Instances filtedTestData = new Instances (new BufferedReader(new FileReader(folderLoc + "1.intermediate_data/training_" + featureUnionString + "_test_" + testYearString + "_All_cleaned_union_" + threshold + ".arff")));		    
	
		for (String d:diseaseList) {
			disease = d;		
		    String resultLocation = folderLoc +"javaRunning_" + disease + "_" + trainYearString  + "_"+ testYearString + "/union_feature_all_"+threshold+"_NB/";
		    String filtedTrainDataFile = resultLocation+"train.arff";	   
		    Instances filtedTrainData = new Instances (new BufferedReader(new FileReader(filtedTrainDataFile)));	
		    filtedTrainData.setClassIndex(filtedTrainData.numAttributes()-1);	
		    String filePath = resultLocation + "NB_new.model";
			FileInputStream fileIn = new FileInputStream(filePath);
		    ObjectInputStream objIn = new ObjectInputStream(fileIn);
		    NaiveBayes classifier = (NaiveBayes) objIn.readObject();
		    

		    String outputFileName2 = resultLocation + "generatedLR_allVisits.csv";
		    printoutLR = new PrintWriter(new File(outputFileName2));	 
		    printoutLR.println(disease+"_loglikelihood_M," + disease+"_loglikelihood_T");
		    getLikelihoods(classifier,filtedTrainData, filtedTestData);	   
		    printoutLR.close();
		    	    
		    
		    String outputFileName1 = resultLocation + "generatedProb_allVisits.csv";
		    printoutProb = new PrintWriter(new File(outputFileName1));   
		    printoutProb.println(disease+"_Prob_M," + disease+"_Prob_T");
		    evaluateModel(classifier,filtedTrainData, filtedTestData);
		    printoutProb.close();
		    
		    objIn.close();
		}//end disease loop
	}
	
	public static void saveLRProb(String thisYearString, String nextYearString) throws Exception {
		season = thisYearString;
		nextyear = nextYearString;
		
		Instances filtedTestData = new Instances (new BufferedReader(new FileReader(folderLoc + "1.intermediate_data/training_" + thisYearString + "_test_" + nextYearString + "_All_cleaned_union_" + threshold + ".arff")));		    
	
		for (String d:diseaseList) {
			disease = d;		
		    String resultLocation = folderLoc +"javaRunning_" + disease + "_" + season  + "_"+ nextYearString + "/union_feature_all_"+threshold+"_NB/";
		    String filtedTrainDataFile = resultLocation+"train.arff";	      	    
		    Instances filtedTrainData = new Instances (new BufferedReader(new FileReader(filtedTrainDataFile)));	
		    filtedTrainData.setClassIndex(filtedTrainData.numAttributes()-1);	
		    String filePath = resultLocation + "NB_new.model";
			FileInputStream fileIn = new FileInputStream(filePath);
		    ObjectInputStream objIn = new ObjectInputStream(fileIn);
		    NaiveBayes classifier = (NaiveBayes) objIn.readObject();
		    

		    String outputFileName2 = resultLocation + "generatedLR_allVisits.csv";
		    printoutLR = new PrintWriter(new File(outputFileName2));	 
		    printoutLR.println(disease+"_loglikelihood_M," + disease+"_loglikelihood_T");
		    getLikelihoods(classifier,filtedTrainData, filtedTestData);	   
		    printoutLR.close();
		    	    
		    
		    String outputFileName1 = resultLocation + "generatedProb_allVisits.csv";
		    printoutProb = new PrintWriter(new File(outputFileName1));   
		    printoutProb.println(disease+"_Prob_M," + disease+"_Prob_T");
		    evaluateModel(classifier,filtedTrainData, filtedTestData);
		    printoutProb.close();
		    
		    objIn.close();
		}//end disease loop
	}
	
	
	
	public static Instances useFilterInfoGain2(String indexListString, Instances inputData) throws Exception {
		System.out.println("original data: number of variables" + inputData.numAttributes()); 
		inputData.setClassIndex(inputData.numAttributes()-1);
		Remove oneRemoveFilter = new Remove();	
		String BNoption = "-R " + indexListString;
		String[] options = BNoption.split(" ");			
		oneRemoveFilter.setOptions(options);
	    oneRemoveFilter.setInputFormat(inputData);
	    
	    Instances filtedData = Filter.useFilter(inputData, oneRemoveFilter);  	    
	    System.out.println("filted data: number of variables" + filtedData.numAttributes());    
	    return filtedData;
	  }
	

	
//	public static void run(NaiveBayes bayesNet, Instances train, Instances test) throws Exception {
//		System.out.println("get likelihoods");
//		printoutLR.println("logT,logF");
//		//double[][] log_likelihood = 
//		getLikelihoods(bayesNet, train, test);
//	
////		System.out.println("get probabilities");	
////		printoutProb.println("probT,probF");
//////		double[][] preds = 
////		evaluateModel(bayesNet, train, test);
//		
//			
////		for (int i=0; i<log_likelihood.length; i++) {
////			//System.out.println(""+originalTrainData.instance(i).value(0));
////			//printout.print(originalTrainData.instance(i).value(0)+",");
////			for (int j=0; j<log_likelihood[i].length;j++) {
////				printout.print(String.format("%.4f", preds[i][j])+",");
////			}
////			for (int j=0; j<log_likelihood[i].length-1;j++) {
////				printout.print(log_likelihood[i][j]+",");
////			}
////			printout.println(log_likelihood[i][log_likelihood[i].length-1]);
////		}
//	}
//	

 
	
	public static double[][] getLikelihoods(NaiveBayes bayesNet, Instances train, Instances data) throws Exception  {
	    // for predictions printing
      int nInstances = data.numInstances();
      int nNumClasses = train.numClasses();
      double[][] log_likelihood = new double[nInstances][nNumClasses];  
      Estimator[][] m_Distributions = bayesNet.getConditionalEstimators();
      //
      for (int i = 0; i < data.numInstances(); i++) {
    	Instance oneInstance = data.instance(i);
        log_likelihood[i] = getLogLikelihoodForInstance(m_Distributions, train, oneInstance, nNumClasses);
        for (int j=0; j<log_likelihood[i].length-1;j++) {
        	printoutLR.print(log_likelihood[i][j]+",");
        	//System.out.print(log_likelihood[i][j]+",");
		}
        printoutLR.println(log_likelihood[i][log_likelihood[i].length-1]);
        //System.out.println(log_likelihood[i][log_likelihood[i].length-1]);
       }	
      printoutLR.flush();
	 return log_likelihood;
	}
	
	/**
     * Calculates log_likelihoods
     * instance.
     * 
     * @param bayesNet the bayes net to use
     * @param instance the instance to be classified
     * @return predicted class probability distribution
     * @throws Exception if there is a problem generating the prediction
     */
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
    
    
	public static void evaluateModel(NaiveBayes bayesNet, Instances train, Instances data) throws Exception  {
	    // for predictions printing
      int nInstances = data.numInstances();
      int nNumClasses = train.numClasses();
      double[][] preds = new double[nInstances][nNumClasses];
      
      Estimator[][] m_Distributions = bayesNet.getConditionalEstimators();    
      Estimator m_ClassDistribution = bayesNet.getClassEstimator();
      // 
      for (int i = 0; i < data.numInstances(); i++) {
    	Instance oneInstance = data.instance(i);
        preds[i] = distributionForInstance(m_Distributions, m_ClassDistribution, train, oneInstance,  nNumClasses);   
        for (int j=0; j< preds[i].length-1;j++) {
        	printoutProb.print(String.format("%.12f", preds[i][j])+",");
        	//printoutProb.print(preds[i][j]+",");
        	//System.out.print(String.format("%.4f", preds[i][j])+",");
		}        
        printoutProb.println(String.format("%.12f", preds[i][preds[i].length-1]));
        //printoutProb.println(preds[i][preds[i].length-1]);   
       // System.out.println(String.format("%.4f", preds[i][preds[i].length-1])); 		
      }	    
      printoutProb.flush();
	  //return preds;
	}
    
	
	 /**
     * Calculates the class membership probabilities for the given test
     * instance.
     * 
     * @param bayesNet the bayes net to use
     * @param instance the instance to be classified
     * @return predicted class probability distribution
     * @throws Exception if there is a problem generating the prediction
     */
    public static double[] distributionForInstance(Estimator[][] m_Distributions, Estimator m_ClassDistribution, Instances train, Instance instance, int nNumClasses) throws Exception {
        Instances instances = train;        
        double[] fProbs = new double[nNumClasses];

        for (int iClass = 0; iClass < nNumClasses; iClass++) {
            fProbs[iClass] = 1.0;
        }

        for (int iClass = 0; iClass < nNumClasses; iClass++) {
            double logfP = 0;

            for (int iAttribute = 0; iAttribute < instances.numAttributes(); iAttribute++) {
                double iCPT = 0;
                iCPT = iCPT * nNumClasses + iClass;
                if (iAttribute == instances.classIndex()) {
                    logfP += Math.log(m_ClassDistribution.getProbability(iClass));
                } else {
                	String name_iAttribute = train.attribute(iAttribute).name();
                	int tempIndex = -1;
                	for (int index=0; index<instance.numAttributes(); index++){
                		if (instance.attribute(index).name().equals(name_iAttribute)){
                			tempIndex = index;
                			break;
                		}
                	}
                	 logfP
                           += Math.log(m_Distributions[iAttribute][(int) iCPT].getProbability(instance.value(tempIndex)));
                }
            }
            fProbs[iClass] += logfP;
        }

        // Find maximum
        double fMax = fProbs[0];
        for (int iClass = 0; iClass < nNumClasses; iClass++) {
            if (fProbs[iClass] > fMax) {
                fMax = fProbs[iClass];
            }
        }
        // transform from log-space to normal-space
        for (int iClass = 0; iClass < nNumClasses; iClass++) {
            fProbs[iClass] = Math.exp(fProbs[iClass] - fMax);
        }

        // Display probabilities
        Utils.normalize(fProbs);

        return fProbs;
    } // distributionForInstance
    
    

}
