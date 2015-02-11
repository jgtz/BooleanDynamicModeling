/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package booleandynamicmodeling;

import fileOperations.DeleteDirectoryfiles;
import fileOperations.FileToWrite;
import java.io.File;
import java.util.Arrays;
import java.util.Random;
import javax.script.ScriptException;

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



public class NetworkModels {
    
    public static Network RandomNetworkNK(int N, int K, double p){
        Network network=new Network(N,1);
        int[] inputNodes=new int[K];        
        int Ninput=(int)(Math.pow(2, K)+0.1);
        int[] regulatoryFunctions=new int[Ninput];
        int[] elegibleNodes=new int[network.getN()];
        int[] elegibleNodes2;
        int NEffective;
        int chosenNode,chosenInteger;
        
        for(int i=0;i<network.getN();i++){
            elegibleNodes[i]=i;
        }
        
        for(int i=0;i<network.getN();i++){
            NEffective=network.getN();
            elegibleNodes2=Arrays.copyOf(elegibleNodes, elegibleNodes.length);            
            for(int j=0;j<K;j++){
                chosenInteger=(int)(NEffective*Math.random());
                chosenNode=elegibleNodes2[chosenInteger];
                inputNodes[j]=chosenNode;
                NEffective--;
                elegibleNodes2[chosenInteger]=elegibleNodes2[NEffective];
                elegibleNodes2[NEffective]=chosenNode;
            }
            
            for(int j=0;j<Ninput;j++){
                regulatoryFunctions[j]= (Math.random()>p)?0:1;
            }
            network.setNode(new Node("n"+i,inputNodes,1),i);
            network.getNode(i).setKi(K);
            network.getNode(i).setRegulatoryFunctions(regulatoryFunctions);
        }
        
        return network;
        
    }

    public static Network RandomNetworkNK(int N, int K, double p, Random ran){
        Network network=new Network(N,1);
        int[] inputNodes=new int[K];
        int Ninput=(int)(Math.pow(2, K)+0.1);
        int[] regulatoryFunctions=new int[Ninput];
        int[] elegibleNodes=new int[N];
        int[] elegibleNodes2;
        int NEffective;
        int chosenNode,chosenInteger;
        
        for(int i=0;i<N;i++){
            elegibleNodes[i]=i;
        }
        
        for(int i=0;i<N;i++){
            NEffective=N;
            elegibleNodes2=Arrays.copyOf(elegibleNodes, elegibleNodes.length);            
            for(int j=0;j<K;j++){
                chosenInteger=(int)(NEffective*ran.nextDouble());
                chosenNode=elegibleNodes2[chosenInteger];
                inputNodes[j]=chosenNode;
                NEffective--;
                elegibleNodes2[chosenInteger]=elegibleNodes2[NEffective];
                elegibleNodes2[NEffective]=chosenNode;
            }
            for(int j=0;j<Ninput;j++){
                regulatoryFunctions[j]= (ran.nextDouble()>p)?0:1;
            }
            network.setNode(new Node("n"+i,inputNodes,1),i);
            network.getNode(i).setKi(K);
            network.getNode(i).setRegulatoryFunctions(regulatoryFunctions);
        }
        
        return network;
        
    }    

    public static Network RandomNetworkScaleFree(int N, double gamma, double p, int maxK){
        Network network=new Network(N,1);
        double normalize=0,kavg=0;
        double probability,randomNumber;
        int[] inputNodes;        
        int Ninput,K;
        int[] regulatoryFunctions;
        int[] elegibleNodes=new int[network.getN()];
        int[] elegibleNodes2;
        int NEffective;
        int chosenNode,chosenInteger;
        
        for(int i=0;i<N;i++){
            elegibleNodes[i]=i;
            normalize+=1/Math.pow(i+1,gamma);
        }
        
        for(int i=0;i<network.getN();i++){
            randomNumber=normalize*Math.random();
            probability=0;
            K=0;
            
            do{
                K++;
                probability+=1/Math.pow(K,gamma);
                
            } while(probability<randomNumber && K<maxK);
            kavg+=K;
            inputNodes=new int[K];
            Ninput=(int)(Math.pow(2, K)+0.1);
            regulatoryFunctions=new int[Ninput];
            NEffective=network.getN();
            elegibleNodes2=Arrays.copyOf(elegibleNodes, elegibleNodes.length);            
            for(int j=0;j<K;j++){
                chosenInteger=(int)(NEffective*Math.random());
                chosenNode=elegibleNodes2[chosenInteger];
                inputNodes[j]=chosenNode;
                NEffective--;
                elegibleNodes2[chosenInteger]=elegibleNodes2[NEffective];
                elegibleNodes2[NEffective]=chosenNode;
            }
            
            for(int j=0;j<Ninput;j++){
                regulatoryFunctions[j]= (Math.random()>p)?0:1;
            }
            network.setNode(new Node("n"+i,inputNodes,1),i);
            network.getNode(i).setKi(K);
            network.getNode(i).setRegulatoryFunctions(regulatoryFunctions);
        }
        return network;
        
    }

