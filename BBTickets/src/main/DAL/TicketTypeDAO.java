package DAL;

import BE.TicketType;
import Exceptions.BBExceptions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketTypeDAO {
    private ConnectionManager connectionManager = new ConnectionManager();

    public List<TicketType> getAllTypes(){
        String sql = "SELECT * FROM TicketTypes";
        List<TicketType> types = new ArrayList<>();

        try(Connection con = connectionManager.getConnection()){

            PreparedStatement pstmnt = con.prepareStatement(sql);
            ResultSet rs = pstmnt.executeQuery();


            while(rs.next()){

                int id = rs.getInt("type_id");
                String name = rs.getString("type_name");

                types.add(new TicketType(id,name));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return types;
    }

    public void addType(int id, String name) throws BBExceptions {
        String sql = "INSERT INTO TicketTypes (type_id,type_name) VALUES (?,?)";

        try(Connection con = connectionManager.getConnection()){

            PreparedStatement pstmnt = con.prepareStatement(sql);

            pstmnt.setInt(1, id);
            pstmnt.setString(2, name);

            pstmnt.executeUpdate();

        } catch (SQLException e) {
            throw new BBExceptions("failed to add TicketType, sorry", e);
        }


    }

}
