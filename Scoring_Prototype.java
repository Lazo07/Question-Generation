
package scoring_prototype;




import java.io.*;
import java.util.*;

//FOR STANFORD

import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.dcoref.*;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.AnnotatorFactories.*;
import edu.stanford.nlp.process.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.*;
import edu.stanford.nlp.util.*;

//FOR RITA WORDNET
import rita.*;
import rita.wordnet.*;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class Scoring_Prototype {

    /**
     * @param args the command line arguments
     */
    public String[] Score_and_Rank(String sentence_title, String sentence_par  ) throws IOException {
        
    //FOR RITA WORDNET
    RiWordnet wordnet = new RiWordnet();          

    //FOR STANFORD NLP
    Properties props = new Properties();
    props.setProperty("annotators","tokenize, ssplit, pos, lemma, ner, parse, dcoref");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);   
        
            
    
    float noun_counter = 0, verb_counter = 0, adj_counter = 0;
    int token_counter_index;
    
    int hubs_ctr = 0, auth_ctr = 0,hubs_loop,auth_loop; 

    //FOR NOUN COUNTER ARRAY LIST
    List<Float> Overall_score = new ArrayList<>();

    //FOR AUTHORITIES ARRAY
    int[][] auth_array = new int[30][2];

    //FOR HUBS ARRAY
    int[][] hubs_array = new int[30][2];

    //FOR OVERALL SCORE ARRAY
    int[][] overall_array = new int[30][2];

String title_text = sentence_title;
String second_text = sentence_par;



//ANNOTATE FIRST SENTENCE
Annotation annotation_first = new Annotation(second_text);
pipeline.annotate(annotation_first);
List<CoreMap> par_sentences = annotation_first.get(CoreAnnotations.SentencesAnnotation.class);

//ANNOTATE TITLE SENTENCE
Annotation annotation_second = new Annotation(title_text);
pipeline.annotate(annotation_second);
List<CoreMap> title_sentence = annotation_second.get(CoreAnnotations.SentencesAnnotation.class);

//GET COREF ANNOTATION
Map<Integer, CorefChain> coref = annotation_first.get(CorefChainAnnotation.class);

        

 //PRINT ALL THE CHAINS DCOREF
    for(int i=1;i<coref.size();i++)
    {
         System.out.println(coref.get(i));
    }

 for(Map.Entry<Integer, CorefChain> entry : coref.entrySet()) 
 
       {
            CorefChain c = entry.getValue();
            
                if(c.getMentionsInTextualOrder().size() <= 1)
            {
                continue;
            }
                
            CorefMention cm = c.getRepresentativeMention();
           // System.out.println("Cluster ID :"+ entry.getKey());
           // System.out.println(cm);//REPRESENTATIVE MENTION
            String clust = "";
            List<CoreLabel> tks = annotation_first.get(SentencesAnnotation.class).get(cm.sentNum-1).get(TokensAnnotation.class);
           
            for(int i = cm.startIndex-1; i < cm.endIndex-1; i++)
            {
//                System.out.println("start index :"+i);
//            clust += tks.get(i).get(TextAnnotation.class) + " ";
            clust = clust;
            
                                    System.out.println("SENTENCE NUMBER :"+cm.sentNum+" "+"representative mention: \"" + clust + "\" is mentioned by:");

                                                    for(CorefMention m : c.getMentionsInTextualOrder())
                                                    {
                                                         String clust2 = "";
                                                         tks = annotation_first.get(SentencesAnnotation.class).get(m.sentNum-1).get(TokensAnnotation.class);
                                                         
                                                                    for(int j = m.startIndex-1; j < m.endIndex-1; j++)
                                                                    {
                                                                           clust2 += tks.get(j).get(TextAnnotation.class) + " ";
                                                                           clust2 = clust2;
                                                                                          //don't need the self mention
                                                                                          if(clust.equals(clust2))
                                                                                          {
                                                                                              continue;
                                                                                          }
                                                                                          

                                                                           
                                                                    }
                                                                    if (cm.sentNum == m.sentNum)
                                                                    {
                                                                        continue;
                                                                    }
                                                                    
                                                                    if(cm.sentNum<=20 && m.sentNum<=20)
                                                                    {
                                                                            if (cm.sentNum != m.sentNum)
                                                                            {
                                                                                        if (hubs_array[m.sentNum-1][1] == 0 )   //CHECK IF THE HUBS_ARRAY HAS ALREADY HAS A SCORE AND HAS ALREADY POINTED TO AUTH_ARRAY
                                                                                        {
                                                                                              auth_array [cm.sentNum-1][1] += 1; 
                                                                                              hubs_array [m.sentNum-1][1] += 1; 
                                                                                        }


                                                                            }
                                                                    }
                                                                    
                                                                    
                                                         System.out.println("\t" + clust2+ " "+m.sentNum);
                                                     }
                                                    
                                                    
            }
           
        }//ENDS DCOREF LOOP

 
 
        System.out.println(" ");
        


for(CoreMap title_sen: title_sentence) 
     
        {
      
        
                       Tree tree_title = title_sen.get(TreeAnnotation.class);
                    
                        System.out.println(tree_title);
                        System.out.println("YIELD: "+tree_title.yield());
                        System.out.println(tree_title.labeledYield());
                        System.out.println(tree_title.constituents());
                        
                        
        for (CoreLabel title_token: title_sen.get(TokensAnnotation.class))
              {
                  String title  = title_token.get(TextAnnotation.class);
		  String title_pos = title_token.get(PartOfSpeechAnnotation.class);
         
                          System.out.println(title_sentence.indexOf(title_sen)+" "+title_pos.contentEquals("NN")+" "+title +" ");
                
                          
        
            System.out.println("");
                    for(CoreMap sentence: par_sentences)
                     
                    {
                        
                            
                                    for (CoreLabel token: sentence.get(TokensAnnotation.class))
                                    {
                                      
                                        String sent = token.get(TextAnnotation.class);
                                        String sent_pos = token.get(PartOfSpeechAnnotation.class);
                                        token_counter_index = par_sentences.indexOf(sentence); // ITERATIVE BECAUSE THE SENTENCE INDEX IS ITERATING
                                        System.out.println(token_counter_index+" "+sent_pos.contentEquals("NN")+" "+sent +" "+sent_pos);
                                        
                                                    if((title_pos.contentEquals("NN") && sent_pos.contentEquals("NN"))||(title_pos.contentEquals("NN") && sent_pos.contentEquals("NNS"))||(title_pos.contentEquals("NN") && sent_pos.contentEquals("NNP"))||(title_pos.contentEquals("NN") && sent_pos.contentEquals("NNPS"))||
                                                       (title_pos.contentEquals("NNS") && sent_pos.contentEquals("NN"))||(title_pos.contentEquals("NNS") && sent_pos.contentEquals("NNS"))||(title_pos.contentEquals("NNS") && sent_pos.contentEquals("NNP"))||(title_pos.contentEquals("NNS") && sent_pos.contentEquals("NNPS"))||
                                                       (title_pos.contentEquals("NNP") && sent_pos.contentEquals("NN"))||(title_pos.contentEquals("NNP") && sent_pos.contentEquals("NNS"))||(title_pos.contentEquals("NNP") && sent_pos.contentEquals("NNP"))||(title_pos.contentEquals("NNP") && sent_pos.contentEquals("NNPS"))||
                                                       (title_pos.contentEquals("NNPS") && sent_pos.contentEquals("NN"))||(title_pos.contentEquals("NNPS") && sent_pos.contentEquals("NNS"))||(title_pos.contentEquals("NNPS") && sent_pos.contentEquals("NNP"))||(title_pos.contentEquals("NNPS") && sent_pos.contentEquals("NNPS"))
                                                       )
                                                    
                                                    {
                                                        noun_counter = noun_counter + wordnet.getDistance(title, sent, "n");
                                                        overall_array [token_counter_index][1] += noun_counter;  
                                                        
                                                    }
                                                    
                                                    if(
                                                            (title_pos.contentEquals("VB") && sent_pos.contentEquals("VB"))||(title_pos.contentEquals("VB") && sent_pos.contentEquals("VBD"))||(title_pos.contentEquals("VB") && sent_pos.contentEquals("VBG"))||(title_pos.contentEquals("VB") && sent_pos.contentEquals("VBN"))||(title_pos.contentEquals("VB") && sent_pos.contentEquals("VBP"))||(title_pos.contentEquals("VB") && sent_pos.contentEquals("VBZ"))||
                                                            (title_pos.contentEquals("VBD") && sent_pos.contentEquals("VB"))||(title_pos.contentEquals("VBD") && sent_pos.contentEquals("VBD"))||(title_pos.contentEquals("VBD") && sent_pos.contentEquals("VBG"))||(title_pos.contentEquals("VBD") && sent_pos.contentEquals("VBN"))||(title_pos.contentEquals("VBD") && sent_pos.contentEquals("VBP"))||(title_pos.contentEquals("VBD") && sent_pos.contentEquals("VBZ"))||
                                                            (title_pos.contentEquals("VBG") && sent_pos.contentEquals("VB"))||(title_pos.contentEquals("VBG") && sent_pos.contentEquals("VBD"))||(title_pos.contentEquals("VBG") && sent_pos.contentEquals("VBG"))||(title_pos.contentEquals("VBG") && sent_pos.contentEquals("VBN"))||(title_pos.contentEquals("VBG") && sent_pos.contentEquals("VBP"))||(title_pos.contentEquals("VBG") && sent_pos.contentEquals("VBZ"))||
                                                            (title_pos.contentEquals("VBN") && sent_pos.contentEquals("VB"))||(title_pos.contentEquals("VBN") && sent_pos.contentEquals("VBD"))||(title_pos.contentEquals("VBN") && sent_pos.contentEquals("VBG"))||(title_pos.contentEquals("VBN") && sent_pos.contentEquals("VBN"))||(title_pos.contentEquals("VBN") && sent_pos.contentEquals("VBP"))||(title_pos.contentEquals("VBN") && sent_pos.contentEquals("VBZ"))||
                                                            (title_pos.contentEquals("VBP") && sent_pos.contentEquals("VB"))||(title_pos.contentEquals("VBP") && sent_pos.contentEquals("VBD"))||(title_pos.contentEquals("VBP") && sent_pos.contentEquals("VBG"))||(title_pos.contentEquals("VBP") && sent_pos.contentEquals("VBN"))||(title_pos.contentEquals("VBP") && sent_pos.contentEquals("VBP"))||(title_pos.contentEquals("VBP") && sent_pos.contentEquals("VBZ"))||
                                                            (title_pos.contentEquals("VBZ") && sent_pos.contentEquals("VB"))||(title_pos.contentEquals("VBZ") && sent_pos.contentEquals("VBD"))||(title_pos.contentEquals("VBZ") && sent_pos.contentEquals("VBG"))||(title_pos.contentEquals("VBZ") && sent_pos.contentEquals("VBN"))||(title_pos.contentEquals("VBZ") && sent_pos.contentEquals("VBP"))||(title_pos.contentEquals("VBZ") && sent_pos.contentEquals("VBZ"))
                                                       )
                                                    {
                                                             verb_counter = noun_counter + wordnet.getDistance(title, sent, "v");
                                                             overall_array [token_counter_index][1] += verb_counter;
                                                       
                                                           
                                                    }
                                                    
                                                    
                                                   /* if((title_pos.contentEquals("JJ") && sent_pos.contentEquals("JJ"))||(title_pos.contentEquals("JJ") && sent_pos.contentEquals("JJR"))||(title_pos.contentEquals("JJ") && sent_pos.contentEquals("JJS"))||
                                                       (title_pos.contentEquals("JJR") && sent_pos.contentEquals("JJ"))||(title_pos.contentEquals("JJR") && sent_pos.contentEquals("JJR"))||(title_pos.contentEquals("JJR") && sent_pos.contentEquals("JJS"))||
                                                       (title_pos.contentEquals("JJS") && sent_pos.contentEquals("JJ"))||(title_pos.contentEquals("JJS") && sent_pos.contentEquals("JJR"))||(title_pos.contentEquals("JJS") && sent_pos.contentEquals("JJS"))
                                                       )
                                                    
                                                    {
                                                        
                                                       
                                                                try {
                                                                                if(Overall_score.get(token_counter_index)!= null)
                                                                                {   

                                                                                    adj_counter = Overall_score.get(token_counter_index);
                                                                                    adj_counter = noun_counter + wordnet.getDistance(title, sent, "a");
                                                                                    System.out.println("DISTANCE SET "+adj_counter);
                                                                                    Overall_score.set(token_counter_index, adj_counter);
                                                                                    adj_counter = 0;
                                                                                }

                                                                    } //END TRY
                                                                                catch ( IndexOutOfBoundsException e ) 
                                                                                {
                                                                                       adj_counter = wordnet.getDistance(title, sent, "n");
                                                                                       System.out.println("DISTANCE ADD "+adj_counter);
                                                                                       Overall_score.add(token_counter_index, adj_counter);
                                                                                       adj_counter = 0;
                                                                                }  
                                                    }*/
                                      
                                      
                                   
                                    }
                                    
                                    
                                    
                                                                
                                    
                                    
                     }
        
                 //   System.out.println("THIS IS THE FINAL SCORE"+" "+Overall_score);
                    
                                          
                }

          }//END TITLE_SENTENCE FOR EACH LOOP */


  
int x,y;
 int k;
                                        System.out.print("AUTHORITIES_SCORE :"+" ");
                                        for (x = 0; x<=19; x++)
                                        {
                                            System.out.print(auth_array[x][1]+" "); 
                                        }
                                                System.out.println(" ");
                                        System.out.print("HUBS_SCORE :"+" ");
                                        for (y = 0; y<=19; y++)
                                        {
                                            System.out.print(hubs_array[y][1]+" "); 
                                        }
                                            System.out.println(" ");
                                        System.out.print("OVERALL_SCORE :"+" ");
                                        for (k = 0; k<=19; k++)
                                        {
                                              System.out.print(overall_array[k][1]+" "); 
                                        }
                                        System.out.println(" ");
                                        
                                        System.out.println("Ranking Start------------------------------------------------------");
                                        System.out.println(" ");
                                        //String[][] values = new String[overall_array.length][4];
                                           /*columns:
                                        1: Sentence ID
                                        2: Overall
                                        3: Authority
                                        4: Hub
                                        
                                        */
                                        
                                        //Transferring of values to 2d Array

                                        Reader reader = new StringReader(second_text);
                                        DocumentPreprocessor dp = new DocumentPreprocessor(reader);
                                        List<String> sentenceList = new ArrayList<String>();

                                        for (List<HasWord> sentence : dp) {
                                           String sentenceString = Sentence.listToString(sentence);
                                           sentenceList.add(sentenceString);
                                        }
                                        String[][] values = new String[overall_array.length][4];
                                        int ctru = 0;
                                        for (String sentence : sentenceList) {

                                           values[ctru][0] = sentence;
                                           ctru++;
                                        }
                                        ctru = 0;
                                        
                                        for (int ctr=0; ctr < overall_array.length; ctr++){
                                            
                                            values[ctr][1] = Integer.toString(overall_array[ctr][1]);
                                            values[ctr][2] = Integer.toString(auth_array[ctr][1]);
                                            values[ctr][3] = Integer.toString(hubs_array[ctr][1]);
                                        }
                                        
                                        
                                        //Pass Value to Ranking Class
                                        
                                        Ranking r = new Ranking (values);
        
                                        //Pass the rank values to "ranked_values" string array
                                        String[] ranked_values = r.DoRanking();
                                        
                                        
                                        System.out.println("TOP 10 Sentences--------------------------------------------------");
                                        
                                        for (int ctr = 0; ctr < 15; ctr++) {
                                           System.out.println(ranked_values[ctr]);
                                        }
        
                                        
                                        
                                        
                                        
                                        
                                        
                                        
                                        
                                        
                                        
                                        
                                        
                                        
                                //     Overgen ov = new Overgen();
                                 //    ov.Question(ranked_values);
                                        
                                       
//                                   Overgeneration_Prototype o = new Overgeneration_Prototype();
//                                         
//                                           Overgeneration_Prototype.main(args);
                                      
                                
                                               
                                //C:\\Users\\Kevin\\Documents\\NetBeansProjects\\Scoring_Prototype\\question-generation.jar               
                                               
                                       
        
        
        
        
        
        return ranked_values;
        
        
    }
    
    
                
   }
    



/*
try{

} 
catch(NoSuchMethodError e){
}
*/