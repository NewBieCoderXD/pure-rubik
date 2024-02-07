package com.ggFROOK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class RubikFROOK {
  private final int[][] directions = new int[][]{
    {1, 3, 4, 2},
    {3, 0, 2, 5},
    {1, 0, 4, 5},
    {4, 0, 1, 5},
    {2, 0, 3, 5},
    {1, 2, 4, 3}
  };
  private final String[] faceToNotation = new String[]{
    "U",
    "F",
    "R",
    "L",
    "B"
  };
  private int[][] rubik;
  public int[][] getRubik(){
      return rubik;
  }

  public void initRubik(){
      rubik = new int[6][8];
      //generate rubik
      for(int i=0;i<6;i++){
          rubik[i]=new int[8];
          Arrays.fill(rubik[i],i);
      }
  }
  
  private int findIndex(int[] list, int value) {
    for (int i = 0; i < list.length; i++) {
      if (list[i] == value) {
        return i;
      }
    }
    return 5;
  }

  private int[][] createClonedRubik(){
    int[][] newRubik = new int[6][8];
    for(int i=0;i<6;i++){
      System.arraycopy(rubik[i], 0, newRubik[i], 0, 8);
    }
    return newRubik;
  }

  private boolean detailPrintOption=false;
  public boolean countMovesOption=false;
  
  private void rotateClockwise(int face,int direction){
    int[][] newRubik = createClonedRubik();
    int lateralSide = directions[face][(direction+1+4)%4];
    for(int i=0;i<4;i++){
      int side = directions[lateralSide][i];
      int nextSide = directions[lateralSide][(i+1)%4];
      for(int j=0;j<3;j++){
        newRubik[nextSide][((findIndex(directions[nextSide],side)-1)*2+j+8)%8]=rubik[side][((findIndex(directions[side],nextSide)+1)*2+j+8)%8];
      }
    }
    for(int i=0;i<8;i++){
      newRubik[lateralSide][i]=rubik[lateralSide][(i+6)%8];
    }
    rubik=newRubik;
  }
  
  private void rotateAntiClockwise(int face,int direction){
    int[][] newRubik = createClonedRubik();
    int lateralSide = directions[face][(direction-1+4)%4];
    for(int i=0;i<4;i++){
      int side = directions[lateralSide][i];
      int nextSide = directions[lateralSide][(i+1)%4];
      for(int j=0;j<3;j++){
        newRubik[nextSide][((findIndex(directions[nextSide],side)+1)*2+j+8)%8]=rubik[side][((findIndex(directions[side],nextSide)-1)*2+j+8)%8];
      }
    }
    for(int i=0;i<8;i++){
      newRubik[5-lateralSide][i]=rubik[5-lateralSide][(i+2)%8];
    }
    rubik=newRubik;
  }
  
  //direction is top side
  private void addToSolution(String s){
    solution.add(s);
  }

  private boolean IsClockwiseNotation(String notation){
    return notation.length()==1;
  }

  public void throwError(String message){
    System.err.println("Error: "+message);
    System.exit(-1);
  }

  private Map<String, Map.Entry<Integer,Integer>> faceAndDirOfNotation=Map.of(
    "R", Map.entry(1,1),
    "L", Map.entry(1,-1),
    "U", Map.entry(1,0),
    "D", Map.entry(1,2),
    "F", Map.entry(3,1),
    "B", Map.entry(3,3)
  );

  public void call(String methodName) throws InvalidRubikNotation{
    call(methodName,false);
  }
  public void call(String methodName, boolean IsScramble) throws InvalidRubikNotation{ //default value is false
    String errorMessage = String.format("'%s' is not valid notation",methodName);
    if(IsClockwiseNotation(methodName)){
      Map.Entry<Integer,Integer> faceAndDir=faceAndDirOfNotation.get(methodName);
      if(faceAndDir==null){
        throw new InvalidRubikNotation(errorMessage);
      }
      rotateClockwise(faceAndDir.getKey(), faceAndDir.getValue());
    }
    else{
      if(methodName.charAt(1)!='_'&&methodName.charAt(1)!='\''){
        throwError(errorMessage);
      }
      // change R_ to R'
      methodName=methodName.charAt(0)+"'";
      Map.Entry<Integer,Integer> faceAndDir=faceAndDirOfNotation.get(methodName.charAt(0)+"");
      if(faceAndDir==null){
        throwError(errorMessage);
      }
      rotateAntiClockwise(faceAndDir.getKey(), faceAndDir.getValue());
    }
    if(!IsScramble){
      addToSolution(methodName);
    }
    unimportantPrint(methodName);
    if(detailPrintOption){
      printRubik(rubik);
    }
  }

  private void rotateTo(int side,int targetSide,String action) throws InvalidRubikNotation{
    int turns=side-targetSide;
    if(turns==0){
      return;
    }
    if(Math.abs(turns)>2){
      call(action+"_");
      return;
    }
    for(int t=0;t<Math.abs(turns);t++){
      if(turns>0){
        call(action);
      }
      else{
        call(action+"_");
      }
    }
  }

  private void cross() throws InvalidRubikNotation{
    for(int i=0;i<6;i++){
      for(int j=1;j<8;j+=2){
        if(rubik[i][j]==5){
          int direction = Math.round((j-1)/2);
          int latSide = directions[i][direction];
          int latDirection = findIndex(directions[latSide],i);
          int color = rubik[latSide][(latDirection*2+1)%8];
          if(i!=5||latSide!=color){
            // System.out.println("i "+i+" latSide "+latSide+" Color "+color+" direction "+direction);
            if(i==5){
              call(faceToNotation[latSide]);
              call(faceToNotation[latSide]);
              rotateTo(findIndex(directions[0],color),findIndex(directions[0],latSide),"U");
              continue;
            }
            if(i==0){
              rotateTo(findIndex(directions[0],color),findIndex(directions[0],latSide),"U");
              call(faceToNotation[color]);call(faceToNotation[color]);
              continue;
            }
            if(color==latSide){
              call(faceToNotation[latSide]+((direction>1)?"_":""));
              continue;
            }
            if(color==5-latSide){
              call(faceToNotation[i]);
              call(faceToNotation[i]);
              call(faceToNotation[color]+((direction>1)?"":"_"));
              continue;
            }
            if(direction==1||direction==3){
              if(findIndex(directions[i],color)<5){
                rotateTo(findIndex(directions[i],color),direction,faceToNotation[i]);
                call(faceToNotation[color]+((direction>2)?"_":""));
                continue;
              }
              if(color==i){
                call("U_");
                call(faceToNotation[directions[5][(findIndex(directions[5],color)+1)%4]]+"_");
                call(faceToNotation[color]);
                call(faceToNotation[directions[5][(findIndex(directions[5],color)+1)%4]]);
                continue;
              }
              call(faceToNotation[color]);
              call(faceToNotation[color]);
              call("U_");
              call(faceToNotation[directions[5][(findIndex(directions[5],color)+5)%4]]+"_");
              call(faceToNotation[color]);
              call(faceToNotation[directions[5][(findIndex(directions[5],color)+5)%4]]);
              continue;
            }
            //direction = 0,2
            call(faceToNotation[latSide]+((direction>1)?"":"_"));
            unimportantPrint(findIndex(directions[0],color)+" "+findIndex(directions[0],latSide));
            rotateTo(findIndex(directions[0],color)+((color==1?4:0)),findIndex(directions[0],latSide),"U");
            call(faceToNotation[latSide]+((direction<1)?"":"_"));
            call(faceToNotation[color]);call(faceToNotation[color]);
          }
        }
      }
    }
  }

  private void corner() throws InvalidRubikNotation{
    for(int i=1;i<5;i++){
      for(int j=0;j<7;j+=2){
        if(rubik[i][j]==5){
          int latSide = directions[5][(findIndex(directions[5],i)+((j>3)?1:-1)+4)%4];
          int color = rubik[latSide][6-j];
          unimportantPrint("Corner found. lat side: "+latSide+" color: "+color+" j: "+j);
          if(j==0||j==6){
            call(faceToNotation[i]+((j<3)?"":"_"));
            call("U"+((j<3)?"":"_"));
            call(faceToNotation[i]+((j<3)?"_":""));
            call("U"+((j<3)?"_":""));
            return;
          }
          else{
            int newSide = directions[5][(findIndex(directions[5],color)+((j>3)?-1:1)+4)%4];
            rotateTo(findIndex(directions[5],latSide),findIndex(directions[5],color),"U");
            call(faceToNotation[newSide]+((j>3)?"_":""));
            call("U"+((j>3)?"_":""));
            call(faceToNotation[newSide]+((j>3)?"":"_"));
            return;
          }
        }
      }
    }
    if(checkCorner()){
      return;
    }
    //yellow side
    for(int j=0;j<8;j+=2){
      if(rubik[0][j]==5){
        unimportantPrint("yellow "+j);
        int side = (new int[]{3,1,2,4})[Math.round(j/2)];
        for(int k=0;k<8;k++){
          if(rubik[5][k]!=5){
            rotateTo(Math.round((10-k)%8/2),Math.round(j/2),"U");
            side = (new int[]{3,1,2,4})[Math.round((10-k)%8/2)];
            break;
          }
        }
        call(faceToNotation[side]);
        call("U");call("U");
        call(faceToNotation[side]+"_");
      }
    }
    for(int j=0;j<8;j++){
      if(rubik[5][j]!=5){
        return;
      }
    }
    for(int side=1;side<5;side++){
      if(rubik[side][2]!=side){
        String turn = faceToNotation[rubik[5-side][2]];
        call(turn);
        call("U");
        call(turn+"_");
        return;
      }
    }
  }
  
  private void edge() throws InvalidRubikNotation{
    boolean hasEdge = false;
    for(int j=1;j<8;j+=2){
      if(rubik[0][j]!=0){
        int latSide = directions[0][Math.round((j-1)/2)];
        if(rubik[latSide][3]!=0){
          hasEdge = true;
          int top=rubik[0][j];
          int bottom=rubik[latSide][3];
          unimportantPrint("Edge found. latSide: "+latSide+" top: "+top+" bottom: "+bottom+" "+findIndex(directions[5],top)+" "+findIndex(directions[5],bottom));
          if(directions[5][(findIndex(directions[5],top)+2)%4]!=latSide){
            rotateTo(findIndex(directions[0],5-top),findIndex(directions[0],latSide),"U");
          }
          if(findIndex(directions[5],top)<findIndex(directions[5],bottom)&&(top!=1||bottom!=3)||(top==3&&bottom==1)){
            call(faceToNotation[top]+"_");
            call("U_");
            call(faceToNotation[top]);
            call("U");
            call(faceToNotation[bottom]);
            call("U");
            call(faceToNotation[bottom]+"_");
          }
          else{
            call(faceToNotation[top]);
            call("U");
            call(faceToNotation[top]+"_");
            call("U_");
            call(faceToNotation[bottom]+"_");
            call("U_");
            call(faceToNotation[bottom]);
          }
          
        }
      }
    }
    if(!hasEdge){
      unimportantPrint("no edges");
      for(int side=1;side<5;side++){
        int color = rubik[side][1];
        if(color!=0&&color!=side){
          int latSide = directions[5][(findIndex(directions[5],side)+3)%4];
          int latColor = rubik[latSide][5];
          if(latColor!=0&&(color!=side||latSide!=latColor)){
            unimportantPrint("wrong edge. side: "+side+" latSide: "+latSide+" color: "+color+" latColor: "+latColor);
            call(faceToNotation[side]);
            call("U");
            call(faceToNotation[side]+"_");
            call("U_");
            call(faceToNotation[latSide]+"_");
            call("U_");
            call(faceToNotation[latSide]);
          }
        }
      }
    }
  }

  private void OLL() throws InvalidRubikNotation{
    // System.out.println("OLL");
    ArrayList<Integer> yellowEdgeList = new ArrayList<Integer>();
    ArrayList<Integer> yellowCornerList = new ArrayList<Integer>();
    for(int j=1;j<8;j+=2){
      if(rubik[0][j]==0){
        yellowEdgeList.add(Math.round(j/2));
      }
    }
    if(yellowEdgeList.size()==0){
      call("F");
      call("R");
      call("U");
      call("R_");
      call("U_");
      call("F_");
      return;
    }
    
    if(yellowEdgeList.size()==2){
      if(yellowEdgeList.get(0)+yellowEdgeList.get(1)==5){ //Line
        int side = directions[0][(yellowEdgeList.get(0)-1+4)%4];
        int latSide = 5-directions[0][yellowEdgeList.get(0)];
        call(faceToNotation[side]);
        call("U");
        call(faceToNotation[latSide]);
        call("U_");
        call(faceToNotation[latSide]+"_");
        call(faceToNotation[side]+"_");
      }
      else{
        int maxDirection = findIndex(directions[0],Math.max(yellowEdgeList.get(0),yellowEdgeList.get(1)));
        int leftSide = directions[0][(maxDirection+1)%4];
        int side = directions[0][maxDirection];
        call(faceToNotation[leftSide]);
        call("U");
        call(faceToNotation[side]);
        call("U_");
        call(faceToNotation[side]+"_");
        call(faceToNotation[leftSide]+"_");
      }
      return;
    }
    
    // 4 yellows
    for(int j=0;j<7;j+=2){
      if(rubik[0][j]==0){
        yellowCornerList.add(j);
      }
    }
    if(yellowCornerList.size()==0){
      for(int side=1;side<5;side++){
        if(rubik[side][2]==0&&rubik[side][4]==0){
          // int direction = findIndex(directions[0],side);
          if(rubik[5-side][2]==0&&rubik[5-side][4]==0){
            call(faceToNotation[side]);
            call("U");
            call(faceToNotation[side]+"_");
            call("U");
            call(faceToNotation[side]);
            call("U_");
            call(faceToNotation[side]+"_");
            call("U");
            call(faceToNotation[side]);
            call("U");call("U");
            call(faceToNotation[side]+"_");
            return;
          }
          else{
            int oppositeSide = 5-side;
            String oppositeFace=faceToNotation[oppositeSide];
            call(oppositeFace);
            call("U");call("U");
            call(oppositeFace);
            call(oppositeFace);
            call("U_");
            call(oppositeFace);
            call(oppositeFace);
            call("U_");
            call(oppositeFace);
            call(oppositeFace);
            call("U");call("U");
            call(oppositeFace);
          }
        }
      }
    }
    if(yellowCornerList.size()==1){ //ปลาตะเพียน
      int side = directions[0][(Math.round(yellowCornerList.get(0)/2)-1+4)%4]; //right side
      //System.out.println(side);
      if(rubik[side][4]==0){
        int latSide = directions[0][(Math.round(yellowCornerList.get(0)/2)+2)%4];
        call(faceToNotation[latSide]);
        call("U");
        call(faceToNotation[latSide]+"_");
        call("U");
        call(faceToNotation[latSide]);
        call("U");call("U");
        call(faceToNotation[latSide]+"_");
        return;
      }
      int latSide = directions[0][(Math.round(yellowCornerList.get(0)/2)+1)%4];
      call(faceToNotation[latSide]+"_");
      call("U_");
      call(faceToNotation[latSide]);
      call("U_");
      call(faceToNotation[latSide]+"_");
      call("U");call("U");
      call(faceToNotation[latSide]);
      return;
    }
    if(yellowCornerList.size()==2){
      int direction1 = Math.round(yellowCornerList.get(0)/2);
      int direction2 = Math.round(yellowCornerList.get(1)/2);
      if(Math.abs(direction1-direction2)==2){
        int side = directions[0][(direction1+2)%4];
        int rightSide = directions[0][(direction1+1)%4];
        int leftSide = directions[0][(direction1+3)%4];
        if(rubik[side][2]!=0){
          side = directions[0][(direction2+2)%4];
          rightSide = directions[0][(direction2+1)%4];
          leftSide = directions[0][(direction2+3)%4];
        }
        String rightFace=faceToNotation[rightSide];
        call(faceToNotation[side]);
        call(rightFace+"_");
        call(faceToNotation[side]+"_");
        call(faceToNotation[leftSide]);
        call(faceToNotation[side]);
        call(rightFace);
        call(faceToNotation[side]+"_");
        call(faceToNotation[leftSide]+"_");
        return;
      }
      //รถถัง
      int side = directions[0][(4-Math.min(yellowCornerList.get(0),yellowCornerList.get(1)))%4];
      int oppositeSide = 5-side;
      int rightSide = directions[0][(4-Math.min(yellowCornerList.get(0),yellowCornerList.get(1))+3)%4];
      String oppositeFace=faceToNotation[oppositeSide];
      String rightFace=faceToNotation[rightSide];
      call(faceToNotation[side]);
      call(rightFace);
      call(oppositeFace+"_");
      call(rightFace+"_");
      call(faceToNotation[side]+"_");
      call(rightFace);
      call(oppositeFace);
      call(rightFace+"_");
    }
  }

  private void PLL() throws InvalidRubikNotation{
    // System.out.println("PLL");
    for(int side=1;side<5;side++){
      if(rubik[side][2]==rubik[side][4]){
        int leftSide = directions[5][(findIndex(directions[5],side)-1+4)%4];
        int rightSide = 5-leftSide;
        if(rubik[leftSide][2]==rubik[leftSide][4]){
          if(rubik[side][2]==rubik[side][3]&&rubik[leftSide][2]==rubik[leftSide][3]){
            int color = rubik[side][2];
            rotateTo(findIndex(directions[5],side),findIndex(directions[5],color),"U");
            return;
          }
          if(rubik[side][2]==rubik[side][3]||rubik[leftSide][2]==rubik[leftSide][3]||rubik[rightSide][2]==rubik[rightSide][3]){
            continue;
          }
          String turn = faceToNotation[rightSide];
          if(rubik[rightSide][2]==rubik[side][3]){
            call(turn);call("U_");
            call(turn);call("U");
            call(turn);call("U");
            call(turn);call("U_");
            call(turn+"_");call("U_");
            call(turn);call(turn);
            return;
          }
          call(turn);call(turn);
          call("U");call(turn);
          call("U");call(turn+"_");
          call("U_");call(turn+"_");
          call("U_");call(turn+"_");
          call("U");call(turn+"_");
          return;
        }
        int oppositeSide = 5-side;
        String oppositeFace=faceToNotation[oppositeSide];
        String rightFace=faceToNotation[rightSide];
        call(oppositeFace);
        call("U");
        call(oppositeFace+"_");
        call("U_");
        call(oppositeFace+"_");
        call(rightFace);
        call(oppositeFace);call(oppositeFace);
        call("U_");
        call(oppositeFace+"_");
        call("U_");
        call(oppositeFace);
        call("U");
        call(oppositeFace+"_");
        call(rightFace+"_");
        return;
      }
    }
    // no perfect corner
    call("F");
    call("R");
    call("U_");
    call("R_");
    call("U_");
    call("R");
    call("U");
    call("R_");
    call("F_");
    call("R");
    call("U");
    call("R_");
    call("U_");
    call("R_");
    call("F");
    call("R");
    call("F_");
    return;
  }

  private boolean checkPLL(){
    for(int side=1;side<5;side++){
      for(int j=2;j<5;j++){
        if(rubik[side][j]!=side){
          return false;
        }
      }
    }
    return true;
  }
  
  private boolean checkOLL(){
    for(int j=0;j<8;j++){
      if(rubik[0][j]!=0){
        return false;
      }
    }
    return true;
  }

  private boolean checkEdge(){
    for(int side=1;side<5;side++){
      for(int j=1;j<6;j+=4){
        if(rubik[side][j]!=side){
          return false;
        }
      }
    }
    return true;
  }
  
  public boolean checkCross(){
    for(int i=1;i<8;i+=2){ //cross
      if(rubik[5][i]!=5){
        return false;
      }
    }
    for(int i=1;i<5;i++){ //right cross
      if(rubik[i][7]!=i){
        return false;
      }
    }
    return true;
  }

  private boolean checkCorner(){
    for(int i=1;i<5;i++){
      if(rubik[i][0]!=i||rubik[i][6]!=i){
        return false;
      }
    }
    return true;
  }

  private ArrayList<String> solution = new ArrayList<String>();

  public ArrayList<String> getSolution(){
    return solution;
  }

  private void unimportantPrint(String message){
    if(detailPrintOption){
      System.out.println(message);
    }
  }

  private void solve() throws InvalidRubikNotation{
    while(!checkCross()){
      cross();
    }
    unimportantPrint("\n"+"finish cross "+solution.size()+"\n");
    while(!checkCorner()){
      corner();
    }
    unimportantPrint("\n"+"finish corner "+solution.size()+"\n");
    while(!checkEdge()){
      edge();
    }
    unimportantPrint("\n"+"finish edge "+solution.size()+"\n");
    while(!checkOLL()){
      OLL();
    }
    unimportantPrint("\n"+"finish OLL "+solution.size()+"\n");
    while(!checkPLL()){
      PLL();
    }
    unimportantPrint("\n"+"finish PLL "+solution.size()+"\n");
  }

  public ArrayList<String> mainSolving(){
    try{
      solve();
    }
    catch(InvalidRubikNotation e){
      System.out.println("given rubik notations are valid\n"+
              "\tavailible scrambles: U D R L F B U' D' R' L' F' B'");
      System.exit(1);
    }
    return solution;
  }
  
  public void mainSolving(String[] scrambles,boolean detailPrintOption, boolean countMovesOption){
    try{
      rubik = new int[6][8];
      //generate rubik
      for(int i=0;i<6;i++){
        rubik[i]=new int[8];
        Arrays.fill(rubik[i],i);
      }
      unimportantPrintRubik(rubik);

      for(String scramble:scrambles){
        call(scramble,true);
      }
      
      // Scanner systemIn = new Scanner(System.in);
      // int numOfScrambles=Integer.valueOf(systemIn.next());
      // for(int i=0;i<numOfScrambles;i++){
      //   String inputScramble=systemIn.next();
      //   scramble+=inputScramble+" ";
      //   call(inputScramble);
      // }
      // systemIn.close();

      // for(int time=0;time<10;time++){
      //   String random = (new String[]{"U","R","L","F","D","B","U_","R_","L_","F_","D_","B_"})[(int) (Math.random()*5)];
      //   call(random);
      //   string+=random+" ";
      // }
      
      // System.out.println("\nStarting Solving\n");
      try{
        solve();
      }
      catch(InvalidRubikNotation e){
        System.out.println("given rubik notations are valid\n"+
        "\tavailible scrambles: U D R L F B U' D' R' L' F' B'");
        System.exit(1);
      }

      // System.out.println("scrambles: "+String.join(", ",scrambles));
      unimportantPrint("solution: "+String.join(", ",solution));
      unimportantPrint("solution moves: "+solution.size());
      unimportantPrint("After cancellation");
      // int i=0;
      // while(i<solution.size()-1){
      for(int i=0;i<solution.size()-1;i++){
        String currentMove = solution.get(i);
        String nextMove = solution.get(i+1);
        // if current and next cancels out
        if(currentMove.charAt(0)==nextMove.charAt(0)){
          if(currentMove.length()!=nextMove.length()){
            unimportantPrint("cancel "+currentMove);
            solution.remove(i);
            solution.remove(i);
          }
        }
        // if there is double next
        if(solution.size()-i<3){
          continue;
        }
        String afterNextMove = solution.get(i+2);
        if(currentMove.equals(nextMove) && nextMove.equals(afterNextMove)){
          unimportantPrint("replace "+currentMove);
          solution.remove(i);
          solution.remove(i);
          solution.remove(i);
          if(i<solution.size()-3){
            if(Objects.equals(solution.get(i), currentMove)){
              unimportantPrint("remove "+currentMove);
              solution.remove(i);
              continue;
            }
          }
          // is clockwise
          if(currentMove.length()>1){
            solution.add(i,Character.toString(currentMove.charAt(0)));
          }
          else{
            solution.add(i,currentMove+"'");
          }
        }
      }
      System.out.println("solution: "+String.join(", ",solution));
      if(countMovesOption){
        System.out.println("solution moves: "+solution.size());
      }
    }
    catch(NullPointerException e){
      System.out.println("No scrambles taken\n"+
      "\tAvailible scrambles: U D R L F B U_ D_ R_ L_ F_ B_\n"+
      "For command usage: rubikFROOK --help");
    }
    catch(InvalidRubikNotation e){
      System.out.println("Internal Error, please report a bug");
    }
  }
  public void printRubik(int[][] rubik){
    System.out.println("      "+rubik[1][2]+" "+rubik[1][3]+" "+rubik[1][4]);
    System.out.println("      "+rubik[1][1]+" 1 "+rubik[1][5]);
    System.out.println("      "+rubik[1][0]+" "+rubik[1][7]+" "+rubik[1][6]);
    System.out.println(
      rubik[3][4]+" "+rubik[3][5]+" "+rubik[3][6]+" "+
      rubik[5][0]+" "+rubik[5][1]+" "+rubik[5][2]+" "+
      rubik[2][0]+" "+rubik[2][1]+" "+rubik[2][2]+" "+
      rubik[0][0]+" "+rubik[0][1]+" "+rubik[0][2]+" "
    );
    System.out.println(
      rubik[3][3]+" 3 "+rubik[3][7]+" "+
      rubik[5][7]+" 5 "+rubik[5][3]+" "+
      rubik[2][7]+" 2 "+rubik[2][3]+" "+
      rubik[0][7]+" 0 "+rubik[0][3]+" "
    );
    System.out.println(
      rubik[3][2]+" "+rubik[3][1]+" "+rubik[3][0]+" "+
      rubik[5][6]+" "+rubik[5][5]+" "+rubik[5][4]+" "+
      rubik[2][6]+" "+rubik[2][5]+" "+rubik[2][4]+" "+
      rubik[0][6]+" "+rubik[0][5]+" "+rubik[0][4]+" "
    );
    System.out.println("      "+rubik[4][6]+" "+rubik[4][7]+" "+rubik[4][0]);
    System.out.println("      "+rubik[4][5]+" 4 "+rubik[4][1]);
    System.out.println("      "+rubik[4][4]+" "+rubik[4][3]+" "+rubik[4][2]);
  }
  private void unimportantPrintRubik(int[][] rubik){
    if(detailPrintOption){
      printRubik(rubik);
    }
  }
}