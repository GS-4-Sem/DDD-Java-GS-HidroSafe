package br.com.fiap.hidrosafe.dao;

import br.com.fiap.hidrosafe.conexoes.ConnectionFactory;
import br.com.fiap.hidrosafe.entities.Rua;
import br.com.fiap.hidrosafe.excecoes.ErroBancoException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RuaDAO {

    public Rua inserir(Rua rua) {
        String sql = "INSERT INTO TB_RUA (id_regiao, nm_rua, nr_limite_cm) VALUES (?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_RUA"})) {

            ps.setLong(1, rua.getIdRegiao());
            ps.setString(2, rua.getNmRua());
            ps.setDouble(3, rua.getNrLimiteCm());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rua.setIdRua(rs.getLong(1));
                }
            }
            return rua;

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public List<Rua> listar() {
        String sql = "SELECT id_rua, id_regiao, nm_rua, nr_limite_cm FROM TB_RUA ORDER BY nm_rua";
        List<Rua> lista = new ArrayList<>();

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

    public Rua buscarPorId(Long id) {
        String sql = "SELECT id_rua, id_regiao, nm_rua, nr_limite_cm FROM TB_RUA WHERE id_rua = ?";

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

    public int atualizar(Rua rua) {
        String sql = "UPDATE TB_RUA SET id_regiao = ?, nm_rua = ?, nr_limite_cm = ? WHERE id_rua = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, rua.getIdRegiao());
            ps.setString(2, rua.getNmRua());
            ps.setDouble(3, rua.getNrLimiteCm());
            ps.setLong(4, rua.getIdRua());
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public int deletar(Long id) {
        String sql = "DELETE FROM TB_RUA WHERE id_rua = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    private Rua montar(ResultSet rs) throws SQLException {
        Rua rua = new Rua();
        rua.setIdRua(rs.getLong("id_rua"));
        rua.setIdRegiao(rs.getLong("id_regiao"));
        rua.setNmRua(rs.getString("nm_rua"));
        rua.setNrLimiteCm(rs.getDouble("nr_limite_cm"));
        return rua;
    }
}