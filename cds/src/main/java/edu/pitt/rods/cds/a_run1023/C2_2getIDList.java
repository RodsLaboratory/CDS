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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class C2_2getIDList {
   //ID,N_DT_ADMISSION_DATE
	static ArrayList<String> IDlist;
	static PrintWriter printout;
	public static void main(String[] args) throws Exception {
		String fileLocation = new String("/Users/yey5/Documents/Greg_PDS_project_after10162023/1.intermediate_data/");
		
		ArrayList<String> seasonList = new ArrayList<String>(); 			
		seasonList.add("2016-2017"); seasonList.add("2017-2018"); seasonList.add("2018-2019"); 
		seasonList.add("2019-2020"); seasonList.add("2020-2021");	
		
		for (String season:seasonList) {
			String outputFileName = fileLocation +"IDList_" + season + ".csv";
			printout = new PrintWriter(new File(outputFileName));
			printout.println("ID");
			String inputFileName = fileLocation + "data07122023_All_" + season + ".csv";	
			IDlist = new ArrayList<String>();
			printoutID(inputFileName);
			printout.close();
		}
	}
	
	public static void printoutID(String inputFileName) throws FileNotFoundException {	
		Scanner input = new Scanner(new File(inputFileName));
		input.nextLine(); //skip first line	
		while (input.hasNext()) {	
			String oneLine = input.nextLine();
			int firstCommaIndex = oneLine.indexOf(",");
			String ID = oneLine.substring(0,firstCommaIndex);
			if (IDlist.contains(ID)) {
				System.out.println("found duplicate");
			}
			else {
				IDlist.add(ID);
				printout.println(ID);
			}		
		}
		input.close();
		printout.flush();
		
	}
}
