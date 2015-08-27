/**
    Dmitri, Arkhipov
    Aug 27, 2015
**/

package com.calamp.connect.messageprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.calamp.connect.messageprocessor.domain.model.ProcessingWrapper;
import com.calamp.connect.messageprocessor.domain.services.RouteAndProcessService;

public class Util {

    @SuppressWarnings("unused")
    private static String getDefaultBeanName(@SuppressWarnings("rawtypes") Class aClass) {
        String beanName = RouteAndProcessService.class.getSimpleName();
        beanName = Character.toLowerCase(beanName.charAt(0)) + (beanName.length() > 1 ? beanName.substring(1) : "");
        return beanName;
    }

    public static <E> ProcessingWrapper<E> wrapData(E dataPayload, List<String> initialPathPlan) {
        UUID siId = UUID.randomUUID();
        ProcessingWrapper<E> payload = new ProcessingWrapper<E>(siId, dataPayload, initialPathPlan);
        return payload;
    }

    public static List<String> copyStringList(List<String> toCopy) {
        ArrayList<String> theCopy = new ArrayList<String>();
        for (String s : toCopy) {
            theCopy.add(new String(s));
        }
        return theCopy;
    }

}
