
import com.googlecode.lanterna.gui.component.*;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.listener.ComponentListener;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Action;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import com.googlecode.lanterna.terminal.Terminal.SGR;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.LanternaException;
import com.googlecode.lanterna.input.CharacterPattern;
import com.googlecode.lanterna.input.InputDecoder;
import com.googlecode.lanterna.input.InputProvider;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.KeyMappingProfile;


public class FinalProject {

	public static ArrayList divide(double xcor, ArrayList coeff){

        Collections.reverse(coeff);

        ArrayList coef = new ArrayList();
        coef.add(0.0 + (int) coeff.get(0));
        for(int i = 1; i < coeff.size()-1;i++){
            coef.add( (double) coef.get(i-1) * xcor + (int) coeff.get(i));

        }

         Collections.reverse(coef);
				 Collections.reverse(coeff);
         return coef;
	}

	public static int eval(int xcor, ArrayList coeff, int yzoom){
		int sum = 0;
		for(int i=0;i<coeff.size();i++){
			sum = sum + (int) coeff.get(i) * ((int) Math.pow(xcor,i));
		}
		return (sum / yzoom);
	}

	public static double eval2(double xcor, ArrayList coeff){
		double sum = 0.0 + (int) coeff.get(0);
		for(int i=1;i<coeff.size();i++){
			int coefficient = (int) coeff.get(i);
			double power = ((double) Math.pow(xcor,i));
			double to_add =  power * coefficient;
			sum = sum + to_add;
		}
		return (sum );
	}

	public static double solve(ArrayList coeff){
	    ArrayList derivative = new ArrayList();
			for(int i=1;i<coeff.size();i++){
				derivative.add(((int) coeff.get(i)) * i);
			}


			double starter_point = 6785;
			double ender_point = 0;
			int trials = 0;
			while(Math.abs(starter_point-ender_point)>0.0001 && trials < 100){

				starter_point = ender_point;
				//System.out.println(starter_point);

				double b = eval2(starter_point, coeff);
				//System.out.println(b);
				double m = eval2(starter_point, derivative);
				//System.out.println(m);
				ender_point -= b / m;
				//System.out.println(ender_point);
				trials ++;
			}
			if(trials < 99){
				return ender_point;
		  }
			else{
				return 69.6969;
			}
	}



	public static void putString(int r, int c,Terminal t, String s){
		t.moveCursor(r,c);
		for(int i = 0; i < s.length();i++){
			t.putCharacter(s.charAt(i));
		}
	}


	public static void setupAxis(Terminal t, int width, int height, ArrayList coef, int xzoom){
		int yzoom = Math.abs(eval(xzoom,coef,1)/40);
		for(int i=0;i<width;i++){
			t.moveCursor(i,height/2);
			t.putCharacter(' ');
			t.applyBackgroundColor(Terminal.Color.WHITE);
			if((i-width/2)%5==0 && (i-width/2)!=0){
				t.moveCursor(i,(height/2)-1);
				t.applyBackgroundColor(Terminal.Color.DEFAULT);
				t.applyForegroundColor(Terminal.Color.DEFAULT);
				putString(i,(height/2)-1,t,Integer.toString(2 * (i-width/2) * xzoom / width));
			}
		}

	for(int i=0;i<height;i++){
		t.moveCursor(width/2,i);
		t.putCharacter(' ');
		t.applyBackgroundColor(Terminal.Color.WHITE);
		if((i-height/2)%5==0 && (i-height/2)!=0){
			t.moveCursor((width/2)-1,i);
			t.applyBackgroundColor(Terminal.Color.DEFAULT);
			t.applyForegroundColor(Terminal.Color.DEFAULT);
			putString((width/2)-1,i,t,Integer.toString((-1) * yzoom * (i-height/2)));
		}
	}
}
	public static void graphRed(Terminal t, ArrayList coef, int width, int height, int xzoom){
		int yzoom = Math.abs(eval(xzoom,coef,1)/40);
		t.applyBackgroundColor(Terminal.Color.RED);

		for(int i = - width / 2; i < width / 2;i++){
			int ycor=eval( 2 * xzoom * i / width , coef, yzoom);
			if(ycor<height/2 && ycor>-height/2){
				t.moveCursor(i + width / 2, height / 2 - ycor);
				t.putCharacter(' ');
			}
		}

	}
	public static void graphBlue(Terminal t, ArrayList coef, int width, int height, int xzoom){
		int yzoom = Math.abs(eval(xzoom,coef,1)/40);
		t.applyBackgroundColor(Terminal.Color.DEFAULT);

		for(int i = - width / 2; i < width / 2;i++){
			int ycor=eval(2 * xzoom * i / width, coef, yzoom);
			if(ycor<height/2 && ycor>-height/2){
				t.moveCursor(i + width / 2, height / 2 - ycor);
				t.putCharacter(' ');
			}
		}
	}
	public static String eqString( ArrayList coef){

		String eq_string="y="+ (int) coef.get(0);
		int next_highest_power = coef.size();

		for(int i=1;i<coef.size();i++){
			if((int) coef.get(i)==1){
				eq_string=eq_string+"+x^"+i;
			}
			else{
				if((int) coef.get(i)==0){

				}
				else{
					if((int) coef.get(i)>0){
						eq_string=eq_string+"+"+(int) coef.get(i)+"x^"+i;
					}
					else{
						eq_string=eq_string+(int) coef.get(i)+"x^"+i;
					}
				}
			}
		}
		return eq_string;
	}

