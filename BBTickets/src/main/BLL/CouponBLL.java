package BLL;

import BE.Coupon;

import java.util.List;

public class CouponBLL
{
    DAL.CouponDAO DAL = new DAL.CouponDAO();

    public void createCoupon(String couponNotes, String couponUUID)
    {
        DAL.createCoupon(couponNotes, couponUUID);
    }

    public List<String> getAllCouponNotes()
    {
        return DAL.getAllCouponNotes();
    }

    public void deleteCoupon(int coupon_id)
    {
        DAL.deleteCoupon(coupon_id);
    }

    public int getCouponId(String couponNote)
    {
        return DAL.getCouponId(couponNote);
    }

    public Coupon getCouponByNote(String couponNote) {
        return DAL.getCouponByNote(couponNote);
    }
}
