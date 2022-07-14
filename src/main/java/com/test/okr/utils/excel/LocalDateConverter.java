package com.test.okr.utils.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/07/18
 * @description 日期格式转换
 */
public class LocalDateConverter implements Converter<LocalDate> {

    private static final LocalDate default_date = LocalDate.of(2000, 1, 1);
    @Override
    public Class<LocalDate> supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        final String stringValue = cellData.getStringValue();
        if (StringUtils.isBlank(stringValue) || Objects.equals("0000-00-00", stringValue)) {
            return default_date;
        } else {
            return LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData<>(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }


}
