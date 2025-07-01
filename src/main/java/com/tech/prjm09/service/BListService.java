package com.tech.prjm09.service;

import java.util.Map;

import org.springframework.ui.Model;

import com.tech.prjm09.dao.IDao;
import com.tech.prjm09.util.SearchVO;

import jakarta.servlet.http.HttpServletRequest;

public class BListService implements BServiceInter{
	private IDao iDao;
	
	public BListService(IDao iDao){
		this.iDao = iDao;
	}
	
	@Override
	public void execute(Model model) {
		System.out.println(">>>>>> BListService");
		Map<String, Object> map=model.asMap();
		HttpServletRequest request=
				(HttpServletRequest) map.get("request");
		SearchVO searchVO = (SearchVO) map.get("searchVO");
		String bid=request.getParameter("bid");
		
		
		//기본페이지
		String strPage = request.getParameter("page");
		if(strPage==null || strPage=="" || strPage == "0") {
			strPage = "1";
		}
		//검색
		String btitle = "";
		String bcontent = "";
		String[] brdTitle = request.getParameterValues("searchType");
		if(brdTitle != null) {
			for (String val : brdTitle) {
				if(val.equals("btitle")) {
					model.addAttribute("btitle", "true");
					btitle = "btitle";
				}
				if(val.equals("bcontent")) {
					model.addAttribute("bcontent", "true");
					bcontent = "bcontent";
				}
			}
		}
		String searchKeyword = request.getParameter("sk");
		if(searchKeyword == null) {
			searchKeyword = "";
		}
		
		int total = 0;
		//페지네이션에 영향을 줄 검색
		if(btitle.equals("btitle") && bcontent.equals("")) {
			total = iDao.selectBoardCount(searchKeyword, "1");
		}else if(btitle.equals("") && bcontent.equals("bcontent")) {
			total = iDao.selectBoardCount(searchKeyword, "2");
		}else if(btitle.equals("btitle") && bcontent.equals("bcontent")) {
			total = iDao.selectBoardCount(searchKeyword, "3");
		}else if(btitle.equals("") && bcontent.equals("")) {
			total = iDao.selectBoardCount(searchKeyword, "4");
		}
		searchVO.pageCalculate(total);
		
		//pagination
		int page = Integer.parseInt(strPage);
		searchVO.setPage(page);		
		int startRow = searchVO.getRowStart();
		int endRow = searchVO.getRowEnd();
		
		if(btitle.equals("btitle") && bcontent.equals("")) {
			model.addAttribute("list", iDao.list(startRow, endRow, searchKeyword, "1"));
		}else if(btitle.equals("") && bcontent.equals("bcontent")) {
			model.addAttribute("list", iDao.list(startRow, endRow, searchKeyword, "2"));
		}else if(btitle.equals("btitle") && bcontent.equals("bcontent")) {
			model.addAttribute("list", iDao.list(startRow, endRow, searchKeyword, "3"));
		}else if(btitle.equals("") && bcontent.equals("")) {
			model.addAttribute("list", iDao.list(startRow, endRow, searchKeyword, "4"));
		}
		
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("totRowCnt", total);
		model.addAttribute("searchVO", searchVO);
	}

}
