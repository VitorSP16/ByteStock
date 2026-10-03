package bytestock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import bytestock.entity.Movimentacao;
import bytestock.entity.Produto;
import bytestock.repository.MovimentacaoRepository;
import bytestock.repository.ProdutoRepository;
import bytestock.service.MovimentacaoService;
import bytestock.service.MovimentacaoService.ResultadoMovimentacao;

@SpringBootTest
@Transactional
class MovimentacaoServiceTests {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Test
    void entradaDeveAumentarEstoqueERegistrarHistorico() {
        Produto produto = criarProduto(10);

        ResultadoMovimentacao resultado = movimentacaoService.registrarEntrada(produto.getId(), 5);

        Produto atualizado = produtoRepository.findById(produto.getId()).orElseThrow();
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByProdutoOrderByDataDesc(atualizado);

        assertTrue(resultado.sucesso());
        assertEquals(15, atualizado.getQuantidade());
        assertEquals(1, movimentacoes.size());
        assertEquals("ENTRADA", movimentacoes.get(0).getTipo());
        assertEquals(5, movimentacoes.get(0).getQuantidade());
    }

    @Test
    void saidaDeveDiminuirEstoqueERegistrarHistorico() {
        Produto produto = criarProduto(10);

        ResultadoMovimentacao resultado = movimentacaoService.registrarSaida(produto.getId(), 4);

        Produto atualizado = produtoRepository.findById(produto.getId()).orElseThrow();
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByProdutoOrderByDataDesc(atualizado);

        assertTrue(resultado.sucesso());
        assertEquals(6, atualizado.getQuantidade());
        assertEquals(1, movimentacoes.size());
        assertEquals("SAIDA", movimentacoes.get(0).getTipo());
        assertEquals(4, movimentacoes.get(0).getQuantidade());
    }

    @Test
    void saidaMaiorQueEstoqueNaoDeveAlterarNada() {
        Produto produto = criarProduto(3);

        ResultadoMovimentacao resultado = movimentacaoService.registrarSaida(produto.getId(), 5);

        Produto atualizado = produtoRepository.findById(produto.getId()).orElseThrow();
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByProdutoOrderByDataDesc(atualizado);

        assertFalse(resultado.sucesso());
        assertEquals(3, atualizado.getQuantidade());
        assertTrue(movimentacoes.isEmpty());
    }

    @Test
    void quantidadeZeroNaoDeveGerarMovimentacao() {
        Produto produto = criarProduto(8);

        ResultadoMovimentacao entrada = movimentacaoService.registrarEntrada(produto.getId(), 0);
        ResultadoMovimentacao saida = movimentacaoService.registrarSaida(produto.getId(), 0);

        Produto atualizado = produtoRepository.findById(produto.getId()).orElseThrow();

        assertFalse(entrada.sucesso());
        assertFalse(saida.sucesso());
        assertEquals(8, atualizado.getQuantidade());
        assertTrue(movimentacaoRepository.findByProdutoOrderByDataDesc(atualizado).isEmpty());
    }

    private Produto criarProduto(int quantidade) {
        Produto produto = new Produto();
        produto.setNome("Produto teste");
        produto.setMarca("Marca teste");
        produto.setCategoria("Teste");
        produto.setQuantidade(quantidade);
        produto.setEstoqueMinimo(2);
        produto.setPreco(BigDecimal.TEN);
        produto.setDescricao("Produto usado nos testes de movimentação");
        return produtoRepository.save(produto);
    }
}
