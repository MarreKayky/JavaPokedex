package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokemonApiService {

    public Pokemon buscarPokemon(String nomeOuId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // VIAGEM 1: Dados básicos (Tipo e HP)
            String urlBase = "https://pokeapi.co/api/v2/pokemon/" + nomeOuId.toLowerCase();
            HttpRequest req1 = HttpRequest.newBuilder().uri(URI.create(urlBase)).GET().build();
            HttpResponse<String> res1 = client.send(req1, HttpResponse.BodyHandlers.ofString());

            // VIAGEM 2: Espécie (Bioma, Raridade e Descrição)
            String urlSpecies = "https://pokeapi.co/api/v2/pokemon-species/" + nomeOuId.toLowerCase();
            HttpRequest req2 = HttpRequest.newBuilder().uri(URI.create(urlSpecies)).GET().build();
            HttpResponse<String> res2 = client.send(req2, HttpResponse.BodyHandlers.ofString());

            if (res1.statusCode() == 200 && res2.statusCode() == 200) {
                JsonObject jsonBase = JsonParser.parseString(res1.body()).getAsJsonObject();
                JsonObject jsonSpec = JsonParser.parseString(res2.body()).getAsJsonObject();

                // Dados Base
                String nome = jsonBase.get("name").getAsString();
                String tipo = jsonBase.get("types").getAsJsonArray().get(0).getAsJsonObject().get("type").getAsJsonObject().get("name").getAsString();
                int hp = jsonBase.get("stats").getAsJsonArray().get(0).getAsJsonObject().get("base_stat").getAsInt();

                // Lógica de Raridade
                String raridade = "Comum";
                if (jsonSpec.get("is_legendary").getAsBoolean()) raridade = "Lendário";
                else if (jsonSpec.get("is_mythical").getAsBoolean()) raridade = "Mítico";

                // Bioma (Habitat)
                String bioma = "Desconhecido";
                if (!jsonSpec.get("habitat").isJsonNull()) {
                    bioma = jsonSpec.get("habitat").getAsJsonObject().get("name").getAsString();
                }

                // Descrição (Pega a primeira descrição em inglês e limpa o texto)
                String descricao = "Sem descrição disponível.";
                JsonArray textos = jsonSpec.get("flavor_text_entries").getAsJsonArray();
                for (int i = 0; i < textos.size(); i++) {
                    JsonObject entry = textos.get(i).getAsJsonObject();
                    if (entry.get("language").getAsJsonObject().get("name").getAsString().equals("en")) {
                        descricao = entry.get("flavor_text").getAsString().replace("\n", " ").replace("\f", " ");
                        break;
                    }
                }

                return new Pokemon(nome, tipo, hp, raridade, bioma, descricao);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return null;
    }
}