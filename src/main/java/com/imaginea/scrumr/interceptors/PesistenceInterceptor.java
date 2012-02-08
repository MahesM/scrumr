package com.imaginea.scrumr.interceptors;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.imaginea.scrumr.entities.IAuditable;

@SuppressWarnings("serial")
public class PesistenceInterceptor extends EmptyInterceptor {
	
	public static final Logger LOGGER = Logger.getLogger(PesistenceInterceptor.class);
	
    private static final String AUDIT = "Audit";
    private Set inserts = new LinkedHashSet();
    private Set updates = new LinkedHashSet();
	
	@Override
    public boolean onSave(Object inEntityObject, Serializable inPrimaryKey, Object[] inFieldValues, String[] inFieldIds, Type[] inTypes) throws CallbackException {
        
		boolean valuesChanged = false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(inEntityObject instanceof IAuditable){
            //this is auditable
            
        	for(int i=0; i<inFieldIds.length;i++){
                LOGGER.debug("Field name = " + inFieldIds[i] + "   value = " +inFieldValues[i]);
                if(inFieldIds[i].equalsIgnoreCase("createdOn") ){
                    inFieldValues[i] = new Date();
                    valuesChanged = true;
                }
                if(inFieldIds[i].equalsIgnoreCase("createdBy") ){
                    if(auth != null){
                        inFieldValues[i] = auth.getName();
                    }
                    valuesChanged = true;
                }

            }

            inserts.add(inEntityObject);

        } else {
        	//this is auditable
            for(int i=0; i<inFieldIds.length;i++){
            	LOGGER.debug("Field name = " + inFieldIds[i] + "   value = " +inFieldValues[i]);
                if(inFieldIds[i].equalsIgnoreCase("createdOn") ){
                    inFieldValues[i] = new Date();
                    valuesChanged = true;
                }
                if(inFieldIds[i].equalsIgnoreCase("createdBy") ){
                    if(auth != null){
                        inFieldValues[i] = auth.getName();
                    }
                    valuesChanged = true;
                }
            }

        }
        
        return valuesChanged;
        
    }

	/*

    @Override
    public boolean onFlushDirty(Object inEntityObject, Serializable inPrimaryKey, Object[] inFieldValues,
                          Object[] inFieldOldValues, String[] inFieldIds, Type[] inTypes) throws CallbackException {
        
    	boolean valuesChanged = false;        

        
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(inEntityObject instanceof IAuditable){
            //this is auditable
        	for(int i=0; i<inFieldIds.length;i++){
                LOGGER.debug("Field name = " + inFieldIds[i] + "   value = " +inFieldValues[i]);
                if(inFieldIds[i].equalsIgnoreCase("updatedOn")){
                    inFieldValues[i] = new Date();
                    valuesChanged = true;
                }
                if(inFieldIds[i].equalsIgnoreCase("updatedBy")){
                    if(auth != null){
                        inFieldValues[i] = auth.getName();
                    }
                    valuesChanged = true;
                }
            }
        	
            updates.add(inEntityObject);
            
        } else {
        	//this is not auditable
        	for(int i=0; i<inFieldIds.length;i++){
                LOGGER.debug("Field name = " + inFieldIds[i] + "   value = " +inFieldValues[i]);
                if(inFieldIds[i].equalsIgnoreCase("updatedOn")){
                    inFieldValues[i] = new Date();
                    valuesChanged = true;
                }
                if(inFieldIds[i].equalsIgnoreCase("updatedBy")){
                    if(auth != null){
                        inFieldValues[i] = auth.getName();
                    }
                    valuesChanged = true;
                }
            }
        	
        }
        
        
        return valuesChanged;
        
    }

    

    @Override
    public void afterTransactionCompletion(Transaction tx){
        if(tx.wasRolledBack()){
            inserts.clear();
            updates.clear();
        }
    }
    
    @Override
    public void postFlush(Iterator iterator){
        try{
            for (Iterator it = 	inserts.iterator(); it.hasNext();) {
                IAuditable entity = (IAuditable) it.next();
                
                System.out.println("postFlush - insert");
            }
            for (Iterator it = updates.iterator(); it.hasNext();) {
                IAuditable entity = (IAuditable) it.next();
                System.out.println("postFlush - update");
               
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
		    inserts.clear();
		    updates.clear();
	    }
    }
	
	*/

}
