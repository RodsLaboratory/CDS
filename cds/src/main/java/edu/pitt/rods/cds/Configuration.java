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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Configuration {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		String location = getExperimentFolderLocation("properties.txt");
		System.out.println(location);
	}
	public static String getExperimentFolderLocation(String inputFile) throws FileNotFoundException{
		Scanner input = new Scanner (new File(inputFile));
		String location = new String("");
		while (input.hasNext()){
			String oneLine = input.nextLine();
			if (oneLine.contains("ExperimentFolderLocation")){
				String[] values = oneLine.split("=");
				location = values[1];
			}
		}
		input.close();
		return location;	
	}
	
	public static ArrayList<String> getRemoveVariableList(String inputFile) throws FileNotFoundException{
		Scanner input = new Scanner (new File(inputFile));
		String variableString = new String("");
		while (input.hasNext()){
			String oneLine = input.nextLine();
			if (oneLine.contains("RemoveVariables")){
				String[] values = oneLine.split("=");
				variableString = values[1];
			}
		}
		String[] variableList = variableString.split(",");
 		input.close();		
 		ArrayList<String> arrayList = new ArrayList<String>(); 
 		Collections.addAll(arrayList, variableList); 	
		return arrayList;	
	}
	
	public static String getClassVariable(String inputFile) throws FileNotFoundException{
		Scanner input = new Scanner (new File(inputFile));
		String classVariable = new String("");
		while (input.hasNext()){
			String oneLine = input.nextLine();
			if (oneLine.contains("ClassVariable")){
				String[] values = oneLine.split("=");
				classVariable = values[1];
			}
		}
		input.close();
		return classVariable;	
	}
	
	public static HashMap<String,String> getPatterns(String inputFile) throws FileNotFoundException{
		Scanner input = new Scanner (new File(inputFile));
		HashMap<String,String> arffFileStringTable = new HashMap<String,String>();	
		while (input.hasNext()){
			String oneLine = input.nextLine();
			if (oneLine.contains("Patterns")){
				String[] values = oneLine.split("=")[1].split(";");
				for (int i=0; i<values.length; i++) {
					String[] list = values[i].split(":");
					arffFileStringTable.put(list[0],list[1]);
				}				
			}
		}
		input.close();
		return arffFileStringTable;	
	}
	
}