	public static String eqString2( ArrayList coef){

		String eq_string="y="+ (double) coef.get(0);
		int next_highest_power = coef.size();

		for(int i=1;i<coef.size();i++){
			if((double) coef.get(i)==(double) 1){
				eq_string=eq_string+"+x^"+i;
			}
			else{
				if((double) coef.get(i)==(double) 0){

				}
				else{
					if((double) coef.get(i)>0){
						eq_string=eq_string+"+"+(double) coef.get(i)+"x^"+i;
					}
					else{
						eq_string=eq_string+(double) coef.get(i)+"x^"+i;
					}
				}
			}
		}
		return eq_string;
	}

	public static void setupEq(Terminal t, ArrayList coef, int width, int height, int xzoom){
		int yzoom = Math.abs(eval(xzoom,coef,1)/40);
		String eq_string = eqString( coef );
		putString(10,5,t,eq_string);
		String spaces = "";
		for(int i=0;i< (coef.size() + 1);i++){
			spaces = spaces + "    ";
		}
		putString(10,7,t,"Enter to go back to editing");
		t.moveCursor(10,8);
		putString(12,12,t,"Y yzoom: "+yzoom );
	}

	public static void clear(int width, int height, Terminal t){
		for(int i=0;i<height;i++){
			for(int j = 0 ; j < width; j ++){
				t.moveCursor(j,i);
				t.applyBackgroundColor(Terminal.Color.DEFAULT);
				t.applyForegroundColor(Terminal.Color.DEFAULT);
				t.putCharacter(' ');
			}

		}
	}

