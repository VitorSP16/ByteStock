package bytestock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bytestock.entity.Produto;
import bytestock.service.MovimentacaoService;
import bytestock.service.MovimentacaoService.ResultadoMovimentacao;
import bytestock.service.ProdutoService;

@Controller
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;
    private final ProdutoService produtoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService, ProdutoService produtoService) {
        this.movimentacaoService = movimentacaoService;
        this.produtoService = produtoService;
    }

    @GetMapping("/movimentacoes")
    public String listarTodas(Model model) {
        model.addAttribute("movimentacoes", movimentacaoService.listarTodas());
        model.addAttribute("totalEntradas", movimentacaoService.totalEntradas());
        model.addAttribute("totalSaidas", movimentacaoService.totalSaidas());
        return "movimentacoes/lista-geral";
    }

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
    public String registrarEntrada(
            @PathVariable Long id,
            @RequestParam Integer quantidade,
            RedirectAttributes redirectAttributes) {

        ResultadoMovimentacao resultado = movimentacaoService.registrarEntrada(id, quantidade);

        if (!resultado.sucesso()) {
            redirectAttributes.addFlashAttribute("erro", resultado.mensagem());
            return "redirect:/produtos/" + id + "/entrada";
        }

        redirectAttributes.addFlashAttribute("sucesso", resultado.mensagem());
        return "redirect:/produtos/" + id + "/historico";
    }

    @PostMapping("/produtos/{id}/saida")
    public String registrarSaida(
            @PathVariable Long id,
            @RequestParam Integer quantidade,
            RedirectAttributes redirectAttributes) {

        ResultadoMovimentacao resultado = movimentacaoService.registrarSaida(id, quantidade);

        if (!resultado.sucesso()) {
            redirectAttributes.addFlashAttribute("erro", resultado.mensagem());
            return "redirect:/produtos/" + id + "/saida";
        }

        redirectAttributes.addFlashAttribute("sucesso", resultado.mensagem());
        return "redirect:/produtos/" + id + "/historico";
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
