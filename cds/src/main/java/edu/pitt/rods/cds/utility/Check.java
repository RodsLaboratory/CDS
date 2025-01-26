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


package edu.pitt.rods.cds.utility;

import java.io.*;
import java.util.*;

public class Check {
	public static void main(String[] args) throws Exception {
		String disease = "INFLUENZA";	
		String property_file_name = "properties.txt";		
		String folderLoc=Configuration.getExperimentFolderLocation(property_file_name);
		//checkHeaderLength(folderLoc+"0.original_data/data06292023_discrete_2017.csv");
		
		
//		ArrayList<String> list1 = getIDList(folderLoc+"1.intermediate_data/training_2014-2015_ENTEROVIRUS_cleaned_new.csv");
//		ArrayList<String> list2 = getIDList(folderLoc+"1.intermediate_data/training_2014-2015_ADENOVIRUS_cleaned_new.csv");
		
		ArrayList<String> list1 = getIDList(folderLoc+"1.intermediate_data/data07032023_ENTEROVIRUS_2011-2012_new.csv");
		ArrayList<String> list2 = getIDList(folderLoc+"1.intermediate_data/data07032023_ADENOVIRUS_2011-2012_new.csv");
		
		
		
		Boolean different = compareTwoList(list1,list2);
		if (different) {
			System.out.println("different");
		}
		else {
			System.out.println("same");
		}
		
	}
	public static void checkHeaderLength(String inputFileName) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputFileName));
		String oneLine = input.nextLine();
		System.out.println(oneLine);
		String[] valueList = oneLine.split(",");
		System.out.println(oneLine);
		
	}
	

	
	public static Boolean compareTwoList(ArrayList<String> list1, ArrayList<String> list2) {
		Boolean different = Boolean.FALSE;
		if (list1.size()>list2.size() | list1.size()<list2.size()) {
			different = Boolean.TRUE;
			return different;
		}
		else {
			for (String s:list1) {
				if (!list2.contains(s)) {
					different = Boolean.TRUE;
					System.out.println(s);
					return different;
				}
			}
		}
		return different;
	}
	
	
	public static ArrayList<String> getIDList(String file) throws FileNotFoundException{
		ArrayList<String> list = new ArrayList<String>();
		Scanner input = new Scanner(new File(file));
		input.nextLine();
		
		while (input.hasNext()) {
			String oneLine = input.nextLine();
			String[] valueList = oneLine.split(",",-1);
			String IDLabel = valueList[0];
			//String IDLabel = valueList[0]+","+ valueList[valueList.length-1];
			if (!list.contains(IDLabel)) {
				list.add(IDLabel);
			}
		}
		return list;
	}
	
//	2011-2016 26057
	
//	2017 20779
	
	//26035
	
	//20757

}
