package DAO;

import DAL.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> getAllCouponNotes() {
        List<String> couponNotes = new ArrayList<>();
        String sql = "SELECT coupon_notes FROM Coupon";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String note = rs.getString("coupon_notes");
                couponNotes.add(note);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return couponNotes;
    }
}