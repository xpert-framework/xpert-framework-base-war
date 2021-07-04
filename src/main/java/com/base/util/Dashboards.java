package com.base.util;

import java.util.Date;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 *
 * @author ayslanms
 */
public class Dashboards {

    /**
     * Retorna a data atual sem precisao de hora minuto segundo
     *
     * @return
     */
    public static Date getDataAtual() {
        return DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
    }

    /**
     * Retorna a data atual menos um mes sem precisao de hora minuto segundo.
     * Exemplo:
     *
     * 10ms 100ms 1s 1min 15min24s48ms 25h40min48s
     *
     * @return
     */
    public static Date getDataAtualMenosUmMes() {
        return DateUtils.truncate(new DateTime().plusMonths(-1).toDate(), java.util.Calendar.DAY_OF_MONTH);
    }

    /**
     * Retorna a duracao formatada a partir de um tempo em milisegundos
     *
     * @param timemiliseconds
     * @return
     */
    public static String getDuracao(long timemiliseconds) {
        Duration duration = new Duration(timemiliseconds); // in milliseconds
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix(getSmall(" dias "))
                .appendHours()
                .appendSuffix(getSmall("h "))
                .appendMinutes()
                .appendSuffix(getSmall("min "))
                .appendSeconds()
                .appendSuffix(getSmall("s "))
                .appendMillis()
                .appendSuffix(getSmall("ms "))
                .toFormatter();
        return formatter.print(duration.toPeriod());
    }

    /**
     * Retorna o texto com uma marcacao HTML "small"
     *
     * @param texto
     * @return
     */
    private static String getSmall(String texto) {
        return "<small>" + texto + "</small>";
    }
}
