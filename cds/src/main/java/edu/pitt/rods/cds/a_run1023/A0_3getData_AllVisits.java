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

public class A0_3getData_AllVisits {
	static PrintWriter printout;
	static ArrayList<String> printout_id_list;
	static ArrayList<String> finished_range_list;
	static String originalfileLoc;
	static String intermediateLoc;
	
	public static void main(String[] args) throws Exception {
		originalfileLoc  = new String("/Users/yey5/Documents/Greg_PDS_project_after10162023/0.original_data/");
		intermediateLoc = new String("/Users/yey5/Documents/Greg_PDS_project_after10162023/1.intermediate_data/");
		String diseaseName = "All";
		runIndividualSeason(diseaseName);
	
	}
	
	
	public static void runIndividualSeason(String diseaseName) throws FileNotFoundException, ParseException {	
		ArrayList<String> seasonList = new ArrayList<String>();	
//		seasonList.add("2016-2017"); seasonList.add("2017-2018"); 		
//		seasonList.add("2018-2019"); 
		seasonList.add("2019-2020");
//		seasonList.add("2020-2021"); 
		
		
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
					printoutAll(Boolean.TRUE, originalfileLoc + "data07122023_discrete_add_label_" + year + ".csv", season);
				}
				else {
					printoutAll(Boolean.FALSE, originalfileLoc + "data07122023_discrete_add_label_" + year + ".csv", season);
				}
				
			}		
			printout.close();
		}		
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
	public static void printoutAll(Boolean printHeader, String inputFileName, String season) throws FileNotFoundException, ParseException {
		Scanner input = new Scanner(new File(inputFileName));
		String header = input.nextLine();
		List<String> header_list = new ArrayList<String>(Arrays.asList(header.split(",")));	
		if (printHeader) {
			printout.println(header);
		}				
		int adateIndex = header_list.indexOf("N_DT_ADMISSION_DATE");
		String year = season.split("-")[0];
		String nextyear = season.split("-")[1];	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     	
		String dateStr1 = year + "-06-01 00:00:00";
		if (year.equals("2011")) {
			dateStr1 = year + "-01-01 00:00:00";
		}
	    String dateStr2 = nextyear + "-05-31 23:59:59";
	    if (nextyear.equals("2021")){
	    	dateStr2 = nextyear + "-12-31 23:59:59";
	    }
	    Date date1 = formatter.parse(dateStr1);  
	    Date date2 = formatter.parse(dateStr2);   		
	    int count=0;
		while (input.hasNext()) {
			String oneLine = input.nextLine();
			String[] oneLineList = oneLine.split(",",-1);
			String ID = oneLineList[0];
			String oneLine_date_time = oneLineList[adateIndex];
			Date oneLine_date = formatter.parse(oneLine_date_time);  
			if ( (oneLine_date.equals(date1)| oneLine_date.after(date1)) && (oneLine_date.equals(date2) | oneLine_date.before(date2))) {
		    	//System.out.println(oneLine_date);    	
	    		if (!printout_id_list.contains(ID)) {
	    			printout.println(oneLine);
	    			printout_id_list.add(ID);
		    		count++;
	    		}
		    }   	
		}
		System.out.println("Retrieved in " + season + " " + count);
	}
	
	
	
}
