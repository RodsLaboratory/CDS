package edu.pitt.rods.cds.a_run1023;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import edu.pitt.rods.cds.utility.Configuration;



public class A2_processTestData {
	static String folderLoc;
	static HashMap<Integer,String> locTable;
	
	public static void main(String[] args) throws Exception {
		/*Please edit the test_properties file to the folder that has two directories: data and intermediate_data*/
		folderLoc=Configuration.getExperimentFolderLocation("properties.txt");
		
		/*2018-2019  has issues, so stopped*/
		
		String year = "2012to2013";
		
		String inputCSVFile = folderLoc + "intermediate_data/ICD_" + year+".csv";
		
		/*find classVariable*/
		String ClassVariable = Configuration.getClassVariable("properties.txt");
		int classIndex = getClassVariableIndex(inputCSVFile, ClassVariable);
		
		/*find removeVariableList*/
		ArrayList<String> removeVariableList = Configuration.getRemoveVariableList("properties.txt");	
		ArrayList<Integer> removeIndexList = getRemoveVariableIndex(removeVariableList, inputCSVFile);
		
		/*find ageVariable*/
		int ageIndex = getAgeIndex(inputCSVFile);
		
		/*Discretize age, put class variable the last */
		String outputCSVFile = folderLoc + "intermediate_data/test_" + year+ "_RSV_cleaned.csv"; 
		getCleanedFile(removeIndexList, ageIndex, classIndex, inputCSVFile, outputCSVFile);
		
		/*Print header */
		HashMap<Integer,ArrayList<String>> table =  getHashTable(outputCSVFile);
		String headerFile = folderLoc + "intermediate_data/header_test_" + year+ ".txt"; 
		PrintHeader(table, headerFile);	
		
		/*get Arff String table*/
//		HashMap<String,String> arffFileStringTable = Configuration.getPatterns("properties.txt");
//		String newHeaderFile = folderLoc + "intermediate_data/header_updated" + year +".txt";  
//		processHeader(headerFile, newHeaderFile, arffFileStringTable); 
		
		
		/*generate Arff file for training purpose */
//		String finalHeaderFile = folderLoc + "intermediate_data/header_updated_all.txt";  
//		String inputFileName1 = finalHeaderFile;
//		String inputFileName2 = outputCSVFile;
//		String outputFileName = folderLoc + "intermediate_data/training_" + year+ "_RSV_cleaned.arff"; 
//		addHeader2CSV(inputFileName1, inputFileName2, outputFileName);
		
		/*compare train and test generate Arff file for testing purpose */
		
	}
	
	
	

	public static int getClassVariableIndex(String inputCSVFile, String ClassVariable) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputCSVFile));
		String header = input.nextLine();
		String[] variables = header.split(",");
		for (int i=0; i<variables.length; i++) {
			if (variables[i].contains(ClassVariable)) {
				return i;
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
	
	/*Discretize age, put class variable the last */
	public static void getCleanedFile(ArrayList<Integer> removeIndexList, int ageIndex, int classIndex, String inputCSVFile, String outputCSVFile) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputCSVFile));
		PrintWriter printout = new PrintWriter(new File(outputCSVFile));
		int nLine = 0;
		String tempClassString = new String("");
		while (input.hasNext()) {
			nLine++;
			String[] valueList = input.nextLine().split(",");
			String newString = "";
			for (int i=0; i<valueList.length; i++) {
				if (i==ageIndex & !valueList[i].contains("AGE")) {
					System.out.println(valueList[i]);
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
				else if (!removeIndexList.contains(i) & i!=classIndex) {
					newString = newString + valueList[i]+",";
				}
				else if (i==classIndex) {
					tempClassString = valueList[i];
				}
			}
			printout.println(newString+tempClassString);
		}
		input.close();
		printout.flush();
		printout.close();
	}
	
	public static HashMap<Integer,ArrayList<String>> getHashTable(String inputCSVFile) throws FileNotFoundException {
		HashMap<Integer,ArrayList<String>> table = new HashMap<Integer,ArrayList<String>>();
		locTable = new HashMap<Integer,String>();
		Scanner input = new Scanner(new File(inputCSVFile));
		String[] header = input.nextLine().split(",");
		for (int i=0; i<header.length; i++) {
			table.put(i, new ArrayList<String>());
			locTable.put(i, header[i]);
		}
		int nLine=0;
		while (input.hasNext()) {
			nLine++;
			String oneLine = input.nextLine();
			String[] valueList = oneLine.split(",");
			for (int i=0; i<valueList.length; i++) {
				String oneValue = valueList[i];
				ArrayList<String> tempValueList = table.get(i);
				if (!tempValueList.contains(oneValue)) {
					tempValueList.add(oneValue);
				}
				table.put(i, tempValueList);
			}
		}
		input.close();
		return table;	
	}
	public static void PrintHeader(HashMap<Integer,ArrayList<String>> finalTable, String outputFileName) throws FileNotFoundException {
		PrintWriter printout = new PrintWriter(new File(outputFileName));
		printout.println("@relation training_2011to2012_RSV_cleaned");
		printout.println();
		for (int i=0; i<finalTable.size(); i++) {
			String nameVariable = locTable.get(i);
			System.out.println(nameVariable);
			ArrayList<String> list = finalTable.get(i);
			Collections.sort(list);
			String listString = String.join(",", list);
			printout.println("@attribute " + nameVariable + " {" + listString + "}");
		}
		printout.flush();
		printout.close();
	}
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
