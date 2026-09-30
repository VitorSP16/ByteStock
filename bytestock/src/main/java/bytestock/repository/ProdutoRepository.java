package bytestock.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import bytestock.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}