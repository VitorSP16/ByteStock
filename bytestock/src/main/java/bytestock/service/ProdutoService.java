package bytestock.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bytestock.entity.Movimentacao;
import bytestock.entity.Produto;
import bytestock.repository.MovimentacaoRepository;
import bytestock.repository.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public ProdutoService(ProdutoRepository produtoRepository, MovimentacaoRepository movimentacaoRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public List<Produto> filtrar(String busca, String categoria, String status) {
        String termo = busca == null ? "" : busca.trim().toLowerCase();
        String categoriaFiltro = categoria == null ? "" : categoria.trim();
        String statusFiltro = status == null ? "" : status.trim();

        return listarTodos().stream()
                .filter(produto -> termo.isBlank()
                        || contem(produto.getNome(), termo)
                        || contem(produto.getMarca(), termo)
                        || contem(produto.getCategoria(), termo)
                        || contem(produto.getDescricao(), termo))
                .filter(produto -> categoriaFiltro.isBlank()
                        || categoriaFiltro.equalsIgnoreCase(produto.getCategoria()))
                .filter(produto -> !"baixo".equalsIgnoreCase(statusFiltro)
                        || estaComEstoqueBaixo(produto))
                .sorted(Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    private boolean contem(String valor, String termo) {
        return valor != null && valor.toLowerCase().contains(termo);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        if (produto.getId() != null) {
            Produto existente = produtoRepository.findById(produto.getId()).orElse(null);
            if (existente != null) {
                produto.setQuantidade(existente.getQuantidade());
                return produtoRepository.save(produto);
            }
        }

        int quantidadeInicial = produto.getQuantidade() == null ? 0 : Math.max(0, produto.getQuantidade());
        produto.setQuantidade(quantidadeInicial);
        Produto salvo = produtoRepository.save(produto);

        if (quantidadeInicial > 0) {
            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setTipo("ENTRADA");
            movimentacao.setQuantidade(quantidadeInicial);
            movimentacao.setData(LocalDateTime.now());
            movimentacao.setProduto(salvo);
            movimentacaoRepository.save(movimentacao);
        }

        return salvo;
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    public List<Produto> listarEstoqueBaixo() {
        return listarTodos().stream()
                .filter(this::estaComEstoqueBaixo)
                .sorted(Comparator.comparingInt(produto -> produto.getQuantidade() == null ? 0 : produto.getQuantidade()))
                .toList();
    }

    private boolean estaComEstoqueBaixo(Produto produto) {
        int quantidade = produto.getQuantidade() == null ? 0 : produto.getQuantidade();
        int minimo = produto.getEstoqueMinimo() == null ? 0 : produto.getEstoqueMinimo();
        return quantidade <= minimo;
    }

    public List<String> listarCategorias() {
        return listarTodos().stream()
                .map(Produto::getCategoria)
                .filter(categoria -> categoria != null && !categoria.isBlank())
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    public long contarProdutos() {
        return produtoRepository.count();
    }

    public int totalUnidades() {
        return listarTodos().stream()
                .mapToInt(produto -> produto.getQuantidade() == null ? 0 : produto.getQuantidade())
                .sum();
    }

    public BigDecimal valorTotalEstoque() {
        return listarTodos().stream()
                .map(produto -> {
                    BigDecimal preco = produto.getPreco() == null ? BigDecimal.ZERO : produto.getPreco();
                    int quantidade = produto.getQuantidade() == null ? 0 : produto.getQuantidade();
                    return preco.multiply(BigDecimal.valueOf(quantidade));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, Integer> quantidadePorCategoria() {
        Map<String, Integer> resumo = new LinkedHashMap<>();

        listarTodos().stream()
                .sorted(Comparator.comparing(Produto::getCategoria, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .forEach(produto -> {
                    String categoria = produto.getCategoria() == null || produto.getCategoria().isBlank()
                            ? "Sem categoria"
                            : produto.getCategoria();
                    int quantidade = produto.getQuantidade() == null ? 0 : produto.getQuantidade();
                    resumo.merge(categoria, quantidade, Integer::sum);
                });

        return resumo;
    }

    @Transactional
    public void excluir(Long id) {
        Produto produto = produtoRepository.findById(id).orElse(null);
        if (produto == null) {
            return;
        }

        movimentacaoRepository.deleteByProduto(produto);
        produtoRepository.delete(produto);
    }
}
