package booleandynamicmodeling;

import java.util.*;
import java.util.regex.Pattern;
import javax.script.ScriptException;
import quinemccluskeyalgorithm.Formula;


/**
 *
 * @author Jorge G. T. Zañudo

Copyright (c) 2013-2015 Jorge G. T. Zañudo and Réka Albert.
 
The MIT License (MIT)

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */ 

public class NetworkReduction {
             
    public static String[] createAndSimplifyRuleDNF(String names[],String functions[]){
        String[][] regulators;
        String[] functionsSimplified;
        Formula f;
        int [][] statesON,simplifiedON;
        String functionEvaluate;
        int[] booleanConfiguration;
        int countZeros,Ninput,randomInt;
        boolean[] booleanState;
        boolean booleanRandom=false;
        Set<String> regulatorsList=new HashSet<String>();
        
        functionsSimplified=new String[names.length];
        regulators=new String[names.length][];
        String[] splitted;
        Pattern pattern = Pattern.compile("[()\\s]+");
        for(int i=0;i<names.length;i++){
            splitted=pattern.split(functions[i].replace("~","not "));
            for(int j=0;j<splitted.length;j++){
                if(!splitted[j].equals("") && !splitted[j].equals("and") && !splitted[j].equals("or") && !splitted[j].equals("not") && !splitted[j].equals("0") && !splitted[j].equals("False") && !splitted[j].equals("1") && !splitted[j].equals("True") && !splitted[j].equals("Random")){
                    regulatorsList.add(splitted[j]);
                }
            }
            regulators[i] = (String[])regulatorsList.toArray(new String[regulatorsList.size()]);
            regulatorsList=new HashSet<String>();
        }
        
        for(int i=0;i<names.length;i++){
        booleanConfiguration=new int[regulators[i].length];
        booleanState=new boolean[regulators[i].length];
        
            
        statesON=new int[0][0];
        countZeros=0;
        Ninput=(int)(Math.pow(2,regulators[i].length)+0.1);
        for(int j=0;j<Ninput;j++){
                functionEvaluate=" "+functions[i].replace("~","not ")+" ";
                OtherMethods.intToBinary(j,booleanConfiguration);
                OtherMethods.intToBoolean(booleanConfiguration,booleanState);                
                for(int k=0;k<regulators[i].length;k++){
                    functionEvaluate=functionEvaluate.replace(" "+regulators[i][k]+" "," "+booleanState[k]+" ");
                    //since a space was added after and before a parenthesis, now the nodes have to have a space
                    //preceding them and after them. Note that this solves the problem of having nodes with names
                    //such as IL2 and IL2R, since it will not be possible to confuse them since theyh will have a
                    //space before and after their names
                }
                
                functionEvaluate=functionEvaluate.replace("and", "&&").replace("or", "||").replace("not", "!").replace("True",""+true).replace("1",""+true).replace("0",""+false).replace("False",""+false);
                try {
                booleanRandom=OtherMethods.EvalStr(functionEvaluate,booleanRandom);
                } catch(ScriptException se) {
                    System.out.println("Formatting error in the function for "+names[i]+":\n"+functions[i]);
                    System.exit(0);
                }
                randomInt=booleanRandom?1:0;
                if(randomInt==1){
                    countZeros++;
                    statesON=Arrays.copyOf(statesON, statesON.length+1);
                    statesON[statesON.length-1]=Arrays.copyOf(booleanConfiguration, booleanConfiguration.length);
                }
                
            }
            
            if(countZeros==0){functionsSimplified[i]=" 0";}
            else if(countZeros==Ninput){functionsSimplified[i]=" 1";}
            else{
                f = Formula.readintArray(statesON);
                f.reduceToPrimeImplicants();
                simplifiedON=f.toArray();
                functionsSimplified[i]=OtherMethods.wildcardToFunction(simplifiedON,regulators[i]);
            }
            
        }
       
        return functionsSimplified;
                
}
    
    public static String createAndSimplifyRule(String names[], String[][] regulators){
        String[][] negativeRegulators;
        String[][] simplifiedRegulators;
        negativeRegulators=revertRule(names,regulators);
        simplifiedRegulators=revertRule(names,negativeRegulators);
        String rule="";
        String composite;
        for(int i=0;i<simplifiedRegulators.length;i++){
            composite="(";
            for(int j=0;j<simplifiedRegulators[i].length;j++){
                composite=composite+" "+simplifiedRegulators[i][j];
                if(j<simplifiedRegulators[i].length-1){
                    composite=composite+" and";
                }
            }
            composite=composite+" )";
            rule=rule+composite;
            if(i<simplifiedRegulators.length-1){
                rule=rule+" or ";
            }
        }
        return rule;
                
}
   