    public static Network RandomNetworkScaleFree(int N, double gamma, double p, int maxK, Random ran){
        Network network=new Network(N,1);
        double normalize=0;
        double probability,randomNumber;
        int[] inputNodes;        
        int Ninput,K;
        int[] regulatoryFunctions;
        int[] elegibleNodes=new int[network.getN()];
        int[] elegibleNodes2;
        int NEffective;
        int chosenNode,chosenInteger;
        
        for(int i=0;i<maxK;i++){
            elegibleNodes[i]=i;
            normalize+=1/Math.pow(i+1,gamma);
        }
        
        for(int i=0;i<network.getN();i++){
            randomNumber=normalize*ran.nextDouble();
            K=0;
            probability=0;
            
            do{
                K++;
                probability+=1/Math.pow(K,gamma);
                
            } while(probability<randomNumber);
            
            
            inputNodes=new int[K];
            Ninput=(int)(Math.pow(2, K)+0.1);
            regulatoryFunctions=new int[Ninput];
            NEffective=network.getN();
            elegibleNodes2=Arrays.copyOf(elegibleNodes, elegibleNodes.length);            
            for(int j=0;j<K;j++){
                chosenInteger=(int)(NEffective*ran.nextDouble());
                chosenNode=elegibleNodes2[chosenInteger];
                inputNodes[j]=chosenNode;
                NEffective--;
                elegibleNodes2[chosenInteger]=elegibleNodes2[NEffective];
                elegibleNodes2[NEffective]=chosenNode;
            }
            
            for(int j=0;j<Ninput;j++){
                regulatoryFunctions[j]= (ran.nextDouble()>p)?0:1;
            }
            network.setNode(new Node("n"+i,inputNodes,1),i);
            network.getNode(i).setKi(K);
            network.getNode(i).setRegulatoryFunctions(regulatoryFunctions);
        }
        
        return network;
        
    } 
      
