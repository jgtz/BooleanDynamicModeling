package booleandynamicmodeling;


import java.util.ArrayList;
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


public class Attractor {
    
    Network network;
    String update;
    ArrayList<Integer[][]> attractors;
    ArrayList<Integer>[] stateSpace;
    ArrayList<String>[] stateSpaceString;
    int[][] stateSpaceInt;
    ArrayList<Integer[][]> attractorsSample;
    ArrayList<String[]> attractorsReduction;
    
    public Attractor(Network network, String update){
        
        this.network=new Network(network);
        this.update=update;
    
    }
    
 
    public boolean compareAttractors(int[][] attractor1, int[][] attractor2){
        boolean same=false;
        if(attractor1.length==attractor2.length){            
            if(attractor1.length==1){
                if(Arrays.equals(attractor1[0], attractor2[0])){
                    same=true;
                }
            }
            else{
                for(int i=0;i<attractor1.length;i++){
                    for(int j=0;j<attractor2.length;j++){
                        if(Arrays.equals(attractor1[i], attractor2[j])){
                            same=true;
                            break;
                        }
                    }
                    if(same){
                        break;
                    }
                }
            }        
        }
                
        return same;
    
    }
    
    public boolean compareAttractorsSample(int[][] attractor1, int[][] attractor2){
        boolean same=false;          
        if(attractor1.length==1){
            if(Arrays.equals(attractor1[0], attractor2[0])){
                same=true;
            }
        }
        else{
            for(int i=0;i<attractor1.length;i++){
                for(int j=0;j<attractor2.length;j++){
                    if(Arrays.equals(attractor1[i], attractor2[j])){
                        same=true;
                        break;
                    }
                }
                if(same){
                    break;
                }
            }
        }        
        
                
        return same;
    
    }    
    
    public boolean containsAttractorSample(int[][] attractor){
        boolean contains=false;
        Integer[][] attractor2;
        int[][] attractorDummy;
        for(int attractorNumber=0;attractorNumber<this.attractorsSample.size();attractorNumber++){
            attractor2=attractorsSample.get(attractorNumber);
            attractorDummy=new int[attractor2.length][];
            for(int i=0;i<attractor2.length;i++){
                attractorDummy[i]=new int[attractor2[i].length];
                for(int j=0;j<attractor2[i].length;j++){
                    attractorDummy[i][j]=attractor2[i][j];
                }
            }
            contains=compareAttractorsSample(attractorDummy,attractor);
            if(contains){break;}
        }
        
        return contains;
    
    }    
 
    public boolean containsAttractor(int[][] attractor){
        boolean contains=false;
        Integer[][] attractor2;
        int[][] attractorDummy;
        for(int attractorNumber=0;attractorNumber<this.attractorsSample.size();attractorNumber++){
            attractor2=attractorsSample.get(attractorNumber);
            attractorDummy=new int[attractor2.length][];
            for(int i=0;i<attractor2.length;i++){
                attractorDummy[i]=new int[attractor2[i].length];
                for(int j=0;j<attractor2[i].length;j++){
                    attractorDummy[i][j]=attractor2[i][j];
                }
            }
            contains=compareAttractors(attractorDummy,attractor);
            if(contains){break;}
        }
        
        return contains;
    
    }        
    

    public ArrayList<Integer[][]> getAttractors(){
        ArrayList<Integer[][]> attractorsCopy=new ArrayList<Integer[][]>();
        for(int i=0;i<attractors.size();i++){
            attractorsCopy.add(attractors.get(i));
        }
        return attractorsCopy;
           
    }
    
        public void setReducedAttractors(ArrayList<String[]> rAttractors){
        this.attractorsReduction=new ArrayList<String[]>();
        for(int i=0;i<rAttractors.size();i++){
            this.attractorsReduction.add(rAttractors.get(i));
        }           
    }
        
 
}
