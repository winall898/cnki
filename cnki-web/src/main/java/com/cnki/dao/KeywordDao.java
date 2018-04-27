package com.cnki.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cnki.vo.KeywordVo;
import com.cnki.vo.PageVo;


@Repository
public interface KeywordDao {

    /**
     * 获取分词总数
     * 
     * @param userName
     * @return
     */
    public int getKeywordCount(String keyword);

    /**
     * 获取分词列表
     * 
     * @param userName
     * @return
     */
    public List<KeywordVo> getKeywordList(String keyword, PageVo page);

    /**
     * 根据ID获取分词
     * 
     * @param id
     * @return
     */
    public KeywordVo getKeywordById(Long id);

    /**
     * 添加
     * 
     * @param keywordVo
     */
    public void addKeyword(KeywordVo keywordVo);

    /**
     * 修改
     * 
     * @param id
     */
    public void updateKeyword(KeywordVo keywordVo);

    /**
     * 根据ID删除分词
     * 
     * @param id
     */
    public void deleteKeywordById(Long id);

    /**
     * 根据ID集合批量删除
     * 
     * @param ids
     */
    public void batchDeleteKeyword(List<Long> ids);

    /**
     * 根据分词获取数量
     * 
     * @param keyword
     * @param id
     * @return
     */
    public int getCountByKeyword(String keyword, String id);
}
