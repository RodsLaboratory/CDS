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

### Data and models
The experiment data and models from our research are available at https://www.rods.pitt.edu/research/pds/.  You will 
need to request and then sign a data use agreement to access the data.  After obtaining the data and models, copy the
files to /data and /models directories in the project directory.

### Description of packages and classes

#### edu.pitt.rods.cds.a_run1023 - Main package for running the case detection system
A0_00compareTwoLists.java
* un-useful code for some data checking, could be removed 

A0_0getDiseaseLabel.java
* function:
  * process cleaning on the input files
    *add “M” for empty cell
    *replace | with ,
    *remove commas within double quotes
  * add diagnose labels to the input files to generate output files
  * note: the current code only process data for year 2020, if you want to process for all years, line 24 should change to the following:
    *for (int year = 2011; year<=2021; year++){
* input files:
  * PDS_ICD_lab_additionallab_combine_labels_10162023_updateCOV.csv
  * Data shared by Jessi, discrete_filtered_encounters_cuis_vitals_diagnosis_labs_2023-07-12/discrete_filtered_"+year+"_encounters_cuis_vitals_diagnosis_labs_2023-07-12.txt
* output files:
  * data07122023_discrete_add_label_" + year + ".csv"

A0_1getData_COV.java
* function:
  * for COV in season (2020-2021), prepare a training dataset:
    *encounters with COV: any encounters with disease LABEL =T in the season
    *encounters with OTHER : any encounters with N_DT_ADMISSION_DATE within [2020-08-01, 2021-08-31] ; excluding encounters that have any disease label and lab test results
* input files: data07122023_discrete_add_label_COV.csv
* output files: data07122023_ COV_2020-2021.csv

A0_1getData_EachDisease.java
* function:
  * for each disease ( INFLUENZA, RSV, ADENOVIRUS,PARAINFLUENZA, HMPV,ENTEROVIRUS) and each season (2011-2012, 2012-2013, 2013-2014, 2014-2015, 2015-2016, 2016-2017, 2017-2018, 2018-2019, 2019-2020, 2020-2021) prepare a training dataset:
    *encounters with the diseases: any encounters with disease LABEL =T in the season
    *encounters with OTHER : any encounters with N_DT_ADMISSION_DATE within [begin year-08-01, begin year-08-31], for example for season 2010-2011, the time range is [2010-08-01, 2011-08-31] ; excluding encounters that have any disease label and lab test results
* input files: data07122023_discrete_add_label_" + year + ".csv
* output files: data07122023_" + diseaseName + "_" + season + ".csv
  A0_2getData_Other.java
* function:
  * for the OTHER category, prepare training datasets
  * For the “Other” disease category in season Sj, our training dataset was sourced from the most recent season prior to Sj (season Sj-1). Negative cases comprised patient encounters with at least one label from the seven monitored diseases, while positive cases were those without any disease labels during August of season Sj-1.
* input files: data07122023_discrete_add_label_" + year + ".csv"
* output files: data07122023_OTHER_" + season + ".csv"));

A0_3getData_AllVisits.java
* function: get all encounters in each season for testing and likelihood generation purpose
* input files: data07122023_discrete_add_label_" + year + ".csv
* output files: “data07122023_All_” + season + ".csv")