    public static String[][] revertRule(String names[], String[][] regulators){
      //In this part we look for the negation of all nodes. The idea is that something like
        //A*= (B and not C) or (D and not E) is transformed to
        //not A= (not B and not D) or (not B and E) or (C and not D) or (C and E)
        //What the following code does is transform the A* into the not A* function
        HashSet<String> regulatorsList=new HashSet<String>();
        HashSet<String[][]> regulatorsArrayList=new HashSet<String[][]>(); //We will store all the composite nodes affecting the negative nodes
                                                       //here since we don't actually now how many nodes will each composite node
                                                       //have, nor how many composite nodes per negation node will there be. The first row
                                                       //contains the elements, the second the name of the composite node
        int [] countArray=new int[regulators.length];
        String[] negations=new String[names.length];
        String newNode;
        String[][] stringdoubleDummy=new String[0][];
        int index;
        int value=-1;
        int[] indexArray;
        Integer[] dummyInt;
        boolean boolDummy1=true;
        boolean boolDummy2;
        boolean boolDummy3;        
        HashSet<String> compositeList;
        HashSet<Integer> indexList;
        String[] stringDummy;
        String[] regulatorsNegationsExpanded;
        String[][] regulatorsNegations;
        Iterator it;
        
        for(int i=0;i<names.length;i++){  
            negations[i]="~"+names[i]; //Name of the names of the negation to facilitate searching for the index
        }
        
        while(boolDummy1){
            compositeList=new HashSet<String>();
            indexList=new HashSet<Integer>();
            boolDummy3=true;
            for(int j=0;j<regulators.length;j++){
                if(regulators.length==1 && regulators[0].length==1){
                    if(regulators[0][0].equals("0") || regulators[0][0].equals("1") || regulators[0][0].equals("UnstableOscillation") || regulators[0][0].equals("FullOscillation") || regulators[0][0].equals("IncompleteOscillation")){
                        boolDummy3=false;
                        boolDummy1=false;
                        if(regulators[0][0].equals("0")){value=0;}
                        else if(regulators[0][0].equals("1")){value=1;}
                        else if(regulators[0][0].equals("UnstableOscillation")){value=-2;}
                        else if(regulators[0][0].equals("FullOscillation")){value=-3;}
                        else{value=-4;}
                        break;
                    }
                }
                if(regulators[j][countArray[j]].startsWith("~")){
                    compositeList.add(regulators[j][countArray[j]].substring(1));
                    //with this we only add the name of the negative regulator, which is what we want since we are taking the negation of the function
                    index=Arrays.asList(negations).indexOf(regulators[j][countArray[j]]);
                    indexList.add(index);
                    if(indexList.contains(-index-1)){
                        boolDummy3=false;
                        //If the composite node contains a node and its negations then its irrelevant
                    }
                }
                else{
                    compositeList.add("~"+regulators[j][countArray[j]]);
                    //with this we add the negation of the regulator, which is what we want since we are taking the negation of the function
                    index=Arrays.asList(names).indexOf(regulators[j][countArray[j]]);
                    indexList.add(-index-1);
                    //since the regulator is negative, we need to distinguish it for the positive node
                    if(indexList.contains(index)){
                        boolDummy3=false;
                        //If the composite node contains a node and its negations then its irrelevant
                    }
                }

            }

            index=0;
            boolDummy2=true;
            
            while(boolDummy2){
                countArray[index]++;
                if(countArray[index]>=regulators[index].length){
                    //If it reaches the maximum allowed values of the index, it will now try to increase the next entry in the array
                    //, similar to when 09 transforms into 10 when you sum 01 to 09.
                    countArray[index]=0;
                    index++;
                }
                else{
                    boolDummy2=false; //When a valid index for the countArray index is obtained it stops searching. The numbers in
                    //countArray will be used in then next cycle controlled by boolDummy1 to create the following composite node
                }

                if(index==regulators.length){
                    boolDummy1=false;  //This means that we have explored all possible composite nodes for the negations of the nodes
                    boolDummy2=false;
                }

            }
  
            if(boolDummy3){ //In case the composite node contains a node and its negations then we skip everything
                        
                dummyInt=indexList.toArray(new Integer[indexList.size()]); //For some reason Java cant convert directly an Int to int
                indexArray=new int[dummyInt.length];
                for(int k=0;k<dummyInt.length;k++){indexArray[k]=dummyInt[k].intValue();}

                indexArray=OtherMethods.orderArraybyOrder(indexArray, 0, indexArray.length-1,OtherMethods.orderHighesttoLowest(indexArray, 0, indexArray.length-1));
                //We are ordering the input nodes of the composite node for higher to lower to have a unique name for each combination
                index=indexArray[0];
                if(index>=0){
                    newNode=names[index];
                    }
                else{
                    newNode=negations[-index-1];
                }
                for(int k=1;k<indexArray.length;k++){
                    index=indexArray[k];
                    if(index>=0){
                        newNode=newNode+"_"+names[index];
                    }
                    else{
                        newNode=newNode+"_"+negations[-index-1];
                    }
                }
                if(!regulatorsList.contains(newNode)){
                    regulatorsList.add(newNode);
                    stringdoubleDummy=new String[2][];
                    stringdoubleDummy[0]=(String[])compositeList.toArray(new String[compositeList.size()]);
                    stringdoubleDummy[1]=new String[1];stringdoubleDummy[1][0]=newNode;
                    regulatorsArrayList.add(stringdoubleDummy);
                }

            }
                    
                
        }
        
        if(value==-1){
            regulatorsNegationsExpanded = (String[])regulatorsList.toArray(new String[regulatorsList.size()]);
            //We now add all the regulators of this node to its corresponding array. The composite nodes in this array are just a single name

            regulatorsNegations=new String[regulatorsArrayList.size()][];
            it=regulatorsArrayList.iterator();
            while(it.hasNext()){
                stringdoubleDummy=(String[][]) it.next();                
                index=Arrays.asList(regulatorsNegationsExpanded).indexOf(stringdoubleDummy[1][0]);
                regulatorsNegations[index]= Arrays.copyOf(stringdoubleDummy[0], stringdoubleDummy[0].length);
            }


            //We need to remove the composite input nodes that are irrelevant in the negations. For example if we have
            //~A*= B or (~C and B) , this is actually equivalent to ~A*=B , but because of the way we go the function of the
            //negation we would have both


            indexArray=new int[regulatorsNegationsExpanded.length];
            for(int j=0;j<regulatorsNegationsExpanded.length;j++){ //We do this for every input node

                for(int l=0;l<regulatorsNegations.length;l++){ //We check every other input node to see if this node is irrelevant

                    if(regulatorsNegations[j].length < regulatorsNegations[l].length && indexArray[l]==0 && indexArray[j]==0){
                        //For the node ij no te be irrelevant it must be contained in the il node. For it to be irrelevant we check if every node in
                        //ij is in il
                        boolDummy2=true;
                        index=0;
                        while(index<regulatorsNegations[j].length && boolDummy2){
                            //if it finds the nodes ij index inside il, then ij could be contained in il
                            boolDummy2=Arrays.asList(regulatorsNegations[l]).contains(regulatorsNegations[j][index]);
                            index++;
                        }
                        //if it found the node ijindex for all nodes, then ij is cointained in il. Then il is irrelevant
                        if(boolDummy2){
                            indexArray[l]=1;
                        }
                    }

                }

            }
            index=0; //We get the number of relevant regulators
            for(int j=0;j<regulatorsNegationsExpanded.length;j++){if(indexArray[j]==0){index++;}}
            stringDummy=new String[index];
            stringdoubleDummy=new String[index][];
            index=0;
            for(int j=0;j<regulatorsNegationsExpanded.length;j++){
                if(indexArray[j]==0){
                    stringDummy[index]=regulatorsNegationsExpanded[j];
                    stringdoubleDummy[index]=Arrays.copyOf(regulatorsNegations[j], regulatorsNegations[j].length);
                    index++;
                }
            }
        }
        
        if(value==1){
            regulatorsNegations=new String[1][1];
            regulatorsNegations[0][0]="0";
        }
        else if(value==0){
            regulatorsNegations=new String[1][1];
            regulatorsNegations[0][0]="1";
        }
        else if(value==-2){
            regulatorsNegations=new String[1][1];
            regulatorsNegations[0][0]="UnstableOscillation";
        }
        else if(value==-3){
            regulatorsNegations=new String[1][1];
            regulatorsNegations[0][0]="FullOscillation";
        }
        else if(value==-4){
            regulatorsNegations=new String[1][1];
            regulatorsNegations[0][0]="IncompleteOscillation";
        }
        else if(stringdoubleDummy.length==0){regulatorsNegations=new String[1][1];regulatorsNegations[0][0]="0";} 
        else{
            regulatorsNegations=Arrays.copyOf(stringdoubleDummy,stringdoubleDummy.length);
        }
        return regulatorsNegations;
     
     } 
        
         
}