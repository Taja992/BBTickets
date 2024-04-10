package GUI.view;

import Exceptions.BBExceptions;
import GUI.controller.CreateTicketController;
import GUI.model.TicketModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;


public class CreateTypeController {
    @FXML
    private TextField typeTxt;

    private CreateTicketController controller;
    private TicketModel model = new TicketModel();

    public void setController(CreateTicketController controller){
        this.controller = controller;
    }

    public void addTicket(ActionEvent actionEvent) throws BBExceptions {
        if(!typeTxt.getText().isEmpty()){
            model.addType(typeTxt.getText());
            closeWindow();
            controller.refreshTypes();
        }

    }

    private void closeWindow(){
        Stage stage = (Stage) typeTxt.getScene().getWindow();
        stage.close();
    }

}
