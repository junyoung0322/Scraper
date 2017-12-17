import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * scraper 
 * create - 2017-12-11
 * 
 * @author jun
 */
public class Scraper {
	public static void main(String[] args) {
		ArrayList<SearchModel> oModelList = new ArrayList<SearchModel>();
		String sUrl = "https://www.google.co.jp/search?q=%E6%B2%96%E7%B8%84%E3%80%80%E9%AB%98%E7%B4%9A%E3%83%9B%E3%83%86%E3%83%AB";

		try {
			ArrayList<String> sCrap = getSource(sUrl);
			for (String sSource: sCrap) {
				Matcher ｍTotalM = null;
				Matcher ｍUrlM = null;
				Matcher mTitleM = null;

				if (sSource.contains("<div class=\"_NId\"><div class=\"srg\">")) {
					String sEntRegex = "<h3 class=\"r\"><a href=\"(.+?)</a>";
					String sUrlRegex = "<h3 class=\"r\"><a href=\"(.+?)\"";
					String sTitleRegex = ",event\\)\">(.+?)</a>";
					ｍTotalM = Pattern.compile(sEntRegex).matcher(sSource);

					while (ｍTotalM.find()) {
						SearchModel oModel = new SearchModel();
						String sEntry = ｍTotalM.group(0);

						ｍUrlM = Pattern.compile(sUrlRegex).matcher(sEntry);
						mTitleM = Pattern.compile(sTitleRegex).matcher(sEntry);
						while (ｍUrlM.find()) {
//							System.out.println(urlM.group(1));
							oModel.setHref(ｍUrlM.group(1));
						}
						while (mTitleM.find()) {
//							System.out.println(titleM.group(1));
							oModel.setTitle(mTitleM.group(1));
						}
						oModelList.add(oModel);
					}
//					Matcher mNew = null;
//					String sNewRegex = "(<h3 class=\"r\"><a href=\"(.+?)\") (,event\\)\">(.+?)</a>)";
//					mNew = Pattern.compile(sNewRegex).matcher(sSource);
//					while (mNew.find()) {
//						System.out.println(mNew.group(1));
//						System.out.println(mNew.group(2));						
//					}
					
					printList(oModelList);
				}
			}
		}
		catch (MalformedURLException e) {

		}
		catch (IOException e) {

		}
		catch (Exception e) {

		}
	}
	public static void printList(ArrayList<SearchModel> mList) throws Exception {
		String sHeader= "<<<  GOOGLE 検索結果 - \"沖縄　高級ホテル\"  >>>";
		String sBoundary = "======================================";
		
		System.out.println(sHeader);
		System.out.println(sBoundary);
		for (int i = 0; i < 10; i++) {
			SearchModel m = mList.get(i);
			System.out.println("<< Title : " + m.getTitle() + " >>");
			System.out.println("    Href : " + m.getHref());
			System.out.println("------------------------");
		}
		System.out.println("");
		System.out.println(sBoundary);
	}

	public static ArrayList<String> getSource(String sUrl) throws MalformedURLException, IOException
	{
		ArrayList<String> output = new ArrayList<>();

		URL urlObject = new URL(sUrl);
		URLConnection urlCon = urlObject.openConnection();
		urlCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");

		InputStream inputStream = urlCon.getInputStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		String sLine;
		while ((sLine = br.readLine()) != null) {
			output.add(sLine);
		}
		br.close();
		
		return output;
	}
}




