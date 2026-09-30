package bytestock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import bytestock.entity.Movimentacao;
import bytestock.entity.Produto;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByProduto(Produto produto);

   
}