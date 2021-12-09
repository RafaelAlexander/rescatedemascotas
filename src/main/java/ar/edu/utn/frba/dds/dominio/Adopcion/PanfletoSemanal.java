package ar.edu.utn.frba.dds.dominio.Adopcion;

import ar.edu.utn.frba.dds.dominio.Excepciones.NotificacionException;
import ar.edu.utn.frba.dds.dominio.Repos.RepoAsociaciones;
import ar.edu.utn.frba.dds.dominio.Rescate.Asociacion;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

public class PanfletoSemanal implements Job {

  private Scheduler scheduler;

  public void comenzarEnvioDeAdopcionesValidasAInteresados(String expresionChron) {
    if (expresionChron.isEmpty() || !CronExpression.isValidExpression(expresionChron)) {
      throw new RuntimeException("No se asigno tiempo de repeticion valida");
    }
    JobDetail job = JobBuilder.newJob(PanfletoSemanal.class)
        .withIdentity("Reporte", "group1").build();
    Trigger trigger = TriggerBuilder
        .newTrigger()
        .withIdentity("TemporizadorReporte", "group1")
        .withSchedule(
            CronScheduleBuilder.cronSchedule(expresionChron)//Cada lunes se ejecuta
        )
        .build();

    //schedule it
    try {
      this.scheduler = new StdSchedulerFactory().getScheduler();
      this.scheduler.start();
      this.scheduler.scheduleJob(job, trigger);

    } catch (SchedulerException e) {
      throw new RuntimeException("El temporizador no inicio correctamente. ID:" + e);
    }
  }

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    for (Asociacion asociacion : RepoAsociaciones.getInstance().listaDeAsociaciones()) {
      try {
        asociacion.notificarAdoptantes();
      } catch (NotificacionException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }
}
