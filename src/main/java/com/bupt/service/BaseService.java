package com.bupt.service;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.mapper.GoodsSkuDAO;
import com.bupt.mapper.MyBatisBaseDao;
import com.bupt.mapper.StaffDAO;
import com.bupt.pojo.BasePojo;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@SuppressWarnings({"all"})
public class BaseService<Table extends BasePojo, Detail extends BasePojo,
        TableDAO extends MyBatisBaseDao,
        DetailDao extends MyBatisBaseDao> {
    @Autowired(required = false)
    public TableDAO tableDAO;
    @Autowired(required = false)
    public DetailDao detailDao;
    @Autowired
    StaffDAO staffDAO;
    @Autowired
    GoodsSkuDAO goodsSkuDAO;

    @Transactional
    public List<Table> screenTable(ScreenDTO<Table> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return tableDAO.screen(screenDTO);
    }

    @Transactional

    public Integer numScreenTable(ScreenDTO<Table> screenDTO) {
        return tableDAO.numScreen(screenDTO);
    }

    @Transactional
    public List<Detail> screenDetail(ScreenDTO<Detail> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        return detailDao.screen(screenDTO);
    }

    @Transactional
    public Integer numScreenDetail(ScreenDTO<Detail> screenDTO) {
        return detailDao.numScreen(screenDTO);
    }

    @Transactional
    public Integer add(Table table) {
        tableDAO.insertSelective(table);
        return table.getId();
    }

    @Transactional
    public Integer del(Table table) {
        return tableDAO.deleteByPrimaryKey(table.getId());
    }

    @Transactional
    public Integer upd(Table table) {
        return tableDAO.updateByPrimaryKeySelective(table);
    }

    @Transactional
    public HttpResult<?> updTable(HeadAndDetail<Table, Detail> headAndDetail) {
        tableDAO.updateByPrimaryKeySelective(headAndDetail.getHead());
        for (int i = 0; i < headAndDetail.getDetails().size(); i++) {
            detailDao.updateByPrimaryKeySelective(headAndDetail.getDetails().get(i));
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    public HttpResult<?> delTable(Table table) {
        List<Detail> detailList = detailDao.selectDetailByPid(table.getId());
        for (Detail detail : detailList)
            detailDao.deleteByPrimaryKey(detail.getId());
        tableDAO.deleteByPrimaryKey(table.getId());
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Transactional
    public Integer updDetail(List<Detail> detail) {
        for (Detail value : detail) {
            detailDao.updateByPrimaryKeySelective(value);
        }
        return detail.size();
    }

    @Transactional
    public Integer delDetail(List<Detail> detail) {
        for (Detail value : detail) {
            detailDao.deleteByPrimaryKey(value.getId());
        }
        return detail.size();
    }

    @Transactional
    public Integer insertDetail(List<Detail> details) {
        for (Detail detail : details) {
            detailDao.insertSelective(detail);
        }
        return details.size();
    }

    public Table audit(Table table) {
        table.setCheckPersonName(staffDAO.selectByPrimaryKey(table.getCheckPersonId()).getStaffName());
        table.setCheckTime(new Date());
        return table;
    }

    public HttpResult<?> skuCheck(Detail detail) {
        if (detail.getSkuName() != null && detail.getSkuId() == null) {
            return HttpResult.of(HttpResultCodeEnum.SKU_ID_IS_NULL);
        }
        if (goodsSkuDAO.selectByPrimaryKey(detail.getSkuId()) == null) {
            return HttpResult.of(HttpResultCodeEnum.SKU_IS_NOT_IN_SYSTEM);
        }
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }
}