	public static void main(String[] args) {


		int x = 3;
		int y = 4;
		Terminal terminal = TerminalFacade.createTextTerminal();
		terminal.enterPrivateMode();

		TerminalSize size = terminal.getTerminalSize();
		int width = size.getColumns();
		int height = size.getRows();
		terminal.setCursorVisible(false);

		boolean running = true;
		boolean text_activated = false;
		long tStart = System.currentTimeMillis();
		long lastSecond = 0;
		int yzoom = 5;
		ArrayList coefficients = new ArrayList();
		coefficients.add(1);
		coefficients.add(-4);
		coefficients.add(3);

		int xzoom = 100;
		//setupAxis( terminal, width,  height, yzoom);
		boolean edit_mode = true;
		clear(width,height,terminal);
		while(running){

			if(edit_mode){
				terminal.moveCursor(x,y);
				terminal.applyBackgroundColor(Terminal.Color.WHITE);
				terminal.applyForegroundColor(Terminal.Color.BLACK);
				//applySGR(a,b) for multiple modifiers (bold,blink) etc.
				terminal.applySGR(Terminal.SGR.ENTER_UNDERLINE);
				terminal.putCharacter('\u00a4');

				//terminal.putCharacter(' ');
				terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
				terminal.applyForegroundColor(Terminal.Color.DEFAULT);
				terminal.applySGR(Terminal.SGR.RESET_ALL);
				putString(50 ,22,terminal,"hit enter to graph");
				putString(50 ,21,terminal,"Use up and down arrows to pick a coefficient to edit");
				putString(50 ,20,terminal,"Use right and left arrows to modify a coefficient by +/- 1");
				for(int i = 0; i < coefficients.size(); i ++){

					putString(3 ,3 + 3 * i,terminal,coefficients.get(i).toString());
					if(i != 0){
						putString(9 ,3 + 3 * i,terminal,"x^" + i);
					}
				}

				putString(3 ,3 + 3 * coefficients.size(),terminal,"hit n to add a x ^ " + coefficients.size() + " term");
				putString(3 ,6 + 3 * coefficients.size(),terminal,"X domain is -"+xzoom+" to "+xzoom);

				Key key = terminal.readInput();
				if (key != null)
				{
					if (key.getKind() == Key.Kind.Escape) {

						terminal.exitPrivateMode();
						running = false;
					}
					if (key.getKind() == Key.Kind.Enter) {
						edit_mode = false;
						clear(width,height,terminal);
						setupAxis(terminal, width,  height, coefficients, xzoom);
						if(solve(coefficients) != 69.6969){
							putString(3,63,terminal, "One real solution is:"+Double.toString(solve(coefficients)));

							putString(3,64,terminal, "The rest of the polynomial is: "+eqString2(divide(solve(coefficients),coefficients)));
						}
						else{
							putString(39,3,terminal, "no roots");
						}
					}
					if (key.getCharacter() == 'n'){
						putString(3 ,3 + 3 * coefficients.size(),terminal,"                                                                                                                                     ");
						coefficients.add(1);
					}

					if (key.getKind() == Key.Kind.ArrowUp) {
						terminal.moveCursor(x,y);
						terminal.putCharacter(' ');
						y -= 3;
					}

					if (key.getKind() == Key.Kind.ArrowDown) {
						terminal.moveCursor(x,y);
						terminal.putCharacter(' ');
						y +=3;
					}

					if (key.getKind() == Key.Kind.ArrowLeft) {

						int power = (y - y % 3)/3 - 1;
						if(power < coefficients.size() && power > -1){
							coefficients.set(power,(int) coefficients.get(power) - 1);
							putString(3 ,3 + 3 * power,terminal,"                                             ");
						}
						else if (power == coefficients.size()+1){
							putString(3 ,3 + 3 * power,terminal,"                                             ");
							xzoom = xzoom - 5;
						}
					}

					if (key.getKind() == Key.Kind.ArrowRight ) {
						int power = (y - y % 3)/3 - 1;
						if(power < coefficients.size() && power > -1){
							coefficients.set(power,(int) coefficients.get(power) + 1);
							putString(3 ,3 + 3 * power,terminal,"                                             ");
						}
						else if (power == coefficients.size()+1){
							putString(3 ,3 + 3 * power,terminal,"                                             ");
							xzoom = xzoom + 5;
						}
					}
				}
			}
			else{
				terminal.moveCursor(x,y);
				terminal.applyBackgroundColor(Terminal.Color.WHITE);
				terminal.applyForegroundColor(Terminal.Color.BLACK);
				//applySGR(a,b) for multiple modifiers (bold,blink) etc.
				terminal.applySGR(Terminal.SGR.ENTER_UNDERLINE);
				terminal.putCharacter('\u00a4');

				//terminal.putCharacter(' ');
				terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
				terminal.applyForegroundColor(Terminal.Color.DEFAULT);
				terminal.applySGR(Terminal.SGR.RESET_ALL);

				setupEq(terminal, coefficients, width, height, xzoom);
				graphRed(terminal, coefficients, width, height, xzoom);

				terminal.applyForegroundColor(Terminal.Color.DEFAULT);
				terminal.applyBackgroundColor(Terminal.Color.DEFAULT);


				Key key = terminal.readInput();

				if (key != null)
				{

					if (key.getKind() == Key.Kind.Escape) {

						terminal.exitPrivateMode();
						running = false;
					}
					if (key.getKind() == Key.Kind.Enter || text_activated) {
						clear(width,height,terminal);
						edit_mode = true;
						x = 3;
						y = 4;
					}
					if (key.getKind() == Key.Kind.Backspace) {

						if(coefficients.size()>1){

							graphBlue(terminal, coefficients, width, height, xzoom);
							coefficients.remove(coefficients.size()-1);
							setupEq(terminal, coefficients, width, height, xzoom);
							setupAxis( terminal, width,  height, coefficients, xzoom);
							graphRed(terminal, coefficients, width, height, xzoom);

						}
					}

					if (key.getKind() == Key.Kind.ArrowLeft) {
						terminal.moveCursor(x,y);
						terminal.putCharacter(' ');
						x--;
					}

					if (key.getKind() == Key.Kind.ArrowRight) {
						terminal.moveCursor(x,y);
						terminal.putCharacter(' ');
						x++;
					}

					if (key.getKind() == Key.Kind.ArrowUp) {
						terminal.moveCursor(x,y);
						terminal.putCharacter(' ');
						y--;
					}

					if (key.getKind() == Key.Kind.ArrowDown) {
						terminal.moveCursor(x,y);
						terminal.putCharacter(' ');
						y++;
					}
					//space moves it diagonally
					if (key.getCharacter() == ' ') {
						terminal.moveCursor(x,y);
						terminal.putCharacter(' ');
						y++;
						x++;
					}

					if (key.getCharacter() == 'p') {
						putString(x+1,y,terminal,"p");
						putString(x-1,y,terminal,"p");
						putString(x,y+1,terminal,"p");
						putString(x,y-1,terminal,"p");
					}
				}

				terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
				terminal.applyForegroundColor(Terminal.Color.DEFAULT);


			}
		}
	}
}
