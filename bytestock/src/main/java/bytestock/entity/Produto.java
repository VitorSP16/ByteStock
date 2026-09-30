package bytestock.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String marca;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "estoque_minimo", nullable = false)
    private Integer estoqueMinimo;

    private BigDecimal preco;

    private String descricao;
}