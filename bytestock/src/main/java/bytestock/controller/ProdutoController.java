package bytestock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import bytestock.entity.Produto;
import bytestock.service.ProdutoService;

@RequestMapping("/produtos")
@Controller
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listarProdutos(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String status,
            Model model) {

        model.addAttribute("produtos", produtoService.filtrar(busca, categoria, status));
        model.addAttribute("categorias", produtoService.listarCategorias());
        model.addAttribute("busca", busca == null ? "" : busca);
        model.addAttribute("categoriaSelecionada", categoria == null ? "" : categoria);
        model.addAttribute("statusSelecionado", status == null ? "" : status);
        return "produtos/lista";
    }

    @GetMapping("/estoque-baixo")
    public String estoqueBaixo(Model model) {
        model.addAttribute("produtos", produtoService.listarEstoqueBaixo());
        return "produtos/estoque-baixo";
    }

    @GetMapping("/novo")
    public String novoProduto(Model model) {
        model.addAttribute("produto", new Produto());
        return "produtos/formulario";
    }

    @PostMapping
    public String salvarProduto(@ModelAttribute Produto produto) {
        produtoService.salvar(produto);
        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable Long id, Model model) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return "redirect:/produtos";
        }
        model.addAttribute("produto", produto);
        return "produtos/formulario";
    }

    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id) {
        produtoService.excluir(id);
        return "redirect:/produtos";
    }
}
