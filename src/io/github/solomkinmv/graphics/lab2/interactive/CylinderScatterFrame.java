package io.github.solomkinmv.graphics.lab2.interactive;

import com.orsoncharts.Chart3D;
import com.orsoncharts.Chart3DFactory;
import com.orsoncharts.Chart3DPanel;
import com.orsoncharts.Colors;
import com.orsoncharts.data.xyz.XYZDataset;
import com.orsoncharts.data.xyz.XYZSeries;
import com.orsoncharts.data.xyz.XYZSeriesCollection;
import com.orsoncharts.graphics3d.Dimension3D;
import com.orsoncharts.graphics3d.ViewPoint3D;
import com.orsoncharts.graphics3d.swing.DisplayPanel3D;
import com.orsoncharts.label.StandardXYZLabelGenerator;
import com.orsoncharts.plot.XYZPlot;
import com.orsoncharts.renderer.xyz.ScatterXYZRenderer;
import io.github.solomkinmv.graphics.lab2.generator.CylinderPoints;
import io.github.solomkinmv.graphics.lab2.points.Point3D;

import javax.swing.*;
import java.awt.*;


public class CylinderScatterFrame extends JFrame {


    public CylinderScatterFrame(String title) {
        super(title);
        addWindowListener(new ExitOnClose());
        getContentPane().add(createDemoPanel());
    }

    public static void main(String[] args) {
        CylinderScatterFrame app = new CylinderScatterFrame(
                "Cylinder");
        app.pack();
        app.setVisible(true);
    }

    public JPanel createDemoPanel() {
        DemoPanel content = new DemoPanel(new BorderLayout());
        Dimension preferredSize = new Dimension(1000, 1000);
        content.setPreferredSize(preferredSize);
        XYZDataset<String> dataset = createDataset(new CylinderPoints(3, 400).generatePoints());
        Chart3D chart = createChart(dataset);
        Chart3DPanel chartPanel = new Chart3DPanel(chart);
        content.setChartPanel(chartPanel);
        chartPanel.zoomToFit(preferredSize);
        content.add(new DisplayPanel3D(chartPanel));
        return content;
    }

    private XYZDataset<String> createDataset(Point3D[][] points) {
        XYZSeries<String> series = new XYZSeries<>("Cylinder");
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                Point3D point3D = points[i][j];
                series.add(point3D.x, point3D.y, point3D.z);
            }
        }
        XYZSeriesCollection<String> dataset = new XYZSeriesCollection<>();
        dataset.add(series);
        return dataset;
    }

    public Chart3D createChart(XYZDataset dataset) {
        Chart3D chart = Chart3DFactory.createScatterChart("CylinderScatterFrame", null, dataset,
                                                          "X", "Y", "Z");
        XYZPlot plot = (XYZPlot) chart.getPlot();
        plot.setDimensions(new Dimension3D(10.0, 10.0, 10.0));
        plot.setLegendLabelGenerator(new StandardXYZLabelGenerator(
                StandardXYZLabelGenerator.COUNT_TEMPLATE));
        ScatterXYZRenderer renderer = (ScatterXYZRenderer) plot.getRenderer();
        renderer.setSize(0.15);
        renderer.setColors(Colors.createIntenseColors());
        chart.setViewPoint(ViewPoint3D.createAboveLeftViewPoint(40));
        return chart;
    }
}
