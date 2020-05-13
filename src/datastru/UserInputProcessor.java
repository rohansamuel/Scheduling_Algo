package datastru;
import java.io.*;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Queue;




 class UserInputProcessor {
    
    public static  void main (String []args){
        
        Scanner scanner = new Scanner(System.in);
        String newline=System.getProperty("line.separator");
        System.out.print("Enter scheduling method: "+ newline +"\"FCFS\",\"RR\",\"SPN\",\"SRT\",\"HRRN\",\"FB\",\"ALL\"");
        List <String> processess = new LinkedList<>(); //list of processess
        List <String> list_arrivalTime = new LinkedList<>(); //list of arrival times
        List <String> list_durationTime = new LinkedList<>(); //duration of processess
        
        String schedulingMethod = scanner.next();

        File file = new File("./jobs.txt");
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null)// getting and seperating the list of process infos
            {
                String []jobinfos = st.split("\\s+");
                processess.add(jobinfos[0]);//adding processs to list
                list_arrivalTime.add(jobinfos[1]);//adding arrival time
                list_durationTime.add(jobinfos[2]);// adding duration time
            }
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        FCFS_Scheduler f =new FCFS_Scheduler();
        RRScheduler r= new RRScheduler();
        SPNScheduler sp= new SPNScheduler();
        HRRNScheduler hrrn=new HRRNScheduler();
        SRTScheduler srt= new SRTScheduler();
        MFBScheduler mfb=new MFBScheduler();
        
        //selecting the algorithm
        if(schedulingMethod.equals("FCFS")){
            f.FCFS(processess, list_arrivalTime, list_durationTime);
        } else if(schedulingMethod.equals("RR")){
            r.RR(processess, list_arrivalTime, list_durationTime, 1);
        }else if(schedulingMethod.equals("FB")){
            mfb.MFB(processess, list_arrivalTime, list_durationTime);
        }else if(schedulingMethod.equals("SPN")){
            sp.SPN(processess, list_arrivalTime, list_durationTime);
        }else if (schedulingMethod.equals("HRRN")) {
        	hrrn.processHRRNScheduling(processess, list_arrivalTime, list_durationTime);
        }else if(schedulingMethod.equals("SRT")){
            srt.SRT(processess, list_arrivalTime, list_durationTime);
        }else if (schedulingMethod.equals("SRT")){
        }else if(schedulingMethod.equals("ALL")){
        	f.FCFS(processess, list_arrivalTime, list_durationTime);
        	r.RR(processess, list_arrivalTime, list_durationTime, 1);
        	sp.SPN(processess, list_arrivalTime, list_durationTime);
        	hrrn.processHRRNScheduling(processess, list_arrivalTime, list_durationTime);
        	srt.SRT(processess, list_arrivalTime, list_durationTime);
        }
        
    }
    
   
}

 class FCFS_Scheduler {
	    
	    public  void FCFS(final List<String> processess, final List<String> list_arrivalTime, final List<String> list_durationTime){
	        printTable(processess, list_arrivalTime, list_durationTime);
	    }
	    
	    private  void printTable(final List<String> processess, final List<String> list_arrivalTime, final List<String> list_durationTime) {
	        System.out.println("");
	        System.out.println("FCFS");
	        
	        
	        int service_Time_Remaining = Integer.parseInt(list_arrivalTime.get(0));//getting the service time for processes
	        
	        int i=0;
	        while(i<processess.size()){ //do it while there is still processess
	            System.out.print(processess.get(i));
	            System.out.print(" ");
	            
	            if(Integer.parseInt(list_arrivalTime.get(i))> service_Time_Remaining) { //if the arrival time greater than service time
	            
	            }
	            int j=0;
	            while(j <= service_Time_Remaining) {
	            	System.out.print(" "); //while there is till serive time remaining
	            	j++;
	            }
	            int k=0;
	            int parseDuration=Integer.parseInt(list_durationTime.get(i));
	            while (k<parseDuration) { //while there is till duration time for processsess left, process the processess
	            	System.out.print("X");// finished taking the processess
	            	k++;
	            }

	            service_Time_Remaining = service_Time_Remaining + parseDuration;//new service time after processing the previous procesess
	            System.out.println(" ");
	            i++;
	        }
	        
	    }
	}


