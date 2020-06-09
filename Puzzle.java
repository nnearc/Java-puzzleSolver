package hw1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Puzzle {

    private static ArrayList<State> fringe = new ArrayList<>();
    private static int allStates=0;


    private static void printPuzzle(State currState){
        System.out.println(currState.getLastmove());
        if(currState.getPuzzle().length==3){
            System.out.println("+-----+-----+-----+");
        }else {
            System.out.println("+------------------------+");
        }

        for(int i=0;i<currState.getPuzzle().length;i++){
            for(int j=0;j<currState.getPuzzle().length;j++){
                System.out.print("|  "+currState.getPuzzle()[i][j]+"  ");
            }
            System.out.println("|");
            if(currState.getPuzzle().length==3){
                System.out.println("+-----+-----+-----+");
            }else {
                System.out.println("+------------------------+");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner;
        System.out.println("Give a filename :");
        scanner=new Scanner(System.in);
        String fileName=scanner.nextLine();
        System.out.println("Choose a heuristic");
        System.out.println("1) Manhattan");
        System.out.println("2) Euclidean");
        int choice=scanner.nextInt();

        File file=new File(fileName);
        try {
            scanner = new Scanner(file);
            int dimension=scanner.nextInt();
            int[][] puzzle=new int[dimension][dimension];
            for(int i=0;i<puzzle.length;i++) {
                for (int j = 0; j < puzzle.length; j++) {
                    puzzle[i][j]=scanner.nextInt();
                }
            }
            State firstState=new State(dimension,0);
            if(!isSolvable(puzzle)){
                System.out.println("There is no solution ");     
                System.exit(0);
            }
            firstState.setPuzzle(puzzle);
            fringe.add(firstState);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

//        fringe.add(State.random(3));
//        int choice=3;
        switch (choice){
            case 1: fringe.get(0).manhatanDistance();
                    break;
            case 2: fringe.get(0).eucildianDistance();
            break;
        }
        State current;
        long startTime=System.currentTimeMillis();
        int numberOfexposed=0;
        while(true){
            Collections.sort(fringe);
            current= fringe.remove(0);
            if(current.isSolution()){
                break;
            }
            boolean[] availableMoves=current.validMoves();
            int c=0;
            if(availableMoves[0]){
                State state=current.moveUp();
                if(!state.equals(current.getPreviousState())){
                    fringe.add(state);
                    c++;
                }
            }
            if(availableMoves[1]){
                State state=current.moveDown();
                if(!state.equals(current.getPreviousState())){
                    fringe.add(state);
                    c++;
                }
            }
            if(availableMoves[2]){
                State state=current.moveRight();
                if(!state.equals(current.getPreviousState())){
                    fringe.add(state);
                    c++;
                }
            }
            if(availableMoves[3]){
                State state=current.moveLeft();
                if(!state.equals(current.getPreviousState())){
                    fringe.add(state);
                    c++;
                }
            }
            for(int i=0;i<c;i++){
                switch (choice){
                    case 1: fringe.get(fringe.size()-i-1).manhatanDistance();
                    break;
                    case 2: fringe.get(fringe.size()-i-1).eucildianDistance();
                    	break;
                }
            }
            if(fringe.isEmpty()){
                current=null;
                break;
            }
            allStates+=c;
            numberOfexposed+=1;
        }
        if(current!=null){
            printSolution(current);
        }
        else{
            System.out.println("There is no solution");
        }
        long endTime=System.currentTimeMillis();
        System.out.println("Time to finish: "+(endTime-startTime));
        System.out.println("Objects created: "+allStates);
        System.out.println("Objects exposed: "+numberOfexposed);

    }


    private static void printSolution(State current){
        if(current==null){
            return;
        }
        else{
            printSolution(current.getPreviousState());
            printPuzzle(current);
        }
    }



    private static int getInvCount(int[][] puzzle) {
    int N=puzzle.length;
    int temp [] = new int [N*N];
    int k=0;
    for(int i=0;i<N;i++)
    	for(int j=0;j<N;j++) {
    		temp[k]=puzzle[i][j];
    		k++;
    	}
    int inv_count = 0;
  /*  for (int i = 0; i < N - 1; i++) {
        for (int j = i + 1; j < N; j++) {
            // Value 0 is used for empty space 
            if (puzzle[j][i] > 0 && puzzle[j][i] > puzzle[i][j]) 
                inv_count++; 
            
        }
        
    }*/
    for (int i = 0; i < temp.length; i++)
    	 for (int j = i + 1; j < temp.length; j++)
    		 if (temp[i] > temp[j] && temp[j] != 0)
             inv_count++;    		 
    return inv_count; 
    }

    // find Position of blank from bottom
    private static int findXPosition(int[][] puzzle) {
        int N=puzzle.length;
        // start from bottom-right corner of matrix
        for (int i = N - 1; i >= 0; i--)
            for (int j = N - 1; j >= 0; j--)
                if (puzzle[i][j] == 0)
                    return N - i;
        return 0;
    }


    private static boolean  isSolvable(int[][] puzzle) {
        int N=puzzle.length;
        // Count inversions in given puzzle
        int invCount = getInvCount(puzzle);

        // If grid is odd, return true if inversion
        // count is even.
        if (N % 2 == 1)
            return (invCount % 2 == 0);
        else     // grid is even
        {
            int pos = findXPosition(puzzle);
            if (pos % 2 == 1)
                return (invCount % 2 == 0);
            else
                return (invCount % 2 == 1);
        }
    }
}
