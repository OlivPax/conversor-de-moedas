import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {
    private static final String API_KEY = "f559ce01f6d9279229147eff";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Conversor de Moedas ===");
            System.out.println("1. USD → BRL");
            System.out.println("2. BRL → USD");
            System.out.println("3. EUR → BRL");
            System.out.println("4. BRL → EUR");
            System.out.println("5. USD → EUR");
            System.out.println("6. EUR → USD");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int option = scanner.nextInt();

            if (option == 0) {
                running = false;
                System.out.println("Encerrando o programa...");
                break;
            }

            System.out.print("Digite o valor a ser convertido: ");
            double amount = scanner.nextDouble();

            String fromCurrency = "", toCurrency = "";

            switch (option) {
                case 1 -> { fromCurrency = "USD"; toCurrency = "BRL"; }
                case 2 -> { fromCurrency = "BRL"; toCurrency = "USD"; }
                case 3 -> { fromCurrency = "EUR"; toCurrency = "BRL"; }
                case 4 -> { fromCurrency = "BRL"; toCurrency = "EUR"; }
                case 5 -> { fromCurrency = "USD"; toCurrency = "EUR"; }
                case 6 -> { fromCurrency = "EUR"; toCurrency = "USD"; }
                default -> {
                    System.out.println("Opção inválida.");
                    continue;
                }
            }

            double rate = getExchangeRate(fromCurrency, toCurrency);
            if (rate != -1) {
                double converted = amount * rate;
                System.out.printf("💱 %.2f %s equivalem a %.2f %s%n", amount, fromCurrency, converted, toCurrency);
            } else {
                System.out.println("Erro ao obter taxa de câmbio.");
            }
        }

        scanner.close();
    }

    private static double getExchangeRate(String from, String to) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + from))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject conversionRates = json.getAsJsonObject("conversion_rates");

            return conversionRates.get(to).getAsDouble();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return -1;
        }
    }
}