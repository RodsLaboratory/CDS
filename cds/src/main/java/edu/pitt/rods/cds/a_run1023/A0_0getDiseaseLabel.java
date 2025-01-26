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

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 */
public class A0_0getDiseaseLabel {

	/**
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String fileLocation = new String("/Users/yey5/Documents/Greg_PDS_project_after10162023/0.original_data/");
		
		/**check header */
//		for (int year = 2011; year<=2021; year++){
//			System.out.println(year);
//			String inputFileName = fileLocation + "discrete_filtered_encounters_cuis_vitals_diagnosis_labs_2023-07-12/discrete_filtered_"+year+"_encounters_cuis_vitals_diagnosis_labs_2023-07-12.txt";		
//			compareHeader(inputFileName, "/Users/yey5/Documents/Greg_PDS_project_before10162023/0.original_data/data06292023_discrete_2011.csv");		
//		}
			
		/** add label, change | to , */
		HashMap<String,String> table = getLabelTable(fileLocation + "PDS_ICD_lab_additionallab_combine_labels_10162023_updateCOV.csv");
		for (int year = 2020; year<=2021; year++){
			System.out.println(year);
			String inputFileName = fileLocation + "discrete_filtered_encounters_cuis_vitals_diagnosis_labs_2023-07-12/discrete_filtered_"+year+"_encounters_cuis_vitals_diagnosis_labs_2023-07-12.txt";		
			String outputFileName = fileLocation + "data07122023_discrete_add_label_" + year + ".csv";
			//String inputFileName = fileLocation + "check.txt";
			//String outputFileName = fileLocation + "check_output.csv";
			addLabel(table, inputFileName, outputFileName);
		}
	}
	
	public static void compareHeader(String inputFileName, String inputFileName2) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputFileName));
		String header = input.nextLine();
		String header_changed = header.replaceAll("\\|", ",");
		
		Scanner input2 = new Scanner(new File(inputFileName2));
		String header2 = input2.nextLine();
		
		if (header_changed.equals(header2)) {
			System.out.println("the same");
		}
		input.close();
		input2.close();
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
		input.close();
		return table; 
	 }
	
	
	 /*
	  * add labels to each csv
	  */
	  public static void addLabel(HashMap<String,String> table, String inputFileName, String outputFileName) throws FileNotFoundException {
		  Scanner input = new Scanner(new File(inputFileName));
		  PrintWriter printout = new PrintWriter(new File(outputFileName));
		  String toPrint = input.nextLine().replaceAll("\\|", ",")+",SEASON,ICD_INFLUENZA,LAB_INFLUENZA,LABEL_INFLUENZA,"
			  		+ "ICD_RSV,LAB_RSV,LABEL_RSV,ICD_HMPV,LAB_HMPV,LABEL_HMPV,"
			  		+ "ICD_ADENOVIRUS,LAB_ADENOVIRUS,LABEL_ADENOVIRUS,"
			  		+ "ICD_ENTEROVIRUS,LAB_ENTEROVIRUS,LABEL_ENTEROVIRUS,"
			  		+ "ICD_PARAINFLUENZA,LAB_PARAINFLUENZA,LABEL_PARAINFLUENZA,"
			  		+ "ICD_COV,LAB_COV,LABEL_COV,LAB_INFLUENZA_ADDITIONAL,LAB_RSV_ADDITIONAL,LAB_HMPV_ADDITIONAL,"
			  		+ "LAB_PARAINFLUENZA_ADDITIONAL,LAB_ENTEROVIRUS_ADDITIONAL,LAB_ADENOVIRUS_ADDITIONAL,LABEL_INFLUENZA_NEW,"
			  		+ "LABEL_RSV_NEW,LABEL_PARAINFLUENZA_NEW,LABEL_HMPV_NEW,LABEL_ENTEROVIRUS_NEW,LABEL_ADENOVIRUS_NEW";
		  printout.println(toPrint);
		 // System.out.println(toPrint);
		  while (input.hasNext()){
			  String oneLine = input.nextLine();
			  oneLine = oneLine.replaceAll(",",""); //remove all commas
//			  if (oneLine.contains("\"")) {
//					oneLine = removeCommasWithinDoubleQuotes(oneLine);	
//			  }
			  /** replace | */
			  oneLine = oneLine.replaceAll("\\|", ","); 
			  String ID = oneLine.substring(0,oneLine.indexOf(","));	   
			  if (table.containsKey(ID)) {		  
				  oneLine = oneLine + ","+table.get(ID);
			  }
			  else {
				  System.out.println("not found " + ID);
			  }
			  printout.println(oneLine);
		  }
		  input.close();
		  printout.flush();
	  }
	
	 
	  public static String removeCommasWithinDoubleQuotes(String input) {
	        // Define the regular expression pattern to match the content within double quotes
	        String pattern = "\"(.*?)\"";
	        
	        // Create a pattern object and a matcher object
	        Pattern regex = Pattern.compile(pattern);
	        Matcher matcher = regex.matcher(input);
	        
	        // Use a StringBuilder to build the modified output string
	        StringBuilder result = new StringBuilder();
	        int lastEnd = 0;
	        
	        // Iterate through the matches and replace commas within each match
	        while (matcher.find()) {
	            String content = matcher.group(1);
	            String modifiedContent = content.replace(",", "");
	            
	            // Append everything between the last match and the current match
	            result.append(input, lastEnd, matcher.start());
	            
	            // Append the modified match (without commas) to the result
	            result.append("\"").append(modifiedContent).append("\"");
	            
	            // Update the lastEnd pointer for the next iteration
	            lastEnd = matcher.end();
	        }
	        
	        // Append the remaining part of the input string after the last match
	        result.append(input.substring(lastEnd));
	        
	        return result.toString();
	    }
	
	 /*
	  * get research data for each disease
	  * season 2011-2012, 2012-2013, 2013-2014, 2014-2015, 2015-2016, 2016-2016
	  */

}
