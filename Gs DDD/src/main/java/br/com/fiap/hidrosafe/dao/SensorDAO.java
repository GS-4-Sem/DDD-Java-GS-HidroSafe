package br.com.fiap.hidrosafe.dao;

import br.com.fiap.hidrosafe.conexoes.ConnectionFactory;
import br.com.fiap.hidrosafe.entities.Sensor;
import br.com.fiap.hidrosafe.excecoes.ErroBancoException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SensorDAO {

    public Sensor inserir(Sensor sensor) {
        String sql = "INSERT INTO TB_SENSOR (id_rua, nm_sensor, ds_status) VALUES (?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_SENSOR"})) {

            ps.setLong(1, sensor.getIdRua());
            ps.setString(2, sensor.getNmSensor());
            ps.setString(3, sensor.getDsStatus());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    sensor.setIdSensor(rs.getLong(1));
                }
            }
            return sensor;

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public List<Sensor> listar() {
        String sql = "SELECT id_sensor, id_rua, nm_sensor, ds_status FROM TB_SENSOR ORDER BY nm_sensor";
        List<Sensor> lista = new ArrayList<>();

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }
            return lista;

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public Sensor buscarPorId(Long id) {
        String sql = "SELECT id_sensor, id_rua, nm_sensor, ds_status FROM TB_SENSOR WHERE id_sensor = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public int atualizar(Sensor sensor) {
        String sql = "UPDATE TB_SENSOR SET id_rua = ?, nm_sensor = ?, ds_status = ? WHERE id_sensor = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, sensor.getIdRua());
            ps.setString(2, sensor.getNmSensor());
            ps.setString(3, sensor.getDsStatus());
            ps.setLong(4, sensor.getIdSensor());
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public int deletar(Long id) {
        String sql = "DELETE FROM TB_SENSOR WHERE id_sensor = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    private Sensor montar(ResultSet rs) throws SQLException {
        Sensor sensor = new Sensor();
        sensor.setIdSensor(rs.getLong("id_sensor"));
        sensor.setIdRua(rs.getLong("id_rua"));
        sensor.setNmSensor(rs.getString("nm_sensor"));
        sensor.setDsStatus(rs.getString("ds_status"));
        return sensor;
    }
}