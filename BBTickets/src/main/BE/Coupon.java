package BE;

public class Coupon {

    private int couponId;
    private String couponNotes;
    private int uuid;

    // Constructors
    public Coupon(String couponNotes, int uuid) {
        this.couponNotes = couponNotes;
        this.uuid = uuid;
    }

    // setters and getters
    public int getCouponId() {
        return couponId;
    }

    public String getCouponNotes() {
        return couponNotes;
    }

    public void setCouponNotes(String couponNotes) {
        this.couponNotes = couponNotes;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }


}
