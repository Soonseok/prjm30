package com.tech.prjm09.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tech.command.BCommand;
import com.tech.prjm09.dao.IDao;
import com.tech.prjm09.dto.BDto;
import com.tech.prjm09.dto.ReBrdImgDto;
import com.tech.prjm09.util.SearchVO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BController {
	BCommand command;
	
	private final IDao iDao;
	
	@Autowired
	public BController(IDao iDao) {
		this.iDao = iDao;
	}
	
	@RequestMapping("/")
	public String init() {
		return "redirect:list";
	}
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request, Model model, SearchVO searchVO) {
		System.out.println("list() ctr");
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
		return "list";
	}

	@RequestMapping("/write_view")
	public String write_view(Model model) {
		System.out.println("write_view() ctr");
		
		return "write_view";
	}
	
//	@RequestMapping("/write")
//	public String write(HttpServletRequest request,
//			Model model) {
//		System.out.println("write() ctr");
//		String bname=request.getParameter("bname");
//		String btitle=request.getParameter("btitle");
//		String bcontent=request.getParameter("bcontent");
//		iDao.write(bname, btitle, bcontent);
//		return "redirect:list";
//	}
	@RequestMapping("/write")
	public String write(MultipartHttpServletRequest mtfRequest,
			Model model) {
		System.out.println("write() ctr");
		String bname=mtfRequest.getParameter("bname");
		String btitle=mtfRequest.getParameter("btitle");
		String bcontent=mtfRequest.getParameter("bcontent");
		boolean result = iDao.write(bname, btitle, bcontent);
		int bid = iDao.selectBid();
		
		String workPath = System.getProperty("user.dir");
		System.out.println(workPath);
		//String root = "C:\\hsts2025\\sts25_work\\prjm29replyboard_mpsupdown_multi\\src\\main\\resources\\static\\files";
		String root = workPath + "\\src\\main\\resources\\static\\files";
		List<MultipartFile> fileList = mtfRequest.getFiles("file");
		for (MultipartFile mf : fileList) {
			String originalFile = mf.getOriginalFilename();
			System.out.println(originalFile);
			long milsec = System.currentTimeMillis();
			String changeFile = milsec + "_" + originalFile;
			System.out.println(changeFile);
			
			try {
				String pathFile = root + "\\" + changeFile;
				if(!originalFile.equals("")) {
					mf.transferTo(new File(pathFile));
					System.out.println("Upload Successed");
					iDao.imgwrite(bid, originalFile, changeFile);
					System.out.println("rebrdimgtb write successed");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String message = result ? "write_success" : "write_failure";
	    return "redirect:list?result=" + message;
	}
	
	@RequestMapping("/content_view")
	public String content_view(HttpServletRequest request,
			Model model) {
		System.out.println("content_view() ctr");
		String bid=request.getParameter("bid");
		BDto dto = iDao.contentView(bid);
		model.addAttribute("content_view",dto);
		
		ArrayList<ReBrdImgDto> imgList = iDao.selectImg(bid);
		model.addAttribute("imgList", imgList);
		
		return "content_view";
	}
	
	@RequestMapping("/download")
	public String download(HttpServletRequest request,
			Model model, HttpServletResponse response) throws Exception {
		System.out.println("download() ctr");
		String fname = request.getParameter("f");
		String bid = request.getParameter("bid");
		
		response.setHeader("Content-Disposition", "Attachment;filename="+URLEncoder.encode(fname, "utf-8"));
		String workPath = System.getProperty("user.dir");
		String realPath = workPath+"\\src\\main\\resources\\static\\files\\"+fname;
		
		FileInputStream fin = new FileInputStream(realPath);
		ServletOutputStream sout = response.getOutputStream();
		
		byte[] buf = new byte[1024];
		int size = 0;
		while((size=fin.read(buf, 0, 1024))!= -1) {
			sout.write(buf, 0, size);
		}
		fin.close();
		sout.close();
		
		return "content_view?bid="+bid;
	}
	
	@RequestMapping("/modify_view")
	public String modify_view(HttpServletRequest request,
			Model model) {
		System.out.println("modify_view() ctr");
		String bid=request.getParameter("bid");
		BDto dto = iDao.modifyView(bid);
		model.addAttribute("content_view", dto);
		return "modify_view";
	}
	
	@PostMapping("/modify")
	public String modify(HttpServletRequest request,
			Model model) {
		System.out.println("modify() mybatis");
		String bid=request.getParameter("bid");
		String bname=request.getParameter("bname");
		String btitle=request.getParameter("btitle");
		String bcontent=request.getParameter("bcontent");
		boolean result = iDao.modify(bid, bname, btitle, bcontent);
		
		String message = result ? "modify_success" : "modify_failure";
	    return "redirect:list?result=" + message;
	}
	
	@RequestMapping("/reply_view")
	public String reply_view(HttpServletRequest request, Model model) {
		System.out.println("reply_view() ctr");
		String bid=request.getParameter("bid");
		BDto dto = iDao.replyView(bid);
		model.addAttribute("reply_view", dto);
		
		return "reply_view";
	}
	
	@PostMapping("/reply")
	public String reply(HttpServletRequest request,
			Model model) {
		System.out.println("reply() mybatis");
		String bid=request.getParameter("bid");
		String bname=request.getParameter("bname");
		String btitle=request.getParameter("btitle");
		String bcontent=request.getParameter("bcontent");
		String bindent=request.getParameter("bindent");
		String bstep=request.getParameter("bstep");
		String bgroup=request.getParameter("bgroup");
		iDao.replyShape(bgroup, bstep);
		boolean result = iDao.reply(bid, bname, btitle, bcontent, bindent, bgroup, bstep);
		
		String message = result ? "reply_success" : "reply_failure";
	    return "redirect:list?result=" + message;
	}
	
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request) {
	    String bid = request.getParameter("bid");
	    String bgroup = request.getParameter("bgroup");

	    Map<String, Object> resultMap = iDao.check_indent(bid, bgroup);
	    System.out.println(resultMap);
	    
	    try {
	        BigDecimal stepObj = (BigDecimal) resultMap.get("OWN_STEP");
	        BigDecimal indentObj = (BigDecimal) resultMap.get("OWN_INDENT");
	        BigDecimal maxSameObj = (BigDecimal) resultMap.get("MAX_SAME");
	        BigDecimal maxIndentObj = (BigDecimal) resultMap.get("MAX_INDENT");

	        if (stepObj == null || indentObj == null || maxSameObj == null || maxIndentObj == null) {
	            return "redirect:list?result=delete_failure";
	        }

	        int ownStep = stepObj.intValue();
	        int ownIndent = indentObj.intValue();
	        int maxSame = maxSameObj.intValue();
	        int maxIndent = maxIndentObj.intValue();

	        boolean removable = (maxSame == ownStep) || 
	                            (maxSame != ownStep && ownIndent == maxIndent);

	        boolean result = false;
	        if (removable) {
	            result = iDao.delete(bid) > 0;
	        }

	        String message = result ? "delete_success" : "delete_failure";
	        return "redirect:list?result=" + message;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:list?result=delete_failure";
	    }
	}
	
}
