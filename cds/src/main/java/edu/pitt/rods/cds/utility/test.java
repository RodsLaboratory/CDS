package edu.pitt.rods.cds.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
	
    public static void main(String[] args) {
        String inputString = "\"This, is a, test, string\", \"Another, string\", \"No comma here\"";
        String outputString = removeCommasWithinDoubleQuotes(inputString);
        System.out.println(outputString);
    }

    public static String removeCommasWithinDoubleQuotes(String input) {
        // Define the regular expression pattern to match the content within double quotes
        String pattern = "\"(.*?)\"";
        
        // Create a pattern object and a matcher object
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);
        
        // Use a StringBuilder to build the modified output string
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;
        
        // Iterate through the matches and replace commas within each match
        while (matcher.find()) {
            String content = matcher.group(1);
            String modifiedContent = content.replace(",", "");
            
            // Append everything between the last match and the current match
            result.append(input, lastEnd, matcher.start());
            
            // Append the modified match (without commas) to the result
            result.append("\"").append(modifiedContent).append("\"");
            
            // Update the lastEnd pointer for the next iteration
            lastEnd = matcher.end();
        }
        
        // Append the remaining part of the input string after the last match
        result.append(input.substring(lastEnd));
        
        return result.toString();
    }
	}
	
