package pw.adithya.SPLT;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.annotation.AcraDialog;
import org.acra.annotation.AcraMailSender;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.MailSenderConfigurationBuilder;
import org.acra.data.StringFormat;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);
        builder.setBuildConfigClass(BuildConfig.class).setReportFormat(StringFormat.JSON);
        builder.getPluginConfigurationBuilder(MailSenderConfigurationBuilder.class).setMailTo("hello@adithya.pw").setEnabled(true);
        ACRA.init(this, builder);
    }
}