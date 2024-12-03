package com.bupt.DTO;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class HeadAndDetail<H,D> {
    @Valid
    H head;
    @Valid
    List<D> details;
}
