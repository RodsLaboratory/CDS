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

public class B5_getSummary_from_resultFiles {
	static String disease;
	static String folderLoc;
	static String season;
	static double threshold;
	static PrintWriter bnResult;
	static String year;
	static String nextYear;
	static PrintWriter printout;
	static Boolean unionAll;
	
	
	public static void main(String[] args) throws Exception {
		unionAll = Boolean.TRUE;
	
		ArrayList<Double> thresholdList = new ArrayList<Double>();
		//thresholdList.add(new Double("0.0")); thresholdList.add(new Double("0.0001"));
		thresholdList.add(0.001);
	
		String property_file_name = "properties10172023.txt";		
		folderLoc=Configuration.getExperimentFolderLocation(property_file_name);	
		
		ArrayList<String> diseaseList = new ArrayList<String>();
		diseaseList.add("INFLUENZA"); 
		diseaseList.add("RSV");	
		diseaseList.add("ADENOVIRUS"); 
		diseaseList.add("PARAINFLUENZA"); 
		diseaseList.add("HMPV"); 
		diseaseList.add("ENTEROVIRUS"); 
		//diseaseList.add("OTHER"); 		
		
		for (Double t:thresholdList) {
			threshold = t;
			if (!unionAll) {
				printout = new PrintWriter(new File(folderLoc +"3.results/auc_summary_union_feature_" + threshold + ".csv"));
			}
			else if (unionAll) {
				printout = new PrintWriter(new File(folderLoc +"3.results/auc_summary_union_feature_all_" + threshold + ".csv"));
			}
			printout.println("disease,train_seasons,test_season,method,auc");
			for (String d:diseaseList) {
				disease = d;
				ArrayList<String> thisYearList = new ArrayList<String>();
				thisYearList.add("2012-2016"); thisYearList.add("2013-2017"); thisYearList.add("2014-2018");  
				thisYearList.add("2015-2019"); thisYearList.add("2016-2020"); 	
				
				ArrayList<String> nextYearList = new ArrayList<String>();
				nextYearList.add("2016-2017"); 	nextYearList.add("2017-2018"); nextYearList.add("2018-2019"); 
				nextYearList.add("2019-2020"); nextYearList.add("2020-2021"); 	
			
				for (int i=0; i<thisYearList.size(); i++) {
					year = thisYearList.get(i);
					nextYear = nextYearList.get(i);
					runOneYearTestDifferentYear();
					
				}	
			}
			disease = "OTHER";
			ArrayList<String> thisYearList = new ArrayList<String>();
			thisYearList.add("2015-2016"); thisYearList.add("2016-2017"); thisYearList.add("2017-2018");  
			thisYearList.add("2018-2019"); thisYearList.add("2019-2020"); 	
			ArrayList<String> nextYearList = new ArrayList<String>();
			nextYearList.add("2016-2017"); 	nextYearList.add("2017-2018"); nextYearList.add("2018-2019"); 
			nextYearList.add("2019-2020"); nextYearList.add("2020-2021"); 	
			for (int i=0; i<thisYearList.size(); i++) {
				year = thisYearList.get(i);
				nextYear = nextYearList.get(i);
				runOneYearTestDifferentYear();	
			}
			
			disease = "COV";
			thisYearList = new ArrayList<String>();
			thisYearList.add("2019-2020"); 	
			nextYearList = new ArrayList<String>();
			nextYearList.add("2020-2021"); 	
			for (int i=0; i<thisYearList.size(); i++) {
				year = thisYearList.get(i);
				nextYear = nextYearList.get(i);
				runOneYearTestDifferentYear();	
			}
			
			printout.close();
		}
	}
	
	public static void runOneYearTestDifferentYear() throws Exception {
		String resultLocation="";
		if (!unionAll) {
			resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "_" + nextYear + "/union_feature_"+threshold+"_NB/";	
		}
		else if (unionAll) {
			resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "_" + nextYear  + "/union_feature_all_"+threshold+"_NB/";	
		}
		
		Scanner input = new Scanner(new File(resultLocation+"result.csv"));
		while (input.hasNext()) {
			String oneLine = input.nextLine();
			if (oneLine.contains("ROC")) {
				String resultLine = input.nextLine().trim();
				if (!resultLine.contains("?")) {
					String result = resultLine.split("\\s+")[6];
					System.out.println(resultLine);
					System.out.println(result);
					printout.println(disease + "," + year + "," + nextYear + ",union_feature_"+threshold+"_NB," + result);
					break;
				}
			}
		}
		printout.flush();
		input.close();
	}
	
	public static void runOneYear() throws Exception {
		String resultLocation="";
		if (!unionAll) {
			resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "/union_feature_"+threshold+"_NB/";	
		}
		else if (unionAll) {
			resultLocation=folderLoc +"javaRunning_" + disease + "_" + year + "/union_feature_all_"+threshold+"_NB/";	
		}
		
		Scanner input = new Scanner(new File(resultLocation+"result.csv"));
		while (input.hasNext()) {
			String oneLine = input.nextLine();
			if (oneLine.contains("ROC")) {
				String resultLine = input.nextLine().trim();
				String result = resultLine.split("\\s+")[6];
				System.out.println(resultLine);
				System.out.println(result);
				printout.println(disease + "," + year + ",union_feature_"+threshold+"_NB," + result);
				break;
			}
		}
		printout.flush();
		input.close();
	}

}
