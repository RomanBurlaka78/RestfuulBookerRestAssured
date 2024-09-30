package api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetBookingDates {
    public  Integer bookingid;
    public String additionalneeds;
    public  String firstname;
    public String lastname;
    public Integer totalprice;

    public Boolean depositpaid;

    public String checkin;

    public String checkout;

    public GetBookingDates() {

    }

    public String getAdditionalneeds() {
        return additionalneeds;
    }

    public Integer getBookingid() {
        return bookingid;
    }

    public GetBookingDates(String firstname, String lastname, Integer totalprice,
                           Boolean depositpaid, String additionalneeds, String checkin, String checkout) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.additionalneeds = additionalneeds;
        this.checkin = checkin;


    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Integer getTotalprice() {
        return totalprice;
    }

    public Boolean getDepositpaid() {
        return depositpaid;
    }

    public String getCheckin() {
        return checkin;
    }

    public String getCheckout() {
        return checkout;
    }
}
