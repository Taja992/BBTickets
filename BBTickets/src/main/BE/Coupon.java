package BE;

public class Coupon {

    private int couponId;
    private String couponNotes;
    private String uuid;

    // Constructors
    public Coupon(String couponNotes, String uuid) {
        this.couponNotes = couponNotes;
        this.uuid = uuid;
    }

    public Coupon(int id, String couponNote, String uuid) {
        this.couponId = id;
        this.couponNotes = couponNote;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


}
