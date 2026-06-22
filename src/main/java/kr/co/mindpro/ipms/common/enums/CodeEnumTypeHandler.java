package kr.co.mindpro.ipms.common.enums;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CodeEnumTypeHandler<E extends Enum<E> & BaseEnum>
        extends BaseTypeHandler<E> {

    private final Class<E> type;

    public CodeEnumTypeHandler(Class<E> type) {
        this.type = type;
    }


    @Override
    public void setNonNullParameter(
            PreparedStatement ps, int i, E parameter, JdbcType jdbcType
    ) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return convert(rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return convert(rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return convert(cs.getString(columnIndex));
    }

    private E convert(String code) {
        if (code == null) return null;

        return Arrays.stream(type.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid enum code: " + code + ", enum=" + type.getSimpleName()
                        ));
    }


}
