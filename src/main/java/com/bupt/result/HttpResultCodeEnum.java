package com.bupt.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ZhaoDesong
 * @date 2020/10/30 16:15
 */
@Getter
@AllArgsConstructor
public enum HttpResultCodeEnum {

    /**
     * 通用部分
     */
    SUCCESS(200, "成功"),
    AUTHORITY_FAILED(10001, "越权操作"),
    SYSTEM_ERROR(10002, "系统错误,请检查填写信息是否齐全，是否有误，是否符合要求"),
    SYSTEM_NULL(10003, "查询结果为空"),
    DATA_NULL(10004, "传入数据为空，请检查数据是否填写"),
    LAYERS_NULL(10005, "请至少选择一层重置数据"),
    NOT_FINISHED(10006, "请将出库任务完成后，之后将入库计划完成并结单，再进行重置操作"),
    NOT_PICKTASK(10007, "此订单无需分拣货物，无需发送至分拣机"),


    /**
     * 权限控制相关 20001开始
     */
    LOGIN_SUCCESS(200, "登陆成功"),
    LOGIN_FAIL(2001, "账号或密码错误"),
    LOGIN_FAIL_NOT_USABLE(2002, "该用户被禁用"),
    LOGIN_UNKNOWN_USERNAME(2003, "用户名错误"),
    LOGIN_INCORRECT_PASSWORD(2004, "密码错误"),
    LOGIN_KICKOUT(2005, "重复登陆，用户已被踢出"),


    /*
     * 基础数据 相关 30001开始
     * */
    GOODS_CODE_DUPLICATED(31001, "商品信息重复，请修改"),
    SYS_CODE_CANNOT_MODIFY(31002, "系统代码不可修改"),
    SKU_ID_IS_NULL(31003, "不允许自主填写Sku信息,请重新填写"),
    SKU_IS_NOT_IN_SYSTEM(31004, "系统中没有此sku,请重新填写"),

    /*
     * 入库操作 相关 40001开始
     * */
    CANCEL_FAILED(40001, "单据已创建完成，无法取消"),
    SET_RECEIVED_FAILED(40002, "存在未确认收货的表单信息，无法设置单据已收货"),
    CANCEL_RECEIVE_AFTER_FAILED(40003, "该入库单已在装箱或码盘状态，无法取消收货"),
    CANCEL_RECEIVE_BEFORE_FAILED(40004, "该入库单已未进入收货状态，无法取消收货"),
    GenerateIPA_FAILED(40005, "该入库单未在收货状态，无法生成装箱单"),
    GenerateCOB_IPA_FAILED(40006, "该装箱单对应的入库计划单未在已装箱状态，无法生成组盘单"),
    GenerateCOB_IPL_FAILED(40006, "该入库计划单未在已收货状态，无法生成组盘单"),

