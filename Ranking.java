

import java.io.*;
import java.util.*;
import java.util.Comparator;
   

public class Ranking {
    
    private String[][] values;   // original values
    private String[][] values_authority, values_hub; //will be used as containers for sorted values based on 
                                                     //     authority and hub scores.
    private String[][] ranked_values;   
    private String[] ave_values;
    
    public static int sentence_counter = 0;
    //Instance of Ranking ; get values
    public Ranking (String[][] s){
        values = values_authority = values_hub = s;
    }
    
    //Do the Ranking ; action method
    String[] DoRanking (){
        
        

        Arrays.sort(values_authority, new Comparator<String[]>() {
            @Override
            public int compare( String[] entry1,  String[] entry2) {
                String time1 = entry1[2];
                String time2 = entry2[2];
                return -Double.compare(Double.parseDouble(entry1[2]),Double.parseDouble(entry2[2]));
            }
        });

        for (String[] s : values_authority) {
            System.out.println(s[0] + " " + s[1] + " " + s[2] + " " + s[3]);
            
            
            if (s[0] != null){sentence_counter++;}
        
        }
        
         
        
        
       /* System.out.println("Hub");
        
        Arrays.sort(values_hub, new Comparator<String[]>() {
            @Override
            public int compare( String[] entry1,  String[] entry2) {
                 String time1 = entry1[3];
                 String time2 = entry2[3];
                return time1.compareTo(time2);
            }
        });
        

        for ( String[] s : values_hub) {
            System.out.println(s[0] + " " + s[1] + " " + s[2] + " " + s[3]);
        }
        */
        String[][] container_values1 = new String [values.length][values[0].length]; //ctr2
       
        Boolean checker = true;
        
        container_values1 = new String [values.length][values[0].length]; //ctr2
       
        checker = true;
        
        for(int ctr = 0, ctr2 = 0, ctr3 = 0; ctr < values.length; ctr++){
           
            container_values1[ctr2][0] = values_authority[ctr][0];
            container_values1[ctr2][1] = values_authority[ctr][1];
            container_values1[ctr2][2] = values_authority[ctr][2];
            container_values1[ctr2][3] = values_authority[ctr][3];
            ctr2++;
            
            
            if(ctr == 0){//left
                if( Integer.valueOf(values_authority[ctr][2]) == Integer.valueOf(values_authority[ctr+1][2])){
                    
                    
                    continue;
                }
              
                    
                    
                    else{
            
                        checker = false;
                    }
                
            }
            
            else if (ctr == values.length-1){//right
                if( Integer.valueOf(values_authority[ctr][2]) == Integer.valueOf(values_authority[ctr-1][2])){
                    
                    checker = false;
                }
                
                    
                
                
            } 
            else{//center
                if(((ctr != values.length-1)&&( ctr != 0)) && (ctr2 == 1)){
                    if( (Integer.valueOf(values_authority[ctr][2]) == Integer.valueOf(values_authority[ctr+1][2]))
                            &&(Integer.valueOf(values_authority[ctr][2]) != Integer.valueOf(values_authority[ctr-1][2]))){
                        
                        container_values1[ctr2][0] = values_authority[ctr][0];
                        continue;
                    }
                    else if((Integer.valueOf(values_authority[ctr][2]) == (Integer.valueOf(values_authority[ctr-1][2])))){
                        
                        checker = false;
                        
                    }
                    else{
                        checker = false;
                    }
                    
                }
                
                else if(((ctr != values.length-1)&&( ctr != 0)) && (ctr2 > 1)){
                    if( Integer.valueOf(values_authority[ctr][2]) == Integer.valueOf(values_authority[ctr+1][2])){
                        
                        continue;
                    }
                    else if ( Integer.valueOf(values_authority[ctr][2]) == Integer.valueOf(values_authority[ctr-1][2])){
                        
            
                        checker = false;
                    }
                    else
                    {
                        
                        checker = false;
                    }
                }
                
                
            }
            if(checker == false){
                String[][] container_values2 = new String [ctr2][values[0].length];
               
                for(int ctr_ctr = 0; ctr_ctr < ctr2; ctr_ctr++){
                    
                    
                    container_values2[ctr_ctr][0] = container_values1[ctr_ctr][0];
                    container_values2[ctr_ctr][1] = container_values1[ctr_ctr][1];
                    container_values2[ctr_ctr][2] = container_values1[ctr_ctr][2];
                    container_values2[ctr_ctr][3] = container_values1[ctr_ctr][3];
                }
                
                
                
                Arrays.sort(container_values2, new Comparator<String[]>() {
                    @Override
                    public int compare(String[] entry1,String[] entry2) {
                        String time1 = entry1[1];
                        String time2 = entry2[1];
                        return -Double.compare(Double.parseDouble(entry1[1]),Double.parseDouble(entry2[1]));
                    }
                });
                
                
                int ctr_ctr2 = ctr - (ctr2 - 1);
                
                for(int ctr_ctr = 0; ctr_ctr < ctr2 ; ctr_ctr++){
                    values_authority[ctr_ctr2][0] = container_values2[ctr_ctr][0];
                    values_authority[ctr_ctr2][1] = container_values2[ctr_ctr][1];
                    values_authority[ctr_ctr2][2] = container_values2[ctr_ctr][2];
                    values_authority[ctr_ctr2][3] = container_values2[ctr_ctr][3];
                    ctr_ctr2++;
                }
                //Reset for next round
                container_values1 = new String [values.length][values[0].length];
                checker = true;
                ctr2 = 0;  
                
            }//if
        
        }  //for
        
        System.out.println("values_authority--------------------------------------------------");
        for (String[] s : values_authority) {
            System.out.println(s[0] + " " + s[1] + " " + s[2] + " " + s[3]);
            
        }
        
        int x = values.length; //number of final sentences per rank
        int y = x*2; // number x 2 as container
        int checker2 = 0; 
                
       String[] final_values = new String [y];
        for (int ctr = 0; ctr < x; ctr++){
            final_values[ctr] = values_authority[ctr][0];
            checker2++;
        }
        
        //----HUB----------------------------------------------------------------------------------------------------    
        
        String [][] values_hub = values;
        
        
        Arrays.sort(values_hub, new Comparator<String[]>() {
            @Override
            public int compare( String[] entry1,  String[] entry2) {
                 String time1 = entry1[3];
                 String time2 = entry2[3];
                return -Double.compare(Double.parseDouble(entry1[3]),Double.parseDouble(entry2[3]));
            }
        });

        
        
        
        container_values1 = new String [values.length][values[0].length]; //ctr2
       
        checker = true;
        
        for(int ctr = 0, ctr2 = 0, ctr3 = 0; ctr < values.length; ctr++){
           
            container_values1[ctr2][0] = values_hub[ctr][0];
            container_values1[ctr2][1] = values_hub[ctr][1];
            container_values1[ctr2][2] = values_hub[ctr][2];
            container_values1[ctr2][3] = values_hub[ctr][3];
            ctr2++;
            
            if(ctr == 0){//left
                if( Integer.valueOf(values_hub[ctr][3]) == Integer.valueOf(values_hub[ctr+1][3])){
                    
                    
                    continue;
                }
              
                    
                    
                    else{
            
                        checker = false;
                    }
                
            }
            
            else if (ctr == values.length-1){//right
                if( Integer.valueOf(values_hub[ctr][3]) == Integer.valueOf(values_hub[ctr-1][3])){
                    
                    checker = false;
                }
                
                    
                
                
            } 
            else{//center
                if(((ctr != values.length-1)&&( ctr != 0)) && (ctr2 == 1)){
                    if( (Integer.valueOf(values_hub[ctr][3]) == Integer.valueOf(values_hub[ctr+1][3]))
                            &&(Integer.valueOf(values_hub[ctr][3]) != Integer.valueOf(values_hub[ctr-1][3]))){
                        container_values1[ctr2][0] = values_hub[ctr][0];
                        
                        continue;
                    }
                    else if((Integer.valueOf(values_hub[ctr][3]) == (Integer.valueOf(values_hub[ctr-1][3])))){
                        
                        checker = false;
                        
                    }
                    
                }
                
                else if(((ctr != values.length-1)&&( ctr != 0)) && (ctr2 > 1)){
                    if( Integer.valueOf(values_hub[ctr][3]) == Integer.valueOf(values_hub[ctr+1][3])){
                        
                        continue;
                    }
                    else if ( Integer.valueOf(values_hub[ctr][3]) == Integer.valueOf(values_hub[ctr-1][3])){
                        
            
                        checker = false;
                    }
                    else
                    {
                        
                        checker = false;
                    }
                }
                
                
            }
            if(checker == false){
                String[][] container_values2 = new String [ctr2][values[0].length];
               
                for(int ctr_ctr = 0; ctr_ctr < ctr2; ctr_ctr++){
                    
                    
                    container_values2[ctr_ctr][0] = container_values1[ctr_ctr][0];
                    container_values2[ctr_ctr][1] = container_values1[ctr_ctr][1];
                    container_values2[ctr_ctr][2] = container_values1[ctr_ctr][2];
                    container_values2[ctr_ctr][3] = container_values1[ctr_ctr][3];
                }
                
                
                
                Arrays.sort(container_values2, new Comparator<String[]>() {
                    @Override
                    public int compare(String[] entry1,String[] entry2) {
                        String time1 = entry1[1];
                        String time2 = entry2[1];
                        return -Double.compare(Double.parseDouble(entry1[1]),Double.parseDouble(entry2[1]));
                    }
                });
                
                
                int ctr_ctr2 = ctr - (ctr2 - 1);
                
                for(int ctr_ctr = 0; ctr_ctr < ctr2 ; ctr_ctr++){
                    values_hub[ctr_ctr2][0] = container_values2[ctr_ctr][0];
                    values_hub[ctr_ctr2][1] = container_values2[ctr_ctr][1];
                    values_hub[ctr_ctr2][2] = container_values2[ctr_ctr][2];
                    values_hub[ctr_ctr2][3] = container_values2[ctr_ctr][3];
                    ctr_ctr2++;
                }
                //Reset for next round
                container_values1 = new String [values.length][values[0].length];
                checker = true;
                ctr2 = 0;  
                
            }//if
        
        }  //for
        
        System.out.println("values HUB--------------------------------------------------");
        for (String[] s : values_hub) {
            System.out.println(s[0] + " " + s[1] + " " + s[2] + " " + s[3]);
        }
        
        
        for (int ctr = 0; ctr < x; ctr++){
            for (int ctr2 = 0; ctr2 < checker2; ctr2++){
                if (values_hub[ctr][0] == final_values[ctr2]){
                    break;
                }
                else if ( (ctr2 == (x - 1)) && (values_hub[ctr][0] != final_values[ctr2])){
                    final_values[checker2-1] = values_hub[ctr][0];
                    checker2++;
                }
            }
        }
        System.out.println("TOP AVE--------------------------------------------------");
        sentence_counter = sentence_counter /2;
        System.out.println("SENTENCE COUNTER VALUE!!!!!!"+sentence_counter);
       // System.out.println("OUTPUT!!!!"+final_values[0]);
        
        
//        for (int ctr = 0; ctr < sentence_counter; ctr++){
//            ave_values[ctr] = final_values[ctr];
//        }
        
//        for (int ctr = 0; ctr < sentence_counter; ctr++) {
//            System.out.println(ave_values[ctr]);
//        }
        
        return (final_values);
        
    }//DoRanking
    
    

}//public class Ranking