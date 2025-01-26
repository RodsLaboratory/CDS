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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import edu.pitt.rods.cds.utility.Configuration;

public class A1_2getArffMultiYear {
	static ArrayList<String> finished_range_list;
	static String folderLoc;;
	static String originalfileLoc;
	static String intermediateLoc;
	static PrintWriter printout;
	
	public static void main(String[] args) throws Exception {
		String property_file_name = "properties10172023.txt";		
		folderLoc=Configuration.getExperimentFolderLocation(property_file_name);		
		originalfileLoc  = folderLoc + "/0.original_data/";
		intermediateLoc = folderLoc + "/1.intermediate_data/";		
				
		ArrayList<String> diseaseList = new ArrayList<String>();
		diseaseList.add("INFLUENZA"); diseaseList.add("RSV"); 
		diseaseList.add("HMPV"); 		diseaseList.add("ADENOVIRUS"); 		
		diseaseList.add("PARAINFLUENZA"); 		diseaseList.add("ENTEROVIRUS");  
		diseaseList.add("OTHER"); 
//		diseaseList.add("COV"); 
		
		for (String d:diseaseList) {
			String diseaseName = d;
			System.out.println(diseaseName);
			finished_range_list = new ArrayList<String> ();
//			finished_range_list.add("2011-2012"); finished_range_list.add("2012-2013"); finished_range_list.add("2013-2014"); 
//			finished_range_list.add("2014-2015"); finished_range_list.add("2015-2016"); 
					
//			if (!diseaseName.equals("OTHER") & !diseaseName.equals("COV")) {
//				printFourYear(diseaseName);
//			}
//			else {
//				printOneYear(diseaseName); // printOneYearForOther
//			}
			
			printOneTestYear(diseaseName);
		}	
	}
	
	public static void printOneTestYear(String diseaseName) throws FileNotFoundException, ParseException {
		for (int y=2021; y<=2021; y++) {
			int temp_end_year = y;
			int temp_start_year = temp_end_year - 1;
			String temp_range = temp_start_year + "-" + temp_end_year;
			//System.out.println("--------- now creating " + temp_range); 
			if (!finished_range_list.contains(temp_range)) {
				System.out.println(temp_range);
				processOneRange(diseaseName, temp_range);		
				finished_range_list.add(temp_range);
			}						
		}	
	}
	
	
	public static void printFourYear(String diseaseName) throws FileNotFoundException, ParseException {
		for (int y=2016; y<=2020; y++) {
			int temp_end_year = y;
			int temp_start_year = temp_end_year - 4;
			String temp_range = temp_start_year + "-" + temp_end_year;
			//System.out.println("--------- now creating " + temp_range); 
			if (!finished_range_list.contains(temp_range)) {
				System.out.println(temp_range);
				processOneRange(diseaseName, temp_range);		
				finished_range_list.add(temp_range);
			}						
		}	
	}
	
	public static void printOneYear(String diseaseName) throws FileNotFoundException, ParseException {
		for (int y=2016; y<=2020; y++) {
			int temp_end_year = y;
			int temp_start_year = temp_end_year - 1;
			String temp_range = temp_start_year + "-" + temp_end_year;
			//System.out.println("--------- now creating " + temp_range); 
			if (!finished_range_list.contains(temp_range)) {
				System.out.println(temp_range);
				processOneRange(diseaseName, temp_range);		
				finished_range_list.add(temp_range);
			}						
		}	
	}
	
	public static void processOneRange(String diseaseName, String oneRange) throws FileNotFoundException, ParseException {
		//String diseaseName = "INFLUENZA";	
		//oneRange such as 2011-2013
		
		//training_2011-2012_ADENOVIRUS_new_cleaned
		
		printout = new PrintWriter(new File(intermediateLoc+"training_"+ oneRange + "_" +  diseaseName + "_cleaned.arff"));		
		int startYear = Integer.parseInt(oneRange.split("-")[0]);	
		int endYear = Integer.parseInt(oneRange.split("-")[1]);	
		
		if (!diseaseName.equals("OTHER")) {
			printHeader(folderLoc + "1.intermediate_data/header_updated_consolidated_diseases.txt");
		}
		else {
			printHeader(folderLoc + "1.intermediate_data/header_updated_consolidated_other.txt");
		}
	
		//ArrayList<String> seasonList = new ArrayList<String>();			
		for (int i=startYear; i<=endYear-1; i++) {
			int start = i;
			int end = i+1;
			//seasonList.add(start+"-"+end);	
			System.out.println("printing " + start+"-"+end+"season");
			copyOneSeason(diseaseName, start+"-"+end);
		}
		printout.flush(); printout.close();
	}
	
	public static void printHeader(String inputFileName1) throws FileNotFoundException {
		Scanner input1 = new Scanner(new File(inputFileName1));
		while (input1.hasNext()) {
			printout.println(input1.nextLine());
		}
		input1.close();
		printout.println();
		printout.println("@data");
		printout.flush();
	}
	
	
	public static void copyOneSeason(String diseaseName, String season) throws FileNotFoundException {
		String fileName = intermediateLoc+"training_"+ season + "_" +  diseaseName + "_cleaned.csv";
		File f = new File(fileName);
		if(f.exists()){
			Scanner input = new Scanner(new File(fileName));
			input.nextLine();//ignore header
			while (input.hasNext()) {
				String oneLine = input.nextLine();
				printout.println(oneLine);
			}
			input.close();
			printout.flush();
		}
		else {
			System.out.println("do not find!");
		}
	}
	

}
