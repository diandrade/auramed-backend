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
            throw new InfrastructureException("Erro ao buscar info teleconsulta por ID: " + e.getMessage());
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
                    throw new EntidadeNaoLocalizadaException("Info teleconsulta não encontrada para paciente: " + idPaciente);
                }
            }
        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar info teleconsulta por paciente: " + e.getMessage());
        }
    }

    @Override
    public List<InfoTeleconsulta> buscarTodos() {
        String sql = "SELECT * FROM T_ARMD_INFO_TELECONSULTA";
        List<InfoTeleconsulta> infosTeleconsulta = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                infosTeleconsulta.add(mapResultSetToInfoTeleconsulta(rs));
            }

            return infosTeleconsulta;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar todas infos teleconsulta: " + e.getMessage());
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
                throw new InfrastructureException("Nenhuma linha afetada ao salvar info teleconsulta.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    infoTeleconsulta.setIdInfoTeleconsulta(generatedKeys.getInt(1));
                }
            }
            return infoTeleconsulta;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao salvar info teleconsulta: " + e.getMessage());
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
                throw new EntidadeNaoLocalizadaException("Info teleconsulta não encontrada com ID: " + infoTeleconsulta.getIdInfoTeleconsulta());
            }

            return infoTeleconsulta;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao editar info teleconsulta: " + e.getMessage());
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
                throw new EntidadeNaoLocalizadaException("Info teleconsulta não encontrada com ID: " + idInfoTeleconsulta);
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao remover info teleconsulta: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarHabilidadesDigitais() {
        String sql = "SELECT cd_habilidade_digital as skill, COUNT(*) as count FROM T_ARMD_INFO_TELECONSULTA GROUP BY cd_habilidade_digital";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String skill = rs.getString("skill");
                Long count = rs.getLong("count");
                Object[] resultado = new Object[]{skill, count};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar habilidades digitais: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarCanaisLembrete() {
        String sql = "SELECT cd_canal_lembrete as canal, COUNT(*) as quantidade FROM T_ARMD_INFO_TELECONSULTA GROUP BY cd_canal_lembrete";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String canal = rs.getString("canal");
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
    public List<Object[]> buscarEstatisticasTeleconsulta() {
        String sql = "SELECT " +
                "COUNT(*) as total, " +
                "SUM(CASE WHEN in_ja_fez_tele = 'S' THEN 1 ELSE 0 END) as ja_fez_tele, " +
                "SUM(CASE WHEN in_precisa_cuidador = 'S' THEN 1 ELSE 0 END) as precisa_cuidador " +
                "FROM T_ARMD_INFO_TELECONSULTA";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Object[] resultado = new Object[]{
                        rs.getLong("total"),
                        rs.getLong("ja_fez_tele"),
                        rs.getLong("precisa_cuidador")
                };
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar estatísticas de teleconsulta: " + e.getMessage());
        }
    }
}