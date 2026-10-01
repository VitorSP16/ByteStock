package bytestock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bytestock.entity.Produto;
import bytestock.repository.MovimentacaoRepository;
import bytestock.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
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
