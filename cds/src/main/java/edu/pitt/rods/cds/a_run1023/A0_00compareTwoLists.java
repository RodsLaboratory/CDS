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


package edu.pitt.rods.cds.a_run1023;

import java.io.*;
import java.util.*;

public class A0_00compareTwoLists {
	static String originalfileLoc;
	static String intermediateLoc;
	
	public static void main(String[] args) throws Exception {
		originalfileLoc  = new String("/Users/yey5/Documents/Greg_PDS_project_after10162023/0.original_data/");
		intermediateLoc = new String("/Users/yey5/Documents/Greg_PDS_project_after10162023/1.intermediate_data/");
		
		printoutIDList("LABEL_INFLUENZA_NEW", intermediateLoc+"data07122023_INFLUENZA_2012-2013.csv", "new_data_list2.csv");
		//printoutIDList("LABEL_INFLUENZA", "/Users/yey5/Documents/Greg_PDS_project_before10162023/1.intermediate_data/data07032023_INFLUENZA_2012-2013_new.csv", "old_data_list.csv");
	
	}
	
	public static void printoutIDList(String variableName, String inputFileName, String tempFileName) throws FileNotFoundException {
		Scanner input = new Scanner(new File(inputFileName));
		PrintWriter printout = new PrintWriter(new File(intermediateLoc + tempFileName));
		String header = input.nextLine(); 
		List<String> header_list = new ArrayList<String>(Arrays.asList(header.split(",")));	
		int index = header_list.indexOf(variableName);
		while (input.hasNext()) {
			String oneLine = input.nextLine();
			String[] stringList = oneLine.split(",");
			String ID = stringList[0];
			String fluStatus = stringList[index];
			if (fluStatus.equals("T")) {
				printout.println(ID+","+fluStatus);
			}
		}
		printout.flush();
		printout.close();
	}
}
