package bytestock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import bytestock.entity.Produto;
import bytestock.service.MovimentacaoService;
import bytestock.service.ProdutoService;

@Controller
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/produtos/{id}/entrada")
    public String paginaEntrada(@PathVariable Long id, Model model) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return "redirect:/produtos";
        }
        model.addAttribute("produto", produto);
        return "movimentacoes/entrada";
    }

    @GetMapping("/produtos/{id}/saida")
    public String paginaSaida(@PathVariable Long id, Model model) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return "redirect:/produtos";
        }
        model.addAttribute("produto", produto);
        return "movimentacoes/saida";
    }

    @PostMapping("/produtos/{id}/entrada")
    public String registrarEntrada(@PathVariable Long id, @RequestParam Integer quantidade) {
        movimentacaoService.registrarEntrada(id, quantidade);
        return "redirect:/produtos";
    }

    @PostMapping("/produtos/{id}/saida")
    public String registrarSaida(@PathVariable Long id, @RequestParam Integer quantidade) {
        movimentacaoService.registrarSaida(id, quantidade);
        return "redirect:/produtos";
    }

    @GetMapping("/produtos/{id}/historico")
    public String historico(@PathVariable Long id, Model model) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return "redirect:/produtos";
        }
        model.addAttribute("produto", produto);
        model.addAttribute("movimentacoes", movimentacaoService.listarPorProduto(id));
        return "movimentacoes/historico";
    }
}
