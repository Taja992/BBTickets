package GUI.model;

import BLL.CouponBLL;

import java.util.List;

public class CouponModel {
    CouponBLL couponBLL = new CouponBLL();

    public void createCoupon(String couponNotes, String couponUUID) {
        couponBLL.createCoupon(couponNotes, couponUUID);
    }

    public List<String> getAllCouponNotes() {
        return couponBLL.getAllCouponNotes();
    }

    public void deleteCoupon(int coupon_id) {
        couponBLL.deleteCoupon(coupon_id);
    }

    public int getCouponId(String couponNote) {
        return couponBLL.getCouponId(couponNote);
    }
}
