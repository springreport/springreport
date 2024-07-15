package com.springreport.util;


import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PlotState;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.*;
import java.text.NumberFormat;

/**
 * @author zwz
 * @date 2020/9/30 14:02
 */
public class CalibrationSpiderWebPlot extends SpiderWebPlot {
    private static final long serialVersionUID = -11L;
    private NumberFormat format = NumberFormat.getInstance();
    private static final double PERPENDICULAR = 90;
    private static final double TICK_SCALE = 0.015;
    private int valueLabelGap = DEFAULT_GAP;
    private static final int DEFAULT_GAP = 10;
    private static final double THRESHOLD = 15;
    //刻度数
    public static final int DEFAULT_TICKS = 5;
    //坐标最大值
    public static final double DEFAULT_MAX_VALUE = 100;
    //画换默认值
    public static final boolean DEFAULT_DRAW_RING = false;
    /**
     * 刻度数/环数
     */
    private int ticks = DEFAULT_TICKS;
    /**
     * 画环
     */
    private boolean drawRing = false;
    /**
     * 画顶点数值
     */
    private boolean drawValue = false;
    /**
     * 刻度前缀
     */
    private String lablePrefix = "";
    /**
     * 刻度后缀
     */
    private String lableSuffix = "";

    /**
     * 默认坐标最大值和画环
     * @param createCategoryDataset
     */
    public CalibrationSpiderWebPlot(CategoryDataset createCategoryDataset){
        //设置坐标最大值为默认的值
        this(createCategoryDataset, DEFAULT_MAX_VALUE);
    }

    /**
     * 默认画环,与刻度数
     * @param createCategoryDataset
     * @param maxValue
     */
    public CalibrationSpiderWebPlot(CategoryDataset createCategoryDataset, double maxValue){
        //设置画换默认值
        this(createCategoryDataset, maxValue, DEFAULT_DRAW_RING);
    }

    /**
     * 自定义坐标最大值和画环
     * @param createCategoryDataset
     * @param maxValue
     * @param drawRing
     */
    public CalibrationSpiderWebPlot(CategoryDataset createCategoryDataset, double maxValue, boolean drawRing){
        //设置刻度数默认值
        this(createCategoryDataset, maxValue, drawRing, DEFAULT_TICKS);
    }

    /**
     * 自定义坐标最大值和画环、刻度数
     * @param createCategoryDataset
     * @param maxValue
     * @param drawRing
     */
    public CalibrationSpiderWebPlot(CategoryDataset createCategoryDataset, double maxValue, boolean drawRing, int ticks){
        //设置刻度前缀默认值
        this(createCategoryDataset, maxValue, drawRing, ticks, "");
    }

    /**
     * 自定义坐标最大值和画环以及刻度前缀、刻度数
     * @param createCategoryDataset
     * @param maxValue
     * @param drawRing
     */
    public CalibrationSpiderWebPlot(CategoryDataset createCategoryDataset, double maxValue, boolean drawRing, int ticks, String lablePrefix){
        this(createCategoryDataset, maxValue, drawRing, ticks, lablePrefix, "");
    }

    /**
     * 自定义坐标最大值和画环以及刻度前/后缀、刻度数
     * @param createCategoryDataset
     * @param maxValue
     * @param drawRing
     */
    public CalibrationSpiderWebPlot(CategoryDataset createCategoryDataset, double maxValue, boolean drawRing, int ticks, String lablePrefix, String lableSuffix){
        super(createCategoryDataset);
        //设置坐标最大值
        this.setMaxValue(maxValue);
        //设置画换
        this.setDrawRing(drawRing);
        //设置刻度数
        this.setTicks(ticks);
        //刻度前缀
        this.setLablePrefix(lablePrefix);
        //刻度后缀
        this.setLableSuffix(lableSuffix);
    }

