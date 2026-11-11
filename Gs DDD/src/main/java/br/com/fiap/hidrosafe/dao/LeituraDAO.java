package br.com.fiap.hidrosafe.dao;

import br.com.fiap.hidrosafe.conexoes.ConnectionFactory;
import br.com.fiap.hidrosafe.entities.Alerta;
import br.com.fiap.hidrosafe.entities.Leitura;
import br.com.fiap.hidrosafe.excecoes.ErroBancoException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LeituraDAO {

    private static final String COLUNAS =
            "id_leitura, id_rua, id_sensor, ds_origem, nr_nivel_cm, dt_leitura, nm_reportante";

    private final AlertaDAO alertaDAO;

    public LeituraDAO(AlertaDAO alertaDAO) {
        this.alertaDAO = alertaDAO;
    }

    public Leitura inserir(Leitura leitura) {
        return inserirComAlerta(leitura, null);
    }

    // grava a leitura e, se vier um alerta, grava os dois na MESMA transação:
    // se qualquer um falhar, nada fica gravado (rollback)
    public Leitura inserirComAlerta(Leitura leitura, Alerta alerta) {
        try (Connection con = ConnectionFactory.getConnection()) {
            try {
                con.setAutoCommit(false);

                inserir(con, leitura);
                if (alerta != null) {
                    alertaDAO.inserir(con, alerta);
                }
                Leitura gravada = buscarPorId(con, leitura.getIdLeitura());

                con.commit();
                return gravada;

            } catch (SQLException | RuntimeException e) {
                desfazer(con, e);
                throw e;
            }

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    // não abre nem fecha a conexão, e não faz commit; dt_leitura é gerada pelo banco
    public Leitura inserir(Connection con, Leitura leitura) throws SQLException {
        String sql = "INSERT INTO TB_LEITURA (id_rua, id_sensor, ds_origem, nr_nivel_cm, nm_reportante) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID_LEITURA"})) {
            ps.setLong(1, leitura.getIdRua());
            if (leitura.getIdSensor() == null) {
                ps.setNull(2, Types.NUMERIC);
            } else {
                ps.setLong(2, leitura.getIdSensor());
            }
            ps.setString(3, leitura.getDsOrigem());
            ps.setDouble(4, leitura.getNrNivelCm());
            ps.setString(5, leitura.getNmReportante());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    leitura.setIdLeitura(rs.getLong(1));
                }
            }
            return leitura;
        }
    }

    public List<Leitura> listar() {
        String sql = "SELECT " + COLUNAS + " FROM TB_LEITURA ORDER BY dt_leitura DESC, id_leitura DESC";
        List<Leitura> lista = new ArrayList<>();

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

    public Leitura buscarPorId(Long id) {
        try (Connection con = ConnectionFactory.getConnection()) {
            return buscarPorId(con, id);

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    private Leitura buscarPorId(Connection con, Long id) throws SQLException {
        String sql = "SELECT " + COLUNAS + " FROM TB_LEITURA WHERE id_leitura = ?";

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

    public int atualizar(Leitura leitura) {
        String sql = "UPDATE TB_LEITURA SET id_rua = ?, id_sensor = ?, ds_origem = ?, nr_nivel_cm = ?, "
                + "nm_reportante = ? WHERE id_leitura = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, leitura.getIdRua());
            if (leitura.getIdSensor() == null) {
                ps.setNull(2, Types.NUMERIC);
            } else {
                ps.setLong(2, leitura.getIdSensor());
            }
            ps.setString(3, leitura.getDsOrigem());
            ps.setDouble(4, leitura.getNrNivelCm());
            ps.setString(5, leitura.getNmReportante());
            ps.setLong(6, leitura.getIdLeitura());
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    public int deletar(Long id) {
        String sql = "DELETE FROM TB_LEITURA WHERE id_leitura = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new ErroBancoException(e.getMessage(), e.getErrorCode());
        }
    }

    private void desfazer(Connection con, Exception causa) {
        try {
            con.rollback();
        } catch (SQLException erroRollback) {
            causa.addSuppressed(erroRollback);
        }
    }

    private Leitura montar(ResultSet rs) throws SQLException {
        Leitura leitura = new Leitura();
        leitura.setIdLeitura(rs.getLong("id_leitura"));
        leitura.setIdRua(rs.getLong("id_rua"));

        long idSensor = rs.getLong("id_sensor");
        if (!rs.wasNull()) {
            leitura.setIdSensor(idSensor);
        }

        leitura.setDsOrigem(rs.getString("ds_origem"));
        leitura.setNrNivelCm(rs.getDouble("nr_nivel_cm"));

        Timestamp data = rs.getTimestamp("dt_leitura");
        if (data != null) {
            leitura.setDtLeitura(data.toLocalDateTime());
        }

        leitura.setNmReportante(rs.getString("nm_reportante"));
        return leitura;
    }
}