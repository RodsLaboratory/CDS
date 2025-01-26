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


import edu.pitt.rods.cds.utility.Configuration;



public class A1_0getCleanFile {
	static String folderLoc;
	static HashMap<Integer,String> locTable;
	static String season;
	static String disease;
	static int ageIndex;
	static int ethnicityIndex;
	static int raceIndex;
	
	public static void main(String[] args) throws Exception {
		/*Please edit the test_properties file to the folder that has two directories: data and intermediate_data*/		
		//disease = args[0];   //for example "INFLUENZA"
		 
		ArrayList<String> diseaseList = new ArrayList<String>();		
//		diseaseList.add("INFLUENZA"); 	
//		diseaseList.add("RSV");	
//		diseaseList.add("ADENOVIRUS"); 		
//		diseaseList.add("PARAINFLUENZA"); 
//		diseaseList.add("HMPV"); 
//		diseaseList.add("ENTEROVIRUS"); 
		diseaseList.add("OTHER"); 
	

		for (String diseaseName:diseaseList) {
			disease = diseaseName;
			runMultiSeason(); 
		}
		
	}
	
	public static void runMultiSeason() throws FileNotFoundException {
		ArrayList<String> seasonList = new ArrayList<String>();
//		seasonList.add("2012-2013");
//		seasonList.add("2013-2014");  seasonList.add("2014-2015"); 
//		seasonList.add("2015-2016"); seasonList.add("2016-2017"); seasonList.add("2017-2018"); 
//		seasonList.add("2018-2019"); 
//		seasonList.add("2019-2020");
		seasonList.add("2020-2021");
		
		
		for (String s:seasonList) {
			season = s;
			String property_file_name = "properties10172023.txt";		
			folderLoc=Configuration.getExperimentFolderLocation(property_file_name);

			String inputCSVFile = folderLoc + "1.intermediate_data/data07122023_" + disease + "_" + season + ".csv";
			
			/*find classVariable*/
			String ClassVariable = Configuration.getClassVariable(property_file_name) + disease;
			System.out.println("classVariableName: " + ClassVariable);
			int classIndex = getClassVariableIndex(inputCSVFile, ClassVariable);
			System.out.println("classIndex:" + classIndex);
			
			/*find removeVariableList*/
			ArrayList<String> removeVariableList = Configuration.getRemoveVariableList(property_file_name);	
			ArrayList<Integer> removeIndexList = getRemoveVariableIndex(removeVariableList, inputCSVFile);
			
			/*find ageVariable*/
			ageIndex = getAgeIndex(inputCSVFile);
			ethnicityIndex = getETHNICITYIndex(inputCSVFile);
			raceIndex = getRACEIndex(inputCSVFile);
			
			/*ETHNICITY: 
			 * HISPANIC_OR_LATINO
			 * NOT_HISPANIC_OR_LATINO 
			 * NOT_SPECIFIED: NOT_SPECIFIED, DECLINED
			 * M: not filling any value */
			
			/*RACE: 
			 * WHITE
			 * BLACK
			 * NOT_SPECIFIED: DECLINED, DNU_OTHER, DNU_UNKNOWN, UNKNOWN, NOT_SPECIFIED
			 * OTHER: ALASKA_NATIVE,AMERICAN_INDIAN,ASIAN,CHINESE,FILIPINO,GUAM/CHAMORRO,HAWAIIAN,INDIAN,INDIAN_(ASIAN),JAPANESE,KOREAN,OTHER_ASIAN,OTHER_PACIFIC_ISLANDER,SAMOAN,VIETNAMESE
			 * M: not filling
			 * */		
			/*Discretize age, put class variable the last */
			String outputCSVFile = folderLoc + "1.intermediate_data/training_" + season + "_" + disease + "_cleaned.csv"; 
			getCleanedFile(removeIndexList, classIndex, inputCSVFile, outputCSVFile);
					
			/*Print header */
//			HashMap<Integer,ArrayList<String>> table =  getHashTableIgnoreID(outputCSVFile);
//			String headerFile = folderLoc + "1.intermediate_data/header_" + season + "_" + disease + ".txt"; 
//			printHeaderHasID(table, headerFile);
		}
		
		
	}
	
