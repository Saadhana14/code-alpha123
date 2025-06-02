/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/

import java.io.*;
import java.util.*;

// Stock Class
class Stock {
    String symbol;
    String name;
    double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public void updatePrice(double newPrice) {
        this.price = newPrice;
    }

    public String toString() {
        return symbol + " - " + name + " - $" + price;
    }
}

// Transaction Class
class Transaction {
    String stockSymbol;
    int quantity;
    double price;
    String type; // BUY or SELL

    public Transaction(String symbol, int qty, double price, String type) {
        this.stockSymbol = symbol;
        this.quantity = qty;
        this.price = price;
        this.type = type;
    }

    public String toString() {
        return type + ": " + quantity + " of " + stockSymbol + " at $" + price;
    }
}

// Portfolio Class
class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    List<Transaction> history = new ArrayList<>();
    double cash = 10000.0; // starting cash

    public void buyStock(Stock stock, int qty) {
        double totalCost = stock.price * qty;
        if (cash >= totalCost) {
            holdings.put(stock.symbol, holdings.getOrDefault(stock.symbol, 0) + qty);
            history.add(new Transaction(stock.symbol, qty, stock.price, "BUY"));
            cash -= totalCost;
            System.out.println("Bought " + qty + " shares of " + stock.symbol);
        } else {
            System.out.println("Not enough cash to buy!");
        }
    }

    public void sellStock(Stock stock, int qty) {
        if (holdings.getOrDefault(stock.symbol, 0) >= qty) {
            holdings.put(stock.symbol, holdings.get(stock.symbol) - qty);
            history.add(new Transaction(stock.symbol, qty, stock.price, "SELL"));
            cash += stock.price * qty;
            System.out.println("Sold " + qty + " shares of " + stock.symbol);
        } else {
            System.out.println("Not enough shares to sell!");
        }
    }

    public void displayPortfolio(Map<String, Stock> market) {
        System.out.println("Cash: $" + cash);
        System.out.println("Holdings:");
        for (String symbol : holdings.keySet()) {
            int qty = holdings.get(symbol);
            double value = market.get(symbol).price * qty;
            System.out.println(symbol + ": " + qty + " shares - Value: $" + value);
        }
    }

    public void showHistory() {
        System.out.println("Transaction History:");
        for (Transaction t : history) {
            System.out.println(t);
        }
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(new ArrayList<>(history));
            System.out.println("Portfolio saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// User Class
class User {
    String name;
    Portfolio portfolio;

    public User(String name) {
        this.name = name;
        this.portfolio = new Portfolio();
    }
}

// Main Simulation
public class StockTradingPlatform {
    static Map<String, Stock> market = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeMarket();

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        User user = new User(name);

        while (true) {
            System.out.println("\n--- Stock Trading Platform ---");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transaction History");
            System.out.println("6. Save Portfolio");
            System.out.println("0. Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> displayMarket();
                case 2 -> buyFlow(user);
                case 3 -> sellFlow(user);
                case 4 -> user.portfolio.displayPortfolio(market);
                case 5 -> user.portfolio.showHistory();
                case 6 -> user.portfolio.saveToFile("portfolio.ser");
                case 0 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void initializeMarket() {
        market.put("AAPL", new Stock("AAPL", "Apple Inc.", 150.0));
        market.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 2800.0));
        market.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 3500.0));
        market.put("TSLA", new Stock("TSLA", "Tesla Inc.", 700.0));
    }

    static void displayMarket() {
        System.out.println("\nMarket Stocks:");
        for (Stock stock : market.values()) {
            System.out.println(stock);
        }
    }

    static void buyFlow(User user) {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.next().toUpperCase();
        System.out.print("Enter quantity: ");
        int qty = scanner.nextInt();

        if (market.containsKey(symbol)) {
            user.portfolio.buyStock(market.get(symbol), qty);
        } else {
            System.out.println("Stock not found.");
        }
    }

    static void sellFlow(User user) {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.next().toUpperCase();
        System.out.print("Enter quantity: ");
        int qty = scanner.nextInt();

        if (market.containsKey(symbol)) {
            user.portfolio.sellStock(market.get(symbol), qty);
        } else {
            System.out.println("Stock not found.");
        }
    }
}