package Biblioteca.BasicObjects.Formas;

import Biblioteca.BasicObjects.YcoolBindings;
import javafx.scene.shape.Line;
import Biblioteca.Lists.ySimpleMap;
import Biblioteca.LogicClasses.Ymathematics;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.StrokeLineCap;
import javafx.beans.value.ObservableValue;
import javafx.beans.binding.DoubleBinding;
import Biblioteca.BasicObjects.YvisibleObjectHandler;
import Biblioteca.Interactions.YEventsHandler;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.transform.Rotate;

public class Yline extends Line implements Yshape, YcoolBindings{
    private YEventsHandler yEvents_Handler = new YEventsHandler(this);
    public ySimpleMap<String, ObservableValue> yWeak_listeners = new ySimpleMap();
    
    private YstrokeOcupation yOutsideStrokeOcupation = new YstrokeOcupation();
    private Rotate yRotation = new Rotate(0);
    
    public DoubleProperty yMax_width = new SimpleDoubleProperty(-1);
    public DoubleProperty yMax_height = new SimpleDoubleProperty(-1);
    
    public Yline(Line line){
        this(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY(), line.getStrokeWidth(), line.getStroke());
    }
    
    public Yline(double Xfinal, double Yfinal, double grossura, Paint cor){
        this(0, 0, Xfinal, Yfinal, grossura, cor);
    }
    
    public Yline(double Xinicial, double Yinicial, double Xfinal, double Yfinal, double grossura, Paint cor){
        this(Xinicial, Yinicial, Xfinal, Yfinal, grossura, cor, StrokeType.CENTERED, true);
    }
    
    /**
     * Cria uma linha. (OBS: Não há setFIll nas linhas, sua cor é definida por setStroke e apenas && strokeLineCap é RECOMENDADO sempre ser BUTT).
     * @param Xinicial Coordenada X do inicio da linha.
     * @param Yinicial Coordenada Y do inicio da linha
     * @param Xfinal Coordenada X do final da linha.
     * @param Yfinal Coordenada Y do final da linha
     * @param grossura É a grossura da linha
     * @param cor Cor da linha
     */
    public Yline(double Xinicial, double Yinicial, double Xfinal, double Yfinal, double grossura, Paint cor, StrokeType stroke_type, boolean correct_location){
        ySetInitialPoint(Xinicial, Yinicial);
        ySetEndPoint(Xfinal, Yfinal);
        ySetStroke(grossura, cor, stroke_type, correct_location);
    }
    
    
    
    //----------------------------- SIZE METHODS -----------------------------\\
    
    public void ySetInitialPoint(Double Xinicial, Double Yinicial){
        if(Xinicial != null)
            setStartX(Xinicial);
        if(Yinicial != null)
            setStartY(Yinicial);
    }
    
    public void ySetEndPoint(Double Xfinal, Double Yfinal){
        if(Xfinal != null)
            setEndX(Xfinal);
        if(Yfinal != null)
            setEndY(Yfinal);
        
        if(getStartX() > Xfinal){
            double aux = getStartX();
            setStartX(Xfinal);
            setEndX(aux);
            //NAO SEI PQ  MAS TEM QUE TROCAR O Y TAMBÉM
            aux = getStartY();
            setStartY(Yfinal);
            setEndY(aux);
        }
    }
    
    /**
     * @return The lenght of the line, in other words, the hypotenuses created by the deltaX and deltaY lengths.
     * @see #yGetWidth(boolean)  
     * @see #yGetHeight(boolean) 
     */
    public double yHypotenuse(){
        return Ymathematics.yHypotenuse(yGetWidth(false), yGetHeight(false));
    }
    
    /**
     * @return The difference between the final X point and the initial X point.
     */
    @Override
    public double yGetWidth(boolean plusStroke){
        double delta = Ymathematics.yModulo(getEndX() - getStartX());
        if(plusStroke)
            delta += yOutsideStrokeOcupation.WIDTH;
        
        return delta;
    }
    
