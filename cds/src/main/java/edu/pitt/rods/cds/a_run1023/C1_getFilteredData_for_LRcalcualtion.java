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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import edu.pitt.rods.cds.utility.Configuration;
//import weka.classifiers.evaluation.output.prediction.AbstractOutput;
//import weka.core.BatchPredictor;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class C1_getFilteredData_for_LRcalcualtion {
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
		
//		diseaseList = new ArrayList<String>();
//		diseaseList.add("INFLUENZA"); diseaseList.add("RSV");	diseaseList.add("ADENOVIRUS"); diseaseList.add("PARAINFLUENZA"); 
//		diseaseList.add("HMPV"); diseaseList.add("ENTEROVIRUS"); diseaseList.add("OTHER"); 
		
		
		ArrayList<String> thisYearList = new ArrayList<String>();
	//	thisYearList.add("2012-2016"); 	
		thisYearList.add("2013-2017"); thisYearList.add("2014-2018"); 
		thisYearList.add("2015-2019"); thisYearList.add("2016-2020");
		
		
		ArrayList<String> nextYearList = new ArrayList<String>(); 	
		//nextYearList.add("2016-2017"); 
		nextYearList.add("2017-2018"); nextYearList.add("2018-2019"); 
		nextYearList.add("2019-2020"); nextYearList.add("2020-2021");
		
		
		ArrayList<Double> thresholdList = new ArrayList<Double>();
		thresholdList.add(0.001);

		
		getFilteredData(thisYearList, nextYearList, thresholdList);
		
	}
	
	public static void getFilteredData(ArrayList<String> thisYearList, ArrayList<String> nextYearList, ArrayList<Double> thresholdList) throws Exception {
		for (int i=0; i<nextYearList.size(); i++) {	
			String nextYearString = nextYearList.get(i);
			String testDataFile = folderLoc + "1.intermediate_data/training_" + nextYearString + "_All_cleaned.arff";
			//String testDataFile = folderLoc + "1.intermediate_data/check.arff";
			System.out.println(testDataFile);
			Instances testData = new Instances (new BufferedReader(new FileReader(testDataFile)));
			String thisYearString = thisYearList.get(i);
			System.out.println("this year: " + thisYearString + " next year: " + nextYearString);
			int totalNumberFeature = testData.numAttributes()-1;
			
			for (Double t:thresholdList) {
				threshold = t;					
				//int totalNumberFeature = 26007; // 26014;
				String removeIndexString = getRemoveIndex(totalNumberFeature, folderLoc + "3.results/union_feature_" + threshold+"_" + thisYearString +"_all.csv");
				Instances filtedTestData = useFilterInfoGain2(removeIndexString, testData);
				
				ArffSaver saver = new ArffSaver();
				saver.setInstances(filtedTestData);
				saver.setFile(new File(folderLoc + "1.intermediate_data/training_" + thisYearString + "_test_" + nextYearString + "_All_cleaned_union_" + threshold + ".arff"));
				saver.writeBatch();		
			}//end season loop
		}//end threshold loop
	}
	
	
	
	
	public static String getRemoveIndex(int totalNumberFeature, String fileName) throws FileNotFoundException {
		ArrayList<String> keepIndexList = new ArrayList<String>();
		ArrayList<String> removeIndexList = new ArrayList<String>();		
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
