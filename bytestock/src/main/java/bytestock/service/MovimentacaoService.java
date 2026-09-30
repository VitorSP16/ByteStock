package bytestock.service;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bytestock.entity.Movimentacao;
import bytestock.entity.Produto;
import bytestock.repository.MovimentacaoRepository;
import bytestock.repository.ProdutoRepository;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public void registrarEntrada(Long produtoId, Integer quantidade) {

        Produto produto = produtoRepository.findById(produtoId).orElse(null);

        if (produto == null) {
            return;
        }

        if (quantidade <= 0) {
        return;
       }

        produto.setQuantidade(
            produto.getQuantidade() + quantidade
        );

        produtoRepository.save(produto);

        Movimentacao movimentacao = new Movimentacao();

        movimentacao.setTipo("ENTRADA");
        movimentacao.setQuantidade(quantidade);
        movimentacao.setData(LocalDateTime.now());
        movimentacao.setProduto(produto);

        movimentacaoRepository.save(movimentacao);
    }

    public void registrarSaida(Long produtoId, Integer quantidade) {

    Produto produto = produtoRepository.findById(produtoId).orElse(null);

    if (produto == null) {
        return;
    }

    if (quantidade <= 0) {
        return;
    }

    if (quantidade > produto.getQuantidade()) {
        return;
    }

    produto.setQuantidade(
        produto.getQuantidade() - quantidade
    );

    produtoRepository.save(produto);

    Movimentacao movimentacao = new Movimentacao();

    movimentacao.setTipo("SAIDA");
    movimentacao.setQuantidade(quantidade);
    movimentacao.setData(LocalDateTime.now());
    movimentacao.setProduto(produto);

    movimentacaoRepository.save(movimentacao);
  }
  public List<Movimentacao> listarPorProduto(Long produtoId) {

    Produto produto = produtoRepository.findById(produtoId).orElse(null);

    if (produto == null) {
        return List.of();
    }

    return movimentacaoRepository.findByProduto(produto);
}
}
