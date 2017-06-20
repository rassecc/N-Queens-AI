import java.util.*;

public class Main {
    private static Random rand = new Random();
    private static Scanner kb;


    public static void main(String[] args) {
        int choice;
        kb = new Scanner(System.in);

        do{
            System.out.println("\nEnter a choice");
            System.out.println("1) Run the Straight-forward steepest-ascent hill climbing algortihm");
            System.out.println("2) Run the genetic algorithm");
            System.out.println("3) Exit the program");
            System.out.print("\nYour choice: ");
            choice = kb.nextInt();

            switch (choice) {
                case 1:
                    doHC();
                    break;
                case 2:
                    doGentetic();
                    break;
                case 3:
                    choice = 3;
                    break;
                default: System.out.println("\nNothing valid was entered. Try again.");
            }
        } while (choice != 3);


    }

    
    private static void doHC(){
        int size;
        int[] board;
        int problems;
        boolean printSteps;
        double averageRunTime = 0;
        double averageMovesCount = 0;
        double averageSearchCost = 0;

        puzzleResult current;

        System.out.print("\nEnter size of board? : ");
        size = kb.nextInt();

        System.out.print("How many problems to create? : ");
        problems = kb.nextInt();

        System.out.print("Print paths? (true or false) : ");
        printSteps = kb.nextBoolean();

        if (printSteps) {
            int count = 0;
            while (count < problems) {
                board = makeBoard(size);
                current = hillClimb(board, printSteps);

                if (current.finalState.costOfBoard != 0) { continue; }

                System.out.println("\nBoard " + (count + 1) + " complete: ");
                System.out.println(current.finalState);
                System.out.println("\nAmount of moves to complete: " + current.movesCount);
                System.out.println("Number of nodes created for this problem: " + current.searchCost);
                System.out.println("Runtime: " + ((current.timeStarted - current.timeFinished) / 1000000) + " ms\n");
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

                count++;
            }
        }
        else {
            double count = 0;
            while (count < problems) {
                board = makeBoard(size);
                current = hillClimb(board, printSteps);

                if (current.finalState.costOfBoard != 0) { continue; }

                averageMovesCount += current.movesCount;
                averageSearchCost += current.searchCost;
                averageRunTime += (current.timeStarted - current.timeFinished);

                count++;
            }
            System.out.println("\nFinished " + problems + " games");
            System.out.println("Average amount of moves to complete: " + (double)(averageMovesCount / count));
            System.out.println("Average search cost of the boards: " + (double)(averageSearchCost / count));
            System.out.println("Average run time : " + (double)((averageRunTime / count) / 1000000) + " ms");
        }
    }


