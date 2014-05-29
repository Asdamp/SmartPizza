package com.asdamp.smartpizza;

import android.app.Application;
import org.acra.*;
import org.acra.annotation.*;


@ReportsCrashes(
		formKey = "",
        formUri = "https://asdamp.cloudant.com/acra-smart_pizza/_design/acra-storage/_update/report",
        reportType = org.acra.sender.HttpSender.Type.JSON,
        httpMethod = org.acra.sender.HttpSender.Method.PUT,
        formUriBasicAuthLogin="ysideepseditedgetinmedly",
        formUriBasicAuthPassword="yEKlMKDLQTDuAwvSCPkDYd8a",	

        mode = ReportingInteractionMode.TOAST, 
                forceCloseDialogAfterToast = true,																																										// false

        resToastText = R.string.invio_della_segnalazione_di_errore_al_developer)
public class MainApplication extends Application {
	@Override
	public void onTerminate() {
		Costanti.chiudiDB();
		super.onTerminate();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		ACRA.init(this);

		initSingletons();
	}

	protected void initSingletons() {
		Costanti.inizializza(this);
		Costanti.getDB().apri();
	}
}
