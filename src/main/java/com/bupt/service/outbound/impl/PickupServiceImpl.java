package com.bupt.service.outbound.impl;

import com.bupt.DTO.ScreenDTO;
import com.bupt.DTO.SearchDTO;
import com.bupt.mapper.*;
import com.bupt.pojo.*;
import com.bupt.result.HttpResult;
import com.bupt.result.HttpResultCodeEnum;
import com.bupt.service.authority.CodeService;
import com.bupt.service.outbound.PickupService;
import com.bupt.service.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class PickupServiceImpl implements PickupService {
    @Autowired
    private PickTaskDAO pickTaskDAO;
    @Autowired
    private PickTaskDetailDAO pickTaskDetailDAO;
    @Autowired
    private CodeService codeService;
    @Autowired
    private InventoryBalanceDAO inventoryBalanceDAO;
    @Autowired
    private WaveDAO waveDAO;
    @Autowired
    PickTaskDetailBlackDAO pickTaskDetailBlackDAO;
    @Autowired
    private UtilService utilService;

    @Override
    public HttpResult<?> searchPicktaskByWaveId(Wave wave) {
        PickTask pickTask = pickTaskDAO.selectDetailByPid(wave.getId()).get(0);
        pickTaskDetailDAO.selectDetailByPid(pickTask.getId());
        return null;
    }

    @Override
    public HttpResult<?> deletePickTask(PickTask pickTask) {
        return null;
    }

    @Override
    public HttpResult<?> updateTaskStatus(PickTask pickTask, Short taskStatus) {
        PickTask rePickTask = new PickTask();
        rePickTask.setId(pickTask.getId());
        rePickTask.setStatus(taskStatus);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public HttpResult<?> updatePickStatus(PickTask pickTask, Short pickStatus) {
        PickTask rePickTask = new PickTask();
        rePickTask.setId(pickTask.getId());
        rePickTask.setStatus(pickStatus);
        return HttpResult.of(HttpResultCodeEnum.SUCCESS);
    }

    @Override
    public List<PickTask> screenPickTask(ScreenDTO<PickTask> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        return pickTaskDAO.screen(screenDTO);
    }

    @Override
    public Integer screenPickTaskNum(ScreenDTO<PickTask> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        return pickTaskDAO.numScreen(screenDTO);
    }

    @Override
    public HttpResult<?> updatePickTaskDetail(PickTaskDetail pickTaskDetail) {
        return HttpResult.of(HttpResultCodeEnum.SUCCESS, pickTaskDetailDAO.updateByPrimaryKeySelective(pickTaskDetail));
    }

    @Override
    public List<PickTaskDetail> screenPickTaskDetail(ScreenDTO<PickTaskDetail> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        } else {
            screenDTO.setScreen("id");
        }
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        return pickTaskDetailDAO.screen(screenDTO);
    }

    @Override
    public Integer screenPickTaskDetailNum(ScreenDTO<PickTaskDetail> screenDTO) {
        if (screenDTO.getScreen() != null && screenDTO.getScreen() != "") {
            screenDTO.setScreen(utilService.xX2x_x(screenDTO.getScreen()));
        } else {
            screenDTO.setScreen("id");
        }
        return pickTaskDetailDAO.numScreen(screenDTO);
    }

    @Override
    public List<PickTaskDetailBlack> screenPickTaskDetailBlack(ScreenDTO<PickTaskDetailBlack> screenDTO) {
        SearchDTO searchDTO = screenDTO.getSearchDTO();
        searchDTO.setPage((searchDTO.getPage() - 1) * searchDTO.getNum());
        screenDTO.setSearchDTO(searchDTO);
        return pickTaskDetailBlackDAO.screen(screenDTO);
    }

    @Override
    public Integer updatePickUpDetail(PickTaskDetail pickTaskDetail) {
        return pickTaskDetailDAO.updateByPrimaryKeySelective(pickTaskDetail);
    }
}
