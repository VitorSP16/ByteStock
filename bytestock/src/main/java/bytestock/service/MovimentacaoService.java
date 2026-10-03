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

    public record ResultadoMovimentacao(boolean sucesso, String mensagem) {
        public static ResultadoMovimentacao ok(String mensagem) {
            return new ResultadoMovimentacao(true, mensagem);
        }

        public static ResultadoMovimentacao erro(String mensagem) {
            return new ResultadoMovimentacao(false, mensagem);
        }
    }

    @Transactional
    public ResultadoMovimentacao registrarEntrada(Long produtoId, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            return ResultadoMovimentacao.erro("A quantidade de entrada deve ser maior que zero.");
        }

        Produto produto = produtoRepository.buscarPorIdParaMovimentacao(produtoId).orElse(null);
        if (produto == null) {
            return ResultadoMovimentacao.erro("Produto não encontrado.");
        }

        int estoqueAtual = produto.getQuantidade() == null ? 0 : produto.getQuantidade();
        long novoEstoque = (long) estoqueAtual + quantidade;

        if (novoEstoque > Integer.MAX_VALUE) {
            return ResultadoMovimentacao.erro("A quantidade informada é muito alta.");
        }

        produto.setQuantidade((int) novoEstoque);
        produtoRepository.save(produto);

        salvarMovimentacao(produto, "ENTRADA", quantidade);

        return ResultadoMovimentacao.ok(
                "Entrada de " + quantidade + " unidade(s) registrada com sucesso.");
    }

    @Transactional
    public ResultadoMovimentacao registrarSaida(Long produtoId, Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            return ResultadoMovimentacao.erro("A quantidade de saída deve ser maior que zero.");
        }

        Produto produto = produtoRepository.buscarPorIdParaMovimentacao(produtoId).orElse(null);
        if (produto == null) {
            return ResultadoMovimentacao.erro("Produto não encontrado.");
        }

        int estoqueAtual = produto.getQuantidade() == null ? 0 : produto.getQuantidade();

        if (quantidade > estoqueAtual) {
            return ResultadoMovimentacao.erro(
                    "Estoque insuficiente. Disponível: " + estoqueAtual + " unidade(s).");
        }

        produto.setQuantidade(estoqueAtual - quantidade);
        produtoRepository.save(produto);

        salvarMovimentacao(produto, "SAIDA", quantidade);

        return ResultadoMovimentacao.ok(
                "Saída de " + quantidade + " unidade(s) registrada com sucesso.");
    }

    private void salvarMovimentacao(Produto produto, String tipo, Integer quantidade) {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTipo(tipo);
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
        if (limite <= 0) {
            return List.of();
        }

        return movimentacaoRepository.findAllByOrderByDataDesc().stream()
                .limit(limite)
                .toList();
    }

    @Transactional(readOnly = true)
    public int totalEntradas() {
        return movimentacaoRepository.findAllByOrderByDataDesc().stream()
                .filter(mov -> "ENTRADA".equals(mov.getTipo()))
                .mapToInt(Movimentacao::getQuantidade)
                .sum();
    }

    @Transactional(readOnly = true)
    public int totalSaidas() {
        return movimentacaoRepository.findAllByOrderByDataDesc().stream()
                .filter(mov -> "SAIDA".equals(mov.getTipo()))
                .mapToInt(Movimentacao::getQuantidade)
                .sum();
    }
}
