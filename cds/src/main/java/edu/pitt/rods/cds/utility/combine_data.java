package edu.pitt.rods.cds.utility;
/**code written by John **/

import java.io.*;
import java.util.*;


import edu.pitt.rods.cds.utility.Configuration;

public class combine_data {

    static String suffix;
    static PrintWriter writer;
    static List<File> names;
    
    static String generalFileLocation;

    public static void main(String[] args) throws IOException {


// running
    	String property_file_name = "properties.txt";		
		generalFileLocation=Configuration.getExperimentFolderLocation(property_file_name);
		
		ArrayList<String> diseaseList = new ArrayList<String>();		
		diseaseList.add("INFLUENZA"); 
		diseaseList.add("RSV");	
		diseaseList.add("ADENOVIRUS"); diseaseList.add("PARAINFLUENZA"); 
		diseaseList.add("HMPV"); 
		diseaseList.add("ENTEROVIRUS"); 
		
		for (String d:diseaseList) {
			String disease = d;		
	        String folder = generalFileLocation  + "1.intermediate_data/";
	        suffix ="_new_cleaned.arff";
	        String title_f = folder + "training_2011-2012_" + disease +"_new_cleaned.arff";

	        // get file names
	        names = folderName(folder, suffix, disease);

	        // start to write data
	        writeData_arff_csv(names,title_f,suffix,2011,2013,disease);
//	        writeData_arff_csv(names,title_f,suffix,2011,2014,disease);
//	        writeData_arff_csv(names,title_f,suffix,2011,2015,disease);
//	        writeData_arff_csv(names,title_f,suffix,2012,2014,disease);
//	        writeData_arff_csv(names,title_f,suffix,2012,2015,disease);   
//	        writeData_arff_csv(names,title_f,suffix,2013,2015,disease);
		}

 
    }

   

    public static List<File> folderName(String folder, String suffix, String disease){
        File directory = new File(folder);
        File[] files = directory.listFiles();
        List<File> result = new ArrayList<>();
        // loop to get file
        for(File f: files){
            if(f.isFile() && f.getName().endsWith(suffix) && f.getName().contains("_"+disease+"_")){
                result.add(f);
                //System.out.println(f);
            }
        }
        return result;

    }

//    public static List<List<File>> generateCombo(List<File> names, String disease){
//        List<List<File>> combo = new ArrayList<>();
//        for(int i = names.size()-1; i>=0;i--){
//            List<File> each = new ArrayList<>();
//            for(int j =0; j<=i;j++){
//                each.add(names.get(j));
//            }
//            combo.add(each);
//        }
//        // remove the one
//        combo.remove(combo.size()-1);
//        display(combo);
//        return combo;
//    }
    
    

    	
    public static void writeData_arff_csv(List<File> combo, String title_f,String suffix, int start, int end, String disease) throws IOException {
        // file name
      
        // output file
        String output = generalFileLocation  + "1.intermediate_data_generated/training_" + start + "-" + end + "_" + disease + "_new_cleaned.arff";
        writer = new PrintWriter(new BufferedWriter(new FileWriter(output)));
        
        
        arffGetTitle(title_f);
        if(suffix.equals(".arff") ){
            for (File fName: combo){
            	readArff(fName);      
            }
        }else{
            // get title csv
            String title = getTitle(title_f);
            writer.println(title);
            // write
            for (File fName: combo){
            	String tempRange =fName.getName().split("_")[1];
            	int startYear_fName = Integer.parseInt(tempRange.split("-")[0]);
            	int endYear_fName = Integer.parseInt(tempRange.split("-")[1]);
            	if (startYear_fName>=start && endYear_fName<=end) {
            		readFile(fName);
            	}
            }
        }
        writer.close();
        end--;
        System.out.println("finish: "+ end);
        
    }



    public static void display(List<List<File>> combo){
        for(List<File> e: combo){

            for(File b: e){
                System.out.print(b+" | ");
            }
            System.out.println();
        }
    }


    public static void readFile(File name) throws IOException{

        BufferedReader br = new BufferedReader(new FileReader(name));

        String line = "";
        // skip title
        line = br.readLine();

        while((line= br.readLine()) != null){

            writer.println(line);
        }
        System.out.println("finished: "+name);
        br.close();


    }


    public static String getTitle(String name) throws IOException{
        String result = "";
        BufferedReader br = new BufferedReader(new FileReader(name));

        result = br.readLine();

        System.out.println(result);

        return result;
    }
///
    public static void arffGetTitle(String name) throws IOException{

        BufferedReader br = new BufferedReader(new FileReader(name));

        String line = "";


        while(!(line= br.readLine()).equals("@data")){

           //System.out.println(line);
            writer.println(line);

        }
        //System.out.println(line);
        writer.println(line);

        br.close();

    }

    public static void readArff(File name) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(name));

        String line = "";
        while(!(line= br.readLine()).equals("@data")){

        }

        // data
        while((line= br.readLine())!=null){
            writer.println(line);
        }

        br.close();

    }
}
