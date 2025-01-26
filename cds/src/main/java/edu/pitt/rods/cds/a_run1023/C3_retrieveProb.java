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



public class C3_retrieveProb {
	
	static ArrayList<String> diseaseList;
	static String folderLoc;	
	static double threshold;
	static HashMap<String,String> labelTable;
	static HashMap<String,String> aDateTable;
	static PrintWriter printout;
	
	public static void main(String[] args) throws Exception {
		String property_file_name = "properties.txt";		
		folderLoc=Configuration.getExperimentFolderLocation(property_file_name);
		threshold = 0.001;
		
	
		ArrayList<String> diseaseList = new ArrayList<String>();
		diseaseList.add("INFLUENZA"); 
		diseaseList.add("RSV");	
		diseaseList.add("ADENOVIRUS"); 
		diseaseList.add("PARAINFLUENZA"); 
		diseaseList.add("HMPV"); 
		diseaseList.add("ENTEROVIRUS"); 
		diseaseList.add("OTHER"); 
		
		for (String d: diseaseList) {
			String disease = d;
			HashMap<Integer,String> table1 = getProbTable(disease, "2012-2013");
			HashMap<Integer,String> table2 = getProbTable(disease, "2011-2013");	
			printProb2(disease, "2013-2014","2012-2013", "2011-2013", table1, table2);
		}
		
		for (String d: diseaseList) {
			String disease = d;
			HashMap<Integer,String> table1 = getProbTable(disease, "2013-2014");
			HashMap<Integer,String> table2 = getProbTable(disease, "2012-2014");	
			HashMap<Integer,String> table3 = getProbTable(disease, "2011-2014");	
			printProb3(disease, "2014-2015","2013-2014", "2012-2014", "2011-2014", table1, table2, table3);
		}
		
		for (String d: diseaseList) {
			String disease = d;
			HashMap<Integer,String> table1 = getProbTable(disease, "2014-2015");
			HashMap<Integer,String> table2 = getProbTable(disease, "2013-2015");	
			HashMap<Integer,String> table3 = getProbTable(disease, "2012-2015");	
			HashMap<Integer,String> table4 = getProbTable(disease, "2011-2015");	
			printProb4(disease, "2015-2016","2014-2015", "2013-2015", "2012-2015", "2011-2015", table1, table2, table3, table4);
		}
		
			
//		labelTable = getLabelTable(folderLoc + "0.original_data/PDS_ICD_lab_combine_labels_07022023.csv");
//		aDateTable = new HashMap<String,String>();
//		getADate(folderLoc + "0.original_data/data06292023_discrete_2011.csv");
//		getADate(folderLoc + "0.original_data/data06292023_discrete_2012.csv");
//		getADate(folderLoc + "0.original_data/data06292023_discrete_2013.csv");
//		getADate(folderLoc + "0.original_data/data06292023_discrete_2014.csv");
//		getADate(folderLoc + "0.original_data/data06292023_discrete_2015.csv");
//		getADate(folderLoc + "0.original_data/data06292023_discrete_2016.csv");		
				
//		ArrayList<String> thisYearList = new ArrayList<String>();
//		//thisYearList.add("2011-2012"); 	thisYearList.add("2012-2013"); thisYearList.add("2013-2014"); thisYearList.add("2014-2015"); 
//		thisYearList.add("2011-2013"); thisYearList.add("2011-2014"); thisYearList.add("2012-2014"); thisYearList.add("2013-2015"); 
//		thisYearList.add("2012-2015"); thisYearList.add("2011-2015");
//		
//		ArrayList<String> nextYearList = new ArrayList<String>(); 	
//		//nextYearList.add("2012-2013"); //removed 7557804395_5391839032587		nextYearList.add("2013-2014"); 
//		//nextYearList.add("2014-2015"); nextYearList.add("2015-2016"); 
//		nextYearList.add("2013-2014"); nextYearList.add("2014-2015"); nextYearList.add("2014-2015"); nextYearList.add("2015-2016"); 
//		nextYearList.add("2015-2016"); nextYearList.add("2015-2016");
//
//		ArrayList<Double> thresholdList = new ArrayList<Double>();
////		thresholdList.add(new Double("0.0")); 
//		thresholdList.add(new Double("0.001")); 
////		thresholdList.add(new Double("0.0001")); 
//		
//		String disease = "INFLUENZA";
//		
//		for (Double t:thresholdList) {
//			threshold = t;	
//			for (int i=0; i<nextYearList.size(); i++) {	
//				String testYear = nextYearList.get(i);
//				String modelSeason = thisYearList.get(i);
//				printProb(disease, Boolean.TRUE, modelSeason, testYear);
//				printProb(disease, Boolean.FALSE, modelSeason, testYear);
//			}
//		}
	}
	public static void printProb2(String disease, String testYear, String trainYear1, String trainYear2, HashMap<Integer,String> table1, HashMap<Integer,String> table2) throws FileNotFoundException {
		PrintWriter printout = new PrintWriter(new File(folderLoc + "3.results/prob_compare_" + disease + "_model_for_test_" + testYear+"_v2.csv"));
		printout.println("label,prob_T_" + trainYear1 + ",prob_M_" + trainYear1 + ",prob_T_" + trainYear2 + ",prob_M_" + trainYear2);
		int length = table1.size();
		for (int i=1; i<=length; i++) {
			printout.println(table1.get(i)+","+table2.get(i).split(",",2)[1]);
		}
		printout.flush();
		printout.close();
	}
	
