package booleandynamicmodeling;

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

public class Main {


        public static void main(String[] args) throws ScriptException {
        
        String fileName="TLGLNetwork.txt";    
        int N;
        int IC=10000;
        int T=10000;
        int NApoptosis;
        int[] nodeStates;
        int ko,ko2;       
        System.out.println("\nFilename: "+fileName);
        System.out.println("Creating Boolean table directory: "+fileName.split("\\.")[0]);
        ReadWriteFiles.createTablesFromBooleanRules(fileName.split("\\.")[0], fileName);
        System.out.println("Boolean table directory created.");
        System.out.println("Creating functions and names files.");
        Network nw=OtherMethods.RecreateNetwork(fileName.split("\\.")[0]);        
        System.out.println("Functions and names files created.");
        System.out.println("Initial conditions: "+IC);
        System.out.println("Time steps: "+T);
        boolean Apo;
        nw.findNodeOutputs(); 
        N=nw.getN();
        nodeStates=new int[N];
            //Ceramide 4
            //SPHK1 14
            //PDGFR 18
            // TBET 24
            // RAS 55
            // GRB2 29
            // IL2RB 16
            // IL2RBT 9
            // ERK 47
            // MEK 45
            // PI3K 28                
            ko=4;
            ko2=18;
            NApoptosis=0;
            for(int r=0;r<IC;r++){
                Apo=false;
                for(int i=0;i<N;i++){nodeStates[i]=(int)(2*Math.random());}
                nodeStates[41]=1;    //IL15
                nodeStates[40]=0;    //CD45
                nodeStates[6]=1;    //Stimuli
                nodeStates[37]=0;    //PDGF
                nodeStates[3]=0;    //Stimuli2
                nodeStates[57]=0;    //TAX
                nodeStates[2]=0; //Apoptosis
                nodeStates[ko]=0;
                nodeStates[ko2]=1;
                for(int t=0;t<T;t++){
                    nodeStates=UpdateMethods.asynchronousGeneralBoolean(nw, nodeStates);
                    nodeStates[41]=1;    //IL15
                    nodeStates[40]=0;    //CD45
                    nodeStates[6]=1;    //Stimuli
                    nodeStates[37]=0;    //PDGF
                    nodeStates[3]=0;    //Stimuli2
                    nodeStates[57]=0;    //TAX
                    nodeStates[ko]=0;
                    nodeStates[ko2]=1;
                }
                for(int t=0;t<T;t++){
                    nodeStates=UpdateMethods.asynchronousGeneralBoolean(nw, nodeStates);
                    nodeStates[41]=1;    //IL15
                    nodeStates[40]=0;    //CD45
                    nodeStates[6]=1;    //Stimuli
                    nodeStates[37]=0;    //PDGF
                    nodeStates[3]=0;    //Stimuli2
                    nodeStates[57]=0;    //TAX                    
                }
                if(nodeStates[2]==1){Apo=true;}
                if(Apo){NApoptosis++;}
            }
            System.out.println(nw.getNode(ko).getName()+"=OFF\t"+nw.getNode(ko2).getName()+"=ON\tApoptosis fraction: "+(1.0*NApoptosis)/IC);
                
    }
  
       
}
