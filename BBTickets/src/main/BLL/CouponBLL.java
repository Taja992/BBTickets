package BLL;

public class CouponBLL
{
    DAO.CouponDAO DAO = new DAO.CouponDAO();

    public void createCoupon(String couponNotes, String couponUUID)
    {
        DAO.createCoupon(couponNotes, couponUUID);
    }
}
