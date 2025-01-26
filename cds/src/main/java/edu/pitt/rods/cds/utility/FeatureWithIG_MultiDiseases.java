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

public class FeatureWithIG_MultiDiseases {

	/**
	 * @param args
	 */
	private String featureCode;
	private int previousIndex;
	private String description;
	
	private Double IGvalue_INFLUENZA;
	private int rank_INFLUENZA;
	
	private Double IGvalue_RSV;
	private int rank_RSV;
	
	private Double IGvalue_HMPV;
	private int rank_HMPV;
	
	private Double IGvalue_PARAINFLUENZA;
	private int rank_PARAINFLUENZA;
	
	private Double IGvalue_ADENOVIRUS;
	private int rank_ADENOVIRUS;
	
	private Double IGvalue_ENTEROVIRUS;
	private int rank_ENTEROVIRUS;
	
	private Double IGvalue_OTHER;
	private int rank_OTHER;
	
	private Double IGvalue_COV;
	private int rank_COV;
	
	public FeatureWithIG_MultiDiseases(String code, String previousIndex, String desc){
		this.featureCode=code;	
		this.previousIndex = Integer.parseInt(previousIndex);	
		this.description = desc;
		setIGvalue("INFLUENZA", "0", "-1");
		setIGvalue("RSV", "0", "-1");
		setIGvalue("HMPV", "0", "-1");
		setIGvalue("PARAINFLUENZA", "0", "-1");
		setIGvalue("ADENOVIRUS", "0", "-1");
		setIGvalue("ENTEROVIRUS", "0", "-1");
		setIGvalue("OTHER", "0", "-1");
		setIGvalue("COV", "0", "-1");
	}
	
	public void setFeature(String feature){
		this.featureCode=feature;
	}
	public String getFeature(){
		return this.featureCode;
	}
	public void setIGvalue(String disease, String IGstring, String rank){
		if (disease.equals("INFLUENZA")) {
			this.IGvalue_INFLUENZA=Double.parseDouble(IGstring);
			this.rank_INFLUENZA=Integer.parseInt(rank);
		}
		else if (disease.equals("RSV")) {
			this.IGvalue_RSV=Double.parseDouble(IGstring);
			this.rank_RSV=Integer.parseInt(rank);
		}
		else if (disease.equals("HMPV")) {
			this.IGvalue_HMPV =Double.parseDouble(IGstring);
			this.rank_HMPV=Integer.parseInt(rank);
		}
		else if (disease.equals("PARAINFLUENZA")) {
			this.IGvalue_PARAINFLUENZA =Double.parseDouble(IGstring);
			this.rank_PARAINFLUENZA=Integer.parseInt(rank);
		}
		else if (disease.equals("ADENOVIRUS")) {
			this.IGvalue_ADENOVIRUS =Double.parseDouble(IGstring);
			this.rank_ADENOVIRUS=Integer.parseInt(rank);
		}
		else if (disease.equals("ENTEROVIRUS")) {
			this.IGvalue_ENTEROVIRUS =Double.parseDouble(IGstring);
			this.rank_ENTEROVIRUS=Integer.parseInt(rank);
		}
		else if (disease.equals("OTHER")) {
			this.IGvalue_OTHER =Double.parseDouble(IGstring);
			this.rank_OTHER=Integer.parseInt(rank);
		}
		else if (disease.equals("COV")) {
			this.IGvalue_COV =Double.parseDouble(IGstring);
			this.rank_COV=Integer.parseInt(rank);
		}
	}
	
	public Double getIGvalue(String disease){
		if (disease.equals("INFLUENZA")) {
			return this.IGvalue_INFLUENZA;
		}
		else if (disease.equals("RSV")) {
			return this.IGvalue_RSV;
		}
		else if (disease.equals("HMPV")) {
			return this.IGvalue_HMPV;
		}
		else if (disease.equals("PARAINFLUENZA")) {
			return this.IGvalue_PARAINFLUENZA;
		}
		else if (disease.equals("ADENOVIRUS")) {
			return this.IGvalue_ADENOVIRUS;
		}
		else if (disease.equals("ENTEROVIRUS")) {
			return this.IGvalue_ENTEROVIRUS;
		}
		else if (disease.equals("OTHER")) {
			return this.IGvalue_OTHER;
		}
		else if (disease.equals("COV")) {
			return this.IGvalue_COV;
		}
		else return -1.0;
	}
	
	public int getPreviousIndex(){
		return this.previousIndex;
	}
	
//	@Override
//	public int compareTo(FeatureWithIG_CVMerit another) {
//		// TODO Auto-generated method stub
//		Double value = this.getIGvalue()-another.getIGvalue();
//		if (value>0)
//			return -1;
//		else if (value<0)
//			return 1;
//		else
//			return 0;
//	}
	public String toString(){
		return 	this.featureCode + ","+ this.description + "," + this.previousIndex  + "," + this.IGvalue_INFLUENZA + "," + this.rank_INFLUENZA 
				+ "," + this.IGvalue_RSV + "," + this.rank_RSV +"," + this.IGvalue_HMPV + "," + this.rank_HMPV + "," + this.IGvalue_PARAINFLUENZA 
				+ "," + this.rank_PARAINFLUENZA+ "," + this. IGvalue_ADENOVIRUS+ "," + this. rank_ADENOVIRUS+ "," + this. IGvalue_ENTEROVIRUS+ "," + this.rank_ENTEROVIRUS
				+ "," + this. IGvalue_OTHER+ "," + this.rank_OTHER+ "," + this. IGvalue_COV+ "," + this.rank_COV;
	}

}
