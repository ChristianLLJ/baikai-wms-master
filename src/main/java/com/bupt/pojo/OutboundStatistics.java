package com.bupt.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author 
 * VIEW
 */
@Data
public class OutboundStatistics implements Serializable {
    private Long todayDespatchNums;

    private Long todayWaveNums;

    private Long todayTobNums;

    private Long todayTocNums;

    private Long todayBoxNums;

    private Long todayExpressNums;

    private Long todayLoadingNums;

    private Long desCheckNums;

    private Long waveCheckNums;

    private Long desWaitWaveNums;

    private Long todayDesBeenWaveNums;

    private Long todayDesBeenPickingNums;

    private Long todayDesWaitExpressNums;

    private Long todayDesBeenExpressNums;

    private Long tobNotBegin;

    private Long tocNotBegin;

    private Long waveNotEnd;

    private Long desNotEnd;

    private static final long serialVersionUID = 1L;
}