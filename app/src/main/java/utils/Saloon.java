package utils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Aisha on 6/16/2017.
 */

public class Saloon implements Serializable {

    private long saloonPoint;
    private String saloonLocation;
    private int saloonRating;
    private String saloonName;
    private boolean saloonUpdated;
    private boolean saloonHirePhotographer;
    private String saloonUID = "";
    private String saloonEmailID;

    private String saloonPhoneNumber;//input
    private String saloonAddress;//input

    double saloonLocationLatitude;
    double saloonLocationLongitude;

    private Map<String, String> saloonImageList;

    private int saloonRatingSum, saloonTotalRating;


    //timing
    private int openingTimeHour, openingTimeMinute, closingTimeHour, closingTimeMinute;

    private String saloonCity ;
    private int saloonCityIndex ;


    public boolean isSaloonHirePhotographer() {
        return saloonHirePhotographer;
    }

    public void setSaloonHirePhotographer(boolean saloonHirePhotographer) {
        this.saloonHirePhotographer = saloonHirePhotographer;
    }


    public String getSaloonUID() {
        return saloonUID;
    }

    public void setSaloonUID(String saloonUID) {
        this.saloonUID = saloonUID;
    }

    public Saloon() {

    }

    public long getSaloonPoint() {
        return saloonPoint;
    }

    public void setSaloonPoint(long saloonPoint) {
        this.saloonPoint = saloonPoint;
    }

    public String getSaloonLocation() {
        return saloonLocation;
    }

    public void setSaloonLocation(String saloonLocation) {
        this.saloonLocation = saloonLocation;
    }

    public int getSaloonRating() {
        return saloonRating;
    }

    public void setSaloonRating(int saloonRating) {
        this.saloonRating = saloonRating;
    }


    public String getSaloonName() {
        return saloonName;
    }

    public void setSaloonName(String saloonName) {
        this.saloonName = saloonName;
    }

    public boolean isSaloonUpdated() {
        return saloonUpdated;
    }

    public void setSaloonUpdated(boolean saloonUpdated) {
        this.saloonUpdated = saloonUpdated;
    }

    public String getSaloonPhoneNumber() {
        return saloonPhoneNumber;
    }

    public void setSaloonPhoneNumber(String saloonPhoneNo) {
        this.saloonPhoneNumber = saloonPhoneNo;
    }

    public String getSaloonAddress() {
        return saloonAddress;
    }

    public void setSaloonAddress(String saloonAddress) {
        this.saloonAddress = saloonAddress;
    }


    public double getSaloonLocationLatitude() {
        return saloonLocationLatitude;
    }

    public void setSaloonLocationLatitude(double saloonLocationLatitude) {
        this.saloonLocationLatitude = saloonLocationLatitude;
    }

    public double getSaloonLocationLongitude() {
        return saloonLocationLongitude;
    }

    public void setSaloonLocationLongitude(double saloonLocationLongitude) {
        this.saloonLocationLongitude = saloonLocationLongitude;
    }

    public Map<String, String> getSaloonImageList() {
        return saloonImageList;
    }

    public void setSaloonImageList(Map<String, String> saloonImageList) {
        this.saloonImageList = saloonImageList;
    }

    public int getOpeningTimeMinute() {
        return openingTimeMinute;
    }

    public void setOpeningTimeMinute(int openingTimeMinute) {
        this.openingTimeMinute = openingTimeMinute;
    }

    public int getOpeningTimeHour() {
        return openingTimeHour;
    }

    public void setOpeningTimeHour(int openingTimeHour) {
        this.openingTimeHour = openingTimeHour;
    }

    public int getClosingTimeHour() {
        return closingTimeHour;
    }

    public void setClosingTimeHour(int closingTimeHour) {
        this.closingTimeHour = closingTimeHour;
    }

    public int getClosingTimeMinute() {
        return closingTimeMinute;
    }

    public void setClosingTimeMinute(int closingTimeMinute) {
        this.closingTimeMinute = closingTimeMinute;
    }

    public String getSaloonEmailID() {
        return saloonEmailID;
    }

    public void setSaloonEmailID(String saloonEmailID) {
        this.saloonEmailID = saloonEmailID;
    }

    public String getSaloonCity() {
        return saloonCity;
    }

    public void setSaloonCity(String saloonCity) {
        this.saloonCity = saloonCity;
    }

    public int getSaloonCityIndex() {
        return saloonCityIndex;
    }

    public void setSaloonCityIndex(int saloonCityIndex) {
        this.saloonCityIndex = saloonCityIndex;
    }


    public int getSaloonRatingSum() {
        return saloonRatingSum;
    }

    public void setSaloonRatingSum(int saloonRatingSum) {
        this.saloonRatingSum = saloonRatingSum;
    }

    public int getSaloonTotalRating() {
        return saloonTotalRating;
    }

    public void setSaloonTotalRating(int saloonTotalRating) {
        this.saloonTotalRating = saloonTotalRating;
    }



    public String resolveSaloonRating() {
        if (saloonTotalRating > 0) {
            return String.valueOf(saloonRatingSum / (saloonTotalRating / 5));
        } else {
            return "0";
        }
    }

    public String resolveSaloonOpeningTime() {
        String string = "";

        if (openingTimeHour < 10) {
            string = string.concat("0" + String.valueOf(openingTimeHour) + ":");
        } else if (openingTimeHour > 12) {
            //  string = string.concat(String.valueOf(openingTimeHour) + ":");
            string = string.concat((openingTimeHour - 12) + ":");
        } else {
            string = string.concat(String.valueOf(openingTimeHour) + ":");

        }

        if (openingTimeMinute < 10) {
            string = string.concat("0" + String.valueOf(openingTimeMinute));
        } else {
            string = string.concat(String.valueOf(openingTimeMinute));
        }


        if (openingTimeHour > 11) {
            string = string.concat("PM");
        } else {
            string = string.concat("AM");
        }


        return string;

    }

    public String resolveSaloonClosingTime() {
        String string = "";
        int closingtimetemp;
        if (closingTimeHour > 12) {
            closingtimetemp = closingTimeHour - 12;
        } else {
            closingtimetemp = closingTimeHour;
        }

        if (closingtimetemp < 10) {
            string = string.concat("0" + String.valueOf(closingtimetemp) + ":");
        } else if (closingtimetemp > 12) {
            string = string.concat((closingTimeHour - 12) + ":");
        } else {
            string = string.concat(String.valueOf(closingTimeHour) + ":");

        }

        if (closingTimeMinute < 10) {
            string = string.concat("0" + String.valueOf(closingTimeMinute));
        } else {
            string = string.concat(String.valueOf(closingTimeMinute));
        }

        if (this.closingTimeHour > 11) {
            string = string.concat("PM");
        } else {
            string = string.concat("AM");

        }

        return string;

    }

}
