package br.com.auramed.infrastructure.persistence;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.Cuidador;
import br.com.auramed.domain.repository.CuidadorRepository;
import br.com.auramed.infrastructure.exception.InfrastructureException;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCuidadorRepository implements CuidadorRepository {

    private final DatabaseConnection databaseConnection;

    @Inject
    public JdbcCuidadorRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    private Cuidador mapResultSetToCuidador(ResultSet rs) throws SQLException {
        Cuidador cuidador = new Cuidador(
                rs.getInt("T_ARMD_PESSOA_id_pessoa"),
                rs.getString("ds_parentesco"),
                rs.getString("ds_tempo_cuidado")
        );
        cuidador.setDataCadastro(rs.getTimestamp("dt_cadastro").toLocalDateTime());
        cuidador.setAtivo(rs.getString("in_ativo"));
        return cuidador;
    }

    @Override
    public Cuidador buscarPorId(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT * FROM T_ARMD_CUIDADOR WHERE T_ARMD_PESSOA_id_pessoa = ? AND in_ativo = 'S'";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCuidador(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("Cuidador não encontrado com ID: " + idPessoa);
                }
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar cuidador por ID: " + e.getMessage());
        }
    }

    @Override
    public List<Cuidador> buscarTodos() {
        String sql = "SELECT * FROM T_ARMD_CUIDADOR WHERE in_ativo = 'S'";
        List<Cuidador> cuidadores = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cuidadores.add(mapResultSetToCuidador(rs));
            }

            return cuidadores;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar todos cuidadores: " + e.getMessage());
        }
    }

    @Override
    public List<Cuidador> buscarAtivos() {
        return buscarTodos();
    }

    @Override
    public Cuidador salvar(Cuidador cuidador) {
        String sql = "INSERT INTO T_ARMD_CUIDADOR (T_ARMD_PESSOA_id_pessoa, ds_parentesco, ds_tempo_cuidado, dt_cadastro, in_ativo) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cuidador.getIdPessoa());
            stmt.setString(2, cuidador.getParentesco());
            stmt.setString(3, cuidador.getTempoCuidado());
            stmt.setString(4, cuidador.getAtivo());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfrastructureException("Nenhuma linha afetada ao salvar cuidador.");
            }

            return cuidador;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao salvar cuidador: " + e.getMessage());
        }
    }

    @Override
    public Cuidador editar(Cuidador cuidador) throws EntidadeNaoLocalizadaException {
        String sql = "UPDATE T_ARMD_CUIDADOR SET ds_parentesco = ?, ds_tempo_cuidado = ?, in_ativo = ? " +
                "WHERE T_ARMD_PESSOA_id_pessoa = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cuidador.getParentesco());
            stmt.setString(2, cuidador.getTempoCuidado());
            stmt.setString(3, cuidador.getAtivo());
            stmt.setInt(4, cuidador.getIdPessoa());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("Cuidador não encontrado com ID: " + cuidador.getIdPessoa());
            }

            return cuidador;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao editar cuidador: " + e.getMessage());
        }
    }

    @Override
    public void remover(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        String sql = "UPDATE T_ARMD_CUIDADOR SET in_ativo = 'N' WHERE T_ARMD_PESSOA_id_pessoa = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("Cuidador não encontrado com ID: " + idPessoa);
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao remover cuidador: " + e.getMessage());
        }
    }
}