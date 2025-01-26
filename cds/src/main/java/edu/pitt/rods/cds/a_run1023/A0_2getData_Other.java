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
 * Retrieve research data for other.
 * 
 * Other: August (T) 
 * Disease: INFLUENZA,RSV,HMPV,ADENOVIRUS,ENTEROVIRUS,PARAINFLUENZA,COV (F).. make sure changing labels
 *		
 * @author yey5
 *
 */

public class A0_2getData_Other {
	static PrintWriter printout;
	static ArrayList<String> printout_id_list;
	static ArrayList<String> finished_range_list;
	static String originalfileLoc;
	static String intermediateLoc;
	
	
	public static void main(String[] args) throws Exception {
		originalfileLoc  = new String("/Users/yey5/Documents/Greg_PDS_project_after10162023/0.original_data/");
		intermediateLoc = new String("/Users/yey5/Documents/Greg_PDS_project_after10162023/1.intermediate_data/");
		String diseaseName = "OTHER";
		runSeasons(diseaseName);
	}

	
	public static void runSeasons(String diseaseName) throws FileNotFoundException, ParseException {	
		ArrayList<String> seasonList = new ArrayList<String>();	
		//use previous one year for training other model
		//did not use these years for training models: 11-12,13-14, 14-15, 20-21 (for testing only)	
//		seasonList.add("2015-2016"); seasonList.add("2016-2017"); 
//		seasonList.add("2017-2018"); seasonList.add("2018-2019"); 
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
					printoutAllDisease(Boolean.TRUE, originalfileLoc + "data07122023_discrete_add_label_" + year + ".csv", season);
				}
				else {
					printoutAllDisease(Boolean.FALSE, originalfileLoc + "data07122023_discrete_add_label_" + year + ".csv", season);
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
	
	
	
	
	
	public static void printoutAllDisease(Boolean printHeader, String inputFileName, String season) throws FileNotFoundException, ParseException {
		Scanner input = new Scanner(new File(inputFileName));
		String header = input.nextLine();
		List<String> header_list = new ArrayList<String>(Arrays.asList(header.split(",")));	
		int seasonIndex = header_list.indexOf("SEASON");
		//System.out.println("SEASON INDEX:" + seasonIndex);
		Boolean inSeason = false;
		//int seasonIndex = header_list.indexOf("SEASON");
		if (printHeader) {
			printout.println(header+",LABEL_OTHER_NEW");
		}
		ArrayList<Integer> label_index_list = new ArrayList<Integer>();	
		for (int i=0; i<header_list.size(); i++) {
			String oneVariableName = header_list.get(i);
			if (season.equals("2019-2020")) {
				if ((oneVariableName.contains("LABEL_") && oneVariableName.contains("_NEW")) | oneVariableName.contains("LABEL_COV")) {
					//System.out.println(oneVariableName);
					label_index_list.add(i); //add index of labels
				}
			}
			//not 2019-2020
			else {
				if ((oneVariableName.contains("LABEL_") && oneVariableName.contains("_NEW"))) {
					//System.out.println(oneVariableName);
					label_index_list.add(i); //add index of labels
				}
			}
			
		}				
		Boolean isDisease = false;		
	    int count=0;
		while (input.hasNext()) {
			inSeason = false;
			isDisease = false;	
			String oneLine = input.nextLine();
//			if (oneLine.contains("\"")) {
//				oneLine = removeCommasWithinDoubleQuotes(oneLine);	
//			}
			String[] oneLineList = oneLine.split(",",-1);
			String oneLine_season = oneLineList[seasonIndex];
			String ID = oneLineList[0];
		    //System.out.println(oneLine_date);  
			if (oneLine_season.contains(season)) {
				inSeason = true;
		    	for (int i=0; i<label_index_list.size(); i++) {
		    		int oneIndex = label_index_list.get(i);
		    		if (oneLineList[oneIndex].equals("T")) {
		    			isDisease = true;
		    			break;
		    		}
		    	}
			}
	    	if (inSeason && isDisease) {
	    		if (!printout_id_list.contains(ID)) {
	    			printout.println(oneLine+",F");
	    			printout_id_list.add(ID);
		    		count++;
	    		}
	    		
	    	}
		      	
		}
		System.out.println("Retrieved diseases " + count);
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
				//System.out.println(oneVariableName);
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
		    		if (oneLineList[oneIndex].equals("T") | oneLineList[oneIndex].equals("F")) {
//		    			System.out.println(header_list.get(oneIndex));
//		    			System.out.println(oneIndex);
//		    			System.out.println(oneLineList[oneIndex]);	
//		    			System.out.println("found one in August");
//		    			System.out.println(ID);
		    			isOther = false;
		    			break;
		    		}
		    	}
		    	if (isOther) {
		    		if (!printout_id_list.contains(ID)) {
		    			printout.println(oneLine+",T");
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