    GenerateONP_FAILED(40007, "该入库计划单未在已收货或已装箱或已码盘状态，无法生成上架单"),
    SET_PACKED_FAILED(40008, "存在未确认装箱的表单信息，无法设置单据已装箱完成"),
    SET_STACKED_FAILED(40009, "存在未确认码盘的箱明细，无法设置该码盘已码盘完成"),
    SET_COMBINE_FAILED(40010, "存在未确认码盘完成的码盘，无法设置该码盘单已完成"),
    SET_ONSHELF_FAILED(40011, "存在未确认上架的表细，无法设置该上架单完成"),
    ONSHELF_ADVICE_LOCATION_NULL(40012, "注意！存在因为无法通过策略筛选出任何一个库位满足该策略或库位已全部被占用导致建议库位为空的表细"),
    CLOSE_FAILED(40013, "当前表单的操作还未完成，不能进行结单操作"),
    QUALITY_NOT_AUDIT_FAILED(40014, "当前入库计划单不在已审核状态，无法质检"),
    INBOUND_DETAIL_RECEIVED_FAILED(40015, "当前表细已经收货，请勿重复收货"),
    INBOUND_PLAN_TABLE_RECEIVED_FAILED(40016, "当前计划单未处于已质检状态，不能进行收货"),
    INBOUND_PLAN_TABLE_RECEIVING_FAILED(40017, "当前计划单处于正在收货状态，不能进行整表收货"),
    PACKAGE_GENERATE_FAILED(40018, "选择的目标入库计划单中存在未收货或已经有装箱信息的明细，无法生成装箱任务"),
    COMBINE_GENERATE_FAILED(40019, "选择的目标入库计划单中存在未收货或未装箱或已有码盘信息的明细，无法生成码盘任务"),
    ONSHELF_GENERATE_FAILED(40020, "选择的目标入库计划单中存在未收货或没有装箱和码盘信息或已有上架信息的明细，无法生成码盘任务"),
    INBOUND_PLAN_GENERATE_FAILED(40021, "选择的采购订单中存在已经生成入库计划单的明细，无法生成入库计划单"),
    QUALITY_CLOSE_FAILED(40022, "该质检单不在正在质检或已审核的状态，无法结单"),
    PACKAGE_CLOSE_FAILED(40023, "该装箱单不在正在装箱或已审核的状态，无法结单"),
    PACKING_DETAIL_PACKED_FAILED(40024, "该装箱明细已经装箱，请勿重复装箱"),
    PACKAGE_AUDIT_FAILED(40025, "该装箱单未审核，无法开始装箱"),
    STACK_DETAIL_STACKED_FAILED(40026, "该码盘已经码盘，请勿重复码盘"),
    STACK_PACKAGE_STACKED_FAILED(40027, "该码箱已经码盘，请勿重复码盘"),
    STACK_STACKED_FAILED(40028, "该码盘单未审核或者未处于正在码盘状态，无法结单"),
    STACK_AUDIT_FAILED(40029, "该码盘单未审核，无法开始码盘"),
    ONSHELF_ADVICE_USE_FAILED(40030, "该上架建议单已经生成过上架任务，无法重复生成"),
    ONSHELF_CLOSE_FAILED(40031, "该上架任务未处于已审核状态或正在上架状态，无法结单"),
    ONSHELF_DETAIL_ONSHELFED_FAILED(40032, "该上架任务明细已经上架，请勿重复上架"),
    ONSHELF_AUDIT_FAILED(40033, "该上架任务未审核或者已经上架完成，不能开始上架"),
    ONSHELF_DETAIL_NOT_IN_UNSEND(40034,"存在不是未发送状态的上架任务，请检查"),

    /*
     * 库内操作 相关 50001开始
     * */
    SELECT_FAILED(50001, "没有足够的可用数量"),
    CHANGE_SAVE_SUCCESS(50002, "库存改变单保存成功"),
    CHANGE_SUBMIT_SUCCESS(50003, "库存改变单提交成功"),
    CHANGE_HAVE_SUBMIT(50004, "库存改变单已提交，不能修改"),
    FREEZE_SAVE_SUCCESS(50005, "库存冻结单保存成功"),
    FREEZE_SUBMIT_SUCCESS(50006, "库存冻结单提交成功"),
    FREEZE_HAVE_SUBMIT(50007, "库存冻结单已提交，不能修改"),
    INVENTORY_SAVE_SUCCESS(50008, "库存盘点单保存成功"),
    INVENTORY_SUBMIT_SUCCESS(50009, "库存盘点单提交成功"),
    INVENTORY_HAVE_SUBMIT(50010, "库存盘点单已提交，不能修改"),
    MOVE_SAVE_SUCCESS(50011, "库存移动单保存成功"),
    MOVE_SUBMIT_SUCCESS(50012, "库存移动单提交成功"),
    MOVE_HAVE_SUBMIT(50013, "库存移动单已提交，不能修改"),
    FREEZE_FAILED(50014, "没有此筛选条件的货品，冻结失败"),
    FREEZE_HAVE_FREEZE(50015, "没有可用数量可以进行冻结"),
    FREEZE_PART_SUCCESS(50016, "部分货品冻结成功"),
    INVENTORY_NULL_FAILED(50017, "没有此筛选条件的货品，盘点失败"),
    FREEZE_NOT_ENOUGH(50018, "没有足够的可用数量用来冻结"),
    NOT_AUDIT_TWICE(50019, "已经审核完毕，不能再次审核"),
    AUDIT_RELEASE(50020, "表单未审核，不能释放"),
    NOT_RELEASE_TWICE(50021, "已经释放完毕，不能再次释放"),
    UNAUDIT_NOT_RELEASE(50022, "表单审核不通过，无法释放"),
    INVENTORY_NULL(50023, "暂时没有未库存盘点的任务"),
    MOVE_NULL(50024, "暂时没有未库存移动的任务"),
    CHANGE_NULL(50025, "暂时没有未库存改变的任务"),
    FREEZE_NULL(50026, "暂时没有未库存冻结的任务"),
    INWAREHOUSE_DETAIL_NULL(50027, "表细未完全提交，不能提交任务"),


