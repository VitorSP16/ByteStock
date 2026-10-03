package bytestock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import bytestock.service.MovimentacaoService;
import bytestock.service.ProdutoService;

@Controller
public class HomeController {

    private final ProdutoService produtoService;
    private final MovimentacaoService movimentacaoService;

    public HomeController(ProdutoService produtoService, MovimentacaoService movimentacaoService) {
        this.produtoService = produtoService;
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping({"/", "/bytestock"})
    public String index(Model model) {
        model.addAttribute("totalProdutos", produtoService.contarProdutos());
        model.addAttribute("totalUnidades", produtoService.totalUnidades());
        model.addAttribute("totalEstoqueBaixo", produtoService.listarEstoqueBaixo().size());
        model.addAttribute("valorTotalEstoque", produtoService.valorTotalEstoque());
        model.addAttribute("ultimasMovimentacoes", movimentacaoService.listarUltimas(5));
        model.addAttribute("produtosEstoqueBaixo", produtoService.listarEstoqueBaixo().stream().limit(5).toList());
        return "index";
    }
}