	public static void printProb3(String disease, String testYear, String trainYear1, String trainYear2, String trainYear3, 
			HashMap<Integer,String> table1, HashMap<Integer,String> table2, HashMap<Integer,String> table3) throws FileNotFoundException {
		PrintWriter printout = new PrintWriter(new File(folderLoc + "3.results/prob_compare_" + disease + "_model_for_test_" + testYear+"_v2.csv"));
		printout.println("label,prob_T_" + trainYear1 + ",prob_M_" + trainYear1 + ",prob_T_" + trainYear2 + ",prob_M_" + trainYear2+ ",prob_T_" + trainYear3 + ",prob_M_" + trainYear3);
		int length = table1.size();
		for (int i=1; i<=length; i++) {
			printout.println(table1.get(i)+","+table2.get(i).split(",",2)[1]+","+table3.get(i).split(",",2)[1]);
		}
		printout.flush();
		printout.close();
	}
	
	public static void printProb4(String disease, String testYear, String trainYear1, String trainYear2, String trainYear3, String trainYear4, 
			HashMap<Integer,String> table1, HashMap<Integer,String> table2, HashMap<Integer,String> table3, HashMap<Integer,String> table4
			) throws FileNotFoundException {
		PrintWriter printout = new PrintWriter(new File(folderLoc + "3.results/prob_compare_" + disease + "_model_for_test_" + testYear+"_v2.csv"));
		printout.println("label,prob_T_" + trainYear1 + ",prob_M_" + trainYear1 + ",prob_T_" + trainYear2 + ",prob_M_" + trainYear2
				+ ",prob_T_" + trainYear3 + ",prob_M_" + trainYear3 + ",prob_T_" + trainYear4 + ",prob_M_" + trainYear4);
		int length = table1.size();
		for (int i=1; i<=length; i++) {
			printout.println(table1.get(i)+","+table2.get(i).split(",",2)[1]+","+table3.get(i).split(",",2)[1]+","+table4.get(i).split(",",2)[1]);
		}
		printout.flush();
		printout.close();
	}
	
	
	
//	public static void printProb(String disease, Boolean firstFile, String modelSeason, String testYear) throws FileNotFoundException {
//		String inputFileName =  folderLoc + "javaRunning_" + disease + "_" + modelSeason +  "/union_feature_all_"+threshold+"_NB/result.csv";
//		Scanner input = new Scanner(new File(inputFileName));
//		//while (inp);
//	}
	
