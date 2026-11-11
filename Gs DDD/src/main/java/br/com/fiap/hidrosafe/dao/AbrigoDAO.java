package br.com.fiap.hidrosafe.dao;

import br.com.fiap.hidrosafe.conexoes.ConnectionFactory;
import br.com.fiap.hidrosafe.entities.Abrigo;
import br.com.fiap.hidrosafe.excecoes.ErroBancoException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AbrigoDAO {

    private static final String COLUNAS =
            "id_abrigo, id_regiao, nm_abrigo, ds_endereco, nr_capacidade, nr_ocupacao, nr_kits_estoque, ds_status";

    public Abrigo inserir(Abrigo abrigo) {
        String sql = "INSERT INTO TB_ABRIGO (id_regiao, nm_abrigo, ds_endereco, nr_capacidade, nr_ocupacao, "
                + "nr_kits_estoque, ds_status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_ABRIGO"})) {

            ps.setLong(1, abrigo.getIdRegiao());
            ps.setString(2, abrigo.getNmAbrigo());
            ps.setString(3, abrigo.getDsEndereco());
            ps.setInt(4, abrigo.getNrCapacidade());
            ps.setInt(5, abrigo.getNrOcupacao());
            ps.setInt(6, abrigo.getNrKitsEstoque());
            ps.setString(7, abrigo.getDsStatus());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    abrigo.setIdAbrigo(rs.getLong(1));
                }
            }
            return abrigo;

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public List<Abrigo> listar() {
        String sql = "SELECT " + COLUNAS + " FROM TB_ABRIGO ORDER BY nm_abrigo";
        List<Abrigo> lista = new ArrayList<>();

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

    public Abrigo buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM TB_ABRIGO WHERE id_abrigo = ?";

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

    public int atualizar(Abrigo abrigo) {
        String sql = "UPDATE TB_ABRIGO SET id_regiao = ?, nm_abrigo = ?, ds_endereco = ?, nr_capacidade = ?, "
                + "nr_ocupacao = ?, nr_kits_estoque = ?, ds_status = ? WHERE id_abrigo = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, abrigo.getIdRegiao());
            ps.setString(2, abrigo.getNmAbrigo());
            ps.setString(3, abrigo.getDsEndereco());
            ps.setInt(4, abrigo.getNrCapacidade());
            ps.setInt(5, abrigo.getNrOcupacao());
            ps.setInt(6, abrigo.getNrKitsEstoque());
            ps.setString(7, abrigo.getDsStatus());
            ps.setLong(8, abrigo.getIdAbrigo());
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public int deletar(Long id) {
        String sql = "DELETE FROM TB_ABRIGO WHERE id_abrigo = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    private Abrigo montar(ResultSet rs) throws SQLException {
        Abrigo abrigo = new Abrigo();
        abrigo.setIdAbrigo(rs.getLong("id_abrigo"));
        abrigo.setIdRegiao(rs.getLong("id_regiao"));
        abrigo.setNmAbrigo(rs.getString("nm_abrigo"));
        abrigo.setDsEndereco(rs.getString("ds_endereco"));
        abrigo.setNrCapacidade(rs.getInt("nr_capacidade"));
        abrigo.setNrOcupacao(rs.getInt("nr_ocupacao"));
        abrigo.setNrKitsEstoque(rs.getInt("nr_kits_estoque"));
        abrigo.setDsStatus(rs.getString("ds_status"));
        return abrigo;
    }
}