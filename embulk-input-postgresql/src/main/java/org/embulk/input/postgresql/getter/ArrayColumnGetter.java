package org.embulk.input.postgresql.getter;

import org.embulk.input.jdbc.getter.AbstractColumnGetter;
import org.embulk.input.jdbc.getter.ColumnGetter;
import org.embulk.input.jdbc.getter.LongColumnGetter;
import org.embulk.spi.Column;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.type.Type;
import org.embulk.spi.type.Types;
import org.msgpack.value.Value;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.msgpack.value.ValueFactory.newBoolean;
import static org.msgpack.value.ValueFactory.newFloat;
import static org.msgpack.value.ValueFactory.newInteger;
import static org.msgpack.value.ValueFactory.newString;
import static org.msgpack.value.ValueFactory.newArray;

public class ArrayColumnGetter
        extends AbstractColumnGetter
{
    private List<Value> value = new ArrayList<>();
    private String fromType;

    public ArrayColumnGetter(PageBuilder to, Type toType, String fromType)
    {
        super(to, toType);
        this.fromType = fromType.substring(1, fromType.length());
    }

    @Override
    protected void fetch(ResultSet from, int fromIndex) throws SQLException
    {
        Object[] array = (Object[]) from.getArray(fromIndex).getArray();
        for (Object v : array) {
            switch (this.fromType) {
                // TODO support more type
                case "int2":
                case "int4":
                case "int8":
                    value.add(newInteger((int) v));
                    break;
                case "float":
                    value.add(newFloat((float) v));
                    break;
                case "bool":
                    value.add(newBoolean((boolean) v));
                    break;
                default:
                    value.add(newString(v.toString()));
            }
        }
    }

    @Override
    protected Type getDefaultToType()
    {
        return Types.JSON;
    }

    @Override
    public void jsonColumn(Column column)
    {
        to.setJson(column, newArray(value));
    }

    @Override
    public void stringColumn(Column column)
    {
        to.setString(column, value.toString());
    }
}
