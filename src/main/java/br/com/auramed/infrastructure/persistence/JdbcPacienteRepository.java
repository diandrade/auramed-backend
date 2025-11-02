package br.com.auramed.infrastructure.persistence;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.Paciente;
import br.com.auramed.domain.repository.PacienteRepository;
import br.com.auramed.infrastructure.exception.InfrastructureException;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcPacienteRepository implements PacienteRepository {

    private final DatabaseConnection databaseConnection;

    @Inject
    public JdbcPacienteRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    private Paciente mapResultSetToPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente(
                rs.getInt("T_ARMD_PESSOA_id_pessoa"),
                rs.getInt("T_ARMD_MEDICO_id_pessoa"),
                rs.getString("nr_cartao_sus")
        );
        paciente.setDataCadastro(rs.getTimestamp("dt_cadastro").toLocalDateTime());
        paciente.setAtivo(rs.getString("in_ativo"));
        return paciente;
    }

    @Override
    public Paciente buscarPorId(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT * FROM T_ARMD_PACIENTE WHERE T_ARMD_PESSOA_id_pessoa = ? AND in_ativo = 'S'";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPaciente(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("Paciente não encontrado com ID: " + idPessoa);
                }
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar paciente por ID: " + e.getMessage());
        }
    }

    @Override
    public Paciente buscarPorCartaoSUS(String nrCartaoSUS) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT * FROM T_ARMD_PACIENTE WHERE nr_cartao_sus = ? AND in_ativo = 'S'";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nrCartaoSUS);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPaciente(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("Paciente não encontrado com Cartão SUS: " + nrCartaoSUS);
                }
            }
        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar paciente por Cartão SUS: " + e.getMessage());
        }
    }

    @Override
    public List<Paciente> buscarPorMedicoResponsavel(Integer idMedicoResponsavel) {
        String sql = "SELECT * FROM T_ARMD_PACIENTE WHERE T_ARMD_MEDICO_id_pessoa = ? AND in_ativo = 'S'";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMedicoResponsavel);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pacientes.add(mapResultSetToPaciente(rs));
                }
            }

            return pacientes;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar pacientes por médico responsável: " + e.getMessage());
        }
    }

    @Override
    public List<Paciente> buscarTodos() {
        String sql = "SELECT * FROM T_ARMD_PACIENTE WHERE in_ativo = 'S'";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapResultSetToPaciente(rs));
            }

            return pacientes;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar todos pacientes: " + e.getMessage());
        }
    }

    @Override
    public List<Paciente> buscarAtivos() {
        return buscarTodos();
    }

    @Override
    public Paciente salvar(Paciente paciente) {
        String sql = "INSERT INTO T_ARMD_PACIENTE (T_ARMD_PESSOA_id_pessoa, T_ARMD_MEDICO_id_pessoa, nr_cartao_sus, dt_cadastro, in_ativo) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, paciente.getIdPessoa());
            stmt.setInt(2, paciente.getIdMedicoResponsavel());
            stmt.setString(3, paciente.getNrCartaoSUS());
            stmt.setString(4, paciente.getAtivo());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfrastructureException("Nenhuma linha afetada ao salvar paciente.");
            }

            return paciente;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao salvar paciente: " + e.getMessage());
        }
    }

    @Override
    public Paciente editar(Paciente paciente) throws EntidadeNaoLocalizadaException {
        String sql = "UPDATE T_ARMD_PACIENTE SET T_ARMD_MEDICO_id_pessoa = ?, nr_cartao_sus = ?, in_ativo = ? " +
                "WHERE T_ARMD_PESSOA_id_pessoa = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, paciente.getIdMedicoResponsavel());
            stmt.setString(2, paciente.getNrCartaoSUS());
            stmt.setString(3, paciente.getAtivo());
            stmt.setInt(4, paciente.getIdPessoa());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("Paciente não encontrado com ID: " + paciente.getIdPessoa());
            }

            return paciente;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao editar paciente: " + e.getMessage());
        }
    }

    @Override
    public void remover(Integer idPessoa) throws EntidadeNaoLocalizadaException {
        String sql = "UPDATE T_ARMD_PACIENTE SET in_ativo = 'N' WHERE T_ARMD_PESSOA_id_pessoa = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("Paciente não encontrado com ID: " + idPessoa);
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao remover paciente: " + e.getMessage());
        }
    }

    @Override
    public boolean existeCartaoSUS(String nrCartaoSUS) {
        String sql = "SELECT 1 FROM T_ARMD_PACIENTE WHERE nr_cartao_sus = ? AND in_ativo = 'S'";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nrCartaoSUS);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao verificar existência do Cartão SUS: " + e.getMessage());
        }
    }
}