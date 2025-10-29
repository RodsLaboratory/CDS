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


package edu.pitt.rods.cds;

import java.io.*;
import java.util.*;

import edu.pitt.rods.cds.utility.Configuration;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.Utils;

public class GetNBmodel_testPerformance {
	static String disease;
	static String folderLoc;
	static String season;
	static double threshold;
	static PrintWriter bnResult;
	static String year;
	static String nextYear;
	static Boolean unionAll;
	
	public static void main(String[] args) throws Exception {
		unionAll = Boolean.TRUE;
		ArrayList<Double> thresholdList = new ArrayList<Double>();
		//thresholdList.add(new Double("0.0")); thresholdList.add(new Double("0.0001"));
		thresholdList.add(0.001);
		ArrayList<String> diseaseList = new ArrayList<String>();
		diseaseList.add("INFLUENZA"); 
		diseaseList.add("RSV");	
		diseaseList.add("ADENOVIRUS"); 
		diseaseList.add("PARAINFLUENZA"); 
		diseaseList.add("HMPV"); 
		diseaseList.add("ENTEROVIRUS"); 
		
		for (Double t:thresholdList) {
			threshold = t;
			for (String d:diseaseList) {
				disease = d;
				ArrayList<String> thisYearList = new ArrayList<String>();
				thisYearList.add("2012-2016"); thisYearList.add("2013-2017"); thisYearList.add("2014-2018");  
				thisYearList.add("2015-2019"); 
				thisYearList.add("2016-2020"); 

				
				ArrayList<String> nextYearList = new ArrayList<String>();
				nextYearList.add("2016-2017"); 	nextYearList.add("2017-2018"); nextYearList.add("2018-2019"); 
				nextYearList.add("2019-2020"); 
				nextYearList.add("2020-2021"); 
				
			
				for (int i=0; i<thisYearList.size(); i++) {
					year = thisYearList.get(i);
					nextYear = nextYearList.get(i);
					runOneYearTestDifferentYear();
				}	
			}
		}
		
		diseaseList = new ArrayList<String>();
		diseaseList.add("OTHER"); 		
		for (Double t:thresholdList) {
			threshold = t;
			for (String d:diseaseList) {
				disease = d;
				ArrayList<String> thisYearList = new ArrayList<String>();
				thisYearList.add("2015-2016"); thisYearList.add("2016-2017"); thisYearList.add("2017-2018");  
				thisYearList.add("2018-2019"); thisYearList.add("2019-2020"); 			
				ArrayList<String> nextYearList = new ArrayList<String>();
				nextYearList.add("2016-2017"); 	nextYearList.add("2017-2018"); nextYearList.add("2018-2019"); 
				nextYearList.add("2019-2020"); nextYearList.add("2020-2021"); 	
				for (int i=0; i<thisYearList.size(); i++) {
					year = thisYearList.get(i);
					nextYear = nextYearList.get(i);
					runOneYearTestDifferentYear();
				}	
			}
		}
		diseaseList = new ArrayList<String>();
		diseaseList.add("COV"); 	
		for (Double t:thresholdList) {
			threshold = t;
			for (String d:diseaseList) {
				disease = d;
				ArrayList<String> thisYearList = new ArrayList<String>();
				thisYearList.add("2019-2020"); 			
				ArrayList<String> nextYearList = new ArrayList<String>();
				nextYearList.add("2020-2021"); 	
				for (int i=0; i<thisYearList.size(); i++) {
					year = thisYearList.get(i);
					nextYear = nextYearList.get(i);
					runOneYearTestDifferentYear();
				}	
			}
		}
		
		
	}
	
	public static void runOneYearTestDifferentYear() throws Exception {
		String property_file_name = "properties.txt";		
		folderLoc=Configuration.getExperimentFolderLocation(property_file_name);			
		
	    String resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "_" + nextYear + "/union_feature_"+threshold+"_NB/";
	    
	    if (unionAll) {
	    	resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "_" + nextYear + "/union_feature_all_"+threshold+"_NB/";
	    }
	    createFolder(resultLocation);
	    
		String filtedTrainDataFile = resultLocation+"train.arff";
		String filtedTestDataFile = resultLocation+"test.arff";
		
		Instances filtedTrainData = new Instances (new BufferedReader(new FileReader(filtedTrainDataFile)));	
		Instances filtedTestData = new Instances (new BufferedReader(new FileReader(filtedTestDataFile)));	
		
		bnResult = new PrintWriter(new File(resultLocation+"result.csv"));
		String BNoption = " ";
		String[] BNoptions = BNoption.split(" ");
		bnResult.println(BNoption);
		String nameOfNetwork = resultLocation + "NB_new.xml"; 
		
		filtedTrainData.setClassIndex(filtedTrainData.numAttributes()-1);
		filtedTestData.setClassIndex(filtedTestData.numAttributes()-1);
		
		learnNBModel(nameOfNetwork,filtedTrainData, BNoptions); 	
		evaluateModel(nameOfNetwork.replace(".xml", ".model"), filtedTestData);
	}
	
	public static void createFolder(String folderName){
		File folderExisting = new File(folderName);	  
		if (!folderExisting.exists()){
			boolean success = (new File(folderName)).mkdirs();
			System.out.println("create folder " + folderName);
		}
	}
	
	
	
	
	
	public static void learnNBModel(String nameOfNetwork, Instances train, String[] options) throws Exception{ 
		String classname = "weka.classifiers.bayes.NaiveBayes";
		Classifier cls = (Classifier) Utils.forName(Classifier.class, classname, options);		
		cls.buildClassifier(train);
		NaiveBayes model = (NaiveBayes) cls;	
		String network = model.toString();		
		Print.print2File(network, nameOfNetwork);
			
		FileOutputStream fileOut = new FileOutputStream(nameOfNetwork.replace(".xml", ".model"));
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(model);
        objOut.flush();
        objOut.close();
		System.out.println("model saved!");		
	}
	
	public static Double evaluateModel(String modelName, Instances test) throws Exception {	
		String filePath = modelName;
		FileInputStream fileIn = new FileInputStream(filePath);
	    ObjectInputStream objIn = new ObjectInputStream(fileIn);
	    NaiveBayes classifier = (NaiveBayes) objIn.readObject();	
		Evaluation eval = new Evaluation(test);	
		eval.evaluateModel(classifier, test);		
		bnResult.println("\n---------------Naive Bayes---------------");
		bnResult.println(eval.toSummaryString("\nSummary\n===\n", true));
		bnResult.println(eval.toClassDetailsString("\nDetailed Accuracy By Class \n===\n"));
		bnResult.println("------------predication-----------");
		
		for (int i=0; i<=test.numInstances()-1; i++){		
			double[] predications = classifier.distributionForInstance(test.instance(i));
			bnResult.print(test.instance(i).stringValue(test.instance(i).numAttributes()-1) + "," );		
			//bnResult.print(String.format("%.4f", predications[0]) + "," + String.format("%.4f",predications[1]) + "\n");
			bnResult.print(predications[0] + "," + predications[1] + "\n");
		}
		bnResult.close();
		System.out.println("model tested!");
		objIn.close();
		return eval.errorRate();
	}
	
	
	
    
}
