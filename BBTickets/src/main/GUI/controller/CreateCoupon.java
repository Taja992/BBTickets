package GUI.controller;

import GUI.model.CouponModel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.util.UUID;

public class CreateCoupon {

    public Button createCouponBtn;
    public MFXTextField couponTxtField;
    public ListView<String> couponLv;

    private CouponModel couponModel = new CouponModel();

    public void createCoupon(ActionEvent actionEvent) {
        String couponName = couponTxtField.getText();
        String couponUUID = UUID.randomUUID().toString();

        // Save the coupon to the database
        couponModel.createCoupon(couponName, couponUUID);
    }
}
