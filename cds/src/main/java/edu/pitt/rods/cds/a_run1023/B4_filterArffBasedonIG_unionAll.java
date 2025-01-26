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

import java.io.*;
import java.util.*;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove ;
import edu.pitt.rods.cds.utility.Configuration;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVSaver;

public class B4_filterArffBasedonIG_unionAll {
	static String disease;
	static String folderLoc;
	static String season;
	static double threshold;
	static PrintWriter bnResult;
	static String year;
	static String nextYear;
	static String featureUnionString;
	static Boolean unionAll;
	
	public static void main(String[] args) throws Exception {
		unionAll = Boolean.TRUE;
		ArrayList<Double> thresholdList = new ArrayList<Double>();
		//thresholdList.add(new Double("0.0")); thresholdList.add(new Double("0.0001"));
		
		thresholdList.add(0.001);
		
		for (Double t:thresholdList) {
			threshold = t;
			ArrayList<String> diseaseList = new ArrayList<String>();
			diseaseList.add("INFLUENZA"); 
			diseaseList.add("RSV");	diseaseList.add("ADENOVIRUS"); 
			diseaseList.add("PARAINFLUENZA"); diseaseList.add("HMPV"); diseaseList.add("ENTEROVIRUS"); 
			//diseaseList.add("OTHER"); diseaseList.add("COV"); 
			
			for (String d:diseaseList) {
				disease = d;
				ArrayList<String> diseaseYearList = new ArrayList<String>();
//				diseaseYearList.add("2012-2016"); diseaseYearList.add("2013-2017"); diseaseYearList.add("2014-2018"); 
//				diseaseYearList.add("2015-2019"); 
				diseaseYearList.add("2016-2020");
				
				
				ArrayList<String> testYearList = new ArrayList<String>();
//				testYearList.add("2016-2017"); testYearList.add("2017-2018"); testYearList.add("2018-2019"); 
//				testYearList.add("2019-2020"); 
				testYearList.add("2020-2021"); 
			
				for (int i=0; i<diseaseYearList.size(); i++) {	
					year = diseaseYearList.get(i);
					nextYear = testYearList.get(i);
					filterTrainTestForDiseases();
				}
			}	
			
//			disease = "OTHER";
//			ArrayList<String> otherYearList = new ArrayList<String>();
//			otherYearList.add("2015-2016"); otherYearList.add("2016-2017"); otherYearList.add("2017-2018"); 
//			otherYearList.add("2018-2019"); otherYearList.add("2019-2020");
//			
//			ArrayList<String> featureUnionStringList = new ArrayList<String>();
//			featureUnionStringList.add("2012-2016"); featureUnionStringList.add("2013-2017"); featureUnionStringList.add("2014-2018"); 
//			featureUnionStringList.add("2015-2019"); featureUnionStringList.add("2016-2020");
//			
//			ArrayList<String> testYearList = new ArrayList<String>();
//			testYearList.add("2016-2017"); testYearList.add("2017-2018"); testYearList.add("2018-2019"); 
//			testYearList.add("2019-2020"); testYearList.add("2020-2021"); 
//			
//			for (int i=0; i<otherYearList.size(); i++) {	
//				year = otherYearList.get(i);
//				nextYear = testYearList.get(i);
//				featureUnionString = featureUnionStringList.get(i);
//				filterTrainTestForOTHER();
//			}
			
//			disease = "COV";
//			ArrayList<String> otherYearList = new ArrayList<String>();
//			otherYearList.add("2019-2020");
//			
//			ArrayList<String> featureUnionStringList = new ArrayList<String>();
//			featureUnionStringList.add("2016-2020");
//			
//			ArrayList<String> testYearList = new ArrayList<String>();
//			testYearList.add("2020-2021"); 
//			
//			for (int i=0; i<otherYearList.size(); i++) {	
//				year = otherYearList.get(i);
//				nextYear = testYearList.get(i);
//				featureUnionString = featureUnionStringList.get(i);
//				filterTrainTestForOTHER();
//			}
			
		}	
	}
	
	
	