    /**
     * 出库操作 相关 成功操作 60001开始，错误操作 61001 开始
     */
    DESPATCH_SAVE_SUCCESS(200, "发运订单保存成功"),
    DESPATCH_SUBMIT_SUCCESS(200, "发运订单提交成功"),
    WAVERULE_UPDATE_SUCCESS(200, "波次规则更新成功"),
    DESPATCH_HAVEBEEN_PRE_DISTRIBUTION(60004, "已经预配成功，请勿重复操作"),
    DESPATCH_PART_PRE_DISTRIBUTION_SUCCESS(60008, "部分预配失败"),
    DESPATCH_PRE_DISTRIBUTION_FAIL(60005, "预配失败"),
    RE_DESPATCH_PRE_DISTRIBUTION_FAIL(60006, "本订单曾预配失败，是否进行二次预配"),
    NOT_RE_DESPATCH_PRE_DISTRIBUTIO(60007, "本订单不是预配失败单，不能进行二次预配"),
    DESPATCH_DISTRIBUTION_SUCCESS(200, "分配成功"),
    DESPATCH_PART_DISTRIBUTION_SUCCESS(60009, "此库区数量不足，需要根据补货单补货"),
    DESPATCH_DISTRIBUTION_FAIL(60003, "仓库数量不足，分配失败"),
    DESPATCH_HAVEBEEN_DISTRIBUTION(60002, "已经分配成功，请勿重复操作"),
    RE_DESPATCH_DISTRIBUTION_FAIL(60001, "本订单曾分配失败，是否进行二次分配"),
    NOT_RE_DESPATCH_DISTRIBUTIO(60010, "本订单不是分配失败单，不能进行二次分配"),
    NOT_AREACODE_DESPATCH(60011, "和第一次分配输入库区不一致"),
    DESPATCH_DISTRIBUTION_NOT_AREA(60012, "库区数量不足，无法进行分配，请根据补货单调货"),
    DESPATCH_DISTRIBUTION_NOT_WAREHOUSE(60013, "仓库数量不足，无法进行分配，请补货"),
    DESPATCH_DISTRIBUTION_AREA_FAIL(60014, "库区数量不足，分配失败"),
    DESPATCH_HAVEBEEN_SUBMITED(61001, "发运订单已提交，不能修改"),
    DESPATCH_HAVENOTBEEN_RELASE(61002, "发运订单中包含未释放库存商品"),
    DESPATCH_CANTBEEN_WAVE(61004, "未创建成功或已生成波次订单不能合成波次"),
    DESPATCH_CANTBEEN_FINDED(61005, "发运订单已全部合成波次，请等待"),
    WAVERULE_BENOT_MODIFIED(61003, "波次规则不允许修改"),
    WAVERULE_HAVEBEEN_RUNNING(61006, "波次规则已经运行，请勿重复提交"),
    WAVERULE_NOTBE_RUNNING(61007, "波次规则并未运行，无需终止"),
    TIMEDWAVERULE_HAVEBEEN_OVERTIME(61007, "定时波次规则，时间列表所有时间点已过时，启动失败"),
    WAVE_CANTBEEN_DELETED(61008, "波次计划单已经分配不能删除"),
    WAVE_CANTBEEN_UPDATE(61008, "波次计划单已经预配或分配不能修改"),
    RECEIVER_CANTBEEN_DIFFERENT(61009, "发运订单收货人不同，不能合并生成配送单"),
    NOTBOXED_CANTBEEN_DISTRIBUTED(61010, "未装箱订单或已生成配送单，不能进行生成配送单操作"),
    BOXEDNUM_WRONG(61011, "装箱数量有误，请检查后重新提交"),
    LOADINGDETAIL_WRONG(61012, "装车单中包含已装车订单，请核对后重试"),
    LOADING_ISOVER(61013, "装车已完成，请勿重复装车"),
    DISTRIBUTION_HAVE_BEEN_LOADED(61014, "配送单未创建成功或者已装车"),
    NOT_NEED_BE_BOXED(61015, "提示，存在无需装箱波次，所有订单状态已变更为'已装箱'"),
    PREPLATFORM_HAVEBEEN_ADDED(61016, "该配送单已生成预约单，请勿重复配送"),
    CANNOT_FIND_EMPTY_LOCATION(61017, "目标库区空库位不足，分配库位失败订单请手动选择库位上架"),
    NOT_AREA_SHORTAGE_WAVE(61018, "目标库区空库位不足，分配库位失败订单请手动选择库位上架"),
    REPLENISH_INVENTORY_SHORETAGE(61019, "本库区库存不足，请更换库区"),
    NOT_FIND_SHORETAGE(61020, "未查询到关联缺货单，请检查后重试"),
    TOB_CONVERT_ERROR(61021, "TOB客户订单转化发运订单失败，请检查订单状态和订单表细数量是否正确"),
    NOT_EXAMINE_ERROR(61022, "未审核或审核不通过订单不能进行此操作，请审核后重复操作"),
    NOT_CREARTED_ERROR(61023, "未审核订单或非“已创建”状态下不能分拣操作"),
    CANNOT_RUN_ERROR(61023, "操作有误，请检查订单状态信息和设备信息"),
    FAIL_PICK_DUE_PREFAIL(61024, "分拣失败存在部分发运订单库存不足，请补货后重试"),
    FAIL_CHECK_ALL(61024, "部分订单审核失败，因为其属于非待审核订单"),
    DELIVER_CONVERT_PART_FAIL(61025, "部分订单转发运计划失败，因为之前已转为发运计划"),
    END_TOB_PART_FAIL(61026, "部分客户订单操作失败，因为表细中还有剩余未分配Sku,或者已经结单完成"),
    NOT_BEGIN_MERGING(61027,"该订单还未进行合单，请选择合单位置后开始合单"),
    NOT_SAME_POSITION(61028,"已选择合单位置不一致，请重置合单位置"),
    HAVE_BEEN_MERGEDG(61029,"该订单已经开始合单，如若修改合单位置，请进行重置位置操作"),
    NOT_RIGHT_STATUS(61030,"部分订单状态不为“已发送WCS分拣任务”，请检查数据是否正确"),
    CAN_NOT_UPD(61031,"修改失败，订单只有在已创建或未审核或审核未通过状态下，才可修改"),
    WRONG_PICK_NUM(61032,"分拣数量有误,或已分拣"),
    WRONG_INV_CODE(61033,"此箱码与实际业务不对应，搜索不到拣货信息，请核对后重试"),
    HAVE_BEEN_PICK_ENOUGH(61034,"已拣完所需数量，请换下一箱"),
    HAVE_BEEN_BOXED(61035,"存在已装箱任务，请检查后重试"),
    HAVE_BEEN_SENDED(61036,"分拣任务已推送小车"),
    HAVE_BEEN_EMPTY(61037,"该箱已为空，请直接出库，无需返库"),

