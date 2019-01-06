


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
	public static void putString(int r, int c,Terminal t, String s){
		t.moveCursor(r,c);
		for(int i = 0; i < s.length();i++){
			t.putCharacter(s.charAt(i));
		}
	}

	public static int eval(int xcor, int [] coeff){
		int sum = 0;
		for(int i=0;i<coeff.length;i++){
			sum = sum + coeff[i] * ((int) Math.pow(xcor,i));
		}
		return sum;
	}

	public static void graph(Terminal t, int [] coef, int width, int height){
		t.applyBackgroundColor(Terminal.Color.RED);

		for(int i = - width / 2; i < width / 2;i++){
			int ycor=eval(i,coef);
			if(ycor<height/2 && ycor>-height/2){
				t.moveCursor(i + width / 2, height / 2 - ycor);
				t.putCharacter(' ');
			}
		}
	}
	public static void main(String[] args) {


		int x = 10;
		int y = 10;
		Terminal terminal = TerminalFacade.createTextTerminal();
		terminal.enterPrivateMode();

		TerminalSize size = terminal.getTerminalSize();
		int width = size.getColumns();
		int height = size.getRows();
		terminal.setCursorVisible(false);

		boolean running = true;

		long tStart = System.currentTimeMillis();
		long lastSecond = 0;
		int [] coefficients = {2,-1};
		int next_highest_power = coefficients.length;

		for(int i=0;i<width;i++){
			terminal.moveCursor(i,height/2);
			terminal.putCharacter(' ');
			terminal.applyBackgroundColor(Terminal.Color.WHITE);
			if((i-width/2)%5==0 && (i-width/2)!=0){
				terminal.moveCursor(i,(height/2)-1);
				terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
				terminal.applyForegroundColor(Terminal.Color.DEFAULT);
				putString(i,(height/2)-1,terminal,Integer.toString(i-width/2));
			}
		}
		for(int i=0;i<height;i++){
			terminal.moveCursor(width/2,i);
			terminal.putCharacter(' ');
			terminal.applyBackgroundColor(Terminal.Color.WHITE);
			if((i-height/2)%5==0 && (i-height/2)!=0){
				terminal.moveCursor((width/2)-1,i);
				terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
				terminal.applyForegroundColor(Terminal.Color.DEFAULT);
				putString((width/2)-1,i,terminal,Integer.toString(i-height/2));
			}
		}
		while(running){
			next_highest_power = coefficients.length;
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
			String eq_string="0=";
			for(int i=0;i<coefficients.length-1;i++){
				if(coefficients[i]==1){
					eq_string=eq_string+"x^"+i+"+ ";
				}
				else{
					eq_string=eq_string+coefficients[i]+"x^"+i+"+";
				}
			}
			if(coefficients[coefficients.length-1]==1){
				eq_string=eq_string+"x^"+(coefficients.length-1);
			}
			else{
				eq_string=eq_string+coefficients[coefficients.length-1]+"x^"+(coefficients.length-1);
			}

			putString(10,5,terminal,eq_string);
			putString(10,7,terminal,"Add x^"+ next_highest_power + " term");
			terminal.moveCursor(10,8);
			putString(12,12,terminal,"Dimensions:"+width+"x"+height);
			graph(terminal, coefficients, width, height);

			terminal.applyForegroundColor(Terminal.Color.DEFAULT);
			terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
			Key key = terminal.readInput();

			if (key != null)
			{

				if (key.getKind() == Key.Kind.Escape) {

					terminal.exitPrivateMode();
					running = false;
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
				putString(1,4,terminal,"["+key.getCharacter() +"]");
				putString(1,1,terminal,key+"        ");//to clear leftover letters pad withspaces
			}

			terminal.applyBackgroundColor(Terminal.Color.DEFAULT);
			terminal.applyForegroundColor(Terminal.Color.DEFAULT);
			//DO EVEN WHEN NO KEY PRESSED:
			long tEnd = System.currentTimeMillis();
			long millis = tEnd - tStart;
			putString(1,2,terminal,"Milliseconds since start of program: "+millis);
			if(millis/1000 > lastSecond){
				lastSecond = millis / 1000;
				//one second has passed.
				putString(1,3,terminal,"Seconds since start of program: "+lastSecond);

			}


		}
	}
}