	public static void filterTrainTestForOTHER() throws Exception {
		String property_file_name = "properties10172023.txt";		
		folderLoc=Configuration.getExperimentFolderLocation(property_file_name);			
		
		String originalTrainFile =  folderLoc + "1.intermediate_data/training_" + year + "_" + disease + "_cleaned.arff";
		String originalTestFile = folderLoc + "1.intermediate_data/training_" + nextYear + "_" + disease + "_cleaned.arff";
		
		System.out.println("train file: " + originalTrainFile);
		System.out.println("test file: " + originalTestFile);
		Instances trainData = new Instances (new BufferedReader(new FileReader(originalTrainFile)));	
		Instances testData = new Instances (new BufferedReader(new FileReader(originalTestFile)));	
		
		int totalNumberFeature = trainData.numAttributes()-1;
		String removeIndexString = "";
		if (!unionAll) {
			removeIndexString = getRemoveIndex(totalNumberFeature, folderLoc + "3.results/union_feature_" + threshold+"_" + featureUnionString+".csv");
		}
		//if unionAll
		else {
			removeIndexString = getRemoveIndex(totalNumberFeature, folderLoc + "3.results/union_feature_" + threshold+"_" + featureUnionString+"_all.csv");
		}
		
		
		ArrayList<Instances> filtedDataList = useFilterInfoGain(removeIndexString, trainData, testData);
		
		String resultLocation= "";
		if (!unionAll) {
			resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "_" + nextYear  + "/union_feature_"+threshold+"_NB/";
		}
		//if unionAll
		else {
			resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "_" + nextYear  + "/union_feature_all_"+threshold+"_NB/";
		}
		createFolder(resultLocation);

		
		Instances filtedTrainData = filtedDataList.get(0);
		Instances filtedTestData = filtedDataList.get(1);
		
		filtedTrainData.setClassIndex(filtedTrainData.numAttributes()-1);
		filtedTestData.setClassIndex(filtedTestData.numAttributes()-1);
		
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(filtedTrainData);
		saver.setFile(new File(resultLocation+"train.arff"));
		saver.writeBatch();
		
		saver.setInstances(filtedTestData);
		saver.setFile(new File(resultLocation+"test.arff"));
		saver.writeBatch();
					
		CSVSaver csvsaver = new CSVSaver();
		csvsaver.setFieldSeparator(",");
		csvsaver.setInstances(filtedTrainData);
		File tmpFile = new File(resultLocation+"train.csv");
		csvsaver.setFile(tmpFile);
		csvsaver.writeBatch();
		
		csvsaver.setInstances(filtedTestData);
		tmpFile = new File(resultLocation+"test.csv");
		csvsaver.setFile(tmpFile);
		csvsaver.writeBatch();
		
	}
	
