package com.example.fipeapp.principal;

import com.example.fipeapp.model.Dados;
import com.example.fipeapp.model.Modelos;
import com.example.fipeapp.model.Veiculo;
import com.example.fipeapp.service.ConsumoApi;
import com.example.fipeapp.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String URL = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu(){
        var menu = """
                ***OPÇÕES***
                (1) Carro 
                (2) Moto 
                (3) Caminhão 
                
                Escolha uma das opções para realizar a consulta:
                """;

        System.out.println(menu);
        var opcao = leitura.nextInt();

        String endereco;

        switch (opcao) {
            case 1 -> endereco = URL + "carros/marcas/";
            case 2 -> endereco = URL + "motos/marcas/";
            case 3 -> endereco = URL + "caminhoes/marcas/";
            default -> {
                System.out.println("Opção inválida!\n\nFechando aplicação...");
                return;
            }
        }
        var json = consumo.obterDados(endereco);
        System.out.println(json);
        var marcas = conversor.converterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta:");
        var codigoMarca = leitura.next();

        endereco = endereco + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = conversor.converterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do carro a ser buscado:");
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o código do modelo para buscar os valores da FIPE:");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.converterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.converterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veículos filtrados com valores por ano: ");
        veiculos.forEach(System.out::println);
    }
}
