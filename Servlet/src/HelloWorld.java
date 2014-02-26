/* Student Name: Tsu-Hao Kuo
 * Andrew ID: tkuo
 * course number: 15637
 * Date: Jan. 28, 2013
 * 
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
    	throws IOException, ServletException
    {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
				
		String A = request.getParameter("X");
        String B = request.getParameter("Y");
		if (A == null)
			A = "";
		if (B == null)
			B = "";
        
		/*pw.println("");
		pw.println("");
		pw.println("");
		pw.println("<h1>Hello World</h1>");
		pw.println("");*/
		pw.println("<html>");
		pw.println("<head>");
		pw.println("<Title> Assignment 1 </Title>");
		pw.println("<style type=\"text/css\">");
		pw.println("label {"); 
		pw.println("display:block;" +
				"margin:10px;" +
				"width:250px;" +
				"height:50px;" +
				"overflow:auto;" +
				"font-family:Helvetica, Arial;" +
				"font-size:25px;" +
				"color:yellow;" +
				"text-shadow:0 0 2px #ddd;" +
				"padding:1px 1px 1px 0;}");
		pw.println("button {" +
				"float:top;" +
				"margin:5px;" +
				"background-color:#6A5ACD;" +
				"width:50px;" +
				"height:30px;"+
				"border:2px solid #6A5ACD;"+
				"border-radius:10px;"+
				"color:white;"+
				"font-size:20px;"+
				"text-shadow: 1px 1px 10px #FFFFFF;}");
		pw.println("button:hover {"+
		    "border-color:#9932CC;"+
		    "box-shadow:0px 0px 30px #FF00FF;}");
		pw.println("button:active {"+
		    "color:red;"+
		"}");
		pw.println("input {"+
		    "width:200px;"+
		    "border:3px solid;"+
		    "border-radius:7px;"+
		    "font-size:15px;"+
		    "padding:5px;"+
		    "margin-top:-10px;}");
		pw.println("input:focus {"+ 
		    "outline:none;"+
		    "border-color:#00FFFF;"+
		    "box-shadow:0px 0px 20px #9ecaed;}");
		pw.println("</style>");
		pw.println("</head>" +
				"<body style=\"background-color:black\">" +
				"<form name=\"calculator\">" +
				"<center>" +
				"<h1 style=\"margin-left:10px;color:white\"> Calculator Application</h1>" +
				"<table border=\"0\" border-color=\"white\">" +
				"<tr>" +
				"<td>" +
				"<label> X: <input name=\"X\" type=\"text\" value = \""+ A + "\" > </label>" +
				"</td>" +
				"<td rowspan=\"2\">" +
				"<button type=\"submit\" name=\"button\" value=\"add\"><b>+</b></button><br>" +
				"<button type=\"submit\" name=\"button\" value=\"subtract\"><b>-</b></button><br>" +
				"<button type=\"submit\" name=\"button\" value=\"multiply\"><b>*</b></button><br> " +
				"<button type=\"submit\" name=\"button\" value=\"divide\"><b>/</b></button><br>" +
				"</td><tr>" +
				"<td>" +
				"<label> Y: <input name=\"Y\" type=\"text\"value = \""+ B + "\" > </label>" +
				"</td>" +
				"</tr>" +
				"</tr>" +
				"</table>" +
				"</center>" +
				"</form>" +
				"</body>" +
				"</html>");
		
		String operation = request.getParameter("button");
	    //String Reply = "";
		//String newline = System.getProperty("line.separator");
		boolean flag = false;
		
		if (operation != null) {
			if (!isNumeric(A)) {
				pw.println("<center><h1 style=\"color:white\"> X is not a number </h2></center>");
				flag = true;
			}
			if (!isNumeric(B)) {
				pw.println("<center><h1 style=\"color:white\"> Y is not a number </h2></center>");
				flag = true;
			}
			if (!flag) {
				double a = Double.parseDouble(A);
				double b = Double.parseDouble(B);
				double c = 0.0;
				String operator = "";
								
				if (operation.equals("add")) {c = a + b; operator = "+";}
				else if (operation.equals("subtract")) {c = a - b; operator = "-";}
				else if (operation.equals("multiply")) {c = a * b; operator = "*";}
				else if (operation.equals("divide")) {
					if (b == 0.0) {
						pw.println("<center><h1 style=\"color:white\">Cannot devided by zero</h2></center>");
						return;
					} else 
						c = a / b; operator = "/";
				}
				
				DecimalFormat df = new DecimalFormat("#,###,##0.00");
				pw.println("<center><h1 style=\"color:white\">" + df.format(a) + " " + operator + " " +  df.format(b) + " = " + df.format(c) + "</h2></center>");
			}
			
			
		}
				
    }
		public static boolean isNumeric(String str)
		{
			if (!str.equals("")) {
				NumberFormat formatter = NumberFormat.getInstance();
				ParsePosition pos = new ParsePosition(0);
				formatter.parse(str, pos);
				return str.length() == pos.getIndex();
			} else
				return false;
		}
    
}
