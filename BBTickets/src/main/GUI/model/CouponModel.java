package GUI.model;

import BLL.CouponBLL;

public class CouponModel {
    CouponBLL couponBLL = new CouponBLL();

    public void createCoupon(String couponNotes, String couponUUID) {
        couponBLL.createCoupon(couponNotes, couponUUID);
    }
}
