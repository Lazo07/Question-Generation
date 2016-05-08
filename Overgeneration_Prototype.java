/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import edu.cmu.ark.AnalysisUtilities;
import edu.cmu.ark.GlobalProperties;
import edu.cmu.ark.InitialTransformationStep;
import edu.cmu.ark.Question;
import edu.cmu.ark.QuestionRanker;
import edu.cmu.ark.QuestionTransducer;
import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import weka.classifiers.functions.LinearRegression;
import edu.cmu.ark.ranking.WekaLinearRegressionRanker;
import edu.stanford.nlp.trees.Tree;

public class Overgeneration_Prototype {

    /**
     * @param args the command line arguments
     */
    
    
    
    public String[][] Question_Generation(String[] top_ten_sentences){
             
                String[][] final_questions_answers = new String[600][2];
                String[] top_ten = new String[11];
                String q_num1,q_num2;
                int x,y,z1,z2=0,q = 1;
                int output_ctr = 0;
                // STORE THE PASSED VARIABLE TO THE TOP_TEN ARRAY
                for(x = 0; x<10; x++){
                    
                  top_ten[x] = top_ten_sentences[x]; 
                    
                   
                }
                
                QuestionTransducer qt = new QuestionTransducer();
		InitialTransformationStep trans = new InitialTransformationStep();
		QuestionRanker qr = null;
		
		
		qt.setAvoidPronounsAndDemonstratives(false);
		
		//pre-load
		AnalysisUtilities.getInstance();
		
		String buf;
		Tree parsed;
		boolean printVerbose = true;
		String modelPath = "models/linear-regression-ranker-reg500.ser.gz";
                
		List<String> finaloutputQuestionList = new ArrayList<String>();
		
		boolean preferWH = false;
		boolean doNonPronounNPC = true;
		boolean doPronounNPC = true;
		Integer maxLength = 1000;
		boolean downweightPronouns = true;
		boolean avoidFreqWords = false;
		boolean dropPro = false;
		boolean justWH = false;
		
		qt.setAvoidPronounsAndDemonstratives(dropPro);
		trans.setDoPronounNPC(doPronounNPC);
		trans.setDoNonPronounNPC(doNonPronounNPC);
		
		
		System.err.println("Loading question ranking models from "+modelPath+"...");
		qr = new QuestionRanker();
		qr.loadModel(modelPath);
                
                List <String> sentences = new ArrayList<>();
                
                long startTime = System.currentTimeMillis();
    		
		try{
                                
                                                             
         for(y=0; y<11; y++){   
             
                                q_num1 = String.valueOf(y+1);
             
                                        List<Tree> inputTrees = new ArrayList<Tree>();
                                        if(top_ten[y]!=null){
					parsed = AnalysisUtilities.getInstance().parseSentence(top_ten[y]).parse;
					inputTrees.add(parsed); 
                                        }
                                        
				
				//step 1 transformations
				List<Question> transformationOutput = new ArrayList<Question>();
                                transformationOutput = trans.transform(inputTrees);
                                
				//step 2 question transducer
				for(Question t: transformationOutput){
				//	if(GlobalProperties.getDebug()) System.err.println("Stage 2 Input: "+t.getIntermediateTree().yield().toString());
					qt.generateQuestionsFromParse(t);
                                        List<Question> outputQuestionList = new ArrayList<Question>();
					outputQuestionList.addAll(qt.getQuestions());
							
				
				//remove duplicates
				QuestionTransducer.removeDuplicateQuestions(outputQuestionList);
				
				//step 3 ranking
				if(qr != null){
					qr.scoreGivenQuestions(outputQuestionList);
					boolean doStemming = true;
					QuestionRanker.adjustScores(outputQuestionList, inputTrees, avoidFreqWords, preferWH, downweightPronouns, doStemming);
					QuestionRanker.sortQuestions(outputQuestionList, false);
                                            }
                                
                                
                                
				
                                z1 = z2;
                                
				for(Question question: outputQuestionList){
                                         z1 = z2;
                                         q_num2 = String.valueOf(q);
					if(question.getTree().getLeaves().size() > maxLength){
						continue;
					}
					if(justWH && question.getFeatureValue("whQuestion") != 1.0){
						continue;
					}
                                        
				//	System.out.println("QUESTION FROM SENTENCE #"+(y+1)+")"+""+question.yield());//PRINTS THE QUESTIONS
                                        final_questions_answers[z1][0] = question.yield().concat("("+q_num1+"."+q_num2+")");
                                        
					Tree ansTree = question.getAnswerPhraseTree();//SETS ALL THE ANSWER PHRASES
				
					if(ansTree != null){
                                      //  System.out.println("ANSWER FOR SENTENCE # "+(y+1)+")"+" "+AnalysisUtilities.getCleanedUpYield(question.getAnswerPhraseTree()));
					final_questions_answers[z1][1] = AnalysisUtilities.getCleanedUpYield(question.getAnswerPhraseTree()).toString().concat("("+q_num1+"."+q_num2+")");
                                        
                                        }
//                                        
                                        
                                        System.out.println("Question :"+final_questions_answers[z1][0]);
                                        System.out.println("Answers :"+final_questions_answers[z1][1]);
                                        
//					System.out.println("\t"+question.getScore());
                                        /*
					System.err.println("Answer depth: "+question.getFeatureValue("answerDepth"));
					
					System.out.println 
                                        */
                                        
//                                         finaloutputQuestionList.add(z1, question.yield().toString());
                                         q++;
                                         z1++;
                                        
                                         z2 = z1; 
                                         
                                        }
                                
                                }
                                       
                                         
                             
                          }
                            //END OF INPUTING OF PARED TREES      
                             
		
		}catch(Exception e){
			e.printStackTrace();
	
//               for(int p = 0; p<200; p++){
//                   System.out.println("asasas"+final_questions[p]);
//               }
                }
                
      return final_questions_answers; 
    
    }
}



         











