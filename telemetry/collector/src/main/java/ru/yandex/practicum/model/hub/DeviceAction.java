package ru.yandex.practicum.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAction {
    private String sensorId;
    private DeviceActionType type;
    private Integer value;
}