    /**
     * @return The difference between the final Y point and the initial Y point.
     */
    @Override
    public double yGetHeight(boolean plusStroke){
        double delta = Ymathematics.yModulo(getEndY() - getStartY());
        if(plusStroke)
            delta += yOutsideStrokeOcupation.HEIGHT;
        
        return delta;
    }

    @Override
    public double yGetWidth() {
        return YvisibleObjectHandler.yGetWidth(this);
    }

    @Override
    public double yGetHeight() {
        return YvisibleObjectHandler.yGetHeight(this);
    }
    
    
    @Override
    public void ySetWidth(double width, boolean stroke_included, boolean correct_location) {
        width = YshapeHandler.ySizeControler(width, stroke_included, yOutsideStrokeOcupation.WIDTH, yMax_width.get());
        
        ySetEndPoint(getStartX() + width, null);
    }

    @Override
    public void ySetHeight(double height, boolean stroke_included, boolean correct_location) {
        height = YshapeHandler.ySizeControler(height, stroke_included, yOutsideStrokeOcupation.HEIGHT, yMax_height.get());
        
        ySetEndPoint(null, getStartY() + height);
    }
    
    
    
    //----------------------------- TRANSLATE METHODS -----------------------------\\
     //LEMBRAR OU AVISAR O USUARIO DE QUE SE ELE USAR O TRANSLATE DO JAVA PROVAVELMENTE O PONTO QUE SERA MOVIDO É O DA MEIUCA DO STROKE
    //(O IMAGINARIO, QUE ATÉ É BOM TER ISSO EM MAOS)
    
    @Override
    public double yGetTranslateX(double pivo) {
        return getTranslateX() - yOutsideStrokeOcupation.LEFT + getStartX() + yGetWidth(true) * pivo;
    }

    @Override
    public double yGetTranslateY(double pivo) {
        return getTranslateY() - yOutsideStrokeOcupation.UP + getStartY() + yGetHeight(true) * pivo;
    }
    
    @Override
    public void ySetTranslateX(double position, double pivo) {
        YshapeHandler.setTranslateX(this, position - getStartX() + yOutsideStrokeOcupation.LEFT, pivo);
    }

    @Override
    public void ySetTranslateY(double position, double pivo) {
        YshapeHandler.setTranslateY(this, position - getStartY() + yOutsideStrokeOcupation.UP, pivo);
    }
    
    @Override
    public void ySetPosition(double X, double Y, double pivoX, double pivoY){
        ySetTranslateX(X, pivoX);
        ySetTranslateY(Y, pivoY);
    }
    
    
    
    //----------------------------- STROKE METHODS -----------------------------\\

    @Override
    public void ySetStroke(Double stroke_width, Paint stroke_color, StrokeType stroke_type, boolean correct_size) {
        double where_wasX = yGetTranslateX(0);
        double where_wasY = yGetTranslateY(0);
        
        if(correct_size)
            setStrokeLineCap(StrokeLineCap.BUTT);
        YshapeHandler.ySetStroke(this, stroke_width, stroke_color, stroke_type, true);
        
        yOutsideStrokeOcupation = new YstrokeOcupation((yGetHeight(false)/yHypotenuse())*YshapeHandler.yGetStrokeOcupation(this)*2,
                (yGetWidth(false)/yHypotenuse())*YshapeHandler.yGetStrokeOcupation(this)*2);
        
        ySetTranslateX(where_wasX, 0);
        ySetTranslateY(where_wasY, 0);
    }
    
    @Override
    public YstrokeOcupation yGetStrokeOcupation(){
        return yOutsideStrokeOcupation;
    }
    
    
    
    //----------------------------- ROTATE METHODS -----------------------------\\
    
    @Override
    public Rotate yGetRotate(){
        return yRotation;
    }
    
    @Override
    public void ySetRotate(double angle, double pivoX, double pivoY){
        YshapeHandler.ySetRotate(this, yRotation, angle, getStartX() + pivoX, getStartY() + pivoY);
    }
    