    public static Network RandomNetworkNKAll(int N, int K, double p ,String directory) throws ScriptException{
        Network network;
        int index;
        String[] rules=new String[N];
        String[] functions=new String[N];
        String[] functionsNetwork=new String[N];
        String[] namesNetwork=new String[N];
        String[] names=new String[N];
        String[][] regulatorsi;
        String[] regulatorsj;
        int Ninput=(int)(Math.pow(2, K)+0.1);
        int[] regulatoryFunctions=new int[Ninput];
        String node;
        DeleteDirectoryfiles.Deldir(new File(directory));
        new File(directory).mkdir();
        FileToWrite fw,fw2,fw3;
        int[] inputNodes=new int[K];
        int[] BooleanState=new int[K];
        int[] elegibleNodes=new int[N];
        int[] elegibleNodes2;
        int NEffective;
        int countZeros;
        double countTotalZeros;        
        int chosenNode,chosenInteger;
        double q=1-p;
        String rule;
        countTotalZeros=0;
        
        fw=new FileToWrite(directory+".txt");
        fw2=new FileToWrite(directory+"Rules.txt");
        fw3=new FileToWrite(directory+"Functions.txt");
        //File with the networkRules inside the directory with the tables for the rules
        fw.writeLine("#INITIAL CONDITION");
        fw.writeLine("#BOOLEAN RULES");
        fw2.writeLine("#BOOLEAN RULES");
        
        for(int i=0;i<N;i++){
            elegibleNodes[i]=i;
            node="n"+i;
            names[i]=node;
        }
        
        for(int i=0;i<N;i++){
            countZeros=0;
            functions[i]=" ";
            node="n"+i;
            NEffective=N;
            elegibleNodes2=Arrays.copyOf(elegibleNodes, elegibleNodes.length);            
            for(int j=0;j<K;j++){
                chosenInteger=(int)(NEffective*Math.random());
                chosenNode=elegibleNodes2[chosenInteger];
                inputNodes[j]=chosenNode;
                NEffective--;
                elegibleNodes2[chosenInteger]=elegibleNodes2[NEffective];
                elegibleNodes2[NEffective]=chosenNode;
            }
            regulatorsi=new String[0][0];
            for(int j=0;j<Ninput;j++){
                OtherMethods.intToBinary(j, BooleanState);  
                regulatoryFunctions[j]= (Math.random()>=p)?0:1;
                if(regulatoryFunctions[j]==1){
                    regulatorsj=new String[K];
                    rule=" (";
                    for(int k=0;k<K;k++){
                        if(BooleanState[k]==1){
                            regulatorsj[k]="n"+inputNodes[k];
                            if(" (".equals(rule)){rule=rule+"n"+inputNodes[k];}
                            else{rule=rule+" and n"+inputNodes[k];}
                        }
                        else{
                            regulatorsj[k]="~n"+inputNodes[k];
                            if(" (".equals(rule)){rule=rule+"~n"+inputNodes[k];}
                            else{rule=rule+" and ~n"+inputNodes[k];} 
                        }           
                    }
                    rule=rule+" )";
                    if(countZeros>0){functions[i]=functions[i]+" or "+rule;}
                    else{functions[i]=rule;} 
                    countZeros++;
                    regulatorsi=Arrays.copyOf(regulatorsi, regulatorsi.length+1);
                    regulatorsi[regulatorsi.length-1]=Arrays.copyOf(regulatorsj, regulatorsj.length);;
                }   
            }
            if(countZeros==0){functions[i]=" 0";}
            else if(countZeros==Ninput){functions[i]=" 1";}
            else{
                functions[i]=NetworkReduction.createAndSimplifyRule(names, regulatorsi);
            }
            rules[i]="*= "+functions[i];
            rules[i]=rules[i].replace("~", "not ");
            fw.writeLine(node+""+rules[i]);
            fw2.writeLine(node+""+rules[i]);
            countTotalZeros=countTotalZeros+countZeros;
        }
        
        fw.close();
        fw2.close();
        
        ReadWriteFiles.createTablesFromBooleanRules(directory,directory+".txt");
        network=new Network(directory);
        for(int i=0;i<N;i++){
            node=network.getNode(i).getName();
            index=Arrays.asList(names).indexOf(node.split("_")[1]);
            functionsNetwork[i]=functions[index].replace("("," ( ").replace(")"," ) ").replace("not ","~").replace(" True "," 1 ").replace(" False "," 0 "); //this is to facilitate the use network reduction process
            fw3.writeLine(functionsNetwork[i]);
            namesNetwork[i]=names[index];
        }
        fw3.close();
        
        network.setFunctions(functionsNetwork);
        network.setNames(namesNetwork);
        return network;
        
    }

