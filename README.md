# Case Detection System 

### Prerequisites
* Java Developer Kit – version 11+
  * Tested using Correto 17 JDK
* Maven

### Source Code
-	https://github.com/rodslaboratory/cds


### Setup
Edit the <project directory>/cds/src/main/resources/properties.txt file to reflect the ExperimentFolderLocation


### Compiling
The project can be compiled using the Maven build tool.  To compile:
```
cd <project directory>/cds 
mvn package
```
### Models
The pre-trained models used in the case detection system can be found in the models subdirectory.  These models are in 
three formats: Weka .model files, comma-delimited files (.csv) and text files (.txt). The conventions we used in the
abbreviations are the following:
* M - missing or not present
* T - true or present
* P - positive
* N - negative
* H - high
* N - normal 
* L - low


### Citing
If you use this software in your research, please cite the following paper:


### Description of packages and classes

#### edu.pitt.rods.cds - Main package for running the case detection system
 

GetNBmodel_testPerformance
* Learn NB models, save models, test performance
* input files: train.arff, test.arff
* output files:  NB_new.xml, result.csv, multiple files saved in multiple folders for each disease each season


GetLR_Prob
* generate likelihoods and probabilities for all encounters
* input files: training_" + thisYearString + "_test_" + nextYearString + "_All_cleaned_union_" + threshold + ".arff”
* output files: generatedLR_allVisits.csv, generatedProb_allVisits.csv multiple files for different disease and multiple seasons

Configuration
* load configuration parameters from properties.txt file

NaiveBayesEvaluator
* load .model files by year
* evaluate case data produced by Metamap and generate prediction results
* input files:  .model files, .arff files
* output files:  prediction results in .csv format
* 