	public static void filterTrainTestForDiseases() throws Exception {
		String property_file_name = "properties10172023.txt";		
		folderLoc=Configuration.getExperimentFolderLocation(property_file_name);			
		
		String originalTrainFile =  folderLoc + "1.intermediate_data/training_" + year + "_" + disease + "_cleaned.arff";
		String originalTestFile = folderLoc + "1.intermediate_data/training_" + nextYear + "_" + disease + "_cleaned.arff";
		
		System.out.println("train file: " + originalTrainFile);
		System.out.println("test file: " + originalTestFile);
		Instances trainData = new Instances (new BufferedReader(new FileReader(originalTrainFile)));	
		Instances testData = new Instances (new BufferedReader(new FileReader(originalTestFile)));	
		
		int totalNumberFeature = trainData.numAttributes()-1;
		String removeIndexString = "";
		if (!unionAll) {
			removeIndexString = getRemoveIndex(totalNumberFeature, folderLoc + "3.results/union_feature_" + threshold+"_" + year+".csv");
		}
		//if unionAll
		else {
			removeIndexString = getRemoveIndex(totalNumberFeature, folderLoc + "3.results/union_feature_" + threshold+"_" + year+"_all.csv");
		}
		
		
		ArrayList<Instances> filtedDataList = useFilterInfoGain(removeIndexString, trainData, testData);
		
		String resultLocation= "";
		if (!unionAll) {
			resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "_" + nextYear  + "/union_feature_"+threshold+"_NB/";
		}
		//if unionAll
		else {
			resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "_" + nextYear  + "/union_feature_all_"+threshold+"_NB/";
		}
		createFolder(resultLocation);

		
		Instances filtedTrainData = filtedDataList.get(0);
		Instances filtedTestData = filtedDataList.get(1);
		
		filtedTrainData.setClassIndex(filtedTrainData.numAttributes()-1);
		filtedTestData.setClassIndex(filtedTestData.numAttributes()-1);
		
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(filtedTrainData);
		saver.setFile(new File(resultLocation+"train.arff"));
		saver.writeBatch();
		
		saver.setInstances(filtedTestData);
		saver.setFile(new File(resultLocation+"test.arff"));
		saver.writeBatch();
					
		CSVSaver csvsaver = new CSVSaver();
		csvsaver.setFieldSeparator(",");
		csvsaver.setInstances(filtedTrainData);
		File tmpFile = new File(resultLocation+"train.csv");
		csvsaver.setFile(tmpFile);
		csvsaver.writeBatch();
		
		csvsaver.setInstances(filtedTestData);
		tmpFile = new File(resultLocation+"test.csv");
		csvsaver.setFile(tmpFile);
		csvsaver.writeBatch();
		
	}
	
	
	
	
//	public static void runOneYear() throws Exception {
//		String property_file_name = "properties.txt";		
//		folderLoc=Configuration.getExperimentFolderLocation(property_file_name);			
//		
//		String originalTrainFile =  folderLoc + "1.intermediate_data/training_" + year + "_" + disease + "_new_cleaned.arff";
//		String originalTestFile = folderLoc + "1.intermediate_data/training_" + nextYear + "_" + disease + "_new_cleaned.arff";
//		
//		System.out.println("train file: " + originalTrainFile);
//		System.out.println("test file: " + originalTestFile);
//		Instances trainData = new Instances (new BufferedReader(new FileReader(originalTrainFile)));	
//		Instances testData = new Instances (new BufferedReader(new FileReader(originalTestFile)));	
//		
//		int totalNumberFeature = trainData.numAttributes()-1;
//		String removeIndexString = "";
//		if (!unionAll) {
//			removeIndexString = getRemoveIndex(totalNumberFeature, folderLoc + "3.results/union_feature_" + threshold+"_" + year+".csv");
//		}
//		//if unionAll
//		else {
//			removeIndexString = getRemoveIndex(totalNumberFeature, folderLoc + "3.results/union_feature_" + threshold+"_" + year+"_all.csv");
//		}
//		
//		
//		ArrayList<Instances> filtedDataList = useFilterInfoGain(removeIndexString, trainData, testData);
//		
//		String resultLocation= "";
//		if (!unionAll) {
//			resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "/union_feature_"+threshold+"_NB/";
//		}
//		//if unionAll
//		else {
//			resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "/union_feature_all_"+threshold+"_NB/";
//		}
//		createFolder(resultLocation);
//
//		
//		Instances filtedTrainData = filtedDataList.get(0);
//		Instances filtedTestData = filtedDataList.get(1);
//		
//		filtedTrainData.setClassIndex(filtedTrainData.numAttributes()-1);
//		filtedTestData.setClassIndex(filtedTestData.numAttributes()-1);
//		
//		
//		ArffSaver saver = new ArffSaver();
//		saver.setInstances(filtedTrainData);
//		saver.setFile(new File(resultLocation+"train.arff"));
//		saver.writeBatch();
//		
//		saver.setInstances(filtedTestData);
//		saver.setFile(new File(resultLocation+"test.arff"));
//		saver.writeBatch();
//					
//		CSVSaver csvsaver = new CSVSaver();
//		csvsaver.setFieldSeparator(",");
//		csvsaver.setInstances(filtedTrainData);
//		File tmpFile = new File(resultLocation+"train.csv");
//		csvsaver.setFile(tmpFile);
//		csvsaver.writeBatch();
//		
//		csvsaver.setInstances(filtedTestData);
//		tmpFile = new File(resultLocation+"test.csv");
//		csvsaver.setFile(tmpFile);
//		csvsaver.writeBatch();
//		
//	}
	public static void createFolder(String folderName){
		File folderExisting = new File(folderName);	  
		if (!folderExisting.exists()){
			boolean success = (new File(folderName)).mkdirs();
			System.out.println("create folder " + folderName);
		}
	}
	

	 
	
	
	public static String getRemoveIndex(int totalNumberFeature, String fileName) throws FileNotFoundException {
		ArrayList<String> removeIndexList = new ArrayList<String>();
		ArrayList<String> keepIndexList = new ArrayList<String>();
		for (int i=1; i<=totalNumberFeature; i++) {
			removeIndexList.add(""+i);
		}
		Scanner input = new Scanner(new File(fileName));
		input.nextLine(); //ignore header
		while (input.hasNext()) {
			String[] valueList = input.nextLine().split(",");
			String previousIndex = valueList[2];
			if (removeIndexList.contains(previousIndex)) {
				removeIndexList.remove(previousIndex); //keep the feature in the dataset
				keepIndexList.add(previousIndex);
			}
		}
		input.close();
		String str = String.join(",", removeIndexList);
		String keepStr = String.join(",", keepIndexList);
		System.out.println("remove "+str);
		System.out.println("keep "+ keepStr);
		return str;
	}
	
	
		
	
	public static ArrayList<Instances> useFilterInfoGain(String indexListString, Instances inputData, Instances inputData2) throws Exception {
		System.out.println("original data: number of variables" + inputData.numAttributes()); 
		inputData.setClassIndex(inputData.numAttributes()-1);
		Remove oneRemoveFilter = new Remove();	
		String BNoption = "-R " + indexListString;
		String[] options = BNoption.split(" ");			
		oneRemoveFilter.setOptions(options);
	    oneRemoveFilter.setInputFormat(inputData);
	    
	    Instances filtedData = Filter.useFilter(inputData, oneRemoveFilter);  	    
	    System.out.println("filted data: number of variables" + filtedData.numAttributes()); 
	    Instances filtedData2 = Filter.useFilter(inputData2, oneRemoveFilter);  
	    ArrayList<Instances> output = new ArrayList<Instances>();
	    output.add(filtedData); output.add(filtedData2);
	    return output;
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
}
