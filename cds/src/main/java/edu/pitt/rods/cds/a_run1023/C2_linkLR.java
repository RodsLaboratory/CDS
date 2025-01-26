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

import java.util.*;

import edu.pitt.rods.cds.utility.Configuration;

import java.io.*;



public class C2_linkLR {
	
	static ArrayList<String> diseaseList;
	static String folderLoc;	
	static double threshold;
	static HashMap<String,String> labelTable;
	static HashMap<String,String> aDateTable;
	
	public static void main(String[] args) throws Exception {
		String property_file_name = "properties10172023.txt";		
		folderLoc=Configuration.getExperimentFolderLocation(property_file_name);	
		labelTable = getLabelTable(folderLoc + "0.original_data/PDS_ICD_lab_additionallab_combine_labels_10162023_updateCOV.csv");
		aDateTable = new HashMap<String,String>();
		getADate(folderLoc + "0.original_data/data07122023_discrete_add_label_2012.csv");
		getADate(folderLoc + "0.original_data/data07122023_discrete_add_label_2013.csv");
		getADate(folderLoc + "0.original_data/data07122023_discrete_add_label_2014.csv");
		getADate(folderLoc + "0.original_data/data07122023_discrete_add_label_2015.csv");
		getADate(folderLoc + "0.original_data/data07122023_discrete_add_label_2016.csv");
		getADate(folderLoc + "0.original_data/data07122023_discrete_add_label_2017.csv");
		getADate(folderLoc + "0.original_data/data07122023_discrete_add_label_2018.csv");
		getADate(folderLoc + "0.original_data/data07122023_discrete_add_label_2019.csv");
		getADate(folderLoc + "0.original_data/data07122023_discrete_add_label_2020.csv");
		getADate(folderLoc + "0.original_data/data07122023_discrete_add_label_2021.csv");
		
		ArrayList<String> featureUnionStringList = new ArrayList<String>();
		featureUnionStringList.add("2012-2016"); featureUnionStringList.add("2013-2017"); featureUnionStringList.add("2014-2018"); 
		featureUnionStringList.add("2015-2019"); featureUnionStringList.add("2016-2020");	
		
		ArrayList<String> trainYearDiseasesList = new ArrayList<String>();
		trainYearDiseasesList.add("2012-2016"); trainYearDiseasesList.add("2013-2017"); trainYearDiseasesList.add("2014-2018"); 
		trainYearDiseasesList.add("2015-2019"); trainYearDiseasesList.add("2016-2020");
		
		ArrayList<String> trainYearOtherList = new ArrayList<String>();
		trainYearOtherList.add("2015-2016"); trainYearOtherList.add("2016-2017"); trainYearOtherList.add("2017-2018"); 
		trainYearOtherList.add("2018-2019"); trainYearOtherList.add("2019-2020");
		
		ArrayList<String> testYearList = new ArrayList<String>(); 			
		testYearList.add("2016-2017"); testYearList.add("2017-2018"); testYearList.add("2018-2019"); 
		testYearList.add("2019-2020"); testYearList.add("2020-2021");	

		ArrayList<Double> thresholdList = new ArrayList<Double>();
		thresholdList.add(0.001);
		
		for (Double t:thresholdList) {
			threshold = t;	
			for (int i=0; i<testYearList.size(); i++) {	
				String featureUnionString = featureUnionStringList.get(i);
				String trainYearDisease  = trainYearDiseasesList.get(i);
				String trainYearOther = trainYearOtherList.get(i);
				String testYear = testYearList.get(i);
				saveResult(featureUnionString, trainYearDisease, trainYearOther, testYear);
			}
		}
	}
	
	
	public static void saveResult(String featureUnionString, String trainYearDisease, String trainYearOther, String testYear) throws Exception {		
		System.out.println("run" + testYear);
		
		Scanner input = new Scanner(new File(folderLoc +"1.intermediate_data/IDList_" + testYear + ".csv"));
		Scanner input1 = new Scanner(new File(folderLoc +"javaRunning_INFLUENZA_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
		Scanner input2 = new Scanner(new File(folderLoc +"javaRunning_RSV_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
		Scanner input3 = new Scanner(new File(folderLoc +"javaRunning_HMPV_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
		Scanner input4 = new Scanner(new File(folderLoc +"javaRunning_PARAINFLUENZA_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
		Scanner input5 = new Scanner(new File(folderLoc +"javaRunning_ADENOVIRUS_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
		Scanner input6 = new Scanner(new File(folderLoc +"javaRunning_ENTEROVIRUS_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
		Scanner input7 = new Scanner(new File(folderLoc +"javaRunning_OTHER_" + trainYearOther + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
		
		Scanner input1b = new Scanner(new File(folderLoc +"javaRunning_INFLUENZA_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
		Scanner input2b = new Scanner(new File(folderLoc +"javaRunning_RSV_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
		Scanner input3b = new Scanner(new File(folderLoc +"javaRunning_HMPV_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
		Scanner input4b = new Scanner(new File(folderLoc +"javaRunning_PARAINFLUENZA_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
		Scanner input5b = new Scanner(new File(folderLoc +"javaRunning_ADENOVIRUS_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
		Scanner input6b = new Scanner(new File(folderLoc +"javaRunning_ENTEROVIRUS_" + trainYearDisease + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
		Scanner input7b = new Scanner(new File(folderLoc +"javaRunning_OTHER_" + trainYearOther + "_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
		
		String outputFileName1 = folderLoc + "3.results/LR_test_" + testYear + "_" + threshold + ".csv";   
	    PrintWriter printout = new PrintWriter(new File(outputFileName1));  

		
		if (!testYear.equals("2020-2021")) {
		    String printLine = input.nextLine() + ",SEASON,ICD_INFLUENZA,LAB_INFLUENZA,LABEL_INFLUENZA,ICD_RSV,LAB_RSV,"
					+ "LABEL_RSV,ICD_HMPV,LAB_HMPV,LABEL_HMPV,ICD_ADENOVIRUS,LAB_ADENOVIRUS,"
					+ "LABEL_ADENOVIRUS,ICD_ENTEROVIRUS,LAB_ENTEROVIRUS,LABEL_ENTEROVIRUS,"
					+ "ICD_PARAINFLUENZA,LAB_PARAINFLUENZA,LABEL_PARAINFLUENZA,ICD_COV,LAB_COV,LABEL_COV,"
					+ "LAB_INFLUENZA_ADDITIONAL,LAB_RSV_ADDITIONAL,LAB_HMPV_ADDITIONAL,LAB_PARAINFLUENZA_ADDITIONAL,"
					+ "LAB_ENTEROVIRUS_ADDITIONAL,LAB_ADENOVIRUS_ADDITIONAL,LABEL_INFLUENZA_NEW,LABEL_RSV_NEW,LABEL_PARAINFLUENZA_NEW,"
					+ "LABEL_HMPV_NEW,LABEL_ENTEROVIRUS_NEW,LABEL_ADENOVIRUS_NEW,Admit_date_time,"
					+ input1.nextLine()+","+ input2.nextLine()+","+input3.nextLine()+","
					+ input4.nextLine()+","+input5.nextLine()+","+ input6.nextLine()+","+input7.nextLine()
					+"," + input1b.nextLine()+","+ input2b.nextLine()+","+input3b.nextLine()+","
					+ input4b.nextLine()+","+input5b.nextLine()+","+ input6b.nextLine()+","+input7b.nextLine();	    
		    printout.println(printLine);
		    while (input.hasNext()) {
				String ID = input.nextLine();
				printLine = ID ;
				if (labelTable.containsKey(ID)) {
					printLine = printLine + "," + labelTable.get(ID);
				}
				if (aDateTable.containsKey(ID)) {
					printLine = printLine + "," + aDateTable.get(ID);
				}
				printLine = printLine  +"," + input1.nextLine()+","+ input2.nextLine()+","+input3.nextLine()+","
						+ input4.nextLine()+","+input5.nextLine()+","+ input6.nextLine()+","+input7.nextLine()
						+"," + input1b.nextLine()+","+ input2b.nextLine()+","+input3b.nextLine()+","
						+ input4b.nextLine()+","+input5b.nextLine()+","+ input6b.nextLine()+","+input7b.nextLine();
				
				printout.println(printLine);
			}
			
			
		}
		else {
			Scanner input8 = new Scanner(new File(folderLoc +"javaRunning_COV_2019-2020_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
			Scanner input8b = new Scanner(new File(folderLoc +"javaRunning_COV_2019-2020_" + testYear + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
			
			String printLine = input.nextLine() + ",SEASON,ICD_INFLUENZA,LAB_INFLUENZA,LABEL_INFLUENZA,ICD_RSV,LAB_RSV,"
					+ "LABEL_RSV,ICD_HMPV,LAB_HMPV,LABEL_HMPV,ICD_ADENOVIRUS,LAB_ADENOVIRUS,"
					+ "LABEL_ADENOVIRUS,ICD_ENTEROVIRUS,LAB_ENTEROVIRUS,LABEL_ENTEROVIRUS,"
					+ "ICD_PARAINFLUENZA,LAB_PARAINFLUENZA,LABEL_PARAINFLUENZA,ICD_COV,LAB_COV,LABEL_COV,"
					+ "LAB_INFLUENZA_ADDITIONAL,LAB_RSV_ADDITIONAL,LAB_HMPV_ADDITIONAL,LAB_PARAINFLUENZA_ADDITIONAL,"
					+ "LAB_ENTEROVIRUS_ADDITIONAL,LAB_ADENOVIRUS_ADDITIONAL,LABEL_INFLUENZA_NEW,LABEL_RSV_NEW,LABEL_PARAINFLUENZA_NEW,"
					+ "LABEL_HMPV_NEW,LABEL_ENTEROVIRUS_NEW,LABEL_ADENOVIRUS_NEW,Admit_date_time,"
					+ input1.nextLine()+","+ input2.nextLine()+","+input3.nextLine()+","
					+ input4.nextLine()+","+input5.nextLine()+","+ input6.nextLine()+","+input7.nextLine()
					+"," + input1b.nextLine()+","+ input2b.nextLine()+","+input3b.nextLine()+","
					+ input4b.nextLine()+","+input5b.nextLine()+","+ input6b.nextLine()+","+input7b.nextLine()+","+input8.nextLine()+","+input8b.nextLine();	    
		    printout.println(printLine);
		    while (input.hasNext()) {
				String ID = input.nextLine();
				printLine = ID ;
				if (labelTable.containsKey(ID)) {
					printLine = printLine + "," + labelTable.get(ID);
				}
				if (aDateTable.containsKey(ID)) {
					printLine = printLine + "," + aDateTable.get(ID);
				}
				printLine = printLine  +"," + input1.nextLine()+","+ input2.nextLine()+","+input3.nextLine()+","
						+ input4.nextLine()+","+input5.nextLine()+","+ input6.nextLine()+","+input7.nextLine()
						+"," + input1b.nextLine()+","+ input2b.nextLine()+","+input3b.nextLine()+","
						+ input4b.nextLine()+","+input5b.nextLine()+","+ input6b.nextLine()+","+input7b.nextLine()+","+input8.nextLine()+","+input8b.nextLine();		
				printout.println(printLine);
			}
		    input8.close(); input8b.close();
		}
		input.close(); input1.close(); input2.close(); input3.close(); input4.close(); input5.close(); input6.close(); input7.close();
		printout.flush();
		printout.close();			
	}
	
	public static void getADate(String inputFileName) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputFileName));
		input.nextLine(); //ignore header
		while (input.hasNext()) {
			String oneLine = input.nextLine();
			String[] valueList = oneLine.split(",",3);
			String ID = valueList[0];
			String adate = valueList[1];
			if (!aDateTable.containsKey(ID)) {
				aDateTable.put(ID, adate);
			}
		}
	}
	
	//
	/* 
	 * read ID,labels as a table (ID,...)
	 * The PDS_ICD_lab_combine_labels_06262023 file contains following:
	 * 
	 * ENCOUNTER_DEID,STUDY_ID,ID,SEASON,ICD_INFLUENZA,LAB_INFLUENZA,LABEL_INFLUENZA,ICD_RSV,LAB_RSV,LABEL_RSV,
	 * ICD_HMPV,LAB_HMPV,LABEL_HMPV,ICD_ADENOVIRUS,LAB_ADENOVIRUS,LABEL_ADENOVIRUS,
	 * ICD_ENTEROVIRUS,LAB_ENTEROVIRUS,LABEL_ENTEROVIRUS,ICD_PARAINFLUENZA,LAB_PARAINFLUENZA,LABEL_PARAINFLUENZA,
	 * ICD_COV,LAB_COV,LABEL_COV
	 */
	 public static HashMap<String,String> getLabelTable(String labelFileName) throws FileNotFoundException{
		HashMap<String,String> table = new HashMap<String,String>();
		Scanner input = new Scanner(new File(labelFileName));
		input.nextLine(); //skip first line
		while (input.hasNext()) {			
			String oneLine = input.nextLine();
			//System.out.println("One line: " + oneLine);
			String[] stringList = oneLine.split(",",-1);
			//System.out.println("length:" + stringList.length);
			String ID = stringList[0];
			String other = stringList[1];
			for (int i=2; i<=stringList.length-1;i++) {
				if (stringList[i].length()==0){
					other = other+",M";
				}
				else {
					other = other+"," + stringList[i];
				}
				
			}
			table.put(ID, other);
			//System.out.println(ID+"|" + other);
		}
		return table; 
	 }
	
	
	 /*
	  * add labels to each csv
	  */
//	  public static void addLabel(HashMap<String,String> table, String inputFileName, String outputFileName) throws FileNotFoundException {
//		  Scanner input = new Scanner(new File(inputFileName));
//		  PrintWriter printout = new PrintWriter(new File(outputFileName));
//		  printout.println(input.nextLine()+",SEASON,ICD_INFLUENZA,LAB_INFLUENZA,LABEL_INFLUENZA,"
//		  		+ "ICD_RSV,LAB_RSV,LABEL_RSV,ICD_HMPV,LAB_HMPV,LABEL_HMPV,"
//		  		+ "ICD_ADENOVIRUS,LAB_ADENOVIRUS,LABEL_ADENOVIRUS,"
//		  		+ "ICD_ENTEROVIRUS,LAB_ENTEROVIRUS,LABEL_ENTEROVIRUS,"
//		  		+ "ICD_PARAINFLUENZA,LAB_PARAINFLUENZA,LABEL_PARAINFLUENZA,"
//		  		+ "ICD_COV,LAB_COV,LABEL_COV");
//		  while (input.hasNext()){
//			  String oneLine = input.nextLine();
//			  String ID = oneLine.substring(0,oneLine.indexOf(","));
//			  if (table.containsKey(ID)) {
//				  oneLine = oneLine + ","+table.get(ID);
//			  }
//			  else {
//				  System.out.println("not found " + ID);
//			  }
//			  printout.println(oneLine);
//		  }
//		  printout.flush();
//	  }
		
	

}
