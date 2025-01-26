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

public class FeatureWithIG_CVMerit implements Comparable<FeatureWithIG_CVMerit>{

	/**
	 * @param args
	 */
	private String featureName;
	private Double IGvalue;
	private Double rank;
	private int previousIndex;
	public FeatureWithIG_CVMerit(String feature, String IGstring, String rank, String previousIndex){
		this.featureName=feature;
		this.IGvalue=Double.parseDouble(IGstring);
		this.rank = Double.parseDouble(rank);
		this.previousIndex = Integer.parseInt(previousIndex);	
	}
	public void setFeature(String feature){
		this.featureName=feature;
	}
	public String getFeature(){
		return this.featureName;
	}
	public void setIGvalue(String IGstring){
		this.IGvalue=Double.parseDouble(IGstring);
	}
	public Double getIGvalue(){
		return this.IGvalue;
	}
	public int getPreviousIndex(){
		return this.previousIndex;
	}
	
	@Override
	public int compareTo(FeatureWithIG_CVMerit another) {
		// TODO Auto-generated method stub
		Double value = this.getIGvalue()-another.getIGvalue();
		if (value>0)
			return -1;
		else if (value<0)
			return 1;
		else
			return 0;
	}
	public String toString(){
		return this.featureName+","+this.IGvalue+","+this.rank+","+this.previousIndex;
	}

}
