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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.*;

/**
 * Use influenza as an example.
 * 
 * INFLUENZA,RSV,HMPV,ADENOVIRUS,ENTEROVIRUS,PARAINFLUENZA
 *		
 * @author yey5
 *
 */

public class A0_1getData_EachDisease {
	static PrintWriter printout;
	static ArrayList<String> printout_id_list;
	static ArrayList<String> finished_range_list;
	static String originalfileLoc;
	static String intermediateLoc;
	
	public static void main(String[] args) throws Exception {
		originalfileLoc  = new String("/Users/yey5/Documents/Greg_PDS_project_after10162023/0.original_data/");
		intermediateLoc = new String("/Users/yey5/Documents/Greg_PDS_project_after10162023/1.intermediate_data/");
		
		ArrayList<String> diseaseList = new ArrayList<String>();		
		diseaseList.add("INFLUENZA"); 	
		diseaseList.add("RSV");	
		diseaseList.add("ADENOVIRUS"); 		
		diseaseList.add("PARAINFLUENZA"); 
		diseaseList.add("HMPV"); 
		diseaseList.add("ENTEROVIRUS"); 
	

		for (String diseaseName:diseaseList) {
			runIndividualSeason(diseaseName);
		}
	
	}
	
	

	
	
	
	public static void runIndividualSeason(String diseaseName) throws FileNotFoundException, ParseException {	
		ArrayList<String> seasonList = new ArrayList<String>();	
		//use previous four seasons to training disease models
		//did not use these two years for training models : 11-12, 20-21
		
//		seasonList.add("2012-2013"); 
//		seasonList.add("2013-2014"); 
//		seasonList.add("2014-2015"); 
//		seasonList.add("2015-2016"); 
//		seasonList.add("2016-2017"); 
//		seasonList.add("2017-2018"); 
//		seasonList.add("2018-2019"); 
//		seasonList.add("2019-2020"); 	
		seasonList.add("2020-2021"); 	
		
		
		for (int i=0; i<seasonList.size(); i++) {	
			String season = seasonList.get(i);
			System.out.println("------Season " + season);
			int startYear = Integer.parseInt(season.split("-")[0]);	
			int endYear = Integer.parseInt(season.split("-")[1]);
			int currentYear = startYear;
			ArrayList<Integer> yearList = new ArrayList<Integer>();
			while (currentYear<=endYear) {
				yearList.add(currentYear);
				currentYear++;	
			}
			printout = new PrintWriter(new File(intermediateLoc+"data07122023_" + diseaseName + "_" + season + ".csv"));	
			printout_id_list = new ArrayList<String>();
			for (int j=0; j<yearList.size(); j++) {
				int year = yearList.get(j);
				System.out.println("get data from year " + year);
				if (j==0) {
					printoutDisease(Boolean.TRUE, originalfileLoc + "data07122023_discrete_add_label_" + year + ".csv", diseaseName, season);
				}
				else {
					printoutDisease(Boolean.FALSE, originalfileLoc + "data07122023_discrete_add_label_" + year + ".csv", diseaseName, season);
				}
				
			}		
			for (int j=0; j<yearList.size(); j++) {
				int year = yearList.get(j);
				if (year<endYear) {
					System.out.println("get data from year " + year);
					printoutOther(Boolean.FALSE, originalfileLoc + "data07122023_discrete_add_label_" + year + ".csv", season);
				}	
			}
			printout.close();
			
			
		}		
	}
	
	
	//avoid duplicate encounters in the printout. 	
	/**
	 * Retrieve all encounters with disease LABEL_SOME_DISEASE=T in the season
	 * ID,SEASON,ICD_INFLUENZA,LAB_INFLUENZA,LABEL_INFLUENZA,ICD_RSV,LAB_RSV,LABEL_RSV,
	 * ICD_HMPV,LAB_HMPV,LABEL_HMPV,ICD_ADENOVIRUS,LAB_ADENOVIRUS,LABEL_ADENOVIRUS,
	 * ICD_ENTEROVIRUS,LAB_ENTEROVIRUS,LABEL_ENTEROVIRUS,ICD_PARAINFLUENZA,LAB_PARAINFLUENZA,LABEL_PARAINFLUENZA,
	 * ICD_COV,LAB_COV,LABEL_COV
	 * @param diseaseName
	 * @param season
	 * @throws FileNotFoundException 
	 */
	public static void printoutDisease(Boolean printHeader, String inputFileName, String diseaseName, String season) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputFileName));
		String header = input.nextLine();
		List<String> header_list = new ArrayList<String>(Arrays.asList(header.split(",")));	
		int diseaseLabelIndex = header_list.indexOf("LABEL_"+ diseaseName+"_NEW");  //LABEL_INFLUENZA_NEW
		int seasonIndex = header_list.indexOf("SEASON");
		//System.out.println("SEASON INDEX:" + seasonIndex);
		Boolean inSeason = false;
		int count=0;
		if (printHeader) {
			printout.println(header);
		}
		while (input.hasNext()) {
			String oneLine = input.nextLine();		
//			if (oneLine.contains("\"")) {
//				oneLine = removeCommasWithinDoubleQuotes(oneLine);	
//			}
			String[] oneLineList = oneLine.split(",",-1);
			String ID = oneLineList[0];
			String oneLine_diseaseLabel = oneLineList[diseaseLabelIndex];
			String oneLine_season = oneLineList[seasonIndex];
			if (oneLine_season.contains(season)) {
				inSeason = true;
				if (oneLine_diseaseLabel.equals("T")) {		
					if (!printout_id_list.contains(ID)) {
						printout.println(oneLine);
						printout_id_list.add(ID);
						count++;
					}							
				}
			}
			else {
				inSeason = false;
			}
		}
		printout.flush();
		System.out.println("Retrieved " + diseaseName + " " + season + " " + count);
	}
	
	
	
	/**
	 * ID,N_DT_ADMISSION_DATE
	 * Original date string format is like 2011-01-01 00:04:00
	 * The other category contains all encounters in the month of August
	 * excluding encounters that have any disease label and lab test results
	 * @param season
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */
	public static void printoutOther(Boolean printHeader, String inputFileName, String season) throws FileNotFoundException, ParseException {
		Scanner input = new Scanner(new File(inputFileName));
		String header = input.nextLine();
		List<String> header_list = new ArrayList<String>(Arrays.asList(header.split(",")));	
		//int seasonIndex = header_list.indexOf("SEASON");
		if (printHeader) {
			printout.println(header);
		}
		ArrayList<Integer> label_index_list = new ArrayList<Integer>();	
		for (int i=0; i<header_list.size(); i++) {
			String oneVariableName = header_list.get(i);
			if (oneVariableName.contains("LABEL_") && oneVariableName.contains("_NEW")) {
				System.out.println(oneVariableName);
				label_index_list.add(i); //add index of labels
			}
		}				
		Boolean isOther = true;	
		int adateIndex = header_list.indexOf("N_DT_ADMISSION_DATE");
		String year = season.split("-")[0];
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");           
		String dateStr1 = year + "-08-01 00:00:00";
	    String dateStr2 = year + "-08-31 23:59:59";
	    Date date1 = formatter.parse(dateStr1);  
	    Date date2 = formatter.parse(dateStr2);   
		
	    int count=0;
		while (input.hasNext()) {
			isOther = true;	
			String oneLine = input.nextLine();
//			if (oneLine.contains("\"")) {
//				oneLine = removeCommasWithinDoubleQuotes(oneLine);	
//			}
			String[] oneLineList = oneLine.split(",",-1);
			String ID = oneLineList[0];
			String oneLine_date_time = oneLineList[adateIndex];
			Date oneLine_date = formatter.parse(oneLine_date_time);  
			//in August
		    if ( (oneLine_date.equals(date1)| oneLine_date.after(date1)) && (oneLine_date.equals(date2) | oneLine_date.before(date2))) {
		    	//System.out.println(oneLine_date);    	
		    	for (int i=0; i<label_index_list.size(); i++) {
		    		int oneIndex = label_index_list.get(i);
		    		//System.out.println(header_list.get(oneIndex)+"," + oneLineList[oneIndex]);
		    		if (oneLineList[oneIndex].equals("T") | oneLineList[oneIndex].equals("F")) {
//		    			System.out.println(header_list.get(oneIndex));
//		    			System.out.println(oneIndex);
//		    			System.out.println(oneLineList[oneIndex]);	
//		    			System.out.println("found one in August");
//		    			System.out.println(ID);
		    			//System.out.println(oneLine);
		    			isOther = false;
		    			break;
		    		}
		    	}
		    	if (isOther) {
		    		if (!printout_id_list.contains(ID)) {
		    			//System.out.println(ID);
		    			//System.out.println(oneLine);
		    			printout.println(oneLine);
		    			printout_id_list.add(ID);
			    		count++;
		    		}
		    		
		    	}
		    }   	
		}
		System.out.println("Retrieved other in August " + year + " " + count);
	}
	
//	public static String removeCommasWithinDoubleQuotes(String input) {
//        // Define the regular expression pattern to match the content within double quotes
//        String pattern = "\"(.*?)\"";
//        
//        // Create a pattern object and a matcher object
//        Pattern regex = Pattern.compile(pattern);
//        Matcher matcher = regex.matcher(input);
//        
//        // Use a StringBuilder to build the modified output string
//        StringBuilder result = new StringBuilder();
//        int lastEnd = 0;
//        
//        // Iterate through the matches and replace commas within each match
//        while (matcher.find()) {
//            String content = matcher.group(1);
//            String modifiedContent = content.replace(",", "");
//            
//            // Append everything between the last match and the current match
//            result.append(input, lastEnd, matcher.start());
//            
//            // Append the modified match (without commas) to the result
//            result.append("\"").append(modifiedContent).append("\"");
//            
//            // Update the lastEnd pointer for the next iteration
//            lastEnd = matcher.end();
//        }
//        
//        // Append the remaining part of the input string after the last match
//        result.append(input.substring(lastEnd));
//        
//        return result.toString();
//    }
}
