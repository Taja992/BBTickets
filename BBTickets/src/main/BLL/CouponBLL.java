package BLL;

import java.util.List;

public class CouponBLL
{
    DAO.CouponDAO DAO = new DAO.CouponDAO();

    public void createCoupon(String couponNotes, String couponUUID)
    {
        DAO.createCoupon(couponNotes, couponUUID);
    }

    public List<String> getAllCouponNotes()
    {
        return DAO.getAllCouponNotes();
    }

    public void deleteCoupon(int coupon_id)
    {
        DAO.deleteCoupon(coupon_id);
    }

    public int getCouponId(String couponNote)
    {
        return DAO.getCouponId(couponNote);
    }
}