	public static int getETHNICITYIndex(String inputCSVFile) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputCSVFile));
		String header = input.nextLine();
		String[] variables = header.split(",");
		for (int i=0; i<variables.length; i++) {
			if (variables[i].contains("ETHNICITY")) {
				return i;
			}
		}
		input.close();
		return -1;
	}
	
	public static int getRACEIndex(String inputCSVFile) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputCSVFile));
		String header = input.nextLine();
		String[] variables = header.split(",");
		for (int i=0; i<variables.length; i++) {
			if (variables[i].contains("RACE")) {
				return i;
			}
		}
		input.close();
		return -1;
	}
	

	
	
	
	
	

	public static int getClassVariableIndex(String inputCSVFile, String ClassVariable) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputCSVFile));
		String header = input.nextLine();
		String[] variables = header.split(",");
		for (int i=0; i<variables.length; i++) {
			if (ClassVariable.equals("LABEL_COV")) {
				if (variables[i].contains(ClassVariable)) {
					return i;
				}
			}
			else {
				if (variables[i].contains(ClassVariable+"_NEW")) {
					return i;
				}
			}
		}
		input.close();
		return -1;
	}
	
	
	public static ArrayList<Integer> getRemoveVariableIndex(ArrayList<String> removeVariableList, String inputCSVFile) throws FileNotFoundException {
		ArrayList<Integer> removeIndexList = new ArrayList<Integer>();
		Scanner input = new Scanner(new File(inputCSVFile));
		String header = input.nextLine();
		String[] variables = header.split(",");
		for (int i=0; i<variables.length; i++) {
			if (removeVariableList.contains(variables[i])) {
				System.out.println(variables[i]+","+i);
				removeIndexList.add(i);
			}
		}
		input.close();
		return removeIndexList;
	}
	
	public static int getAgeIndex(String inputCSVFile) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputCSVFile));
		String header = input.nextLine();
		String[] variables = header.split(",");
		for (int i=0; i<variables.length; i++) {
			if (variables[i].contains("AGE")) {
				return i;
			}
		}
		input.close();
		return -1;
	}
	

	
	
	public static void getCleanedFile(ArrayList<Integer> removeIndexList, int classIndex, String inputCSVFile, String outputCSVFile) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputCSVFile));
		PrintWriter printout = new PrintWriter(new File(outputCSVFile));
		int nLine = 0;	
		String tempClassString = new String("");
		while (input.hasNext()) {
			nLine++;
			String oneLine = input.nextLine();
			String[] valueList = oneLine.split(",",-1);
			String newString = "";
			//System.out.println("total length: " + valueList.length);
			for (int i=0; i<valueList.length; i++) {	
				if (i==0 & !valueList[i].contains("ID")) {
					newString = newString + valueList[i].replace("_", "0000000000")+",";
				}
				else if (i==ageIndex & !valueList[i].contains("AGE")) {
					//System.out.println(valueList[i]);
					if (valueList[i].contains("M") | valueList[i].length()==0) {
						newString = newString + "M" +",";
					}
					else {
						if (Double.parseDouble(valueList[i])<5) {
							newString = newString + "less5" +",";
						}
						else if (Double.parseDouble(valueList[i])<18) {
							newString = newString + "ge5less18" +",";
						}
						else if (Double.parseDouble(valueList[i])<65) {
							newString = newString + "ge18less65" +",";
						}
						else if (Double.parseDouble(valueList[i])>=65) {
							newString = newString + "ge65" +",";
						}	
					}
				}
				
				/*ETHNICITY: 
				 * HISPANIC_OR_LATINO
				 * NOT_HISPANIC_OR_LATINO 
				 * NOT_SPECIFIED: NOT_SPECIFIED, DECLINED
				 * M: not filling any value */
				
				else if (i==ethnicityIndex & !valueList[i].contains("ETHNICITY")) {
					//System.out.println(valueList[i]);
					if (valueList[i].equals("M") | valueList[i].length()==0) {
						newString = newString + "M" +",";
					}
					else if (valueList[i].equals("DECLINED") | valueList[i].equals("NOT_SPECIFIED") | valueList[i].contains("UNKNOWN")) {
						newString = newString + "NOT_SPECIFIED" +",";
					}
					else {
						newString = newString + valueList[i] +",";
					}	
				}
				
				/*RACE: 
				 * WHITE
				 * BLACK
				 * NOT_SPECIFIED: NOT_SPECIFIED, DECLINED, DNU_OTHER, DNU_UNKNOWN, UNKNOWN, 
				 * OTHER: ALASKA_NATIVE,AMERICAN_INDIAN,ASIAN,CHINESE,FILIPINO,GUAM/CHAMORRO,HAWAIIAN,INDIAN,INDIAN_(ASIAN),JAPANESE,KOREAN,OTHER_ASIAN,OTHER_PACIFIC_ISLANDER,SAMOAN,VIETNAMESE
				 * M: not filling
				 * */
				
				else if (i==raceIndex & !valueList[i].contains("RACE")) {
					//System.out.println(valueList[i]);
					if (valueList[i].equals("M") | valueList[i].length()==0) {
						newString = newString + "M" +",";
					}
					else if (valueList[i].equals("WHITE") | valueList[i].equals("BLACK")) {
						newString = newString + valueList[i] +",";
					}
					else if (valueList[i].equals("NOT_SPECIFIED") | valueList[i].equals("DECLINED") | valueList[i].contains("UNKNOWN") | valueList[i].contains("DNU_")) {
						newString = newString + "NOT_SPECIFIED" +",";
					}
					else {
						newString = newString + "OTHER" +",";
					}	
				}
				
				else if (!removeIndexList.contains(i) & i!=classIndex) {	
					if (valueList[i].length()==0) {
						newString = newString +"M,";
					}
					else {
						newString = newString + valueList[i]+",";
					}
					
				}
				else if (i==classIndex) {
					tempClassString = valueList[i];
				}
			}
			//System.out.println(newString+tempClassString);
			printout.println((newString+tempClassString).replaceAll("\\s+", "_"));
		}
		input.close();
		printout.flush();
		printout.close();
	}
	
