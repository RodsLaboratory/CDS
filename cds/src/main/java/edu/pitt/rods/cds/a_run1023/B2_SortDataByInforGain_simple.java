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


/**run on July 7, 2023*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import edu.pitt.rods.cds.utility.Configuration;
import edu.pitt.rods.cds.utility.FeatureWithIG_CVMerit;
import weka.core.Instances;
import weka.core.converters.ArffSaver;


public class B2_SortDataByInforGain_simple {
	static String generalFileLocation;
	static String fileLocation;
	static ArrayList<FeatureWithIG_CVMerit> list;
	static ArrayList<FeatureWithIG_CVMerit> finalList;
	static HashMap<String,String> featureTable;
	static String season;
	static String disease;
//	static ArrayList<String> featureList;
	

	/*
	 ./data/d.arff/ ./result/bn.csv ./result/nn.csv ./result/svm.csv ./result/total.csv ./result/errorRateResultForInfoGain.txt ./result/errorRateResult.txt
	 */
	public static void main(String[] args) throws Exception {
	//	System.out.println("Provide trainDataName, testDataName, resultLocation, for example:");	
		//season = "2016-2017";
		
		ArrayList<String> diseaseList = new ArrayList<String>();
		diseaseList.add("INFLUENZA"); 
		diseaseList.add("RSV"); diseaseList.add("HMPV"); 
		diseaseList.add("ADENOVIRUS"); diseaseList.add("PARAINFLUENZA"); diseaseList.add("ENTEROVIRUS"); diseaseList.add("COV"); 
		diseaseList.add("OTHER");
		
		for (String d: diseaseList) {
			disease = d;
			String property_file_name = "properties10172023.txt";		
			generalFileLocation=Configuration.getExperimentFolderLocation(property_file_name);
			
			featureTable = new HashMap<String,String>();
			getFeatureTable("C_D_",generalFileLocation+"0.original_data/pds_dataset_umls_cui.txt");
			getFeatureTable("L_D_",generalFileLocation+"0.original_data/pds_dataset_loinc.txt");
			
			ArrayList<String> yearList = new ArrayList<String>();		
			yearList.add("2012-2016"); yearList.add("2013-2017"); yearList.add("2014-2018"); yearList.add("2015-2019"); yearList.add("2016-2020");
			yearList.add("2015-2016"); yearList.add("2016-2017"); yearList.add("2017-2018"); yearList.add("2018-2019"); yearList.add("2019-2020");
			
			for (int j=0; j<yearList.size(); j++) {
				String year= yearList.get(j);
				System.out.println("year:" + year );
			    run(year);			
			}
		}
		
	}
	
	public static void run(String year) throws FileNotFoundException {
		String file1 = generalFileLocation + "3.results/featureRanking_" + disease + "_" + year + ".txt";
		File f = new File(file1);
		if(f.exists()){	
			fileLocation=Configuration.getExperimentFolderLocation("properties10172023.txt") +"javaRunning_" + disease + "_" + year +  "/intermediate/";
			createFolder(fileLocation);		
			System.out.println(fileLocation);	
			sortIG_simple(file1, fileLocation+  "total_informationGainScore_CVMerit_simple.csv", fileLocation+  "informationGainScore_CVMerit_simple.csv");	
			addFeatureName(fileLocation + "total_informationGainScore_CVMerit_simple.csv", fileLocation + "total_informationGainScore_CVMerit_simple_description.csv");		

		}
    }
	

	
	public static void runOLD(String year, String nextyear) throws FileNotFoundException {
		fileLocation=Configuration.getExperimentFolderLocation("properties_" + disease.toLowerCase() + ".txt") +"javaRunning_" + disease + "_" + year +  "/intermediate/";
		createFolder(fileLocation);
		
		System.out.println(fileLocation);
		
//		String trainDataName = generalFileLocation+ "1.intermediate_data/training_"+ year + "_" + disease + "_cleaned.arff";	
//		String orderTrainDataName = fileLocation+ "ordered_findings_simple.arff";	
//		
//		String testDataName = generalFileLocation+ "1.intermediate_data/training_"+ nextyear + "_" + disease + "_cleaned.arff";	
//		String orderTestDataName = fileLocation+ "ordered_findings_test_simple.arff";	
		
     	sortIG_simple(generalFileLocation +  "3.results/featureRanking_" + disease + "_" + year + ".txt", fileLocation+  "total_informationGainScore_CVMerit_simple.csv", fileLocation+  "informationGainScore_CVMerit_simple.csv");	
		addFeatureName(fileLocation + "total_informationGainScore_CVMerit_simple.csv", fileLocation + "total_informationGainScore_CVMerit_simple_description.csv");		
		//putAgeLast(fileLocation+  "informationGainScore_CVMerit_simple.csv", fileLocation+  "inforGain_ageLast_simple.csv");
//		HashMap<Integer, Integer> table = getTable(fileLocation+  "informationGainScore_CVMerit_simple.csv");
//		sortData(trainDataName,orderTrainDataName,table);
//		sortData(testDataName,orderTestDataName,table);
	}
	
	public static void createFolder(String folderName){
		File folderExisting = new File(folderName);	  
		if (!folderExisting.exists()){
			boolean success = (new File(folderName)).mkdirs();
			System.out.println("create folder " + folderName);
		}
	}

	public static void addFeatureName(String inputFileName, String outputFileName) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputFileName));
		PrintWriter printout = new PrintWriter(new File(outputFileName));
		input.nextLine(); //ignore header
		printout.println("Feature,InformationGainScore,RawRank,PreviousIndex,Rank,description");
		while (input.hasNext()) {	
			String oneLine = input.nextLine();
			String[] valueList = oneLine.split(",");
			if (featureTable.containsKey(valueList[0])) {
				oneLine = oneLine+","+featureTable.get(valueList[0]);
			}
			printout.println(oneLine);
		}
		input.close();
		printout.flush();
		printout.close();
	}
	
	public static void getFeatureTable(String prefix, String inputFileName) throws FileNotFoundException{
		Scanner input = new Scanner(new File(inputFileName));
		input.nextLine(); //ignore header
		while (input.hasNext()) {
			String oneLine = input.nextLine();
			oneLine.replaceAll(",", "");
			oneLine.replaceAll(":", "");
			String[] valueList = oneLine.split("\\|");
			if (valueList.length>=2) {
				featureTable.put(prefix+valueList[0],valueList[1].replace(",", ""));
			}
		}
		input.close();
	}
	
	
	/**
	 * This method transforms the arff file into a sorted one to facilitate K2 learning with features and agegroup.
	 * The order is as follows:
	 * 
	 * @param originalDataName unsorted findings.arff
	 * @param orderDataName ordered_findings.arff based on information gain  
	 * @param table
	 * @throws FileNotFoundException
	 */
	static void sortData(String originalDataName,
			String orderDataName, HashMap<Integer, Integer> table) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(new File(originalDataName));
		ArrayList<String> nameList = new ArrayList<String>(); //contains items in the header of an arff file	
		input.nextLine(); 
		input.nextLine();
		while (input.hasNext()){
			String oneLine = input.nextLine();
			//System.out.println(oneLine);
			String code = oneLine.split("\\s+")[1];
			if (oneLine.contains("@attribute LABEL_")){
				break;
			}
			nameList.add(oneLine);	
		}
		nameList.add(new String("@attribute DISEASE {M,T}"));
		input.nextLine();
		PrintWriter printout = new PrintWriter(new File(orderDataName));
		printout.println("@relation " + orderDataName.substring(30));
		printout.println();	
		//finalList was filled by the previous sortIG method
		for (int i=0; i<finalList.size();i++){
			FeatureWithIG_CVMerit oneFeature = finalList.get(i);
			int previousIndex  = oneFeature.getPreviousIndex();
			printout.println(nameList.get(previousIndex));
			printout.flush();
		}	
		printout.println("@attribute DISEASE {M,T}");
		input.nextLine(); 
		//input.nextLine();
		printout.println();
		printout.println("@data");
		while (input.hasNext()){
			printout.println(getSortedRow(input.nextLine(),table));
		}
		input.close();
		printout.flush();
		printout.close();
	}
	
	

	/**
	 * This method transforms the arff file into a sorted one
	 * @param originalDataName nsorted findings.arff
	 * @param orderDataName ordered_findings.arff based on information gain  
	 * @param table
	 * @param rankedFeatureFile
	 * @throws FileNotFoundException
	 */