    public void yRotateBy(double angle, double pivoX, double pivoY){
        double points[] = {getStartX(), getStartY(), getEndX(), getEndY()};
        
        pivoX -= getTranslateX();
        pivoY -= getTranslateY();
        
        for (int i = 0; i < points.length; i+=2) {
            double X = points[i] - pivoX;
            double Y = points[i + 1] - pivoY;
            double newX = X * Math.cos(Math.toRadians(angle)) - Y * Math.sin(Math.toRadians(angle)) + pivoX;
            double newY = X * Math.sin(Math.toRadians(angle)) + Y * Math.cos(Math.toRadians(angle)) + pivoY;

            points[i] = newX;
            points[i + 1] = newY;
        }
        ySetInitialPoint(points[0], points[1]);
        ySetEndPoint(points[2], points[3]);
    }
    
    
    //----------------------------- SCALE METHODS -----------------------------\\
    
    @Override
    public void ySetScaleX(double scale, boolean correct_location) {
        YshapeHandler.ySetScaleX(this, scale, correct_location);
    }

    @Override
    public void ySetScaleY(double scale, boolean correct_location) {
        YshapeHandler.ySetScaleY(this, scale, correct_location);
    }

    @Override
    public void yScaleXby(double multiplier, boolean correct_location) {
        YshapeHandler.yScaleXby(this, multiplier, correct_location);
    }

    @Override
    public void yScaleYby(double multiplier, boolean correct_location) {
        YshapeHandler.yScaleYby(this, multiplier, correct_location);
    }
    
    @Override
    public void ySetWidthWithScale(double width, boolean stroke_included, boolean correct_location) {
        YshapeHandler.ySetWidthWithScale(this, width, stroke_included, correct_location);
    }

    @Override
    public void ySetHeigthWithScale(double height, boolean stroke_included, boolean correct_location) {
        YshapeHandler.ySetHeigthWithScale(this, height, stroke_included, correct_location);
    }
    
    
    
    //----------------------------- EVENTS METHODS -----------------------------\\
    
    @Override
    public YEventsHandler yGetEventsHandler(){
        return yEvents_Handler;
    }
    
    
    
    //----------------------------- BIND/LISTENER METHODS -----------------------------\\
    
    @Override
    public void yAddBind(String bind_name, ObservableValue<? extends Number> bind){
        YshapeHandler.yAddBind(yWeak_listeners, bind_name, bind);
    }
    
    @Override
    public DoubleBinding yTranslateXbind(double pivo){
        return YshapeHandler.yTranslateXbind(this, pivo);
    }
    
    @Override
    public DoubleBinding yTranslateYbind(double pivo){
        return YshapeHandler.yTranslateYbind(this, pivo);
    }
    
    @Override
    public void yBindTranslateX(String bind_name, ObservableValue<? extends Number> X, double pivo){
        YshapeHandler.yBindTranslateX(this, yWeak_listeners, bind_name, X, pivo);
    }
    
    @Override
    public void yBindTranslateY(String bind_name, ObservableValue<? extends Number> Y, double pivo){
        YshapeHandler.yBindTranslateY(this, yWeak_listeners, bind_name, Y, pivo);
    }
    
    @Override
    public DoubleBinding yWidthBind(boolean stroke_included){
        return YshapeHandler.yWidthBind(endXProperty().subtract(startXProperty()), stroke_included ? yOutsideStrokeOcupation : null);
    }
    
    @Override
    public DoubleBinding yHeightBind(boolean stroke_included){
        return YshapeHandler.yHeightBind(endYProperty().subtract(startYProperty()), stroke_included ? yOutsideStrokeOcupation : null);
    }
    
    @Override
    public void yBindWidth(String bind_name, ObservableValue<? extends Number> width, boolean stroke_included){
        YshapeHandler.yBindWidth(this, yWeak_listeners, bind_name, width, stroke_included);
    }
    
    @Override
    public void yBindHeight(String bind_name, ObservableValue<? extends Number> height, boolean stroke_included){
        YshapeHandler.yBindHeight(this, yWeak_listeners, bind_name, height, stroke_included);
    }
    
    @Override
    public void yUnbind(String bind_name){
        YshapeHandler.yUnbind(yWeak_listeners, bind_name);
    }
}