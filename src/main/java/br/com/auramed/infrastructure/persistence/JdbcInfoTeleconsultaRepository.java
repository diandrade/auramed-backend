package br.com.auramed.infrastructure.persistence;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.InfoTeleconsulta;
import br.com.auramed.domain.repository.InfoTeleconsultaRepository;
import br.com.auramed.infrastructure.exception.InfrastructureException;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcInfoTeleconsultaRepository implements InfoTeleconsultaRepository {

    private final DatabaseConnection databaseConnection;

    @Inject
    public JdbcInfoTeleconsultaRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    private InfoTeleconsulta mapResultSetToInfoTeleconsulta(ResultSet rs) throws SQLException {
        InfoTeleconsulta infoTeleconsulta = new InfoTeleconsulta();
        infoTeleconsulta.setIdInfoTeleconsulta(rs.getInt("id_info_teleconsulta"));
        infoTeleconsulta.setIdPaciente(rs.getInt("T_ARMD_PACIENTE_id_pessoa"));
        infoTeleconsulta.setCdHabilidadeDigital(rs.getString("cd_habilidade_digital"));
        infoTeleconsulta.setCdCanalLembrete(rs.getString("cd_canal_lembrete"));
        infoTeleconsulta.setInPrecisaCuidador(rs.getString("in_precisa_cuidador"));
        infoTeleconsulta.setInJaFezTele(rs.getString("in_ja_fez_tele"));

        Timestamp dataCadastro = rs.getTimestamp("dt_cadastro");
        if (dataCadastro != null) {
            infoTeleconsulta.setDataCadastro(dataCadastro.toLocalDateTime());
        }

        Timestamp dataAtualizacao = rs.getTimestamp("dt_atualizacao");
        if (dataAtualizacao != null) {
            infoTeleconsulta.setDataAtualizacao(dataAtualizacao.toLocalDateTime());
        }

        return infoTeleconsulta;
    }

    @Override
    public InfoTeleconsulta buscarPorId(Integer idInfoTeleconsulta) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT * FROM T_ARMD_INFO_TELECONSULTA WHERE id_info_teleconsulta = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idInfoTeleconsulta);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInfoTeleconsulta(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("InfoTeleconsulta não encontrada com ID: " + idInfoTeleconsulta);
                }
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar infoTeleconsulta por ID: " + e.getMessage());
        }
    }

    @Override
    public InfoTeleconsulta buscarPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT * FROM T_ARMD_INFO_TELECONSULTA WHERE T_ARMD_PACIENTE_id_pessoa = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInfoTeleconsulta(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("InfoTeleconsulta não encontrada para paciente: " + idPaciente);
                }
            }
        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar infoTeleconsulta por paciente: " + e.getMessage());
        }
    }

    @Override
    public List<InfoTeleconsulta> buscarTodos() {
        String sql = "SELECT * FROM T_ARMD_INFO_TELECONSULTA";
        List<InfoTeleconsulta> infoTeleconsultas = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                infoTeleconsultas.add(mapResultSetToInfoTeleconsulta(rs));
            }

            return infoTeleconsultas;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar todas infoTeleconsultas: " + e.getMessage());
        }
    }

    @Override
    public InfoTeleconsulta salvar(InfoTeleconsulta infoTeleconsulta) {
        String sql = "INSERT INTO T_ARMD_INFO_TELECONSULTA (T_ARMD_PACIENTE_id_pessoa, cd_habilidade_digital, cd_canal_lembrete, in_precisa_cuidador, in_ja_fez_tele, dt_cadastro, dt_atualizacao) " +
                "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_info_teleconsulta"})) {

            stmt.setInt(1, infoTeleconsulta.getIdPaciente());
            stmt.setString(2, infoTeleconsulta.getCdHabilidadeDigital());
            stmt.setString(3, infoTeleconsulta.getCdCanalLembrete());
            stmt.setString(4, infoTeleconsulta.getInPrecisaCuidador());
            stmt.setString(5, infoTeleconsulta.getInJaFezTele());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfrastructureException("Nenhuma linha afetada ao salvar infoTeleconsulta.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    infoTeleconsulta.setIdInfoTeleconsulta(generatedKeys.getInt(1));
                }
            }
            return infoTeleconsulta;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao salvar infoTeleconsulta: " + e.getMessage());
        }
    }

    @Override
    public InfoTeleconsulta editar(InfoTeleconsulta infoTeleconsulta) throws EntidadeNaoLocalizadaException {
        String sql = "UPDATE T_ARMD_INFO_TELECONSULTA SET cd_habilidade_digital = ?, cd_canal_lembrete = ?, in_precisa_cuidador = ?, in_ja_fez_tele = ?, dt_atualizacao = CURRENT_TIMESTAMP " +
                "WHERE id_info_teleconsulta = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, infoTeleconsulta.getCdHabilidadeDigital());
            stmt.setString(2, infoTeleconsulta.getCdCanalLembrete());
            stmt.setString(3, infoTeleconsulta.getInPrecisaCuidador());
            stmt.setString(4, infoTeleconsulta.getInJaFezTele());
            stmt.setInt(5, infoTeleconsulta.getIdInfoTeleconsulta());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("InfoTeleconsulta não encontrada com ID: " + infoTeleconsulta.getIdInfoTeleconsulta());
            }

            return infoTeleconsulta;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao editar infoTeleconsulta: " + e.getMessage());
        }
    }

    @Override
    public void remover(Integer idInfoTeleconsulta) throws EntidadeNaoLocalizadaException {
        String sql = "DELETE FROM T_ARMD_INFO_TELECONSULTA WHERE id_info_teleconsulta = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idInfoTeleconsulta);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("InfoTeleconsulta não encontrada com ID: " + idInfoTeleconsulta);
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao remover infoTeleconsulta: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarHabilidadesDigitais() {
        String sql = "SELECT cd_habilidade_digital, COUNT(*) as quantidade FROM T_ARMD_INFO_TELECONSULTA WHERE cd_habilidade_digital IS NOT NULL GROUP BY cd_habilidade_digital ORDER BY quantidade DESC";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String habilidade = rs.getString("cd_habilidade_digital");
                Long quantidade = rs.getLong("quantidade");
                Object[] resultado = new Object[]{habilidade, quantidade};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar habilidades digitais: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarCanaisLembrete() {
        String sql = "SELECT cd_canal_lembrete, COUNT(*) as quantidade FROM T_ARMD_INFO_TELECONSULTA WHERE cd_canal_lembrete IS NOT NULL GROUP BY cd_canal_lembrete ORDER BY quantidade DESC";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String canal = rs.getString("cd_canal_lembrete");
                Long quantidade = rs.getLong("quantidade");
                Object[] resultado = new Object[]{canal, quantidade};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar canais de lembrete: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarPreferenciasCuidador() {
        String sql = "SELECT in_precisa_cuidador, COUNT(*) as quantidade FROM T_ARMD_INFO_TELECONSULTA WHERE in_precisa_cuidador IS NOT NULL GROUP BY in_precisa_cuidador";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String precisaCuidador = rs.getString("in_precisa_cuidador");
                Long quantidade = rs.getLong("quantidade");
                Object[] resultado = new Object[]{precisaCuidador, quantidade};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar preferências de cuidador: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarExperienciaTeleconsulta() {
        String sql = "SELECT in_ja_fez_tele, COUNT(*) as quantidade FROM T_ARMD_INFO_TELECONSULTA WHERE in_ja_fez_tele IS NOT NULL GROUP BY in_ja_fez_tele";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String jaFezTele = rs.getString("in_ja_fez_tele");
                Long quantidade = rs.getLong("quantidade");
                Object[] resultado = new Object[]{jaFezTele, quantidade};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar experiência com teleconsulta: " + e.getMessage());
        }
    }

    @Override
    public Long getTotalRegistros() {
        String sql = "SELECT COUNT(*) FROM T_ARMD_INFO_TELECONSULTA";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao contar total de registros: " + e.getMessage());
        }
    }
}