A1_0getCleanFile.java
* function:
  * data cleaning for some categorical variables
  * some rules are saved in properties10172023.txt
    *RemoveVariables=N_DT_ADMISSION_DATE,N_DT_DISCHARGE_DATE,N_D_HOSPITAL_ID,N_V_DRG_CODE,N_F_CHIEF_COMPLAINT,D_T_ZIP_CODE,T_D_RSV,T_D_INFLUENZA_A,T_D_INFLUENZA_B,T_D_PARAINFLUENZA_1,T_D_PARAINFLUENZA_2,T_D_PARAINFLUENZA_3,T_D_PARAINFLUENZA_4,T_D_METAPNEUMOVIRUS,T_D_ADENOVIRUS,T_D_SARS_COV2,T_D_ENTEROVIRUS,N_C_BMI,N_C_BP_SYSTOLIC,N_C_BP_DIASTOLIC,N_C_PULSE,N_C_TEMPERATURE,N_C_RESPIRATORY_RATE,N_C_OXYGEN_SATURATION,PRIMARY_ICD9,PRIMARY_ICD10,ICD9,ICD10,SEASON,ICD_INFLUENZA,LAB_INFLUENZA,LABEL_INFLUENZA,ICD_RSV,LAB_RSV,LABEL_RSV,ICD_HMPV,LAB_HMPV,LABEL_HMPV,ICD_ADENOVIRUS,LAB_ADENOVIRUS,LABEL_ADENOVIRUS,ICD_ENTEROVIRUS,LAB_ENTEROVIRUS,LABEL_ENTEROVIRUS,ICD_PARAINFLUENZA,LAB_PARAINFLUENZA,LABEL_PARAINFLUENZA,ICD_COV,LAB_COV,LABEL_COV,LABEL_OTHER,LAB_INFLUENZA_ADDITIONAL,LAB_RSV_ADDITIONAL,LAB_HMPV_ADDITIONAL,LAB_PARAINFLUENZA_ADDITIONAL,LAB_ENTEROVIRUS_ADDITIONAL,LAB_ADENOVIRUS_ADDITIONAL,LABEL_INFLUENZA_NEW,LABEL_RSV_NEW,LABEL_PARAINFLUENZA_NEW,LABEL_HMPV_NEW,LABEL_ENTEROVIRUS_NEW,LABEL_ADENOVIRUS_NEW
    *ETHNICITY: HISPANIC_OR_LATINO;   NOT_HISPANIC_OR_LATINO; NOT_SPECIFIED(NOT_SPECIFIED, DECLINED);
    M(not filling any value)
    *RACE: WHITE; BLACK; NOT_SPECIFIED (DECLINED, DNU_OTHER, DNU_UNKNOWN, UNKNOWN, NOT_SPECIFIED); OTHER(ALASKA_NATIVE,AMERICAN_INDIAN,ASIAN,CHINESE,FILIPINO,GUAM/CHAMORRO,HAWAIIAN,INDIAN,INDIAN_(ASIAN),JAPANESE,KOREAN,OTHER_ASIAN,OTHER_PACIFIC_ISLANDER,SAMOAN,VIETNAMESE); M(not filling)
    *Age: less5 (<5), ge5less18 [5,18), ge18less65 [18,65), ge65 (>=65)
  *
* input files: properties10172023.txt, data07122023_" + disease + "_" + season + ".csv"
* output files: training_" + season + "_" + disease + "_cleaned.csv
  A1_0getCleanFileAllEncounters.java
* function: same as A1_0getCleanFile.java file, do that for all encounters
* input files: data07122023_" + disease + "_" + season + ".csv"
* output files: training_" + season + "_all_cleaned.csv";
  A1_1getArffHeader.java
* function: get the header (for arff file generations) by considering multiple year situation
  * For all UMLS codes, potential values are {P,N,M};
  * For all lab codes, potential values are {H,L,N,M}
* input files: training_" + season + "_" + disease + "_cleaned.csv"
* output files: header_updated_consolidated.txt
  A1_2getArffAll.java
* function: add arff header to csv file (all encoutners) to generate arff files
* input files: header_updated_consolidated_all.txt; training_" + oneSeason +  "_All_cleaned.csv";
* output files: training_" + oneSeason +  "_All_cleaned.arff"
  A1_2getArffMultiYear.java