//	public static HashMap<Integer,ArrayList<String>> getHashTable(String inputCSVFile) throws FileNotFoundException {
//		HashMap<Integer,ArrayList<String>> table = new HashMap<Integer,ArrayList<String>>();
//		locTable = new HashMap<Integer,String>();
//		Scanner input = new Scanner(new File(inputCSVFile));
//		String[] header = input.nextLine().split(",");
//		for (int i=0; i<header.length; i++) {
//			table.put(i, new ArrayList<String>());
//			locTable.put(i, header[i]);
//		}
//		int nLine=0;
//		while (input.hasNext()) {
//			nLine++;
//			//System.out.println("nLine"+nLine);
//			String oneLine = input.nextLine();
//			//System.out.println(oneLine);
//			String[] valueList = oneLine.split(",");
//			for (int i=0; i<valueList.length; i++) {
//				String oneValue = valueList[i];
//				//System.out.println("i:"+i + " oneValue:" + oneValue);
//				if (table.containsKey(i)) {
//					ArrayList<String> tempValueList = table.get(i);
//					if (oneValue.length()>0){
//						if (!tempValueList.contains(oneValue)) {
//							tempValueList.add(oneValue);
//						}
//						table.put(i, tempValueList);
//					}
//				}
//			}
//		}
//		input.close();
//		return table;	
//	}
	public static HashMap<Integer,ArrayList<String>> getHashTableIgnoreID(String inputCSVFile) throws FileNotFoundException {
		HashMap<Integer,ArrayList<String>> table = new HashMap<Integer,ArrayList<String>>();
		locTable = new HashMap<Integer,String>();
		Scanner input = new Scanner(new File(inputCSVFile));
		String[] header = input.nextLine().split(",");
		int IDindex = -1;
		for (int i=0; i<header.length; i++) {
			if (!header[i].equals("ID")) {
				table.put(i, new ArrayList<String>());
				locTable.put(i, header[i]);
			}
			else if (header[i].equals("ID")) {
				IDindex = i;
			}
		}
		int nLine=0;
		while (input.hasNext()) {
			nLine++;
			String oneLine = input.nextLine();
			String[] valueList = oneLine.split(",");
			for (int i=0; i<valueList.length; i++) {
				if (i==IDindex) {
					//ignore ID;
				}
				else {
					String oneValue = valueList[i];
					ArrayList<String> tempValueList = table.get(i);
					if (!tempValueList.contains(oneValue)) {
						tempValueList.add(oneValue);
					}
					table.put(i, tempValueList);
				}
			}
		}
		input.close();
		return table;	
	}
	
	public static void printHeaderHasID(HashMap<Integer,ArrayList<String>> finalTable, String outputFileName) throws FileNotFoundException {
		PrintWriter printout = new PrintWriter(new File(outputFileName));
		printout.println("@relation training_" + season + "_" + disease + "_cleaned");
		printout.println();		
		printout.println("@attribute ID Numeric");
		List<Integer> finalTableKeyList = new ArrayList<>(finalTable.keySet());
		Collections.sort(finalTableKeyList);	
		for (int i=0; i<finalTableKeyList.size(); i++) {
			int oneLocationKey = finalTableKeyList.get(i);	
			String nameVariable = locTable.get(oneLocationKey);
			System.out.println(nameVariable);
			ArrayList<String> list = finalTable.get(oneLocationKey);
			Collections.sort(list);
			String listString = String.join(",", list);
			printout.println("@attribute " + nameVariable + " {" + listString + "}");
		}
		printout.flush();
		printout.close();
	}
	
