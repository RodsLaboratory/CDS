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

public class B1_univariate_rank_feature {
	/*
	 * see A3_IG_filtering.txt
	 * 
	 * java -Xmx60g -cp weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i /Users/yey5/Documents/Greg_PDS_project/1.intermediate_data/training_2012-2015_INFLUENZA_cleaned.arff -x 10  > /Users/yey5/Documents/Greg_PDS_project/3.results/featureRanking_2012-2015.txt
	 * 
	 * java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2011to2012_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2011to2012\featureRanking.txt
	 * 
	 * java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2011to2012_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2011to2012\featureRanking.txt

java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2012to2013_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2012to2013\featureRanking.txt

java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2013to2014_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2013to2014\featureRanking.txt

java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2014to2015_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2014to2015\featureRanking.txt

java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2015to2016_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2015to2016\featureRanking.txt

java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2016to2017_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2016to2017\featureRanking.txt

java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2017to2018_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2017to2018\featureRanking.txt

java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2018to2019_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2018to2019\featureRanking.txt


java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2019to2020_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2019to2020\featureRanking.txt


java -Xmx8g -cp E:\Dropbox\YeEclipse\lib\weka.jar weka.attributeSelection.InfoGainAttributeEval -s "weka.attributeSelection.Ranker -T -1.7976931348623157E308 -N -1" -i E:\Greg_PDS_research_exp2023\intermediate_data\training_2020to2021_RSV_cleaned.arff -x 10  > E:\Greg_PDS_research_exp2023\results\2020to2021\featureRanking.txt


	 * */
}
