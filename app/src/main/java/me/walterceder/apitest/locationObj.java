package me.walterceder.apitest;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iguest on 5/6/15.
 */
public class locationObj implements Parcelable{
    private String name;
    private List dates;
    private List results;
    private List desc;
    private List type;

    public locationObj(String name) {
        this.name = name;
        this.dates = new ArrayList<String>();
        this.results = new ArrayList<String>();
        this.desc = new ArrayList<String>();
        this.type = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public List getDates() {
        return dates;
    }

    public List getResults() {
        return results;
    }

    public List getDesc() {
        return desc;
    }

    public List getType() {
        return type;
    }

    public void addDate(String date){
        dates.add(date);
    }
    public void addResult(String result){
        results.add(result);
    }
    public void addDesc(String de){

        desc.add(de);

    }
    public void addType(String ty){
        type.add(ty);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(dates);
        dest.writeList(results);
        dest.writeList(desc);
        dest.writeList(type);
    }

    public static final Parcelable.Creator<locationObj> CREATOR = new Parcelable.Creator<locationObj>() {
        public locationObj createFromParcel(Parcel in) {
            return new locationObj(in);
        }

        public locationObj[] newArray(int size) {
            return new locationObj[size];
        }

    };
    private locationObj(Parcel in) {
        this.name = in.readString();
        dates = new ArrayList<String>();
        in.readList(dates, null);
        results = new ArrayList<String>();
        in.readList(results, null);
        desc = new ArrayList<String>();
        in.readList(desc, null);
        type = new ArrayList<String>();
        in.readList(type, null);

    }
}
