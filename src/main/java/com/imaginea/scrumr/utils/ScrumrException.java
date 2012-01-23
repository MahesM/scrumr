package com.imaginea.scrumr.utils;

import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ScrumrException extends Exception{
    private String _message;
    private MessageLevel _messageLevel;
    private Exception _nextException;

    public MessageLevel getMessageLevel() {
        return _messageLevel;
    }

    public void setMessageLevel(MessageLevel _messageLevel) {
        this._messageLevel = _messageLevel;
    }

    public Exception getNextException() {
        return _nextException;
    }

    public void setNextException(Exception _inNextException) {
        this._nextException = _inNextException;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String inMessage) {
        this._message = inMessage;
    }

    private ScrumrException(String inMessage, MessageLevel inLevel, Exception inException){
        super(inMessage,inException);
        this._message = inMessage;
        this._messageLevel = inLevel;
        this._nextException = inException;
    }

    public Iterator iterator() {
        final ScrumrException exception = this;
        Iterator it = new Iterator() {
            public boolean hasNext() {
                return _currBv != null;
            }

            public Object next() {
                Exception myBv = _currBv;
                if(_currBv instanceof ScrumrException)
                	_currBv = ((ScrumrException) _currBv).getNextException();
                else
                    _currBv = null;
                return myBv;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            private Exception _currBv = exception;
        };
        return it;
    }

    public ScrumrException appendToChain(ScrumrException inException) {
        if (inException == null) {
            return this;
        } else {

            // find last link in the exception chain
            Exception thisLink = inException;
            Exception priorLink = inException;
            while (thisLink != null) {
                priorLink = thisLink;
                thisLink = ((ScrumrException) thisLink).getNextException();
            }

            // append self to the end
            if(priorLink instanceof ScrumrException)
            	((ScrumrException) priorLink).setNextException(this);
            return inException;
        }
    }
    /**
     * Creates a custom Scrumr exception
     * @param inMessage
     * @return
     */
    public static ScrumrException create(String inMessage){
        return new ScrumrException(inMessage, MessageLevel.SEVERE, null);
    }

    public static ScrumrException create(String inMessage, MessageLevel inLevel){
        return new ScrumrException(inMessage, inLevel, null);
    }

    public static ScrumrException create(String inMessage, MessageLevel inLevel, Exception inException){
        return new ScrumrException(inMessage, inLevel, inException);
    }
    
}
