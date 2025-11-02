package br.com.auramed.domain.service;

import br.com.auramed.domain.exception.EntidadeNaoLocalizadaException;
import br.com.auramed.domain.model.Endereco;
import java.util.List;

public interface EnderecoService {
    Endereco criar(Endereco endereco);
    Endereco editar(Integer id, Endereco endereco) throws EntidadeNaoLocalizadaException;
    Endereco remover(Integer id) throws EntidadeNaoLocalizadaException;
    Endereco localizar(Integer id) throws EntidadeNaoLocalizadaException;
    List<Endereco> listarTodos();
    List<Endereco> listarPorPessoaId(Integer pessoaId);
    Endereco definirComoPrincipal(Integer id) throws EntidadeNaoLocalizadaException;
    Endereco definirComoNaoPrincipal(Integer id) throws EntidadeNaoLocalizadaException;
    List<Endereco> listarEnderecosPrincipais();
    void removerPorPessoa(Integer idPessoa) throws EntidadeNaoLocalizadaException;
    List<Endereco> buscarPorPessoa(Integer idPessoa) throws EntidadeNaoLocalizadaException;
}