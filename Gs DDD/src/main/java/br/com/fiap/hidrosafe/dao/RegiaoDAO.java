package br.com.fiap.hidrosafe.dao;

import br.com.fiap.hidrosafe.conexoes.ConnectionFactory;
import br.com.fiap.hidrosafe.entities.Regiao;
import br.com.fiap.hidrosafe.excecoes.ErroBancoException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RegiaoDAO {

    public Regiao inserir(Regiao regiao) {
        String sql = "INSERT INTO TB_REGIAO (nm_regiao, nm_cidade, ds_nivel_risco) VALUES (?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_REGIAO"})) {

            ps.setString(1, regiao.getNmRegiao());
            ps.setString(2, regiao.getNmCidade());
            ps.setString(3, regiao.getDsNivelRisco());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    regiao.setIdRegiao(rs.getLong(1));
                }
            }
            return regiao;

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public List<Regiao> listar() {
        String sql = "SELECT id_regiao, nm_regiao, nm_cidade, ds_nivel_risco FROM TB_REGIAO ORDER BY nm_regiao";
        List<Regiao> lista = new ArrayList<>();

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

    public Regiao buscarPorId(Long id) {
        String sql = "SELECT id_regiao, nm_regiao, nm_cidade, ds_nivel_risco FROM TB_REGIAO WHERE id_regiao = ?";

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

    public int atualizar(Regiao regiao) {
        String sql = "UPDATE TB_REGIAO SET nm_regiao = ?, nm_cidade = ?, ds_nivel_risco = ? WHERE id_regiao = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, regiao.getNmRegiao());
            ps.setString(2, regiao.getNmCidade());
            ps.setString(3, regiao.getDsNivelRisco());
            ps.setLong(4, regiao.getIdRegiao());
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public int deletar(Long id) {
        String sql = "DELETE FROM TB_REGIAO WHERE id_regiao = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    private Regiao montar(ResultSet rs) throws SQLException {
        Regiao regiao = new Regiao();
        regiao.setIdRegiao(rs.getLong("id_regiao"));
        regiao.setNmRegiao(rs.getString("nm_regiao"));
        regiao.setNmCidade(rs.getString("nm_cidade"));
        regiao.setDsNivelRisco(rs.getString("ds_nivel_risco"));
        return regiao;
    }
}
