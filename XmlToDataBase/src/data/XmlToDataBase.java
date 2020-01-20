package data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.ProtectVO;

public class XmlToDataBase {
	public static void main(String[] args) {
		Document doc;
		
		String bgnde="20190101";
		String endde="20200117";
		
		int totalCount =getTotalCount(bgnde, endde);
		System.out.println("전체 개수 : " +totalCount);
		
		for(int i=1;i<=(totalCount/1000)+1;i++) {
			toDB(bgnde, endde, i);			
		}	
	}

	private static void toDB(String bgnde, String endde,int i) {
		Document doc;
		ProtectVO vo = new ProtectVO();
		
		DBConn db = new DBConn();
		Connection conn = null;
		try {
        	conn = db.getConnection();
			String strUrl =	"http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?"
					+ "bgnde=" +bgnde
					+ "&endde=" +endde
					+ "&pageNo="+i
					+ "&numOfRows=1000"
					+ "&ServiceKey=O2GKwCxkLW84OZXlbc7wuiJE8cAieTSwRG%2FVOe3KQVKnFOoOpxqzYt7CsWezKxah0HuRCGpKAMj%2FqA7lhOG0Vg%3D%3D";
    
			doc = Jsoup.connect(strUrl).get();
			Elements item = doc.select("item");
			for (Element o : item) {
				vo.setAge(o.select("age").html());
	        	vo.setCareAddr(o.select("careAddr").html());
	        	vo.setCareNm(o.select("careNm").html());
	        	vo.setCareTel(o.select("careTel").html());
	        	vo.setChargeNm(o.select("chargeNm").html());
	        	vo.setColorCd(o.select("colorCd").html());
	        	vo.setDesertionNo(o.select("desertionNo").html());
	        	vo.setFilename(o.select("filename").html());
	        	vo.setHappenDt(o.select("happenDt").html());
	        	vo.setHappenPlace(o.select("happenPlace").html());
	        	vo.setKindCd(o.select("kindCd").html());
	        	vo.setNeuterYn(o.select("neuterYn").html());
	        	vo.setNoticeEdt(o.select("noticeEdt").html());
	        	vo.setNoticeNo(o.select("noticeNo").html());
	        	vo.setNoticeSdt(o.select("noticeSdt").html());
	        	vo.setOfficetel(o.select("officetel").html());
	        	vo.setOrgNm(o.select("orgNm").html());
	        	vo.setPopfile(o.select("popfile").html());
	        	vo.setProcessState(o.select("processState").html());
	        	vo.setSexCd(o.select("sexCd").html());
	        	vo.setSpecialMark(o.select("specialMark").html());
	        	vo.setWeight(o.select("weight").html());
				alter(vo, conn);
			}
			System.out.println(bgnde+"~"+endde+"_"+i+"_db 추가 성공");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.commit();
				conn.close();				
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}		
	}
	
	private static void alter(ProtectVO vo ,Connection conn) {
       	String sql;
		PreparedStatement pstmt = null;
        
       	sql = "insert into withanimal_protect values ("+
				"withanimal_protect_idx_seq.nextval,  ?, ?, ?, ?," + 
												  "?, ?, ?, ?, ?," + 
												  "?, ?, ?, ?, ?," + 
												  "?, ?, ?, ?, ?," + 
												  "?, ?, ? )";
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, vo.getAge());
		    pstmt.setString(2, vo.getCareAddr());
		    pstmt.setString(3, vo.getCareNm());
		    pstmt.setString(4, vo.getCareTel());
		    pstmt.setString(5, vo.getChargeNm());
		    pstmt.setString(6, vo.getColorCd());
		    pstmt.setString(7, vo.getDesertionNo());
		    pstmt.setString(8, vo.getFilename());
		    pstmt.setString(9, vo.getHappenDt());
		    pstmt.setString(10,vo.getHappenPlace());
		    pstmt.setString(11,vo.getKindCd());
		    pstmt.setString(12,vo.getNeuterYn());
		    pstmt.setString(13,vo.getNoticeEdt());
		    pstmt.setString(14,vo.getNoticeNo());
		    pstmt.setString(15,vo.getNoticeSdt());
		    pstmt.setString(16,vo.getOfficetel());
		    pstmt.setString(17,vo.getOrgNm());
		    pstmt.setString(18,vo.getPopfile());
		    pstmt.setString(19,vo.getProcessState());
		    pstmt.setString(20,vo.getSexCd());
		    pstmt.setString(21,vo.getSpecialMark());
		    pstmt.setString(22,vo.getWeight());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();			
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
	}	
	
	private static int getTotalCount(String bgnde, String endde) {
		Document doc;
		int i=0;
		try {
			String strUrl =	"http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?"
					+ "bgnde=" +bgnde
					+ "&endde=" +endde
					+ "&pageNo=1"
					+ "&numOfRows=0"
					+ "&ServiceKey=O2GKwCxkLW84OZXlbc7wuiJE8cAieTSwRG%2FVOe3KQVKnFOoOpxqzYt7CsWezKxah0HuRCGpKAMj%2FqA7lhOG0Vg%3D%3D";
    
			doc = Jsoup.connect(strUrl).get();
			Elements els = doc.select("body");
			for (Element el : els) {
				i = Integer.parseInt(el.select("totalCount").html().trim());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return i;
	}
}
