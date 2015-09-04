/*Class to desc all data about iconUrls on simple news page!
  Include: year, img path, author's nickyear, number of iconUrls, current rating by user's, and url link to full news page
  Also here supported writeToParcel to save this data after rotate screen on all of the devices!
  Author Roman Gaitbaev writed for ..
  http://vk.com/romzesrover 
  Created: 18.08.2013 00:58*/

/*Modified to lenfilm at 22 06 2014 */

package com.BBsRs.bspuscheduler;

import android.os.Parcel;
import android.os.Parcelable;

public class DiscCollection implements Parcelable {
  
	public String discName;
	public String weeks;
    public String numberInUni;
    public String Time;
    public String group;
    public String place;
    public String curator;
    public String type;
  

  public DiscCollection(String _discName, String _weeks, String _numberInUni, String _Time, String _group, String _place, String _curator, String _type) {
	  discName = _discName;
	  weeks = _weeks;
	  numberInUni = _numberInUni;
	  Time = _Time;
	  group = _group;
	  place = _place;
	  curator = _curator;
	  type = _type;
  }


@Override
public int describeContents() {
	return 0;
}

private DiscCollection(Parcel in) {
	discName = in.readString();
	weeks = in.readString();
	numberInUni = in.readString();
	Time = in.readString();
	group = in.readString();
	place = in.readString();
	curator = in.readString();
	type = in.readString();
}

@Override
public void writeToParcel(Parcel out, int flags) {
	 out.writeString(discName);
	 out.writeString(weeks);
	 out.writeString(numberInUni);
	 out.writeString(Time);
	 out.writeString(group);
	 out.writeString(place);
	 out.writeString(curator);
	 out.writeString(type);
	 
}

public static final Parcelable.Creator<DiscCollection> CREATOR = new Parcelable.Creator<DiscCollection>() {
    public DiscCollection  createFromParcel(Parcel in) {
        return new DiscCollection (in);
    }

    public DiscCollection [] newArray(int size) {
        return new DiscCollection [size];
    }
};
}