class RRScheduler {
    
    
    public void RR(final List<String> processess, final List<String> list_arrivalTime, final List<String> list_durationTime, final int quantum) {
        
        System.out.println("");
        System.out.println("RR");
        
        
        LinkedList<String> list_remainingTime = new LinkedList<>();//list of remaining time
        RRProcessleft(list_durationTime, list_remainingTime); //creating a table with duration and time remaining
        LinkedList<String> process_finishedInfo = new LinkedList<>();//list of finished processess
        RRProcessInfo(processess, process_finishedInfo);//table of proceesess and finished proceses
        
        int service_Time_Remaining = 0;
       
        
        while (true) {
            boolean process_finished = true;
            int i=0;
           
           
            while(i<processess.size()) {
                
                int job_timeRemaining = Integer.parseInt(list_remainingTime.get(i));
                int parseArrival=Integer.parseInt(list_arrivalTime.get(i));
                
                if (job_timeRemaining > 0) {   //checking if there is more time
                    process_finished = false;
                    
                    if (job_timeRemaining < quantum) { //while less than quantum time, process it
                    	
                        if (parseArrival <= service_Time_Remaining) { //finding the remaining time list of process
                            list_remainingTime.set(i, Integer.toString(0));//adding time to the list
                            String info = process_finishedInfo.get(i);//getting the details of the process
                            int infoLength = info.length();
                            
                            int j=0;
                            while (j <service_Time_Remaining - (infoLength-2)) {//going through the process
                            	 info = info + " ";
                            	 j++;                
                            }
                            
                           int k=0;
                           while(k<job_timeRemaining) {// adding the finished process
                        	   info = info + "X";
                        	   k++;
                           }
                           
                            service_Time_Remaining += job_timeRemaining;
                            process_finishedInfo.set(i, info);
                        }
                    	
                        
                    } else {
                        
                    	 if (parseArrival <= service_Time_Remaining) {// same thing but now adding the quantum time
                             
                             list_remainingTime.set(i, Integer.toString(job_timeRemaining - quantum));//change the process if the quantum time hit
                             String info = process_finishedInfo.get(i);
                             int infoLength = info.length();
                             
                             int j=0;
                             while(j<service_Time_Remaining- (infoLength-2)) {                         	
                             	info = info + " ";
                             	j++;
                             	
                             }
                      
                             int k=0;
                            while(k<quantum) {//finsish process while under quantum
                         	   info=info+ "X";
                         	   k++;
                            }
                         
                             service_Time_Remaining += quantum;
                             process_finishedInfo.set(i, info);//updated list
    
                         }
                    }
                }
                i++;
            }
           
            if (process_finished) {
                break;
            }
            
            
        }
        int j=0;
        while(j < process_finishedInfo.size()) {
            System.out.println(process_finishedInfo.get(j));//list of processesed processess
            j++;
        }
    }
    
    private  void RRProcessInfo(List<String> processess, List<String> process_finishedInfo) {
        for (int i = 0; i < processess.size(); i++) {
            process_finishedInfo.add(processess.get(i) + " ");//details of processess left to process
        }
    }
    
