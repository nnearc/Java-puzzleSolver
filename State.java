package hw1;

import java.util.Arrays;

public class State implements Comparable{
    private int[][] puzzle;
    private int heuristic;
    private int steps;
    private int row;
    private int column;
    private String lastmove="";
    private State previousState;

    public State(int dimension,int steps){
        puzzle=new int[dimension][dimension];
        this.steps=steps;
    }

    public State getPreviousState() {
        return previousState;
    }

    public void setPreviousState(State previousState) {
        this.previousState = previousState;
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
        for(int i=0;i<puzzle.length;i++){
            for(int j=0;j<puzzle.length;j++){
                if (puzzle[i][j] == 0) {
                    row=i;
                    column=j;
                }
            }
        }
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void manhatanDistance(){
        for (int i=0;i<puzzle.length;i++){
            for(int j=0;j<puzzle.length;j++){
                if(puzzle[i][j]==0){
                    continue;
                }
                heuristic+=Math.abs(i-(puzzle[i][j]-1)/puzzle.length)+Math.abs(j-(puzzle[i][j]-1)%puzzle.length);
            }
        }
    }
    
    public void eucildianDistance(){
        for (int i=0;i<puzzle.length;i++){
            for(int j=0;j<puzzle.length;j++){
                if(puzzle[i][j]==0){
                    continue;
                }
                heuristic+=Math.sqrt(Math.pow(i-(puzzle[i][j]-1)/puzzle.length,2)+Math.pow(j-(puzzle[i][j]-1)%puzzle.length,2));
            }
        }
    }

    public State moveUp(){
        State mvUp=new State(puzzle.length,this.steps);
        int[][] newPuzl=arrayCopy();
        newPuzl[row][column]=newPuzl[row-1][column];
        newPuzl[row-1][column]=0;
        mvUp.row=row-1;
        mvUp.setPuzzle(newPuzl);
        mvUp.setSteps(steps+1);
        mvUp.lastmove="Move Up";
        mvUp.setPreviousState(this);
        return mvUp;
    }

    public State moveDown(){
        State mvDn=new State(puzzle.length,this.steps);
        int[][] newPuzl=arrayCopy();
        newPuzl[row][column]=newPuzl[row+1][column];
        newPuzl[row+1][column]=0;
        mvDn.row=row+1;
        mvDn.setPuzzle(newPuzl);
        mvDn.setSteps(steps+1);
        mvDn.lastmove="Move Down";
        mvDn.setPreviousState(this);
        return mvDn;
    }

    public State moveRight(){
        State mvRg=new State(puzzle.length,this.steps);
        int[][] newPuzl=arrayCopy();
        newPuzl[row][column]=newPuzl[row][column+1];
        newPuzl[row][column+1]=0;
        mvRg.column=column+1;
        mvRg.setPuzzle(newPuzl);
        mvRg.setSteps(steps+1);
        mvRg.lastmove="Move Right";
        mvRg.setPreviousState(this);
        return mvRg;
    }

    public State moveLeft(){
        State mvLf=new State(puzzle.length,this.steps);
        int[][] newPuzl=arrayCopy();
        newPuzl[row][column]=newPuzl[row][column-1];
        newPuzl[row][column-1]=0;
        mvLf.column=column-1;
        mvLf.setPuzzle(newPuzl);
        mvLf.setSteps(steps+1);
        mvLf.lastmove="Move Left";
        mvLf.setPreviousState(this);
        return mvLf;
    }

    private int[][] arrayCopy(){
        int[][] newPuzl=new int[puzzle.length][puzzle.length];
        for(int i=0;i<puzzle.length;i++){
            System.arraycopy(puzzle[i],0,newPuzl[i],0,puzzle.length);
        }
        return newPuzl;
    }

    public boolean[] validMoves(){
        boolean[] validmoves=new boolean[4];
        if(row!=0){
            validmoves[0]=true;
        }
        if(row!=puzzle.length-1){
            validmoves[1]=true;
        }
        if(column!=puzzle.length-1){
            validmoves[2]=true;
        }
        if(column!=0){
            validmoves[3]=true;
        }
        return validmoves;
    }

    @Override
    public int compareTo(Object o) {
        State newState=(State) o;
        return Integer.compare(heuristic+steps,newState.getHeuristic()+newState.getSteps());
//        if((heuristic+steps)>(newState.getHeuristic()+newState.getSteps())){
//            return 1;
//        }else if((heuristic+steps)==(newState.getHeuristic()+newState.getSteps())){
//            return 0;
//        }
//        return -1;

    }

    public boolean equals(Object object) {
        if(object==null){
            return false;
        }
        State state = (State) object;
        int[][] sPuzzle=state.getPuzzle();
        for(int i=0;i<puzzle.length;i++){
            for(int j=0;j<puzzle.length;j++){
                if(puzzle[i][j]!=sPuzzle[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSolution() {
        int c=1;
        for(int i=0;i<puzzle.length;i++) {
            for (int j = 0; j < puzzle.length; j++) {
                if((i==puzzle.length-1) && (j==puzzle.length-1)){
                    System.out.print("OUT");
                    break;
                }
                if(puzzle[i][j]!=c){
                    return false;
                }
                c++;
            }
        }
        return true;
    }

    public String getLastmove() {
        return lastmove;
    }
    public static State random(int dimension){
        State newState= new State(dimension,0);
        int table [][]= new int [dimension][dimension];
        int c=1;
        for(int i=0;i<dimension;i++) {
            for (int j = 0; j < dimension; j++) {
                table[i][j]=c;
                c++;
            }
        }
        table[dimension-1][dimension-1]=0;
        newState.setPuzzle(table);
        for(int i=0;i<50;i++){
            boolean validMoves[]=newState.validMoves();
            int random=(int)(Math.random()*4);
            if(validMoves[random]){
                if(random==0){
                    newState=newState.moveUp();
                }
                else if(random==1){
                    newState=newState.moveDown();
                }
                else if(random==2){
                    newState=newState.moveRight();
                }
                else if(random==3){
                    newState=newState.moveLeft();
                }
            }

        }
        newState.setSteps(0);
        newState.previousState=null;
        return newState;
    }
}