    /**
     * 库内操作 相关 成功操作 70001开始，错误操作 71001 开始
     */
    INVENTORY_BALANCE_SHORTAGE(70001, "库存不足"),
    NOT_HAVE_BEEN_ASSIGNED_WAVE(70002, "请确认发运订单是否分配波次"),
    NOT_HAVE_BEEN_PRE_ASSIGNED(70002, "请确认发运订单是否完成预配操作"),
    EXPICKING_FAILED(70003, "请确认发运订单是否为已分配订单"),
    EXPICKING_NOTHAVE_BEEN_ASSIGNED(70004, "该订单并未生成装箱计划单"),


    /**
     * baokai对接
     */
    WAVE_STATUS_WRONG_PICKING(0, "该订单非待分拣订单，请核对后重试"),
    WAVE_STATUS_WRONG_PICKED(80002, "该订单非分拣中订单，请核对后重试"),
    DESPATCH_SKU_WRONG(80003, "该SKU并不存在于该发运订单中，请核对后重试"),
    DESPATCH_CNT_OVER(80004, "该SKU分拣后数量超过预定数量，请核对后重试"),
    NOT_PICKED_ENOUGH(0, "还有人工拣货或者整箱出库任务未执行完毕"),


    /**
     * TODO 待进一步完善
     */


    ;
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误信息
     */
    private String msg;
}
