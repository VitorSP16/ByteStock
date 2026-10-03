package bytestock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import bytestock.service.MovimentacaoService;
import bytestock.service.ProdutoService;

@Controller
public class RelatorioController {

    private final ProdutoService produtoService;
    private final MovimentacaoService movimentacaoService;

    public RelatorioController(ProdutoService produtoService, MovimentacaoService movimentacaoService) {
        this.produtoService = produtoService;
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping("/relatorios")
    public String relatorios(Model model) {
        model.addAttribute("totalProdutos", produtoService.contarProdutos());
        model.addAttribute("totalUnidades", produtoService.totalUnidades());
        model.addAttribute("valorTotalEstoque", produtoService.valorTotalEstoque());
        model.addAttribute("totalEstoqueBaixo", produtoService.listarEstoqueBaixo().size());
        model.addAttribute("totalEntradas", movimentacaoService.totalEntradas());
        model.addAttribute("totalSaidas", movimentacaoService.totalSaidas());
        model.addAttribute("quantidadePorCategoria", produtoService.quantidadePorCategoria());
        model.addAttribute("produtosEstoqueBaixo", produtoService.listarEstoqueBaixo());
        return "relatorios";
    }
}
