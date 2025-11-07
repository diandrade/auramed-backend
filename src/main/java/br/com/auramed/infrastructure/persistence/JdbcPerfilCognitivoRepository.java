package br.com.auramed.infrastructure.persistence;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.PerfilCognitivo;
import br.com.auramed.domain.repository.PerfilCognitivoRepository;
import br.com.auramed.infrastructure.exception.InfrastructureException;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcPerfilCognitivoRepository implements PerfilCognitivoRepository {

    private final DatabaseConnection databaseConnection;

    @Inject
    public JdbcPerfilCognitivoRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    private PerfilCognitivo mapResultSetToPerfilCognitivo(ResultSet rs) throws SQLException {
        PerfilCognitivo perfilCognitivo = new PerfilCognitivo();
        perfilCognitivo.setIdPerfilCognitivo(rs.getInt("id_perfil_cognitivo"));
        perfilCognitivo.setIdPaciente(rs.getInt("T_ARMD_PACIENTE_id_pessoa"));
        perfilCognitivo.setInDificuldadeVisao(rs.getString("in_dificuldade_visao"));
        perfilCognitivo.setInUsaOculos(rs.getString("in_usa_oculos"));
        perfilCognitivo.setInDificuldadeAudicao(rs.getString("in_dificuldade_audicao"));
        perfilCognitivo.setInUsaAparelhoAud(rs.getString("in_usa_aparelho_aud"));
        perfilCognitivo.setInDificuldadeCogn(rs.getString("in_dificuldade_cogn"));

        Timestamp dataCadastro = rs.getTimestamp("dt_cadastro");
        if (dataCadastro != null) {
            perfilCognitivo.setDataCadastro(dataCadastro.toLocalDateTime());
        }

        Timestamp dataAtualizacao = rs.getTimestamp("dt_atualizacao");
        if (dataAtualizacao != null) {
            perfilCognitivo.setDataAtualizacao(dataAtualizacao.toLocalDateTime());
        }

        return perfilCognitivo;
    }

    @Override
    public PerfilCognitivo buscarPorId(Integer idPerfilCognitivo) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT * FROM T_ARMD_PERFIL_COGNITIVO WHERE id_perfil_cognitivo = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPerfilCognitivo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPerfilCognitivo(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("PerfilCognitivo não encontrado com ID: " + idPerfilCognitivo);
                }
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar perfil cognitivo por ID: " + e.getMessage());
        }
    }

    @Override
    public PerfilCognitivo buscarPorPaciente(Integer idPaciente) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT * FROM T_ARMD_PERFIL_COGNITIVO WHERE T_ARMD_PACIENTE_id_pessoa = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPerfilCognitivo(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("Perfil cognitivo não encontrado para paciente: " + idPaciente);
                }
            }
        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar perfil cognitivo por paciente: " + e.getMessage());
        }
    }

    @Override
    public List<PerfilCognitivo> buscarTodos() {
        String sql = "SELECT * FROM T_ARMD_PERFIL_COGNITIVO";
        List<PerfilCognitivo> perfisCognitivos = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                perfisCognitivos.add(mapResultSetToPerfilCognitivo(rs));
            }

            return perfisCognitivos;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar todos perfis cognitivos: " + e.getMessage());
        }
    }

    @Override
    public PerfilCognitivo salvar(PerfilCognitivo perfilCognitivo) {
        String sql = "INSERT INTO T_ARMD_PERFIL_COGNITIVO (T_ARMD_PACIENTE_id_pessoa, in_dificuldade_visao, in_usa_oculos, in_dificuldade_audicao, in_usa_aparelho_aud, in_dificuldade_cogn, dt_cadastro, dt_atualizacao) " +
                "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_perfil_cognitivo"})) {

            stmt.setInt(1, perfilCognitivo.getIdPaciente());
            stmt.setString(2, perfilCognitivo.getInDificuldadeVisao());
            stmt.setString(3, perfilCognitivo.getInUsaOculos());
            stmt.setString(4, perfilCognitivo.getInDificuldadeAudicao());
            stmt.setString(5, perfilCognitivo.getInUsaAparelhoAud());
            stmt.setString(6, perfilCognitivo.getInDificuldadeCogn());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfrastructureException("Nenhuma linha afetada ao salvar perfil cognitivo.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    perfilCognitivo.setIdPerfilCognitivo(generatedKeys.getInt(1));
                }
            }
            return perfilCognitivo;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao salvar perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public PerfilCognitivo editar(PerfilCognitivo perfilCognitivo) throws EntidadeNaoLocalizadaException {
        String sql = "UPDATE T_ARMD_PERFIL_COGNITIVO SET in_dificuldade_visao = ?, in_usa_oculos = ?, in_dificuldade_audicao = ?, in_usa_aparelho_aud = ?, in_dificuldade_cogn = ?, dt_atualizacao = CURRENT_TIMESTAMP " +
                "WHERE id_perfil_cognitivo = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, perfilCognitivo.getInDificuldadeVisao());
            stmt.setString(2, perfilCognitivo.getInUsaOculos());
            stmt.setString(3, perfilCognitivo.getInDificuldadeAudicao());
            stmt.setString(4, perfilCognitivo.getInUsaAparelhoAud());
            stmt.setString(5, perfilCognitivo.getInDificuldadeCogn());
            stmt.setInt(6, perfilCognitivo.getIdPerfilCognitivo());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("Perfil cognitivo não encontrado com ID: " + perfilCognitivo.getIdPerfilCognitivo());
            }

            return perfilCognitivo;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao editar perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public void remover(Integer idPerfilCognitivo) throws EntidadeNaoLocalizadaException {
        String sql = "DELETE FROM T_ARMD_PERFIL_COGNITIVO WHERE id_perfil_cognitivo = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPerfilCognitivo);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("Perfil cognitivo não encontrado com ID: " + idPerfilCognitivo);
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao remover perfil cognitivo: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarNecessidadesAcessibilidade() {
        String sql = "SELECT 'Visual' as tipo, COUNT(*) as quantidade FROM T_ARMD_PERFIL_COGNITIVO WHERE in_dificuldade_visao = 'S' " +
                "UNION ALL " +
                "SELECT 'Auditiva', COUNT(*) FROM T_ARMD_PERFIL_COGNITIVO WHERE in_dificuldade_audicao = 'S' " +
                "UNION ALL " +
                "SELECT 'Cognitiva', COUNT(*) FROM T_ARMD_PERFIL_COGNITIVO WHERE in_dificuldade_cogn = 'S'";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString("tipo");
                Long quantidade = rs.getLong("quantidade");
                Object[] resultado = new Object[]{tipo, quantidade};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar necessidades de acessibilidade: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarUsoAuxiliares() {
        String sql = "SELECT 'Óculos' as auxiliar, COUNT(*) as quantidade FROM T_ARMD_PERFIL_COGNITIVO WHERE in_usa_oculos = 'S' " +
                "UNION ALL " +
                "SELECT 'Aparelho Auditivo', COUNT(*) FROM T_ARMD_PERFIL_COGNITIVO WHERE in_usa_aparelho_aud = 'S'";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String auxiliar = rs.getString("auxiliar");
                Long quantidade = rs.getLong("quantidade");
                Object[] resultado = new Object[]{auxiliar, quantidade};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar uso de auxiliares: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarDadosAcessibilidade() {
        return buscarNecessidadesAcessibilidade();
    }

    @Override
    public Long count() {
        String sql = "SELECT COUNT(*) FROM T_ARMD_PERFIL_COGNITIVO";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao contar perfis cognitivos: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarEstatisticasCompletas() {
        String sql = "SELECT " +
                "COUNT(*) as total, " +
                "SUM(CASE WHEN in_dificuldade_visao = 'S' THEN 1 ELSE 0 END) as com_dificuldade_visao, " +
                "SUM(CASE WHEN in_usa_oculos = 'S' THEN 1 ELSE 0 END) as usa_oculos, " +
                "SUM(CASE WHEN in_dificuldade_audicao = 'S' THEN 1 ELSE 0 END) as com_dificuldade_audicao, " +
                "SUM(CASE WHEN in_usa_aparelho_aud = 'S' THEN 1 ELSE 0 END) as usa_aparelho_aud, " +
                "SUM(CASE WHEN in_dificuldade_cogn = 'S' THEN 1 ELSE 0 END) as com_dificuldade_cogn " +
                "FROM T_ARMD_PERFIL_COGNITIVO";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Object[] resultado = new Object[]{
                        rs.getLong("total"),
                        rs.getLong("com_dificuldade_visao"),
                        rs.getLong("usa_oculos"),
                        rs.getLong("com_dificuldade_audicao"),
                        rs.getLong("usa_aparelho_aud"),
                        rs.getLong("com_dificuldade_cogn")
                };
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar estatísticas completas: " + e.getMessage());
        }
    }
}