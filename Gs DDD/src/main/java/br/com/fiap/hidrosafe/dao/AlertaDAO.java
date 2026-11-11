package br.com.fiap.hidrosafe.dao;

import br.com.fiap.hidrosafe.conexoes.ConnectionFactory;
import br.com.fiap.hidrosafe.entities.Alerta;
import br.com.fiap.hidrosafe.excecoes.ErroBancoException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AlertaDAO {

    private static final String COLUNAS = "id_alerta, id_rua, ds_gravidade, ds_mensagem, ds_status, dt_emissao";

    public Alerta inserir(Alerta alerta) {
        try (Connection con = ConnectionFactory.getConnection()) {
            inserir(con, alerta);
            return buscarPorId(con, alerta.getIdAlerta());

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    // usado na transação da leitura: não abre nem fecha a conexão, e não faz commit
    public Alerta inserir(Connection con, Alerta alerta) throws SQLException {
        String sql = "INSERT INTO TB_ALERTA (id_rua, ds_gravidade, ds_mensagem, ds_status) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_ALERTA"})) {
            ps.setLong(1, alerta.getIdRua());
            ps.setString(2, alerta.getDsGravidade());
            ps.setString(3, alerta.getDsMensagem());
            ps.setString(4, alerta.getDsStatus());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    alerta.setIdAlerta(rs.getLong(1));
                }
            }
            return alerta;
        }
    }

    public List<Alerta> listar() {
        String sql = "SELECT " + COLUNAS + " FROM TB_ALERTA ORDER BY dt_emissao DESC, id_alerta DESC";
        List<Alerta> lista = new ArrayList<>();

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

    public Alerta buscarPorId(Long id) {
        try (Connection con = ConnectionFactory.getConnection()) {
            return buscarPorId(con, id);

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    private Alerta buscarPorId(Connection con, Long id) throws SQLException {
        String sql = "SELECT " + COLUNAS + " FROM TB_ALERTA WHERE id_alerta = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
                return null;
            }
        }
    }

    public int atualizar(Alerta alerta) {
        String sql = "UPDATE TB_ALERTA SET id_rua = ?, ds_gravidade = ?, ds_mensagem = ?, ds_status = ? WHERE id_alerta = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, alerta.getIdRua());
            ps.setString(2, alerta.getDsGravidade());
            ps.setString(3, alerta.getDsMensagem());
            ps.setString(4, alerta.getDsStatus());
            ps.setLong(5, alerta.getIdAlerta());
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public int deletar(Long id) {
        String sql = "DELETE FROM TB_ALERTA WHERE id_alerta = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    private Alerta montar(ResultSet rs) throws SQLException {
        Alerta alerta = new Alerta();
        alerta.setIdAlerta(rs.getLong("id_alerta"));
        alerta.setIdRua(rs.getLong("id_rua"));
        alerta.setDsGravidade(rs.getString("ds_gravidade"));
        alerta.setDsMensagem(rs.getString("ds_mensagem"));
        alerta.setDsStatus(rs.getString("ds_status"));
        Timestamp emissao = rs.getTimestamp("dt_emissao");
        if (emissao != null) {
            alerta.setDtEmissao(emissao.toLocalDateTime());
        }
        return alerta;
    }
}