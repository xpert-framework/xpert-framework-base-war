package com.base.bo.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Years;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.AxesGridLines;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

/**
 *
 * @author ayslanms
 */
public class Charts {

    /**
     * Cores padroes dos graficos de multiplas series, para mais cores deve-ser
     * adicionar aqui
     */
    public final static String COLOR_WHITE = "rgba(255, 255, 255)";
    public final static String COLOR_SERIE_1 = "rgba(75, 192, 192)";
    public final static String COLOR_SERIE_2 = "rgba(255, 159, 64)";
    public final static String COLOR_SERIE_3 = "rgba(255, 205, 86)";
    public final static String COLOR_SERIE_4 = "rgba(255, 99, 132)";
    public final static String COLOR_SERIE_5 = "rgba(54, 162, 235)";
    public final static String COLOR_SERIE_6 = "rgba(153, 102, 255)";
    public final static String COLOR_SERIE_7 = "rgba(201, 203, 207)";

    public final static List<String> COLORS = new ArrayList<>();

    static {
        COLORS.add(COLOR_SERIE_1);
        COLORS.add(COLOR_SERIE_2);
        COLORS.add(COLOR_SERIE_3);
        COLORS.add(COLOR_SERIE_4);
        COLORS.add(COLOR_SERIE_5);
        COLORS.add(COLOR_SERIE_6);
        COLORS.add(COLOR_SERIE_7);
    }

    /**
     * Retorna um grafico combinado de Barra com linha para quantitativo
     * (barras) e media (linhas). Util por exemplo para a faixa de horario
     *
     * @param valuesQuantidade
     * @param valuesMedia
     * @param labels
     * @param intervalo
     * @return
     */
    public static BarChartModel getGraficoQuantidadeMedia(List<Number> valuesQuantidade, List<Number> valuesMedia, List<String> labels, int intervalo) {

        BarChartModel barChartModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet dataSetQuantidade = new BarChartDataSet();
        dataSetQuantidade.setYaxisID("quantidade");
        dataSetQuantidade.setData(valuesQuantidade);
        dataSetQuantidade.setLabel("Quantidade");
        dataSetQuantidade.setBorderColor(Charts.COLOR_SERIE_1);
        dataSetQuantidade.setBackgroundColor(Charts.COLOR_SERIE_1);

        LineChartDataSet dataSetMedia = new LineChartDataSet();
        dataSetMedia.setYaxisID("media");
        dataSetMedia.setData(valuesMedia);
        dataSetMedia.setLabel("Média por dia (" + intervalo + " dias)");
        dataSetMedia.setFill(false);
        dataSetMedia.setBorderDash((List) Arrays.asList(new Integer[]{10, 5}));
        dataSetMedia.setBorderColor(Charts.COLOR_SERIE_2);
        dataSetMedia.setBackgroundColor(Charts.COLOR_WHITE);

        data.addChartDataSet(dataSetMedia);
        data.addChartDataSet(dataSetQuantidade);

        data.setLabels(labels);

        barChartModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();

        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setId("quantidade");
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);

        //essa escala nova sera alinha a direita
        CartesianLinearAxes linearAxes2 = new CartesianLinearAxes();
        linearAxes2.setId("media");
        linearAxes2.setPosition("right");
        AxesGridLines axesGridLines = new AxesGridLines();
        axesGridLines.setDisplay(false);
        linearAxes2.setGridLines(axesGridLines);
        CartesianLinearTicks ticks2 = new CartesianLinearTicks();
        ticks2.setBeginAtZero(true);
        linearAxes2.setTicks(ticks2);

        cScales.addYAxesData(linearAxes);
        cScales.addYAxesData(linearAxes2);

        options.setScales(cScales);
        barChartModel.setOptions(options);

