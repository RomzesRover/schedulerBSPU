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

public class SchedulesCollection implements Parcelable {
  
	public String id;
	public String university;
    public String faculty;
    public String group;
    public String comment;
    public String link;
  

  public SchedulesCollection(String _id, String _university, String _faculty, String _group, String _comment, String _link) {
	  id = _id;
	  university = _university;
	  faculty = _faculty;
	  group = _group;
	  comment = _comment;
	  link = _link;
  }


@Override
public int describeContents() {
	return 0;
}

private SchedulesCollection(Parcel in) {
	id = in.readString();
	university = in.readString();
	faculty = in.readString();
	group = in.readString();
	comment = in.readString();
	link = in.readString();
}

@Override
public void writeToParcel(Parcel out, int flags) {
	 out.writeString(id);
	 out.writeString(university);
	 out.writeString(faculty);
	 out.writeString(group);
	 out.writeString(comment);
	 out.writeString(link);
	 
}

public static final Parcelable.Creator<SchedulesCollection> CREATOR = new Parcelable.Creator<SchedulesCollection>() {
    public SchedulesCollection  createFromParcel(Parcel in) {
        return new SchedulesCollection (in);
    }

    public SchedulesCollection [] newArray(int size) {
        return new SchedulesCollection [size];
    }
};
}