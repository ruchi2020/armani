
import java.net.*;
import java.io.*;
import java.util.Date;
import java.sql.Timestamp;
/**
 * 
 * @author Vishal Yevale
 * This is Server socket for handling request (100 , 106 , 150 and 111) from POS
 *
 */
public class ArmaniServerSocket {
	public static void main(String[] args)
	{  
		ServerSocket serverSocket=null;
		Socket socket=null;
		try{  
			// created once for all request with common port no.
			serverSocket=new ServerSocket(1998);  
			System.out.println("Server Started . .");
		} catch (IOException e) {
		      System.out.println(e);
		    }
			
			while(true)
			{
				try
				{
					socket=serverSocket.accept();  
					System.out.println("Client connected");
					new RequestThread(socket).start();
					
				} catch (IOException e) 
		      		{
						System.out.println(e);
		      		}
		    }				
		}
	}
class RequestThread extends Thread {
	  Socket socket = null;
	  BufferedReader dataInput=null;
	  OutputStream os=null;
	  BufferedWriter bw = null;
	  StringBuffer sb=null;
	  int timeout = 10;
      int temp=0;
		String response = "";
		String type="";
		String accNo="";
		String amount="";

	  public RequestThread(Socket socket) {
	    this.socket = socket;
	  }

	  /**
	   * Read request from POS server and write response to server
	   */
	  public void run() 
	  {
		    Date date=new Date();
		    try {
		  while(true)
	    	{
			  String input=null;
	    		
	    		dataInput = new BufferedReader(
	    			new InputStreamReader(socket.getInputStream()));	
	    		
	    			sb = new StringBuffer();
	    			char STX = '\002';
	    			char ETX = '\003';
	    			int value = 0;
	    			if(dataInput.toString().length()>0)
	    			{
	    				while ((value = dataInput.read()) != -1) 
	    				{
	    					char c = (char) value;
	    					if (c == STX)
	    						continue;
	    					// end? jump out
	    					if (c == ETX)
	    					{
	    						break;
	    					}
	    					sb.append(c);
	    			
	    				}
	    				if(sb.toString()!=null){
	    				System.out.println((new Timestamp(date.getTime()))+"  Request received as "+(sb.toString()));
	    				}
	    				input=sb.toString();
	    			}
	    		if(!(sb.toString()).isEmpty())
		    	{
	    			
	    			String fields[]=input.split(",");
	    			if(input.startsWith("106"))
	    			{
	    				System.out.println("It is ping request");
	    				response="106,,,,,,,53,223,$Id: cafipay.c 1.18 2000/04/13 17:21:48 nbangia Exp nbangia $,172.18.1.222";
	    			}
	    			else if(input.startsWith("150"))
	    			{
	    				System.out.println("Item bucket request");
	    				response="151,,,0,26,,,695,8,Prompt,,,Please Sign,,,,SigFile.bmp,,,,*SIGNATURE";
	    			}
	    			else if(input.startsWith("111")){
	    				System.out.println("there is bank link down so it's 111 request to store data");
	    				response=input;
	    			}else if(input.startsWith("107")){
	    					 response = "107,2,5896290340000135=6146548641361456";	
	    					 //response = "107,0,5896290340000135,4912,,,,,4,,,,,,,,,,,, *CEM_MANUAL,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,5896290340000135,,";
	    			}
	    			else if(input.startsWith("100"))
	    			{
	    				try{ System.out.println("Threding is about to sleep");
	    					Thread.currentThread().sleep(120000);
	    					System.out.println("Threding is released");
	    					} catch(Exception e){System.out.println("got exception");}
	    				amount=fields[15];
	    				String gcNo="";
	    				 if(fields[5].toUpperCase().startsWith("GIFTCARD") && fields[9].toUpperCase().startsWith("ACTIVATE")){
	    					response="101,,,0,45,GIFTCARD,,485,4,ISSUE,,,,,5896290340000135,0001000,01900802,,,1000, DebitAckReq,,,,,,,,,,,066880,,,,,001000,Approved ,,,,,,,trace\20080614.fip 101064011783,436619,,436619,,06142008,170858,1000,01,SVSGCD1,0,,40701802,,,,,,124,170858867,,,,,,,,,,,,,,,,,,Approved ,,"; 
	    					}
	    				 else if(fields[5].toUpperCase().startsWith("GIFTCARD") && fields[9].toUpperCase().startsWith("CASHOUT")){
	    					 response="101,,,1,90,GIFTCARD,,485,4,Cashout,,,,,5896290340000135,"+amount+",01876494,,,2001,RemainTender DebitAckReq,,,,,,,,,,,066880,,,,,000000,Approved ,,,,,,,trace\20080516.fip59685 004308,401254,,401254,,05162008,160608,-1,01,SVSGCD1,0,,40701494,,,,,,140,160608862,,,,,,,,,,,,,,,,,,Approved ,,";
	    				 }
	    				 else if(fields[5].toUpperCase().startsWith("GIFTCARD") && fields[9].toUpperCase().startsWith("VOID")){
	    					 gcNo=fields[12];
	    					// response="101,,,0,90,GIFTCARD,,485,4,Voidsale,,,,,5896290340000135,"+amount+",01876494,,,2001,RemainTender DebitAckReq,,,,,,,,,,,066880,,,,,000000,Approved ,,,,,,,trace\20080516.fip59685 004308,401254,,401254,,05162008,160608,-1,01,SVSGCD1,0,,40701494,,,,,,140,160608862,,,,,,,,,,,,,,,,,,Approved ,,";
	    				     response="101,,,0,115,giftcard,,485,4,VoidSale,,,"+gcNo+",,,"+amount+",4854491164172,English,,,noexpcheck nomod10 *DEVCAP=F0 *E2EE *ReqTOR *StoreSTAN 040023 *FloorLimit_30000,,,,,,,,,,,,,5896294709070408,,1000,012728,,,,,,,,00400002,000621,,000621,English,03152017,132147,53750,00,MPSDPN1,1,,48504172,,,,,,869,132148093,,,,,,,,,,,,,,,,200400002,,";

	    				 }
	    				 else if(fields[5].toUpperCase().startsWith("GIFTCARD") && fields[9].toUpperCase().startsWith("SALE")){
	    					 gcNo=fields[12];
	    				     response="101,,,0,115,giftcard,,485,4,Sale,,,"+gcNo+",,,"+amount+",4854491164172,English,,,noexpcheck nomod10 *DEVCAP=F0 *E2EE *ReqTOR *StoreSTAN 040023 *FloorLimit_30000,,,,,,,,,,,,,5896294709070408,,1000,012728,,,,,,,,00400002,000621,,000621,English,03152017,132147,53750,00,MPSDPN1,1,,48504172,,,,,,869,132148093,,,,,,,,,,,,,,,,200400002,,";

	    					// response="101,,,0,90,GIFTCARD,,485,4,sale,,,,,5896290340000135,"+amount+",01876494,,,2001,RemainTender DebitAckReq,,,,,,,,,,,066880,,,,,000000,Approved ,,,,,,,trace\20080516.fip59685 004308,401254,,401254,,05162008,160608,-1,01,SVSGCD1,0,,40701494,,,,,,140,160608862,,,,,,,,,,,,,,,,,,Approved ,,";
	    					 //response="101,,,0,115,giftcard,,485,4,Sale,,,5896290340000135,,,38150,4854150281444,English,,,noexpcheck nomod10 DEVCAP=F0 E2EE ReqTOR StoreSTAN 040655  *FloorLimit_30000,,,,,,,,,,,,,5896293876970135,,1000,008195,,,,,,,,50400102,002299,,002299,English,11112016,121805,111850,00,MPSDPN1,1,,48504444,,,,,,1115,121806587,,,,,,,,,,,,,,,,250400102,,";
	    				 }
	    				 else if(fields[5].toUpperCase().startsWith("GIFTCARD") && fields[9].toUpperCase().startsWith("RELOAD")){
	    					 response="101,,,1,115,giftcard,,485,4,Reload,,,,,5896290340000135,100000,4854150199188,English,,,noexpcheck nomod10 DEVCAP=F0 E2EE ReqTOR StoreSTAN 040300  *FloorLimit_0,,,,,,,,,,,,,5896291926830887,,1000,001222,,,,,,,,50400015,001937,,001937,English,10272016,161546,120000,00,MPSDPN1,1,,48504188,,,,,,804,161547032,,,,,,,,,,,,,,,,250400015,,";	    					 
	    				 }
	    				 else if(fields[5].toUpperCase().startsWith("GIFTCARD") && fields[9].toUpperCase().startsWith("BALANCEINQUIRY")){
	    					 gcNo=fields[15];
	    						//response = "101,,,0,45,GIFTCARD,,000407,01,balanceInquiry,,,,,123456789789,600.53,01000001,,,0,,,,,,,,,,,,066880,,,,,000000,Approved ,,,,,,,trace\20080630.fip 76407 017761,457936,,457936,,06302008,164339,0,01,SVSGCD1,0,,40701001,,,,,,109,164339199,,,,,,,,,,,,,,,,,,Approved ,,";			
	    					 response="101,,,0,115,giftcard,,485,4,balanceInquiry,,,,,9922151098,,4854150252341,English,,,noexpcheck nomod10 DEVCAP=F0 E2EE StoreSTAN 040508  FloorLimit_0,,,,,,,,,,,,,5896291643100879,,1000,004051,,,,,,,,50400022,002149,,002149,English,11082016,144309,100000,900000,MPSDPN1,1,,48504341,,,,,,799,144310181,,,,,,,,,,,,,,,,250400022,,";
	    				//response="101,,,0,115,giftcard,,485,4,balanceInquiry,,,,,5896290340000135,,4854150281391,English,,,noexpcheck nomod10 DEVCAP=F0 E2EE StoreSTAN 040654  FloorLimit_0,,,,,,,,,,,,,5896293876970135,,1000,008194,,,,,,,,50400101,002298,,002298,English,11112016,121803,000150000,00,MPSDPN1,1,,48504391,,,,,,883,121804362,,,,,,,,,,,,,,,,250400101";
	    				 }
	    				 else if(fields[5].toUpperCase().startsWith("GIFTCARD") && fields[9].toUpperCase().startsWith("REFUND")){
	    						//response = "101,,,0,45,GIFTCARD,,000407,01,balanceInquiry,,,,,123456789789,600.53,01000001,,,0,,,,,,,,,,,,066880,,,,,000000,Approved ,,,,,,,trace\20080630.fip 76407 017761,457936,,457936,,06302008,164339,0,01,SVSGCD1,0,,40701001,,,,,,109,164339199,,,,,,,,,,,,,,,,,,Approved ,,";			
	    					 response="101,,,0,115,giftcard,,485,4,Refund,,,,,107,2,5896290340000135,100000,4854150199188,English,,,noexpcheck nomod10 DEVCAP=F0 E2EE ReqTOR StoreSTAN 040300  *FloorLimit_0,,,,,,,,,,,,,5896291926830887,,1000,001222,,,,,,,,50400015,001937,,001937,English,10272016,161546,12000,00,MPSDPN1,1,,48504188,,,,,,804,161547032,,,,,,,,,,,,,,,,250400015,,";	    					 
	    				 }
	    				//response="101,,,0,115,credit,000022812545,485,4,sale,,MAESTRO,411774******5110,1909,,"+amount+",414311786207,English,,,NoExpCheck CEM_Swiped DEVCAP=F0 SIGNATURE E2EE ReqTOR StoreSTAN 030976 CardLevel=  CNOSIGNATURE,,,,,,,,,,,,,4117744075925110,,1000,182612,,1340,,,,,E2861296606837305JRB,,003635,12926834862,003635,English,05082016,142036,,,MPSDPN1,1,,41403207,,,,,,733,142109247,,,,,,,,,,,,,,,,026834862,,,,,,,,,,,,4117744163125110=19092015678919614605,,01010089250260150b289-753-287160d028975328700083108f298cee75a2e44e841037ed0912a1437adb851037a89a0664f0e1ea861037a89a0664f0e1ead809000000065d3020908c8,,";
	    				//response="101,,,3,115,credit,,485,4,sale,,VISA,405166******9853,1901,,"+amount+",4854150121778,English,,,noexpcheck nomod10   CEM_Swiped DEVCAP=F0 SIGNATURE E2EE ReqTOR StoreSTAN 040172   CNOSIGNATURE FloorLimit_30000,,,,,,,,,,,,,,,706,,SYSTEM PROBLEM LINK DOWN ,,,,,,,,48504778,,,English,10192016,155057,,,MPSDPN,22,,,,,,SYSTEM PROBLEM LINK DOWN ,,21508,155128691,,,,,,,,,,,,,,,,,SYSTEM PROBLEM LINK DOWN ,,,,,,,,SAFable,,,4051666751659853=190110117092018,,01010089250260150b288-796-690160d02887966900008310d42de65d7530c1b68410d42de65d7530c1b685109a6e9ded5bb238e786109a6e9ded5bb238e7d809000000083d30209023e,,";
	    				 else if(fields[5].startsWith("debit") && fields[9].startsWith("Binlookup")){
	    					System.out.println("Payment Type and it's authorization request");
		    				 amount=fields[15];
		    				 if(temp==0){
		    				response="101,,,0,115,credit,000022812545,485,4,sale,,MAESTRO,411774******5110,1909,,"+amount+",414311786207,English,,,NoExpCheck CEM_Swiped DEVCAP=F0 SIGNATURE E2EE ReqTOR StoreSTAN 030976 CardLevel=  CNOSIGNATURE,,,,,,,,,,,,,4117744075925110,,1000,182612,,1340,,,,,E2861296606837305JRB,,003635,12926834862,003635,English,05082016,142036,,,MPSDPN1,1,,41403207,,,,,,733,142109247,,,,,,,,,,,,,,,,026834862,,,,,,,,,,,,4117744163125110=19092015678919614605,,01010089250260150b289-753-287160d028975328700083108f298cee75a2e44e841037ed0912a1437adb851037a89a0664f0e1ea861037a89a0664f0e1ead809000000065d3020908c8,,";
		    				temp++;
		    				 }
		    				 else if(temp==1){
			    				    response="101,,,0,115,credit,,485,4,sale,,AMEX,374245*****1006,1909,,"+amount+",4854491164759,English,,,noexpcheck nomod10 *CEM_Swiped *DEVCAP=F0 *SIGNATURE *E2EE *ReqTOR *StoreSTAN 040025 *CNOSIGNATURE *FloorLimit_0,,,,,,,,,,,,,3059991747550022,,1000,093124,,,,,,,,50400004,000623,01023965529,000623,English,03152017,132341,,00,MPSDPN1,1,,48504759,,,,,,1496,132355320,,,,,,,,,,,,,,,,250400004,,,,,,,,,,,,3059996778560022=19091012764516017805,,01010089250260150b288-796-690160d028879669000083100542f79c8143f84c84100542f79c8143f84c85109a6e9ded5bb238e786109a6e9ded5bb238e7d809000000083d30209615d,,";		    				 
		    				temp++;
		    				 }
		    				 else if(temp==2){
		    				    response="101,,,0,115,credit,,485,4,sale,,DINERS,305999******0022,1909,,"+amount+",4854491164759,English,,,noexpcheck nomod10 *CEM_Swiped *DEVCAP=F0 *SIGNATURE *E2EE *ReqTOR *StoreSTAN 040025 *CNOSIGNATURE *FloorLimit_0,,,,,,,,,,,,,3059991747550022,,1000,093124,,,,,,,,50400004,000623,01023965529,000623,English,03152017,132341,,00,MPSDPN1,1,,48504759,,,,,,1496,132355320,,,,,,,,,,,,,,,,250400004,,,,,,,,,,,,3059996778560022=19091012764516017805,,01010089250260150b288-796-690160d028879669000083100542f79c8143f84c84100542f79c8143f84c85109a6e9ded5bb238e786109a6e9ded5bb238e7d809000000083d30209615d,,";
				    				temp++;    		
		    					 }
		    				 else{
		    					 response="101,,,0,115,credit,,248,2,sale,,VISA,5413335103860060,2512,5413335103860060=2512201635279213,"+amount+",2482120640140,English,,,noexpcheck nomod10     *CEM_Swiped *FALLBACK_EMV *DEVCAP=E0 *SIGNATURE *E2EE *ReqTOR *StoreSTAN 020158  *CNOSIGNATURE *FloorLimit_0,,,,,,,,,,,,,5413335145520060,,1000,091196,,,,,,261101204140,Y0906MCC003338  NN  ,,000186,01324259520,000186,English,09062016,155022,,,MPSDPN1,1,,24802140,,,,,,718,155049809,,,,,,,,,,,,,,,,250200084,,,,,,,,,,,,5413335103860060=2512201635279213,,01010061250220150b288-796-690160d02887966900008410dfe708c1f6505d7e8610812574e4790f98f0d809000000083d302094096,,_RAW DATA REQUEST=false";
		    				temp=0;
		    				 }
		    				 //response="101,,,0,115,Credit,,248,2,Sale,,AMEX,374245*****1006,1810,,2644,2482121264799,English,,,noexpcheck nomod10 *EMV *CEM_Insert *DEVCAP=E0 *E2EE *ReqTOR *StoreSTAN 020199  *SIGNATURE *CNOSIGNATURE *FloorLimit_0,,,,,,,,,,,,,374245677371006,,1000,064896,Approved,,,,,261101204140,,,000226,01387175188,000226,English,09092016,152543,,,MPSDPN1,1,<4f>a000000025010802</4f><50>AMEX 2</50><5f25>131001</5f25><5f28>0840</5f28><5f2a>0840</5f2a><5f30>0201</5f30><5f34>00</5f34><82>3c00</82><84>a000000025010802</84><8a>00</8a><8e>0000000000000000420142035f0300000000000000000000</8e><8f>c3</8f><91>28a78e16846c285d0012</91><95>0800808000</95><9a>160909</9a><9b>f800</9b><9c>00</9c><9f02>000000002644</9f02><9f03>000000000000</9f03><9f06>a000000025010802</9f06><9f09>0001</9f09><9f0d>fc50eca800</9f0d><9f0e>0000000000</9f0e><9f0f>fc78fcf800</9f0f><9f10>06020103a40002</9f10><9f1a>0840</9f1a><9f1b>00000000</9f1b><9f1e>88796690</9f1e><9f21>152543</9f21><9f26>5c221dc28eb72fcf</9f26><9f27>40</9f27><9f33>e0b0c8</9f33><9f34>000001</9f34><9f35>22</9f35><9f36>0001</9f36><9f37>f03d132a</9f37><9f39>05</9f39><9f41>00000005</9f41><9f53>52</9f53><appname>AMEX 2</appname><authenstatus>31</authenstatus><c2>31</c2><df03>C800000000</df03><df04>0000000000</df04><df05>C800000000</df05><emvgenac>Yes</emvgenac><final9f10>06020103640002</final9f10><language>31</language><online>Y</online><req95>0800808000</req95><req9f10>06020103a40002</req9f10><req9f26>357fd4f718f2bd44</req9f26><req9f27>80</req9f27><signature>Yes</signature>,24802799,,,,,,1071,152554803,,,,,,,,,,,,,,,,250200114,,,,,,,,,SAFable,,,374245863641006=181020138303003477568,,01010061250220150b288-796-690160d028879669000084109a86ea2e827a8f68861027a3f47a76e694eed809000000083d302094e3e,,";
		    				 String respFields[]=response.split(",");
		    				type=respFields[11];
		    				accNo=respFields[12];
		    				amount=fields[15];
	    				}else if(fields[5].startsWith("credit") && fields[9].startsWith("Binlookup")){
	    					System.out.println("Payment Type and it's authorization request");
		    				 amount=fields[15];
		    				 
		    				try{ Thread.currentThread().sleep(5000);} catch(Exception e){System.out.println("got exception");}
		    				//response="101,,,2,115,debit,000022812545,485,4,sale,,VISA,411774******5110,1909,,"+""+",414311786207,English,,,NoExpCheck CEM_Swiped DEVCAP=F0 SIGNATURE E2EE ReqTOR StoreSTAN 030976 CardLevel=  CNOSIGNATURE,,,,,,,,,,,,,4117744075925110,,1000,182612,,1340,,,,,E2861296606837305JRB,,003635,12926834862,003635,English,05082016,142036,,,MPSDPN1,1,,41403207,,,,,,733,142109247,,,,,,,,,,,,,,,,026834862,,,,,,,,,,,,4117744163125110=19092015678919614605,,01010089250260150b289-753-287160d028975328700083108f298cee75a2e44e841037ed0912a1437adb851037a89a0664f0e1ea861037a89a0664f0e1ead809000000065d3020908c8,,";
		    				response="101,,,0,115,debit,000022812545,485,4,sale,,VISA,411774******5110,1909,,"+amount+",414311786207,English,,,NoExpCheck CEM_Swiped DEVCAP=F0 SIGNATURE E2EE ReqTOR StoreSTAN 030976 CardLevel=  CNOSIGNATURE,,,,,,,,,,,,,4117744075925110,,1000,182612,,1340,,,,,E2861296606837305JRB,,003635,12926834862,003635,English,05082016,142036,,,MPSDPN1,1,,41403207,,,,,,733,142109247,,,,,,,,,,,,,,,,026834862,,,,,,,,,,,,4117744163125110=19092015678919614605,,01010089250260150b289-753-287160d028975328700083108f298cee75a2e44e841037ed0912a1437adb851037a89a0664f0e1ea861037a89a0664f0e1ead809000000065d3020908c8,,";
		    				String respFields[]=response.split(",");
		    				type=respFields[11];
		    				accNo=respFields[12];
	    				}else if(fields[5].startsWith("credit") && fields[9].startsWith("Refund")){
	    					System.out.println("This is refund request (credit)");
		    				 amount=fields[15];
	    					response="101,,,0,118,credit,000001874883,248,2,Refund,,"+type+","+accNo+",1912,,"+amount+",2482120426706,English,,,NoExpCheck DEVCAP=F0 ReqTOR StoreSTAN 020216  NOSIGNATURE *CSIGNATURE,,,,,,,,,,,,,379016763021358,,1000,017623,,1340,,,,261101604140,,,000245,11337483713,000245,English,04222016,141523,,,MPSDPN1,0,,24802706,,,,,,419,141524846,,,,,,,,,,,,,,,,037483713,,,,,,,,,SAFable,,";
	    				}else if(fields[5].startsWith("debit") && fields[9].startsWith("Refund")){
	    					System.out.println("This is refund request (debit)");
		    				 amount=fields[15];
	    					response="101,,,0,118,debit,000001874883,248,2,Refund,,"+type+","+accNo+",1912,,"+amount+",2482120426706,English,,,NoExpCheck DEVCAP=F0 ReqTOR StoreSTAN 020216  NOSIGNATURE *CSIGNATURE,,,,,,,,,,,,,379016763021358,,1000,017623,,1340,,,,261101604140,,,000245,11337483713,000245,English,04222016,141523,,,MPSDPN1,0,,24802706,,,,,,419,141524846,,,,,,,,,,,,,,,,037483713,,,,,,,,,SAFable,,";
	    				
	    				}else if(fields[5].startsWith("Void")){
	    					System.out.println("this is void request (delete tender)");
		    				 amount=fields[15];
	    					response="101,,,0,118,credit,000001874883,248,2,Voidsale,,"+type+","+accNo+",1912,,"+amount+",2482120419177,English,482983,,*OrgDateTime 1461349590 OrgIsoResp  cem_swiped e2ee NoExpCheck OrgInvoice 2482120419509 StoreSTAN 020209 DEVCAP=F0  NOSIGNATURE CSIGNATURE,,,,,,,,,,,,,379016763021358,,1000,483200,,1340,,,,261101604140,,,000236,,000238,English,04222016,134135,,,MPSDPN1,0,,24802177,,,,,,409,134135908,,,,,,,,,,,,,,,,37483200,,,,,,,,,SAFable,,,379016*****1358=1912*****************,,";
	    				}
	    				else{
	    					 response="101,,,0,115,giftcard,,485,4,Reload,,,,,9595909545,100000,4854150199188,English,,,noexpcheck nomod10 DEVCAP=F0 E2EE ReqTOR StoreSTAN 040300  *FloorLimit_0,,,,,,,,,,,,,5896291926830887,,1000,001222,,,,,,,,50400015,001937,,001937,English,10272016,161546,12000,00,MPSDPN1,1,,48504188,,,,,,804,161547032,,,,,,,,,,,,,,,,250400015,,";	    					 
	    					System.out.println("something wrong here ");
	    				}
	    		
	    				//response="101,,,0,115,Credit,,248,2,Sale,,MAESTRO,546616******3862,1905,,"+amount+",2482120929533,English,,,noexpcheck nomod10 PinpadReg_Success EMV CEM_Insert RTSEMVCapable DEVCAP=F0 E2EE ReqTOR StoreSTAN 020002  SIGNATURE CNOSIGNATURE *FloorLimit_0,,,,,,,,,,,,,5466168042513862,,1000,92528P,Approved,,,,,261101204140,Y0927MWEM8RX9Z  NN  ,50200002,000002,01766029663,000002,English,09272016,172642,,00,MPSDPN1,1,<4f>a0000000041010</4f><50>MasterCard</50><5f24>190531</5f24><5f25>160401</5f25><5f28>0840</5f28><5f2a>0840</5f2a><5f30>0201</5f30><5f34>01</5f34><82>3800</82><84>a0000000041010</84><8a>00</8a><8e>000000000000000042011e031f034203</8e><8f>05</8f><91>eb1e64699d874ddf0012</91><95>0000008000</95><9a>160927</9a><9b>e800</9b><9c>00</9c><9f02>000000000001</9f02><9f03>000000000000</9f03><9f06>a0000000041010</9f06><9f09>0002</9f09><9f0d>b8509c0800</9f0d><9f0e>0000000000</9f0e><9f0f>b8709c9800</9f0f><9f10>0110a0800322000069bb00000000000000ff</9f10><9f1a>0840</9f1a><9f1b>00000000</9f1b><9f1e>89752682</9f1e><9f21>172643</9f21><9f26>787e17b8a2113907</9f26><9f27>40</9f27><9f33>e0f8c8</9f33><9f34>1e0300</9f34><9f35>22</9f35><9f36>0035</9f36><9f37>166b55c0</9f37><9f39>05</9f39><9f41>00000001</9f41><9f53>52</9f53><appname>MasterCard</appname><authenstatus>31</authenstatus><c2>31</c2><df03>FC50B8A000</df03><df04>0400000000</df04><df05>FC50B8F800</df05><emvgenac>Yes</emvgenac><final9f10>011060900322000069bb00000000000000ff</final9f10><language>31</language><online>Y</online><req95>0000008000</req95><req9f10>0110a0800322000069bb00000000000000ff</req9f10><req9f26>9ca859792c7a3617</req9f26><req9f27>80</req9f27><signature>Yes</signature>,24802533,,,,,,764,172655235,,,,,,,,,,,,,,,,250200002,,,,,,,,,SAFable,,,5466165790363862=1905201124969968,,01010089250260150b289-752-682160d02897526820008310fda863cf198bd8c08410a9ee73234dffcf46851068a273f80de0b6d9861068a273f80de0b6d9d809000000065d3020952b4,,";
	    				//response="101,,,3,115,credit,,485,4,sale,,VISA,405166******9853,1901,,"+amount+",4854150121778,English,,,noexpcheck nomod10   CEM_Swiped DEVCAP=F0 SIGNATURE E2EE ReqTOR StoreSTAN 040172   CNOSIGNATURE FloorLimit_30000,,,,,,,,,,,,,,,706,,SYSTEM PROBLEM LINK DOWN ,,,,,,,,48504778,,,English,10192016,155057,,,MPSDPN,22,,,,,,SYSTEM PROBLEM LINK DOWN ,,21508,155128691,,,,,,,,,,,,,,,,,SYSTEM PROBLEM LINK DOWN ,,,,,,,,SAFable,,,4051666751659853=190110117092018,,01010089250260150b288-796-690160d02887966900008310d42de65d7530c1b68410d42de65d7530c1b685109a6e9ded5bb238e786109a6e9ded5bb238e7d809000000083d30209023e,,";

	    			}
	    			
	    			os = socket.getOutputStream();
	    		
	    			OutputStreamWriter osw = new OutputStreamWriter(os);
	    			bw = new BufferedWriter(osw);
	    			System.out.println((new Timestamp(date.getTime()))+"  Response sent as : "+ response+"\n");	 
	    			response='\002'+response+'\003';
	    	    		
	    			bw.write(response, 0, response.length());
	    			bw.flush();
	    		}
	    		else
	    		{
	    			break;
	    		}
	    	   
	    } }
	    		catch (IOException e) {
	    			System.out.println("Connection Closed");
	    		}
	    		
	   
	  }
	}