//	public static void sortDataByFeatureRankFile(String originalDataName,
//			String orderDataName, HashMap<Integer, Integer> table, String rankedFeatureFile) throws FileNotFoundException {
//		// TODO Auto-generated method stub
//		Scanner input = new Scanner(new File(originalDataName));
//		ArrayList<String> nameList = new ArrayList<String>(); //contains items in the header of an arff file
//		nameList.add(new String("@attribute category {O,N,I}"));
//		input.nextLine(); 
//		input.nextLine();
//		while (input.hasNext()){
//			String oneLine = input.nextLine();
//			String code = oneLine.split("\\s+")[1];
//			if (oneLine.contains("@attribute category {O,N,I}")){
//				break;
//			}
//			nameList.add(oneLine);		
//		}
//		input.nextLine();
//		PrintWriter printout = new PrintWriter(new File(orderDataName));
//		printout.println("@relation " + orderDataName.substring(30));
//		printout.println();
//		printout.println("@attribute category {O,N,I}");
//		
//		Scanner featureListInput = new Scanner(new File(rankedFeatureFile));
//		featureListInput.nextLine();  // no header
//		ArrayList<FeatureWithIG_CVMerit> featureList = new ArrayList<FeatureWithIG_CVMerit> ();
//		while (featureListInput.hasNext()){
//			 String[] values = featureListInput.nextLine().split(",");	 
//			 FeatureWithIG_CVMerit one = new  FeatureWithIG_CVMerit(values[0], values[1], values[4], values[3]);
//			 featureList.add(one);
//		}
//		featureListInput.close();
//
//		for (int i=0; i<featureList.size();i++){
//			FeatureWithIG_CVMerit oneFeature = featureList.get(i);
//			int previousIndex  = oneFeature.getPreviousIndex();
//			if (!oneFeature.getFeature().equals("agegroup")){
//				printout.println(nameList.get(previousIndex));
//			}
//			printout.flush();
//		}	
//		printout.println("@attribute agegroup {ls6,ge6ls65,ge65}");
//		input.nextLine(); 
//		//input.nextLine();
//		printout.println();
//		printout.println("@data");
//		while (input.hasNext()){
//			printout.println(getSortedRow(input.nextLine(),table));
//		}
//		input.close();
//		printout.flush();
//		printout.close();
//	}
	/**
	 * 
	 * @param inforMeritFile
	 * @return a hash table, pair (information gain rank, previousIndex)
	 * @throws FileNotFoundException
	 */
	public static HashMap<Integer, Integer> getTable(String inforMeritFile) throws FileNotFoundException{
		HashMap<Integer, Integer> table = new HashMap<Integer, Integer>();
		Scanner input = new Scanner(new File(inforMeritFile));
		input.nextLine();
		while (input.hasNext()){
			String oneLine = input.nextLine();
			String[] values = oneLine.split(",");
			table.put(Integer.parseInt(values[4]), Integer.parseInt(values[3]));
		}
		input.close();
		return table;
		
	}


	public static String getSortedRow(String old, HashMap<Integer, Integer> table){
		String[] values = old.split(",");
		String newRow = values[values.length-1]; // put category as first
		for (int i=0; i<table.size(); i++){
			int rank = i+1;
			if (table.containsKey(rank)){
				int previousIndex = table.get(rank)-1;
				newRow = newRow+ ","+values[previousIndex] ;
			}
		}
		return newRow;	
	}
	
	/**
	 * This method saves sorted feature list.
	 * @param featureRankFileFromWeka This file is the output of weka information gain method (simple IG), the file featureRanking.txt.
	 * @param sortedFile This file lists sorted features from the highest information gain to the lowest information gain.
	 * @param finalFile This file is a short version of sorted file by only keeping information gain > 0.
	 * @throws FileNotFoundException
	 */
	public static void sortIG_simple(String featureRankFileFromWeka, String sortedFile, String finalFile) throws FileNotFoundException{
		Scanner unsorted = new Scanner(new File(featureRankFileFromWeka));
		list = new ArrayList<FeatureWithIG_CVMerit> ();
		Boolean begin=false;
		Boolean stop=false;
		while (unsorted.hasNext()){
			String oneLine = unsorted.nextLine();	
			if (oneLine.length()==0 & begin) {
				stop = true;
			}
			if (begin & !stop){
			//	System.out.println(oneLine);
				String[] values = oneLine.split("\\s+");
				System.out.println(oneLine);
				System.out.println(values.length);		
				for (int k=0; k<values.length; k++) {
					System.out.println(values[k]);
				}
				if (values.length==4){
					FeatureWithIG_CVMerit one = new FeatureWithIG_CVMerit(values[3],values[1],"-1",values[2]);
					if (one.getIGvalue()>0){
						list.add(one);
					}
					
				}
			}
			if (oneLine.contains("Ranked attributes:")){
				begin=true;
			}
		}
		unsorted.close();
		PrintWriter printout = new PrintWriter(new File(sortedFile));
		PrintWriter printoutFinal = new PrintWriter(new File(finalFile));
		printout.println("Feature,InformationGainScore,RawRank,PreviousIndex,Rank");
		printoutFinal.println("Feature,InformationGainScore,RawRank,PreviousIndex,Rank");
		int rank=1;
		int rankInFinal=1;
		finalList = new ArrayList<FeatureWithIG_CVMerit> ();
		for (int i=0; i<list.size(); i++){
			FeatureWithIG_CVMerit one = list.get(i);
			 printout.println(one+","+rank);
			 if (one.getIGvalue()>0){
				 printoutFinal.println(one+","+rankInFinal);	 
				 finalList.add(one);
				 rankInFinal++;
			 }
			 printout.flush();
			 rank++;
		}
		printout.close();
		printoutFinal.close();
	}
	
	
	/**
	 * This method saves sorted feature list (keeping features whose IG > 0).
	 * @param featureRankFileFromWeka This file is the output of weka information gain method (10-fold cross-validation IG), the file featureRanking.txt.
	 * @param sortedFile This file lists sorted features from the highest information gain to the lowest information gain.
	 * @param finalFile This file is a short version of sorted file by only keeping information gain > 0.
	 * @throws FileNotFoundException
	 */
	public static void sortIG(String featureRankFileFromWeka, String sortedFile, String finalFile) throws FileNotFoundException{
		Scanner unsorted = new Scanner(new File(featureRankFileFromWeka));
		list = new ArrayList<FeatureWithIG_CVMerit> ();
		Boolean begin=false;
		while (unsorted.hasNext()){
			String oneLine = unsorted.nextLine();		
			if (begin){
			//	System.out.println(oneLine);
				String[] values = oneLine.split("\\s+");
				if (values.length>=9){
//					System.out.println(values[8]);
//					System.out.println(values[1]);
//					System.out.println(values[4]);
//					System.out.println(values[7]);
					FeatureWithIG_CVMerit one = new FeatureWithIG_CVMerit(values[8],values[1],values[4],values[7]);
					list.add(one);
				}
				else if (values.length<9){
//					System.out.println(values[7]);
//					System.out.println(values[1]);
//					System.out.println(values[4]);
//					System.out.println(values[6]);
					FeatureWithIG_CVMerit one = new FeatureWithIG_CVMerit(values[7],values[1],values[4],values[6]);
					list.add(one);
				}	
				
			}
			if (oneLine.contains("average merit      average rank  attribute")){
				begin=true;
			}
		}
		unsorted.close();
		PrintWriter printout = new PrintWriter(new File(sortedFile));
		PrintWriter printoutFinal = new PrintWriter(new File(finalFile));
		printout.println("Feature,InformationGainScore,RawRank,PreviousIndex,Rank");
		printoutFinal.println("Feature,InformationGainScore,RawRank,PreviousIndex,Rank");
		int rank=1;
		int rankInFinal=1;
		finalList = new ArrayList<FeatureWithIG_CVMerit> ();
		for (int i=0; i<list.size(); i++){
			FeatureWithIG_CVMerit one = list.get(i);
			 printout.println(one+","+rank);
			 if (one.getIGvalue()>0){
				 printoutFinal.println(one+","+rankInFinal);	 
				 finalList.add(one);
				 rankInFinal++;
			 }
			 printout.flush();
			 rank++;
		}
		printout.close();
		printoutFinal.close();
	}
	

	
	public static void saveInstance2Arff(Instances dataSet, String fileName) throws IOException{
		 ArffSaver saver = new ArffSaver();
		 saver.setInstances(dataSet);
		 saver.setFile(new File(fileName));
		 //saver.setDestination(new File("./data/test.arff"));   // **not** necessary in 3.5.4 and later
		  saver.writeBatch();
	}
}


