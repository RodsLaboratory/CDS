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
### Data and models
Experimental models from our research are available at https://github.com/RodsLaboratory/CDS/tree/branch-10272025/models.  You will 
need to request research data and then sign a data use agreement to access the data.  After obtaining the data and models, copy the
files to /data and /models directories in the project directory.

### Citing
If you use this software in your research, please cite the following paper:


### Description of packages and classes

#### edu.pitt.rods.cds - Main package for running the case detection system


 

GetNBmodel_testPerformance.java
* Learn NB models, save models, test performance
* input files: train.arff, test.arff
* output files:  NB_new.xml, result.csv, multiple files saved in multiple folders for each disease each season


GetLR_Prob.java
* generate likelihoods and probabilities for all encounters
* input files: training_" + thisYearString + "_test_" + nextYearString + "_All_cleaned_union_" + threshold + ".arff”
* output files: generatedLR_allVisits.csv, generatedProb_allVisits.csv multiple files for different disease and multiple seasons


#### edu.pitt.rods.cds.utility - Utility classes

Configuration.java 


### Data File Descriptions
LR_test_20XX-20XX_0.001_V2.csv - Likelihood data file.  
This is a file of likelihood ratios for each feature for each disease.  The file is in CSV format with the following columns:
* ID - Record ID
* SEASON - Season of the data
* ICD_<DISEASE> - ICD Status of the disease [M - missing, T - true, F - false]
* LAB_<DISEASE> - Lab Status of the disease [M,T,F]
* LABEL_<DISEASE> - ICD/Lab Status of the disease [M,T,F] - using rule ***
* Admit_date_time - Time of ED visit in YYYY-MM-DD HH24:MM:SS
* <DISEASE>_loglikelihood_M - Log likelihood of the disease missing given the Label value
* <DISEASE>_loglikelihood_T - Log likelihood of the diseasetrue  given the Label value
* <DISEASE>_Prob_M - Probability of the disease missing given the Label value
* <DISEASE>_Prob_T - Probability of the disease true given the Label value


* LABEL_X: Comprehensive label derived from both ICD_X and LAB_X.
* LAB_X_ADDITIONAL: counts from theradoc data
* LABEL_X_NEW: An advanced label combining LABEL_X and LAB_X_ADDITIONAL

