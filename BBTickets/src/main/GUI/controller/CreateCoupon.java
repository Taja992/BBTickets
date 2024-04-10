package GUI.controller;

import GUI.model.CouponModel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.UUID;

public class CreateCoupon {

    public Button createCouponBtn;
    public MFXTextField couponTxtField;
    public ListView<String> couponLv;

    private CouponModel couponModel = new CouponModel();

    @FXML
    public void initialize() {
        List<String> couponNotes = couponModel.getAllCouponNotes();
        couponLv.getItems().addAll(couponNotes);
    }

    public void createCoupon(ActionEvent actionEvent) {
        String couponName = couponTxtField.getText();
        String couponUUID = UUID.randomUUID().toString();

        // Save the coupon to the database
        couponModel.createCoupon(couponName, couponUUID);

        // Clear the ListView
        couponLv.getItems().clear();

        // Repopulate the ListView with the updated list of coupon notes
        List<String> couponNotes = couponModel.getAllCouponNotes();
        couponLv.getItems().addAll(couponNotes);
    }
}
