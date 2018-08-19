package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

/*
 * this class hold data of blood glucose level in a date
 */
public class bloodGlucoseLevel implements Serializable, Comparable{
    private static final long serialVersionUID = 6986391446511511154L;
    private UUID ID;
    int fasting = -1;
    int bf;
    int lunch;
    int dinner;
    String note = "";
    Calendar date;
    int HYPOGLYCEMIC = -1;
    int NORMAL = 0;
    int ABNORMAL =1;

    public bloodGlucoseLevel(Calendar date){
        this(date, UUID.randomUUID());
    }
    public bloodGlucoseLevel(Calendar date, UUID id){
        this.date = date;
        ID = id;

    }

    public void setFasting(int fasting) {
        this.fasting = fasting;
    }
    public void setBf(int bf){
        this.bf = bf;
    }
    public void setLunch(int lunch) {
        this.lunch = lunch;
    }
    public void setDinner(int dinner) {
        this.dinner = dinner;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public int normalFasting(){
        if (fasting >= 70&& fasting<100) {
            return NORMAL;
        } else {
            return ABNORMAL;
        }
    }
    public int normalBF(){
        return isNormal(bf);
    }
    public int normalLunch(){
        return isNormal(lunch);
    }
    public int normalDinner(){
        return isNormal(dinner);
    }
    public UUID getID(){
        return ID;
    }

    // return true is every thing in this day was normal
    public boolean normalDay(){
        if(normalFasting() == 0){
            if(normalBF() == 0){
                if(normalLunch() == 0){
                    if(normalDinner() == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // return -1 for Hypoglycemic, 0 for normal, 1 for abnormal
    // to represent if passed in data is nor
    private int isNormal(int data){
            if (data < 70) {
                return HYPOGLYCEMIC;
            } else if (data < 140) {
                return NORMAL;
            } else {
                return ABNORMAL;
            }
    }

    @Override
    public boolean equals(Object obj) {
        bloodGlucoseLevel other = (bloodGlucoseLevel) obj;
        try{
            return (getDate().equals(other.getDate()));
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public int compareTo(Object obj){
        bloodGlucoseLevel other = (bloodGlucoseLevel) obj;
        try {
            return date.compareTo(other.date);
        }catch (Exception e){
            return 1;
        }
    }

    public String getDate(){
        String[] string = date.getTime().toString().split(" ");
        return string[1]+" "+string[2]+", "+string[5];
    }

    public int getAvg(){
        return (fasting+bf+lunch+dinner)/4;
    }

    // return all data stored in the obj in string
    public String toString(){
        String string = "";
        String note = "SYSTEM.NO.STRING";
        if(this.note != null){
            note = this.note;
        }
        string = getDate()+",-=-=" + note+",-=-="+getAvg()+",-=-="+normalDay();
        return string;
    }

    public JSONObject toJSON() throws Exception{
        JSONObject json = new JSONObject();
        json.put("ID", ID.toString());
        json.put("date", getDate());
        json.put("fasting",fasting);
        json.put("breakfast",bf);
        json.put("lunch",lunch);
        json.put("dinner",dinner);
        if(normalDay()){
            json.put("isNormal",1);
        }else {
            json.put("isNormal", 0);
        }
        json.put("note", note);
        return json;
    }
}
