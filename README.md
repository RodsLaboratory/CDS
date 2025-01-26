# Case Detection System Documentation

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

### Description of packages and classes

#### edu.pitt.rods.cds.a_run1023 - Main package for running the case detection system
A0_00compareTwoLists.java
A0_0getDiseaseLabel.java
A0_1getData_COV.java
A0_1getData_EachDisease.java
A0_2getData_Other.java
A0_3getData_AllVisits.java
A1_0getCleanFile.java
A1_0getCleanFileAllEncounters.java
A1_1getArffHeader.java
A1_2getArffAll.java
A1_2getArffMultiYear.java
A2_processTestData.java
B1_univariate_rank_feature.java
B2_SortDataByInforGain_simple.java
B3_getFeatureUnionAll.java
B4_filterArffBasedonIG_unionAll.java
B5_getNBmodel_testPerformance.java
B5_getSummary_from_resultFiles.java
C1_getFilteredData_for_LRcalcualtion.java - clean the data to produce likelihood ratios
C2_1getLR.java
C2_2getIDList.java
C2_linkLR.java
C3_retrieveProb.java

#### edu.pitt.rods.cds.utility - Utility classes
Check.java
Configuration.java
FeatureWithIG_CVMerit.java
FeatureWithIG_MultiDiseases.java
Print.java
combine_data.java
test.java

### Data File Descriptions
LR_test_20XX-20XX_0.001_V2.csv - Likelihood ratio data file.  
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