	public static HashMap<Integer,String> getProbTable(String disease, String modelSeason) throws FileNotFoundException{
		HashMap<Integer,String> table = new HashMap<Integer,String>();
		String inputFileName =  folderLoc + "javaRunning_" + disease + "_" + modelSeason +  "/union_feature_all_"+threshold+"_NB/result_v2.csv";
		Scanner input = new Scanner(new File(inputFileName));
		Boolean begin = Boolean.FALSE;
		int count = 0;
		while (input.hasNext()) {	
			String oneLine = input.nextLine();	
			if (begin) {
				count++;
				table.put(count,oneLine);
			}
			else if (!begin) {
				if (oneLine.contains("predication")) {
					begin = Boolean.TRUE;
				}
				
			}	
		}
		return table; 
	}
		
//	public static void saveResult(String modelSeason, String testYear) throws Exception {		
//		System.out.println("run" + testYear);
//		Scanner input = new Scanner(new File(folderLoc +"1.intermediate_data/IDList_" + testYear + ".csv"));
//		Scanner input1 = new Scanner(new File(folderLoc +"javaRunning_INFLUENZA_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
//		Scanner input2 = new Scanner(new File(folderLoc +"javaRunning_RSV_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
//		Scanner input3 = new Scanner(new File(folderLoc +"javaRunning_HMPV_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
//		Scanner input4 = new Scanner(new File(folderLoc +"javaRunning_PARAINFLUENZA_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
//		Scanner input5 = new Scanner(new File(folderLoc +"javaRunning_ADENOVIRUS_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
//		Scanner input6 = new Scanner(new File(folderLoc +"javaRunning_ENTEROVIRUS_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
//		Scanner input7 = new Scanner(new File(folderLoc +"javaRunning_OTHER_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedLR_allVisits.csv"));
//		
//		Scanner input1b = new Scanner(new File(folderLoc +"javaRunning_INFLUENZA_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
//		Scanner input2b = new Scanner(new File(folderLoc +"javaRunning_RSV_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
//		Scanner input3b = new Scanner(new File(folderLoc +"javaRunning_HMPV_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
//		Scanner input4b = new Scanner(new File(folderLoc +"javaRunning_PARAINFLUENZA_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
//		Scanner input5b = new Scanner(new File(folderLoc +"javaRunning_ADENOVIRUS_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
//		Scanner input6b = new Scanner(new File(folderLoc +"javaRunning_ENTEROVIRUS_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
//		Scanner input7b = new Scanner(new File(folderLoc +"javaRunning_OTHER_" + modelSeason + "/union_feature_all_"+threshold+"_NB/generatedProb_allVisits.csv"));
//		
//		String outputFileName1 = folderLoc + "3.results/LR_train_" + modelSeason + "_test_" + testYear + "_" + threshold + "_v2.csv";   
//	    PrintWriter printout = new PrintWriter(new File(outputFileName1));  
//
//	    String printLine = input.nextLine() + "," + input1.nextLine()+","+ input2.nextLine()+","+input3.nextLine()+","
//				+ input4.nextLine()+","+input5.nextLine()+","+ input6.nextLine()+","+input7.nextLine()
//				+"," + input1b.nextLine()+","+ input2b.nextLine()+","+input3b.nextLine()+","
//				+ input4b.nextLine()+","+input5b.nextLine()+","+ input6b.nextLine()+","+input7b.nextLine()
//				+ ",SEASON,ICD_INFLUENZA,LAB_INFLUENZA,LABEL_INFLUENZA,ICD_RSV,LAB_RSV,LABEL_RSV,ICD_HMPV,LAB_HMPV,"
//				+ "LABEL_HMPV,ICD_ADENOVIRUS,LAB_ADENOVIRUS,LABEL_ADENOVIRUS,"
//				+ "ICD_ENTEROVIRUS,LAB_ENTEROVIRUS,LABEL_ENTEROVIRUS,"
//				+ "ICD_PARAINFLUENZA,LAB_PARAINFLUENZA,LABEL_PARAINFLUENZA,"
//				+ "ICD_COV,LAB_COV,LABEL_COV,Admit_date_time";
//	    printLine = printLine.replaceAll("log","logLikelihood_");
//	    printout.println(printLine);
//
//		while (input.hasNext()) {
//			String ID = input.nextLine();
//			printLine = ID +"," + input1.nextLine()+","+ input2.nextLine()+","+input3.nextLine()+","
//					+ input4.nextLine()+","+input5.nextLine()+","+ input6.nextLine()+","+input7.nextLine()
//					+"," + input1b.nextLine()+","+ input2b.nextLine()+","+input3b.nextLine()+","
//					+ input4b.nextLine()+","+input5b.nextLine()+","+ input6b.nextLine()+","+input7b.nextLine();
//			if (labelTable.containsKey(ID)) {
//				printLine = printLine + "," + labelTable.get(ID);
//			}
//			if (aDateTable.containsKey(ID)) {
//				printLine = printLine + "," + aDateTable.get(ID);
//			}
//			printout.println(printLine);
//		}
//		input.close(); input1.close(); input2.close(); input3.close(); input4.close(); input5.close(); input6.close(); input7.close();
//		printout.flush();
//		printout.close();
//			
//	}
//	
//	public static void getADate(String inputFileName) throws FileNotFoundException {
//		Scanner input = new Scanner(new File(inputFileName));
//		input.nextLine(); //ignore header
//		while (input.hasNext()) {
//			String oneLine = input.nextLine();
//			String[] valueList = oneLine.split(",",3);
//			String ID = valueList[0];
//			String adate = valueList[1];
//			if (!aDateTable.containsKey(ID)) {
//				aDateTable.put(ID, adate);
//			}
//		}
//	}
//	
//	//
//	/* 
//	 * read ID,labels as a table (ID,...)
//	 * The PDS_ICD_lab_combine_labels_06262023 file contains following:
//	 * 
//	 * ENCOUNTER_DEID,STUDY_ID,ID,SEASON,ICD_INFLUENZA,LAB_INFLUENZA,LABEL_INFLUENZA,ICD_RSV,LAB_RSV,LABEL_RSV,
//	 * ICD_HMPV,LAB_HMPV,LABEL_HMPV,ICD_ADENOVIRUS,LAB_ADENOVIRUS,LABEL_ADENOVIRUS,
//	 * ICD_ENTEROVIRUS,LAB_ENTEROVIRUS,LABEL_ENTEROVIRUS,ICD_PARAINFLUENZA,LAB_PARAINFLUENZA,LABEL_PARAINFLUENZA,
//	 * ICD_COV,LAB_COV,LABEL_COV
//	 */
//	 public static HashMap<String,String> getLabelTable(String labelFileName) throws FileNotFoundException{
//		HashMap<String,String> table = new HashMap<String,String>();
//		Scanner input = new Scanner(new File(labelFileName));
//		input.nextLine(); //skip first line
//		while (input.hasNext()) {			
//			String oneLine = input.nextLine();
//			//System.out.println("One line: " + oneLine);
//			String[] stringList = oneLine.split(",",-1);
//			//System.out.println("length:" + stringList.length);
//			String ID = stringList[0];
//			String other = stringList[1];
//			for (int i=2; i<=stringList.length-1;i++) {
//				if (stringList[i].length()==0){
//					other = other+",M";
//				}
//				else {
//					other = other+"," + stringList[i];
//				}
//				
//			}
//			table.put(ID, other);
//			//System.out.println(ID+"|" + other);
//		}
//		return table; 
//	 }
//	
//	
//	 /*
//	  * add labels to each csv
//	  */
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
