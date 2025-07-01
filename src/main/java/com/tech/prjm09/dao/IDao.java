package com.tech.prjm09.dao;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tech.prjm09.dto.BDto;
import com.tech.prjm09.dto.ReBrdImgDto;

@Mapper
public interface IDao {
	public ArrayList<BDto> list(int startRow, int endRow, String sk, String selNum);
	public boolean write(String bname, String btitle, String bcontent);
	public BDto contentView(String sbid);
	public BDto modifyView(String sbid);
	public boolean modify(String bid,String bname,
			String btitle,String bcontent);
	public BDto replyView(String sbid);
	public boolean reply(String bid, String bname, String btitle, String bcontent, 
			String bindent, String bgroup, String bstep);
	public void replyShape(String strgroup, String strstep);
	public int delete(String sbid);
	public int selectBoardCount(String sk, String selNum);
	public int selectBid();
	public void imgwrite(int bid, String originalFile, String changeFile);
	public ArrayList<ReBrdImgDto> selectImg(String bid);
	public Map<String, Object> check_indent(String sbid, String sbgroup);
	public void upHit(String bid);
}