    private  void RRProcessleft(List<String> list_durationTime,  List<String> list_remainingTime) {
        for (String duration : list_durationTime) {
            list_remainingTime.add(duration);//process proceesess left
        }
    }
}


 class SPNScheduler {

	    public  void SPN(final List<String> processess, final List<String> list_arrivalTime, final List<String> list_durationTime) {

	        System.out.println("");
	        System.out.println("SPN");
	       
	        int finish = 0;//finished processess
	        int shortest_Process = 0;//shortest processess
	        int shortest_ProcessDuration = 0;//duration of shortest processess
	        LinkedList<Integer> finishedlist = new LinkedList<>();//list of finished list
	        LinkedList<String> process_finishedInfo = new LinkedList<>();
	        ProcessInfo(processess, process_finishedInfo);//processess left
	        int service_Time_Remaining = 0;//time remaining for service
	        boolean found = false;

	        while (finish!= processess.size()) {//run while there there is still processess

	            shortest_ProcessDuration = 0;
	            found = false;
	            int i=0;
	            while(i<processess.size()) {
	            	
	           
	            int parseArrival= Integer.parseInt(list_arrivalTime.get(i));//arrival time parsed
	            int parseDuration= Integer.parseInt(list_durationTime.get(i));//duration time parsed
	            //getting the shortest process
	            	while(parseArrival <= service_Time_Remaining && shortest_ProcessDuration == 0 && !finishedlist.contains(i) ) {
	                    shortest_ProcessDuration = Integer.parseInt(list_durationTime.get(i));
	                    shortest_Process = i;
	                    found = true;
	            	}

	                while (parseDuration < shortest_ProcessDuration && !finishedlist.contains(i) && parseArrival <= service_Time_Remaining) {
	                    shortest_ProcessDuration = Integer.parseInt(list_durationTime.get(i));
	                    shortest_Process = i;
	                    found = true;
	                }
	            i++;
	            }

	            if (found=true) {
	                String info = process_finishedInfo.get(shortest_Process);

	                if (service_Time_Remaining > 0) {
	                	int k=0;
	                   while(k < service_Time_Remaining) {
	                        info = info + " ";
	                        k++;
	                    }
	                }
	                int j=0;
	               while( j < shortest_ProcessDuration) {
	                    info = info + "X";
	                    service_Time_Remaining++;
	                    j++;
	                }
	                process_finishedInfo.set(shortest_Process, info);
	                finishedlist.add(shortest_Process);
	                finish++;
	            } 

	        }
	        int j=0;
	       while(j<process_finishedInfo.size()) {
	            System.out.println(process_finishedInfo.get(j));
	            j++;
	        }

	    }

	    private void ProcessInfo(List<String> processess, List<String> process_finishedInfo) {
	    	int i=0;
	    	while(i<processess.size()) {
	            process_finishedInfo.add(processess.get(i)+" ");
	            i++;
	        }
	    }
	}
 
 
 
 class SRTScheduler {

	    public   void SRT(final List<String> processess, final List<String> list_arrivalTime, final List<String> list_durationTime) {

	        System.out.println("");
	        System.out.println("SRT");
	       
	        int list_shortestRemainingTime = 0;
	        int service_Time_Remaining = 0;
	        int finished = 0;
	        LinkedList<Integer> list_remainingTime = new LinkedList<>();
	        populatelist_remainingTime(list_durationTime, list_remainingTime);
	        LinkedList<Integer> finishedlist = new LinkedList<>();
	        LinkedList<String> process_finishedinfos = new LinkedList<>();
	        fillprocess_finishedinfos(processess, process_finishedinfos);

	        
	       
	        for(int i=0;finished != processess.size();i++) {

	            list_shortestRemainingTime = get_list_shortestRemainingTime(list_remainingTime, list_arrivalTime, service_Time_Remaining, finishedlist);

	            if (list_shortestRemainingTime >= 0) {

	                String info = process_finishedinfos.get(list_shortestRemainingTime);
	                int infoLength = info.length() - 2;
	                int x=0;
	              while( x <= service_Time_Remaining - infoLength) {
	                    info = info + " ";
	                    x++;
	                }
	                process_finishedinfos.set(list_shortestRemainingTime, info + "X");
	                int currentRemainingTime = list_remainingTime.get(list_shortestRemainingTime);
	                list_remainingTime.set(list_shortestRemainingTime, currentRemainingTime - 1);

	                service_Time_Remaining++;
	                if(list_remainingTime.get(list_shortestRemainingTime) == 0) {
	                   finished++;
	                    finishedlist.add(list_shortestRemainingTime);
	                }
	            } else {
	               break;
	            }

	        }
	       
	        for (String info : process_finishedinfos) {
	            System.out.println(info);
	        }
	    }

	    private   int get_list_shortestRemainingTime(final List<Integer> list_remainingTime, final List<String> list_arrivalTime, final int service_Time_Remaining, final List<Integer> finishedlist) {
	        int shortestTime = 0;
	        boolean found = false;
	      
	       int i=0;
	        while(i < list_remainingTime.size()) {
	            if (!finishedlist.contains(i) && Integer.parseInt(list_arrivalTime.get(i)) <= service_Time_Remaining) {
	                shortestTime = i;
	                break;
	            }
	            i++;
	        }
	     
	       while (i< list_arrivalTime.size()) {

	            if (list_remainingTime.get(i) <= list_remainingTime.get(shortestTime) && Integer.parseInt(list_arrivalTime.get(i)) <= service_Time_Remaining && !finishedlist.contains(i)) {
	                if(list_remainingTime.get(i) < list_remainingTime.get(shortestTime)) {
	                    shortestTime = i;
	                }
	                found = true;
	            }
	            i++;
	        }
	        return shortestTime;
	    }

	    private   void fillprocess_finishedinfos(final List<String> processess, final List<String> process_finishedinfos) {
	        for (String process : processess) {
	            process_finishedinfos.add(process + " ");
	        }
	    }

	    private   void populatelist_remainingTime(final List<String> list_durationTime, final List<Integer> list_remainingTime) {

	        for (String duration : list_durationTime) {
	            list_remainingTime.add(Integer.parseInt(duration));
	        }
	    }
	}

 class HRRNScheduler {

     public void processHRRNScheduling(final List<String> processess, final List<String> list_arrivalTime, final List<String> list_durationTime) {

         System.out.println("");
         System.out.println("HRRN");
         

         LinkedList<Integer> finishedlist = new LinkedList<>();
         List<Integer> wait_Time = new LinkedList<>();
         int biggestRatio = 0;
         int finished = 0;
         int service_Time_Remaining = 0;
         List<String> process_finishedinfos = new LinkedList<>();
         list_watinglist(wait_Time, processess.size());
         fillprocess_finishedinfoList(processess, process_finishedinfos);

         
        for(finished=0;finished != processess.size();finished++) {
             list_newWaintinglist(wait_Time, list_arrivalTime, service_Time_Remaining);
             biggestRatio = shortest_ProcessList(wait_Time, list_arrivalTime, list_durationTime, finishedlist, service_Time_Remaining);
             while(biggestRatio < 0) {
            	 service_Time_Remaining++;
             } 
             if(biggestRatio >=0) {
            	 String info = process_finishedinfos.get(biggestRatio);
            	 int k=0;
                 while(k < service_Time_Remaining){
                     info = info + " ";
                     k++;
                 }
                 int j=0;
                 while(j< Integer.parseInt(list_durationTime.get(biggestRatio))) {
                     info = info + "X";
                     service_Time_Remaining++;
                     j++;
                 }
                 finishedlist.add(biggestRatio);
                 process_finishedinfos.set(biggestRatio, info);
             }
          
         }
        int s=0;
         while( s< process_finishedinfos.size()){
             System.out.println(process_finishedinfos.get(s));
             s++;
         }
     }
     private void fillprocess_finishedinfoList(final List<String> processess, final List<String> process_finishedinfos) {
         int i=0;
    	while( i< processess.size()){
             process_finishedinfos.add(processess.get(i) + " ");
             i++;
         }
     }

     private   void list_newWaintinglist(final List<Integer> wait_Time, final List<String> list_arrivalTime, final int service_Time_Remaining) {
         int i=0;
         
    	 while(i < list_arrivalTime.size()) {
    		 int parseArrival=Integer.parseInt(list_arrivalTime.get(i));
             if (parseArrival <= service_Time_Remaining) {
                 int waitingTime = service_Time_Remaining - parseArrival;
                 wait_Time.set(i, waitingTime);
             }
             i++;
         }
     }

     private  int shortest_ProcessList(final List<Integer> wait_Time, final List<String> list_arrivalTime, final List<String> list_durationTime, final List<Integer> finishedlist, int service_Time_Remaining) {
         int maximumRate = 0;
         int biggestRatio = -1;
         int i=0;
         
        while(i < list_arrivalTime.size()) {
        	int parseArrival=Integer.parseInt(list_arrivalTime.get(i));
        	int parseDuration=Integer.parseInt(list_durationTime.get(i));
        	
             int ratio = (wait_Time.get(i) + parseDuration) / parseDuration;
             while (ratio > maximumRate && !finishedlist.contains(i) && parseArrival<=service_Time_Remaining) {
                 maximumRate = ratio;
                 biggestRatio = i;
             }
             i++;
         }
         return biggestRatio;
     }

     private void list_watinglist(final List<Integer> wait_Time, final int processLeft) {

    	 int i=0;
         while(i < processLeft) {
             wait_Time.add(0);
             i++;
         }
     }
 }


 
 class MFBScheduler {

	 public  void MFB(final List<String> processess, final List<String> list_arrivalTime, final List<String> list_durationTime) {

	        System.out.println("");
	        System.out.println("FB");
	       
	        int finish = 0;//finished processess
	        int shortest_Process = 0;//shortest processess
	        int shortest_ProcessDuration = 0;//duration of shortest processess
	        LinkedList<Integer> finishedlist = new LinkedList<>();//list of finished list
	        LinkedList<String> process_finishedInfo = new LinkedList<>();
	        ProcessInfo(processess, process_finishedInfo);//processess left
	        int service_Time_Remaining = 0;//time remaining for service
	        boolean found = false;

	        while (finish!= processess.size()) {//run while there there is still processess

	            shortest_ProcessDuration = 0;
	            found = false;
	            int i=0;
	            while(i<processess.size()) {
	            	
	           
	            int parseArrival= Integer.parseInt(list_arrivalTime.get(i));//arrival time parsed
	            int parseDuration= Integer.parseInt(list_durationTime.get(i));//duration time parsed
	            //getting the shortest process
	            	while(parseArrival <= service_Time_Remaining && shortest_ProcessDuration == 0 && !finishedlist.contains(i) ) {
	                    shortest_ProcessDuration = Integer.parseInt(list_durationTime.get(i));
	                    shortest_Process = i;
	                    found = true;
	            	}

	                while (parseDuration < shortest_ProcessDuration && !finishedlist.contains(i) && parseArrival <= service_Time_Remaining) {
	                    shortest_ProcessDuration = Integer.parseInt(list_durationTime.get(i));
	                    shortest_Process = i;
	                    found = true;
	                }
	            i++;
	            }

	            if (found=true) {
	                String info = process_finishedInfo.get(shortest_Process);

	                if (service_Time_Remaining > 0) {
	                	int k=0;
	                   while(k < service_Time_Remaining) {
	                        info = info + " ";
	                        k++;
	                    }
	                }
	                int j=0;
	               while( j < shortest_ProcessDuration) {
	                    info = info + "X";
	                    service_Time_Remaining++;
	                    j++;
	                }
	                process_finishedInfo.set(shortest_Process, info);
	                finishedlist.add(shortest_Process);
	                finish++;
	            } 

	        }
	        int j=0;
	       while(j<process_finishedInfo.size()) {
	            System.out.println(process_finishedInfo.get(j));
	            j++;
	        }

	    }

	    private void ProcessInfo(List<String> processess, List<String> process_finishedInfo) {
	    	int i=0;
	    	while(i<processess.size()) {
	            process_finishedInfo.add(processess.get(i)+" ");
	            i++;
	        }
	    }
	}
