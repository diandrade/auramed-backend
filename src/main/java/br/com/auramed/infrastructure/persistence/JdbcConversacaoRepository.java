package br.com.auramed.infrastructure.persistence;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.Conversacao;
import br.com.auramed.domain.repository.ConversacaoRepository;
import br.com.auramed.infrastructure.exception.InfrastructureException;
import jakarta.inject.Inject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcConversacaoRepository implements ConversacaoRepository {

    private final DatabaseConnection databaseConnection;

    @Inject
    public JdbcConversacaoRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    private Conversacao mapResultSetToConversacao(ResultSet rs) throws SQLException {
        Conversacao conversacao = new Conversacao();
        conversacao.setId(String.valueOf(rs.getInt("ID_CONVERSACAO")));
        conversacao.setUsuarioId(rs.getString("ID_USUARIO"));
        conversacao.setPerguntaUsuario(rs.getString("DS_PERGUNTA_USUARIO"));
        conversacao.setRespostaBot(rs.getString("DS_RESPOSTA_BOT"));
        conversacao.setSentimento(rs.getString("TP_SENTIMENTO"));
        conversacao.setCategoria(rs.getString("TP_CATEGORIA"));
        conversacao.setFonteResposta(rs.getString("TP_FONTE_RESPOSTA"));
        conversacao.setDataHora(rs.getTimestamp("DT_CONVERSACAO").toLocalDateTime());
        return conversacao;
    }

    @Override
    public Conversacao salvar(Conversacao conversacao) {
        String sql = "INSERT INTO T_ARMD_CONVERSACAO (ID_USUARIO, DS_PERGUNTA_USUARIO, DS_RESPOSTA_BOT, TP_SENTIMENTO, TP_CATEGORIA, TP_FONTE_RESPOSTA, DT_CONVERSACAO) " +
                "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"ID_CONVERSACAO"})) {

            stmt.setString(1, conversacao.getUsuarioId());
            stmt.setString(2, conversacao.getPerguntaUsuario());
            stmt.setString(3, conversacao.getRespostaBot());
            stmt.setString(4, conversacao.getSentimento());
            stmt.setString(5, conversacao.getCategoria());
            stmt.setString(6, conversacao.getFonteResposta());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new InfrastructureException("Nenhuma linha afetada ao salvar conversação.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    conversacao.setId(String.valueOf(generatedKeys.getInt(1)));
                }
            }
            return conversacao;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao salvar conversação: " + e.getMessage());
        }
    }

    @Override
    public Conversacao buscarPorId(Integer id) throws EntidadeNaoLocalizadaException {
        String sql = "SELECT * FROM T_ARMD_CONVERSACAO WHERE ID_CONVERSACAO = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToConversacao(rs);
                } else {
                    throw new EntidadeNaoLocalizadaException("Conversação não encontrada com ID: " + id);
                }
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar conversação por ID: " + e.getMessage());
        }
    }

    @Override
    public List<Conversacao> buscarPorUsuario(String usuarioId) {
        String sql = "SELECT * FROM T_ARMD_CONVERSACAO WHERE ID_USUARIO = ? ORDER BY DT_CONVERSACAO DESC";
        List<Conversacao> conversacoes = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    conversacoes.add(mapResultSetToConversacao(rs));
                }
            }

            return conversacoes;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar conversações por usuário: " + e.getMessage());
        }
    }

    @Override
    public List<Conversacao> buscarTodos() {
        String sql = "SELECT * FROM T_ARMD_CONVERSACAO ORDER BY DT_CONVERSACAO DESC";
        List<Conversacao> conversacoes = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                conversacoes.add(mapResultSetToConversacao(rs));
            }

            return conversacoes;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar todas conversações: " + e.getMessage());
        }
    }

    @Override
    public void remover(Integer id) throws EntidadeNaoLocalizadaException {
        String sql = "DELETE FROM T_ARMD_CONVERSACAO WHERE ID_CONVERSACAO = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EntidadeNaoLocalizadaException("Conversação não encontrada com ID: " + id);
            }

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao remover conversação: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarPerguntasFrequentes(int limite) {
        String sql = "SELECT DS_PERGUNTA_USUARIO, COUNT(*) as frequencia, COALESCE(TP_CATEGORIA, 'GERAL') as categoria FROM T_ARMD_CONVERSACAO WHERE DS_PERGUNTA_USUARIO IS NOT NULL AND LENGTH(TRIM(DS_PERGUNTA_USUARIO)) > 7 AND UPPER(DS_PERGUNTA_USUARIO) NOT IN ('OI', 'OLA', 'OLÁ', 'HELLO', 'HI', 'HEY', 'TCHAU', 'BYE', 'OK', 'TESTE') AND REGEXP_LIKE(DS_PERGUNTA_USUARIO, '.*[A-Za-zÀ-ÿ].*') GROUP BY DS_PERGUNTA_USUARIO, TP_CATEGORIA ORDER BY frequencia DESC FETCH FIRST ? ROWS ONLY";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limite);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String pergunta = rs.getString("DS_PERGUNTA_USUARIO");
                    Long frequencia = rs.getLong("frequencia");
                    String categoria = rs.getString("categoria");
                    Object[] resultado = new Object[]{pergunta, frequencia, categoria};
                    resultados.add(resultado);
                }
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar perguntas frequentes: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarUsoPorMes() {
        String sql = "SELECT TO_CHAR(DT_CONVERSACAO, 'YYYY-MM') as mes, COUNT(*) as quantidade FROM T_ARMD_CONVERSACAO WHERE DT_CONVERSACAO >= ADD_MONTHS(CURRENT_DATE, -6) GROUP BY TO_CHAR(DT_CONVERSACAO, 'YYYY-MM') ORDER BY mes";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String mes = rs.getString("mes");
                Long quantidade = rs.getLong("quantidade");
                Object[] resultado = new Object[]{mes, quantidade};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar uso por mês: " + e.getMessage());
        }
    }

    @Override
    public List<String> buscarPerguntasComBaixaConfianca() {
        String sql = "SELECT DS_PERGUNTA_USUARIO FROM T_ARMD_CONVERSACAO WHERE DS_PERGUNTA_USUARIO IS NOT NULL AND LENGTH(TRIM(DS_PERGUNTA_USUARIO)) > 10 AND (TP_CATEGORIA = 'OUTRAS_DUVIDAS' OR TP_FONTE_RESPOSTA = 'GEMINI') ORDER BY DT_CONVERSACAO DESC FETCH FIRST 10 ROWS ONLY";
        List<String> perguntas = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                perguntas.add(rs.getString("DS_PERGUNTA_USUARIO"));
            }

            return perguntas;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar perguntas com baixa confiança: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarPerguntasFrequentes() {
        return buscarPerguntasFrequentes(10);
    }

    @Override
    public Long getTotalConversacoes() {
        String sql = "SELECT COUNT(*) FROM T_ARMD_CONVERSACAO";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0L;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao contar total de conversações: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarEstatisticasSentimentos() {
        String sql = "SELECT TP_SENTIMENTO, COUNT(*) as quantidade FROM T_ARMD_CONVERSACAO WHERE TP_SENTIMENTO IS NOT NULL GROUP BY TP_SENTIMENTO ORDER BY quantidade DESC";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String sentimento = rs.getString("TP_SENTIMENTO");
                Long quantidade = rs.getLong("quantidade");
                Object[] resultado = new Object[]{sentimento, quantidade};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar estatísticas de sentimentos: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarUsoPorPeriodo(String periodo) {
        String sql = "";
        switch (periodo.toUpperCase()) {
            case "DAY":
                sql = "SELECT TO_CHAR(DT_CONVERSACAO, 'DD/MM') as data, COUNT(*) as quantidade FROM T_ARMD_CONVERSACAO WHERE DT_CONVERSACAO >= CURRENT_DATE - 30 GROUP BY TO_CHAR(DT_CONVERSACAO, 'DD/MM') ORDER BY MIN(DT_CONVERSACAO)";
                break;
            case "WEEK":
                sql = "SELECT TO_CHAR(DT_CONVERSACAO, 'WW') as semana, COUNT(*) as quantidade FROM T_ARMD_CONVERSACAO WHERE DT_CONVERSACAO >= CURRENT_DATE - 90 GROUP BY TO_CHAR(DT_CONVERSACAO, 'WW') ORDER BY MIN(DT_CONVERSACAO)";
                break;
            default:
                sql = "SELECT TO_CHAR(DT_CONVERSACAO, 'MM/YYYY') as mes, COUNT(*) as quantidade FROM T_ARMD_CONVERSACAO WHERE DT_CONVERSACAO >= CURRENT_DATE - 365 GROUP BY TO_CHAR(DT_CONVERSACAO, 'MM/YYYY') ORDER BY MIN(DT_CONVERSACAO)";
        }

        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String periodoStr = rs.getString(1);
                Long quantidade = rs.getLong("quantidade");
                Object[] resultado = new Object[]{periodoStr, quantidade};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar uso por período: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarMetricasEngajamento() {
        String sql = "SELECT " +
                "COUNT(*) as total_conversas, " +
                "COUNT(DISTINCT ID_USUARIO) as usuarios_unicos, " +
                "ROUND(COUNT(*) / NULLIF(COUNT(DISTINCT ID_USUARIO), 0), 2) as media_conversas_por_usuario, " +
                "SUM(CASE WHEN LENGTH(DS_PERGUNTA_USUARIO) > 20 THEN 1 ELSE 0 END) as perguntas_detalhadas " +
                "FROM T_ARMD_CONVERSACAO";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                Object[] resultado = new Object[]{
                        rs.getLong("total_conversas"),
                        rs.getLong("usuarios_unicos"),
                        rs.getDouble("media_conversas_por_usuario"),
                        rs.getLong("perguntas_detalhadas")
                };
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar métricas de engajamento: " + e.getMessage());
        }
    }

    @Override
    public List<Object[]> buscarEstatisticasFontesResposta() {
        String sql = "SELECT TP_FONTE_RESPOSTA, COUNT(*) as quantidade FROM T_ARMD_CONVERSACAO WHERE TP_FONTE_RESPOSTA IS NOT NULL GROUP BY TP_FONTE_RESPOSTA ORDER BY quantidade DESC";
        List<Object[]> resultados = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String fonte = rs.getString("TP_FONTE_RESPOSTA");
                Long quantidade = rs.getLong("quantidade");
                Object[] resultado = new Object[]{fonte, quantidade};
                resultados.add(resultado);
            }

            return resultados;

        } catch (SQLException e) {
            throw new InfrastructureException("Erro ao buscar estatísticas de fontes de resposta: " + e.getMessage());
        }
    }
}