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
import edu.pitt.rods.cds.utility.FeatureWithIG_MultiDiseases;


public class B3_getFeatureUnionAll {
	static String generalFileLocation;
	static String disease;
	static String year;
	static PrintWriter printout;
	static ArrayList<String> featureList;
	static ArrayList<String> diseaseList;
	static HashMap<String,String> influenzaTable;
	static HashMap<String,String> rsvTable;
	static HashMap<String,String> hmpvTable;
	static HashMap<String,String> adenovirusTable;
	static HashMap<String,String> parainfluenzaTable;
	static HashMap<String,String> enterovirusTable;
	static HashMap<String,String> covTable;
	static HashMap<String,String> otherTable;
	static Double threshold;
	
	
	public static void main(String[] args) throws Exception {
		String property_file_name = "properties10172023.txt";		
		generalFileLocation=Configuration.getExperimentFolderLocation(property_file_name);
	
		ArrayList<Double> thresholdList = new ArrayList<Double>();
		//thresholdList.add(new Double("0.0")); thresholdList.add(new Double("0.0001"));
		thresholdList.add(0.001);
		for (Double t:thresholdList) {
			threshold = t;
			getFeatureUnion_All();
		}
		
	}
	
	public static void getFeatureUnion_All() throws FileNotFoundException {
		ArrayList<String> diseaseYearList = new ArrayList<String>();
		//diseaseYearList.add("2012-2016"); diseaseYearList.add("2013-2017"); diseaseYearList.add("2014-2018"); diseaseYearList.add("2015-2019"); 	
		diseaseYearList.add("2016-2020");
		
		ArrayList<String> otherYearList = new ArrayList<String>();
		//otherYearList.add("2015-2016"); otherYearList.add("2016-2017"); otherYearList.add("2017-2018"); otherYearList.add("2018-2019"); 		
		otherYearList.add("2019-2020");
	
		
		for (int i=0; i<diseaseYearList.size(); i++) {
			year = diseaseYearList.get(i);
			featureList = new ArrayList<String>();
			diseaseList = new ArrayList<String>();
			diseaseList.add("INFLUENZA");  diseaseList.add("RSV");	diseaseList.add("ADENOVIRUS"); 
			diseaseList.add("PARAINFLUENZA"); diseaseList.add("HMPV"); diseaseList.add("ENTEROVIRUS"); diseaseList.add("OTHER"); 
			
				
			disease = "INFLUENZA";
			influenzaTable = getFeatureTable(generalFileLocation +"javaRunning_" + disease + "_" + year +  "/intermediate/total_informationGainScore_CVMerit_simple.csv");
			
			disease = "RSV";
			rsvTable = getFeatureTable(generalFileLocation +"javaRunning_" + disease + "_" + year +  "/intermediate/total_informationGainScore_CVMerit_simple.csv");
			
			disease = "HMPV";
			hmpvTable = getFeatureTable(generalFileLocation +"javaRunning_" + disease + "_" + year +  "/intermediate/total_informationGainScore_CVMerit_simple.csv");
			
			disease = "ADENOVIRUS";
			adenovirusTable = getFeatureTable(generalFileLocation +"javaRunning_" + disease + "_" + year +  "/intermediate/total_informationGainScore_CVMerit_simple.csv");
			
			disease = "PARAINFLUENZA";
			parainfluenzaTable = getFeatureTable(generalFileLocation +"javaRunning_" + disease + "_" + year +  "/intermediate/total_informationGainScore_CVMerit_simple.csv");
			
			disease = "ENTEROVIRUS";
			enterovirusTable = getFeatureTable(generalFileLocation +"javaRunning_" + disease + "_" + year +  "/intermediate/total_informationGainScore_CVMerit_simple.csv");
			
			disease = "OTHER";
			otherTable = getFeatureTable(generalFileLocation +"javaRunning_" + disease + "_" + otherYearList.get(i) +  "/intermediate/total_informationGainScore_CVMerit_simple.csv");
		
			if (year.equals("2016-2020")) {
				diseaseList.add("COV"); 
				disease = "COV";
				covTable = getFeatureTable(generalFileLocation +"javaRunning_" + disease + "_2019-2020/intermediate/total_informationGainScore_CVMerit_simple.csv");
			}
				
			printout = new PrintWriter(new File(generalFileLocation+"3.results/union_feature_" + threshold + "_" + year + "_all_V2.csv"));
			printout.println("featureCode,description,previousIndex,IGvalue_INFLUENZA,rank_INFLUENZA,IGvalue_RSV,rank_RSV,IGvalue_HMPV,rank_HMPV,"
					+ "IGvalue_PARAINFLUENZA,rank_PARAINFLUENZA,IGvalue_ADENOVIRUS,rank_ADENOVIRUS,IGvalue_ENTEROVIRUS,rank_ENTEROVIRUS,IGvalue_OTHER,rank_OTHER,IGvalue_COV,rank_COV");
			
			for (String d:diseaseList) {
				
				if (d.equals("OTHER") ) {
					printFeature(d, generalFileLocation +"javaRunning_" + d + "_" + otherYearList.get(i) +  "/intermediate/total_informationGainScore_CVMerit_simple_description.csv", threshold);
				}
				else if (d.equals("COV") ) {
					printFeature(d, generalFileLocation +"javaRunning_" + d + "_2019-2020/intermediate/total_informationGainScore_CVMerit_simple_description.csv", threshold);
				}
				else {
					printFeature(d, generalFileLocation +"javaRunning_" + d + "_" + year +  "/intermediate/total_informationGainScore_CVMerit_simple_description.csv", threshold);
					
				}	
			}
			printout.flush();
			printout.close();
		}
	}
	
	
	
