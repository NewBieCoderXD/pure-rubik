package com.ggFROOK;

import java.util.*;
import java.util.stream.Collectors;

import static com.ggFROOK.Notation.*;
public class RubikFROOK {
  private ArrayList<Notation> solution = new ArrayList<>();
  private final int[][] directions = new int[][]{
    {1, 3, 4, 2},
    {3, 0, 2, 5},
    {1, 0, 4, 5},
    {4, 0, 1, 5},
    {2, 0, 3, 5},
    {1, 2, 4, 3}
  };
  private final Notation[] faceToNotation = new Notation[]{
    U,
    F,
    R,
    L,
    B
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
  private void addToSolution(Notation s){
    solution.add(s);
  }

//  public void throwError(String message){
//    System.err.println("Error: "+message);
//    System.exit(-1);
//  }

  private final Map<Notation, Map.Entry<Integer,Integer>> faceAndDirOfNotation=Map.of(
    R, Map.entry(1,1),
    L, Map.entry(1,-1),
    U, Map.entry(1,0),
    D, Map.entry(1,2),
    F, Map.entry(3,1),
    B, Map.entry(3,3)
  );
//  public void call(String methodName) throws InvalidRubikNotation{
//    call(methodName, false);
//  }
 public void call(String methodName, boolean IsScramble) throws InvalidRubikNotation{
    try {
      Notation notation = Notation.valueOf(methodName);
      call(notation, IsScramble);
    }
    catch(IllegalArgumentException e) {
      throw new InvalidRubikNotation();
    }
  }

  public void call(Notation notation){
    call(notation, false);
  }
  public void call(Notation methodName, boolean IsScramble){
    if(!methodName.isInverted()){
      Map.Entry<Integer,Integer> faceAndDir=faceAndDirOfNotation.get(methodName);
      rotateClockwise(faceAndDir.getKey(), faceAndDir.getValue());
    }
    else{
      // change R_ to R'
      Map.Entry<Integer,Integer> faceAndDir=faceAndDirOfNotation.get(methodName.toggle());
      rotateAntiClockwise(faceAndDir.getKey(), faceAndDir.getValue());
    }
    if(!IsScramble){
      addToSolution(methodName);
    }
    unimportantPrint(methodName+"");
    if(detailPrintOption){
      printRubik(rubik);
    }
  }

  private void rotateTo(int side,int targetSide,Notation action){
    int turns=side-targetSide;
    if(turns==0){
      return;
    }
    if(Math.abs(turns)>2){
      call(action.toggle());
      return;
    }
    for(int t=0;t<Math.abs(turns);t++){
      if(turns>0){
        call(action);
      }
      else{
        call(action.toggle());
      }
    }
  }

  private void cross(){
    for(int i=0;i<6;i++){
      for(int j=1;j<8;j+=2){
        if(rubik[i][j]==5){
          int direction = (j-1)/2;
          int latSide = directions[i][direction];
          int latDirection = findIndex(directions[latSide],i);
          int color = rubik[latSide][(latDirection*2+1)%8];
          if(i!=5||latSide!=color){
            // System.out.println("i "+i+" latSide "+latSide+" Color "+color+" direction "+direction);
            if(i==5){
              call(faceToNotation[latSide]);
              call(faceToNotation[latSide]);
              rotateTo(findIndex(directions[0],color),findIndex(directions[0],latSide),U);
              continue;
            }
            if(i==0){
              rotateTo(findIndex(directions[0],color),findIndex(directions[0],latSide),U);
              call(faceToNotation[color]);call(faceToNotation[color]);
              continue;
            }
            if(color==latSide){
              call((direction>1)? faceToNotation[latSide].toggle() : faceToNotation[latSide]);
              continue;
            }
            if(color==5-latSide){
              call(faceToNotation[i]);
              call(faceToNotation[i]);
              call((direction>1)? faceToNotation[color] : faceToNotation[color].toggle());
              continue;
            }
            if(direction==1||direction==3){
              if(findIndex(directions[i],color)<5){
                rotateTo(findIndex(directions[i],color),direction,faceToNotation[i]);
                call((direction>2)? faceToNotation[color].toggle() : faceToNotation[color]);
//                call(faceToNotation[color]+((direction>2)?"_":""));
                continue;
              }
              if(color==i){
                call(U_);
                call(faceToNotation[directions[5][(findIndex(directions[5],color)+1)%4]].toggle());
                call(faceToNotation[color]);
                call(faceToNotation[directions[5][(findIndex(directions[5],color)+1)%4]]);
                continue;
              }
              call(faceToNotation[color]);
              call(faceToNotation[color]);
              call(U_);
              call(faceToNotation[directions[5][(findIndex(directions[5],color)+5)%4]].toggle());
              call(faceToNotation[color]);
              call(faceToNotation[directions[5][(findIndex(directions[5],color)+5)%4]]);
              continue;
            }
            //direction = 0,2
            call((direction>1)? faceToNotation[latSide] : faceToNotation[latSide].toggle());
//            call(faceToNotation[latSide]+((direction>1)?"":"_"));
            unimportantPrint(findIndex(directions[0],color)+" "+findIndex(directions[0],latSide));
            rotateTo(findIndex(directions[0],color)+((color==1?4:0)),findIndex(directions[0],latSide),U);

            call((direction<1)? faceToNotation[latSide] : faceToNotation[latSide].toggle());
//            call(faceToNotation[latSide]+((direction<1)?"":"_"));

            call(faceToNotation[color]);call(faceToNotation[color]);
          }
        }
      }
    }
  }

  private void corner(){
    for(int i=1;i<5;i++){
      for(int j=0;j<7;j+=2){
        if(rubik[i][j]==5){
          int latSide = directions[5][(findIndex(directions[5],i)+((j>3)?1:-1)+4)%4];
          int color = rubik[latSide][6-j];
          unimportantPrint("Corner found. lat side: "+latSide+" color: "+color+" j: "+j);
          if(j==0){
            call(faceToNotation[i]);
//            call(faceToNotation[i]+((j<3)?"":"_"));
            call(U);
            call(faceToNotation[i].toggle());
            call(U_);
            return;
          }
          if(j==6){
            call(faceToNotation[i].toggle());
//            call(faceToNotation[i]+((j<3)?"":"_"));
            call(U_);
            call(faceToNotation[i]);
            call(U);
            return;
          }
          else{
            int newSide = directions[5][(findIndex(directions[5],color)+((j>3)?-1:1)+4)%4];
            rotateTo(findIndex(directions[5],latSide),findIndex(directions[5],color),U);
            if(j>3){
              call(faceToNotation[newSide].toggle());
              call(U_);
              call(faceToNotation[newSide]);
            }
            else{
              call(faceToNotation[newSide]);
              call(U);
              call(faceToNotation[newSide].toggle());
            }
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
        int side = (new int[]{3,1,2,4})[j/2];
        for(int k=0;k<8;k++){
          if(rubik[5][k]!=5){
            rotateTo((10-k)%8/2,j/2,U);
            side = (new int[]{3,1,2,4})[(10-k)%8/2];
            break;
          }
        }
        call(faceToNotation[side]);
        call(U);call(U);
        call(faceToNotation[side].toggle());
      }
    }
    for(int j=0;j<8;j++){
      if(rubik[5][j]!=5){
        return;
      }
    }
    for(int side=1;side<5;side++){
      if(rubik[side][2]!=side){
        Notation turn = faceToNotation[rubik[5-side][2]];
        call(turn);
        call(U);
        call(turn.toggle());
        return;
      }
    }
  }
  
  private void edge(){
    boolean hasEdge = false;
    for(int j=1;j<8;j+=2){
      if(rubik[0][j]!=0){
        int latSide = directions[0][(j-1)/2];
        if(rubik[latSide][3]!=0){
          hasEdge = true;
          int top=rubik[0][j];
          int bottom=rubik[latSide][3];
          unimportantPrint("Edge found. latSide: "+latSide+" top: "+top+" bottom: "+bottom+" "+findIndex(directions[5],top)+" "+findIndex(directions[5],bottom));
          if(directions[5][(findIndex(directions[5],top)+2)%4]!=latSide){
            rotateTo(findIndex(directions[0],5-top),findIndex(directions[0],latSide),U);
          }
          if(findIndex(directions[5],top)<findIndex(directions[5],bottom)&&(top!=1||bottom!=3)||(top==3&&bottom==1)){
            call(faceToNotation[top].toggle());
            call(U_);
            call(faceToNotation[top]);
            call(U);
            call(faceToNotation[bottom]);
            call(U);
            call(faceToNotation[bottom].toggle());
          }
          else{
            call(faceToNotation[top]);
            call(U);
            call(faceToNotation[top].toggle());
            call(U_);
            call(faceToNotation[bottom].toggle());
            call(U_);
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
          if(latColor!=0&&latSide!=latColor){
            unimportantPrint("wrong edge. side: "+side+" latSide: "+latSide+" color: "+color+" latColor: "+latColor);
            call(faceToNotation[side]);
            call(U);
            call(faceToNotation[side].toggle());
            call(U_);
            call(faceToNotation[latSide].toggle());
            call(U_);
            call(faceToNotation[latSide]);
          }
        }
      }
    }
  }

  private void OLL(){
    // System.out.println("OLL");
    ArrayList<Integer> yellowEdgeList = new ArrayList<>();
    ArrayList<Integer> yellowCornerList = new ArrayList<>();
    for(int j=1;j<8;j+=2){
      if(rubik[0][j]==0){
        yellowEdgeList.add(j/2);
      }
    }
    if(yellowEdgeList.isEmpty()){
      call(F);
      call(R);
      call(U);
      call(R_);
      call(U_);
      call(F_);
      return;
    }
    
    if(yellowEdgeList.size()==2){
      if(yellowEdgeList.get(0)+yellowEdgeList.get(1)==5){ //Line
        int side = directions[0][(yellowEdgeList.get(0)-1+4)%4];
        int latSide = 5-directions[0][yellowEdgeList.get(0)];
        call(faceToNotation[side]);
        call(U);
        call(faceToNotation[latSide]);
        call(U_);
        call(faceToNotation[latSide].toggle());
        call(faceToNotation[side].toggle());
      }
      else{
        int maxDirection = findIndex(directions[0],Math.max(yellowEdgeList.get(0),yellowEdgeList.get(1)));
        int leftSide = directions[0][(maxDirection+1)%4];
        int side = directions[0][maxDirection];
        call(faceToNotation[leftSide]);
        call(U);
        call(faceToNotation[side]);
        call(U_);
        call(faceToNotation[side].toggle());
        call(faceToNotation[leftSide].toggle());
      }
      return;
    }
    
    // 4 yellows
    for(int j=0;j<7;j+=2){
      if(rubik[0][j]==0){
        yellowCornerList.add(j);
      }
    }
    if(yellowCornerList.isEmpty()){
      for(int side=1;side<5;side++){
        if(rubik[side][2]==0&&rubik[side][4]==0){
          // int direction = findIndex(directions[0],side);
          if(rubik[5-side][2]==0&&rubik[5-side][4]==0){
            call(faceToNotation[side]);
            call(U);
            call(faceToNotation[side].toggle());
            call(U);
            call(faceToNotation[side]);
            call(U_);
            call(faceToNotation[side].toggle());
            call(U);
            call(faceToNotation[side]);
            call(U);call(U);
            call(faceToNotation[side].toggle());
            return;
          }
          else{
            int oppositeSide = 5-side;
            Notation oppositeFace=faceToNotation[oppositeSide];
            call(oppositeFace);
            call(U);call(U);
            call(oppositeFace);
            call(oppositeFace);
            call(U_);
            call(oppositeFace);
            call(oppositeFace);
            call(U_);
            call(oppositeFace);
            call(oppositeFace);
            call(U);call(U);
            call(oppositeFace);
          }
        }
      }
    }
    if(yellowCornerList.size()==1){ //ปลาตะเพียน
      int side = directions[0][(yellowCornerList.get(0)/2-1+4)%4]; //right side
      //System.out.println(side);
      if(rubik[side][4]==0){
        int latSide = directions[0][(yellowCornerList.get(0)/2+2)%4];
        call(faceToNotation[latSide]);
        call(U);
        call(faceToNotation[latSide].toggle());
        call(U);
        call(faceToNotation[latSide]);
        call(U);call(U);
        call(faceToNotation[latSide].toggle());
        return;
      }
      int latSide = directions[0][(yellowCornerList.get(0)/2+1)%4];
      call(faceToNotation[latSide].toggle());
      call(U_);
      call(faceToNotation[latSide]);
      call(U_);
      call(faceToNotation[latSide].toggle());
      call(U);call(U);
      call(faceToNotation[latSide]);
      return;
    }
    if(yellowCornerList.size()==2){
      int direction1 = yellowCornerList.get(0)/2;
      int direction2 = yellowCornerList.get(1)/2;
      if(Math.abs(direction1-direction2)==2){
        int side = directions[0][(direction1+2)%4];
        int rightSide = directions[0][(direction1+1)%4];
        int leftSide = directions[0][(direction1+3)%4];
        if(rubik[side][2]!=0){
          side = directions[0][(direction2+2)%4];
          rightSide = directions[0][(direction2+1)%4];
          leftSide = directions[0][(direction2+3)%4];
        }
        Notation rightFace=faceToNotation[rightSide];
        call(faceToNotation[side]);
        call(rightFace.toggle());
        call(faceToNotation[side].toggle());
        call(faceToNotation[leftSide]);
        call(faceToNotation[side]);
        call(rightFace);
        call(faceToNotation[side].toggle());
        call(faceToNotation[leftSide].toggle());
        return;
      }
      //รถถัง
      int side = directions[0][(4-Math.min(yellowCornerList.get(0),yellowCornerList.get(1)))%4];
      int oppositeSide = 5-side;
      int rightSide = directions[0][(4-Math.min(yellowCornerList.get(0),yellowCornerList.get(1))+3)%4];
      Notation oppositeFace=faceToNotation[oppositeSide];
      Notation rightFace=faceToNotation[rightSide];
      call(faceToNotation[side]);
      call(rightFace);
      call(oppositeFace.toggle());
      call(rightFace.toggle());
      call(faceToNotation[side].toggle());
      call(rightFace);
      call(oppositeFace);
      call(rightFace.toggle());
    }
  }

  private void PLL(){
    // System.out.println("PLL");
    for(int side=1;side<5;side++){
      if(rubik[side][2]==rubik[side][4]){
        int leftSide = directions[5][(findIndex(directions[5],side)-1+4)%4];
        int rightSide = 5-leftSide;
        if(rubik[leftSide][2]==rubik[leftSide][4]){
          if(rubik[side][2]==rubik[side][3]&&rubik[leftSide][2]==rubik[leftSide][3]){
            int color = rubik[side][2];
            rotateTo(findIndex(directions[5],side),findIndex(directions[5],color),U);
            return;
          }
          if(rubik[side][2]==rubik[side][3]||rubik[leftSide][2]==rubik[leftSide][3]||rubik[rightSide][2]==rubik[rightSide][3]){
            continue;
          }
          Notation turn = faceToNotation[rightSide];
          if(rubik[rightSide][2]==rubik[side][3]){
            call(turn);call(U_);
            call(turn);call(U);
            call(turn);call(U);
            call(turn);call(U_);
            call(turn.toggle());call(U_);
            call(turn);call(turn);
            return;
          }
          call(turn);call(turn);
          call(U);call(turn);
          call(U);call(turn.toggle());
          call(U_);call(turn.toggle());
          call(U_);call(turn.toggle());
          call(U);call(turn.toggle());
          return;
        }
        int oppositeSide = 5-side;
        Notation oppositeFace=faceToNotation[oppositeSide];
        Notation rightFace=faceToNotation[rightSide];
        call(oppositeFace);
        call(U);
        call(oppositeFace.toggle());
        call(U_);
        call(oppositeFace.toggle());
        call(rightFace);
        call(oppositeFace);call(oppositeFace);
        call(U_);
        call(oppositeFace.toggle());
        call(U_);
        call(oppositeFace);
        call(U);
        call(oppositeFace.toggle());
        call(rightFace.toggle());
        return;
      }
    }
    // no perfect corner
    call(F);
    call(R);
    call(U_);
    call(R_);
    call(U_);
    call(R);
    call(U);
    call(R_);
    call(F_);
    call(R);
    call(U);
    call(R_);
    call(U_);
    call(R_);
    call(F);
    call(R);
    call(F_);
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

  public ArrayList<Notation> getSolution(){
    return solution;
  }

  private void unimportantPrint(String message){
    if(detailPrintOption){
      System.out.println(message);
    }
  }

  private void solve(){
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

  public ArrayList<Notation> mainSolving(){
    solution.clear();
    solve();
    optimizeSolution();
    return solution;
  }

//  private int[][] createCube(){
//    rubik = new int[6][8];
//    //generate rubik
//    for(int i=0;i<6;i++){
//      rubik[i]=new int[8];
//      Arrays.fill(rubik[i],i);
//    }
//    return rubik;
//  }
  private void optimizeSolution(){
    ArrayList<Notation> optimizedSolution = new ArrayList<>(solution.size());
    for(int i=0;i<solution.size();i++){
      Notation currentNotation = solution.get(i);
      if(i==solution.size()-1){
        optimizedSolution.add(currentNotation);
        continue;
      }
      Notation nextNotation = solution.get(i+1);
      // if current and next cancels out
      if(currentNotation.isOpposite(nextNotation)){
        unimportantPrint("cancel "+currentNotation);
        i++;
        continue;
      }
      // if there is double next
      if(solution.size()-i<3){
        optimizedSolution.add(solution.get(i));
        continue;
      }
      Notation thirdCurrentMove = solution.get(i+2);
      if(currentNotation.equals(nextNotation) && nextNotation.equals(thirdCurrentMove)){
        if(i+3<solution.size() && solution.get(i+3).equals(currentNotation)){
          unimportantPrint("remove "+currentNotation);
          i+=3;
          System.out.println(solution.get(i).toString());
          continue;
        }
        // is clockwise
        unimportantPrint("replace "+currentNotation);
        optimizedSolution.add(solution.get(i).toggle());
        i+=2;
        continue;
      }
      optimizedSolution.add(solution.get(i));
    }
    System.out.println("solution: "+solutionToString());
    if(countMovesOption) {
      System.out.println("solution moves: " + optimizedSolution.size());
    }
    solution=optimizedSolution;
  }
  private boolean handleReplace(ArrayList<Notation> optimizedSolution, int i){
    if(solution.size()-i<3){
      optimizedSolution.add(solution.get(i));
      return false;
    }
    Notation currentNotation = solution.get(i);
    Notation nextNotation = solution.get(i+1);
    Notation secondCurrentMove = solution.get(i+2);
    if(currentNotation.equals(nextNotation) && nextNotation.equals(secondCurrentMove)){
      unimportantPrint("replace "+currentNotation);
      optimizedSolution.add(solution.get(i).toggle());
      return true;
    }
    return false;
  }

  public void mainSolving(String[] scrambles,boolean detailPrintOption, boolean countMovesOption){
    try{
      initRubik();
      solution.clear();
      unimportantPrintRubik(rubik);
      this.detailPrintOption = detailPrintOption;
      this.countMovesOption = countMovesOption;
      try{
        for(String scramble:scrambles){
          call(scramble,true);
        }
      }
      catch(InvalidRubikNotation e){
        System.out.println("given rubik notations are valid\n"+
        "\tavailible scrambles: U D R L F B U' D' R' L' F' B'");
        System.exit(1);
      }
      solve();
      unimportantPrint("solution: "+solutionToString());
      if(countMovesOption){
        System.out.println("solution moves: "+solution.size());
      }
      unimportantPrint("After cancellation");
      optimizeSolution();
    }
    catch(NullPointerException e){
      System.out.println("""
              No scrambles taken
              \tAvailible scrambles: U D R L F B U_ D_ R_ L_ F_ B_
              For command usage: rubikFROOK --help""");
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
  public static void main(String[] args){
    RubikFROOK instance = new RubikFROOK();
    instance.solution = new ArrayList<>(List.of(R,R,R_));
    instance.detailPrintOption=true;
    instance.optimizeSolution();
    System.out.println("gg"+instance.solutionToString());
  }
  private String solutionToString(){
    return solution.stream().map(Object::toString).collect(Collectors.joining(", "));
  }
}