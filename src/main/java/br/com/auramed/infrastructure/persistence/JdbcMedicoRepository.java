package br.com.auramed.infrastructure.persistence;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.Medico;
import br.com.auramed.domain.model.Pessoa;
import br.com.auramed.domain.repository.MedicoRepository;
import br.com.auramed.infrastructure.exception.InfrastructureException;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcMedicoRepository implements MedicoRepository {

    private final DatabaseConnection databaseConnection;

    @Inject
    public JdbcMedicoRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    private Medico mapResultSetToMedico(ResultSet rs) throws SQLException {
        Pessoa pessoa = new Pessoa(
                rs.getString("NM_PESSOA"),
                rs.getString("NR_TELEFONE"),
                rs.getString("TP_PESSOA")
        );

        pessoa.setId(rs.getInt("ID_PESSOA"));
        pessoa.setEmail(rs.getString("NM_EMAIL"));
        pessoa.setCpf(rs.getString("NR_CPF"));
        pessoa.setDataNascimento(rs.getDate("DT_NASCIMENTO") != null ?
                rs.getDate("DT_NASCIMENTO").toLocalDate() : null);
        pessoa.setGenero(rs.getString("ST_GENERO"));
        Timestamp timestampCadastro = rs.getTimestamp("DT_CADASTRO");
        if (timestampCadastro != null) {
            pessoa.setDataCadastro(timestampCadastro.toLocalDateTime());
        }

        pessoa.setAtivo(rs.getString("IN_ATIVO"));

        Medico medico = new Medico(pessoa, rs.getString("NR_CRM"));
        medico.setId(rs.getInt("ID_PESSOA"));
        medico.setAceitaTeleconsulta(rs.getString("IN_ACEITA_TELECONSULTA"));

        return medico;
    }

    @Override
    public Medico buscarPorId(Integer id) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT m.T_ARMD_PESSOA_ID_PESSOA as ID_PESSOA, m.NR_CRM, m.IN_ACEITA_TELECONSULTA, " +
                "p.NM_PESSOA, p.NM_EMAIL, p.NR_CPF, p.DT_NASCIMENTO, p.ST_GENERO, p.NR_TELEFONE, " +
                "p.TP_PESSOA, p.DT_CADASTRO, p.IN_ATIVO " +
                "FROM T_ARMD_MEDICO m " +
                "INNER JOIN T_ARMD_PESSOA p ON m.T_ARMD_PESSOA_ID_PESSOA = p.ID_PESSOA " +
                "WHERE m.T_ARMD_PESSOA_ID_PESSOA = ? AND p.IN_ATIVO = 'S'";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedico(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("Médico não encontrado com ID: " + id);
                }
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar médico por ID: " + e.getMessage());
        }
    }

    @Override
    public List<Medico> buscarTodos() {
        String sql = "SELECT m.T_ARMD_PESSOA_ID_PESSOA as ID_PESSOA, m.NR_CRM, m.IN_ACEITA_TELECONSULTA, " +
                "p.NM_PESSOA, p.NM_EMAIL, p.NR_CPF, p.DT_NASCIMENTO, p.ST_GENERO, p.NR_TELEFONE, " +
                "p.TP_PESSOA, p.DT_CADASTRO, p.IN_ATIVO " +
                "FROM T_ARMD_MEDICO m " +
                "INNER JOIN T_ARMD_PESSOA p ON m.T_ARMD_PESSOA_ID_PESSOA = p.ID_PESSOA " +
                "WHERE p.IN_ATIVO = 'S'";

        List<Medico> medicos = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                medicos.add(mapResultSetToMedico(rs));
            }

            return medicos;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar todos médicos: " + e.getMessage());
        }
    }

    @Override
    public Medico buscarPorCrm(String crm) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT m.T_ARMD_PESSOA_ID_PESSOA as ID_PESSOA, m.NR_CRM, m.IN_ACEITA_TELECONSULTA, " +
                "p.NM_PESSOA, p.NM_EMAIL, p.NR_CPF, p.DT_NASCIMENTO, p.ST_GENERO, p.NR_TELEFONE, " +
                "p.TP_PESSOA, p.DT_CADASTRO, p.IN_ATIVO " +
                "FROM T_ARMD_MEDICO m " +
                "INNER JOIN T_ARMD_PESSOA p ON m.T_ARMD_PESSOA_ID_PESSOA = p.ID_PESSOA " +
                "WHERE m.NR_CRM = ? AND p.IN_ATIVO = 'S'";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, crm);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedico(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("Médico não encontrado com CRM: " + crm);
                }
            }
        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar médico por CRM: " + e.getMessage());
        }
    }

    @Override
    public Medico buscarPorPessoaId(Integer pessoaId) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT m.T_ARMD_PESSOA_ID_PESSOA as ID_PESSOA, m.NR_CRM, m.IN_ACEITA_TELECONSULTA, " +
                "p.NM_PESSOA, p.NM_EMAIL, p.NR_CPF, p.DT_NASCIMENTO, p.ST_GENERO, p.NR_TELEFONE, " +
                "p.TP_PESSOA, p.DT_CADASTRO, p.IN_ATIVO " +
                "FROM T_ARMD_MEDICO m " +
                "INNER JOIN T_ARMD_PESSOA p ON m.T_ARMD_PESSOA_ID_PESSOA = p.ID_PESSOA " +
                "WHERE m.T_ARMD_PESSOA_ID_PESSOA = ? AND p.IN_ATIVO = 'S'";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pessoaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedico(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("Médico não encontrado para pessoa ID: " + pessoaId);
                }
            }
        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar médico por pessoa ID: " + e.getMessage());
        }
    }

    @Override
    public List<Medico> buscarPorEspecialidade(Integer idEspecialidade) {
        String sql = "SELECT DISTINCT m.T_ARMD_PESSOA_ID_PESSOA as ID_PESSOA, m.NR_CRM, m.IN_ACEITA_TELECONSULTA, " +
                "p.NM_PESSOA, p.NM_EMAIL, p.NR_CPF, p.DT_NASCIMENTO, p.ST_GENERO, p.NR_TELEFONE, " +
                "p.TP_PESSOA, p.DT_CADASTRO, p.IN_ATIVO " +
                "FROM T_ARMD_MEDICO m " +
                "INNER JOIN T_ARMD_PESSOA p ON m.T_ARMD_PESSOA_ID_PESSOA = p.ID_PESSOA " +
                "INNER JOIN T_ARMD_MEDICO_ESPECIALIDADE me ON m.T_ARMD_PESSOA_ID_PESSOA = me.T_ARMD_MEDICO_ID_PESSOA " +
                "WHERE me.T_ARMD_ESPECIALIDADE_ID_ESPECIALIDADE = ? AND p.IN_ATIVO = 'S'";
        List<Medico> medicos = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEspecialidade);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapResultSetToMedico(rs));
                }
            }

            return medicos;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar médicos por especialidade: " + e.getMessage());
        }
    }

    @Override
    public List<Medico> buscarPorAceitaTeleconsulta(String aceitaTeleconsulta) {
        String sql = "SELECT m.T_ARMD_PESSOA_ID_PESSOA as ID_PESSOA, m.NR_CRM, m.IN_ACEITA_TELECONSULTA, " +
                "p.NM_PESSOA, p.NM_EMAIL, p.NR_CPF, p.DT_NASCIMENTO, p.ST_GENERO, p.NR_TELEFONE, " +
                "p.TP_PESSOA, p.DT_CADASTRO, p.IN_ATIVO " +
                "FROM T_ARMD_MEDICO m " +
                "INNER JOIN T_ARMD_PESSOA p ON m.T_ARMD_PESSOA_ID_PESSOA = p.ID_PESSOA " +
                "WHERE m.IN_ACEITA_TELECONSULTA = ? AND p.IN_ATIVO = 'S'";
        List<Medico> medicos = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aceitaTeleconsulta);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapResultSetToMedico(rs));
                }
            }

            return medicos;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar médicos por aceita teleconsulta: " + e.getMessage());
        }
    }

    @Override
    public List<Medico> buscarAtivos() {
        return buscarTodos(); // Já filtra por ativos
    }

    @Override
    public Medico salvar(Medico medico) {
        String sql = "INSERT INTO T_ARMD_MEDICO (T_ARMD_PESSOA_ID_PESSOA, NR_CRM, IN_ACEITA_TELECONSULTA, DT_CADASTRO) " +
                "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medico.getPessoa().getId());
            stmt.setString(2, medico.getCrm());
            stmt.setString(3, medico.getAceitaTeleconsulta());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfrastructureException("Nenhuma linha afetada ao salvar médico.");
            }

            medico.setId(medico.getPessoa().getId());
            return medico;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao salvar médico: " + e.getMessage());
        }
    }

    @Override
    public Medico editar(Medico medico) throws EntidadeNaoLocalizadaException {
        if (medico.getId() == null) {
            throw new IllegalArgumentException("ID do médico não pode ser nulo para edição");
        }

        String sql = "UPDATE T_ARMD_MEDICO SET NR_CRM = ?, IN_ACEITA_TELECONSULTA = ? WHERE T_ARMD_PESSOA_ID_PESSOA = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medico.getCrm());
            stmt.setString(2, medico.getAceitaTeleconsulta());
            stmt.setInt(3, medico.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("Médico não encontrado com ID: " + medico.getId());
            }

            return medico;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao editar médico: " + e.getMessage());
        }
    }

    @Override
    public void remover(Integer id) throws EntidadeNaoLocalizadaException {
        String sql = "UPDATE T_ARMD_PESSOA SET IN_ATIVO = 'N' WHERE ID_PESSOA = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("Médico não encontrado com ID: " + id);
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao remover médico: " + e.getMessage());
        }
    }
}