        return barChartModel;
    }

    /**
     * Utilitario para calcular o melhor tipo de exibicao do grafico, seguindo a
     * seguinte regra:
     *
     * <pre>
     * 1 - Se dataFim - dataInicio > 2 anos: Exibir em anos
     * 2 - Senao se dataFim - dataInicio > 2 meses: Exibir em meses
     * 3 - Senao Exibir em dias
     * </pre>
     *
     * @param campo
     * @param dataInicio
     * @param dataFim
     * @return
     */
    public static String getGroupByTempo(String campo, Date dataInicio, Date dataFim) {
        DateTime inicio = new DateTime(dataInicio);
        DateTime fim = new DateTime(dataFim);
        if (Years.yearsBetween(inicio, fim).getYears() > 2) {
            return "YEAR(" + campo + ")";
        } else if (Months.monthsBetween(inicio, fim).getMonths() > 2) {
            //utilizando ano/mes para manter a ordenacao correta, pois o campo se torna string
            return "TO_CHAR(" + campo + ", 'YYYY/MM')";
        } else {
            return "cast(" + campo + " as date)";
        }
    }

    /**
     * Metodo que retorna um LineChartDataSet padrao
     *
     * @param titulo
     * @param cor
     * @param values
     * @return
     */
    public static LineChartDataSet getLineChartDataSet(String titulo, String cor, List<Number> values) {
        LineChartDataSet dataSet = new LineChartDataSet();
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel(titulo);
        dataSet.setBorderColor(cor);
        dataSet.setBackgroundColor(cor);
        //define line tension
        //dataSet.setLineTension(0.1);
        return dataSet;
    }

    /**
     * Metodo que retorna um BarChartDataSet padrao
     *
     * @param titulo
     * @param cor
     * @param values
     * @return
     */
    public static BarChartDataSet getBarChartDataSet(String titulo, String cor, List<Number> values) {
        BarChartDataSet dataSet = new BarChartDataSet();
        dataSet.setBackgroundColor(cor);
        dataSet.setBorderColor(cor);
        dataSet.setData(values);
        dataSet.setLabel(titulo);
        return dataSet;
    }

    /**
     * Retorna um grafico de linhas generico de apenas uma lista de series
     *
     * @param dataSets
     * @param labels
     * @return
     */
    public static LineChartModel getLineChartModel(List<LineChartDataSet> dataSets, List<String> labels) {
        LineChartModel model = new LineChartModel();
        ChartData data = new ChartData();

        for (LineChartDataSet dataSet : dataSets) {
            data.addChartDataSet(dataSet);
        }

        data.setLabels(labels);

        //Options
        LineChartOptions options = new LineChartOptions();
        Title title = new Title();
        title.setDisplay(false);
        options.setTitle(title);

        model.setOptions(options);
        model.setData(data);

        return model;
    }

    /**
     * Retorna um grafico de linhas generico de apenas uma serie
     *
     * @param titulo
     * @param values
     * @param labels
     * @return
     */
    public static LineChartModel getLineChartModel(String titulo, List<Number> values, List<String> labels) {
        return getLineChartModel(titulo, Charts.COLOR_SERIE_1, values, labels);
    }

    /**
     * Retorna um grafico de linhas generico de apenas uma serie
     *
     * @param titulo
     * @param cor
     * @param values
     * @param labels
     * @return
     */
    public static LineChartModel getLineChartModel(String titulo, String cor, List<Number> values, List<String> labels) {

        LineChartDataSet dataSet = getLineChartDataSet(titulo, cor, values);
        List<LineChartDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        return getLineChartModel(dataSets, labels);
    }

    /**
     * Retorna um grafico de pizza generico de apenas uma serie
     *
     * @param values
     * @param labels
     * @return
     */
    public static PieChartModel getPieChartModel(List<Number> values, List<String> labels) {
        PieChartModel model = new PieChartModel();
        ChartData data = new ChartData();
        PieChartDataSet dataSet = new PieChartDataSet();
        dataSet.setBackgroundColor(Charts.COLORS);

        dataSet.setData(values);
        data.addChartDataSet(dataSet);
        data.setLabels(labels);

        model.setData(data);
        return model;
    }

    /**
     * Retorna um grafico de barras generico de apenas uma serie
     *
     * @param titulo
     * @param values
     * @param labels
     * @return
     */
    public static BarChartModel getBarChartModel(String titulo, List<Number> values, List<String> labels) {
        return getBarChartModel(titulo, Charts.COLOR_SERIE_1, values, labels);
    }

    /**
     * Retorna um grafico de barras generico de apenas uma serie
     *
     * @param titulo
     * @param dataSets
     * @param labels
     * @return
     */
    public static BarChartModel getBarChartModel(List<BarChartDataSet> dataSets, String titulo, List<String> labels) {
        BarChartModel model = new BarChartModel();
        ChartData data = new ChartData();

        for (BarChartDataSet dataSet : dataSets) {
            data.addChartDataSet(dataSet);
        }
        data.setLabels(labels);
        model.setData(data);
        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setAutoSkip(false);
        linearAxes.setTicks(ticks);
        linearAxes.setStacked(true);

        cScales.addXAxesData(linearAxes);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        if (titulo != null) {
            title.setText(titulo);
            title.setDisplay(true);
        } else {
            title.setDisplay(false);
        }
        options.setTitle(title);

        Legend legend = new Legend();
        //para mais de uma serie eh importante ter a legenda
        if (dataSets.size() > 1) {
            legend.setDisplay(true);
        } else {
            legend.setDisplay(false);
        }
        options.setLegend(legend);

        Tooltip tooltip = new Tooltip();
        tooltip.setMode("index");
        tooltip.setIntersect(false);
        options.setTooltip(tooltip);

        model.setOptions(options);

        return model;
    }

    /**
     * Retorna um grafico de barras generico de apenas uma serie
     *
     * @param titulo
     * @param cor
     * @param values
     * @param labels
     * @return
     */
    public static BarChartModel getBarChartModel(String titulo, String cor, List<Number> values, List<String> labels) {

        BarChartDataSet dataSet = getBarChartDataSet(titulo, cor, values);
        List<BarChartDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        return getBarChartModel(dataSets, titulo, labels);
    }

    /**
     * Retorna a partir de uma determinada hora a faixa de horario
     *
     * Exemplo: 7 retorna 07h-08h Exemplo: 14 retorna 14h-15h
     *
     *
     * @param hora
     * @return
     */
    public static String getFaixaHorario(Number hora) {
        Integer horaSeguinte = hora.intValue() + 1;
        return StringUtils.leftPad(hora.toString(), 2, "0") + "h - " + StringUtils.leftPad(horaSeguinte.toString(), 2, "0") + "h";
    }

}