    private static void doGentetic() {
        int size;
        int choice;
        int problems;
        int initialPopu;
        puzzleResult board;
        double probOfMutation;
        double averageRunTime = 0;
        double averageMovesCount = 0;
        double averageSearchCost = 0;


        System.out.print("\nEnter size of board? : ");
        size = kb.nextInt();

        System.out.print("How many problems to create? : ");
        problems = kb.nextInt();

        System.out.print("How many items in initial population? :");
        initialPopu = kb.nextInt();

        System.out.print("Enter the mutatation chance (0.1 - 1.0) : ");
        probOfMutation = kb.nextDouble();

        System.out.print("Print solutions of each or get averages of all? (1 or 2) : ");
        choice = kb.nextInt();

        if (choice == 1) {
            for (int i = 0; i < problems; i++) {
                List<Node> firstStates = new ArrayList(initialPopu);

                for (int j = 0; j < initialPopu; j++) { firstStates.add(new Node(makeBoard(size))); }

                board = genetic(firstStates, probOfMutation);

                System.out.println("\nXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                System.out.println("\nBoard " + (i + 1) + " complete: ");
                System.out.print(board.finalState);
                System.out.print("\nNumber of nodes created for this problem: " + board.searchCost);
                System.out.print("\nRuntime: " + ((board.timeStarted - board.timeFinished) / 1000000) + " ms\n");
                System.out.println("Amount of moves to complete: " + board.movesCount);

            }
        } 
        else {
            for (int i = 0; i < problems; i++) {
                List<Node> firstStates = new ArrayList(initialPopu);

                for (int j = 0; j < initialPopu; j++) { firstStates.add(new Node(makeBoard(size))); }

                board = genetic(firstStates, probOfMutation);

                averageMovesCount += board.movesCount;
                averageRunTime += (board.timeStarted - board.timeFinished);
                averageSearchCost += board.searchCost;
            }

            System.out.println("Average search cost of the boards: " + (double)(averageSearchCost / problems));
            System.out.println("\nAverage amount of moves to complete: " + (double)(averageMovesCount / problems));
            System.out.println("Average run time : " + (double)((averageRunTime / problems) / 1000000) + " ms");
        }
    }


    //use the Straight-forward steepest-ascent hill climbing algorithm to solve 
    private static puzzleResult hillClimb(int[] initialState, boolean printSteps) {
        Node nod;
        int moves = 0;
        int searchCost = 0;
        List<Node> nodeNeighbors;
        Node current = new Node(initialState);
        double timeFinished = System.nanoTime();


        if (current.costOfBoard == 0) {
            System.out.println(current);
            return new puzzleResult(current, 0, 0, System.nanoTime());
        }

        while (true) {
            if (printSteps) {
                System.out.println("\nMove #" + moves);
                System.out.println(current);
            }

            nodeNeighbors = current.createSuccessors();
            searchCost += nodeNeighbors.size();
            nod = nodeNeighbors.get(0);

            if (current.costOfBoard <= nod.costOfBoard) { return new puzzleResult(current, moves, searchCost, timeFinished); }

            current = nod;
            moves++;
        }
    }

    //use the genetic algorithm that goes through generations to find the final state of each problem
    private static puzzleResult genetic(List<Node> firstState, double mutationChance) {
        int generations = 0;
        int searchCost = 0;
        double timeFinished = System.nanoTime();
        Node[] pairCrossover;
        Node bestFitness;
        List<Node> crossedOverNodes = new ArrayList(firstState.size());
        List<Node> fittestOfCurrentGen;
        List<Node> nextGen = firstState;

        while (true) {
            fittestOfCurrentGen = geneticHelper.chooseBest(nextGen);
            for (Node n : fittestOfCurrentGen) {
                if (n.costOfBoard == 0) { return new puzzleResult(n, generations, 0, timeFinished); }
            }

            crossedOverNodes.clear();
            bestFitness = fittestOfCurrentGen.get(0);

            for (int i = 1; i < fittestOfCurrentGen.size(); i++) {
                pairCrossover = geneticHelper.pairCrossover(bestFitness, fittestOfCurrentGen.get(i));
                crossedOverNodes.add(pairCrossover[0]);
                crossedOverNodes.add(pairCrossover[1]);
            }

            nextGen.clear();

            for (Node n : crossedOverNodes) { nextGen.add(geneticHelper.mutate(n, mutationChance)); }

            searchCost += nextGen.size();
            generations++;
        }
    }

    //methods needed for the genetic algorithm
    //chooseBest method decides which items are good for breeding
    private static class geneticHelper {
        static List<Node> chooseBest(List<Node> genePool) {
            List<Node> choice = new ArrayList<>();
            
            Collections.sort(genePool, (Node mate1, Node mate2) -> { return mate1.costOfBoard - mate2.costOfBoard; });
            
            for (int i = 0; i <= genePool.size()>>1; i++) { choice.add(genePool.get(i)); }

            return choice;
        }


        static Node mutate(Node nod, double chance) {
            if (chance < rand.nextDouble()) { return nod; }

            int[] mutations = Arrays.copyOf(nod.gameBoard, nod.gameBoard.length);
            int position = rand.nextInt(mutations.length);
            mutations[position] = rand.nextInt(mutations.length);
            return new Node(mutations);
        }

        //crosses over 2 nodes
        static Node[] pairCrossover(Node mate1, Node mate2) {
            int size = mate1.gameBoard.length;
            int[] temp = new int[size];
            int[] temp2 = new int[size];
            int pairCrossover = rand.nextInt(size-1);

            System.arraycopy(mate1.gameBoard, 0, temp, 0, pairCrossover);
            System.arraycopy(mate2.gameBoard, pairCrossover, temp, pairCrossover, size - pairCrossover);
            Node mate12 = new Node(temp);

            System.arraycopy(mate2.gameBoard, 0, temp2, 0, pairCrossover);
            System.arraycopy(mate1.gameBoard, pairCrossover, temp2, pairCrossover, size - pairCrossover);
            Node mate21 = new Node(temp2);

            return new Node[] { mate12, mate21 };
        }
    }
    
    
    private static class Node {
        int gameBoard[];
        int costOfBoard;
        
        Node(int[] board)           { this(board, countAttacking(board)); }
        Node(int[] board, int cost) { this.gameBoard = board; this.costOfBoard = cost; }


        List<Node> createSuccessors() {
            int cost;
            int oldValue;
            int[] boardCopy;
            int[] board = Arrays.copyOf(gameBoard, gameBoard.length);
            List<Node> successors = new ArrayList<>((gameBoard.length * gameBoard.length) - gameBoard.length);
            
            for (int i = 0; i < board.length; i++) {
                oldValue = board[i];
                for (int j = 0; j < board.length; j++) {
                    if (j == oldValue) { continue; }

                    board[i] = j;
                    cost = countAttacking(board);
                    boardCopy = Arrays.copyOf(board, board.length);
                    successors.add(new Node(boardCopy, cost));
                }

                board[i] = oldValue;
            }

            Collections.sort(successors, (Node nod1, Node nod2) -> { return nod1.costOfBoard - nod2.costOfBoard; });
            return successors;
        }


        static int countAttackers(int[] gameBoard, int position) {
            int count = 0;
            int curr = gameBoard[position];
            
            for (int j = 0; j < position; j++) {
                if (gameBoard[j] == curr)         { count++; }

                if (gameBoard[j] - curr == position - j) { count++; }

                if (curr - gameBoard[j] == position - j) { count++; }
            }
            return count;
        }


        static int countAttacking(int[] gameBoard) {
            int count = 0;
            
            for (int i = 0; i < gameBoard.length; i++) { count += countAttackers(gameBoard, i); }
            return count;
        }


        public String toString() {
            StringBuilder maze = new StringBuilder();

            for (int i : gameBoard) {
                for (int j = 0; j < gameBoard.length; j++) {

                    if (i == j) {
                        maze.append('Q');
                    } else {
                        maze.append('.');
                    }
                    maze.append(' ');
                }
                maze.append('\n');
            }
            maze.deleteCharAt(maze.length() - 1);
            return maze.toString();
        }
    }

    //hold all result info about puzzle completion
    private static final class puzzleResult {
        int movesCount;
        int searchCost;
        double timeFinished;
        double timeStarted;
        Node finalState;

        puzzleResult(Node terminalState, int numMoves, int searchCost, double timeFinished) {
            this.movesCount = numMoves;
            this.searchCost = searchCost;
            this.timeFinished = timeFinished;
            this.timeStarted = System.nanoTime();
            
            this.finalState = terminalState;
        }
    }


    private static int[] makeBoard(int size) {
        int[] gameBoard = new int[size];

        for (int i = 0; i < gameBoard.length; i++) { gameBoard[i] = rand.nextInt(gameBoard.length); }

        return gameBoard;
    }
}
