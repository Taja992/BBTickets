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
}
