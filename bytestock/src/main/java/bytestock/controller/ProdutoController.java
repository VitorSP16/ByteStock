package bytestock.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import bytestock.entity.Produto;
import bytestock.service.ProdutoService;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/produtos")
@Controller
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public String listarProdutos(Model model) {

        model.addAttribute("produtos", produtoService.listarTodos());

        return "produtos/lista";
    }

    @GetMapping("/novo")
    public String novoProduto(Model model) {

        model.addAttribute("produto", new Produto());

        return "produtos/formulario";
    }
    @PostMapping("/produtos")
    public String salvarProduto(@ModelAttribute Produto produto) {
    produtoService.salvar(produto);
    return "redirect:/produtos";
  }
   @GetMapping("/produtos/editar/{id}")
   public String editarProduto(@PathVariable Long id, Model model) {
    Produto produto = produtoService.buscarPorId(id);
    model.addAttribute("produto", produto);
    return "produtos/formulario";
  }
   @GetMapping("/produtos/excluir/{id}")
   public String excluirProduto(@PathVariable Long id) {
    produtoService.excluir(id);
    return "redirect:/produtos";
  }
}