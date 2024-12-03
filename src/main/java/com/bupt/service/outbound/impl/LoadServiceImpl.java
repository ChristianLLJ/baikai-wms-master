package com.bupt.service.outbound.impl;

import com.bupt.DTO.HeadAndDetail;
import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.BaseService;
import com.bupt.service.authority.CodeService;
import com.bupt.service.outbound.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LoadServiceImpl extends BaseService<Loading,LoadingDetail,LoadingDAO,LoadingDetailDAO> implements LoadService {
    @Autowired
    private LoadingDAO loadingDAO;
    @Autowired
    private LoadingDetailDAO loadingDetailDAO;
    @Autowired
    private DespatchDAO despatchDAO;
    @Autowired
    private CodeService codeService;

    @Override
    public HttpResult<?> getLoadDetailMsg(List<Despatch> despatches) {
        List<LoadingDetail> loadingDetails=new ArrayList<>();
        despatches.forEach(e->{
            Despatch d=despatchDAO.selectByPrimaryKey(e.getId());
            LoadingDetail l=new LoadingDetail();
            l.setCustomId(d.getCustomerId());
            l.setCustomName(d.getCustomerName());
            l.setReceiverId(d.getReceiverId());
            l.setRecerverName(d.getReceiverName());
            l.setDespatchCode(d.getDespatchId());
            l.setDespatchId(d.getId());
            l.setCreateTime(new Date());
            loadingDetails.add(l);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,loadingDetails);
    }

    @Override
    public HttpResult<?> addLoadingAndDetail(HeadAndDetail<Loading, LoadingDetail> headAndDetail) {
        Loading head = headAndDetail.getHead();
        if (head.getLoadId()==null ){
            head.setLoadId(codeService.code("LOD"));
        }
        loadingDAO.insertSelective(head);
        headAndDetail.getDetails().forEach(e->{
            e.setPid(head.getId());
            loadingDetailDAO.insertSelective(e);
        });
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> startLoad(Loading loading1) {
        Loading loading = new Loading();
        if (loadingDAO.selectByPrimaryKey(loading1.getId()).getStatus()>1) {
            return HttpResult.of(HttpResultCodeEnum.LOADING_ISOVER);
        }
        loading.setId(loading1.getId());
        loading.setStartTime(new Date());
        loading.setStatus((short) 2);//装车中 todo -状态模式
        loadingDAO.updateByPrimaryKeySelective(loading);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,"开始装车");
    }
    @Override
    public HttpResult<?> endLoad(Loading loading1) {
        Loading loading = new Loading();
        loading.setId(loading1.getId());
        if (loadingDAO.selectByPrimaryKey(loading1.getId()).getStatus()>2) {
            return HttpResult.of(HttpResultCodeEnum.LOADING_ISOVER);
        }
        loading.setStatus((short) 3);//已装车 todo -状态模式
        loading.setEndTime(new Date());
        loadingDAO.updateByPrimaryKeySelective(loading);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS,"装车结束");
    }

    @Override
    public List<Loading> getHeads(ScreenDTO<Loading> screenDTO) {
        if (screenDTO.getSearchDTO() != null)
            screenDTO.getSearchDTO().setPage((screenDTO.getSearchDTO().getPage() - 1) * screenDTO.getSearchDTO().getNum());
        List<Loading> loadings = loadingDAO.screen(screenDTO);
        return loadings;
    }

    @Override
    public List<Loading> getHeads(List<Loading> loadingList) {
        List<Loading> loadings=new ArrayList<>();
        loadingList.forEach(e->{
            loadings.add(loadingDAO.selectByPrimaryKey(e.getId()));
        });
        return loadings;
    }

    @Override
    public List<HeadAndDetail> getHeadDetails(List<Loading> loadingList) {
        List<HeadAndDetail> headAndDetailList = new ArrayList<>();
        loadingList.forEach(e -> {
            HeadAndDetail<Loading, LoadingDetail> headAndDetail = new HeadAndDetail<>();
            headAndDetail.setHead(e);
            headAndDetail.setDetails(loadingDetailDAO.selectDetailByPid(e.getId()));
            headAndDetailList.add(headAndDetail);
        });
        return headAndDetailList;
    }
}
