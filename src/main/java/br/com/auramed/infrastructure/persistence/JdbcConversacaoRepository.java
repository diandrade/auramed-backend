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
        // Ajustado para String - usando valueOf para converter int para String
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
}