* function: add arff header to csv file (disease training datasets) to generate arff files
  * To develop a predictive model for disease Di in season Sj, we compiled a training dataset from the four seasons preceding Sj (specifically, seasons Sj-4, Sj-3, Sj-2, and Sj-1). This dataset included all patient encounters labeled with disease Di. As negative cases, we selected patient encounters from August of season Sj-1 that did not exhibit any ICD codes or positive laboratory results for our seven key monitored diseases: influenza, RSV, hMPV, adenovirus, enterovirus, parainfluenza, and COVID-19. August was chosen based on expert consensus as a month typically exhibiting low prevalence for these diseases. An encounter in this period would have a low possibility of having any of these diseases if it did not exhibit any ICD codes or positive laboratory results.
* input files: header_updated_consolidated_all.txt; training_" + oneSeason +  "_All_cleaned.csv";
* output files: training_"+ oneRange + "_" +  diseaseName + "_cleaned.arff"
  A2_processTestData.java
* ignore this file. The files with “All” are test files.
  B1_univariate_rank_feature.java
* Run following command to get information gain, for examples:
* java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2012to2013_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2012to2013\featureRanking.txt
* input files: for example, training_2012to2013_RSV_cleaned.arff
* output files: for example, featureRanking.txt
  B2_SortDataByInforGain_simple.java
* Process information gain result files (add feature names)
* input files: pds_dataset_umls_cui.txt, pds_dataset_loinc.txt
* output files:  total_informationGainScore_CVMerit_simple.csv , total_informationGainScore_CVMerit_simple_description.csv
  B3_getFeatureUnionAll.java
* Get the union of features (for all diseases) based on threshold (0.001)
* input files: total_informationGainScore_CVMerit_simple.csv in different disease folders
* output files:  union_feature_" + threshold + "_" + year + "_all_V2.csv”
  B4_filterArffBasedonIG_unionAll.java
* Filter arff  files based on information gain features
* input files: training_" + year + "_" + disease + "_cleaned.arff"
  training_" + nextYear + "_" + disease + "_cleaned.arff
* output files:
  * train.arff, test.arff for multiple diseases and multiple years  
    B5_getNBmodel_testPerformance.java
* Learn NB models, save models, test performance
* input files: train.arff, test.arff
* output files:  NB_new.xml, result.csv, multiple files saved in multiple folders for each disease each season
  B5_getSummary_from_resultFiles.java
* Get result summary from the test performance
* Input files: result.csv
* Output files: auc_summary_union_feature_all_" + threshold + ".csv"
  C1_getFilteredData_for_LRcalcualtion.java
* clean the data to produce likelihoods
* input: union_feature_" + threshold+"_" + thisYearString +"_all.csv”; training_" + nextYearString + "_All_cleaned.arff";
* output files: training_" + thisYearString + "_test_" + nextYearString + "_All_cleaned_union_" + threshold + ".arff”
  C2_1getLR.java
* generate likelihoods for all encounters
* input files: training_" + thisYearString + "_test_" + nextYearString + "_All_cleaned_union_" + threshold + ".arff”
* output files: generatedLR_allVisits.csv, generatedProb_allVisits.csv multiple files for different disease and multiple seasons
  C2_2getIDList.java
* just data quality check to whether ID is distinct or not. Could be removed
  C2_linkLR.java
* link likelihood files with the labels and dates to provide files for John
* input files: IDList_" + testYear + ".csv", generatedLR_allVisits.csv for multiple diseases
* output files: LR_test_" + testYear + "_" + threshold + ".csv"

* C3_retrieveProb.java
* Could be removed

#### edu.pitt.rods.cds.utility - Utility classes
Check.java
* Could be removed

Configuration.java 

* FeatureWithIG_CVMerit.java
* Called by B2_SortDataByInforGain_simple

* FeatureWithIG_MultiDiseases.java
* Could be removed
  Print.java
* Could be removed
  combine_data.java
* Could be removed
  test.java
* Could be removed

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


ICD_X: Label generated solely from ICD codes.
LAB_X: Label generated solely from laboratory data (excluding Theradoc data).
LABEL_X: Comprehensive label derived from both ICD_X and LAB_X.
LAB_X_ADDITIONAL: counts from theradoc data
LABEL_X_NEW: An advanced label combining LABEL_X and LAB_X_ADDITIONAL

