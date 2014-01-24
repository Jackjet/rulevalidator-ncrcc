/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package ncmdp.pdmpath;

import org.eclipse.core.resources.IFile;

public class PDMPSServiceProxy
{

    public PDMPSServiceProxy()
    {
    }

    public static synchronized String getSuggestionPDMPath(IFile file)
    {
//        if(service == null && !isSearched)
//        {
//            BundleContext bc = CommonCoreActivator.getContext();
//            ServiceReference reference = bc.getServiceReference(ncmdp.pdmpath.IPDMPathSuggestionService.class.getName());
//            if(reference != null)
//                service = (IPDMPathSuggestionService)bc.getService(reference);
//            isSearched = true;
//        }
//        if(service != null)
//            return service.getSuggestionPath(file);
//        else
//            return null;
    	return "c:/pdm-generate";
    }

    private static IPDMPathSuggestionService service;
    private static boolean isSearched = false;

}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\WS\NCMDP\NCMDP_6.1.1.1122.jar
	Total time: 0 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/