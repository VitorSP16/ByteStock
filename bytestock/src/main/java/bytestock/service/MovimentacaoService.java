package bytestock.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bytestock.entity.Movimentacao;
import bytestock.entity.Produto;
import bytestock.repository.MovimentacaoRepository;
import bytestock.repository.ProdutoRepository;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, ProdutoRepository produtoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public void registrarEntrada(Long produtoId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId).orElse(null);

        if (produto == null || quantidade == null || quantidade <= 0) {
            return;
        }

        int estoqueAtual = produto.getQuantidade() == null ? 0 : produto.getQuantidade();

        produto.setQuantidade(estoqueAtual + quantidade);
        produtoRepository.save(produto);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTipo("ENTRADA");
        movimentacao.setQuantidade(quantidade);
        movimentacao.setData(LocalDateTime.now());
        movimentacao.setProduto(produto);

        movimentacaoRepository.save(movimentacao);
    }

    @Transactional
    public void registrarSaida(Long produtoId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId).orElse(null);

        if (produto == null || quantidade == null || quantidade <= 0) {
            return;
        }

        int estoqueAtual = produto.getQuantidade() == null ? 0 : produto.getQuantidade();

        if (quantidade > estoqueAtual) {
            return;
        }

        produto.setQuantidade(estoqueAtual - quantidade);
        produtoRepository.save(produto);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTipo("SAIDA");
        movimentacao.setQuantidade(quantidade);
        movimentacao.setData(LocalDateTime.now());
        movimentacao.setProduto(produto);

        movimentacaoRepository.save(movimentacao);
    }

    @Transactional(readOnly = true)
    public List<Movimentacao> listarPorProduto(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId).orElse(null);

        if (produto == null) {
            return List.of();
        }

        return movimentacaoRepository.findByProdutoOrderByDataDesc(produto);
    }

    @Transactional(readOnly = true)
    public List<Movimentacao> listarTodas() {
        return movimentacaoRepository.findAllByOrderByDataDesc();
    }

    @Transactional(readOnly = true)
    public List<Movimentacao> listarUltimas(int limite) {
        return movimentacaoRepository.findTop5ByOrderByDataDesc().stream()
                .limit(Math.max(0, limite))
                .toList();
    }

    @Transactional(readOnly = true)
    public int totalEntradas() {
        return listarTodas().stream()
                .filter(mov -> "ENTRADA".equals(mov.getTipo()))
                .mapToInt(Movimentacao::getQuantidade)
                .sum();
    }

    @Transactional(readOnly = true)
    public int totalSaidas() {
        return listarTodas().stream()
                .filter(mov -> "SAIDA".equals(mov.getTipo()))
                .mapToInt(Movimentacao::getQuantidade)
                .sum();
    }
}