//	public static void PrintHeader(HashMap<Integer,ArrayList<String>> finalTable, String outputFileName) throws FileNotFoundException {
//		PrintWriter printout = new PrintWriter(new File(outputFileName));
//		printout.println("@relation training_2011to2012_RSV_cleaned");
//		printout.println();
//		for (int i=0; i<finalTable.size(); i++) {
//			String nameVariable = locTable.get(i);
//			System.out.println(nameVariable);
//			ArrayList<String> list = finalTable.get(i);
//			Collections.sort(list);
//			String listString = String.join(",", list);
//			printout.println("@attribute " + nameVariable + " {" + listString + "}");
//		}
//		printout.flush();
//		printout.close();
//	}
	public static void createArff(String inputFileName, String headerFileName, String outputFileName) throws FileNotFoundException {
		Scanner inputHeader = new Scanner(new File(headerFileName));
		Scanner inputData= new Scanner(new File(inputFileName));
		PrintWriter printout = new PrintWriter(new File(outputFileName));
		while (inputHeader.hasNext()) {
			printout.println(inputHeader.nextLine());
		}
		inputHeader.close();
		while (inputData.hasNext()) {
			printout.println(inputData.nextLine());
		}
		inputData.close();
	}

	public static void processHeader(String inputFileName, String outputFileName, HashMap<String,String> arffFileStringTable) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputFileName));
		PrintWriter printout = new PrintWriter(new File(outputFileName));	
		List<String> keyList = new ArrayList<String>(arffFileStringTable.keySet());		
		while (input.hasNext()) {
			String oneLine = input.nextLine();
			System.out.println("oneLine:" + oneLine);
			for (int i=0; i<keyList.size(); i++) {
				String key = keyList.get(i);
				System.out.println("key:" + key);
				String value = arffFileStringTable.get(key);
				if (oneLine.contains("@attribute") & oneLine.contains(key)) {
					String[] valueList = oneLine.split("\\s+");
					oneLine = "";
					for (int j=0; j<valueList.length-1; j++) {
						oneLine = oneLine + valueList[j] + " ";
					}
					oneLine = oneLine + value;
				}
			}
			printout.println(oneLine);
		}
		input.close();
		printout.flush();
		printout.close();
	}
	
	public static void consolidateMultiHeaderFiles(ArrayList<String> fileNameList, String outputFile) throws FileNotFoundException {
		HashMap<String, ArrayList<String>> attributeTable = new HashMap<String, ArrayList<String>>();
		ArrayList<String> attributeList = new ArrayList<String>();
		for (int i=0; i<fileNameList.size(); i++) {
			String oneFileName = fileNameList.get(i);
			Scanner oneInput = new Scanner(new File(oneFileName));
			while (oneInput.hasNext()) {
				String oneLine = oneInput.nextLine();
				if (oneLine.contains("@attribute")) {
					String[] valueList = oneLine.split("\\s+");
					String name = valueList[1];
					if (!name.equals("ID")) {
						if (i==0) {
							attributeList.add(name);
						}
						String valueString = valueList[2].replace("{", "").replace("}", "");
						ArrayList<String> list = new ArrayList<String>();
						for (String s: valueString.split(",")) {
							list.add(s);
						}
						if (attributeTable.containsKey(name)) {
							ArrayList<String> currentList = attributeTable.get(name);
							for (String s:list) {
								if (!currentList.contains(s)) {
									currentList.add(s);
								}
							}
						}
						else {
							attributeTable.put(name, (ArrayList<String>) list);
						}
					}
					
					
				}
			}
		}
		PrintWriter printout = new PrintWriter(new File(outputFile));
		printout.println("@relation training_"  + disease + "_cleaned");
		printout.println();		
		printout.println("@attribute ID Numeric");
		for (int i=0; i<attributeList.size(); i++) {		
			String nameVariable = attributeList.get(i);			
			ArrayList<String> list = attributeTable.get(nameVariable);				
			Collections.sort(list);
			String listString = String.join(",", list);
			printout.println("@attribute " + nameVariable + " {" + listString + "}");
		}
		printout.flush();
		printout.close();
	}
	
	
	
	/*This method generates arff files.*/
	public static void addHeader2CSV(String inputFileName1, String inputFileName2, String outputFileName) throws FileNotFoundException {
		Scanner input1 = new Scanner(new File(inputFileName1));
		Scanner input2 = new Scanner(new File(inputFileName2));
		PrintWriter printout = new PrintWriter(new File(outputFileName));	
		while (input1.hasNext()) {
			printout.println(input1.nextLine());
		}
		input1.close();
		printout.println();
		printout.println("@data");
		input2.nextLine(); //skip the header of the CSV file.
		while (input2.hasNext()) {
			printout.println(input2.nextLine());
		}
		input1.close();
		input2.close();
		printout.flush();
		printout.close();
	}
	
}
