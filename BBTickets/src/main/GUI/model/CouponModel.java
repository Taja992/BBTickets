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
}
