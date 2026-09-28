package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

import model.Entrega;

public class EntregaDAO {
    public void guardar(Entrega entrega) throws SQLException {
        String sql = "INSERT INTO entrega (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, entrega.getPedido().getId());
            sentencia.setInt(2, entrega.getRepartidor().getId());
            sentencia.setDate(3, Date.valueOf(entrega.getFecha()));
            sentencia.setTime(4, Time.valueOf(entrega.getHora()));
            sentencia.executeUpdate();
        }
    }
}
