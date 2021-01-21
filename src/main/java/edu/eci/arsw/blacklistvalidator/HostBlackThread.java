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
	int ocurrencesCount;
	private static final int BLACK_LIST_ALARM_COUNT=5;
	
	public HostBlackThread(String ipaddress, int A, int B) {
		this.A = A;
		this.B = B;
		this.ipaddress = ipaddress;
		
	}
	
	public void run() {
		LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        
        ocurrencesCount=0;
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();

        
        for (int i=A;i<B && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            
            if (skds.isInBlackListServer(i, ipaddress)){
                
                blackListOcurrences.add(i);
                
                ocurrencesCount++;
            }
        }
        
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
	}
	
	public int getOcurrences() {
		
		return ocurrencesCount;
	}
}
