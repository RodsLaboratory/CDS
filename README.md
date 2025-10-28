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
Experimental models from our research are available at https://github.com/RodsLaboratory/CDS/tree/main/models.  You will 
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