    public static Network RandomThresholdNetwork(int N, int K, double p ,int ag, int ar, double theta, String directory) throws ScriptException{
        Network network;
        int index;
        String[] rules=new String[N];
        String[] functions=new String[N];
        String[] functionsNetwork=new String[N];
        String[] namesNetwork=new String[N];
        String[] names=new String[N];
        String[][] regulatorsi;
        String[] regulatorsj;
        int Ninput=(int)(Math.pow(2, K)+0.1);
        int[] regulatoryFunctions=new int[Ninput];
        String node;
        DeleteDirectoryfiles.Deldir(new File(directory));
        new File(directory).mkdir();
        FileToWrite fw,fw2,fw3;
        int[] inputNodes=new int[K];
        int[] inputNature=new int[K];
        int[] BooleanState=new int[K];
        int[] elegibleNodes=new int[N];
        int[] elegibleNodes2;
        int NEffective;
        int countZeros;
        double countTotalZeros;        
        int chosenNode,chosenInteger;
        double q=1-p;
        String rule;
        countTotalZeros=0;
        
        fw=new FileToWrite(directory+".txt");
        fw2=new FileToWrite(directory+"Rules.txt");
        fw3=new FileToWrite(directory+"Functions.txt");
        //File with the networkRules inside the directory with the tables for the rules
        fw.writeLine("#INITIAL CONDITION");
        fw.writeLine("#BOOLEAN RULES");
        fw2.writeLine("#BOOLEAN RULES");
        
        for(int i=0;i<N;i++){
            elegibleNodes[i]=i;
            node="n"+i;
            names[i]=node;
        }
        
        for(int i=0;i<N;i++){
            countZeros=0;
            functions[i]=" ";
            node="n"+i;
            NEffective=N;
            elegibleNodes2=Arrays.copyOf(elegibleNodes, elegibleNodes.length);            
            for(int j=0;j<K;j++){
                chosenInteger=(int)(NEffective*Math.random());
                chosenNode=elegibleNodes2[chosenInteger];
                inputNodes[j]=chosenNode;
                if(Math.random()<p){inputNature[j]=ag;}
                else{inputNature[j]=ar;}
                NEffective--;
                elegibleNodes2[chosenInteger]=elegibleNodes2[NEffective];
                elegibleNodes2[NEffective]=chosenNode;
            }
            regulatorsi=new String[0][0];
            for(int j=0;j<Ninput;j++){
                OtherMethods.intToBinary(j, BooleanState);
                regulatoryFunctions[j]=0;
                for(int k=0;k<K;k++){
                    regulatoryFunctions[j]=regulatoryFunctions[j]+BooleanState[k]*inputNature[k];
                }
                if(regulatoryFunctions[j]*1.0>theta){regulatoryFunctions[j]=1;}
                else{regulatoryFunctions[j]=0;}
                
                if(regulatoryFunctions[j]==1){
                    regulatorsj=new String[K];
                    rule="( ";
                    for(int k=0;k<K;k++){
                        if(BooleanState[k]==1){
                            regulatorsj[k]="n"+inputNodes[k];
                            if("( ".equals(rule)){rule=rule+"n"+inputNodes[k];}
                            else{rule=rule+" and n"+inputNodes[k];}
                        }
                        else{
                            regulatorsj[k]="~n"+inputNodes[k];
                            if("( ".equals(rule)){rule=rule+"~n"+inputNodes[k];}
                            else{rule=rule+" and ~n"+inputNodes[k];} 
                        }           
                    }
                    rule=rule+" )";
                    if(countZeros>0){functions[i]=functions[i]+" or "+rule;}
                    else{functions[i]=rule;} 
                    countZeros++;
                    regulatorsi=Arrays.copyOf(regulatorsi, regulatorsi.length+1);
                    regulatorsi[regulatorsi.length-1]=Arrays.copyOf(regulatorsj, regulatorsj.length);
                }   
            }
            if(countZeros==0){functions[i]=" 0";}
            else if(countZeros==Ninput){functions[i]=" 1";}
            else{           
                functions[i]=NetworkReduction.createAndSimplifyRule(names, regulatorsi);               

            }
            
            rules[i]="*= "+functions[i];
            rules[i]=rules[i].replace("~", "not ");
            fw.writeLine(node+""+rules[i]);
            fw2.writeLine(node+""+rules[i]);
            countTotalZeros=countTotalZeros+countZeros;
        }

        
        fw.close();
        fw2.close();
        
        ReadWriteFiles.createTablesFromBooleanRules(directory,directory+".txt");
        network=new Network(directory);
        for(int i=0;i<N;i++){
            node=network.getNode(i).getName();
            index=Arrays.asList(names).indexOf(node.split("_")[1]);
            functionsNetwork[i]=functions[index].replace("("," ( ").replace(")"," ) ").replace("not ","~").replace(" True "," 1 ").replace(" False "," 0 "); //this is to facilitate the use network reduction process
            fw3.writeLine(functionsNetwork[i]);
            namesNetwork[i]=names[index];
        }
        fw3.close();
        
        network.setFunctions(functionsNetwork);
        network.setNames(namesNetwork);
        return network;
        
    }
    
 
}

