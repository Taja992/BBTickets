package DAO;

import DAL.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CouponDAO {

    private ConnectionManager connectionManager;

    public CouponDAO() {
        connectionManager = new ConnectionManager();
    }

    public void createCoupon(String couponNotes, String couponUUID) {
        String sql = "INSERT INTO Coupon (coupon_notes, coupon_uuid) VALUES (?, ?)";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, couponNotes);
            pstmt.setString(2, couponUUID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}