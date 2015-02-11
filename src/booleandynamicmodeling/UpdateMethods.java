/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package booleandynamicmodeling;

import java.util.Arrays;

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
 

public class UpdateMethods {
    
    public static void synchronousBoolean(Network network){
        int inputNumber;
        int[] stateInputs;
        int[] newStates=new int[network.getN()];
        for(int i=0;i<network.getN();i++){
            stateInputs=new int[network.getNode(i).getKi()];
            for(int j=0;j<stateInputs.length;j++){
                inputNumber=network.getNode(i).getInputNode(j);
                stateInputs[j]=network.getNode(inputNumber).getStateDiscrete();
            }
            if(stateInputs.length>0){
                newStates[i]=updateNodeBoolean(network.getNode(i),stateInputs);
            }
            else{
                newStates[i]=network.getNode(i).getStateDiscrete();
            }
        }
        network.setNetworkState(newStates);
        
    }
    
    public static int[] synchronousBoolean(Network network, int[] state){
        
        int inputNumber;
        int[] stateInputs;
        int[] newStates=new int[network.getN()];
        for(int i=0;i<network.getN();i++){
            stateInputs=new int[network.getNode(i).getKi()];
            for(int j=0;j<stateInputs.length;j++){
                inputNumber=network.getNode(i).getInputNode(j);
                stateInputs[j]=state[inputNumber];
            }
            

                newStates[i]=updateNodeBoolean(network.getNode(i),stateInputs);

                    
        }
        return Arrays.copyOf(newStates, newStates.length);        
    }    
    
    public static void asynchronousGeneralBoolean(Network network){
        
        int inputNumber,newState,selectedNode;
        int[] stateInputs;
        selectedNode=(int)(network.getN()*Math.random());
        stateInputs=new int[network.getNode(selectedNode).getKi()];
        for(int j=0;j<stateInputs.length;j++){
            inputNumber=network.getNode(selectedNode).getInputNode(j);
            stateInputs[j]=network.getNode(inputNumber).getStateDiscrete();
        }
        newState=updateNodeBoolean(network.getNode(selectedNode),stateInputs);
        network.getNode(selectedNode).setStateDiscrete(newState);
                    
    }
    
    public static int[] asynchronousGeneralBoolean(Network network, int[] state){
        
        int inputNumber,newState,selectedNode;
        int[] stateInputs;
        int[] resultingState;
        resultingState=Arrays.copyOf(state, state.length);
        selectedNode=(int)(network.getN()*Math.random());
        stateInputs=new int[network.getNode(selectedNode).getKi()];
        for(int j=0;j<stateInputs.length;j++){
            inputNumber=network.getNode(selectedNode).getInputNode(j);
            stateInputs[j]=state[inputNumber];
        }
        newState=updateNodeBoolean(network.getNode(selectedNode),stateInputs);
        resultingState[selectedNode]=newState;
        return resultingState;
                    
    }    

    public static int[] asynchronousGeneralBooleanKO(Network network, int[] state, int ko){
        
        int inputNumber,newState,selectedNode;
        int[] stateInputs;
        int[] resultingState;
        resultingState=Arrays.copyOf(state, state.length);
        selectedNode=(int)(network.getN()*Math.random());
        stateInputs=new int[network.getNode(selectedNode).getKi()];
        for(int j=0;j<stateInputs.length;j++){
            inputNumber=network.getNode(selectedNode).getInputNode(j);
            stateInputs[j]=state[inputNumber];
        }
        newState=updateNodeBoolean(network.getNode(selectedNode),stateInputs);
        resultingState[selectedNode]=newState;
        if(selectedNode==ko){resultingState[selectedNode]=0;}
        return resultingState;
                    
    }    

    
    
    public static void asynchronousRandomOrderBoolean(Network network){
                
        int inputNumber,newState,selectedNode;
        int[] stateInputs;
        int[] order=OtherMethods.getRandomOrder(network.getN());
        for(int i=0;i<network.getN();i++){
            selectedNode=order[i];
            stateInputs=new int[network.getNode(selectedNode).getKi()];
            for(int j=0;j<stateInputs.length;j++){
                inputNumber=network.getNode(selectedNode).getInputNode(j);
                stateInputs[j]=network.getNode(inputNumber).getStateDiscrete();
            }

            newState=updateNodeBoolean(network.getNode(selectedNode),stateInputs);
            network.getNode(selectedNode).setStateDiscrete(newState);
            

        }
        
    }
    
    public static int updateNodeBoolean(Node node,int[] stateInputs){
        int stateNumber,newState;
        stateNumber=OtherMethods.binaryToInt(stateInputs);
        newState=node.getRegulatoryFunction(stateNumber);
        return newState;
        
    }
    
        public static int updateSingleNodeBoolean(Network network, int[] state, int nodeIndex){
        
        int inputNumber;
        int[] stateInputs;
        int[] newStates=new int[network.getN()];
        stateInputs=new int[network.getNode(nodeIndex).getKi()];
        for(int j=0;j<stateInputs.length;j++){
            inputNumber=network.getNode(nodeIndex).getInputNode(j);
            stateInputs[j]=state[inputNumber];
        }

            newStates[nodeIndex]=updateNodeBoolean(network.getNode(nodeIndex),stateInputs);

                    
        
        return newStates[nodeIndex];        
    }    
    
    
}
