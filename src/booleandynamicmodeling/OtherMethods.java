package booleandynamicmodeling;

import fileOperations.FileToRead;
import java.util.Arrays;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
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

public class OtherMethods {
    
        public static int[] getRandomOrder(int N){
        int[] normalOrder=new int[N];
        int[] newOrder=new int[N];
        int index,temp;
        
        for(int n = 0; n < N; ++n){
            normalOrder[n]=n;
            newOrder[n]=n;
        }
        for(int n = 0; n < N; ++n){
            index=(int)((N-n)*Math.random());
            temp=normalOrder[N-n-1];
            newOrder[n]=normalOrder[index];
            normalOrder[N-n-1]=normalOrder[index];
            normalOrder[index]=temp;
        }
        return Arrays.copyOf(newOrder, N);
        
        
        
    }
    
    public static int searchIndex(String search, String[] dictionary){
		int i = -1;
		for(int n = 0; n < dictionary.length; ++n){
			if(search.equals(dictionary[n])){
				i = n;
				break;
			}
		}
		return i;
	}    
    
    
    public static String wildcardToFunction(int[][] wildcard, String[] inputs){
		String function = " ";
                String ANDpart;
		for(int i = 0; i < wildcard.length; i++){
                    ANDpart="( ";
                    for(int j = 0; j < wildcard[i].length; j++){         
                        if(wildcard[i][j]!=-1){
                            if(!ANDpart.equals("( ")){
                                ANDpart=ANDpart+" and ";
                            }
                            if(wildcard[i][j]==1){ANDpart=ANDpart+inputs[j];}
                            else{ANDpart=ANDpart+"~"+inputs[j];}                            
                        }                                                    
                    }                   
                    ANDpart=ANDpart+" )";
                    function=function+ANDpart;
                    if(i<wildcard.length-1){function=function+" or ";}
		}
		return function;
	}
            
    public static int binaryToInt(int[] binaryArray){
		int integerState = 0;
		int power = 1;

		for(int n = 0; n < binaryArray.length; ++n){
			integerState += (power*binaryArray[n]);
			power *= 2;
		}
		return integerState;
	}
        
    public static void intToBinary(int integerState, int[] binaryArray){
		for(int m = 0; m < binaryArray.length; ++m)
                    binaryArray[m] = 0;
		if(integerState > 0){
                    int p = 1;
                    int m = 0;
                    int res = integerState;
                    while(p <= integerState){
                        p *= 2;
			++m;
                    }
                    p /= 2;
                    --m;
                    while(m >= 0){
                        binaryArray[m] = res/p;
			if(p > 0)
                            res %= p;
			p /= 2;
			--m;
                    }
		}
	}
        
    public static Integer[] intToBinary(int integerState, int L){
                Integer[] binaryArray=new Integer[L];
		for(int m = 0; m < binaryArray.length; ++m){
                    binaryArray[m] = 0;
                }
		if(integerState > 0){
                    int p = 1;
                    int m = 0;
                    int res = integerState;
                    while(p <= integerState){
                        p *= 2;
			++m;
                    }
                    p /= 2;
                    --m;
                    while(m >= 0){
                        binaryArray[m] = res/p;
			if(p > 0)
                            res %= p;
			p /= 2;
			--m;
                    }
		}
                return binaryArray;
	}        
           
    public static boolean EvalStr(String expression, boolean booleanResult) throws ScriptException{
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        booleanResult=((Boolean) engine.eval(expression)).booleanValue();
        
        return booleanResult;
        
    }
  
    public static void intToBoolean(int[] intArray, boolean[] booleanArray){
        for(int i=0;i<intArray.length;i++){
            if(intArray[i]==0){booleanArray[i]=false;}
            else{booleanArray[i]=true;}
        }
    }
 
    public static int[] orderLowesttoHighest(int [] array, int initialPosition, int finalPosition){
    int shift;
    int index,element;
    int[] order=new int[array.length];
    int[] newArray=new int[array.length];
    for(int position=initialPosition;position<=finalPosition;position++){
        order[position]=position;
        newArray[position]=array[position];
    }
    
    for(int position=initialPosition+1;position<=finalPosition;position++){
        if(newArray[position-1]>newArray[position]){
            shift=0;
            while(newArray[position-1-shift]>newArray[position]){
                shift++;
                if(position-1-shift<0){break;}
            }
            index=order[position];
            element=newArray[position];
            for(int i=0;i<shift;i++){
                order[position-i]=order[position-i-1];
                newArray[position-i]=newArray[position-i-1];
            }
            order[position-shift]=index;
            newArray[position-shift]=element;
        }
    }
    return order;
}
    
    public static int[] orderHighesttoLowest(int [] array, int initialPosition, int finalPosition){
    int[] order;
    int[] reversedOrder;
    int counter;
    order=orderLowesttoHighest(array,initialPosition,finalPosition);
    reversedOrder=Arrays.copyOf(order, array.length);
    counter=finalPosition;
    for(int i=initialPosition;i<=finalPosition;i++){
        reversedOrder[counter]=order[i];
        counter--;
    }
    
    return reversedOrder;
}    

    public static int[] orderArraybyOrder(int [] array, int initialPosition, int finalPosition, int[] order){
    int [] newArray= new int[array.length];
        
    for(int i=initialPosition;i<=finalPosition;i++)
    {
        newArray[i]=array[order[i]];
    }
    
    return newArray;
}

    public static String[] orderArraybyOrder(String [] array, int initialPosition, int finalPosition, int[] order){
    String [] newArray= new String[array.length];
        
    for(int i=initialPosition;i<finalPosition;i++)
    {
        newArray[i]=array[order[i]];
    }
    
    return newArray;
}
    
    public static String[] ArrayByLength(String [] names){
        int N=names.length;
        String[] orderedNames;
        int [] lengths=new int[N];
        
        for(int n = 0; n < N; ++n){
            lengths[n]=names[n].length();        
        }
        
        orderedNames=orderArraybyOrder(names,0,N,orderHighesttoLowest(lengths,0,N-1));
        
        return orderedNames;
            
            
     }
    
        public static double getHammingDistance(int[] state, int[] state2){
        double hammingDistance=0;
        for(int i=0;i<state.length;i++){
            hammingDistance+=(state[i]==state2[i])?0:1;
        }
        hammingDistance/=(double)(state.length);
        
        return hammingDistance;
        
    }

    public static Network RecreateNetwork(String directory){
        
        Network network=new Network(directory);
        FileToRead fr=new FileToRead("Functions-"+directory+".txt");
        int N,index=0;        
        String node;
        String[] namesNetwork,names,functionsNetwork;
        while(fr.hasNext()){
            fr.nextLine();
            index++;
        }
        fr.close();
        N=index;       
        fr=new FileToRead("Names-"+directory+".txt");
        names=new String[N];
        namesNetwork=new String[N];
        functionsNetwork=new String[N];
        for(int i=0;i<N;i++){
            namesNetwork[i]=fr.nextLine();
        }
        fr.close();
        fr=new FileToRead("Functions-"+directory+".txt");
        for(int i=0;i<N;i++){
            functionsNetwork[i]=fr.nextLine();
        } 
        network.setFunctions(functionsNetwork);
        network.setNames(namesNetwork);
        fr.close();
        return network;
    }

}