	public static HashMap<String,String> getFeatureTable(String inputFileName) throws FileNotFoundException{
		HashMap<String,String> table = new HashMap<String,String>();
		Scanner input = new Scanner(new File(inputFileName));
		input.nextLine();
		while (input.hasNext()) {
			String oneLine = input.nextLine();
			String[] valueList = oneLine.split(",");
			table.put(valueList[0],valueList[1]+","+valueList[4]);
		}
		input.close();
		return table;
	}
	
	//Feature,InformationGainScore,RawRank,PreviousIndex,Rank,description
	//L_D_14627-4,9.082445E-4,-1.0,21179,1,Bicarbonate
	
	public static void printFeature(String currentDisease, String inputFileName, double threshold) throws FileNotFoundException {
		System.out.println(inputFileName);
		Scanner input = new Scanner(new File(inputFileName));
		input.nextLine(); //skip header
		int count=0;
		while (input.hasNext()) {
			count++;
			String oneLine = input.nextLine();
			String[] valueList = oneLine.split(",",-1);	
			String IGString = valueList[1];
			Double IGScore = Double.parseDouble(IGString);
			if (IGScore>threshold) {
				if (count<5) {
					System.out.println(oneLine);
				}
				String code = valueList[0];
				String previousIndex = valueList[3];
				String desc = "";
				if (valueList.length==5) {
					desc = valueList[0];
				}
				else {
					desc = valueList[5];
				}
				String rank = valueList[4];
				if (!code.equals("ID") && !featureList.contains(code)) {
					featureList.add(code);
					FeatureWithIG_MultiDiseases one = new FeatureWithIG_MultiDiseases(code, previousIndex, desc);
					//one.setIGvalue(code, previousIndex, desc);
					one.setIGvalue(currentDisease, IGString, rank);
					for (String d:diseaseList) {
						if (!d.equals(currentDisease)) {
							//System.out.println(d);
							HashMap<String,String> table = new HashMap<String,String>();
							if (d.equals("INFLUENZA")) {
								table = influenzaTable;	
							}
							else if (d.equals("RSV")) {
								table = rsvTable;	
							}
							else if (d.equals("HMPV")) {
								table = hmpvTable;	
							}
							else if (d.equals("ADENOVIRUS")) {
								table = adenovirusTable;	
							}
							else if (d.equals("PARAINFLUENZA")) {
								table = parainfluenzaTable;	
							}
							else if (d.equals("ENTEROVIRUS")) {
								table = enterovirusTable;	
							}	
							else if (d.equals("OTHER")) {
								table = otherTable;	
							}	
							else if (d.equals("COV")) {
								table = covTable;	
							}
							if (table.containsKey(code)){
								String temp = table.get(code);
								String tempIG = temp.split(",")[0];
								String tempRank = temp.split(",")[1];
								one.setIGvalue(d, tempIG, tempRank);
							}
						}
						if (count<5) {
							System.out.println(one);
						}
					}
					printout.println(one);
				}
			}
			
		}
		input.close();
	}
	
//	public static void getUnion(ArrayList<String> fileNameList, Double threshold) {
//	
//	0          1                  2         3            4     5
//	Feature,InformationGainScore,RawRank,PreviousIndex,Rank,description
//	
//	
//}
	
//	FeatureWithIG_MultiDiseases one = new FeatureWithIG_MultiDiseases(String code, String previousIndex, String desc);
//	
//	FeatureWithIG_CVMerit one = new FeatureWithIG_CVMerit(values[3],values[1],"-1",values[2]);

}