    /**
     * 画图，支持添加圆环
     * @param g2  the graphics device.
     * @param area  the area within which the plot should be drawn.
     * @param anchor  the anchor point (<code>null</code> permitted).
     * @param parentState  the state from the parent plot, if there is one.
     * @param info  collects info about the drawing.
     */
    @Override
    public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo info){
        // adjust for insets...
        RectangleInsets insets = getInsets();
        insets.trim(area);
        if(info != null){
            info.setPlotArea(area);
            info.setDataArea(area);
        }
        drawBackground(g2, area);
        drawOutline(g2, area);
        Shape savedClip = g2.getClip();
        g2.clip(area);
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getForegroundAlpha()));
        if(!DatasetUtils.isEmptyOrNull(this.getDataset())){
            int seriesCount = 0, catCount = 0;
            if(this.getDataExtractOrder() == TableOrder.BY_ROW){
                seriesCount = this.getDataset().getRowCount();
                catCount = this.getDataset().getColumnCount();
            }else{
                seriesCount = this.getDataset().getColumnCount();
                catCount = this.getDataset().getRowCount();
            }
            // ensure we have a maximum value to use on the axes
            if(this.getMaxValue() == DEFAULT_MAX_VALUE) {
                calculateMaxValue(seriesCount, catCount);
            }
            // Next, setup the plot area
            // adjust the plot area by the interior spacing value
            double gapHorizontal = area.getWidth() * getInteriorGap();
            double gapVertical = area.getHeight() * getInteriorGap();
            double X = area.getX() + gapHorizontal / 2;
            double Y = area.getY() + gapVertical / 2;
            double W = area.getWidth() - gapHorizontal;
            double H = area.getHeight() - gapVertical;
            double headW = area.getWidth() * this.headPercent;
            double headH = area.getHeight() * this.headPercent;
            // make the chart area a square
            double min = Math.min(W, H) / 2;
            X = (X + X + W) / 2 - min;
            Y = (Y + Y + H) / 2 - min;
            W = 2 * min;
            H = 2 * min;
            Point2D centre = new Point2D.Double(X + W / 2, Y + H / 2);
            Rectangle2D radarArea = new Rectangle2D.Double(X, Y, W, H);
            // draw the axis and category label
            for(int cat = 0; cat < catCount; cat++){
                double angle = getStartAngle() + (getDirection().getFactor() * cat * 360 / catCount);
                //如果只有两个分类，设置固定角度
                if(catCount == 2 && cat == 1){
                    angle = 0;
                }
                Point2D endPoint = getWebPoint(radarArea, angle, 1);
                // 1 = end of axis
                Line2D line = new Line2D.Double(centre, endPoint);
                g2.setPaint(this.getAxisLinePaint());
                g2.setStroke(this.getAxisLineStroke());
                g2.draw(line);
                drawLabel(g2, radarArea, 0.0, cat, angle, 360.0 / catCount);
            }
            //画环
            if(this.isDrawRing()){
                //以90度为轴心，计算各个圆环的x、y坐标
                Point2D topPoint = getWebPoint(radarArea, 90, 1);
                //轴心顶点圆的半径
                double topPointR = centre.getY() - topPoint.getY();
                //每个刻度的半径长
                double step = topPointR / this.getTicks();
                for(int p = this.getTicks(); p >= 1; p--){
                    double r = p * step;
                    double upperLeftX = centre.getX() - r;
                    double upperLeftY = centre.getY() - r;
                    double d = 2 * r;
                    Ellipse2D ring = new Ellipse2D.Double(upperLeftX, upperLeftY, d, d);
                    g2.setPaint(Color.lightGray);
                    g2.draw(ring);
                }
            }
            // Now actually plot each of the series polygons..
            for(int series = 0; series < seriesCount; series++){
                drawRadarPoly(g2, radarArea, centre, info, series, catCount, headH, headW);
            }
        }else{
            drawNoDataMessage(g2, area);
        }
        g2.setClip(savedClip);
        g2.setComposite(originalComposite);
        drawOutline(g2, area);
    }

    /**
     * 增加刻度
     */
    @Override
    protected void drawLabel(final Graphics2D g2, final Rectangle2D plotArea, final double value, final int cat,
                             final double startAngle, final double extent){
        super.drawLabel(g2, plotArea, value, cat, startAngle, extent);
        final FontRenderContext frc = g2.getFontRenderContext();
        final double[] transformed = new double[2];
        final double[] transformer = new double[2];
        final Arc2D arc1 = new Arc2D.Double(plotArea, startAngle, 0, Arc2D.OPEN);
        //设置默认值
        if(ticks <= 0){
            ticks = DEFAULT_TICKS;
        }
        for(int i = 1; i <= ticks; i++){
            final Point2D point1 = arc1.getEndPoint();
            final double deltaX = plotArea.getCenterX();
            final double deltaY = plotArea.getCenterY();
            double labelX = point1.getX() - deltaX;
            double labelY = point1.getY() - deltaY;
            final double scale = ((double) i / (double) ticks);
            final AffineTransform tx = AffineTransform.getScaleInstance(scale, scale);
            final AffineTransform pointTrans = AffineTransform.getScaleInstance(scale + TICK_SCALE, scale + TICK_SCALE);
            transformer[0] = labelX;
            transformer[1] = labelY;
            pointTrans.transform(transformer, 0, transformed, 0, 1);
            final double pointX = transformed[0] + deltaX;
            final double pointY = transformed[1] + deltaY;
            tx.transform(transformer, 0, transformed, 0, 1);
            labelX = transformed[0] + deltaX;
            labelY = transformed[1] + deltaY;
            double rotated = (PERPENDICULAR);
            AffineTransform rotateTrans = AffineTransform.getRotateInstance(Math.toRadians(rotated), labelX, labelY);
            transformer[0] = pointX;
            transformer[1] = pointY;
            rotateTrans.transform(transformer, 0, transformed, 0, 1);
            final double x1 = transformed[0];
            final double y1 = transformed[1];
            rotated = (-PERPENDICULAR);
            rotateTrans = AffineTransform.getRotateInstance(Math.toRadians(rotated), labelX, labelY);
            rotateTrans.transform(transformer, 0, transformed, 0, 1);
            final Composite saveComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.draw(new Line2D.Double(transformed[0], transformed[1], x1, y1));
            if(startAngle == this.getStartAngle()){//只在第一个画上刻度
                String label = format.format(((double) i / (double) ticks) * this.getMaxValue());
                final LineMetrics lm = getLabelFont().getLineMetrics(label, frc);
                final double ascent = lm.getAscent();
                if(Math.abs(labelX - plotArea.getCenterX()) < THRESHOLD){
                    labelX += valueLabelGap;
                    labelY += ascent / (float) 2;
                }else if(Math.abs(labelY - plotArea.getCenterY()) < THRESHOLD){
                    labelY += valueLabelGap;
                }else if(labelX >= plotArea.getCenterX()){
                    if(labelY < plotArea.getCenterY()){
                        labelX += valueLabelGap;
                        labelY += valueLabelGap;
                    }else{
                        labelX -= valueLabelGap;
                        labelY += valueLabelGap;
                    }
                }else{
                    if(labelY > plotArea.getCenterY()){
                        labelX -= valueLabelGap;
                        labelY -= valueLabelGap;
                    }else{
                        labelX += valueLabelGap;
                        labelY -= valueLabelGap;
                    }
                }
                g2.setPaint(getLabelPaint());
                g2.setFont(getLabelFont());
                //添加刻度的前缀和后缀
                if(null != this.getLablePrefix() && !"".equals(this.getLablePrefix())){
                    label = this.getLablePrefix() + label;
                }
                if(null != this.getLableSuffix() && !"".equals(this.getLableSuffix())){
                    label = label + this.getLableSuffix();
                }
                g2.drawString(label, (float) labelX, (float) labelY);
            }
            g2.setComposite(saveComposite);
        }
    }

    /**
     * Draws a radar plot polygon. 如果只有两个分类，设置固定角度
     * @param g2 the graphics device.
     * @param plotArea the area we are plotting in (already adjusted).
     * @param centre the centre point of the radar axes
     * @param info chart rendering info.
     * @param series the series within the dataset we are plotting
     * @param catCount the number of categories per radar plot
     * @param headH the data point height
     * @param headW the data point width
     */
    @Override
    protected void drawRadarPoly(Graphics2D g2, Rectangle2D plotArea, Point2D centre, PlotRenderingInfo info,
                                 int series, int catCount, double headH, double headW){
        Polygon polygon = new Polygon();
        EntityCollection entities = null;
        if(info != null){
            entities = info.getOwner().getEntityCollection();
        }
        // plot the data...
        for(int cat = 0; cat < catCount; cat++){
            Number dataValue = getPlotValue(series, cat);
            if(dataValue != null){
                double value = dataValue.doubleValue();
                if(value >= 0){
                    // draw the polygon series...
                    // Finds our starting angle from the centre for this axis
                    double angle = getStartAngle() + (getDirection().getFactor() * cat * 360 / catCount);
                    //如果只有两个分类，设置固定角度
                    if(catCount == 2 && cat == 1){
                        angle = 0;
                    }
                    // The following angle calc will ensure there isn't a top
                    // vertical axis - this may be useful if you don't want any
                    // given criteria to 'appear' move important than the
                    // others..
                    //  + (getDirection().getFactor()
                    //          * (cat + 0.5) * 360 / catCount);
                    // find the point at the appropriate distance end point
                    // along the axis/angle identified above and add it to the
                    // polygon
                    Point2D point = getWebPoint(plotArea, angle, value / this.getMaxValue());
                    polygon.addPoint((int) point.getX(), (int) point.getY());
                    // put an elipse at the point being plotted..
                    Paint paint = getSeriesPaint(series);
                    Paint outlinePaint = getSeriesOutlinePaint(series);
                    Stroke outlineStroke = getSeriesOutlineStroke(series);
                    Ellipse2D head = new Ellipse2D.Double(point.getX() - headW / 2, point.getY() - headH / 2, headW, headH);
                    g2.setPaint(paint);
                    g2.fill(head);
                    g2.setStroke(outlineStroke);
                    g2.setPaint(outlinePaint);
                    g2.draw(head);

                    //画顶点数值
                    if(true){
                        g2.setPaint(paint);
                        //g2.drawString(StrUtil.obj2str(value,""), StrUtil.obj2int(point.getX() - headW), StrUtil.obj2int(point.getY() - headH));
                        g2.drawString(value+"", (int)(point.getX() - headW), (int)(point.getY() - headH));
                        g2.setStroke(outlineStroke);
                        g2.setPaint(outlinePaint);
                    }

                    if(entities != null){
                        int row = 0;
                        int col = 0;
                        if(this.getDataExtractOrder() == TableOrder.BY_ROW){
                            row = series;
                            col = cat;
                        }else{
                            row = cat;
                            col = series;
                        }
                        String tip = null;
                        if(this.getToolTipGenerator() != null){
                            tip = this.getToolTipGenerator().generateToolTip(this.getDataset(), row, col);
                        }
                        String url = null;
                        if(this.getURLGenerator() != null){
                            url = this.getURLGenerator().generateURL(this.getDataset(), row, col);
                        }
                        Shape area = new Rectangle((int) (point.getX() - headW), (int) (point.getY() - headH),
                                (int) (headW * 2), (int) (headH * 2));
                        CategoryItemEntity entity = new CategoryItemEntity(area, tip, url, this.getDataset(), this
                                .getDataset().getRowKey(row), this.getDataset().getColumnKey(col));
                        entities.add(entity);
                    }
                }
            }
        }
        // Plot the polygon
        Paint paint = getSeriesPaint(series);
        g2.setPaint(paint);
        g2.setStroke(getSeriesOutlineStroke(series));
        g2.draw(polygon);
        // Lastly, fill the web polygon if this is required
        if(this.isWebFilled()){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
            g2.fill(polygon);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getForegroundAlpha()));
        }
    }

    /**
     *获取分类的最大值
     * @param seriesCount  the number of series
     * @param catCount  the number of categories
     */
    private void calculateMaxValue(int seriesCount, int catCount){
        double v = 0;
        Number nV = null;
        for(int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++){
            for(int catIndex = 0; catIndex < catCount; catIndex++){
                nV = getPlotValue(seriesIndex, catIndex);
                if(nV != null){
                    v = nV.doubleValue();
                    if(v > this.getMaxValue()){
                        this.setMaxValue(v);
                    }
                }
            }
        }
    }

    //getters and setters
    public String getLablePrefix(){
        return lablePrefix;
    }
    public void setLablePrefix(String lablePrefix){
        this.lablePrefix = lablePrefix;
    }
    public String getLableSuffix(){
        return lableSuffix;
    }
    public void setLableSuffix(String lableSuffix){
        this.lableSuffix = lableSuffix;
    }
    public boolean isDrawRing(){
        return drawRing;
    }
    public void setDrawRing(boolean drawRing){
        this.drawRing = drawRing;
    }
    public int getTicks(){
        return ticks;
    }
    public void setTicks(int ticks){
        this.ticks = ticks;
    }
    public boolean isDrawValue(){
        return drawValue;
    }
    public void setDrawValue(boolean drawValue){
        this.drawValue = drawValue;
    }
}

