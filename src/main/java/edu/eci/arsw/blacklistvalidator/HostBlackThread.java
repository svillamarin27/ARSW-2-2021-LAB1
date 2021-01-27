package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HostBlackThread extends Thread {
	public int A;
	public int B;
	public String ipaddress;
	int ocurrencesCount=0;
	int checkedListCount=0;
	private static final int BLACK_LIST_ALARM_COUNT=5;
	LinkedList<Integer> blackListOcurrences=new LinkedList<>();
	
	public HostBlackThread(String ipaddress, int A, int B) {
		this.A = A;
		this.B = B;
		this.ipaddress = ipaddress;
		
	}
	
	public void run() {

        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();

        
        for (int i=A;i<=B && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            
            if (skds.isInBlackListServer(i, ipaddress)){
                
                blackListOcurrences.add(i);
                
                checkedListCount++;
                ocurrencesCount++;
            }
        }               
        
	}
	
	public int getOcurrences() {
		
		return ocurrencesCount;
	}
	
	public int getCheckedListCount() {
		
		return checkedListCount;
	}
	
	public LinkedList<Integer> getBlackListOcurrences(){
		
		return blackListOcurrences;
	}
}
