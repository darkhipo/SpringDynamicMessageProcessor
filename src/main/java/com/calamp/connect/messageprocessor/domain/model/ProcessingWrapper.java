/**
    Dmitri, Arkhipov
    Aug 27, 2015
 **/

package com.calamp.connect.messageprocessor.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.calamp.connect.messageprocessor.Util;

public class ProcessingWrapper<I> {

    private UUID siIdent;
    private List<String> transitPath;
    private List<String> futurePath;
    private Exception wrappedException;
    private I dataPayload;

    public ProcessingWrapper(UUID siIdent, I dataPayload, List<String> planPath) {
        super();
        this.wrappedException = null;
        this.siIdent = siIdent;
        this.dataPayload = dataPayload;
        this.transitPath = new ArrayList<String>();
        this.futurePath = new ArrayList<String>();

        if (planPath != null) {
            this.futurePath.addAll(planPath);
        }
    }

    public <O> ProcessingWrapper(ProcessingWrapper<O> copyMe) {
        super();
        this.wrappedException = copyMe.getWrappedException();
        this.siIdent = UUID.fromString(copyMe.getSiIdent().toString());
        this.transitPath = Util.copyStringList(copyMe.getTransitPath());
        this.futurePath = Util.copyStringList(copyMe.getFuturePath());

        if (copyMe.getDataPayload().getClass() == this.getClass()) {
            // Shallow Copy.
            this.dataPayload = (I) copyMe.getDataPayload();
        } else {
            // Assume that in this case data is set through "setData"!
            this.dataPayload = null;
        }
    }

    public ProcessingWrapper(Exception e) {
        this.wrappedException = e;
    }

    public String advance(String step) {
        ProcessingWrapper.assertIndexCorrect(futurePath.get(0), step);
        if (futurePath.size() > 0) {
            String advancingStep = nextStepPop();
            transitPath.add(advancingStep);
            return advancingStep;
        }
        return null;
    }

    // When null is returned that means this was the last stage.
    public String nextStepPeek() {
        if (this.futurePath.size() > 0) {
            return this.futurePath.get(0);
        }
        return null;
    }

    private String nextStepPop() {
        if (this.futurePath.size() > 0) {
            return this.futurePath.remove(0);
        }
        return null;
    }

    private static void assertIndexCorrect(String myName, String concretePresent) {
        if (!myName.equals(concretePresent)) {
            throw new IllegalStateException("In state [" + myName + "] with inconsistent path value: ["
                    + concretePresent + "]");
        }
    }

    @Override
    public String toString() {
        if (this.wrappedException == null) {
            return "CalAmpSIWrapper [siIdent=" + siIdent + ", transitPath=" + transitPath + ", futurePath="
                    + futurePath + ", dataPayload=" + dataPayload + "]";
        } else {
            return "ErrorCalAmpSIWrapper " + this.wrappedException;
        }
    }

    public final UUID getSiIdent() {
        return this.siIdent;
    }

    public List<String> getTransitPath() {
        return Util.copyStringList(this.transitPath);
    }

    public List<String> getFuturePath() {
        return futurePath;
    }

    public Exception getWrappedException() {
        return wrappedException;
    }

    public I getDataPayload() {
        return dataPayload;
    }

    public void setSiIdent(UUID siIdent) {
        this.siIdent = siIdent;
    }

    public void setFuturePath(List<String> futurePath) {
        this.futurePath = futurePath;
    }

    public void setDataPayload(I data) {
        this.dataPayload = data;
    }

}